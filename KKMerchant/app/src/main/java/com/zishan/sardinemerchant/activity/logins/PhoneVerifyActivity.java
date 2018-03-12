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
import com.example.wislie.rxjava.presenter.base.login.VerifyPhonePresenter;
import com.example.wislie.rxjava.view.base.login.PhoneVerifyView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.activity.personal.SellerEntranceActivity;
import com.zishan.sardinemerchant.utils.AccountUtil;
import com.zishan.sardinemerchant.utils.CountDownTimerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.RegexUtil;


/**
 * 手机密码登录后 的验证码登录
 * Created by yang on 2017/9/8.
 */

public class PhoneVerifyActivity extends BActivity<PhoneVerifyView,
        VerifyPhonePresenter> implements PhoneVerifyView {


    @BindView(R.id.verify_hint)
    TextView mVerifyHintText; //验证码提示

    @BindView(R.id.verify_code_input)
    EditText mVerifyCodeInput;

    @BindView(R.id.iv_verify_code_delete)
    ImageView mVerifyCodeDel;

    @BindView(R.id.get_verify_code)
    TextView mGetVerifyCode;

    private String preLoginId;

    private String mPhoneNum;
    //验证码
    private String mVercode;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_phone_verify;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            preLoginId = intent.getStringExtra("preLoginId");
            mPhoneNum = intent.getStringExtra("mobile");
        }

        mVerifyHintText.setText("为了您的账户安全请进行短信验证。验证码已发送至手机" +
                formatPhoneNum(mPhoneNum) + ",  请按提示操作。");
        mVerifyCodeInput.addTextChangedListener(new CustomTextWatcher(mVerifyCodeDel));

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.phone_yanzheng));
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        setActionBarHomeIcon(R.mipmap.back_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected VerifyPhonePresenter createPresenter() {
        return new VerifyPhonePresenter(this, this);
    }

    @OnClick({R.id.get_verify_code, R.id.verify_login, R.id.iv_verify_code_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取验证码
            case R.id.get_verify_code:
                mPresenter.requestVerifyCode(preLoginId);
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetVerifyCode, 60 * 1000, 1000);
                countDownTimerUtils.start();
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            //验证码登录
            case R.id.verify_login:
                mVercode = mVerifyCodeInput.getText().toString();
                if (TextUtils.isEmpty(mVercode)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.requestPswLogin(mVercode, preLoginId);
                break;
            //删除
            case R.id.iv_verify_code_delete:
                mVerifyCodeInput.setText("");
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


    }

    @Override
    public void getVerifyCodeComplete(VerifyCodeEntity data) {

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
            Intent intent = new Intent(getActivity(), SellerEntranceActivity.class);
            startActivity(intent);
        } else {
            AccountUtil.saveAccount(dataList.get(0));
            //密码登录 成功跳转到主页
            ActivityManager.getInstance().finishAllActivityExcept(this);
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getUserAccountFailed() {

    }

    //格式化用户名为手机号的用户
    private String formatPhoneNum(String username) {
        if (!TextUtils.isEmpty(username) && RegexUtil.isMobile(username)) {
            String replaceString = "****";
            String temp = username.substring(0, 3);
            temp = temp + replaceString + username.substring(7);
            return temp;
        }
        return username;

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