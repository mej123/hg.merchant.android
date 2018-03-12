package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.personal.PositionListEntity;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/19.
 *
 * 职位列表  基本职位适配器
 *
 */

public class BasicNewPositionAdapter extends BaseQuickAdapter<PositionListEntity> {
    public BasicNewPositionAdapter(int layoutResId, List<PositionListEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionListEntity item) {

    }
}
