package com.zishan.sardinemerchant.adapter.personal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.MyStaffEntity;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.utils.SlidingMenu;

import java.util.List;

/**
 * Created by yang on 2017/10/29.
 * <p>
 * 个人  我的员工  适配器
 */

public class MyStaffAdapter extends RecyclerView.Adapter<MyStaffAdapter.MyViewHolder> {

    private List<MyStaffEntity> mData;
    private Context mContext;

    private SlidingMenu mOpenMenu;
    private SlidingMenu mScrollingMenu;

    public SlidingMenu getScrollingMenu() {
        return mScrollingMenu;
    }

    public void setScrollingMenu(SlidingMenu scrollingMenu) {
        mScrollingMenu = scrollingMenu;
    }

    public void holdOpenMenu(SlidingMenu slidingMenu) {
        mOpenMenu = slidingMenu;
    }

    public void closeOpenMenu() {
        if (mOpenMenu != null && mOpenMenu.isOpen()) {
            mOpenMenu.closeMenu();
            mOpenMenu = null;
        }
    }

    public MyStaffAdapter(List<MyStaffEntity> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_staff, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String name = mData.get(position).getRole().getName();
        if (name==null)return;
        holder.staffName.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        holder.menuText.setText("删除");
        holder.staffName.setText(mData.get(position).getRealName());
        int currentposition = position - 1;
        String currentStr = mData.get(position).getRole().getName();
        String previewStr = (currentposition) >= 0 ? mData.get(
                currentposition).getRole().getName() : "-1";
        if (!previewStr.equals(currentStr)) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(currentStr);
        } else {
            holder.title.setVisibility(View.GONE);
            holder.title.setText("");
        }
        holder.menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // closeOpenMenu();
                if (mOnClickListener != null) {
                    mOnClickListener.onMenuClick(position);
                }
            }
        });
        holder.slidingMenu.setCustomOnClickListener(new SlidingMenu.CustomOnClickListener() {
            @Override
            public void onClick() {
                if (mOnClickListener != null) {
                    mOnClickListener.onContentClick(position);
                }
            }
        });
    }

    public interface OnClickListener {
        void onMenuClick(int position);

        void onContentClick(int position);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView menuText;
        RelativeLayout content;
        TextView title;
        SlidingMenu slidingMenu;
        TextView staffName;

        MyViewHolder(View itemView) {
            super(itemView);
            menuText = (TextView) itemView.findViewById(R.id.menuText);
            staffName = (TextView) itemView.findViewById(R.id.staff_name);
            content = (RelativeLayout) itemView.findViewById(R.id.content);
            slidingMenu = (SlidingMenu) itemView.findViewById(R.id.slidingMenu);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
