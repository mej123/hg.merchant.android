package com.zishan.sardinemerchant.activity.page;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableBoxEntity;
import com.example.wislie.rxjava.presenter.base.page.table_box.TableBoxListPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.table_box.TableBoxListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableBoxAdapter;
import com.zishan.sardinemerchant.entity.TableBoxData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;


/**
 * 桌台包厢
 * Created by wislie on 2017/9/27.
 */

public class TableBoxActivity extends BActivity<TableBoxListView, TableBoxListPresenter>
        implements TableBoxListView {

    @BindView(R.id.table_box_springview)
    SpringView mSpringView;
    @BindView(R.id.table_box_recycler_view)
    RecyclerView mRecycler;


    private TableBoxAdapter mAdapter;

    private ArrayList<TableBoxData> mDataList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_box;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.table_box));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected TableBoxListPresenter createPresenter() {
        return new TableBoxListPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mAdapter = new TableBoxAdapter(R.layout.item_table_box, mDataList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 22));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TableBoxData tableBoxData = mAdapter.getItem(position);
                if (tableBoxData == null) return;
                Intent intent = new Intent(TableBoxActivity.this, TableTypeActivity.class);
                intent.putExtra("type", tableBoxData.getType());
                startActivity(intent);
            }
        });
        showProgressDialog();
        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
//            mSpringView.setVisibility(View.GONE);
//            mEmptyLayout.setVisibility(View.VISIBLE);
        }
        loadData();
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void loadData() {
        mPresenter.getTableBoxList(UserConfig.getInstance(this).getStoreId());
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }


    private ArrayList<TableBoxEntity> getTableBoxList(List<TableBoxEntity> result, int type) {
        ArrayList<TableBoxEntity> tableBoxList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            TableBoxEntity tableBoxEntity = result.get(i);
            if (tableBoxEntity.getType() == type) {
                tableBoxList.add(tableBoxEntity);
            }
        }
        return tableBoxList;
    }


    @Override
    public void getTableBoxListSuccess(List<TableBoxEntity> dataList) {
        ArrayList<TableBoxEntity> hallList = getTableBoxList(dataList, Constant.HALL);
        ArrayList<TableBoxEntity> cardList = getTableBoxList(dataList, Constant.CARD);
        ArrayList<TableBoxEntity> boxList = getTableBoxList(dataList, Constant.BOX);

        List<TableBoxData> newDataList = new ArrayList<>();
        newDataList.add(new TableBoxData(hallList, Constant.HALL));
        newDataList.add(new TableBoxData(cardList, Constant.CARD));
        newDataList.add(new TableBoxData(boxList, Constant.BOX));
        mAdapter.setNewData(newDataList);
        finishRefreshAndLoad();


    }

    @Override
    public void getTableBoxListFail() {

    }

    @Override
    public void showNoData() {

        List<TableBoxData> newDataList = new ArrayList<>();
        newDataList.add(new TableBoxData(new ArrayList<TableBoxEntity>(), Constant.HALL));
        newDataList.add(new TableBoxData(new ArrayList<TableBoxEntity>(), Constant.CARD));
        newDataList.add(new TableBoxData(new ArrayList<TableBoxEntity>(), Constant.BOX));
        mAdapter.setNewData(newDataList);

    }


    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }
}
