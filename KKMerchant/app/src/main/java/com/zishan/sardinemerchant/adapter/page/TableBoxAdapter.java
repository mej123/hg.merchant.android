package com.zishan.sardinemerchant.adapter.page;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.TableBoxData;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 桌台包厢的适配器
 * Created by wislie on 2017/9/20.
 */

public class TableBoxAdapter extends BaseQuickAdapter<TableBoxData> {


    public TableBoxAdapter(int layoutResId, List<TableBoxData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TableBoxData data) {
        RelativeLayout tableBoxLayout = holder.getView(R.id.item_table_box);
        TextView tableNameText = holder.getView(R.id.table_box_name);
        if(data.getType() == Constant.HALL){
            tableBoxLayout.setBackgroundResource(R.mipmap.hall_tag_icon);
            tableNameText.setText("大厅");
        }else if(data.getType() == Constant.CARD){
            tableBoxLayout.setBackgroundResource(R.mipmap.card_tag_icon);
            tableNameText.setText("卡座");
        }else if(data.getType() == Constant.BOX){
            tableBoxLayout.setBackgroundResource(R.mipmap.box_tag_icon);
            tableNameText.setText("包厢");
        }

        TextView tablePersonCountText = holder.getView(R.id.table_box_setting);
        if(data.getTableBoxList() != null && data.getTableBoxList().size() > 0 ){
            tablePersonCountText.setText(data.getTableBoxList().size()+"个桌台");
        }else{
            tablePersonCountText.setText("未设置");
        }
    }
}
