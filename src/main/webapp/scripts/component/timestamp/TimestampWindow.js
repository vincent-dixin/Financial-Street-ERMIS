Ext.define('FHD.ux.timestamp.TimestampWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.timestampWindow',
	modal : true,//窗口呈现底部组件变灰不可点击
	constrain:true,//不可拖动到IE外
	height : 600,//窗口高度
	width : 800,//窗口宽度
	layout : {type : 'fit'},//布局样式
	title : FHD.locale.get('fhd.timestampSelector.labelText'),//窗口标题
	buttons : null,//关闭、确定按钮集合组件
	quarter : null,//季度组件
	month : null,//月份组件
	dictpanel : null,//主panel
	choose : null,//年、季度、月份、周选项组件
	bo : null,//首选项组件
	year : null,//年组件
	week : null,//周组件
	yearId : '',//年ID
	quarterId : '',//季度ID
	monthId : '',//月份ID
	weekId : '',//周ID
	eType : '',//日期类型
	count : 0,//计数
	boArray : null,//首集合
	chooseArray : null,//日期类型、年、季度、月份集合
	yearArray : null,//年集合
	quarterArray : null,//季集合
	monthArray : null,//月集合
	weekArray : null,//周集合
	
	//利用AJAX得到日期类型、年、季度、月份参数并给予赋值
	getUrlArray : function(eType, eYear, eQuarter, eMonthId){
		var array = new Array();
		var split = null;
		var me = this;
		
		FHD.ajax({
			async:false,
			url: __ctxPath + '/sys/dic/findTimePeriodByEtype.f?eType=' + eType + '&eYear=' + eYear + 
							'&eQuarter=' + eQuarter + "&eMonthId=" + eMonthId,
            callback: function (data) {
            	Ext.each(data.dictEnties,function(r,i){
		        	array.push({xtype:"button", id:r.id, text:r.text, inputValue:r.id, name:r.name, disabled:r.disabled, scale: 'medium',
		        		handler: function(){
		        			if(this.name == '0frequecy_year'){
		        				me.setYearDiAndEn(me.yearArray, this.id);
		        				me.yearSelect(this.id);
		    		        }else if(this.name == '0frequecy_quarter'){
		    		        	me.setDiAndEn(me.quarterArray, this.id);
		    		        	
		    		        	for(var i = 0; i < me.quarterArray.length; i++){
		    		        		if(me.quarterArray[i].id == this.id){
		    		        			me.yearId = me.quarterArray[i].id.split('Q')[0];
		    		        			me.quarterId = me.quarterArray[i].id.split('Q')[1];
		    		        			
		    		        			for(var j = 0; j < me.chooseArray.length; j++){
		    		        				if(me.chooseArray[j].disabled){
		    		        					if(me.chooseArray[j].id == 'choose1'){
		    		        						//季度
													me.removeMonth();
													me.removeWeek();
		    		        					}else if(me.chooseArray[j].id == 'choose2'){
													//月份
													me.removeMonth();
													me.removeWeek();
													me.dictpanel.add(me.getMonth(me.yearId, me.quarterId));
												}else if(me.chooseArray[j].id == 'choose3'){
													//周
													me.removeMonth();
													me.removeWeek();
													me.dictpanel.add(me.getMonth(me.yearId, me.quarterId));
													me.dictpanel.add(me.getWeek(me.yearId, me.quarterId, me.monthId));
												}
		    		        					break;
		    		        				}
		    		        			}
		    		        			break;
		    		        		}
								}
		    		        	
		    		        }else if(this.name == '0frequecy_month'){
		    		        	me.setDiAndEn(me.monthArray, this.id);
		    		        	
		    		        	for(var i = 0; i < me.monthArray.length; i++){
		    		        		if(me.monthArray[i].id == this.id){
		    		        			me.yearId = me.monthArray[i].id.split('---')[1].substring(0,4);
										me.quarterId = me.monthArray[i].id.split('---')[1].substring(5,6);
										me.monthId = me.monthArray[i].id.split('---')[0];
										
										for(var j = 0; j < me.chooseArray.length; j++){
		    		        				if(me.chooseArray[j].disabled){
		    		        					if(me.chooseArray[j].id != 'choose2'){
													me.removeWeek();
													me.dictpanel.add(me.getWeek(me.yearId, me.quarterId, me.monthId));
												}
												break;
		    		        				}
										}
										break;
		    		        		}
		    		        	}
		    		        }else if(this.name == '0frequecy_week'){
		    		        	me.setDiAndEn(me.weekArray, this.id);
		    		        	
		    		        	for(var i = 0; i < me.weekArray.length; i++){
		    		        		if(me.weekArray[i].id == this.id){
		    		        			me.weekId = me.weekArray[i].id;
		    		        			break;
		    		        		}
		    		        	}
		    		        }
					    }});
		        	
		        	 if(eType == '0frequecy_year'){
		        		 array.push({xtype: 'label',text:' '});
		        	 }
		        	
		        })
		        
		        
		        if(eType == '0frequecy_year'){
		        	me.yearArray = array;
		        }else if(eType == '0frequecy_quarter'){
		        	me.quarterArray = array;
		        }else if(eType == '0frequecy_month'){
		        	me.monthArray = array;
		        }else if(eType == '0frequecy_week'){
		        	me.weekArray = array;
		        }
            }
        });
        
        if(eType == '0frequecy_year'){
        	this.yearId = array[0].id;
        }else if(eType == '0frequecy_quarter'){
        	split = array[0].id.split('Q');
        	this.yearId = split[0];
        	this.quarterId = split[1];
        	this.monthId = array[0].id;
        }else if(eType == '0frequecy_month'){
        	
        	split = array[0].id.split('---');
        	this.yearId = split[1].substring(0,4);
        	this.quarterId = split[1].substring(5,6);
        	this.monthId = split[0];
        }else if(eType == '0frequecy_week'){
        	this.weekId = array[0].id;
        }
        
        return array; 
	},
	
	setDiAndEn : function(arrays, id){
		for(var i = 0; i < arrays.length; i++){
			if(arrays[i].id == id){
				Ext.getCmp(arrays[i].id).disable(); 
				arrays[i].disabled = true;
			}else{
				Ext.getCmp(arrays[i].id).enable();
				arrays[i].disabled = false;
			}
		}
	},
	
	setYearDiAndEn : function(arrays, id){
		for(var i = 0; i < arrays.length; i++){
			if(i != 0){
				i = i + 1;
				if(i >= arrays.length){
					break;
				}
			}
			if(arrays[i].id == id){
				Ext.getCmp(arrays[i].id).disable(); 
				arrays[i].disabled = true;
			}else{
				Ext.getCmp(arrays[i].id).enable();
				arrays[i].disabled = false;
			}
		}
	},
	
	//点击选择首选项事件
	boSelect : function(id){
		var me = this;
		
		if(id == 'bo0'){
			this.choose.hide();
			this.year.hide();
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
		}else if(id == 'bo1'){
			this.choose.show();
			this.year.show();
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
			me.setDiAndEn(me.chooseArray, 'choose0');
			this.eType = '0frequecy_year';
			for(var i = 0; i < me.yearArray.length; i++){
				if(me.yearArray[i].disabled){
					this.yearId = me.yearArray[i].id;
				}
			}
		}
	},
	
	//点击选择年、季度、月、周选项事件
	chooseSelect : function(id){
		var me = this;
		
		this.count++;
		for(var i = 0; i < me.yearArray.length; i++){
			if(me.yearArray[i].disabled){
				this.yearId = me.yearArray[i].id;
				break;
			}
		}
		
		if(id == 'choose0'){
			//年份
			this.year.show();
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
			this.eType = '0frequecy_year';
		}if(id == 'choose1'){
			debugger;
			//季度
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
			this.year.show();
			if(FHD.data.yearId != ''){
				if(this.count > 1){
					this.dictpanel.add(this.getQuarter(this.yearId));
				}else{
					this.dictpanel.add(this.getQuarter(FHD.data.yearId));
				}
			}else{
				this.dictpanel.add(this.getQuarter(this.yearId));	
			}
			this.eType = '0frequecy_quarter';
		}if(id == 'choose2'){
			//月份
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
			this.year.show();
			if(FHD.data.monthId != ''){
				if(this.count > 1){
					this.dictpanel.add(this.getQuarter(this.yearId));
					this.dictpanel.add(this.getMonth(this.yearId, this.quarterId));
				}else{
					this.dictpanel.add(this.getQuarter(FHD.data.yearId));
					this.dictpanel.add(this.getMonth(FHD.data.yearId, FHD.data.quarterId.split('Q')[1]));
				}
			}else{
				this.dictpanel.add(this.getQuarter(this.yearId));
				this.dictpanel.add(this.getMonth(this.yearId, this.quarterId));
			}
			
			this.eType = '0frequecy_month';
		}if(id == 'choose3'){
			
			//周
			this.removeQuarter();
			this.removeMonth();
			this.removeWeek();
			this.year.show();
			if(FHD.data.weekId != ''){
				if(this.count > 1){
					this.dictpanel.add(this.getQuarter(this.yearId));
					this.dictpanel.add(this.getMonth(this.yearId, this.quarterId));
					this.dictpanel.add(this.getWeek(this.yearId, this.quarterId, this.monthId));
				}else{
					
					this.dictpanel.add(this.getQuarter(FHD.data.yearId));
					this.dictpanel.add(this.getMonth(FHD.data.yearId, FHD.data.quarterId.split('Q')[1]));
					this.dictpanel.add(this.getWeek(FHD.data.yearId, FHD.data.quarterId.split('Q')[1], FHD.data.monthId));
				}
			}else{
				this.dictpanel.add(this.getQuarter(this.yearId));
				this.dictpanel.add(this.getMonth(this.yearId, this.quarterId));
				this.dictpanel.add(this.getWeek(this.yearId, this.quarterId, this.monthId));
			}
			
			this.eType = '0frequecy_week';
		}
	},
	
	
	
	
	
	
	
	//点击选择年事件
	yearSelect : function (id){
		
		var me = this;
		for(var i = 0; i < me.yearArray.length; i++){
			if(me.yearArray[i].disabled){
				this.yearId = me.yearArray[i].id;
				this.quarterId = '1';
				break;
			}
		}
		
		for(var i = 0; i < me.chooseArray.length; i++){
			if(me.chooseArray[i].disabled){
				this.chooseSelect(me.chooseArray[i].id);
				break;
			}
		}
	},
	
	//获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
	
	//得到URL格式为:?X=A&Y=B参数结果值
	getResult : function(eType, me){
		
		var result = '';
		FHD.data.isNewValue = false;
		
		for(var i = 0; i < me.boArray.length; i++){
			if(me.boArray[i].disabled){
				if(me.boArray[i].id == 'bo0'){
					this.yearId = this.getYear();
					FHD.data.yearId = this.yearId;
					FHD.data.newValue = '最新的值';
					FHD.data.isNewValue = true;
					this.quarterId = '';
					this.monthId = '';
					this.weekId = '';
					result = this.yearId + ',';
				}else if(me.boArray[i].id == 'bo1'){
					
					this.quarterId = this.yearId + 'Q' + this.quarterId;
					FHD.data.eType = eType;
					if(eType == '0frequecy_year'){
						//年份  
						FHD.data.yearId = this.yearId;
						FHD.data.newValue = this.yearId + '年';
						result = this.yearId;
						this.quarterId = null;
						this.monthId = null;
						this.weekId = null;
					}else if(eType == '0frequecy_quarter'){
						//季度
						FHD.data.yearId = this.yearId;
						
						FHD.data.quarterId = this.quarterId;
						var messageQuarter = this.quarterId.split('Q')[1];
						FHD.data.newValue = this.yearId + '年' + '第' + messageQuarter + '季';
						result = this.yearId + ',' + this.quarterId;
						this.monthId = null;
						this.weekId = null;
					}else if(eType == '0frequecy_month'){
						//月份
						
						FHD.data.yearId = this.yearId;
						FHD.data.quarterId = this.quarterId;
						FHD.data.monthId = this.monthId;
						var messageQuarter = this.quarterId.split('Q')[1];
						var messageMonth = this.monthId.split('mm')[1];
						FHD.data.newValue = this.yearId + '年' + '第' + messageQuarter+ '季' + messageMonth + '月';
						result = this.yearId + ',' + this.quarterId + ',' + this.monthId;
						this.weekId = null;
					}else if(eType == '0frequecy_week'){
						//周
						FHD.data.yearId = this.yearId;
						FHD.data.quarterId = this.quarterId;
						FHD.data.monthId = this.monthId;
						FHD.data.weekId = this.weekId;
						
						var messageQuarter = this.quarterId.split('Q')[1];
						var messageMonth = this.monthId.split('mm')[1];
						var messageWeek = this.weekId.split('w')[1];
						FHD.data.newValue = this.yearId + '年' + '第' + messageQuarter + '季' + messageMonth + '月' + messageWeek + '周';
						result = this.yearId + ',' + this.quarterId + ',' + this.monthId + ',' + this.weekId;
					}
				}
			}
		}
		
		return result;
	},
	
	
	
	//得到首选项元素
	getBoItem : function(){
		var me = this;
		var array = new Array();
		
		for(var i = 0; i < 2; i++){
			if(i == 0){
				array[i] = {id : "bo" + i, text: FHD.locale.get('fhd.timestampWindow.viewNewValueTitle'),
						width:150, scale: 'medium', name: "bo", inputValue: "bo" + i, disabled:true,
						handler: function(){
							me.setDiAndEn(array, this.id);
							me.boSelect(this.id);
					    }};
			}else{
				array[i] = {id : "bo" + i, text: FHD.locale.get('fhd.timestampWindow.viewBoTitle'),
						width:150, scale: 'medium', name: "bo", inputValue: "bo" + i, disabled: false,
						handler: function(){
							me.setDiAndEn(array, this.id);
							me.boSelect(this.id);
					    }};
			}
		}
		me.boArray = array; 
		return array;
	},
	
	//得到年份、季度、月份、周元素
	getChooseItem : function(){
		var me = this;
		var array = new Array();
		
		var boxLaelArray = new Array(FHD.locale.get('fhd.timestampWindow.year'),
				FHD.locale.get('fhd.timestampWindow.quarter'),
				FHD.locale.get('fhd.timestampWindow.month'),
				FHD.locale.get('fhd.timestampWindow.week'));
		
		for(var i = 0; i < 4; i++){
			if(i == 0){
				array[i] = {id : "choose" + i, text: boxLaelArray[i], scale: 'medium', name: "choose", inputValue: "choose" + i , disabled:true,
						handler: function(){
							me.setDiAndEn(array, this.id);
							me.chooseSelect(this.id);
					    }};
			}else{
				array[i] = {id : "choose" + i, text: boxLaelArray[i], scale: 'medium', name: "choose", inputValue: "choose" + i, disabled:false,
						handler: function(){
							me.setDiAndEn(array, this.id);
							me.chooseSelect(this.id);
					    }};
			}
		}
		me.chooseArray = array;
		return array;
	},
	
	aa : function(){
		var array = new Array();
		array[0] = {id : "chooseaaaa", text: '2004', scale: 'medium', name: "chooseaa", inputValue: "choose1ss"  , disabled:true};
		return array;
	},
	
	//得到季度组件
	getQuarter : function(yearId){
		var me = this;
		
		if(me.quarter == null){
			me.quarter = Ext.create('Ext.form.FieldSet', {
				collapsed : false,
				collapsible : false,
				title : FHD.locale.get('fhd.timestampWindow.quarter'),
				items : [
			        {	
			        	buttonAlign : 'left',
			        	border:false,
			        	buttons:me.getUrlArray("0frequecy_quarter", yearId, null, null)
			        }
				]
			});
		}
		
		return me.quarter;
	},
	
	//移除季度组件
	removeQuarter : function (){
		if(this.quarter != null){
			this.dictpanel.remove(this.quarter);
			this.quarter = null;
		}
	},
	
	//得到月份组件
	getMonth : function(yearId, quarterId){
		var me = this;
		
		if(me.month == null){
			me.month = Ext.create('Ext.form.FieldSet', {
				collapsed : false,
				collapsible : false,
				title : FHD.locale.get('fhd.timestampWindow.month'),
				items : [
			        {	
			        	buttonAlign : 'left',
			        	border:false,
			        	buttons:me.getUrlArray("0frequecy_month", yearId, quarterId, null)
			        }
				]
			});
		}
		
		return me.month;
	},
	
	//移除月份组件
	removeMonth : function(){
		if(this.month != null){
			this.dictpanel.remove(this.month);
			this.month = null;
		}
	},
	
	//得到周组件
	getWeek : function(yearId, quarterId, monthId){
		var me = this;
		
		if(me.week == null){
			me.week = Ext.create('Ext.form.FieldSet', {
				collapsed : false,
				collapsible : false,
				title : FHD.locale.get('fhd.timestampWindow.week'),
				items : [
			        {
			        	buttonAlign : 'left',
			        	border:false,
			        	buttons:me.getUrlArray("0frequecy_week", yearId, quarterId, monthId)
			        }
				]
			});
		}
		
		return me.week;
	},
	
	//移除周组件
	removeWeek : function(){
		if(this.week != null){
			this.dictpanel.remove(this.week);
			this.week = null;
		}
	},
	
	gatArray : function(){
		var array = new Array();
		array.push({xtype:"button", text:'333', scale: 'medium'});
		return array;
	},
		
	
	//初始化
	initComponent : function() {
		var me = this;
		var text = '';
		
		me.bo = Ext.create('Ext.form.FieldSet', {
			title : FHD.locale.get('fhd.timestampWindow.choosevViewTitle'),
			items : [
		        {	
		        	buttonAlign : 'left',
				    border:false,
				    buttons: me.getBoItem(),
		        }
			]
		});
		
		me.choose = Ext.create('Ext.form.FieldSet', {
			title : FHD.locale.get('fhd.timestampWindow.BoTitle'),
			items : [
		        {	
				    buttonAlign : 'left',
				    border:false,
				    buttons: me.getChooseItem()
		        }
			]
		});
		
		me.year = Ext.create('Ext.form.FieldSet', {
			title : FHD.locale.get('fhd.timestampWindow.year')
			
//			layout:'table'
//			columns: 5
//			items : [
//		        {	
//		        	buttonAlign : 'left',
//				    border:false,
//		        	buttons:me.getUrlArray("0frequecy_year", null, null, null)
//		        }
//			]
		});
		me.getUrlArray("0frequecy_year", null, null, null);
		debugger;
		me.year.add(me.yearArray);
//		me.year.items.items[0].buttons = me.gatArray();
//		var button = new Array();
//		for(var i = 0; i < me.yearArray.length; i++){
//			button[i].push({buttonAlign : 'left', border:false, buttons:null});
//		}
//		debugger;
//		me.year.items.items[0].buttons.add(me.yearArra);
		
		me.dictpanel = Ext.create('Ext.form.Panel', {
			bodyPadding : 5,
			height : FHD.getCenterPanelHeight(),
			items : [me.bo, me.choose, me.year],
			autoScroll:true
		});

		me.buttons = [ {
			xtype : 'button',
			text : $locale('fhd.common.confirm'),
			handler : function() {
				me.onSubmit(me.getResult(me.eType, me));
				me.close();
			}
		}, {
			xtype : 'button',
			text : $locale('fhd.common.close'),
			style : {
				marginLeft : '10px'
			},
			handler : function() {
				me.close();
			}
		} ];

		Ext.applyIf(me, {
			items : [me.dictpanel]
		});
		
		me.callParent(arguments);
		
		me.choose.hide();
		me.year.hide();
		
		if(!FHD.data.isNewValue){
			
			//选择要查看的业务日历级
			me.setDiAndEn(me.boArray, 'bo1');
			me.boSelect('bo1');
			
			//业务日历级别
			if(FHD.data.eType == '0frequecy_year'){
				me.setDiAndEn(me.chooseArray, 'choose0');
				me.chooseSelect('choose0');
			}else if(FHD.data.eType == '0frequecy_quarter'){
				me.setDiAndEn(me.chooseArray, 'choose1');
				me.chooseSelect('choose1');
			}else if(FHD.data.eType == '0frequecy_month'){
				me.setDiAndEn(me.chooseArray, 'choose2');
				me.chooseSelect('choose2');
			}else if(FHD.data.eType == '0frequecy_week'){
				me.setDiAndEn(me.chooseArray, 'choose3');
				me.chooseSelect('choose3');
			}
			
			
			//年份
			if(FHD.data.yearId != ''){
				me.setYearDiAndEn(me.yearArray, FHD.data.yearId);
			}
			
			//季度
			if(FHD.data.quarterId != ''){
				me.setDiAndEn(me.quarterArray, FHD.data.quarterId);
			}
			
			//月份
			if(FHD.data.monthId != ''){
				var quarterId = FHD.data.quarterId.split('Q')[1];
				var monthId = '';
				for(var i = 0; i < me.monthArray.length; i++){
					monthId = me.monthArray[i].id.split('---')[0];
					if(monthId == FHD.data.monthId){
						Ext.getCmp(me.monthArray[i].id).disable(); 
						me.monthArray[i].disabled = true;
						this.monthId = FHD.data.monthId;
					}else{
						Ext.getCmp(me.monthArray[i].id).enable();
						me.monthArray[i].disabled = false;
					}
				}
			}
			
			//周	
			if(FHD.data.weekId != ''){
				for(var i = 0; i < me.weekArray.length; i++){
					if(me.weekArray[i].id == FHD.data.weekId){
						Ext.getCmp(me.weekArray[i].id).disable(); 
						me.weekArray[i].disabled = true;
						this.weekId = FHD.data.weekId;
					}else{
						Ext.getCmp(me.weekArray[i].id).enable();
						me.weekArray[i].disabled = false;
					}
				}
			}
		}
	}
});