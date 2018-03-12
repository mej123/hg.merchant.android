package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.PositionManagerEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/22.
 *
 * 职位权限基本职位
 *
 */

public class BasicPositionAdapter extends BaseQuickAdapter<PositionManagerEntity> {


    public BasicPositionAdapter(int layoutResId, List<PositionManagerEntity> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, PositionManagerEntity data) {

        if (data==null) return;
        holder.setText(R.id.tv_add_position_name, data.getName());
    }
}
