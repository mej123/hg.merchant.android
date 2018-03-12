package com.zishan.sardinemerchant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kun on 2016/12/20.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<BFragment> mFragments;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<BFragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }



    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}