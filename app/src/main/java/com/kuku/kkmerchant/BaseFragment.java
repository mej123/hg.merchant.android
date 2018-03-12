package com.kuku.kkmerchant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wislie.rxjava.presenter.BasePresenter;
import com.example.wislie.rxjava.view.BaseView;
import com.hg.ftas.fragment.FtasMobileFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.dunit.annotation.DUnitHidden;

/**
 * Created by tik on 17/7/12.
 */

@DUnitHidden
@DUnit(group = RootGroup.OtherGroup.class)
public abstract class BaseFragment<V extends BaseView, T extends BasePresenter<V>> extends FtasMobileFragment{
	protected Activity mActivity;
	protected T mPresenter;
	protected Unbinder mUnbinder;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//创建Presenter
		mPresenter = createPresenter();
		if(mPresenter != null){
			mPresenter.attachView((V)this);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Activity){
			mActivity = (Activity) context;
		}else {
			throw new IllegalStateException("Fragment被引用到非Activity实例上");
		}
	}

	protected abstract int getLayoutResId();
	protected abstract void initBizView(View view);

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(getLayoutResId(), container, false);
		//绑定控件
		mUnbinder = ButterKnife.bind(this, root);
		initBizView(root);
		return root;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//解除presenter与view之间的关系
		if(mPresenter != null) {
			mPresenter.detachView();
		}
		//解绑
		if(mUnbinder != null){
			mUnbinder.unbind();
		}
	}

	protected abstract T createPresenter();
}
