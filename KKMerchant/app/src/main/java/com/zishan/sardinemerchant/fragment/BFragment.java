package com.zishan.sardinemerchant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.PermissionEntity;
import com.example.wislie.rxjava.model.page.PermissionGroupsEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.view.base.BView;
import com.hg.ftas.fragment.FtasMobileFragment;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.RootGroup;
import com.zishan.sardinemerchant.activity.logins.NewPswLoginActivity;
import com.zishan.sardinemerchant.dialog.GrantDialog;
import com.zishan.sardinemerchant.dialog.LoadingDialog;
import com.zishan.sardinemerchant.dialog.PermissionDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.dunit.annotation.DUnitHidden;
import top.ftas.ftasbase.common.util.ActivityManager;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.StatusBarUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftasbase.eventbus.BaseEventObserver;

/**
 * Created by wislie on 17/11/12.
 */

@DUnitHidden
@DUnit(group = RootGroup.OtherGroup.class)
public abstract class BFragment<V extends BView, T extends BPresenter<V>> extends
        FtasMobileFragment implements BaseEventObserver {

    /**
     * 控件是否初始化完成
     */
    private boolean isViewCreated;
    /**
     * 数据是否已加载完毕
     */
    protected boolean isLoadDataCompleted;

    protected T mPresenter;
    protected Unbinder mUnbinder;

    protected String ACTION_NAME = this.getClass().getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏
        StatusBarUtil.transparencyBar(getActivity());
        StatusBarUtil.StatusBarLightMode(getActivity());
        StatusBarUtil.setStatusBarTranslucent(getActivity(), true);

        //创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        //注册EventBus
        BaseEventManager.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(getLayoutResId(), container, false);
        //绑定控件
        mUnbinder = ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
//        initActionBar();
        //初始化View和数据
        initBizView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            isLoadDataCompleted = true;
            loadData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            isLoadDataCompleted = true;
            loadData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除presenter与view之间的关系
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        //解绑
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        //销毁EventBus
        BaseEventManager.unregister(this);
    }

    //没有更多了
    protected View showFooterNoMoreData() {
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer_no_more, null);
        footerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return footerView;
    }



    /**
     * 判断是否可以继续往下滑动
     *
     * @param recyclerView
     * @param direction    1表示是否能向上滚动, -1表示是否能向下滚动
     * @param footerView   底部视图
     * @return
     */
    protected boolean canScrollVertical(RecyclerView recyclerView, int direction, View footerView) {
        int measuredHeight = DensityUtil.getMeasuredHeight(footerView);
        int offset = recyclerView.computeVerticalScrollOffset();
        int scrollRange = recyclerView.computeVerticalScrollRange();
        int scrollExtent = recyclerView.computeVerticalScrollExtent();
        int range = scrollRange - scrollExtent + measuredHeight;
        if (range == 0) return false;
        if (direction < 0) return offset > 0;
        return offset < range - 1;
    }


    private LoadingDialog mDialog;


    /**
     * 显示正在加载。。。
     */
    public void showLoadingDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
            return;
        }

        mDialog = LoadingDialog.newInstance();
        mDialog.showDialog(getActivity().getFragmentManager());
    }


    /**
     * 取消加载
     */
    public void dismissLoadingDialog() {

        if (mDialog != null && mDialog.getActivity() != null && !mDialog.getActivity().isFinishing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null && this != null){
                        Activity activity = ActivityManager.getInstance().getLastActivity();
                        mDialog.dismiss(activity.getFragmentManager());
                    }
                    mDialog = null;
                }
            }, 300);

        }
    }

    //重新登录
    protected void reOnLogin() {
        UserConfig.getInstance(ClientApplication.getApp()).clearAll();
        Activity activity = ActivityManager.getInstance().getLastActivity();
        if(activity != null && !activity.isFinishing()){
            Intent intent = new Intent(activity, NewPswLoginActivity.class);
            startActivity(intent);
            ActivityManager.getInstance().finishAllActivity();
        }

    }

    /**
     * 未开通dialog提示
     * @param title
     */
    protected void showGrantDialog(String title, String content){
        GrantDialog dialog = GrantDialog.newInstance(title, content);
        dialog.showDialog(getActivity().getFragmentManager());
    }

    /**
     * 权限dialog提示
     * @param title
     */
    protected void showPermisssionDialog(String title){
        PermissionDialog dialog = PermissionDialog.newInstance(title);
        dialog.showDialog(getActivity().getFragmentManager());
    }

    //true表示可以查看
    protected boolean permissionAllowed(int index) {
        ArrayList<Boolean> dataList = UserConfig.getInstance(ClientApplication.getApp()).getPermission();
        if (dataList != null && dataList.size() == 15) {
            Boolean data = dataList.get(index);
            if (data == Boolean.TRUE) return true;
            return false;
        }
        return true;
    }

    //设置权限列表
    protected void setPermissionList(List<PermissionGroupsEntity> permissionList) {
        if (permissionList == null || permissionList.size() == 0) return;
        ArrayList<Boolean> boolList = new ArrayList();
        for (int j = 0; j < permissionList.size(); j++) {
            PermissionGroupsEntity data = permissionList.get(j);
            List<PermissionEntity> pList = data.getPermissions();
            for (int k = 0; k < pList.size(); k++) {
                PermissionEntity pData = pList.get(k);
                ////0为启用，1为禁用，2为未设置
                int isSelected = pData.getIsSelected();
                if (isSelected == 0) {
                    boolList.add(true);
                } else {
                    boolList.add(false);
                }
            }
        }
        UserConfig.getInstance(ClientApplication.getApp()).setPermission(boolList);
    }



    protected abstract int getLayoutResId();

    //加载视图
    protected abstract void initBizView();

    protected abstract T createPresenter();

    //加载数据
    protected abstract void loadData();


    @Subscribe
    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventPost(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventBackground(BaseEvent baseEvent) {

    }

    @Subscribe
    @Override
    public void onMessageEventAsync(BaseEvent baseEvent) {

    }

}
