package com.zishan.sardinemerchant.activity.personal.order;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.adapter.personal.DateSelectAdapter;
import com.zishan.sardinemerchant.dialog.PickerDateWheelDialog;
import com.zishan.sardinemerchant.entity.SelectDateData;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.personal.StoreOrderFragment;
import com.zishan.sardinemerchant.view.DropMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.DensityUtil;

import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * 店铺订单
 * Created by wislie on 2017/11/7.
 */

public class StoreOrderActivity extends BActivity {

    @BindView(R.id.drop_menu_date)
    DropMenu mDateDropMenu;

    @BindView(R.id.order_tab)
    XTabLayout mTabLayout;

    @BindView(R.id.store_order_date)
    TextView mOrderSelectDateText;

    @BindView(R.id.order_viewpager)
    NonSwipeableViewPager mViewPager;

    private View mPopView;
    private DateSelectAdapter mDateAdapter;

    //数据集合
    private List<List<SelectDateData>> mDataList = new ArrayList<>();
    //下拉框的集合
    private List<SelectDateData> mDropMenuList = new ArrayList<>();

    private List<BFragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_order;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getResources().getString(R.string.store_order));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
    }

    @Override
    protected void initContentView() {
        int position = 0;
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", position);
        }

        mFragments.add(StoreOrderFragment.newInstance(Constant.STORE_ORDER_ALL));
        mFragments.add(StoreOrderFragment.newInstance(new int[]{1, 2}, Constant.STORE_ORDER_RECIPIENT));
        mFragments.add(StoreOrderFragment.newInstance(new int[]{3}, Constant.STORE_ORDER_CHECK_TICKET));

        mTitles.add(getString(R.string.all));
        mTitles.add(getString(R.string.recipient_pay));
        mTitles.add(getString(R.string.scan_check_ticket));

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new XTabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                mDateDropMenu.hideDropMenu(StoreOrderActivity.this);
                mViewPager.setCurrentItem(tab.getPosition());
                List<SelectDateData> dataList = mDataList.get(tab.getPosition());
                SelectDateData data = getSelectData(dataList);
                mOrderSelectDateText.setText(data.getContent());

            }
        });
        mViewPager.setCurrentItem(position);

        initData();
        initSelectPopupView();
    }

    private void initData() {

        for (int i = 0; i < 3; i++) {
            List<SelectDateData> dataList = new ArrayList<>();
            initSelectData(dataList);
            mDataList.add(dataList);
        }
    }

    private void initSelectPopupView() {
        int selectedPos = mTabLayout.getSelectedTabPosition();
        mDropMenuList.clear();
        mDropMenuList.addAll(mDataList.get(selectedPos));
        SelectDateData data = getSelectData(mDropMenuList);
        mOrderSelectDateText.setText(data.getContent());
        //弹出框
        mPopView = LayoutInflater.from(this).inflate(R.layout.dropmenu_date_select, null);
        mPopView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        mDateAdapter = new DateSelectAdapter(this, mDropMenuList);

        RecyclerView dateRecycler = (RecyclerView) mPopView.findViewById(R.id.date_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        dateRecycler.setLayoutManager(gridLayoutManager);
        SpaceItemDecoration decoration = new SpaceItemDecoration(
                DensityUtil.dip2px(this, 4), DensityUtil.dip2px(this, 0),
                DensityUtil.dip2px(this, 4), DensityUtil.dip2px(this, 9));
        dateRecycler.addItemDecoration(decoration);
        dateRecycler.setAdapter(mDateAdapter);
        mDateAdapter.setOnItemListener(new DateAdapterListener(mDropMenuList));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = 1;
                if (position == mDropMenuList.size() - 1) {
                    spanSize = 2;
                }
                return spanSize;
            }
        });


        mDateDropMenu.initDropMenuView(this, mPopView, null, -1, -1);

    }

    private void initSelectData(List<SelectDateData> dateList) {
        if (dateList.size() > 0) return;
        //日期
        dateList.add(new SelectDateData("今日", DatePickerUtil.getStartTime(0), DatePickerUtil.getEndTime(0)));
        dateList.add(new SelectDateData("近3日", DatePickerUtil.getStartTime(-3), DatePickerUtil.getEndTime(0)));
        dateList.add(new SelectDateData("近7日", DatePickerUtil.getStartTime(-7), DatePickerUtil.getEndTime(0)));
        SelectDateData data = new SelectDateData("近30日", DatePickerUtil.getStartTime(-30), DatePickerUtil.getEndTime(0));
        data.setSelected(true);
        dateList.add(data);
        dateList.add(new SelectDateData("自定义"));
    }


    private void dispatchEvent() {
        int selectedPos = mTabLayout.getSelectedTabPosition();
        List<SelectDateData> dataList = mDataList.get(selectedPos);
        //选中的数据
        SelectDateData dateData = getSelectData(dataList);
        Long startTime = null;
        Long endTime = null;
        if (dateData != null) {
            startTime = dateData.getStart_time();
            endTime = dateData.getEnd_time();
        }

        SelectDateData newData = new SelectDateData(dateData.getContent(), startTime, endTime);

        int tagType = 0;
        switch (selectedPos) {
            case 0:
                tagType = Constant.STORE_ORDER_ALL;
                break;
            case 1:
                tagType = Constant.STORE_ORDER_RECIPIENT;
                break;
            case 2:
                tagType = Constant.STORE_ORDER_CHECK_TICKET;
                break;

        }
        mOrderSelectDateText.setText(dateData.getContent());
        BaseEventManager.post(newData, tagType, StoreOrderFragment.class.getName());
    }


    /**
     * 获得选中的数据
     *
     * @param dataList
     * @return
     */
    public SelectDateData getSelectData(List<SelectDateData> dataList) {
        if (dataList == null || dataList.size() == 0) return null;
        for (SelectDateData selectData : dataList) {
            if (selectData.isSelected()) return selectData;
        }
        return dataList.get(0);
    }



    private class DateAdapterListener extends CommonAdapter.OnAdapterItemListener {

        private List<SelectDateData> dateList;

        public DateAdapterListener(List<SelectDateData> dateList) {
            this.dateList = dateList;
        }

        @Override
        public void onItemClick(final View view, final int position) {
            /*final SelectDateData selectData = dateList.get(position);
            selectData.setSelected(!selectData.isSelected());
            view.setSelected(selectData.isSelected());

            if (selectData.isSelected()) {
                mDateAdapter.notifyAdapterData(position);
            }*/

            //这个和日期有关
            if (position == dateList.size() - 1) {

                /*if (!selectData.isSelected()) {
                    mDateAdapter.notifyAdapterData(position);
                    return;
                } else {
                    mDateAdapter.notifyAdapterData(position);
                }*/

                PickerDateWheelDialog dialog = PickerDateWheelDialog.newInstance("", true, false);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        SelectDateData selectData = dateList.get(position);
                        selectData.setSelected(!selectData.isSelected());
                        view.setSelected(selectData.isSelected());

                        if (selectData.isSelected()) {
                            mDateAdapter.notifyAdapterData(position);
                        }


                        long startTime = DatePickerUtil.getFutureStartTime(Integer.parseInt(values[0]),
                                Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                        long endTime = DatePickerUtil.getFutureEndTime(Integer.parseInt(values[3]),
                                Integer.parseInt(values[4]) - 1, Integer.parseInt(values[5]));
                        selectData.setStart_time(startTime);
                        selectData.setEnd_time(endTime);
                        String startDate = getSelectDate(values[0], values[1], values[2]);
                        String endDate = getSelectDate(values[3], values[4], values[5]);
                        selectData.setContent(startDate + "~" + endDate);
                        selectData.setSelected(true);
                        mDateAdapter.notifyAdapterData(position);

                        dispatchEvent();
                        mDateDropMenu.hideDropMenu(StoreOrderActivity.this);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }else{

                SelectDateData selectData = dateList.get(position);
                selectData.setSelected(!selectData.isSelected());
                view.setSelected(selectData.isSelected());

                if (selectData.isSelected()) {
                    mDateAdapter.notifyAdapterData(position);
                }

                dispatchEvent();
                mDateDropMenu.hideDropMenu(StoreOrderActivity.this);
            }

        }
    }


    private String getSelectDate(String year, String month, String day) {
        if (month.length() < 2) {
            month = "0" + month;
        }

        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "." + month + "." + day;
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.store_order_select_date})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.store_order_select_date:
                if (mDateDropMenu.isMenuVisible()) {
                    mDateDropMenu.hideDropMenu(StoreOrderActivity.this);
                    return;
                }

                int selectedPos = mTabLayout.getSelectedTabPosition();
                mDropMenuList.clear();
                mDropMenuList.addAll(mDataList.get(selectedPos));
                mDateAdapter.notifyDataSetChanged();
                mDateDropMenu.showDropMenu(StoreOrderActivity.this);
                break;
        }
    }


}
