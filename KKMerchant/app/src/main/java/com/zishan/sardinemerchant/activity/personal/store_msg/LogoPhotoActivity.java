package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.ImagesLoadEntity;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.model.store.ImageEnitity;
import com.example.wislie.rxjava.presenter.base.personal.LogoPhotoPresenter;
import com.example.wislie.rxjava.view.base.personal.LogoPhotoView;
import com.hg.ftas.func2.func.dialog.PhotoDialog;
import com.hg.ftas.upyun.UpyunListener;
import com.hg.ftas.upyun.UpyunManager;
import com.hg.ftas.util.LGImgCompressor;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.upyun.library.utils.UpYunUtils;
import com.zishan.sardinemerchant.utils.PhotoManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2018/1/16.
 * <p>
 * 门脸照片
 */

public class LogoPhotoActivity extends BActivity<LogoPhotoView, LogoPhotoPresenter> implements
        LogoPhotoView, PhotoManager.PhotoListener {//LGImgCompressor.CompressListener
    @BindView(R.id.logo_icon)
    ImageView mLogoIcon;
    @BindView(R.id.image_title)
    TextView mImageTitle;
    @BindView(R.id.upload_again)
    TextView mUploadAgain;
    private String mGoodsPath;
    private String resourceId;

    private long store_id = 0;
    private String logoPicUrl;
    @Override
    public void initActionBar() {
        super.initActionBar();
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_black_icon);
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.logo_photo));
        setActionBarMenuIcon(-1);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });
        TextView confirm = setActionBarMenuText(getResources().getString(R.string.confirm));
        confirm.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editStoreMsg(store_id, null,
                        null, null, null, null, null, null, null, null, null, resourceId, null);
            }
        });
    }

    @Override
    protected LogoPhotoPresenter createPresenter() {
        return new LogoPhotoPresenter(this, this);
    }

    @Override
    public void onBackPressed() {
        exitEdit();
    }

    private void exitEdit() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认放弃此次编辑?", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                finish();
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logo_photo;
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent!=null){
            store_id = intent.getLongExtra("store_id", store_id);
            logoPicUrl = intent.getStringExtra("logoPicUrl");
        }


        if (!TextUtils.isEmpty(logoPicUrl)) {
            GlideLoader.getInstance().load(this, mLogoIcon, StringUtil.appendHttps(logoPicUrl));//图片
            mUploadAgain.getBackground().setAlpha(80);
            mUploadAgain.setVisibility(View.VISIBLE);
        }

        UpyunManager.getInstance().setUpyunListener(new UpyunListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void callBack(String localId, boolean isSuccess) {
                if (isSuccess) {
                    dismissProgressDialog();
                    ToastUtil.show("上传成功");
                }
            }
        });
        PhotoManager.getInstance().initImagePicker();
        PhotoManager.getInstance().setPhotoListener(this);
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }

    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    @Override
    public void uploadImageSuccess(ImagesLoadEntity data, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            mLogoIcon.setVisibility(View.VISIBLE);
            mGoodsPath = filePath;
            resourceId = data.getResourceUid();
        }
        //上传图片到又拍云
        UpyunManager.getInstance().uploadYun(new File(filePath), data.getProviderUploadParamString());
        //加载图片
        GlideLoader.getInstance().load(this, mLogoIcon, mGoodsPath);

    }

    @Override
    public void uploadImageFailed() {

    }

    @Override
    public void editStoreMsgSuccess(StoreMsgEntity data) {
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }

    @OnClick({R.id.image_area})
    public void onClick(View view) {
        switch (view.getId()) {
            //1.弹出选中相册对话框
            case R.id.image_area:
                PhotoDialog dialog = PhotoDialog.newInstance();
                dialog.showDialog(getFragmentManager());
                dialog.setPhotoListener(new PhotoDialog.OnPhotoListener() {
                    @Override
                    public void onSelectFromAlbum() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent1 = new Intent(LogoPhotoActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                        startActivityForResult(intent1, Constant.REQUEST_CODE_SELECT);
                    }

                    @Override
                    public void onTakePhoto() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent = new Intent(LogoPhotoActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, Constant.REQUEST_CODE_SELECT);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().compressImage(this, requestCode, resultCode, data);
    }

    @Override
    public void compressFinish(LGImgCompressor.CompressResult compressResult, int uploadSize) {
        File imageFile = new File(compressResult.getOutPath());
        if (imageFile != null && imageFile.exists()) {
            ImageEnitity compressedEntity = new ImageEnitity(0, imageFile.getName(), imageFile.getPath(), true,
                    imageFile.length(), UpYunUtils.md5(imageFile, Constant.BLOCK_SIZE));
            //上传图片
            mPresenter.uploadImage(1, compressedEntity.getName(),
                    compressedEntity.getFileMd5(), compressedEntity.getLen(), compressedEntity.getPath());
        }
    }
}
