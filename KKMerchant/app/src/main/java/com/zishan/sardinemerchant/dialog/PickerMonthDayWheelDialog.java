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
 * 月 和 日的对话框
 * Created by wislie on 2017/9/29.
 */

public class PickerMonthDayWheelDialog extends CommonDialog implements View.OnClickListener {


    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;

    private TextView mDateStartText, mDateEndText;

    private WheelView mMonthWheel, mDayWheel;

    private String mTitle;


    //是否在选择起始时间, 默认情况下为选择起始时间
    private boolean mIsSelectedStartDate = true;

    //当前年份
    private int mCurrentYear;

    //选择的起始时间
    private int mStartMonth, mStartDay;
    //选择的结束时间
    private int mEndMonth, mEndDay;

    private ListenersHandler mHandler;
    private static final int UPDATE_YEAR = 1;
    private static final int UPDATE_MONTH = 2;
    private static final int UPDATE_DAY = 3;


    public static PickerMonthDayWheelDialog newInstance(String title) {
        PickerMonthDayWheelDialog dialog = new PickerMonthDayWheelDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mHandler = new ListenersHandler(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_month_day_pick;
    }


    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {

        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        mDateStartText = (TextView) view.findViewById(R.id.date_start);
        mDateEndText = (TextView) view.findViewById(R.id.date_end);

        mMonthWheel = (WheelView) view.findViewById(R.id.wheel_month);
        mDayWheel = (WheelView) view.findViewById(R.id.wheel_day);

        mDateStartText.setOnClickListener(this);
        mDateEndText.setOnClickListener(this);
        mDialogConfirm.setOnClickListener(this);
        mDialogCancel.setOnClickListener(this);
        mDialogTitle.setText(mTitle);

        initData();
    }

    //初始化开始时间和结束时间
    private void initData() {

        mCurrentYear = DatePickerUtil.getCurrentYear();
        mStartMonth = DatePickerUtil.getCurrentMonth();
        mStartDay = DatePickerUtil.getCurrentDay();

        mEndMonth = mStartMonth;
        mEndDay = mStartDay;

        //设置数据集
        mMonthWheel.setData(getListMonths());
        List<String> days = getListDays(mCurrentYear, mStartMonth);
        mDayWheel.setData(days);


        mMonthWheel.setDefault(mStartMonth - 1);
        mDayWheel.setDefault(mStartDay - 1);
        mDateStartText.setText(getSelectDate(String.valueOf(mStartMonth), String.valueOf(mStartDay)));
        mDateEndText.setText(getSelectDate(String.valueOf(mEndMonth), String.valueOf(mEndDay)));
        initWheelData();
    }


    private void initWheelData() {

        mMonthWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {

                final int selectedMonth = position + 1;
                final List<String> days = getListDays(mCurrentYear, selectedMonth);
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

    private String getSelectDate(String month, String day) {
        return month + "月" + day + "日";
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
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择起始
            case R.id.date_start:
                mIsSelectedStartDate = true;
                mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mMonthWheel.setDefault(mStartMonth - 1);
                mDayWheel.setDefault(mStartDay - 1);
                break;
            //选择结束
            case R.id.date_end:
                mIsSelectedStartDate = false;
                mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mMonthWheel.setDefault(mEndMonth - 1);
                mDayWheel.setDefault(mEndDay - 1);

                break;
            //确定
            case R.id.dialog_confirm:
                if (mDialogListener != null) {

                    if (mStartMonth > mEndMonth
                            || (mStartMonth == mEndMonth && mStartDay >= mEndDay)) {
                        ToastUtil.show("开始日期不能大于结束日期");
                        return;
                    }

                    mDialogListener.onInputConfirm(String.valueOf(mCurrentYear),
                            String.valueOf(mStartMonth), String.valueOf(mStartDay),
                            String.valueOf(mCurrentYear), String.valueOf(mEndMonth),
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
        WeakReference<PickerMonthDayWheelDialog> mDialogReference;

        public ListenersHandler(PickerMonthDayWheelDialog dialog) {
            mDialogReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            PickerMonthDayWheelDialog dialog = mDialogReference.get();
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
        int selectedMonth = mMonthWheel.getSelected() + 1;
        int selectedDay = mDayWheel.getSelected() + 1;
        if (mIsSelectedStartDate) {
            mStartMonth = selectedMonth;
            mStartDay = selectedDay;
            mDateStartText.setText(getSelectDate(String.valueOf(mStartMonth), String.valueOf(mStartDay)));
        } else {
            mEndMonth = selectedMonth;
            mEndDay = selectedDay;
            mDateEndText.setText(getSelectDate(String.valueOf(mEndMonth), String.valueOf(mEndDay)));
        }
        if (what != UPDATE_DAY) {
            List<String> days = getListDays(mCurrentYear, selectedMonth);
            mDayWheel.refreshData(days);
        }

    }

}
