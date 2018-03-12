package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TableBoxEntity;
import com.example.wislie.rxjava.presenter.base.page.table_box.TableBoxTypePresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.table_box.TableTypeView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.TableTypeAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.dialog.PairInputDialog;

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
 * 桌台(大厅 卡座 包厢)
 * Created by wislie on 2017/11/6.
 */

public class TableTypeActivity extends BActivity<TableTypeView, TableBoxTypePresenter>
        implements TableTypeView {

    @BindView(R.id.table_type_springview)
    SpringView mSpringView;
    @BindView(R.id.table_type_recycler_view)
    RecyclerView mRecycler;

    @BindView(R.id.no_table)
    TextView mNoTableText;
    @BindView(R.id.table_empty_layout)
    LinearLayout mEmptyLayout;

    private TextView mAddText;

    private TableTypeAdapter mAdapter;
    private ArrayList<TableBoxEntity> mDataList = new ArrayList<>();
    private int mType = 0;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_table_type;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if(intent != null){
            mType = intent.getIntExtra("type", mType);
        }
        if (mType == Constant.HALL) {
            setActionbarTitle(getString(R.string.lobby));
        } else if (mType == Constant.CARD) {
            setActionbarTitle(getString(R.string.cassette));
        } else if (mType == Constant.BOX) {
            setActionbarTitle(getString(R.string.box));
        }

        setActionBarHomeIcon(R.mipmap.back_white_icon);
        mAddText = setActionBarMenuText(getString(R.string.add));
        mAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTable();
            }
        });
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAdapter = new TableTypeAdapter(R.layout.item_table_type, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                TableBoxEntity tableBoxEntity = mAdapter.getItem(position);
                if (tableBoxEntity == null) return;
                Intent intent = new Intent(TableTypeActivity.this, TableTypeEditActivity.class);
                intent.putExtra("table_box", tableBoxEntity);
                startActivity(intent);

//                Intent intent = new Intent(TableTypeActivity.this, TransformTableActivity.class);
//                intent.putExtra("seat_name", tableBoxEntity.getSeatName());
//                intent.putExtra("table_id", tableBoxEntity.getId());
                //startActivity(intent);
            }

            @Override
            public void onDelete(final int position) {
                final TableBoxEntity tableBoxEntity = mAdapter.getItem(position);
                if (tableBoxEntity == null) return;
                ArrayList<String> titles = new ArrayList<String>();
                titles.add(tableBoxEntity.getSeatName());
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance(
                        "确认删除" + tableBoxEntity.getSeatName() + "桌台", titles);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.requestDeleteTableBox(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                tableBoxEntity.getRepastLocationKey(), position);

                        //将"删除"按钮缩回去
                        SwipeItemLayout.closeAllItems(mRecycler);
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
        });

        if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
            mSpringView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
        loadData();

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
        mPresenter.getTableBoxList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
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

    @OnClick({R.id.table_empty_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.table_empty_layout:
                addTable();
                break;
        }
    }

    /**
     * 设置布局文件是否可见
     */
    private void setLayoutVisible() {
        List<TableBoxEntity> dataList = mAdapter.getData();
        if (dataList != null && dataList.size() > 0) {
            mSpringView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mAddText.setVisibility(View.VISIBLE);
        } else {
            mSpringView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            mAddText.setVisibility(View.GONE);
            if (mType == Constant.HALL) {
                mNoTableText.setText(getResources().getString(R.string.no_hall));
            } else if (mType == Constant.CARD) {
                mNoTableText.setText(getResources().getString(R.string.no_card));
            } else if (mType == Constant.BOX) {
                mNoTableText.setText(getResources().getString(R.string.no_box));
            }
        }
    }

    //添加桌台
    private void addTable() {
        PairInputDialog dialog = PairInputDialog.newInstance("桌台添加",
                "桌台名称", "适合用餐人数", PairInputDialog.InputStyle.Text, PairInputDialog.InputStyle.Number);
        dialog.showDialog(getFragmentManager());
        dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onInputConfirm(String... values) {
                int seatNum = 0;
                try {
                    seatNum = Integer.parseInt(values[1]);
                } catch (NumberFormatException e) {
                    ToastUtil.show("输入座位的格式出错");
                    return;
                }
                mPresenter.requestAddTableBox(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        null, values[0], mType, seatNum);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected TableBoxTypePresenter createPresenter() {
        return new TableBoxTypePresenter(this, this);
    }

    @Override
    public void getTableBoxListSuccess(List<TableBoxEntity> dataList) {
        ArrayList<TableBoxEntity> newDataList = getTableBoxList(dataList, mType);
        mAdapter.setNewData(newDataList);
        finishRefreshAndLoad();
        if (newDataList.size() > 0) {
            mSpringView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getTableBoxListFail() {

    }

    @Override
    public void showNoData() {
        mSpringView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addTableBoxSuccess(TableBoxEntity data) {
        data.setModel(0);
        data.setIsShare(0);
        mAdapter.add(mAdapter.getItemCount(), data);
        setLayoutVisible();
    }

    @Override
    public void addTableBoxFailed() {

    }

    @Override
    public void deleteTableBoxSuccess(Object data, int position) {
        mAdapter.remove(position);
        setLayoutVisible();
    }


    @Override
    public void deleteTableBoxFailed() {

    }

    private ArrayList<TableBoxEntity> getTableBoxList(List<TableBoxEntity> result, int type) {
        ArrayList<TableBoxEntity> tableBoxList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            TableBoxEntity tableBoxEntity = result.get(i);
            if (tableBoxEntity.getType() == type) {
                tableBoxList.add(tableBoxEntity);
            }
        }
        return tableBoxList;
    }
}
