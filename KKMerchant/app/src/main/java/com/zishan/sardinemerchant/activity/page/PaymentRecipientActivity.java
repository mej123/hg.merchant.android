package com.zishan.sardinemerchant.activity.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.page.qrcode.QRCodePresenter;
import com.example.wislie.rxjava.view.base.page.QRCodeView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.runtimepermission.MPermissionUtils;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.FileUtil;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * 买单收银
 * Created by wislie on 2017/11/2.
 */

public class PaymentRecipientActivity extends BActivity<QRCodeView, QRCodePresenter> implements QRCodeView {

    @BindView(R.id.consume_amount_unit)
    TextView mConsumeUnitText;
    @BindView(R.id.consume_amount_input)
    EditText mConsumeInput;
    @BindView(R.id.out_discount_amount_unit)
    TextView mDiscountUnitText;
    @BindView(R.id.out_discount_amount_input)
    EditText mDiscountInput;
    @BindView(R.id.scan_code_generate)
    TextView mGenerateText;


    private Long amount = null;
    private Long no_discount_amount = null;

    @Override
    protected QRCodePresenter createPresenter() {
        return new QRCodePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_payment_recipient;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(UserConfig.getInstance(ClientApplication.getApp()).getMerchantName());
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        mConsumeInput.addTextChangedListener(new InputWatcher(mConsumeUnitText));
        mDiscountInput.addTextChangedListener(new InputWatcher(mDiscountUnitText));
        SoftInputUtil.showSoftInput(this, mConsumeInput);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.scan_code_recipient_confirm, R.id.scan_code_generate, R.id.ticket_title})
    public void onClick(View view) {
        switch (view.getId()) {
            //二维码收款
            case R.id.scan_code_recipient_confirm:
                requestQRCodePermission(null, null, false);
                break;
            //生成二维码
            case R.id.scan_code_generate:
                String consumeInput = mConsumeInput.getText().toString().trim();
                String discountInput = mDiscountInput.getText().toString().trim();
                if (TextUtils.isEmpty(consumeInput) && TextUtils.isEmpty(discountInput)) return;

                transformString2Long(consumeInput, discountInput);
                requestQRCodePermission(amount, no_discount_amount, true);
                break;
            //验券
            case R.id.ticket_title:
                Intent intent = new Intent(this, QRcodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    //转换字符串为Long类型
    private void transformString2Long(String consumeInput, String discountInput) {
        if (!TextUtils.isEmpty(consumeInput)) {
            amount = StringUtil.String2Long(consumeInput, 2, true);
        } else {
            amount = null;
        }
        if (!TextUtils.isEmpty(discountInput)) {
            no_discount_amount = StringUtil.String2Long(discountInput, 2, true);
        } else {
            no_discount_amount = null;
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
    public void generateQRCodeSuccess(String data, boolean isInputGenerated) {
        Bitmap qRBitmap = CodeUtils.createImage(data, DensityUtil.dip2px(this, 175),
                DensityUtil.dip2px(this, 175), null);

        String qRCodePath = FileUtil.generateFile(qRBitmap);

        CustomToast.makeText(PaymentRecipientActivity.this, "二维码已生成", 2000).show();
        skipNext(qRCodePath, isInputGenerated);
    }

    //生成二维码权限
    private void requestQRCodePermission(final Long amount, final Long no_discount_amount, final boolean isInputGenerated) {

        MPermissionUtils.requestPermissionsResult(this, Constant.PERMISSION_QR_CODE, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mPresenter.generateQRCode(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                amount, no_discount_amount, isInputGenerated);
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });
    }


    private void skipNext(String qrcode_path, boolean isInputGenerated) {
        Intent intent = new Intent(this, ScanCodeRecipientActivity.class);
        if (isInputGenerated) {
            intent.putExtra("amount", amount);
            intent.putExtra("no_discount_amount", no_discount_amount);
        }
        intent.putExtra("isInputGenerated", isInputGenerated);
        intent.putExtra("qrcode_path", qrcode_path);
        startActivity(intent);
    }

    @Override
    public void generateQRCodeFailed() {

    }

    private class InputWatcher implements TextWatcher {

        private TextView mText;

        public InputWatcher(TextView text) {
            mText = text;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            StringUtil.pointDigitLimited(s, 2);
            if (s.length() == 0) {
                mText.setTextColor(ContextCompat.getColor(PaymentRecipientActivity.this,
                        R.color.text_color_gray));
                enableGenerateIcon();
                return;
            }
            mText.setTextColor(ContextCompat.getColor(PaymentRecipientActivity.this,
                    R.color.text_color_orange));
            enableGenerateIcon();
        }
    }

    //生成二维码是否可点击
    private void enableGenerateIcon() {
        String consumeInput = mConsumeInput.getText().toString().trim();
        if (TextUtils.isEmpty(consumeInput)) {
            mGenerateText.setSelected(false);
            mGenerateText.setEnabled(false);
            return;
        }
        mGenerateText.setSelected(true);
        mGenerateText.setEnabled(true);
        return;
    }
}
