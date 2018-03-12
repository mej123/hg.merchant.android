package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.CouponEntity;
import com.example.wislie.rxjava.model.page.TicketDetailEntity;
import com.example.wislie.rxjava.presenter.base.page.ticket.TicketDetailPresenter;
import com.example.wislie.rxjava.view.base.page.ticket.TicketDetailView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;


import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 卡券详情
 * Created by wislie on 2018/1/22.
 */

public class TicketDetailActivity extends BActivity<TicketDetailView, TicketDetailPresenter>
        implements TicketDetailView {

    @BindView(R.id.get_ticket_create_time)
    TextView mCreateTimeText;
    @BindView(R.id.get_ticket_close_time)
    TextView mCloseTimeText;
    @BindView(R.id.ticket_store)
    TextView mStoreNumText;
    @BindView(R.id.ticket_cost)
    TextView mTicketCostText;
    @BindView(R.id.ticket_award)
    TextView mTicketAwardText;
    @BindView(R.id.cancel_release)
    TextView mTicketReleaseText;

    @BindView(R.id.item_choose_ticket)
    RelativeLayout mTicketLayout;
    @BindView(R.id.ticket_name)
    TextView mTicketNameText;
    @BindView(R.id.ticket_id)
    TextView mTicketIdText;
    @BindView(R.id.ticket_date_role)
    TextView mTicketDaysText;
    @BindView(R.id.ticket_value_layout)
    LinearLayout mTicketValueLayout;
    @BindView(R.id.ticket_sign)
    TextView mTicketSignText;
    @BindView(R.id.ticket_value)
    TextView mTicketValueText;
    @BindView(R.id.ticket_desc_role)
    TextView mTicketDescText;
    @BindView(R.id.ticket_expire_desc)
    TextView mTicketExpireText;
    //标题
    private TextView mTitleText;

    private long coupon_give_out_id;
    private long giveOutStartTime;
    private long giveOutEndTime;

    @Override
    protected TicketDetailPresenter createPresenter() {
        return new TicketDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_detail;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color);
        mTitleText = setActionbarTitle(getString(R.string.ticket_send_msg));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarDivderVisible(false);
        TextView menuText = setActionBarMenuText(getString(R.string.ticket_calculate));
        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, TicketCountActivity.class);
                intent.putExtra("coupon_give_out_id", coupon_give_out_id);
                intent.putExtra("giveOutStartTime", giveOutStartTime);
                intent.putExtra("giveOutEndTime", giveOutEndTime);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent == null) return;
        coupon_give_out_id = intent.getLongExtra("coupon_give_out_id", 0);
        mPresenter.getTicketDetail(coupon_give_out_id);
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
    public void getTicketDetailSuccess(TicketDetailEntity data) {
        if (data == null) return;
        updateDetail(data);
        if (data.getGiveOutStartTime() != null) {
            giveOutStartTime = data.getGmtCreate();
        }
        if (data.getGiveOutEndTime() != null) {
            giveOutEndTime = data.getGiveOutEndTime();
        }

    }


    private void updateDetail(TicketDetailEntity data) { //发券中带颜色,已到期，已发完，已取消灰色


        CouponEntity ticketData = data.getCouponDefine();

        if (ticketData == null) return;
        //(1:发券中,2:已到期,3:已发完,4已取消)
        if (data.getStatus() != null && data.getStatus() == 1) {
            mTitleText.setText("发放详情-发券中");
            mTicketLayout.setBackgroundResource(R.mipmap.ticket_discount_abled_bg);
        } else if (data.getStatus() != null && data.getStatus() == 2) {
            mTitleText.setText("发放详情-已到期");
            mTicketReleaseText.setVisibility(View.GONE);
            updateCardColor();
        } else if (data.getStatus() != null && data.getStatus() == 3) {
            mTitleText.setText("发放详情-已发完");
            mTicketReleaseText.setVisibility(View.GONE);
            updateCardColor();
        } else if (data.getStatus() != null && data.getStatus() == 4) {
            mTitleText.setText("发放详情-已取消");
            mTicketReleaseText.setVisibility(View.GONE);
            updateCardColor();
        }

        mCreateTimeText.setText(DatePickerUtil.getFormatDate(data.getGmtCreate(), "yyyy-MM-dd HH:mm"));
        mCloseTimeText.setText(DatePickerUtil.getFormatDate(data.getGiveOutEndTime(), "yyyy-MM-dd HH:mm"));
        mTicketCostText.setText(StringUtil.point2String(data.getCost()) + "元");
        mTicketAwardText.setText(StringUtil.point2String(data.getChargeOffAward()) + "元");
        if (data.getStock() != null && data.getStock() == -1) {
            mStoreNumText.setText("无限");
        } else {
            mStoreNumText.setText(String.valueOf(data.getStock()));
        }

        if (ticketData == null) return;

        if (ticketData.getCouponType() != null && ticketData.getCouponType() == 1) { //满减券
            mTicketValueLayout.setVisibility(View.VISIBLE);
            if (data.getStatus() == 1) {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_discount_abled_bg);
            } else {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_discount_disabled_bg);
            }
            mTicketDescText.setText("满减券" + ticketData.getRealAmount() + "元; 满" + ticketData.getLimitAmount() + "元可用");
        } else if (ticketData.getCouponType() != null && ticketData.getCouponType() == 2) { //代金券
            mTicketValueLayout.setVisibility(View.VISIBLE);
            if (data.getStatus() == 1) {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_golden_abled_bg);
            } else {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_golden_disabled_bg);
            }
            mTicketDescText.setText("代金券面值" + ticketData.getRealAmount() + "元");
        } else if (ticketData.getCouponType() != null && ticketData.getCouponType() == 3) { //凭证券
            mTicketValueLayout.setVisibility(View.GONE);
            if (data.getStatus() == 1) {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_certification_abled_bg);
            } else {
                mTicketLayout.setBackgroundResource(R.mipmap.ticket_certification_disabled_bg);
            }

            mTicketDescText.setText("此次是商品内容或者服务内容此次是商品");
        }


        mTicketNameText.setText(ticketData.getName());
        mTicketIdText.setText("卡券ID " + ticketData.getCouDefId());
        mTicketValueText.setText(String.valueOf(ticketData.getRealAmount()));
        mTicketDaysText.setText(ticketData.getMarketingMeta());

        if (ticketData.getValidityType() == 1) { //1固定天数
            mTicketExpireText.setText("领券" + ticketData.getEffectiveTime() + "天后可用,有效期" + ticketData.getValidityTime() + "天");
        } else if (ticketData.getValidityType() == 2) { //2 指定时间段
            mTicketExpireText.setText("有效期:" + DatePickerUtil.getFormatDate(ticketData.getValidityStartTime(), "yyyy.MM.dd")
                    + "-" + DatePickerUtil.getFormatDate(ticketData.getValidityEndTime(), "yyyy.MM.dd"));
        }


    }

    //更新卡券颜色
    private void updateCardColor() {
        mTicketDescText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketSignText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketNameText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketIdText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketValueText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketDaysText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
        mTicketExpireText.setTextColor(ContextCompat.getColor(this, R.color.text_color_gray_9));
    }

    @Override
    public void getTicketDetailFailed() {

    }

    @Override
    public void cancelTicketSuccess(Boolean data) {
        finish();
    }

    @Override
    public void cancelTicketFailed() {

    }

    @OnClick({R.id.cancel_release})
    public void onClick(View view) {
        switch (view.getId()) {
            //取消发券
            case R.id.cancel_release:
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认取消该卡券发布", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        //取消卡券发布
                        mPresenter.requestCancelTicket(coupon_give_out_id);
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

}
