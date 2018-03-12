package com.zishan.sardinemerchant.activity.logins;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import com.example.wislie.rxjava.presenter.base.login.ResetPswPresenter;
import com.example.wislie.rxjava.view.base.login.ResetNewPwdView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.RegexUtil;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * Created by yang on 2018/1/3.
 * <p>
 * 设置登录密码
 */

public class SettingLoginPswActivity extends BActivity<ResetNewPwdView, ResetPswPresenter> implements ResetNewPwdView {

    @BindView(R.id.pwd_input)
    EditText pwdInput;
    @BindView(R.id.pwd_ret)
    EditText retpwdInput;

    @BindView(R.id.iv_new_pwd_close)
    ImageView pwdIcon;
    @BindView(R.id.iv_ret_pwd_close)
    ImageView retpwdIcon;
    @BindView(R.id.setting_psw_confirm)
    TextView mSettingPswConfirm;

    private String checkVercodeId;
    private String mobile;

    private boolean mPwdHided = true;
    private boolean mRetPwdHided = true;

    @Override
    protected ResetPswPresenter createPresenter() {
        return new ResetPswPresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.SettingLoginPsw));
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
        setActionBarMenuText(getResources().getString(R.string.jump)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().finishAllActivityExcept(SettingLoginPswActivity.this);
                Intent intent = new Intent(SettingLoginPswActivity.this, MainPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting_login_psw;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            checkVercodeId = intent.getStringExtra("checkVercodeId");
            mobile = intent.getStringExtra("mobile");
        }
        pwdInput.addTextChangedListener(new PswWatcher(pwdIcon));
        retpwdInput.addTextChangedListener(new PswWatcher(retpwdIcon));
    }

    private class PswWatcher implements TextWatcher {
        private ImageView ivDel;

        public PswWatcher(ImageView ivDel) {
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
                mSettingPswConfirm.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            } else {
                mSettingPswConfirm.setBackgroundResource(R.drawable.login_new_confirm_bg);
            }
        }
    }

    ;

    @OnClick({R.id.iv_new_pwd_close, R.id.iv_ret_pwd_close, R.id.setting_psw_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            //显示或隐藏 新密码
            case R.id.iv_new_pwd_close:
                mPwdHided = !mPwdHided;
                if (mPwdHided) { //隐藏
                    pwdIcon.setImageResource(R.mipmap.close_input);
                    pwdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else { //显示
                    pwdIcon.setImageResource(R.mipmap.open_input);
                    pwdInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                //设置光标在最后
                pwdInput.setSelection(pwdInput.getText().toString().length());

                break;
            //显示或隐藏 确认新密码
            case R.id.iv_ret_pwd_close:
                mRetPwdHided = !mRetPwdHided;
                if (mRetPwdHided) { //隐藏
                    retpwdIcon.setImageResource(R.mipmap.close_input);
                    retpwdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else { //显示
                    retpwdIcon.setImageResource(R.mipmap.open_input);
                    retpwdInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                //设置光标在最后
                retpwdInput.setSelection(retpwdInput.getText().toString().length());
                break;
            //下一步
            case R.id.setting_psw_confirm:
                settingPwd();
                break;
        }
    }

    /**
     * 设置新密码
     */
    private void settingPwd() {


        String newpwd = pwdInput.getText().toString().trim();
        String retpwd = retpwdInput.getText().toString().trim();

        if (TextUtils.isEmpty(newpwd)) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(retpwd)) {
            Toast.makeText(this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!RegexUtil.isPwd(newpwd)) {
            Toast.makeText(this, "新密码格式不正确,密码格式应为6到20位数字或字母组合", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!RegexUtil.isPwd(retpwd)) {
            Toast.makeText(this, "新密码格式不正确,密码格式应为6到20位数字或字母组合", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newpwd.equals(retpwd)) {
            Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        //键盘缩回
        SoftInputUtil.hideDirectly(this);
        mPresenter.requestResetPwd(mobile, newpwd, checkVercodeId);
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

    }

    @Override
    public void resetPwdSucess(Object data) {
        CustomToast.makeText(this, "修改密码成功", 1000 * 2);
        ActivityManager.getInstance().finishAllActivityExcept(this);
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void resetPwdFailed() {

    }
}
