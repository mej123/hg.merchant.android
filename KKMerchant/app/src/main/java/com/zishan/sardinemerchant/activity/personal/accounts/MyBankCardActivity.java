package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelMyAccountDialog;
import com.zishan.sardinemerchant.utils.Skip;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;


/**
 * Created by yang on 2017/11/3.
 * <p>
 * 我的银行卡
 */

public class MyBankCardActivity extends BActivity {

    @BindView(R.id.tv_state)
    TextView mState;
    @BindView(R.id.tv_last_four)
    TextView mLastFour;
    @BindView(R.id.rl_bg)
    RelativeLayout mBankBg;
    @BindView(R.id.change_bind_bank_card)
    TextView mChangeBindBankCard;
    private int bindBankState;
    private String isWithdrawIng;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_bank_card;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.my_bank_cark));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent == null) return;
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null) return;
        //绑定状态
        bindBankState = bundle.getInt("bindBankState");
        String bindBankType = bundle.getString("bindBankType");//卡类型
        String bindBankCardNo = bundle.getString("bindBankCardNo");//银行卡号
        isWithdrawIng = bundle.getString("IsWithdrawIng");

        //1.审核中   2.审核失败
        if (bindBankState == 1) {
            mState.setText("审核中");
            mChangeBindBankCard.setEnabled(false);
        } else if (bindBankState == 2) {
            mState.setText("审核失败");
            mChangeBindBankCard.setText("重新绑定");
            mChangeBindBankCard.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            mChangeBindBankCard.setEnabled(true);
        } else {
            mChangeBindBankCard.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);
            mChangeBindBankCard.setEnabled(true);
        }

        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            if (bindBankType.equals(String.valueOf(bankCode))) {
                int bankBg = bankCardBackgroundList[i];
                mBankBg.setBackgroundResource(bankBg);//银行卡背景
            }
        }
        String lastFour = bindBankCardNo.substring(bindBankCardNo.length() - 4, bindBankCardNo.length());
        mLastFour.setText(lastFour);//设置卡号
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    final int[] bankCodeList = new int[]{102, 103, 104, 105, 301, 302,
            303, 304, 305, 306, 308, 309, 310, 319, 403};

    //银行卡背景的一个数组

    int[] bankCardBackgroundList = new int[]{
            R.mipmap.identity_bank_icbc_icon, R.mipmap.identity_bank_abc_icon, R.mipmap.identity_bank_bc_icon,
            R.mipmap.identity_bank_ccb_icon, R.mipmap.identity_bank_bocom_icon, R.mipmap.identity_bank_ecitic_icon, R.mipmap.identity_bank_ceb_icon, R.mipmap.identity_bank_hxb_icon
            , R.mipmap.identity_bank_cmbc_icon, R.mipmap.identity_bank_cgb_icon, R.mipmap.identity_bank_cmbc_icon
            , R.mipmap.identity_bank_cib_icon, R.mipmap.identity_bank_spdb_icon, R.mipmap.identity_bank_hsb_icon, R.mipmap.iidentity_china_post_icon
    };

    @OnClick(R.id.change_bind_bank_card)
    public void onViewClicked() {
        //2  审核失败  直接发起绑卡
        if (bindBankState == 2) {
            Skip.toActivity(this, NewAccountPhoneVerificationActivity.class);
        } else {
            showHintDialog();
        }
    }

    private void showHintDialog() {
        final ConfirmOrCancelMyAccountDialog dialog = ConfirmOrCancelMyAccountDialog
                .newInstance("您确定发送更换绑定银行卡的请求？\n" + "在此期间不可进行提现操作");
        dialog.showDialog(MyBankCardActivity.this.getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

                if (!TextUtils.isEmpty(isWithdrawIng) && isWithdrawIng.equals("withdrawing")) {
                    ToastUtil.show(MyBankCardActivity.this, "当前有未完成的提现,不可换绑");
                    return;
                }
                Skip.toActivity(MyBankCardActivity.this, NewAccountPhoneVerificationActivity.class);  //正常换卡
            }

            @Override
            public void onInputConfirm(String... values) {
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
