package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.store.GoodsUnitAdapter;
import com.zishan.sardinemerchant.adapter.store.GoodsUnitSwipeAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.SettingInputDialog;
import com.zishan.sardinemerchant.entity.UnitData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.view.MaxRecyclerView;

/**
 * Created by yang on 2017/9/30.
 * <p>
 * 菜品单位
 */

public class GoodsUnitsActivity extends BActivity {


    @BindView(R.id.goods_unit_regular_recycler_view)
    MaxRecyclerView mUnitRecycler;
    @BindView(R.id.goods_unit_recycler_view)
    MaxRecyclerView mUnitSwipeRecycler;

    private GoodsUnitAdapter mAdapter;
    private List<UnitData> mUnitList = new ArrayList<UnitData>();

    private GoodsUnitSwipeAdapter mSwipeAdapter;
    private ArrayList<UnitData> mUnitSwipeList = new ArrayList<UnitData>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_units;
    }


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.goods_units));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        //标题栏菜单按钮
        setActionBarMenuText("添加").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingInputDialog inputDialog = SettingInputDialog.newInstance("单位添加",
                        "请输入单位名称", SettingInputDialog.InputStyle.Text);
                inputDialog.showDialog(getFragmentManager());
                inputDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        UnitData unitData = new UnitData();
                        unitData.setSelected(false);
                        unitData.setUnitName(values[0]);
                        mUnitSwipeList.add(unitData);

                        UserConfig.getInstance(ClientApplication.getApp()).setDishUnitList(getUnitNameList());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    protected void initContentView() {
        String unitName = null;
        Intent intent = getIntent();
        if (intent != null) {
            unitName = intent.getStringExtra("unitContent");
        }
        //国定的菜品单位
        List<String> staticUnitList = Arrays.asList("份", "瓶", "个", "位");
        for (int i = 0; i < staticUnitList.size(); i++) {
            UnitData unitData = new UnitData();
            unitData.setUnitName(staticUnitList.get(i));
            if (!unitName.equals(staticUnitList.get(i))) {
                unitData.setSelected(false);
            } else {
                unitData.setSelected(true);
            }

            mUnitList.add(unitData);
        }
        mUnitRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new GoodsUnitAdapter(R.layout.item_goods_unit, mUnitList);
        mUnitRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                resetUnitList(position, true);
            }
        });


        List<String> unitNameList = UserConfig.getInstance(ClientApplication.getApp()).getDishUnitList();
        if (unitNameList != null && unitNameList.size() > 0) {
            for (int i = 0; i < unitNameList.size(); i++) {
                UnitData unitData = new UnitData();
                unitData.setUnitName(unitNameList.get(i));
                unitData.setSelected(false);
                mUnitSwipeList.add(unitData);
            }
        }

        mUnitSwipeRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mUnitSwipeRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mSwipeAdapter = new GoodsUnitSwipeAdapter(R.layout.item_goods_unit_swipe, mUnitSwipeList);
        mUnitSwipeRecycler.setAdapter(mSwipeAdapter);
        mSwipeAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {
            @Override
            public void onDelete(int position) {
                final UnitData unitData = mUnitSwipeList.get(position);
                String unitName = unitData.getUnitName();

                ArrayList<String> unitNameList = new ArrayList<String>();
                unitNameList.add(unitName);
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.
                        newInstance("确认删除" + unitName + "这个菜品单位?", unitNameList);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        mUnitSwipeList.remove(unitData);
                        UserConfig.getInstance(ClientApplication.getApp()).setDishUnitList(getUnitNameList());
                        mSwipeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onItemClick(View view, int position) {
                resetUnitList(position, false);
            }
        });

        //设置菜品单位
        setCheckedUnitName(unitName);


    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.goods_unit_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_unit_confirm:

                Intent resultIntent = new Intent();
                String checkedName = getCheckedUnitName();
                if (!TextUtils.isEmpty(checkedName)) {
                    resultIntent.putExtra("unitContent", checkedName);
                }
                setResult(RESULT_OK, resultIntent);
                finish();

                break;

        }
    }

    /**
     * 得到菜品名称集合
     *
     * @return
     */
    private ArrayList<String> getUnitNameList() {
        ArrayList<String> unitNameList = new ArrayList<>();
        for (int i = 0; i < mUnitSwipeList.size(); i++) {
            UnitData unitData = mUnitSwipeList.get(i);
            unitNameList.add(unitData.getUnitName());
        }
        return unitNameList;
    }

    /**
     * 重置选中的
     *
     * @param position
     * @param isStaticUnitList
     */
    private void resetUnitList(int position, boolean isStaticUnitList) {
        if (isStaticUnitList) {
            for (int i = 0; i < mUnitList.size(); i++) {
                UnitData unitData = mUnitList.get(i);
                if (i == position) {
                    unitData.setSelected(true);
                    continue;
                }
                unitData.setSelected(false);
            }

            for (int i = 0; i < mUnitSwipeList.size(); i++) {
                UnitData unitData = mUnitSwipeList.get(i);
                unitData.setSelected(false);
            }
            mAdapter.notifyDataSetChanged();
            mSwipeAdapter.notifyDataSetChanged();
        } else {


            for (int i = 0; i < mUnitList.size(); i++) {
                UnitData unitData = mUnitList.get(i);

                unitData.setSelected(false);
            }

            for (int i = 0; i < mUnitSwipeList.size(); i++) {
                UnitData unitData = mUnitSwipeList.get(i);
                if (i == position) {
                    unitData.setSelected(true);
                    continue;
                }
                unitData.setSelected(false);
            }
            mAdapter.notifyDataSetChanged();
            mSwipeAdapter.notifyDataSetChanged();

        }

    }

    private void setCheckedUnitName(String name) {
        boolean isSelected = false;
        for (int i = 0; i < mUnitList.size(); i++) {
            UnitData unitData = mUnitList.get(i);
            if (unitData.getUnitName().equals(name)) {
                isSelected = true;
                unitData.setSelected(true);
                break;
            }
        }

        if (!isSelected) {
            for (int i = 0; i < mUnitSwipeList.size(); i++) {
                UnitData unitData = mUnitSwipeList.get(i);
                if (unitData.getUnitName().equals(name)) {
                    unitData.setSelected(true);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        mSwipeAdapter.notifyDataSetChanged();
    }

    private String getCheckedUnitName() {
        for (int i = 0; i < mUnitList.size(); i++) {
            UnitData unitData = mUnitList.get(i);
            if (unitData.isSelected()) return unitData.getUnitName();
        }

        for (int i = 0; i < mUnitSwipeList.size(); i++) {
            UnitData unitData = mUnitSwipeList.get(i);
            if (unitData.isSelected()) return unitData.getUnitName();
        }
        return null;
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

}
