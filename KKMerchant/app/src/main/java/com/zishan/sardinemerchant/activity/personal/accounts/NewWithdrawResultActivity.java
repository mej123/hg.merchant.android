package com.zishan.sardinemerchant.activity.personal.accounts;

import android.support.v4.content.ContextCompat;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.Skip;

import butterknife.OnClick;

/**
 * Created by yang on 2017/11/24.
 * <p>
 * 提现结果
 */

public class NewWithdrawResultActivity extends BActivity {
    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw_result;
    }

    @Override
    protected void initContentView() {

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.commit_audit);
    }

    @OnClick(R.id.tv_finish)//提现成功
    public void onViewClicked() {
        Skip.toActivity(this, NewMyAccountFirstActivity.class);
        NewWithdrawResultActivity.this.finish();
    }
}
