package com.kuku.kkmerchant.fragment;

import android.view.View;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;

/**
 *  闲置中
 * Created by wislie on 2017/9/21.
 */

public class EmptyOrderFragment extends BaseFragment {


    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initBizView(View view) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
