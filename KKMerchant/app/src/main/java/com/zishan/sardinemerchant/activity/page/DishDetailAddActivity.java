package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.fragment.page.DishListFragment;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftasbase.eventbus.BaseEventManager;

/**
 * Created by yang on 2017/12/4.
 * <p>
 * 菜品添加
 */

public class DishDetailAddActivity extends BActivity {


    @BindView(R.id.dish_price)
    TextView mDishPriceText;
    @BindView(R.id.dish_num)
    TextView mDishNumText;
    @BindView(R.id.total_price)
    TextView mDishTotalPriceText;

    private ProductEntity mDishData;

    private int mDishNum;

    private int mPosition = -1;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", mPosition);
            mDishData = intent.getParcelableExtra("dish_data");
            setActionbarTitle(mDishData.getName());
        }

        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_dish_detail_add;
    }

    @Override
    protected void initContentView() {

        if (mDishData != null) {
            mDishNum = mDishData.getSelectedCount();
            mDishPriceText.setText(
                    StringUtil.point2String(mDishData.getRealPrice()));
            updateDish(true, mDishNum);
        }

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.reduce_icon, R.id.plus_icon, R.id.add_dish_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reduce_icon:
                if (mDishNum > 0) {
                    mDishNum--;
                }
                updateDish(false, mDishNum);
                break;

            case R.id.plus_icon:
                mDishNum++;
                updateDish(false, mDishNum);
                break;
            //添加菜品确认
            case R.id.add_dish_confirm:
                mDishData.setSelectedCount(mDishNum);
                Bundle data = new Bundle();
                data.putParcelable("dish_data", mDishData);
                data.putInt("position", mPosition);
                BaseEventManager.post(data, DishListFragment.class.getName());
                BaseEventManager.post(ChooseDishActivity.class.getName());
                finish();
                break;
        }

    }

    private void updateDish(boolean isInit, int dishNum) {
        if(isInit && dishNum == 0){
            mDishNum = 1;
        }
        mDishNumText.setText(String.valueOf(mDishNum));
        mDishTotalPriceText.setText(StringUtil.point2String(mDishData.getRealPrice() * mDishNum));
    }
}
