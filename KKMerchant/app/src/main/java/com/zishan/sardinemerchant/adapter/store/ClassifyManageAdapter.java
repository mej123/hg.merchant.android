package com.zishan.sardinemerchant.adapter.store;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 分类管理的适配器
 * Created by wislie on 2017/9/20.
 */
public class ClassifyManageAdapter extends BaseQuickAdapter<ProductGroupEntity> {
    public ClassifyManageAdapter(int layoutResId, List<ProductGroupEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, ProductGroupEntity data) {
        RelativeLayout layout = holder.getView(R.id.classify_manage_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });

        Button delete = holder.getView(R.id.classify_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onDelete(holder.getLayoutPosition());
                }
            }
        });

        ImageView tagIcon = holder.getView(R.id.classify_tag_icon);
        if (holder.getAdapterPosition() % 2 == 0) {
            tagIcon.setImageResource(R.mipmap.rect_blue);
        } else {
            tagIcon.setImageResource(R.mipmap.rect_yellow);
        }

        TextView classifyNameText = holder.getView(R.id.classify_name);
        classifyNameText.setText(data.getName());
        TextView classifyCountText = holder.getView(R.id.classify_goods_count);
        int productNum = data.getProductNum() == null ? 0 : data.getProductNum();
        classifyCountText.setText(" 共" + productNum + "件商品");
    }
}

