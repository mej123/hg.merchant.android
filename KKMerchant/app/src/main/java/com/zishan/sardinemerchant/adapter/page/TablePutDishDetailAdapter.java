package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseMultiItemQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 已出菜/未出菜
 * Created by wislie on 2017/12/12.
 */

public class TablePutDishDetailAdapter extends BaseMultiItemQuickAdapter<TableMenuItemEntity> {

    //可以滑动的
    public static final int TYPE_SWIPE = 1;
    //正常的
    public static final int TYPE_NORMAL = 2;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public TablePutDishDetailAdapter(List<TableMenuItemEntity> data) {
        super(data);
        addItemType(TYPE_SWIPE, R.layout.item_put_dish_detail_swipe);
        addItemType(TYPE_NORMAL, R.layout.item_put_dish_detail);
    }


    @Override
    protected void convert(final BaseViewHolder holder, TableMenuItemEntity data) {
        TextView dishNameText = holder.getView(R.id.dish_name);
        TextView dishPriceText = holder.getView(R.id.dish_price);
        TextView dishTotalPriceText = holder.getView(R.id.dish_total_price);
        TextView dishNumText = holder.getView(R.id.dish_num);
        TextView dishStatusText = holder.getView(R.id.dish_status);

        dishNameText.setText(data.getProductName());
        dishPriceText.setText("单价: " + StringUtil.point2String(data.getValuationPrice()));
        dishTotalPriceText.setText("总价: " + StringUtil.point2String(data.getValuationPrice() * data.getNum()));
        dishNumText.setText("×" + data.getNum());
        if (data.getState() < 3) {
            dishStatusText.setText("未出菜");
            dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_4));
        } else {

            dishStatusText.setText("已出菜");
            dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_blue));

        }

        if (holder.getItemViewType() == TYPE_SWIPE) {

            Button putDishBtn = holder.getView(R.id.put_dish);
            putDishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onSelected(holder.getLayoutPosition());
                    }
                }
            });
        }


    }
}
