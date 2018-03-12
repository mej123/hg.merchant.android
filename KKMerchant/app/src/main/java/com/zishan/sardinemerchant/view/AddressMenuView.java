package com.zishan.sardinemerchant.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import top.ftas.ftaswidget.recyclerview.SpecifiedRecyclerView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;


/**
 * 城市列表
 * Created by wislie on 2018/1/12.
 */

public abstract class AddressMenuView extends RelativeLayout {

    private TabLayout mTabLayout;

    private SpecifiedRecyclerView mRecycler;

    //选中的第一级菜单下标,第二级菜单下标, 第三级菜单下标
    private int[] mMenuIndexs = {-1, -1, -1};

    public AddressMenuView(Context context) {
        super(context);
        init(context);
    }

    public AddressMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.drop_menu_address, this, true);
        mTabLayout = (TabLayout) findViewById(R.id.address_tab_layout);

        mRecycler = (SpecifiedRecyclerView) findViewById(R.id.drop_menu_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(getAdapter());
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                int totalCount = mTabLayout.getTabCount();
                removeTab(pos, totalCount);
                setTabSelected(pos);
                onTabClick(pos, mMenuIndexs);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                int totalCount = mTabLayout.getTabCount();
                removeTab(pos, totalCount);
                onTabClick(tab.getPosition(), mMenuIndexs);
            }
        });

        findViewById(R.id.address_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认
                onConfirm(getConfirmPosition(), mMenuIndexs);
            }
        });

        findViewById(R.id.address_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                onCancel();
            }
        });

        addTab();
    }

    private int getConfirmPosition(){
        int selectedTabPosition = getSelectedTabPosition();
        if(mMenuIndexs[selectedTabPosition] >= 0) return selectedTabPosition;
        if(selectedTabPosition > 0) return selectedTabPosition - 1;
        return -1;
    }


    //添加tab
    public void addTab() {
        //先设置选中状态
        setTabSelected(mTabLayout.getTabCount());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.tab_address, null);
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setCustomView(view);
        mTabLayout.addTab(tab);
        tab.select();
    }

    //设置菜单级选中的位置
    public void setMenuSelectedPos(int pos) {
        mMenuIndexs[mTabLayout.getTabCount() - 1] = pos;
    }


    //设置选中状态
    public void setTabSelected(int pos) {
        if (mTabLayout == null) return;
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            TextView tabTitleText = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
            View tabLine = tab.getCustomView().findViewById(R.id.tab_line);
            if (i == pos) {
                tabTitleText.setSelected(true);
                tabLine.setVisibility(VISIBLE);
            } else {
                tabTitleText.setSelected(false);
                tabLine.setVisibility(GONE);
            }
        }
    }

    //设置tab的title
    public void setTabTitle(int pos, String title) {
        if (mTabLayout == null) return;
        TabLayout.Tab tab = mTabLayout.getTabAt(pos);
        TextView tabTitleText = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
        tabTitleText.setText(title);
    }


    //删除tab
    private void removeTab(int pos, int totalCount) {
        //删除pos 与 mTabLayout.getTabCount() - 1之间的tab
        if (pos < totalCount - 1) {
            for (int i = totalCount - 1; i > pos; i--) {
                mTabLayout.removeTabAt(i);
                mMenuIndexs[i] = -1;
            }
        }
    }

    /*public void setMenuIndexs(int[] menuIndexs) {
        mMenuIndexs = menuIndexs;
    }*/

    public int getSelectedTabPosition() {
        if (mTabLayout == null) return 0;
        return mTabLayout.getSelectedTabPosition();
    }

    protected abstract BaseQuickAdapter getAdapter();

    //tab点击
    protected abstract void onTabClick(int position, int[] menuIndexs);

    //确定 position tab的下标值, menuIndexs 为第一级菜单,第二级菜单, 第三级菜单组成的数组
    protected abstract void onConfirm(int position, int[] menuIndexs);

    //取消
    protected abstract void onCancel();

}
