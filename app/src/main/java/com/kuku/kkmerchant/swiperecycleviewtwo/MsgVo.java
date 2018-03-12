package com.kuku.kkmerchant.swiperecycleviewtwo;

import java.io.Serializable;

/**
 * 消息vo
 */
public class MsgVo implements Serializable {
    private boolean checked;
    private String title;
    private String content;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
