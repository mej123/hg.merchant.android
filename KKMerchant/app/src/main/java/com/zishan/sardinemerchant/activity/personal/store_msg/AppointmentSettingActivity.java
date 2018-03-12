package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreEditPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreEditView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.PickerDaysWheelDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 预约设置
 * Created by wislie on 2017/11/28.
 */

public class AppointmentSettingActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {

    @BindView(R.id.appointment_setting_icon)
    ImageView mAppointmentSettingIcon;
    @BindView(R.id.appointment_setting_time_layout)
    RelativeLayout mTimeLayout;
    @BindView(R.id.appointment_setting_num_layout)
    RelativeLayout mNumLayout;
    @BindView(R.id.appointment_setting_time)
    TextView mSettingTimeText;
    @BindView(R.id.appointment_setting_num)
    EditText mAppointmentSettingNum;
    private String appointmentTime;
    private String Num;
    private boolean mIsSetting;
    private StoreMsgEntity storeMsgEntity;
    private Long consumption;
    private Long mixConsumption;
    private boolean bespeakSwitch;
    private Integer advanceBespeakDays;
    private Integer advanceBespeakNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_appointment_setting;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.appointment_setting));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            storeMsgEntity = (StoreMsgEntity) intent.getSerializableExtra("storeMsgEntity");
            String appointmentTime = intent.getStringExtra("appointmentTime");
            String appointmentNum = intent.getStringExtra("appointmentNum");
            String mSettingText = intent.getStringExtra("mIsSetting");
            if (!TextUtils.isEmpty(mSettingText) && mSettingText.equals("已设置")) {
                mIsSetting = true;

                if (appointmentTime.equals("0")) {
                    mSettingTimeText.setText("当日");
                } else {
                    mSettingTimeText.setText(appointmentTime + "天");
                }
                mAppointmentSettingNum.setText(appointmentNum);
            } else {
                mIsSetting = false;
            }
        }

        updateSetting();
    }

    @OnClick({R.id.appointment_setting_icon, R.id.appointment_setting_time, R.id.confirm_appointment_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            //预约设置
            case R.id.appointment_setting_icon:
                mIsSetting = !mIsSetting;
                updateSetting();
                break;
            //时间设置
            case R.id.appointment_setting_time:
                PickerDaysWheelDialog dialog = PickerDaysWheelDialog.newInstance("预约时间设置");
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        String appointmentTime = values[0];
                        mSettingTimeText.setText(appointmentTime);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.confirm_appointment_setting:
                requestNetwork();
                break;
        }
    }

    private void requestNetwork() {

        //预约设置是否开启
        if (mIsSetting) {
            appointmentTime = mSettingTimeText.getText().toString();
            Num = mAppointmentSettingNum.getText().toString();
            if (TextUtils.isEmpty(appointmentTime)) {
                ToastUtil.show(this, "预约时间不能为空");
                return;
            }
            if (TextUtils.isEmpty(Num)) {
                ToastUtil.show(this, "预约人数不能为空");
                return;
            }
            if (appointmentTime.equals("当日")) {
                bespeakSwitch = true;
                advanceBespeakDays = 0;
            } else {
                String time = appointmentTime.substring(0, appointmentTime.length() - 1);
                bespeakSwitch = true;
                advanceBespeakDays = Integer.parseInt(time);//预约时间
            }
            advanceBespeakNum = Integer.parseInt(Num);//预约人数
        } else {
            bespeakSwitch = false;
            advanceBespeakDays = null;
            advanceBespeakNum = null;
        }

        if (storeMsgEntity == null) return;

        String telephone = storeMsgEntity.getTelephone();

        String merchantDescription = storeMsgEntity.getMerchantDescription();//门店介绍
        if (TextUtils.isEmpty(merchantDescription)) {
            merchantDescription = "empty";
        }
        consumption = (long)storeMsgEntity.getConsumption();    //人均消费
        mixConsumption = (long)storeMsgEntity.getMixConsumption();  //最低人均消费
        if (TextUtils.isEmpty(String.valueOf(consumption))) {
            consumption = null;
        } else if (TextUtils.isEmpty(String.valueOf(mixConsumption))) {
            mixConsumption = null;
        }
        //营业时间是否是24小时
        Boolean is24th = storeMsgEntity.getIs24th();
        String firstOpenTime;
        String firstCloseTime;
        String secondOpenTime;
        String secondCloseTime;
        if (is24th) {
            firstOpenTime = null;
            firstCloseTime = null;
            secondOpenTime = null;
            secondCloseTime = null;
        } else {
            firstOpenTime = storeMsgEntity.getFirstOpenTime();//第一段开始时间
            firstCloseTime = storeMsgEntity.getFirstCloseTime();//第一段结束时间
            secondOpenTime = storeMsgEntity.getSecondOpenTime();//第二段开始时间
            if (secondOpenTime.equals("00:00")) {
                secondOpenTime = null;
            }
            secondCloseTime = storeMsgEntity.getSecondCloseTime();//第二段结束时间
            if (secondCloseTime.equals("00:00")) {
                secondCloseTime = null;
            }
        }

        //发起请求
//        mPresenter.editStoreMsg(UserConfig.getInstance(this).getStoreId(), telephone,
//                merchantDescription, consumption, mixConsumption, firstOpenTime,
//                firstCloseTime, secondOpenTime, secondCloseTime, is24th, bespeakSwitch,
//                advanceBespeakDays, advanceBespeakNum);
    }

    private void updateSetting() {
        if (mIsSetting) {
            mAppointmentSettingIcon.setSelected(true);
            mTimeLayout.setVisibility(View.VISIBLE);
            mNumLayout.setVisibility(View.VISIBLE);
        } else {
            mAppointmentSettingIcon.setSelected(false);
            mTimeLayout.setVisibility(View.GONE);
            mNumLayout.setVisibility(View.GONE);
        }
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
        resultIntent.putExtra("mIsSetting", mIsSetting);
        resultIntent.putExtra("appointmentTime", appointmentTime);
        resultIntent.putExtra("appointmentNum", Num);
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this, "保存成功");
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }
}
