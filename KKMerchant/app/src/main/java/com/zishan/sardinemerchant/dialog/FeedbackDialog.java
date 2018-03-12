package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.CustomEditText;

/**
 * 输入对话框（拒绝理由）
 * Created by wislie on 2017/9/26.
 */

public class FeedbackDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private String mTitle;

    private CustomEditText mInputContent;

    public static FeedbackDialog newInstance(String title) {
        FeedbackDialog dialog = new FeedbackDialog();
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
        return R.layout.dialog_feedback_input;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mInputContent = (CustomEditText) view.findViewById(R.id.dialog_input_edit);
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
                String inputStr = mInputContent.getInputEdit().getText().toString();
              /*  if (TextUtils.isEmpty(inputStr)) {
                    ToastUtil.show("输入不能为空");
                    return;
                }*/
                if (mDialogListener != null) {
                    dismiss();
                    mDialogListener.onInputConfirm(inputStr);
                }
                dismiss();

            }
        });
    }

}
