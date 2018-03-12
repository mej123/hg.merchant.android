package com.zishan.sardinemerchant.adapter.personal;

import android.view.View;

import com.example.wislie.rxjava.model.PositionManagerEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/10/29.
 * <p>
 * 个人  权限管理  适配器
 */

public class AddPositionAdapter extends BaseQuickAdapter<PositionManagerEntity> {

    public AddPositionAdapter(int layoutResId, List<PositionManagerEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, PositionManagerEntity data) {

        if (data == null) return;

        holder.getView(R.id.rl_item_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnItemListener != null) {
                    mOnItemListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });

        holder.getView(R.id.my_add_delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onDelete(holder.getLayoutPosition());
                }
            }
        });
        holder.setText(R.id.tv_add_position_name, data.getName());
    }
}
