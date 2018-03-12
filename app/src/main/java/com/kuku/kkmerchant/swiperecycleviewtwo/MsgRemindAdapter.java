package com.kuku.kkmerchant.swiperecycleviewtwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 消息adapter
 * <p/>
 * -->特别注意extends后面Adapter<>里面要写自己定义的ViewHolder
 */
public class MsgRemindAdapter extends RecyclerView.Adapter<MsgRemindAdapter.RemindViewHolder>
        implements ItemSlideHelper.Callback {


    private Context context;
    private List<MsgVo> mDatas = new ArrayList<MsgVo>();

    private RecyclerView mRecyclerView;

    public MsgRemindAdapter(Context context, List<MsgVo> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_msg_remind, parent, false);
        return new RemindViewHolder(view);
    }

    /**
     * 将recyclerView绑定Slide事件
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
    }

    @Override
    public void onBindViewHolder(final RemindViewHolder holder, final int position) {
        /**
         * 消息状态
         */
        if (mDatas.get(position).isChecked()) {
            holder.msgRemindPoint.setBackgroundResource(R.drawable.shape_remind_point_gray);
        } else {
            holder.msgRemindPoint.setBackgroundResource(R.drawable.shape_remind_point_theme);
        }
        //消息标题
        holder.tvRemindTitle.setText(mDatas.get(position).getTitle());
        //消息内容
        holder.tvRemindContent.setText(mDatas.get(position).getContent());

        /**
         * -->特别注意，敲黑板了啊！！！在执行notify的时候，取position要取holder.getAdapterPosition()，
         * 消息被删除之后，他原来的position是final的，所以取到的值不准确，会报数组越界。
         */

        //消息主体监听，这里我是让他添加一条数据，替换成你需要的操作即可
//        holder.llMsgRemindMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: 我是第" + position + "个条目");
//                addData(mDatas.size());
//            }
//        });
        //标记已读监听
        holder.tvMsgRemindCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas.get(holder.getAdapterPosition()).setChecked(true);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        //删除监听
        holder.tvMsgRemindDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeData(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 此方法用来计算水平方向移动的距离
     *
     * @param holder
     * @return
     */
    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            //viewGroup包含3个控件，即消息主item、标记已读、删除，返回为标记已读宽度+删除宽度
            return viewGroup.getChildAt(1).getLayoutParams().width
                    + viewGroup.getChildAt(2).getLayoutParams().width;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

    /**
     * 自定义的ViewHolder
     */
    public class RemindViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.msg_remind_point)
        View msgRemindPoint;
        @BindView(R.id.tv_remind_title)
        TextView tvRemindTitle;
        @BindView(R.id.tv_remind_content)
        TextView tvRemindContent;
        @BindView(R.id.ll_msg_remind_main)
        LinearLayout llMsgRemindMain;
        @BindView(R.id.tv_msg_remind_check)
        TextView tvMsgRemindCheck;
        @BindView(R.id.tv_msg_remind_delete)
        TextView tvMsgRemindDelete;

        public RemindViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 添加单条数据
     *
     * @param position
     */
    public void addData(int position) {
        MsgVo vo = new MsgVo();
        if (position % 2 == 1) {
            vo.setChecked(false);
            vo.setTitle("隔壁的二蛋");
            vo.setContent("对方撤回了一条消息并砍了你的狗，问你服不服。");
        } else {
            vo.setChecked(false);
            vo.setTitle("对面的三娃");
            vo.setContent("今天晚上开黑，4缺1，来不来？");
        }
        mDatas.add(position, vo);
        notifyItemInserted(position);
    }

    /**
     * 删除单条数据
     *
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

}
