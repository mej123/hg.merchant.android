package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsDetailPresenter;
import com.example.wislie.rxjava.view.base.store.goods.GoodsDetailView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.fragment.store.GoodsListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * Created by yang on 2017/9/29.
 * 商品详情
 */

public class GoodsDetailActivity extends BActivity<GoodsDetailView, GoodsDetailPresenter>
        implements GoodsDetailView {

    @BindView(R.id.goods_icon)
    ImageView mGoodsIcon;

    @BindView(R.id.tv_goods_name)
    TextView mGoodsName;

    @BindView(R.id.tv_goods_discount)
    TextView mGoodsDiscount;

    @BindView(R.id.tv_goods_price)
    TextView mGoodsPrice;

    @BindView(R.id.tv_goods_original_price)
    TextView mGoodsOriginalPrice;

    @BindView(R.id.goods_state)
    TextView mGoodsState;

    @BindView(R.id.goods_on)
    TextView mGoodsOn;

    //分类列表
    private ArrayList<ProductGroupEntity> mProductGroupList = new ArrayList<>();

    //商品实体类
    private ProductEntity mProduct;


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.goods_detical));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        //标题栏菜单按钮
        setActionBarMenuText("编辑").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mProduct == null) return;
                Intent intent = new Intent(GoodsDetailActivity.this, StoreGoodsEditActivity.class);
                intent.putExtra(Constant.CONFIG_PRODUCT, mProduct);
                intent.putExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroupList);
                startActivity(intent);
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
                finish();
            }
        });

    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            mProduct = intent.getParcelableExtra(Constant.CONFIG_PRODUCT);
            ArrayList<ProductGroupEntity> dishList =intent.getParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP);
            if (dishList != null && dishList.size() > 0) {
                mProductGroupList.addAll(dishList);
            }
        }


        updateProduct();
    }

    //更新商品
    private void updateProduct() {
        if (mProduct == null) return;
        //加载图片
        if(!TextUtils.isEmpty(mProduct.getPicUrl())){
            GlideLoader.getInstance().load(this, mGoodsIcon, StringUtil.appendHttps(mProduct.getPicUrl()));
        }

        mGoodsName.setText(mProduct.getName());
        mGoodsDiscount.setText(mProduct.getTitle());
        if(mProduct.getDiscount()!= null && mProduct.getDiscount() == Boolean.TRUE){
            mGoodsPrice.setText("¥ " + StringUtil.point2String(mProduct.getStrategyPrice()) );
        }else{
            mGoodsPrice.setText("¥ " + StringUtil.point2String(mProduct.getRealPrice()) );
        }
        mGoodsOriginalPrice.setText("¥ " + StringUtil.point2String(mProduct.getPrice()));
        mGoodsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        updateGoodsState();
    }

    //更新商品状态
    private void updateGoodsState() {
        if (mProduct.getState() == 0) {
            if (mProduct.getSoldOut()) {
                mGoodsState.setText("售空");
                mGoodsState.setBackgroundResource(R.drawable.goods_detail_off_sale_shape);
            } else {
                mGoodsState.setText("在售");
                mGoodsState.setBackgroundResource(R.drawable.goods_detail_on_sale_shape);
            }
            mProduct.setSoldOut(!mProduct.getSoldOut());
            mGoodsOn.setSelected(true);//设置下架为选中状态
            mGoodsOn.setText("下架");

        } else if (mProduct.getState() == 1) {
            mGoodsState.setText("下架");
            mGoodsState.setBackgroundResource(R.drawable.goods_detail_off_sale_shape);

            mGoodsOn.setSelected(false);//设置上架为未选中状态
            mGoodsOn.setText("上架");

        }
    }


    @OnClick({R.id.goods_delete, R.id.goods_on})
    public void onClick(View view) {
        switch (view.getId()) {
            //删除
            case R.id.goods_delete:
                if (mProduct == null) return;
                mPresenter.requestDeleteGoods(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        mProduct.getId(), UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId());
                break;
            //上架
            case R.id.goods_on:

                if (!mGoodsOn.isSelected()) {
                    ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("重新上架该商品", null);
                    dialog.showDialog(getFragmentManager());
                    dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                        @Override
                        public void onConfirm() {
                            if (mProduct == null) return;
                            mPresenter.requestPutGoods(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), mProduct.getId(), 0,
                                    UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(), 0);
                        }

                        @Override
                        public void onInputConfirm(String... values) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                } else {
                    //下架
                    if (mProduct == null) return;
                    mPresenter.requestPutGoods(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), mProduct.getId(), 1,
                            UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(), 0);
                }

                break;
        }
    }

    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this, this);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_detail;
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void putGoodsSuccess(int position) {
        if (mProduct == null) return;
        if (mProduct.getState() == 0) {
            mProduct.setState(1);
        } else if (mProduct.getState() == 1) {
            mProduct.setState(0);
        }
        updateGoodsState();
        post();
    }

    @Override
    public void putGoodsFailed() {

    }

    @Override
    public void deleteGoodsSuccess(Object obj) {
        mProduct.setDeleted(true);
        CustomToast.makeText(this, "删除商品成功", 2000).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                post();
                finish();
            }
        }, 2000);
    }

    @Override
    public void deleteGoodsFailed() {

    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            mProduct = bundle.getParcelable(Constant.CONFIG_PRODUCT);
            updateProduct();
        }
    }


    //发送
    private void post(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.CONFIG_PRODUCT, mProduct);
        //商品编辑
        bundle.putInt(Constant.CONFIG_PRODUCT_STATE, Constant.GOODS_UPDATE);
        BaseEventManager.post(bundle, GoodsListFragment.class.getName());
    }

    @Override
    public void onBackPressed() {
        post();
        super.onBackPressed();
    }
}
