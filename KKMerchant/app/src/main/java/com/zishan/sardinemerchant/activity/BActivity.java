package com.zishan.sardinemerchant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.PermissionEntity;
import com.example.wislie.rxjava.model.page.PermissionGroupsEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.view.base.BView;
import com.hg.ftas.activity.ftas.ftasview.SimpleFtasMobileActivity;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.logins.NewPswLoginActivity;
import com.zishan.sardinemerchant.dialog.GrantDialog;
import com.zishan.sardinemerchant.dialog.LoadingDialog;
import com.zishan.sardinemerchant.dialog.PermissionDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.ftas.ftasbase.common.runtimepermission.MPermissionUtils;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.StatusBarUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftasbase.eventbus.BaseEventObserver;

/**
 * 新的base activity
 * Created by wislie on 2017/11/2.
 */

public abstract class BActivity<V extends BView, T extends BPresenter<V>>
        extends SimpleFtasMobileActivity implements BaseEventObserver {

    protected T mPresenter;
    protected Unbinder mUnbinder;
    protected String ACTION_NAME = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 把actvity放到application栈中管理
        ActivityManager.getInstance().onCreate(this);
        //设置状态栏
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, getStatusBarColor());
        StatusBarUtil.setStatusBarTranslucent(this, true);

        View baseView = LayoutInflater.from(this).inflate(R.layout.activity_base, null, false);
        RelativeLayout container = (RelativeLayout) baseView.findViewById(R.id.base_container);
        //加载并设置布局文件
        View contentView = LayoutInflater.from(this).inflate(getLayoutResId(), null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lp);
        //添加布局文件
        container.addView(contentView);

        setContentView(baseView);

        //创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        //绑定控件
        mUnbinder = ButterKnife.bind(this);
        initActionBar();
        initContentView();

        //注册EventBus
        BaseEventManager.register(this);

    }

    /**
     * @desc 子类分别单独实现
     */
    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null), layoutParams);
    }

    public void setToolBarColor(int color) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        toolbar.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    /**
     * @desc 设置actionbar title
     */
    public TextView setActionbarTitle(String title) {
        TextView titleText = ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.custom_action_title));
        titleText.setText(title);
        titleText.setVisibility(View.VISIBLE);
        return titleText;
    }


    /**
     * @desc 设置actionbar Home图标和点击事件
     */
    protected ImageView setActionBarHomeIcon(int resId) {
        ImageView home = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.custom_action_home);
        if (resId < 0) {
            home.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            home.setVisibility(View.INVISIBLE);
        } else {
            home.setImageResource(resId);
            home.setVisibility(View.VISIBLE);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return home;
    }


    /**
     * @desc 设置actionbar home文字内容和点击事件
     */
    protected TextView setActionBarHomeText(String title) {
        TextView textMenu = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.custom_text_home);
        textMenu.setText(title);
        textMenu.setVisibility(View.VISIBLE);
        return textMenu;
    }


    /**
     * @desc 设置actionbar Menu图标和点击事件
     */
    public ImageView setActionBarMenuIcon(int resId) {
        ImageView menu = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.custom_img_menu);
        if (resId < 0) {
            menu.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            menu.setVisibility(View.INVISIBLE);
        } else {
            menu.setImageResource(resId);
            menu.setVisibility(View.VISIBLE);
        }
        return menu;
    }

    /**
     * @desc 设置actionbar Menu文字内容和点击事件
     */
    protected TextView setActionBarMenuText(String title) {
        TextView textMenu = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.custom_text_menu);
        textMenu.setText(title);
        textMenu.setVisibility(View.VISIBLE);
        return textMenu;

    }

    //设置toolbar最底下的线是否可见
    protected View setActionBarDivderVisible(boolean visible) {
        View divderView = getSupportActionBar().getCustomView().findViewById(R.id.custom_divder_view);
        divderView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return divderView;
    }

    //没有更多了
    protected View showFooterNoMoreData() {
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_footer_no_more, null);
        footerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return footerView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected LoadingDialog mDialog;


    /**
     * 显示正在加载。。。
     */
    public void showLoadingDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
            return;
        }

        mDialog = LoadingDialog.newInstance();
        mDialog.showDialog(getFragmentManager());
    }


    /**
     * 取消加载
     */
    public void dismissLoadingDialog() {

        if (mDialog != null && this != null && !isFinishing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null)
                        mDialog.dismiss(getFragmentManager());
                    mDialog = null;
                }
            }, 300);

        }
    }

    //重新登录
    protected void reOnLogin() {
        UserConfig.getInstance(ClientApplication.getApp()).clearAll();
        Intent intent = new Intent(this, NewPswLoginActivity.class);
        startActivity(intent);
        ActivityManager.getInstance().finishAllActivity();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 权限dialog提示
     *
     * @param title
     */
    protected void showPermisssionDialog(String title) {
        PermissionDialog dialog = PermissionDialog.newInstance(title);
        dialog.showDialog(getFragmentManager());
    }

    /**
     * 未开通dialog提示
     * @param title
     */
    protected void showGrantDialog(String title, String content){
        GrantDialog dialog = GrantDialog.newInstance(title, content);
        dialog.showDialog(getActivity().getFragmentManager());
    }

    //true表示可以查看
    protected boolean permissionAllowed(int index) {
        ArrayList<Boolean> dataList = UserConfig.getInstance(ClientApplication.getApp()).getPermission();
        if (dataList != null && dataList.size() == 15) {
            Boolean data = dataList.get(index);
            if (data == Boolean.TRUE) return true;
            return false;
        }
        return true;
    }


    //设置权限列表
    protected void setPermissionList(List<PermissionGroupsEntity> permissionList) {
        if (permissionList == null || permissionList.size() == 0) return;
        ArrayList<Boolean> boolList = new ArrayList();
        for (int j = 0; j < permissionList.size(); j++) {
            PermissionGroupsEntity data = permissionList.get(j);
            List<PermissionEntity> pList = data.getPermissions();
            for (int k = 0; k < pList.size(); k++) {
                PermissionEntity pData = pList.get(k);
                ////0为启用，1为禁用，2为未设置
                int isSelected = pData.getIsSelected();
                if (isSelected == 0) {
                    boolList.add(true);
                } else {
                    boolList.add(false);
                }
            }
        }
        UserConfig.getInstance(ClientApplication.getApp()).setPermission(boolList);
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


    protected abstract T createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initContentView();

    protected abstract int getStatusBarColor();

}
