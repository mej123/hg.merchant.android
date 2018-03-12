package com.kuku.kkmerchant.ordermeal;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.recycleviewdelteitem.ItemRemoveRecyclerView;
import com.kuku.kkmerchant.recycleviewdelteitem.MyAdapter;
import com.kuku.kkmerchant.recycleviewdelteitem.OnItemClickListener;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by yang on 2017/9/13.
 */

public class TakeDetailsActivity extends BaseActivity {


    @BindView(R.id.id_item_remove_recyclerview)
    ItemRemoveRecyclerView idItemRemoveRecyclerview;
    private ArrayList<String> mList;
    @Override
    protected int getLayoutResId() {
        return R.layout.take_detical;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.take_detical_title));
        setActionBarHomeIcon(R.mipmap.kk_common_back);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mList.add(i + "");
        }

        final MyAdapter adapter = new MyAdapter(this, mList);
        idItemRemoveRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        idItemRemoveRecyclerview.setAdapter(adapter);
        idItemRemoveRecyclerview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TakeDetailsActivity.this, "** " + mList.get(position) + " **", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
            }
        });












    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


}
