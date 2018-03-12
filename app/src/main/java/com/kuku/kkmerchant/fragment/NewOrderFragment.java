package com.kuku.kkmerchant.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.recycleviewadapter.EatBeforePayAdapter;

import java.util.Arrays;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;

/**
 * 新订单
 * Created by wislie on 2017/9/21.
 */

public class NewOrderFragment extends BaseFragment {


    @BindView(R.id.order_recycler_view)
    RecyclerView mOrderRecycler;

    private EatBeforePayAdapter mAdapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initBizView(View view) {
        mAdapter = new EatBeforePayAdapter(getActivity(), Arrays.asList("","","","","","","","",""));
        mOrderRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mOrderRecycler.addItemDecoration(new SpaceItemDecoration(14, 11, 14, 11));
        mOrderRecycler.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
