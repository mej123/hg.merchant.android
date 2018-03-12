package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.Arrays;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.DiffTextView;

/**
 * 未开通对话框
 * Created by wislie on 2017/12/11.
 */

public class GrantDialog extends CommonDialog {

    private String mTitle;
    private String mContent;

    public static GrantDialog newInstance(String title,String content) {
        GrantDialog dialog = new GrantDialog();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = (TextView) view.findViewById(R.id.dialog_title);
        TextView contentText = (TextView) view.findViewById(R.id.dialog_content);

        TextView confirmText = (TextView) view.findViewById(R.id.grant_confirm);
        titleText.setText(mTitle);
        contentText.setText(mContent);
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onConfirm();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_grant;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
