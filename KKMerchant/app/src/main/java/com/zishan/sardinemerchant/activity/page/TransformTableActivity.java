package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableTransformPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableTransformView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableTransformAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;


/**
 * Created by yang on 2017/12/5.
 * <p>
 * 转台
 */

public class TransformTableActivity extends BActivity<TableTransformView, TableTransformPresenter>
        implements TableTransformView {
    @BindView(R.id.table_name)
    TextView tableNameText;
    @BindView(R.id.transform_recycler_view)
    RecyclerView mRecycler;

    private List<StoreSeatEntity> mDataList = new ArrayList<>();

    private TableTransformAdapter mAdapter;

    private String mSeatName;
    private long table_id = 0;
    private long repast_id = 0;

    @Override
    protected TableTransformPresenter createPresenter() {
        return new TableTransformPresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        Intent intent = getIntent();
        if (intent != null) {
            mSeatName = intent.getStringExtra("seat_name");
            table_id = intent.getLongExtra("table_id", table_id);
            repast_id = intent.getLongExtra("repast_id", repast_id);
        }
        setActionbarTitle(getString(R.string.transform_table));
        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transform_table;
    }

    @Override
    protected void initContentView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new TableTransformAdapter(this, mDataList);
        mRecycler.setAdapter(mAdapter);
        tableNameText.setText(mSeatName);
        mAdapter.setCheckListener(new TableTransformAdapter.OnCheckListener() {
            @Override
            public void check(int position) {
                StoreSeatEntity data = mDataList.get(position);
                if (data.getIsChecked() == 1) return;
                //设置选中桌台
                setTableChecked(position);
            }
        });
        loadData();
    }

    private void setTableChecked(int position) {
        for (int i = 0; i < mDataList.size(); i++) {
            StoreSeatEntity data = mDataList.get(i);
            if (data.getIsChecked() == 1 && i != position) {
                data.setIsChecked(0);
            }
        }
        mDataList.get(position).setIsChecked(1);
        mAdapter.notifyDataSetChanged();
    }

    private StoreSeatEntity getCheckedTable() {
        for (int i = 0; i < mDataList.size(); i++) {
            StoreSeatEntity data = mDataList.get(i);
            if (data.getIsChecked() == 1) {
                return data;
            }
        }
        return null;
    }

    private void loadData() {
        mPresenter.getTableList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    @Override
    public void getTableListSuccess(ArrayList<StoreSeatEntity> dataList) {
        mDataList.clear();
        //大厅
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.HALL && data.getState() == Constant.EMPTY_ORDER) {
                mDataList.add(data);
            }
        }
        //卡座
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.CARD && data.getState() == Constant.EMPTY_ORDER) {
                mDataList.add(data);
            }
        }
        //包厢
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.BOX && data.getState() == Constant.EMPTY_ORDER) {
                mDataList.add(data);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTableListFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void transformTableSuccess(Object data) {
        finish();
    }

    @Override
    public void transformTableFailed() {

    }

    @OnClick({R.id.table_transform_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table_transform_confirm:
                StoreSeatEntity data = getCheckedTable();
                if (data == null) {
                    ToastUtil.show("需选中一张桌台才能转台");
                    return;
                }

                showTransformTableDialog(data);
                break;
        }

    }

    private void showTransformTableDialog(final StoreSeatEntity data) {
        String title = "确认将该桌台转换至" + data.getSeatName();
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance(title, null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                mPresenter.tansformTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        table_id, data.getId(), repast_id);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
