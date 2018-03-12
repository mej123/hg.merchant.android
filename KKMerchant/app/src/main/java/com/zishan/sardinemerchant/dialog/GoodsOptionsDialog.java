package com.zishan.sardinemerchant.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.zishan.sardinemerchant.listener.GoodsOptionListener;
import com.zishan.sardinemerchant.adapter.store.GoodsOptionsAdapter;

import java.util.ArrayList;
import java.util.List;

import top.ftas.ftaswidget.dialog.CommonDialog;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;

import com.zishan.sardinemerchant.R;

/**
 * 商品管理的对话框(全部 在售 下架)
 * Created by wislie on 2017/9/22.
 */

public class GoodsOptionsDialog extends CommonDialog {

    private int x;//对话框的起始x值
    private int y;//对话框的起始y值
    private List<String> mOptionList;
    private RecyclerView mOptionRecycler;
    private GoodsOptionsAdapter mAdapter;
    private GoodsOptionListener mOptionListener;

    //mOptionList 不应该只是String类型
    public static GoodsOptionsDialog newInstance(ArrayList<String> optionList, int x, int y){
        GoodsOptionsDialog dialog = new GoodsOptionsDialog();
        Bundle data = new Bundle();
        data.putInt("x", x);
        data.putInt("y", y);
        data.putStringArrayList("optionList", optionList);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x = getArguments().getInt("x");
        y = getArguments().getInt("y");
        mOptionList = getArguments().getStringArrayList("optionList");
    }

    @Override
    public void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        mOptionRecycler = (RecyclerView) view.findViewById(R.id.goods_options_recycler_view);
        mAdapter = new GoodsOptionsAdapter(getActivity(), mOptionList);
        mOptionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mOptionRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new CommonAdapter.OnAdapterItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                if(mOptionListener != null){
                    mOptionListener.onItemClick(position);
                    dismiss();
                }
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_goods_options;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getGravity() {
        return Gravity.TOP | Gravity.LEFT;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public boolean dimAmountIsZero() {
        return true;
    }

    @Override
    public void dismissDialog() {
        if(mOptionListener != null){
            mOptionListener.dismissDialog();
        }

    }

    public void setGoodsOptionListener(GoodsOptionListener listener){
        this.mOptionListener = listener;
    }


}
