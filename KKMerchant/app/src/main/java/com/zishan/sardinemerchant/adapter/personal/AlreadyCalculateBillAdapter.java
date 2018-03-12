package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.personal.AlreadyListEntity;
import com.example.wislie.rxjava.model.personal.DayBillsEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/13.
 * <p>
 * 已结算列表适配器
 */

public class AlreadyCalculateBillAdapter extends BaseQuickAdapter<AlreadyListEntity> {


    public AlreadyCalculateBillAdapter(int layoutResId, List<AlreadyListEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlreadyListEntity data) {

        if (data == null) return;
        Long billStartTimeStamp = data.getBillStartTimeStamp();//每个条目的开始时间
        Long billEndTimeStamp = data.getBillEndTimeStamp();//每个条目的开始时间
        holder.setText(R.id.tv_start_time, DatePickerUtil.getFormatDate(billStartTimeStamp,"yyyy.MM.dd"));//开始日期
        holder.setText(R.id.tv_end_time, DatePickerUtil.getFormatDate(billEndTimeStamp,"yyyy.MM.dd"));//结束日期

        List<DayBillsEntity> dayBills = data.getDayBills();

        if (dayBills == null) return;


        if (dayBills.size() == 1) {
            DayBillsEntity startEntity = dayBills.get(0);
            if (startEntity != null) {
                holder.setText(R.id.tv_money, FormatMoneyUtil.pennyChangeDollar(startEntity.getClearRevenue()));//开始日期收益
                holder.setText(R.id.tv_money_second,"");//结束日期收益
            }
        }
        if (dayBills.size() == 2) {
            DayBillsEntity startEntity = dayBills.get(0);
            DayBillsEntity endEntity = dayBills.get(1);
            if (startEntity != null) {
                holder.setText(R.id.tv_money, FormatMoneyUtil.pennyChangeDollar(startEntity.getClearRevenue()));//开始日期收益
            }
            if (endEntity != null) {
                holder.setText(R.id.tv_money_second, FormatMoneyUtil.pennyChangeDollar(endEntity.getClearRevenue()));//结束日期收益
            }
        }
        holder.setText(R.id.tv_entry_money,FormatMoneyUtil.pennyChangeDollar(data.getClearRevenue()));//两天总收益
    }
}

