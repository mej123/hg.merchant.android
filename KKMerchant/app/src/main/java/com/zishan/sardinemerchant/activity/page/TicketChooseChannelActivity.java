package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择分销渠道
 * Created by wislie on 2018/1/20.
 */

public class TicketChooseChannelActivity extends BActivity {

    @BindView(R.id.channel_icon)
    ImageView mChannelIcon;
    @BindView(R.id.channel_confirm)
    TextView mChannelConfirmText;


    //是否是全部分销商, 默认不是
    private int distribution_way;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_choose_channel;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.all_seller));
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        setActionBarHomeIcon(R.mipmap.back_black_icon);

    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent == null) return;
        distribution_way = intent.getIntExtra("distribution_way", distribution_way);
        if(distribution_way == 0){
            mChannelIcon.setSelected(false);
        }else if(distribution_way == 1){
            mChannelIcon.setSelected(true);
        }
        mChannelConfirmText.setSelected(true);
//        mChannelConfirmText.setSelected(mChannelIcon.isSelected());
    }


    @OnClick({R.id.channel_icon, R.id.channel_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.channel_icon:
//                mChannelIcon.setSelected(!mChannelIcon.isSelected());
//                mChannelConfirmText.setSelected(mChannelIcon.isSelected());
                break;
            case R.id.channel_confirm:
//                if (mChannelConfirmText.isSelected()) {
                    Intent intent = new Intent();
                    if(mChannelIcon.isSelected()){
                        intent.putExtra("distribution_way", 1);
                    }else{
                        intent.putExtra("distribution_way", 0);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
//                }
                break;
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
    }
}
