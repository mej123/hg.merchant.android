package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.WheelView;

/**
 * 年 月 日的对话框
 * Created by wislie on 2017/9/29.
 */

public class PickerDateWheelDialog extends CommonDialog implements View.OnClickListener {


    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private TextView mDateStartText, mDateEndText;

    private WheelView mYearWheel, mMonthWheel, mDayWheel;

    private String mTitle;
    private boolean dimAmountIsZero;
    private boolean mSingleTag; //表示是否为单个

    //是否在选择起始时间, 默认情况下为选择起始时间
    private boolean mIsSelectedStartDate = true;

    //选择的起始时间
    private int mStartYear, mStartMonth, mStartDay;
    //选择的结束时间
    private int mEndYear, mEndMonth, mEndDay;

    private final int START_YEAR = 2000;

    private ListenersHandler mHandler;
    private static final int UPDATE_YEAR = 1;
    private static final int UPDATE_MONTH = 2;
    private static final int UPDATE_DAY = 3;


    public static PickerDateWheelDialog newInstance(String title, boolean dimAmountIsZero, boolean singleTag) {
        PickerDateWheelDialog dialog = new PickerDateWheelDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putBoolean("dimAmountIsZero", dimAmountIsZero);
        data.putBoolean("singleTag", singleTag);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        dimAmountIsZero = getArguments().getBoolean("dimAmountIsZero");
        mSingleTag = getArguments().getBoolean("singleTag");
        mHandler = new ListenersHandler(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_date_pick;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        LinearLayout pickerLayout = (LinearLayout) view.findViewById(R.id.data_picker_layout);
        mDateStartText = (TextView) view.findViewById(R.id.date_start);
        mDateEndText = (TextView) view.findViewById(R.id.date_end);

        mYearWheel = (WheelView) view.findViewById(R.id.wheel_year);
        mMonthWheel = (WheelView) view.findViewById(R.id.wheel_month);
        mDayWheel = (WheelView) view.findViewById(R.id.wheel_day);

        mDateStartText.setOnClickListener(this);
        mDateEndText.setOnClickListener(this);
        mDialogConfirm.setOnClickListener(this);
        mDialogCancel.setOnClickListener(this);
        mDialogTitle.setText(mTitle);
        if(mSingleTag){
            pickerLayout.setVisibility(View.GONE);
        }
        initData();
    }

    //初始化开始时间和结束时间
    private void initData() {

        mStartYear = DatePickerUtil.getCurrentYear();
        mStartMonth = DatePickerUtil.getCurrentMonth();
        mStartDay = DatePickerUtil.getCurrentDay();

        mEndYear = mStartYear;
        mEndMonth = mStartMonth;
        mEndDay = mStartDay;

        //设置数据集
        mYearWheel.setData(getListYears());
        mMonthWheel.setData(getListMonths());
        List<String> days = getListDays(mStartYear, mStartMonth);
        mDayWheel.setData(days);

        //设置默认的数据
        mYearWheel.setDefault(mStartYear - START_YEAR);
        mMonthWheel.setDefault(mStartMonth - 1);
        mDayWheel.setDefault(mStartDay - 1);
        mDateStartText.setText(getSelectDate(String.valueOf(mStartYear),
                String.valueOf(mStartMonth), String.valueOf(mStartDay)));
        mDateEndText.setText(getSelectDate(String.valueOf(mEndYear),
                String.valueOf(mEndMonth), String.valueOf(mEndDay)));
        initWheelData();
    }


    private void initWheelData() {

        mYearWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {

                //重新设置当前年份，当前月份 有几天
                final int selectedYear = position + START_YEAR;
                final int selectedMonth = mMonthWheel.getSelected() + 1;
                final List<String> days = getListDays(selectedYear, selectedMonth);
                if (days != null && days.size() > 0) {
                    mDayWheel.setData(days);
                }
                mHandler.sendEmptyMessage(UPDATE_YEAR);
            }

            @Override
            public void selecting(int position, String text) {

            }
        });


        mMonthWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {

                //重新设置当前年份，当前月份 有几天
                final int selectedYear = mYearWheel.getSelected() + 2000;
                final int selectedMonth = position + 1;
                final List<String> days = getListDays(selectedYear, selectedMonth);
                if (days != null && days.size() > 0) {
                    mDayWheel.setData(days);
                }
                mHandler.sendEmptyMessage(UPDATE_MONTH);

            }

            @Override
            public void selecting(int position, String text) {

            }
        });

        mDayWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {
                mHandler.sendEmptyMessage(UPDATE_DAY);
            }

            @Override
            public void selecting(int position, String text) {

            }
        });
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


    /**
     * 得到years集合
     *
     * @return
     */
    private List<String> getListYears() {

        int startYear = 2000;
        List<String> years = new ArrayList<>();
        for (int i = startYear; i <= startYear + 100; i++) {
            String year = i + "年";
            years.add(year);
        }
        return years;

    }


    /**
     * 得到months集合
     *
     * @return
     */
    private List<String> getListMonths() {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= DatePickerUtil.months.length; i++) {
            String month = i > 9 ? i + "月" : "0" + i + "月";
            months.add(month);
        }
        return months;
    }

    /**
     * 根据year和month得到days集合
     *
     * @param year
     * @param month
     * @return
     */
    private List<String> getListDays(int year, int month) {
        List<String> days = new ArrayList<>();
        int daysOfMonth = DatePickerUtil.getDaysOfMonth(year, month);
        for (int i = 1; i <= daysOfMonth; i++) {
            String day = i > 9 ? i + "日" : "0" + i + "日";
            days.add(day);
        }
        return days;
    }


    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int animIn() {
        return R.anim.fade_in;
    }

    @Override
    public int animOut() {
        return R.anim.fade_out;
    }

    @Override
    public boolean dimAmountIsZero() {
        return dimAmountIsZero;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择起始
            case R.id.date_start:
                mIsSelectedStartDate = true;
                mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mYearWheel.setDefault(mStartYear - START_YEAR);
                mMonthWheel.setDefault(mStartMonth - 1);
                mDayWheel.setDefault(mStartDay - 1);
                break;
            //选择结束
            case R.id.date_end:
                mIsSelectedStartDate = false;
                mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mYearWheel.setDefault(mEndYear - START_YEAR);
                mMonthWheel.setDefault(mEndMonth - 1);
                mDayWheel.setDefault(mEndDay - 1);

                break;
            //确定
            case R.id.dialog_confirm:
                if (mDialogListener != null) {

                    if (!mSingleTag && (mStartYear > mEndYear || (mStartYear == mEndYear && mStartMonth > mEndMonth)
                            || (mStartYear == mEndYear && mStartMonth == mEndMonth && mStartDay >= mEndDay))) {
                        ToastUtil.show("开始日期不能大于结束日期");
                        return;
                    }

                    mDialogListener.onInputConfirm(String.valueOf(mStartYear),
                            String.valueOf(mStartMonth), String.valueOf(mStartDay),
                            String.valueOf(mEndYear), String.valueOf(mEndMonth),
                            String.valueOf(mEndDay));
                    dismiss();
                }
                break;
            //取消
            case R.id.dialog_cancel:
                dismiss();
                break;
        }
    }

    private static final class ListenersHandler extends Handler {
        WeakReference<PickerDateWheelDialog> mDialogReference;

        public ListenersHandler(PickerDateWheelDialog dialog) {
            mDialogReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            PickerDateWheelDialog dialog = mDialogReference.get();
            switch (msg.what) {
                case UPDATE_YEAR:
                    dialog.updateSelectDate(UPDATE_YEAR);
                    break;
                case UPDATE_MONTH:
                    dialog.updateSelectDate(UPDATE_MONTH);
                    break;
                case UPDATE_DAY:
                    dialog.updateSelectDate(UPDATE_DAY);
                    break;
            }
        }
    }

    //更新选中的日期
    private void updateSelectDate(int what) {


        int selectedYear = mYearWheel.getSelected() + START_YEAR;
        int selectedMonth = mMonthWheel.getSelected() + 1;
        int selectedDay = mDayWheel.getSelected() + 1;
        if (mIsSelectedStartDate) {
            mStartYear = selectedYear;
            mStartMonth = selectedMonth;
            mStartDay = selectedDay;
            mDateStartText.setText(getSelectDate(String.valueOf(mStartYear),
                    String.valueOf(mStartMonth), String.valueOf(mStartDay)));
        } else {
            mEndYear = selectedYear;
            mEndMonth = selectedMonth;
            mEndDay = selectedDay;
            mDateEndText.setText(getSelectDate(String.valueOf(mEndYear),
                    String.valueOf(mEndMonth), String.valueOf(mEndDay)));
        }
        if (what != UPDATE_DAY) {
            List<String> days = getListDays(selectedYear, selectedMonth);
            mDayWheel.refreshData(days);
        }

    }

}
