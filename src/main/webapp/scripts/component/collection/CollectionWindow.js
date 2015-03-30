Ext.define('FHD.ux.collection.CollectionWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.collectionWindow',
	modal : true,//窗口呈现底部组件变灰不可点击
	constrain:true,//不可拖动到IE外
	height : 400,//窗口高度
	width : 800,//窗口宽度
	layout : {type : 'fit'},//布局样式
	title : FHD.locale.get('fhd.collectionWindow.title'),//窗口标题
	buttons : null,//关闭、确定按钮集合组件
	valueText : '',//表现在文本框中文字
	valueCron : '',//CRON格式定时规则
	periodText : '',//每X期间文字
	valueDictType : '',//字典类型
	parentId : '',//父组件ID
	valueRadioType : '',//单选项第几个
	parameters : '',//输入参数
	
	
	getWeek : function(weekStr){
		var week = 0;
		if(weekStr == '星期一'){
			week = 2;
		}else if(weekStr == '星期二'){
			week = 3;
		}else if(weekStr == '星期三'){
			week = 4;
		}else if(weekStr == '星期四'){
			week = 5;
		}else if(weekStr == '星期五'){
			week = 6;
		}else if(weekStr == '星期六'){
			week = 7;
		}else if(weekStr == '星期日'){
			week = 1;
		}
		
		return week;
	},
	
	getWeekStr : function(weekStr){
		var week = '';
		if(weekStr == '星期一'){
			week = 'Monday';
		}else if(weekStr == '星期二'){
			week = 'Tuesday';
		}else if(weekStr == '星期三'){
			week = 'Wednesday';
		}else if(weekStr == '星期四'){
			week = 'Thursday';
		}else if(weekStr == '星期五'){
			week = 'Friday';
		}else if(weekStr == '星期六'){
			week = 'Saturday';
		}else if(weekStr == '星期日'){
			week = 'Sunday';
		}
		
		return week;
	},
	
	getValueText : function(radioType, typeText, periodText, collectionNumberDayIdValue, 
			collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId){
		var text = '';
		if(radioType == "1"){
			//期间首日
			text = typeText + '期间首日' + periodText;
		}else if(radioType == "2"){
			//期间末日
			text = typeText + '期间末日' + periodText;
		}else if(radioType == "3"){
			//期间的第几天
			text = typeText + '期间的第' + collectionNumberDayIdValue + '天' + periodText;
		}else if(radioType == "4"){
			//期间的首个星期几
			text = typeText + '期间的首个' + collectionWeeks1Id + '' + periodText;
		}else if(radioType == "5"){
			//期间的最后一个
			text = typeText + '期间的最后一个' + collectionWeeks2Id + '' + periodText;
		}else if(radioType == "0frequecy_week"){
			//期间的最后一个星期几
			text = typeText + '期间的最后一个' + collectionWeeks3Id + '' + periodText;
		}else if(radioType == "0frequecy_weekday"){
			//每天(星期一至星期五)
			text = typeText + periodText;
		}else if(radioType == "0frequecy_year"){
			//每年日期
			text = typeText + '期间' + collectionDateId + '' + periodText;
		}else if(radioType == '0frequecy_day'){
			text = typeText + periodText;
		}else if(radioType == '0frequecy_relatime'){
			//实时
			text = typeText + periodText;
		}
		
		return text;
	},
	
	getCron : function(id, radioType){
//		periodText = '每' + Ext.getCmp('collectionNumberId').value + '期间';
		periodText = '';
		var typeText = '';
		var cron = '';
		var collectionNumberDayIdValue = Ext.getCmp('collectionNumberDayId').value;
		var collectionWeeks1Id = Ext.getCmp('collectionWeeks1Id').value;
		var collectionWeeks2Id = Ext.getCmp('collectionWeeks2Id').value;
		var collectionWeeks3Id = Ext.getCmp('collectionWeeks3Id').value;
		var collectionDateId = Ext.getCmp('collectionDateId').rawValue;
		
		if(id == '0frequecy_day'){
			//每天
			typeText = '每天';
			valueText = this.getValueText(id, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			cron = '0 0 0 * * ?';
		}else if(id == '0frequecy_halfmonth'){
			//每半个月
			typeText = '每半个月,';
			valueText = this.getValueText(radioType, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			if(radioType == "1"){
				//期间首日
				valueText = typeText + '期间首日' + periodText;
				cron = '0 0 0 1,15 *  ?';
			}else if(radioType == "2"){
				//期间末日
				valueText = typeText + '期间末日' + periodText;
				cron = '0 0 0 15,30 * ?';
			}else if(radioType == "3"){
				//期间的第几天
				var one = Number(1) + Number(collectionNumberDayIdValue);
				var last = Number(15) + Number(collectionNumberDayIdValue);
				valueText = typeText + '期间的第' + collectionNumberDayIdValue + '天' + periodText;
				this.parameters = collectionNumberDayIdValue;
				cron = '0 0 0 ' + one + ',' + last + ' * ?';
			}else if(radioType == "4"){
				//期间的首个星期几
				var week = this.getWeek(collectionWeeks1Id);
				valueText = typeText + '期间的首个' + collectionWeeks1Id + '' + periodText;
				this.parameters = collectionWeeks1Id;
				cron = '0 0 0 ? * ' + week + '#1,' + week + '#3';
			}else if(radioType == "5"){
				//期间的最后一个
				var week = this.getWeek(collectionWeeks2Id);
				this.parameters = collectionWeeks2Id;
				valueText = typeText + '期间的最后一个' + collectionWeeks2Id + '' + periodText;
				cron = '0 0 0 ? * ' + week + '#2,' + week + '#4';
			}
		}else if(id == '0frequecy_halfyear'){
			//每半年
			typeText = '每半年,';
			valueText = this.getValueText(radioType, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			if(radioType == "1"){
				//期间首日
				cron = '0 0 0 1 1,7 ?';
			}else if(radioType == "2"){
				//期间末日
				cron = '0 0 0 L 1,7 ?';
			}else if(radioType == "3"){
				//期间的第几天
				var one = Number(1) + Number(collectionNumberDayIdValue);
				var last = Number(15) + Number(collectionNumberDayIdValue);
				this.parameters = collectionNumberDayIdValue;
				cron = '0 0 0 ' + one + ',' + last + ' 1,7 ?';
			}else if(radioType == "4"){
				//期间的首个星期几
				var week = this.getWeek(collectionWeeks1Id);
				this.parameters = collectionWeeks1Id;
				cron = '0 0 0 ? 1,7 ' + week + '#1';
			}else if(radioType == "5"){
				//期间的最后一个
				var week = this.getWeek(collectionWeeks2Id);
				this.parameters = collectionWeeks2Id;
				cron = '0 0 0 ? 1,7 ' + week + 'L';
			}
		}else if(id == '0frequecy_month'){
			//每月
			typeText = '每月,';
			valueText = this.getValueText(radioType, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			if(radioType == "1"){
				//期间末日
				cron = '0 0 0 1 * ?';
			}else if(radioType == "2"){
				//期间末日
				cron = '0 0 0 L * ?';
			}else if(radioType == "3"){
				//期间的第几天
				var day = Number(collectionNumberDayIdValue);
				this.parameters = collectionNumberDayIdValue;
				cron = '0 0 0 ' + day + ' * ?';
			}else if(radioType == "4"){
				//期间的首个星期几
				var week = this.getWeek(collectionWeeks1Id);
				this.parameters = collectionWeeks1Id;
				cron = '0 0 0 ? * ' + week + '#1';
			}else if(radioType == "5"){
				//期间的最后一个
				var week = this.getWeek(collectionWeeks2Id);
				this.parameters = collectionWeeks2Id;
				cron = '0 0 0 ? * ' + week + 'L';
			}
		}else if(id == '0frequecy_quarter'){
			//每季
			typeText = '每季,';
			valueText = this.getValueText(radioType, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			if(radioType == "1"){
				//期间末日
				cron = '0 0 0 1 1,4,7,10 ?';
			}else if(radioType == "2"){
				//期间末日
				cron = '0 0 0 L 1,4,7,10 ?';
			}else if(radioType == "3"){
				//期间的第几天
				var day = Number(collectionNumberDayIdValue);
				this.parameters = collectionNumberDayIdValue;
				cron = '0 0 0 ' + day + ' 1,4,7,10 ?';
			}else if(radioType == "4"){
				//期间的首个星期几
				var week = this.getWeek(collectionWeeks1Id);
				this.parameters = collectionWeeks1Id;
				cron = '0 0 0 ? 1,4,7,10 ' + week + '#1';
			}else if(radioType == "5"){
				//期间的最后一个
				var week = this.getWeek(collectionWeeks2Id);
				this.parameters = collectionWeeks2Id;
				cron = '0 0 0 ? 1,4,7,10 ' + week + 'L';
			}
		}else if(id == '0frequecy_week'){
			//每周
			typeText = '每周,';
			valueText = this.getValueText(id, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			var week = this.getWeekStr(collectionWeeks3Id);
			this.parameters = collectionWeeks3Id;
			cron = '0 0 0 ? * ' + week;
		}else if(id == '0frequecy_weekday'){
			//每天(星期一至星期五)
			typeText = '每天(星期一至星期五)';
			valueText = this.getValueText(id, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			cron = '0 0 0 ? * MON-FRI';
		}else if(id == '0frequecy_year'){
			//每年
			typeText = '每年,';
			valueText = this.getValueText(id, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			
			if(radioType == "1"){
				//期间首日
				valueText = typeText + '期间首日' + periodText;
				cron = '0 0 0 1 1 ?';
				this.parameters = '1';
			}else if(radioType == "2"){
				//期间末日
				valueText = typeText + '期间末日' + periodText;
				cron = '0 0 0 L 12 ?';
				this.parameters = '2';
			}else{
				this.parameters = collectionDateId;
				var year = collectionDateId.split('-')[0];
				var month = collectionDateId.split('-')[1];
				var day = collectionDateId.split('-')[2];
				cron = '0 0 0 ' + day + ' ' + month + ' ? ' + year;
			}
		}else if(id == '0frequecy_relatime'){
			//实时
			typeText = '实时';
			valueText = this.getValueText(id, typeText, periodText, collectionNumberDayIdValue, 
					collectionWeeks1Id, collectionWeeks2Id, collectionWeeks3Id, collectionDateId);
			
//			if(radioType == "1"){
//				//期间首日
//				valueText = typeText + '期间首日' + periodText;
//				cron = '0 0 0 1 1 ?';
//				this.parameters = '1';
//			}else if(radioType == "2"){
//				//期间末日
//				valueText = typeText + '期间末日' + periodText;
//				cron = '0 0 0 L 12 ?';
//				this.parameters = '2';
//			}else{
//				this.parameters = collectionDateId;
//				var year = collectionDateId.split('-')[0];
//				var month = collectionDateId.split('-')[1];
//				var day = collectionDateId.split('-')[2];
//				cron = '0 0 0 ' + day + ' ' + month + ' ? ' + year;
//			}
		}
		return cron;
	},

	mainSelectClick : function(id, moreRadio, oneRadio, date, isCheck, select, yearRadio){
		
		if(id == '0frequecy_relatime'){
			//实时
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.hide();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
		}else if(id == '0frequecy_day'){
			//每天
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.hide();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
		}else if(id == '0frequecy_halfmonth'){
			//每半个月
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.show();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
			moreRadio.items.items[0].items.items[0].checked = true;
			moreRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == '0frequecy_halfyear'){
			//每半年
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.show();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
			moreRadio.items.items[0].items.items[0].checked = true;
			moreRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == '0frequecy_month'){
			//每月
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.show();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
			moreRadio.items.items[0].items.items[0].checked = true;
			moreRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == '0frequecy_quarter'){
			//每季
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.show();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
			moreRadio.items.items[0].items.items[0].checked = true;
			moreRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == '0frequecy_week'){
			//每周
			Ext.getCmp('collectionWeeks3Id').allowBlank = false;
			oneRadio.show();
			moreRadio.hide();
			date.hide();
			yearRadio.hide();
			oneRadio.items.items[0].items.items[0].checked = true;
			oneRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == '0frequecy_weekday'){
			//每天(星期一至星期五)
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.hide();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
		}else if(id == '0frequecy_year'){
			//每年
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			date.hide();
			moreRadio.hide();
			oneRadio.hide();
			yearRadio.show();
			yearRadio.items.items[0].items.items[0].checked = true;
			yearRadio.items.items[0].items.items[0].setValue(true);
		}else if(id == ''){
			//每月
			Ext.getCmp('collectionWeeks3Id').allowBlank = true;
			moreRadio.show();
			oneRadio.hide();
			date.hide();
			yearRadio.hide();
			id = '0frequecy_month';
			moreRadio.items.items[0].items.items[0].checked = true;
			moreRadio.items.items[0].items.items[0].setValue(true);
		}
		
		if(isCheck){
			for(var i = 0; i < select.items.length; i++){
				if(select.items.items[i].inputValue == id){
					select.items.items[i].checked = true;
					select.items.items[i].setValue(true);
					break;
				}
			}
		}
	},
	
	getEditRadioType : function(valueRadioType, moreRadio, oneRadio, date, weekStore, select, valueDictType, yearRadio){
		if(valueRadioType != ''){
			var collectionNumberDayIdValue = Ext.getCmp('collectionNumberDayId');
			var collectionWeeks1Id = Ext.getCmp('collectionWeeks1Id');
			var collectionWeeks2Id = Ext.getCmp('collectionWeeks2Id');
			var collectionWeeks3Id = Ext.getCmp('collectionWeeks3Id');
			var collectionDateId = Ext.getCmp('collectionDateId');
			var par = '';
			if(valueRadioType.split(',') != null){
				if(valueRadioType.split(',').length > 2){
					var type = '7';
					var pars = valueRadioType.split(',')[2];
					valueRadioType = type;
					par = pars;
				}else{
					var type = valueRadioType.split(',')[0];
					var pars = valueRadioType.split(',')[1];
					valueRadioType = type;
					par = pars;
				}
			}
			for(var i = 0; i < moreRadio.items.length; i++){
				if(moreRadio.items.items[i].items.items[0].inputValue == valueRadioType){
					moreRadio.items.items[i].items.items[0].checked = true;
					moreRadio.items.items[i].items.items[0].setValue(true);
					break;
				}
			}
			
			if(valueRadioType == "3"){
				//期间的第几天
				collectionNumberDayIdValue.setValue(par);
			}else if(valueRadioType == "4" || valueRadioType == "5" || valueRadioType == "6"){
				//期间的首个星期几
				for(var i = 0; i < weekStore.totalCount; i++){
		        	var record = weekStore.getAt(i);
		        	if(record.get('text') == par){
		        		if(valueRadioType == "4"){
		        			collectionWeeks1Id.setValue(weekStore.getAt(i).get('text'));
		        		}else if(valueRadioType == "5"){
		        			collectionWeeks2Id.setValue(weekStore.getAt(i).get('text'));
		        		}else if(valueRadioType == "6"){
		        			collectionWeeks3Id.setValue(weekStore.getAt(i).get('text'));
		        		}
		        		break;
			        }
			    }
			}else if(valueRadioType == '7'){
				//collectionDateId.setValue(par);
				if(pars =='1'){
					yearRadio.items.items[0].items.items[0].checked = true;
					yearRadio.items.items[0].items.items[0].setValue(true);
				}else if(pars == '2'){
					yearRadio.items.items[1].items.items[0].checked = true;
					yearRadio.items.items[1].items.items[0].setValue(true);
				}
			}
			
			for(var i = 0; i < select.items.length; i++){
				if(select.items.items[i].inputValue == valueDictType){
					select.items.items[i].checked = true;
					select.items.items[i].setValue(true);
					break;
				}
			}
		}
	},
	
	getEdit : function(parentId, select, moreRadio, oneRadio, date, weekStore, yearRadio) {
		if(this.valueDictType != ''){
			if(parentId == ''){
				this.mainSelectClick(this.valueDictType, moreRadio, oneRadio, date, true, select, yearRadio);
				this.getEditRadioType(this.valueRadioType, moreRadio, oneRadio, date, weekStore, select, this.valueDictType, yearRadio);
				return;
			}
		}if(parentId != ''){
			var dictType = Ext.getCmp(parentId).valueDictType;
			var items2 = null;
			var i = 0;
			while(true){
				if(select.items.items[i].inputValue != dictType){
					select.remove(i);
				}else{
					i++;
				}
				if(select.items.length == 1){
					break;
				}
			}
			
			this.mainSelectClick(select.items.items[0].inputValue, moreRadio, oneRadio, date, false, null, yearRadio);
			this.getEditRadioType(this.valueRadioType, moreRadio, oneRadio, date, weekStore, select, this.valueDictType, yearRadio);
			select.items.items[0].checked = true;
			select.items.items[0].setValue(true);
		}else{
			this.mainSelectClick(this.valueDictType, moreRadio, oneRadio, date, true, select, yearRadio);
			//moreRadio.hide();
			//oneRadio.hide();
			//date.hide();
		}
	},
	
	getUrlAjax : function(type){
		Ext.Ajax.request({
		    url: __ctxPath + '/sys/dic/findDictEntryByTypeId.f?typeId=' + type,
		    async :  false,
		    success: function(response){
		        var text = response.responseText;
		        Ext.each(Ext.JSON.decode(text).dictEnties,function(r,i){
	        		items.push({id:r.id,xtype:'radiofield',boxLabel:r.name,inputValue:r.id,name:me.name});
		        });
		    }
		});
	},
	
	initComponent : function() {
		var me = this;
		var text = '';
		var select = Ext.create('FHD.ux.dict.DictRadio',{
			  dictTypeId:'0frequecy',
			  fieldLabel : FHD.locale.get('fhd.collectionWindow.rate'),
			  columns : 4,
			  name:'mainSelect',
			  labelAlign : 'center',
			  listeners: {
				  click: {
			          element: 'el',
			          fn: function(){
							for(var i = 0; i < select.items.length; i++){
								if(select.items.items[i].checked){
									me.mainSelectClick(select.items.items[i].inputValue, moreRadio, oneRadio, date, false, null, yearRadio);
								}
							}
			          }
		      	  }
			  }
		}); 
		
		var weekStore = Ext.create('Ext.data.Store', {
		    fields: ['id', 'text'],
		    data : [
		        {"id":"1", "text":FHD.locale.get('fhd.sys.planEdit.monday')},
		        {"id":"2", "text":FHD.locale.get('fhd.sys.planEdit.tuesday')},
		        {"id":"3", "text":FHD.locale.get('fhd.sys.planEdit.wednesday')},
		        {"id":"4", "text":FHD.locale.get('fhd.sys.planEdit.thursday')},
		        {"id":"5", "text":FHD.locale.get('fhd.sys.planEdit.friday')},
		        {"id":"6", "text":FHD.locale.get('fhd.sys.planEdit.saturday')},
		        {"id":"7", "text":FHD.locale.get('fhd.sys.planEdit.sunday')}
		       
		    ],
		    autoLoad:true
		});
		
		var yearRadio = Ext.create('Ext.form.FieldSet', {
			collapsed : false,
			collapsible : false,
			title : '',
			items : [
			        {
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'1', name:'radioType' },
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.one')}
		                ]
					},
					{
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'2', name:'radioType'},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.last')}
		                ]
					}
			]
		});
		
		var moreRadio = Ext.create('Ext.form.FieldSet', {
			collapsed : false,
			collapsible : false,
			title : '',
			items : [
			        {
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'1', name:'radioType'},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.one')}
		                ]
					},
					{
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'2', name:'radioType'},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.last')}
		                ]
					},
					{
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'3', name:'radioType',
		                    	 listeners: {
		    					        click: {
		    					            element: 'el',
		    					            fn: function(){
		    									Ext.getCmp('collectionNumberDayId').allowBlank = false;
		    									Ext.getCmp('collectionWeeks1Id').allowBlank = true;
		    									Ext.getCmp('collectionWeeks2Id').allowBlank = true;
		    									Ext.getCmp('collectionWeeks3Id').allowBlank = true;
		    					        	}
		    							}
		    					    }},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.no') + 
		                    	 '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp'},
		                     {xtype: 'textfield', name:'collectionNumberDay', id:'collectionNumberDayId'
		                            /*,validator:function(value){
		                            }*/
		                     },
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.day')}
		                ]
					},
					{
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'4', name:'radioType',
		                    	 listeners: {
		    					        click: {
		    					            element: 'el',
		    					            fn: function(){
		    									Ext.getCmp('collectionWeeks1Id').allowBlank = false;
		    									Ext.getCmp('collectionNumberDayId').allowBlank = true;
		    									Ext.getCmp('collectionWeeks2Id').allowBlank = true;
		    									Ext.getCmp('collectionWeeks3Id').allowBlank = true;
		    					        	}
		    							}
		    					    }},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.ones') + '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp'},
		                     {xtype: 'combo', name:'collectionWeeks1', store:weekStore, id:'collectionWeeks1Id'}
		                ]
					},
					{
						xtype: 'fieldcontainer',
		                layout: 'hbox',
		                items: [
		                     {xtype: 'radio', inputValue:'5', name:'radioType',
		                    	 listeners: {
		    					        click: {
		    					            element: 'el',
		    					            fn: function(){
		    					            	Ext.getCmp('collectionWeeks2Id').allowBlank = false;
		    									Ext.getCmp('collectionWeeks1Id').allowBlank = true;
		    									Ext.getCmp('collectionNumberDayId').allowBlank = true;
		    									Ext.getCmp('collectionWeeks3Id').allowBlank = true;
		    					        	}
		    							}
		    					    }},
		                     {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.lasts')},
		                     {xtype: 'combo', name:'collectionWeeks2', store:weekStore, id:'collectionWeeks2Id'}
		                ]
					}
			]
		});
		
		var oneRadio = Ext.create('Ext.form.FieldSet', {
			collapsed : false,
			collapsible : false,
			title : FHD.locale.get('fhd.collectionWindow.info'),
			items : [
					{
						xtype: 'fieldcontainer',
					    layout: 'hbox',
					    items: [
					         {xtype: 'radio', checked:true, inputValue:'6', name:'radioType',
					        	 listeners: {
		    					        click: {
		    					            element: 'el',
		    					            fn: function(){
		    					            	Ext.getCmp('collectionWeeks3Id').allowBlank = false;
		    					            	Ext.getCmp('collectionWeeks2Id').allowBlank = true;
		    									Ext.getCmp('collectionWeeks1Id').allowBlank = true;
		    									Ext.getCmp('collectionNumberDayId').allowBlank = true;
		    					        	}
		    							}
		    					    }},
					         {xtype : 'displayfield', value : FHD.locale.get('fhd.collectionWindow.lasts')},
					         {xtype: 'combo', name:'collectionWeeks3', store:weekStore, id:'collectionWeeks3Id'}
					    ]
					}
			]
		});
		
		
		var date = Ext.create('Ext.form.FieldSet', {
			collapsed : false,
			collapsible : false,
			title : FHD.locale.get('fhd.collectionWindow.info'),
			items : [
					{
						xtype:'datefield',
						id : 'collectionDateId',
						format:"Y-m-d",
						labelWidth:70,
						margin: '5 10 5 5',
						fieldLabel: FHD.locale.get('fhd.collectionWindow.date')
					}
			]
		});
		
		var dictpanel = Ext.create('Ext.form.Panel', {
			bodyPadding : 5,
			height : FHD.getCenterPanelHeight(),
			items : [ {
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1 / 1,
					margin : '5 10 5 10'
				},
				layout : {
					type : 'column'
				},
				bodyPadding : 5,
				collapsed : false,
				collapsible : false,
				title : FHD.locale.get('fhd.collectionWindow.module'),
				items : [ select, 
				          {
							xtype: 'fieldcontainer',
		                    layout: 'hbox',
		                    items: [
		                        {xtype: 'displayfield', value: FHD.locale.get('fhd.collectionWindow.every')},
//		                        {xtype: 'textfield',name:'collectionNumber',id:'collectionNumberId',value:'1',disabled:true
//		                            /*,validator:function(value){
//		                            }*/
//		                       	},
		                        {xtype: 'displayfield', value: FHD.locale.get('fhd.collectionWindow.period')}
		                    ]
						}, moreRadio, oneRadio, date, yearRadio
				]
			} ]
		});
		
		me.buttons = [ {
			xtype : 'button',
			text : $locale('fhd.common.confirm'),
			handler : function() {
				var form = dictpanel.getForm();
				if(form.isValid()){
					me.valueCron = me.getCron(dictpanel.getValues().mainSelect, dictpanel.getValues().radioType);
					me.valueDictType = dictpanel.getValues().mainSelect;
					if(me.valueDictType == '0frequecy_year'){
						me.valueRadioType = '7' + ',' + me.parameters;
					}else{
						me.valueRadioType = dictpanel.getValues().radioType + ',' + me.parameters;
					}
					me.onSubmit(valueText + '---' + me.valueCron + '---' + me.valueDictType + '---' + me.valueRadioType);
					me.close();
				}
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
			items : [ dictpanel ]
		});
		
		me.callParent(arguments);
		
		me.getEdit(me.parentId, select, moreRadio, oneRadio, date, weekStore, yearRadio);
	}
});