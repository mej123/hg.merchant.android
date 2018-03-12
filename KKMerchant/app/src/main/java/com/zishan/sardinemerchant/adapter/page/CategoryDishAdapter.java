package com.zishan.sardinemerchant.adapter.page;

import android.graphics.Color;
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
 * 点菜显示菜品
 * Created by wislie on 2017/12/6.
 */

public class CategoryDishAdapter extends BaseQuickAdapter<ProductEntity> {

    public CategoryDishAdapter(int layoutResId, List<ProductEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity data) {

        ImageView dishIcon = helper.getView(R.id.dish_icon);
        TextView dishNameText = helper.getView(R.id.dish_name);
        TextView dishPriceText = helper.getView(R.id.dish_price);
        TextView dishCountText = helper.getView(R.id.dish_count);

        dishNameText.setText(data.getName());
        dishPriceText.setText("¥ " + StringUtil.point2String(data.getRealPrice()));

        GlideLoader.getInstance().load(mContext, dishIcon, StringUtil.appendHttps(data.getLogoPicUrl()));

        if (data.getSelectedCount() != null && data.getSelectedCount() > 0) {
            if (data.getSelectedCount() < 10) {
                dishCountText.setText(String.valueOf(data.getSelectedCount()));
                dishCountText.setBackgroundResource(R.mipmap.choose_dish_right_one_icon);
            } else {
                dishCountText.setText(String.valueOf(data.getSelectedCount()));
                dishCountText.setBackgroundResource(R.mipmap.choose_dish_right_three_icon);
            }
        } else {
            dishCountText.setText("");
            dishCountText.setBackgroundColor(Color.WHITE);
        }


    }
}
