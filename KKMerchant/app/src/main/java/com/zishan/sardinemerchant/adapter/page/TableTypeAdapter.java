package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableBoxEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

import static android.R.attr.data;

/**
 * 桌台类型(大厅，包厢，卡座等)
 * Created by wislie on 2017/11/6.
 */

public class TableTypeAdapter extends BaseQuickAdapter<TableBoxEntity> {


    public TableTypeAdapter(int layoutResId, List<TableBoxEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, TableBoxEntity data) {
        RelativeLayout layout = holder.getView(R.id.table_type_layout);

        TextView tableNameText = holder.getView(R.id.table_name);
        tableNameText.setText(data.getSeatName());
        TextView tablePersonCountText = holder.getView(R.id.table_person_count);
        tablePersonCountText.setText(data.getSeatNum()+"人");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListener != null){
                    mOnItemListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        Button deleteBtn = holder.getView(R.id.table_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListener != null){
                    mOnItemListener.onDelete(holder.getAdapterPosition());
                }
            }
        });
    }
}
