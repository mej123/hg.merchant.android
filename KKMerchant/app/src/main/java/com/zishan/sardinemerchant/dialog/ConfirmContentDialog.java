package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 比如下单成功对话框
 * Created by wislie on 2017/12/4.
 */

public class ConfirmContentDialog extends CommonDialog {


    private String mTitle;
    private String mContent;
    private boolean mIsCancelable;
    public static ConfirmContentDialog newInstance(String title, String content, boolean isCancelable) {
        ConfirmContentDialog dialog = new ConfirmContentDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("content", content);
        data.putBoolean("isCancelable", isCancelable);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mContent = getArguments().getString("content");
        mIsCancelable = getArguments().getBoolean("isCancelable");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm_content;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialogContent = (TextView) view.findViewById(R.id.dialog_content);
        TextView dialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        dialogTitle.setText(mTitle);
        dialogContent.setText(mContent);
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDialogListener != null) {
                    mDialogListener.onConfirm();
                }
                dismiss();

            }
        });
    }

    @Override
    public boolean isCancelable() {
        return mIsCancelable;
    }
}
