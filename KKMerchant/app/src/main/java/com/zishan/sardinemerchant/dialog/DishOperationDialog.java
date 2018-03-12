package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 菜品操作
 * Created by wislie on 2017/12/4.
 */

public class DishOperationDialog extends CommonDialog implements View.OnClickListener, TextWatcher {

    //true表示已上菜
    private boolean mIsPut;
    //true表示免单,false表示取消免单
    private boolean mIsFree;

    //选中的状态
    private String mDishSelectedState = Constant.DISH_EDIT_PRICE;

    private TextView dishCancelText;
    private TextView dishFreeText;
    private TextView dishPutText;
    private EditText dishPriceEdit;
    private TableMenuItemEntity mData;


    //isput 表示是否已上菜
    public static DishOperationDialog newInstance(boolean isFree, boolean isPut, TableMenuItemEntity item) {
        DishOperationDialog dialog = new DishOperationDialog();
        Bundle data = new Bundle();
        data.putBoolean("isPut", isPut);
        data.putBoolean("isFree", isFree);
        data.putSerializable("item", item);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsPut = getArguments().getBoolean("isPut");
        mIsFree = getArguments().getBoolean("isFree");
        mData = (TableMenuItemEntity) getArguments().getSerializable("item");
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView dishNameText = (TextView) view.findViewById(R.id.dish_name);
        dishPriceEdit = (EditText) view.findViewById(R.id.dish_price_edit);
        TextView dishOriginPriceText = (TextView) view.findViewById(R.id.dish_origin_price);
        TextView dishSinglePriceText = (TextView) view.findViewById(R.id.dish_single_price);
        TextView dishNumText = (TextView) view.findViewById(R.id.dish_number);
        TextView cancleText = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView confirmText = (TextView) view.findViewById(R.id.dialog_confirm);
        dishCancelText = (TextView) view.findViewById(R.id.dish_cancel);
        dishFreeText = (TextView) view.findViewById(R.id.dish_free);
        dishPutText = (TextView) view.findViewById(R.id.dish_put);

        cancleText.setOnClickListener(this);
        confirmText.setOnClickListener(this);
        dishCancelText.setOnClickListener(this);
        dishFreeText.setOnClickListener(this);
        dishPutText.setOnClickListener(this);
        dishPriceEdit.addTextChangedListener(this);

        dishFreeText.setText(mIsFree ? "免单" : "取消免单");
        dishPutText.setVisibility(mIsPut ? View.VISIBLE : View.GONE);

        if (mData != null) {
            dishNameText.setText(mData.getProductName());
            dishPriceEdit.setText(StringUtil.point2String(mData.getValuationPrice() * mData.getNum()));
            dishOriginPriceText.setText(StringUtil.point2String(mData.getValuationPrice() * mData.getNum()));
            dishSinglePriceText.setText(StringUtil.point2String(mData.getValuationPrice()));
            dishNumText.setText(String.valueOf(mData.getNum()));
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_dish_operation;
    }

    @Override
    public int getHeight() {
        return DensityUtil.dip2px(getActivity(), 266);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //免单/取消免单
            case R.id.dish_free:
                dishCancelText.setSelected(false);
                dishPutText.setSelected(false);
                if (!dishFreeText.isSelected()) {
                    dishFreeText.setSelected(true);
                    mDishSelectedState = Constant.DISH_FREE;
                } else {
                    dishFreeText.setSelected(false);
                    mDishSelectedState = Constant.DISH_EDIT_PRICE;
                }
                break;
            //退菜
            case R.id.dish_cancel:
                dishFreeText.setSelected(false);
                dishPutText.setSelected(false);
                if (!dishCancelText.isSelected()) {
                    dishCancelText.setSelected(true);
                    mDishSelectedState = Constant.DISH_CANCEL;
                } else {
                    dishCancelText.setSelected(false);
                    mDishSelectedState = Constant.DISH_EDIT_PRICE;
                }
                break;
            //已上菜
            case R.id.dish_put:
                dishFreeText.setSelected(false);
                dishCancelText.setSelected(false);
                if (!dishPutText.isSelected()) {
                    dishPutText.setSelected(true);
                    mDishSelectedState = Constant.DISH_PUT;
                } else {
                    dishPutText.setSelected(false);
                    mDishSelectedState = Constant.DISH_EDIT_PRICE;
                }
                break;

            case R.id.dialog_confirm:
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(mDishSelectedState, dishPriceEdit.getText().toString().trim());
                }

                break;
            case R.id.dialog_cancel:
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onCancel();
                }
                break;
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        StringUtil.pointDigitLimited(s, 2);
    }
}
