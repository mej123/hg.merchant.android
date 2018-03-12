package com.zishan.sardinemerchant.activity.personal.accounts;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;

import static com.zishan.sardinemerchant.R.id.rl_remark;

/**
 * Created by yang on 2017/11/5.
 * <p>
 * 提现明细详情
 */

public class WithdrawDetailParticularsActivity extends BActivity {
    @BindView(R.id.iv_commit)
    ImageView mCommitIcon;//提交图片
    @BindView(R.id.tv_commit)
    TextView mCommit;//提交
    @BindView(R.id.iv_audit)
    ImageView mAuditIcon;//审核图片
    @BindView(R.id.tv_audit)
    TextView mAudit;//审核
    @BindView(R.id.iv_remittanced)
    ImageView mRemittancedIcon;//已打款图片
    @BindView(R.id.tv_remittanced)
    TextView mRemittanced;//已打款
    @BindView(R.id.iv_remittance)
    ImageView mRemittanceIcon;//打款图片
    @BindView(R.id.tv_remittance)
    TextView mRemittance;//打款
    @BindView(R.id.tv_withdraw_amount)
    TextView mWithdrawAmount;//提现金额
    @BindView(R.id.withdraw_time)
    TextView mWithdrawTime;
    @BindView(R.id.pay_time)
    TextView mPayTime;
    @BindView(R.id.withdraw_bank)
    TextView mWithdrawBank;
    @BindView(R.id.flow_id)
    TextView mFlowId;
    @BindView(R.id.remark_content)
    TextView mRemarkContent;
    @BindView(rl_remark)
    RelativeLayout mRemark;
    @BindView(R.id.rl_pay_time)
    RelativeLayout rlPayTime;
    @BindView(R.id.rl_flow)
    RelativeLayout rlFlow;
    @BindView(R.id.rl_withdraw_bank_layout)
    RelativeLayout mWithdrawBankLayout;
    private Bundle lastWithdraw;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.withdraw_detail));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw_detail_particulars;
    }

    @Override
    protected void initContentView() {

        lastWithdraw = getIntent().getBundleExtra("lastWithdraw");
        if (lastWithdraw == null) return;

        int withdrawStatus = lastWithdraw.getInt("withdrawStatus");

        //最近提现状态
        if (withdrawStatus == 1)//1:审批中
        {
            mCommitIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mCommit.setText("提交成功");

            mAuditIcon.setImageResource(R.mipmap.checking_icon);
            mAudit.setText("审核中");

            mRemittancedIcon.setImageResource(R.mipmap.unchecked_icon);
            mRemittanced.setText("已打款");

            mRemittanceIcon.setImageResource(R.mipmap.unchecked_icon);
            mRemittance.setText("打款成功");
            setShowReviewMsg();//统一设置审核状态时的显示信息

        } else if (withdrawStatus == 2)//2:打款成功
        {
            mCommitIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mCommit.setText("提交成功");

            mAuditIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mAudit.setText("审核成功");

            mRemittancedIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mRemittanced.setText("已打款");

            mRemittanceIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mRemittance.setText("打款成功");

            setShowPayMsg();//统一设置打款状态时的显示信息

        } else if (withdrawStatus == 3)//3:打款失败

        {
            mCommitIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mCommit.setText("提交成功");

            mAuditIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mAudit.setText("审核成功");

            mRemittancedIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mRemittanced.setText("已打款");

            mRemittanceIcon.setImageResource(R.mipmap.failure_icon);
            mRemittance.setText("打款失败");

            setShowPayMsg();//统一设置打款状态时的显示信息

        } else if (withdrawStatus == 4)//4:已打款

        {
            mCommitIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mCommit.setText("提交成功");

            mAuditIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mAudit.setText("审核成功");

            mRemittancedIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mRemittanced.setText("已打款");

            mRemittanceIcon.setImageResource(R.mipmap.unchecked_icon);
            mRemittance.setText("打款成功");
            setShowPayMsg();//统一设置打款状态时的显示信息

        } else if (withdrawStatus == 5) {//5:审核失败
            mCommitIcon.setImageResource(R.mipmap.bind_card_check_icon);
            mCommit.setText("提交成功");

            mAuditIcon.setImageResource(R.mipmap.failure_icon);
            mAudit.setText("审核失败");

            mRemittancedIcon.setImageResource(R.mipmap.unchecked_icon);
            mRemittanced.setText("已打款");

            mRemittanceIcon.setImageResource(R.mipmap.unchecked_icon);
            mRemittance.setText("打款失败");
            setShowReviewMsg();//统一设置审核状态时的显示信息
            setRemark();//设置备注
        }

    }

    private void setShowReviewMsg() {

        rlPayTime.setVisibility(View.GONE);//无打款时间字段
        mWithdrawBankLayout.setBackgroundColor(ContextCompat.getColor(WithdrawDetailParticularsActivity.this
                , R.color.commit_audit));

        rlFlow.setBackgroundColor(ContextCompat.getColor(WithdrawDetailParticularsActivity.this
                , R.color.bg_while));

        commonShowMsg();//相同的显示信息
    }

    private void commonShowMsg() {

        String number = lastWithdraw.getString("number");//提现金额
        mWithdrawAmount.setText(number);//提现金额
        long withdrawTime = lastWithdraw.getLong("withdrawTime");//提现时间

        // mWithdrawTime.setText(DatePickerUtil.stampToDate(String.valueOf(withdrawTime)));//提现时间
        mWithdrawTime.setText(DatePickerUtil.getFormatDate(withdrawTime, "yyyy.MM.dd   HH:mm:ss"));//提现时间
        Long flowId = lastWithdraw.getLong("flowId");//流水编号
        mFlowId.setText(String.valueOf(flowId));

        String bankCardNo = lastWithdraw.getString("bankCardNo");
        String lastFour = bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length());
        String withdrawBank = lastWithdraw.getString("withdrawBank");//提现银行
        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            if (String.valueOf(bankCode).equals(withdrawBank)) {
                String bankName = bankNameList[i];
                mWithdrawBank.setText(bankName + "(" + lastFour + ")");//银行卡+卡号后四位
            }
        }
    }

    private void setShowPayMsg() {
        long payTime = lastWithdraw.getLong("payTime");//打款时间
        mPayTime.setText(DatePickerUtil.stampToDate(String.valueOf(payTime)));
        commonShowMsg();//相同的显示信息
    }

    private void setRemark() {
        mRemark.setVisibility(View.VISIBLE);//有备注
        mRemarkContent.setText(lastWithdraw.getString("remark"));
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    String[] bankNameList = new String[]{"中国工商银行", "中国农业银行", "中国银行", "中国建设银行",
            " 交通银行", "中信银行", "中国光大银行", "华夏银行", "中国民生银行", " 广东发展银行",
            "招商银行", "兴业银行", "上海浦东发展银行",
            "徽商银行", "中国邮政储蓄银行",
    };

    final int[] bankCodeList = new int[]{102, 103, 104, 105, 301, 302,
            303, 304, 305, 306, 308, 309, 310, 319, 403};
}
