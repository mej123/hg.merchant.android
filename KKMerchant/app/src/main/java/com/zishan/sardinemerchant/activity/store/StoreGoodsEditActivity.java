package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.GoodsUpdateEntity;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsUpdatePresenter;
import com.example.wislie.rxjava.view.base.store.goods.GoodsUpdateView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;

import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.DishClassifyDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2017/9/21.
 * <p>
 * 商品编辑
 */

public class StoreGoodsEditActivity extends BActivity<GoodsUpdateView, GoodsUpdatePresenter>
        implements GoodsUpdateView, TextWatcher {

    @BindView(R.id.packet_switch)
    ImageView mPacketSwitch;

    @BindView(R.id.join_discount_switch)
    ImageView mDiscountSwitch;

    @BindView(R.id.goods_unit)
    TextView mGoodsUnitText;

    @BindView(R.id.goods_classify_name)
    TextView mGoodsClassifyText;

    @BindView(R.id.goods_recommend)
    TextView mGoodsRecommendText;
    @BindView(R.id.goods_taste)
    TextView mGoodsTasteText;
    @BindView(R.id.goods_category)
    TextView mGoodsCategoryText;

    @BindView(R.id.goods_icon)
    ImageView mGoodsIcon;
    @BindView(R.id.goods_name)
    EditText mGoodsNameEdit;

    @BindView(R.id.et_original_price)
    EditText mOriginPriceEdit;
    @BindView(R.id.goods_sale_price)
    EditText mSalePriceEdit;

    @BindView(R.id.goods_category_layout)
    RelativeLayout mCategoryLayout;

    public static final int REQUEST_CODE_TASTE = 0x01;
    public static final int REQUEST_CODE_CATEGORY = 0x02;
    public static final int REQUEST_CODE_RECOMMEND = 0x03;
    public static final int REQUEST_CODE_PHOTO = 0x04;
    public static final int REQUEST_CODE_UNIT = 0x05;


    //商品图片路径
    private String mGoodsPath;
    //图片资源 id
    private String mResouceId;
    //分组id
    private Long custom_group_id;

    private Long product_id;
    //商品实体类
    private ProductEntity mProduct;
    //分类列表
    private ArrayList<ProductGroupEntity> mProductGroupList = new ArrayList<>();
    //初始状态下是否推荐
    private boolean mInitedRecommend = false;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.goods_edit));
        setActionBarHomeIcon(R.mipmap.back_icon);
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEdit();
            }
        });
        setActionBarMenuIcon(-1);
        //标题栏菜单按钮
        setActionBarMenuText("完成").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGoodsEdit();
            }
        });
    }

    @Override
    protected void initContentView() {
        //键盘缩回
        SoftInputUtil.hideDirectly(this);
        Intent intent = getIntent();
        if (intent != null) {
            mProduct = intent.getParcelableExtra(Constant.CONFIG_PRODUCT);
            ArrayList<ProductGroupEntity> groupList = intent.getParcelableArrayListExtra(
                    Constant.CONFIG_PRODUCT_GROUP);
            mProductGroupList.clear();
            if (groupList != null && groupList.size() > 0) {
                mProductGroupList.addAll(groupList);

                for (int i = 0; i < groupList.size(); i++) {
                    ProductGroupEntity productGroup = groupList.get(i);
                    if (mProduct == null) break;
                    if (productGroup.getId().longValue() == mProduct.getCustomGroupId().longValue()) {
                        mGoodsClassifyText.setText(productGroup.getName());
                    }
                }

                if (mProduct == null) {
                    ProductGroupEntity productGroup = groupList.get(0);
                    mGoodsClassifyText.setText(productGroup.getName());
                }

            }
        }
        if (mProduct == null) return;
        if (!TextUtils.isEmpty(mProduct.getPicUrl())) {
            mGoodsIcon.setVisibility(View.VISIBLE);
            mGoodsPath = mProduct.getPicUrl();
            //加载图片
            GlideLoader.getInstance().load(this, mGoodsIcon, StringUtil.appendHttps(mGoodsPath));
        }


        product_id = mProduct.getId();
        custom_group_id = mProduct.getCustomGroupId();
        mInitedRecommend = mProduct.getRecommend();
        //套餐设置
        if (mProduct.getSetMeal() != null && mProduct.getSetMeal() == true) {
            mPacketSwitch.setImageResource(R.mipmap.switch_on);
            mPacketSwitch.setSelected(true);
            mCategoryLayout.setVisibility(View.VISIBLE);
        } else {
            mPacketSwitch.setImageResource(R.mipmap.switch_off);
            mPacketSwitch.setSelected(false);
            mCategoryLayout.setVisibility(View.GONE);
        }

        mGoodsNameEdit.setText(mProduct.getName());

        if (mProduct.getDiscount() != null && mProduct.getDiscount() == Boolean.TRUE) {
            mSalePriceEdit.setText(StringUtil.point2String(mProduct.getStrategyPrice()));
        } else {
            mSalePriceEdit.setText(StringUtil.point2String(mProduct.getRealPrice()));
        }
        mOriginPriceEdit.setText((StringUtil.point2String(mProduct.getPrice())));
        mGoodsUnitText.setText(mProduct.getUnit());

        if (!TextUtils.isEmpty(mProduct.getTasteDesc())) {
            mGoodsTasteText.setText("已编辑");
        }

        if (!TextUtils.isEmpty(mProduct.getDescription())) {
            mGoodsCategoryText.setText("已编辑");
        }

        //菜品推荐设置
        if (mProduct.getRecommend() != null && mProduct.getRecommend() == true) {
            mGoodsRecommendText.setText("已设置");
        } else {
            mGoodsRecommendText.setText("");
            mGoodsRecommendText.setHint("未设置");
        }

        if (mProduct.getEnableWholeShopDiscount() != null && mProduct.getEnableWholeShopDiscount() == true) {
            mDiscountSwitch.setImageResource(R.mipmap.switch_on);
            mDiscountSwitch.setSelected(true);
        } else {
            mDiscountSwitch.setImageResource(R.mipmap.switch_off);
            mDiscountSwitch.setSelected(false);
        }

        mOriginPriceEdit.addTextChangedListener(this);
        mSalePriceEdit.addTextChangedListener(this);
    }

    @Override
    protected GoodsUpdatePresenter createPresenter() {
        return new GoodsUpdatePresenter(this, this);
    }

    @OnClick({R.id.packet_switch, R.id.join_discount_switch, R.id.goods_recommend_layout, R.id.goods_taste_layout,
            R.id.goods_category_layout, R.id.goods_classify_layout, R.id.goods_unit_layout, R.id.goods_image_area})
    public void onClick(View view) {
        switch (view.getId()) {
            //添加商品图片
            case R.id.goods_image_area:

                Intent imageIntent = new Intent(this, GoodsImageActivity.class);
                if (!TextUtils.isEmpty(mGoodsPath)) {
                    imageIntent.putExtra("path", mGoodsPath);
                }
                startActivityForResult(imageIntent, REQUEST_CODE_PHOTO);

                break;

            //套餐设置
            case R.id.packet_switch:
                if (!mPacketSwitch.isSelected()) {
                    mPacketSwitch.setImageResource(R.mipmap.switch_on);
                    mPacketSwitch.setSelected(true);
                    mCategoryLayout.setVisibility(View.VISIBLE);
                } else {
                    mPacketSwitch.setImageResource(R.mipmap.switch_off);
                    mPacketSwitch.setSelected(false);
                    mCategoryLayout.setVisibility(View.GONE);
                }
                break;
            //是否参与全店优惠
            case R.id.join_discount_switch:
                if (!mDiscountSwitch.isSelected()) {
                    mDiscountSwitch.setImageResource(R.mipmap.switch_on);
                    mDiscountSwitch.setSelected(true);
                } else {
                    mDiscountSwitch.setImageResource(R.mipmap.switch_off);
                    mDiscountSwitch.setSelected(false);
                }
                break;
            //推荐菜品设置
            case R.id.goods_recommend_layout:
                Intent recommendIntent = new Intent(this, RecommendGoodsSetActivity.class);
                String recommendContent = mGoodsRecommendText.getText().toString();
                boolean isRecommend;
                if (TextUtils.isEmpty(recommendContent)) {
                    isRecommend = false;
                } else {
                    isRecommend = true;
                }
                recommendIntent.putExtra("isRecommend", isRecommend);
                recommendIntent.putExtra("initedRecommend", mInitedRecommend);
                startActivityForResult(recommendIntent, REQUEST_CODE_RECOMMEND);
                break;
            //口味
            case R.id.goods_taste_layout:
                Intent tasteIntent = new Intent(this, GoodTasteActivity.class);
                tasteIntent.putExtra("inputContent", mProduct.getTasteDesc());
                startActivityForResult(tasteIntent, REQUEST_CODE_TASTE);
                break;
            //套餐商品
            case R.id.goods_category_layout:
                Intent categoryIntent = new Intent(this, SetmealGoodsActivity.class);
                categoryIntent.putExtra("inputContent", mProduct.getDescription());
                startActivityForResult(categoryIntent, REQUEST_CODE_CATEGORY);
                break;
            //分类(需要按照productgroup进行修改)
            case R.id.goods_classify_layout:
                DishClassifyDialog dishDialog = DishClassifyDialog.newInstance(mProductGroupList);
                dishDialog.showDialog(getFragmentManager());
                dishDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        int pos = 0;
                        try {
                            pos = Integer.parseInt(values[0]);
                        } catch (Exception e) {
                            pos = 0;
                        }
                        ProductGroupEntity productGroup = mProductGroupList.get(pos);
                        mGoodsClassifyText.setText(productGroup.getName());
                        custom_group_id = productGroup.getId();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            //单位
            case R.id.goods_unit_layout:
                Intent unitIntent = new Intent(this, GoodsUnitsActivity.class);
                String unitContent = mGoodsUnitText.getText().toString();
                unitIntent.putExtra("unitContent", unitContent);
                startActivityForResult(unitIntent, REQUEST_CODE_UNIT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_TASTE:
                if (!TextUtils.isEmpty(data.getStringExtra("inputContent"))) {
                    mProduct.setTasteDesc(data.getStringExtra("inputContent"));
                    mGoodsTasteText.setText("已编辑");
                } else {
                    mGoodsTasteText.setText("");
                }
                break;
            case REQUEST_CODE_CATEGORY:
                if (!TextUtils.isEmpty(data.getStringExtra("inputContent"))) {
                    mProduct.setDescription(data.getStringExtra("inputContent"));
                    mGoodsCategoryText.setText("已编辑");
                } else {
                    mGoodsCategoryText.setText("");
                }
                break;
            case REQUEST_CODE_RECOMMEND:
                boolean isRecommend = data.getBooleanExtra("isRecommend", false);
                if (isRecommend) {
                    mGoodsRecommendText.setText("已设置");
                } else {
                    mGoodsRecommendText.setText("");
                }

                break;
            case REQUEST_CODE_PHOTO:
                mGoodsPath = data.getStringExtra("path");
                mResouceId = data.getStringExtra("pic_id");
                if (!TextUtils.isEmpty(mGoodsPath)) {
                    mGoodsIcon.setVisibility(View.VISIBLE);
                    GlideLoader.getInstance().load(this, mGoodsIcon, mGoodsPath);
                }

                break;
            case REQUEST_CODE_UNIT:
                mGoodsUnitText.setText(data.getStringExtra("unitContent"));
                break;
        }

    }

    private void requestGoodsEdit() {
        Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        Long employeeId = UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId();
        Integer is_set_meal = mPacketSwitch.isSelected() ? 1 : 0;
        String product_name = mGoodsNameEdit.getText().toString();
        if (TextUtils.isEmpty(product_name)) {
            ToastUtil.show("菜品名称不能为空");
            return;
        }
        String originPriceStr = mOriginPriceEdit.getText().toString().trim();
        String salePriceStr = mSalePriceEdit.getText().toString().trim();
        if (TextUtils.isEmpty(salePriceStr)) {
            ToastUtil.show("实际售价不能为空");
            return;
        }
        String unit = mGoodsUnitText.getText().toString();
        if (TextUtils.isEmpty(unit)) {
            ToastUtil.show("单位不能为空");
            return;
        }


        String mealDesc = "";
        String tasteDesc = "";
        if (!TextUtils.isEmpty(mProduct.getDescription())) {
            mealDesc = mProduct.getDescription();
        }
        if (!TextUtils.isEmpty(mProduct.getTasteDesc())) {
            tasteDesc = mProduct.getTasteDesc();
        }

        Boolean is_recommend = TextUtils.isEmpty(mGoodsRecommendText.getText().toString()) ? false : true;

        long originPrice = 0;
        try {
            originPrice = (long) (Double.parseDouble(originPriceStr) * 100);
        } catch (NumberFormatException e) {
            originPrice = 0;
        }

        long salePrice = 0;
        try {
            salePrice = (long) (Double.parseDouble(salePriceStr) * 100);
        } catch (NumberFormatException e) {
            salePrice = 0;
        }
        Boolean enable_whole_shop_discount = mDiscountSwitch.isSelected() ? true : false;
        mPresenter.requestUpdateGoods(product_id, storeId, employeeId, mResouceId, is_set_meal, product_name,
                originPrice, salePrice, unit, custom_group_id, mealDesc, tasteDesc,
                is_recommend, enable_whole_shop_discount);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_goods_edit;
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
    public void onBackPressed() {
        exitEdit();
    }

    private void exitEdit() {
        ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认退出此次编辑?", null);
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void updateGoodsSuccess(GoodsUpdateEntity data) { //商品更新
//Log.e("wislie","logourl:"+data.getLogoPicUrl());
        mProduct.setName(mGoodsNameEdit.getText().toString().trim());
        mProduct.setLogoPicUrl(data.getLogoPicUrl());
        mProduct.setPicUrl(data.getPicUrl());
        //加载图片
        mProduct.setUnit(data.getUnit());
        mProduct.setDeleted(data.getDeleted());
        mProduct.setSoldOut(data.getSoldOut());
        mProduct.setState(data.getState());
        mProduct.setRecommend(data.getRecommend());
        mProduct.setPrice(data.getPrice());
        mProduct.setRealPrice(data.getRealPrice());
        mProduct.setCustomGroupId(data.getCustomGroupId());
        mProduct.setEnableWholeShopDiscount(data.getEnableWholeShopDiscount());
        mProduct.setTasteDesc(data.getTasteDesc());
        mProduct.setSetMeal(data.getSetMeal());

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.CONFIG_PRODUCT, mProduct);
        BaseEventManager.post(bundle, GoodsDetailActivity.class.getName());
        finish();
    }

    @Override
    public void updateGoodsFailed() {

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
    }
}
