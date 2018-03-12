package com.kuku.kkmerchant.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.kuku.kkmerchant.BaseFragment;
import com.kuku.kkmerchant.R;
import com.kuku.kkmerchant.dialog.GoodsOptionsDialog;
import com.kuku.kkmerchant.listener.GoodsOptionListener;
import com.kuku.kkmerchant.recycleviewadapter.GoodsListAdapter;
import com.kuku.kkmerchant.store.StoreGoodsEditActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.listener.ItemOnClickListener;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;

/**
 * 商品管理 商品列表
 * Created by wislie on 2017/9/21.
 */

public class GoodsListFragment extends BaseFragment {


    @BindView(R.id.goods_recycler_view)
    RecyclerView mGoodsRecycler;

    @BindView(R.id.goods_option_direction)
    ImageView mGoodOptionDirection;

    private GoodsListAdapter mAdapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_goods;
    }


    public static GoodsListFragment newInstance(){
        GoodsListFragment fragment = new GoodsListFragment();
        return fragment;
    }

    @Override
    protected void initBizView(View view) {
        mAdapter = new GoodsListAdapter(getActivity(), Arrays.asList("","","","","","","","",""));
        mGoodsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mGoodsRecycler.addItemDecoration(new SpaceItemDecoration(14, 11, 14, 11));
        mGoodsRecycler.setAdapter(mAdapter);
        mAdapter.setItemOnClickListener(new ItemOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), StoreGoodsEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.goods_spinner)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.goods_spinner:

                //先转180度
                rotateView(mGoodOptionDirection, 180f);
                ArrayList<String> options = new ArrayList<String> (Arrays.asList("全部","在售","下架"));
                //因为是全屏的，所以需要先减去状态栏高度, 因为弹框在"全部"下方，所以要加上"全部"TextView的高度, 又因为需求是 往上偏移18像素,所以 减18
                int h = DensityUtil.getViewLocation(view)[1] - DensityUtil.getStatusHeight(getActivity()) + view.getHeight() - 18;
                GoodsOptionsDialog dialog = GoodsOptionsDialog.newInstance(options, DensityUtil.getViewLocation(view)[0], h);
//                Log.e("Wislie","高度:"+DensityUtil.getStatusHeight(getActivity()));
                dialog.showDialog(getActivity().getFragmentManager());
                dialog.setGoodsOptionListener(new GoodsOptionListener() {
                    @Override
                    public void dismissDialog() {
                        //因为是帧动画，所以相当于保持不变
                        rotateView(mGoodOptionDirection, 360f);
                    }
                });
                break;

        }
    }

    /**
     * 视图选中
     * @param view
     * @param degrees
     */
    private void rotateView(View view, float degrees){
        RotateAnimation animation = new RotateAnimation(0f,degrees,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

}
