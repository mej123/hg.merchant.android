package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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

/**
 * Created by yang on 2017/12/1.
 * <p>
 * 门店电话
 */

public class ContactWayActivity extends BActivity<StoreEditView, StoreEditPresenter> implements StoreEditView {

    @BindView(R.id.store_mobile_edit)
    EditText mStoreMobileEdit;
    private StoreMsgEntity storeMsgEntity;

    private Long store_id = 0l;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.contact_way));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected StoreEditPresenter createPresenter() {
        return new StoreEditPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contact_way;
    }

    @Override
    protected void initContentView() {
        if (getIntent() == null) return;
        storeMsgEntity = (StoreMsgEntity) getIntent().getSerializableExtra("storeMsgEntity");
        if (storeMsgEntity == null) return;
        store_id =  storeMsgEntity.getStoreId();
        String phone = getIntent().getStringExtra("phone");
        mStoreMobileEdit.setText(phone);
        mStoreMobileEdit.setSelection(mStoreMobileEdit.getText().toString().length());
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
        resultIntent.putExtra("phone", mStoreMobileEdit.getText().toString().trim());
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this, "保存成功");
        finish();
    }

    @Override
    public void editStoreMsgFailed() {

    }

    @OnClick(R.id.confirm)
    public void onViewClicked() {
        SoftInputUtil.hideDirectly(this);
        requestNetwork();
    }

    private void requestNetwork() {

        String telephone = mStoreMobileEdit.getText().toString();

        if (TextUtils.isEmpty(telephone)) {
            ToastUtil.show(this, "手机号不能为空");
            return;
        }

        mPresenter.editStoreMsg(store_id, telephone,
                null, null, null, null, null, null, null, null, null, null, null);
    }
}
