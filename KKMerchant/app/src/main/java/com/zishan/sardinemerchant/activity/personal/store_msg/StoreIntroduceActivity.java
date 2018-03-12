package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreEditPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreEditView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.view.CustomEditText;

/**
 * 店铺介绍
 * Created by wislie on 2017/11/23.
 */

public class StoreIntroduceActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {

    @BindView(R.id.custom_edit_text)
    CustomEditText mInputEdit;
    private String merchantDescription;
    private StoreMsgEntity storeMsgEntity;
    //门店id
    private Long store_id = 0l;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.store_introduce));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_introduce;
    }

    @Override
    protected void initContentView() {
        if (getIntent() == null) return;
        storeMsgEntity = (StoreMsgEntity) getIntent().getSerializableExtra("storeMsgEntity");
        if (storeMsgEntity == null) return;
        store_id = storeMsgEntity.getStoreId();
        merchantDescription = storeMsgEntity.getMerchantDescription();
        EditText inputEdit = mInputEdit.getInputEdit();
        inputEdit.setText(merchantDescription);
        inputEdit.setSelection(inputEdit.getText().toString().length());
    }

    @OnClick({R.id.confirm_introduce, R.id.left_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_introduce: //确定
                SoftInputUtil.hideDirectly(this);  //键盘缩回
                requestNetwork();
                break;
            case R.id.left_layout:
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                break;
        }
    }

    private void requestNetwork() {
        merchantDescription = mInputEdit.getInputEdit().getText().toString();
        if (TextUtils.isEmpty(merchantDescription)) {
            merchantDescription = "empty";
        }

        mPresenter.editStoreMsg(store_id, null,
                merchantDescription, null, null, null, null, null, null, null, null, null, null);
    }

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
    public void editStoreMsgSuccess(StoreMsgEntity data) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("merchantDescription", merchantDescription);
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this, "保存成功");
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }
}
