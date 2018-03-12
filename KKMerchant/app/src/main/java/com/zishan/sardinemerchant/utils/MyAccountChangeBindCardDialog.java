package com.zishan.sardinemerchant.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.listener.OnclickChangeBindCardListener;


/**
 * Created by Administrator on 2016/11/22.
 *
 * 更换绑定银行卡弹窗提醒框
 *
 */

public class MyAccountChangeBindCardDialog extends Dialog {

    private OnclickChangeBindCardListener mListener;

    public MyAccountChangeBindCardDialog(Context context) {
        super(context, R.style.MyDialogStyleTop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_bind_bank_card);
        TextView mChangeBind = (TextView) findViewById(R.id.tv_change_bind);
        TextView mCancel = (TextView) findViewById(R.id.tv_cancel);
        setCanceledOnTouchOutside(false);

//        Window window = this.getWindow();
//        window.getDecorView().setPadding(28, 0, 28, 0);
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        mChangeBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void onClickListener(OnclickChangeBindCardListener onclickChangeBindCardListener) {
        this.mListener = onclickChangeBindCardListener;
    }

}
