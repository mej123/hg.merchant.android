package com.kuku.kkmerchant.ordermeal;

import android.support.v7.widget.LinearLayoutManager;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.entity.AdvaceOrderDetical;
import com.kuku.kkmerchant.recycleviewadapter.ModeAdapter;
import com.kuku.kkmerchant.recycleviewadapter.ViewHolder;
import com.kuku.kkmerchant.view.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yang on 2017/9/13.
 */

public class AdvaceOrderDeticalActivity extends BaseActivity {
    @BindView(R.id.ry_advace_order_detical)
    PullToRefreshRecyclerView ryAdvaceOrderDetical;
    private List<AdvaceOrderDetical> data;

    @Override
    protected int getLayoutResId() {
        return R.layout.advance_order_detical;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.advace_order_detical));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {


        data = new ArrayList<>();
        AdvaceOrderDetical s1 = new AdvaceOrderDetical();
        AdvaceOrderDetical s2 = new AdvaceOrderDetical();
        AdvaceOrderDetical s3 = new AdvaceOrderDetical();
        data.add(s1);
        data.add(s2);
        data.add(s3);

        //设置列表的样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryAdvaceOrderDetical.setLayoutManager(layoutManager);

        ModeAdapter adpter = new ModeAdapter<AdvaceOrderDetical>(this, R.layout.tables_detical_item, data) {


            @Override
            public void convert(ViewHolder holder, AdvaceOrderDetical advaceOrderDetical, int position) {


            }
        };

        ryAdvaceOrderDetical.setAdapter(adpter);
        //设置是否开启上拉加载
        ryAdvaceOrderDetical.setLoadingMoreEnabled(false);
        //设置是否开启下拉刷新
        ryAdvaceOrderDetical.setPullRefreshEnabled(false);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


}
