package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 桌台详情总览
 * Created by wislie on 2017/11/16.
 */

public class DetailReviewOrderAdapter extends BaseQuickAdapter<TableMenuItemEntity> {


    public DetailReviewOrderAdapter(int layoutResId, List<TableMenuItemEntity> data) {
        super(layoutResId, data);

    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {

        return new BaseViewHolder(mContext, getItemView(layoutResId, parent));


    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, TableMenuItemEntity data) {

        LinearLayout tagLayout = holder.getView(R.id.dish_tag_layout);
        TextView dishCategoryNameText = holder.getView(R.id.dish_category);

        TextView dishNameText = holder.getView(R.id.dish_name);
        TextView dishTypeText = holder.getView(R.id.dish_type);

        TextView dishPriceText = holder.getView(R.id.dish_price);
        TextView dishTotalPriceText = holder.getView(R.id.dish_total_price);
        TextView dishNumberText = holder.getView(R.id.dish_num);
        TextView dishStatusText = holder.getView(R.id.dish_status);

        if (!TextUtils.isEmpty(data.getCustomGroupName())) {
            tagLayout.setVisibility(View.VISIBLE);
            dishCategoryNameText.setText(data.getCustomGroupName());
        } else {
            tagLayout.setVisibility(View.GONE);
        }

        long custom_group_id = getGroupidForPosition(data);
        if (holder.getAdapterPosition() == getPositionForSection(custom_group_id)) {

            tagLayout.setVisibility(View.VISIBLE);
        } else {
            tagLayout.setVisibility(View.GONE);
        }

        dishNameText.setText(data.getProductName());
        dishPriceText.setText("单价: " + StringUtil.point2String(data.getValuationPrice()));
        if (data.getState() == 6 || data.getType() == 3) {
            dishTotalPriceText.setText("总价: 0.00");
        } else {
            dishTotalPriceText.setText("总价: " + StringUtil.point2String(data.getValuationPrice() * data.getNum()));
        }

        dishNumberText.setText("×" + data.getNum());

        switch (data.getType()) {
            case 0:
//                dishTypeText.setText("初始");
                dishTypeText.setVisibility(View.GONE);
                break;
            case 1:
                dishTypeText.setText("点菜");
                dishTypeText.setVisibility(View.GONE);
                break;
            case 3:
                dishTypeText.setText("免单");
                dishTypeText.setVisibility(View.VISIBLE);
                break;
            default:
                dishTypeText.setVisibility(View.GONE);
                break;
        }


        switch (data.getState()) { //0未入单，1已入单，3已上菜 4结束 5非正常结束 6已退菜
            case Constant.DISH_STATE_INIT:
                dishStatusText.setText("未入单");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
                break;
            case Constant.DISH_STATE_CONFIRM:
                dishStatusText.setText("已入单");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
                break;
            case Constant.DISH_STATE_COOK:
//                dishStatusText.setText("下厨");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
                break;
            case Constant.DISH_STATE_PUT:
                dishStatusText.setText("已上菜");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
                break;
            case Constant.DISH_STATE_FINISH:
                dishStatusText.setText("结束");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
                break;
            case Constant.DISH_STATE_UNEXPECTED:
                dishStatusText.setText("非正常结束");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));

                break;
            case Constant.DISH_STATE_CANCEL:
                dishStatusText.setText("已退菜");
                dishStatusText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));
                break;
        }
    }


    public long getGroupidForPosition(TableMenuItemEntity data) {
        if(data == null) return 0;
        Long groupId = data.getCustomGroupId();
        if(groupId == null) return 0;
        return data.getCustomGroupId();
    }

    private int getPositionForSection(long groupid) {
        for (int i = 0; i < getItemCount(); i++) {
            TableMenuItemEntity data = getItem(i);
            if(data.getCustomGroupId() == null) return -1;
            if (data.getCustomGroupId().longValue() == groupid) {
                return i;
            }
        }
        return -1;
    }
}
