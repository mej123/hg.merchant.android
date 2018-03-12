package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.OrderAttachmentEntity;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreOrderPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.personal.StoreOrderView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.CerificationRecordAdapter;
import com.zishan.sardinemerchant.dialog.PickerDateWheelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewFooter;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 红包核销记录
 * Created by wislie on 2017/11/3.
 */

public class CertificationRecordActivity extends BActivity<StoreOrderView, StoreOrderPresenter>
        implements StoreOrderView {

    @BindView(R.id.cerification_record_springview)
    SpringView mSpringView;
    @BindView(R.id.cerification_record_recycler_view)
    RecyclerView mRecycler;

    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;

    //开始时间(秒级时间戳)
    private Long start_time = null;
    //结束时间(秒级时间戳)
    private Long end_time = null;

    private CerificationRecordAdapter mAdapter;
    private List<StoreOrderEntity> mDataList = new ArrayList<>();
    //订单类型
    private final int[] order_type = {3};

    @Override
    protected StoreOrderPresenter createPresenter() {
        return new StoreOrderPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cerification_record;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getResources().getString(R.string.record));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(R.mipmap.time_select_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickerDateWheelDialog dialog = PickerDateWheelDialog.newInstance("", false, false);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        start_time = DatePickerUtil.getFutureStartTime(Integer.parseInt(values[0]),
                                Integer.parseInt(values[1]) - 1, Integer.parseInt(values[2]));
                        end_time = DatePickerUtil.getFutureEndTime(Integer.parseInt(values[3]),
                                Integer.parseInt(values[4]) - 1, Integer.parseInt(values[5]));

                        loadData();

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
        initSpringView();
        mAdapter = new CerificationRecordAdapter(R.layout.item_cerification_record, mDataList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(CertificationRecordActivity.this,
                        CerificationDetailActivity.class);
                StoreOrderEntity data = mAdapter.getItem(position);
                if (data == null) return;
                OrderAttachmentEntity attachment = GsonParser.parseJsonToClass(data.getAttachment(),
                        OrderAttachmentEntity.class);
                intent.putExtra("useTimetamp", attachment.getStartTime());
                intent.putExtra("userMobile", attachment.getUserMobile());
                intent.putExtra("oprName", attachment.getOprName());
                intent.putExtra("orderId", data.getId());
                intent.putParcelableArrayListExtra("products", attachment.getProducts());
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestRecordList();
            }
        });
        showProgressDialog();
        loadData();
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
        mCurrentPage = 0;
        mDataList.clear();
        mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                start_time, end_time, null, order_type, mCurrentPage, PAGE_SIZE, true);
    }

    private void requestRecordList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                    start_time, end_time, null, order_type, mCurrentPage, PAGE_SIZE, false);
        } else {
            finishRefreshAndLoad();
        }

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

    @Override
    public void loadFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
    }

    @Override
    public void newStoreOrderList(List<StoreOrderEntity> dataList) {
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void addStoreOrderList(List<StoreOrderEntity> dataList) {
        mAdapter.notifyDataChangedAfterLoadMore(dataList, true);
    }

    @Override
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_footer_no_more, null);
        footerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mAdapter.addFooterView(footerView);
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
    }
}
