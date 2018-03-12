package com.zishan.sardinemerchant.activity.store;


import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.GoodsClassifyEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.classify.ClassifyManagePresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.store.classify.ClassifyManageView;
import com.zishan.sardinemerchant.ClientApplication;

import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.store.ClassifyManageEditAdapter;
import com.zishan.sardinemerchant.dialog.SettingInputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewFooter;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 分类管理编辑
 * Created by wislie on 2017/9/29.
 */

public class ClassifyManageEditActivity extends BActivity<ClassifyManageView, ClassifyManagePresenter>
        implements ClassifyManageView {

    @BindView(R.id.classify_manage_edit_springview)
    SpringView mSpringView;

    @BindView(R.id.classify_manage_edit_recycler_view)
    RecyclerView mRecycler;

    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    private ClassifyManageEditAdapter mAdapter;
    private List<ProductGroupEntity> mDataList = new ArrayList<>();

    @Override
    public void initActionBar() {

        super.initActionBar();
        setActionbarTitle(getString(R.string.classify_manage));
        setActionBarHomeIcon(R.mipmap.back_white_icon);

    }

    @Override
    protected ClassifyManagePresenter createPresenter() {
        return new ClassifyManagePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classify_manage_edit;
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        mAdapter = new ClassifyManageEditAdapter(R.layout.item_classify_manage_edit, mDataList);
        mAdapter.openLoadMore(true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                SettingInputDialog inputDialog = SettingInputDialog.newInstance("编辑分类",
                        "请输入分类名称", SettingInputDialog.InputStyle.Text);
                inputDialog.showDialog(getFragmentManager());
                inputDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        ProductGroupEntity productGroup = mAdapter.getItem(position);
                        productGroup.setName(values[0]);
                        if (productGroup == null) return;

                        mPresenter.requestEditClassify(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(),
                                values[0], productGroup.getId(), position);
                    }

                    @Override
                    public void onCancel() {

                    }


                });
            }
        });

        showProgressDialog();
        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
            mSpringView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            loadData();
        }
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setFooter(new SpringViewFooter(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }


    private void loadData() {
        mPresenter.getClassifyList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                UserConfig.getInstance(ClientApplication.getApp()).getMerchantId());
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


    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void getClassifyListSuccess(List<ProductGroupEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
        }
        if (dataList.size() > 0) {
            mSpringView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void getClassifyListFailed() {

    }

    @Override
    public void showNoData() {
        mSpringView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestAddClassifySuccess(GoodsClassifyEntity data) {

    }

    @Override
    public void requestAddClassifyFailed() {

    }

    @Override
    public void requestDeleteClassifySuccess(int position) {

    }

    @Override
    public void requestDeleteClassifyFailed() {

    }

    @Override
    public void requestEditClassifySuccess(GoodsClassifyEntity data, int position) {
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void requestEditClassifyFailed() {

    }
}
