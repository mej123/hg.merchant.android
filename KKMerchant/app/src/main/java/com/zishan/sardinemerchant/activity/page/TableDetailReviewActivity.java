package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableDetailEntity;
import com.example.wislie.rxjava.model.page.TableDishItemEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableDetailReviewPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableDetailReviewView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableDetailReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 订单详情总览
 * Created by wislie on 2017/12/10.
 */

public class TableDetailReviewActivity extends BActivity<TableDetailReviewView, TableDetailReviewPresenter>
        implements TableDetailReviewView {

    @BindView(R.id.meal_date)
    TextView mDateText;
    @BindView(R.id.meal_time)
    TextView mTimeText;
    @BindView(R.id.meal_num)
    TextView mPersonNumText;
    @BindView(R.id.detail_review_recycler)
    MaxRecyclerView mRecycler;

    @BindView(R.id.meal_modify)
    TextView mMealModifyText;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    private TableDetailReviewAdapter mAdapter;

    private List<TableDishItemEntity> mDataList = new ArrayList<>();

    //就餐id
    private long repast_id;

    @Override
    protected TableDetailReviewPresenter createPresenter() {
        return new TableDetailReviewPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_detail_review;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            repast_id = intent.getLongExtra("repast_id", repast_id);
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TableDetailReviewAdapter(R.layout.item_table_detail_review, mDataList);
        mRecycler.setAdapter(mAdapter);
        mMealModifyText.setVisibility(View.GONE);
        loadData();
    }

    private void loadData() {
        mPresenter.getTableDetail(repast_id, UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
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
    public void getTableDetailSuccess(TableDetailEntity data) {
        updateTableDetailHeader(data);
    }

    @Override
    public void getTableDishList(List<TableDishItemEntity> dataList) {
        mAdapter.setNewData(dataList);

        mEmptyLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);

    }

    @Override
    public void getTableDetailFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);

    }

    //更新头部信息
    private void updateTableDetailHeader(TableDetailEntity data) {
        setActionbarTitle(data.getSeatName());
        mDateText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "yyyy-MM.dd"));
        mTimeText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "HH:mm"));
        mPersonNumText.setText(String.valueOf(data.getDinnerNum()));
    }

}
