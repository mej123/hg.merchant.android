package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableBlanceEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.BlancePresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableCleanView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.dialog.ConfirmContentDialog;

import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 结算页面
 * Created by wislie on 2017/12/6.
 */

public class BlanceActivity extends BActivity<TableCleanView, BlancePresenter>
        implements TableCleanView {

    //就餐id
    private long repast_id = 0;
    //订单id
    private String order_id;
    //总结算金额
    private long totalAmounts;
    //桌台id
    private long table_id = 0;
    @Override
    protected BlancePresenter createPresenter() {
        return new BlancePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionbarTitle(getString(R.string.blance));
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if(intent != null){
            repast_id = intent.getLongExtra("repast_id", repast_id);
            order_id = intent.getStringExtra("order_id");
            table_id = intent.getLongExtra("table_id", table_id);
        }
        loadData();
    }

    private void loadData(){
        //获取结算金额
        mPresenter.getTableBlance(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), repast_id);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.clean_layout, R.id.blance_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //清台
            case R.id.clean_layout:
                showCleanTable();
                break;

            //结算
            case R.id.blance_layout:
                Intent recipentIntent = new Intent(this,
                        BalanceRecipientActivity.class);
                recipentIntent.putExtra("order_id", order_id);
                recipentIntent.putExtra("totalAmounts", totalAmounts);
                recipentIntent.putExtra("repast_id", repast_id);
                recipentIntent.putExtra("table_id", table_id);
                startActivity(recipentIntent);
                break;
        }
    }

    //清台
    private void showCleanTable() {
        ConfirmContentDialog dialog = ConfirmContentDialog.newInstance("清台",
                "确认清台后该桌台将恢复接待客户功能，并清空之前的消费信息?", true);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                //清台的具体操作
                mPresenter.cleanTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        repast_id);
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
    public void cleanTableSuccess(Object data) {
        ActivityManager.getInstance().finishAllActivityExcept(MainPageActivity.class, RepastManageActivity.class);
    }

    @Override
    public void cleanTableFailed() {

    }

    @Override
    public void getTableBlanceSuccess(TableBlanceEntity data) {
        totalAmounts = data.getTotalAmounts();
    }

    @Override
    public void getTableBlanceFailed() {

    }
}
