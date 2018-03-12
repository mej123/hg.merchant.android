package com.zishan.sardinemerchant.adapter.page;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.TableDishItemEntity;
import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.recyclerview.WrapContentLinearLayoutManager;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * 订单详情查看
 * Created by wislie on 2017/11/16.
 */

public class TableDetailReviewAdapter extends BaseQuickAdapter<TableDishItemEntity> {

    public TableDetailReviewAdapter(int layoutResId, List<TableDishItemEntity> data) {
        super(layoutResId, data);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {

        return new BaseViewHolder(mContext, getItemView(layoutResId, parent));


    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, null);
    }

    @Override
    protected void convert(final BaseViewHolder holder, TableDishItemEntity data) {
        TextView titleText = holder.getView(R.id.arrived_title);

        RecyclerView recycler = holder.getView(R.id.item_arrived_dish);
        recycler.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        final int state = data.getState();

        if (state < 3) { //新订单
            titleText.setText("新订单");


        } else {
            if (data.getType() == 0) {
                titleText.setText("首次下单");
            } else if (data.getType() == 1) {
                titleText.setText("追加订单");
            }
        }

        DetailReviewOrderAdapter adapter = new DetailReviewOrderAdapter(
                R.layout.item_detail_review_order, getDishByCategory(data.getItems()));
        recycler.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemListener != null) {
                    if (mOnItemListener != null) {

                        if (state < 3) {
                            mOnItemListener.onSelected(holder.getAdapterPosition(), position, false);
                        } else {
                            mOnItemListener.onSelected(holder.getAdapterPosition(), position, true);
                        }

                    }
                }
            }
        });


        TextView timeText = holder.getView(R.id.arrived_time);
        if (data.getSubmitTime() != null) {
            timeText.setText(DatePickerUtil.getFormatDate(data.getSubmitTime(), "HH:mm"));
        }

        TextView remarkText = holder.getView(R.id.arrived_remark);
        if (!TextUtils.isEmpty(data.getRemarks())) {
            remarkText.setHint("备注:" + data.getRemarks());
        } else {
            remarkText.setHint("备注:");
        }
    }


    //对TableMenuItemEntity进行归类
    private ArrayList<TableMenuItemEntity> getDishByCategory(ArrayList<TableMenuItemEntity> dataList) {

        Collections.sort(dataList, new Comparator<TableMenuItemEntity>() {
            @Override
            public int compare(TableMenuItemEntity o1, TableMenuItemEntity o2) {
                if(o1.getCustomGroupId() == null || o2.getCustomGroupId() == null) return 0;
                if(o1.getCustomGroupId().longValue() > o2.getCustomGroupId().longValue()) return 1;
                return -1;
            }
        });
        return dataList;
    }
}
