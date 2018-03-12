package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 输入对话框
 * Created by wislie on 2017/9/26.
 */

public class PositionLookSaveDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;
    private EditText mInputContent;

    private String mTitle;
    private String mHintContent;
    private InputStyle mInputType;
    private String roleName;

    public static PositionLookSaveDialog newInstance(String title,String hintContent,String roleName,  InputStyle inputStyle) {
        PositionLookSaveDialog dialog = new PositionLookSaveDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("roleName",roleName);
        data.putString("hintContent", hintContent);
        data.putSerializable("inputStyle", inputStyle);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mHintContent = getArguments().getString("hintContent");
        mInputType = (InputStyle) getArguments().getSerializable("inputStyle");
        roleName = getArguments().getString("roleName");
    }


    @Override
    public int getLayoutId() {
        return R.layout.position_look_save;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mInputContent = (EditText) view.findViewById(R.id.dialog_input_edit);

        if (mInputType == InputStyle.Number) {
            mInputContent.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        } else if (mInputType == InputStyle.Text) {
            mInputContent.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        } else if (mInputType == InputStyle.NUMBER_DECIMAL) {
            mInputContent.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        }

        mInputContent.setText(roleName);//回显返回的角色名称
        mInputContent.setHint(mHintContent);
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
                String inputStr = mInputContent.getText().toString();
                if (TextUtils.isEmpty(inputStr)) {
                    ToastUtil.show("输入不能为空");
                    return;
                }

                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(inputStr);
                }
                dismiss();
            }
        });


    }


    public enum InputStyle {
        NUMBER_DECIMAL,
        Number, //数字
        Text //文字
    }

}
