package com.kuku.kkmerchant.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;

import butterknife.BindView;
import top.ftas.ftaswidget.tablayout.SlidingTabLayout;
import top.ftas.ftaswidget.view.CustomToolBar;

/**
 * Created by yang on 2017/9/12.
 */

public class MainStoreFragement extends BaseFragment {

    @BindView(R.id.toolbar)
    CustomToolBar mToolbar;
    @BindView(R.id.slide_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.store_vp)
    ViewPager mVPage;

    private String[] titles = {"全部", "折扣", "推荐", "热菜", "冷盘"};
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_store;
    }

    @Override
    protected void initBizView(View view) {
        mVPage.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                //这里后面需要传递参数过去
                return GoodsListFragment.newInstance();
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabLayout.setViewPager(mVPage);
        mToolbar.setCustomToolBarListener(new CustomToolBar.CustomToolBarListener() {
            @Override
            public void back() {

            }

            @Override
            public void operate() {

            }
        });
    }


}
