package com.zishan.sardinemerchant.activity.page;

import android.support.v4.content.ContextCompat;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.page.AppointmentRemindRefuseFragment;
import com.zishan.sardinemerchant.fragment.page.AppointmentRemindSeatedFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * Created by yang on 2017/10/13.
 *
 * 预约提醒  总览历史
 *
 */
public class AppointmentPandectHistoryActivity extends BActivity {

    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewpager;

    @Override
    protected int getLayoutResId() {
        return R.layout.appointment_pandect_history;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionbarTitle(getString(R.string.history));
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        List<BFragment> fragments = new ArrayList<>();
        fragments.add(AppointmentRemindSeatedFragment.newInstance(Constant.APPOINTMENT_SEATED, false, false));
        fragments.add(AppointmentRemindRefuseFragment.newInstance(Constant.APPOINTMENT_REFUSE, false));
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, Arrays.asList("已接受", "已拒绝"));
        //给TabLayout设置适配器
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(fragments.size());
        //将TabLayout和ViewPager关联起来。
        xTablayout.setupWithViewPager(viewpager);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }


}
