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
 * 两个输入框
 * Created by wislie on 2017/11/6.
 */

public class PairInputDialog extends CommonDialog {

    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;


    private EditText mInputVal1;
    private EditText mInputVal2;

    private String mTitle;
    private String mHintVal1;
    private String mHintVal2;
    private InputStyle mInputType1;
    private InputStyle mInputType2;

    public static PairInputDialog newInstance(String title, String hintVal1, String hintVal2,
                                              InputStyle inputStyle1, InputStyle inputStyle2) {
        PairInputDialog dialog = new PairInputDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("hintVal1", hintVal1);
        data.putString("hintVal2", hintVal2);
        data.putSerializable("inputStyle1", inputStyle1);
        data.putSerializable("inputStyle2", inputStyle2);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mHintVal1 = getArguments().getString("hintVal1");
        mHintVal2 = getArguments().getString("hintVal2");
        mInputType1 = (InputStyle) getArguments().getSerializable("inputStyle1");
        mInputType2 = (InputStyle) getArguments().getSerializable("inputStyle2");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_pair_input;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mInputVal1 = (EditText) view.findViewById(R.id.dialog_table_name);
        mInputVal2 = (EditText) view.findViewById(R.id.dialog_table_person_count);

        mDialogTitle.setText(mTitle);
        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mInputVal1.setHint(mHintVal1);
        mInputVal2.setHint(mHintVal2);

        if(mInputType1 == InputStyle.Number){
            mInputVal1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        }else if(mInputType1 == InputStyle.Text){
            mInputVal1.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }

        if(mInputType2 == InputStyle.Number){
            mInputVal2.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        }else if(mInputType2 == InputStyle.Text){
            mInputVal2.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
        mDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDialogListener != null) {
                    String val1 = mInputVal1.getText().toString();
                    if(TextUtils.isEmpty(val1)){
                        ToastUtil.show("输入名称不能为空");
                        return;
                    }
                    String val2 = mInputVal2.getText().toString();
                    if(TextUtils.isEmpty(val2)){
                        ToastUtil.show("输入的适合用餐人数不能为空");
                        return;
                    }
                    mDialogListener.onInputConfirm(val1, val2);
                }
                dismiss();
            }
        });
    }

    public enum InputStyle{
        Number, //数字
        Text //文字
    }
}
