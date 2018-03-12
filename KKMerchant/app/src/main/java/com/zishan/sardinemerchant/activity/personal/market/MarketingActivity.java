package com.zishan.sardinemerchant.activity.personal.market;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseFragmentActivity;
import com.zishan.sardinemerchant.activity.page.LoadingUrlActivity;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.personal.MarketingFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * 营销活动
 * Created by wislie on 2017/12/12.
 */

public class MarketingActivity extends BaseFragmentActivity {

    @BindView(R.id.tab_layout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager mViewPager;


    //我的活动
    private final String my_activity_url = "https://h5.tenv.mttstudio.net/sardinemch/marketing/index.html";
    //新建活动
    private final String new_activity_url = "https://h5.tenv.mttstudio.net/sardinemch/marketing/index.html#/setactivity";
    //记录
    private final String record_url = "https://h5.tenv.mttstudio.net/sardinemch/marketing/index.html#/record";
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_marketing;
    }

    @Override
    protected void initContentView() {
        List<String> titles = new ArrayList<>(Arrays.asList("我的活动", "新建活动",""));

        final List<BFragment> fragments = new ArrayList<>();
        fragments.add(MarketingFragment.newInstance(my_activity_url));
        fragments.add(MarketingFragment.newInstance(new_activity_url));
        fragments.add(MarketingFragment.newInstance(record_url));
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给TabLayout设置适配器
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        //将TabLayout和ViewPager关联起来。
        xTablayout.setupWithViewPager(mViewPager);
        xTablayout.setOnTabSelectedListener(new XTabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());
                int position = xTablayout.getSelectedTabPosition();
                MarketingFragment fg = (MarketingFragment) fragments.get(position);
                if(fg != null){
                    fg.initData();
                }

            }
        });
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.custom_action_home, R.id.custom_text_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.custom_action_home:
                finish();
                break;
            //记录
            case R.id.custom_text_menu:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) { //这样写更合理
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            String url = bundle.getString("url");
            Intent intent = new Intent(this, LoadingUrlActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

}
