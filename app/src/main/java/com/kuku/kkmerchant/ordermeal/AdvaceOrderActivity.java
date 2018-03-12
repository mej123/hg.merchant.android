package com.kuku.kkmerchant.ordermeal;


import android.support.v4.view.ViewPager;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Created by yang on 2017/9/13.
 */

public class AdvaceOrderActivity  extends BaseActivity {


    @Override
    protected int getLayoutResId() {
        return R.layout.advace_order;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.advace_order));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void initContentView() {
        final FragmentPagerItems fragmentPagerItems = FragmentPagerItems.with(this)
                .add("预约通知",AdvaceOrderNoticeFragment.class)
                .add("预约订单",AdvaceOrderOrderFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), fragmentPagerItems);

        ViewPager viewPager = (ViewPager) findViewById(R.id.advace_order_viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout smartTab = (SmartTabLayout) findViewById(R.id.account_smartTabLayout);
        smartTab.setViewPager(viewPager);

        smartTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentPagerItem pagerItem = fragmentPagerItems.get(position);
                setActionbarTitle(pagerItem.getTitle().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



}
