package com.kuku.kkmerchant.loginorregist;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.model.CheckVercodeEntity;
import com.example.wislie.rxjava.model.VercodeEntity;
import com.example.wislie.rxjava.presenter.ForgetPswPresenter;
import com.example.wislie.rxjava.view.ForgetPswView;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.utils.CountDownTimerUtils;
import com.kuku.kkmerchant.utils.Skip;
import com.kuku.kkmerchant.utils.VerificationUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;



/**
 * Created by yang on 2017/9/6.
 */

public class ForgetPswActivity extends BaseActivity<ForgetPswView, ForgetPswPresenter> implements ForgetPswView {


    @BindView(R.id.phone_input)
    EditText phoneInput;
    @BindView(R.id.tv_obtain_code)
    TextView tvObtainCode;
    @BindView(R.id.shurumima)
    EditText shurumima;
    Unbinder unbinder;
    @BindView(R.id.login_submit_btn)
    TextView loginSubmitBtn;
    private String mobile;

    @Override
    protected int getLayoutResId() {
        return R.layout.forget_psw_layout;
    }

    @Override
    protected void initContentView() {
        init();
    }


    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.forget_psw));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    private void init() {

        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginSubmitBtn.setEnabled(true);
                if (s.length() > 0 && !TextUtils.isEmpty(s)) {

                    loginSubmitBtn.setEnabled(true);
                } else {
                    loginSubmitBtn.setEnabled(false);
                }
            }
        });


    }

    @OnClick({R.id.tv_obtain_code, R.id.login_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_obtain_code:
                if (VerificationUtils.matcherPhoneNum(phoneInput.getText().toString())) {
                    mobile = phoneInput.getText().toString();
                    //拿到手机号,发送验证码
                    mPresenter.getVercode(mobile);
                    //sendCode();

                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.login_submit_btn:


                String vercode = shurumima.getText().toString();
                mPresenter.checkVercode(vercode,mobile);
                //点击下一步,验证码验证成功，跳转到设置密码界面
                if (shurumima.length() == 0) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Skip.toActivity(this,ResetPswActivity.class);
                break;
        }
    }

    @Override
    public void requestFailed(String message) {

    }

    @Override
    public void getVercode(VercodeEntity vercodeEntity) {

        Long limitInTime = vercodeEntity.getLimitInTime();
      //  Toast.makeText(this, "获取验证码"+limitInTime, Toast.LENGTH_SHORT).show();
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(tvObtainCode, 60000, 1000);
        countDownTimerUtils.start();
        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void checkVercode(CheckVercodeEntity checkVercodeEntity) {

        String checkVercodeId = checkVercodeEntity.getCheckVercodeId();
       // Toast.makeText(this, "获取验证码"+checkVercodeId, Toast.LENGTH_SHORT).show();
        Intent ResetPswintent = new Intent(this,ResetPswActivity.class);
        ResetPswintent.putExtra("mobile", mobile);
        ResetPswintent.putExtra("checkVercodeId", checkVercodeId);
        startActivity(ResetPswintent);
    }


    @Override
    protected ForgetPswPresenter createPresenter() {
        return new ForgetPswPresenter(this,this);
    }

}
