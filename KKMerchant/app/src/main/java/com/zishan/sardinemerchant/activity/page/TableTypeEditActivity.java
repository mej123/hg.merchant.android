package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableBoxEntity;
import com.example.wislie.rxjava.presenter.base.page.table_box.TableBoxTypeEditPresenter;
import com.example.wislie.rxjava.view.base.page.table_box.TableTypeEditView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;


/**
 * 桌台编辑(大厅 卡座 包厢)
 * Created by wislie on 2017/11/6.
 */

public class TableTypeEditActivity extends BActivity<TableTypeEditView, TableBoxTypeEditPresenter>
        implements TableTypeEditView {

    @BindView(R.id.table_name)
    EditText mTableNameText;

    @BindView(R.id.table_person_count)
    EditText mTablePersonCountText;

    private TableBoxEntity mTableBoxEntity;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_type_edit;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if(intent != null){
            mTableBoxEntity =  intent.getParcelableExtra("table_box");
        }

        setActionbarTitle("桌台编辑");
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        TextView deleteText = setActionBarMenuText(getString(R.string.delete));
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("你确定删除该桌台吗", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        if(mTableBoxEntity == null) return;
                        mPresenter.requestDeleteTableBox(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                mTableBoxEntity.getRepastLocationKey());

                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    protected TableBoxTypeEditPresenter createPresenter() {
        return new TableBoxTypeEditPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        if(mTableBoxEntity != null){
            mTableNameText.setText(mTableBoxEntity.getSeatName());
            mTablePersonCountText.setText(String.valueOf(mTableBoxEntity.getSeatNum()));
        }
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


    @OnClick({R.id.edit_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.edit_confirm:
                String seatName = mTableNameText.getText().toString();
                if(TextUtils.isEmpty(seatName)){
                    ToastUtil.show("桌台名称不能为空");
                    return;
                }
                String seatNum = mTablePersonCountText.getText().toString();
                if(TextUtils.isEmpty(seatNum)){
                    ToastUtil.show("桌台人数不能为空");
                    return;
                }
                Integer seatNumber = Integer.parseInt(seatNum);

                mPresenter.requestUpdateTableBox(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        mTableBoxEntity.getId(), null,
                        seatName, mTableBoxEntity.getType(), seatNumber);
                break;
        }
    }

    @Override
    public void updateTableBoxSuccess(TableBoxEntity data) {


        finish();
    }

    @Override
    public void updateTableBoxFailed() {

    }

    @Override
    public void deleteTableBoxSuccess(Object data) {
        finish();
    }


    @Override
    public void deleteTableBoxFailed() {

    }
}
