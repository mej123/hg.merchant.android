package com.zishan.sardinemerchant.activity.personal.bill;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.BillDetailsEntity;
import com.example.wislie.rxjava.presenter.personal.BillDetailsPresenter;
import com.example.wislie.rxjava.view.personal.BillDetailsView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;

/**
 * Created by yang on 2017/11/13.
 * <p>
 * 账单详情
 */

public class BillDetailsActivity extends BActivity<BillDetailsView, BillDetailsPresenter>
        implements BillDetailsView {
    @BindView(R.id.bill_amount)
    TextView mBillAmount;
    @BindView(R.id.activity_reduction)
    TextView mActivityReduction;
    @BindView(R.id.user_reduction)
    TextView mUserReduction;
    @BindView(R.id.trading_volume)
    TextView mTradingVolume;
    @BindView(R.id.pay_fee_commission)
    TextView mPayFeeCommission;
    @BindView(R.id.promotion_fee_money)
    TextView mPromotionFeeMoney;
    @BindView(R.id.merchant_order_number)
    TextView mMerchantOrderNumber;
    @BindView(R.id.tv_clear_revenue)
    TextView mClearRevenue;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.bill_details));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_details;
    }

    @Override
    protected void initContentView() {
        Long storeId = UserConfig.getInstance(this).getStoreId();
        Long id = getIntent().getLongExtra("id", 0);
        if (storeId == null || id == null) return;
        mPresenter.getBillDetails(storeId, id);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected BillDetailsPresenter createPresenter() {
        return new BillDetailsPresenter(this, this);
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
    public void getBillDetailsComplete(BillDetailsEntity billDetailsEntity) {
        if (billDetailsEntity == null) return;
        Long clearRevenue = billDetailsEntity.getClearRevenue();//入账金额
        Long expenditure = billDetailsEntity.getExpenditure();//支出金额
        Long totalAmount = billDetailsEntity.getTotalAmount();//订单总额
        Long actDiscountAmount = billDetailsEntity.getActDiscountAmount();//活动减免
        Long userDiscountAmount = billDetailsEntity.getUserDiscountAmount();//用户减免
        Long dealAmount = billDetailsEntity.getDealAmount();//成交金额
        Integer type = billDetailsEntity.getType();
        Long payFee = billDetailsEntity.getPayFee();//支付手续费
        Long promotionFee = billDetailsEntity.getPromotionFee();//营销手续费
        Long orderId = billDetailsEntity.getOrderId();//商户订单号
        mBillAmount.setText(FormatMoneyUtil.pennyChangeDollar(totalAmount));
        mActivityReduction.setText(FormatMoneyUtil.pennyChangeDollar(actDiscountAmount));
        mUserReduction.setText(FormatMoneyUtil.pennyChangeDollar(userDiscountAmount));
        mTradingVolume.setText(FormatMoneyUtil.pennyChangeDollar(dealAmount));
        mPayFeeCommission.setText(FormatMoneyUtil.pennyChangeDollar(payFee));
        mPromotionFeeMoney.setText(FormatMoneyUtil.pennyChangeDollar(promotionFee));
        mMerchantOrderNumber.setText(String.valueOf(orderId));
        //(1:加,2:减)
        if (type == 1) {
            mClearRevenue.setText("+" + FormatMoneyUtil.pennyChangeDollar(clearRevenue));
            mClearRevenue.setTextColor(ContextCompat.getColor(this, R.color.stroke_color_blue));

        } else if (type == 2) {
            mClearRevenue.setText("-" + FormatMoneyUtil.pennyChangeDollar(expenditure));
            mClearRevenue.setTextColor(ContextCompat.getColor(this, R.color.reduce_money));
        }
    }

    @Override
    public void getBillDetailsFailed() {
    }
}
