package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yang on 2017/11/2.
 * <p>
 * 个人  账户管理
 */

public class AccountManageActivity extends BActivity {
    @BindView(R.id.public_account_icon)
    ImageView mPublicAccountIcon;
    @BindView(R.id.private_account_icon)
    ImageView mPrivateAccountIcon;

    private boolean publicAccount = true;//默认公有账户选中
    private boolean PrivateAccount = false;//默认私有账户未选中
    private String selectType = "public";//默认选中公款账户    银行卡账户类型(0:个人,1:公司)
    // private int result_ok = 001;

    @Override
    protected int getLayoutResId() {

        return R.layout.activity_account_manage;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.account_manage));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initContentView() {
        String accountType = getIntent().getStringExtra("accountType");
        if (!TextUtils.isEmpty(accountType)) {
            if (accountType.equals("public")) {
                mPublicAccountIcon.setImageResource(R.mipmap.checked_blue_icon);
                mPrivateAccountIcon.setImageResource(R.mipmap.unchecked);
                PrivateAccount = false;
                publicAccount = true;
                selectType = "public";
            }

            if (accountType.equals("private")) {
                mPrivateAccountIcon.setImageResource(R.mipmap.checked_blue_icon);
                mPublicAccountIcon.setImageResource(R.mipmap.unchecked);
                publicAccount = false;
                PrivateAccount = true;
                selectType = "private";
            }
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.rl_public_account, R.id.rl_private_account, R.id.tv_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_public_account:

                if (publicAccount) {
                    // mPublicAccountIcon.setImageResource(R.mipmap.unchecked);
                    publicAccount = false;
                } else {
                    mPublicAccountIcon.setImageResource(R.mipmap.checked_blue_icon);
                    mPrivateAccountIcon.setImageResource(R.mipmap.unchecked);
                    PrivateAccount = false;
                    publicAccount = true;
                    selectType = "public";
                }

                break;
            case R.id.rl_private_account:

                if (PrivateAccount) {
                    mPrivateAccountIcon.setImageResource(R.mipmap.checked_blue_icon);
                    PrivateAccount = false;
                } else {
                    mPrivateAccountIcon.setImageResource(R.mipmap.checked_blue_icon);
                    mPublicAccountIcon.setImageResource(R.mipmap.unchecked);
                    publicAccount = false;
                    PrivateAccount = true;
                    selectType = "private";
                }
                break;
            case R.id.tv_quit:
                //BaseEventManager.post(selectType);
                Intent intent1 = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("selectType", selectType);
                intent1.putExtras(bundle1);
                setResult(RESULT_OK, intent1);
                this.finish();
                break;
        }
    }
}
