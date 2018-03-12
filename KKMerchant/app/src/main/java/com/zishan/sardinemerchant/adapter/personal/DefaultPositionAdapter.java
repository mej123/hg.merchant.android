package com.zishan.sardinemerchant.adapter.personal;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.wislie.rxjava.model.MyStaffEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * Created by yang on 2017/12/7.
 * <p>
 * 我的员工默认店长 收银 服务员适配器
 */

public class DefaultPositionAdapter extends CommonAdapter<MyStaffEntity> {


    public DefaultPositionAdapter(Context context, List<MyStaffEntity> objects) {
        super(context, objects);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_default_my_staff;
    }

    @Override
    public void convert(final CommonViewHolder holder, MyStaffEntity myStaffEntity) {


        if (myStaffEntity == null) return;

        RelativeLayout itemLayout = holder.getView(R.id.content);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });

        Button delete = holder.getView(R.id.staff_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onDelete(holder.getLayoutPosition());
                }
            }
        });
        holder.setText(R.id.staff_name, myStaffEntity.getRealName());


    }
}
