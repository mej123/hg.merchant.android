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

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StatusBarUtil;
import top.ftas.ftasbase.common.util.StringUtil;

/**
 * 二维码收款
 * Created by wislie on 2017/11/2.
 */

public class ScanCodeRecipientActivity extends BaseFragmentActivity {

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

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan_code_recipient;
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

    @OnClick({R.id.custom_action_home})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.custom_action_home:
                finish();
                break;
        }
    }


}
