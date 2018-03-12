package com.zishan.sardinemerchant.adapter.page;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.StoreSeatEntity;

import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;

import java.util.List;


/**
 * 拼桌
 * Created by yang on 2017/12/5.
 */

public class TableMergeAdapter extends RecyclerView.Adapter<TableMergeAdapter.ViewHolder> {


    private List<StoreSeatEntity> mDataList;
    private LayoutInflater mInflater;

    public TableMergeAdapter(Context context, List<StoreSeatEntity> dataList) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_table_merge, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_table_seat);
        viewHolder.checkedIcon = (ImageView) view.findViewById(R.id.table_check_icon);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        StoreSeatEntity data = mDataList.get(position);
        int type = data.getType();
        if (position == getPositionForType(type)) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            if (data.getType() == Constant.HALL) {
                holder.tvTitle.setText("大厅");
            } else if (data.getType() == Constant.CARD) {
                holder.tvTitle.setText("卡座");
            } else if (data.getType() == Constant.BOX) {
                holder.tvTitle.setText("包厢");
            }
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        if (data.getIsChecked() == 0) {
            holder.checkedIcon.setImageResource(R.mipmap.unchecked_icon);
        } else {
            holder.checkedIcon.setImageResource(R.mipmap.checked_blue_icon);
        }

        holder.checkedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckListener != null) {
                    mCheckListener.check(position);
                }

            }
        });
        holder.tvName.setText(data.getSeatName());
    }

    @Override
    public int getItemCount() {
        return (mDataList == null) ? 0 : mDataList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvName;
        ImageView checkedIcon;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    //根据类型获得第一次出现的位置
    private int getPositionForType(int type) {
        for (int i = 0; i < getItemCount(); i++) {
            int curType = mDataList.get(i).getType();
            if (curType == type) return i;
        }
        return -1;
    }

    private OnCheckListener mCheckListener;

    public interface OnCheckListener {
        void check(int position);
    }

    public void setCheckListener(OnCheckListener checkListener) {
        this.mCheckListener = checkListener;
    }
}
