package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.page.table_detail.ChooseDishPresenter;
import com.example.wislie.rxjava.view.base.page.table_detail.ChooseDishView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.CategoryAdapter;
import com.zishan.sardinemerchant.adapter.page.ExtraDishAdapter;
import com.zishan.sardinemerchant.dialog.FeedbackDialog;
import com.zishan.sardinemerchant.entity.SelectedDishBean;
import com.zishan.sardinemerchant.fragment.page.DishListFragment;
import com.zishan.sardinemerchant.listener.CheckListener;
import com.zishan.sardinemerchant.utils.ItemHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;
import top.ftas.ftaswidget.recyclerview.SpecifiedRecyclerView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;


/**
 * 点菜
 * Created by wislie on 2017/12/4.
 */

public class ChooseDishActivity extends BActivity<ChooseDishView, ChooseDishPresenter>
        implements ChooseDishView, CheckListener {


    @BindView(R.id.extra_recycler_view)
    SpecifiedRecyclerView mExtraRecycler;
    @BindView(R.id.category_recycler_view)
    RecyclerView mCategoryRecycler;

    @BindView(R.id.dish_total_price)
    TextView mTotalPriceText;
    @BindView(R.id.dish_selected_num)
    TextView mSelectedNumText;

    //额外的菜品
    private ExtraDishAdapter mExtraDishAdapter;

    private LinearLayoutManager mLayoutManager;

    private CategoryAdapter mAdapter;

    private final int PAGE_SIZE = 500;
    private int mCurrentPage = 0;

    private DishListFragment mDishListFragment = null;
    private boolean isMoved;
    //左侧菜单
    private List<ProductGroupEntity> mCategoryList = new ArrayList<>();
    //右侧显示的内容
    private ArrayList<ProductEntity> mDishList = new ArrayList<>();

    //桌台详情
    private String mSeatName;
    private long repast_id = 0;


    //临时菜品列表
    private List<SelectedDishBean> mTempDishList = new ArrayList<>();
    //左侧列表中选中的菜品列表
    private List<SelectedDishBean> mLeftDishList = new ArrayList<>();

    @Override
    protected ChooseDishPresenter createPresenter() {
        return new ChooseDishPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_choose_dish;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();

        Intent intent = getIntent();
        if (intent != null) {
            mSeatName = intent.getStringExtra("seat_name");
            repast_id = intent.getLongExtra("repast_id", repast_id);
            setActionbarTitle(mSeatName);
        }

        setActionBarHomeIcon(R.mipmap.back_white_icon);
        TextView addDishText = setActionBarMenuText(getString(R.string.add_dish));
        addDishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseDishActivity.this, AddDishesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initContentView() {
        mExtraRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mExtraDishAdapter = new ExtraDishAdapter(R.layout.item_extra_dish, mTempDishList);
        mExtraRecycler.setAdapter(mExtraDishAdapter);

        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mCategoryRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryAdapter(R.layout.item_category, mCategoryList);
        mCategoryRecycler.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDishListFragment != null) {
                    isMoved = true;
                    setChecked(position, true);
                }
            }
        });
        loadData();
    }

    private void loadData() {
        showProgressDialog();
        mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                mCurrentPage, PAGE_SIZE, null, null, null, null, null, true);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
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
    public void getGoodsFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void newGoodsList(ArrayList<ProductEntity> dataList) {
        //对datalist进行过滤
        mDishList.clear();
        mDishList.addAll(getDishListByGroupId(dataList));
        createFragment(mDishList);
    }


    @Override
    public void addGoodsList(ArrayList<ProductEntity> dataList) {

    }

    @Override
    public void showCompleteAllData() {

    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {

    }

    @Override
    public void addGoodsGroups(ArrayList<ProductGroupEntity> dataList) {
        mCategoryList.clear();
        mCategoryList.addAll(dataList);
        mAdapter.setNewData(mCategoryList);

    }

    @Override
    public void getPermissionFailed() {

    }


    //按照custom_group_id进行排序
    private ArrayList<ProductEntity> getDishListByGroupId(ArrayList<ProductEntity> dishList) {
        ArrayList<ProductEntity> dataList = new ArrayList<>();
        for (int i = 0; i < mCategoryList.size(); i++) {
            long id = mCategoryList.get(i).getId();
            for (int j = 0; j < dishList.size(); j++) {
                ProductEntity dish = dishList.get(j);
                //在售 且 上架状态
                if (dish.getCustomGroupId() == id &&
                        dish.getSoldOut() == Boolean.FALSE &&
                        dish.getState() == 0) {
                    dataList.add(dish);
                }
            }
        }
        return dataList;
    }


    //根据分类得到商品的数量大小
    private int getProductSizeByCategory(List<ProductEntity> dishList, int position) {

        int size = 0;
        for (int i = 0; i < position; i++) {
            long id = mCategoryList.get(i).getId();
            for (int j = 0; j < dishList.size(); j++) {

                ProductEntity dish = dishList.get(j);
                if (dish.getCustomGroupId() == id) {
                    size++;
                }
            }
        }

        return size;
    }

    private void createFragment(ArrayList<ProductEntity> dataList) {

        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mDishListFragment = DishListFragment.newInstance(dataList);
        mDishListFragment.setListener(this);
        fragmentTransaction.add(R.id.dish_fragment, mDishListFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void check(int position, boolean isScroll) {
        setChecked(position, isScroll);
    }

    private void setChecked(int position, boolean isLeft) {
        if (isLeft) {

            mAdapter.setSelectedPos(position);
            //此处的位置需要根据每个分类的集合来进行计算
            int count = getProductSizeByCategory(mDishList, position);
            mDishListFragment.setData(count);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(position));//凡是点击左边，将左边点击的位置作为当前的tag
        } else {
            if (isMoved) {
                isMoved = false;
            } else {
                mAdapter.setSelectedPos(position);
            }

            ItemHeaderDecoration.setCurrentTag(String.valueOf(position));//如果是滑动右边联动左边，则按照右边传过来的位置作为tag

        }
        moveToCenter(position);

    }

    //将当前选中的item居中
    private void moveToCenter(int position) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        View childAt = mCategoryRecycler.getChildAt(position - mLayoutManager.findFirstVisibleItemPosition());
        if (childAt != null) {
            int y = (childAt.getTop() - mCategoryRecycler.getHeight() / 2);
            mCategoryRecycler.smoothScrollBy(0, y);
        }

    }

    @Override
    public void chooseDishSuccess(Object data) {
        finish();
    }

    @Override
    public void chooseDishFailed() {

    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) {

            if (baseEvent.getTagInt() == Constant.DISH_ADD) {

                Bundle bundle = (Bundle) baseEvent.getData();
                SelectedDishBean extraDish = (SelectedDishBean) bundle.getSerializable("extra_dish");
                mTempDishList.add(extraDish);
                mExtraDishAdapter.setNewData(mTempDishList);
            }

            mTotalPriceText.setText(StringUtil.point2String(caculateTotalPrice() + caculateExtraPrice()));
            updateCategoryNum();
        }
    }

    //计算总价
    private Long caculateTotalPrice() {
        long totalPrice = 0;
        for (int i = 0; i < mDishList.size(); i++) {
            ProductEntity data = mDishList.get(i);

            //数量大于0的要进行累加
            if (data.getSelectedCount() != null && data.getSelectedCount() > 0) {
                totalPrice += data.getRealPrice() * data.getSelectedCount();
            }
        }
        return totalPrice;
    }

    //更新选中的数量
    private void updateCategoryNum() {
        List<ProductGroupEntity> categoryList = mAdapter.getData();
        if (categoryList == null) return;
        int totalSelectNum = 0;
        mLeftDishList.clear();
        for (int i = 0; i < categoryList.size(); i++) {
            ProductGroupEntity categoryData = categoryList.get(i);
            int selectedCount = 0;
            for (int j = 0; j < mDishList.size(); j++) {
                ProductEntity data = mDishList.get(j);
                //计算被选中的数量
                if (data.getCustomGroupId().longValue() == categoryData.getId().longValue()) {
                    selectedCount += data.getSelectedCount();
                    categoryData.setSelectedCount(selectedCount);
                    totalSelectNum += data.getSelectedCount();

                    if (data.getSelectedCount() == 0) continue;
                    SelectedDishBean leftDish = new SelectedDishBean(data.getSelectedCount(),
                            data.getRealPrice(), data.getName(), data.getId());
                    mLeftDishList.add(leftDish);


                }
            }
        }
        mSelectedNumText.setVisibility(View.VISIBLE);
        mSelectedNumText.setText(String.valueOf(totalSelectNum + getExtraNum()));
        mAdapter.notifyDataSetChanged();
    }

    //计算额外选中的菜品价格
    private long caculateExtraPrice() {
        long extraPrice = 0;
        for (int i = 0; i < mTempDishList.size(); i++) {
            SelectedDishBean extraData = mTempDishList.get(i);
            extraPrice += extraData.getNum() * extraData.getPrice();
        }
        return extraPrice;
    }

    //额外选中的数量
    private int getExtraNum() {
        int num = 0;
        for (int i = 0; i < mTempDishList.size(); i++) {
            SelectedDishBean extraData = mTempDishList.get(i);
            num += extraData.getNum();
        }
        return num;
    }

    @OnClick({R.id.confirm_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_order:

                List<SelectedDishBean> selectedDishList = new ArrayList<>();
                selectedDishList.addAll(mLeftDishList);
                selectedDishList.addAll(mTempDishList);

                if (selectedDishList.size() == 0) {
                    ToastUtil.show("未选中任何菜品");
                    return;
                }

                String totalPrice = mTotalPriceText.getText().toString().trim();
                final long real_item_total_amount = StringUtil.String2Long(totalPrice, 2, true);

                final String productJson = GsonParser.parseObjToJson(selectedDishList);

                FeedbackDialog dialog = FeedbackDialog.newInstance(getString(R.string.confirm_order));
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onInputConfirm(String... values) {
                        String remark = values[0];
                        mPresenter.chooseDish(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                                repast_id, real_item_total_amount, remark, productJson);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

}
