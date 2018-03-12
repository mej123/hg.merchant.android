package com.kuku.kkmerchant.recycleviewadapter;

import android.content.Context;
import android.util.Log;

import com.kuku.kkmerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 先付后吃桌号的适配器
 * Created by wislie on 2017/9/20.
 */

public class PayBeforeEatAdapter extends CommonAdapter<String> {

    public PayBeforeEatAdapter(Context context, List<String> objects) {
        super(context, objects);
    }

    public int getItemLayoutId() {
        return R.layout.item_pay_before_eat;
    }

    @Override
    public void convert(CommonViewHolder holder, String data) {
        Log.e("Wislie","convert");
    }
}
