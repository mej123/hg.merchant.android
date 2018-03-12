package com.zishan.sardinemerchant.activity;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.fragment.BFragment;

/**
 * 空页面
 * Created by wislie on 2017/12/18.
 */

public class EmptyFragment extends BFragment {
    public static EmptyFragment newInstance() {
        EmptyFragment fg = new EmptyFragment();
        return fg;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void initBizView() {

    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }
}
