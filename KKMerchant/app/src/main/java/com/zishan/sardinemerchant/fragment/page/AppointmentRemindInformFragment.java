package com.zishan.sardinemerchant.fragment.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.AppointmentRemindEntity;
import com.example.wislie.rxjava.presenter.base.page.advance_remind.AppointmentInformPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.advance_remind.AppointmentInformView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.AppointmentRemindDetailActivity;
import com.zishan.sardinemerchant.activity.page.FeedbackActivity;
import com.zishan.sardinemerchant.adapter.page.InformAdapter;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;
import com.zishan.sardinemerchant.entity.SelectData;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.view.XTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * Created by yang on 2017/10/13.
 * <p>
 * 预约通知  fragment
 */
public class AppointmentRemindInformFragment extends BFragment<AppointmentInformView, AppointmentInformPresenter>
        implements AppointmentInformView {
    @BindView(R.id.inform_springview)
    SpringView mSpringView;
    @BindView(R.id.inform_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.select_options)
    TextView mSelectOptionsText;
    @BindView(R.id.order_amount)
    XTextView xtextview;
    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;
    private List<AppointmentRemindEntity> mDataList = new ArrayList<>();
    private InformAdapter mAdapter;

    private int[] states = new int[]{0, 5};
    //预约类型
    private int mType;
    //是否可选
    private boolean mIsOptional;

    private Long start_time = null;
    private Long end_time = null;
    private Integer min_dinner_num = null;
    private Integer max_dinner_num = null;
    private Integer need_room = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_appointment_remind_inform;
    }


    public static AppointmentRemindInformFragment newInstance(int type, boolean isOptional) {
        AppointmentRemindInformFragment fg = new AppointmentRemindInformFragment();
        Bundle data = new Bundle();
        data.putInt("type", type);
        data.putBoolean("isOptional", isOptional);
        fg.setArguments(data);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        mIsOptional = getArguments().getBoolean("isOptional");
    }


    @Override
    protected void initBizView() {

        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new SpaceItemDecoration(0, 0, 0, DensityUtil.dip2px(getActivity(), 8)));
        mAdapter = new InformAdapter(R.layout.item_appointment_remind_inform, mDataList);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppointmentRemindEntity data = mAdapter.getItem(position);
                if (data == null) return;
                //预约id
                Integer bespeak_id = data.getId();
                if (bespeak_id == null) return;
                Intent intent = new Intent(getActivity(), AppointmentRemindDetailActivity.class);
                intent.putExtra("type", mType);
                intent.putExtra("seat_data", data);
                startActivity(intent);
            }
        });


        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {

            @Override
            public void onRefuse(int position) {
                AppointmentRemindEntity data = mAdapter.getItem(position);
                if (data == null) return;
                //预约id
                Integer bespeak_id = data.getId();
                if (bespeak_id == null) return;
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra("bespeak_id", bespeak_id);
                startActivity(intent);
            }

            @Override
            public void onAccept(int position) {
                AppointmentRemindEntity data = mAdapter.getItem(position);
                if (data == null) return;
                //预约id
                final Integer bespeak_id = data.getId();
                if (bespeak_id == null) return;
                ConfirmOrCancelDialog confirmOrCancelDialog = ConfirmOrCancelDialog.newInstance("确认接受该笔预约订单吗？", null);
                confirmOrCancelDialog.showDialog(getActivity().getFragmentManager());
                confirmOrCancelDialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                        mPresenter.requestAcceptAppointmentRemindInform(UserConfig.getInstance(
                                ClientApplication.getApp()).getStoreId(), bespeak_id);
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
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                reqeustAppointmentRemindList();
            }
        });
    }

    @Override
    protected AppointmentInformPresenter createPresenter() {
        return new AppointmentInformPresenter(getActivity(), this);
    }


    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(getActivity()));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad(); //停止加载
                } else {
                    //第一次加载
                    initAppointmentRemindList();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }


    @Override
    protected void loadData() {
        showProgressDialog();
        initAppointmentRemindList();
    }

    private void initAppointmentRemindList() {
        mCurrentPage = 0;
        mPresenter.getAppointmentRemindList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                mCurrentPage, PAGE_SIZE, states, start_time, end_time, min_dinner_num, max_dinner_num, need_room, true);
    }

    //非第一次加载
    private void reqeustAppointmentRemindList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getAppointmentRemindList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                    mCurrentPage, PAGE_SIZE, states, start_time, end_time, min_dinner_num, max_dinner_num, need_room, false);
        } else {
            finishRefreshAndLoad(); //停止加载
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
    public void acceptAppointmentInformSuccess(Object data) {

    }

    @Override
    public void acceptAppointmentInformFailed() {

    }

    @Override
    public void getAppointmentRemindListFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void newAppointmentRemindList(ArrayList<AppointmentRemindEntity> dataList) {
        if (dataList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }

        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void addAppointmentRemindList(ArrayList<AppointmentRemindEntity> dataList) {
        mAdapter.notifyDataChangedAfterLoadMore(dataList, true);
    }

    @Override
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(showFooterNoMoreData());
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
        xtextview.setText("共 sBigeFontsBlue" + totalElements + "eBig 笔预约");
    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);

        if (mIsOptional && baseEvent.getTagString().equals(ACTION_NAME)) {

            SelectData newData = (SelectData) baseEvent.getData();
            start_time = newData.getStart_time();
            end_time = newData.getEnd_time();
            min_dinner_num = newData.getMin_dinner_num();
            max_dinner_num = newData.getMax_dinner_num();
            need_room = newData.getNeed_room();

            mSelectOptionsText.setText(getSelectOptions(newData));
            loadData();

        }

    }

    private String getSelectOptions(SelectData data) {
        StringBuilder sBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(data.getDateContent()) && !data.getDateContent().equals("自定义")) {
            sBuilder.append(data.getDateContent()).append("  ");
        }

        if (!TextUtils.isEmpty(data.getSeatContent())) {
            sBuilder.append(data.getSeatContent()).append("  ");
        }

        if (!TextUtils.isEmpty(data.getNumContent())) {
            sBuilder.append(data.getNumContent()).append("  ");
        }
        return sBuilder.toString();
    }

}