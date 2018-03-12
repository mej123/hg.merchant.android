package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableDetailEntity;
import com.example.wislie.rxjava.model.page.TableDishItemEntity;
import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TablePutDishPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TablePutDishView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TablePutDishDetailAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 订单详情(未出菜 已出菜)
 * Created by wislie on 2017/12/12.
 */

public class TableOutputDishDetailActivity extends BActivity<TablePutDishView, TablePutDishPresenter>
        implements TablePutDishView {

    @BindView(R.id.table_detail_recycler)
    MaxRecyclerView mRecycler;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    @BindView(R.id.table_name)
    TextView mTableNameText;
    @BindView(R.id.order_date)
    TextView mTableDateText;
    @BindView(R.id.order_time)
    TextView mTableTimeText;

    @BindView(R.id.table_total_price)
    TextView mTotalPriceText;

    @BindView(R.id.put_dish)
    TextView mPutDishText;

    private TablePutDishDetailAdapter mAdapter;
    private List<TableMenuItemEntity> mDataList = new ArrayList<>();

    //就餐id
    private long repast_id;

    private long order_dishes_id;
    //总价
    private long totalPrice;
    //true表示已出菜,false表示未出菜
    private boolean is_out_dish;

    @Override
    protected TablePutDishPresenter createPresenter() {
        return new TablePutDishPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_output_dish_detail;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionbarTitle(getString(R.string.order_detail));
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null) {
            repast_id = intent.getLongExtra("repast_id", repast_id);
            is_out_dish = intent.getBooleanExtra("is_out_dish", is_out_dish);
        }

        if (is_out_dish) {
            mPutDishText.setVisibility(View.GONE);
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAdapter = new TablePutDishDetailAdapter(mDataList);

        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {
            @Override
            public void onSelected(int position) {

                List<TableMenuItemEntity> dataList = mAdapter.getData();
                if (dataList == null) return;
                TableMenuItemEntity data = dataList.get(position);
                Long product_id = null;
                if (data.getIsNonstandard() == 0) {
                    product_id = data.getProductId();
                }

                mPresenter.putDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        repast_id, order_dishes_id, product_id, data.getProductName());
            }
        });
        showProgressDialog();
        loadData();
    }

    private void loadData() {
        mPresenter.getTableDetail(repast_id, UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
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
    public void putSingleDishSuccess(Object data) {
        loadData();
    }

    @Override
    public void putSingleDishFailed() {
        //将"删除"按钮缩回去
        SwipeItemLayout.closeAllItems(mRecycler);
    }

    @Override
    public void putAllDishSuccess(Object data) {
        loadData();
    }

    @Override
    public void putAllDishFailed() {

    }

    @Override
    public void getTableDetailSuccess(TableDetailEntity data) {
        updateTableDetailHeader(data);
    }

    @Override
    public void getTableDishList(List<TableDishItemEntity> dataList) {
        if (dataList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            mDataList.clear();
            totalPrice = 0;
            //datalist size只会为1
            for (int i = 0; i < dataList.size(); i++) {
                TableDishItemEntity data = dataList.get(i);
                order_dishes_id = data.getId();
                if (data == null) continue;
                List<TableMenuItemEntity> itemList = data.getItems();
                if (itemList == null || itemList.size() == 0) continue;
                totalPrice += data.getTotalAmount();
                updateType(itemList);
                mDataList.addAll(itemList);
            }
            mAdapter.setNewData(mDataList);
            mTotalPriceText.setText(StringUtil.point2String(totalPrice));
        }
    }

    //更新头部信息
    private void updateTableDetailHeader(TableDetailEntity data) {
        mTableDateText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "yyyy.MM.dd"));
        mTableTimeText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "HH:mm"));
        mTableNameText.setText(data.getSeatName());

    }

    //根据state更新itemType的值
    private void updateType(List<TableMenuItemEntity> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            TableMenuItemEntity item = itemList.get(i);
            if (item.getState() < 3) {
                item.setItemType(TablePutDishDetailAdapter.TYPE_SWIPE);
            } else {
                item.setItemType(TablePutDishDetailAdapter.TYPE_NORMAL);
            }
        }
    }

    @Override
    public void getTableDetailFailed() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        mTotalPriceText.setText("0.00");
    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        mTotalPriceText.setText("0.00");
    }

    @OnClick({R.id.put_dish})
    public void onClick(View view) {
        switch (view.getId()) {
            //已上菜
            case R.id.put_dish:
                putAllDishes();
                break;
        }
    }

    //全部上菜
    public void putAllDishes() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确定该笔订单出菜完毕", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                mPresenter.putAllDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        repast_id, order_dishes_id);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
