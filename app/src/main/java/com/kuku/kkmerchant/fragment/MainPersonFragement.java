package com.kuku.kkmerchant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;

/**
 * Created by yang on 2017/9/12.
 */

public class MainPersonFragement extends BaseFragment {
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_person;
    }

    @Override
    protected void initBizView(View view) {

    }



}
