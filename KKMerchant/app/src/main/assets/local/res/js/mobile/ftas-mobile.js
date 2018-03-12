/*!
 * FtasMobile JavaScript Library v1.0 for zepto
 * http://www.huiguan.com/
 * author:tik
 * Copyright 2016, FTAS
 */
;(function(global, factory) {
  if (typeof define === 'function' && define.amd)
    define(function() { return factory(global) })
  else
    factory(global)
}(this, function(window) {
	/*基础功能*/
	var FtasMobile = (function() {
		var FM = {};
		
		FM.isAndroid = function(){
    		return window.deviceType == 'a';
		}
		
    	FM.isIOS = function(){
    		return window.deviceType == 'i';
    	}
    	
    	FM.isWP = function(){
    		return window.deviceType == 'w';
    	}
    	
    	FM.isMobile = function(){
			return !!window.deviceType;
    	}
    	
		FM.call = function(telNum,autoCall){
			return this.execute("call",[telNum,autoCall]);
		}
		
		FM.nextPageWithUrl = function(url,isAnimation){
			return this.execute("nextPageWithUrl",[url,isAnimation]);
		}
		
		FM.back = function(){
			return this.execute("back",[]);
		}
		
		return FM;
	})();
	
	/*对象导出*/
	window.FtasMobile = FtasMobile;
	window.FM === undefined && (window.FM = FtasMobile);
	
	/*页面交互*/
	;(function(FM) {
		FM.openPage = function(){
			
		}
	})(FtasMobile)
	
	/*缓存*/
	;(function(FM) {
		
		FM.getMemoryCache = function(key,defValue){
			return this.execute("getMemoryCache",[key,defValue]);
		}

		FM.setMemoryCache = function(key,value){
			return this.execute("setMemoryCache",[key,value]);
		}
		
		FM.removeMemoryCache = function(key){
			return this.execute("removeMemoryCache",[key]);
		}
		
		FM.clearMemoryCache = function(){
			return this.execute("clearMemoryCache",[]);
		}
		
	})(FtasMobile)
	
	/*js交互*/
	;(function(FM) {
		FM.execute = function(action,params,callback,error){
			params = JSON.stringify(params);
			if(this.isAndroid()){
				return this.androidExecute(action, params, callback, error);
			}else if(this.isIOS()){
				return this.iosExecute(action, params, callback, error);
			}else if(this.isWP()){
				return this.winphoneExecute(action, params, callback, error);
			}else{
				ftaslog(action + "\t未知的终端类型");
				return;
			}
		}

		FM.androidExecute = function(action,params,callback,error){
			ftasdebug("-->action:"+action+" params:"+params);
			return FtasPluginManager.exec(action,params,"");
		}
	})(FtasMobile)

}));