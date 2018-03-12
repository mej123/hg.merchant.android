package com.kuku.kkmerchant.loginorregist;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.model.LoginEntity;
import com.example.wislie.rxjava.presenter.LoginPresenter;
import com.example.wislie.rxjava.view.LoginView;
import com.hg.ftas.util.ToastUtil;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.activity.MainPageActivity;
import com.kuku.kkmerchant.utils.Skip;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.RegexUtil;

/**
 * 密码登录
 * Created by yang on 2017/9/1.
 */

public class PswLoginFragment extends BaseFragment<LoginView, LoginPresenter> implements View.OnClickListener, LoginView {
    
    @BindView(R.id.phone_input)
    EditText mPhoneInput;
    @BindView(R.id.pwd_input)
    EditText mPwdInput;
    
    @BindView(R.id.pwd_login)
    TextView mPwdLogin;
    
    @BindView(R.id.iv_phone_delete)
    ImageView mPhoneDel;
    @BindView(R.id.iv_input_psw_hide)
    ImageView mPwdIcon;
    
    
    //手机号
    private String mPhoneNum;
    //默认情况下隐藏
    private boolean mPwdHided = true;
    
    private String preLoginId;
    
    
    @Override
    protected int getLayoutResId() {
        return R.layout.psw_login_layout;
    }
    
    @Override
    protected void initBizView() {
        mPhoneInput.addTextChangedListener(mPhoneWatcher);
        
    }
    
    @Override
    public void requestFailed(String message) {
        
        
    }
    
    @Override
    public void loginByPwd(LoginEntity loginEntity) {
        
        if (loginEntity.getPreLoginId() != null) {
            preLoginId = loginEntity.getPreLoginId();
            Intent intent = new Intent();
            intent.setClass(getContext(), PhoneVerifyActivity.class);
            intent.putExtra("preLoginId", preLoginId);
            intent.putExtra("mobile", mPhoneNum);
            startActivity(intent);
            ToastUtil.show("跳到某一页");
            return;
        }
        //密码登录 成功跳转到主页
        Skip.toActivity(getActivity(), MainPageActivity.class);
        getActivity().finish();
        
    }
    
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(getActivity(), this);
    }
    
    @Override
    protected void loadData() {
        
    }
    
    /**
     * 密码登录
     */
    private void pswLogin() {
        
        mPhoneNum = mPhoneInput.getText().toString();
        if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String password = mPwdInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            //CustomToastUtil.show("密码不能为空");
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mPresenter.loginByPwd(mPhoneNum, password);
    }
    
    @OnClick({R.id.iv_wechat_login, R.id.iv_alipay_login, R.id.pwd_free_into, R.id.pwd_forget, R.id.pwd_login,
        R.id.iv_phone_delete, R.id.iv_input_psw_hide})
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
                //密码登录
            case R.id.pwd_login:
                pswLogin();
                break;
                //手机号删除
            case R.id.iv_phone_delete:
                mPhoneInput.setText("");
                mPhoneDel.setVisibility(View.INVISIBLE);
                break;
                //显示密码 或 隐藏密码
            case R.id.iv_input_psw_hide:
                
                if(mPwdHided){ //隐藏
                    mPwdIcon.setImageResource(R.mipmap.close_input);
                    mPwdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{ //显示
                    mPwdIcon.setImageResource(R.mipmap.open_input);
                    mPwdInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                
                mPwdHided = !mPwdHided;
                
                break;
        }
        
    }
    
    private TextWatcher mPhoneWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    
    }
    
    @Override
    public void afterTextChanged(Editable s) {
    if (s.length() > 0 && !TextUtils.isEmpty(s)) {
    mPhoneDel.setVisibility(View.VISIBLE);
} else {
mPhoneDel.setVisibility(View.INVISIBLE);
}
}
};
}
