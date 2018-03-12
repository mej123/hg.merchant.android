package com.zishan.sardinemerchant.adapter.personal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.PositionManagerEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/20.
 * <p>
 * 职位选择
 */

public class NewPositionSelectAdapter extends BaseQuickAdapter<PositionManagerEntity> {
    public NewPositionSelectAdapter(int layoutResId, List<PositionManagerEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PositionManagerEntity data) {

        if (data == null) return;
        TextView positionName = holder.getView(R.id.tv_add_position_name);//职位名称
        TextView positionState = holder.getView(R.id.position_state);//职位性质(默认或自定义)
        TextView describeMsg = holder.getView(R.id.tv_describe_message);//职位性质(默认或自定义)
        ImageView checkIcon = holder.getView(R.id.check_icon);//选中icon

        String name = data.getName();
        positionName.setText(name);

        if (name.equals("店长") || name.equals("收银") || name.equals("服务员")) {
            positionState.setText("系统默认");
            describeMsg.setVisibility(View.VISIBLE);
        } else {
            positionState.setText("自定义");
            describeMsg.setVisibility(View.GONE);
        }
        if (data.getSelect()){
            checkIcon.setVisibility(View.VISIBLE);
        }else {
            checkIcon.setVisibility(View.GONE);
        }
    }
}
