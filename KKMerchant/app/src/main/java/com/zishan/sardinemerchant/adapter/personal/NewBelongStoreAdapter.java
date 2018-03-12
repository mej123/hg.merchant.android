package com.zishan.sardinemerchant.adapter.personal;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 归属店铺适配器
 */

public class NewBelongStoreAdapter extends BaseQuickAdapter<StoreMsgEntity> {
    public NewBelongStoreAdapter(int layoutResId, List<StoreMsgEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoreMsgEntity data) {
        if (data == null) return;
        TextView storeName = holder.getView(R.id.store_name);//店铺名称
        TextView provinceCityDistrict = holder.getView(R.id.province_city);//省市区
        TextView detailsAddress = holder.getView(R.id.details_address);//省市区
        ImageView checkIcon = holder.getView(R.id.check_icon);//选中icon
        storeName.setText(data.getStoreName());
        provinceCityDistrict.setText(data.getProvinceName() + "\t" + data.getCityName() + "\t" + data.getDistrictName());
        detailsAddress.setText(data.getAddress());
        if (data.isCheck()) {
            checkIcon.setImageResource(R.mipmap.checked_blue_icon);
        } else {
            checkIcon.setImageResource(R.mipmap.unchecked);
        }
    }
}
