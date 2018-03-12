package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreEditPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreEditView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftasbase.common.util.StringUtil;

import static top.ftas.ftasbase.common.util.FormatMoneyUtil.pennyChangeDollar;

/**
 * Created by yang on 2018/1/17.
 * <p>
 * 消费管理 修改
 */

public class NewConsumeManageActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {

    @BindView(R.id.setting_average_amount)
    EditText mAverageAmountEdit;
    @BindView(R.id.setting_min_amount)
    EditText mMinAmountEdit;
    private StoreMsgEntity storeMsgEntity;
    private String averageCount;
    private String minCount;
    private String mConsumption = "0";
    private String mMixConsumption = "0";

    private Long store_id = 0l;
    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_black_icon);
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.consume_manage));
        setActionBarMenuIcon(-1);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_consume_manage;
    }

    @Override
    protected void initContentView() {
        if(getIntent() == null) return;
        storeMsgEntity = (StoreMsgEntity) getIntent().getSerializableExtra("storeMsgEntity");
        if (storeMsgEntity == null) return;
        store_id = storeMsgEntity.getStoreId();
        Long consumption = storeMsgEntity.getConsumption();
        Long mixConsumption = storeMsgEntity.getMixConsumption();
        store_id = storeMsgEntity.getStoreId();
        if (consumption != 0) {
            mAverageAmountEdit.setText(pennyChangeDollar((long) consumption));
            mAverageAmountEdit.setSelection(mAverageAmountEdit.getText().toString().length());
        }

        if (mixConsumption != 0) {
            mMinAmountEdit.setText(pennyChangeDollar((long) mixConsumption));
            mMinAmountEdit.setSelection(mMinAmountEdit.getText().toString().length());
        }
        mAverageAmountEdit.addTextChangedListener(new CustomTextWatcher());
        mMinAmountEdit.addTextChangedListener(new CustomTextWatcher());
    }

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
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

        Intent resultIntent = new Intent();
        resultIntent.putExtra("averageCount", averageCount);
        resultIntent.putExtra("minCount", minCount);
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this, "保存成功");
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }

    private class CustomTextWatcher implements TextWatcher {
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
            }
        }
    }

    @OnClick(R.id.confirm)
    public void onViewClicked() {

        SoftInputUtil.hideDirectly(this);//键盘缩回
        averageCount = mAverageAmountEdit.getText().toString();  //人均消费
        minCount = mMinAmountEdit.getText().toString(); //最低消费

        //限制用户不能输入0元
        if (averageCount.equals("0") || minCount.equals("0")) {
            ToastUtil.show(this, "输入金额不能为零");
            return;
        }

        if (!TextUtils.isEmpty(averageCount) && averageCount.length() > 0) {
            mConsumption = String.valueOf(StringUtil.String2Long(averageCount, 2, true));
        }

        if (!TextUtils.isEmpty(minCount) && minCount.length() > 0) {
            mMixConsumption = String.valueOf(StringUtil.String2Long(minCount, 2, true));
        }

        mPresenter.editStoreMsg(store_id, null,
                null, mConsumption, mMixConsumption, null, null, null, null, null, null, null, null);
    }
}
