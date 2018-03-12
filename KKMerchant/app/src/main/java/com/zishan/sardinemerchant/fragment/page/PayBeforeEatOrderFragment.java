package com.zishan.sardinemerchant.fragment.page;

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
import com.example.wislie.rxjava.model.MakeDishEntity;
import com.example.wislie.rxjava.presenter.base.page.main.PayBeforeEatPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.table_detail.PayBeforeEatView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.TableOutputDishDetailActivity;
import com.zishan.sardinemerchant.adapter.page.PayBeforeEatAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 先付后吃订单
 * Created by wislie on 2017/9/21.
 */

public class PayBeforeEatOrderFragment extends BFragment<PayBeforeEatView,
        PayBeforeEatPresenter> implements PayBeforeEatView {

    @BindView(R.id.order_springview)
    SpringView mSpringView;
    @BindView(R.id.order_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.permission_layout)
    LinearLayout mPermissionLayout;

    private PayBeforeEatAdapter mAdapter;
    private ArrayList<MakeDishEntity> mDishList = new ArrayList<>();

    //true表示已出菜,false表示未出菜
    private boolean is_out_dish;

    public static PayBeforeEatOrderFragment newInstance(boolean is_out_dish) {
        PayBeforeEatOrderFragment fg = new PayBeforeEatOrderFragment();
        Bundle data = new Bundle();
        data.putBoolean("is_out_dish", is_out_dish);
        fg.setArguments(data);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        is_out_dish = getArguments().getBoolean("is_out_dish");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_pay_before_eat_order;
    }

    @Override
    protected void initBizView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PayBeforeEatAdapter(R.layout.item_pay_before_eat, mDishList, is_out_dish);
        mAdapter.openLoadMore(true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<MakeDishEntity> dataList = mAdapter.getData();
                if (dataList == null) return;
                MakeDishEntity data = dataList.get(position);
                Intent intent = new Intent(getActivity(), TableOutputDishDetailActivity.class);
                intent.putExtra("repast_id", data.getId());
                intent.putExtra("is_out_dish", is_out_dish);
                startActivity(intent);
            }
        });
    }

    @Override
    protected PayBeforeEatPresenter createPresenter() {
        return new PayBeforeEatPresenter(getActivity(), this);
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
                    initData();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });

    }


    @Override
    protected void loadData() {
        initData();
    }

    public void initData() {
        Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        if (storeId != null && storeId.longValue() > 0) {
            mPresenter.getPayBeforeEat(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoadDataCompleted && getUserVisibleHint()) {
            if (mPermissionLayout != null) {
                mPermissionLayout.setVisibility(View.INVISIBLE);
            }
            loadData();
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


    private ArrayList<MakeDishEntity> getDishList(
            ArrayList<MakeDishEntity> dishList, boolean is_out_dish) {
        ArrayList<MakeDishEntity> dList = new ArrayList<>();
        if (!is_out_dish) {
            for (MakeDishEntity dish : dishList) {
                if (dish.getDishesOutNum() < dish.getDishesNum()) {
                    dList.add(dish);
                }
            }
        } else {
            for (MakeDishEntity dish : dishList) {
                if (dish.getDishesOutNum() == dish.getDishesNum()) {
                    dList.add(dish);
                }
            }
        }
        return dList;
    }


    @Override
    public void getPayBeforeEatSuccess(ArrayList<MakeDishEntity> dataList) {
        ArrayList<MakeDishEntity> currentDishList = getDishList(dataList, is_out_dish);

        if (currentDishList.size() > 0) {
            mRecycler.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mAdapter.setNewData(currentDishList);
            mAdapter.notifyDataChangedAfterLoadMore(false);
           /* View footer = showFooterNoMoreData();
            if (canScrollVertical(mRecycler, 1, footer)) {
                mAdapter.notifyDataChangedAfterLoadMore(false);
                mAdapter.addFooterView(footer);
            }*/
        }
        finishRefreshAndLoad();
        mPermissionLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getPayBeforeEatFailed() {
        finishRefreshAndLoad();

    }

    @Override
    public void showNoPayBeforeEatData() {
        if (mRecycler != null)
            mRecycler.setVisibility(View.GONE);
        if (mEmptyLayout != null)
            mEmptyLayout.setVisibility(View.VISIBLE);
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.INVISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void showNewMessage(Integer msgCount) {

    }

    @Override
    public void getPermissionFailed() {
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.permission_layout})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.permission_layout:
                showPermisssionDialog("营业桌台管理权限");
                break;
        }
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
