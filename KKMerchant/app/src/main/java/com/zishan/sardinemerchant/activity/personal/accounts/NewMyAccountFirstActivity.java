package com.zishan.sardinemerchant.activity.personal.accounts;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.AccountBalanceRecordEntity;
import com.example.wislie.rxjava.model.personal.BalanceRecordDetailsEntity;
import com.example.wislie.rxjava.presenter.personal.NewMyAccountFirstPresenter;
import com.example.wislie.rxjava.view.personal.NewMyAccountFirstView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.BalanceDetailListAdapter;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;

/**
 * Created by yang on 2018/1/9.
 * <p>
 * 个人  我的账户
 */

public class NewMyAccountFirstActivity extends BActivity<NewMyAccountFirstView,
        NewMyAccountFirstPresenter> implements NewMyAccountFirstView {
    @BindView(R.id.tv_left_money)
    TextView mLeftMoney;
    @BindView(R.id.account_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.withdraw_list_empty_layout)
    LinearLayout mWithdrawListEmptyLayout;
    //当前页
    private int mCurrentPage = 0;
    //每页的size
    private final int PAGE_SIZE = 10;
    private List<BalanceRecordDetailsEntity> mDataList = new ArrayList<>();
    private BalanceDetailListAdapter mAdapter;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.my_account));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected NewMyAccountFirstPresenter createPresenter() {
        return new NewMyAccountFirstPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_my_account_first;
    }

    @Override
    protected void initContentView() {
        mPresenter.getAccountBalance(UserConfig.getInstance(ClientApplication.getApp())
                .getMerchantId());//获取账户余额
        mPresenter.getAccountBalanceRecord(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                mCurrentPage, PAGE_SIZE, null, null, null);//获取余额明细
        mAdapter = new BalanceDetailListAdapter(R.layout.item_balance_detail_list, mDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //解决滑动卡顿  或设置mRecycleView.setNestedScrollingEnabled(false)效果一样
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.tv_withdraw, R.id.rl_more_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_withdraw:
                Skip.toActivity(this, NewMerchantMerchantWithdrawActivity.class);
                break;
            case R.id.rl_more_layout:
                Skip.toActivity(this, BalanceRecordActivity.class);
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
    public void getAccountBalanceSuccess(Long data) {
        if (data == null) return;
        mLeftMoney.setText(FormatMoneyUtil.pennyChangeDollar(data) + "元");//账户余额
    }

    @Override
    public void getAccountBalanceFailed() {

    }

    @Override
    public void getAccountBalanceRecordSuccess(AccountBalanceRecordEntity accountBalanceRecordEntity) {

        if (accountBalanceRecordEntity == null) return;
        List<BalanceRecordDetailsEntity> BalanceRecordDetailsList = accountBalanceRecordEntity.getContent();
        if (BalanceRecordDetailsList == null || BalanceRecordDetailsList.size() == 0) {
            mWithdrawListEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }

        mAdapter.setNewData(BalanceRecordDetailsList);
        mAdapter.notifyDataSetChanged();
        if (BalanceRecordDetailsList.size()>3){
            mAdapter.addFooterView(showFooterNoMoreData());
        }
    }

    @Override
    public void getAccountBalanceRecordFailed() {

    }
}
