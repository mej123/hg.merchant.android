package com.zishan.sardinemerchant.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.wislie.rxjava.UserConfig;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.activity.page.AppointmentRemindActivity;
import com.zishan.sardinemerchant.activity.page.ChartActivity;
import com.zishan.sardinemerchant.activity.page.MyStoreActivity;
import com.zishan.sardinemerchant.activity.page.RepastManageActivity;
import com.zishan.sardinemerchant.activity.page.TableBoxActivity;
import com.zishan.sardinemerchant.activity.page.TicketConfigActivity;
import com.zishan.sardinemerchant.activity.page.TicketListActivity;
import com.zishan.sardinemerchant.activity.personal.bill.BillActivity;
import com.zishan.sardinemerchant.activity.personal.market.MarketingActivity;
import com.zishan.sardinemerchant.activity.personal.accounts.NewMyAccountFirstActivity;
import com.zishan.sardinemerchant.activity.personal.staffs.NewStaffListActivity;
import com.zishan.sardinemerchant.activity.personal.store_msg.StoreMsgEditActivity;
import com.zishan.sardinemerchant.activity.store.StoreManageActivity;
import com.zishan.sardinemerchant.dialog.PermissionDialog;

import java.util.ArrayList;

/**
 * 根据指定信息跳转activity
 * Created by wislie on 2018/1/2.
 */

public class SkipUtil {

    public static void skipActivity(Activity context, String page) {

        switch (page) {
            case "HGInfoEditeVC":
                if (!permissionAllowed(4)) {
                    showPermisssionDialog(context, "店铺信息管理功能权限");
                } else {
                    Intent storeMsgIntent = new Intent(context, StoreMsgEditActivity.class);

                    storeMsgIntent.putExtra("store_id",
                            UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
                    context.startActivity(storeMsgIntent);
                }
                break;
            case "HGMerchantShopVC":
                if (!permissionAllowed(5)) {
                    showPermisssionDialog(context, "商品功能权限");
                } else {
                    Intent storeIntent = new Intent(context, StoreManageActivity.class);
                    context.startActivity(storeIntent);
                }
                break;
            case "HGBillVC":
                if (!permissionAllowed(11)) {
                    showPermisssionDialog(context, "账单功能权限");
                } else {
                    Intent billIntent = new Intent(context, BillActivity.class);
                    context.startActivity(billIntent);
                }
                break;
            case "HGAccountVC":
                if (!permissionAllowed(12)) {
                    showPermisssionDialog(context, "账户功能权限");
                } else {
                    Intent accountIntent = new Intent(context, NewMyAccountFirstActivity.class);
                    context.startActivity(accountIntent);
                }
                break;
            case "HGEmployeeVC":
                if (!permissionAllowed(7)) {
                    showPermisssionDialog(context, "员工功能权限");
                } else {
                    Intent staffIntent = new Intent(context, NewStaffListActivity.class);
                    context.startActivity(staffIntent);
                }
                break;
            case "HGMarketingVC":
                if (!permissionAllowed(6)) {
                    showPermisssionDialog(context, "营销功能权限");
                } else {
                    Intent marketIntent = new Intent(context, MarketingActivity.class);
                    context.startActivity(marketIntent);
                }
                break;
            case "HGTableListVC":
                Intent tableBoxIntent = new Intent(context, TableBoxActivity.class);
                context.startActivity(tableBoxIntent);
                break;
            case "HGOrderingSegmentVC":
//                UserConfig.getInstance(ClientApplication.getApp()).setNewMsg(false);
                Intent appointmentIntent = new Intent(context, AppointmentRemindActivity.class);
                context.startActivity(appointmentIntent);
                break;
            case "HGRestaurantManagerVC":
                if (!permissionAllowed(14)) {
                    showPermisssionDialog(context, "营业桌台功能权限");
                } else {
                    Intent repastIntent = new Intent(context, RepastManageActivity.class);
                    context.startActivity(repastIntent);
                }
                break;
            case "HGMerchantDataVC":
                Intent chartIntent = new Intent(context, ChartActivity.class);
                context.startActivity(chartIntent);
                break;
            case "HGMerchantListVC":
                Intent mystoreIntent = new Intent(context, MyStoreActivity.class);
                context.startActivity(mystoreIntent);
                break;
            case "HGCouponConfigManagerVC":
                Intent ticketConfigIntent = new Intent(context, TicketConfigActivity.class);
                context.startActivity(ticketConfigIntent);
                break;
            case "HGDistributionPoolVC":
                Intent ticketListIntent = new Intent(context, TicketListActivity.class);
                context.startActivity(ticketListIntent);
                break;
        }
    }


    //true表示可以查看
    private static boolean permissionAllowed(int index) {
        ArrayList<Boolean> dataList = UserConfig.getInstance(ClientApplication.getApp()).getPermission();
        if (dataList != null && dataList.size() == 15) {
            Boolean data = dataList.get(index);
            if (data == Boolean.TRUE) return true;
            return false;
        }
        return true;
    }

    /**
     * 权限dialog提示
     *
     * @param title
     */
    private static void showPermisssionDialog(Activity context, String title) {
        PermissionDialog dialog = PermissionDialog.newInstance(title);
        dialog.showDialog(context.getFragmentManager());
    }
}
