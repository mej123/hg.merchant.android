package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.List;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.WheelView;

/**
 * 商品添加 中的 分类
 * Created by wislie on 2017/10/27.
 */

public class DishClassifyDialog extends CommonDialog {


    private List<ProductGroupEntity> mDishList = new ArrayList<>();
    private WheelView mDishWheel;

    public static DishClassifyDialog newInstance(ArrayList<ProductGroupEntity> dishList) {
        DishClassifyDialog dialog = new DishClassifyDialog();
        Bundle data = new Bundle();
        data.putParcelableArrayList(Constant.CONFIG_PRODUCT_GROUP, dishList);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<ProductGroupEntity> dishList = getArguments().getParcelableArrayList(Constant.CONFIG_PRODUCT_GROUP);
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
            ProductGroupEntity bean = mDishList.get(i);
            dishNameList.add(bean.getName());
        }

        return dishNameList;
    }
}
