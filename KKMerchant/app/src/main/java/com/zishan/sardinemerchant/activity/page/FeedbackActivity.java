package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.page.advance_remind.FeedbackPresenter;
import com.example.wislie.rxjava.view.base.page.advance_remind.FeedbackView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.FeedbackAdapter;
import com.zishan.sardinemerchant.entity.FeedbackData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.SoftInputUtil;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.view.CustomEditText;
import top.ftas.ftaswidget.view.CustomToast;

/**
 * 拒绝理由
 * Created by wislie on 2017/11/16.
 */

public class FeedbackActivity extends BActivity<FeedbackView, FeedbackPresenter>
        implements FeedbackView {

    @BindView(R.id.input_edit)
    CustomEditText mInputEdit;
    @BindView(R.id.feedback_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.feedback_confirm)
    Button mConfirmBtn;

    private FeedbackAdapter mAdapter;

    private List<FeedbackData> mDataList = new ArrayList<>();

    private int bespeak_id;

    @Override
    protected FeedbackPresenter createPresenter() {
        return new FeedbackPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.refuse_reason_title));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            bespeak_id = intent.getIntExtra("bespeak_id", bespeak_id);
        }
        mDataList.add(new FeedbackData("理由是什么ne?我怎么知道1"));
        mDataList.add(new FeedbackData("理由是什么ne?我怎么知道2"));
        mDataList.add(new FeedbackData("理由是什么ne?我怎么知道3"));
        mDataList.add(new FeedbackData("理由是什么ne?我怎么知道4"));

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new FeedbackAdapter(this, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new CommonAdapter.OnAdapterItemListener() {

            @Override
            public void onSelected(int position) {
                FeedbackData data = mDataList.get(position);
                data.setChecked(!data.isChecked());
                //如果选中，则填充输入框的内容
                if (data.isChecked()) {
                    mInputEdit.getInputEdit().setText(data.getContent());
                }

                resetChecked(position);
                mAdapter.notifyDataSetChanged();

            }
        });

        mInputEdit.setInputTextListener(new CustomEditText.InputTextListener() {
            @Override
            public void observeTextLength(int len) {
                if (len != 0) {
                    mConfirmBtn.setSelected(true);
                } else {
                    mConfirmBtn.setSelected(false);
                    clearChecked();
                    mAdapter.notifyDataSetChanged();
                }
            }


        });
    }

    @OnClick({R.id.feedback_confirm, R.id.left_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_confirm:
                String input = mInputEdit.getInputEdit().getText().toString().trim();
                if (TextUtils.isEmpty(input)) return;
                mPresenter.requestRefuseAppointmentRemind(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        bespeak_id, input);
                break;
            case R.id.left_layout:
                //键盘缩回
                SoftInputUtil.hideDirectly(this);
                break;
        }
    }


    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }


    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    /**
     * 将原来选中的设置为未选中
     *
     * @param position
     */
    private void resetChecked(int position) {
        for (int i = 0; i < mDataList.size(); i++) {
            FeedbackData data = mDataList.get(i);
            if (position != i) {
                data.setChecked(false);
            }
        }
    }

    /**
     * 清空选中的
     */
    private void clearChecked() {
        for (int i = 0; i < mDataList.size(); i++) {
            FeedbackData data = mDataList.get(i);
            data.setChecked(false);

        }
    }


    @Override
    public void refuseAppointmentInformSuccess(Object data) {
        CustomToast.makeText(this, "用户反馈成功", 2000).show();
        finish();
    }

    @Override
    public void refuseAppointmentInformFailed() {

    }
}
