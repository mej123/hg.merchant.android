package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import java.util.ArrayList;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.DiffTextView;

/**
 * 确认取消对话框
 * Created by wislie on 2017/9/26.
 */

public class ConfirmOrCancelDialog extends CommonDialog {

    private DiffTextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private String mTitle;
    private ArrayList<String> mTagArr;
    public static ConfirmOrCancelDialog newInstance(String title, ArrayList<String> tagArr) {
        ConfirmOrCancelDialog dialog = new ConfirmOrCancelDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putStringArrayList("tagArr", tagArr);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mTagArr = getArguments().getStringArrayList("tagArr");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm_or_cancel;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (DiffTextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mDialogTitle.setTitle(mTitle, mTagArr);

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
