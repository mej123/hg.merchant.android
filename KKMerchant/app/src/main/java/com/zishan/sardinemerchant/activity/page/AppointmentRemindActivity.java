package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseFragmentActivity;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.page.AppointmentRemindInformFragment;
import com.zishan.sardinemerchant.fragment.page.AppointmentRemindPandectFragment;

import com.zishan.sardinemerchant.fragment.page.AppointmentRemindSeatedFragment;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;


/**
 * Created by yang on 2017/10/13.
 * <p>
 * 预约提醒 落座
 */

public class AppointmentRemindActivity extends BaseFragmentActivity {

    @BindView(R.id.custom_text_menu)
    TextView mRightText;
    @BindView(R.id.tab_layout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager mViewPager;

    private int mSelectedPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_appointment_remind;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if(intent != null){
            mSelectedPosition = intent.getIntExtra("selected_pos", mSelectedPosition);
        }

        List<String> titles = new ArrayList<>(Arrays.asList("落座", "通知", "总览"));
        List<BFragment> fragments = new ArrayList<>();
        fragments.add(AppointmentRemindSeatedFragment.newInstance(Constant.APPOINTMENT_SEATED, false, true));
        fragments.add(AppointmentRemindInformFragment.newInstance(Constant.APPOINTMENT_INFORM, false));
        fragments.add(AppointmentRemindPandectFragment.newInstance());
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
                if (position == 0 || position == 1) {
                    mRightText.setText("");
                }

                if (position == 2) {
                    mRightText.setText("历史");
                    mRightText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Skip.toActivity(AppointmentRemindActivity.this,
                                    AppointmentPandectHistoryActivity.class);
                        }
                    });
                }
            }
        });
        mViewPager.setCurrentItem(mSelectedPosition);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.custom_action_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_action_home:
                finish();
                break;
        }
    }


}
