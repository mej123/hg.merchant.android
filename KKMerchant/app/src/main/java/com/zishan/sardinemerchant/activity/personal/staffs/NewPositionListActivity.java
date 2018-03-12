package com.zishan.sardinemerchant.activity.personal.staffs;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.PositionManagerEntity;
import com.example.wislie.rxjava.presenter.personal.staff.PositionManagerPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.personal.staff.PositionManagerView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.AddPositionAdapter;
import com.zishan.sardinemerchant.adapter.personal.BasicPositionAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * Created by yang on 2017/10/28
 * <p>
 * 员工  职位管理
 */

public class NewPositionListActivity extends BActivity<PositionManagerView, PositionManagerPresenter>
        implements PositionManagerView {

    @BindView(R.id.basic_position_recycler_view)
    MaxRecyclerView mBasicRecycleView;
    @BindView(R.id.add_position_recycler_view)
    MaxRecyclerView mAddRecycler;
    private List<PositionManagerEntity> mBasicDataList = new ArrayList<>();
    private List<PositionManagerEntity> mAddDataList = new ArrayList<>();
    private AddPositionAdapter mAddAdapter;
    private BasicPositionAdapter mBasicPositionAdapter;
    private List<PositionManagerEntity> data;
    private PositionManagerEntity positionManagerEntity;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_position_manager;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.position_manager));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        showLoadingDialog();
        loadData();

        //三种基本职位
        mBasicRecycleView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mBasicPositionAdapter = new BasicPositionAdapter(R.layout.item_new_basic_position, mBasicDataList);
        mBasicRecycleView.setAdapter(mBasicPositionAdapter);
        mBasicPositionAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PositionManagerEntity positionManagerEntity = mBasicPositionAdapter.getItem(position);
                if (positionManagerEntity == null) return;
                String positionName = positionManagerEntity.getName();
                Long roleId = positionManagerEntity.getId();
                Intent basicIntent = new Intent(NewPositionListActivity.this, NewPositionDetailsActivity.class);
                basicIntent.putExtra("roleId", roleId);
                basicIntent.putExtra("positionName", positionName);
                basicIntent.putExtra("positionType", "basic");
                startActivity(basicIntent);
            }
        });

        //自定义添加职位
        mAddRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAddRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAddAdapter = new AddPositionAdapter(R.layout.item_add_position, mAddDataList);
        mAddRecycler.setAdapter(mAddAdapter);
        mAddAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {

            @Override
            public void onItemClick(View view, int position) {

                PositionManagerEntity entity = mAddAdapter.getItem(position);
                if (entity == null) return;
                String positionName = entity.getName();
                Long roleId = entity.getId();
                Intent addIntent = new Intent(NewPositionListActivity.this, NewPositionDetailsActivity.class);
                addIntent.putExtra("positionName", positionName);
                addIntent.putExtra("positionType", "add");
                addIntent.putExtra("roleId", roleId);
                startActivity(addIntent);

            }

            @Override
            public void onDelete(final int position) {
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认删除该角色?", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        // if (position >= mBasicDataList.size()) return;
                        data = mAddAdapter.getData();
                        positionManagerEntity = data.get(position);
                        if (positionManagerEntity == null) return;
                        mPresenter.deleteRole(UserConfig.getInstance(NewPositionListActivity.this)
                                .getMerchantId(), positionManagerEntity.getId());
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void loadData() {
        mPresenter.getStoreRoleList(UserConfig.getInstance(this).getMerchantId());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
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
    protected PositionManagerPresenter createPresenter() {
        return new PositionManagerPresenter(this, this);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void getStoreRoleListSuccess(List<PositionManagerEntity> dataList) {
        mBasicPositionAdapter.removeAll();
        mAddAdapter.removeAll();
        if (dataList == null || dataList.size() == 0) return;
        //处理得到的数据
        for (int i = 0; i < dataList.size(); i++) {
            PositionManagerEntity positionManagerEntity = dataList.get(i);
            if (positionManagerEntity.getName().equals("店长")) {
                mBasicDataList.add(positionManagerEntity);
            }
        }
        for (int i = 0; i < dataList.size(); i++) {
            PositionManagerEntity positionManagerEntity = dataList.get(i);
            if (positionManagerEntity.getName().equals("收银")) {
                mBasicDataList.add(positionManagerEntity);
            }
        }
        for (int i = 0; i < dataList.size(); i++) {
            PositionManagerEntity positionManagerEntity = dataList.get(i);
            if (positionManagerEntity.getName().equals("服务员")) {
                mBasicDataList.add(positionManagerEntity);
            }
        }

        mBasicPositionAdapter.setNewData(mBasicDataList);

        for (int i = 0; i < dataList.size(); i++) {
            PositionManagerEntity positionManagerEntity = dataList.get(i);
            String name = positionManagerEntity.getName();
            if (!name.equals("店长") && !name.equals("收银") && !name.equals("服务员")) {
                mAddDataList.add(positionManagerEntity);
            }
        }

        if (mAddDataList != null) {
            //mAddRecycler.setVisibility(View.VISIBLE);
            mAddAdapter.setNewData(mAddDataList);
        }
        mBasicPositionAdapter.notifyDataSetChanged();
        mAddAdapter.notifyDataSetChanged();
    }


    @Override
    public void getStoreRoleListFailed() {

    }

    @Override
    public void deleteRoleSuccess(Object object) {
        List<PositionManagerEntity> data = mAddAdapter.getData();
        data.remove(positionManagerEntity);
        mAddAdapter.setNewData(data);
        mAddAdapter.notifyDataSetChanged();
        ToastUtil.show(this, "角色删除成功");
    }

    @Override
    public void deleteRoleFailed() {

    }

    @OnClick(R.id.confirm_and_save)
    public void onViewClicked() {
        //新增职位
        Skip.toActivity(this, NewNewAddPositionActivity.class);
    }
}
