package com.zishan.sardinemerchant.adapter.page;


import android.util.Log;
import android.widget.TextView;

import com.example.wislie.rxjava.model.MakeDishEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 先付后吃桌号的适配器
 * Created by wislie on 2017/9/20.
 */

public class PayBeforeEatAdapter extends BaseQuickAdapter<MakeDishEntity> {

    private boolean is_out_dish;

    public PayBeforeEatAdapter(int layoutResId, List<MakeDishEntity> data, boolean is_out_dish) {
        super(layoutResId, data);
        this.is_out_dish = is_out_dish;
    }

    @Override
    protected void convert(BaseViewHolder holder, MakeDishEntity data) {

        TextView tableNameText = holder.getView(R.id.table_name);
        TextView orderNumText = holder.getView(R.id.order_num);
        TextView typeText = holder.getView(R.id.type_icon);
        TextView orderCountText = holder.getView(R.id.output_order_count);
        TextView orderTimeText = holder.getView(R.id.order_time);
        TextView orderPriceText = holder.getView(R.id.order_price);

        tableNameText.setText(data.getSeatName());
        orderNumText.setText(String.valueOf(data.getSerialNum()));
        if (!is_out_dish) {
            orderCountText.setText("未出菜品" + data.getInitBespeakCount() + "件");
        } else {
            orderCountText.setText("");
        }
        orderTimeText.setText(DatePickerUtil.getFormatDate(data.getFirstOrderTime(), "HH小时mm分钟"));
        orderPriceText.setText(StringUtil.point2String(data.getTotalMoney()) + "元");
        switch (data.getType()) {
            case Constant.HALL:
                typeText.setBackgroundResource(R.mipmap.hall_icon);
                typeText.setText("大厅");
                break;
            case Constant.CARD:
                typeText.setBackgroundResource(R.mipmap.card_icon);
                typeText.setText("卡座");
                break;
            case Constant.BOX:
                typeText.setBackgroundResource(R.mipmap.box_icon);
                typeText.setText("包厢");
                break;
        }

    }
}
