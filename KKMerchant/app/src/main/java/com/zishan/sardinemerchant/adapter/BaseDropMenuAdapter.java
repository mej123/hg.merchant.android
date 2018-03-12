package com.zishan.sardinemerchant.adapter;

import com.zishan.sardinemerchant.entity.BaseDropMenuEntity;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 下拉框适配器基类
 * Created by wislie on 2017/12/28.
 */

public class BaseDropMenuAdapter extends BaseQuickAdapter<BaseDropMenuEntity> {


    public BaseDropMenuAdapter(int layoutResId, List<BaseDropMenuEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseDropMenuEntity data) {

    }
}
