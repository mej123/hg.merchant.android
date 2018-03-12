package com.zishan.sardinemerchant.activity.personal.bill;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.BillEntity;
import com.example.wislie.rxjava.model.personal.ExpectDayBillsEntity;
import com.example.wislie.rxjava.presenter.personal.BillPresenter;
import com.example.wislie.rxjava.view.personal.BillView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.BillAdapter;
import com.zishan.sardinemerchant.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2017/11/13.
 * <p>
 * 账单
 */
public class BillActivity extends BActivity<BillView, BillPresenter> implements BillView {
    @BindView(R.id.recycle_wait_calculate_bill_view)
    RecyclerView mRecycleWaitCalculateBillView;
    @BindView(R.id.total_revenue_count)
    TextView mTotalRevenueCount;
    @BindView(R.id.tv_advance_entry_bill)
    TextView mAdvanceEntryBill;
    private List<ExpectDayBillsEntity> dataList = new ArrayList<>();
    private BillAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.bill));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected BillPresenter createPresenter() {
        return new BillPresenter(this, this);
    }

    @Override
    protected void initContentView() {

        mAdapter = new BillAdapter(R.layout.item_bill, dataList);
        mRecycleWaitCalculateBillView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleWaitCalculateBillView.setNestedScrollingEnabled(false);
        mRecycleWaitCalculateBillView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ExpectDayBillsEntity expectDayBillsEntity = mAdapter.getItem(position);
                if (expectDayBillsEntity == null) return;
                Intent advanceIntent = new Intent();
                advanceIntent.setClass(BillActivity.this, BillParticularsActivity.class);
                advanceIntent.putExtra("id", String.valueOf(expectDayBillsEntity.getId()));//未入账日账单id
                advanceIntent.putExtra("calculateState", "0");//已入账未入账类型
                advanceIntent.putExtra("date", String.valueOf(expectDayBillsEntity.getBillDay()));//未入账日账单日期
                startActivity(advanceIntent);
            }
        });
        Long storeId = UserConfig.getInstance(this).getStoreId();
        if (storeId == null) return;
        mPresenter.getMerchantBillMainInfo(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.rl_already_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_already_layout://已结算订单
                Intent alreadyIntent = new Intent();
                alreadyIntent.setClass(this, AlreadyCalculateBillActivity.class);
                startActivity(alreadyIntent);
                break;
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
    public void getMerchantBillMainInfoComplete(BillEntity billEntity) {

        if (billEntity == null) return;
        Long totalRevenue = billEntity.getTotalRevenue();
        Long expectRevenue = billEntity.getExpectRevenue();
        mTotalRevenueCount.setText(FormatMoneyUtil.pennyChangeDollar(totalRevenue));//总收益
        mAdvanceEntryBill.setText(FormatMoneyUtil.pennyChangeDollar(expectRevenue));//预计入账
        List<ExpectDayBillsEntity> expectDayBills = billEntity.getExpectDayBills();

        if (expectDayBills != null) {
            mAdapter.setNewData(expectDayBills);
           // dataList.addAll(expectDayBills);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMerchantBillMainInfoFailed() {

    }
}
