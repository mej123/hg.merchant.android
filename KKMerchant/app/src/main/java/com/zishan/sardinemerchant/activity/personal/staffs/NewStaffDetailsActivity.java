package com.zishan.sardinemerchant.activity.personal.staffs;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.MyStaffEntity;
import com.example.wislie.rxjava.model.page.PermissionEntity;
import com.example.wislie.rxjava.model.page.PermissionGroupsEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewStaffDetailsPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.staff.NewStaffDetailsView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新 员工详情
 */

public class NewStaffDetailsActivity extends BActivity<NewStaffDetailsView, NewStaffDetailsPresenter>
        implements NewStaffDetailsView {

    @BindView(R.id.input_name)
    TextView mInputName;
    @BindView(R.id.et_input_phone)
    TextView mInputPhone;
    @BindView(R.id.tv_select_position)
    TextView mSelectPosition;
    @BindView(R.id.tv_select_belong_store)
    TextView mSelectBelongStore;
    public static final int REQUEST_CODE_POSITION_MANAGER = 0x01;
    public static final int REQUEST_CODE_BELONG_STORE = 0x02;
    //收银管理
    @BindView(R.id.rl_casher_management_layouts)
    RelativeLayout mCasherManagementLayouts;
    @BindView(R.id.rl_pay_bill_layout)
    RelativeLayout mPayBillLayout;
    @BindView(R.id.rl_gathering_layout)
    RelativeLayout mGatheringLayout;
    @BindView(R.id.rl_red_bag_layout)
    RelativeLayout mRedBagLayout;
    @BindView(R.id.rl_order_layout)
    RelativeLayout mOrderLayout;
    //店铺管理
    @BindView(R.id.rl_store_management_layouts)
    RelativeLayout mStoreManagementLayouts;
    @BindView(R.id.rl_store_message_management_layout)
    RelativeLayout mStoreMessageManagementLayout;
    @BindView(R.id.rl_commodity_management_layout)
    RelativeLayout mCommodityManagementLayout;
    @BindView(R.id.rl_marketing_management_layout)
    RelativeLayout mMarketingManagementLayout;
    @BindView(R.id.rl_staff_management_layout)
    RelativeLayout mStaffManagementLayout;
    //数据管理
    @BindView(R.id.rl_data_management_layouts)
    RelativeLayout mDataManagementLayouts;
    @BindView(R.id.rl_rl_operating_statistics_layout)
    RelativeLayout mRlOperatingStatisticsLayout;
    @BindView(R.id.rl_pay_overview_layout)
    RelativeLayout mPayOverviewLayout;
    @BindView(R.id.rl_sales_situation_layout)
    RelativeLayout mSalesSituationLayout;
    //财务管理
    @BindView(R.id.rl_financing_management_layouts)
    RelativeLayout mFinancingManagementLayouts;
    @BindView(R.id.rl_bill_management_layout)
    RelativeLayout mBillManagementLayout;
    @BindView(R.id.rl_account_management_layout)
    RelativeLayout mAccountManagementLayout;
    //操作管理
    @BindView(R.id.rl_operation_management_layouts)
    RelativeLayout mOperationManagementLayouts;
    @BindView(R.id.rl_goods_list_management_layout)
    RelativeLayout mGoodsListManagementLayout;
    @BindView(R.id.rl_opening_table_management_layout)
    RelativeLayout mOpeningTableManagementLayout;
    private String selectPosition;//选择职位
    private Long employeeId;
    private Long role_id;
    private String mStoreIds = null;//拼接好的storeIds
    List<PermissionGroupsEntity> mDataList = new ArrayList<>();

    private Boolean all_store = false;//是否选中所以集团下的门店  默认未选中
    private String name;
    private Boolean allStore;

    @Override
    protected NewStaffDetailsPresenter createPresenter() {
        return new NewStaffDetailsPresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_staff_details));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });
        setActionBarMenuIcon(-1);
        setActionBarMenuText(getString(R.string.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.
                        newInstance("确认删除该员工？", null);
                confirmOrCancelDialog.showDialog(NewStaffDetailsActivity.this.getFragmentManager());
                confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        //1.门店id 2.员工id
                        mPresenter.deleteStaff(UserConfig.getInstance(NewStaffDetailsActivity.this)
                                .getMerchantId(), employeeId);
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

    private void exitEdit() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("本次修改未保存,确定退出?", null);
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
    protected int getLayoutResId() {
        return R.layout.activity_new_staff_details;
    }

    @Override
    protected void initContentView() {
        mSelectPosition.addTextChangedListener(selectPositionWatch);

        MyStaffEntity myStaff = (MyStaffEntity) getIntent().getSerializableExtra("myStaff");
        if (myStaff == null) return;
        //角色id
        role_id = myStaff.getRole().getId();//员工列表跳转获取职位id
        mStoreIds = myStaff.getStoreIds();
        //是否归属所有门店
        allStore = myStaff.getAllStore();
        if (allStore) {
            mSelectBelongStore.setText("归属集团下所有店铺");//所属门店数量
        } else {
            if (!TextUtils.isEmpty(mStoreIds) && mStoreIds.length() > 0 && null != mStoreIds) {
                int storeNumber = showStoreNumber(mStoreIds);//根据storeIds包含的“,”,去判断当前的门店数量
                mSelectBelongStore.setText(storeNumber + "家店铺");
            } else {
                mSelectBelongStore.setText("无所属门店");
            }
        }
//        if (!TextUtils.isEmpty(mStoreIds) && mStoreIds.length() > 0 && null != all_store) {
//            if (allStore) {
//                mSelectBelongStore.setText("所有集团门店");//所属门店数量
//            } else {
//                int storeNumber = showStoreNumber(mStoreIds);//根据storeIds包含的“,”,去判断当前的门店数量
//                mSelectBelongStore.setText(storeNumber + "家店铺");//所属门店数量
//            }
//        } else {
//            mSelectBelongStore.setText("无所属门店");
//        }

        //员工id
        employeeId = myStaff.getId();
        mInputName.setText(myStaff.getRealName());
        mSelectPosition.setText(myStaff.getRole().getName());
        mInputPhone.setText(myStaff.getMobile());
    }

    private TextWatcher selectPositionWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                selectPosition = mSelectPosition.getText().toString().trim();
                //获取职位所属权限
                if (role_id == null) return;
                mPresenter.getPermissionList(UserConfig.getInstance(
                        NewStaffDetailsActivity.this).getMerchantId(), role_id);
            }
        }
    };

    @Override
    public void onBackPressed() {
        exitEdit();
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.rl_position, R.id.rl_belong_store, R.id.confirm_and_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_position://职位选择
                String currentPosition = mSelectPosition.getText().toString();
                Intent positionManagerIntent = new Intent(this, NewPositionSelectActivity.class);
                if (!TextUtils.isEmpty(currentPosition) && currentPosition.length() > 0) {
                    positionManagerIntent.putExtra("currentPosition", currentPosition);
                }
                startActivityForResult(positionManagerIntent, REQUEST_CODE_POSITION_MANAGER);

                break;
            case R.id.rl_belong_store://选择归属店铺
                Intent belongStoreIntent = new Intent(this, NewBelongStoreActivity.class);
                belongStoreIntent.putExtra("mStoreIds", mStoreIds);
                startActivityForResult(belongStoreIntent, REQUEST_CODE_BELONG_STORE);
                break;
            case R.id.confirm_and_save://确认
                //姓名
                name = mInputName.getText().toString().trim();
                selectPosition = mSelectPosition.getText().toString().trim();//职位
                String belongStore = mSelectBelongStore.getText().toString().trim();//归属店铺

                if (TextUtils.isEmpty(selectPosition)) {
                    ToastUtil.show(NewStaffDetailsActivity.this, "职位不能为空");
                    return;
                }
                save();
                break;
        }
    }

    private void save() {
        ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.newInstance
                ("是否添加该员工？", null);
        confirmOrCancelDialog.showDialog(NewStaffDetailsActivity.this.getFragmentManager());
        confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                if (all_store) {
                    //修改员工信息
                    mPresenter.updateStaff(UserConfig.getInstance(NewStaffDetailsActivity.this).getMerchantId(),
                            mStoreIds, name, role_id, employeeId, 1);
                } else {
                    //修改员工信息
                    mPresenter.updateStaff(UserConfig.getInstance(NewStaffDetailsActivity.this).getMerchantId(),
                            mStoreIds, name, role_id, employeeId, 0);
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_POSITION_MANAGER://职位选择
                String positionName = data.getStringExtra("positionName");//职位名称
                //职位id
                role_id = (Long) data.getSerializableExtra("positionId");
                mSelectPosition.setText(positionName);
                break;

            case REQUEST_CODE_BELONG_STORE://归属店铺
                ArrayList<String> mSelectStoreDataList = data.getStringArrayListExtra("mSelectStoreDataList");
                all_store = data.getBooleanExtra("switchAllStoreState", false);

                if (all_store) {
                    mSelectBelongStore.setText("归属集团下所有店铺");
                    if (mSelectStoreDataList == null || mSelectStoreDataList.size() == 0) {
                        mStoreIds = null;
                    } else {
                        mStoreIds = getStoreIds(mSelectStoreDataList);//拿到的所有选中拼接好的店铺StoreIds
                    }
                } else {
                    if (mSelectStoreDataList == null || mSelectStoreDataList.size() == 0) {
                        mSelectBelongStore.setText("无所属门店");
                        mStoreIds = null;
                    } else {
                        mSelectBelongStore.setText("已选" + mSelectStoreDataList.size() + "家店铺");
                        mStoreIds = getStoreIds(mSelectStoreDataList);//拿到的所有选中拼接好的店铺StoreIds
                    }
                }

//                if (mSelectStoreDataList == null || mSelectStoreDataList.size() == 0) {
//                    mSelectBelongStore.setText("无所属门店");
//                    mStoreIds = null;
//                }
//
//                if (mSelectStoreDataList.size() > 0) {
//                    if (all_store) {
//                        mSelectBelongStore.setText("归属集团下所有店铺");
//                        mStoreIds = getStoreIds(mSelectStoreDataList);//拿到的所有选中拼接好的店铺StoreIds
//                    } else {
//                        mSelectBelongStore.setText("已选" + mSelectStoreDataList.size() + "家店铺");
//                        mStoreIds = getStoreIds(mSelectStoreDataList);//拿到的所有选中拼接好的店铺StoreIds
//                    }
//                }

                break;
        }
    }

    public String getStoreIds(ArrayList<String> mSelectStoreDataList) {
        String storeIds = "";//清空上次请求的数据,防止重复拼接
        for (int i = 0; i < mSelectStoreDataList.size(); i++) {
            String storeId = mSelectStoreDataList.get(i) + ",";
            storeIds = storeIds + storeId;
        }
        return storeIds.substring(0, storeIds.length() - 1);
    }

    private int showStoreNumber(String storeIds) {
        int count = 0, start = 0;
        String sub = ",";
        while ((start = storeIds.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count + 1;
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
    public void updateStaffSuccess(Object object) {
        ToastUtil.show(this, "修改成功");
        finish();
    }

    @Override
    public void updateStaffFailed() {

    }

    @Override
    public void deleteStaffSuccess(Object object) {
        ToastUtil.show(this, "删除成功");
        finish();
    }

    @Override
    public void deleteStaffFailed() {

    }

    @Override
    public void getPermissionListSuccess(List<PermissionGroupsEntity> dataList) {
        if (dataList == null || dataList.size() == 0) return;
        mDataList.clear();
        mDataList.addAll(dataList);
        showOldPermissionView();//显示刚进来时的权限状态
    }

    private void showOldPermissionView() {
        for (int i = 0; i < mDataList.size(); i++) {
            PermissionGroupsEntity permissionGroupsEntity = mDataList.get(i);
            String name = permissionGroupsEntity.getName();
            if (TextUtils.isEmpty(name)) return;
            if (name.equals("收银管理")) {
                List<PermissionEntity> permissions = permissionGroupsEntity.getPermissions();
                if (permissions.size() == 0 || permissions == null) return;
                for (int j = 0; j < permissions.size(); j++) {
                    PermissionEntity permissionEntity = permissions.get(j);
                    Integer isSelect = permissionEntity.getIsSelected();
                    Long permissionId = permissionEntity.getId();
                    //买单收银
                    if (permissionId == 1) {
                        if (isSelect == 0) {
                            mPayBillLayout.setVisibility(View.VISIBLE);
                            mCasherManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mPayBillLayout.setVisibility(View.GONE);
                            mCasherManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //收款信息
                    if (permissionId == 2) {
                        if (isSelect == 0) {
                            mGatheringLayout.setVisibility(View.VISIBLE);
                            mCasherManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mGatheringLayout.setVisibility(View.GONE);
                            mCasherManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //红包核销
                    if (permissionId == 3) {
                        if (isSelect == 0) {
                            mRedBagLayout.setVisibility(View.VISIBLE);
                            mCasherManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mRedBagLayout.setVisibility(View.GONE);
                            mCasherManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //订单查看
                    if (permissionId == 4) {
                        if (isSelect == 0) {
                            mOrderLayout.setVisibility(View.VISIBLE);
                            mCasherManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mOrderLayout.setVisibility(View.GONE);
                            mCasherManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (name.equals("店铺管理")) {
                List<PermissionEntity> permissions = permissionGroupsEntity.getPermissions();
                if (permissions.size() == 0 || permissions == null) return;
                for (int j = 0; j < permissions.size(); j++) {
                    PermissionEntity permissionEntity = permissions.get(j);
                    Integer isSelect = permissionEntity.getIsSelected();
                    Long permissionId = permissionEntity.getId();
                    //店铺信息管理
                    if (permissionId == 5) {
                        if (isSelect == 0) {
                            mStoreMessageManagementLayout.setVisibility(View.VISIBLE);
                            mStoreManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mStoreMessageManagementLayout.setVisibility(View.GONE);
                            mStoreManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //商品管理
                    if (permissionId == 6) {
                        if (isSelect == 0) {
                            mCommodityManagementLayout.setVisibility(View.VISIBLE);
                            mStoreManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mCommodityManagementLayout.setVisibility(View.GONE);
                            mStoreManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //营销管理
                    if (permissionId == 7) {
                        if (isSelect == 0) {
                            mMarketingManagementLayout.setVisibility(View.VISIBLE);
                            mStoreManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mMarketingManagementLayout.setVisibility(View.GONE);
                            mStoreManagementLayouts.setVisibility(View.GONE);

                        }
                    }
                    //员工管理
                    if (permissionId == 8) {
                        if (isSelect == 0) {
                            mStaffManagementLayout.setVisibility(View.VISIBLE);
                            mStoreManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mStaffManagementLayout.setVisibility(View.GONE);
                            mStoreManagementLayouts.setVisibility(View.GONE);
                        }
                    }

                }
            } else if (name.equals("数据管理")) {
                List<PermissionEntity> permissions = permissionGroupsEntity.getPermissions();
                if (permissions.size() == 0 || permissions == null) return;
                for (int j = 0; j < permissions.size(); j++) {
                    PermissionEntity permissionEntity = permissions.get(j);
                    Integer isSelect = permissionEntity.getIsSelected();
                    Long permissionId = permissionEntity.getId();
                    //经营统计
                    if (permissionId == 9) {
                        if (isSelect == 0) {
                            mRlOperatingStatisticsLayout.setVisibility(View.VISIBLE);
                            mDataManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mRlOperatingStatisticsLayout.setVisibility(View.GONE);
                            mDataManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //支付总览
                    if (permissionId == 10) {
                        if (isSelect == 0) {
                            mPayOverviewLayout.setVisibility(View.VISIBLE);
                            mDataManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mPayOverviewLayout.setVisibility(View.GONE);
                            mDataManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //销量情况
                    if (permissionId == 11) {
                        if (isSelect == 0) {
                            mSalesSituationLayout.setVisibility(View.VISIBLE);
                            mDataManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mSalesSituationLayout.setVisibility(View.GONE);
                            mDataManagementLayouts.setVisibility(View.GONE);
                        }
                    }

                }
            } else if (name.equals("财务管理")) {
                List<PermissionEntity> permissions = permissionGroupsEntity.getPermissions();
                if (permissions.size() == 0 || permissions == null) return;
                for (int j = 0; j < permissions.size(); j++) {
                    PermissionEntity permissionEntity = permissions.get(j);
                    Integer isSelect = permissionEntity.getIsSelected();
                    Long permissionId = permissionEntity.getId();

                    ////账单管理
                    if (permissionId == 12) {
                        if (isSelect == 0) {
                            mBillManagementLayout.setVisibility(View.VISIBLE);
                            mFinancingManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mBillManagementLayout.setVisibility(View.GONE);
                            mFinancingManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //账户管理
                    if (permissionId == 13) {
                        if (isSelect == 0) {
                            mAccountManagementLayout.setVisibility(View.VISIBLE);
                            mFinancingManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mAccountManagementLayout.setVisibility(View.GONE);
                            mFinancingManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (name.equals("操作管理")) {
                List<PermissionEntity> permissions = permissionGroupsEntity.getPermissions();
                if (permissions.size() == 0 || permissions == null) return;
                for (int j = 0; j < permissions.size(); j++) {
                    PermissionEntity permissionEntity = permissions.get(j);
                    Integer isSelect = permissionEntity.getIsSelected();
                    Long permissionId = permissionEntity.getId();
                    //商品列表管理
                    if (permissionId == 14) {
                        if (isSelect == 0) {
                            mGoodsListManagementLayout.setVisibility(View.VISIBLE);
                            mOperationManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mGoodsListManagementLayout.setVisibility(View.GONE);
                            mOperationManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                    //营业桌台管理
                    if (permissionId == 15) {
                        if (isSelect == 0) {
                            mOpeningTableManagementLayout.setVisibility(View.VISIBLE);
                            mOperationManagementLayouts.setVisibility(View.VISIBLE);
                        } else {
                            mOpeningTableManagementLayout.setVisibility(View.GONE);
                            mOperationManagementLayouts.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getPermissionListFailed() {
    }
}
