package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;


import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 确认取消对话框(含有标题)
 * Created by wislie on 2017/9/26.
 */

public class ConfirmOrCancelTitleDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogContent;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private String mTitle;
    private String mContent;
    public static ConfirmOrCancelTitleDialog newInstance(String title, String content) {
        ConfirmOrCancelTitleDialog dialog = new ConfirmOrCancelTitleDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("content", content);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mContent = getArguments().getString("content");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_title_confirm_or_cancel;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogContent = (TextView) view.findViewById(R.id.dialog_content);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mDialogTitle.setText(mTitle);
        mDialogContent.setText(mContent);
        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogListener != null){
                    mDialogListener.onCancel();
                }
                dismiss();
            }
        });

        mDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onConfirm();
                }

            }
        });
    }
}
