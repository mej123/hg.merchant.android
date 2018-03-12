package com.kuku.kkmerchant.ordermeal;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.entity.AdvaceOrderNoticeEntity;
import com.kuku.kkmerchant.recycleviewadapter.ModeAdapter;
import com.kuku.kkmerchant.recycleviewadapter.ViewHolder;
import com.kuku.kkmerchant.utils.Skip;
import com.kuku.kkmerchant.view.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yang on 2017/9/13.
 */

public class AdvaceOrderNoticeFragment extends BaseFragment {


    @BindView(R.id.ry_advace_order_notice)
    PullToRefreshRecyclerView ryAdvaceOrderNotice;
    private List<AdvaceOrderNoticeEntity> data;
    private AdvaceOrderNoticeEntity s4;

    @Override
    protected int getLayoutResId() {
        return R.layout.advace_order_notice;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void initBizView(View view) {


        ryAdvaceOrderNotice = (PullToRefreshRecyclerView) view.findViewById(R.id.ry_advace_order_notice);

        init();
    }

    private void init() {


        data = new ArrayList<>();

        AdvaceOrderNoticeEntity s1 = new AdvaceOrderNoticeEntity("马云");
        AdvaceOrderNoticeEntity s2 = new AdvaceOrderNoticeEntity("马云");
        AdvaceOrderNoticeEntity s3 = new AdvaceOrderNoticeEntity("马云");
        AdvaceOrderNoticeEntity s4 = new AdvaceOrderNoticeEntity("马云");


        data.add(s1);
        data.add(s2);
        data.add(s3);
        data.add(s4);

        //设置列表的样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryAdvaceOrderNotice.setLayoutManager(layoutManager);

        ModeAdapter adpter = new ModeAdapter<AdvaceOrderNoticeEntity>(getActivity(), R.layout.advace_order_item, data) {

            @Override
            public void convert(ViewHolder holder, AdvaceOrderNoticeEntity advaceOrderNoticeEntity, final int position) {


                holder.setText(R.id.tv_name, advaceOrderNoticeEntity.getName());
                holder.setOnclickListener(R.id.ll_advace_order_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "点击了" + position + "条目", Toast.LENGTH_SHORT).show();


                        Skip.toActivity(getActivity(), AdvaceOrderDeticalActivity.class);


                    }
                });

            }
        };

        ryAdvaceOrderNotice.setAdapter(adpter);

    }


}
