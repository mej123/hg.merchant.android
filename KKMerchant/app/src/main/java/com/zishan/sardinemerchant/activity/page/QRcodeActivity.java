package com.zishan.sardinemerchant.activity.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.utils.Skip;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.runtimepermission.MPermissionUtils;
import top.ftas.ftasbase.common.runtimepermission.PermissionsManager;
import top.ftas.ftasbase.common.runtimepermission.PermissionsResultAction;

/**
 * 二维码扫描
 * Created by wislie on 2017/11/6.
 */

public class QRcodeActivity extends BActivity {

    @BindView(R.id.flashlight_icon)
    ImageView mFlashLightIcon;
    @BindView(R.id.flashlight_text)
    TextView mFlashLightText;

    private boolean isOpen = false;
    private CaptureFragment captureFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.scanCode));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        requestCameraPermission();
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_qrcode);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container,
                captureFragment).commit();
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    //开启相机权限
    private void requestCameraPermission() {

        MPermissionUtils.requestPermissionsResult(this, Constant.PERMISSION_CAMERA, new String[]{
                        Manifest.permission.CAMERA}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied() {

                    }
                });

    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent intent = new Intent(QRcodeActivity.this, ScanCodeResultActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent intent = new Intent(QRcodeActivity.this, ScanCodeResultActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @OnClick({R.id.flashlight_layout, R.id.cerification_code_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //扫码
            case R.id.flashlight_layout:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    mFlashLightIcon.setImageResource(R.mipmap.flashlight_open_icon);
                    mFlashLightText.setText(getResources().getString(R.string.flashlight_close));
                } else {
                    CodeUtils.isLightEnable(false);
                    mFlashLightIcon.setImageResource(R.mipmap.flashlight_close_icon);

                    mFlashLightText.setText(getResources().getString(R.string.flashlight_open));
                }

                isOpen = !isOpen;
                break;
            //输入核销码
            case R.id.cerification_code_layout:
                Skip.toActivity(this, CertificationInputActivity.class);
                break;
        }
    }
}
