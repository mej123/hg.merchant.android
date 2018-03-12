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
 * 位置
 * Created by tik on 17/8/27.
 */

public class MainPositionTabItem extends TabItem{
	@BindView(R.id.position_name)
	TextView positionName;
	@BindView(R.id.position_percent)
	TextView positionPercent;
	@BindView(R.id.bottom_line)
	View bottomLine;

	public MainPositionTabItem(Fragment itemFragment, View itemView, String title, String percent) {
		super(itemFragment, itemView);
		ButterKnife.bind(this,itemView);
		positionName = (TextView) itemView.findViewById(R.id.position_name);
		positionPercent = (TextView) itemView.findViewById(R.id.position_percent);
		bottomLine = itemView.findViewById(R.id.bottom_line);
		positionName.setText(title);
		positionPercent.setText(percent);
	}
	@Override
	public void onItemStateChanged(boolean selected) {
		positionName.setSelected(selected);
		positionPercent.setSelected(selected);
		bottomLine.setVisibility(selected ? View.VISIBLE : View.GONE);
	}
}
