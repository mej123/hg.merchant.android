package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.entity.FeedbackData;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 拒绝理由
 * Created by wislie on 2017/11/16.
 */

public class FeedbackAdapter extends CommonAdapter<FeedbackData> {

    public FeedbackAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public void convert(final CommonViewHolder holder, FeedbackData data) {

        TextView contentText = holder.getView(R.id.feedback_content);
        contentText.setText(data.getContent());

        ImageView checkedIcon = holder.getView(R.id.feedback_check_icon);
        if(!data.isChecked()){
            checkedIcon.setImageResource(R.mipmap.unchecked_icon);
        }else{
            checkedIcon.setImageResource(R.mipmap.checked_blue_icon);
        }

        checkedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListener != null){
                    mOnItemListener.onSelected(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_feedback;
    }


}
