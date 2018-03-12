package com.kuku.kkmerchant.ordermeal;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.entity.StoreTablesEntity;
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

public class StoreTablesActivity extends BaseActivity {


    private List<StoreTablesEntity> data;

    @BindView(R.id.ry_store_table)
    PullToRefreshRecyclerView ryStoreTable;

    @Override
    protected int getLayoutResId() {
        return R.layout.ordermeal_store_tables;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.store_tables));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        data = new ArrayList<>();

        StoreTablesEntity s1 = new StoreTablesEntity("A1188(包厢)", 0);
        StoreTablesEntity s2 = new StoreTablesEntity("A1166(包厢)", 1);
        StoreTablesEntity s3 = new StoreTablesEntity("A110", 1);
        StoreTablesEntity s4 = new StoreTablesEntity("A111", 2);
        StoreTablesEntity s5 = new StoreTablesEntity("A112", 2);

        data.add(s1);
        data.add(s2);
        data.add(s3);
        data.add(s4);
        data.add(s5);


        //设置列表的样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryStoreTable.setLayoutManager(layoutManager);


        ModeAdapter adapter = new ModeAdapter<StoreTablesEntity>(this, R.layout.store_tables_item, data) {

            @Override
            public void convert(ViewHolder holder, StoreTablesEntity storeTablesEntity, final int position) {


                holder.setText(R.id.tv_tables_number, storeTablesEntity.getTableNumber());


                //0代表消费中 1代表已安排  2代表空
                int takeState = storeTablesEntity.getTakeState();

                String statedes = "";

                if (takeState == 0) {

                    statedes = "消费中";

                } else if (takeState == 1) {

                    statedes = "已安排";
                } else if (takeState == 2) {

                    statedes = "空";
                }

                holder.setText(R.id.tv_take_state, statedes);


                holder.setOnclickListener(R.id.recycle_store_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(StoreTablesActivity.this, "点击了" + position + "条目", Toast.LENGTH_SHORT).show();

                        if (position == 0) {

                            Skip.toActivity(StoreTablesActivity.this, TakeDetailsActivity.class);

                        }else  if (position==1){

                            Skip.toActivity(StoreTablesActivity.this, AdvaceOrderActivity.class);

                        }
                    }
                });
            }
        };

        ryStoreTable.setAdapter(adapter);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}
