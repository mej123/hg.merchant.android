package com.zishan.sardinemerchant.activity.personal.bill;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.BillParticularsDetailsEntity;
import com.example.wislie.rxjava.model.personal.BillParticularsEntity;
import com.example.wislie.rxjava.presenter.personal.BillParticularsPresenter;
import com.example.wislie.rxjava.view.personal.BillParticularsView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.BillDateSelectAdapter;
import com.zishan.sardinemerchant.adapter.personal.BillParticularsAdapter;
import com.zishan.sardinemerchant.entity.SelectDateData;
import com.zishan.sardinemerchant.view.DropMenu;
import com.zishan.sardinemerchant.view.FullyLinearLayoutManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftasbase.common.util.DatePickerUtil;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftasbase.common.util.FormatMoneyUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;


/**
 * Created by yang on 2017/11/13.
 * <p>
 * 账单明细
 */
public class BillParticularsActivity extends BActivity<BillParticularsView, BillParticularsPresenter>
        implements BillParticularsView {
    @BindView(R.id.tv_order_count)
    TextView tvOrderCount;//订单
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;//总额
    @BindView(R.id.bill_particulars_recycler_view)
    RecyclerView billParticularsRecyclerView;
    @BindView(R.id.drop_menu_date)
    DropMenu mDateDropMenu;
    private View mPopView;
    private List<BillParticularsDetailsEntity> billList = new ArrayList<>();
    private List<SelectDateData> dateList = new ArrayList<>();
    private BillDateSelectAdapter mDateAdapter;
    private String date;
    private String start_time;
    private String end_time;
    private BillParticularsAdapter mAdapter;
    private Long storeId;

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.bill_particulars));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BillParticularsPresenter createPresenter() {
        return new BillParticularsPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_particulars;
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        String calculateState = intent.getStringExtra("calculateState");
        if (TextUtils.isEmpty(calculateState)) return;
        storeId = UserConfig.getInstance(ClientApplication.getApp()).
                getStoreId();
        if (storeId == null) return;

        mAdapter = new BillParticularsAdapter(R.layout.item_bill_particulars, billList);
        billParticularsRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        billParticularsRecyclerView.setNestedScrollingEnabled(false);
        billParticularsRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BillParticularsDetailsEntity billParticularsDetailsEntity = mAdapter.getItem(position);
                if (billParticularsDetailsEntity == null) return;
                Intent intent = new Intent();
                intent.setClass(BillParticularsActivity.this, BillDetailsActivity.class);
                Long id = billParticularsDetailsEntity.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //待结算界面
        if (calculateState.equals("0")) {
            String data = intent.getStringExtra("date");
            JSONArray json = new JSONArray();
            json.put(data);
            mPresenter.getDayBillFromDate(storeId, json);
        }
        //已结算界面
        if (calculateState.equals("1")) {
            showSelectButton();//显示筛选按钮
            start_time = intent.getStringExtra("start_time");
            end_time = intent.getStringExtra("end_time");
            initSelectData(dateList);
            initSelectPopupView();
            // tvData.setText(start_time + "~" + end_time);
            JSONArray json = new JSONArray();
            json.put(start_time);
            json.put(end_time);
            mPresenter.getDayBillFromDate(storeId, json);
        }
    }

    private void showSelectButton() {
        setActionBarMenuText(getString(R.string.select)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDateDropMenu.isMenuVisible()) {
                    mDateDropMenu.hideDropMenu(BillParticularsActivity.this);
                    return;
                }
                mDateDropMenu.showDropMenu(BillParticularsActivity.this);
            }
        });
    }

    private void initSelectData(List<SelectDateData> dateList) {
        if (dateList.size() > 0) return;
        //日期
        dateList.add(new SelectDateData(start_time, DatePickerUtil.getStartTime(-1), DatePickerUtil.getEndTime(-1)));
        dateList.add(new SelectDateData(end_time, DatePickerUtil.getStartTime(0), DatePickerUtil.getEndTime(3)));
    }

    private void initSelectPopupView() {

        //弹出框
        mPopView = LayoutInflater.from(this).inflate(R.layout.dropmenu_store_order_date_select, null);
        mPopView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        mDateAdapter = new BillDateSelectAdapter(this, dateList);

        RecyclerView dateRecycler = (RecyclerView) mPopView.findViewById(R.id.date_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        dateRecycler.setLayoutManager(gridLayoutManager);
        SpaceItemDecoration decoration = new SpaceItemDecoration(
                DensityUtil.dip2px(this, 4), DensityUtil.dip2px(this, 0),
                DensityUtil.dip2px(this, 4), DensityUtil.dip2px(this, 9));
        dateRecycler.addItemDecoration(decoration);
        dateRecycler.setAdapter(mDateAdapter);
        mDateAdapter.setOnItemListener(new DateAdapterListener(dateList));
        mPopView.findViewById(R.id.clear_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空
                clearSelectData(dateList);
            }
        });

        mPopView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billList.clear();
                //选中的数据
                SelectDateData dateData = getSelectData(dateList);
                String content = dateData.getContent();
                mDateDropMenu.hideDropMenu(BillParticularsActivity.this);
                //tvData.setText(content);//设置选中日期
                JSONArray json = new JSONArray();
                json.put(content);
                mPresenter.getDayBillFromDate(storeId, json);
            }
        });

        mDateDropMenu.initDropMenuView(this, mPopView, null, -1, -1);
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

    }

    @Override
    public void getDayBillFromDateComplete(BillParticularsEntity billParticularsList) {

        if (billParticularsList == null) return;
        Long TotalAmount = billParticularsList.getTotalAmount();
        tvTotalMoney.setText(FormatMoneyUtil.pennyChangeDollar(TotalAmount));//总额
        Long orderCount = billParticularsList.getOrderCount();
        tvOrderCount.setText(String.valueOf(orderCount));//订单笔数
        List<BillParticularsDetailsEntity> detailList = billParticularsList.getDetails();
        if (detailList != null) {
            // mAdapter.setNewData(detailList);
            List<BillParticularsDetailsEntity> billParticularsDetailsEntities = addLineTag(detailList);
            billList.addAll(billParticularsDetailsEntities);
            mAdapter.setNewData(billParticularsDetailsEntities);
            mAdapter.notifyDataSetChanged();

            if (billParticularsDetailsEntities.size()>5){
                mAdapter.addFooterView(showFooterNoMoreData());
            }
        }
    }

    @Override
    public void getDayBillFromDateFailed() {

    }


    private class DateAdapterListener extends CommonAdapter.OnAdapterItemListener {

        private List<SelectDateData> dateList;

        public DateAdapterListener(List<SelectDateData> dateList) {
            this.dateList = dateList;
        }

        @Override
        public void onItemClick(View view, final int position) {
            final SelectDateData selectData = dateList.get(position);
            selectData.setSelected(!selectData.isSelected());
            view.setSelected(selectData.isSelected());

            if (selectData.isSelected()) {
                mDateAdapter.notifyAdapterData(position);
            }
        }
    }

    /**
     * 获得选中的数据
     *
     * @param dataList
     * @return
     */
    public SelectDateData getSelectData(List<SelectDateData> dataList) {
        for (SelectDateData selectData : dataList) {
            if (selectData.isSelected()) return selectData;
        }
        return dataList.get(dataList.size() - 1); //即最后一个SelectDateData
    }


    /**
     * 重置列表数据
     *
     * @param dateList
     */
    private void clearSelectData(List<SelectDateData> dateList) {
        for (int i = 0; i < dateList.size(); i++) {
            SelectDateData data = dateList.get(i);
            data.setSelected(false);
        }
        mDateAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    //遍历集合数据,为每组的第一个对象添加一个标记,判定是否为第一个
    private List<BillParticularsDetailsEntity> addLineTag(List<BillParticularsDetailsEntity> dataList) {
        for (int position = 0; position < dataList.size(); position++) {
            int currentposition = position - 1;
            String currentStr = dataList.get(position).getBillDay();
            String previewStr = (currentposition) >= 0 ? dataList.get(currentposition).getBillDay() : "-1";
            if (!previewStr.equals(currentStr)) {
                dataList.get(position).setFirstLine(true);
            } else {
                dataList.get(position).setFirstLine(false);
            }
        }
        return dataList;
    }
}