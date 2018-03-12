package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.BankCardBindEntity;
import com.example.wislie.rxjava.presenter.personal.BankCardBindPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.BankCardBindView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.view.ClearWithSpaceEditText;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.RegexUtil;

import static com.zishan.sardinemerchant.R.id.open_bank_province_city;

/**
 * Created by yang on 2017/11/2.
 * <p>
 * 个人   银行卡绑定
 */

public class NewBankCardBindActivity extends BActivity<BankCardBindView, BankCardBindPresenter>
        implements BankCardBindView {

    @BindView(R.id.tv_account_type)
    TextView mAccountType;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.et_input_opening_person)
    EditText mInputOpeningPerson;//开户人
    @BindView(R.id.et_input_band_card_number)
    ClearWithSpaceEditText mInputBankCardNumber;//银行卡号
    @BindView(R.id.et_input_opening_bank_name)
    EditText mInputOpeningBankName;//开户行名称
    @BindView(R.id.tv_opening_province_city_name)
    TextView mOpeningProvinceCityName;//开户行地区  省/市
    @BindView(R.id.tv_feedback_content)
    TextView mFeedbackContent;
    @BindView(R.id.rl_feedback)
    RelativeLayout mFeedback;
    @BindView(R.id.rl_select_bank)
    RelativeLayout mSelectBank;
    @BindView(R.id.open_bank_province_city)
    RelativeLayout mOpenBankProvinceCity;
    @BindView(R.id.rl_bind_card_type)
    RelativeLayout mBindCardType;
    private int bankCode;//银行名称代码
    private String accountType;//银行卡账户类型(0:个人,1:公司)
    private String bankName; //银行
    public static final int REQUEST_CODE_TYPE = 0x01;
    public static final int REQUEST_CODE_SELECT_BANK = 0x02;
    public static final int CITY_SELECT_RESULT_FRAG = 0x03;


    private Long storeId;
    private String uuidCode;
    private String provinceId;
    private String adcode;
    private String changeCard;
    public static final int BIND_BANK_CARD = 0;
    public static final int CHANGE_BANK_CARD = 1;
    private String tag;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bank_card_bind;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.bank_card_bind));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BankCardBindPresenter createPresenter() {
        return new BankCardBindPresenter(this, this);
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent == null) return;
        tag = intent.getStringExtra("changeBankCard");
        uuidCode = intent.getStringExtra("uuidCode");
        storeId = UserConfig.getInstance(this).getStoreId();
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.rl_bind_card_type, R.id.tv_next, R.id.rl_select_bank, R.id.open_bank_province_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_bind_card_type://账户类型

                Intent intent = new Intent();
                intent.setClass(NewBankCardBindActivity.this, AccountManageActivity.class);
                intent.putExtra("accountType", accountType);
                startActivityForResult(intent, REQUEST_CODE_TYPE);

                break;
            case R.id.tv_next://下一步

                String owner = mInputOpeningPerson.getText().toString().trim();//开户人
                String bank_card_no = mInputBankCardNumber.getNoSpaceText();//银行卡号
                String openingBankName = mInputOpeningBankName.getText().toString().trim();//开户行名称

                if (TextUtils.isEmpty(owner)) {
                    ToastUtil.show(this, "开户人不能为空");
                    return;
                }

                if (TextUtils.isEmpty(bank_card_no)) {
                    ToastUtil.show(this, "银行卡号不能为空");
                    return;
                }

                if (RegexUtil.checkBankCard(bank_card_no) != true) {
                    ToastUtil.show(this, "银行卡号格式不正确");
                    return;
                }

                if (TextUtils.isEmpty(bankName)) {
                    ToastUtil.show(this, "请选择银行");
                    return;
                }

                if (TextUtils.isEmpty(bankName)) {
                    ToastUtil.show(this, "请选择银行");
                    return;
                }
                if (TextUtils.isEmpty(openingBankName)) {
                    ToastUtil.show(this, "开户行不能为空");
                    return;
                }

                if (TextUtils.isEmpty(accountType)) {
                    ToastUtil.show(this, "请选择账户类型");
                    return;
                }
                //发起绑定银行卡请求
                //相等时发起换绑请求,不等则进行正常绑卡
                if (!TextUtils.isEmpty(tag) && tag.equals("changeBankCard")) {
                    mPresenter.bindBankCard(storeId, owner, bank_card_no, bankCode, provinceId, adcode,
                            openingBankName, accountType, CHANGE_BANK_CARD, uuidCode);
                } else {
                    mPresenter.bindBankCard(storeId, owner, bank_card_no, bankCode, provinceId, adcode,
                            openingBankName, accountType, BIND_BANK_CARD, uuidCode);
                }

                break;
            case R.id.rl_select_bank://选择银行
                Intent selectBankIntent = new Intent();
                selectBankIntent.setClass(NewBankCardBindActivity.this, SelectBankActivity.class);
                startActivityForResult(selectBankIntent, REQUEST_CODE_SELECT_BANK);
                break;
            case open_bank_province_city:
                Intent intentSelectCity = new Intent(NewBankCardBindActivity.this, SelectCityActivity.class);
                startActivityForResult(intentSelectCity, NewBankCardBindActivity.CITY_SELECT_RESULT_FRAG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_TYPE:
                Bundle bundleType = data.getExtras();
                if (bundleType == null) return;
                accountType = (String) bundleType.get("selectType");
                if (accountType.equals("private")) {
                    mAccountType.setText("私人账户");
                } else if (accountType.equals("public")) {
                    mAccountType.setText("公款账户");
                }
                break;

            case REQUEST_CODE_SELECT_BANK:
                Bundle bundleBank = data.getExtras();
                bankName = (String) bundleBank.get("bankName");
                bankCode = bundleBank.getInt("bankCode");
                tvBank.setText(bankName);
                break;

            case CITY_SELECT_RESULT_FRAG:
                if (data == null) {
                    return;
                }
                Bundle bundleName = data.getExtras();
                String fullname = bundleName.getString("fullname");//市全名
                adcode = bundleName.getString("adcode");//市id
                provinceId = bundleName.getString("provinceId");//省id
                String provinceName = bundleName.getString("provinceName");//省全名
                if (provinceName != null) {
                    mOpeningProvinceCityName.setText(provinceName + "/" + fullname);
                } else {
                    mOpeningProvinceCityName.setText(fullname);
                }
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
    public void bindBankCardComplete(BankCardBindEntity bankCardBindEntity) {
        startActivity(new Intent(this, NewCommitAuditActivity.class));
        NewBankCardBindActivity.this.finish();
    }

    @Override
    public void bindBankCardFailed() {

    }
}
