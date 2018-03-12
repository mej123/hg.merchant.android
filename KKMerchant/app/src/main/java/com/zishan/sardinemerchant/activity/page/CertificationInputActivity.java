package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.CertificationConfirmEntity;
import com.example.wislie.rxjava.model.page.CouponProductEntity;
import com.example.wislie.rxjava.model.page.CouponResultEntity;
import com.example.wislie.rxjava.presenter.base.page.certification.ScanCodeResultPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.page.certification.ScanCodeResultView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;

/**
 * 输入核销码
 * Created by wislie on 2017/11/20.
 */

public class CertificationInputActivity extends BActivity<ScanCodeResultView,
        ScanCodeResultPresenter> implements ScanCodeResultView {

    @BindView(R.id.input_cerification_code)
    EditText mInputCodeEdit;

    @BindView(R.id.cerification_code_confirm)
    Button mConfirmBtn;

    @Override
    protected ScanCodeResultPresenter createPresenter() {
        return new ScanCodeResultPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cerification_input;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionbarTitle(getString(R.string.input_cerification_code));
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        mInputCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() <= 0) {
                    mConfirmBtn.setSelected(false);
                    mConfirmBtn.setEnabled(false);
                } else {
                    mConfirmBtn.setSelected(true);
                    mConfirmBtn.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.cerification_code_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            //确认核销码
            case R.id.cerification_code_confirm:

                String content = mInputCodeEdit.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(this, "输入不能为空");
                    return;
                }

                long coupon_id = 0;
                //如果合法
                try {
                    coupon_id = Long.parseLong(content);
                } catch (NumberFormatException e) {
                    coupon_id = 0;
                }
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                mPresenter.getCouponResult(UserConfig.getInstance(
                        ClientApplication.getApp()).getStoreId(), coupon_id);


                break;
        }
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
    public void getScanCodeResultSuccess(CouponResultEntity data) {
        Intent intent = new Intent(this, ScanCodeResultActivity.class);
        ArrayList<CouponProductEntity> dataList = data.getRefProducts();
        if (dataList != null && dataList.size() > 0) {
            intent.putExtra("coupon_id", data.getCouponInstanceId());
            intent.putParcelableArrayListExtra("couponlist", dataList);
            startActivity(intent);
        }
    }

    @Override
    public void getScanCodeResultFailed() {

    }

    @Override
    public void confirmCertificationSuccess(CertificationConfirmEntity data) {

    }

    @Override
    public void confirmCertificationFailed() {

    }
}
