package com.zishan.sardinemerchant.fragment.page;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.adapter.FragmentAdapter;
import com.zishan.sardinemerchant.adapter.page.AppointmentSelectAdapter;
import com.zishan.sardinemerchant.dialog.PickerMonthDayWheelDialog;
import com.zishan.sardinemerchant.entity.SelectData;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.view.DropMenu;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by yang on 2017/10/13.
 * <p>
 * 预约总览  fragment
 */

public class AppointmentRemindPandectFragment extends BFragment {


    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewpager;

    @BindView(R.id.pandect_tablayout)
    View mTabLayout;
    @BindView(R.id.appointment_divder)
    View mUnderLine;

    //下拉菜单
    @BindView(R.id.drop_menu)
    DropMenu mDropMenu;
    @BindView(R.id.select_icon)
    ImageView mSelectIcon;
    private List<BFragment> fragments = new ArrayList<>();

    //弹出框
    private View mPopView;
    private RecyclerView mDateRecycler;
    private RecyclerView mSeatRecycler;
    private RecyclerView mPersonRecycler;

    private AppointmentSelectAdapter mDateAdapter;
    private AppointmentSelectAdapter mSeatAdapter;
    private AppointmentSelectAdapter mPersonAdapter;

    private List<SelectData> mUndealDateList = new ArrayList<>();
    private List<SelectData> mUndealSeatList = new ArrayList<>();
    private List<SelectData> mUndealPersonList = new ArrayList<>();


    private List<SelectData> mAcceptDateList = new ArrayList<>();
    private List<SelectData> mAcceptSeatList = new ArrayList<>();
    private List<SelectData> mAcceptPersonList = new ArrayList<>();

    private List<SelectData> mRefuseDateList = new ArrayList<>();
    private List<SelectData> mRefuseSeatList = new ArrayList<>();
    private List<SelectData> mRefusePersonList = new ArrayList<>();


    private List<SelectData> mDateList = new ArrayList<>();
    private List<SelectData> mSeatList = new ArrayList<>();
    private List<SelectData> mPersonList = new ArrayList<>();



    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_appointment_remind_pandect;
    }

    public static AppointmentRemindPandectFragment newInstance() {
        AppointmentRemindPandectFragment fg = new AppointmentRemindPandectFragment();
        return fg;
    }

    @Override
    protected void initBizView() {
        fragments.add(AppointmentRemindInformFragment.newInstance(Constant.APPOINTMENT_INFORM, true));
        fragments.add(AppointmentRemindSeatedFragment.newInstance(Constant.APPOINTMENT_SEATED, true, false));
        fragments.add(AppointmentRemindRefuseFragment.newInstance(Constant.APPOINTMENT_REFUSE, true));

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragments,
                Arrays.asList("未处理", "已接受", "已拒绝"));
        //给TabLayout设置适配器
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(fragments.size());
        //将TabLayout和ViewPager关联起来。
        xTablayout.setupWithViewPager(viewpager);
        xTablayout.setOnTabSelectedListener(new XTabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {

                if(tab.getPosition() == viewpager.getCurrentItem()) return;
                viewpager.setCurrentItem(tab.getPosition());
                updateDropMenu();
            }
        });

        mDateList.addAll(mUndealDateList);
        mSeatList.addAll(mUndealSeatList);
        mPersonList.addAll(mUndealPersonList);
        initSelectData(mDateList, mSeatList, mPersonList);

        initSelectPopupView();
    }

    private void initSelectPopupView() {

        //弹出框
        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.item_book_select, null);
        mPopView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        mDateRecycler = (RecyclerView) mPopView.findViewById(R.id.appointment_date_recycler);
        mSeatRecycler = (RecyclerView) mPopView.findViewById(R.id.appointment_seat_recycler);
        mPersonRecycler = (RecyclerView) mPopView.findViewById(R.id.appointment_person_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mDateRecycler.setLayoutManager(gridLayoutManager);
        mSeatRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mPersonRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        SpaceItemDecoration decoration = new SpaceItemDecoration(
                DensityUtil.dip2px(getActivity(), 4), DensityUtil.dip2px(getActivity(), 0),
                DensityUtil.dip2px(getActivity(), 4), DensityUtil.dip2px(getActivity(), 9));
        mDateRecycler.addItemDecoration(decoration);
        mSeatRecycler.addItemDecoration(decoration);
        mPersonRecycler.addItemDecoration(decoration);

        mDateAdapter = new AppointmentSelectAdapter(getActivity(), mDateList);
        mDateRecycler.setAdapter(mDateAdapter);
        mSeatAdapter = new AppointmentSelectAdapter(getActivity(), mSeatList);
        mSeatRecycler.setAdapter(mSeatAdapter);
        mPersonAdapter = new AppointmentSelectAdapter(getActivity(), mPersonList);
        mPersonRecycler.setAdapter(mPersonAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = 1;
                if (position == mDateList.size() - 1) {
                    spanSize = 4;
                }
                return spanSize;
            }
        });

        SelectItemOnClickListener dateItemListener = new SelectItemOnClickListener(mDateAdapter, mDateList);
        mDateAdapter.setOnItemListener(dateItemListener);
        SelectItemOnClickListener seatItemListener = new SelectItemOnClickListener(mSeatAdapter, mSeatList);
        mSeatAdapter.setOnItemListener(seatItemListener);
        SelectItemOnClickListener personItemListener = new SelectItemOnClickListener(mPersonAdapter, mPersonList);
        mPersonAdapter.setOnItemListener(personItemListener);
        mPopView.findViewById(R.id.clear_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空
                clearSelectData();
            }
        });

        mPopView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPos = xTablayout.getSelectedTabPosition();
                String className = fragments.get(selectedPos).getClass().getName();
                SelectData dateData = getSelectData(mDateList);
                SelectData seatData = getSelectData(mSeatList);
                SelectData personData = getSelectData(mPersonList);

                Long startTime = null;
                Long endTime = null;
                Integer needRoom = null;
                Integer minNum = null;
                Integer maxNum = null;

                if (dateData != null) {
                    startTime = dateData.getStart_time();
                    endTime = dateData.getEnd_time();
                }

                if (seatData != null) {
                    needRoom = seatData.getNeed_room();
                }

                if (personData != null) {
                    minNum = personData.getMin_dinner_num();
                    maxNum = personData.getMax_dinner_num();
                }

                String dateContent = "";
                String seatContent = "";
                String numContent = "";

                if (dateData != null) {
                    dateContent = dateData.getDateContent();
                }
                if (seatData != null) {
                    seatContent = seatData.getSeatContent();
                }
                if (personData != null) {
                    numContent = personData.getNumContent();
                }

                SelectData newData = new SelectData(dateContent, seatContent, numContent,
                        startTime, endTime, needRoom, minNum, maxNum);
                BaseEventManager.post(newData, className);

                mDropMenu.hideDropMenu(getActivity());

            }
        });
        mDropMenu.initDropMenuView(getActivity(), mPopView, mSelectIcon,
                R.mipmap.arrow_bottom, R.mipmap.arrow_top);
    }


    private void initSelectData(List<SelectData> dateList, List<SelectData> seatList, List<SelectData> personList) {

        if (dateList.size() > 0 || seatList.size() > 0 || personList.size() > 0) return;

        //日期
        dateList.add(new SelectData("今日", DatePickerUtil.getStartTime(0), DatePickerUtil.getEndTime(0)));
        dateList.add(new SelectData("明日", DatePickerUtil.getStartTime(1), DatePickerUtil.getEndTime(1)));
        dateList.add(new SelectData("未来3日", DatePickerUtil.getStartTime(3), DatePickerUtil.getEndTime(3)));
        dateList.add(new SelectData("未来7日", DatePickerUtil.getStartTime(7), DatePickerUtil.getEndTime(7)));
        dateList.add(new SelectData("自定义"));

        //卡座 包厢
        seatList.add(new SelectData("卡座", 0));
        seatList.add(new SelectData("包厢", 1));

        //人数
        personList.add(new SelectData("2", 2, 2));
        personList.add(new SelectData("2~6", 2, 6));
        personList.add(new SelectData("6~10", 6, 10));
        personList.add(new SelectData("10人以上", 10, 10));
    }


    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.select_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            //弹出筛选框
            case R.id.select_icon:
                if (mDropMenu.isMenuVisible()) {
                    mDropMenu.hideDropMenu(getActivity());
                    return;
                }
                updateDropMenu();
                mDropMenu.showDropMenu(getActivity());

                break;
        }
    }

    private void updateDropMenu(){
        int selectedPos = xTablayout.getSelectedTabPosition();
        mDateList.clear();
        mSeatList.clear();
        mPersonList.clear();
        switch (selectedPos) {
            case 0:
                initSelectData(mUndealDateList, mUndealSeatList, mUndealPersonList);
                mDateList.addAll(mUndealDateList);
                mSeatList.addAll(mUndealSeatList);
                mPersonList.addAll(mUndealPersonList);
                break;
            case 1:
                initSelectData(mAcceptDateList, mAcceptSeatList, mAcceptPersonList);
                mDateList.addAll(mAcceptDateList);
                mSeatList.addAll(mAcceptSeatList);
                mPersonList.addAll(mAcceptPersonList);
                break;
            case 2:
                initSelectData(mRefuseDateList, mRefuseSeatList, mRefusePersonList);
                mDateList.addAll(mRefuseDateList);
                mSeatList.addAll(mRefuseSeatList);
                mPersonList.addAll(mRefusePersonList);
                break;
        }
        mDateAdapter.notifyDataSetChanged();
        mSeatAdapter.notifyDataSetChanged();
        mPersonAdapter.notifyDataSetChanged();

    }

    private class SelectItemOnClickListener extends CommonAdapter.OnAdapterItemListener {
        private AppointmentSelectAdapter adapter;
        private List<SelectData> dataList;

        public SelectItemOnClickListener(AppointmentSelectAdapter adapter, List<SelectData> dataList) {
            this.adapter = adapter;
            this.dataList = dataList;
        }

        @Override
        public void onItemClick(View view, final int position) {
            final SelectData selectData = dataList.get(position);
            selectData.setSelected(!selectData.isSelected());
            view.setSelected(selectData.isSelected());

            if (selectData.isSelected()) {
                adapter.notifyAdapterData(position);
            }

            if (position != mDateList.size() - 1) return;

            //最后一个是自定义日期
            if (!selectData.isSelected()) {
                mDateAdapter.notifyAdapterData(position);
                return;
            } else {
                mDateAdapter.notifyAdapterData(position);
            }

            PickerMonthDayWheelDialog dialog = PickerMonthDayWheelDialog.newInstance("");
            dialog.showDialog(getActivity().getFragmentManager());
            dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                @Override
                public void onConfirm() {

                }

                @Override
                public void onInputConfirm(String... values) {

                    long startTime = DatePickerUtil.getFutureStartTime(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                    long endTime = DatePickerUtil.getFutureEndTime(Integer.parseInt(values[3]),
                            Integer.parseInt(values[4]) - 1, Integer.parseInt(values[5]));
                    selectData.setStart_time(startTime);
                    selectData.setEnd_time(endTime);
                    String startDate = getSelectDate(values[1], values[2]);
                    String endDate = getSelectDate(values[4], values[5]);
                    selectData.setDateContent(startDate + "~" + endDate);
                    selectData.setSelected(true);
                    mDateAdapter.notifyAdapterData(position);
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }


    private void clearSelectData() {

        for (int i = 0; i < mDateList.size(); i++) {
            SelectData data = mDateList.get(i);
            data.setSelected(false);
            if (i == mDateList.size() - 1) {
                data.setDateContent("自定义");
            }
        }

        for (int i = 0; i < mSeatList.size(); i++) {
            SelectData data = mSeatList.get(i);
            data.setSelected(false);
        }

        for (int i = 0; i < mPersonList.size(); i++) {
            SelectData data = mPersonList.get(i);
            data.setSelected(false);
        }

        mDateAdapter.notifyDataSetChanged();
        mSeatAdapter.notifyDataSetChanged();
        mPersonAdapter.notifyDataSetChanged();
    }

    /**
     * 获得选中的数据
     *
     * @param dataList
     * @return
     */
    public SelectData getSelectData(List<SelectData> dataList) {
        for (SelectData selectData : dataList) {
            if (selectData.isSelected()) return selectData;
        }
        return null;
    }

    private String getSelectDate(String month, String day) {
        return month + "月" + day + "日";
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mDropMenu != null) {
                mDropMenu.hideDropMenu(getActivity());
                clearSelectData();
            }

        }
    }

}
