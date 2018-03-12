package com.zishan.sardinemerchant.activity.personal.setting;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.ApkVersionCodeUtils;

/**
 * Created by yang on 2017/12/18.
 */

public class AboutUsActivity extends BActivity {
    @BindView(R.id.version_code)
    TextView mVersionCode;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.about_us));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initContentView() {
        int versionCode = ApkVersionCodeUtils.getVersionCode(this);//版本号
        String verName = ApkVersionCodeUtils.getVerName(this);//版本名称
        mVersionCode.setText(verName);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

}
