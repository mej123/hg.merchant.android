package com.zishan.sardinemerchant.adapter.page;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.StoreOrderProductEntity;
import com.example.wislie.rxjava.model.personal.OrderAttachmentEntity;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 红包核销 记录
 * Created by wislie on 2017/11/3.
 */

public class CerificationRecordAdapter extends BaseQuickAdapter<StoreOrderEntity> {


    private String mPreDate = "";

    public CerificationRecordAdapter(int layoutResId, List<StoreOrderEntity> data) {
        super(layoutResId, data);
    }

    private void setTitleVisible(LinearLayout recordTitleLayout, TextView dateText,
                                 TextView countText, StoreOrderEntity data, OrderAttachmentEntity attachment, int position) {

        String date = DatePickerUtil.getFormatDate(data.getPayTime(), "yyyy-MM-dd");
        //和之前的进行比较
        if (date.equals(mPreDate) && position != 0) {
            recordTitleLayout.setVisibility(View.GONE);
        } else {
            mPreDate = date;
            recordTitleLayout.setVisibility(View.VISIBLE);
            dateText.setText(date);
            if (attachment != null && attachment.getProducts() != null) {
                countText.setText("(" + attachment.getProducts().size() + "笔)");
            }

        }
    }

    //得到商品描述
    private String getDesc(OrderAttachmentEntity attachment) {
        if (attachment == null) return null;

        List<StoreOrderProductEntity> products = attachment.getProducts();
        if (products == null || products.size() == 0) return null;
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            StoreOrderProductEntity product = products.get(i);
            sBuilder.append(product.getProductName()).append(product.getNum()).append(product.getUnit()).append(";");
        }
        sBuilder.delete(sBuilder.length() - 1, sBuilder.length());
        return sBuilder.toString();
    }


    @Override
    protected void convert(BaseViewHolder holder, StoreOrderEntity data) {
        LinearLayout recordTitleLayout = holder.getView(R.id.record_title_layout);
        TextView dateText = holder.getView(R.id.record_date);
        TextView countText = holder.getView(R.id.record_count);
        TextView timeText = holder.getView(R.id.record_time);
        TextView recordDescText = holder.getView(R.id.record_desc);
        String attachmentJson = data.getAttachment();
        //附件
        OrderAttachmentEntity attachment = GsonParser.parseJsonToClass(attachmentJson,
                OrderAttachmentEntity.class);

        recordDescText.setText(getDesc(attachment));
        timeText.setText(DatePickerUtil.getFormatDate(data.getPayTime(), "HH:mm"));
        setTitleVisible(recordTitleLayout, dateText, countText, data, attachment, holder.getAdapterPosition());
    }
}
