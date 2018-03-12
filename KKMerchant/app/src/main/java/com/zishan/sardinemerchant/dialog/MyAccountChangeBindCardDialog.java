package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * Created by yang on 2017/11/8.
 * <p>
 * 更换绑定银行卡弹窗提醒框
 */

public class MyAccountChangeBindCardDialog extends CommonDialog {

    private TextView mDialogChangeBind;

    private TextView mDialogCancel;
    private String mChangeBind;
    private String mCancel;


    public static MyAccountChangeBindCardDialog newInstance(String changeBind, String cancel) {
        MyAccountChangeBindCardDialog dialog = new MyAccountChangeBindCardDialog();
        Bundle data = new Bundle();
        data.putString("changeBind", changeBind);
        data.putString("cancel", cancel);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChangeBind = getArguments().getString("changeBind");
        mCancel = getArguments().getString("cancel");

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_change_bind_bank_card;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogChangeBind = (TextView) view.findViewById(R.id.tv_change_bind);//更换绑定
        mDialogCancel = (TextView) view.findViewById(R.id.tv_cancel);//取消
        mDialogChangeBind.setText(mChangeBind);
        mDialogCancel.setText(mCancel);

        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDialogChangeBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onConfirm();
                }
                dismiss();
            }
        });
    }


}
