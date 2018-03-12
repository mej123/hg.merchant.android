package com.zishan.sardinemerchant.adapter.page;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.MyStoreStateEntity;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

import java.util.List;

/**
 * 门店状态
 * Created by wislie on 2018/1/12.
 */

public class StoreStateAdapter extends BaseQuickAdapter<MyStoreStateEntity> {

    private int selectedPos = -1;
    public StoreStateAdapter(int layoutResId, List<MyStoreStateEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyStoreStateEntity data) {

        TextView storeStateText = holder.getView(R.id.store_desc);
        storeStateText.setText(data.getName());

        ImageView storeIcon = holder.getView(R.id.store_selected_icon);
        if(selectedPos != holder.getAdapterPosition()){
            storeIcon.setVisibility(View.GONE);
            storeStateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_light_black));
        }else{
            storeIcon.setVisibility(View.VISIBLE);
            storeStateText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_blue_6));
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

}
