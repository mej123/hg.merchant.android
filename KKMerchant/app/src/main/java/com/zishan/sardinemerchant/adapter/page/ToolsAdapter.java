package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.ToolItem;
import com.zishan.sardinemerchant.R;

import java.lang.reflect.Field;
import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 首页管理
 * Created by wislie on 2017/12/29.
 */

public class ToolsAdapter extends CommonAdapter<ToolItem> {


    public ToolsAdapter(Context context, List<ToolItem> data) {
        super(context, data);
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    @Override
    public void convert(CommonViewHolder holder, ToolItem data) {
        ImageView icon = holder.getView(R.id.image_icon);
        icon.setImageResource(getResourceByReflect(data.getIcon()));

        TextView contentText = holder.getView(R.id.content);
        contentText.setText(data.getTitle());


    }

    public int getItemLayoutId() {
        return R.layout.item_mainpage_manage_tool;
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    private int getResourceByReflect(String imageName) {
        Class drawable = R.mipmap.class;
        Field field = null;
        int r_id;
        try {
            field = drawable.getField(imageName);
            r_id = field.getInt(field.getName());
        } catch (Exception e) {
            r_id = R.mipmap.placeholder_icon;
            Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id;
    }
}
