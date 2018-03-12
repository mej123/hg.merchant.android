package com.zishan.sardinemerchant.adapter.personal;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.PromotionRecordEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 商家优惠
 * Created by wislie on 2017/11/20.
 */

public class StoreDetailDiscountAdapter extends CommonAdapter<PromotionRecordEntity> {

    private RelativeLayout.LayoutParams mLayoutParams;

    public StoreDetailDiscountAdapter(Context context, List objects) {
        super(context, objects);
        mLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 44));
    }

    @Override
    public void convert(CommonViewHolder holder, PromotionRecordEntity data) {
        RelativeLayout item = holder.getView(R.id.item_store_detail_discount);
        item.setLayoutParams(mLayoutParams);

        ImageView discountIcon = holder.getView(R.id.discount_icon);
        TextView discountTitleText = holder.getView(R.id.discount_title);
        TextView discountAmountText = holder.getView(R.id.discount_amount);

        if (data.getBeforeStrategyOrderPrice() == null &&
                data.getAfterStrategyOrderPrice() == null ||
                data.getBeforeStrategyOrderPrice() == 0 ||
                data.getAfterStrategyOrderPrice() == 0) {
            discountIcon.setImageResource(R.mipmap.sales_ticket_icon);
            discountTitleText.setText("卡券抵扣");
            discountAmountText.setText("-¥ " + data.getDiscountAmount() / 100);

        } else {
            discountIcon.setImageResource(R.mipmap.sales_reduce_icon);
            discountTitleText.setText("满" + data.getFullAmount() / 100 + "减" + data.getDiscountAmount() / 100);
            discountAmountText.setText("-¥ " + data.getDiscountAmount() / 100);

        }

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_store_detail_discount;
    }
}
