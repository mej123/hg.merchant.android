package com.zishan.sardinemerchant.fragment.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.example.wislie.rxjava.presenter.base.page.advance_remind.AppointmentRemindListPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.advance_remind.AppointmentRemindListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.AppointmentRemindDetailActivity;
import com.zishan.sardinemerchant.activity.page.TableArrangeActivity;
import com.zishan.sardinemerchant.adapter.page.SeatedAdapter;
import com.zishan.sardinemerchant.entity.SelectData;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.view.XTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * Created by yang on 2017/10/13.
 * <p>
 * 预约落座  fragment
 */

public class AppointmentRemindSeatedFragment extends BFragment<AppointmentRemindListView,
        AppointmentRemindListPresenter> implements AppointmentRemindListView {

    @BindView(R.id.seated_springview)
    SpringView mSpringView;
    @BindView(R.id.seated_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.select_options)
    TextView mSelectOptionsText;
    @BindView(R.id.order_amount)
    XTextView xtextview;

    //当前页
    private int mCurrentPage;
    //每页的size
    private int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;

    //到店状态数组
    private int[] states = new int[]{1, 2, 6};

    private Long start_time = null;
    private Long end_time = null;
    private Integer min_dinner_num = null;
    private Integer max_dinner_num = null;
    private Integer need_room = null;

    private List<AppointmentRemindEntity> mDataList = new ArrayList<>();
    private SeatedAdapter mAdapter;

    //预约类型
    private int mType;
    //是否可筛选
    private boolean mIsOptional;
    //是否倒序
    private boolean mIsDesc;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_appointment_remind_seated;
    }

    public static AppointmentRemindSeatedFragment newInstance(int type, boolean isOptional, boolean isDesc) {
        AppointmentRemindSeatedFragment fg = new AppointmentRemindSeatedFragment();
        Bundle data = new Bundle();
        data.putInt("type", type);
        data.putBoolean("isOptional", isOptional);
        data.putBoolean("isDesc", isDesc);
        fg.setArguments(data);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        mIsOptional = getArguments().getBoolean("isOptional");
        mIsDesc = getArguments().getBoolean("isDesc");
    }

    @Override
    protected void initBizView() {

        initSpringView();

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new SpaceItemDecoration(0, 0, 0,
                DensityUtil.dip2px(getActivity(), 8)));
        mAdapter = new SeatedAdapter(R.layout.item_appointment_remind_seated, mDataList);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                reqeustAppointmentRemindList();
            }
        });
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppointmentRemindEntity seatData = mAdapter.getItem(position);
                if (seatData == null) return;
                //未到店 或 已接受超时
                if (seatData.getState() == states[0] || seatData.getState() == states[2]) {
                    Intent intent = new Intent(getActivity(), AppointmentRemindDetailActivity.class);
                    intent.putExtra("seat_data", seatData);
                    intent.putExtra("type", mType);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {
            @Override
            public void onSelected(int position) {
                AppointmentRemindEntity seatData = mAdapter.getItem(position);
                if (seatData == null) return;
                Intent intent = new Intent(getActivity(), TableArrangeActivity.class);
                intent.putExtra("seat_data", seatData);
                startActivity(intent);
            }
        });

        if (mIsDesc) {
            PAGE_SIZE = 500;
        }

        loadData();

    }

    @Override
    protected AppointmentRemindListPresenter createPresenter() {
        return new AppointmentRemindListPresenter(getActivity(), this);
    }


    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(getActivity()));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();//停止加载
                } else {
                    //第一次加载
                    initAppointmentRemindList();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }


    @Override
    protected void loadData() {
        showProgressDialog();
        initAppointmentRemindList();
    }

    private void initAppointmentRemindList() {
        mCurrentPage = 0;
        mPresenter.getAppointmentRemindList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                mCurrentPage, PAGE_SIZE, states, start_time, end_time, min_dinner_num, max_dinner_num, need_room, true);
    }


    //非第一次加载
    private void reqeustAppointmentRemindList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getAppointmentRemindList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                    mCurrentPage, PAGE_SIZE, states, start_time, end_time, min_dinner_num, max_dinner_num, need_room, false);
        } else {
            finishRefreshAndLoad(); //停止加载
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
    public void getAppointmentRemindListFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void newAppointmentRemindList(ArrayList<AppointmentRemindEntity> dataList) {
        if (dataList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
        if (!mIsDesc) {
            mAdapter.setNewData(dataList);
        } else {
            mAdapter.setNewData(getAppointmentRemindListByDesc(dataList));
        }

        finishRefreshAndLoad();
    }

    //按照时间降序
    private ArrayList<AppointmentRemindEntity> getAppointmentRemindListByDesc(
            ArrayList<AppointmentRemindEntity> dataList) {
        int in, out;
        long temp = 0;

        for (out = 0; out < dataList.size(); out++) {

            for (in = dataList.size() - 1; in > out; in--) {
                AppointmentRemindEntity previous = dataList.get(in);
                AppointmentRemindEntity next = dataList.get(in - 1);

                if (previous.getDinnerTime() > next.getDinnerTime()) {
                    temp = previous.getDinnerTime();
                    previous.setDinnerTime(next.getDinnerTime());
                    next.setDinnerTime(temp);
                }
            }

        }
        return dataList;
    }


    @Override
    public void addAppointmentRemindList(ArrayList<AppointmentRemindEntity> dataList) {
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
        xtextview.setText("共 sBigeFontsBlue" + totalElements + "eBig 笔预约");
    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);

        if (mIsOptional && baseEvent.getTagString().equals(ACTION_NAME)) {

            SelectData newData = (SelectData) baseEvent.getData();
            start_time = newData.getStart_time();
            end_time = newData.getEnd_time();
            min_dinner_num = newData.getMin_dinner_num();
            max_dinner_num = newData.getMax_dinner_num();
            need_room = newData.getNeed_room();

            mSelectOptionsText.setText(getSelectOptions(newData));
            loadData();

        }

    }

    private String getSelectOptions(SelectData data) {
        StringBuilder sBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(data.getDateContent()) && !data.getDateContent().equals("自定义")) {
            sBuilder.append(data.getDateContent()).append("  ");
        }

        if (!TextUtils.isEmpty(data.getSeatContent())) {
            sBuilder.append(data.getSeatContent()).append("  ");
        }

        if (!TextUtils.isEmpty(data.getNumContent())) {
            sBuilder.append(data.getNumContent()).append("  ");
        }
        return sBuilder.toString();
    }


}
