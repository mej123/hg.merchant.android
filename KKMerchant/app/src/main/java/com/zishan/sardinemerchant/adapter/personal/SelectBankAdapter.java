package com.zishan.sardinemerchant.adapter.personal;

import com.example.wislie.rxjava.model.personal.SelectBankEntity;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseViewHolder;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 绑定银行卡   选择银行适配器
 */

public class SelectBankAdapter extends BaseQuickAdapter<SelectBankEntity> {


    public SelectBankAdapter(int layoutResId, List<SelectBankEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SelectBankEntity mSelectBankEntity) {
        if (mSelectBankEntity == null) return;

        holder.setText(R.id.bank_name, mSelectBankEntity.getBankName());
        holder.setImageResource(R.id.iv_bank_icon, mSelectBankEntity.getBankIcon());
    }
}
