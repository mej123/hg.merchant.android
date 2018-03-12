package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.VerificationCodeEntity;
import com.example.wislie.rxjava.presenter.personal.AccountPhoneVerificationPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.AccountPhoneVerificationView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StringUtil;

/**
 * Created by yang on 2017/11/2.
 * <p>
 * 商户绑定银行卡获取验证码(B端)
 */
public class NewAccountPhoneVerificationActivity extends BActivity<AccountPhoneVerificationView,
        AccountPhoneVerificationPresenter> implements AccountPhoneVerificationView {
    @BindView(R.id.tv_hint_message)
    TextView mHintMessage;
    @BindView(R.id.et_input_band_card_number)
    EditText mInputNumber;
    @BindView(R.id.tv_get_code)
    TextView mGetCode;
    @BindView(R.id.iv_verify_code_delete)
    ImageView mVerifyCodeDelete;
    private String changeBankCard;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account_phone_verfication;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.phone_verification));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected AccountPhoneVerificationPresenter createPresenter() {
        return new AccountPhoneVerificationPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        String userMobile = UserConfig.getInstance(this).getUserMobile();
        if (userMobile == null) return;
        mHintMessage.setText("银行绑定短信确认，验证码将发送至手机" + StringUtil.formatPhoneNum(userMobile) + ",请按提示操作");
        changeBankCard = getIntent().getStringExtra("changeBankCard");
        mInputNumber.addTextChangedListener(new CustomTextWatcher(mVerifyCodeDelete));
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.tv_get_code, R.id.tv_quit, R.id.iv_verify_code_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                if (UserConfig.getInstance(this).getMerchantId() != 0) {
                    //merchant_id  商户id
                    mPresenter.getVerificationCodeBindCardComplete(UserConfig.getInstance(this).getMerchantId());
                }
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetCode, 60 * 1000, 1000);
                countDownTimerUtils.start();
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_quit:
                String versionCode = mInputNumber.getText().toString();
                if (TextUtils.isEmpty(versionCode)) {
                    ToastUtil.show(this, "验证码不能为空");
                    return;
                }
                mPresenter.merchantBindVerificationCode(UserConfig.getInstance(this).getMerchantId(), versionCode);
                break;
            case R.id.iv_verify_code_delete:
                mInputNumber.setText("");
                mVerifyCodeDelete.setVisibility(View.INVISIBLE);
                break;
        }
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
    public void getVerificationCodeBindCardComplete(Object object) {


    }

    @Override
    public void getVerificationCodeBindCardFailed() {

    }

    @Override
    public void merchantBindVerificationCodeComplete(VerificationCodeEntity verificationCodeEntity) {

        if (verificationCodeEntity == null) return;
//        ActivityManager.getInstance().finishAllActivityExcept(this);
        String uuidCode = verificationCodeEntity.getUuidCode();
        Intent intent = new Intent();
        intent.setClass(this, NewBankCardBindActivity.class);
        if (!TextUtils.isEmpty(changeBankCard)) {
            intent.putExtra("changeBankCard", changeBankCard);
        }
        intent.putExtra("uuidCode", uuidCode);
        startActivity(intent);//手机验证成功，跳转到绑卡界面
        NewAccountPhoneVerificationActivity.this.finish();
    }

    @Override
    public void merchantBindVerificationCodeFailed() {

    }

    private class CustomTextWatcher implements TextWatcher {

        private ImageView ivDel;

        public CustomTextWatcher(ImageView ivDel) {
            this.ivDel = ivDel;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !TextUtils.isEmpty(s)) {
                ivDel.setVisibility(View.VISIBLE);
            } else {
                ivDel.setVisibility(View.INVISIBLE);
            }
        }
    }

}
