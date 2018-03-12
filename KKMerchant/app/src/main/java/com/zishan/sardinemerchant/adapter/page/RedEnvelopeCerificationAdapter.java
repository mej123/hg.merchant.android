package com.zishan.sardinemerchant.adapter.page;

import android.util.Log;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.StoreOrderProductEntity;
import com.example.wislie.rxjava.model.personal.OrderAttachmentEntity;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 红包核销
 * Created by wislie on 2017/11/3.
 */

public class RedEnvelopeCerificationAdapter extends BaseQuickAdapter<StoreOrderEntity> {


    public RedEnvelopeCerificationAdapter(int layoutResId, List<StoreOrderEntity> data) {
        super(layoutResId, data);
    }


    //得到商品描述
    private String getDesc(StoreOrderEntity data) {

        String attachmentJson = data.getAttachment();

        //解析字符串为实体对象
        OrderAttachmentEntity attachment = GsonParser.parseJsonToClass(attachmentJson,
                OrderAttachmentEntity.class);
        if (attachment == null) return null;
        ArrayList<StoreOrderProductEntity> products = attachment.getProducts();
        if(products == null || products.size() == 0){
            return null;
        }
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
        TextView dateText = holder.getView(R.id.certification_date);
        TextView timeText = holder.getView(R.id.certification_time);
        TextView descText = holder.getView(R.id.certification_desc);

        dateText.setText(DatePickerUtil.getStringFormatDate(data.getPayTime() / 1000, "yyyy-MM-dd"));//getStringFormatDate
        timeText.setText(DatePickerUtil.getFormatDate(data.getPayTime(), "HH:mm"));
        descText.setText(getDesc(data));
    }
}
