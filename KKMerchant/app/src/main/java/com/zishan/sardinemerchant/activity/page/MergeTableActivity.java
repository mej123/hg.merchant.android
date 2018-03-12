package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableMergePresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableMergeView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableMergeAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2017/12/5.
 * <p>
 * 合并
 */

public class MergeTableActivity extends BActivity<TableMergeView, TableMergePresenter>
        implements TableMergeView {
    @BindView(R.id.combine_recycle)
    RecyclerView mRecycler;
    private List<StoreSeatEntity> mDataList = new ArrayList<>();
    private TableMergeAdapter mAdapter;

    private String mSeatName;
    private long table_id = 0;

    @Override
    protected TableMergePresenter createPresenter() {
        return new TableMergePresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        Intent intent = getIntent();
        if (intent != null) {
            mSeatName = intent.getStringExtra("seat_name");
            table_id = intent.getLongExtra("table_id", table_id);
            setActionbarTitle(mSeatName + "合拼");
        }

        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_merge;
    }

    @Override
    protected void initContentView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new TableMergeAdapter(this, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setCheckListener(new TableMergeAdapter.OnCheckListener() {
            @Override
            public void check(int position) {
                //可被选中
                if (isCheckable(position)) {
                    StoreSeatEntity data = mDataList.get(position);
                    if (data.getIsChecked() == 0) {
                        data.setIsChecked(1);
                    } else {
                        data.setIsChecked(0);
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        });

        loadData();
    }

    private boolean isCheckable(int position) {
        int checkedNum = 0;
        boolean isCurChecked = false;
        for (int i = 0; i < mDataList.size(); i++) {
            StoreSeatEntity data = mDataList.get(i);
            if (data.getIsChecked() == 1) {
                checkedNum++;
                if (position == i) {
                    isCurChecked = true;
                }
            }
        }
        if (checkedNum < 2) return true;
        if (checkedNum == 2 && isCurChecked) return true;
        return false;
    }

    private void loadData() {
        mPresenter.getTableList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.table_merge_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table_merge_confirm:
                JSONArray table_ids = getTableIds();
                if (table_ids.length() <= 1) {
                    ToastUtil.show("至少选择一张桌台才能合拼");
                    return;
                }
                showMergeTableDialog(table_ids);
                break;
        }

    }

    private JSONArray getTableIds() {
        JSONArray tableIdsArr = new JSONArray();
        tableIdsArr.put(table_id);
        for (int i = 0; i < mDataList.size(); i++) {
            StoreSeatEntity data = mDataList.get(i);
            if (data.getIsChecked() == 1) {
                tableIdsArr.put(data.getId());
            }
        }
        return tableIdsArr;
    }

    //选中的桌台名称
    public ArrayList<String> getSelectedTableNames() {
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add(mSeatName);
        for (int i = 0; i < mDataList.size(); i++) {
            StoreSeatEntity data = mDataList.get(i);
            if (data.getIsChecked() == 1) {
                tableNames.add(data.getSeatName());
            }
        }
        return tableNames;
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
    public void mergeTableSuccess(Object data) {
       finish();
    }

    @Override
    public void mergeTableFailed() {

    }

    private void showMergeTableDialog(final JSONArray table_ids) {
        StringBuilder titleBuilder = new StringBuilder(mSeatName);
        ArrayList<String> tableNames = getSelectedTableNames();
        titleBuilder.append("与");
        for (int i = 1; i < tableNames.size(); i++) {
            titleBuilder.append(tableNames.get(i)).append("、");
        }
        titleBuilder.delete(titleBuilder.toString().length() - 1, titleBuilder.toString().length());
        titleBuilder.append("桌台进行合拼后, 合拼后的桌台并为一桌,确认继续此次操作?");
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance(titleBuilder.toString(), tableNames);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                mPresenter.mergeTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), table_ids);
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
