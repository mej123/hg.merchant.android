package com.kuku.kkmerchant.fragment;

import android.view.View;
import android.view.ViewGroup;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.utils.MainOrderTabItem;

import butterknife.BindView;
import top.ftas.ftaswidget.tabbar.tabswitch.helper.TabSwitchHelper;

/**
 * 大厅有 "新订单，就餐中， 闲置中, 已买单"
 * Created by wislie on 2017/9/21.
 */

public class HallFragment extends BaseFragment {

    @BindView(R.id.new_order)
    ViewGroup itemNewOrder;
    @BindView(R.id.at_meal)
    ViewGroup itemAtMeal;
    @BindView(R.id.empty_order)
    ViewGroup itemEmptyOrder;
    @BindView(R.id.have_order)
    ViewGroup itemHaveOrder;

    private TabSwitchHelper mTabSwitchHelper;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBizView(View view) {
        mTabSwitchHelper = new TabSwitchHelper(R.id.order_container);
        mTabSwitchHelper.setAppCompatActivity(getActivity());
        mTabSwitchHelper.addTabItem(new MainOrderTabItem(new NewOrderFragment(), itemNewOrder, "新订单"));
        mTabSwitchHelper.addTabItem(new MainOrderTabItem(new NewOrderFragment(), itemAtMeal, "就餐中"));
        mTabSwitchHelper.addTabItem(new MainOrderTabItem(new NewOrderFragment(), itemEmptyOrder, "闲置中"));
        mTabSwitchHelper.addTabItem(new MainOrderTabItem(new NewOrderFragment(), itemHaveOrder, "已买单"));
        mTabSwitchHelper.init();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
