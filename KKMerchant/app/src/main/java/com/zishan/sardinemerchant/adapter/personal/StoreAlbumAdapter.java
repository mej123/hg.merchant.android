package com.zishan.sardinemerchant.adapter.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreAlbumEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 店铺照片
 * Created by wislie on 2017/11/28.
 */

public class StoreAlbumAdapter extends BaseQuickAdapter<StoreAlbumEntity> {


    public StoreAlbumAdapter(int layoutResId, List<StoreAlbumEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, StoreAlbumEntity storeAlbumEntity) {
        if (storeAlbumEntity == null) return;
        TextView albumNameText = helper.getView(R.id.store_album_name);//名字
        ImageView albumIcon = helper.getView(R.id.store_album_icon);//上传图片
        albumNameText.setText(storeAlbumEntity.getName());//图片名称
        String picUrl = storeAlbumEntity.getPicUrl();
        /*if (storeAlbumEntity.isLogo()) {
            if (picUrl.contains("http")) {
                GlideLoader.getInstance().load(mContext, albumIcon, picUrl);//图片
            } else {
                GlideLoader.getInstance().load(mContext, albumIcon, "https:" + picUrl);//图片
            }
        } else {
            GlideLoader.getInstance().load(mContext, albumIcon, "https:" + picUrl);//图片
        }*/

        if (!TextUtils.isEmpty(picUrl)) {
            //加载图片
            GlideLoader.getInstance().load(mContext, albumIcon, StringUtil.appendHttps(picUrl));
        }


        ImageView checkIcon = helper.getView(R.id.store_album_checked_icon);//选中图片
        boolean editState = storeAlbumEntity.getEditState();
        //处于编辑状态中
        if (editState) {
            checkIcon.setVisibility(View.VISIBLE);
            boolean select = storeAlbumEntity.isSelect();
            if (select) {
                checkIcon.setImageResource(R.mipmap.checked_blue_icon);
            } else {
                checkIcon.setImageResource(R.mipmap.unchecked);
            }
        } else {
            //非可编辑状态
            checkIcon.setVisibility(View.GONE);//隐藏选中框
        }
    }
}


