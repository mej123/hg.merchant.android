package com.zishan.sardinemerchant.activity.logins;

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
import com.example.wislie.rxjava.model.LoginEntity;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.model.VerifyCodeEntity;
import com.example.wislie.rxjava.presenter.base.login.PhoneLoginPresenter;
import com.example.wislie.rxjava.view.base.login.PhoneLoginView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.personal.SellerEntranceActivity;
import com.zishan.sardinemerchant.utils.AccountUtil;
import com.zishan.sardinemerchant.utils.CountDownTimerUtils;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.SoftInputUtil;

import static com.zishan.sardinemerchant.R.id.phone_login;

/**
 * Created by wislie on 2018/1/3.
 * <p>
 * 手机验证码登录
 */

public class NewPhoneCodeLoginActivity extends BActivity<PhoneLoginView, PhoneLoginPresenter>
        implements View.OnClickListener, PhoneLoginView {

    @BindView(R.id.phone_input)
    EditText mPhoneInput;
    @BindView(R.id.verify_code_input)
    EditText mVerifyCodeInput;

    @BindView(R.id.get_verify_code)
    TextView mGetVerifyCode;

    @BindView(R.id.iv_phone_delete)
    ImageView mPhoneDel;
    @BindView(R.id.iv_verify_code_delete)
    ImageView mVerifyCodeDel;
    @BindView(R.id.phone_login)
    TextView mPhoneLogin;

    private String checkVercodeId;


    private String mPhoneNum;
    private Integer mCheck = 1;//0:不返回checkToken，1：返回checkToken

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_phone_code_login));
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected PhoneLoginPresenter createPresenter() {
        return new PhoneLoginPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_phone_code_login;
    }

    @Override
    protected void initContentView() {
        mPhoneInput.addTextChangedListener(new CustomTextWatcher(mPhoneDel));
        mVerifyCodeInput.addTextChangedListener(new CustomTextWatcher(mVerifyCodeDel));
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.get_verify_code, phone_login, R.id.account_psw_login, R.id.pwd_free_into,
            R.id.iv_phone_delete, R.id.iv_verify_code_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_verify_code://获取验证码
                getVerifyCode();
                break;
            case phone_login://手机验证登录
                phoneLogin();
                break;
            case R.id.iv_phone_delete://手机号删除
                mPhoneInput.setText("");
                mPhoneDel.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_verify_code_delete://验证码删除
                mVerifyCodeInput.setText("");
                mVerifyCodeDel.setVisibility(View.INVISIBLE);
                break;
            case R.id.account_psw_login://账号密码登录
                ActivityManager.getInstance().finishAllActivityExcept(this);
                Skip.toActivity(this, NewPswLoginActivity.class);
                finish();
                break;
            case R.id.pwd_free_into://免费入驻
//                Skip.toActivity(this, RegisterActivity.class);
                Skip.toActivity(this, SellerEntranceActivity.class);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        mPhoneNum = mPhoneInput.getText().toString();
        if (TextUtils.isEmpty(mPhoneNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //键盘缩回
        SoftInputUtil.hideDirectly(this);
        mPresenter.requestVerifyCode(mPhoneNum);
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
    public void getVerifyCodeComplete(VerifyCodeEntity data) {
        //获取验证码 后倒计时
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetVerifyCode, 60000, 1000);
        countDownTimerUtils.start();
    }

    @Override
    public void getVerifyCodeFailed() {

    }

    @Override
    public void loginSuccess(LoginEntity data) {

        if (data == null) return;
        if (!TextUtils.isEmpty(data.getToken())) {
            UserConfig.getInstance(ClientApplication.getApp()).setAccessToken(data.getToken());
        }
        if (data.getAccountId() != null) {
            UserConfig.getInstance(ClientApplication.getApp()).setAccountId(data.getAccountId());
        }

        if (!TextUtils.isEmpty(data.getCheckToken())) {
            checkVercodeId = data.getCheckToken();
        }

        //保存手机号
        UserConfig.getInstance(ClientApplication.getApp()).setUserMobile(mPhoneNum);
        mPresenter.getUserAccount(UserConfig.getInstance(ClientApplication.getApp()).getAccountId());

    }

    @Override
    public void loginFailed() {

    }

    @Override
    public void getUserAccountSuccess(List<UserAccountEntity> dataList) {
        if (dataList == null || dataList.size() == 0) {
            Intent intent = new Intent(this, SellerEntranceActivity.class);
            startActivity(intent);
        } else {
            AccountUtil.saveAccount(dataList.get(0));
            //手机验证码登录成功跳转到设置登录密码界面
            ActivityManager.getInstance().finishAllActivityExcept(this);
            Intent settingIntent = new Intent(this, SettingLoginPswActivity.class);
            settingIntent.putExtra("checkVercodeId", checkVercodeId);
            settingIntent.putExtra("mobile", mPhoneNum);
            startActivity(settingIntent);
            finish();
        }
    }

    @Override
    public void getUserAccountFailed() {

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
                mPhoneLogin.setEnabled(true);
                mPhoneLogin.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            } else {
                ivDel.setVisibility(View.INVISIBLE);
                mPhoneLogin.setEnabled(false);
                mPhoneLogin.setBackgroundResource(R.drawable.login_new_confirm_bg);
            }
        }
    }

    /**
     * 手机登录
     */
    private void phoneLogin() {

        mPhoneNum = mPhoneInput.getText().toString();
        if (TextUtils.isEmpty(mPhoneNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

       /* if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }*/

        String verifyCode = mVerifyCodeInput.getText().toString();

        if (TextUtils.isEmpty(verifyCode)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //键盘缩回
        SoftInputUtil.hideDirectly(this);
        //调用手机登录的接口
        mPresenter.requestPhoneLogin(mPhoneNum, verifyCode, mCheck);
    }
}
