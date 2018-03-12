package com.zishan.sardinemerchant.adapter.personal;

import android.view.View;

import com.example.wislie.rxjava.model.personal.BillParticularsDetailsEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/13.
 * <p>
 * 账单明细适配器
 */

public class BillParticularsAdapter extends BaseQuickAdapter<BillParticularsDetailsEntity> {
    private List<BillParticularsDetailsEntity> mdataList;

    public BillParticularsAdapter(int layoutResId, List<BillParticularsDetailsEntity> dataList) {
        super(layoutResId, dataList);
        mdataList = dataList;
    }

    @Override
    protected void convert(BaseViewHolder holder, BillParticularsDetailsEntity data) {
        if (data == null) return;
        int position = holder.getAdapterPosition();
        int currentposition = position - 1;
        String currentStr = data.getBillDay();
        String previewStr = currentposition >= 0 ? mdataList.get(
                currentposition).getBillDay() : "-1";
        if (!previewStr.equals(currentStr)) {
            holder.getView(R.id.tv_data).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_data, currentStr);
        } else {
            holder.getView(R.id.tv_data).setVisibility(View.GONE);
            holder.setText(R.id.tv_data, "");
        }

        if (data.getFirstLine()) {
            holder.getView(R.id.line).setVisibility(View.GONE);//去除每个分组第一个对象的分割线
        } else {
            holder.getView(R.id.line).setVisibility(View.VISIBLE);//去除每个分组第一个对象的分割线
        }
        Long bizTimestamp = data.getBizTimestamp();
        holder.setText(R.id.order_time, DatePickerUtil.stampToDate(String.valueOf(bizTimestamp)));//账单时间

        holder.setText(R.id.order_into_out, data.getTitle());

        Integer type = data.getType();//1:加,2:减
        Long revenue = data.getRevenue();//收入
        Long expenditure = data.getExpenditure();//支出
        if (type == 1) {
            holder.setText(R.id.tv_money, "+\t" + FormatMoneyUtil.pennyChangeDollar(revenue));//收益金额
        } else if (type == 2) {
            holder.setText(R.id.tv_money, "-\t" + FormatMoneyUtil.pennyChangeDollar(expenditure));//支出金额
        }
    }
}
