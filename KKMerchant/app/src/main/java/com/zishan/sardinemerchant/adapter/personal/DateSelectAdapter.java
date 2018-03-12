package com.zishan.sardinemerchant.adapter.personal;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.SelectDateData;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 预约筛选 适配器
 * Created by wislie on 2017/10/24.
 */

public class DateSelectAdapter extends CommonAdapter<SelectDateData> {

    private LinearLayout.LayoutParams mLayoutParams;
    private LinearLayout.LayoutParams mBigLayoutParams;
    private List<SelectDateData> mDataList;

    public DateSelectAdapter(Context context, List<SelectDateData> dataList) {
        super(context, dataList);
        mLayoutParams = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 105), DensityUtil.dip2px(context, 30));
        mBigLayoutParams = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 162), DensityUtil.dip2px(context, 30));
        this.mDataList = dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    @Override
    public void convert(CommonViewHolder holder, SelectDateData data) {
        TextView optionText = holder.getView(R.id.appointment_select_item);

        if (data.getContent().length() < 7) {
            optionText.setLayoutParams(mLayoutParams);
        } else {
            optionText.setLayoutParams(mBigLayoutParams);
        }
        optionText.setText(data.getContent());
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
            SelectDateData data = mDataList.get(i);
            if (i != selectPos) {
                data.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }


}
