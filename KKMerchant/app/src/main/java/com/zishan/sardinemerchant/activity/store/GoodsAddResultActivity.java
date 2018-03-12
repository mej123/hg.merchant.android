package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.MainPageActivity;

import java.util.ArrayList;

import butterknife.OnClick;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.eventbus.BaseEventManager;

/**
 * Created by yang on 2017/9/30.
 * 商品添加成功
 */

public class GoodsAddResultActivity extends BActivity {

    //分类列表
    private ArrayList<ProductGroupEntity> mProductGroupList = new ArrayList<>();
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_add_result;
    }


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarDivderVisible(false);
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if(intent != null){
            ArrayList<ProductGroupEntity> productGroups =
                    intent.getParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP);
            if(productGroups != null){
                mProductGroupList.addAll(productGroups);
            }
        }

    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.continue_add_goods, R.id.add_goods_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_add_goods:
                Intent intent = new Intent(this, StoreGoodsAddActivity.class);
                intent.putParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroupList);
                startActivity(intent);
                finish();
                break;
            //完成
            case R.id.add_goods_complete:
                finish();
                break;
        }

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
