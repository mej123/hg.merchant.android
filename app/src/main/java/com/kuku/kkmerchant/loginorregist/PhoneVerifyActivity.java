package com.kuku.kkmerchant.loginorregist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wislie.rxjava.model.PhoneVerificationEntity;
import com.example.wislie.rxjava.model.VerificationQuitEntity;
import com.example.wislie.rxjava.presenter.PhoneVerificationPresenterB;
import com.example.wislie.rxjava.view.PhoneVerificationView;
import com.hg.ftas.frame.IFtasPage;
import com.hg.ftas.util.OpenPageUtil;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.utils.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;



/**
 * Created by yang on 2017/9/8.
 */

public class PhoneVerifyActivity extends BaseActivity<PhoneVerificationView, PhoneVerificationPresenterB> implements PhoneVerificationView, IFtasPage {

    @BindView(R.id.tv_tishi_message)
    TextView tvTishiMessage;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.yanzhengma)
    RelativeLayout yanzhengma;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    Unbinder unbinder;
    private String preLoginId;
    private String vercode;
    private Long limitInTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.phone_yanzheng_layout;
    }

    @Override
    protected void initContentView() {

        preLoginId = getIntent().getStringExtra("preLoginId");
        String pNumber = getIntent().getStringExtra("mobil");
        if(!TextUtils.isEmpty(pNumber) && pNumber.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
          tvTishiMessage.setText("为了您的账户安全请进行短信验证。验证码已发送至手机"+sb.toString()+",  请按提示操作。");
        }
        initdata();
    }
    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.phone_yanzheng));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected PhoneVerificationPresenterB createPresenter() {
        return new PhoneVerificationPresenterB(this, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initdata() {

    }

    @OnClick({R.id.yanzhengma, R.id.rl_queding_layout,R.id.tv_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yanzhengma:

                break;
            case R.id.rl_queding_layout:

                vercode = etYanzhengma.getText().toString();

                if (vercode.length()==0){
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                mPresenter.phoneYanzheng(vercode,preLoginId);
                break;

            case R.id.tv_get_code:
                getCode();
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(tvGetCode, 60 * 1000, 1000);
                countDownTimerUtils.start();
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void requestFailed(String message) {

    }

    //获取验证码成功
    @Override
    public void getVercodeYanzheng(PhoneVerificationEntity phoneVerificationEntity) {

    }

    //手机验证成功
    @Override
    public void phoneYanzhengQuit(VerificationQuitEntity verificationQuitEntity) {
        //跳转到js交互界面F
        String url = "https://h5.tenv.mttstudio.net/bridge/index.html ";
        OpenPageUtil.startUrl(url, this);

    }

    public void getCode() {
        //传过来的pre_login_id参数
       // String preLoginId = " b280f8ab-9283-4386-af5a-a04963b025a2";


      mPresenter.getVercodeYanzheng(preLoginId);
    }

    @Override
    public void finishAfterAnim() {

    }

    @Override
    public void finishWithAnim() {

    }

    @Override
    public void finishWithReverseAnim() {

    }

    @Override
    public void startActivityWithAnim(Intent intent) {

    }

    @Override
    public void startActivityWithReverseAnim(Intent intent) {

    }

    @Override
    public void startActivityWithAnim(Intent intent, int inAnim, int outAnim) {

    }
}
