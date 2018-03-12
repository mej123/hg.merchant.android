package com.zishan.sardinemerchant.adapter.personal;

import android.content.Context;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.LastWithdrawEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/9.
 * <p>
 * 提现  最近提现列表适配器
 */

public class LastWithdrawRecordAdapter extends BaseQuickAdapter<LastWithdrawEntity> {

    private TextView withdrawState;
    private Context mContext;

    public LastWithdrawRecordAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder holder, LastWithdrawEntity data) {

        if (data == null) return;
        TextView bankNameAndCardNumber = holder.getView(R.id.tv_bank_name_and_card_number);//银行和卡号后四位
        TextView expenditureMoney = holder.getView(R.id.expenditure_money);//交易数量
        //提现状态
        withdrawState = holder.getView(R.id.tv_withdraw_state);
        // TextView expenditureTime = holder.getView(R.id.tv_expenditure_time);//提现时间
        expenditureMoney.setText(FormatMoneyUtil.pennyChangeDollar(data.getNum())); //最近提现金额

        String bankCardNo = data.getBankCardNo();//提现银行卡号
        String bankTypeCode = data.getBankTypeCode();//银行卡类型
        String lastFour = bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length());
        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            if (bankTypeCode.equals(String.valueOf(bankCode))) {
                String bankName = bankNameList[i];
                bankNameAndCardNumber.setText(bankName + "(" + lastFour + ")");//银行卡+卡号后四位
            }
        }

        showWithdrawState(holder, data);//提现状态
        showTime(holder, data);//显示时间
    }

    private void showWithdrawState(BaseViewHolder holder, LastWithdrawEntity data) {

        //审批状态:   1未审批 2审批中 3审批结束 4审批失败 5不可用
        Integer approvalState = data.getApprovalState();
        Integer payState = data.getPayState();
        if (payState == 4) {
            withdrawState.setText("打款失败");
            withdrawState.setTextColor(mContext.getResources().getColor(R.color.red));
            withdrawState.setBackgroundResource(R.drawable.new_withdraw_red_bg_shape);
        }
        if (approvalState == 2) {
            withdrawState.setText("审核中");
            withdrawState.setTextColor(mContext.getResources().getColor(R.color.yellow));
            withdrawState.setBackgroundResource(R.drawable.new_withdraw_yellow_bg_shape);
        } else if (approvalState == 3) {
            if (payState == 2) {
                withdrawState.setText("已打款");
                withdrawState.setTextColor(mContext.getResources().getColor(R.color.yellow));
                withdrawState.setBackgroundResource(R.drawable.new_withdraw_yellow_bg_shape);
                return;
            }
            withdrawState.setText("打款成功");
            withdrawState.setTextColor(mContext.getResources().getColor(R.color.green));
            withdrawState.setBackgroundResource(R.drawable.new_withdraw_green_bg_shape);
        } else if (approvalState == 4) {
            withdrawState.setText("审核失败");
            withdrawState.setTextColor(mContext.getResources().getColor(R.color.red));
            withdrawState.setBackgroundResource(R.drawable.new_withdraw_red_bg_shape);
        }
    }

    private void showTime(BaseViewHolder holder, LastWithdrawEntity data) {
        Long withdrawTime = data.getWithdrawTime();
        //将提现时间转化成年月日格式
        String withdrawTimeYMD = DatePickerUtil.getFormatDate(withdrawTime, "yyyy-MM-dd");//得到当前提现时间的格式为年月日
        //获取系统的今天和明天的年月日格式的日期,然后比对判断属于今天还是昨天
        //今日
        Long todayTime = DatePickerUtil.getEndTime(0);
        //昨日
        //Long yesterdayTime = DatePickerUtil.getEndTime(-1);

        //将时间转化为指定格式
        String todayYMD = DatePickerUtil.getFormatDate(todayTime, "yyyy-MM-dd");//年月日  今天

        //String yesterdayYMD = DatePickerUtil.getFormatDate(yesterdayTime, "yyyy-MM-dd");//年月日  昨天

        String withdrawHMS = DatePickerUtil.getFormatDate(withdrawTime, "HH:mm");//提现时间 时分秒

        if (withdrawTimeYMD.equals(todayYMD)) {

            holder.setText(R.id.tv_expenditure_time, "今天\t\t" + withdrawHMS);

        } else {
            holder.setText(R.id.tv_expenditure_time, DatePickerUtil.getFormatDate(withdrawTime, "yyyy-MM-dd   HH:mm"));//完整格式时间
        }
    }


    String[] bankNameList = new String[]{"中国工商银行", "中国农业银行", "中国银行", "中国建设银行",
            " 交通银行", "中信银行", "中国光大银行", "华夏银行", "中国民生银行", " 广东发展银行",
            "招商银行", "兴业银行", "上海浦东发展银行", "徽商银行", "中国邮政储蓄银行",
    };

    final int[] bankCodeList = new int[]{102, 103, 104, 105, 301, 302,
            303, 304, 305, 306, 308, 309, 310, 319, 403};
}
