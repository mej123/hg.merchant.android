package com.zishan.sardinemerchant.activity.personal.setting;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.RegexUtil;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * 设置新密码
 * Created by yang on 2017/9/6.
 */

public class ResetNewPswActivity extends BActivity<ResetNewPwdView, ResetPswPresenter>
        implements ResetNewPwdView {

    @BindView(R.id.pwd_input)
    EditText pwdInput;
    @BindView(R.id.pwd_ret)
    EditText retpwdInput;

    @BindView(R.id.iv_new_pwd_close)
    ImageView pwdIcon;
    @BindView(R.id.iv_ret_pwd_close)
    ImageView retpwdIcon;

    @BindView(R.id.setting_pwd_next)
    TextView mCompleteText;

    private String checkVercodeId;
    private String mobile;

    private boolean mPwdHided = true;
    private boolean mRetPwdHided = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_psw;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            checkVercodeId = intent.getStringExtra("checkVercodeId");
            mobile = intent.getStringExtra("mobile");
        }
        mCompleteText.setText(getResources().getText(R.string.complete));
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.reset_new_pwd));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected ResetPswPresenter createPresenter() {
        return new ResetPswPresenter(this, this);
    }


    @OnClick({R.id.iv_new_pwd_close, R.id.iv_ret_pwd_close, R.id.setting_pwd_next})
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
            case R.id.setting_pwd_next:
                settingPwd();
                break;
        }
    }



    /**
     * 设置新密码
     */
    private void settingPwd() {


        String newpwd = pwdInput.getText().toString();
        String retpwd = retpwdInput.getText().toString();

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
    public void resetPwdSucess(Object data) {
        CustomToast.makeText(this, "修改密码成功", 1000 * 2);
        ActivityManager.getInstance().finishActivity(MobileVerifyActivity.class);
        finish();
    }

    @Override
    public void resetPwdFailed() {

    }
}
