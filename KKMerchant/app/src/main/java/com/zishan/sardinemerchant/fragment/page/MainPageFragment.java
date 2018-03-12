package com.zishan.sardinemerchant.fragment.page;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.model.page.GroupItem;
import com.example.wislie.rxjava.model.page.OpenStoresNumEntity;
import com.example.wislie.rxjava.model.page.ToolGrantEntity;
import com.example.wislie.rxjava.model.page.ToolItem;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.page.main.MainPagePresenter;
import com.example.wislie.rxjava.view.base.page.MainPageView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.AllToolsActivity;
import com.zishan.sardinemerchant.activity.page.MyStoreActivity;
import com.zishan.sardinemerchant.activity.page.PaymentRecipientActivity;
import com.zishan.sardinemerchant.activity.page.QRcodeActivity;
import com.zishan.sardinemerchant.activity.personal.order.StoreOrderActivity;
import com.zishan.sardinemerchant.activity.personal.setting.SwitchUserActivity;
import com.zishan.sardinemerchant.adapter.page.ToolsAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.utils.SkipUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SpannableStringUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.view.MaxRecyclerView;


/**
 * 首页
 * Created by wislie on 2017/10/12.
 */

public class MainPageFragment extends BFragment<MainPageView, MainPagePresenter>
        implements MainPageView {

    @BindView(R.id.title_tv)
    TextView mTitleText;
    @BindView(R.id.main_page_recycler)
    MaxRecyclerView mRecycler;
    @BindView(R.id.store_num)
    TextView mStoreNumText;
    private ToolsAdapter mAdapter;
    private List<ToolItem> mDataList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_page;
    }

    @Override
    protected void initBizView() {
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAdapter = new ToolsAdapter(getActivity(), mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new CommonAdapter.OnAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                if (position >= mDataList.size()) return;
                if (position == mDataList.size() - 1) {
                    Intent intent = new Intent(getActivity(), AllToolsActivity.class);
                    startActivity(intent);
                    return;
                }
                ToolItem toolItem = mDataList.get(position);
                if (toolItem == null) return;

                ToolGrantEntity data = UserConfig.getInstance(ClientApplication.getApp()).
                        getGrantTool(toolItem.getModuleId());
                if (data == null) return;
                if (data.getIsGrant() == 0) {
                    //未开通
                    showGrantDialog(toolItem.getTitle(),"未开通");
                    return;
                }
                if (data.getIsJurisdiction() == 0) {
                    //有权限
                    showPermisssionDialog(toolItem.getTitle());
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put(toolItem.getTitle(), toolItem.getTitle());
                MobclickAgent.onEventValue(getActivity(), Constant.UMENG_EVENT_1, map, 1);
                SkipUtil.skipActivity(getActivity(), toolItem.getPage());
            }
        });
    }

    @Override
    protected MainPagePresenter createPresenter() {
        return new MainPagePresenter(getActivity(), this);
    }

    @Override
    protected void loadData() {
        mPresenter.getToolGroups(Constant.TOOL_URL);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTitleText != null) {
            if (UserConfig.getInstance(ClientApplication.getApp()).getStoreId() == 0) {
                mTitleText.setText(UserConfig.getInstance(ClientApplication.getApp()).getMerchantName());
            } else {
                mTitleText.setText(UserConfig.getInstance(ClientApplication.getApp()).getStoreName());
            }
        }

        mPresenter.getOpenStoresNum();

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


    @OnClick({R.id.purchase_checkout, R.id.order_info,
            R.id.title_tv, R.id.scan_icon, R.id.store_open_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //买单收银
            case R.id.purchase_checkout:

                ToolGrantEntity data = UserConfig.getInstance(ClientApplication.getApp()).
                        getGrantTool(201001l);
                if (data == null) return;
                if (data.getIsGrant() == 0) {
                    //未开通
                    showGrantDialog("收银验券功能","未开通");
                    return;
                }
                if(data.getIsJurisdiction() == 0) {
                    //有权限
                    showPermisssionDialog("收银验券");
                    return;
                }
                Intent recipientIntent = new Intent(getActivity(), PaymentRecipientActivity.class);
                startActivity(recipientIntent);
                break;

            //订单信息
            case R.id.order_info:
                if (!permissionAllowed(3)) {
                    showPermisssionDialog("订单查看功能权限");
                } else {
                    Intent orderIntent = new Intent(getActivity(), StoreOrderActivity.class);
                    startActivity(orderIntent);
                }
                break;

            //切换商店
            case R.id.title_tv:
                Intent titleIntent = new Intent(getActivity(), SwitchUserActivity.class);
                startActivity(titleIntent);
                break;
            //扫一扫
            case R.id.scan_icon:
                Intent intent = new Intent(getActivity(), QRcodeActivity.class);
                startActivity(intent);
                break;
            //点击查看
            case R.id.store_open_layout:
                Intent mystoreIntent = new Intent(getActivity(), MyStoreActivity.class);
                mystoreIntent.putExtra("state", 5);
                startActivity(mystoreIntent);
                break;
        }
    }


    //显示开店中的门店数量
    private void showStoreOpeningNum(int num) {
        if (mStoreNumText != null) {
            mStoreNumText.setText(SpannableStringUtil.setSpannableColor("您有" + num + "家店铺",
                    2, 2 + String.valueOf(num).length(),
                    ContextCompat.getColor(ClientApplication.getApp(), R.color.text_color_black_7),
                    ContextCompat.getColor(ClientApplication.getApp(), R.color.bg_color_blue_14)));
        }
    }


    @Override
    public void getUserAccountSuccess(List<UserAccountEntity> dataList) {

    }

    @Override
    public void getUserAccountFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void getStoreMsgSuccess(StoreMsgEntity data) {

    }

    @Override
    public void getStoreMsgFailed() {

    }

    @Override
    public void getGroupToolsSuccess(List<GroupItem> dataList) {
        mDataList.clear();
        if (dataList == null) return;
        for (int i = 0; i < dataList.size(); i++) {
            GroupItem groupItem = dataList.get(i);
            if (groupItem == null) continue;
            List<ToolItem> items = groupItem.getItems();
            for (int j = 0; j < items.size(); j++) {
                ToolItem toolItem = items.get(j);
                if (toolItem != null && toolItem.getHome() == Boolean.TRUE) {
                    mDataList.add(items.get(j));
                }
            }
        }
        Collections.sort(mDataList, new Comparator<ToolItem>() {
                    @Override
                    public int compare(ToolItem o1, ToolItem o2) {

                        return o1.getOrder().compareTo(o2.getOrder());
                    }
                }
        );

        mDataList.retainAll(mDataList.subList(0, mDataList.size() > 7 ? 7 : mDataList.size()));
        mDataList.add(new ToolItem("更多", "more_icon"));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void getGroupToolsFailed() {

    }

    @Override
    public void getOpenStoresNumSuccess(OpenStoresNumEntity data) {
        if (data == null) {
            showStoreOpeningNum(0);
            return;
        }
        showStoreOpeningNum(data.getStoreQua());
    }

    @Override
    public void getOpenStoresNumFailed() {
        showStoreOpeningNum(0);
    }

}
