package com.zishan.sardinemerchant.fragment.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.AppointmentRemindTableArrangeEntity;
import com.example.wislie.rxjava.presenter.base.page.advance_remind.TableArrangePresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.page.advance_remind.TableArrangeView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.adapter.page.TableArrangeTypeAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.recyclerview.WrapContentLinearLayoutManager;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2017/10/13.
 * <p>
 * 桌台安排
 */

public class TableArrangeTypeFragment extends BFragment<TableArrangeView, TableArrangePresenter>
        implements TableArrangeView {
    @BindView(R.id.arrange_type_recycler)
    RecyclerView mRecycler;
    private TableArrangeTypeAdapter mAdapter;
    private List<AppointmentRemindTableArrangeEntity> mDataList = new ArrayList<>();
    private int mType;
    private int bespeak_id;

    public static TableArrangeTypeFragment newInstance(int bespeak_id, int type) {
        TableArrangeTypeFragment fragment = new TableArrangeTypeFragment();
        Bundle data = new Bundle();
        data.putInt("bespeak_id", bespeak_id);
        data.putInt("type", type);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bespeak_id = getArguments().getInt("bespeak_id");
        mType = getArguments().getInt("type");

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_arrange_type;
    }

    @Override
    protected void initBizView() {


        mRecycler.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new TableArrangeTypeAdapter(R.layout.item_table_arrange, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppointmentRemindTableArrangeEntity data = mAdapter.getItem(position);
                if (data == null) return;
                resetCheckedTable(position);
                mAdapter.notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putInt("type", mType);
                BaseEventManager.post(bundle, ACTION_NAME);
            }
        });


    }


    private void resetCheckedTable(int pos) {
        List<AppointmentRemindTableArrangeEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            AppointmentRemindTableArrangeEntity data = dataList.get(i);
            if (i != pos) {
                data.setSelected(false);
            } else {
                data.setSelected(true);
            }
        }
    }


    private void clearCheckedTable() {
        List<AppointmentRemindTableArrangeEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            AppointmentRemindTableArrangeEntity data = dataList.get(i);
            if (data.isSelected()) {
                data.setSelected(false);
            }
        }
    }

    private AppointmentRemindTableArrangeEntity getSelectedTable() {
        List<AppointmentRemindTableArrangeEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            AppointmentRemindTableArrangeEntity data = dataList.get(i);
            if (!data.isSelected()) {
                continue;
            }
            return data;
        }
        return null;
    }

    @Override
    protected TableArrangePresenter createPresenter() {
        return new TableArrangePresenter(getActivity(), this);
    }

    @Override
    protected void loadData() {
        mPresenter.getTableArrangeList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
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

    private List<AppointmentRemindTableArrangeEntity> getTableArrangeList(
            List<AppointmentRemindTableArrangeEntity> result) {
        List<AppointmentRemindTableArrangeEntity> dataList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            AppointmentRemindTableArrangeEntity bean = result.get(i);
            //属于闲置中吗?
            if (bean.getType() == mType) {
                dataList.add(bean);
            }
        }
        return dataList;
    }


    //当大厅和卡座桌位被选中时设置当前条目为未选中状态
    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);

        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            if (mType != bundle.getInt("type")) {
                clearCheckedTable();
                mAdapter.notifyDataSetChanged();
            }

        }

    }

    @OnClick({R.id.confirm_table_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_table_text:
                AppointmentRemindTableArrangeEntity selectData = getSelectedTable();
                if (selectData == null) {
                    ToastUtil.show(getActivity(), "桌号未选中");
                    return;
                }
                mPresenter.confirmTableArrange(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        bespeak_id, selectData.getId());
                break;
        }
    }

    @Override
    public void getTableArrangeListSuccess(List<AppointmentRemindTableArrangeEntity> dataList) {
        mAdapter.setNewData(getTableArrangeList(dataList));
    }

    @Override
    public void getTableArrangeListFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void confirmTableArrangeSuccess(Object data) {

    }

    @Override
    public void confirmTableArrangeFailed() {

    }
}
