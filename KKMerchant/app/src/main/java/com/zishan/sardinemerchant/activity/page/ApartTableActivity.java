package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableApartEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableApartPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.table_detail.TableApartView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.adapter.page.ApartTableAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 拆分桌台
 * Created by wislie on 2017/12/4.
 */

public class ApartTableActivity extends BActivity<TableApartView, TableApartPresenter> implements TableApartView {

    @BindView(R.id.apart_table_springview)
    SpringView mSpringView;
    @BindView(R.id.apart_table_recycler_view)
    RecyclerView mRecycler;

    private String mSeatName;
    private long table_id = 0;

    private ApartTableAdapter mAdapter;
    private List<TableApartEntity> mDataList = new ArrayList<>();

    @Override
    protected TableApartPresenter createPresenter() {
        return new TableApartPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apart_table;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            mSeatName = intent.getStringExtra("seat_name");
            table_id = intent.getLongExtra("table_id", table_id);
            setActionbarTitle("拆台" + mSeatName);
        }
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }


    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new ApartTableAdapter(R.layout.item_apart_table, mDataList);
        mRecycler.setAdapter(mAdapter);

        showProgressDialog();
        loadData();
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    private void loadData() {
        mPresenter.getApartTableList(table_id);
    }

    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }

    @OnClick({R.id.confirm_apart_table})
    public void onClick(View view) {
        switch (view.getId()) {
            //拆分桌台
            case R.id.confirm_apart_table:
                List<TableApartEntity> dataList = mAdapter.getData();
                if (dataList.size() <= 1) {
                    ToastUtil.show("拆台数量需要大于1");
                    return;
                }

                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认拆分该桌台拼桌台?", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.apartTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                getTableIds());

                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

    private JSONArray getTableIds() {
        JSONArray tableIdsArr = new JSONArray();
        List<TableApartEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            TableApartEntity data = dataList.get(i);
            tableIdsArr.put(data.getId());
        }
        return tableIdsArr;
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
    public void getApartTableListSuccess(List<TableApartEntity> dataList) {
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void getApartTableListFailed() {
        finishRefreshAndLoad();
    }

    @Override
    public void showNoData() {
        finishRefreshAndLoad();
    }

    @Override
    public void apartTableSuccess(Object data) {
        ActivityManager.getInstance().finishAllActivityExcept(MainPageActivity.class, RepastManageActivity.class);
    }

    @Override
    public void apartTableFailed() {
    }


}
