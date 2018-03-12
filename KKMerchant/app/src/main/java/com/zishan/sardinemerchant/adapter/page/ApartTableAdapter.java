package com.zishan.sardinemerchant.adapter.page;

import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableApartEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 拆分桌台
 * Created by wislie on 2017/12/4.
 */

public class ApartTableAdapter extends BaseQuickAdapter<TableApartEntity> {


    public ApartTableAdapter(int layoutResId, List<TableApartEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TableApartEntity data) {
        TextView tableNameText = holder.getView(R.id.table_name);
        tableNameText.setText(data.getSeatName());
    }
}
