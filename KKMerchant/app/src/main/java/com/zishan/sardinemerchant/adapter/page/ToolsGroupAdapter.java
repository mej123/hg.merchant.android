package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.GroupItem;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 首页管理
 * Created by wislie on 2017/12/29.
 */

public class ToolsGroupAdapter extends CommonAdapter<GroupItem> {

    private OnMultiItemListener mOnMultiItemListener;

    public ToolsGroupAdapter(Context context, List<GroupItem> data) {
        super(context, data);
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext, getItemLayoutId());
    }

    @Override
    public void convert(final CommonViewHolder holder, GroupItem data) {
        TextView titleText = holder.getView(R.id.title);
        MaxRecyclerView toolRecycler = holder.getView(R.id.tool_recycler);
        toolRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        ToolsAdapter adapter = new ToolsAdapter(mContext, data.getItems());
        toolRecycler.setAdapter(adapter);
        titleText.setText(data.getGroup());
        adapter.setOnItemListener(new OnAdapterItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                if(mOnMultiItemListener != null){
                    mOnMultiItemListener.onItemClick(holder.getAdapterPosition(), position);
                }

            }
        });
    }

    public int getItemLayoutId() {
        return R.layout.item_mainpage_manage;
    }

    public void setOnMultiItemListener(OnMultiItemListener onMultiItemListener) {
        this.mOnMultiItemListener = onMultiItemListener;
    }

    public interface OnMultiItemListener{
        void onItemClick(int parentPos, int childPos);
    }
}
