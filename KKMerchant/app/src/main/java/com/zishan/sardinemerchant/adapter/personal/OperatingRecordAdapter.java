package com.zishan.sardinemerchant.adapter.personal;

import android.view.View;
import android.widget.ImageView;

import com.example.wislie.rxjava.model.OperatingRecordEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/10/29.
 * <p>
 * 操作记录  适配器
 */

public class OperatingRecordAdapter extends BaseQuickAdapter<OperatingRecordEntity> {

    List<OperatingRecordEntity> mDataList;

    public OperatingRecordAdapter(int layoutResId, List<OperatingRecordEntity> data) {
        super(layoutResId, data);
        mDataList = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, OperatingRecordEntity operatingRecordEntity) {
        if (operatingRecordEntity == null) return;
        int position = holder.getAdapterPosition();
        int currentposition = position - 1;
        String currentStr = DatePickerUtil.getFormatDate(operatingRecordEntity.getDatestamp(), "yyyy-MM.dd");
        String previewStr = (currentposition) >= 0 ? DatePickerUtil.getFormatDate(mDataList.get(
                currentposition).getDatestamp(), "yyyy-MM.dd") : "-1";
        if (!previewStr.equals(currentStr)) {
            holder.getView(R.id.rl_title_time).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_time, currentStr);
        } else {
            holder.getView(R.id.rl_title_time).setVisibility(View.GONE);
            holder.setText(R.id.tv_time, "");
        }

        ImageView hintIcon = holder.getView(R.id.record_tag);
        String tag = operatingRecordEntity.getTag();
        if (tag.equals("yellow")) {
            hintIcon.setImageResource(R.mipmap.orange_circle_icon);
        } else if (tag.equals("blue")) {
            hintIcon.setImageResource(R.mipmap.blue_circle_icon);
        }
        holder.setText(R.id.tv_operating_record, operatingRecordEntity.getDescription());

        holder.setText(R.id.employee_name_role, "("+operatingRecordEntity.getEmployeeName()+"\t\t"+ operatingRecordEntity.getRoleName()+")");
    }
}
