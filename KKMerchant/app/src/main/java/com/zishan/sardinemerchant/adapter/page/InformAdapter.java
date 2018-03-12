package com.zishan.sardinemerchant.adapter.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/10/27.
 * <p>
 * 预约提醒  总览  通知  适配器
 */

public class InformAdapter extends BaseQuickAdapter<AppointmentRemindEntity> {


    public InformAdapter(int layoutResId, List<AppointmentRemindEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, AppointmentRemindEntity data) {
        if (data != null) {
            //预约人名字
            String bookerName = data.getBookerName();
            //预约人手机号
            String bookerPhone = data.getBookerPhone();
            //是否需要包厢
            int needRoom = data.getNeedRoom();
            //预约人数
            int dinnerNum = data.getDinnerNum();
            //预约时间
            String time = DatePickerUtil.getFormatDate(data.getDinnerTime(), "yyyy-MM-dd HH:mm:ss");

            holder.setText(R.id.tv_custom_name, bookerName);//联系人姓名
            holder.setText(R.id.tv_phone, bookerPhone);//联系人手机号
            holder.setText(R.id.tv_eat_time, time);//用餐时间

            if (needRoom == 0) {
                holder.setText(R.id.tv_eat_person_count, dinnerNum + "  |  " + "不需要包厢");
            } else if (needRoom == 1) {
                holder.setText(R.id.tv_eat_person_count, dinnerNum + "  |  " + "需要包厢");
            }

            //过期时间
            long expireIn = data.getExpireIn();
            long outtime = expireIn / 60;

            TextView outTimeText = holder.getView(R.id.tv_out_time);
            outTimeText.setText("剩余" + outtime + "分");
            if (outtime < 3) {
                outTimeText.setBackgroundResource(R.drawable.red_round_shape_2);
            } else {
                outTimeText.setBackgroundResource(R.drawable.table_pandect_shape_green);
            }

            //设置图片
            ImageView avatar = holder.getView(R.id.iv_icon);
            GlideLoader.getInstance().load(mContext, avatar, data.getAccountHeadUrl());

            //点击拒绝
            holder.getView(R.id.tv_refuse).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onRefuse(holder.getAdapterPosition());
                    }
                }
            });

            //点击接受
            holder.getView(R.id.tv_receive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onAccept(holder.getAdapterPosition());
                    }
                }
            });

        }
    }
}

