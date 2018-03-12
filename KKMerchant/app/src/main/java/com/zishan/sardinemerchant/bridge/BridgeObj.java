package com.zishan.sardinemerchant.bridge;

import java.io.Serializable;

/**
 * Created by wislie on 2018/3/12.
 */

public class BridgeObj implements Serializable{

    private String action;
    private String callId;
    private String paramsJsonStr;

    public BridgeObj(String action, String callId, String paramsJsonStr) {
        this.action = action;
        this.callId = callId;
        this.paramsJsonStr = paramsJsonStr;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getParamsJsonStr() {
        return paramsJsonStr;
    }

    public void setParamsJsonStr(String paramsJsonStr) {
        this.paramsJsonStr = paramsJsonStr;
    }
}
