package com.zishan.sardinemerchant.activity.personal.staffs;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.OperatingRecordEntity;
import com.example.wislie.rxjava.presenter.personal.staff.OperatingRecordPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.personal.staff.OperatingRecordView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.NewOperatingRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新 操作记录
 */

public class NewOperatingRecordActivity extends BActivity<OperatingRecordView, OperatingRecordPresenter>
        implements OperatingRecordView {

    @BindView(R.id.operating_record_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.operating_record_empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.operating_record_spring_view)
    SpringView mSpringView;
    private NewOperatingRecordAdapter mAdapter;

    private List<OperatingRecordEntity> mDataList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_operating_record;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.operating_record));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected OperatingRecordPresenter createPresenter() {
        return new OperatingRecordPresenter(this, this);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected void initContentView() {

        initSpringView();
        mAdapter = new NewOperatingRecordAdapter(R.layout.item_new_operating_record, mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewOperatingRecordActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        showLoadingDialog();
        mPresenter.getOperatingRecord(UserConfig.getInstance(this).getMerchantId());
    }

    private void initSpringView() {

        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad(); //停止加载
                } else {
                    //刷新加载数据
                    mPresenter.getOperatingRecord(UserConfig.getInstance(NewOperatingRecordActivity.this).getMerchantId());
                }
            }

            @Override
            public void onLoadmore() {

            }
        });


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
    public void getOperatingRecordComplete(List<OperatingRecordEntity> dataList) {
        if (dataList == null) return;
        if (dataList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mAdapter.setNewData(dataList);
            mAdapter.notifyDataSetChanged();
            if (dataList.size()>6){
                mAdapter.addFooterView(showFooterNoMoreData());
            }
        }
        finishRefreshAndLoad();
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

    @Override
    public void getOperatingRecordFailed() {

    }

}
