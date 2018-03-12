package com.zishan.sardinemerchant.adapter.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

import static android.R.attr.data;

/**
 * 先吃后付桌号的适配器
 * Created by wislie on 2017/9/20.
 */

public class EatBeforePayAdapter extends BaseQuickAdapter<StoreSeatEntity> {


    public EatBeforePayAdapter(int layoutResId, List<StoreSeatEntity> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, StoreSeatEntity data) {

        TextView titleText = holder.getView(R.id.table_pandect_title);
        titleText.setVisibility(View.GONE);

        //桌台编号
        TextView tableNumText = holder.getView(R.id.table_name);
        tableNumText.setText(String.valueOf(data.getSeatName()));
        //下单时间
        long startTime = data.getStartTime();
        ImageView circleView = holder.getView(R.id.order_tag);
        //桌台总览状态
        TextView tableStateText = holder.getView(R.id.tv_table_pandect_state);

        //时间
        TextView orderTimeText = holder.getView(R.id.order_time);
        orderTimeText.setText(DatePickerUtil.getLongFormatTime(startTime));

        //价格
        TextView orderPriceText = holder.getView(R.id.order_price);
        orderPriceText.setText(StringUtil.point2String(data.getTotalMoney()) + "元");

        LinearLayout orderTimeLayout = holder.getView(R.id.pandect_time_layout);
        LinearLayout orderPriceLayout = holder.getView(R.id.pandect_amount_layout);


        orderTimeLayout.setVisibility(View.VISIBLE);
        orderPriceLayout.setVisibility(View.VISIBLE);

        //桌台状态 0新订单，1就餐中，2 闲置中 3已买单
        if (data.getState() == Constant.NEW_ORDER) {
            tableStateText.setText("新订单");
            tableStateText.setBackgroundResource(R.drawable.table_pandect_shape_green);
            circleView.setImageResource(R.mipmap.green_circle_icon);
        } else if (data.getState() == Constant.AT_MEAL) {
            tableStateText.setText("就餐中");
            tableStateText.setBackgroundResource(R.drawable.table_pandect_shape_yellow);
            circleView.setImageResource(R.mipmap.orange_circle_icon);
        } else if (data.getState() == Constant.EMPTY_ORDER) {
            tableStateText.setText("闲置中");
            tableStateText.setBackgroundResource(R.drawable.table_pandect_shape_coffee);
            circleView.setImageResource(R.mipmap.coffee_circle_icon);
            orderTimeLayout.setVisibility(View.INVISIBLE);
            orderPriceLayout.setVisibility(View.INVISIBLE);
        } else if (data.getState() == Constant.PAID_ORDER) {
            tableStateText.setText("已买单");
            tableStateText.setBackgroundResource(R.drawable.table_pandect_shape_blue);
            circleView.setImageResource(R.mipmap.blue_circle_icon);
        }


    }
}
