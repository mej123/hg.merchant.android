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
 * Created by yang on 2017/11/21.
 *
 * 添加员工
 *
 */

public class AddStaffDialog extends CommonDialog {

    private List<String> mDishList = new ArrayList<>();
    private WheelView mDishWheel;

    public static AddStaffDialog newInstance(ArrayList<String> dishList) {
        AddStaffDialog dialog = new AddStaffDialog();
        Bundle data = new Bundle();
        data.putStringArrayList("add_Staff_List", dishList);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> dishList = getArguments().getStringArrayList("add_Staff_List");
        if (dishList != null && dishList.size() > 0) {
            mDishList.clear();
            mDishList.addAll(dishList);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_dish_classify;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.dialog_confirm);
        mDishWheel = (WheelView) view.findViewById(R.id.dish_wheel);
        if (mDishList != null && mDishList.size() > 0) {
            mDishWheel.setData(getDishNameList());
            if (mDishList.size() == 1) {
                mDishWheel.setDefault(0);
            }
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPos = mDishWheel.getSelected();
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(String.valueOf(selectedPos));
                }
                dismiss();
            }
        });


    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
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
    public boolean dimAmountIsZero() {
        return true;
    }


    private List<String> getDishNameList() {
        List<String> dishNameList = new ArrayList<>();
        for (int i = 0; i < mDishList.size(); i++) {
            String  type = mDishList.get(i);
            dishNameList.add(type);
        }
        return dishNameList;
    }



}
