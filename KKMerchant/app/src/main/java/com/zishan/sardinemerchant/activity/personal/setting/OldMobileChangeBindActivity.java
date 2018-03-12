package com.zishan.sardinemerchant.activity.personal.setting;

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

import com.example.wislie.rxjava.model.CheckVercodeEntity;
import com.example.wislie.rxjava.model.VerifyCodeEntity;
import com.example.wislie.rxjava.presenter.base.personal.OldPhonePresenter;
import com.example.wislie.rxjava.view.base.personal.OldPhoneView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定里的 手机验证
 * Created by wislie on 2017/11/28.
 */

public class OldMobileChangeBindActivity extends BActivity<OldPhoneView, OldPhonePresenter> implements OldPhoneView {

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
        return R.layout.activity_forget_psw;
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


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.phone_verification));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }


    @Override
    protected OldPhonePresenter createPresenter() {
        return new OldPhonePresenter(this, this);
    }

    @OnClick({R.id.pwd_forget_next, R.id.iv_phone_delete, R.id.iv_verify_code_delete, R.id.get_verify_code})
    public void onClick(View view) {
        switch (view.getId()) {
            //下一步
            case R.id.pwd_forget_next:
                changePhoneNext();
                break;
            //手机号删除
            case R.id.iv_phone_delete:
                mPhoneInput.setText("");
                break;
            //验证码删除
            case R.id.iv_verify_code_delete:
                mVerifyCodeInput.setText("");
                break;
            //获取验证码
            case R.id.get_verify_code:
                getVerifyCode();
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

    /*    if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }*/
        mPresenter.requestBindPhoneVerifyCode(mPhoneNum);

    }

    /**
     * 下一步
     */
    private void changePhoneNext() {

      /*  mPhoneNum = mPhoneInput.getText().toString();
        if (TextUtils.isEmpty(mPhoneNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
*/
       /* if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }*/

        String verifyCode = mVerifyCodeInput.getText().toString();

        if (TextUtils.isEmpty(verifyCode)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //验证码验证成功，跳转到设置密码界面
        mPresenter.requestCheckPhone(verifyCode);
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
        //已获取验证码
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mGetVerifyCode, 60000, 1000);
        countDownTimerUtils.start();
    }

    @Override
    public void getVerifyCodeFailed() {

    }

    @Override
    public void checkPhoneSuccess(CheckVercodeEntity data) {
        Intent intent = new Intent(this, NewMobileBindActivity.class);
        intent.putExtra("check_vercode_id", data.getCheckVercodeId());
        startActivity(intent);
    }

    @Override
    public void checkPhoneFailed() {

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
