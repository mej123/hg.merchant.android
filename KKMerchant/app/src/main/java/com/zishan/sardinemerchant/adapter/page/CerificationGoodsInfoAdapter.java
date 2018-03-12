package com.zishan.sardinemerchant.adapter.page;

import android.widget.TextView;

import com.example.wislie.rxjava.model.page.StoreOrderProductEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 红包核销详情的适配器
 * Created by wislie on 2017/11/3.
 */

public class CerificationGoodsInfoAdapter extends BaseQuickAdapter<StoreOrderProductEntity> {

    public CerificationGoodsInfoAdapter(int layoutResId, List<StoreOrderProductEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoreOrderProductEntity data) {
        TextView goodsNameText = holder.getView(R.id.goods_name);
        TextView goodsCountText = holder.getView(R.id.goods_count);

        goodsNameText.setText(data.getProductName());
        goodsCountText.setText(data.getNum() + data.getUnit());
    }
}
