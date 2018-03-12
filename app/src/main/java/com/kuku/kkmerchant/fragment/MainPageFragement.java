package com.kuku.kkmerchant.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.activity.ClassifyManageActivity;
import com.kuku.kkmerchant.dialog.ConfirmDialog;
import com.kuku.kkmerchant.dialog.ConfirmOrCancelDialog;
import com.kuku.kkmerchant.dialog.FeedbackDialog;

import com.kuku.kkmerchant.dialog.SettingInputDialog;
import com.kuku.kkmerchant.store.TablePandectActivity;

import com.kuku.kkmerchant.utils.MainPositionTabItem;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.tabbar.tabswitch.helper.TabSwitchHelper;

/**
 * Created by yang on 2017/9/12.
 */

public class MainPageFragement extends BaseFragment {
    
    @BindView(R.id.card_seat_tab)
    ViewGroup itemCardSeat;
    @BindView(R.id.hall_tab)
    ViewGroup itemHall;
    @BindView(R.id.box_tab)
    ViewGroup itemBox;
    private TabSwitchHelper mTabSwitchHelper;
    
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    
    @Override
    protected void loadData() {
        
    }
    
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_page;
    }
    
    @Override
    protected void initBizView() {
        mTabSwitchHelper = new TabSwitchHelper(R.id.position_container);
        mTabSwitchHelper.setAppCompatActivity(getActivity());
        mTabSwitchHelper.addTabItem(new MainPositionTabItem(new HallFragment(), itemHall, "大厅", "1/30"));
        mTabSwitchHelper.addTabItem(new MainPositionTabItem(new HallFragment(), itemCardSeat, "卡座","2/30"));
        mTabSwitchHelper.addTabItem(new MainPositionTabItem(new HallFragment(), itemBox, "包厢","3/30"));
        mTabSwitchHelper.init();
    }
    
    
    @OnClick({R.id.order_remind, R.id.purchase_checkout, R.id.table_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_remind:
                
                ArrayList<String> strArr = new ArrayList<>();
                strArr.add("块");
                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认删除块这个菜品单位", strArr);
                dialog.showDialog(getActivity().getFragmentManager());
                break;
            case R.id.purchase_checkout:
                FeedbackDialog feedbackDialog = FeedbackDialog.newInstance("拒绝理由");
                feedbackDialog.showDialog(getActivity().getFragmentManager());
                break;
                
            case R.id.table_view: //桌台总览
                //                Skip.toActivity(getActivity(), TablePandectActivity.class);
                //Eidttext有点问题
                SettingInputDialog personNumInputDialog = SettingInputDialog.newInstance("请设置用餐人数", "请输入用餐人数", SettingInputDialog.InputStyle.Number);
                personNumInputDialog.showDialog(getActivity().getFragmentManager());
                
                //                Intent intent = new Intent(getActivity(), TableBoxActivity.class);
                //                startActivity(intent);
                
                Intent intent = new Intent(getActivity(), ClassifyManageActivity.class);
                startActivity(intent);
                
                //                ConfirmDialog confirmDialog = ConfirmDialog.newInstance("分类中含有该商品，不可删除");
                //                confirmDialog.showDialog(getActivity().getFragmentManager());
                break;
        }
    }
    
}

