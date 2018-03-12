package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.personal.store_msg.StoreModelActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 就餐状态
 * Created by wislie on 2018/1/2.
 */

public class RepastModelActivity extends BActivity {

    @BindView(R.id.repast_model)
    TextView mRepastModelText;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_repast_model;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.repast_manage));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
    }

    @Override
    protected void initContentView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateModel();
    }

    private void updateModel() {
        boolean isEatFirst = UserConfig.getInstance(ClientApplication.getApp()).isEatFirst();
        if (isEatFirst) {
            mRepastModelText.setText("先吃后付");
        } else {
            mRepastModelText.setText("先付后吃");
        }
    }

    @OnClick({R.id.repast_model, R.id.repast_model_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repast_model:
            case R.id.repast_model_title:
                Intent storeModelIntent = new Intent(
                        this, StoreModelActivity.class);
                startActivity(storeModelIntent);
                break;
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
