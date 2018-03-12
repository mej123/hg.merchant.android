package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.activity.personal.order.StoreOrderDetailActivity;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.StringUtil;

/**
 * 收款成功
 * Created by wislie on 2017/12/4.
 */

public class ReceiptSuccessActivity extends BActivity {
    @BindView(R.id.receipt_success_text)
    TextView mReceiptSuccessText;
    @BindView(R.id.receipt_price)
    TextView mReceiptPriceText;

    //true表示二维码收款，false表示现金收款
    private boolean mIsQRCodeReciept;

    private long mReceivedPrice = 0;
    //订单id
    private String order_id;
    //就餐id
    private long repast_id = 0;
    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_receipt_success;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().finishAllActivityExcept(MainPageActivity.class);
            }
        });
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            mIsQRCodeReciept = intent.getBooleanExtra("is_qrcode_reciept", mIsQRCodeReciept);
            mReceivedPrice = intent.getLongExtra("receive_price", mReceivedPrice);
            order_id = intent.getStringExtra("order_id");
            repast_id = intent.getLongExtra("repast_id", repast_id);
        }

        if (!mIsQRCodeReciept) {
            mReceiptSuccessText.setText("收款成功");
        } else {
            mReceiptSuccessText.setText("成功收取现金");
        }

        mReceiptPriceText.setText(StringUtil.point2String(mReceivedPrice));
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.view_order_detail_text})
    public void onClick(View view) {
        switch (view.getId()) {
            //查看订单
            case R.id.view_order_detail_text:
                ActivityManager.getInstance().finishAllActivityExcept(MainPageActivity.class);
                Intent intent = new Intent(this, StoreOrderDetailActivity.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("repast_id", repast_id);
                intent.putExtra("clean_table", true);
                startActivity(intent);
                break;
        }
    }
}
