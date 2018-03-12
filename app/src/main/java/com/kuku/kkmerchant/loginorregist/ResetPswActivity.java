package com.kuku.kkmerchant.loginorregist;

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

import com.example.wislie.rxjava.presenter.ResetPswPresenter;
import com.example.wislie.rxjava.view.ResetPswView;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by yang on 2017/9/6.
 */

public class ResetPswActivity extends BaseActivity<ResetPswView, ResetPswPresenter> implements ResetPswView {

    @BindView(R.id.pwd_ret1)
    EditText pwdRet1;
    Unbinder unbinder;
    @BindView(R.id.login_submit_btn1)
    TextView loginSubmitBtn1;
    @BindView(R.id.iv_input_psw1)
    ImageView ivInputPsw1;
    @BindView(R.id.pwd_ret2)
    EditText pwdRet2;
    @BindView(R.id.iv_queren_psw2)
    ImageView ivQuerenPsw2;
    boolean eyeOpen = true;
    boolean eyeOpen1 = true;
    private String checkVercodeId;
    private String mobile;

    @Override
    protected int getLayoutResId() {
        return R.layout.reset_psw_layout;
    }

    @Override
    protected void initContentView() {


        checkVercodeId = getIntent().getStringExtra("checkVercodeId");
        mobile = getIntent().getStringExtra("mobile");


        init();

    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.reset_psw));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }


    private void init() {

        pwdRet1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                loginSubmitBtn1.setEnabled(true);
                if (s.length() > 0 && !TextUtils.isEmpty(s)) {

                    loginSubmitBtn1.setEnabled(true);
                } else {
                    loginSubmitBtn1.setEnabled(false);
                }


            }
        });


    }


    @OnClick({R.id.rl_input_psw_layout1, R.id.rl_queren_psw_layout2, R.id.login_submit_btn1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_input_psw_layout1:

                if (eyeOpen) {
                    //密码
                    pwdRet1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivInputPsw1.setImageResource(R.mipmap.yanjing_guangbi);
                    eyeOpen = false;
                } else {
                    //明文
                    pwdRet1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivInputPsw1.setImageResource(R.mipmap.yanjing_dakai);
                    eyeOpen = true;
                }
                break;
            case R.id.rl_queren_psw_layout2:

                if (eyeOpen1) {
                    //密码
                    pwdRet2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivQuerenPsw2.setImageResource(R.mipmap.yanjing_guangbi);
                    eyeOpen1 = false;
                } else {
                    //明文
                    pwdRet2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivQuerenPsw2.setImageResource(R.mipmap.yanjing_dakai);
                    eyeOpen1 = true;
                }

                break;
            case R.id.login_submit_btn1:

                String newPsw = pwdRet1.getText().toString().trim();
                String querenPsw = pwdRet2.getText().toString().trim();

                if (querenPsw.length() == 0) {
                    Toast.makeText(this, "请输入确认新密码", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!newPsw.equals(querenPsw)) {
                    Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                mPresenter.resetPassWord(mobile, querenPsw, checkVercodeId);

                break;
        }
    }


    @Override
    public void requestFailed(String message) {

    }

    @Override
    public void resetPassWord(Object resetPswEntity) {

        Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected ResetPswPresenter createPresenter() {
        return new ResetPswPresenter(this, this);
    }
}
