package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.personal.ExpectDayBillsEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/13.
 *
 * 账单   待结算账单  适配器
 *
 */

public class BillAdapter extends BaseQuickAdapter<ExpectDayBillsEntity> {


    public BillAdapter(int layoutResId, List<ExpectDayBillsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExpectDayBillsEntity data) {

        if (data==null)return;
        holder.setText(R.id.tv_wait_time, data.getBillDay());
        Long clearRevenue = data.getClearRevenue();
        holder.setText(R.id.tv_money, FormatMoneyUtil.pennyChangeDollar(clearRevenue));
    }
}
