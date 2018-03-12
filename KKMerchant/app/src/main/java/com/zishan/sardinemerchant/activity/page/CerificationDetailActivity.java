package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.example.wislie.rxjava.model.page.StoreOrderProductEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.CerificationGoodsInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 红包核销的详细信息
 * Created by wislie on 2017/11/3.
 */

public class CerificationDetailActivity extends BActivity {

    @BindView(R.id.cerification_date)
    TextView mDateText;
    @BindView(R.id.certification_time)
    TextView mTimeText;
    @BindView(R.id.user_info)
    TextView mUserInfoText;
    @BindView(R.id.cerification_person)
    TextView mCertificationPersonText;
    @BindView(R.id.order_num)
    TextView mOrderNumText;

    @BindView(R.id.goods_info_recycler_view)
    MaxRecyclerView mRecyclerView;

    private CerificationGoodsInfoAdapter mAdapter;

    private long useTimetamp;
    private String userMobile;
    private String oprName;
    private long orderId;
    private List<StoreOrderProductEntity> mDataList = new ArrayList<>();
    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cerification_detail;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle("详细信息");
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if(intent != null){
            useTimetamp = intent.getLongExtra("useTimetamp", useTimetamp);
            userMobile = intent.getStringExtra("userMobile");
            oprName = intent.getStringExtra("oprName");
            orderId = intent.getLongExtra("orderId", orderId);
            ArrayList<StoreOrderProductEntity> dataList = intent.getParcelableArrayListExtra("products");
            if(dataList != null){
                mDataList.addAll(dataList);
            }
            mDateText.setText(DatePickerUtil.getFormatDate(useTimetamp, "yyyy.MM.dd"));
            mTimeText.setText(DatePickerUtil.getFormatDate(useTimetamp, "HH:mm"));
            mUserInfoText.setText(StringUtil.formatPhoneNum(userMobile));
            mCertificationPersonText.setText(oprName);
            mOrderNumText.setText(String.valueOf(orderId));
        }

        mAdapter = new CerificationGoodsInfoAdapter(R.layout.item_certification_goods_info, mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
