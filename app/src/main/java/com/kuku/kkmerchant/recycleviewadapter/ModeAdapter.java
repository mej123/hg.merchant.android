package com.kuku.kkmerchant.recycleviewadapter;

import android.content.Context;

import java.util.List;


/**
 * Created by Kun on 2016/12/14.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:模版
 */

public abstract class ModeAdapter<T> extends BaseAdapter<T> {


    public ModeAdapter(Context context, int layoutid, List datas) {
        super(context, layoutid, datas);
    }

    @Override
    public abstract void convert(ViewHolder holder, T t, int position);
}
