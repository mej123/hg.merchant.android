package com.zishan.sardinemerchant.fragment.page;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;

import butterknife.BindView;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * 先付后吃
 * Created by wislie on 2017/9/12.
 */

public class MainPagePayBeforeEatFragment extends BFragment {

    @BindView(R.id.dish_tab_layout)
    TabLayout mDishLayout;
    @BindView(R.id.dish_divder)
    View mDishView;
    @BindView(R.id.dish_vp)
    NonSwipeableViewPager mViewPager;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    private ArrayList<PayBeforeEatOrderFragment> mFragments = new ArrayList<>();

    private ArrayList<String> mTitles = new ArrayList<String>();


    public static MainPagePayBeforeEatFragment newInstance() {
        MainPagePayBeforeEatFragment fg = new MainPagePayBeforeEatFragment();
        return fg;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_page_pay_before_eat;
    }

    @Override
    protected void initBizView() {
        mTitles.add("未出菜");
        mTitles.add("已出菜");
        mFragments.add(PayBeforeEatOrderFragment.newInstance(false));
        mFragments.add(PayBeforeEatOrderFragment.newInstance(true));

        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                //这里后面需要传递参数过去
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });
        mDishLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTitles.size(); i++) {
            TabLayout.Tab tab = mDishLayout.getTabAt(i);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_out_dish_layout, null);
            tab.setCustomView(view);

            TextView tabTitleText = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
            ImageView tabMsgIcon = (ImageView) tab.getCustomView().findViewById(R.id.tab_msg);
            if (i == 0) {
                tabTitleText.setSelected(true);
            }

            tabTitleText.setText(mTitles.get(i));
        }

        mDishLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_title).setSelected(true);
                tab.getCustomView().findViewById(R.id.tab_indicator).setSelected(true);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_title).setSelected(false);
                tab.getCustomView().findViewById(R.id.tab_indicator).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {

    }

}

