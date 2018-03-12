package com.zishan.sardinemerchant.adapter.personal;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.SelectDishData;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 店铺订单 筛选菜品
 * Created by wislie on 2017/11/9.
 */

public class DishSelectAdapter extends CommonAdapter<SelectDishData> {

    private LinearLayout.LayoutParams mLayoutParams;
    private Context mContext;

    public DishSelectAdapter(Context context, List<SelectDishData> objects) {
        super(context, objects);
        mContext = context;
        mLayoutParams = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 77), DensityUtil.dip2px(context, 30));
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    public int getItemLayoutId() {
        return R.layout.item_appointment_select;
    }

    @Override
    public void convert(CommonViewHolder holder, SelectDishData data) {
        TextView optionText = holder.getView(R.id.appointment_select_item);
        optionText.setLayoutParams(mLayoutParams);

        optionText.setText(data.getContent());
        optionText.setSelected(data.isSelected());
    }
}
