package com.zishan.sardinemerchant.adapter.personal;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.wislie.rxjava.model.personal.MerchantBillDtoEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/5.
 * <p>
 * 入账金额适配器
 */

public class EntryMoneyAdapter extends BaseQuickAdapter<MerchantBillDtoEntity> {


    public EntryMoneyAdapter(int layoutResId, List<MerchantBillDtoEntity> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(BaseViewHolder holder, MerchantBillDtoEntity merchantBillDtoEntity) {
        Long billStartTimeStamp = merchantBillDtoEntity.getBillStartTimeStamp();//开始时间
        Long billEndTimeStamp = merchantBillDtoEntity.getBillEndTimeStamp();//结束时间
        Long clearRevenue = merchantBillDtoEntity.getClearRevenue();//入账金额是clearRevenue

        holder.setText(R.id.tv_time, DatePickerUtil.getFormatDate(billStartTimeStamp, "yyyy.MM.dd")
                + "~" + DatePickerUtil.getFormatDate(billEndTimeStamp, "yyyy.MM.dd"));

        holder.setText(R.id.entry_time, DatePickerUtil.getSpecifiedDayAfter(billEndTimeStamp,
                "yyyy.MM.dd"));//入账时间是billEndTimeStamp +1天
        holder.setText(R.id.entry_money, "+" + FormatMoneyUtil.pennyChangeDollar(clearRevenue));
    }
}

