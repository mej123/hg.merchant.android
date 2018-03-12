package com.zishan.sardinemerchant.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

public abstract class SingleMenuView extends RelativeLayout {

    private RecyclerView mRecycler;

    public SingleMenuView(Context context) {
        super(context);
        init(context);
    }

    public SingleMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SingleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.drop_menu_list, this, true);
        mRecycler = (RecyclerView) findViewById(R.id.drop_menu_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        mRecycler.setAdapter(getAdapter());


    }


    protected abstract BaseQuickAdapter getAdapter();

}
