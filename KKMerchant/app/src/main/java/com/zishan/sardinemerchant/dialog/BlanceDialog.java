package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * 结算
 * Created by wislie on 2017/12/4.
 */

public class BlanceDialog extends CommonDialog implements View.OnClickListener{

    public static BlanceDialog newInstance() {
        BlanceDialog dialog = new BlanceDialog();

        return dialog;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.operate_clean).setOnClickListener(this);
        view.findViewById(R.id.operate_blance).setOnClickListener(this);
        view.findViewById(R.id.operate_blance_clean).setOnClickListener(this);
        view.findViewById(R.id.operation_cancel_icon).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_blance;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            //清台
            case R.id.operate_clean:
                dismiss();
                if(mDialogListener != null){
                    mDialogListener.onInputConfirm(Constant.OPERATION_CLEAN);
                }
                break;
            //结算
            case R.id.operate_blance:
                dismiss();
                if(mDialogListener != null){
                    mDialogListener.onInputConfirm(Constant.OPERATION_BLANCE);
                }
                break;
            //清台并结算
            case R.id.operate_blance_clean:
                dismiss();
                if(mDialogListener != null){
                    mDialogListener.onInputConfirm(Constant.OPERATION_CLEAN_BLANCE);
                }
                break;
            //停止
            case R.id.operation_cancel_icon:
                dismiss();
                if(mDialogListener != null){
                    mDialogListener.onCancel();
                }
                break;
        }
    }
}
