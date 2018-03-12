package com.zishan.sardinemerchant.fragment.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreOrderPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.personal.StoreOrderView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.personal.order.StoreOrderDetailActivity;
import com.zishan.sardinemerchant.adapter.personal.StoreOrderAdapter;
import com.zishan.sardinemerchant.entity.SelectDateData;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 店铺订单
 * Created by wislie on 2017/11/7.
 */

public class StoreOrderFragment extends BFragment<StoreOrderView, StoreOrderPresenter>
        implements StoreOrderView {

    @BindView(R.id.store_order_springview)
    SpringView mSpringView;
    @BindView(R.id.store_order_recycler_view)
    RecyclerView mRecycler;

    @BindView(R.id.empty_recipient_layout)
    LinearLayout mEmptyLayout;

    private StoreOrderAdapter mAdapter;
    private List<StoreOrderEntity> mDataList = new ArrayList<>();
    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;
    //订单类型
    private int[] order_type = null;
    //标签类型
    private int mTagType;

    //开始时间
    private Long start_time = null;
    //结束时间
    private Long end_time = null;

    public static StoreOrderFragment newInstance(int tag_type) {
        StoreOrderFragment fg = new StoreOrderFragment();
        Bundle data = new Bundle();
        data.putInt("tag_type", tag_type);
        fg.setArguments(data);
        return fg;
    }

    public static StoreOrderFragment newInstance(int[] order_type, int tag_type) {
        StoreOrderFragment fg = new StoreOrderFragment();
        Bundle data = new Bundle();
        data.putInt("tag_type", tag_type);
        data.putIntArray("order_type", order_type);
        fg.setArguments(data);
        return fg;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            order_type = data.getIntArray("order_type");
            mTagType = data.getInt("tag_type");
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_order;
    }

    @Override
    protected void initBizView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));


        mAdapter = new StoreOrderAdapter(R.layout.item_store_order, mDataList);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestStoreOrderList(start_time, end_time);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                StoreOrderEntity orderEntity = mAdapter.getItem(position);
                if (orderEntity == null) return;
                Intent intent = new Intent(getActivity(), StoreOrderDetailActivity.class);
                String order_id = orderEntity.getId();
                intent.putExtra("order_id", order_id);
                startActivity(intent);
            }
        });
    }


    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(getActivity()));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    initRequestStoreOrderList(start_time, end_time);
                    finishRefreshAndLoad();
                }
            }

            @Override
            public void onLoadmore() {
            }
        });
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
    protected StoreOrderPresenter createPresenter() {
        return new StoreOrderPresenter(getActivity(), this);
    }

    @Override
    protected void loadData() {
        showProgressDialog();
        initRequestStoreOrderList(start_time, end_time);
    }

    /*
   * 初始化请求
   */
    private void initRequestStoreOrderList(Long start_time, Long end_time) {
        mCurrentPage = 0;
        mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                start_time, end_time, null, order_type, mCurrentPage, PAGE_SIZE, true);
    }

    /**
     * 非第一次发起请求
     */
    private void requestStoreOrderList(Long start_time, Long end_time) {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                    start_time, end_time, null, order_type, mCurrentPage, PAGE_SIZE, false);
        } else {
            finishRefreshAndLoad();
        }

    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            if (baseEvent.getTagInt() == mTagType) {
                SelectDateData data = (SelectDateData) baseEvent.getData();
                if (data == null) return;
                start_time = data.getStart_time();
                end_time = data.getEnd_time();

                initRequestStoreOrderList(start_time, end_time);
            }
        }
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
    public void newStoreOrderList(List<StoreOrderEntity> dataList) {
        mAdapter.setNewData(dataList);
        mRecycler.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void addStoreOrderList(List<StoreOrderEntity> dataList) {
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
       /* if (mOrderTotalCountText != null)
            mOrderTotalCountText.setText("共 sBigeFontsBlue" + totalElements + "eBig 笔订单");*/
    }
}
