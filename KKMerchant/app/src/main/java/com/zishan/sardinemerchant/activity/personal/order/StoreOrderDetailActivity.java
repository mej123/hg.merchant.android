package com.zishan.sardinemerchant.activity.personal.order;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.MerchantOrderDetailEntity;
import com.example.wislie.rxjava.model.personal.MerchantOrderItemEntity;
import com.example.wislie.rxjava.model.personal.OrderDetailEntity;
import com.example.wislie.rxjava.model.personal.PromotionRecordEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreOrderDetailPresenter;
import com.example.wislie.rxjava.view.base.personal.StoreOrderDetailView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.StoreDetailAdapter;
import com.zishan.sardinemerchant.adapter.personal.StoreDetailDiscountAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 订单详情
 * Created by wislie on 2017/11/8.
 */

public class StoreOrderDetailActivity extends BActivity<StoreOrderDetailView, StoreOrderDetailPresenter>
        implements StoreOrderDetailView {

    @BindView(R.id.goods_info_title)
    TextView mGoodsInfoTitleText;
    @BindView(R.id.goods_info_recycler_view)
    MaxRecyclerView mRecycler;
    @BindView(R.id.order_space)
    View mSpace;
    @BindView(R.id.pay_info_title)
    TextView mPayInfoTitleText; //支付信息
    @BindView(R.id.consume_layout)
    RelativeLayout mConsumeLayout;
    @BindView(R.id.no_discount_layout)
    RelativeLayout mNoDiscountLayout;

    @BindView(R.id.consume_amount)
    TextView mConsumeAmountText; //消费金额
    @BindView(R.id.out_discount_amount)
    TextView mNoDiscountAmountText; //不参与优惠金额


    @BindView(R.id.discount_recycler_view)
    MaxRecyclerView mDiscountRecycler; //商家优惠
    @BindView(R.id.total_price_title)
    TextView mTotalPriceTitleText;
    @BindView(R.id.total_price)
    TextView mTotalPriceText;//收到的真实总金额

    @BindView(R.id.store_order_layout)
    RelativeLayout mOrderInfoLayout;
    @BindView(R.id.store_order_table_name)
    TextView mTableNumText; //桌台编号
    @BindView(R.id.store_order_pay_way)
    TextView mPaywayText; //支付方式
    @BindView(R.id.store_order_user_info)
    TextView mUserAccountText; //用户信息
    @BindView(R.id.store_order_leave_date)
    TextView mCloseDateText;//结束日期
    @BindView(R.id.store_order_leave_time)
    TextView mCloseTimeText; //结束时间
    @BindView(R.id.store_order_start_date)
    TextView mStartDateText;//开台日期
    @BindView(R.id.store_order_start_time)
    TextView mStartTimeText; //开台时间
    @BindView(R.id.store_order_seller_num)
    TextView mSellerNumText; //商户订单号
    @BindView(R.id.store_order_num)
    TextView mOrderNumText; //订单编号

    //商家优惠
    private StoreDetailDiscountAdapter mDiscountAdapter;
    private List<PromotionRecordEntity> mDiscountList = new ArrayList<>();

    private StoreDetailAdapter mAdapter;
    private List<MerchantOrderItemEntity> mDataList = new ArrayList<>();

    //订单id
    private String order_id;
    //true表示清台
    private boolean clean_table;
    //就餐id
    private long repast_id;

    @Override
    protected StoreOrderDetailPresenter createPresenter() {
        return new StoreOrderDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_order_detail;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("order_id");
            clean_table = intent.getBooleanExtra("clean_table", clean_table);
            repast_id = intent.getLongExtra("repast_id", repast_id);
        }
        setActionbarTitle(getResources().getString(R.string.order_detail));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        if (clean_table) {
            //清台
            TextView cleanText = setActionBarMenuText(getString(R.string.clean));
            cleanText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.cleanTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), repast_id);
                }
            });
        }
    }

    @Override
    protected void initContentView() {

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new StoreDetailAdapter(mDataList);
        mRecycler.setAdapter(mAdapter);

        mDiscountRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDiscountAdapter = new StoreDetailDiscountAdapter(this, mDiscountList);
        mDiscountRecycler.setAdapter(mDiscountAdapter);

        if (!TextUtils.isEmpty(order_id)) {
            mPresenter.getStoreOrderDetail(UserConfig.getInstance(
                    ClientApplication.getApp()).getStoreId(), order_id);
        }

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


    private void updateDetail(OrderDetailEntity result) {
        List<PromotionRecordEntity> discountList = result.getPromotionRecord();
        if (discountList != null && discountList.size() > 0) {
            mDiscountList.clear();
            mDiscountList.addAll(discountList);
        }
        mDiscountAdapter.notifyDataSetChanged();

        MerchantOrderDetailEntity data = result.getMerchantOrderDetail();
        if (data != null) {
            if (data.getOrderType() == Constant.STORE_ORDER_TYPE_LIVE_CONSUME
                    && data.getType() == Constant.STORE_PAY_TYPE_LIVE) { //现金支付

                mPayInfoTitleText.setVisibility(View.GONE);
                mConsumeLayout.setVisibility(View.GONE);
                mNoDiscountLayout.setVisibility(View.GONE);
//                mSellerDiscountTitleText.setVisibility(View.GONE);
                mDiscountRecycler.setVisibility(View.GONE);
                mSpace.setVisibility(View.GONE);
                mTotalPriceTitleText.setText("订单金额");
            } else if (data.getOrderType() == Constant.STORE_ORDER_TYPE_LIVE_CONSUME
                    && data.getType() == Constant.STORE_PAY_TYPE_ONLINE) { //线上支付


            } else if (data.getOrderType() == Constant.STORE_ORDER_TYPE_LIVE_SCAN
                    && data.getType() == Constant.STORE_PAY_TYPE_ONLINE) { //扫码支付
                mGoodsInfoTitleText.setVisibility(View.GONE);
                mRecycler.setVisibility(View.GONE);
                mSpace.setVisibility(View.GONE);
                mTotalPriceTitleText.setText("实收");

            } else if (data.getOrderType() == Constant.STORE_ORDER_TYPE_CERIFICATION) { //核销


            }

            mConsumeAmountText.setText("¥ " + StringUtil.point2String(data.getTotalAmount()));
            mNoDiscountAmountText.setText("¥ " + StringUtil.point2String(data.getNoDiscountAmount()));
            mTotalPriceText.setText("¥ " + StringUtil.point2String(data.getRealTotalAmount()));
            mTableNumText.setText(data.getSeatName());

            switch (data.getPayChannel()) {
                case 1:
                    mPaywayText.setText("支付宝");
                    break;
                case 2:
                    mPaywayText.setText("微信");
                    break;
                case 3:
                    mPaywayText.setText("手Q");
                    break;
                case 4:
                    mPaywayText.setText("无需付款");
                    break;
                case 5:
                    mPaywayText.setText("线下收款");
                    break;
            }

            if (TextUtils.isEmpty(data.getTelephone())) {
                mUserAccountText.setText(data.getNickName());
            } else {
                mUserAccountText.setText(data.getTelephone());
            }

            mCloseDateText.setText(DatePickerUtil.getFormatDate(data.getPayTime(), "yyyy-MM.dd"));
            mCloseTimeText.setText(DatePickerUtil.getFormatDate(data.getPayTime(), "HH:mm"));
            mStartDateText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "yyyy-MM.dd"));
            mStartTimeText.setText(DatePickerUtil.getFormatDate(data.getStartTime(), "HH:mm"));
            mSellerNumText.setText(String.valueOf(data.getRepastId()));
            mOrderNumText.setText(String.valueOf(data.getId()));

            List<MerchantOrderItemEntity> dataList = data.getMerchantOrderItemDTOList();
            if (dataList == null || dataList.size() == 0) return;
            for (int i = 0; i < dataList.size(); i++) {
                MerchantOrderItemEntity item = dataList.get(i);
                if (item.getPrice() != null && item.getPrice() != 0) {
                    item.setItemType(StoreDetailAdapter.TYPE_DISCOUNT);
                } else {
                    item.setItemType(StoreDetailAdapter.TYPE_NO_DISCOUNT);
                }

            }
            mDataList.clear();
            mDataList.addAll(dataList);
        }

        mAdapter.notifyDataSetChanged();
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
    public void getStoreOrderDetailComplete(OrderDetailEntity data) {
        if (data == null) return;
        updateDetail(data);
    }

    @Override
    public void getStoreOrderDetailFailed() {

    }

    @Override
    public void cleanTableSuccess(Object data) {

    }

    @Override
    public void cleanTableFailed() {

    }
}
