package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreMsgPresenter;
import com.example.wislie.rxjava.view.base.personal.StoreMsgView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.page.StoreOpenStateActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zishan.sardinemerchant.R.id.shop_front_edit;

/**
 * 门店详情
 * Created by wislie on 2017/11/23.
 */

public class StoreMsgEditActivity extends BActivity<StoreMsgView, StoreMsgPresenter> implements StoreMsgView {

    @BindView(R.id.store_name_edit)
    TextView mStoreNameEdit;
    @BindView(R.id.store_address_edit)
    TextView mStoreAddressEdit;
    @BindView(R.id.store_mobile_edit)
    TextView mStoreMobileEdit;
    @BindView(R.id.store_introduce_edit)
    TextView mStoreIntroduceEdit;
    @BindView(R.id.store_consume_edit)
    TextView mStoreConsumeEdit;
    @BindView(R.id.store_open_time_edit)
    TextView mStoreOpenTimeEdit;
    @BindView(R.id.store_photo_edit)
    TextView mStorePhotoEdit;

    public static final int REQUEST_CODE_INTRODUCE = 0x01;
    public static final int REQUEST_CODE_CONSUME = 0x02;
    public static final int REQUEST_CODE_PHONE = 0x03;
    public static final int REQUEST_CODE_OPEN_TIME = 0x06;
    @BindView(R.id.operating_status_edit)
    TextView mOperatingStatusEdit;
    @BindView(shop_front_edit)
    TextView mShopFrontEdit;
    private StoreMsgEntity storeMsgEntity;
    private String averageCount;
    private String minCount;
    private String firstOpenTime;
    private String firstCloseTime;
    private String secondOpenTime;
    private String secondCloseTime;
    private String substringOpenHouseTime;
    private String substringCloseHouseTime;
    private String logoPicUrl;

    private long store_id = 0;

    @Override
    protected StoreMsgPresenter createPresenter() {
        return new StoreMsgPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_details;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.store_details));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            store_id = intent.getLongExtra("store_id", store_id);
        }
        showProgressDialog();
        loadData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void loadData() {
        mPresenter.getStoreMsg(store_id);
    }

    @OnClick({R.id.store_introduce_layout, R.id.store_consume_layout, R.id.store_open_time_layout,
            R.id.store_photo_layout, R.id.rl_store_phone, R.id.shop_front_layout, R.id.rl_operating_status_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //门店状态
            case R.id.rl_operating_status_layout:
                Intent stateIntent = new Intent(this, StoreOpenStateActivity.class);
                stateIntent.putExtra("store_msg", storeMsgEntity);
                startActivity(stateIntent);
                break;
            //店铺介绍
            case R.id.store_introduce_layout:
                Intent introduceIntent = new Intent(this, StoreIntroduceActivity.class);
                introduceIntent.putExtra("storeMsgEntity", storeMsgEntity);
                startActivityForResult(introduceIntent, REQUEST_CODE_INTRODUCE);
                break;
            //消费管理
            case R.id.store_consume_layout:
                Intent consumeIntent = new Intent(this, NewConsumeManageActivity.class);
                consumeIntent.putExtra("storeMsgEntity", storeMsgEntity);
                startActivityForResult(consumeIntent, REQUEST_CODE_CONSUME);
                break;
            //营业时间
            case R.id.store_open_time_layout:
                Intent openTimeIntent = new Intent(this, StoreOpenTimeActivity.class);
                openTimeIntent.putExtra("firstOpenTime", firstOpenTime);
                openTimeIntent.putExtra("firstCloseTime", firstCloseTime);
                openTimeIntent.putExtra("secondOpenTime", secondOpenTime);
                openTimeIntent.putExtra("secondCloseTime", secondCloseTime);
                openTimeIntent.putExtra("openTime", mStoreOpenTimeEdit.getText().toString());
                openTimeIntent.putExtra("storeMsgEntity", storeMsgEntity);
                startActivityForResult(openTimeIntent, REQUEST_CODE_OPEN_TIME);
                break;
            //店铺照片
            case R.id.store_photo_layout:
                Intent albumIntent = new Intent(this, StoreAlbumActivity.class);
                albumIntent.putExtra("store_id", store_id);
                startActivity(albumIntent);
                break;
            //门店电话
            case R.id.rl_store_phone:
                Intent phoneIntent = new Intent(this, ContactWayActivity.class);
                String phone = mStoreMobileEdit.getText().toString();
                phoneIntent.putExtra("storeMsgEntity", storeMsgEntity);
                phoneIntent.putExtra("phone", phone);
                startActivityForResult(phoneIntent, REQUEST_CODE_PHONE);
                break;

            //门脸照片
            case R.id.shop_front_layout:
                Intent shopFrontIntent = new Intent(this, LogoPhotoActivity.class);
                if (!TextUtils.isEmpty(logoPicUrl)) {
                    shopFrontIntent.putExtra("store_id", store_id);
                    shopFrontIntent.putExtra("logoPicUrl", logoPicUrl);
                }
                startActivity(shopFrontIntent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_INTRODUCE:
                String merchantDescription = data.getStringExtra("merchantDescription");
                if (merchantDescription.equals("empty")) {
                    mStoreIntroduceEdit.setHint("未设置");
                } else {
                    mStoreIntroduceEdit.setHint(" ");
                }

                break;
            case REQUEST_CODE_CONSUME:
                averageCount = data.getStringExtra("averageCount");
                minCount = data.getStringExtra("minCount");
                break;

            case REQUEST_CODE_PHONE:
                String phone = data.getStringExtra("phone");
                mStoreMobileEdit.setText(phone);
                break;
            case REQUEST_CODE_OPEN_TIME:
                boolean is24th = data.getBooleanExtra("is24th", false);
                if (is24th) {
                    mStoreOpenTimeEdit.setText("24小时");
                } else {
                    firstOpenTime = data.getStringExtra("firstOpenTime");
                    firstCloseTime = data.getStringExtra("firstCloseTime");
                    secondOpenTime = data.getStringExtra("secondOpenTime");
                    secondCloseTime = data.getStringExtra("secondCloseTime");
                    if (!TextUtils.isEmpty(firstOpenTime) && !TextUtils.isEmpty(secondCloseTime)) {
                        mStoreOpenTimeEdit.setText(firstOpenTime + "~" + firstCloseTime + "\t" +
                                secondOpenTime + "~" + secondCloseTime);
                        return;
                    } else if (firstOpenTime != null) {
                        mStoreOpenTimeEdit.setText(firstOpenTime + "~" + firstCloseTime);
                        return;
                    } else if (!TextUtils.isEmpty(secondOpenTime)) {
                        mStoreOpenTimeEdit.setText(secondOpenTime + "~" + secondCloseTime);
                        return;
                    }
                }
                break;
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
    public void getStoreMsgSuccess(StoreMsgEntity data) {
        if (data != null) {
            this.storeMsgEntity = data;
        }

        if (data.getMerchantName() != null) {
            mStoreNameEdit.setText(data.getMerchantName());
        }

        if (data.getAddress() != null) {
            mStoreAddressEdit.setText(data.getAddress());
        }

        //营业状态（0:上架,1:下架，2停业整顿，3开始营业，4封停，5开店中）
        Integer state = data.getState();
        if (state == 0) {
            mOperatingStatusEdit.setText("上架");

        } else if (state == 1) {
            mOperatingStatusEdit.setText("下架");
        } else if (state == 2) {
            mOperatingStatusEdit.setText("停业修整");
            mOperatingStatusEdit.setTextColor(ContextCompat.getColor(this, R.color.stop_reorganize));

        } else if (state == 3) {
            mOperatingStatusEdit.setText("营业中");
            mOperatingStatusEdit.setTextColor(ContextCompat.getColor(this, R.color.opening));

        } else if (state == 4) {
            mOperatingStatusEdit.setText("封停");
            mOperatingStatusEdit.setTextColor(ContextCompat.getColor(this, R.color.closure));

        } else if (state == 5) {
            mOperatingStatusEdit.setText("开店中");
        }

        //设置手机号
        if (data.getTelephone() != null) {
            mStoreMobileEdit.setText(data.getTelephone());
        }
        //店铺介绍
        if (!TextUtils.isEmpty(data.getMerchantDescription()) && !("").equals(data.getMerchantDescription())) {
            mStoreIntroduceEdit.setHint(" ");
        }
        //消费管理
        if (data.getConsumption() != 0 || data.getMixConsumption() != 0) {
            mStoreConsumeEdit.setText(" ");
        } else {
            mStoreConsumeEdit.setText("未设置");
        }

        //环境照片
        if (data.getPicNum() != null) {
            mStorePhotoEdit.setText(data.getPicNum() + "张");
        }
        //门脸照片
        if (!TextUtils.isEmpty(data.getLogoPicUrl())) {
            logoPicUrl = data.getLogoPicUrl();
            mShopFrontEdit.setText("1张");
        }

        //营业时间
        if (data.getIs24th() != null && data.getIs24th() == Boolean.TRUE) {
            mStoreOpenTimeEdit.setText("24小时");
        } else if (data.getIs24th() != null && data.getIs24th() == Boolean.FALSE) {
            firstOpenTime = data.getFirstOpenTime();
            firstCloseTime = data.getFirstCloseTime();
            secondOpenTime = data.getSecondOpenTime();
            secondCloseTime = data.getSecondCloseTime();


            //两段时间都为空,营业时间未设置
            if (firstOpenTime == null || ("").equals(firstOpenTime)) {
                mStoreOpenTimeEdit.setText("未设置");
                return;
            }
            //第一段时间设置,第二段未设置,只显示第一段时间
            if (secondOpenTime == null || ("").equals(secondOpenTime)) {
                mStoreOpenTimeEdit.setText(firstOpenTime + "~" + firstCloseTime);//营业时间
                return;
            }

            //第一、第二段时间都有设置
            substringOpenHouseTime = secondOpenTime.substring(0, secondOpenTime.indexOf(":")); //第二段开始小时时间前两位
            String substringOpenMinuteTime = secondOpenTime.substring(secondOpenTime.length() - 2,
                    secondOpenTime.length());//第二段开始分钟时间后两位
            int mSecondOpenTime = Integer.parseInt(substringOpenHouseTime);//截取第二段开始小时时间
            if (mSecondOpenTime > 23) {
                int surplusSecondOpenTime = mSecondOpenTime - 24;//次日的小时数
                if (surplusSecondOpenTime < 10) {
                    secondOpenTime = "次日" + "0" + surplusSecondOpenTime + ":" + substringOpenMinuteTime;
                } else {
                    secondOpenTime = "次日" + surplusSecondOpenTime + ":" + substringOpenMinuteTime;
                }
            }

            substringCloseHouseTime = secondCloseTime.substring(0, secondCloseTime.indexOf(":"));
            String substringCloseMinuteTime = secondCloseTime.substring(secondCloseTime.length() - 2,
                    secondCloseTime.length());//第二段结束分钟时间后两位
            int mSecondCloseTime = Integer.parseInt(substringCloseHouseTime);//截取第二段开始小时时间

            if (mSecondCloseTime > 23) {
                int surplusSecondCloseTime = mSecondCloseTime - 24;//次日的小时数
                if (surplusSecondCloseTime < 10) {
                    secondCloseTime = "次日" + "0" + surplusSecondCloseTime + ":" + substringCloseMinuteTime;
                } else {
                    secondCloseTime = "次日" + surplusSecondCloseTime + ":" + substringCloseMinuteTime;
                }
            }
            mStoreOpenTimeEdit.setText(firstOpenTime + "~" + firstCloseTime + "\t"
                    + secondOpenTime + "~" + secondCloseTime);//营业时间
        }
    }

    @Override
    public void getStoreMsgFailed() {

    }
}
