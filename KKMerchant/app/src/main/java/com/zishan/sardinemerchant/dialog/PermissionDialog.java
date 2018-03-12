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
 * 权限对话框
 * Created by wislie on 2017/12/11.
 */

public class PermissionDialog extends CommonDialog {

    private String mTitle;
    private String content = "商家/店长并未赋予您该项功能权限";
    private ArrayList<String> tagArr = new ArrayList<>(Arrays.asList("商家", "店长"));

    public static PermissionDialog newInstance(String title) {
        PermissionDialog dialog = new PermissionDialog();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = (TextView) view.findViewById(R.id.dialog_title);
        DiffTextView contentText = (DiffTextView) view.findViewById(R.id.dialog_content);
        TextView confirmText = (TextView) view.findViewById(R.id.permission_confirm);
        titleText.setText(mTitle);
        contentText.setTitle(content, tagArr);
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
        return R.layout.dialog_permission;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
