package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.personal.WithdrawEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 提现明细  适配器
 */

public class WithdrawListAdapter extends BaseQuickAdapter<WithdrawEntity> {


    public WithdrawListAdapter(int layoutResId, List<WithdrawEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, WithdrawEntity withdrawEntity) {

        if (withdrawEntity == null) return;
        //审批状态:   1未审批 2审批中 3审批结束 4审批失败 5不可用
        Integer approvalState = withdrawEntity.getApprovalState();
        Integer payState = withdrawEntity.getPayState();
        if (approvalState == 2) {
            holder.setText(R.id.withdraw_state, "审核中");
            holder.getView(R.id.withdraw_state).setBackgroundResource(R.drawable.my_account_shape_yellow);
        } else if (approvalState == 3) {
            if (payState == 3) {
            } else if (payState == 4) {
                holder.setText(R.id.withdraw_state, "失败");
                holder.getView(R.id.withdraw_state).setBackgroundResource(R.drawable.my_account_shape_red);

            } else if (payState == 2) {
            }
        } else if (approvalState == 4) {
            holder.setText(R.id.withdraw_state, "失败");
            holder.getView(R.id.withdraw_state).setBackgroundResource(R.drawable.my_account_shape_red);
        }

        Long withdrawTime = withdrawEntity.getWithdrawTime();
        //将提现时间转化成年月日格式
        String withdrawTimeYMD = DatePickerUtil.getFormatDate(withdrawTime, "yyyy-MM-dd");//得到当前提现时间的格式为年月日

        //获取系统的今天和明天的年月日格式的日期,然后比对判断属于今天还是昨天

        //今日
        Long todayTime = DatePickerUtil.getEndTime(0);
        //昨日
        Long yesterdayTime = DatePickerUtil.getEndTime(-1);

        //将时间转化为指定格式
        String todayYMD = DatePickerUtil.getFormatDate(todayTime, "yyyy-MM-dd");//年月日  今天

        String yesterdayYMD = DatePickerUtil.getFormatDate(yesterdayTime, "yyyy-MM-dd");//年月日  昨天

        String withdrawHMS = DatePickerUtil.getFormatDate(withdrawTime, "HH:mm:ss");//提现时间 时分秒

        if (withdrawTimeYMD.equals(todayYMD)) {

            holder.setText(R.id.withdraw_time, "今日\t\t" + withdrawHMS);

        } else if (withdrawTimeYMD.equals(yesterdayYMD)) {

            holder.setText(R.id.withdraw_time, "昨日\t\t" + withdrawHMS);
        } else {
            holder.setText(R.id.withdraw_time, DatePickerUtil.stampToDate(String.valueOf(withdrawTime)));//完整格式时间
        }

        holder.setText(R.id.tv_money, FormatMoneyUtil.pennyChangeDollar(withdrawEntity.getNum()) + "元");
    }
}
