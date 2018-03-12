package com.zishan.sardinemerchant.adapter.page;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.AppointmentRemindTableArrangeEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 桌台安排
 * Created by wislie on 2017/11/15.
 */

public class TableArrangeTypeAdapter extends BaseQuickAdapter<AppointmentRemindTableArrangeEntity> {


    public TableArrangeTypeAdapter(int layoutResId, List<AppointmentRemindTableArrangeEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, AppointmentRemindTableArrangeEntity data) {
        TextView tableNumText = holder.getView(R.id.table_num_text);
        tableNumText.setText(data.getSeatName());
        ImageView tableSelectedIcon = holder.getView(R.id.table_selected_icon);

        if (data.isSelected()) {
            tableSelectedIcon.setBackgroundResource(R.mipmap.checked_blue_icon);
        } else {
            tableSelectedIcon.setBackgroundResource(R.mipmap.unchecked);
        }


    }
}
