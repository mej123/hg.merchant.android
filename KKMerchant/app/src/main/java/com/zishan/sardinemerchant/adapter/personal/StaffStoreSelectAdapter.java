package com.zishan.sardinemerchant.adapter.personal;

import android.graphics.Color;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/24.
 */

public class StaffStoreSelectAdapter extends BaseQuickAdapter<StoreMsgEntity> {

    public StaffStoreSelectAdapter(int layoutResId, List<StoreMsgEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoreMsgEntity data) {
        if (data == null) return;
        TextView storeName = holder.getView(R.id.tv_store_name);//门店名字
        //int position = holder.getAdapterPosition();
        storeName.setText(data.getStoreName());
//        if (position == 0) {
//            storeName.setText("全部门店");
//        } else {
//            storeName.setText(data.getStoreName());
//        }
        if (data.getClick()) {
            storeName.setTextColor(Color.parseColor("#1784ff"));
        }else {
            storeName.setTextColor(Color.parseColor("#404040"));
        }
    }
}

