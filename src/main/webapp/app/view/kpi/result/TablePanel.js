/**
 * HTML表格面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.kpi.result.TablePanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.tablePanel',
	
	isNumber : function(oNum){
		if(oNum.indexOf(' ') != -1) return false;
	    if(oNum =='') return true;
	    var pattern = new RegExp('[^d|^-]+(.d+)?$');
	    if(!oNum.match(pattern)) 
	            return false; 
	        try{ 
	        if(parseFloat(oNum)!=oNum) return false; 
	        } 
	        catch(ex) 
	        { 
	            return false; 
	        } 
	    return true; 
	},
	// 初始化方法
	initComponent : function() {
		var me = this;
		me.id = 'tablePanel';
		
		Ext.apply(me, {
			html : me.html,
			border : false,
			autoScroll : true,
			bodyStyle : 'border-bottom: 1px solid #bec0c0 !important;'
		});

		me.callParent(arguments);
	},
	
	//全部修改UI
    inputGatherResult : function(me){
		me.body.mask("Loading...","x-mask-loading"); 
		var jsobj = {};
		jsobj.name = me.resultParam.kpiname;
		FHD.ajax({
            url: __ctxPath + '/kpi/kpi/findkpiinfobyname.f',
            params: {
                param: Ext.JSON.encode(jsobj)
            },
            callback: function (data) {
                if (data && data.success) {
                 	me.resultParam.kpiid = data.kpiid;
                 	me.resultParam.frequenceTemp = data.frequence;
                }
            }
        });
		FHD.ajax({
            url: __ctxPath + '/kpi/kpi/createtable.f?edit=true',
            params: {
            	condItem: Ext.JSON.encode(me.resultParam.paraobj)
            },
            callback: function (data) {
                if (data && data.success) {
                	me.items.items[1].body.update(data.tableHtml);
                	Ext.getCmp('gatherresulttableinputsave').show();
					Ext.getCmp('tableEditId').hide();
					me.body.unmask();
                }
            }
        });
	},
    
    //单点编辑
    oneInput : function(timeId, yearId){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
    	resultCardPanel.resultParam.paraobj.oneEdit = 1;
    	resultCardPanel.resultParam.paraobj.timeId = timeId;
    	resultCardPanel.resultParam.paraobj.yearId = yearId;
    	resultCardPanel.resultParam.paraobj.isNewValue = FHD.data.isNewValue;
    	
    	resultCardPanel.body.mask("Loading...","x-mask-loading"); 
    	FHD.ajax({
            url: __ctxPath + '/kpi/kpi/createtable.f?edit=false',
            params: {
            	condItem: Ext.JSON.encode(resultCardPanel.resultParam.paraobj)
            },
            callback: function (data) {
                if (data && data.success) {
                	resultCardPanel.items.items[1].body.update(data.tableHtml);
                	resultCardPanel.body.unmask();
                }
            }
        });
    	
    	FHD.pram.edit = 1;
    },
    
    //单点保存
    oneSave : function(yearId, kpiId, timeId, realityValue, targetValue, assessValue){
    	var me = this;
    	if(!me.isNumber(realityValue)){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
    		return ;
    	}
    	if(!me.isNumber(targetValue)){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
    		return ;
    	}
    	if(!me.isNumber(assessValue)){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
    		return ;
    	}
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
    	var paraarr = [];
    	var value = {};
    	
    	value[timeId] = realityValue + ',' + targetValue + ',' + assessValue;
    	paraarr.push(value);
    	
    	resultCardPanel.body.mask("Loading...","x-mask-loading"); 
    	FHD.ajax({
            url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiId,
            params: {
            	params: Ext.JSON.encode(paraarr)
            },
            callback: function (data) {
                if (data && data.success) {
                	resultCardPanel.resultParam.paraobj.timeId = '';
                	resultCardPanel.resultParam.paraobj.oneEdit = 1;
		        	FHD.ajax({
	                    url: __ctxPath + '/kpi/kpi/createtable.f?edit=false',
	                    params: {
	                    	condItem: Ext.JSON.encode(resultCardPanel.resultParam.paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	resultCardPanel.items.items[1].body.update(data.tableHtml);
	                        	resultCardPanel.body.unmask();
	                        }
	                    }
	                });
                }
            }
        });
    	FHD.pram.save = 1;
    },
    
    //全部修改保存
    save : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
     	if(resultCardPanel.resultParam.frequenceTemp=="0frequecy_month"){
     		this.saveMonth();
     	}else if(resultCardPanel.resultParam.frequenceTemp=="0frequecy_quarter"){
     		this.saveQuarter();
     	}else if(resultCardPanel.resultParam.frequenceTemp=="0frequecy_year"){
     		this.saveYear();
     	}else if(resultCardPanel.resultParam.frequenceTemp=="0frequecy_week"){
     		this.saveWeek();
     	}else if(resultCardPanel.resultParam.frequenceTemp=="0frequecy_halfyear"){
     		this.saveHalfYear();
     	}
     },
     
    saveYear : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
		var paraarr = [];
 		var realityYear = null;
 		var targetYear = null;
 		var assessYear = null;
 		var me = this;
 		
 		//年
			realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
			targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
			assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
			if(realityYear != null && targetYear != null && assessYear != null){
				if(!me.isNumber(realityYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
		    	 
				var value = {};
				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
				paraarr.push(value);
			}
			
		var resultCardPanel = Ext.getCmp('resultCardPanel');
		
		resultCardPanel.body.mask("Loading...","x-mask-loading");
 		FHD.ajax({
             url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid='+ resultCardPanel.resultParam.kpiid,
             params: {
             	params: Ext.JSON.encode(paraarr)
             },
             callback: function (data) {
            	 debugger;
                 if (data && data.success) {
                 	me.refresh();
                 }
             }
         });
     },
     
     getNumber : function(str){
     	if(parseInt(str) < 10){
     		str = '0' + str;
     	}
     	
     	return str;
     },
     
     saveWeek : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
		var paraarr = [];
			
 		var week = 0;
 		var month = 0;
 		var realityWeek = null;
 		var targetWeek = null;
 		var assessWeek = null;
 		
 		var realityMonth = null;
 		var targetMonth = null;
 		var assessMonth = null;
 		
 		var realityYear = null;
 		var targetYear = null;
 		var assessYear = null;
 		var me = this;
 		
 		//周
			for(var j = 1; j < 55 + 1; j++){
				week = j;
				realityWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'reality');
				targetWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'target');
				assessWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'assess');
 			if(realityWeek != null && targetWeek != null && assessWeek != null){
 				if(!me.isNumber(realityWeek.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetWeek.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessWeek.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'w' + week] = realityWeek.value + ',' + targetWeek.value + ',' + assessWeek.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//月
			for(var j = 1; j < 12 + 1; j++){
				month = j;
				realityMonth = document.getElementById('realityMonthId' + month + 'reality');
				targetMonth = document.getElementById('targetMonthId' + month + 'target');
				assessMonth = document.getElementById('assessMonthId' + month + 'assess');
 			if(realityMonth != null && targetMonth != null && assessMonth != null){
 				if(!me.isNumber(realityMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'mm' + this.getNumber(j)] = realityMonth.value + ',' + targetMonth.value + ',' + assessMonth.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//年
			realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
			targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
			assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
			if(realityYear != null && targetYear != null && assessYear != null){
				if(!me.isNumber(realityYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
				var value = {};
				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
				paraarr.push(value);
			}
 		
			resultCardPanel.body.mask("Loading...","x-mask-loading");
 		FHD.ajax({
             url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid='+ resultCardPanel.resultParam.kpiid,
             params: {
             	params: Ext.JSON.encode(paraarr)
             },
             callback: function (data) {
                 if (data && data.success) {
                 	me.refresh();
                 }
             }
         });
     },
     
     saveMonth : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
		var paraarr = [];
 		var quarter = 0;
 		var month = 0;
 		var realityQuarter = null;
 		var targetQuarter = null;
 		var assessQuarter = null;
 		
 		var realityMonth = null;
 		var targetMonth = null;
 		var assessMonth = null;
 		
 		var realityYear = null;
 		var targetYear = null;
 		var assessYear = null;
 		var me = this;
 		
 		//季度
			for(var j = 1; j < 4 + 1; j++){
				quarter = j;
				realityQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'reality');
				targetQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'target');
				assessQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'assess');
 			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
 				if(!me.isNumber(realityQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//月
			for(var j = 1; j < 12 + 1; j++){
				month = j;
				realityMonth = document.getElementById(FHD.data.yearId + 'mm' + this.getNumber(month) + 'reality');
				targetMonth = document.getElementById(FHD.data.yearId + 'mm' +  this.getNumber(month) + 'target');
				assessMonth = document.getElementById(FHD.data.yearId + 'mm' +  this.getNumber(month) + 'assess');
 			if(realityMonth != null && targetMonth != null && assessMonth != null){
 				if(!me.isNumber(realityMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessMonth.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'mm' + this.getNumber(j)] = realityMonth.value + ',' + targetMonth.value + ',' + assessMonth.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//年
			realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
			targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
			assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
			if(realityYear != null && targetYear != null && assessYear != null){
				if(!me.isNumber(realityYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
				var value = {};
				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
				paraarr.push(value);
			}
			
			resultCardPanel.body.mask("Loading...","x-mask-loading");
 		FHD.ajax({
             url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid=' + resultCardPanel.resultParam.kpiid,
             params: {
             	params: Ext.JSON.encode(paraarr)
             },
             callback: function (data) {
                 if (data && data.success) {
                 	me.refresh();
                 }
             }
         });
     },
     
     saveQuarter : function (){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
		var paraarr = [];
 		var quarter = 0;
 		var month = 0;
 		var realityQuarter = null;
 		var targetQuarter = null;
 		var assessQuarter = null;
 		
 		var realityYear = null;
 		var targetYear = null;
 		var assessYear = null;
 		var me = this;
 		
 		//季度
			for(var j = 1; j < 4 + 1; j++){
				quarter = j;
				realityQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'reality');
				targetQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'target');
				assessQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'assess');
 			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
 				if(!me.isNumber(realityQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessQuarter.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//年
			realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
			targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
			assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
			if(realityYear != null && targetYear != null && assessYear != null){
				if(!me.isNumber(realityYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
				var value = {};
				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
				paraarr.push(value);
			}
			
			resultCardPanel.body.mask("Loading...","x-mask-loading");
 		FHD.ajax({
             url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid='+ resultCardPanel.resultParam.kpiid,
             params: {
             	params: Ext.JSON.encode(paraarr)
             },
             callback: function (data) {
                 if (data && data.success) {
                 	me.refresh();
                 }
             }
         });
     },
     
     saveHalfYear : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
		var paraarr = [];
 		var halfYear = 0;
 		var month = 0;
 		var realityHalfYear = null;
 		var targetHalfYear = null;
 		var assessHalfYear = null;
 		
 		var realityYear = null;
 		var targetYear = null;
 		var assessYear = null;
 		var me = this;
 		
 		//半年
			for(var j = 0; j < 2; j++){
				halfYear = j;
				realityHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'reality');
				targetHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'target');
				assessHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'assess');
 			if(realityHalfYear != null && targetHalfYear != null && assessHalfYear != null){
 				if(!me.isNumber(realityHalfYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetHalfYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessHalfYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	}
 				var value = {};
 				value[FHD.data.yearId + 'hf' + halfYear] = realityHalfYear.value + ',' + targetHalfYear.value + ',' + assessHalfYear.value;
 				paraarr.push(value);
 			}
 		}
 		
 		//年
			realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
			targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
			assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
			if(realityYear != null && targetYear != null && assessYear != null){
				if(!me.isNumber(realityYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(targetYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "目标值请输入数字.");
		    		return ;
		    	}
		    	if(!me.isNumber(assessYear.value)){
		    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "评估值请输入数字.");
		    		return ;
		    	} 
				var value = {};
				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
				paraarr.push(value);
			}
			
			resultCardPanel.body.mask("Loading...","x-mask-loading");
 		FHD.ajax({
             url: __ctxPath + '/kpi/kpi/savekpigatherresultquarter.f?kpiid='+ resultCardPanel.resultParam.kpiid,
             params: {
             	params: Ext.JSON.encode(paraarr)
             },
             callback: function (data) {
                 if (data && data.success) {
                 	me.refresh();
                 }
             }
         });
     },
     
     refresh : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
     	FHD.ajax({
             url: __ctxPath + '/kpi/kpi/createtable.f?edit=false',
             params: {
             	condItem: Ext.JSON.encode(resultCardPanel.resultParam.paraobj)
             },
             callback: function (data) {
            	 debugger
                 if (data && data.success) {
                	resultCardPanel.items.items[1].body.update(data.tableHtml);
                 	Ext.getCmp('gatherresulttableinputsave').hide();
 					Ext.getCmp('tableEditId').show();
 					resultCardPanel.body.unmask();
                 }
             }
         });
     },
     
     //返回
    goback : function(){
    	var resultCardPanel = Ext.getCmp('resultCardPanel');
    	
	 	if("sm" == resultCardPanel.resultParam.type){
	 		this.smGoback();
	 	}else if("kpitype" == resultCardPanel.resultParam.type){
	 		this.kpitypeGoback();
	 	}else if("myfolder" == resultCardPanel.resultParam.type){
	 		this.myfolderGoback();
	 	}else if("myKpi" == resultCardPanel.resultParam.type){
	 		this.myKpiGoback();
	 	}else{
	 		this.categoryGoBack();
	 	}
    },
    //我的指标返回事件
    myKpiGoback:function(){
    	Ext.getCmp('myfoldertab').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('myfoldertab'));
    },
    //我的文件夹返回事件
    myfolderGoback : function(){
    	Ext.getCmp('allmetricmainpanel').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('allmetricmainpanel'));
    },
    
    //指标类型返回事件
    kpitypeGoback : function(){
    	var kpitypemainpanel = Ext.getCmp('kpitypemainpanel');
    	kpitypemainpanel.navigationBar.renderHtml('kpitypecontainer', kpitypemainpanel.paramObj.kpitypeid , '', 'kpi');
    	Ext.getCmp('kpitypetab').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(kpitypemainpanel);
    },
    
    //战略目标返回事件
    smGoback : function(){
    	var strategyobjectivemainpanel = Ext.getCmp('strategyobjectivemainpanel');
    	strategyobjectivemainpanel.navigationBar.renderHtml('smtabcontainer', strategyobjectivemainpanel.paramObj.smid, '', 'sm');
    	Ext.getCmp('strategyobjectivetab').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(strategyobjectivemainpanel);
    },
    
    //记分卡返回
    categoryGoBack : function(){
    	var scorecardmainpanel = Ext.getCmp('scorecardmainpanel');
    	scorecardmainpanel.navigationBar.renderHtml('scorecardtabcontainer', scorecardmainpanel.paramObj.categoryid , '', 'sc');
    	Ext.getCmp('scorecardtab').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(scorecardmainpanel);
    }
});