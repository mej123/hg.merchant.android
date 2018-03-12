package com.zishan.sardinemerchant.activity.personal.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.wislie.rxjava.model.personal.SelectBankEntity;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.SelectBankAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 选择银行
 */

public class SelectBankActivity extends BActivity {
    @BindView(R.id.recycle_select_bank_view)
    RecyclerView mRecycleSelectBankView;
    private List<SelectBankEntity> dataList = new ArrayList<>();
   // private int result_ok = 002;
    private SelectBankAdapter mAdapter;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.select_bank));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_bank;
    }

    @Override
    protected void initContentView() {

        String[] bankNameList = new String[]{"中国工商银行", "中国农业银行", "中国银行",
                "中国建设银行"," 交通银行", "中信银行", "中国光大银行", "华夏银行",
                "中国民生银行", " 广东发展银行", "招商银行", "兴业银行", "上海浦东发展银行",
                "徽商银行", "中国邮政储蓄银行",
        };
        int[] imageBankList = new int[]{R.mipmap.bank_icbc_icon, R.mipmap.bank_abc_icon,
                R.mipmap.bank_bc_icon, R.mipmap.bank_ccb_icon, R.mipmap.bank_bocom_icon,
                R.mipmap.bank_ecitic_icon, R.mipmap.bank_ceb_icon, R.mipmap.bank_hxb_icon
                , R.mipmap.bank_cmbc_icon, R.mipmap.bank_cgb_icon, R.mipmap.bank_cmb_icon
                , R.mipmap.bank_cib_icon, R.mipmap.bank_spdb_icon, R.mipmap.bank_hsb_icon,
                R.mipmap.china_post_icon};
        final int[] bankCodeList = new int[]{102, 103, 104, 105,301, 302,
                303, 304, 305, 306, 308, 309, 310, 319, 403};

        //遍历银行名字数据,存入集合
        for (int i = 0; i < bankNameList.length; i++) {
            String name = bankNameList[i];
            SelectBankEntity selectBankEntity = new SelectBankEntity();
            selectBankEntity.setBankName(name);
            dataList.add(selectBankEntity);
        }

        //遍历银行名称数据,存入集合
        for (int i = 0; i < imageBankList.length; i++) {
            int bankImage = imageBankList[i];
            dataList.get(i).setBankIcon(bankImage);
        }
        //遍历银行名称代码,存入集合
        for (int i = 0; i < bankCodeList.length; i++) {
            int bankCode = bankCodeList[i];
            dataList.get(i).setBankCode(bankCode);
        }

        mAdapter = new SelectBankAdapter(R.layout.item_select_bank, dataList);
        mRecycleSelectBankView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleSelectBankView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SelectBankEntity selectBankEntity = mAdapter.getItem(position);
//                SelectBankEntity selectBankEntity = dataList.get(position);
                if (selectBankEntity == null) return;
                String bankName = selectBankEntity.getBankName();//银行卡名
                int bankCode = selectBankEntity.getBankCode();//银行卡代码
                if (TextUtils.isEmpty(bankName)) return;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("bankName", bankName);
                bundle.putInt("bankCode", bankCode);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                SelectBankActivity.this.finish();
            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

}
