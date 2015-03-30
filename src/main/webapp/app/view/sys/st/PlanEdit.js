/**
 * 计划任务编辑模版
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.st.PlanEdit', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planEdit',
    
    requires: [
       'FHD.view.sys.st.PlanSelect',
       'FHD.view.sys.st.PlanEditFind'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	var triggerTypeComboBox;//开始任务comboBox
    	var appointDictRadio;//定期模式
    	var triggerDataFieldDate;//触发日期控件
    	var weekCheckboxGroup;//月的星期几
    	var tempComboBox;//选择模版
    	var lookTypeDictRadio;//发送方式

    	var dayByMonthStore;//月的第几天store
    	var severalStore;//第几个store
    	var weekStore;//星期几store
    	var monthStore;//几月store
    	var dayStore;//几天store
    	var tempStore;//选择模版store

    	var planEditPanel;//主panel
    	var formPanel;//form表单panel
    	//var param = Ext.getCmp(me.planManId).formwindow.initialConfig;//父窗口属性
    	var queryUrl = 'sys/st/findPlanById.f';//查询分页
    	var planEdit_saveUrl = 'sys/st/savePlan.f';//保存
    	var queryTempListUrl = 'sys/st/findTempAll.f';//选择模版下拉菜单
    	//var queryEntryByDictTypeIdUrl = 'sys/st/findDictEntryByDictTypeId.f?dictTypeId=st_trigger_type';//开始任务下拉菜单
    	var dayByMonthStoreUrl = 'sys/st/dayByMonthStore.f';//对按天的月数
    	var monthStore = 'sys/st/monthStore.f';//月数
    	var re;
    	var reText = FHD.locale.get('fhd.sys.planEdit.mustNumber');//输入非数字验证提示消息
    	var vHour = 24;//不能超过的小时数
    	var vDay = 31;//不能超过的天数
    	var vWeek = 4;//不能超过的周数
    	var vMonth = 12;//不能超过的月数
    	var vMuch = 20;//任务名称不能超过字符数
    	var vLack = 6;//任务名称不能少于字符数
    	var planSelect = Ext.widget('planSelect',{me : me});
    	
    	tempStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'text'],
    	    proxy: {
    	         type: 'ajax',
    	         url: queryTempListUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});

    	dayByMonthStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'text'],
    	    proxy: {
    	         type: 'ajax',
    	         url: dayByMonthStoreUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});

    	 monthStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'text'],
    	    proxy: {
    	         type: 'ajax',
    	         url: monthStore,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});
    		
    	 dayStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'text'],
    	    proxy: {
    	         type: 'ajax',
    	         url: dayByMonthStoreUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});

    	 severalStore = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'text'],
    	    data : [
    	        {"id":"1", "text":FHD.locale.get('fhd.sys.planEdit.no1')},
    	        {"id":"2", "text":FHD.locale.get('fhd.sys.planEdit.no2')},
    	        {"id":"3", "text":FHD.locale.get('fhd.sys.planEdit.no3')},
    	        {"id":"4", "text":FHD.locale.get('fhd.sys.planEdit.no4')},
    	        {"id":"5", "text":FHD.locale.get('fhd.sys.planEdit.noEnd')}
    	    ],
    	    autoLoad:true
    	});
    		
    	  weekStore = Ext.create('Ext.data.Store', {
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


    		
    	weekCheckboxGroup=Ext.create('Ext.form.CheckboxGroup',{
    	        vertical: true,
    	        items: [
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.monday'), name: 'everyWeeksCheck', inputValue: '1',checked:true},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.tuesday'), name: 'everyWeeksCheck', inputValue: '2'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.wednesday'), name: 'everyWeeksCheck', inputValue: '3'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.thursday'), name: 'everyWeeksCheck', inputValue: '4'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.friday'), name: 'everyWeeksCheck', inputValue: '5'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.saturday'), name: 'everyWeeksCheck', inputValue: '6'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.sunday'), name: 'everyWeeksCheck', inputValue: '7'}
    	        ]
    	});
    	
    	  tempComboBox = Ext.create('Ext.form.field.ComboBox', {
    	    store: tempStore,
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.createBy')+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示  
    	    editable:false,
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	    queryMode: 'local',
    	    name:'createBy',
    	    displayField: 'text',
    	    valueField: 'id',
    	    triggerAction :'all'
    	});

    	triggerTypeComboBox =Ext.create('FHD.ux.dict.DictSelect',{
    		name:'triggerType',
    		dictTypeId:'st_trigger_type',
    		labelAlign:'center',
    		multiSelect:false,
    		fieldLabel: FHD.locale.get('fhd.sys.planMan.triggerType')+'<font color=red>*</font>',
    	    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示  

    	    editable:false,
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	    valueField: 'id',listeners:
                  {
                  select:function(c,r,i){ 
    				if(c.value=='st_trigger_type_time'){
    					planSelect.triggerTypeTimeSelect(dt, triggerDataFieldDate, planSelect, radioGroup1);
    				}else{
    					planSelect.triggerTypeAddSelect(dt, triggerDataFieldDate, planSelect);
    				}
    			 }
                  },
    	    triggerAction :'all'
    	  });
    	  
    	  var radioGroup1=	Ext.create('Ext.form.RadioGroup', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.type')+'<font color=red>*</font>',
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	        vertical: true,
    	        items: [
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.single'), name: 'tx', inputValue: '1',checked:true,id:'single'},
    	            { boxLabel: FHD.locale.get('fhd.sys.planEdit.period'), name: 'tx', inputValue: '2',id:'more'}

    	        ]
    	    ,
    	         listeners: {
    	        click: {
    	            element: 'el',
    	            fn: function(){
               			
    	            	if(radioGroup1.items.items[0].checked){
    	            		planSelect.singleSelect(dt, triggerDataFieldDate, planSelect);
    		            }else{
    		            	planSelect.periodSelect(dt, triggerDataFieldDate);
    	            	}
    	             }
    	        }
    	    }
    	});
    	  
    	appointDictRadio =Ext.create('FHD.ux.dict.DictRadio',{
    		  dictTypeId:'st_cycle_mode',
    		  labelWidth:70,
    		  margin: '3 15 3 15',
    		  fieldLabel:'',
    		  columns:5,
    		  name:'manageTime',
    		  labelAlign : 'center',
    		  listeners: {
    			  click: {
    		          element: 'el',
    		          fn: function(){
    						for(var i = 0; i < appointDictRadio.items.length; i++){
    							if(appointDictRadio.items.items[i].checked){
    								if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_hour'){
    									
    									Ext.getCmp('ms1').show();
    					            	Ext.getCmp('ms2').hide();
    					            	Ext.getCmp('ms3').hide();
    					            	Ext.getCmp('ms4').hide();
    					            	Ext.getCmp('ms5').hide();

    					            	planSelect.hourSelect(re, reText, dt);
    								}else if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_day'){
    									Ext.getCmp('ms1').hide();
    					            	Ext.getCmp('ms2').show();
    					            	Ext.getCmp('ms3').hide();
    					            	Ext.getCmp('ms4').hide();
    					            	Ext.getCmp('ms5').hide();

    					            	Ext.getCmp('everyDayRadio1').checked = true;
    					            	Ext.getCmp('everyDayRadio1').setValue(true);
    					            	
    					            	planSelect.daySelect1(re, reText);
    								}else if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_week'){
    									Ext.getCmp('ms1').hide();
    					            	Ext.getCmp('ms2').hide();
    					            	Ext.getCmp('ms3').show();
    					            	Ext.getCmp('ms4').hide();
    					            	Ext.getCmp('ms5').hide();

    					            	planSelect.weekSelect(re, reText);
    								}else if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_month'){
    									Ext.getCmp('ms1').hide();
    					            	Ext.getCmp('ms2').hide();
    					            	Ext.getCmp('ms3').hide();
    					            	Ext.getCmp('ms4').show();
    					            	Ext.getCmp('ms5').hide();

    					            	Ext.getCmp('monthId1').checked = true;
    					            	Ext.getCmp('monthId1').setValue(true);
    					            	
    					            	planSelect.monthSelect1(re, reText);
    								}else if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_year'){
    									Ext.getCmp('ms1').hide();
    					            	Ext.getCmp('ms2').hide();
    					            	Ext.getCmp('ms3').hide();
    					            	Ext.getCmp('ms4').hide();
    					            	Ext.getCmp('ms5').show();

    					            	Ext.getCmp('yearId1').checked = true;
    					            	Ext.getCmp('yearId1').setValue(true);
    					            	
    					            	planSelect.yearSelect1();
    								}
    							}
    						}
    		          }
    	      	  }
    		  }
    		  
    	  });

		  lookTypeDictRadio =Ext.create('FHD.ux.dict.DictCheckbox',{
			  fieldLabel:FHD.locale.get('fhd.sys.planEdit.lookType')+'<font color=red>*</font>',
			  name:'rb',
			  dictTypeId:'st_deal_measure',
			  labelWidth:70,
			  margin: '3 15 3 15',
			  labelAlign : 'center'
		  });
    			  
    	var nameArea=Ext.create('Ext.form.field.TextArea', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.name')+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	    name:'name',
    	    id:'name',
    	    validator:function(value){
    		    if(value != ''){			    
    			    if(value.length > vMuch){
    				    return FHD.locale.get('fhd.sys.planEdit.name') + FHD.locale.get('fhd.sys.planEdit.length') + 
    				    		FHD.locale.get('fhd.sys.planEdit.much') + vMuch + FHD.locale.get('fhd.sys.planEdit.char');
    				}else if(value.length < vLack){
    					return FHD.locale.get('fhd.sys.planEdit.name') + FHD.locale.get('fhd.sys.planEdit.length') + 
    		    				FHD.locale.get('fhd.sys.planEdit.lack') + vLack + FHD.locale.get('fhd.sys.planEdit.char');
    				}else{
    					return true;
    				}
    			}
    	    }
    	});

    	var dt=Ext.create('Ext.form.field.Time', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.triggerTime')+'<font color=red>*</font>',
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	    editable:false,
    	    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),
    	    name:'triggetTime',
    	    format:'G:i'
    	});

    	
    	triggerDataFieldDate=Ext.create('Ext.form.field.Date', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.triggerData')+'<font color=red>*</font>',
    	    labelWidth:70,
    	    margin: '3 15 3 15',
    	    name:'triggerData',
    	    editable:false,
    	    format:"Y-m-d"
    	});
    /***attribute end***/

    /***function start***/
    	
    	re = /^\d*$/;
    	lookTypeDictRadio.items.items[0].checked = true;
    	lookTypeDictRadio.items.items[0].setValue(true);
    	debugger;
    	//me.planManId.formwindow.body.mask("保存中...","x-mask-loading");
    	formPanel = Ext.create('Ext.form.Panel',{
    		id:'formPanelId',
    		bodyPadding:5,
    		border:false,
    		autoScroll:true,
    		buttons: [{
    			text: FHD.locale.get('fhd.common.save'),
    				handler:function(){
    					var form = formPanel.getForm();
    						if(form.isValid()){
    							me.planManId.formwindow.body.mask("保存中...","x-mask-loading");
    							if(!me.planManId.isAdd){
    								if(planSelect.weekCheckboxGroupCheck(weekCheckboxGroup)){
    									if(planSelect.lookTypeCheck(lookTypeDictRadio)){
    										//更新
    										FHD.submit({
    											form:form,
    											url:planEdit_saveUrl + '?id=' + me.planManId.planId,//param.id,
    											callback:function(data){
    												me.planManId.grid.store.load();
    												me.planManId.formwindow.close();
    											}
    					  					});	
    									}
    								}
    							}else{
    								if(planSelect.weekCheckboxGroupCheck(weekCheckboxGroup)){
    									if(planSelect.lookTypeCheck(lookTypeDictRadio)){
    										//保存
    										FHD.submit({
    											form:form,
    											url:planEdit_saveUrl,
    											callback:function(data){
    												me.planManId.grid.store.load();
    												me.planManId.formwindow.close();
    											}
    					  					});
    									}
    								}
    							}
    							//me.planManId.formwindow.body.unmask();
    					}
    				}
    			},{
    				text: FHD.locale.get('fhd.common.cancel'),
    				handler:function(){
    					if(!me.planManId.isAdd){
    						//更新
    						//findPlanById();
    						me.planManId.formwindow.close();
    					}else{
    						me.planManId.formwindow.close();
    					}
    				}
    		}],
    		
    		items:[
    			{	
    				xtype: 'fieldset',
    				defaults:{columnWidth: 1/1},
        			layout:{type: 'column'},
    				id:'jbxx',
    				title: FHD.locale.get('fhd.sys.planEdit.basic'),
    				items:[
    					nameArea,triggerTypeComboBox,lookTypeDictRadio,tempComboBox
    				]
    			},
    			{
    				xtype: 'fieldset',
    				defaults:{columnWidth: 1/1},
    				layout: {type: 'column'},
    				title: FHD.locale.get('fhd.sys.planEdit.time'),
    				id:'sjxx',
    				items:[
    					radioGroup1,triggerDataFieldDate,dt
    				]
    			},
    			{
    				xtype: 'fieldset',
    				defaults:{columnWidth: 1/1},
    				layout: {type: 'column'},
    				id:'sf1',
    				title: FHD.locale.get('fhd.sys.planEdit.model'),
    				items:[
    					appointDictRadio,
    					{
    						xtype: 'fieldset',
    						defaults:{columnWidth: 1/1},
    						layout: {type: 'column'},
    						id:'ms1',					
    						title: FHD.locale.get('fhd.sys.planEdit.hourSet'),
    						items:[
    							{
    								xtype: 'fieldcontainer',
    								margin: '3 8 3 8',                      
    		                        layout: 'hbox',
    		                        items: [
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.every')},
    		                            {xtype: 'textfield',name:'everyHour',id:'everyHourId',
    			                            validator:function(value){
    				                            if(value != ''){
    				                            	if(value > vHour){
    					                            	return FHD.locale.get('fhd.sys.planEdit.much') + 
    					                            			vHour + FHD.locale.get('fhd.sys.planEdit.hour');
    					                            }else{
    						                            return true;
    						                        }
    				                            }else{
    					                            return true;
    					                        }
    			                            }
    		                           	},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.hour')}
    		                        ]
    							}
    						]
    					},
    					{
    						xtype: 'fieldset',
    						defaults:{columnWidth: 1/1},
    						layout: {type: 'column'},
    						id:'ms2',
    						listeners: {
    					        click: {
    					            element: 'el',
    					            fn: function(){
    									if(Ext.getCmp('everyDayRadio1').checked == true){
    										planSelect.daySelect1(re, reText);
    									}else if(Ext.getCmp('everyDayRadio2').checked == true){
    										planSelect.daySelect2();
    									}
    					        	}
    							}
    					    },
    						title: FHD.locale.get('fhd.sys.planEdit.daySet'),
    						items:[
    							{
    		                        xtype: 'fieldcontainer',
    		                        margin: '3 8 3 8',
    		                        layout: 'hbox',
    		                        items: [
    		                            {xtype: 'radio',checked:true,name:'day',inputValue:'1',id:'everyDayRadio1'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.every')},
    		                            {xtype: 'textfield',name:'everyDay',id:'everyDay',
    			                            validator:function(value){
    			                            if(value != ''){
    			                            	if(value > vDay){
    				                            	return FHD.locale.get('fhd.sys.planEdit.much') + 
    				                            			vDay + FHD.locale.get('fhd.sys.planEdit.day');
    				                            }else{
    					                            return true;
    					                        }
    			                            }else{
    				                            return true;
    				                        }
    		                            }},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.day')}
    		                            
    		                        ]
    							},                    
    		                    {
    		                        xtype: 'fieldcontainer',
    		                        margin: '3 8 3 8',
    		                        layout: 'hbox',
    		                        items: [
    		                           {xtype: 'radio',name:'day',inputValue:'2',id:'everyDayRadio2'},
    		                           {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.everyWork')}
    		                        ]
    		                    }
    						]
    					},
    					{
    						xtype: 'fieldset',
    						defaults:{columnWidth: 1/1},
    						layout: {type: 'column'},
    						id:'ms3',
    						title: FHD.locale.get('fhd.sys.planEdit.weekSet'),
    						items:[
    							{
    								xtype: 'fieldcontainer',                     
    		                        layout: 'hbox',
    		                        margin: '3 8 3 8',
    		                        items: [
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.every')},
    		                            {xtype: 'textfield',name:'everyWeeks',id:'everyWeeksId',
    			                            validator:function(value){
    			                            if(value != ''){
    			                            	if(value > vWeek){
    			                            		return FHD.locale.get('fhd.sys.planEdit.much') + 
    			                            				vWeek + FHD.locale.get('fhd.sys.planEdit.week');
    				                            }else{
    					                            return true;
    					                        }
    			                            }else{
    				                            return true;
    				                        }
    		                            }},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.weekOf')}
    		                        ]
    							},weekCheckboxGroup
    						]
    					},
    					{
    						xtype: 'fieldset',
    						defaults:{columnWidth: 1/1},
    						layout: {type: 'column'},
    						id:'ms4',
    						listeners: {
    					        click: {
    					            element: 'el',
    					            fn: function(){
    									if(Ext.getCmp('monthId1').checked == true){
    										planSelect.monthSelect1(re, reText);
    									}else if(Ext.getCmp('monthId2').checked == true){
    										planSelect.monthSelect2(re, reText);
    									}
    					        	}
    							}
    					    },
    						title: FHD.locale.get('fhd.sys.planEdit.monthSet'),
    						items:[
    							{
    								xtype: 'fieldcontainer',   
    		                        layout: 'hbox',
    		                        margin: '3 8 3 8',
    		                        items: [
    		                            {xtype: 'radio',name:'xx',checked:true,name:'month',inputValue:'1',id:'monthId1'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.every')},
    		                            {xtype: 'textfield',allowBlank:true,name:'everyMonth1',id:'everyMonth1Id',
    			                            validator:function(value){
    			                            if(value != ''){
    			                            	if(value > vMonth){
    			                            		return FHD.locale.get('fhd.sys.planEdit.much') + 
    			                            				vMonth + FHD.locale.get('fhd.sys.planEdit.month');
    				                            }else{
    					                            return true;
    					                        }
    			                            }else{
    				                            return true;
    				                        }
    		                            }},
    		                            {xtype: 'displayfield', value:FHD.locale.get('fhd.sys.planEdit.monthOf')},
    		                            {xtype: 'combo', name:'everyDay2',emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false, store:dayByMonthStore,id:'everyDay2Id'},
    		                            {xtype: 'displayfield',value: FHD.locale.get('fhd.sys.planEdit.day')}
    		                        ]
    							},
    							{
    		                        xtype: 'fieldcontainer',
    		                        layout: 'hbox',
    		                        margin: '3 8 3 8',
    		                        items: [
    		                            {xtype: 'radio',name:'month',inputValue:'2',id:'monthId2'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.every')},
    		                            {xtype: 'textfield', allowBlank: true, name:'everyMonth2',id:'everyMonth2Id',
    			                            validator:function(value){
    			                            if(value != ''){
    			                            	if(value > vMonth){
    			                            		return FHD.locale.get('fhd.sys.planEdit.much') + 
    	                            						vMonth + FHD.locale.get('fhd.sys.planEdit.month');
    				                            }else{
    					                            return true;
    					                        }
    			                            }else{
    				                            return true;
    				                        }
    		                            }},
    		                            {xtype: 'displayfield',value:FHD.locale.get('fhd.sys.planEdit.monthOf')},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'everyNum', store:severalStore,id:'everyNumId'},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'everyWeeksNum', store:weekStore,id:'everyWeeksNumId'}
    		                        ]
    		                    },
    						]
    					},
    					{
    						xtype: 'fieldset',
    						defaults:{columnWidth: 1/1},
    						layout: {type: 'column'},
    						id:'ms5',
    						listeners: {
    					        click: {
    					            element: 'el',
    					            fn: function(){
    									if(Ext.getCmp('yearId1').checked == true){
    										planSelect.yearSelect1();
    									}else if(Ext.getCmp('yearId2').checked == true){
    										planSelect.yearSelect2();
    									}
    					        	}
    							}
    					    },
    						title: FHD.locale.get('fhd.sys.planEdit.yearSet'),
    						items:[
    							{
    		                        xtype: 'fieldcontainer',
    		                        layout: 'hbox',
    		                        margin: '3 8 3 8',
    		                        items: [
    		                            {xtype: 'radio',name:'year',checked:true,inputValue:'1', id:'yearId1'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.everyYear')},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'monthNum', store:monthStore, id:'monthNumId'},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'dayNum', store:dayStore, id:'dayNum'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.daily')}
    		                        ]
    		                    },
    							{
    		                        xtype: 'fieldcontainer',
    		                        layout: 'hbox',
    		                        margin: '3 8 3 8',
    		                        items: [
    		                            {xtype: 'radio',name:'year',inputValue:'2', id:'yearId2'},
    		                            {xtype: 'displayfield', value: FHD.locale.get('fhd.sys.planEdit.everyYear')},
    		                         	{xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'monthNum2', store:monthStore, id:'monthNum2Id'},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'everyNum2', store:severalStore, id:'everyNum2Id'},
    		                            {xtype: 'combo', emptyText:FHD.locale.get('fhd.common.pleaseSelect'),editable:false,name:'everyWeeksNum2', store:weekStore, id:'everyWeeksNum2Id'},
    		                            {xtype:'hidden',hidden:true, name:'isRecycle'},
    		                            {xtype:'hidden',hidden:true, name:'triggerDataSet'}
    		                        ]
    							}
    						]
    					},
    					{
    						xtype: 'fieldset',
    						id:'times',
    						title: '周期范围',
    						items:[
    								{
    								    xtype: 'radiogroup',
    								    margin: '3 15 3 15',
    								    id: 'timeTypeId',
    								    fieldLabel: "",
    								    columns: 2,
    								    items: [
    								        {id:"timeTypeId1", boxLabel: FHD.locale.get('fhd.sys.planEdit.timeScope'), name: "timeType", inputValue: "1" , checked:"true"},
    								        {id:"timeTypeId2", boxLabel: FHD.locale.get('fhd.sys.planEdit.timeForever'), name: "timeType", inputValue: "0" }
    								    ],
    								    listeners: {
    										  click: {
    									          element: 'el',
    									          fn: function(){
    								      				var timeTypeId = Ext.getCmp('timeTypeId');
    													for(var i = 0; i < timeTypeId.items.length; i++){
    														if(timeTypeId.items.items[i].checked){
    															if(timeTypeId.items.items[i].id == 'timeTypeId1'){
    																Ext.getCmp('sf2').show();
    																//开始日期
    												            	Ext.getCmp('startTimeid').allowBlank = false;
    												            	//结束日期
    												            	Ext.getCmp('endTimeid').allowBlank = false;
    															}else if(timeTypeId.items.items[i].id == 'timeTypeId2'){
    																Ext.getCmp('sf2').hide();
    																//开始日期
    												            	Ext.getCmp('startTimeid').allowBlank = true;
    												            	Ext.getCmp('startTimeid').setValue('');
    												            	//结束日期
    												            	Ext.getCmp('endTimeid').allowBlank = true;
    												            	Ext.getCmp('endTimeid').setValue('');
    															}
    														}
    													}
    									          }
    								      	  }
    									  }
    								}
    						]
    					},
    					{
    						xtype: 'fieldset',
    						id:'sf2',
    						title: FHD.locale.get('fhd.sys.planEdit.range'),
    						items:[
    							{
    								xtype:'datefield',
    								name:'startTime',
    								id:'startTimeid',
    								format:"Y-m-d",
    								labelWidth:70,
    								margin: '3 15 3 15',
    								editable:false,
    					        	fieldLabel: FHD.locale.get('fhd.sys.planEdit.startTime') + '<font color=red>*</font>',
    	                            validator:function(value){
    									if(value != ''){
    										var myDate = new Date();
    										var month = myDate.getMonth() + 1;
    						        		var date = myDate.getFullYear() + '' + month + '' + myDate.getDate();
    						        		var starts = Ext.getCmp('startTimeid').rawValue.split('-');
    										var startDate = starts[0] + '' + starts[1] + '' + starts[2];
    										if(Number(startDate) < Number(date)){
    											return FHD.locale.get('fhd.sys.planEdit.startTime') + 
    													FHD.locale.get('fhd.sys.planEdit.lack') + FHD.locale.get('fhd.sys.planEdit.nowTime');
    										}else if(Ext.getCmp('startTimeid').value > Ext.getCmp('endTimeid').value){
    											return FHD.locale.get('fhd.sys.planEdit.startTime') + 
    													FHD.locale.get('fhd.sys.planEdit.much') + FHD.locale.get('fhd.sys.planEdit.entTime');
    			                            }else{
    				                            return true;
    				                        }
    		                            }else{
    			                            return true;
    			                        }
                                	}
    							},
    							{
    								xtype: 'datefield',
    								name:'endTime',
    								id:'endTimeid',
    								format:"Y-m-d",
    								labelWidth:70,
    								margin: '3 15 3 15',
    								editable:false,
    					        	fieldLabel: FHD.locale.get('fhd.sys.planEdit.entTime') + '<font color=red>*</font>',
    					        	validator:function(value){
    		                            if(value != ''){
    		                            	if(Ext.getCmp('endTimeid').value < Ext.getCmp('startTimeid').value){
    			                            	return FHD.locale.get('fhd.sys.planEdit.entTime') + 
    			                            			FHD.locale.get('fhd.sys.planEdit.eqOrLe') + FHD.locale.get('fhd.sys.planEdit.startTime');
    			                            }if(Ext.getCmp('endTimeid').value == Ext.getCmp('startTimeid').value){
    			                            	return FHD.locale.get('fhd.sys.planEdit.entTime') + 
    	                            			FHD.locale.get('fhd.sys.planEdit.eqOrLe') + FHD.locale.get('fhd.sys.planEdit.startTime');
    	                            		}else{
    				                            return true;
    				                        }
    		                            }else{
    			                            return true;
    			                        }
                            		}
    							}
    						]
    					}			                    
    			]
    		}]});
    	
        Ext.apply(me, {
        	//width:formwindow.width - 10,
			//height:formwindow.height - 40,
			layout: {
		        type: 'fit'
		    },
		    items:[formPanel]
        });
        
        Ext.getCmp('sf1').hide();
        Ext.getCmp('sf2').hide();
		Ext.getCmp('sjxx').hide();
        Ext.getCmp('ms1').hide();
        Ext.getCmp('ms3').hide();
        Ext.getCmp('ms4').hide();
        Ext.getCmp('ms5').hide();
        
    	/*formwindow.on('resize',function(me){
    		planEditPanel.setWidth(me.getWidth()-10);
    		planEditPanel.setHeight(me.getHeight()-40);
    	});*/
        
    	triggerTypeComboBox.store.on('beforeload', function (store, options) {
    		if(!me.planManId.isAdd){
    			me.planEditFind = Ext.widget('planEditFind');
    			me.planEditFind.findPlanById(formPanel, 
    					me.planManId.planId, 
    					queryUrl, 
    					triggerTypeComboBox, 
    					triggerDataFieldDate, 
    					appointDictRadio,
    					planSelect,
    					lookTypeDictRadio,
    					tempStore,
    					tempComboBox,
    					dt,
    					re, 
    					reText, 
    					dayByMonthStore,
    					severalStore,
    					weekStore,
    					weekCheckboxGroup,
    					monthStore,
    					dayStore);
           	}
    	});

        me.callParent(arguments);
    }
});