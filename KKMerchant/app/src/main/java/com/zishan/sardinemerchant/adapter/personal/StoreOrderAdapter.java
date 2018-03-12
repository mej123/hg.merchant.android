package com.zishan.sardinemerchant.adapter.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.StoreOrderProductEntity;
import com.example.wislie.rxjava.model.personal.OrderAttachmentEntity;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 店铺订单
 * Created by wislie on 2017/11/7.
 */
public class StoreOrderAdapter extends BaseQuickAdapter<StoreOrderEntity> {



    public StoreOrderAdapter(int layoutResId, List<StoreOrderEntity> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(final BaseViewHolder holder, StoreOrderEntity data) {
        //手机号
        TextView customerNameText = holder.getView(R.id.customer_name);
        customerNameText.setText(StringUtil.formatPhoneNum(data.getBuyerMobile()));

        //支付类型
        TextView typeText = holder.getView(R.id.store_status);
        //桌台编号
        TextView orderNumText = holder.getView(R.id.store_order_num);
        //交易金额
        TextView orderAmountText = holder.getView(R.id.store_amount);

        //桌台编号的布局
        RelativeLayout orderNumLayout = holder.getView(R.id.store_order_num_layout);


        //验券方式的标题
        TextView orderExchangeTitleText = holder.getView(R.id.store_order_exchange_date_title);


        //支付方式的标题
        TextView orderPaywayTitleText = holder.getView(R.id.store_order_pay_way_title);
        //支付方式
        TextView payWayText = holder.getView(R.id.store_order_pay_way);

        showPayType(orderExchangeTitleText, orderPaywayTitleText, typeText,
                payWayText, orderNumLayout, data.getOrderType(), data.getAttachment());

        orderNumText.setText(String.valueOf(data.getAccountId()));
        //交易时间
        TextView orderDateText = holder.getView(R.id.store_order_exchange_date);
        TextView orderTimeText = holder.getView(R.id.store_order_exchange_time);
        orderDateText.setText(DatePickerUtil.getStringFormatDate(data.getPayTime(), "MM月dd日"));
        orderTimeText.setText(DatePickerUtil.getFormatDate(data.getPayTime(), "HH:mm"));

        showPayWay(payWayText, data.getPayChannel());

        orderAmountText.setText("+"+StringUtil.point2String(data.getRealAmount()));

        ImageView avatar = holder.getView(R.id.store_order_avatar);
        GlideLoader.getInstance().load(mContext, avatar, data.getBuyerLogPic());

    }

    //显示支付类型
    private void showPayType(TextView orderExchangeTitleText, TextView orderPaywayTitleText, TextView payTypeText,
                             TextView goodsContentText,RelativeLayout orderNumLayout, Integer orderType, String attachment){
        if(orderType == null) return;
        if(orderType == Constant.STORE_ORDER_TYPE_LIVE_CONSUME){
            orderExchangeTitleText.setText(mContext.getResources().getString(R.string.exchange_time));
            orderPaywayTitleText.setText(mContext.getResources().getString(R.string.pay_way));
            payTypeText.setText(mContext.getResources().getString(R.string.elec_pay));
            payTypeText.setBackgroundResource(R.drawable.blue_round_shape_1);
            orderNumLayout.setVisibility(View.VISIBLE);
        }else if(orderType == Constant.STORE_ORDER_TYPE_LIVE_SCAN){
            orderExchangeTitleText.setText(mContext.getResources().getString(R.string.exchange_time));
            orderPaywayTitleText.setText(mContext.getResources().getString(R.string.pay_way));
            payTypeText.setText(mContext.getResources().getString(R.string.scan_pay));
            payTypeText.setBackgroundResource(R.drawable.store_order_green_shape);
            orderNumLayout.setVisibility(View.GONE);
        }else if(orderType == Constant.STORE_ORDER_TYPE_CERIFICATION){
            orderExchangeTitleText.setText(mContext.getResources().getString(R.string.check_ticket_time_title));
            orderPaywayTitleText.setText(mContext.getResources().getString(R.string.goods_title));
            payTypeText.setText(mContext.getResources().getString(R.string.scan_check_ticket));
            payTypeText.setBackgroundResource(R.drawable.cyan_round_shape_1);
            orderNumLayout.setVisibility(View.GONE);
            if(TextUtils.isEmpty(attachment)) return;
            OrderAttachmentEntity attachmentData = GsonParser.parseJsonToClass(attachment,
                    OrderAttachmentEntity.class);
            goodsContentText.setText(getDesc(attachmentData));

        }

    }
    //显示支付方式
    private void showPayWay(TextView payWayText, Integer payChannel ){
        if(payChannel  == null) return;
        if(payChannel  == 1){
            payWayText.setText("支付宝");
        }else if(payChannel  == 2){
            payWayText.setText("微信");
        }else if(payChannel  == 3){
            payWayText.setText("手Q");
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
            sBuilder.append(product.getProductName()).append("×").append(product.getNum()).append(" ");
        }
        sBuilder.delete(sBuilder.length() - 1, sBuilder.length());
        return sBuilder.toString();
    }
}