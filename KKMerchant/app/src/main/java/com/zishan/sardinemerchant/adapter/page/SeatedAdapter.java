package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.view.View;

import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/10/27.
 * <p>
 * 预约提醒  落座适配器
 */

public class SeatedAdapter extends BaseQuickAdapter<AppointmentRemindEntity> {
    private int state;
    private int productsNum;
    private String remarks;
    private int needRoom;
    private String bookerName;
    private String bookerPhone;
    private int dinnerNum;
    private String time;

    public SeatedAdapter(int layoutResId, List<AppointmentRemindEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, AppointmentRemindEntity seatedEntity) {
        if (seatedEntity != null) {
            bookerName = seatedEntity.getBookerName();    //预约人名字
            bookerPhone = seatedEntity.getBookerPhone();      //预约人手机号
            needRoom = seatedEntity.getNeedRoom(); //是否需要包厢
            dinnerNum = seatedEntity.getDinnerNum();      //预约人数
            time = DatePickerUtil.stampToDate(String.valueOf(seatedEntity.getDinnerTime()) );     //预约时间
            remarks = seatedEntity.getRemarks();//备注
            //当前落座状态
            state = seatedEntity.getState();
            productsNum = seatedEntity.getProductsNum();
            holder.setText(R.id.tv_custom_name, bookerName);
            holder.setText(R.id.tv_phone, bookerPhone);
            holder.setText(R.id.tv_time, time);//用餐时间
            holder.setText(R.id.tv_remark, "备注: " + remarks);//备注信息
            holder.setText(R.id.tv_appointment_count, productsNum + "件");//预约菜品数量
//            holder.setText(R.id.tv_number, dinnerNum + " | ");

            if (needRoom == 0) {
                //holder.setText(R.id.number_and_room,dinnerNum+ "  |  " + "不需要包厢");
                holder.setText(R.id.tv_number, dinnerNum + "  |  " + "不需要包厢");
//                holder.setText(R.id.tv_room, "不需要包厢");
            }else if (needRoom == 1) {
                // holder.setText(R.id.number_and_room, dinnerNum + " | " + "需要包厢");
                holder.setText(R.id.tv_number, dinnerNum + "  |  " + "需要包厢");
//                holder.setText(R.id.tv_room, "需要包厢");
            }

            //未到店 灰色
            if (state == 1) {
                holder.setText(R.id.tv_state, "未到店");
                holder.getView(R.id.tv_arrange).setVisibility(View.VISIBLE);
                holder.getView(R.id.tv_state).setBackgroundResource(R.drawable.table_pandect_sharp_seat_hui);
            }
            //已到店  蓝色
            if (state == 2) {
                holder.setText(R.id.tv_state, "已到店");
                holder.getView(R.id.tv_arrange).setVisibility(View.GONE);
                holder.getView(R.id.tv_state).setBackgroundResource(R.drawable.table_pandect_sharp_seat_blue);
            }
            //已接受超时  红色
            if (state == 6) {
                holder.setText(R.id.tv_state, "已超时");
                holder.getView(R.id.tv_arrange).setVisibility(View.VISIBLE);
                holder.getView(R.id.tv_state).setBackgroundResource(R.drawable.table_pandect_sharp_seat_red);
            }

            //安排入座
            holder.getView(R.id.tv_arrange).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onSelected(holder.getAdapterPosition());
                    }
                }
            });
        }
    }
}
