package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 确认对话框
 * Created by wislie on 2017/9/26.
 */

public class ConfirmDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogConfirm;

    private String mTitle;

    public static ConfirmDialog newInstance(String title){
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");

    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        mDialogTitle.setText(mTitle);

        mDialogConfirm.setOnClickListener(new View.OnClickListener() {
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
