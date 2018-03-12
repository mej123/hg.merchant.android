package com.zishan.sardinemerchant.activity.logins;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.LoginEntity;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.presenter.base.login.PswLoginPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.login.PswLoginView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.BridgeTestActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.activity.personal.SellerEntranceActivity;
import com.zishan.sardinemerchant.bridge.BridgeTest;
import com.zishan.sardinemerchant.utils.AccountUtil;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.SoftInputUtil;

import static com.zishan.sardinemerchant.R.id.pwd_login;

/**
 * Created by wislie on 2018/1/3.
 * <p>
 * 账号密码登录
 */

public class NewPswLoginActivity extends BActivity<PswLoginView, PswLoginPresenter>
        implements View.OnClickListener, PswLoginView {


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
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_psw_login));
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected PswLoginPresenter createPresenter() {
        return new PswLoginPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_psw_login;
    }

    @Override
    protected void initContentView() {

        mPhoneInput.addTextChangedListener(mPhoneWatcher);
        mPwdInput.addTextChangedListener(mPswWatcher);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.iv_phone_delete, R.id.iv_input_psw_hide, R.id.pwd_login,
            R.id.phone_verification_login, R.id.pwd_free_into})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phone_delete://手机号删除
                mPhoneInput.setText("");
                mPhoneDel.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_input_psw_hide: //显示密码 或 隐藏密码
                mPwdHided = !mPwdHided;
                if (mPwdHided) { //隐藏
                    mPwdIcon.setImageResource(R.mipmap.close_input);
                    mPwdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else { //显示
                    mPwdIcon.setImageResource(R.mipmap.open_input);
                    mPwdInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                //设置光标在最后
                mPwdInput.setSelection(mPwdInput.getText().toString().length());
                break;
            case R.id.pwd_login://密码登录
                pswLogin();
                break;
            case R.id.phone_verification_login://手机验证登录
                Skip.toActivity(this, NewPhoneCodeLoginActivity.class);
                break;
            case R.id.pwd_free_into://免费入驻
//                Skip.toActivity(this, RegisterActivity.class);
                Skip.toActivity(this, BridgeTest.class);

//                Skip.toActivity(this, SellerEntranceActivity.class);
                break;
        }
    }

    /**
     * 密码登录
     */
    private void pswLogin() {

        mPhoneNum = mPhoneInput.getText().toString();

        if (TextUtils.isEmpty(mPhoneNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (!RegexUtil.isMobile(mPhoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }*/

        String password = mPwdInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //键盘缩回
        SoftInputUtil.hideDirectly(this);
        mPresenter.requestPswLogin(mPhoneNum, password);

    }

    private TextWatcher mPswWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !TextUtils.isEmpty(s)) {
                mPwdLogin.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            } else {
                mPwdLogin.setBackgroundResource(R.drawable.login_new_confirm_bg);
            }
        }
    };

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
                mPwdLogin.setEnabled(true);
                mPwdLogin.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            } else {
                mPwdLogin.setEnabled(false);
                mPhoneDel.setVisibility(View.INVISIBLE);
                mPwdLogin.setBackgroundResource(R.drawable.login_new_confirm_bg);
            }
        }
    };


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
    public void loginSuccess(LoginEntity data) {
        if (data == null) return;
        Log.e("wislie", "token:" + data.getToken());
        if (!TextUtils.isEmpty(data.getToken())) {
            UserConfig.getInstance(ClientApplication.getApp()).setAccessToken(data.getToken());
        }
        if (data.getAccountId() != null) {
            UserConfig.getInstance(ClientApplication.getApp()).setAccountId(data.getAccountId());
        }

        if (data.getPreLoginId() != null) {
            preLoginId = data.getPreLoginId();
            Intent intent = new Intent(this, PhoneVerifyActivity.class);
            intent.putExtra("preLoginId", preLoginId);
            intent.putExtra("mobile", mPhoneNum);
            startActivity(intent);
            return;
        }

        //保存手机号
        UserConfig.getInstance(ClientApplication.getApp()).setUserMobile(mPhoneNum);


        mPresenter.getUserAccount(data.getAccountId());
    }

    @Override
    public void loginFailed() {

    }

    @Override
    public void getUserAccountSuccess(List<UserAccountEntity> dataList) {

        if (dataList == null || dataList.size() == 0) {
            Intent intent = new Intent(this, SellerEntranceActivity.class);
            startActivity(intent);
        } else {
            AccountUtil.saveAccount(dataList.get(0));
            //密码登录 成功跳转到主页
            ToastUtil.show(this, "登录成功");
            ActivityManager.getInstance().finishAllActivityExcept(this);
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getUserAccountFailed() {

    }
}
