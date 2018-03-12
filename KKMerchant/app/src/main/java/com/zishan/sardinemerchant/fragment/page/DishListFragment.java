package com.zishan.sardinemerchant.fragment.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.DishDetailAddActivity;
import com.zishan.sardinemerchant.adapter.page.CategoryDishAdapter;
import com.zishan.sardinemerchant.entity.RightBean;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.listener.CheckListener;
import com.zishan.sardinemerchant.utils.ItemHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * 点菜列表
 * Created by wislie on 2017/12/5.
 */

public class DishListFragment extends BFragment implements CheckListener {

    @BindView(R.id.dish_recycler_view)
    RecyclerView mRecycler;

    private LinearLayoutManager mManager;

    private ArrayList<ProductEntity> mDataList = new ArrayList<>();

    private CategoryDishAdapter mAdapter;
    private ItemHeaderDecoration mDecoration;
    private CheckListener mCheckListener;
    private boolean move = false;
    private int mIndex = 0;
    private List<RightBean> mDatas = new ArrayList<>();

    private int index = 0;
    private long preCustomGroupId = -1;



    //添加商品
    public static final int REQUEST_DISH_DETIAL_ADD = 0x01;

    public static DishListFragment newInstance(ArrayList<ProductEntity> dataList) {
        DishListFragment fragment = new DishListFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList("dataList", dataList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<ProductEntity> dataList = getArguments().getParcelableArrayList("dataList");
        if (dataList != null && dataList.size() > 0) {
            mDataList.addAll(dataList);
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dish_list;
    }

    @Override
    protected void initBizView() {
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mManager);
        mRecycler.addOnScrollListener(new RecyclerViewListener());
        mAdapter = new CategoryDishAdapter(R.layout.item_category_dish, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<ProductEntity> dishList = mAdapter.getData();
                ProductEntity dishData = dishList.get(position);
                if (dishData == null) return;
                Intent intent = new Intent(getActivity(), DishDetailAddActivity.class);
                intent.putExtra("dish_data", dishData);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_DISH_DETIAL_ADD);
            }
        });

        mDecoration = new ItemHeaderDecoration(getActivity(), mDatas);
        mRecycler.addItemDecoration(mDecoration);
        mDecoration.setCheckListener(mCheckListener);

        for (int i = 0; i < mDataList.size(); i++) {
            Long customGroupId = mDataList.get(i).getCustomGroupId();
            RightBean body = new RightBean(customGroupId);
            body.setTag(String.valueOf(index));
            if (preCustomGroupId != customGroupId) {
                index++;
            }
            preCustomGroupId = customGroupId;
            mDatas.add(body);
        }
        mDecoration.setData(mDatas);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }




    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if(baseEvent.getTagString().equals(ACTION_NAME)){
            Bundle data = (Bundle) baseEvent.getData();
            int position = data.getInt("position");
            ProductEntity productData = data.getParcelable("dish_data");

            if (position != -1 && productData != null) {
                ProductEntity dish = (ProductEntity) mAdapter.getData().get(position);
                dish.setSelectedCount(productData.getSelectedCount());
//                mAdapter.getData().set(position, productData);
                mAdapter.notifyDataSetChanged();

            }
        }
    }

    public void setListener(CheckListener listener) {
        this.mCheckListener = listener;
    }


    @Override
    public void check(int position, boolean isScroll) {
        mCheckListener.check(position, isScroll);
    }


    public void setData(int n) {
        mIndex = n;
        mRecycler.stopScroll();
        smoothMoveToPosition(n);
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRecycler.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRecycler.getChildAt(n - firstItem).getTop();
            mRecycler.scrollBy(0, top);
        } else {
            mRecycler.scrollToPosition(n);
            move = true;
        }
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRecycler.getChildCount()) {
                    int top = mRecycler.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRecycler.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecycler.getChildCount()) {
                    int top = mRecycler.getChildAt(n).getTop();
                    mRecycler.scrollBy(0, top);
                }
            }
        }
    }
}
