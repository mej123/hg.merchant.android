(function(window) {
	
	window.myTimestamp = (new Date()).valueOf();
	
	
	/*
	FtasMobile/v1/android/00/1.0/Hybrid
	userAgent格式
	i1版本规范:
	标识符/规范版本号/终端类型(ios,android,wp)/终端型号(平板，或尺寸,00表示默认)/框架版本号/ftasUniqueCode/结尾标识符
	*/
	if(!window.hasLoadJs){
		window.hasLoadJs = true;
		var sUserAgent = window.navigator.userAgent;
		//          标识符     规范1  类型2 型号3  框架4 唯一标识5 结尾标识符
		var re = /FtasMobile\/(.*)\/(.*)\/(.*)\/(.*)\/(.*)\/Hybrid/ig
		window.ftasInfoArray = re.exec(sUserAgent);
		if(ftasInfoArray && ftasInfoArray[1] =="v1" ){
			window.ftasDeviceType = ftasInfoArray[2];
			window.ftasUniqueCode = ftasInfoArray[5];
		}else{
			console.log("无法从window.navigator.userAgent获取设备类型。")
			return null;
		}
	}
	
	
	var base64encodechars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	function base64encode(str) {
		if (str === undefined) {
			return str;
		}
	
        var out, i, len;
        var c1, c2, c3;
        len = str.length;
        i = 0;
        out = "";
        while (i < len) {
            c1 = str.charCodeAt(i++) & 0xff;
            if (i == len) {
                out += base64encodechars.charAt(c1 >> 2);
                out += base64encodechars.charAt((c1 & 0x3) << 4);
                out += "==";
                break;
            }
            c2 = str.charCodeAt(i++);
            if (i == len) {
                out += base64encodechars.charAt(c1 >> 2);
                out += base64encodechars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xf0) >> 4));
                out += base64encodechars.charAt((c2 & 0xf) << 2);
                out += "=";
                break;
            }
            c3 = str.charCodeAt(i++);
            out += base64encodechars.charAt(c1 >> 2);
            out += base64encodechars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xf0) >> 4));
            out += base64encodechars.charAt(((c2 & 0xf) << 2) | ((c3 & 0xc0) >> 6));
            out += base64encodechars.charAt(c3 & 0x3f);
        }
        return out;
    }
	
	 var UTF8 = {

		        // public method for url encoding
		        encode: function(string) {
		            string = string.replace(/\r\n/g, "\n");
		            var utftext = "";

		            for (var n = 0; n < string.length; n++) {

		                var c = string.charCodeAt(n);

		                if (c < 128) {
		                    utftext += String.fromCharCode(c);
		                } else if ((c > 127) && (c < 2048)) {
		                    utftext += String.fromCharCode((c >> 6) | 192);
		                    utftext += String.fromCharCode((c & 63) | 128);
		                } else {
		                    utftext += String.fromCharCode((c >> 12) | 224);
		                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
		                    utftext += String.fromCharCode((c & 63) | 128);
		                }

		            }

		            return utftext;
		        },

		        // public method for url decoding
		        decode: function(utftext) {
		            var string = "";
		            var i = 0;
		            var c = c1 = c2 = 0;

		            while (i < utftext.length) {

		                c = utftext.charCodeAt(i);

		                if (c < 128) {
		                    string += String.fromCharCode(c);
		                    i++;
		                } else if ((c > 191) && (c < 224)) {
		                    c2 = utftext.charCodeAt(i + 1);
		                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
		                    i += 2;
		                } else {
		                    c2 = utftext.charCodeAt(i + 1);
		                    c3 = utftext.charCodeAt(i + 2);
		                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
		                    i += 3;
		                }

		            }

		            return string;
		        }
		        
	 };
	
	var _callback_map = {},
	_callback_count = 1000,
	_sendMessageQueue = [],
	_MESSAGE_TYPE = '__msg_type',
	_EVENT_ID = '__event_id',
	_CALLBACK_ID = '__callback_id';
	
	var _handleMessageIdentifier = _handleMessageFromKuKu;
	var _fetchQueueIdentifier = _fetchQueue
	var _callIdentifier = _call;
	
	function readyOk() {

		var event = document.createEvent("CustomEvent");
		event.initCustomEvent("SardineJSBridgeReady", true, true, null);
		window.document.dispatchEvent(event);
	}

	var __KuKuJSBridge = {
		invoke : _call,
		call:_call,
		_handleMessageFromKuKu : _handleMessageFromKuKu,
		_fetchQueue:_fetchQueue,
		_continueSetResult: _continueSetResult
	};

    function _call(action, params, callback) {
        var curFuncIdentifier = __KuKuJSBridge.call;
        if (curFuncIdentifier !== _callIdentifier) {
            return;
        }

        if (!action || typeof action !== 'string') {
            return;
        };

        if (typeof params !== 'object') {
            params = {};
        };

		var callbackID = (_callback_count++).toString();
		if (typeof callback === 'function') {
			_callback_map[callbackID] = callback;
		}

		var msgObj = {
			'action' : action,
			'params' : params
		};

		msgObj[_MESSAGE_TYPE] = 'call';
		msgObj[_CALLBACK_ID] = callbackID;
		_sendMessage(JSON.stringify(msgObj));
    }

    function _sendMessage(message) {
//    	alert(window.myTimestamp + "  message 是：" + message);

        _sendMessageQueue.push(message);
        if(window.FtasPluginManager){
			FtasPluginManager.hasMessage(window.ftasUniqueCode);
        }else{
        	console.log("_sendMessage -- FtasPluginManager does not exist.")
        }
    }

	function _handleMessageFromKuKu(message){
		//console.log("message--->" + message );
		;
		var curFuncIdentifier = __KuKuJSBridge._handleMessageFromKuKu;
        if (curFuncIdentifier !== _handleMessageIdentifier) {
            return '{}';
        }
        var msgWrap = message;
        switch(msgWrap[_MESSAGE_TYPE]){
			case 'callback':
			{
			alert(msgWrap['__params']);
//                alert("__params:"+msgWrap['__params']);
				if(typeof msgWrap[_CALLBACK_ID] === 'string' && typeof _callback_map[msgWrap[_CALLBACK_ID]] === 'function'){
					var params = msgWrap['__params'];
					if(params){
						params = JSON.parse(params);
					}
		            var ret = _callback_map[msgWrap[_CALLBACK_ID]](params);
		            delete _callback_map[msgWrap[_CALLBACK_ID]];
					_setResultValue('SCENE_HANDLEMSGFROMKUKU', JSON.stringify(ret));
		            return JSON.stringify(ret);
		          }
				_setResultValue('SCENE_HANDLEMSGFROMKUKU', JSON.stringify({'__err_code':'cb404'}));
		        return JSON.stringify({'__err_code':'cb404'});
			}
			break;
	        case 'event':
	        {
//                alert("event");
	          if (typeof msgWrap[_EVENT_ID] === 'string' ) {
	        	  if(msgWrap[_EVENT_ID] == "sys:init"){
	        			if (window.SardineJSBridge && window.SardineJSBridge._hasInit) {
	        				console.log('hasInit, no need to init again');
	        			}else if(window.SardineJSBridge){
	        				window.SardineJSBridge._hasInit = true;
	        				readyOk();
	        			}
	        			_setResultValue('SCENE_EVALUATEJAVASCRIPT', "sys:init-success");
	        			return "sys:init-success";
	        	  }
	          }
	        }
	        break;
        }
	}

    //取出队列中的消息，并清空接收队列
    function _fetchQueue() {
        var curFuncIdentifier = __KuKuJSBridge._fetchQueue;
        if (curFuncIdentifier !== _fetchQueueIdentifier) {
            return '';
        }

        var messageQueueString = JSON.stringify(_sendMessageQueue);
//        alert(window.myTimestamp + " _sendMessageQueue 是：" + messageQueueString);

        _sendMessageQueue = [];

		_setResultValue('SCENE_FETCHQUEUE', messageQueueString);
        return messageQueueString;
    };

	var _setResultQueue = [];
	var _setResultQueueRunning = false;

	function _setResultValue(scene, result) {

		if (result === undefined) {
			result = 'dummy';
		}
		_setResultQueue.push(scene + '&' + base64encode(UTF8.encode(result)));
		if (!_setResultQueueRunning) {
			_continueSetResult();
		}
	}

	function _continueSetResult() {

		var result = _setResultQueue.shift();
		if (result === undefined) {
			_setResultQueueRunning = false;
		} else {
			 _setResultQueueRunning = true;
			 FtasPluginManager.setResult(result,window.ftasUniqueCode);
		}
	}


	if(!window.SardineJSBridge){
		window.SardineJSBridge = __KuKuJSBridge;
	}
	
	_setResultValue('SCENE_EVALUATEJAVASCRIPT', "sys:preinit-success");
	return "sys:preinit-success";
})(window);