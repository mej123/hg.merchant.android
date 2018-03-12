package com.zishan.sardinemerchant.adapter.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.UnitData;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 菜品单位
 * Created by wislie on 2017/10/27.
 */

public class GoodsUnitAdapter extends BaseQuickAdapter<UnitData> {

    public GoodsUnitAdapter(int layoutResId, List<UnitData> data) {
        super(layoutResId, data);
    }

    /*   public GoodsUnitAdapter(Context context, List<UnitData> objects) {
            super(context, objects);

        }
    */
   /* public int getItemLayoutId() {
        return R.layout.item_goods_unit;
    }*/



    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {

        return new BaseViewHolder(mContext, getItemView(layoutResId, parent));


    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, UnitData data) {
        TextView unitNameText = holder.getView(R.id.goods_unit_name);
        unitNameText.setText(data.getUnitName());
        ImageView checkImageView = holder.getView(R.id.goods_unit_check_icon);
        /*checkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListener != null){
                    mOnItemListener.onSelected(holder.getAdapterPosition());
                }
            }
        });*/

        if(data.isSelected()){
            checkImageView.setImageResource(R.mipmap.checked_blue_icon);
        }else{
            checkImageView.setImageResource(R.mipmap.unchecked_icon);
        }
    }


}
