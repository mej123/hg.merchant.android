package com.zishan.sardinemerchant.adapter.store;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 商品管理下的列表
 * Created by wislie on 2017/9/20.
 */

public class GoodsOptionsAdapter extends CommonAdapter<String> {

    //默认情况下第一个为选中
    private int mSelectedPos = 0;

    public GoodsOptionsAdapter(Context context, List<String> objects) {
        super(context, objects);

    }

    public int getItemLayoutId() {
        return R.layout.item_store_goods_option;
    }

    @Override
    public void convert(CommonViewHolder holder, String data) {
        TextView goodOption = holder.getView(R.id.goods_option);
        goodOption.setText(data);
        if(holder.getLayoutPosition() == mSelectedPos){
            goodOption.setSelected(true);
        }else{
            goodOption.setSelected(false);
        }

    }

    public void notifySelectedPosition(int pos){
        if(mSelectedPos == pos) return;
        mSelectedPos = pos;
        notifyDataSetChanged();
    }
}
