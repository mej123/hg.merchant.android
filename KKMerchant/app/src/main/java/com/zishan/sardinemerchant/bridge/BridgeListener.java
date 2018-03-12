package com.zishan.sardinemerchant.bridge;

import java.util.Map;

/**
 * Created by wislie on 2018/3/9.
 */

public interface BridgeListener {

    void getJsInvokeNativeResult(Map<String, String> resultMap);
}
