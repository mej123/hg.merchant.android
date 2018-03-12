package com.zishan.sardinemerchant.activity.page;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.GroupItem;
import com.example.wislie.rxjava.model.page.ToolGrantEntity;
import com.example.wislie.rxjava.model.page.ToolItem;
import com.example.wislie.rxjava.presenter.base.page.main.ToolPresenter;
import com.example.wislie.rxjava.view.base.page.ToolView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.ToolsGroupAdapter;
import com.zishan.sardinemerchant.utils.SkipUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * 基础工具
 * Created by wislie on 2017/12/29.
 */

public class AllToolsActivity extends BActivity<ToolView, ToolPresenter> implements ToolView {

    @BindView(R.id.base_recycler)
    MaxRecyclerView mRecycler;

    private ToolsGroupAdapter mAdapter;

    private List<GroupItem> mDataList = new ArrayList<>();

    @Override
    protected ToolPresenter createPresenter() {
        return new ToolPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_all_tools;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.all_tools));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ToolsGroupAdapter(this, mDataList);
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnMultiItemListener(new ToolsGroupAdapter.OnMultiItemListener() {
            @Override
            public void onItemClick(int parentPos, int childPos) {
                if (parentPos >= mDataList.size()) return;
                GroupItem groupItem = mDataList.get(parentPos);
                if (groupItem == null) return;
                List<ToolItem> dataList = groupItem.getItems();
                if (dataList == null || dataList.size() == 0 || childPos >= dataList.size()) return;
                ToolItem data = dataList.get(childPos);
                if (data == null) return;


                ToolGrantEntity grantTool = UserConfig.getInstance(ClientApplication.getApp()).
                        getGrantTool(data.getModuleId());
                if(data == null) return;
                if(grantTool.getIsGrant() == 0){
                    //未开通
                    showGrantDialog(data.getTitle(),"未开通");
                    return;
                }
                if(grantTool.getIsJurisdiction() == 0){
                    //有权限
                    showPermisssionDialog(data.getTitle());
                    return;
                }

                //统计
                Map<String, String> map = new HashMap<>();
                map.put(data.getTitle(), data.getTitle());
                MobclickAgent.onEventValue(AllToolsActivity.this, Constant.UMENG_EVENT_1, map, 1);
                SkipUtil.skipActivity(AllToolsActivity.this, data.getPage());
            }
        });

        mPresenter.getToolGroups(Constant.TOOL_URL);
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
    public void getGroupToolsSuccess(List<GroupItem> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getGroupToolsFailed() {

    }
}
