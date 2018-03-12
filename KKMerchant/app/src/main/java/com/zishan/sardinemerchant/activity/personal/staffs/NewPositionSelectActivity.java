package com.zishan.sardinemerchant.activity.personal.staffs;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.PositionManagerEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewPositionSelectPresenter;
import com.example.wislie.rxjava.view.personal.staff.NewPositionSelectView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.NewPositionSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 职位选择
 */

public class NewPositionSelectActivity extends BActivity<NewPositionSelectView,
        NewPositionSelectPresenter> implements NewPositionSelectView {
    @BindView(R.id.position_select_recycle_view)
    RecyclerView mRecycleView;

    private List<PositionManagerEntity> mDataList = new ArrayList<>();
    private NewPositionSelectAdapter mAdapter;
    private String currentPositionName;


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_staff_select));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected NewPositionSelectPresenter createPresenter() {
        return new NewPositionSelectPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_position_select;
    }

    @Override
    protected void initContentView() {
        if (getIntent() == null) return;
        currentPositionName = getIntent().getStringExtra("currentPosition");
        mAdapter = new NewPositionSelectAdapter(R.layout.item_new_position_select, mDataList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(NewPositionSelectActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PositionManagerEntity positionManagerEntity = mAdapter.getItem(position);
                List<PositionManagerEntity> mDataList = mAdapter.getData();
                if (positionManagerEntity == null) return;
                for (int i = 0; i < mDataList.size(); i++) {
                    if (i == position) {
                        mDataList.get(i).setSelect(true);
                    } else {
                        mDataList.get(i).setSelect(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                returnData(positionManagerEntity);//关闭当前界面并返回数据
            }
        });
        mPresenter.getStoreRoleList(UserConfig.getInstance(this).getMerchantId());
    }

    private void showLastSelect() {
        if (!TextUtils.isEmpty(currentPositionName) && currentPositionName.length() > 0) {
            List<PositionManagerEntity> mDataList = mAdapter.getData();
            for (int i = 0; i < mDataList.size(); i++) {
                PositionManagerEntity positionManagerEntity = mDataList.get(i);
                if (positionManagerEntity.getName().equals(currentPositionName)) {
                    mDataList.get(i).setSelect(true);
                } else {
                    mDataList.get(i).setSelect(false);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void returnData(PositionManagerEntity positionManagerEntity) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("positionName", positionManagerEntity.getName());
        Long id = positionManagerEntity.getId();
        resultIntent.putExtra("positionId", id);
        setResult(RESULT_OK, resultIntent);
        finish();
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

    @Override
    public void getStoreRoleListSuccess(List<PositionManagerEntity> dataList) {

        if (dataList == null) return;
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setSelect(false);//全部为未选中
            }
            mAdapter.setNewData(dataList);
            mAdapter.notifyDataSetChanged();
        }

        showLastSelect();//回显选择数据
    }

    @Override
    public void getStoreRoleListFailed() {

    }
}
