package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.CouponEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.presenter.base.page.ticket.CouponPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.ticket.CouponListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TicketChooseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 卡券配置
 * Created by wislie on 2018/1/23.
 */

public class TicketConfigActivity extends BActivity<CouponListView, CouponPresenter>
        implements CouponListView {

    @BindView(R.id.ticket_config_springview)
    SpringView mSpringView;
    @BindView(R.id.ticket_config_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.empty_ticket_config_layout)
    LinearLayout mEmptyLayout;

    private List<CouponEntity> mDataList = new ArrayList<>();
    private TicketChooseAdapter mAdapter;

    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;
    //当前页
    private int mCurrentPage;

    @Override
    protected CouponPresenter createPresenter() {
        return new CouponPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_config;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.ticket_config_manage));
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        setActionBarHomeIcon(R.mipmap.back_black_icon);
        ImageView addTicketIcon = setActionBarMenuIcon(R.mipmap.add_ticket_icon);
//        addTicketIcon.setVisibility(View.GONE);
        addTicketIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketConfigActivity.this, TicketConfigWebActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initContentView() {
        initSpringView();

        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new TicketChooseAdapter(R.layout.item_choose_ticket, mDataList, true);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestCouponList();
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        CouponEntity data = mAdapter.getItem(position);
//                        if(data == null) return;
//                        Intent intent = new Intent(TicketConfigActivity.this, TicketAddActivity.class);
//                        intent.putExtra("coupon", data);
//                        startActivity(intent);
                    }
                });
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
                    initRequestCouponList();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });

        showProgressDialog();
        initRequestCouponList();
    }

    /*
     * 初始化请求
     */
    private void initRequestCouponList() {
        mCurrentPage = 0;
        mPresenter.getCouponConfigList(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                PAGE_SIZE, mCurrentPage, true);
    }

    /**
     * 非第一次发起请求
     */
    private void requestCouponList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getCouponConfigList(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                    PAGE_SIZE, mCurrentPage, false);
        } else {
            finishRefreshAndLoad();
        }

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
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
        mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void newTicketList(List<CouponEntity> dataList) {
        mAdapter.setNewData(dataList);
        mRecycler.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void addTicketList(List<CouponEntity> dataList) {
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
}
