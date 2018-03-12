package com.zishan.sardinemerchant.fragment.personal;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.personal.SellerEntranceActivity;
import com.zishan.sardinemerchant.activity.personal.UserFeedbackActivity;
import com.zishan.sardinemerchant.activity.personal.customer_service.ChatActivity;
import com.zishan.sardinemerchant.activity.personal.setting.AboutUsActivity;
import com.zishan.sardinemerchant.activity.personal.setting.CurAccountActivity;
import com.zishan.sardinemerchant.activity.personal.setting.SwitchUserActivity;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.utils.Skip;


import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GlideLoader;
import top.ftas.ftasbase.common.util.StringUtil;


/**
 * 个人
 * Created by yang on 2017/9/12.
 */

public class MainPersonFragment extends BFragment {

    @BindView(R.id.my_house)
    TextView mMyHouseText;
    @BindView(R.id.store_layout)
    LinearLayout mStoreLayout;
    @BindView(R.id.store_name)
    TextView mStoreNameText;
    @BindView(R.id.role_name)
    TextView mRoleNameText;
    @BindView(R.id.personal_avatar)
    ImageView mAvatarIcon;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_person;
    }

    @Override
    protected void initBizView() {

    }

    private void updateData() {
        boolean isOwner = UserConfig.getInstance(ClientApplication.getApp()).isOwner();
        if (isOwner) {
            mMyHouseText.setVisibility(View.VISIBLE);
            mStoreLayout.setVisibility(View.GONE);
        } else {
            mMyHouseText.setVisibility(View.GONE);
            mStoreLayout.setVisibility(View.VISIBLE);
            mRoleNameText.setText(UserConfig.getInstance(ClientApplication.getApp()).getRoleName());
            if(UserConfig.getInstance(ClientApplication.getApp()).getStoreId() == 0){
                mStoreNameText.setText(UserConfig.getInstance(ClientApplication.getApp()).getMerchantName());
            }else{
                mStoreNameText.setText(UserConfig.getInstance(ClientApplication.getApp()).getStoreName());
            }

        }
        GlideLoader.getInstance().load(ClientApplication.getApp(), mAvatarIcon,
                StringUtil.appendHttps(UserConfig.getInstance(ClientApplication.getApp()).getPersonalAvatar()),
                R.mipmap.avatar_placeholder_icon, true);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @OnClick({R.id.store_info_layout, R.id.rl_account_layout, R.id.rl_contact_layout,
            R.id.rl_feedback_layout, R.id.rl_about_layout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            //切换身份
            case R.id.store_info_layout:
                Intent identityIntent = new Intent(getActivity(), SwitchUserActivity.class);
                startActivity(identityIntent);
                break;
            //当前账号
            case R.id.rl_account_layout:
                Skip.toActivity(getActivity(), CurAccountActivity.class);
                break;
            //联系我们
            case R.id.rl_contact_layout:
                ChatActivity.createRandomAccountThenLoginChatServer(getActivity());
                break;
            //意见反馈
            case R.id.rl_feedback_layout:

//                Skip.toActivity(getActivity(), SellerEntranceActivity.class);
                Skip.toActivity(getActivity(), UserFeedbackActivity.class);
                break;
            //关于我们
            case R.id.rl_about_layout:
                Skip.toActivity(getActivity(), AboutUsActivity.class);//关于我们
                break;
        }

    }

}
