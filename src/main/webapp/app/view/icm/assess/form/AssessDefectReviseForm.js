/**
 * 缺陷修改页面
 */
Ext.define('FHD.view.icm.assess.form.AssessDefectReviseForm',{
	extend: 'Ext.panel.Panel',
    alias: 'widget.assessdefectreviseform',
    
    index:1,
    parameter:{},
    autoScroll:true,
    defaults : {
		columnWidth : 1 / 1,
		margin:'7 10 0 30'
	},
	
	initComponent : function() {
	    var me = this;
	    
	    var arr = '<img src="'+__ctxPath+'/images/resultset_next.png">';
		me.bbar=[
			'->',
			{
				text:'提交',
				handler: function () {
					me.save();
				}
			}
		];
		me.infoForm=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			parameter:{
				assessPlanId:me.businessId
			},
			margin: '7 0 0 0'
		});
		me.AssessDefectGrid=Ext.create('FHD.view.icm.assess.component.AssessDefectFeedbackGrid',{
			parameter:{
				assessPlanId:me.businessId
			},
			defectsIsAvailable:true,
			feedbackIsAvailable:false,
			height:FHD.getCenterPanelHeight()/2
		});
		/*
		me.northPanel={
			xtype:'fieldset',
			title: '基本信息',
			collapsible: true,
			collapsed:true,
			items :[me.infoForm]
    	}
    	*/
		me.centerPanel={
			xtype:'fieldset',
			title: '缺陷清单',
			collapsible: true,
			items :[me.AssessDefectGrid]
		}
		me.items=[me.infoForm,me.centerPanel];
		me.callParent(arguments);
	},
	nextStep:function(){
		var me=this;
		if(me.index==1){//当前打开的是编辑页面
			//调用编辑页面保存方法
			me.centerPanel.nextStep();
		}
	},
	setCenterContainer:function(comp){
		var me=this;
		me.remove(centerPanel);
		me.centerPanel=comp;
		me.add(centerPanel);
	},
	save:function(){
		var me = this;
    	var rows = me.AssessDefectGrid.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
				jsonArray.push(item.data);
		});
		url=__ctxPath + '/icm/defect/saveDefects.f',
		FHD.ajax({
			url:url,
            params: {
                    jsonString:Ext.encode(jsonArray),
                    assessorId:__user.empId,
                    assessPlanId:me.businessId
            },
            callback: function (data) {
					me.subMit();
            }
		});			
	},
	subMit:function(){//提交方法
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/assess/assessDefectRevise.f',
			params : {
				businessId:me.businessId,
				executionId:me.executionId
			},
			callback : function(data) {
				if(me.winId){
					Ext.getCmp(me.winId).close();
				}
			}
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});