package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.view.CustomEditText;

/**
 * 套餐商品
 * Created by yang on 2017/9/30.
 */

public class SetmealGoodsActivity extends BActivity {
    @BindView(R.id.custom_edit_text)
    CustomEditText mInputContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setmeal_goods;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.goods_category));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //键盘缩回
                SoftInputUtil.hideDirectly(SetmealGoodsActivity.this);
                finish();
            }
        });
        setActionBarMenuIcon(-1);
        //标题栏菜单按钮
        setActionBarMenuText("保存").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //键盘缩回
                SoftInputUtil.hideDirectly(SetmealGoodsActivity.this);
                //商品套餐中输入的内容
                String inputContent = mInputContent.getInputEdit().getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("inputContent", inputContent);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            String inputContent = intent.getStringExtra("inputContent");
            EditText inputEdit = mInputContent.getInputEdit();
            inputEdit.setText(inputContent);
            inputEdit.setSelection(inputEdit.getText().toString().length());
        }
    }


    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.left_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                break;
        }
    }
}
