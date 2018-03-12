package com.zishan.sardinemerchant.activity.personal.accounts;

import android.support.v4.content.ContextCompat;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.Skip;
import com.zishan.sardinemerchant.view.XTextView;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;

/**
 * Created by yang on 2017/11/2.
 * <p>
 * 已提交审核
 */

public class NewCommitAuditActivity extends BActivity {

    @BindView(R.id.xt_hint_message)
    XTextView xtHintMessage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_commit_audit;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initContentView() {
        xtHintMessage.setText("您的银行卡已绑定成功,您可以在我的余额中进行提现");
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick(R.id.tv_finish)
    public void onViewClicked() {
        ActivityManager.getInstance().finishAllActivityExcept(this);
        Skip.toActivity(this, NewMyAccountFirstActivity.class);
        this.finish();
    }
}
