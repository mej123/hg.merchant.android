package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 卡券库存
 * Created by wislie on 2018/1/20.
 */

public class TicketRepositoryActivity extends BActivity {

    @BindView(R.id.repository_icon)
    ImageView mRepositoryIcon;
    @BindView(R.id.repository_num_layout)
    RelativeLayout mRepositoryNumLayout;
    @BindView(R.id.repository_confirm)
    TextView mRepositoryConfirmText;
    @BindView(R.id.ticket_num)
    EditText mTicketNumText;

    //卡券库存数量
    private long stock = -1;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_repository;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.ticket_store_num));
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        setActionBarHomeIcon(R.mipmap.back_black_icon);

    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent == null) return;
        stock = intent.getLongExtra("stock", stock);
        if(stock >= 0){
            if(stock > 0){
                mTicketNumText.setText(String.valueOf(stock));
            }
            mRepositoryIcon.setSelected(false);
        }else{
            mRepositoryIcon.setSelected(true);
        }

        updateRepositoryLayout(mRepositoryIcon.isSelected());
        mTicketNumText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateRepositoryLayout(mRepositoryIcon.isSelected());
            }
        });
    }

    private void updateRepositoryLayout(boolean isSelected) {
        if (isSelected) {
            mRepositoryNumLayout.setVisibility(View.INVISIBLE);
            mRepositoryConfirmText.setSelected(true);
            stock = -1;
        } else {
            mRepositoryNumLayout.setVisibility(View.VISIBLE);

            String ticketNumStr = mTicketNumText.getText().toString().trim();
            if (TextUtils.isEmpty(ticketNumStr)) {
                mRepositoryConfirmText.setSelected(false);
                return;
            }
            try {
                int repositoryNum = Integer.parseInt(ticketNumStr);
                if (repositoryNum <= 0) {
                    mRepositoryConfirmText.setSelected(false);
                } else {
                    stock = repositoryNum;
                    mRepositoryConfirmText.setSelected(true);
                }
            } catch (NumberFormatException e) {
                stock = -1;
                mRepositoryConfirmText.setSelected(false);
            }

        }
    }

    @OnClick({R.id.repository_icon, R.id.repository_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            //不限库存
            case R.id.repository_icon:
                mRepositoryIcon.setSelected(!mRepositoryIcon.isSelected());
                updateRepositoryLayout(mRepositoryIcon.isSelected());
                break;
            //确定
            case R.id.repository_confirm:
                if (mRepositoryConfirmText.isSelected()) {
                    Intent intent = new Intent();
                    intent.putExtra("stock",stock);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }


    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }
}
