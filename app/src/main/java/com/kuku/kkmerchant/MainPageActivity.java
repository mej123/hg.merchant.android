package com.kuku.kkmerchant;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.fragment.MainDataFragement;
import com.kuku.kkmerchant.fragment.MainPageFragement;
import com.kuku.kkmerchant.fragment.MainPersonFragement;
import com.kuku.kkmerchant.fragment.MainStoreFragement;
import com.kuku.kkmerchant.utils.CustomTabItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.ftas.ftaswidget.tabbar.tabswitch.helper.TabSwitchHelper;

/**
 * 首页
 * Created by yang on 2017/9/12.
 */

public class MainPageActivity  extends BaseActivity {

    @BindView(R.id.tab_item_main_page)
    ViewGroup itemMainpageView;
    @BindView(R.id.tab_item_store)
    ViewGroup itemStoreView;
    @BindView(R.id.tab_item_data)
    ViewGroup itemDataView;
    @BindView(R.id.tab_item_personal)
    ViewGroup itemPersonalView;

    private TabSwitchHelper mTabSwitchHelper;

    @Override
    protected int getLayoutResId() {
        return R.layout.mainpage_layout;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        mTabSwitchHelper = new TabSwitchHelper(R.id.fragmentContainer);
        mTabSwitchHelper.setAppCompatActivity(this);
        mTabSwitchHelper.addTabItem(new CustomTabItem(new MainPageFragement(), itemMainpageView, R.drawable.main_tab_item_icon_mainpage_selector, "首页"));
        mTabSwitchHelper.addTabItem(new CustomTabItem(new MainStoreFragement(), itemStoreView,R.drawable.main_tab_item_icon_store_selector, "商品"));
        mTabSwitchHelper.addTabItem(new CustomTabItem(new MainDataFragement(), itemDataView,R.drawable.main_tab_item_icon_data_selector, "数据"));
        mTabSwitchHelper.addTabItem(new CustomTabItem(new MainPersonFragement(), itemPersonalView, R.drawable.main_tab_item_icon_personal_selector, "个人"));
        mTabSwitchHelper.init();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
