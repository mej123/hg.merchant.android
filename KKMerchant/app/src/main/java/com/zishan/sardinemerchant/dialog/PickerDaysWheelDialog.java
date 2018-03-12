package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.List;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.WheelView;

/**
 * 预约天数的对话框
 * Created by wislie on 2017/9/29.
 */

public class PickerDaysWheelDialog extends CommonDialog implements View.OnClickListener {


    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private WheelView mDaysWheel;

    private String mTitle;

    private List<String> mDataList = new ArrayList<>();

    public static PickerDaysWheelDialog newInstance(String title) {
        PickerDaysWheelDialog dialog = new PickerDaysWheelDialog();
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
        return R.layout.dialog_days_pick;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        mDaysWheel = (WheelView) view.findViewById(R.id.wheel_days);

        mDialogConfirm.setOnClickListener(this);
        mDialogCancel.setOnClickListener(this);
        mDialogTitle.setText(mTitle);
        initData();
    }

    private void initData() {
        mDataList.add("当日");
        for (int i = 1; i <= 7; i++) {
            String day = i + "天";
            mDataList.add(day);
        }
        mDaysWheel.setData(mDataList);
        mDaysWheel.setDefault(0);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int animIn() {
        return R.anim.fade_in;
    }

    @Override
    public int animOut() {
        return R.anim.fade_out;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确定
            case R.id.dialog_confirm:
                if (mDialogListener != null) {
                    int pos = mDaysWheel.getSelected();
                    mDialogListener.onInputConfirm(mDataList.get(pos));
                    dismiss();
                }
                break;
            //取消
            case R.id.dialog_cancel:
                dismiss();
                break;
        }
    }
}
