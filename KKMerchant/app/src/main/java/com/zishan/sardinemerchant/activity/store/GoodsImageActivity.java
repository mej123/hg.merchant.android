package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.ImagesLoadEntity;
import com.example.wislie.rxjava.model.store.ImageEnitity;
import com.example.wislie.rxjava.presenter.base.store.goods.ImageUploadPresenter;
import com.example.wislie.rxjava.view.base.store.goods.ImageUploadView;
import com.hg.ftas.func2.func.dialog.PhotoDialog;
import com.hg.ftas.upyun.UpyunListener;
import com.hg.ftas.upyun.UpyunManager;
import com.hg.ftas.util.LGImgCompressor;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.upyun.library.utils.UpYunUtils;
import com.zishan.sardinemerchant.utils.PhotoManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;

/**
 * Created by yang on 2017/9/30.
 * <p>
 * 商品图片
 */

public class GoodsImageActivity extends BActivity<ImageUploadView, ImageUploadPresenter> implements ImageUploadView,
        PhotoManager.PhotoListener { //LGImgCompressor.CompressListener
    @BindView(R.id.goods_default_tv)
    TextView mGoodsDefaultText;
    @BindView(R.id.goods_icon)
    ImageView mGoodsIcon;
    private String mGoodsPath;
    private String resourceId;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_image;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.goods_image));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                mGoodsIcon.setVisibility(View.VISIBLE);
                //加载图片
                GlideLoader.getInstance().load(this, mGoodsIcon, StringUtil.appendHttps(path));
            }
        }

        UpyunManager.getInstance().setUpyunListener(new UpyunListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void callBack(String localId, boolean isSuccess) {
                if(isSuccess){
                    dismissProgressDialog();
                    ToastUtil.show("上传成功");
                }
            }
        });
        PhotoManager.getInstance().initImagePicker();
        PhotoManager.getInstance().setPhotoListener(this);
    }

    @Override
    protected ImageUploadPresenter createPresenter() {
        return new ImageUploadPresenter(this, this);
    }

    @OnClick({R.id.goods_image_area, R.id.goods_icon, R.id.goods_image_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_image_area:
            case R.id.goods_icon:
                PhotoDialog dialog = PhotoDialog.newInstance();
                dialog.showDialog(getFragmentManager());
                dialog.setPhotoListener(new PhotoDialog.OnPhotoListener() {
                    @Override
                    public void onSelectFromAlbum() {

                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent1 = new Intent(GoodsImageActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                        startActivityForResult(intent1, Constant.REQUEST_CODE_SELECT);
                    }

                    @Override
                    public void onTakePhoto() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent = new Intent(GoodsImageActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, Constant.REQUEST_CODE_SELECT);
                    }
                });

                break;

            case R.id.goods_image_submit:
                Intent intent = new Intent();
                intent.putExtra("path", mGoodsPath);
                intent.putExtra("pic_id", resourceId);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().compressImage(this, requestCode, resultCode, data);
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void uploadImageSuccess(ImagesLoadEntity data, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            mGoodsIcon.setVisibility(View.VISIBLE);
            mGoodsPath = filePath;
            resourceId = data.getResourceUid();
        }
        //上传图片到又拍云
        UpyunManager.getInstance().uploadYun(new File(filePath), data.getProviderUploadParamString());
//        Log.e("wislie","filePath:"+filePath+ " mGoodsPath："+mGoodsPath);
        //加载图片
        GlideLoader.getInstance().load(this, mGoodsIcon, mGoodsPath);
    }

    @Override
    public void uploadImageFailed() {
    }

    @Override
    public void compressFinish(LGImgCompressor.CompressResult compressResult, int uploadSize) {
        File imageFile = new File(compressResult.getOutPath());
        if (imageFile != null && imageFile.exists()) {
            //int imageId, String name, String path, boolean isSelected, long len, String fileMd5
            ImageEnitity compressedEntity = new ImageEnitity(0, imageFile.getName(), imageFile.getPath(), true,
                    imageFile.length(), UpYunUtils.md5(imageFile, Constant.BLOCK_SIZE)); //MD5Util.getMd5ByFile(imageFile)
            //上传图片
            mPresenter.uploadImage(1, compressedEntity.getName(),
                    compressedEntity.getFileMd5(), compressedEntity.getLen(), compressedEntity.getPath());
        }
    }
}
