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
import com.example.wislie.rxjava.model.page.PermissionEntity;
import com.example.wislie.rxjava.model.page.PermissionGroupsEntity;
import com.example.wislie.rxjava.model.page.PermissionIdsEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewPositionDetailPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.staff.NewPositionDetailView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.PositionLookSaveDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

import static com.zishan.sardinemerchant.R.id.new_position_name;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新 职位详情
 */

public class NewPositionDetailsActivity extends BActivity<NewPositionDetailView,
        NewPositionDetailPresenter> implements NewPositionDetailView, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_position_name)
    TextView mPositionName;
    @BindView(new_position_name)
    EditText mNewPositionName;
    //收银管理
    @BindView(R.id.pay_bill)
    CheckBox mPayBill;
    @BindView(R.id.gathering_switch)
    CheckBox mGatheringSwitch;
    @BindView(R.id.red_bag_switch)
    CheckBox mRedBagSwitch;
    @BindView(R.id.order_switch)
    CheckBox mOrderSwitch;
    //店铺管理
    @BindView(R.id.message_management_switch)
    CheckBox mMessageManagementSwitch;
    @BindView(R.id.commodity_management_switch)
    CheckBox mCommodityManagementSwitch;
    @BindView(R.id.marketing_management_switch)
    CheckBox mMarketingManagementSwitch;
    @BindView(R.id.staff_management_switch)
    CheckBox mStaffManagementSwitch;
    //数据管理
    @BindView(R.id.operating_statistics_switch)
    CheckBox mOperatingStatisticsSwitch;
    @BindView(R.id.pay_overview_switch)
    CheckBox mPayOverviewSwitch;
    @BindView(R.id.sales_situation_switch)
    CheckBox mSalesSituationSwitch;
    //财务管理
    @BindView(R.id.bill_management)
    CheckBox mBillManagement;
    @BindView(R.id.account_management_switch)
    CheckBox mAccountManagementSwitch;
    //操作管理
    @BindView(R.id.goods_list_management)
    CheckBox mGoodsListManagement;
    @BindView(R.id.opening_table_switch)
    CheckBox mOpeningTableSwitch;
    @BindView(R.id.save)
    TextView mSave;

    //收银管理
    private boolean mPayBillCheckState = false;
    private boolean mGatheringSwitchState = false;
    private boolean mRedBagSwitchState = false;
    private boolean mOrderSwitchState = false;
    private String outStatePayBillCheckState;
    private String outGatheringSwitchState;
    private String outRedBagSwitchState;
    private String outOrderSwitchState;
    //店铺管理
    private boolean mMessageSwitchState = false;//店铺信息管理
    private boolean mCommoditySwitchState = false;//商品管理
    private boolean mMarketingSwitchState = false;//营销管理
    private boolean mStaffSwitchState = false;//员工管理
    private String outMessageSwitchState;
    private String outCommoditySwitchState;
    private String outMarketingSwitchState;
    private String outStaffSwitchState;

    //数据管理
    private boolean mOperatingSwitchState = false;//经营统计
    private boolean mPayOverviewSwitchState = false;//支付总览
    private boolean mSalesSituationSwitchState = false;//销量情况
    private String outOperatingSwitchState;
    private String outPayOverviewSwitchState;
    private String outSalesSituationSwitchState;
    //财务管理
    private boolean mBillSwitchState = false;//账单管理
    private boolean mAccountSwitchState = false;//账户管理
    private String outBillSwitchState;
    private String outAccountSwitchState;
    //操作管理
    private boolean mGoodsListSwitchState = false;//商品列表管理
    private boolean mOpeningTableSwitchState = false;//营业桌台管理
    private String outGoodsListSwitchState;
    private String outOpeningTableSwitchState;

    private Long roleId;
    private String positionType;
    private String positionName;//第一次进入时默认的角色名称
    List<PermissionGroupsEntity> mPermissionGroupDataList = new ArrayList<>();//获取所有权限的集合
    private String currentPositionName;//改变后的角色名称

    List<PermissionIdsEntity> permissionIdsList = new ArrayList<>();//权限id集合
    private boolean isPermissionChange = false;
    private String permission_ids;
    private ArrayList<PermissionIdsEntity> mFirstPermissionStateList = new ArrayList<>();//保存第一次进来时请求权限的集合的状态
    private String[] saveIntoStateList = new String[15];//第一次进入时的状态

    @Override
    protected NewPositionDetailPresenter createPresenter() {
        return new NewPositionDetailPresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.position_details));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });

        setActionBarMenuIcon(-1);
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
    public void onBackPressed() {
        exitEdit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_position_details;
    }

    @Override
    protected void initContentView() {
        addCheckBoxListener();//监听所有权限的开关
        initData();//初始化view
        mNewPositionName.addTextChangedListener(positionNames);
        mPresenter.getPermissionList(UserConfig.getInstance(this).getMerchantId(), roleId);
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


    private TextWatcher positionNames = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s) && s.length() > 0) {
                if (positionName.equals(s)) {
                    mSave.setEnabled(false);
                    mSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
                } else {
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                }
            }
        }
    };

    private void initData() {
        if (getIntent() == null) return;
        positionName = getIntent().getStringExtra("positionName");
        positionType = getIntent().getStringExtra("positionType");
        //roleId = intent.getLongExtra("roleId", roleId);
        roleId = (Long) getIntent().getSerializableExtra("roleId");
        if (this.roleId == null) return;
        if (positionType.equals("basic")) {
            // mDelete.setVisibility(View.GONE);
            mPositionName.setVisibility(View.GONE);
            mNewPositionName.setVisibility(View.VISIBLE);
            mNewPositionName.setText(positionName);
            mNewPositionName.setClickable(false);
            mNewPositionName.setFocusable(false);
        } else if (positionType.equals("add")) {
            //showDeleteButton();
            mNewPositionName.setVisibility(View.VISIBLE);
            mPositionName.setVisibility(View.GONE);
            mNewPositionName.setFocusable(true);
            mNewPositionName.setText(positionName);
            mNewPositionName.setSelection(mNewPositionName.getText().toString().length());
        }
    }

    @OnClick(R.id.save)
    public void onViewClicked() {
        //判空
        currentPositionName = mNewPositionName.getText().toString().trim();//当前角色名称
        if (TextUtils.isEmpty(currentPositionName)) {
            ToastUtil.show(NewPositionDetailsActivity.this, "职位名称不能为空");
            return;
        }
        isPermissionChange = false;
        permissionIdsList.clear();
        initPermissionIdsData();//获取角色权限集合id
        booleanChangeIntegerState();//boolean转变成String状态，与进来时的状态作对比
        isPermissionChange();//权限对比是否发生变化
        //权限id集合
        permission_ids = GsonParser.parseObjToJson(permissionIdsList);
        //对默认权限做判断
        if (currentPositionName.equals("店长") || currentPositionName.equals("收银") || currentPositionName.equals("服务员")) {
            //若当前职位时三种默认职位,并且默认权限发生了改变，弹框提示用户,重新生成新的职位
            //重点是判断当前默认权限是否发生变化mIsPermissionChange
            if (isPermissionChange) {
//                mSave.setEnabled(true);
//                mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                updateBasicPositionDialog();
            } else {
//                mSave.setEnabled(false);
//                mSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
                //finish();
            }
        } else {
            showHintDialog();
        }
    }

    private void isPermissionChange() {
        try {
            //收银管理
            if (!saveIntoStateList[0].equals(outStatePayBillCheckState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[1].equals(outGatheringSwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[2].equals(outRedBagSwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[3].equals(outOrderSwitchState)) {
                isPermissionChange = true;
                return;
            }

            //店铺管理
            if (!saveIntoStateList[4].equals(outMessageSwitchState)) {
                isPermissionChange = true;
                return;
            }

            if (!saveIntoStateList[5].equals(outCommoditySwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[6].equals(outMarketingSwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[7].equals(outStaffSwitchState)) {
                isPermissionChange = true;
                return;
            }

            //数据管理
            if (!saveIntoStateList[8].equals(outOperatingSwitchState)) {
                isPermissionChange = true;
                return;
            }

            if (!saveIntoStateList[9].equals(outPayOverviewSwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[10].equals(outSalesSituationSwitchState)) {
                isPermissionChange = true;
                return;
            }

            //财务管理
            if (!saveIntoStateList[11].equals(outBillSwitchState)) {
                isPermissionChange = true;
                return;
            }

            if (!saveIntoStateList[12].equals(outAccountSwitchState)) {
                isPermissionChange = true;
                return;
            }

            //操作管理
            if (!saveIntoStateList[13].equals(outGoodsListSwitchState)) {
                isPermissionChange = true;
                return;
            }
            if (!saveIntoStateList[14].equals(outOpeningTableSwitchState)) {
                isPermissionChange = true;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void booleanChangeIntegerState() {

        //收银管理
        if (mPayBillCheckState) {
            outStatePayBillCheckState = "0";
        } else {
            outStatePayBillCheckState = "1";
        }
        if (mGatheringSwitchState) {
            outGatheringSwitchState = "0";
        } else {
            outGatheringSwitchState = "1";
        }
        if (mRedBagSwitchState) {
            outRedBagSwitchState = "0";
        } else {
            outRedBagSwitchState = "1";
        }
        if (mOrderSwitchState) {
            outOrderSwitchState = "0";
        } else {
            outOrderSwitchState = "1";
        }

        //店铺管理
        if (mMessageSwitchState) {
            outMessageSwitchState = "0";
        } else {
            outMessageSwitchState = "1";
        }
        if (mCommoditySwitchState) {
            outCommoditySwitchState = "0";
        } else {
            outCommoditySwitchState = "1";
        }
        if (mMarketingSwitchState) {
            outMarketingSwitchState = "0";
        } else {
            outMarketingSwitchState = "1";
        }
        if (mStaffSwitchState) {
            outStaffSwitchState = "0";
        } else {
            outStaffSwitchState = "1";
        }

        //数据管理
        if (mOperatingSwitchState) {
            outOperatingSwitchState = "0";
        } else {
            outOperatingSwitchState = "1";
        }
        if (mPayOverviewSwitchState) {
            outPayOverviewSwitchState = "0";
        } else {
            outPayOverviewSwitchState = "1";
        }
        if (mSalesSituationSwitchState) {
            outSalesSituationSwitchState = "0";
        } else {
            outSalesSituationSwitchState = "1";
        }

        //财务管理
        if (mBillSwitchState) {
            outBillSwitchState = "0";
        } else {
            outBillSwitchState = "1";
        }
        if (mAccountSwitchState) {
            outAccountSwitchState = "0";
        } else {
            outAccountSwitchState = "1";
        }

        //操作管理
        if (mGoodsListSwitchState) {
            outGoodsListSwitchState = "0";
        } else {
            outGoodsListSwitchState = "1";
        }
        if (mOpeningTableSwitchState) {
            outOpeningTableSwitchState = "0";
        } else {
            outOpeningTableSwitchState = "1";
        }
    }

    private void showHintDialog() {
        ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.
                newInstance("确认修改该项职位？", null);
        confirmOrCancelDialog.showDialog(NewPositionDetailsActivity.this.getFragmentManager());
        confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                Log.d("mej", "permissionId: ==" + permission_ids);
                //非修改默认权限
                //区分两种情况 1.职位名称改变  2.职位名称未改变
                if (positionName.equals(currentPositionName)) {
                    //如果当前职位名称未改变
                    if (isPermissionChange) {
//                        mSave.setEnabled(true);
//                        mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                        mPresenter.PermissionSetting(UserConfig.getInstance(NewPositionDetailsActivity.this)
                                .getMerchantId(), roleId, null, permission_ids);//设置权限ids
                    } else {
                        //                       mSave.setEnabled(false);
//                        mSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
                        //finish();
                    }
                } else {
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                    String name = mNewPositionName.getText().toString().trim();
                    mPresenter.PermissionSetting(UserConfig.getInstance(NewPositionDetailsActivity.this)
                            .getMerchantId(), roleId, name, permission_ids);//设置权限ids
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

    private void updateBasicPositionDialog() {
        //弹出修改提示框
        PositionLookSaveDialog inputDialog = PositionLookSaveDialog.newInstance("您已对默认职位进行"
                        + "重新设定,系统将为您自动生成新职位，请在下方编辑职位名称。", "请输入职位名称",
                positionName, PositionLookSaveDialog.InputStyle.Text);
        inputDialog.showDialog(getFragmentManager());
        inputDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {


            }

            @Override
            public void onInputConfirm(String... values) {
                String newName = values[0];
                mNewPositionName.setText(newName);
                requestNetwork();
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void requestNetwork() {
        String name = mNewPositionName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) return;
        if (name.equals("店长") || name.equals("收银") || name.equals("服务员")) {
            ToastUtil.show(this, "默认角色名称不能重复");
        } else {
            String newName = mNewPositionName.getText().toString().trim();
            mPresenter.PermissionSetting(UserConfig.getInstance(NewPositionDetailsActivity.this)
                    .getMerchantId(), null, newName, permission_ids);//设置权限ids
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

    @Override
    public void permissionSettingSuccess(List<PermissionGroupsEntity> dataList) {
        ToastUtil.show(this, "添加或修改成功");
        finish();
    }

    @Override
    public void permissionSettingFailed() {

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
    public void getPermissionListSuccess(List<PermissionGroupsEntity> dataList) {
        if (dataList == null || dataList.size() == 0) return;
        // mPermissionGroupDataList.clear();
        mPermissionGroupDataList.addAll(dataList);
        selectData();//筛选联网获取的数据信息
        saveBaseState(mFirstPermissionStateList);//保存当前进入时的权限状态信息
        showOldPermissionView(dataList);//显示刚进来时的权限状态
    }

    private void saveBaseState(ArrayList<PermissionIdsEntity> mFirstPermissionStateList) {
        if (mFirstPermissionStateList != null && mFirstPermissionStateList.size() != 0) {
            for (int i = 0; i < mFirstPermissionStateList.size(); i++) {
                PermissionIdsEntity permissionIdsEntity = mFirstPermissionStateList.get(i);
                if (permissionIdsEntity == null) return;
                String isSelect = permissionIdsEntity.getIsSelect().toString();
                saveIntoStateList[i] = isSelect;
            }
        }
    }

    private void selectData() {

        List<PermissionEntity> casherPermissions = mPermissionGroupDataList.get(0).getPermissions();
        for (int i = 0; i < casherPermissions.size(); i++) {
            PermissionEntity permissionEntity = casherPermissions.get(i);
            Long id = permissionEntity.getId();
            Integer permissionId = Integer.parseInt(String.valueOf(id));
            Integer isSelected = permissionEntity.getIsSelected();
            mFirstPermissionStateList.add(new PermissionIdsEntity(isSelected, permissionId));//保存收银管理第一次请求到的集合数据
        }

        List<PermissionEntity> storePermissions = mPermissionGroupDataList.get(1).getPermissions();
        for (int i = 0; i < storePermissions.size(); i++) {
            PermissionEntity permissionEntity = storePermissions.get(i);
            Long id = permissionEntity.getId();
            Integer permissionId = Integer.parseInt(String.valueOf(id));
            Integer isSelected = permissionEntity.getIsSelected();
            mFirstPermissionStateList.add(new PermissionIdsEntity(isSelected, permissionId));
        }

        List<PermissionEntity> dataPermissions = mPermissionGroupDataList.get(2).getPermissions();
        for (int i = 0; i < dataPermissions.size(); i++) {
            PermissionEntity permissionEntity = dataPermissions.get(i);
            Long id = permissionEntity.getId();
            Integer permissionId = Integer.parseInt(String.valueOf(id));
            Integer isSelected = permissionEntity.getIsSelected();
            mFirstPermissionStateList.add(new PermissionIdsEntity(isSelected, permissionId));
        }

        List<PermissionEntity> financingPermissions = mPermissionGroupDataList.get(3).getPermissions();
        for (int i = 0; i < financingPermissions.size(); i++) {
            PermissionEntity permissionEntity = financingPermissions.get(i);
            Long id = permissionEntity.getId();
            Integer permissionId = Integer.parseInt(String.valueOf(id));
            Integer isSelected = permissionEntity.getIsSelected();
            mFirstPermissionStateList.add(new PermissionIdsEntity(isSelected, permissionId));
        }

        List<PermissionEntity> operationPermissions = mPermissionGroupDataList.get(4).getPermissions();
        for (int i = 0; i < operationPermissions.size(); i++) {
            PermissionEntity permissionEntity = operationPermissions.get(i);
            Long id = permissionEntity.getId();
            Integer permissionId = Integer.parseInt(String.valueOf(id));
            Integer isSelected = permissionEntity.getIsSelected();
            mFirstPermissionStateList.add(new PermissionIdsEntity(isSelected, permissionId));
        }
    }

    private void showOldPermissionView(List<PermissionGroupsEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            PermissionGroupsEntity permissionGroupsEntity = dataList.get(i);
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
                            mPayBill.setChecked(true);
                        }
                    }
                    //收款信息
                    if (permissionId == 2) {
                        if (isSelect == 0) {
                            mGatheringSwitch.setChecked(true);
                        }
                    }
                    //红包核销
                    if (permissionId == 3) {
                        if (isSelect == 0) {
                            mRedBagSwitch.setChecked(true);
                        }
                    }
                    //订单查看
                    if (permissionId == 4) {
                        if (isSelect == 0) {
                            mOrderSwitch.setChecked(true);
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
                            mMessageManagementSwitch.setChecked(true);
                        }
                    }
                    //商品管理
                    if (permissionId == 6) {
                        if (isSelect == 0) {
                            mCommodityManagementSwitch.setChecked(true);
                        }
                    }
                    //营销管理
                    if (permissionId == 7) {
                        if (isSelect == 0) {
                            mMarketingManagementSwitch.setChecked(true);
                        }
                    }
                    //员工管理
                    if (permissionId == 8) {
                        if (isSelect == 0) {
                            mStaffManagementSwitch.setChecked(true);
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
                            mOperatingStatisticsSwitch.setChecked(true);
                        }
                    }
                    //支付总览
                    if (permissionId == 10) {
                        if (isSelect == 0) {
                            mPayOverviewSwitch.setChecked(true);
                        }
                    }
                    //销量情况
                    if (permissionId == 11) {
                        if (isSelect == 0) {
                            mSalesSituationSwitch.setChecked(true);
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
                            mBillManagement.setChecked(true);
                        }
                    }
                    //账户管理
                    if (permissionId == 13) {
                        if (isSelect == 0) {
                            mAccountManagementSwitch.setChecked(true);
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
                            mGoodsListManagement.setChecked(true);
                        }
                    }
                    //营业桌台管理
                    if (permissionId == 15) {
                        if (isSelect == 0) {
                            mOpeningTableSwitch.setChecked(true);
                        }
                    }
                }
            }
        }


    }

    @Override
    public void getPermissionListFailed() {

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
        isPermissionChange = false;
        initPermissionIdsData();//获取角色权限集合id
        booleanChangeIntegerState();//boolean转变成String状态，与进来时的状态作对比
        isPermissionChange();//权限对比是否发生变化

        if (isPermissionChange) {
            mSave.setEnabled(true);
            mSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
        } else {
            mSave.setEnabled(false);
            mSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
        }
    }

}
