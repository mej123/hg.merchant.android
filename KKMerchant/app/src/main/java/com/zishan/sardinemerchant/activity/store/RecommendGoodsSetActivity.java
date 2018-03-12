package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsListPresenter;
import com.example.wislie.rxjava.view.base.store.goods.GoodsListView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yang on 2017/9/30.
 * <p>
 * 推荐菜品设置
 */

public class RecommendGoodsSetActivity extends BActivity<GoodsListView, GoodsListPresenter> implements GoodsListView {
    @BindView(R.id.recommend_total_count)
    TextView mRecommendTotalCount;
    @BindView(R.id.recommend_count)
    TextView mRecommendCount;
    @BindView(R.id.recommend_switch)
    ImageView mRecommendSwitch;
    @BindView(R.id.recommend_number)
    TextView mRecommendNum;

    private boolean mIsRecommend;
    //初始状态时是否推荐
    private boolean mInitedRecommend;
    //每页的size
    private final int PAGE_SIZE = 20;

    //选中的数量
    private int mRecommendSelectedCount = 0;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_goods_setting;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.recommend_goods_set));
        ImageView recommendImageView = setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        recommendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isRecommend", mIsRecommend);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            mIsRecommend = intent.getBooleanExtra("isRecommend", mIsRecommend);
            mInitedRecommend = intent.getBooleanExtra("initedRecommend", mInitedRecommend);
        }
        mRecommendSwitch.setImageResource(mIsRecommend ? R.mipmap.switch_on : R.mipmap.switch_off);
        initData();
    }

    //更新推荐数量
    private void updateRecommendCount() {
        if (!mInitedRecommend) {
            if (mIsRecommend) {
                mRecommendCount.setText(String.valueOf(mRecommendSelectedCount + 1));
                return;
            }
        }else{
            if (!mIsRecommend) {
                mRecommendCount.setText(String.valueOf(mRecommendSelectedCount - 1));
                return;
            }

        }
        mRecommendCount.setText(String.valueOf(mRecommendSelectedCount));
    }

    private void initData() {
        showProgressDialog();
        mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), 0,
                PAGE_SIZE, null, null, true, null, null, true);
    }

    @Override
    protected GoodsListPresenter createPresenter() {
        return new GoodsListPresenter(this, this);
    }


    @OnClick({R.id.recommend_switch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_switch:

                if (mIsRecommend) {
                    mRecommendSwitch.setImageResource(R.mipmap.switch_off);
                } else {
                    if(mRecommendSelectedCount >= 20){
                        ToastUtil.show("推荐数量不能超过20");
                        return;
                    }
                    mRecommendSwitch.setImageResource(R.mipmap.switch_on);
                }
                mIsRecommend = !mIsRecommend;
                updateRecommendCount();
                break;
        }

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isRecommend", mIsRecommend);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
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
    public void getGoodsFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void newGoodsList(ArrayList<ProductEntity> dataList) {

    }

    @Override
    public void addGoodsList(ArrayList<ProductEntity> dataList) {

    }

    @Override
    public void showCompleteAllData() {

    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mRecommendSelectedCount = totalElements;
        updateRecommendCount();
    }

    @Override
    public void addGoodsGroups(ArrayList<ProductGroupEntity> dataList) {

    }

    @Override
    public void getPermissionFailed() {

    }
}
