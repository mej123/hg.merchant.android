package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

import java.util.List;

/**
 *
 * Created by wislie on 2018/1/12.
 */

public class StoreAddressAdapter extends BaseQuickAdapter<StoreMsgEntity> {

    private int selectedPos = -1;

    private int level = 0;

    public StoreAddressAdapter(int layoutResId, List<StoreMsgEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoreMsgEntity data) {

        TextView storeNameText = holder.getView(R.id.store_desc);
        if(level == 0 ){
            storeNameText.setText(data.getProvinceName());
        }else if(level == 1){
            storeNameText.setText(data.getCityName());
        }else if(level == 2 || level == 3 ){
            storeNameText.setText(data.getDistrictName());
        }

        ImageView storeIcon = holder.getView(R.id.store_selected_icon);
        if(selectedPos != holder.getAdapterPosition()){
            storeIcon.setVisibility(View.GONE);
            storeNameText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
        }else{
            storeIcon.setVisibility(View.VISIBLE);
            storeNameText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_blue_6));
        }
    }

    //设置选中
    public void setSelectedPos(int pos){
        if(selectedPos == pos) return;
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPos(){
        return selectedPos;
    }

    public void setLevel(int level){
        this.level = level;
    }

}
