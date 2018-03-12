package com.kuku.kkmerchant;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yang on 2017/9/4.
 */

public abstract class BaseActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getLayoutResId());
        initActionBar();
        initContentView();

    }

    protected abstract int getLayoutResId();

    protected void initContentView() {
    }

    /**
     * @desc 子类分别单独实现
     */
    protected void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(LayoutInflater.from(this).inflate(getCustomActionBarResId(), null), layoutParams);
    }

    protected int getCustomActionBarResId() {
        return R.layout.custom_action_bar;
    }

    /**
     * @desc 设置actionbar title
     */
    protected void setActionbarTitle(String title) {
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.custom_action_title)).setText(title);
        getSupportActionBar().getCustomView().findViewById(R.id.custom_action_title).setVisibility(View.VISIBLE);
    }

    /**
     * @desc 设置actionbar Home图标和点击事件
     */
    protected void setActionBarHomeIcon(int resId) {
        ImageView home = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.custom_action_home);
        if (resId < 0) {
            home.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            home.setVisibility(View.INVISIBLE);
        } else {
            home.setImageResource(resId);
            home.setVisibility(View.VISIBLE);
            home.setOnClickListener(getActionBarClickListener());
        }
    }


    private View.OnClickListener mActionBarClickListener;

    public View.OnClickListener getActionBarClickListener() {
        if (mActionBarClickListener == null) {
            mActionBarClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionBarClick(v);
                }
            };
        }
        return mActionBarClickListener;
    }

    protected void onActionBarClick(View v) {
        switch (v.getId()) {
            case R.id.custom_action_home:
                finish();
                break;
        }
    }

    /**
     * @desc 设置actionbar Menu图标和点击事件
     */
    protected void setActionBarMenuIcon(int resId) {
        ImageView menu = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.custom_img_menu);
        if (resId < 0) {
            menu.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            menu.setVisibility(View.INVISIBLE);
        } else {
            menu.setImageResource(resId);
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(getActionBarClickListener());
        }
    }

}
