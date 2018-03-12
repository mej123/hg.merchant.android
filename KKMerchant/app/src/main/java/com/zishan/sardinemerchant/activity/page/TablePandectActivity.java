package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.presenter.base.page.main.TablePandectPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.TablePandectView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TablePandectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.RecyclerSpace;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * Created by yang on 2017/9/28.
 * <p>
 * 桌台总览
 */

public class TablePandectActivity extends BActivity<TablePandectView, TablePandectPresenter>
        implements TablePandectView {

    @BindView(R.id.table_pandect_springview)
    SpringView mSpringView;
    @BindView(R.id.hall_recycler_view)
    MaxRecyclerView mHallRecycler;
    @BindView(R.id.card_recycler_view)
    MaxRecyclerView mCardRecycler;
    @BindView(R.id.box_recycler_view)
    MaxRecyclerView mBoxRecycler;

    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.empty_view)
    View mEmptyView;

    private TablePandectAdapter mHallAdapter;
    private TablePandectAdapter mCardAdapter;
    private TablePandectAdapter mBoxAdapter;
    private List<StoreSeatEntity> mHallListData = new ArrayList();
    private List<StoreSeatEntity> mCardListData = new ArrayList();
    private List<StoreSeatEntity> mBoxListData = new ArrayList();

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.table_pandect));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_pandect;
    }

    @Override
    protected void initContentView() {
        initSpringView();
        //大厅
        mHallRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mHallRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(this, 7), DensityUtil.dip2px(this, 10),
                DensityUtil.dip2px(this, 7), 0));
        mHallRecycler.addItemDecoration(new RecyclerSpace(1, getResources().getDrawable(
                R.drawable.custom_classify_divder_shape)));
        mHallAdapter = new TablePandectAdapter(R.layout.item_table_pandect, mHallListData);
        mHallRecycler.setAdapter(mHallAdapter);

        mHallAdapter.setOnRecyclerViewItemClickListener(new PandectItemClickListener(mHallAdapter));

        //卡座
        mCardRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mCardRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(this, 7), DensityUtil.dip2px(this, 10),
                DensityUtil.dip2px(this, 7), 0));
        mCardRecycler.addItemDecoration(new RecyclerSpace(1, getResources().getDrawable(
                R.drawable.custom_classify_divder_shape)));
        mCardAdapter = new TablePandectAdapter(R.layout.item_table_pandect, mCardListData);
        mCardRecycler.setAdapter(mCardAdapter);
        mCardAdapter.setOnRecyclerViewItemClickListener(new PandectItemClickListener(mCardAdapter));

        //包厢
        mBoxRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mBoxRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(this, 7), DensityUtil.dip2px(this, 10),
                DensityUtil.dip2px(this, 7), 0));
        mBoxRecycler.addItemDecoration(new RecyclerSpace(1, getResources().getDrawable(
                R.drawable.custom_classify_divder_shape)));
        mBoxAdapter = new TablePandectAdapter(R.layout.item_table_pandect, mBoxListData);
        mBoxRecycler.setAdapter(mBoxAdapter);
        mBoxAdapter.setOnRecyclerViewItemClickListener(new PandectItemClickListener(mBoxAdapter));

        showProgressDialog();
        loadData();

    }

    private class PandectItemClickListener implements BaseQuickAdapter.OnRecyclerViewItemClickListener {

        private TablePandectAdapter mAdapter;

        public PandectItemClickListener(TablePandectAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onItemClick(View view, int position) {
            StoreSeatEntity seatData = mAdapter.getItem(position);
            if (seatData == null) return;
            //0新订单，1就餐中，2 闲置中 3已买单
            switch (seatData.getState()) {
                case Constant.NEW_ORDER:
                case Constant.AT_MEAL:
                case Constant.PAID_ORDER:
                    Intent neworderIntent = new Intent(TablePandectActivity.this, TableDetailActivity.class);
                    neworderIntent.putExtra("repast_id", seatData.getRepastId());
                    neworderIntent.putExtra("order_id", seatData.getOrderId());
                    startActivity(neworderIntent);
                    break;

                case Constant.EMPTY_ORDER:
                    Intent emptyIntent = new Intent(TablePandectActivity.this, TableStartActivity.class);
                    emptyIntent.putExtra("seat_data", seatData);
                    startActivity(emptyIntent);
                    break;

            }
        }
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
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    private void loadData() {
        mPresenter.getEatBeforePay(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected TablePandectPresenter createPresenter() {
        return new TablePandectPresenter(this, this);
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
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
    public void getTablePandectSuccess(ArrayList<StoreSeatEntity> dataList) {

        if (dataList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
        }

        mHallListData.clear();
        //大厅
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.HALL) {
                mHallListData.add(data);
            }
        }
        mHallAdapter.setNewData(mHallListData);

        //卡座
        mCardListData.clear();
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.CARD) {
                mCardListData.add(data);
            }
        }
        mCardAdapter.setNewData(mCardListData);

        //包厢
        mBoxListData.clear();
        for (StoreSeatEntity data : dataList) {
            if (data.getType() == Constant.BOX) {
                mBoxListData.add(data);
            }
        }
        mBoxAdapter.setNewData(mBoxListData);

        finishRefreshAndLoad();

    }

    @Override
    public void getTablePandectFailed() {

    }

    @Override
    public void showNoData() {
        mHallAdapter.removeAll();
        mCardAdapter.removeAll();
        mBoxAdapter.removeAll();
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mHallRecycler.setVisibility(View.GONE);
        mCardRecycler.setVisibility(View.GONE);
        mBoxRecycler.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @OnClick(R.id.empty_view)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.empty_view:
                Intent intent = new Intent(this, TableBoxActivity.class);
                startActivity(intent);
                break;
        }

    }
}