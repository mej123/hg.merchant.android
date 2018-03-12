package com.zishan.sardinemerchant.fragment.page;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.StoreSeatEntity;
import com.example.wislie.rxjava.presenter.base.page.main.EatBeforePayPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.EatBeforePayView;

import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.TableBoxActivity;
import com.zishan.sardinemerchant.fragment.BFragment;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.viewpager.NonSwipeableViewPager;

/**
 * 先吃后付
 * Created by yang on 2017/9/12.
 */

public class MainPageEatBeforePayFragment extends BFragment<EatBeforePayView, EatBeforePayPresenter>
        implements EatBeforePayView {

    @BindView(R.id.type_tab_layout)
    TabLayout mTypeLayout;
    @BindView(R.id.type_vp)
    NonSwipeableViewPager mViewPager;
    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;
    @BindView(R.id.permission_layout)
    LinearLayout mPermissionLayout;
    //线程池
    private ExecutorService mExService = Executors.newCachedThreadPool();

    private ArrayList<String> mTitles = new ArrayList<>();

    //首页是否可见, mIsHidden为false表示可见, mIsHidden为true表示不可见
    private boolean mIsHidden = false;

    private PositionPagerAdapter mPageAdapter;

    public static MainPageEatBeforePayFragment newInstance() {
        MainPageEatBeforePayFragment fg = new MainPageEatBeforePayFragment();
        return fg;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_page_eat_before_pay;
    }


    public class PositionPagerAdapter extends FragmentPagerAdapter {
        private int mSize;
        private List<String> mTitles;
        private long baseId = 0;

        public PositionPagerAdapter(FragmentManager fm, List<String> list) {
            super(fm);
            mTitles = list;
            mSize = list == null ? 0 : list.size();
        }

        @Override
        public int getCount() {
            return mSize;
        }

        //只有通过tag查找碎片的时候没找到，才会调用这个方法。从这个方法里得到fragment然后add进
        //ViewPager中。
        @Override
        public Fragment getItem(int position) {
            String title = mTitles.get(position);
            if (title.equals("大厅")) return PositionFragment.newInstance(Constant.HALL);
            else if (title.equals("卡座")) return PositionFragment.newInstance(Constant.CARD);
            else if (title.equals("包厢")) return PositionFragment.newInstance(Constant.BOX);
            return null;
//            return PositionFragment.newInstance(mTitles.get(position));
        }

        //this is called when notifyDataSetChanged() is called
        //重建所有item，新建item的时候会调用instantiateItem。
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        //每次在instantiateItem中调用这个的时候，都会是不同的id。适配器发现找不到之前的碎片，
        //就会重新调用getItem来新建碎片。
        //这个方法是适配器用来组装tag的一部分。只要改变了它，也就改变了tag。
        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * 删除之后，在调用notifyDataSetChanged之前，先调用这个notifyChangeInPosition(1)。
         * 改变tag。
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

        // 自己写的一个方法用来添加数据这个可是重点啊
        public void setList(List<String> list) {
            this.mTitles = list;
            mSize = list == null ? 0 : list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= mTitles.size()) return "";
            String title = mTitles.get(position);
            return title;
        }
    }


    @Override
    protected void initBizView() {
        initData();
        mExService.execute(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
                    if (storeId != null && storeId.longValue() != 0) {
                        loadPageData();
                    }
                }
            }
        });

    }

    private void initData() {
        mPageAdapter = new PositionPagerAdapter(getChildFragmentManager(), mTitles);
        mViewPager.setAdapter(mPageAdapter);
        mTypeLayout.setupWithViewPager(mViewPager);
        mTypeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mViewPager != null)
                    mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadPageData();
    }


    @Override
    protected void loadData() {

    }

    private void loadPageData() {
        Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        if (storeId != null && storeId.longValue() > 0) {
            if(mPresenter == null) return;
            mPresenter.getEatBeforePay(UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
        }
    }

    @Override
    protected EatBeforePayPresenter createPresenter() {
        return new EatBeforePayPresenter(getActivity(), this);
    }


    /**
     * 根据桌台类型得到桌台数量 如大厅，卡座，包厢
     *
     * @param seatList
     * @param type
     * @return
     */
    private int getSeatNumByType(ArrayList<StoreSeatEntity> seatList, int type) {
        int typeNum = 0;
        for (StoreSeatEntity seat : seatList) {
            if (seat.getType() == type) {
                typeNum++;
            }
        }
        return typeNum;
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
    public void getEatBeforePaySuccess(ArrayList<StoreSeatEntity> dataList) {
        if (mViewPager != null)
            mViewPager.setVisibility(View.VISIBLE);
        if (mEmptyLayout != null)
            mEmptyLayout.setVisibility(View.GONE);
        if (mPermissionLayout != null)
            mPermissionLayout.setVisibility(View.GONE);


        int hallNum = getSeatNumByType(dataList, Constant.HALL);
        int cardNum = getSeatNumByType(dataList, Constant.CARD);
        int boxNum = getSeatNumByType(dataList, Constant.BOX);


        if (!mIsHidden) {
            if (mTypeLayout != null)
                if (hallNum > 0) {
                    if (!mTitles.contains("大厅")) {
                        mTitles.add("大厅");
                        mPageAdapter.setList(mTitles);
                        mPageAdapter.notifyDataSetChanged();
                    }

                } else {
                    if (mTitles.contains("大厅")) {
                        int delIndex = mTitles.indexOf("大厅");
                        mTitles.remove(delIndex);
                        mPageAdapter.setList(mTitles);
                        mPageAdapter.notifyChangeInPosition(delIndex);
                        mPageAdapter.notifyDataSetChanged();
                    }
                }


            if (cardNum > 0) {
                if (!mTitles.contains("卡座")) {
                    mTitles.add("卡座");
                    mPageAdapter.setList(mTitles);
                    mPageAdapter.notifyDataSetChanged();

                }

            } else {
                if (mTitles.contains("卡座")) {
                    int delIndex = mTitles.indexOf("卡座");
                    mTitles.remove(delIndex);
                    mPageAdapter.setList(mTitles);
                    mPageAdapter.notifyChangeInPosition(delIndex);
                    mPageAdapter.notifyDataSetChanged();
                }
            }

            if (boxNum > 0) {
                if (!mTitles.contains("包厢")) {
                    mTitles.add("包厢");
                    mPageAdapter.setList(mTitles);
                    mPageAdapter.notifyDataSetChanged();
                }
            } else {
                if (mTitles.contains("包厢")) {
                    int delIndex = mTitles.indexOf("包厢");
                    mTitles.remove(delIndex);
                    mPageAdapter.setList(mTitles);
                    mPageAdapter.notifyChangeInPosition(delIndex);
                    mPageAdapter.notifyDataSetChanged();
                }
            }

            if (hallNum > 0) {

                addTabLayout(hallNum, dataList.size(), Constant.HALL);
            }

            if (cardNum > 0) {
                addTabLayout(cardNum, dataList.size(), Constant.CARD);

            }

            if (boxNum > 0) {
                addTabLayout(boxNum, dataList.size(), Constant.BOX);
            }

        }

        Bundle bundle = new Bundle();
        bundle.putInt("permission_state", Constant.PERMISSION_ALLOWED);
        bundle.putParcelableArrayList("seat_list", dataList);
        //更新
        BaseEventManager.post(bundle, PositionFragment.class.getName());
    }


    private void addTabLayout(int num, int totalNum, int type) {

        int index = -1;
        if (type == Constant.HALL) {
            index = mTitles.indexOf("大厅");
        } else if (type == Constant.CARD) {
            index = mTitles.indexOf("卡座");
        } else if (type == Constant.BOX) {
            index = mTitles.indexOf("包厢");
        }

        if (mTypeLayout == null) return;

        int selectedPos = -1;
        TabLayout.Tab tab = mTypeLayout.getTabAt(index);
        if (tab != null) {
            if (tab.isSelected()) {
                selectedPos = tab.getPosition();
            }
            mTypeLayout.removeTabAt(index);
        }
        tab = mTypeLayout.newTab();
        mTypeLayout.addTab(tab, index);
        if (selectedPos != -1) {
            tab.select();
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_type_tab_layout, null);
        tab.setCustomView(view);
        TextView tabTitleText = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
        TextView tabPercentText = (TextView) tab.getCustomView().findViewById(R.id.tab_percent);
        if (type == Constant.HALL) {
            tabTitleText.setText("大厅");
            tabPercentText.setText("(" + num + "/" + totalNum + ")");
        } else if (type == Constant.CARD) {
            tabTitleText.setText("卡座");
            tabPercentText.setText("(" + num + "/" + totalNum + ")");
        } else if (type == Constant.BOX) {
            tabTitleText.setText("包厢");
            tabPercentText.setText("(" + num + "/" + totalNum + ")");
        }
    }

    @Override
    public void getEatBeforePayFailed() {
        updateTabLayout();
    }

    //获取权限失败则隐藏
    private void updateTabLayout() {
        if (mTypeLayout == null) return;
        mEmptyLayout.setVisibility(View.INVISIBLE);
//        mPermissionLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < mTitles.size(); i++) {
            TabLayout.Tab tab = mTypeLayout.getTabAt(i);
            TextView tabPercentText = (TextView) tab.getCustomView().findViewById(R.id.tab_percent);
            ImageView tabMsgIcon = (ImageView) tab.getCustomView().findViewById(R.id.tab_msg);
            tabPercentText.setVisibility(View.INVISIBLE);
            tabMsgIcon.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void showNoEatBeforePayData() {
        mEmptyLayout.setVisibility(View.VISIBLE);
        mPermissionLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void showNewMessage(Integer msgCount) {
        Bundle bundle = new Bundle();
        bundle.putInt("msg_count", msgCount);
        BaseEventManager.post(bundle, MainPageFragment.class.getName());
    }

    @Override
    public void getPermissionFailed() {
        updateTabLayout();
        Bundle bundle = new Bundle();
        bundle.putInt("permission_state", Constant.PERMISSION_FORBIDDEN);
        //更新
        BaseEventManager.post(bundle, PositionFragment.class.getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExService != null) {
            mExService.shutdown();
        }

    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            int type = bundle.getInt("type");
            switch (type) {
                case Constant.HALL:
                    updateTabLayout(0, true);
                    break;
                case Constant.CARD:
                    updateTabLayout(1, true);
                    break;
                case Constant.BOX:
                    updateTabLayout(2, true);
                    break;
            }
        }

    }

    //更新tablayout上的小红点
    private void updateTabLayout(int position, boolean visible) {
        TabLayout.Tab tab = mTypeLayout.getTabAt(position);
        ImageView tabMsgIcon = (ImageView) tab.getCustomView().findViewById(R.id.tab_msg);
        tabMsgIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick({R.id.empty_layout})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.empty_layout:
                Intent intent = new Intent(getActivity(), TableBoxActivity.class);
                startActivity(intent);
                break;
        }
    }
}

