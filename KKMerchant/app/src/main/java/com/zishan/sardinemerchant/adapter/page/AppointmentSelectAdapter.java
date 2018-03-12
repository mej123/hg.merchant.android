package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.SelectData;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 预约筛选 适配器
 * Created by wislie on 2017/10/24.
 */

public class AppointmentSelectAdapter extends CommonAdapter<SelectData> {

    private LinearLayout.LayoutParams mLayoutParams;
    private LinearLayout.LayoutParams mBigLayoutParams;
    private List<SelectData> mDataList;

    public AppointmentSelectAdapter(Context context, List<SelectData> dataList) {
        super(context, dataList);
        mLayoutParams = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 76), DensityUtil.dip2px(context, 29));
        mBigLayoutParams = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 162), DensityUtil.dip2px(context, 29));
        this.mDataList = dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    @Override
    public void convert(CommonViewHolder holder, SelectData data) {
        TextView optionText = holder.getView(R.id.appointment_select_item);

        if(!TextUtils.isEmpty(data.getDateContent()) && data.getDateContent().length() >= 7){
            optionText.setLayoutParams(mBigLayoutParams);
        }else{
            optionText.setLayoutParams(mLayoutParams);
        }

        if(!TextUtils.isEmpty(data.getDateContent())){
            optionText.setText(data.getDateContent());
        }else if(!TextUtils.isEmpty(data.getSeatContent())){
            optionText.setText(data.getSeatContent());
        }else if(!TextUtils.isEmpty(data.getNumContent())){
            optionText.setText(data.getNumContent());
        }

        optionText.setSelected(data.isSelected());

    }

    public int getItemLayoutId() {
        return R.layout.item_appointment_select;
    }

    /**
     * @param selectPos 选中的下标
     */
    public void notifyAdapterData(int selectPos) {
        if (mDataList == null || mDataList.size() == 0) return;
        for (int i = 0; i < mDataList.size(); i++) {
            SelectData data = mDataList.get(i);
            if (i != selectPos) {
                data.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }


}
