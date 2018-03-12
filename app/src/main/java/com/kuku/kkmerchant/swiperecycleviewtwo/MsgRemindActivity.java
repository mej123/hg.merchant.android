package com.kuku.kkmerchant.swiperecycleviewtwo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.kuku.kkmerchant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息主界面
 */
public class MsgRemindActivity extends Activity {
    @BindView(R.id.rv_msg_remind)
    RecyclerView rvMsgRemind;
    private MsgRemindAdapter msgRemindAdapter;
    private List<MsgVo> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_msg_remind);
        ButterKnife.bind(this);
        initData();
        //设置布局类型为listview的LinearLayoutManager
        rvMsgRemind.setLayoutManager(new LinearLayoutManager(this));
        rvMsgRemind.addItemDecoration(new RecyclerViewListDecoration(this,
                RecyclerViewListDecoration.VERTICAL_LIST));
        //固定recyclerview大小
        rvMsgRemind.setHasFixedSize(true);
        rvMsgRemind.setAdapter(msgRemindAdapter);
    }

    private void initData() {
        mDatas = new ArrayList<MsgVo>();
        for (int i = 'A'; i < 'G'; i++) {
            MsgVo vo = new MsgVo();
            if (i % 2 == 1) {
                vo.setChecked(true);
                vo.setTitle("原始消息，已读状态" + (char) i);
                vo.setContent("乌啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦" +
                        "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦" +
                        "啦啦啦啦啦啦啦啦啦");
            } else {
                vo.setChecked(false);
                vo.setTitle("原始消息，未读状态");
                vo.setContent("唔噜噜噜噜噜噜噜噜噜噜");
            }
            mDatas.add(vo);
        }
        msgRemindAdapter = new MsgRemindAdapter(this, mDatas);
    }
}
