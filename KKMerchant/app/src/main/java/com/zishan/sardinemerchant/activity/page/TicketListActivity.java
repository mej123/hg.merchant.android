package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.page.TicketEntity;
import com.example.wislie.rxjava.presenter.base.page.ticket.TicketPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.ticket.TicketListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.StoreStateAdapter;
import com.zishan.sardinemerchant.adapter.page.TicketListAdapter;
import com.zishan.sardinemerchant.entity.MyStoreStateEntity;
import com.zishan.sardinemerchant.view.SingleMenuView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;
import top.ftas.ftaswidget.view.DropDownMenus;

/**
 * 分销池子
 * Created by wislie on 2018/1/19.
 */

public class TicketListActivity extends BActivity<TicketListView, TicketPresenter> implements TicketListView {

    @BindView(R.id.drop_down_menu)
    DropDownMenus mDropDownMenu;

    SpringView mSpringView;
    RecyclerView mRecycler;
    LinearLayout mEmptyLayout;

    private List<View> mPopupViews = new ArrayList<>();//菜单列表视图
    private List<MyStoreStateEntity> mTypeDataList = new ArrayList<>();
    private List<MyStoreStateEntity> mStateDataList = new ArrayList<>();
    private List<TicketEntity> mDataList = new ArrayList<>();
    private StoreStateAdapter mTypeAdapter;
    private StoreStateAdapter mStateAdapter;

    private TicketListAdapter mAdapter;

    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;
    //当前页
    private int mCurrentPage;

    //1 发放券 分销池子
    private int type = 1;

    private Integer coupon_type;

    private Integer status;

    @Override
    protected TicketPresenter createPresenter() {
        return new TicketPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket_list;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);

        TextView titleText = setActionbarTitle(getString(R.string.ticket_manage));
        setActionBarHomeIcon(R.mipmap.back_black_icon);
        ImageView addTicketIcon = setActionBarMenuIcon(R.mipmap.add_ticket_icon);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
        addTicketIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketListActivity.this, TicketChooseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initContentView() {
        mTypeDataList.add(new MyStoreStateEntity(0, "全部"));
        mTypeDataList.add(new MyStoreStateEntity(3, "凭证券"));
        mTypeDataList.add(new MyStoreStateEntity(2, "代金券"));
        mTypeDataList.add(new MyStoreStateEntity(1, "满减券"));

        //注意state的值
        mStateDataList.add(new MyStoreStateEntity(0, "全部"));
        mStateDataList.add(new MyStoreStateEntity(1, "发券中"));
        mStateDataList.add(new MyStoreStateEntity(2, "已到期"));
        mStateDataList.add(new MyStoreStateEntity(3, "已发完"));
        mStateDataList.add(new MyStoreStateEntity(4, "已取消"));

        mTypeAdapter = new StoreStateAdapter(R.layout.item_single_menu, mTypeDataList);
        mStateAdapter = new StoreStateAdapter(R.layout.item_single_menu, mStateDataList);

        initPopViews();
        initSpringView();

        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new TicketListAdapter(R.layout.item_ticket_list, mDataList);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestTicketList(coupon_type, status);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TicketEntity data = mAdapter.getItem(position);
                        if(data == null) return;
                        Long coupon_give_out_id = data.getCouponGiveOutId();
                        if(coupon_give_out_id == null) return;
                        Intent intent = new Intent(TicketListActivity.this, TicketDetailActivity.class);
                        intent.putExtra("coupon_give_out_id", coupon_give_out_id.longValue());
                        startActivity(intent);

                    }
                });

        showProgressDialog();
        initRequestTicketList(coupon_type, status);
    }

    /*
    * 初始化请求
    */
    private void initRequestTicketList(Integer coupon_type, Integer status) {
        mCurrentPage = 0;
        mPresenter.getTicketList(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                coupon_type, status, type, PAGE_SIZE, mCurrentPage, true);
    }

    /**
     * 非第一次发起请求
     */
    private void requestTicketList(Integer coupon_type, Integer status) {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getTicketList(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                    coupon_type, status, type, PAGE_SIZE, mCurrentPage, false);
        } else {
            finishRefreshAndLoad();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRequestTicketList(coupon_type, status);
    }

    private void initPopViews() {
        String[] headers = new String[]{"全部类型", "全部状态"};

        //类型
        SingleMenuView typeMenuView = new SingleMenuView(this) {
            @Override
            protected BaseQuickAdapter getAdapter() {
                return mTypeAdapter;
            }
        };

        mTypeAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MyStoreStateEntity data = mTypeAdapter.getItem(position);
                        mTypeAdapter.setSelectedPos(position);
                        mDropDownMenu.setTabText(data.getName());
                        mDropDownMenu.closeMenu();

                        if (position == 0) {
                            coupon_type = null;
                        } else {
                            coupon_type = data.getState();
                        }
                        initRequestTicketList(coupon_type, status);
                    }
                });

        //门店状态
        SingleMenuView statusMenuView = new SingleMenuView(this) {
            @Override
            protected BaseQuickAdapter getAdapter() {
                return mStateAdapter;
            }
        };
        mStateAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MyStoreStateEntity data = mStateAdapter.getItem(position);
                        mStateAdapter.setSelectedPos(position);
                        mDropDownMenu.setTabText(data.getName());
                        mDropDownMenu.closeMenu();
                        if (position == 0) {
                            status = null;
                        } else {
                            status = data.getState();
                        }
                        initRequestTicketList(coupon_type, status);

                    }
                });

        mPopupViews.add(typeMenuView);
        mPopupViews.add(statusMenuView);

        //初始化内容视图
        View contentView = LayoutInflater.from(this).inflate(R.layout.content_ticket_list, null);
        mSpringView = (SpringView) contentView.findViewById(R.id.ticket_list_springview);
        mRecycler = (RecyclerView) contentView.findViewById(R.id.ticket_list_recycler_view);
        mEmptyLayout = (LinearLayout) contentView.findViewById(R.id.empty_ticket_layout);

        //装载
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), mPopupViews, contentView);
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
                    initRequestTicketList(coupon_type, status);
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
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
        mRecycler.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void newTicketList(List<TicketEntity> dataList) {
        mAdapter.setNewData(dataList);
        mRecycler.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
        finishRefreshAndLoad();
    }

    @Override
    public void addTicketList(List<TicketEntity> dataList) {
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
    }
}
