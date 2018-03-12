package com.zishan.sardinemerchant.activity.personal.bill;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.AlreadyListEntity;
import com.example.wislie.rxjava.presenter.personal.AlreadyCalculateBillPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.personal.AlreadyCalculateBillView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.AlreadyCalculateBillAdapter;
import com.zishan.sardinemerchant.dialog.PickerDateWheelDialog;
import com.zishan.sardinemerchant.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;


/**
 * Created by yang on 2017/11/13.
 * <p>
 * 已结算
 */

public class AlreadyCalculateBillActivity extends BActivity<AlreadyCalculateBillView,
        AlreadyCalculateBillPresenter> implements AlreadyCalculateBillView {
    @BindView(R.id.already_calculate_bill_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.already_calculate_bill_springview)
    SpringView mSpringView;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.bill_empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.tv_select_time)
    TextView mSelectTime;
    //当前页
    private int mCurrentPage = 0;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;
    private List<AlreadyListEntity> mDataList = new ArrayList<>();
    private AlreadyCalculateBillAdapter mAdapter;
    private long endTime;
    private long startTime;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.already_calculate_bills));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarMenuIcon(R.mipmap.time_select_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PickerDateWheelDialog dialog = PickerDateWheelDialog.newInstance("", false, false);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        long startTime = DatePickerUtil.getFutureStartTime(Integer.parseInt(values[0]),//开始时间秒级时间戳
                                Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                        long endTime = DatePickerUtil.getFutureEndTime(Integer.parseInt(values[3]),//结束时间秒级时间戳
                                Integer.parseInt(values[4]) - 1, Integer.parseInt(values[5]));
                        String start_time = getSelectDate(values[0], values[1], values[2]);
                        String end_time = getSelectDate(values[3], values[4], values[5]);
                        mView.setVisibility(View.GONE);
                        mSelectTime.setVisibility(View.VISIBLE);
                        mSelectTime.setText(start_time + "~" + end_time);
                        mDataList.clear();
                        mPresenter.getCalculateBillByPage(UserConfig.getInstance(ClientApplication.getApp()).
                                getStoreId(), mCurrentPage, PAGE_SIZE, startTime, endTime, true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    protected AlreadyCalculateBillPresenter createPresenter() {
        return new AlreadyCalculateBillPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_already_calculate_bills;
    }

    @Override
    protected void initContentView() {

        initSpringView();
        mAdapter = new AlreadyCalculateBillAdapter(R.layout.item_already_calculate_bill, mDataList);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //传递开始时间和结束时间
                AlreadyListEntity mEntity = mAdapter.getItem(position);
                //List<DayBillsEntity> dayBills = mEntity.getDayBills();
                Intent Intent = new Intent();
                Intent.setClass(AlreadyCalculateBillActivity.this, BillParticularsActivity.class);
                Intent.putExtra("calculateState", "1");
                String start_time = DatePickerUtil.getFormatDate(mEntity.getBillStartTimeStamp(), "yyyy-MM-dd");
                String end_time = DatePickerUtil.getFormatDate(mEntity.getBillEndTimeStamp(), "yyyy-MM-dd");
                Intent.putExtra("start_time", start_time);
                Intent.putExtra("end_time", end_time);
                // Intent.putExtra("totalRevenue", mEntity.getRevenue());
                startActivity(Intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestStoreOrderList();
            }
        });
        showProgressDialog();
        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            loadData();
        }
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    mSpringView.onFinishFreshAndLoad(); //停止加载
                } else {
                    //第一次加载
                    loadData();

                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    private void loadData() {
        // mDataList.clear();
        mCurrentPage = 0;
        //默认查询前15天的账单列表
        //今日23:59:59
        endTime = DatePickerUtil.getEndTime(0);
        //15天前的00：00:00
        startTime = DatePickerUtil.getEndTime(-15);
        mSelectTime.setText(DatePickerUtil.getFormatDate(startTime, "yyyy.MM.dd")+"~"+
                DatePickerUtil.getFormatDate(endTime, "yyyy.MM.dd"));
        mSelectTime.setVisibility(View.VISIBLE);
        mPresenter.getCalculateBillByPage(UserConfig.getInstance(ClientApplication.getApp()).
                getStoreId(), mCurrentPage, PAGE_SIZE, startTime, endTime, true);
        finishRefreshAndLoad();
    }

    /**
     * 非第一次发起请求
     */
    private void requestStoreOrderList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            // finishRefreshAndLoad();
            mPresenter.getCalculateBillByPage(UserConfig.getInstance(ClientApplication.getApp()).
                    getStoreId(), mCurrentPage, PAGE_SIZE, startTime, endTime, false);
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
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    private String getSelectDate(String year, String month, String day) {
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "." + month + "." + day;
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
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void newCalculateBillByPageList(List<AlreadyListEntity> dataList) {
        //清空之前加载的数据
        List<AlreadyListEntity>  data = mAdapter.getData();
        data.clear();
        if (dataList == null) return;
        if (dataList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mAdapter.setNewData(dataList);
        }
        finishRefreshAndLoad();
    }

    @Override
    public void addCalculateBillByPageList(List<AlreadyListEntity> dataList) {
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
}
