package com.kuku.kkmerchant.activity;



import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseActivity;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.recycleviewadapter.TableboxAdapter;

import java.util.Arrays;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.view.CustomToolBar;


/**
 * 桌台包厢
 * Created by wislie on 2017/9/27.
 */

public class TableBoxActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    CustomToolBar mToolbar;
    @BindView(R.id.table_box_recycler_view)
    RecyclerView mTableboxRecycler;

    private TableboxAdapter mAdapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_box;
    }

    @Override
    protected void initContentView() {

        mToolbar.setCustomToolBarListener(new CustomToolBar.CustomToolBarListener() {
            @Override
            public void back() {
                finish();
            }

            @Override
            public void operate() {

            }
        });

        mAdapter = new TableboxAdapter(this, Arrays.asList("","","","","","","","",""));
        mTableboxRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTableboxRecycler.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 22));
        mTableboxRecycler.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }



}
