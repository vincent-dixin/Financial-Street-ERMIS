
FHD =  {
	version : '5.0',
	
	titleJs:{
	},
	
	data:{
		yearId : '',
		quarterId : '',
		monthId : '',
		weekId : '',
		newValue : '',
		kpiName : '',
		currentId : '',
		parentid : '',
		editflag : '',
		parentname : '',
		eType : '',
		edit : false,
		isNewValue : true,
		aa : true
		
	},
	
	pram:{
		edit:0,
        save:0
	},
	
	panel:{
		basicPanel : null,
		funsionChartPanel : null,
		tablePanel : null
	},
	
	pageSize : 20,
	
	alert : function(msg) {
		Ext.Msg.alert('提示',msg);
	},
	getKeyEvent : function(e){
		var o = {};
		o.event = window.event || e;  
		o.key = o.event.keyCode || e.which; 
		o.source = o.event.srcElement || o.event.target;
		return o;
	},
	getCenterPanel : function(){
		
		return Ext.getCmp('center-panel');
	},
	
	getCenterPanelHeight : function(){
		var me = this;
		return me.getCenterPanel().getHeight()-me.getCenterPanel().getTabBar().getHeight()-1;
	},
	
	getCenterPanelWidth : function(){
		var me = this;
		return me.getCenterPanel().getWidth();
	},
	componentResize : function(c,width,height){//c要被设置的对象，width：需要减去的宽度,height:需要减去的高度
		var me = this;
		FHD.getCenterPanel().on('resize',function(t,w,h){
			c.setWidth(w-width); 
			c.setHeight(h-me.getCenterPanel().getTabBar().getHeight()-1-height);
		});
		
		Ext.EventManager.onWindowResize(function(){
			c.setHeight(me.getCenterPanelHeight()-height); 
			c.setWidth(me.getCenterPanelWidth()-width); 
		});
	},
	
	setCookie:function (b, d, a, f, c, e) {
		document.cookie = b + "=" + escape(d)
		+ ((a) ? "; expires=" + a.toGMTString() : "")
		+ ((f) ? "; path=" + f : "") + ((c) ? "; domain=" + c : "")
		+ ((e) ? "; secure" : "");
	},
	
	getCookie:function (b) {
		var d = b + "=";
		var e = document.cookie.indexOf(d);
		if (e == -1) {
			return null;
		}
		var a = document.cookie.indexOf(";", e + d.length);
		if (a == -1) {
			a = document.cookie.length;
		}
		var c = document.cookie.substring(e + d.length, a);
		return unescape(c);
	},
	
	deleteCookie: function(a, c, b) {
		var me =this;
		if (me.getCookie(a)) {
			document.cookie = a + "=" + ((c) ? "; path=" + c : "")
					+ ((b) ? "; domain=" + b : "")
					+ "; expires=Thu, 01-Jan-70 00:00:01 GMT";
		}
	},
	
	ajax:function(config) { // 封装、简化AJAX
		Ext.Ajax.request({
			url : config.url, // 请求地址
			async:config.async!=null?config.async:true,
			params : config.params, // 请求参数
			method : config.mehtod!=null?config.mehtod:'post', // 方法
			callback : function(options, success, response) {
				// 调用回调函数
				if(response.responseText){
					config.callback(Ext.JSON.decode(response.responseText));
				}
			}
		});
		return false;
	},
	submit:function(cfg){
		cfg.form.submit({
			url:cfg.url,
			method : cfg.mehtod!=null?cfg.mehtod:'post', // 方法
			params : cfg.params,
			//waitMsg : FHD.locale.get('fhd.common.saving'),
			success: function(form, action) {
				var data;
				if(action.response && action.response.responseText){
					data = Ext.JSON.decode(action.response.responseText);
				}
				cfg.callback(data);
		       	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
		    },  
		    failure: function(form, action) {  
		    	var data;
				if(action.response && action.response.responseText){
					data = Ext.JSON.decode(action.response.responseText);
				}
				cfg.callback(data);
		        switch (action.failureType) {  
		            case Ext.form.Action.CLIENT_INVALID:  
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.error'),'<font color=red>'+FHD.locale.get('fhd.common.clientInvalid')+'</font>');
		                break;  
		            case Ext.form.Action.CONNECT_FAILURE:  
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.error'),'<font color=red>'+FHD.locale.get('fhd.common.connectFailure')+'</font>');
		                break;  
		            case Ext.form.Action.SERVER_INVALID:  
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.error'),'<font color=red>'+FHD.locale.get('fhd.common.operateFailure')+'</font>');
		       }  
		    }
		});
	},
	/**
	 * 靠右tab页 单击变颜色事件
	 */
	tabRightClick :function(e){
		var me = this;
		Ext.each(me.parentNode.children,function(c){
			var t = me.innerText;
			if($(c).html() === t){
				$(c).attr('class','fhd_tab_right fhd_tab_right_seleted')
			}else{
				$(c).attr('class','fhd_tab_right')
			}
		});
	},
	/**
	 * 导出excel
	 */
	exportExcel:function(grid,exportFileName,sheetName){
		var arr = "[";
		grid.store.each(function(r){
			var o = "{";
			Ext.Array.forEach(grid.columns,function(c,index,array){ 
				if(c.dataIndex != "" && !c.hidden){
					var v = r.get(c.dataIndex);
					if(c.type == "alert"){
						v = getStatusMsg(v);
					}
					o += ("\"" + c.text + "\":\"" + v + "\",");
				}
			});  
			o = o.substring(0,o.length-1);
			o += "}";
			arr += (o + ",");
		});
		arr = arr.substring(0,arr.length-1);
		arr += "]";
		Ext.Ajax.request({  
			method:'post',   
	        url: __ctxPath + "/gridExportXls.f",  
	        params:{
	        	datas:arr
	        },  
	        success:function(req){  
	        	window.location.href = __ctxPath + "/downloadXls.f?exportFileName="+exportFileName+"&sheetName="+sheetName;
	        }  
	    });  
	}
};