package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.model.page.TableStartEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.presenter.base.page.table_detail.TableStartPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.TableStartView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 桌台开台
 * Created by wislie on 2017/11/29.
 */

public class TableStartActivity extends BActivity<TableStartView, TableStartPresenter> implements TableStartView {


    @BindView(R.id.table_name)
    TextView mTableNameText;
    @BindView(R.id.table_person_num)
    EditText mPersonNumText;

    private StoreSeatEntity mSeatData;

    @Override
    protected TableStartPresenter createPresenter() {
        return new TableStartPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_start;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        //落座数据
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionbarTitle(getResources().getString(R.string.table_start));

    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            mSeatData = intent.getParcelableExtra("seat_data");
        }

        if (mSeatData != null) {
            mTableNameText.setText(mSeatData.getSeatName());
            if (mSeatData.getSeatNum() != null && mSeatData.getSeatNum() != 0) {
                mPersonNumText.setText(String.valueOf(mSeatData.getSeatNum()));
            }
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
    public void startTableSuccess(TableStartEntity data) {
        Intent intent = new Intent(this, TableDetailActivity.class);
        intent.putExtra("repast_id", data.getRepastId());
        startActivity(intent);
        finish();
    }

    @Override
    public void startTableFailed() {

    }

    @OnClick({R.id.table_start_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            //确认开台
            case R.id.table_start_confirm:
                String personNum = mPersonNumText.getText().toString();

                if (TextUtils.isEmpty(personNum)) {
                    ToastUtil.show("请输入人数");
                    return;
                }

                int dinner_num = 0;
                try {
                    dinner_num = Integer.parseInt(personNum);
                } catch (Exception e) {
                    dinner_num = 0;
                }

                if (dinner_num == 0) {
                    ToastUtil.show("输入人数不能为0");
                    return;
                }

                mPresenter.requestStartTable(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        dinner_num, mSeatData.getId(), mSeatData.getRepastLocationKey());

                break;
        }
    }
}
