package com.kuku.kkmerchant;


import android.support.v4.view.ViewPager;

import com.kuku.kkmerchant.loginorregist.PhoneLoginFragment;
import com.kuku.kkmerchant.loginorregist.PswLoginFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;



/**
 * Created by yang on 2017/9/4.
 */

public class LoginMainActivity  extends BaseActivity1 {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main1;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.title_login));
       // setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }


    @Override
    protected void initContentView() {
        final FragmentPagerItems fragmentPagerItems = FragmentPagerItems.with(this)
                .add("密码登录",PswLoginFragment.class)
                .add("手机登录",PhoneLoginFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), fragmentPagerItems);

        ViewPager viewPager = (ViewPager) findViewById(R.id.account_viewPager);
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
