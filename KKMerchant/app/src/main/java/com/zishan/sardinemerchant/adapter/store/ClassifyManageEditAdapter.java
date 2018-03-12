package com.zishan.sardinemerchant.adapter.store;

import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 分类管理的适配器
 * Created by wislie on 2017/9/20.
 */

public class ClassifyManageEditAdapter extends BaseQuickAdapter<ProductGroupEntity> {


    public ClassifyManageEditAdapter(int layoutResId, List<ProductGroupEntity> data) {
        super(layoutResId, data);
    }




    @Override
    protected void convert(BaseViewHolder holder, ProductGroupEntity data) {

        TextView classifyNameText = holder.getView(R.id.classify_name);
        classifyNameText.setText(data.getName());
        TextView classifyCountText = holder.getView(R.id.classify_goods_count);
        int productNum = data.getProductNum() == null ? 0 : data.getProductNum();
        classifyCountText.setText(" 共" + productNum + "件商品");
    }
}
