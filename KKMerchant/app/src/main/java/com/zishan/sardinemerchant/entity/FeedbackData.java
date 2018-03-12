package com.zishan.sardinemerchant.entity;

/**
 * 反馈
 * Created by wislie on 2017/11/16.
 */

public class FeedbackData {
    //是否选中
    private boolean isChecked;
    //内容
    private String content;

    public FeedbackData(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
