package com.zishan.sardinemerchant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.view.base.BView;
import com.hg.ftas.activity.ftas.ftasview.SimpleFtasMobileActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.StatusBarUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftasbase.eventbus.BaseEventObserver;

/**
 * 全屏, 无标题栏和状态栏 的activity,
 * Created by wislie on 2017/11/2.
 */

public abstract class BaseFragmentActivity<V extends BView, T extends BPresenter<V>>
        extends AppCompatActivity implements BaseEventObserver {
    protected T mPresenter;

    protected Unbinder mUnbinder;
    protected String ACTION_NAME = this.getClass().getName();
    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        // 把actvity放到application栈中管理
        ActivityManager.getInstance().onCreate(this);
        StatusBarUtil.setStatusBarTranslucent(this, true);
        setContentView(getLayoutResId());
        //绑定控件
        mUnbinder = ButterKnife.bind(this);

        //创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        initContentView();
        //注册EventBus
        BaseEventManager.register(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除presenter与view之间的关系
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        //解绑
        mUnbinder.unbind();
        //销毁EventBus
        BaseEventManager.unregister(this);
        ActivityManager.getInstance().onDestroy(this);
    }

    @Subscribe
    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventPost(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventBackground(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventAsync(BaseEvent baseEvent) {

    }

    protected abstract int getLayoutResId();

    protected abstract void initContentView();

    protected abstract T createPresenter();
}
