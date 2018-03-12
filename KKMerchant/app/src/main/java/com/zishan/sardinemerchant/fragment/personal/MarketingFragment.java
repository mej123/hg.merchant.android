package com.zishan.sardinemerchant.fragment.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.fragment.BaseWebFragment;

import butterknife.BindView;

/**
 * 营销
 * Created by wislie on 2017/12/12.
 */

public class MarketingFragment extends BaseWebFragment {
    @BindView(R.id.webview_container)
    LinearLayout mContainer;

    private String mUrl;

    public static MarketingFragment newInstance(String url) {
        MarketingFragment fg = new MarketingFragment();
        Bundle data = new Bundle();
        data.putString("url", url);
        fg.setArguments(data);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mUrl = data.getString("url");
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    protected void initBizView() {
        super.initBizView();
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

        syncCookie(getActivity(), mUrl);
        if(getCurrentWebView() != null){
            getCurrentWebView().loadUrl(mUrl + "?shopid=" + UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
        }
    }

    public void initData(){
        loadData();
    }

    @Override
    protected void addWebView(AbstractFtasWebView webview) {
        mContainer.addView(webview);

    }

    @Override
    protected void loadUrlByWebView(AbstractFtasWebView webview) {
        setCurrentWebView(webview);
    }


}
