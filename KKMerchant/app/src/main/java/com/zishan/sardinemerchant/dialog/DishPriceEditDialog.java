package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 价格编辑
 * Created by wislie on 2017/12/10.
 */

public class DishPriceEditDialog extends CommonDialog implements View.OnClickListener, TextWatcher{

    private EditText mDishPriceEdit;
    private TableMenuItemEntity mData;

    public static DishPriceEditDialog newInstance(TableMenuItemEntity item) {
        DishPriceEditDialog dialog = new DishPriceEditDialog();
        Bundle data = new Bundle();
        data.putSerializable("item", item);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = (TableMenuItemEntity) getArguments().getSerializable("item");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView dishNameText = (TextView) view.findViewById(R.id.dish_name);
        mDishPriceEdit = (EditText) view.findViewById(R.id.dish_price_edit);
        TextView dishOriginPriceText = (TextView) view.findViewById(R.id.dish_origin_price);
        TextView dishSinglePriceText = (TextView) view.findViewById(R.id.dish_single_price);
        TextView dishNumText = (TextView) view.findViewById(R.id.dish_number);
        TextView cancleText = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView confirmText = (TextView) view.findViewById(R.id.dialog_confirm);

        mDishPriceEdit.addTextChangedListener(this);
        cancleText.setOnClickListener(this);
        confirmText.setOnClickListener(this);

        if (mData != null) {
            dishNameText.setText(mData.getProductName());
            mDishPriceEdit.setText(StringUtil.point2String(mData.getValuationPrice() * mData.getNum()));
            dishOriginPriceText.setText(StringUtil.point2String(mData.getValuationPrice() * mData.getNum()));
            dishSinglePriceText.setText(StringUtil.point2String(mData.getValuationPrice()));
            dishNumText.setText(String.valueOf(mData.getNum()));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_dish_edit_price;
    }


    @Override
    public int getHeight() {
        return DensityUtil.dip2px(getActivity(), 235);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //取消
            case R.id.dialog_cancel:
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onCancel();
                }
                break;
            //确定
            case R.id.dialog_confirm:
                String inputPrice = mDishPriceEdit.getText().toString().trim();
                if (TextUtils.isEmpty(inputPrice)) {
                    ToastUtil.show("输入不能为空");
                    return;
                }
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(inputPrice);
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
