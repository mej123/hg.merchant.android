package com.zishan.sardinemerchant.adapter.personal;

import android.widget.TextView;

import com.example.wislie.rxjava.model.OperatingRecordEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/10/29.
 * <p>
 * 新 操作记录  适配器
 */

public class NewOperatingRecordAdapter extends BaseQuickAdapter<OperatingRecordEntity> {

    public NewOperatingRecordAdapter(int layoutResId, List<OperatingRecordEntity> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, OperatingRecordEntity operatingRecordEntity) {
        if (operatingRecordEntity == null) return;
        TextView updateContent = holder.getView(R.id.tv_operating_record);//操作内容
        updateContent.setText(operatingRecordEntity.getDescription());
        showTime(holder, operatingRecordEntity);//显示时间
    }

    private void showTime(BaseViewHolder holder, OperatingRecordEntity data) {

        Long withdrawTime = data.getDatestamp();
        //将提现时间转化成年月日格式
        String withdrawTimeYMD = DatePickerUtil.getFormatDate(withdrawTime, "yyyy-MM-dd");//得到当前提现时间的格式为年月日
        //获取系统的今天和明天的年月日格式的日期,然后比对判断属于今天还是昨天
        //今日
        Long todayTime = DatePickerUtil.getEndTime(0);
        //昨日
        //Long yesterdayTime = DatePickerUtil.getEndTime(-1);

        //将时间转化为指定格式
        String todayYMD = DatePickerUtil.getFormatDate(todayTime, "yyyy-MM-dd");//年月日  今天

        //String yesterdayYMD = DatePickerUtil.getFormatDate(yesterdayTime, "yyyy-MM-dd");//年月日  昨天

        String withdrawHMS = DatePickerUtil.getFormatDate(withdrawTime, "HH:mm:ss");//提现时间 时分秒

        if (withdrawTimeYMD.equals(todayYMD)) {

            holder.setText(R.id.tv_time, "今天\t\t" + withdrawHMS);//修改时间

        } else {
            holder.setText(R.id.tv_time, DatePickerUtil.getFormatDate(withdrawTime, "yyyy-MM-dd   HH:mm:ss"));//完整格式时间
        }
    }
}
