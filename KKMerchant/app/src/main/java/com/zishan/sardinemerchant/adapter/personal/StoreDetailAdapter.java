package com.zishan.sardinemerchant.adapter.personal;

import android.graphics.Paint;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.MerchantOrderItemEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseMultiItemQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 订单详情
 * Created by wislie on 2017/11/20.
 */

public class StoreDetailAdapter extends BaseMultiItemQuickAdapter<MerchantOrderItemEntity> {

    //没有优惠的
    public static final int TYPE_NO_DISCOUNT = 1;
    //有优惠的
    public static final int TYPE_DISCOUNT = 2;

    public StoreDetailAdapter(List<MerchantOrderItemEntity> data) {
        super(data);
        addItemType(TYPE_NO_DISCOUNT, R.layout.item_order_detail);
        addItemType(TYPE_DISCOUNT, R.layout.item_order_multi_detail);
    }


    @Override
    protected void convert(BaseViewHolder holder, MerchantOrderItemEntity data) {
        TextView goodsNameText = holder.getView(R.id.goods_name);
        TextView goodsNumText = holder.getView(R.id.goods_num);
        TextView goodsPriceText = holder.getView(R.id.goods_sale_price);


        goodsNameText.setText(data.getProductName());
        goodsNumText.setText("×" + data.getNum());
        goodsPriceText.setText("¥ " + data.getRealPrice() / 100);

        if (holder.getItemViewType() == TYPE_DISCOUNT) {
            TextView goodsOriginalPriceText = holder.getView(R.id.goods_original_price);
            goodsOriginalPriceText.setText("¥ " + data.getPrice());
            goodsOriginalPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
