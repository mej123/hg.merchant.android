package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.AppointmentRemindInformDetailEntity;
import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.example.wislie.rxjava.model.page.OrderDishItemBean;
import com.example.wislie.rxjava.model.page.OrderDishesListBean;
import com.example.wislie.rxjava.presenter.base.page.advance_remind.AppointmentDetailPresenter;
import com.example.wislie.rxjava.view.base.page.advance_remind.AppointmentDetailView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.AppointmentDetailAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.DishOperationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;


/**
 * Created by yang on 2017/9/13.
 * <p>
 * 预约落座 未进店/已超时 预约通知详情
 */

public class AppointmentRemindDetailActivity extends BActivity<AppointmentDetailView,
        AppointmentDetailPresenter> implements AppointmentDetailView {
    @BindView(R.id.customer_name)
    TextView mCustomerNameText;
    @BindView(R.id.customer_phone)
    TextView mCustomerPhoneText;
    @BindView(R.id.meal_date)
    TextView mDateText;
    @BindView(R.id.meal_time)
    TextView mTimeText;
    @BindView(R.id.meal_num)
    TextView mPersonNumText;
    @BindView(R.id.meal_num_mark)
    TextView mPersonNumMarkText;
    @BindView(R.id.meal_num_desc)
    TextView mPersonNumDescText;
    @BindView(R.id.remark_msg)
    TextView mRemarkMsgText;
    @BindView(R.id.dish_num)
    TextView mDishNum;

    @BindView(R.id.appointment_detail_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.remark_detail_layout)
    LinearLayout mRemarkDetailLayout;
    @BindView(R.id.dish_remark_msg)
    TextView mDishRemarkMsgText;

    @BindView(R.id.table_arrange_confirm)
    TextView mArrangeConfirm;
    @BindView(R.id.table_accept_refuse_layout)
    LinearLayout mAcceptRefuseLayout;
    @BindView(R.id.table_refuse_layout)
    RelativeLayout mRefuseLayout;
    @BindView(R.id.operator_name)
    TextView mOperatorNameText;
    @BindView(R.id.operator_date)
    TextView mOperatorDateText;
    @BindView(R.id.operator_time)
    TextView mOperatorTimeText;
    @BindView(R.id.refuse_reason)
    TextView mRefuseReasonText;

    @BindView(R.id.detail_state)
    TextView mDetailStateText;

    private List<OrderDishItemBean> mDataList = new ArrayList<>();
    private AppointmentDetailAdapter mAdapter;
    private AppointmentRemindEntity mSeatData;
    //预约提醒的类型
    private int mType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_appointment_remind_unarrived;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.advace_order_detical));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected AppointmentDetailPresenter createPresenter() {
        return new AppointmentDetailPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if(intent != null){
            //落座数据
            mSeatData = (AppointmentRemindEntity) intent.getSerializableExtra("seat_data");
            mType = intent.getIntExtra("type", mType);
        }


        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AppointmentDetailAdapter(R.layout.item_table_advance_detail, mDataList);
        mRecycler.setAdapter(mAdapter);

        //初始化详情数据
        initData();

        if (mSeatData != null) {
            mPresenter.getAppointmentRemindDetail(mSeatData.getId());
        }
    }

    private void initData() {
        if (mSeatData != null) {
            mCustomerNameText.setText(mSeatData.getBookerName());
            mCustomerPhoneText.setText(mSeatData.getBookerPhone());
            mDateText.setText(DatePickerUtil.getFormatDate(mSeatData.getDinnerTime(), "yyyy-MM.dd"));
            mTimeText.setText(DatePickerUtil.getFormatDate(mSeatData.getDinnerTime(), "HH:mm"));
            mPersonNumText.setText(String.valueOf(mSeatData.getDinnerNum()));
            if (mSeatData.getNeedRoom() == 0) {
                mPersonNumDescText.setText("不需要包厢");
            } else if (mSeatData.getNeedRoom() == 1) {
                mPersonNumDescText.setText("需要包厢");
            }
            mRemarkMsgText.setText(String.valueOf(mSeatData.getRemarks()));
        }

        if (mType == Constant.APPOINTMENT_SEATED) {
            mArrangeConfirm.setVisibility(View.VISIBLE);
            mAcceptRefuseLayout.setVisibility(View.GONE);
            mRefuseLayout.setVisibility(View.GONE);
        } else if (mType == Constant.APPOINTMENT_INFORM) {
            mArrangeConfirm.setVisibility(View.GONE);
            mAcceptRefuseLayout.setVisibility(View.VISIBLE);
            mRefuseLayout.setVisibility(View.GONE);
        } else if (mType == Constant.APPOINTMENT_REFUSE) {
            mArrangeConfirm.setVisibility(View.GONE);
            mAcceptRefuseLayout.setVisibility(View.GONE);
            mRefuseLayout.setVisibility(View.VISIBLE);
            mDetailStateText.setVisibility(View.VISIBLE);
        }

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

    private List<OrderDishItemBean> getDishList(List<OrderDishesListBean> dataList) {
        List<OrderDishItemBean> itemList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            OrderDishesListBean data = dataList.get(i);

            List<OrderDishItemBean> items = data.getItems();
            if (items != null && items.size() != 0) {
                itemList.addAll(items);
            }
        }
        return itemList;
    }


    @OnClick({R.id.table_arrange_confirm, R.id.table_refuse_confirm, R.id.table_accept_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table_arrange_confirm:
                Intent intent = new Intent(this, TableArrangeActivity.class);
                intent.putExtra("seat_data", mSeatData);
                startActivity(intent);
                break;
            //拒绝
            case R.id.table_refuse_confirm:
                Intent refuseIntent = new Intent(this, FeedbackActivity.class);
                refuseIntent.putExtra("bespeak_id", mSeatData.getId());
                startActivity(refuseIntent);
                break;
            //接受
            case R.id.table_accept_confirm:
                ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.newInstance("确认接受该笔预约订单吗？", null);
                confirmOrCancelDialog.showDialog(getFragmentManager());
                confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.requestAcceptAppointmentRemindInform(UserConfig.getInstance(
                                ClientApplication.getApp()).getStoreId(), mSeatData.getId());
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void getAppointmentDetailSuccess(AppointmentRemindInformDetailEntity data) {


        mOperatorNameText.setText(data.getBookerName());
        mOperatorDateText.setText(DatePickerUtil.getFormatDate(data.getDinnerTime(), "MM月dd日"));
        mOperatorTimeText.setText(DatePickerUtil.getFormatDate(data.getDinnerTime(), "HH:mm"));
        mRefuseReasonText.setText("拒绝理由:" + data.getRemarks());

    }

    @Override
    public void getAppointmentDetailFailed() {

    }

    @Override
    public void getAppointmentDishList(List<OrderDishesListBean> dataList) {

        mDishNum.setText("(" + dataList.size() + "件)");
        if (dataList.size() != 0) {
            mRemarkDetailLayout.setVisibility(View.VISIBLE);
        }
        List<OrderDishItemBean> dishList = getDishList(dataList);
        if (dishList != null && dishList.size() > 0) {
            mAdapter.setNewData(dishList);
        }

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();


    }


    @Override
    public void acceptAppointmentInformSuccess(Object data) {

    }

    @Override
    public void acceptAppointmentInformFailed() {

    }


}
