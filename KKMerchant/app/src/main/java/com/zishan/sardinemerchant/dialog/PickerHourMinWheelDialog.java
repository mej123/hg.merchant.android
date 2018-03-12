package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.List;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.view.WheelView;

import static com.zishan.sardinemerchant.R.id.wheel_start_hour;

/**
 * 小时 分的对话框
 * Created by wislie on 2017/9/29.
 */

public class PickerHourMinWheelDialog extends CommonDialog implements View.OnClickListener {


    private TextView mDialogTitle;

    private TextView mDialogCancel;

    private TextView mDialogConfirm;


    private WheelView mStartHourWheel, mStartMinWheel, mEndHourWheel,
            mEndMinWheel, mStartColonWheel, mStartMorrowDayWheel, mEndColonWheel;

    private String mTitle;

    //是否在选择起始时间, 默认情况下为选择起始时间
    private boolean mIsSelectedStartDate = true;


    //选择的起始时间
    private int mStartHour, mStartMin;
    //选择的结束时间
    private int mEndHour, mEndMin;


    private static final int UPDATE_YEAR = 1;
    private static final int UPDATE_MONTH = 2;
    private static final int UPDATE_DAY = 3;


    public static PickerHourMinWheelDialog newInstance(String title) {
        PickerHourMinWheelDialog dialog = new PickerHourMinWheelDialog();
        Bundle data = new Bundle();
        data.putString("title", title);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");

    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_hour_min_pick;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        mDialogCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        mDialogConfirm = (TextView) view.findViewById(R.id.dialog_confirm);

        mStartHourWheel = (WheelView) view.findViewById(wheel_start_hour);
        mStartMinWheel = (WheelView) view.findViewById(R.id.wheel_start_min);

        mEndHourWheel = (WheelView) view.findViewById(R.id.wheel_end_hour);
        mEndMinWheel = (WheelView) view.findViewById(R.id.wheel_end_min);

        mStartColonWheel = (WheelView) view.findViewById(R.id.wheel_start_colon);
        mEndColonWheel = (WheelView) view.findViewById(R.id.wheel_end_colon);

        // mStartMorrowDayWheel = (WheelView) view.findViewById(R.id.wheel_morrow_day);

        mDialogConfirm.setOnClickListener(this);
        mDialogCancel.setOnClickListener(this);
        mDialogTitle.setText(mTitle);

        initData();
    }

    //初始化开始时间和结束时间
    private void initData() {
        mStartHourWheel.setData(getListHours());
        mStartMinWheel.setData(getListMin());

        mEndHourWheel.setData(getListHours());
        mEndMinWheel.setData(getListMin());

        mStartColonWheel.setData(getColonList());
        mEndColonWheel.setData(getColonList());

//        mStartMorrowDayWheel.setData(getMorrowDayList());

      /*  mMonthWheel.setDefault(mStartMonth - 1);
        mDayWheel.setDefault(mStartDay - 1);*/

        initWheelData();
    }

//    private List<String> getMorrowDayList() {
//
//        List<String> morrow = new ArrayList<>();
//        for (int i = 0; i <23 ; i++) {
//            morrow.add("");
//        }
//        for (int i = 0; i <= 23; i++) {
//            String hour = i > 9 ? String.valueOf(i) : "0" + i;
//            morrow.add("次日" + hour);
//        }
//
//        return morrow;
//    }

    private void initWheelData() {

        mStartHourWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {


            }

            @Override
            public void selecting(int position, String text) {

            }
        });

        mStartMinWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {

            }

            @Override
            public void selecting(int position, String text) {

            }
        });

        mEndHourWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {


            }

            @Override
            public void selecting(int position, String text) {

            }
        });

        mEndMinWheel.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int position, String text) {

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
     * 得到hour集合
     *
     * @return
     */
    private List<String> getListHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            String hour = i > 9 ? String.valueOf(i) : "0" + i;
            hours.add(hour);
        }
        return hours;
    }

    /**
     * 得到分集合
     *
     * @return
     */
    private List<String> getListMin() {
        List<String> mins = new ArrayList<>();
        mins.add("00");
        mins.add("30");
        return mins;
    }


    private List<String> getColonList() {
        List<String> colonList = new ArrayList<>();
        colonList.add(":");
        return colonList;
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
                /*mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mMonthWheel.setDefault(mStartMonth - 1);
                mDayWheel.setDefault(mStartDay - 1);*/
                break;
            //选择结束
            case R.id.date_end:
                mIsSelectedStartDate = false;
                /*mDateStartText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_light_gray));
                mDateEndText.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                mMonthWheel.setDefault(mEndMonth - 1);
                mDayWheel.setDefault(mEndDay - 1);*/
                break;
            //确定
            case R.id.dialog_confirm:

                if (mDialogListener != null) {
                    String mStartHour = mStartHourWheel.getSelectedText();
                    String mStartMin = mStartMinWheel.getSelectedText();
                    String mEndHour = mEndHourWheel.getSelectedText();
                    String mEndMin = mEndMinWheel.getSelectedText();

                    //判断开始时间是否大于结束时间
                    int mmStartHour = Integer.parseInt(mStartHour);
                    int mmEndHour = Integer.parseInt(mEndHour);
                    int mmStartMin = Integer.parseInt(mStartMin);
                    int mmEndMin = Integer.parseInt(mEndMin);
                    if (mmStartHour > mmEndHour
                            || (mmStartHour == mmEndHour && mmStartMin >= mmEndMin)) {
                        ToastUtil.show("开始日期不能大于或等于结束日期");
                        return;
                    }
                    mDialogListener.onInputConfirm(mStartHour, mStartMin, mEndHour, mEndMin);
                    dismiss();
                }
                break;
            //取消
            case R.id.dialog_cancel:
                dismiss();
                break;
        }
    }


}
