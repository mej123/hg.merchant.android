package com.kuku.kkmerchant.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

/**
 * Created by yang on 2017/9/6.
 */

public class DialogUtils {

    //弹出带标题取消和确定按钮的对话框
    public static void showQuxiaoDingdanDialog(Context context, String content, String sureText, String cancelText, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View verify = inflater.inflate(R.layout.dialog_custom_alert, null);
        builder.setView(verify);
        TextView tvContent = (TextView) verify.findViewById(R.id.tv_content);
        tvContent.setText(content);
        TextView sure = (TextView) verify.findViewById(R.id.tv_sure);
        sure.setText(sureText);
        TextView cancel = (TextView) verify.findViewById(R.id.tv_cancel);
        cancel.setText(cancelText);
        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        //创建对话框
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);    //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    if (onClickListener != null) {
                        onClickListener.onClick(alertDialog, 1);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    if (onClickListener != null) {
                        onClickListener.onClick(alertDialog, 0);
                    }
                }
            }
        });
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

}
