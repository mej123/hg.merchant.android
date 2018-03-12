package com.zishan.sardinemerchant.activity.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseFragmentActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.utils.Skip;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.OnClick;
/*import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;*/
import top.ftas.ftasbase.common.util.ImageUtil;
import top.ftas.ftasbase.common.util.StatusBarUtil;

/**
 * 核销失败
 * Created by wislie on 2017/11/20.
 */

public class CerificationFailedActivity extends BaseFragmentActivity  { //implements EasyPermissions.PermissionCallbacks

    private static final int REQUEST_CODE = 111;
    private static final int REQUEST_IMAGE = 112;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cerification_failed;
    }

    @Override
    protected void initContentView() {
        //设置状态栏
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);

    }

    @OnClick({R.id.return_mainpage, R.id.continue_cerfication})
    public void onClick(View view) {
        switch (view.getId()) {
            //首页
            case R.id.return_mainpage:
                Skip.toActivity(this, MainPageActivity.class);
                finish();
                break;
            //继续核销
            case R.id.continue_cerfication:
//                startQRcode();
                Intent intent = new Intent(this, QRcodeActivity.class);
                startActivity(intent);
                break;
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        *//**
         * 处理二维码扫描结果
         *//*
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Skip.toActivity(CerificationFailedActivity.this, ScanCodeResultActivity.class);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Skip.toActivity(CerificationFailedActivity.this, ScanCodeResultActivity.class);
                    Toast.makeText(CerificationFailedActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        *//**
         * 选择系统图片并解析
         *//*
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            //解析结果
                            Toast.makeText(CerificationFailedActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Skip.toActivity(CerificationFailedActivity.this, ScanCodeResultActivity.class);
                            Toast.makeText(CerificationFailedActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/


    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;


    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void startQRcode() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
//            onClick(viewId);
            Intent intent = new Intent(CerificationFailedActivity.this, QRcodeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null *//* click listener *//*)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }*/
}
