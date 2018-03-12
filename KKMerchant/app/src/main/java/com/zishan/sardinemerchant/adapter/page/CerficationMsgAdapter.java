package com.zishan.sardinemerchant.adapter.page;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.zishan.sardinemerchant.R;

import java.util.List;

import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.adapter.CommonViewHolder;

/**
 * 核销信息
 * Created by wislie on 2017/11/6.
 */

public class CerficationMsgAdapter extends CommonAdapter<String> {

    private LayoutParams lp;
    public CerficationMsgAdapter(Context context, List<String> objects) {
        super(context, objects);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 46));
    }

    @Override
    public void convert(final CommonViewHolder holder, String data) {
        RelativeLayout layout = holder.getView(R.id.item_cerfication_msg);
//        layout.setLayoutParams(lp);

    }
    public int getItemLayoutId() {
        return R.layout.item_cerification_msg;
    }
}
