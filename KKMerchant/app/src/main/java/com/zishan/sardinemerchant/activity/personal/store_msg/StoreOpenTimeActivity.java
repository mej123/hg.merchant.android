package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreEditPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreEditView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.PickerHourMinAddMorrowWheelDialog;
import com.zishan.sardinemerchant.dialog.PickerHourMinWheelDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 营业时间
 * Created by wislie on 2017/11/24.
 */

public class StoreOpenTimeActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {

    @BindView(R.id.hours_24_icon)
    ImageView mHours24Icon;
    @BindView(R.id.first_duration_icon)
    ImageView mFirstIcon;
    @BindView(R.id.second_duration_icon)
    ImageView mSecondIcon;

    @BindView(R.id.first_duration_layout)
    RelativeLayout mFirstDurationLayout;
    @BindView(R.id.second_duration_layout)
    RelativeLayout mSecondDurationLayout;

    @BindView(R.id.first_duration)
    TextView mFirstDurationText;
    @BindView(R.id.second_duration)
    TextView mSecondDurationText;
    @BindView(R.id.confirm_open_time)
    TextView confirmOpenTime;

    //默认表示未选中
    private boolean mHours24Selected = false;

    //第一时段 默认表示未选中
    private Boolean mFirstDurationSeleted = false;
    //第二时段 默认表示未选中
    private Boolean mSecondDurationSeleted = false;

    private StoreMsgEntity storeMsgEntity;
    private Long consumption;
    private Long mixConsumption;
    private boolean is24th;
    private int tag;//0：点击第一时段  1：表示第二时段
    private String firstOpenTime = null;
    private String firstCloseTime = null;
    private String secondOpenTime = "empty";
    private String secondCloseTime = "empty";
    private String secondStartHouseTime;
    private String secondEndHouseTime;
    private String secondDurationStartTime;
    private String secondDurationEndTime;

    private Long store_id = 0l;

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_open_time;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.store_open_time));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        if (getIntent() == null) return;
        storeMsgEntity = (StoreMsgEntity) getIntent().getSerializableExtra("storeMsgEntity");
        if (storeMsgEntity == null) return;
        store_id = storeMsgEntity.getStoreId();
        String openTime = getIntent().getStringExtra("openTime");//确定是否为24小时
        if (openTime.equals("24小时")) {
            mHours24Selected = true;
            mFirstDurationSeleted = false;
            mSecondDurationSeleted = false;
            updateLayout();
        } else {
            firstOpenTime = getIntent().getStringExtra("firstOpenTime");
            firstCloseTime = getIntent().getStringExtra("firstCloseTime");
            secondOpenTime = getIntent().getStringExtra("secondOpenTime");//带有次日标签数据
            secondCloseTime = getIntent().getStringExtra("secondCloseTime");//带有次日标签数据

            //两段时间都为空,营业时间未设置
            if (firstOpenTime == null || ("").equals(firstOpenTime)) {
                mFirstDurationText.setText("无");
                mSecondDurationText.setText("无");
                mFirstDurationSeleted = false;
                mSecondDurationSeleted = false;
                mFirstDurationText.setTextColor(getResources().getColor(R.color.unselected_tab_color));
                mSecondDurationText.setTextColor(getResources().getColor(R.color.unselected_tab_color));
                updateLayout();
                return;
            }
            //第一段时间设置,第二段未设置,只显示第一段时间
            if (secondOpenTime == null || ("").equals(secondOpenTime)) {
                mFirstDurationText.setText(firstOpenTime + "~" + firstCloseTime);//营业时间
                mSecondDurationText.setText("无");
                mFirstDurationSeleted = true;
                mSecondDurationSeleted = false;
                mSecondDurationText.setTextColor(getResources().getColor(R.color.unselected_tab_color));
                updateLayout();
                return;
            }

            if (!TextUtils.isEmpty(firstOpenTime)) {
                mFirstDurationText.setText(firstOpenTime + "~" + firstCloseTime);
                mFirstDurationSeleted = true;
            }
            if (!TextUtils.isEmpty(secondOpenTime)) {
                mSecondDurationText.setText(secondOpenTime + "~" + secondCloseTime);
                mSecondDurationSeleted = true;
            }
            secondOpenTime = storeMsgEntity.getSecondOpenTime();//无次日标签数据
            secondCloseTime = storeMsgEntity.getSecondCloseTime();//无次日标签数据
            updateLayout();
        }
    }

    private void updateLayout() {
        if (mHours24Selected) {
            mHours24Icon.setSelected(true);
            mFirstDurationLayout.setVisibility(View.INVISIBLE);
            mSecondDurationLayout.setVisibility(View.INVISIBLE);
        } else {
            mHours24Icon.setSelected(false);
            mFirstDurationLayout.setVisibility(View.VISIBLE);
            mSecondDurationLayout.setVisibility(View.VISIBLE);

            if (!mFirstDurationSeleted) {
                mFirstIcon.setSelected(false);
            } else {
                mFirstIcon.setSelected(true);
            }

            if (!mSecondDurationSeleted) {
                mSecondIcon.setSelected(false);
            } else {
                mSecondIcon.setSelected(true);
            }
        }
    }

    @OnClick({R.id.confirm_open_time, R.id.hours_24_icon, R.id.first_duration_icon
            , R.id.second_duration_icon, R.id.first_duration_layout, R.id.second_duration_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //确定
            case R.id.confirm_open_time:
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                requestNetwork();
                break;
            //24小时开关
            case R.id.hours_24_icon:
                mHours24Selected = !mHours24Selected;
                updateLayout();
                break;
            //第一时段 checkbox
            case R.id.first_duration_icon:
                mFirstDurationSeleted = !mFirstDurationSeleted;
                updateLayout();
                break;
            //第二时段 checkbox
            case R.id.second_duration_icon:
                mSecondDurationSeleted = !mSecondDurationSeleted;
                updateLayout();
                break;
            //第一时段 弹出时间对话框
            case R.id.first_duration_layout:
                tag = 0;
                showFirstDialog();
                break;
            //第二时段 弹出时间对话框
            case R.id.second_duration_layout:
                tag = 1;
                showSecondDialog();
                break;
        }
    }

    private void requestNetwork() {

        //营业时间是否是24小时
        if (mHours24Selected) {
            is24th = true;
            firstOpenTime = null;
            firstCloseTime = null;
            secondOpenTime = "empty";
            secondCloseTime = "empty";
        } else {
            is24th = false;
            String firstDuration = mFirstDurationText.getText().toString();//第一时段
            if (TextUtils.isEmpty(firstDuration)) {
                ToastUtil.show(this, "请先设置第一时段时间");
                return;
            }

            //时段都未选中
            if (!mFirstDurationSeleted && !mSecondDurationSeleted) {
                ToastUtil.show(this, "请勾选时段");
                return;
            }

            //如果第二时间段未选中,第二段时间设置为空
            if (Boolean.FALSE == mSecondDurationSeleted) {
                secondOpenTime = "empty";
                secondCloseTime = "empty";
            } else {
                //  secondOpenTime;
                String scondTime = mSecondDurationText.getText().toString();
                if (scondTime.equals("无") || ("").equals(scondTime)) {
                    ToastUtil.show(this, "请先设置第二时段时间");
                    return;
                }
            }

            //第二时段选中 第一时段未选中
            if (!mFirstDurationSeleted && mSecondDurationSeleted) {
                ToastUtil.show(this, "请优先设置第一时段");
                return;
            }
            //第一时段选中  第二时段选中
            if (mFirstDurationSeleted && mSecondDurationSeleted) {
                String secondDuration = mSecondDurationText.getText().toString();//第二时段
                if (secondDuration.equals("无") || ("").equals(secondDuration)) {
                    ToastUtil.show(this, "请先设置第二时段时间");
                    return;
                }
            }
        }
        mPresenter.editStoreMsg(store_id, null,
                null, null, null, firstOpenTime, firstCloseTime, secondOpenTime, secondCloseTime,
                is24th, null, null, null);
    }

    private void showFirstDialog() {
        PickerHourMinWheelDialog firstDialog = PickerHourMinWheelDialog.newInstance("");
        firstDialog.showDialog(getFragmentManager());
        firstDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {

                String startHouseTime = values[0];
                String startMinuteTime = values[1];
                String endHouseTime = values[2];
                String endMinuteTime = values[3];

                mFirstDurationText.setText(startHouseTime + ":" + startMinuteTime + "~" +
                        endHouseTime + ":" + endMinuteTime);
                mFirstDurationText.setTextColor(getResources().getColor(R.color.bg_color_blue_6));
                mFirstDurationSeleted = true;
                updateLayout();
                firstOpenTime = startHouseTime + ":" + startMinuteTime;
                firstCloseTime = endHouseTime + ":" + endMinuteTime;
                // updateLayout();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    private void showSecondDialog() {

        PickerHourMinAddMorrowWheelDialog secondDialog = PickerHourMinAddMorrowWheelDialog.newInstance("");
        secondDialog.showDialog(getFragmentManager());
        secondDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                String mSecondStartHouseTime = values[0];
                String secondStartMinuteTime = values[1];
                String mSecondEndHouseTime = values[2];
                String secondEndMinuteTime = values[3];
                secondDurationStartTime = mSecondStartHouseTime + ":" + secondStartMinuteTime;
                secondDurationEndTime = mSecondEndHouseTime + ":" + secondEndMinuteTime;
                mSecondDurationText.setText(secondDurationStartTime + "~"
                        + secondDurationEndTime);
                mSecondDurationText.setTextColor(getResources().getColor(R.color.bg_color_blue_6));
                mSecondDurationSeleted = true;
                updateLayout();

                if (mSecondStartHouseTime.contains("次日")) {
                    secondStartHouseTime = String.valueOf(Integer.parseInt(mSecondStartHouseTime.
                            substring(2, mSecondStartHouseTime.length())) + 24);
                } else {
                    secondStartHouseTime = String.valueOf(Integer.parseInt(mSecondStartHouseTime));
                }
                if (mSecondEndHouseTime.contains("次日")) {
                    secondEndHouseTime = String.valueOf(Integer.parseInt(mSecondEndHouseTime.substring
                            (2, mSecondEndHouseTime.length())) + 24);
                } else {
                    secondEndHouseTime = String.valueOf(Integer.parseInt(mSecondEndHouseTime));
                }

                if (Integer.parseInt(secondStartHouseTime) < 10) {
                    secondStartHouseTime = "0" + secondStartHouseTime;
                }

                if (Integer.parseInt(secondEndHouseTime) < 10) {

                    secondEndHouseTime = "0" + secondEndHouseTime;
                }

                secondOpenTime = secondStartHouseTime + ":" + secondStartMinuteTime;
                secondCloseTime = secondEndHouseTime + ":" + secondEndMinuteTime;
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    @Override
    public void editStoreMsgSuccess(StoreMsgEntity data) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("is24th", is24th);
        resultIntent.putExtra("firstOpenTime", firstOpenTime);
        resultIntent.putExtra("firstCloseTime", firstCloseTime);
        resultIntent.putExtra("secondOpenTime", secondDurationStartTime);
        resultIntent.putExtra("secondCloseTime", secondDurationEndTime);
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this, "保存成功");
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }
}
