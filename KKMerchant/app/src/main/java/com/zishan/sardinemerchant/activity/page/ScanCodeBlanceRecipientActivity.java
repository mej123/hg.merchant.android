package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseFragmentActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StatusBarUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 结算的二维码收款
 * Created by wislie on 2017/11/2.
 */

public class ScanCodeBlanceRecipientActivity extends BaseFragmentActivity {

    @BindView(R.id.qr_icon)
    ImageView mQRImageView;

    @BindView(R.id.consume_amount)
    TextView mConsumeAmountText;
    @BindView(R.id.consume_amount_out_discount)
    TextView mDiscountAmountText;

    @BindView(R.id.consume_amount_title)
    TextView mConsumeAmountTitleText;
    @BindView(R.id.consume_amount_out_discount_title)
    TextView mDiscountAmountTitleText;

    private long amount = 0, no_discount_amount = 0;

    private boolean isInputGenerated = false;
    //订单id
    private String order_id;
    //总价
    private long totalAmounts = 0;
    //就餐id
    private long repast_id = 0;
    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan_code_blance_recipient;
    }

    @Override
    protected void initContentView() {
        //设置状态栏
        StatusBarUtil.transparencyBar(this);

        Intent intent = getIntent();
        String qrcodePath = null;
        if (intent != null) {
            qrcodePath = intent.getStringExtra("qrcode_path");
            amount = intent.getLongExtra("amount", amount);
            no_discount_amount = intent.getLongExtra("no_discount_amount", no_discount_amount);
            isInputGenerated = intent.getBooleanExtra("isInputGenerated", isInputGenerated);
            order_id = intent.getStringExtra("order_id");
            repast_id = intent.getLongExtra("repast_id", repast_id);
        }

        if (!isInputGenerated) {
            mConsumeAmountTitleText.setVisibility(View.GONE);
            mDiscountAmountTitleText.setVisibility(View.GONE);
        } else {
            mConsumeAmountText.setText(StringUtil.point2String(amount) + "元");
            mDiscountAmountText.setText(StringUtil.point2String(no_discount_amount) + "元");
        }

        if (!TextUtils.isEmpty(qrcodePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(qrcodePath, null);
            mQRImageView.setImageBitmap(bitmap);
        }

    }

    @OnClick({R.id.custom_action_home, R.id.receive_payment_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.custom_action_home:
                finish();
                break;
            case R.id.receive_payment_confirm:
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("您确认收到该笔款项?", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(ScanCodeBlanceRecipientActivity.this, ReceiptSuccessActivity.class);
                        intent.putExtra("order_id", order_id);
                        intent.putExtra("is_qrcode_reciept", true);
                        intent.putExtra("receive_price", totalAmounts);
                        intent.putExtra("repast_id", repast_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }


}
