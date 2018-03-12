package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 桌台详情 订单的适配器
 * Created by wislie on 2017/9/20.
 */

public class TableDishAdapter extends CommonAdapter<String> {

    private RelativeLayout.LayoutParams mLayoutParam;

    public TableDishAdapter(Context context, List<String> objects) {
        super(context, objects);
        mLayoutParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 68));
    }

    public int getItemLayoutId() {
        return R.layout.item_table_dish;
    }


    @Override
    public void convert(CommonViewHolder holder, String data) {
        View itemView = holder.getView(R.id.item_table_dish);
        itemView.setLayoutParams(mLayoutParam);
    }

}
