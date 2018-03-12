package com.kuku.kkmerchant.recycleviewadapter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 商品管理下的列表
 * Created by wislie on 2017/9/20.
 */

public class GoodsOptionsAdapter extends CommonAdapter<String> {

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
    }
}
