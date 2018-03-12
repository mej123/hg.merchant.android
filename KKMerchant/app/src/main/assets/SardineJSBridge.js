
function __nativeAppInvokeCallback(callId, resultJsonStr) {
//    SardineJSBridge._nativeInvokeCallback(callId, resultJsonStr);
     SardineJSBridge._nativeInvokeCallback();
};

function __nativeAppEventCallback(eventName, resultJsonStr) {
    SardineJSBridge._nativeEventCallback(eventName, resultJsonStr);

};

(function (window) {
    function readyOk() {
        alert("readyOk");
        var event = document.createEvent("CustomEvent");
        event.initCustomEvent("SardineJSBridgeReady", true, true, null);
        window.document.dispatchEvent(event);
    }

    var jsCallbackArray = {};
    var jsEventHandlers = {};
    var callIdPre = new Date().getTime() + Math.floor(Math.random() * 10) + '';
    var callSec = 0;

    function getCallbackId() {

        return callIdPre + (++callSec);
    }

    function nativeInvoke(action, params, callback) {
//    alert("nativeInvoke");
        var paramsJsonStr = JSON.stringify(params || {});
        var callId = getCallbackId();
        jsCallbackArray[callId] = callback;
alert(" action:"+action+" paramsJson:"+paramsJsonStr);
        setTimeout(function () {
            JsInvokeNativeMethods.setResult(action, callId, paramsJsonStr);
//            window.webkit.messageHandlers.__nativeApp && window.webkit.messageHandlers.__nativeApp.postMessage({'action': action, 'param': paramsJsonStr, 'callId': callId});
//                   NewBridgeManager && NewBridgeManager.callNative(action, paramsJsonStr, callId);
        }, 0);

    };

    function nativeInvokeCallback(callId, resultJsonStr) {
//      alert("nativeInvokeCallback");
        var result = JSON.parse(resultJsonStr || '{}') || {};
//alert("call result:"+result);
        setTimeout(function () {
            var func = jsCallbackArray[callId];
            delete jsCallbackArray[callId];
            func(result);
        }, 0);

    };



    function nativeEventCallback(eventName, resultJsonStr) {
    alert("nativeEventCallback");
        if (!jsEventHandlers[eventName])
            return;

        var result = JSON.parse(resultJsonStr || '{}') || {};
alert("event result:"+result);
        setTimeout(function () {
            jsEventHandlers[eventName](result);
        }, 0);

    };

    function nativeEventOn(eventName, callback) {
//       alert("nativeEventOn");
        var needOn = !jsEventHandlers[eventName];

        jsEventHandlers[eventName] = callback;

        if (needOn) {
            setTimeout(function () {
//                window.__nativeApp && window.__nativeApp.eventOn(eventName);
            }, 0);
        }

    };

    function nativeEventOff(eventName) {
//        alert("nativeEventOff");
        if (!jsEventHandlers[eventName])
            return;

        delete jsEventHandlers[eventName];

        setTimeout(function () {
//            window.__nativeApp && window.__nativeApp.eventOff(eventName);
        }, 0);

    };

    var bridge = {
        version: '2.0.2',
        _nativeInvokeCallback: nativeInvokeCallback,
        _nativeEventCallback: nativeEventCallback,
        invoke: nativeInvoke,
        on: nativeEventOn,
        off: nativeEventOff
    };

    window.SardineJSBridge = bridge;

    readyOk();
})(window); 
