package com.zishan.sardinemerchant.activity.personal.staffs;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.PermissionGroupsEntity;
import com.example.wislie.rxjava.model.page.PermissionIdsEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewNewAddPositionPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.staff.NewNewAddPositionView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新增职位
 */

public class NewNewAddPositionActivity extends BActivity<NewNewAddPositionView,
        NewNewAddPositionPresenter> implements NewNewAddPositionView, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.new_position_name)
    EditText mPositionName;
    @BindView(R.id.save)
    TextView mSave;
    @BindView(R.id.pay_bill)
    CheckBox mPayBill;//买单收银
    @BindView(R.id.gathering_switch)
    CheckBox mGatheringSwitch;//收款信息
    @BindView(R.id.red_bag_switch)
    CheckBox mRedBagSwitch;//红包核销
    @BindView(R.id.order_switch)
    CheckBox mOrderSwitch;//订单查看
    @BindView(R.id.message_management_switch)
    CheckBox mMessageManagementSwitch;//店铺信息管理
    @BindView(R.id.commodity_management_switch)
    CheckBox mCommodityManagementSwitch;//商品管理
    @BindView(R.id.marketing_management_switch)
    CheckBox mMarketingManagementSwitch;//营销管理
    @BindView(R.id.staff_management_switch)
    CheckBox mStaffManagementSwitch;//员工管理
    @BindView(R.id.operating_statistics_switch)
    CheckBox mOperatingStatisticsSwitch;//经营统计
    @BindView(R.id.pay_overview_switch)
    CheckBox mPayOverviewSwitch;//支付总览
    @BindView(R.id.sales_situation_switch)
    CheckBox mSalesSituationSwitch;//销量情况
    @BindView(R.id.bill_management)
    CheckBox mBillManagement;//账单管理
    @BindView(R.id.account_management_switch)
    CheckBox mAccountManagementSwitch;//账户管理
    @BindView(R.id.goods_list_management)
    CheckBox mGoodsListManagement;//商品列表管理
    @BindView(R.id.opening_table_switch)
    CheckBox mOpeningTableSwitch;//营业桌台管理
    private String positionName;

    //收银管理
    private boolean mPayBillCheckState = false;
    private boolean mGatheringSwitchState = false;
    private boolean mRedBagSwitchState = false;
    private boolean mOrderSwitchState = false;
    //店铺管理
    private boolean mMessageSwitchState = false;//店铺信息管理
    private boolean mCommoditySwitchState = false;//商品管理
    private boolean mMarketingSwitchState = false;//营销管理
    private boolean mStaffSwitchState = false;//员工管理
    //数据管理
    private boolean mOperatingSwitchState = false;//经营统计
    private boolean mPayOverviewSwitchState = false;//支付总览
    private boolean mSalesSituationSwitchState = false;//销量情况
    //财务管理
    private boolean mBillSwitchState = false;//账单管理
    private boolean mAccountSwitchState = false;//账户管理
    //操作管理
    private boolean mGoodsListSwitchState = false;//商品列表管理
    private boolean mOpeningTableSwitchState = false;//营业桌台管理


    List<PermissionIdsEntity> permissionIdsList = new ArrayList<>();//权限id集合

    @Override
    protected NewNewAddPositionPresenter createPresenter() {
        return new NewNewAddPositionPresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_new_add_position));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });

    }

    private void exitEdit() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认放弃此次编辑?", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                finish();
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
    public void onBackPressed() {
        exitEdit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_add_position;
    }

    @Override
    protected void initContentView() {
        SoftInputUtil.hideDirectly(this);
        mPositionName.addTextChangedListener(positionNameWatch);
        addCheckBoxListener();//监听所有权限的开关
    }

    private void addCheckBoxListener() {
        mPayBill.setOnCheckedChangeListener(this);
        mGatheringSwitch.setOnCheckedChangeListener(this);
        mRedBagSwitch.setOnCheckedChangeListener(this);
        mOrderSwitch.setOnCheckedChangeListener(this);
        mMessageManagementSwitch.setOnCheckedChangeListener(this);
        mCommodityManagementSwitch.setOnCheckedChangeListener(this);
        mMarketingManagementSwitch.setOnCheckedChangeListener(this);
        mStaffManagementSwitch.setOnCheckedChangeListener(this);
        mOperatingStatisticsSwitch.setOnCheckedChangeListener(this);
        mPayOverviewSwitch.setOnCheckedChangeListener(this);
        mSalesSituationSwitch.setOnCheckedChangeListener(this);
        mBillManagement.setOnCheckedChangeListener(this);
        mAccountManagementSwitch.setOnCheckedChangeListener(this);
        mGoodsListManagement.setOnCheckedChangeListener(this);
        mOpeningTableSwitch.setOnCheckedChangeListener(this);
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

    private TextWatcher positionNameWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !TextUtils.isEmpty(s)) {
                mSave.setEnabled(true);
                mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
            } else {
                mSave.setEnabled(false);
                mSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
            }
        }
    };

    @Override
    public void permissionSettingSuccess(List<PermissionGroupsEntity> dataList) {
        ToastUtil.show(this, "添加角色成功");
        finish();
    }

    @Override
    public void permissionSettingFailed() {

    }

    @OnClick({R.id.save, R.id.rl_pay_bill, R.id.rl_gathering, R.id.rl_red_bag, R.id.rl_order,
            R.id.rl_store_message_management, R.id.rl_commodity_management,
            R.id.rl_marketing_management, R.id.rl_staff_management, R.id.rl_rl_operating_statistics,
            R.id.rl_pay_overview, R.id.rl_sales_situation, R.id.rl_bill_management,
            R.id.rl_account_management, R.id.rl_goods_list_management, R.id.rl_opening_table_management})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save://确认
                positionName = mPositionName.getText().toString().trim();
                if (TextUtils.isEmpty(positionName)) {
                    ToastUtil.show(NewNewAddPositionActivity.this, "职位名称不能为空");
                    return;
                }
                showHintDialog();
                break;
            case R.id.rl_pay_bill://买单收银
                mPayBill.performClick();
                break;
            case R.id.rl_gathering://收款信息
                mGatheringSwitch.performClick();
                break;
            case R.id.rl_red_bag://红包核销

                mRedBagSwitch.performClick();
                break;
            case R.id.rl_order://订单查看
                mOrderSwitch.performClick();
                break;
            case R.id.rl_store_message_management://店铺信息管理
                mMessageManagementSwitch.performClick();
                break;
            case R.id.rl_commodity_management://商品管理
                mCommodityManagementSwitch.performClick();
                break;
            case R.id.rl_marketing_management://营销管理
                mMarketingManagementSwitch.performClick();
                break;
            case R.id.rl_staff_management://员工管理
                mStaffManagementSwitch.performClick();
                break;
            case R.id.rl_rl_operating_statistics:
                mOperatingStatisticsSwitch.performClick();
                break;
            case R.id.rl_pay_overview:
                mPayOverviewSwitch.performClick();
                break;
            case R.id.rl_sales_situation:
                mSalesSituationSwitch.performClick();
                break;
            case R.id.rl_bill_management:
                mBillManagement.performClick();
                break;
            case R.id.rl_account_management:
                mAccountManagementSwitch.performClick();
                break;
            case R.id.rl_goods_list_management:
                mGoodsListManagement.performClick();
                break;
            case R.id.rl_opening_table_management:
                mOpeningTableSwitch.performClick();
                break;
        }
    }


    private void showHintDialog() {
        ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.
                newInstance("确认添加该项职位？", null);
        confirmOrCancelDialog.showDialog(NewNewAddPositionActivity.this.getFragmentManager());
        confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                initPermissionIdsData();//获取角色权限集合id
                String permission_ids = GsonParser.parseObjToJson(permissionIdsList); //权限id集合
                Log.d("mej", "permissionId: ==" + permission_ids);
                mPresenter.PermissionSetting(UserConfig.getInstance(NewNewAddPositionActivity.this)
                        .getMerchantId(), null, positionName, permission_ids);//设置权限ids
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void initPermissionIdsData() {

        //mPayBillCheckState==true时,权限开关打开,反之关闭
        // //收银管理
        if (mPayBillCheckState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 1));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 1));//权限关闭
        }

        if (mGatheringSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 2));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 2));//权限关闭
        }

        if (mRedBagSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 3));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 3));//权限关闭
        }

        if (mOrderSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 4));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 4));//权限关闭
        }
        //店铺管理
        if (mMessageSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 5));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 5));//权限关闭
        }

        if (mCommoditySwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 6));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 6));//权限关闭
        }
        if (mMarketingSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 7));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 7));//权限关闭
        }
        if (mStaffSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 8));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 8));//权限关闭
        }
        //数据管理
        if (mOperatingSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 9));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 9));//权限关闭
        }
        if (mPayOverviewSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 10));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 10));//权限关闭
        }
        if (mSalesSituationSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 11));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 11));//权限关闭
        }
        //财务管理
        if (mBillSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 12));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 12));//权限关闭
        }
        if (mAccountSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 13));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 13));//权限关闭
        }
        //操作管理
        if (mGoodsListSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 14));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 14));//权限关闭
        }
        if (mOpeningTableSwitchState) {
            permissionIdsList.add(new PermissionIdsEntity(0, 15));//权限打开
        } else {
            permissionIdsList.add(new PermissionIdsEntity(1, 15));//权限关闭
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String tag = (String) buttonView.getTag();
        if (TextUtils.isEmpty(tag)) return;
        //收银管理
        if (tag.equals("payBill")) {
            mPayBillCheckState = isChecked;
        }
        if (tag.equals("gathering")) {
            mGatheringSwitchState = isChecked;
        }
        if (tag.equals("redBag")) {
            mRedBagSwitchState = isChecked;
        }

        if (tag.equals("order")) {
            mOrderSwitchState = isChecked;
        }

        //店铺管理

        if (tag.equals("messageManagement")) {
            mMessageSwitchState = isChecked;
        }
        if (tag.equals("commodityManagement")) {
            mCommoditySwitchState = isChecked;
        }
        if (tag.equals("marketingManagement")) {
            mMarketingSwitchState = isChecked;
        }

        if (tag.equals("staffManagement")) {
            mStaffSwitchState = isChecked;
        }

        //数据管理

        if (tag.equals("operatingStatistics")) {
            mOperatingSwitchState = isChecked;
        }
        if (tag.equals("payOverview")) {
            mPayOverviewSwitchState = isChecked;
        }
        if (tag.equals("salesSituation")) {
            mSalesSituationSwitchState = isChecked;
        }

        //财务管理
        if (tag.equals("billManagement")) {
            mBillSwitchState = isChecked;
        }
        if (tag.equals("accountManagement")) {
            mAccountSwitchState = isChecked;
        }

        //操作管理
        if (tag.equals("goodsList")) {
            mGoodsListSwitchState = isChecked;
        }
        if (tag.equals("openingTable")) {

            mOpeningTableSwitchState = isChecked;
        }

    }
}
