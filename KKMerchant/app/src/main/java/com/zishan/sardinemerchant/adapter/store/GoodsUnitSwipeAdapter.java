package com.zishan.sardinemerchant.adapter.store;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.UnitData;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 菜品单位
 * Created by wislie on 2017/10/27.
 */

public class GoodsUnitSwipeAdapter extends BaseQuickAdapter<UnitData> {


    public GoodsUnitSwipeAdapter(int layoutResId, List<UnitData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, UnitData data) {
        TextView unitNameText = holder.getView(R.id.goods_unit_name);
        unitNameText.setText(data.getUnitName());
        Button deleteBtn = holder.getView(R.id.classify_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null){
                    mOnItemListener.onDelete(holder.getAdapterPosition());
                }
            }
        });
        ImageView checkImageView = holder.getView(R.id.goods_unit_check_icon);
        holder.getView(R.id.item_goods_unit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListener != null){
                    mOnItemListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });
        if(data.isSelected()){
            checkImageView.setImageResource(R.mipmap.checked_blue_icon);
        }else{
            checkImageView.setImageResource(R.mipmap.unchecked_icon);
        }
    }
}
