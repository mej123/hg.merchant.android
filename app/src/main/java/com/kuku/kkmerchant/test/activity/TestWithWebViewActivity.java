package com.kuku.kkmerchant.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.hg.ftas.activity.ftas.ftasview.SimpleFtasMobileActivity;
import com.hg.ftas.event.impl.FtasWebViewEvent;
import com.hg.ftas.view.AbstractFtasWebView;
import com.kuku.kkmerchant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.ftas.dunit.annotation.DUnit;

/**
 * Created by tik on 17/8/30.
 */

@DUnit
public class TestWithWebViewActivity extends SimpleFtasMobileActivity {
	@BindView(R.id.root_ll)
	LinearLayout rootLl;

	protected int getLayoutResId() {
		return R.layout.test_with_webview_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		initContentView();
	}

	protected void initContentView() {
		ButterKnife.bind(this);
		AbstractFtasWebView webView = mFtasWebViewManager.getFtasWebView(new FtasWebViewEvent(this) {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				dimissLoadingWindow();
//				dimissProgress();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
//				dimissLoadingWindow();
//				dimissProgress();
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				setTitle(title);
			}
		});
		LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		webViewParams.weight = 1;
		webView.setLayoutParams(webViewParams);
		webView.setBackgroundColor(Color.WHITE);
		rootLl.addView(webView);
		setCurrentWebView(webView);

		webView.loadRemoteUrl("http://120.27.5.209:8080/kuku-server-net/test-kkjs.html");
	}
}
