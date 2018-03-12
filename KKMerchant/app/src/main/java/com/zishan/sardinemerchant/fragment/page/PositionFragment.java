package com.zishan.sardinemerchant.fragment.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.presenter.base.page.main.EatBeforePayPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.table_detail.EatBeforePayView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.TableDetailActivity;
import com.zishan.sardinemerchant.activity.page.TableStartActivity;

import com.zishan.sardinemerchant.adapter.page.EatBeforePayAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.recyclerview.WrapStaggeredGridLayoutManager;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;


/**
 * 大厅 卡座 包厢 fragment
 * Created by wislie on 2017/9/21.
 */

public class PositionFragment extends BFragment<EatBeforePayView, EatBeforePayPresenter>
        implements EatBeforePayView {


    @BindView(R.id.order_springview)
    SpringView mSpringView;
    @BindView(R.id.order_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.empty_table_layout)
    LinearLayout mEmptyTableLayout;
    @BindView(R.id.empty_dish_layout)
    LinearLayout mEmptyDishLayout;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.permission_layout)
    LinearLayout mPermissionLayout;

    @BindView(R.id.state_all)
    View mStateAll;
    @BindView(R.id.state_new_order)
    View mStateNewOrder;
    @BindView(R.id.state_at_meal)
    View mStateAtMeal;
    @BindView(R.id.state_empty)
    View mStateEmpty;
    @BindView(R.id.state_paid_order)
    View mStatePaidOrder;

    private EatBeforePayAdapter mAdapter;
    private List<StoreSeatEntity> mSeatList = new ArrayList<>();

    //类型，状态
    private int mType, mState;

    //是否初始化完成
    private boolean mInited = false;
    //操作小红点
    private MyHandler mHandler = new MyHandler(this);

    private static final int UPDATE_RED_DOT = 0x01;
    private static final int UPDATE_LIST = 0x02;


    //初始化的map
    private Map<Integer, Integer> mInitStateMap = new ConcurrentHashMap<>();
    //需要检测的map
    private Map<Integer, Integer> mCheckStateMap = new ConcurrentHashMap<>();

    private static class MyHandler extends Handler {
        WeakReference<PositionFragment> softReference;

        public MyHandler(PositionFragment fg) {
            softReference = new WeakReference<>(fg);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PositionFragment fg = softReference.get();
            if (fg != null) {
                switch (msg.what) {
                    case UPDATE_RED_DOT:
                        fg.updateRedDot(msg.arg1, true);
                        break;
                    case UPDATE_LIST:
                        Bundle bundle = msg.getData();
                        ArrayList<StoreSeatEntity> currentSeatList = bundle.getParcelableArrayList("seat_list");
                        fg.updateLayout(currentSeatList);
                        break;
                }


            }
        }
    }

    public static PositionFragment newInstance(int type) {
        PositionFragment fg = new PositionFragment();
        Bundle data = new Bundle();
        data.putInt("type", type);
        fg.setArguments(data);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        mState = Constant.ALL_ORDER;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBizView() {
        initSpringView();
        mRecycler.setLayoutManager(new WrapStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(getActivity(), 7), DensityUtil.dip2px(getActivity(), 10),
                DensityUtil.dip2px(getActivity(), 7), 0));
        mAdapter = new EatBeforePayAdapter(R.layout.item_table_pandect, mSeatList);//R.layout.item_eat_before_pay
        mRecycler.setAdapter(mAdapter);
        mAdapter.openLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadOrderData();
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StoreSeatEntity seatData = mAdapter.getItem(position);
                if (seatData == null) return;
                //0新订单，1就餐中，2 闲置中 3已买单
                switch (seatData.getState()) {
                    case Constant.NEW_ORDER:
                    case Constant.AT_MEAL:
                    case Constant.PAID_ORDER:
                        Intent neworderIntent = new Intent(getActivity(), TableDetailActivity.class);
                        neworderIntent.putExtra("repast_id", seatData.getRepastId());
                        neworderIntent.putExtra("order_id", seatData.getOrderId());
                        startActivity(neworderIntent);
                        break;

                    case Constant.EMPTY_ORDER:
                        Intent emptyIntent = new Intent(getActivity(), TableStartActivity.class);
                        emptyIntent.putExtra("seat_data", seatData);
                        startActivity(emptyIntent);
                        break;

                }
            }
        });
        initTabView(0);
    }

    private void initTabView(int position) {


        TextView titleAllText = (TextView) mStateAll.findViewById(R.id.tab_title);

        TextView titleNewOrderText = (TextView) mStateNewOrder.findViewById(R.id.tab_title);

        TextView titleAtMealText = (TextView) mStateAtMeal.findViewById(R.id.tab_title);

        TextView titleEmptyText = (TextView) mStateEmpty.findViewById(R.id.tab_title);

        TextView titlePaidText = (TextView) mStatePaidOrder.findViewById(R.id.tab_title);

        if (position == 0) {
            titleAllText.setSelected(true);
            titleNewOrderText.setSelected(false);
            titleAtMealText.setSelected(false);
            titleEmptyText.setSelected(false);
            titlePaidText.setSelected(false);
        }

        if (position == 1) {
            titleAllText.setSelected(false);
            titleNewOrderText.setSelected(true);
            titleAtMealText.setSelected(false);
            titleEmptyText.setSelected(false);
            titlePaidText.setSelected(false);
        }

        if (position == 2) {
            titleAllText.setSelected(false);
            titleNewOrderText.setSelected(false);
            titleAtMealText.setSelected(true);
            titleEmptyText.setSelected(false);
            titlePaidText.setSelected(false);
        }

        if (position == 3) {
            titleAllText.setSelected(false);
            titleNewOrderText.setSelected(false);
            titleAtMealText.setSelected(false);
            titleEmptyText.setSelected(true);
            titlePaidText.setSelected(false);
        }

        if (position == 4) {
            titleAllText.setSelected(false);
            titleNewOrderText.setSelected(false);
            titleAtMealText.setSelected(false);
            titleEmptyText.setSelected(false);
            titlePaidText.setSelected(true);
        }

        titleAllText.setText("全部");
        titleNewOrderText.setText("新订单");
        titleAtMealText.setText("就餐中");
        titleEmptyText.setText("闲置中");
        titlePaidText.setText("已买单");

    }


    //更新tablayout上的小红点

    private void updateRedDot(int position, boolean visible) {
        if(mStateNewOrder == null) return;
        ImageView msgNewOrderIcon = (ImageView) mStateNewOrder.findViewById(R.id.tab_msg);

        ImageView msgAtMealIcon = (ImageView) mStateAtMeal.findViewById(R.id.tab_msg);

        ImageView msgEmptyIcon = (ImageView) mStateEmpty.findViewById(R.id.tab_msg);

        ImageView msgPaidIcon = (ImageView) mStatePaidOrder.findViewById(R.id.tab_msg);

        switch (position) {
            case Constant.NEW_ORDER:

                msgNewOrderIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                break;
            case Constant.AT_MEAL:
                msgAtMealIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                break;
            case Constant.EMPTY_ORDER:
                msgEmptyIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                break;
            case Constant.PAID_ORDER:
                msgPaidIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                break;
        }

    }


    @Override
    protected EatBeforePayPresenter createPresenter() {
        return new EatBeforePayPresenter(getActivity(), this);
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
                    loadOrderData();
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

    private long mTempStoreId;

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrderData();
    }

    private void loadOrderData() {

        Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        if (storeId != null && storeId.longValue() > 0) {
            if (mTempStoreId != 0 && mTempStoreId != storeId) {
                mInitStateMap.clear();
                mInited = false;
            }
            mPresenter.getEatBeforePay(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
            mTempStoreId = storeId;
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
    public void getEatBeforePaySuccess(ArrayList<StoreSeatEntity> dataList) {
        if (!mInited) {
            saveStates(getSeatListByType(dataList));
        } else {
            compareStateCount(getSeatListByType(dataList));
        }
        mInited = true;


        ArrayList<StoreSeatEntity> currentSeatList = getSeatList(dataList, mType, mState);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("seat_list", currentSeatList);
        msg.setData(bundle);
        msg.what = UPDATE_LIST;
        mHandler.sendMessage(msg);


    }

    //更新布局
    private void updateLayout(ArrayList<StoreSeatEntity> currentSeatList) {

        if (currentSeatList.size() > 0) {
            if (mRecycler != null)
                mRecycler.setVisibility(View.VISIBLE);
            if (mEmptyTableLayout != null)
                mEmptyTableLayout.setVisibility(View.GONE);
            if (mEmptyDishLayout != null)
                mEmptyDishLayout.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            mAdapter.setNewData(currentSeatList);
            mAdapter.notifyDataChangedAfterLoadMore(false);
            finishRefreshAndLoad();
        } else {
            showNoEatBeforePayData();
        }
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.INVISIBLE);
    }


    private ArrayList<StoreSeatEntity> getSeatList(
            ArrayList<StoreSeatEntity> seatList, int type, int state) {
        ArrayList<StoreSeatEntity> seats = new ArrayList<>();

        if (state == Constant.ALL_ORDER) {
            for (StoreSeatEntity seat : seatList) {
                if (seat.getType() == type) {
                    seats.add(seat);
                }
            }
            return seats;
        }


        for (StoreSeatEntity seat : seatList) {
            if (seat.getType() == type && seat.getState() == state) {
                seats.add(seat);
            }
        }
        return seats;
    }

    //根据类型获取seatlist
    private ArrayList<StoreSeatEntity> getSeatListByType(
            ArrayList<StoreSeatEntity> seatList) {
        ArrayList<StoreSeatEntity> seats = new ArrayList<>();
        for (StoreSeatEntity seat : seatList) {
            if (seat.getType() == mType) {
                seats.add(seat);
            }
        }
        return seats;
    }

    @Override
    public void getEatBeforePayFailed() {

    }

    @Override
    public void showNoEatBeforePayData() {
        mAdapter.removeAll();
        if (mState != Constant.EMPTY_ORDER) {
            if (mEmptyTableLayout != null)
                mEmptyTableLayout.setVisibility(View.GONE);
            if (mEmptyDishLayout != null)
                mEmptyDishLayout.setVisibility(View.VISIBLE);
        } else {
            if (mEmptyTableLayout != null)
                mEmptyTableLayout.setVisibility(View.VISIBLE);
            if (mEmptyDishLayout != null)
                mEmptyDishLayout.setVisibility(View.GONE);
        }
        if (mRecycler != null)
            mRecycler.setVisibility(View.GONE);
        if (mEmptyView != null)
            mEmptyView.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNewMessage(Integer msgCount) {

    }

    @Override
    public void getPermissionFailed() {
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i <= 4; i++) {
            updateRedDot(i, false);
        }

    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            int permission_state = bundle.getInt("permission_state");
            if (permission_state == Constant.PERMISSION_ALLOWED) {
                //权限开启
                ArrayList<StoreSeatEntity> dataList = bundle.getParcelableArrayList("seat_list");
                if (dataList != null) {
                    if (mPermissionLayout != null) {
                        mPermissionLayout.setVisibility(View.INVISIBLE);
                    }
                    finishRefreshAndLoad();
                }
                compareStateCount(getSeatListByType(dataList));

            } else if (permission_state == Constant.PERMISSION_FORBIDDEN) {
                //权限禁止
                if (mPermissionLayout != null) {
                    mPermissionLayout.setVisibility(View.VISIBLE);
                }
                finishRefreshAndLoad();
            }
        }

    }


    //state 的数量
    private synchronized void saveStates(ArrayList<StoreSeatEntity> dataList) {
        mInitStateMap.clear();
        mInitStateMap.put(Constant.NEW_ORDER, 0);
        mInitStateMap.put(Constant.AT_MEAL, 0);
        mInitStateMap.put(Constant.EMPTY_ORDER, 0);
        mInitStateMap.put(Constant.PAID_ORDER, 0);

        for (int i = 0; i < dataList.size(); i++) {
            StoreSeatEntity data = dataList.get(i);
            int state = data.getState();
            if (mInitStateMap.get(state) == null
                    || mInitStateMap.get(state) == 0) {
                mInitStateMap.put(state, 1);
            } else {
                mInitStateMap.put(state, mInitStateMap.get(state) + 1);
            }
        }
    }

    //比较数量是否发生变化, 数量变多了就有小红点
    private synchronized void compareStateCount(ArrayList<StoreSeatEntity> dataList) {
        mCheckStateMap.clear();
        mCheckStateMap.put(Constant.NEW_ORDER, 0);
        mCheckStateMap.put(Constant.AT_MEAL, 0);
        mCheckStateMap.put(Constant.EMPTY_ORDER, 0);
        mCheckStateMap.put(Constant.PAID_ORDER, 0);

        for (int i = 0; i < dataList.size(); i++) {
            StoreSeatEntity data = dataList.get(i);
            int state = data.getState();

            if (mCheckStateMap.get(state) == null
                    || mCheckStateMap.get(state) == 0) {
                mCheckStateMap.put(state, 1);
            } else {
                mCheckStateMap.put(state, mCheckStateMap.get(state) + 1);
            }
        }


        for (int i = 0; i < 4; i++) {
            if (mInitStateMap == null) continue;
            if (mInitStateMap.get(i) == null || mCheckStateMap.get(i) == null) continue;
            int value = mInitStateMap.get(i);
            int checkedValue = mCheckStateMap.get(i);
            if (checkedValue > value) {
                Message msg = Message.obtain();
                msg.what = UPDATE_RED_DOT;
                msg.arg1 = i;
                mHandler.sendMessage(msg);
            }
            mInitStateMap.put(i, checkedValue);
        }
        mCheckStateMap.clear();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInited = false;
        mInitStateMap.clear();

        mHandler.removeCallbacksAndMessages(null);

    }


    @OnClick({R.id.state_all, R.id.state_new_order, R.id.state_at_meal, R.id.state_empty, R.id.state_paid_order, R.id.permission_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //全部
            case R.id.state_all:
                mState = Constant.ALL_ORDER;
                initTabView(0);
                loadOrderData();
                break;
            //新订单
            case R.id.state_new_order:
                mState = Constant.NEW_ORDER;
                initTabView(1);
                updateRedDot(0, false);
                loadOrderData();
                break;
            //就餐中
            case R.id.state_at_meal:
                mState = Constant.AT_MEAL;
                initTabView(2);
                updateRedDot(1, false);
                loadOrderData();
                break;
            //闲置中
            case R.id.state_empty:
                mState = Constant.EMPTY_ORDER;
                initTabView(3);
                updateRedDot(2, false);
                loadOrderData();
                break;
            //已买单
            case R.id.state_paid_order:
                mState = Constant.PAID_ORDER;
                initTabView(4);
                updateRedDot(3, false);
                loadOrderData();
                break;
            //无权限
            case R.id.permission_layout:
                showPermisssionDialog("营业桌台管理权限");
                break;
        }
    }

}
