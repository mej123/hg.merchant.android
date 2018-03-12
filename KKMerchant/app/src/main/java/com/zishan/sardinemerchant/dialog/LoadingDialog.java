package com.zishan.sardinemerchant.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.dialog.CommonDialog;

/**
 * loading 加载提示
 * Created by wislie on 2017/9/5.
 */

public class LoadingDialog extends CommonDialog {

    public static LoadingDialog newInstance() {
        LoadingDialog dialog = new LoadingDialog();
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_loading;
    }


    @Override
    public boolean isCancelable() {
        return true;
    }


}
