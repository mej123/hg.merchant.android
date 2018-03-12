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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.page.qrcode.QRCodePresenter;
import com.example.wislie.rxjava.view.base.page.QRCodeView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.SettingInputDialog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.runtimepermission.MPermissionUtils;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.FileUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * 结算
 * Created by wislie on 2017/11/2.
 */

public class BalanceRecipientActivity extends BActivity<QRCodeView, QRCodePresenter> implements QRCodeView {

    @BindView(R.id.consume_amount_unit)
    TextView mConsumeUnitText;
    @BindView(R.id.consume_amount_input)
    EditText mConsumeInput;
    @BindView(R.id.out_discount_amount_unit)
    TextView mDiscountUnitText;
    @BindView(R.id.out_discount_amount_input)
    EditText mDiscountInput;
    @BindView(R.id.scan_code_generate)
    LinearLayout mGenerateCodeLayout; //生成二维码布局
    @BindView(R.id.scan_code_generate_icon)
    ImageView mGenerateIcon;
    @BindView(R.id.scan_code_generate_title)
    TextView mGenerateText;

    private Long amount = null;
    private Long no_discount_amount = null;

    //订单id
    private String order_id;
    //总价
    private long totalAmounts = 0;
    //就餐id
    private long repast_id = 0;
    //桌台id
    private long table_id = 0;

    @Override
    protected QRCodePresenter createPresenter() {
        return new QRCodePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blance_recipient;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("order_id");
            totalAmounts = intent.getLongExtra("totalAmounts", totalAmounts);
            repast_id = intent.getLongExtra("repast_id", repast_id);
            table_id = intent.getLongExtra("table_id", table_id);
        }
        setActionbarTitle(getResources().getString(R.string.blance));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        mConsumeInput.addTextChangedListener(new InputWatcher(mConsumeUnitText));
        mDiscountInput.addTextChangedListener(new InputWatcher(mDiscountUnitText));
        if (totalAmounts != 0) {
            mConsumeInput.setText(StringUtil.point2String(totalAmounts));
        } else {
            mDiscountInput.setEnabled(false);
            mGenerateIcon.setImageResource(R.mipmap.scan_code_disable);
            mGenerateCodeLayout.setBackgroundResource(R.drawable.payment_recipient_gray_shape);
            mGenerateText.setTextColor(ContextCompat.getColor(this,
                    R.color.text_color_bright_gray));
            mGenerateCodeLayout.setEnabled(false);
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.scan_code_generate, R.id.receive_cash})
    public void onClick(View view) {
        switch (view.getId()) {
            //已收取现金
            case R.id.receive_cash:
                receiveCash();
                break;
            //生成二维码
            case R.id.scan_code_generate:
                String consumeInput = mConsumeInput.getText().toString().trim();
                String discountInput = mDiscountInput.getText().toString().trim();

                transformString2Long(consumeInput, discountInput);
                requestQRCodePermission();

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
        Bitmap qRBitmap = CodeUtils.createImage(data, DensityUtil.dip2px(BalanceRecipientActivity.this, 175),
                DensityUtil.dip2px(BalanceRecipientActivity.this, 175), null);

        String qRCodePath = FileUtil.generateFile(qRBitmap);

        CustomToast.makeText(BalanceRecipientActivity.this, "二维码已生成", 2000).show();
        skipNext(qRCodePath, isInputGenerated);
    }

    //生成二维码权限
    private void requestQRCodePermission() {

        MPermissionUtils.requestPermissionsResult(this, Constant.PERMISSION_QR_CODE, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mPresenter.generateQRCodeFromBlance(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                repast_id, table_id, true);
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });
    }


    private void skipNext(String qrcode_path, boolean isInputGenerated) {
        Intent intent = new Intent(this, ScanCodeBlanceRecipientActivity.class);
        if (isInputGenerated) {
            intent.putExtra("amount", amount);
            intent.putExtra("no_discount_amount", no_discount_amount);
        }
        intent.putExtra("order_id", order_id);
        intent.putExtra("repast_id", repast_id);
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

            if (s.length() == 0) {
                mText.setTextColor(ContextCompat.getColor(BalanceRecipientActivity.this,
                        R.color.text_color_gray));

                return;
            }
            mText.setTextColor(ContextCompat.getColor(BalanceRecipientActivity.this,
                    R.color.text_color_orange));


        }
    }

    //收取现金
    private void receiveCash() {
        SettingInputDialog dialog = SettingInputDialog.newInstance("您确认已收金额", "请输入已收金额",
                SettingInputDialog.InputStyle.NUMBER_DECIMAL);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                Intent intent = new Intent(BalanceRecipientActivity.this, ReceiptSuccessActivity.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("is_qrcode_reciept", false);
                intent.putExtra("receive_price", StringUtil.String2Long(values[0], 2, true));
                intent.putExtra("repast_id", repast_id);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }
        });
    }

}
