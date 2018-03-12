package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;

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
 *
 * 预约提醒  总览  已拒绝适配器
 *
 */

public class PandectRefuseAdapter  extends BaseQuickAdapter<AppointmentRemindEntity> {

    private String productsNum;
    private String remarks;
    private String gmtModified;
    private String assistantName;
    private String needRoom;
    private String dinnerNum;
    private String dinerTime;
    private String bookerPhone;
    private String bookerName;
    private String abnormalEndReason;

    public PandectRefuseAdapter(int layoutResId, List<AppointmentRemindEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AppointmentRemindEntity data) {
        if (data != null) {
            //预约人
            bookerName = data.getBookerName();
            //预约手机号
            bookerPhone = data.getBookerPhone();
            //用餐时间
            dinerTime = DatePickerUtil.stampToDate(data.getDinnerTime() + "");
            //用餐人数
            dinnerNum = data.getDinnerNum() + "";
            //是否需要包厢
            needRoom = data.getNeedRoom() + "";
            //拒绝理由
            abnormalEndReason = data.getAbnormalEndReason();
            productsNum = data.getProductsNum() + "";
            //操作人姓名
            assistantName = data.getAssistantName();
            //操作时间
            gmtModified = DatePickerUtil.stampToDate(data.getGmtModified() + "");
            //拒绝理由
            remarks = data.getRemarks();
            holder.setText(R.id.tv_custom_name, bookerName);
            holder.setText(R.id.tv_phone, bookerPhone);
            holder.setText(R.id.tv_time, dinerTime);

            if (needRoom.equals("0")) {
                //holder.setText(R.id.number_and_room,dinnerNum+ "  |  " + "不需要包厢");
                holder.setText(R.id.tv_count_room, dinnerNum + "  |  " + "不需要包厢");
            }
            if (needRoom.equals("1")) {

                // holder.setText(R.id.number_and_room, dinnerNum + " | " + "需要包厢");
                holder.setText(R.id.tv_count_room, dinnerNum + "  |  " + "需要包厢");
            }
            holder.setText(R.id.tv_person, assistantName);
            holder.setText(R.id.tv_gmtmodified, gmtModified);
            holder.setText(R.id.tv_refuse_reason, "拒绝理由：" + abnormalEndReason);  //拒绝理由
        }

    }
}
