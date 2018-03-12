package com.kuku.kkmerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 输入对话框（拒绝理由）
 * Created by wislie on 2017/9/26.
 */

public class InputDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private String mTitle;

    public static InputDialog newInstance(String title){
        InputDialog dialog = new InputDialog();
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
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        Log.e("Wislie","title:"+(mDialogTitle == null));
        mDialogTitle.setText(mTitle);
        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个需要监听器模式？
            }
        });
    }

}
