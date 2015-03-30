/**
 * 缺陷反馈页面
 */
Ext.define('FHD.view.icm.assess.form.AssessDefectFeedbackForm',{
	extend: 'Ext.panel.Panel',
    alias: 'widget.assessdefectfeedbackform',
    
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
				text: '提交',
				handler: function () {
					me.subMit();
				}
			}
		];
		me.infoForm=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			parameter:{
				assessPlanId:me.businessId
			},
			margin: '7 0 0 0'
		});
		me.assessDefectFeedbackGrid=Ext.create('FHD.view.icm.assess.component.AssessDefectFeedbackGrid',{
			parameter:{
				assessPlanId:me.businessId,
				assessorId:__user.empId
			},
			defectsIsAvailable:false,
			feedbackIsAvailable:true,
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
			title: '缺陷反馈',
			collapsible: true,
			items :[me.assessDefectFeedbackGrid]
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
	subMit:function(){//提交方法
		var me=this;
		
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/assess/assessDefectFeedback.f',
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