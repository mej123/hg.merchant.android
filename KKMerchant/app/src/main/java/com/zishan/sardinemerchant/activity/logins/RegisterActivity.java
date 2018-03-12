package com.zishan.sardinemerchant.activity.logins;

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

import com.example.wislie.rxjava.model.CheckVercodeEntity;
import com.example.wislie.rxjava.model.RegisterEntity;
import com.example.wislie.rxjava.model.VerifyCodeEntity;
import com.example.wislie.rxjava.presenter.base.login.RegisterPresenter;
import com.example.wislie.rxjava.view.base.login.RegisterView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.RegexUtil;
import top.ftas.ftasbase.common.util.SoftInputUtil;


/**
 * 免费入驻
 * Created by yang on 2017/9/11.
 */

public class RegisterActivity extends BActivity<RegisterView, RegisterPresenter> implements RegisterView {

    @BindView(R.id.phone_input)
    EditText mInputPhoneEdit; //输入手机号
    @BindView(R.id.verify_code_input)
    EditText mInputVerifyCodeEdit; //输入验证码
    @BindView(R.id.get_verify_code)
    TextView mGetVerifyCodeText; //获取验证码
    @BindView(R.id.pwd_input)
    EditText mInputPwdEdit; //输入密码
    @BindView(R.id.confirm_pwd_input)
    EditText mInputConfirmPwdEdit; //确认密码

    @BindView(R.id.iv_phone_delete)
    ImageView mPhoneDelIcon;
    @BindView(R.id.iv_verify_code_delete)
    ImageView mVerifyCodeDelIcon;
    @BindView(R.id.iv_input_psw_hide)
    ImageView mHidePwdIcon;
    @BindView(R.id.iv_input_confirm_psw_hide)
    ImageView mHideConfirmPwdIcon;

    @BindView(R.id.user_protocol)
    ImageView mUserProtocolIcon;

    @BindView(R.id.bind)
    TextView mBindBtn; //注册


    //手机号
    private String mPhoneNum;
    //验证码
    private String mVercode;
    //输入的密码 和 确认密码
    private String mInputPwd;
    private String mInputConfirmPwd;

    //密码或确认密码是否隐藏, false表示不隐藏, 默认不隐藏
    private boolean mIsPwdHided = true;
    private boolean mIsConfirmPwdHided = true;

    //默认情况下位不同意
    private boolean mIsAgree = false;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.register_title));
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        setActionBarHomeIcon(R.mipmap.back_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this, this);
    }


    @Override
    protected void initContentView() {

        mInputPhoneEdit.addTextChangedListener(new CustomTextWatcher(mPhoneDelIcon));
        mInputVerifyCodeEdit.addTextChangedListener(new CustomTextWatcher(mVerifyCodeDelIcon));
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
    public void getVerifyCodeComplete(VerifyCodeEntity data) { //获取验证码成功
        //获取验证码 后倒计时
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetVerifyCodeText, 60000, 1000);
        countDownTimerUtils.start();
    }

    @Override
    public void getVerifyCodeFailed() {

    }

    @Override
    public void checkVerifyCodeComplete(CheckVercodeEntity data) { //校验成功
        String checkVercodeId = data.getCheckVercodeId();
        //立即注册
        mPresenter.requestRegister(checkVercodeId, mPhoneNum, mInputPwd);
    }

    @Override
    public void checkVerifyCodeFailed() {

    }

    @Override
    public void registerComplete(RegisterEntity data) { //注册完成
        //跳转到登录界面
        finish();
    }

    @Override
    public void registerFailed() {

    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }


    @OnClick({R.id.iv_phone_delete, R.id.get_verify_code, R.id.bind, R.id.protocol_layout, R.id.return_login,
            R.id.iv_verify_code_delete, R.id.iv_input_psw_hide, R.id.iv_input_confirm_psw_hide})
    public void onClick(View view) {
        switch (view.getId()) {

            //绑定
            case R.id.bind:
                //手机号
                mPhoneNum = mInputPhoneEdit.getText().toString();
                //验证码
                mVercode = mInputVerifyCodeEdit.getText().toString();
                //输入密码
                mInputPwd = mInputPwdEdit.getText().toString();
                //确认密码
                mInputConfirmPwd = mInputConfirmPwdEdit.getText().toString();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
               /* if (!RegexUtil.isMobile(mPhoneNum)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if (TextUtils.isEmpty(mVercode)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mInputPwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mInputConfirmPwd)) {
                    Toast.makeText(this, "请确认输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mInputPwd.equals(mInputConfirmPwd)) {
                    Toast.makeText(this, "请确保两次输入密码一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!RegexUtil.isPwd(mInputPwd)) {
                    Toast.makeText(this, "密码应为数字字母组合(6-20位)", Toast.LENGTH_SHORT).show();
                    return;
                }
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                //校验验证码
                mPresenter.requestCheckVerifyCode(mVercode, mPhoneNum);
                break;
            //手机号删除
            case R.id.iv_phone_delete:
                mInputPhoneEdit.setText("");
                break;
            //验证码删除
            case R.id.iv_verify_code_delete:
                mInputVerifyCodeEdit.setText("");
                break;
            //显示/隐藏密码
            case R.id.iv_input_psw_hide:
                mIsPwdHided = !mIsPwdHided;
                if (mIsPwdHided) { //隐藏
                    mHidePwdIcon.setImageResource(R.mipmap.close_input);
                    mInputPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else { //显示
                    mHidePwdIcon.setImageResource(R.mipmap.open_input);
                    mInputPwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                //设置光标在最后
                mInputPwdEdit.setSelection(mInputPwdEdit.getText().toString().length());
                break;

            //显示/隐藏确认密码
            case R.id.iv_input_confirm_psw_hide:
                mIsConfirmPwdHided = !mIsConfirmPwdHided;
                if (mIsConfirmPwdHided) { //隐藏
                    mHideConfirmPwdIcon.setImageResource(R.mipmap.close_input);
                    mInputConfirmPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else { //显示
                    mHideConfirmPwdIcon.setImageResource(R.mipmap.open_input);
                    mInputConfirmPwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                //设置光标在最后
                mInputConfirmPwdEdit.setSelection(mInputConfirmPwdEdit.getText().toString().length());
                break;
            //获取验证码
            case R.id.get_verify_code:
                mPhoneNum = mInputPhoneEdit.getText().toString();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                mPresenter.requestVerifyCode(mPhoneNum);
                break;

            //同意or不同意协议
            case R.id.protocol_layout:
                mIsAgree = !mIsAgree;
                if (mIsAgree) {
                    mUserProtocolIcon.setImageResource(R.mipmap.agree_icon);
                } else {
                    mUserProtocolIcon.setImageResource(R.mipmap.disagree_icon);
                }
                mBindBtn.setEnabled(mIsAgree);
                break;
            //返回登录
            case R.id.return_login:
                finish();
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
