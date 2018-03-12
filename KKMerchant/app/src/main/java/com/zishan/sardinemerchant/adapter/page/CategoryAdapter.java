package com.zishan.sardinemerchant.adapter.page;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 点菜的tab栏
 * Created by wislie on 2017/12/6.
 */

public class CategoryAdapter extends BaseQuickAdapter<ProductGroupEntity> {

    private int selectedPos = 0;

    public CategoryAdapter(int layoutResId, List<ProductGroupEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductGroupEntity data) {

        TextView categoryNameText = helper.getView(R.id.category_name);
        categoryNameText.setText(data.getName());

        TextView categoryCountText = helper.getView(R.id.category_count);
        if (data.getSelectedCount() != null && data.getSelectedCount() > 0) {
            if (data.getSelectedCount() < 10) {
                categoryCountText.setText(String.valueOf(data.getSelectedCount()));
                categoryCountText.setBackgroundResource(R.mipmap.choose_dish_left_one_icon);
            } else {
                categoryCountText.setText(String.valueOf(data.getSelectedCount()));
                categoryCountText.setBackgroundResource(R.mipmap.choose_dish_left_three_icon);
            }
        } else {
            categoryCountText.setText("");
        }

        RelativeLayout categoryLayout = helper.getView(R.id.dish_name_layout);
        if (helper.getAdapterPosition() == selectedPos) {
            categoryLayout.setSelected(true);
        } else {
            categoryLayout.setSelected(false);
        }

    }

    //设置选中的分类
    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }


}
