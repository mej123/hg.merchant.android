package com.zishan.sardinemerchant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 * Created by yang on 2017/8/28.
 */

public class Skip {





    public static void toActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);

    }







}
