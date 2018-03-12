package com.zishan.sardinemerchant.activity.page;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.CertificationConfirmEntity;
import com.example.wislie.rxjava.model.page.CouponProductEntity;
import com.example.wislie.rxjava.model.page.CouponResultEntity;
import com.example.wislie.rxjava.presenter.base.page.certification.ScanCodeResultPresenter;
import com.example.wislie.rxjava.view.base.page.certification.ScanCodeResultView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.ScanCodeResultAdapter;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫码核销结果
 */
public class ScanCodeResultActivity extends BActivity<ScanCodeResultView,
        ScanCodeResultPresenter> implements ScanCodeResultView {

    @BindView(R.id.cerification_recycler_view)
    RecyclerView mRecycler;

    private ScanCodeResultAdapter mAdapter;

    private ArrayList<CouponProductEntity> mDataList = new ArrayList<>();
    //券核销id
    private long coupon_id = 0;



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan_code_verification_result;
    }


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarDivderVisible(false);
    }

    @Override
    protected ScanCodeResultPresenter createPresenter() {
        return new ScanCodeResultPresenter(this, this);
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if(intent != null){
            coupon_id = intent.getLongExtra("coupon_id", coupon_id);
            ArrayList<CouponProductEntity> dataList = intent.getParcelableArrayListExtra("couponlist");
            if(dataList != null && dataList.size() > 0){
                mDataList.addAll(dataList);
            }
        }

        mAdapter = new ScanCodeResultAdapter(R.layout.item_scancode_result, mDataList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


    @OnClick({R.id.confirm_cerfication, R.id.not_cerification})
    public void onClick(View view) {
        switch (view.getId()) {
            //确认核销
            case R.id.confirm_cerfication:
             mPresenter.requestConfirmCertification(UserConfig.getInstance(
                     ClientApplication.getApp()).getStoreId(), coupon_id);
                break;
            //暂不核销
            case R.id.not_cerification:
                finish();
                break;
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

    @Override
    public void getScanCodeResultSuccess(CouponResultEntity data) {
        /*coupon_id = data.getCouponInstanceId();
        ArrayList<CouponProductEntity> products = data.getProducts();
        if(products != null){
            mAdapter.setNewData(products);
        }*/
    }

    @Override
    public void getScanCodeResultFailed() {
        Intent intent = new Intent(this, CerificationFailedActivity.class);
        startActivity(intent);
    }

    @Override
    public void confirmCertificationSuccess(CertificationConfirmEntity data) {
        if (data == null) return;
        //核销成功
        Intent intent = new Intent(this, CerificationSucceedActivity.class);
        ArrayList<CouponProductEntity> products = (ArrayList<CouponProductEntity>) mAdapter.getData();
        intent.putParcelableArrayListExtra("products", products);
        startActivity(intent);
    }


    @Override
    public void confirmCertificationFailed() {

    }
}
