package com.zishan.sardinemerchant.adapter.page;

import android.widget.TextView;

import com.example.wislie.rxjava.model.page.CouponProductEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 扫码核销结果
 * Created by wislie on 2017/11/20.
 */

public class ScanCodeResultAdapter extends BaseQuickAdapter<CouponProductEntity> {


    public ScanCodeResultAdapter(int layoutResId, List<CouponProductEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CouponProductEntity data) {
        TextView goodsNameText = holder.getView(R.id.goods_name);
        TextView goodsNumText = holder.getView(R.id.goods_num);
        goodsNameText.setText(data.getProductName());
        goodsNumText.setText(data.getNum() + data.getUnit());
    }
}
