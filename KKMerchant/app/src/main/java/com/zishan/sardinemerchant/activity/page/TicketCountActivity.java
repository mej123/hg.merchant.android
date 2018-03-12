package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TicketCountEntity;
import com.example.wislie.rxjava.presenter.base.page.ticket.TicketCountPresenter;
import com.example.wislie.rxjava.view.base.page.ticket.TicketCountView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.PickerDateWheelDialog;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 统计详情
 * Created by wislie on 2018/1/23.
 */

public class TicketCountActivity extends BActivity<TicketCountView, TicketCountPresenter>
        implements TicketCountView {

    @BindView(R.id.release_date)
    TextView mReleaseDateText;
    @BindView(R.id.revenue_total)
    TextView mRevenueTotalText;
    @BindView(R.id.dispatch_num)
    TextView mDispatchNumText;
    @BindView(R.id.certificate_num)
    TextView mCertificateNumText;
    @BindView(R.id.cost_price)
    TextView mCostPriceText;
    @BindView(R.id.award_unit)
    TextView mAwardUnitText;
    @BindView(R.id.certificate_total_num)
    TextView mCertificateTotalNumText;
    @BindView(R.id.recycle_cost)
    TextView mRecycleCostText;
    @BindView(R.id.award_deliver)
    TextView mAwardDeliverText;
    @BindView(R.id.exchange_fee)
    TextView mExchangeFeeText;
    @BindView(R.id.revenue)
    TextView mRevenueText;
    @BindView(R.id.input_num)
    TextView mInputNumText;
    @BindView(R.id.reject_num)
    TextView mRejectNumText;
    @BindView(R.id.recycle_price)
    TextView mRecyclePriceText;
    @BindView(R.id.supply_price)
    TextView mSupplyPriceText;
    @BindView(R.id.user_return_num)
    TextView mUserReturnNumText;
    @BindView(R.id.expired_return_num)
    TextView mExpiredReturnNumText;
    @BindView(R.id.seller_return_num)
    TextView mSellerReturnNumText;
    @BindView(R.id.receive_amount)
    TextView mReceiveAmountText;
    @BindView(R.id.exchange_fee_2)
    TextView mExchangeFee2Text;

    private long give_out_id;
    private long start_time;
    private long end_time;

    @Override
    protected TicketCountPresenter createPresenter() {
        return new TicketCountPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_result;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color);
        setActionbarTitle(getString(R.string.release_detail));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        ImageView menuIcon = setActionBarMenuIcon(R.mipmap.time_select_white_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateDialog();
            }
        });
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null) {
            give_out_id = intent.getLongExtra("coupon_give_out_id", give_out_id);
            start_time = intent.getLongExtra("giveOutStartTime", start_time);
            end_time = intent.getLongExtra("giveOutEndTime", end_time);
        }
        mPresenter.getTicketCount(give_out_id,
                DatePickerUtil.getFormatDate(start_time, "yyyy-MM-dd"),
                DatePickerUtil.getFormatDate(end_time, "yyyy-MM-dd"));
        mReleaseDateText.setText(DatePickerUtil.getFormatDate(end_time, "yyyy-MM-dd"));
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
    public void getTicketCountSuccess(TicketCountEntity data) {
        if (data == null) return;
        updateTicketCount(data);

    }

    @Override
    public void getTicketCountFailed() {

    }

    private void updateTicketCount(TicketCountEntity data) {
        mRevenueTotalText.setText(getFormatData(data.getAmount()));
        mDispatchNumText.setText(getFormatData(data.getGiveOutNum()) + "张");
        mCertificateNumText.setText(getFormatData(data.getUseNum()) + "张");
        mCostPriceText.setText(StringUtil.point2String(data.getCost()) + "元");
        mAwardUnitText.setText(StringUtil.point2String(data.getTotalAward()) + "元");
        mCertificateTotalNumText.setText(getFormatData(data.getUseNum()) + "张");
        mRecycleCostText.setText("+" + StringUtil.point2String(data.getTotalCost()) + "元");
        mAwardDeliverText.setText("-" + StringUtil.point2String(data.getTotalAward()) + "元");
        if (data.getUseServiceMoney() != null && data.getServiceMoney() != null) {
            mExchangeFeeText.setText("-" + StringUtil.point2String(data.getUseServiceMoney() + data.getServiceMoney()) + "元");
        }
//        mRevenueText.setText("+" + data.getTotalCost() + "元");
//        mInputNumText.setText("+" + data.getTotalCost() + "元");

        mRejectNumText.setText("+" + getFormatData(data.getTotalDrop()) + "张");

        mRecyclePriceText.setText(StringUtil.point2String(data.getTotalCost()) + "元");
//        mSupplyPriceText.setText(data.getTotalCost() + "元");

        mUserReturnNumText.setText(getFormatData(data.getDropNum()) + "张");

        mExpiredReturnNumText.setText(getFormatData(data.getAutoDropNum()) + "张");

//        mSellerReturnNumText.setText("-" + data.getServiceMoney() + "元");
//        mReceiveAmountText.setText(data.getTotalCost() + "元");
//        mExchangeFee2Text.setText("-" + StringUtil.point2String(data.getServiceMoney()) + "元");
    }

    private String getFormatData(Long data) {
        if (data == null) return "0";
        return String.valueOf(data);
    }


    //筛选日期
    private void showSelectDateDialog() {
        PickerDateWheelDialog dialog = PickerDateWheelDialog.newInstance("", true, false);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {

                long startTime = DatePickerUtil.getFutureStartTime(Integer.parseInt(values[0]),
                        Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                long endTime = DatePickerUtil.getFutureEndTime(Integer.parseInt(values[3]),
                        Integer.parseInt(values[4]) - 1, Integer.parseInt(values[5]));
                mPresenter.getTicketCount(give_out_id,
                        DatePickerUtil.getFormatDate(startTime, "yyyy-MM-dd"),
                        DatePickerUtil.getFormatDate(endTime, "yyyy-MM-dd"));
                mReleaseDateText.setText(DatePickerUtil.getFormatDate(endTime, "yyyy-MM-dd"));
            }

            @Override
            public void onCancel() {

            }
        });

    }
}
