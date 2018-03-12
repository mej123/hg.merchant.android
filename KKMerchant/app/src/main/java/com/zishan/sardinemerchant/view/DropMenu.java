package com.zishan.sardinemerchant.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zishan.sardinemerchant.R;

/**
 * 总览筛选的 下拉对话框
 * Created by wislie on 2017/10/23.
 */

public class DropMenu extends LinearLayout {

    private View mMaskView;
    private View mPopView;
    private ImageView mArrowIcon;
    //图片id
    private int mNormalResId;
    private int mDropResId;

    //遮罩的颜色设置为半透明
    private int maskColor = 0x7f000000;
    private FrameLayout mContainerLayout;

    public DropMenu(Context context) {
        super(context);

    }

    //创建帧布局
    public DropMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mContainerLayout = new FrameLayout(context);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainerLayout.setLayoutParams(lp);
        addView(mContainerLayout);

    }

    //初始化遮罩 下拉框布局, 并添加到帧布局中
    public void initDropMenuView(Context context, View popupView, ImageView arrowIcon, int normalResId, int dropResId) {

        mMaskView = new View(context);
        mMaskView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mMaskView.setBackgroundColor(maskColor);

        if (mContainerLayout.getChildAt(0) != null) {
            mContainerLayout.removeViewAt(0);
        }
        mMaskView.setVisibility(GONE);
        mContainerLayout.addView(mMaskView, 0);

        mPopView = popupView;
        popupView.setVisibility(GONE);
        if (mContainerLayout.getChildAt(1) != null) {
            mContainerLayout.removeViewAt(1);
        }
        mContainerLayout.addView(popupView, 1);
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDropMenu(getContext());
            }
        });
        mArrowIcon = arrowIcon;
        mNormalResId = normalResId;
        mDropResId = dropResId;
    }

    /**
     * 显示对话框
     *
     * @param context
     */
    public void showDropMenu(Context context) {
        mMaskView.setVisibility(VISIBLE);
        mPopView.setVisibility(VISIBLE);
        mMaskView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dropdownmenu_mask_in));
        mPopView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dropdownmenu_in));
        if (mArrowIcon != null)
            mArrowIcon.setImageResource(mDropResId);
    }

    /**
     * 隐藏对话框
     *
     * @param context
     */
    public void hideDropMenu(Context context) {
        mMaskView.setVisibility(GONE);
        mPopView.setVisibility(GONE);
        mMaskView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dropdownmenu_mask_out));
        mPopView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dropdownmenu_out));
        if (mArrowIcon != null)
            mArrowIcon.setImageResource(mNormalResId);
    }

    /**
     * 判断下拉框是否可见
     *
     * @return
     */
    public boolean isMenuVisible() {
        if (mPopView == null || mPopView.getVisibility() != View.VISIBLE) return false;
        return true;
    }


}
