package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.EnabledBankCardEntity;
import com.example.wislie.rxjava.model.personal.HandingBankCardEntity;
import com.example.wislie.rxjava.model.personal.LastWithdrawEntity;
import com.example.wislie.rxjava.model.personal.LastWithdrawRecordEntity;
import com.example.wislie.rxjava.model.personal.MyAccountFirstEntity;
import com.example.wislie.rxjava.presenter.personal.MerchantWithdrawPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.MerchantWithdrawView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.LastWithdrawRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 商户提现
 */

public class NewMerchantMerchantWithdrawActivity extends BActivity<MerchantWithdrawView,
        MerchantWithdrawPresenter> implements MerchantWithdrawView {

    @BindView(R.id.tv_arrive_account)
    TextView mArriveAccount;
    @BindView(R.id.tv_enable_account)
    TextView mEnableAccount;
    @BindView(R.id.tv_hint_text)
    TextView mHintText;
    @BindView(R.id.et_input_withdraw_money_sum)
    EditText mWithdrawMoneySum;
    @BindView(R.id.last_record_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_withdraw)
    TextView mWithdraw;
    @BindView(R.id.tv_beyond_quota)
    TextView mBeyondQuota;
    @BindView(R.id.tv_bank_card_withdraw_state)
    TextView mBankCardWithdrawState;
    @BindView(R.id.iv_bank_icon)
    ImageView mBankIcon;
    @BindView(R.id.withdraw_state)
    TextView mWithdrawState;
    @BindView(R.id.withdraw_layout)
    RelativeLayout mWithdrawLayout;
    @BindView(R.id.rl_bind_bank_card_layout)
    RelativeLayout mBindBankCardLayout;
    private Long limitTakeCashAmount;
    private HandingBankCardEntity handingBankCard;
    private EnabledBankCardEntity enabledBankCard;
    private String bindBankCardNo;//银行卡号
    private List<LastWithdrawEntity> mDataList = new ArrayList<>();
    private String bindBankType;//银行类型
    private String BankTypeCode;//银行编码
    private Long balance;//余额
    private String IsWithdrawIng;//是否有正在提现
    private int withdrawStatus;//最近提现的状态
    //当前页
    private int mCurrentPage = 0;
    //每页的size
    private final int PAGE_SIZE = 10;
    private LastWithdrawRecordAdapter mAdapter;
    private String bankName;
    private String lastFour;
    private int mStatus;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_merchant_withdraw;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.withdraw));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected MerchantWithdrawPresenter createPresenter() {
        return new MerchantWithdrawPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        mBindBankCardLayout.setEnabled(false);
        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
            mWithdrawMoneySum.setEnabled(false);
            mBindBankCardLayout.setEnabled(false);
            ToastUtil.show(this, "网络不可用");
        } else {
            initData();
            initRecycleView();
        }
    }

    private void initData() {
        mPresenter.lookAccountAndNewestRecord(UserConfig.getInstance(this).getStoreId());
        mWithdrawMoneySum.addTextChangedListener(mInPutMoney);
    }

    private void initRecycleView() {
        mAdapter = new LastWithdrawRecordAdapter(R.layout.item_last_withdraw_record, mDataList, this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(NewMerchantMerchantWithdrawActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setNestedScrollingEnabled(false);//禁止recycleView滑动,解决滑动卡顿
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LastWithdrawEntity lastWithdrawEntity = mAdapter.getItem(position);
                if (lastWithdrawEntity == null) return;
                //审批状态:   1未审批 2审批中 3审批结束 4审批失败 5不可用
                Integer approvalState = lastWithdrawEntity.getApprovalState();
                Integer payState = lastWithdrawEntity.getPayState();
                if (payState == 4) {
                    withdrawStatus = 3;//3:打款失败
                }
                if (approvalState == 2) {
                    withdrawStatus = 1;//1:审批中
                } else if (approvalState == 3) {
                    if (payState == 2) {
                        withdrawStatus = 4;//4:已打款
                        return;
                    }
                    withdrawStatus = 2;//2:打款成功
                } else if (approvalState == 4) {
                    withdrawStatus = 5;//5:审核失败
                }
                Intent WithdrawListIntent = new Intent();
                WithdrawListIntent.setClass(NewMerchantMerchantWithdrawActivity.this, WithdrawDetailParticularsActivity.class);
                Bundle bundle = new Bundle();
                Long num = lastWithdrawEntity.getNum();
                bundle.putString("number", FormatMoneyUtil.pennyChangeDollar(num));//提现金额
                bundle.putInt("withdrawStatus", withdrawStatus);//最近提现状态
                bundle.putLong("withdrawTime", lastWithdrawEntity.getWithdrawTime());//提现时间
                bundle.putLong("payTime", lastWithdrawEntity.getStartTime());//打款时间
                bundle.putString("withdrawBank", lastWithdrawEntity.getBankTypeCode());//提现银行
                bundle.putLong("flowId", lastWithdrawEntity.getInsideFlowId());//流水编号
                bundle.putString("bankCardNo", lastWithdrawEntity.getBankCardNo());
                bundle.putString("remark", lastWithdrawEntity.getFailureReason());
                WithdrawListIntent.putExtra("lastWithdraw", bundle);
                startActivity(WithdrawListIntent);
            }
        });

        mPresenter.getLastWithdrawRecord(UserConfig.getInstance(this).getMerchantId(), mCurrentPage,
                PAGE_SIZE, null, null, null, null);

    }


    private TextWatcher mInPutMoney = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!TextUtils.isEmpty(s) && s.length() > 0) {

                StringUtil.pointDigitLimited(s, 2);

                mWithdrawMoneySum.setTextSize(30);
                mWithdraw.setBackgroundResource(R.drawable.login_new_confirm_blue_bg);

                long mWithdrawAmount = StringUtil.String2Long(s.toString(), 2, true);//用户输入金额(Long)
                if (mWithdrawAmount > balance) {
                    mBeyondQuota.setVisibility(View.VISIBLE);//提现金额超限
                    //超过提现金额,提现按钮灰色,不可点击
                } else {
                    mBeyondQuota.setVisibility(View.GONE);
                }
                mWithdraw.setEnabled(true);

            } else {
                mWithdrawMoneySum.setTextSize(12);
                mWithdraw.setBackgroundResource(R.drawable.login_new_confirm_bg);
                mWithdraw.setEnabled(false);
            }
        }
    };

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
    public void merchantWithdrawComplete(Object object) {
        Intent intent = new Intent();
        intent.setClass(this, NewWithdrawResultActivity.class);//提现成功界面
        intent.putExtra("withdraw", "withdraw");
        startActivity(intent);
        NewMerchantMerchantWithdrawActivity.this.finish();
    }

    @Override
    public void merchantWithdrawFailed() {

    }

    @Override
    public void getLastWithdrawRecordSuccess(LastWithdrawRecordEntity lastWithdrawRecordEntity) {

        if (lastWithdrawRecordEntity == null) return;
        List<LastWithdrawEntity> content = lastWithdrawRecordEntity.getContent();
        if (content == null || content.size() == 0) return;
        //根据提现记录的最后一条记录去判断当前是否有未完成的提现申请
        LastWithdrawEntity lastWithdrawEntity = content.get(0);
        Integer approvalState = lastWithdrawEntity.getApprovalState();
        Integer payState = lastWithdrawEntity.getPayState();

        if (approvalState == 1 || approvalState == 2) {
            commonShowMsg();
        }
        if (approvalState == 3 && (payState == 1) || payState == 2) {
            commonShowMsg();
        }
        mAdapter.setNewData(content);
        mAdapter.notifyDataSetChanged();

        if (content.size()>5){
            mAdapter.addFooterView(showFooterNoMoreData());
        }
    }

    private void commonShowMsg() {
        mWithdrawState.setText("正在进行提现中,当前不可提现");
        IsWithdrawIng = "withdrawing";
        mWithdrawLayout.setVisibility(View.GONE);
        mWithdrawState.setVisibility(View.VISIBLE);
        mWithdraw.setBackgroundResource(R.drawable.login_new_confirm_bg);
        mWithdraw.setEnabled(false);
    }

    @Override
    public void getLastWithdrawRecordFailed() {
    }

    @Override
    public void lookAccountAndNewestRecordComplete(MyAccountFirstEntity myAccountFirstEntity) {
        if (myAccountFirstEntity == null) return;
        balance = myAccountFirstEntity.getBalance();
        limitTakeCashAmount = myAccountFirstEntity.getLimitTakeCashAmount(); //最小提现金额
        enabledBankCard = myAccountFirstEntity.getEnabledBankCard();  //有可用的卡
        handingBankCard = myAccountFirstEntity.getHandingBankCard(); //有正在处理中的卡

        if (handingBankCard != null) {
            bindBankCardNo = handingBankCard.getBankCardNo();
            BankTypeCode = handingBankCard.getBankTypeCode();
            mBindBankCardLayout.setEnabled(true);
            Integer status = handingBankCard.getStatus();
            mStatus = Integer.valueOf(status).intValue();
            if (status == 1) {
                mBankCardWithdrawState.setText("审核中");
                mWithdrawState.setText("银行卡绑定审核中,当前不可提现");
                mWithdraw.setBackgroundResource(R.drawable.login_new_confirm_bg);
                mWithdraw.setEnabled(false);
            } else if (status == 2) {
                mBankCardWithdrawState.setText("审核失败");
                mWithdrawState.setText("银行卡绑定审核失败,当前不可提现");
                mWithdraw.setBackgroundResource(R.drawable.login_new_confirm_bg);
                mWithdraw.setEnabled(false);
            }
        } else if (enabledBankCard != null) {
            mBindBankCardLayout.setEnabled(true);
            BankTypeCode = enabledBankCard.getBankTypeCode();//银行卡类别代码
            bindBankType = enabledBankCard.getBankTypeCode();//银行卡类型
            bindBankCardNo = enabledBankCard.getBankCardNo();//银行卡号
        } else {
            mWithdrawMoneySum.setEnabled(false);
            mBindBankCardLayout.setEnabled(false);
            return;
        }

        lastFour = bindBankCardNo.substring(bindBankCardNo.length() - 4, bindBankCardNo.length());
        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            if (BankTypeCode.equals(String.valueOf(bankCode))) {
                bankName = bankNameList[i];
                mArriveAccount.setText(bankName + "(" + lastFour + ")");
            }
        }

        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            if (bindBankType.equals(String.valueOf(bankCode))) {
                int bankIcon = imageBankList[i];
                mBankIcon.setImageResource(bankIcon);//设置银行图标
            }
        }
        String mLimitTakeCashAmount = FormatMoneyUtil.pennyChangeDollar(limitTakeCashAmount);//最小提现金额
        String mBalance = FormatMoneyUtil.pennyChangeDollar(balance);//可用余额
        mHintText.setText("注：\t最低额度" + mLimitTakeCashAmount + "元,最高不可超过可提取金额");
        mEnableAccount.setText("可提现金额：\t\t¥" + mBalance);//可提现金额

    }

    @Override
    public void lookAccountAndNewestRecordFailed() {

    }

    @OnClick({R.id.rl_bind_bank_card_layout, R.id.tv_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_bind_bank_card_layout:
                Intent intent = new Intent();
                intent.setClass(this, MyBankCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bindBankType", bindBankType);//卡类型
                bundle.putInt("bindBankState", mStatus);//绑定状态
                bundle.putString("bindBankCardNo", bindBankCardNo);//银行卡号
                bundle.putString("IsWithdrawIng", IsWithdrawIng);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                break;
            case R.id.tv_withdraw:
                String withdrawAmount = mWithdrawMoneySum.getText().toString();
                if (TextUtils.isEmpty(withdrawAmount)) {
                    ToastUtil.show(this, "提现金额不能为空");
                    return;
                }

                long mWithdrawAmount = StringUtil.String2Long(withdrawAmount, 2, true);//用户输入金额(Long)

                if (mWithdrawAmount < limitTakeCashAmount) {
                    ToastUtil.show(this, "最低提现金额10元");
                    return;
                }
                if (mWithdrawAmount > balance) {
                    ToastUtil.show(this, "大于可提现金额");
                    mBeyondQuota.setVisibility(View.VISIBLE);
                    return;
                } else {
                    mBeyondQuota.setVisibility(View.GONE);
                }
                mPresenter.merchantWithdraw(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(), mWithdrawAmount);
                break;
        }
    }

    int[] imageBankList = new int[]{R.mipmap.bank_icbc_icon, R.mipmap.bank_abc_icon,
            R.mipmap.bank_bc_icon, R.mipmap.bank_ccb_icon, R.mipmap.bank_bocom_icon,
            R.mipmap.bank_ecitic_icon, R.mipmap.bank_ceb_icon, R.mipmap.bank_hxb_icon,
            R.mipmap.bank_cmbc_icon, R.mipmap.bank_cgb_icon, R.mipmap.bank_cmb_icon
            , R.mipmap.bank_cib_icon, R.mipmap.bank_spdb_icon, R.mipmap.bank_hsb_icon,
            R.mipmap.china_post_icon};

    String[] bankNameList = new String[]{"中国工商银行", "中国农业银行", "中国银行", "中国建设银行",
            " 交通银行", "中信银行", "中国光大银行", "华夏银行", "中国民生银行", " 广东发展银行",
            "招商银行", "兴业银行", "上海浦东发展银行", "徽商银行", "中国邮政储蓄银行",
    };

    final int[] bankCodeList = new int[]{102, 103, 104, 105, 301, 302,
            303, 304, 305, 306, 308, 309, 310, 319, 403};
}
