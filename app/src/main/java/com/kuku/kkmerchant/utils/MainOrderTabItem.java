package com.kuku.kkmerchant.utils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.ftas.ftaswidget.tabbar.tabswitch.model.TabItem;


/**
 * 订单
 * Created by tik on 17/8/27.
 */

public class MainOrderTabItem extends TabItem{
	@BindView(R.id.order_name)
	TextView orderName;

	public MainOrderTabItem(Fragment itemFragment, View itemView, String title) {
		super(itemFragment, itemView);
		ButterKnife.bind(this,itemView);
		orderName = (TextView) itemView.findViewById(R.id.order_name);
		orderName.setText(title);
	}
	@Override
	public void onItemStateChanged(boolean selected) {
		orderName.setSelected(selected);
	}
}
