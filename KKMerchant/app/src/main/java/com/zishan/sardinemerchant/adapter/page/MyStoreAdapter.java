package com.zishan.sardinemerchant.adapter.page;

import android.util.Log;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 我的门店
 * Created by wislie on 2018/1/15.
 */

public class MyStoreAdapter extends BaseQuickAdapter<StoreMsgEntity> {
    private StringBuilder mLocationBuilder;

    public MyStoreAdapter(int layoutResId, List<StoreMsgEntity> data) {
        super(layoutResId, data);
        mLocationBuilder = new StringBuilder();
    }

    @Override
    protected void convert(BaseViewHolder holder, StoreMsgEntity data) {
        TextView storeNameText = holder.getView(R.id.store_name);
        storeNameText.setText(data.getStoreName());
        TextView storeStateText = holder.getView(R.id.store_state);
        //0:上架,1:下架，2停业整顿，3开始营业，4封停，5开店中

        if (data.getState() == 0) {
            storeStateText.setText("上架");
        } else if (data.getState() == 1) {
            storeStateText.setText("下架");
        } else if (data.getState() == 2) {
            storeStateText.setText("停业整顿");
            storeStateText.setBackgroundResource(R.drawable.my_store_state_repair_shape);
        } else if (data.getState() == 3) {
            storeStateText.setText("开始营业");
            storeStateText.setBackgroundResource(R.drawable.my_store_state_start_shape);
        } else if (data.getState() == 4) {
            storeStateText.setText("封停");
            storeStateText.setBackgroundResource(R.drawable.my_store_state_stop_shape);
        } else if (data.getState() == 5) {
            storeStateText.setText("开店中");
            storeStateText.setBackgroundResource(R.drawable.my_store_state_open_shape);
        }
        TextView storeLocationText = holder.getView(R.id.store_location);
        if(mLocationBuilder.length() > 0){
            mLocationBuilder.delete(0, mLocationBuilder.length());
        }
        storeLocationText.setText(mLocationBuilder.append(data.getProvinceName()).append("-")
                .append(data.getCityName()).append("-").append(data.getDistrictName()));
        TextView storeLocationDetailText = holder.getView(R.id.store_location_detail);
        storeLocationDetailText.setText("为" + data.getAddress());
        TextView storeDateText = holder.getView(R.id.store_date);
        storeDateText.setText(DatePickerUtil.getFormatDate(data.getContractTimestamp(), "yyyy-MM-dd"));
    }
}
