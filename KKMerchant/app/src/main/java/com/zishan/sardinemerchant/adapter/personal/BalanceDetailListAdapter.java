package com.zishan.sardinemerchant.adapter.personal;

import android.graphics.Color;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.BalanceRecordDetailsEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/9.
 * <p>
 * 个人  我的账户   余额明细列表适配器
 */

public class BalanceDetailListAdapter extends BaseQuickAdapter<BalanceRecordDetailsEntity> {

    public BalanceDetailListAdapter(int layoutResId, List<BalanceRecordDetailsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BalanceRecordDetailsEntity data) {
        if (data == null) return;
        TextView bankNameAndCardNumber = holder.getView(R.id.tv_bank_name_and_card_number);//银行和卡号后四位
        TextView expenditureMoney = holder.getView(R.id.expenditure_money);//交易数量
        TextView currentMoney = holder.getView(R.id.tv_current_money);//当次结算商户余额
        // TextView expenditureTime = holder.getView(R.id.tv_expenditure_time);//交易时间

        bankNameAndCardNumber.setText(data.getTitle());

        Integer type = data.getType();//增/减标识(1增,2减)
        if (type == 1) {
            expenditureMoney.setText("+" + FormatMoneyUtil.pennyChangeDollar(data.getNum()));
            expenditureMoney.setTextColor(Color.parseColor("#1784ff"));
        } else if (type == 2) {
            expenditureMoney.setText("-" + FormatMoneyUtil.pennyChangeDollar(data.getNum()));
        }
        currentMoney.setText(FormatMoneyUtil.pennyChangeDollar(data.getCurrentBalance()));

        showTime(holder, data);//显示时间
    }

    private void showTime(BaseViewHolder holder, BalanceRecordDetailsEntity data) {
        Long withdrawTime = data.getTimeStamp();
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
}
