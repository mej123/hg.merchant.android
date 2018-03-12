package com.zishan.sardinemerchant.activity.personal.accounts;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.BalanceRecordDetailsEntity;
import com.example.wislie.rxjava.presenter.personal.BalanceRecordListPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.personal.BalanceRecordListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.BalanceDetailListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 提现明细列表展示
 */

public class BalanceRecordActivity extends BActivity<BalanceRecordListView,
        BalanceRecordListPresenter> implements BalanceRecordListView {
    @BindView(R.id.recycle_withdraw_detail_view)
    RecyclerView mRecycler;
    @BindView(R.id.withdrawList_spring_view)
    SpringView mWithdrawListSpringView;
    @BindView(R.id.withdraw_list_empty_layout)
    LinearLayout mWithdrawListEmptyLayout;
    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 20;
    //总页数
    private int mPageCount;
    private List<BalanceRecordDetailsEntity> mDataList = new ArrayList<>();
    private BalanceDetailListAdapter mAdapter;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.balance_detail));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BalanceRecordListPresenter createPresenter() {
        return new BalanceRecordListPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_record;
    }

    @Override
    protected void initContentView() {

        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAdapter = new BalanceDetailListAdapter(R.layout.item_balance_detail_list, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();//停止加载
                } else {
                    //非第一次加载
                    loadDataWithdrawRecord();
                }
            }
        });

        showProgressDialog();

        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
//            setLayoutVisible();
            mRecycler.setVisibility(View.GONE);
            mWithdrawListEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            loadData();
            finishRefreshAndLoad();
        }

    }

    private void initSpringView() {
        mWithdrawListSpringView.setType(SpringView.Type.FOLLOW);
        mWithdrawListSpringView.setHeader(new SpringViewHeader(this));
        mWithdrawListSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad(); //停止加载
                } else {
                    //第一次加载
                    loadData();
                    finishRefreshAndLoad();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    private void loadDataWithdrawRecord() {

        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
//            mWithdrawListSpringView.onFinishFreshAndLoad();
            mPresenter.getAccountBalanceRecord(UserConfig.getInstance(ClientApplication.getApp()).
                    getMerchantId(), mCurrentPage, PAGE_SIZE, null, null, null, false);
        } else {
            finishRefreshAndLoad();
        }
    }

    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWithdrawListSpringView != null)
                    mWithdrawListSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }


    private void loadData() {
        mCurrentPage = 0;
        mPresenter.getAccountBalanceRecord(UserConfig.getInstance(ClientApplication.getApp()).
                getMerchantId(), mCurrentPage, PAGE_SIZE, null, null, null, true);

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
    public void loadFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mRecycler.setVisibility(View.GONE);
        mWithdrawListEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void newBalanceDetailsList(List<BalanceRecordDetailsEntity> dataList) {
        int size = dataList.size();
        if (dataList == null) return;
        if (dataList.size() > 0) {
            mRecycler.setVisibility(View.VISIBLE);
            mWithdrawListEmptyLayout.setVisibility(View.GONE);
        }
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void addBalanceDetailsList(List<BalanceRecordDetailsEntity> dataList) {
        mAdapter.notifyDataChangedAfterLoadMore(dataList, true);
    }

    @Override
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(showFooterNoMoreData());
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
