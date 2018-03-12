package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TicketEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;
import top.ftas.ftaswidget.view.CircleView;

/**
 * 分销池子列表
 * Created by wislie on 2018/1/19.
 */

public class TicketListAdapter extends BaseQuickAdapter<TicketEntity> {


    public TicketListAdapter(int layoutResId, List<TicketEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TicketEntity data) {
        //1:发券中,2:已到期,3:已发完, 4.已取消
        int status = data.getStatus();

        TextView ticketNameText = holder.getView(R.id.ticket_name);
        ticketNameText.setText(data.getGiveOutName());

        TextView ticketIdTitleText = holder.getView(R.id.ticket_id_title);
        TextView ticketIdText = holder.getView(R.id.ticket_id);
        ticketIdText.setText(String.valueOf(data.getCouponGiveOutId()));

        TextView ticketStoreTitleText = holder.getView(R.id.ticket_store_title);
        TextView ticketStoreNumText = holder.getView(R.id.ticket_store_num);
        if (data.getStock() == -1) {
            ticketStoreNumText.setText("不限");
        } else {
            ticketStoreNumText.setText(String.valueOf(data.getStock()));
        }

        CircleView stateColorText = holder.getView(R.id.ticket_state_color);
        TextView ticketStateText = holder.getView(R.id.ticket_state);

        TextView ticketDeliverTitleText = holder.getView(R.id.ticket_deliver_title);
        TextView ticketDeliverNumText = holder.getView(R.id.ticket_deliver_num);
        ticketDeliverNumText.setText(String.valueOf(data.getGiveOutNum()));


        ImageView ticketStartTimeIcon = holder.getView(R.id.ticket_start_icon);
        TextView ticketStartTimeText = holder.getView(R.id.ticket_start_time);
        ticketStartTimeText.setText("创建时间: " + DatePickerUtil.getStringFormatDate(data.getGmtCreate(), "yyyy-MM-dd") +
                DatePickerUtil.getFormatDate(data.getGmtCreate(), " HH:mm"));

        ImageView ticketCloseTimeIcon = holder.getView(R.id.ticket_close_icon);
        TextView ticketCloseTimeText = holder.getView(R.id.ticket_close_time);
        ticketCloseTimeText.setText("截止时间: " + DatePickerUtil.getStringFormatDate(data.getGiveOutEndTime(), "yyyy-MM-dd") +
                DatePickerUtil.getFormatDate(data.getGiveOutEndTime(), " HH:mm"));

        if (status == 1) { //1:发券中
            ticketStateText.setText("发券中");
            ticketNameText.setSelected(true);
            ticketIdTitleText.setSelected(true);
            ticketIdText.setSelected(true);
            ticketStoreTitleText.setSelected(true);
            ticketStoreNumText.setSelected(true);
            ticketNameText.setSelected(true);
            ticketDeliverTitleText.setSelected(true);
            ticketDeliverNumText.setSelected(true);
            ticketStartTimeIcon.setSelected(true);
            ticketCloseTimeIcon.setSelected(true);
            ticketStartTimeText.setSelected(true);
            ticketCloseTimeText.setSelected(true);
            stateColorText.setDrawColor(ContextCompat.getColor(mContext, R.color.bg_color_blue_13));
        } else { //2:已到期,3:已发完 4.已取消
            if(status == 2){
                ticketStateText.setText("已过期");
            }else if(status == 3){
                ticketStateText.setText("已发完");
            }else if(status == 4){
                ticketStateText.setText("已取消");
            }
            ticketNameText.setSelected(false);
            ticketIdTitleText.setSelected(false);
            ticketIdText.setSelected(false);
            ticketStoreTitleText.setSelected(false);
            ticketStoreNumText.setSelected(false);
            ticketNameText.setSelected(false);
            ticketDeliverTitleText.setSelected(false);
            ticketDeliverNumText.setSelected(false);
            ticketStartTimeIcon.setSelected(false);
            ticketCloseTimeIcon.setSelected(false);
            ticketStartTimeText.setSelected(false);
            ticketCloseTimeText.setSelected(false);
            stateColorText.setDrawColor(ContextCompat.getColor(mContext, R.color.text_color_gray_7));
        }
    }
}
