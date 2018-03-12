package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreEditPresenter;
import com.example.wislie.rxjava.view.base.personal.StoreEditView;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelTitleDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 营业状态
 * Created by wislie on 2018/1/16.
 */

public class StoreOpenStateActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {
    @BindView(R.id.open_state_icon)
    ImageView mStateIcon;

    @BindView(R.id.store_stop_icon)
    ImageView mStoreStopIcon;
    @BindView(R.id.state_click)
    TextView mStateText;

    @BindView(R.id.duration_layout_1)
    RelativeLayout mDruationFirstLayout;
    @BindView(R.id.duration_layout_2)
    RelativeLayout mDruationSecondLayout;

    @BindView(R.id.duration_1)
    TextView mDurationFirstText;//第一时间段
    @BindView(R.id.duration_2)
    TextView mDurationSecondText;//第二时间段

    private StoreMsgEntity mData;

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_open_state;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.operating_status));
        setActionBarHomeIcon(R.mipmap.back_black_icon);
        setActionBarMenuIcon(-1);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null){
            mData = (StoreMsgEntity) intent.getSerializableExtra("store_msg");
            updateState(mData);
        }
    }
    //2停业整顿，3开始营业，4封停，5开店中
    private void updateState(StoreMsgEntity data) {
        if(data == null) return;

        if (data.getState() == Constant.STORE_REPAIRED) { //停业整顿
            mStateText.setVisibility(View.VISIBLE);
            mStoreStopIcon.setVisibility(View.GONE);
            mStateIcon.setBackgroundResource(R.mipmap.store_repair_bg);
            mStateText.setText(getResources().getString(R.string.store_in));
        } else if (data.getState() == Constant.STORE_IN) { //营业中
            mStateText.setVisibility(View.VISIBLE);
            mStoreStopIcon.setVisibility(View.GONE);
            mStateIcon.setBackgroundResource(R.mipmap.store_in_bg);
            mStateText.setText(getResources().getString(R.string.store_repair));
        }else if (data.getState() ==  Constant.STORE_STOPPED) { //已被封停
            mStateText.setVisibility(View.GONE);
            mStoreStopIcon.setVisibility(View.VISIBLE);
            mStateIcon.setBackgroundResource(R.mipmap.store_stop_bg);
        }
        if(TextUtils.isEmpty(data.getFirstOpenTime()) && TextUtils.isEmpty(data.getFirstCloseTime())){
            mDruationFirstLayout.setVisibility(View.GONE);
        }else{
            mDruationFirstLayout.setVisibility(View.VISIBLE);
            mDurationFirstText.setText(data.getFirstOpenTime()+"至"+data.getFirstCloseTime());
        }

        if(TextUtils.isEmpty(data.getSecondOpenTime()) && TextUtils.isEmpty(data.getSecondCloseTime())){
            mDruationSecondLayout.setVisibility(View.GONE);
        }else{
            mDruationSecondLayout.setVisibility(View.VISIBLE);
            mDurationSecondText.setText(data.getSecondOpenTime()+"至"+data.getSecondCloseTime());
        }
    }

    @OnClick({R.id.state_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.state_click:
                if (mData.getState() == Constant.STORE_REPAIRED) { //确认是否开始营业

                    showStartOpenDialog();
                } else if (mData.getState() == Constant.STORE_IN) { //弹出停止营业对话框
                    showStopOpenDialog();
                }
                break;
        }
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }

    //开始营业
    private void showStartOpenDialog() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认恢复正常营业?", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                if(mData == null || mData.getStoreId() == null) return;
                mPresenter.editStoreMsg(mData.getStoreId(), null,
                        null, null, null, null, null, null, null, null, Constant.STORE_IN, null, null);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    //停止营业
    private void showStopOpenDialog() {
        ConfirmOrCancelTitleDialog dialog = ConfirmOrCancelTitleDialog.newInstance("修改状态",
                "当前处于营业状态\n确认将店铺转为暂停休息?");
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                if(mData == null || mData.getStoreId() == null) return;
                mPresenter.editStoreMsg(mData.getStoreId(), null,
                        null, null, null, null, null, null, null, null, Constant.STORE_REPAIRED, null, null);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
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
    public void editStoreMsgSuccess(StoreMsgEntity data) {

        //设置新的状态
        mData = data;
        updateState(data);
    }

    @Override
    public void editStoreMsgFailed() {

    }
}
