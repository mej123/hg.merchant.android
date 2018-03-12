package com.zishan.sardinemerchant.adapter.store;


import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;


import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 招牌 适配器
 * Created by wislie on 2017/9/20.
 */

public class GoodsBrandAdapter extends BaseQuickAdapter<ProductEntity> {


    public GoodsBrandAdapter(int layoutResId, List<ProductEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity data) {

        ImageView goodsIcon = holder.getView(R.id.goods_icon);
        GlideLoader.getInstance().load(mContext, goodsIcon, StringUtil.appendHttps(data.getLogoPicUrl()));
        TextView goodsNameText = holder.getView(R.id.goods_name);
        goodsNameText.setText(data.getName());
        TextView goodsPriceText = holder.getView(R.id.goods_price);
        if (data.getDiscount() != null && data.getDiscount() == Boolean.TRUE) {
            goodsPriceText.setText("¥ " + StringUtil.point2String(data.getStrategyPrice()));
        } else {
            goodsPriceText.setText("¥ " + StringUtil.point2String(data.getRealPrice()));
        }
    }
}
