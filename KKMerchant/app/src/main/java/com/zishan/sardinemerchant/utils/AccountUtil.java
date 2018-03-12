package com.zishan.sardinemerchant.utils;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.model.page.ToolGrantEntity;
import com.zishan.sardinemerchant.ClientApplication;

import java.util.List;

/**
 * 账号信息
 * Created by wislie on 2018/1/23.
 */

public class AccountUtil {

    public static void saveAccount(UserAccountEntity data){
        //保存storeid
        UserConfig.getInstance(ClientApplication.getApp()).setStoreId(data.getStoreId());
        //保存 员工id
        UserConfig.getInstance(ClientApplication.getApp()).setEmployeeId(data.getEmployeeId());
        //保存角色id
        UserConfig.getInstance(ClientApplication.getApp()).setRoleId(data.getRoleId());
        //保存商店名称
        UserConfig.getInstance(ClientApplication.getApp()).setStoreName(data.getStoreName());
        //保存商户名称
        UserConfig.getInstance(ClientApplication.getApp()).setMerchantName(data.getMerchantName());
        //保存商户id
        UserConfig.getInstance(ClientApplication.getApp()).setMerchantId(data.getMerchantId());
        UserConfig.getInstance(ClientApplication.getApp()).setOwner(data.getOwner());
        UserConfig.getInstance(ClientApplication.getApp()).setRoleName(data.getRoleName());
        UserConfig.getInstance(ClientApplication.getApp()).setPersonalAvatar(data.getLogoPicUrl());

        //开通与权限
        List<ToolGrantEntity> dataList = data.getModuleDTO();
        if(dataList == null) return;
        for(int i = 0; i < dataList.size(); i++){
            ToolGrantEntity entity = dataList.get(i);
            UserConfig.getInstance(ClientApplication.getApp()).setGrantTool(entity.getId(), entity);
        }
    }
}
