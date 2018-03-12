package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.DishPriceModifyEntity;
import com.example.wislie.rxjava.model.page.TableDetailEntity;
import com.example.wislie.rxjava.model.page.TableDishItemEntity;
import com.example.wislie.rxjava.model.page.TableMenuItemEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableDetailPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableDetailView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableDetailAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmContentDialog;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.DishOperationDialog;
import com.zishan.sardinemerchant.dialog.DishPriceEditDialog;
import com.zishan.sardinemerchant.dialog.OrderOperationDialog;
import com.zishan.sardinemerchant.dialog.SettingInputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2017/10/13.
 * <p>
 * 预约提醒中落座详情的已到店/桌台详情
 */

public class TableDetailActivity extends BActivity<TableDetailView,
        TableDetailPresenter> implements TableDetailView {


    @BindView(R.id.meal_date)
    TextView mDateText;
    @BindView(R.id.meal_time)
    TextView mTimeText;
    @BindView(R.id.meal_num)
    TextView mPersonNumText;

    @BindView(R.id.table_total_price)
    TextView mTotalPriceText;

    @BindView(R.id.table_detail_recycler)
    RecyclerView mRecycler;

    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    private TableDetailAdapter mAdapter;
    private List<TableDishItemEntity> mDataList = new ArrayList<>();

    //就餐id
    private long repast_id;
    //订单id
    private String order_id;

    //总价
    private long totalPrice;
    //桌台详情
    private TableDetailEntity mTableDetailData;


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        ImageView operationIcon = setActionBarMenuIcon(R.mipmap.order_operation_icon);
        operationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderOperationDialog();
            }
        });
    }

    @Override
    protected TableDetailPresenter createPresenter() {
        return new TableDetailPresenter(this, this);
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null) {
            repast_id = intent.getLongExtra("repast_id", repast_id);
            order_id = intent.getStringExtra("order_id");
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TableDetailAdapter(R.layout.item_table_detail, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {

            @Override
            public void onSelected(int position) {
                showPlaceDishDialog(position);
            }

            @Override
            public void onSelected(final int parentPos, final int childrenPos, boolean tag) {
                List<TableDishItemEntity> dishList = mAdapter.getData();
                if (dishList == null || dishList.size() == 0) return;
                final TableDishItemEntity data = dishList.get(parentPos);
                if (data == null) return;
                ArrayList<TableMenuItemEntity> dataList = data.getItems();
                if (dataList == null || dataList.size() == 0) return;
                final TableMenuItemEntity item = dataList.get(childrenPos);

                //是否免单
                Boolean is_free_charge = true;
                if (item.getFree() == 3) {
                    is_free_charge = false;
                }
                //菜品状态0未入单，1已入单，3已上菜 4结束 5非正常结束 6已退菜

                switch (item.getState()) {
                    //未入单
                    case Constant.DISH_STATE_INIT:
                        showDishPriceEditDialog(data.getId(), item);
                        break;
                    //已入单 什么情况下取消免单
                    case Constant.DISH_STATE_CONFIRM:
                        showDishOperationDialog(is_free_charge, true, data.getId(), item);
                        break;
                    //已上菜
                    case Constant.DISH_STATE_PUT:
                        showDishOperationDialog(is_free_charge, false, data.getId(), item);
                        break;
                }
            }
        });
        showProgressDialog();
        loadData();
    }

    private void loadData() {
        mPresenter.getTableDetail(repast_id, UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_detail;
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


    @Override
    public void getTableDetailSuccess(TableDetailEntity data) {
        updateTableDetailHeader(data);

    }

    @Override
    public void getTableDishList(List<TableDishItemEntity> dataList) {
        mAdapter.setNewData(dataList);
        updateTableDetailList(dataList);
    }

    @Override
    public void getTableDetailFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        mTotalPriceText.setText("0.00");
    }

    @Override
    public void requestModifyTablePersonNumSuccess(Object data, int num) {
        mPersonNumText.setText(String.valueOf(num));
    }

    @Override
    public void requestModifyTablePersonNumFailed() {

    }

    @Override
    public void cancelDishesSuccess(Object object) {
        loadData();
    }

    @Override
    public void cancelDishesFailed() {

    }

    @Override
    public void cancelTableSuccess(Object data) {
        loadData();
    }

    @Override
    public void cancelTableFailed() {

    }

    @Override
    public void modifyDishPriceSuccess(DishPriceModifyEntity data) {
        loadData();
    }

    @Override
    public void modifyDishPriceFailed() {

    }

    @Override
    public void putDishesSuccess(Object data) {
        loadData();
    }

    @Override
    public void putDishesFailed() {
    }

    @Override
    public void freeDishSuccess(Object data) {
        loadData();
    }

    @Override
    public void freeDishFailed() {

    }

    @Override
    public void placeDishSuccess(Object data) {
        loadData();
    }

    @Override
    public void placeDishFailed() {

    }

    //更新头部信息
    private void updateTableDetailHeader(TableDetailEntity data) {
        mTableDetailData = data;
        setActionbarTitle(data.getSeatName());
        mDateText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "yyyy-MM.dd"));
        mTimeText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "HH:mm"));
        mPersonNumText.setText(String.valueOf(data.getDinnerNum()));

    }

    private void updateTableDetailList(List<TableDishItemEntity> dataList) {
        totalPrice = 0;
        for (int i = 0; i < dataList.size(); i++) {
            TableDishItemEntity data = dataList.get(i);
            int state = data.getState();
            if (state < 3) {
                continue;
            }
            totalPrice += data.getRealTotalAmount();
        }
        if (dataList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
        mTotalPriceText.setText(StringUtil.point2String(totalPrice));
    }

    @OnClick({R.id.meal_modify})
    public void onClick(View view) {
        switch (view.getId()) {
            //修改人数
            case R.id.meal_modify:

                SettingInputDialog inputDialog = SettingInputDialog.newInstance("更改用餐人数",
                        "请输入用餐人数", SettingInputDialog.InputStyle.Number);
                inputDialog.showDialog(getFragmentManager());
                inputDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        int dinner_num = 0;
                        try {
                            dinner_num = Integer.parseInt(values[0]);
                        } catch (Exception e) {
                            dinner_num = 0;
                        }
                        mPresenter.modifyTablePersonNum(repast_id,
                                UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), dinner_num);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                break;

        }
    }

    //订单操作
    private void showOrderOperationDialog() {
        OrderOperationDialog menuActionDialog = OrderOperationDialog.newInstance();
        menuActionDialog.showDialog(getFragmentManager());
        menuActionDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                showOperation(values[0]);
            }

            @Override
            public void onCancel() {

            }
        });
    }


    private void showOperation(String value) {
        switch (value) {
            //结算
            case Constant.OPERATION_BLANCE:
                Intent blanceIntent = new Intent(this, BlanceActivity.class);
                blanceIntent.putExtra("repast_id", repast_id);
                blanceIntent.putExtra("order_id", order_id);
                blanceIntent.putExtra("table_id", mTableDetailData.getTableId());
                startActivity(blanceIntent);
                break;
            //拼台
            case Constant.OPERATION_MERGE: //只有闲置中才能拼台
                Intent mergeIntent = new Intent(this, MergeTableActivity.class);
                if (mTableDetailData != null) {
                    mergeIntent.putExtra("seat_name", mTableDetailData.getSeatName());
                    mergeIntent.putExtra("table_id", mTableDetailData.getTableId());
                }
                startActivity(mergeIntent);
                break;
            //拆台
            case Constant.OPERATION_APART: //拆完都会在闲置中
                Intent apartIntent = new Intent(this, ApartTableActivity.class);
                if (mTableDetailData != null) {
                    apartIntent.putExtra("seat_name", mTableDetailData.getSeatName());
                    apartIntent.putExtra("table_id", mTableDetailData.getTableId());
                }
                startActivity(apartIntent);
                break;
            //转台
            case Constant.OPERATION_TRANSMIT:
                Intent transformIntent = new Intent(this, TransformTableActivity.class);
                if (mTableDetailData != null) {
                    transformIntent.putExtra("seat_name", mTableDetailData.getSeatName());
                    transformIntent.putExtra("repast_id", mTableDetailData.getRepastId());
                    transformIntent.putExtra("table_id", mTableDetailData.getTableId());
                }
                startActivity(transformIntent);
                break;
            //撤单
            case Constant.OPERATION_REMOVE:
                cancelTableOperation();
                break;
            //点菜
            case Constant.OPERATION_CHOOSE:
                Intent chooseIntent = new Intent(this, ChooseDishActivity.class);
                if (mTableDetailData != null) {
                    chooseIntent.putExtra("seat_name", mTableDetailData.getSeatName());
                    chooseIntent.putExtra("repast_id", mTableDetailData.getRepastId());
                }
                startActivity(chooseIntent);

                break;
            //查看订单详情
            case Constant.OPERATION_VIEW:
                Intent reviewIntent = new Intent(this, TableDetailReviewActivity.class);
                reviewIntent.putExtra("repast_id", repast_id);
                startActivity(reviewIntent);
                break;
        }
    }

    //撤单
    private void cancelTableOperation() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.
                newInstance("撤回订单后将会删除该订单的全部内容,您确认撤回该笔订单？", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                mPresenter.cancelTable(UserConfig.getInstance(ClientApplication.getApp())
                        .getStoreId(), repast_id);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    //修改单一商品价格
    private void showDishPriceEditDialog(final long order_dishes_id, final TableMenuItemEntity item) {
        DishPriceEditDialog dialog = DishPriceEditDialog.newInstance(item);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                modifyPrice(order_dishes_id, item, values[0]);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //退菜，免单，上菜
    private void showDishOperationDialog(final boolean isFree, boolean isPut,
                                         final long order_dishes_id, final TableMenuItemEntity item) {
        DishOperationDialog dialog = DishOperationDialog.newInstance(isFree, isPut, item);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {

                Long product_id = null;
                if (item.getIsNonstandard() == 0) {
                    product_id = item.getProductId();
                }
                if (TextUtils.isEmpty(values[0])) return;
                switch (values[0]) {
                    //退菜
                    case Constant.DISH_CANCEL:
                        mPresenter.cancelDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                repast_id, order_dishes_id, product_id, item.getProductName(), item.getNum());
                        break;
                    //免单/取消免单
                    case Constant.DISH_FREE:
                        mPresenter.freeDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                repast_id, order_dishes_id, product_id, item.getProductName(), isFree);
                        break;
                    //已上菜
                    case Constant.DISH_PUT:

                        mPresenter.putDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                repast_id, order_dishes_id, product_id, item.getProductName());

                        modifyPrice(order_dishes_id, item, values[1]);
                        break;
                    //编辑价格
                    case Constant.DISH_EDIT_PRICE:
                        modifyPrice(order_dishes_id, item, values[1]);
                        break;
                }

            }

            @Override
            public void onCancel() {

            }
        });
    }

    //修改价格
    private void modifyPrice(long order_dishes_id, TableMenuItemEntity item, String value) {

        long total_money = 0;
        if (!TextUtils.isEmpty(value)) {
            total_money = StringUtil.String2Long(value, 2, true);
        }
        Long product_id = null;
        if (item.getIsNonstandard() == 0) {
            product_id = item.getProductId();
        }

        mPresenter.modifyDishPrice(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                repast_id, order_dishes_id, product_id,
                item.getProductName(), item.getNum(), item.getValuationPrice(), total_money);
    }

    //下单
    private void showPlaceDishDialog(final int position) {
        ConfirmContentDialog dialog = ConfirmContentDialog.newInstance("下单成功", "订单内容已发至厨房", true);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {

            @Override
            public void onConfirm() {
                List<TableDishItemEntity> dataList = mAdapter.getData();
                if (dataList == null || dataList.size() == 0) return;
                TableDishItemEntity data = dataList.get(position);
                mPresenter.placeDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        repast_id, data.getId());
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
