package com.zishan.sardinemerchant.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Created by wislie on 2017/11/27.
 */


@SuppressWarnings("unused")
public class ProgressLoadingDialog extends ProgressDialog {

    private long mStartTime = -1;

    private boolean mPostedHide = false;

    private boolean mPostedShow = false;

    private boolean mDismissed = false;

    private int minShowTime = 500; // ms

    private int minDelay = 500; // ms

    private Handler mHandler;

    private final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            ProgressLoadingDialog.super.dismiss();
        }
    };

    private final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mPostedShow = false;
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis();
                ProgressLoadingDialog.super.show();
            }
        }
    };

    public ProgressLoadingDialog(Context context) {
        super(context);
        mHandler = new Handler();
    }

    public ProgressLoadingDialog(Context context, int theme) {
        super(context, theme);
        mHandler = new Handler();
    }

    public void setMinShowTime(int minShowTime) {
        this.minShowTime = minShowTime;
    }

    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
    }

    public static ProgressLoadingDialog make(Context context, CharSequence title,
                                             CharSequence message) {
        return make(context, title, message, false);
    }

    public static ProgressLoadingDialog make(Context context, CharSequence title,
                                             CharSequence message, boolean indeterminate) {
        return make(context, title, message, indeterminate, false, null);
    }

    public static ProgressLoadingDialog make(Context context, CharSequence title,
                                             CharSequence message, boolean indeterminate, boolean cancelable) {
        return make(context, title, message, indeterminate, cancelable, null);
    }

    public static ProgressLoadingDialog make(Context context, CharSequence title,
                                             CharSequence message,
                                             boolean indeterminate, boolean cancelable,
                                             OnCancelListener cancelListener) {
        ProgressLoadingDialog dialog = new ProgressLoadingDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        return dialog;
    }

    public static ProgressLoadingDialog showDelayed(Context context, CharSequence title,
                                                    CharSequence message) {
        return showDelayed(context, title, message, false);
    }

    public static ProgressLoadingDialog showDelayed(Context context, CharSequence title,
                                                    CharSequence message, boolean indeterminate) {
        return showDelayed(context, title, message, indeterminate, false, null);
    }

    public static ProgressLoadingDialog showDelayed(Context context, CharSequence title,
                                                    CharSequence message, boolean indeterminate,
                                                    boolean cancelable) {
        return showDelayed(context, title, message, indeterminate, cancelable, null);
    }

    public static ProgressLoadingDialog showDelayed(Context context, CharSequence title,
                                                    CharSequence message,
                                                    boolean indeterminate, boolean cancelable,
                                                    OnCancelListener cancelListener) {
        ProgressLoadingDialog dialog = make(context, title, message,
                indeterminate, cancelable, cancelListener);
        dialog.show();
        return dialog;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mDelayedHide);
            mHandler.removeCallbacks(mDelayedShow);
        }
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @Override
    public void dismiss() {
        mDismissed = true;
        mHandler.removeCallbacks(mDelayedShow);
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= minShowTime || mStartTime == -1) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            super.dismiss();
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                mHandler.postDelayed(mDelayedHide, minShowTime - diff);
                mPostedHide = true;
            }
        }
        super.dismiss();
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @Override
    public void show() {
        // Reset the start time.
        mStartTime = -1;
        mDismissed = false;
        mHandler.removeCallbacks(mDelayedHide);
        if (!mPostedShow) {
            mHandler.postDelayed(mDelayedShow, minDelay);
            mPostedShow = true;
        }
    }

}