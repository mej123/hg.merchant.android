package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.page.TableArrangeTypeFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * Created by yang on 2017/10/13.
 * <p>
 * 预约提醒 桌台安排
 */

public class TableArrangeActivity extends BActivity {
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewpager;

    @BindView(R.id.tv_custom_name)
    TextView tvCustomName;
    @BindView(R.id.tv_number)
    TextView tvNumber;

    @BindView(R.id.tv_date)
    TextView mTableDateText;
    @BindView(R.id.tv_time)
    TextView mTableTimeText;
    @BindView(R.id.tv_count)
    TextView tvCount;


    private List<BFragment> mFragments = new ArrayList<>();
    private AppointmentRemindEntity mSeatData;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionbarTitle(getString(R.string.table_arrange));
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_arrange;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        int bespeak_id = 0;
        if (intent != null) {
            //落座数据
            mSeatData = (AppointmentRemindEntity) intent.getSerializableExtra("seat_data");

            if (mSeatData != null) {
                bespeak_id = mSeatData.getId();
                mFragments.add(TableArrangeTypeFragment.newInstance(bespeak_id, Constant.HALL));
                mFragments.add(TableArrangeTypeFragment.newInstance(bespeak_id, Constant.CARD));
                mFragments.add(TableArrangeTypeFragment.newInstance(bespeak_id, Constant.BOX));

                List<String> titles = new ArrayList<>(Arrays.asList("大厅", "卡座", "包厢"));
                FragmentAdapter adatper = new FragmentAdapter(getSupportFragmentManager(), mFragments, titles);
                viewpager.setAdapter(adatper);
                viewpager.setOffscreenPageLimit(mFragments.size());
                //将TabLayout和ViewPager关联起来。
                XTabLayout tabLayout = (XTabLayout) findViewById(R.id.xTablayout);
                tabLayout.setupWithViewPager(viewpager);

                initData();
            }
        }
    }

    private void initData() {
        if (mSeatData != null) {
            tvCustomName.setText(mSeatData.getBookerName());
            tvNumber.setText(mSeatData.getBookerPhone());
            mTableDateText.setText(DatePickerUtil.getFormatDate(mSeatData.getDinnerTime(), "yyyy-MM.dd"));
            mTableTimeText.setText(DatePickerUtil.getFormatDate(mSeatData.getDinnerTime(), "HH:mm"));
            tvCount.setText(String.valueOf(mSeatData.getDinnerNum()));
        }
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


}
