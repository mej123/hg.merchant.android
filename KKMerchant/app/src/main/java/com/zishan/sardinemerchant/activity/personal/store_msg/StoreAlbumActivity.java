package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreAlbumEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreAlbumPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreAlbumView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.StoreAlbumAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.recyclerview.WrapStaggeredGridLayoutManager;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 店铺照片
 * Created by wislie on 2017/11/28.
 */

public class StoreAlbumActivity extends BActivity<StoreAlbumView, StoreAlbumPresenter>
        implements StoreAlbumView {

    @BindView(R.id.album_springview)
    SpringView mSpringView;
    @BindView(R.id.album_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.operate_album)
    TextView mOperateAlbumText;
    //选择 or 全选
    private TextView mChooseText;
    //相册是否被选中， 默认情况下不被选中
    private StoreAlbumAdapter mAdapter;
    private List<StoreAlbumEntity> mDataList = new ArrayList<StoreAlbumEntity>();
    private List<StoreAlbumEntity> mSelectDeleteImageList = new ArrayList<>();
    private TextView mCancle;
    private ImageView backIcon;
    private int state = 1;//判断当前的选中状态 0 删除 1 添加
    private boolean isFirstSelect = true;
    private int[] imageIds;
    private long store_id = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_album;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getResources().getString(R.string.store_photo));
        backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        mChooseText = setActionBarMenuText(getResources().getString(R.string.choose_some));
        mCancle = setActionBarHomeText(getResources().getString(R.string.cancel));
        mCancle.setVisibility(View.GONE);
        //选择操作
        mChooseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstSelect) {
                    mChooseText.setText(getResources().getString(R.string.choose_all));
                    mOperateAlbumText.setText(getResources().getString(R.string.delete));
                    setActionBarHomeIcon(R.mipmap.back_icon).setVisibility(View.GONE);
                    mCancle.setVisibility(View.VISIBLE);
                    state = 0;

                    List<StoreAlbumEntity> mDataList = mAdapter.getData();
                    for (int i = 0; i < mDataList.size(); i++) {
                        StoreAlbumEntity storeAlbumEntity = mDataList.get(i);
                        storeAlbumEntity.setEditState(true);
                    }

                    isFirstSelect = false;
                } else {
                    if (state == 1) {
                        mChooseText.setText(getResources().getString(R.string.choose_all));
                        mOperateAlbumText.setText(getResources().getString(R.string.delete));
                        setActionBarHomeIcon(R.mipmap.back_icon).setVisibility(View.GONE);
                        mCancle.setVisibility(View.VISIBLE);
                        List<StoreAlbumEntity> mDataList = mAdapter.getData();
                        for (int i = 0; i < mDataList.size(); i++) {
                            StoreAlbumEntity storeAlbumEntity = mDataList.get(i);
                            storeAlbumEntity.setEditState(true);
                            storeAlbumEntity.setSelect(false);
                        }
                        state = 0;
                    } else {
                        changeAllImageSelectState(true);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        //取消操作
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancle.setVisibility(View.GONE);
                backIcon.setVisibility(View.VISIBLE);
                mChooseText.setText(getResources().getString(R.string.choose_some));
                mOperateAlbumText.setText(getResources().getString(R.string.add_album));
                state = 1;
                AllSelectNoShow();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void AllSelectNoShow() {

        List<StoreAlbumEntity> mDataList = mAdapter.getData();

        for (int i = 0; i < mDataList.size(); i++) {
            StoreAlbumEntity storeAlbumEntity = mDataList.get(i);
            storeAlbumEntity.setAllCancel(true);
            storeAlbumEntity.setEditState(false);
        }
    }

    private void changeAllImageSelectState(boolean isSelectAll) {
        List<StoreAlbumEntity> mDataList = mAdapter.getData();
        if (isSelectAll) {
            for (int i = 0; i < mDataList.size(); i++) {
                StoreAlbumEntity storeAlbumEntity = mDataList.get(i);
                storeAlbumEntity.setSelect(true);
                storeAlbumEntity.setEditState(true);
            }
        } else {
            for (int i = 0; i < mDataList.size(); i++) {
                StoreAlbumEntity storeAlbumEntity = mDataList.get(i);
                storeAlbumEntity.setSelect(false);
                storeAlbumEntity.setEditState(true);
            }
        }
    }

    @Override
    protected StoreAlbumPresenter createPresenter() {
        return new StoreAlbumPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            store_id = intent.getLongExtra("store_id", store_id);
        }

        initSpringView();
        mRecycler.setLayoutManager(new WrapStaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(this, 6), DensityUtil.dip2px(this, 9),
                DensityUtil.dip2px(this, 6), 0));
        mAdapter = new StoreAlbumAdapter(R.layout.item_store_album, mDataList);
        mAdapter.openLoadMore(true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StoreAlbumEntity storeAlbumEntity = mAdapter.getItem(position);
                // 0编辑状态  1 非编辑状态
                if (state == 0) {
                    boolean select = storeAlbumEntity.isSelect();//当前的选中状态

                    if (select) {
                        storeAlbumEntity.setSelect(false);
                    } else {
                        storeAlbumEntity.setSelect(true);
                    }
                    mAdapter.notifyDataSetChanged();
                } else if (state == 1) {
                    //非编辑状态，点击更换图片,区分logo和其它类型图片

                    Intent editIntent = new Intent(StoreAlbumActivity.this, StoreAlbumAddActivity.class);
                    editIntent.putExtra("store_id", store_id);
                    editIntent.putExtra("album_photo_data", storeAlbumEntity);
                    startActivity(editIntent);
                }
            }
        });
        showProgressDialog();
        loadData();
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();//停止加载
                } else {
                    //初始化加载
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
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

    @OnClick({R.id.operate_album})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.operate_album:
                if (state == 0) {
                    //删除
                    SelectImageId();
                    if (imageIds.length == 0) {
                        ToastUtil.show(this, "未选中图片");
                        return;
                    }
                    mPresenter.deleteStoreAlbum(store_id, imageIds);
                    mCancle.setVisibility(View.GONE);
                    backIcon.setVisibility(View.VISIBLE);
                    mChooseText.setText(getResources().getString(R.string.choose_some));
                    mOperateAlbumText.setText(getResources().getString(R.string.add_album));
                    state = 1;
                    AllSelectNoShow();
                    mAdapter.notifyDataSetChanged();
                } else if (state == 1) {
                    //添加照片
                    Intent addImageIntent = new Intent(this, StoreAlbumAddActivity.class);
                    addImageIntent.putExtra("store_id", store_id);
                    startActivity(addImageIntent);
                }
                break;
        }
    }

    private void SelectImageId() {
        List<StoreAlbumEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            StoreAlbumEntity storeAlbumEntity = dataList.get(i);
            if (storeAlbumEntity.isSelect()) {
                mSelectDeleteImageList.add(storeAlbumEntity);//添加到选中的图片集合中
            }
        }
        int size = mSelectDeleteImageList.size();
        imageIds = new int[size];
        for (int i = 0; i < mSelectDeleteImageList.size(); i++) {
            Long id = mSelectDeleteImageList.get(i).getId();
            int imageId = Integer.parseInt(String.valueOf(id));
            imageIds[i] = imageId;//选中图片id添加到数组中
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();

    }

    private void loadData() {
        mPresenter.getStoreAlbumList(store_id);//获得店铺照片列表请求
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
    public void getStoreAlbumListSuccess(List<StoreAlbumEntity> dataList) {
        if (dataList == null) return;
        List<StoreAlbumEntity> orgainDateList = mAdapter.getData();
        orgainDateList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            StoreAlbumEntity storeAlbumEntity = dataList.get(i);

            storeAlbumEntity.setAllCancel(true);
            mDataList.add(storeAlbumEntity);
        }
        mAdapter.setNewData(mDataList);
        mAdapter.notifyDataSetChanged();
        finishRefreshAndLoad();
    }

    @Override
    public void getStoreAlbumListFailed() {
        finishRefreshAndLoad();
    }

    @Override
    public void deleteStoreAlbumSuccess(Object data) {
        mDataList.removeAll(mSelectDeleteImageList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteStoreAlbumFailed() {

    }

}
