package com.zishan.sardinemerchant.adapter.personal;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.UserAccountEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 切换身份
 * Created by wislie on 2017/11/28.
 */

public class SwitchUserAdapter extends BaseQuickAdapter<UserAccountEntity> {
    public SwitchUserAdapter(int layoutResId, List<UserAccountEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, UserAccountEntity data) {

        ImageView logoIcon = helper.getView(R.id.store_logo);
        ImageView selectIcon = helper.getView(R.id.store_select);
        TextView myStoreText = helper.getView(R.id.store_mine);
        LinearLayout otherLayout = helper.getView(R.id.other_layout);


        TextView otherStoreText = helper.getView(R.id.store_other);
        TextView roleNameText = helper.getView(R.id.role_name);

        GlideLoader.getInstance().load(mContext, logoIcon,
                StringUtil.appendHttps(data.getLogoPicUrl()),
                R.mipmap.avatar_placeholder_icon, true);


        if(data.getOwner() != null && data.getOwner() == Boolean.TRUE){
            myStoreText.setVisibility(View.VISIBLE);
            otherLayout.setVisibility(View.GONE);
        }else{
            myStoreText.setVisibility(View.GONE);
            otherLayout.setVisibility(View.VISIBLE);
            otherStoreText.setText(data.getStoreName());
            roleNameText.setText(data.getRoleName());
        }

        if(data.getSelected() != null && data.getSelected() == Boolean.TRUE){
            selectIcon.setVisibility(View.VISIBLE);
        }else{
            selectIcon.setVisibility(View.GONE);
        }
    }
}
