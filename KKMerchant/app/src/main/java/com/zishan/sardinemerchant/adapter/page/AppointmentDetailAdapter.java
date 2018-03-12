package com.zishan.sardinemerchant.adapter.page;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.OrderDishItemBean;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;


/**
 * 预约详情
 * Created by wislie on 2017/11/15.
 */

public class AppointmentDetailAdapter extends BaseQuickAdapter<OrderDishItemBean> {


    public AppointmentDetailAdapter(int layoutResId, List<OrderDishItemBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, OrderDishItemBean data) {


        ImageView icon = holder.getView(R.id.iv_icon);
        TextView titleText = holder.getView(R.id.tv_title);
        TextView priceText = holder.getView(R.id.tv_price);
        TextView numText = holder.getView(R.id.tv_number);

        titleText.setText(data.getProductName());
        priceText.setText("¥ " + data.getPrice());
        numText.setText(String.valueOf(data.getNum()));

        if (icon != null) {
            GlideLoader.getInstance().load(mContext, icon,
                    data.getPicUrl(), R.mipmap.avatar_placeholder_icon, true);
        }

    }
}
