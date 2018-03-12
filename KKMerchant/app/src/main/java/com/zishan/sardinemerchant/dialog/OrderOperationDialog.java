package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 订单操作
 * Created by wislie on 2017/12/4.
 */

public class OrderOperationDialog extends CommonDialog implements View.OnClickListener {

    public static OrderOperationDialog newInstance() {
        OrderOperationDialog dialog = new OrderOperationDialog();

        return dialog;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.operation_back_icon).setOnClickListener(this);
        view.findViewById(R.id.operate_blance).setOnClickListener(this);
        view.findViewById(R.id.operate_merge).setOnClickListener(this);
        view.findViewById(R.id.operate_apart).setOnClickListener(this);
        view.findViewById(R.id.operate_tansmit).setOnClickListener(this);
        view.findViewById(R.id.operate_remove).setOnClickListener(this);
        view.findViewById(R.id.operate_choose).setOnClickListener(this);
        view.findViewById(R.id.operate_view).setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_order_operation;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.operation_back_icon:

                break;
            //结算
            case R.id.operate_blance:

                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_BLANCE);
                }
                break;
            //合并
            case R.id.operate_merge:
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_MERGE);
                }
                break;
            //拆台
            case R.id.operate_apart:

                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_APART);
                }
                break;
            //转台
            case R.id.operate_tansmit:
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_TRANSMIT);
                }
                break;
            //撤单
            case R.id.operate_remove:
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_REMOVE);
                }
                break;
            //点菜
            case R.id.operate_choose:
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_CHOOSE);
                }
                break;
            //查看订单总览
            case R.id.operate_view:
                if (mDialogListener != null) {
                    mDialogListener.onInputConfirm(Constant.OPERATION_VIEW);
                }
                break;
        }
    }
}
