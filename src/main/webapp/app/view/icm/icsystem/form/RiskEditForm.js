/**
 * 风险预览页面，其中嵌套了风险控制矩阵
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.form.RiskEditForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.riskeditform',
    requires: [
	   'FHD.view.icm.icsystem.form.MeaSureEditForm'	
       ],
    frame: false,
    border : false,
    paramObj : {
       processId : "",
       processRiskId : "",
       measureId : ""
    },
	selectArray : [],
	measureeditform : [],
	autoScroll : true,
	autoHeight : true,
	initParam:function(paramObj){
        var me = this;
    	me.paramObj = paramObj;
	 },
     addComponent: function () {
    	var me = this;
    	//基本信息fieldset
        me.basicinfofieldset = Ext.widget('fieldset', {
            flex:1,
            collapsible: false,
			autoHeight: true,
            autoWidth: true,
            defaults: {
                columnWidth : 1 / 2,
                margin: '7 30 3 30',
                labelWidth: 95
            	},
	            layout: {
	                type: 'column'
	            },
            	title: '风险信息'
	    });
        me.add(me.basicinfofieldset);
        // 流程id 隐藏域
    	me.processId = Ext.widget('textfield', {name: 'processId',hidden : true });
        // 流程id 隐藏域
        me.measureForm = Ext.widget('textfield', {name: 'measureForm',hidden : true });
        // 流程id 隐藏域
        me.measureFormstr = Ext.widget('textfield', {name: 'measureFormstr',hidden : true });
        // 节点Id 隐藏域 
        me.processRiskId = Ext.widget('textfield', {name: 'processRiskId',hidden : true });
        me.basicinfofieldset.add(me.processId,me.processRiskId,me.measureForm,me.measureFormstr);
        me.code = Ext.widget('textfield', {
            name : 'code',
            fieldLabel : '风险编号' + '<font color=red>*</font>',
            value: '',
            allowBlank: false,
            columnWidth: .4
	        });
		     // 初始化方法
	    me.riskCreateCodeBtn = {
             xtype: 'button',
             margin: '7 30 3 3',
             id:'riskCreateCodeBtnId',
             text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
             handler: function(){
       			 FHD.ajax({
	                 url:__ctxPath+'/ProcessPoint/ProcessPoint/ProcessPointCode.f',
	                 params: {
	                 	processId : me.processId,
	                 	processRiskId : me.processRiskId
                 	 },
	                 callback: function (data) {
	                 	 me.getForm().setValues({'code':data.data.code});//给code表单赋值
	                 }
	             });
		     },
		        columnWidth: .1
	    };
        me.basicinfofieldset.add(me.code,me.riskCreateCodeBtn);
		//风险名称
        me.name = Ext.widget('textfield', {
			fieldLabel : '风险名称'+ '<font color=red>*</font>',
			value : '',
			allowBlank: false,
			name : 'name'
        });
        me.basicinfofieldset.add(me.name);
		//隐藏主管责任部门
        me.measureInitOrgId = Ext.widget('textfield', {
			name : 'measureInitOrgId',
			hidden : true
        });
		//隐藏主管责任人
        me.measureInitEmpId = Ext.widget('textfield', {
			name : 'measureInitEmpId',
			hidden : true
        });
        me.basicinfofieldset.add(me.measureInitOrgId,me.measureInitEmpId);
			//风险描述
        me.desc = Ext.widget('textareafield', {
			height:60,
			rows : 3,
			fieldLabel : '风险描述',
			value : '',
			columnWidth: 1,
			name : 'desc'
        });
        me.basicinfofieldset.add(me.desc);
        me.add(me.basicinfofieldset);
    },
    reloadData: function() {
        var me = this;
        me.load({
           	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
           	url: __ctxPath + '/processrisk/loadriskeditformdata.f',
           	params: {
               	processRiskId: me.paramObj.processRiskId,
               	processId : me.paramObj.processId
           	},
           	success: function (form, action) {
               	return true;
           	}
        });
    },
   // 初始化方法
    initComponent: function() {
        var me = this;
        Ext.applyIf(me,{
    	    bbar: {
                items: ['->',	
                {   
            	    text: '添加控制',
                    iconCls: 'icon-database-add',
                    handler: me.addMeaSure
                },
                {   
            	    text: '删除控制',
                    iconCls: 'icon-database-delete',
                    handler: me.delMeaSure
                },{   
            	    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),
                    iconCls: 'icon-operator-home',
                    handler: me.cancel
                },{    
            	    text: FHD.locale.get("fhd.common.save"),
                    iconCls: 'icon-control-stop-blue',
                    handler: me.save
                    }
                ]
           },bodyPadding: "0 3 3 3"
		});
  		me.callParent(arguments);
   		//向form表单中添加控件
   		me.addComponent();
       	},
    addMeaSure : function(){
   	    var me = this.up('riskeditform');
   	    var editform = Ext.widget('measureeditform',{id:'measureeditform'+me.measureeditform.length,processId:me.paramObj.processId, num : me.measureeditform.length,measureInitOrgId : me.measureInitOrgId.value,measureInitEmpId : me.measureInitEmpId.value});
   	    me.measureeditform.push(editform);
   	    me.add(editform);
    },
    delMeaSure : function(){
		var me = this.up('riskeditform');
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {// 确认删除
					var measureIdArray = [];
					for(var i = 0;i<me.selectArray.length;i++){
						Ext.Array.push(measureIdArray,me.measureeditform[me.selectArray[i]].measureId);
						me.remove(me.measureeditform[me.selectArray[i]]);
						Ext.Array.remove(me.measureeditform,me.measureeditform[me.selectArray[i]]);
					}
					FHD.ajax({
	 						url : __ctxPath+ '/processrisk/removemeasurebyids.f',
	 						params : {
	 							measureIds : measureIdArray
	 						},
	 						
	 						callback : function(data) {
	 							if (data.success) { 
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
	                            } else {
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
	                            }
	   						}
						});
					me.selectArray.length = 0;
				}else{
					return false;
				}
			}
    	})
    },
    save: function() {
		var me = this.up('riskeditform');
    	var jsonArray = [];
    	for(var i = 0;i<me.measureeditform.length;i++){
		    //将控制评价点放入隐藏域中var rows =  me.store.data;
		    me.measureeditform[i].noteDepart.renderBlankColor(me.measureeditform[i].noteDepart);
		    me.measureeditform[i].noteradio.renderBlankColor(me.measureeditform[i].noteradio);
		    var rows = me.measureeditform[i].assesspointeditgrid.store.data;
	    	var allRows = me.measureeditform[i].assesspointeditgrid.store.getCount();
	    	if(allRows == 0){
	    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '评价点列表不能为空!');
	    		return false;
	    	}
	    	var assessArray=[];
			Ext.each(rows.items,function(item){
				assessArray.push(item.data);
			});
		    me.measureeditform[i].getForm().setValues({editGridJson : Ext.encode(assessArray)});
	    	jsonArray.push(me.measureeditform[i].getForm().getValues(false,false,true));
	    	//添加控制评价点信息 
	    }
	    var jsonStr = Ext.encode(jsonArray);
	    var riskForm = me.getForm();
	   	riskForm.setValues({//paramObj
	               processId: me.paramObj.processId,
	               processRiskId : me.paramObj.processRiskId,
	               measureFormstr : jsonStr
	               }); //editGridJson
    	if(riskForm.isValid()) {
    		FHD.submit({
				form : riskForm,
				url : __ctxPath + '/processrisk/saveriskmeasure.f',
				callback: function (data) {
					me.cancel();
				}
			});
		}
	},
	cancel : function(){
		me = this.up('riskmeasuremainpanel');
   	  	me.getLayout().setActiveItem(0);
  		me.flowrisklist.reloadData();
   	},
    getInitData : function(){
   	    var me = this;
   		me.measureeditform = [];
   		FHD.ajax({
			url:__ctxPath+'/processrisk/findmeasureidbyriskid.f',
			params: {
				processId: me.paramObj.processId,
				processRiskId : me.paramObj.processRiskId
			},
	     	callback: function (data) {
				me.paramObj.measureId = data.data;
				for(var i = 0;i<me.paramObj.measureId.length;i++){
					me.editform = Ext.widget('measureeditform',{id:'measureeditform'+me.measureeditform.length,processId:me.paramObj.processId,measureId:me.paramObj.measureId[i],num:me.measureeditform.length});
					me.measureeditform.push(me.editform);
					me.editform.initParam({
						measureId : me.paramObj.measureId[i]
					});
					me.measureeditform[i].reloadData();
					me.add(me.measureeditform[i]);
	             }
	         }
         });
	   }
});