package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.ImagesLoadEntity;
import com.example.wislie.rxjava.model.personal.StoreAlbumAddEntity;
import com.example.wislie.rxjava.model.personal.StoreAlbumEntity;
import com.example.wislie.rxjava.model.store.ImageEnitity;
import com.example.wislie.rxjava.presenter.base.personal.StoreAlbumAddPresenter;
import com.example.wislie.rxjava.view.base.personal.StoreAlbumAddView;
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
 * 添加照片 /修改照片
 * Created by wislie on 2017/11/28.
 */

public class StoreAlbumAddActivity extends BActivity<StoreAlbumAddView, StoreAlbumAddPresenter>
        implements StoreAlbumAddView, PhotoManager.PhotoListener { //, LGImgCompressor.CompressListener

    @BindView(R.id.image_name_edit)
    EditText mImageNameEdit;
    @BindView(R.id.goods_icon)
    ImageView mGoodsIcon;
    @BindView(R.id.return_name)
    TextView mReturnName;
    @BindView(R.id.image_title)
    TextView mImageTitle;
    @BindView(R.id.tv_image_icon_hint)
    TextView mImageIconHint;
    @BindView(R.id.upload_again)
    TextView mUploadAgain;
    private StoreAlbumEntity mAlbumPhotoData;
    private String mGoodsPath;
    private String resourceId;
    private long store_id = 0;
    private long id = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_album_add;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getResources().getString(R.string.add_album));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });
        TextView confirm = setActionBarMenuText(getResources().getString(R.string.confirm));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageName = mImageNameEdit.getText().toString();
                if (TextUtils.isEmpty(imageName)) {
                    ToastUtil.show("图片名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(resourceId)) {
                    ToastUtil.show("图片上传不能为空");
                    return;
                }
                //修改图片  	图片id新增图片不传，修改图片传递
                if(id != 0){
                    mPresenter.addStoreAlbum(id, store_id, imageName, resourceId);
                }else{
                    mPresenter.addStoreAlbum(null, store_id, imageName, resourceId);
                }
            }
        });
    }

    @Override
    protected StoreAlbumAddPresenter createPresenter() {
        return new StoreAlbumAddPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            mAlbumPhotoData = (StoreAlbumEntity) intent.getSerializableExtra("album_photo_data");
            store_id = intent.getLongExtra("store_id", store_id);
        }
        if (mAlbumPhotoData != null) {
            String picUrl = mAlbumPhotoData.getPicUrl();
            if (!TextUtils.isEmpty(picUrl)) {
                GlideLoader.getInstance().load(this, mGoodsIcon, StringUtil.appendHttps(picUrl));
                mUploadAgain.getBackground().setAlpha(80);
                mUploadAgain.setVisibility(View.VISIBLE);
            }
            mImageNameEdit.setText(mAlbumPhotoData.getName());
            id = mAlbumPhotoData.getId();
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
                        Intent intent1 = new Intent(StoreAlbumAddActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                        startActivityForResult(intent1, Constant.REQUEST_CODE_SELECT);
                    }

                    @Override
                    public void onTakePhoto() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent = new Intent(StoreAlbumAddActivity.this, ImageGridActivity.class);
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
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
    public void StoreAlbumAddSuccess(StoreAlbumAddEntity data) {
        finish();
    }

    @Override
    public void StoreAlbumAddFailed() {

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
            ImageEnitity compressedEntity = new ImageEnitity(0, imageFile.getName(), imageFile.getPath(), true,
                    imageFile.length(), UpYunUtils.md5(imageFile, Constant.BLOCK_SIZE));
            //上传图片
            mPresenter.uploadImage(1, compressedEntity.getName(),
                    compressedEntity.getFileMd5(), compressedEntity.getLen(), compressedEntity.getPath());
        }
    }
}
