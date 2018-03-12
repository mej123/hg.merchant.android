package com.kuku.kkmerchant.loginorregist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.model.CheckVercodeEntity;
import com.example.wislie.rxjava.model.RegesiterEntity;
import com.example.wislie.rxjava.model.VercodeEntity;
import com.example.wislie.rxjava.presenter.RegesiterPresenterB;
import com.example.wislie.rxjava.view.RegesiterView;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.utils.CountDownTimerUtils;
import com.kuku.kkmerchant.utils.VerificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by yang on 2017/9/11.
 */

public class RegistActivity extends BaseActivity<RegesiterView, RegesiterPresenterB> implements  RegesiterView {

    @BindView(R.id.user_name)
    EditText mUserNameText1;
    @BindView(R.id.yanzhengma)
    RelativeLayout yanzhengma;
    @BindView(R.id.send_vercode)
    TextView send_vercode;
    @BindView(R.id.vercode)
    EditText mVercodeText;
    @BindView(R.id.pwd)
    EditText mPwdText;
    @BindView(R.id.iv_input_psw)
    ImageView iv_input_psw;
    @BindView(R.id.pwd_ret)
    EditText pwd_ret;
    @BindView(R.id.iv_queren_psw)
    ImageView iv_queren_psw;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.regesiter_quick)
    TextView regesiterquick;

    boolean eyeOpen = true;
    boolean eyeOpen1 = true;
    private String mMobile;
    private String mPwd;
    private String vercode;
    private String inputPsw;

    @Override
    protected int getLayoutResId() {
        return R.layout.regist_layout;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.regist));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        mUserNameText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                regesiterquick.setEnabled(true);
                if (s.length() > 0 && !TextUtils.isEmpty(s)) {

                    regesiterquick.setEnabled(true);
                } else {
                    regesiterquick.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected RegesiterPresenterB createPresenter() {

        return new RegesiterPresenterB(this, this);
    }



    @Override
    public void requestFailed(String message) {

    }

    @Override
    public void getVercode(VercodeEntity vercodeEntity) {

        Log.e("Wislie", "vercodeEntity:" + vercodeEntity);

        Long limitInTime = vercodeEntity.getLimitInTime();

        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(send_vercode, 60000, 1000);
        countDownTimerUtils.start();
        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void checkVercode(CheckVercodeEntity checkVercodeEntity) {

        Log.e("Wislie", "checkVercodeEntity:" + checkVercodeEntity);

        String checkVercodeId = checkVercodeEntity.getCheckVercodeId();
        //立即注册
        mPresenter.regesiterByPhone(checkVercodeEntity.getCheckVercodeId(), mMobile, mPwd);
    }

    @Override
    public void regesiterByPhone(RegesiterEntity regesiterEntity) {

        Log.e("Wislie", "regesiterEntity:" + regesiterEntity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_input_psw_layout,R.id.rl_queren_psw_layout,R.id.send_vercode,R.id.regesiter_quick,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_input_psw_layout:
                if (eyeOpen) {
                    //密码
                    mPwdText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_input_psw.setImageResource(R.mipmap.yanjing_guangbi);
                    eyeOpen = false;
                } else {
                    //明文
                    mPwdText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_input_psw.setImageResource(R.mipmap.yanjing_dakai);
                    eyeOpen = true;
                }
                break;

            case R.id.rl_queren_psw_layout:

                if (eyeOpen1) {
                    //密码
                    pwd_ret.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_queren_psw.setImageResource(R.mipmap.yanjing_guangbi);
                    eyeOpen1 = false;
                } else {
                    //明文
                    pwd_ret.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_queren_psw.setImageResource(R.mipmap.yanjing_dakai);
                    eyeOpen1 = true;
                }
                break;


            case R.id.send_vercode:

                //判断手机号是否为空
                if (mUserNameText1.length() == 0) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (VerificationUtils.matcherPhoneNum(mUserNameText1.getText().toString())) {

                    mMobile = mUserNameText1.getText().toString();


                    // TODO: 2017/8/17
                    //拿到手机号,发送验证码
                    mPresenter.getVercode(mMobile);
                    //sendCode();

                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;

            case R.id.regesiter_quick:

                //验证码
                vercode = mVercodeText.getText().toString();

                //输入密码
                inputPsw = mPwdText.getText().toString();

                //确认密码
                mPwd = pwd_ret.getText().toString();

                if (vercode.length() == 0) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputPsw.length() == 0) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mPwd.length() == 0) {
                    Toast.makeText(this, "请确认输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputPsw.equals(mPwd)) {


                    //校验验证码
                    mPresenter.checkVercode(vercode, mMobile);


                } else {

                    Toast.makeText(this, "请确保两次输入密码一致", Toast.LENGTH_SHORT).show();
                    return;

                }
                break;

        }
    }

}
