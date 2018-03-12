package com.zishan.sardinemerchant.adapter.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.SelectedDishBean;

import java.util.List;

import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 临时菜品
 * Created by wislie on 2017/12/7.
 */

public class ExtraDishAdapter extends BaseQuickAdapter<SelectedDishBean> {

    //默认展开
    private boolean isExpanded = true;
    public ExtraDishAdapter(int layoutResId, List<SelectedDishBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectedDishBean item) {
        TextView dishTitle = helper.getView(R.id.extra_dish_title);
        TextView dishNameText = helper.getView(R.id.extra_dish_name);
        TextView dishNumText = helper.getView(R.id.extra_dish_num);
        TextView dishPriceText = helper.getView(R.id.extra_dish_price);
        ImageView dishIcon = helper.getView(R.id.dish_icon);

        dishNameText.setText(item.getProductName());
        dishNumText.setText(String.valueOf(item.getNum()));
        dishPriceText.setText("¥ " + StringUtil.point2String(item.getPrice()));

        if (helper.getAdapterPosition() != 0) {
            dishTitle.setVisibility(View.GONE);
            dishIcon.setVisibility(View.GONE);
        } else {
            dishTitle.setVisibility(View.VISIBLE);
            dishIcon.setVisibility(View.VISIBLE);
            if(!isExpanded){
                dishIcon.setImageResource(R.mipmap.arrow_towards_top_icon);
            }else{
                dishIcon.setImageResource(R.mipmap.arrow_towards_bottom_icon);
            }

        }
        dishIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(isExpanded){
            return super.getItemCount();
        }
        return super.getItemCount() > 1 ? 1 : super.getItemCount();
    }
}
