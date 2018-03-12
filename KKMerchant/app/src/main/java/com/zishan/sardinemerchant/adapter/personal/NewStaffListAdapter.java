package com.zishan.sardinemerchant.adapter.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.model.MyStaffEntity;
import com.example.wislie.rxjava.model.personal.RoleEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

import static com.zishan.sardinemerchant.R.id.tv_belong_store_number;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新员工列表适配器
 */

public class NewStaffListAdapter extends BaseQuickAdapter<MyStaffEntity> {


    public NewStaffListAdapter(int layoutResId, List<MyStaffEntity> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder holder, MyStaffEntity data) {

        if (data == null) return;
        TextView staffName = holder.getView(R.id.tv_staff_name);//员工姓名
        TextView staffPosition = holder.getView(R.id.tv_staff_position);//员工职位
        TextView storeNum = holder.getView(tv_belong_store_number);//拥有店铺数量

        staffName.setText(data.getRealName());
        RoleEntity roleEntity = data.getRole();
        String storeIds = data.getStoreIds();

        if (roleEntity != null) {
            String name = roleEntity.getName();
            staffPosition.setText(name);
        }
        //控制当前员工对应门店数量的显示和隐藏
        if (data.isShowStoreNum()) {
            storeNum.setVisibility(View.VISIBLE);
            //区分三种情况  1.无所属门店   2.xxx家店铺  3.归属集团下所有店铺
            Boolean allStore = data.getAllStore();
            if (allStore) {
                storeNum.setText("归属集团下所有店铺");
            } else {
                if (!TextUtils.isEmpty(storeIds) && storeIds.length() > 0 && null != storeIds) {
                    int storeNumber = showStoreNumber(storeIds);//根据storeIds包含的“,”,去判断当前的门店数量
                    storeNum.setText(storeNumber + "家店铺");
                } else {
                    storeNum.setText("无所属门店");
                }
            }
        } else {
            storeNum.setVisibility(View.GONE);
        }
    }

    private int showStoreNumber(String storeIds) {
        int count = 0, start = 0;
        String sub = ",";
        while ((start = storeIds.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count + 1;
    }
}

