package com.zishan.sardinemerchant.adapter.store;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 商品管理下的列表
 * Created by wislie on 2017/9/20.
 */

public class GoodsListAdapter extends BaseQuickAdapter<ProductEntity> {

    private boolean is_discount;
    private boolean is_recommend;
    private List<ProductGroupEntity> mProductsGroup;

    public GoodsListAdapter(int layoutResId, List<ProductEntity> data, List<ProductGroupEntity> productGroups) {
        super(layoutResId, data);
        mProductsGroup = productGroups;
    }

    /**
     * 更新
     *
     * @param is_discount
     * @param is_recommend
     */
    public void notifyDataSetChanged(boolean is_discount, boolean is_recommend) {
        this.is_discount = is_discount;
        this.is_recommend = is_recommend;
        notifyDataSetChanged();
    }


    /**
     * 获取菜单名称
     *
     * @param customGroupId
     * @return
     */
    private String getGoodsType(long customGroupId) {

        if (mProductsGroup.size() <= 0) return null;
        if (is_discount) return "折扣";
        if (is_recommend) return "推荐";

        for (ProductGroupEntity productGroup : mProductsGroup) {
            if (customGroupId == productGroup.getId()) {
                return productGroup.getName();
            }
        }
        return null;
    }


    @Override
    protected void convert(final BaseViewHolder holder, final ProductEntity data) {
        ImageView imageView = holder.getView(R.id.goods_icon);
        //折扣
        TextView discountText = holder.getView(R.id.goods_discount);
        if(TextUtils.isEmpty(data.getTitle())){
            discountText.setVisibility(View.GONE);
        }else{
            discountText.setVisibility(View.VISIBLE);
            if (data.getTitle().length() <= 2) {
                discountText.setBackgroundResource(R.mipmap.discount_icon_integer);
            } else {
                discountText.setBackgroundResource(R.mipmap.discount_icon_float);
            }
            discountText.setText(data.getTitle());
        }


        ImageView leftIcon = holder.getView(R.id.good_type_left_icon);
        ImageView rightIcon = holder.getView(R.id.good_type_right_icon);

        //菜名
        TextView goodsNameText = holder.getView(R.id.goods_name);
        goodsNameText.setText(data.getName());



        //菜品类型
        TextView descText = holder.getView(R.id.goods_type);
        String goodType = getGoodsType(data.getCustomGroupId());
        if (goodType != null) {
            descText.setText(goodType);
        }

        if(TextUtils.isEmpty(goodType)){
            leftIcon.setVisibility(View.INVISIBLE);
            goodsNameText.setBackground(null);
            rightIcon.setVisibility(View.INVISIBLE);
        }else{
            leftIcon.setVisibility(View.VISIBLE);
            goodsNameText.setBackgroundResource(R.mipmap.goods_type_rect_icon);
            rightIcon.setVisibility(View.VISIBLE);
        }

        //菜价
        TextView goodsPriceText = holder.getView(R.id.goods_price);

        if(data.getDiscount()!= null && data.getDiscount() == Boolean.TRUE){
            goodsPriceText.setText("¥ " + StringUtil.point2String(data.getStrategyPrice()) );
        }else{
            goodsPriceText.setText("¥ " + StringUtil.point2String(data.getRealPrice()) );
        }

        //显示的当前状态
        TextView goodsStateText = holder.getView(R.id.goods_state);


        //状态操作
        TextView goodsStateOperateText = holder.getView(R.id.goods_state_change);
        //on or off
        TextView goodsSaleOperateText = holder.getView(R.id.goods_sale_change);


        if (data.getSoldOut() == Boolean.TRUE) {

            goodsStateText.setText("售空");

            goodsStateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_medium_black));

            //发售
            goodsStateOperateText.setText("发售");
            goodsStateOperateText.setTextColor(ContextCompat.getColor(mContext, R.color.stroke_color_light_red));
            goodsStateOperateText.setBackgroundResource(R.drawable.main_stroke_sale_start_shape);

        } else {
            goodsStateText.setText("在售");
            goodsStateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));

            goodsStateOperateText.setText("售空");
            goodsStateOperateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_gray));
            goodsStateOperateText.setBackgroundResource(R.drawable.main_stroke_sale_empty_shape);
        }


        final int state = data.getState();
        if (state == 0) {
            //下架
            goodsSaleOperateText.setText("下架");
            goodsSaleOperateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_white));
            goodsSaleOperateText.setBackgroundResource(R.drawable.main_stroke_sale_off_shape);
        } else if (state == 1) {
            //上架
            goodsSaleOperateText.setText("上架");
            goodsSaleOperateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_white));
            goodsSaleOperateText.setBackgroundResource(R.drawable.main_stroke_sale_on_shape);
        }

        goodsSaleOperateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    int states = state == 0 ? 1 : 0;
                    mOnItemListener.onGoodsOn(holder.getLayoutPosition(), states);
                }
            }
        });

        goodsStateOperateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    int is_sold_out = data.getSoldOut() == Boolean.TRUE ? 0 : 1;
                    mOnItemListener.onSoldout(holder.getLayoutPosition(), is_sold_out);
                }
            }
        });


        GlideLoader.getInstance().load(mContext, imageView, StringUtil.appendHttps(data.getLogoPicUrl()));
    }
}
