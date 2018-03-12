package com.zishan.sardinemerchant.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hg.ftas.util.LGImgCompressor;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.zishan.sardinemerchant.Constant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wislie on 2018/3/12.
 */

public class PhotoManager implements LGImgCompressor.CompressListener{

    private PhotoListener mPhotoListener;

    private static PhotoManager mInstance;
    public static PhotoManager getInstance(){
        if(mInstance == null){
            synchronized (PhotoManager.class){
                if(mInstance == null){
                    mInstance = new PhotoManager();
                }
            }
        }
        return mInstance;
    }

    public void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
//        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    public  void compressImage(Context context, int requestCode, int resultCode, Intent data){
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            Log.e("wislie","data 为空:"+(data == null) +" requestcode:"+requestCode+" REQUEST_CODE_SELECT:"+Constant.REQUEST_CODE_SELECT);
            //添加图片返回
            if (data != null && requestCode == Constant.REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>)
                        data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                compressImage(context, images);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == Constant.REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>)
                        data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                compressImage(context, images);
            }
        }
    }

    //压缩图片
    private  void compressImage(Context context, ArrayList<ImageItem> images) {
        if (images != null && images.size() > 0) {

            for (ImageItem item : images) {
                File cameraFile = new File(item.path);
                LGImgCompressor.getInstance(context).withListener(this).
                        starCompress(Uri.fromFile(cameraFile).toString(), 1024, images.size());
            }
        }
    }

    @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult, int uploadSize) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR) { //压缩失败
            return;
        }

        if(mPhotoListener != null){
            mPhotoListener.compressFinish(compressResult, uploadSize);
        }
    }

    public void setPhotoListener(PhotoListener photoListener) {
        this.mPhotoListener = photoListener;
    }

    public interface PhotoListener{
        //压缩完成
        void compressFinish(LGImgCompressor.CompressResult compressResult, int uploadSize);

//        int setCallBack(int callbackId);
    }
}
