package com.kuku.kkmerchant.utils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuku.kkmerchant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.ftas.ftaswidget.tabbar.tabswitch.model.TabItem;


/**
 * Created by tik on 17/8/27.
 */

public class CustomTabItem extends TabItem{
	@BindView(R.id.main_tab_icon)
	ImageView tabIcon;
	@BindView(R.id.main_tab_title)
	TextView tabTitle;

	public CustomTabItem(Fragment itemFragment, View itemView, int iconResId, String title) {
		super(itemFragment, itemView);
		ButterKnife.bind(this,itemView);
		tabIcon = (ImageView) itemView.findViewById(R.id.main_tab_icon);
		tabTitle = (TextView) itemView.findViewById(R.id.main_tab_title);
		tabIcon.setImageResource(iconResId);
		tabTitle.setText(title);
	}
	@Override
	public void onItemStateChanged(boolean selected) {
		tabIcon.setSelected(selected);
		tabTitle.setSelected(selected);
	}
}
