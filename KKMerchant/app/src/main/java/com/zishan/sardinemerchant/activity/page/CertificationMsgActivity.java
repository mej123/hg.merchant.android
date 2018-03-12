package com.zishan.sardinemerchant.activity.page;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.CerficationMsgAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewFooter;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 核销消息
 * Created by wislie on 2017/11/7.
 */

public class CertificationMsgActivity extends BActivity {

    @BindView(R.id.no_msg_springview)
    SpringView mSpringView;
    @BindView(R.id.no_msg_recycler_view)
    RecyclerView mRecycler;

    private CerficationMsgAdapter mAdapter;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cerification_msg;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getResources().getString(R.string.cerification_msg));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mDataList.addAll(Arrays.asList("a","a","a"));
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CerficationMsgAdapter(this, mDataList);
        mRecycler.setAdapter(mAdapter);
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setFooter(new SpringViewFooter(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {

                    //暂时这些写
                    finishRefreshAndLoad();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }
}
