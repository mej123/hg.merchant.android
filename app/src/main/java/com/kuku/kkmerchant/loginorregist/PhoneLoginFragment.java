package com.kuku.kkmerchant.loginorregist;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.model.LoginVercodeEntity;
import com.example.wislie.rxjava.model.VercodeEntity;
import com.example.wislie.rxjava.presenter.LoginVercodePresenter;
import com.example.wislie.rxjava.view.LoginVersodeView;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.activity.MainPageActivity;
import com.kuku.kkmerchant.utils.CountDownTimerUtils;
import com.kuku.kkmerchant.utils.Skip;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.RegexUtil;


/**
 * 手机登录
 * Created by yang on 2017/9/1.
 */

public class PhoneLoginFragment extends BaseFragment<LoginVersodeView, LoginVercodePresenter> implements View.OnClickListener, LoginVersodeView {
    
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
    
    
    private String mPhoneNum;
    
    
    @Override
    protected int getLayoutResId() {
        return R.layout.phone_login_layout;
    }
    
    @Override
    protected void initBizView() {
        mPhoneInput.addTextChangedListener(new CustomTextWatcher(mPhoneDel));
        mVerifyCodeInput.addTextChangedListener(new CustomTextWatcher(mVerifyCodeDel));
        
    }
    
    @Override
    protected LoginVercodePresenter createPresenter() {
        return new LoginVercodePresenter(getActivity(), this);
    }
    
    @Override
    protected void loadData() {
        
    }
    
    
    @Override
    public void requestFailed(String message) {
        
    }
    
    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        mPhoneNum = mPhoneInput.getText().toString();
        if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mPresenter.getVercode(mPhoneNum);
        
    }
    
    /**
     * 手机登录
     */
    private void phoneLogin() {
        String verifyCode = mVerifyCodeInput.getText().toString();
        if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(verifyCode)) {
            Toast.makeText(getActivity(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //调用手机登录的接口
        mPresenter.loginByCoed(mPhoneNum, verifyCode);
    }
    
    @Override
    public void getVercode(VercodeEntity vercodeEntity) {
        //获取验证码 后倒计时
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetVerifyCode, 60000, 1000);
        countDownTimerUtils.start();
    }
    
    @Override
    public void loginByVercode(LoginVercodeEntity loginVercodeEntity) {
        //手机登录 成功跳转到主页
        Skip.toActivity(getActivity(), MainPageActivity.class);
        getActivity().finish();
    }
    
    @OnClick({R.id.iv_wechat_login, R.id.iv_alipay_login, R.id.pwd_forget, R.id.phone_login, R.id.pwd_free_into,
        R.id.iv_phone_delete, R.id.iv_verify_code_delete, R.id.get_verify_code}) //
    public void onClick(View view) {
        switch (view.getId()) {
                //微信登录
            case R.id.iv_wechat_login:
                
                break;
                //支付宝登录
            case R.id.iv_alipay_login:
                
                break;
                //免费入驻
            case R.id.pwd_free_into:
                Skip.toActivity(getActivity(), RegistActivity.class);
                break;
                //忘记密码
            case R.id.pwd_forget:
                Skip.toActivity(getActivity(), ForgetPswActivity.class);
                break;
                //手机登录
            case R.id.phone_login:
                phoneLogin();
                break;
                //手机号删除
            case R.id.iv_phone_delete:
                mPhoneInput.setText("");
                mPhoneDel.setVisibility(View.INVISIBLE);
                break;
                //验证码删除
            case R.id.iv_verify_code_delete:
                mVerifyCodeInput.setText("");
                mVerifyCodeDel.setVisibility(View.INVISIBLE);
                break;
                //获取验证码
            case R.id.get_verify_code:
                getVerifyCode();
                break;
        }
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

