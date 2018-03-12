package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.GoodsClassifyEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.classify.ClassifyManagePresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.store.classify.ClassifyManageView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.store.ClassifyManageAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmDialog;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.SettingInputDialog;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 分类管理
 * Created by wislie on 2017/9/29.
 */

public class ClassifyManageActivity extends BActivity<ClassifyManageView, ClassifyManagePresenter>
        implements ClassifyManageView {

    @BindView(R.id.classify_manage_springview)
    SpringView mSpringView;
    @BindView(R.id.classify_manage_recycler_view)
    RecyclerView mRecycler;

    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    private ClassifyManageAdapter mAdapter;
    private ArrayList<ProductGroupEntity> mDataList = new ArrayList<>();

    public static final int CLASSIFY_MANAGE_BACK = 0xaa;



    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.classify_manage));
        ImageView backIcon = setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classify_manage;
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAdapter = new ClassifyManageAdapter(R.layout.item_classify_manage, mDataList);
        mAdapter.openLoadMore(true);
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {

            @Override
            public void onDelete(final int position) {
                deleteClassify(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ClassifyManageActivity.this, GoodsBrandActivity.class);
                ProductGroupEntity productGroup = mAdapter.getItem(position);
                ArrayList<ProductGroupEntity> productGroups = new ArrayList<>();
                productGroups.add(productGroup);
                intent.putExtra(Constant.CONFIG_PRODUCT_GROUP, productGroups);
                intent.putExtra("position", position);
                startActivity(intent);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }


    private void loadData() {
        mPresenter.getClassifyList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                UserConfig.getInstance(ClientApplication.getApp()).getMerchantId());
    }


    @Override
    protected ClassifyManagePresenter createPresenter() {
        return new ClassifyManagePresenter(this, this);
    }


    @OnClick({R.id.new_goods, R.id.edit_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            //新建
            case R.id.new_goods:
                SettingInputDialog inputDialog = SettingInputDialog.newInstance("新建分类",
                        "请输入分类名称", SettingInputDialog.InputStyle.Text);
                inputDialog.showDialog(getFragmentManager());
                inputDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        mPresenter.requestAddClassify(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                values[0], UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            //编辑
            case R.id.edit_goods:
                Skip.toActivity(this, ClassifyManageEditActivity.class);
                break;
        }
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
                if (mSpringView != null)
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
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
        if (dataList.size() > 0) {
            mSpringView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
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
        if (data == null) return;
        ProductGroupEntity newProductGroup = new ProductGroupEntity();
        newProductGroup.setId(data.getCustomGroupId());
        if(data.getStoreId() == null){
            newProductGroup.setStoreId(0l);
        }else{
            newProductGroup.setStoreId(data.getStoreId());
        }
        newProductGroup.setName(data.getGroupName());
        newProductGroup.setIcon("");
        newProductGroup.setSort("");
        newProductGroup.setProductNum(0);
        newProductGroup.setSelectedCount(0);
        int position = mAdapter.getItemCount();
        mAdapter.add(position, newProductGroup);
        mSpringView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);

        //分类添加，商品管理主页更新
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constant.CONFIG_CLASSIFY_TYPE, Constant.CLASSIFY_ADD);
//        bundle.putSerializable(Constant.CONFIG_CLASSIFY, data);
//        BaseEventManager.post(bundle, MainStoreFragment.class.getSimpleName());
    }



    @Override
    public void requestAddClassifyFailed() {

    }

    @Override
    public void requestDeleteClassifySuccess(int position) {
        mAdapter.remove(position);
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getItemCount() <= 0){
            showNoData();
        }
    }

    @Override
    public void requestDeleteClassifyFailed() {

    }

    @Override
    public void requestEditClassifySuccess(GoodsClassifyEntity data, int position) {

    }

    @Override
    public void requestEditClassifyFailed() {

    }

    //删除分类
    private void deleteClassify(final int position) {
        final ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认删除该分类?", null);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

                ProductGroupEntity productGroup = mAdapter.getItem(position);
                if (productGroup == null) return;
                //删除之前要判断count是否为0
                if (productGroup.getProductNum() != null && productGroup.getProductNum() != 0) {
                    ConfirmDialog dialog = ConfirmDialog.newInstance("分类中含有商品,不可删除该分类");
                    dialog.showDialog(getFragmentManager());
                    dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                        @Override
                        public void onConfirm() {
                            //将"删除"按钮缩回去
                            SwipeItemLayout.closeAllItems(mRecycler);
                        }

                        @Override
                        public void onInputConfirm(String... values) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                //分类删除
                mPresenter.requestDeleteClassify(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        productGroup.getId(), UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(), position);
            }

            @Override
            public void onInputConfirm(String... values) {

            }

            @Override
            public void onCancel() {
                //将"删除"按钮缩回去
                SwipeItemLayout.closeAllItems(mRecycler);
            }
        });
    }
    //结束当前的activity
    private void finishThisActivity(){
        ArrayList<ProductGroupEntity> dataList = (ArrayList<ProductGroupEntity>) mAdapter.getData();
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("product_group", dataList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishThisActivity();
        super.onBackPressed();
    }
}
