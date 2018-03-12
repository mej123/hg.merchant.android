package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.CouponEntity;
import com.example.wislie.rxjava.model.page.CouponProductBean;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 选择卡券
 * Created by wislie on 2018/1/20.
 */

public class TicketChooseAdapter extends BaseQuickAdapter<CouponEntity> {

    //判断是否有过期的标志, 卡券配置和卡券选择列表 适配器相似
    private boolean hasExpiredTag = false;

    public TicketChooseAdapter(int layoutResId, List<CouponEntity> data, boolean hasExpiredTag) {
        super(layoutResId, data);
        this.hasExpiredTag = hasExpiredTag;
    }

    @Override
    protected void convert(BaseViewHolder holder, CouponEntity data) {
        RelativeLayout item_layout = holder.getView(R.id.item_choose_ticket);
        TextView ticketNameText = holder.getView(R.id.ticket_name);
        TextView ticketIdText = holder.getView(R.id.ticket_id);
        TextView ticketDaysText = holder.getView(R.id.ticket_date_role);

        LinearLayout ticketValueLayout = holder.getView(R.id.ticket_value_layout);
        TextView ticketValueText = holder.getView(R.id.ticket_value);
        TextView ticketDescText = holder.getView(R.id.ticket_desc_role);
        TextView ticketSignText = holder.getView(R.id.ticket_sign);

        ImageView ticketExpiredIcon = holder.getView(R.id.is_expired_tag);
        TextView ticketExpireText = holder.getView(R.id.ticket_expire_desc);

        boolean isExpired;
        if (!hasExpiredTag || data.getValidityEndTime() > (System.currentTimeMillis() / 1000)) { //未过期
            isExpired = false;
            ticketExpiredIcon.setVisibility(View.GONE);
            ticketDescText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_medium_black));
            ticketSignText.setTextColor(ContextCompat.getColor(mContext, R.color.bg_color_red_3));
            ticketNameText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
            ticketIdText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_medium_black));
            ticketValueText.setTextColor(ContextCompat.getColor(mContext, R.color.bg_color_red_3));
            ticketDaysText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_medium_black));
            ticketExpireText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_medium_black));
        } else {
            isExpired = true;
            ticketExpiredIcon.setVisibility(View.VISIBLE);
            ticketDescText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketSignText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketNameText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketIdText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketValueText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketDaysText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
            ticketExpireText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray_9));
        }

        if (data.getCouponType() != null && data.getCouponType() == 1) { //满减券
            if (isExpired) {
                item_layout.setBackgroundResource(R.mipmap.ticket_discount_disabled_bg);
            } else {
                item_layout.setBackgroundResource(R.mipmap.ticket_discount_abled_bg);
            }
            ticketValueLayout.setVisibility(View.VISIBLE);
            ticketDescText.setText("满减券" + data.getRealAmount() + "元; 满" + data.getLimitAmount() + "元可用");
        } else if (data.getCouponType() != null && data.getCouponType() == 2) { //代金券
            if (isExpired) {
                item_layout.setBackgroundResource(R.mipmap.ticket_golden_disabled_bg);
            } else {
                item_layout.setBackgroundResource(R.mipmap.ticket_golden_abled_bg);
            }
            ticketValueLayout.setVisibility(View.VISIBLE);
            ticketDescText.setText("代金券面值" + data.getRealAmount() + "元");
        } else if (data.getCouponType() != null && data.getCouponType() == 3) { //凭证券
            if (isExpired) {
                item_layout.setBackgroundResource(R.mipmap.ticket_certification_disabled_bg);
            } else {
                item_layout.setBackgroundResource(R.mipmap.ticket_certification_abled_bg);
            }
            ticketValueLayout.setVisibility(View.GONE);
            ticketDescText.setText("此次是商品内容或者服务内容此次是商品");
            List<CouponProductBean> productList = data.getCouponProducts();
            if(productList != null && productList.size() > 0){
                for(int i = 0; i < productList.size(); i++){
                    CouponProductBean product = productList.get(i);
                    if(product.getId() != null && product.getId() == 1){
                        ticketDescText.setText("优先显示服务商品");
                        break;
                    }
                }
            }

        }

        ticketNameText.setText(data.getName());
        ticketIdText.setText("卡券ID " + data.getCouDefId());
        ticketValueText.setText(String.valueOf(data.getRealAmount()));
        ticketDaysText.setText(data.getMarketingMeta());
        if (data.getValidityType() == 1) { //1固定天数
            ticketExpireText.setText("领券" + data.getEffectiveTime() + "天后可用,有效期" + data.getValidityTime() + "天");
        } else if (data.getValidityType() == 2) { //2 指定时间段
            ticketExpireText.setText("有效期:" + DatePickerUtil.getFormatDate(data.getValidityStartTime(), "yyyy.MM.dd")
                    + "-" + DatePickerUtil.getFormatDate(data.getValidityEndTime(), "yyyy.MM.dd"));
        }
    }


}
