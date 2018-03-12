package com.zishan.sardinemerchant.activity.page;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.entity.SelectedDishBean;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftasbase.eventbus.BaseEventManager;

/**
 * Created by yang on 2017/12/4.
 * <p>
 * 添加菜品
 */

public class AddDishesActivity extends BActivity {


    @BindView(R.id.dishes_name_edit)
    EditText mDishNameEdit;
    @BindView(R.id.menu_price_edit)
    EditText mDishPriceEdit;
    @BindView(R.id.dish_num)
    TextView mDishNumText;
    @BindView(R.id.total_price)
    TextView mTotalPriceText;


    private int mDishNum;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionbarTitle(getString(R.string.add_dishes));
        setActionBarMenuIcon(-1);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_dishes;
    }

    @Override
    protected void initContentView() {
        if(mDishNum == 0){
            mDishNum = 1;
        }
        mDishNumText.setText(String.valueOf(mDishNum));
        mDishPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StringUtil.pointDigitLimited(s, 2);

                String input = s.toString();
                if(!TextUtils.isEmpty(input)){
                    long cent = StringUtil.String2Long(input, 2, true);
                    String totalPrice = StringUtil.point2String(cent * mDishNum);
                    mTotalPriceText.setText(totalPrice);
                }else{
                    mTotalPriceText.setText("0.00");
                }

            }
        });
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
                updateDish();
                break;

            case R.id.plus_icon:
                mDishNum++;
                updateDish();
                break;
            //添加菜品确认
            case R.id.add_dish_confirm:
                String dishName = mDishNameEdit.getText().toString();
                if(TextUtils.isEmpty(dishName)){
                    ToastUtil.show("请输入菜名");
                    return;
                }

                String dishPrice = mDishPriceEdit.getText().toString();
                if(TextUtils.isEmpty(dishPrice)){
                    ToastUtil.show("请输入价格");
                    return;
                }

                if(mDishNum < 1){
                    ToastUtil.show("数量需大于0");
                    return;
                }

                SelectedDishBean data = new SelectedDishBean(mDishNum,
                        StringUtil.String2Long(dishPrice, 2, true), dishName);
                Bundle bundle = new Bundle();
                bundle.putSerializable("extra_dish", data);
                BaseEventManager.post(bundle, Constant.DISH_ADD, ChooseDishActivity.class.getName());
                finish();
                break;
        }

    }

    private void updateDish() {
        mDishNumText.setText(String.valueOf(mDishNum));
        String input = mDishPriceEdit.getText().toString();
        if(!TextUtils.isEmpty(input)){
            long cent = StringUtil.String2Long(input, 2, true);
            String totalPrice = StringUtil.point2String(cent * mDishNum);
            mTotalPriceText.setText(totalPrice);
        }

    }
}
