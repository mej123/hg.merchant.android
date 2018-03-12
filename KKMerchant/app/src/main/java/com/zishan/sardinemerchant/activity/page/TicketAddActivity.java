package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.CouponEntity;
import com.example.wislie.rxjava.model.page.CouponProductBean;
import com.example.wislie.rxjava.model.page.TicketAddEntity;
import com.example.wislie.rxjava.presenter.base.page.ticket.TicketAddPresenter;
import com.example.wislie.rxjava.view.base.page.ticket.TicketAddView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.PickerDateWheelDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * 新增卡券
 * Created by wislie on 2018/1/19.
 */

public class TicketAddActivity extends BActivity<TicketAddView, TicketAddPresenter> implements TicketAddView {

    @BindView(R.id.get_ticket_close_time)
    TextView mTicketCloseTimeText;
    @BindView(R.id.ticket_store_num)
    TextView mTicketStoreNumText;
    @BindView(R.id.hint_layout)
    RelativeLayout mHintLayout;
    @BindView(R.id.service_fee)
    TextView mServiceFeeText;
    @BindView(R.id.ticket_cost)
    EditText mTicketCostEdit;
    @BindView(R.id.certification_cost)
    EditText mCertificationCostEdit;


    @BindView(R.id.item_choose_ticket)
    RelativeLayout mTicketLayout;
    @BindView(R.id.ticket_name)
    TextView mTicketNameText;
    @BindView(R.id.ticket_id)
    TextView mTicketIdText;
    @BindView(R.id.ticket_date_role)
    TextView mTicketDaysText;
    @BindView(R.id.ticket_value_layout)
    LinearLayout mTicketValueLayout;
    @BindView(R.id.ticket_value)
    TextView mTicketValueText;
    @BindView(R.id.ticket_desc_role)
    TextView mTicketDescText;
    @BindView(R.id.ticket_expire_desc)
    TextView mTicketExpireText;

    //卡券库存 请求
    public static final int REQUEST_REPOSITORY = 0x01;


    //最大保证金和核销奖金
    private long max_award = 0;
    //券定义id
    private Long cou_def_id;
    //券库存
    private long stock = 0;
    //领券截止时间
    private Long give_out_end_time;
    //此处默认为1
    private int type = 1;
    //分销渠道（0:无分销商 1：分销商,2：平台)
//    private int distribution_way = 1;
    //分销商id列表
//    private String distor_ids;
    //成本
    private Long cost;
    //保证金
//    private Long earnest_money;
    //核销奖金
    private Long charge_off_award;
    //服务费
    private Integer servicerate;
    @Override
    protected TicketAddPresenter createPresenter() {
        return new TicketAddPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_add;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color);
        setActionbarTitle(getString(R.string.ticket_send_msg));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarDivderVisible(false);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            CouponEntity data = (CouponEntity) intent.getSerializableExtra("coupon");
            updateCoupon(data);
        }

        mTicketCostEdit.addTextChangedListener(new InputWatcher(R.id.ticket_cost));
        mCertificationCostEdit.addTextChangedListener(new InputWatcher(R.id.certification_cost));
    }

    private void updateCoupon(CouponEntity ticketData) {
        if (ticketData == null) return;
        cou_def_id = ticketData.getCouDefId();
        servicerate = ticketData.getServiceRate();
        if (ticketData.getCouponType() != null && ticketData.getCouponType() == 1) { //满减券
            mTicketValueLayout.setVisibility(View.VISIBLE);
            mTicketLayout.setBackgroundResource(R.mipmap.ticket_discount_abled_bg);
            mTicketDescText.setText("满减券" + ticketData.getRealAmount() + "元; 满" + ticketData.getLimitAmount() + "元可用");
        } else if (ticketData.getCouponType() != null && ticketData.getCouponType() == 2) { //代金券
            mTicketValueLayout.setVisibility(View.VISIBLE);
            mTicketLayout.setBackgroundResource(R.mipmap.ticket_golden_abled_bg);
            mTicketDescText.setText("代金券面值" + ticketData.getRealAmount() + "元");
        } else if (ticketData.getCouponType() != null && ticketData.getCouponType() == 3) { //凭证券
            mTicketValueLayout.setVisibility(View.GONE);
            mTicketLayout.setBackgroundResource(R.mipmap.ticket_certification_abled_bg);

            mTicketDescText.setText("此次是商品内容或者服务内容此次是商品");
            List<CouponProductBean> productList = ticketData.getCouponProducts();
            if(productList != null && productList.size() > 0){
                for(int i = 0; i < productList.size(); i++){
                    CouponProductBean product = productList.get(i);
                    if(product.getId() != null && product.getId() == 1){
                        mTicketDescText.setText("优先显示服务商品");
                        break;
                    }
                }
            }
        }


        mTicketNameText.setText(ticketData.getName());
        mTicketIdText.setText("卡券ID " + ticketData.getCouDefId());
        mTicketValueText.setText(String.valueOf(ticketData.getRealAmount()));
        mTicketDaysText.setText(ticketData.getMarketingMeta());
        if (ticketData.getValidityType() == 1) { //1固定天数
            mTicketExpireText.setText("领券" + ticketData.getEffectiveTime() + "天后可用,有效期" + ticketData.getValidityTime() + "天");
        } else if (ticketData.getValidityType() == 2) { //2 指定时间段
            mTicketExpireText.setText("有效期:" + DatePickerUtil.getFormatDate(ticketData.getValidityStartTime(), "yyyy.MM.dd")
                    + "-" + DatePickerUtil.getFormatDate(ticketData.getValidityEndTime(), "yyyy.MM.dd"));
        }
    }

    @OnClick({R.id.close_time_layout, R.id.ticket_store_layout,
            R.id.confirm_release})
    public void onClick(View view) {
        switch (view.getId()) {
            //领券截止时间
            case R.id.close_time_layout:
                chooseDateDialog();
                break;
            //卡券库存
            case R.id.ticket_store_layout:
                Intent reposIntent = new Intent(this, TicketRepositoryActivity.class);
                String repositoryNumStr = mTicketStoreNumText.getText().toString();
                if (!TextUtils.isEmpty(repositoryNumStr)) {
                    try {
                        stock = Integer.parseInt(repositoryNumStr);
                    } catch (NumberFormatException e) {
                        stock = -1;
                    }
                }
                reposIntent.putExtra("stock", stock);
                startActivityForResult(reposIntent, REQUEST_REPOSITORY);

                break;
            //确认发布
            case R.id.confirm_release:
                releaseTicketDialog();
                break;

        }
    }

    //选择截止日期
    private void chooseDateDialog() {
        PickerDateWheelDialog dialog = PickerDateWheelDialog.newInstance("领券截止时间", false, true);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                if (values.length <= 0) return;

                Calendar calendar = Calendar.getInstance();
                DatePickerUtil.setDate(calendar, Integer.parseInt(values[0]),
                        Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                if( calendar.getTimeInMillis() <=  System.currentTimeMillis()){
                    ToastUtil.show("领券截止时间不能小于当前时间");
                    return;
                }
                mTicketCloseTimeText.setText(values[0] + "-" + values[1] + "-" + values[2]);
                give_out_end_time = calendar.getTimeInMillis() / 1000;
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //确认发布
    private void releaseTicketDialog() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认发布该卡券?", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {
                releaseTicket();
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
            case REQUEST_REPOSITORY:
                stock = data.getLongExtra("stock", -1);
                if (stock > 0) {
                    mTicketStoreNumText.setText(String.valueOf(stock));
                } else {
                    mTicketStoreNumText.setText("不限");
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
    public void addTicketSuccess(TicketAddEntity data) {

        ActivityManager.getInstance().finishActivity(TicketChooseActivity.class);
        ActivityManager.getInstance().finishActivity(TicketAddActivity.class);

    }

    @Override
    public void addTicketFailed() {

    }

    //其实可以用filter代替
    private class InputWatcher implements TextWatcher {

        private int mEditId;

        public InputWatcher(int editId) {
            mEditId = editId;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            StringUtil.pointDigitLimited(s, 2);
            switch (mEditId) {
                //卡券成本
                case R.id.ticket_cost:
                    if (s.length() > 0) {
                        mHintLayout.setVisibility(View.VISIBLE);
                        long total = StringUtil.String2Long(s.toString(), 2, true);
                        long fee = 0;
                        if(servicerate != null){
                            fee = total * servicerate / 100;
                        }
                        max_award = total - fee;

                        cost = total;
                        mServiceFeeText.setText(StringUtil.point2String(fee) + "元");
                        mCertificationCostEdit.setEnabled(true);
                        mCertificationCostEdit.setHint("最大可设置" + StringUtil.point2String(max_award));
                    } else {
                        mHintLayout.setVisibility(View.GONE);
                        mServiceFeeText.setText("");
                        mCertificationCostEdit.setEnabled(false);
                        mCertificationCostEdit.setHint("请先输入卡券成本");
                        cost = null;
                    }
                    break;
                //核销
                case R.id.certification_cost:
                    if (s.toString().length() > 0) {
                        long val = StringUtil.String2Long(s.toString(), 2, true);
                        if (max_award < val) {
                            s.delete(s.length() - 1, s.length());
                            return;
                        }
                        charge_off_award = val;
                    } else {
                        charge_off_award = null;
                    }

                    break;

            }

        }
    }


    private void releaseTicket() {
        if (give_out_end_time == null) {
            ToastUtil.show("领取截止时间不能为空");
            return;
        }
        mPresenter.requestAddTicket(cou_def_id, UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                stock, give_out_end_time, type, null,
                null, cost, null, charge_off_award);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
