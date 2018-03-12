package com.kuku.kkmerchant.utils;

import android.content.Context;
import android.content.Intent;

import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.BaseFragment;

import top.ftas.dunit.activity.SingleSupportFragmentActivity;

import static top.ftas.dunit.util.DUnitConstant.Sys.KEY_FRAGMENT_CLASS;

/**
 * Created by yang on 2017/8/28.
 */

public class Skip {

    public static void toFragment(Context context, Class<? extends BaseFragment> clazz) {
        Intent intent = new Intent(context, SingleSupportFragmentActivity.class);
        intent.putExtra(KEY_FRAGMENT_CLASS, clazz);
        context.startActivity(intent);

    }



    public static void toActivity(Context context, Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);

    }







}
