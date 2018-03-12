//是否不输出控制台日志
window._no_log_console = false;
//是否不弹出对话框日志
window._no_log_alert = false;
//是否在输出控制台日志之后，还输出对话框日志
window._both_console_and_alert = true;
//是否输出控制台的调试信息
window._mobile_console_debug = false;
//是否输出对话框的调试信息
window._mobile_alert_debug = false;

/*mobile基础api，放置于所有js的最前面*/
window.ftaslog = function(msg){
	//没有window._no_log_console标识位时，输出控制台日志
	if(!window._no_log_console && window.console){
		console.log(msg);
		//没有window._both_console_and_alert标识位时，如果已经输出了控制台日志，则不会弹出对话框日志。
		if(!window._both_console_and_alert){
			return;
		}
	}
	//没有window._no_log_alert标识位时，弹出对话框日志
	if(!window._no_log_alert){
		alert(msg);
	}
}

/*mobile基础api，放置于所有js的最前面*/
window.ftasdebug = function(msg){
	//没有window._no_log_console标识位，同时有widow._mobile_console_debug标识位时，输出调试信息
	if(window._mobile_console_debug && !window._no_log_console && window.console){
		console.log(msg);
	}
	//没有window._no_log_alert标识位，同时有window._mobile_alert_debug标识位时，弹出对话框调试信息
	if(window._mobile_alert_debug && !window._no_log_alert){
		alert(msg);
	}
}

/*mobile基础api，放置于所有js的最前面*/
window.deviceType = (function(){
	/*
	FtasMobile/v1/android/00/1.0/Hybrid
	userAgent格式
	i1版本规范:
	标识符/规范版本号/终端类型(ios,android,wp)/终端型号(平板，或尺寸,00表示默认)/框架版本号/结尾标识符
	*/
	var sUserAgent = window.navigator.userAgent;
	//          标识符     规范1  类型2 型号3  框架4 结尾标识符
	var re = /FtasMobile\/(.*)\/(.*)\/(.*)\/(.*)\/Hybrid/ig
	var arrMessages = re.exec(sUserAgent);
	if(arrMessages && arrMessages[1] =="v1" ){
		if(arrMessages[2] == "android"){
			return "a";
		}else if(arrMessages[2] == "ios"){
			return "i"
		}else if(arrMessages[2] == "wp"){
			return "w";
		}else{
			return null;
		}
	}else{
		ftasdebug("无法从window.navigator.userAgent获取设备类型。")
		return null;
	}
})();