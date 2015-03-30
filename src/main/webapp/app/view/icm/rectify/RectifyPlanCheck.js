/*整改方案复核*/
Ext.define('FHD.view.icm.rectify.RectifyPlanCheck', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyplancheck',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding: '0 3 3 3',
	requires: [
		'FHD.view.icm.rectify.form.RectifyView',
		'FHD.view.icm.rectify.form.ImprovePlanViewForm',
        'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoWidth:true,
	collapsed : false,
	autoScroll:true,
	border: false,
	initComponent :function() {
		var me = this;
		me.rectifyview = Ext.widget('rectifyview');
		me.improveplanviewform = Ext.widget('improveplanviewform');
        me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			height:200,
			columnWidth:1
		});
		var select = Ext.create('FHD.ux.dict.DictRadio',{
			margin:'7 10 10 10', 
			fieldLabel:'您确认补充测试通过并提交审批吗',
			labelAlign : 'left',
			labelWidth: 300,
			columns : 4,
			name: 'isPassId',
			columnWidth : 0.6,
			dictTypeId:'workflow_approve_status',
			defaultValue :'workflow_approve_status_agree'
		});
		me.items= [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.方案上报',status:'done'},
	    		{index: 2, context:'2.指定复核人',status:'done'},
	    		{index: 3, context:'3.进度汇报',status:'done'},
	    		{index: 4, context:'4.整改复核',status:'current'},
	    		{index: 5, context:'5.结果审核',status:'undo'}
	    	]
    	}),me.rectifyview, me.improveplanviewform,{
			xtype : 'fieldset',
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : false,
			title : '您可以直接编辑整改结果',
			items:[ 
				{xtype: 'displayfield', fieldLabel: '进度&nbsp;(%)', margin:'7 10 10 0', columnWidth : 0.3, name : 'finishRate'},
				{xtype: 'displayfield', fieldLabel: '填&nbsp;报&nbsp;人&nbsp;', margin:'7 10 10 0', columnWidth : 0.3, name : 'imporveTraceEmpId'},
				{xtype: 'displayfield', fieldLabel: '填报日期',margin:'7 10 10 0', columnWidth : 0.3, name : 'imporveTraceCreateTime'},
		       	{xtype:'textareafield', hideLabel:true, columnWidth : 1, fieldLabel:'整改结果', name:'improveResult', rows:3}
		       	
			]
		},
		{
			xtype : 'fieldset',
			defaults : {
				columnWidth : 1
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : false,
			title : '对于补充测试的情况，您可以在这里填写',
			items:[ 
				{xtype:'textfield', name:'improvePlanRelaDefectId', hidden:true},
		       	{xtype:'textareafield', hideLabel:true, fieldLabel:'复核测试情况', name:'compensationControl', rows:3}
			]
		},
		select,
				Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		  			id:'afterDefectLevelIdId',
	    			name:'afterDefectLevelId',
	    			dictTypeId:'ca_defect_level',
	    			labelAlign:'left',
	    			margin:'7 10 10 0',
	    			fieldLabel : '整改后缺陷等级',
	    			columnWidth : 0.3,
	    			multiSelect:false
	    		}),
				{xtype:'textfield', id:'rectifytextfieldId', hidden:true, margin:'7 10 10 10', fieldLabel:'实际贡献值', columnWidth : 0.3, name:'num'},
			    {xtype:'datefield', id:'rectifydatefieldId', hidden:true, margin:'7 10 10 10', fieldLabel:'下次观察日期', columnWidth : 0.3, format:'Y-m-d', name:'nextCheckDate'},
			    {xtype : 'textareafield', hideLabel:true, margin:'7 10 10 10', fieldLabel : '说明', rows : 3, columnWidth : 1, name : 'checkResult' },
		{
			xtype : 'fieldset',
			layout : {
				type : 'column'
			},
			collapsed : true,
			collapsible : true,
			columnWidth:1,
			collapsible : true,
			title : '审批意见列表',
			items : [me.approvalIdeaGrid]
		}
    ];
	me.bbar={
		items:[
			'->',{//保存，操作后 执行返回功能 
				text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.save'),
				iconCls: 'icon-control-stop-blue',
				handler: function () {
					if(me.getForm().isValid()){
						FHD.submit({
							form: me.getForm(),
							url: __ctxPath+'/icm/rectify/saveRectifyCheck.f',
							callback: function (data){
	                	  
							}
						});
					}
             	} 
			},
			{//提交，操作后 执行返回功能 
				text: FHD.locale.get('fhd.common.submit'),
				iconCls: 'icon-operator-submit',
				id: 'rectify_plan_check_submit_btn',
				handler: function () {
					if(me.getForm().isValid()){
						this.setDisabled(true);
						FHD.submit({
							form: me.getForm(),
							url: __ctxPath+'/icm/rectify/saveRectifyCheck.f',
							callback: function (data){
								FHD.ajax({//ajax调用
			   						url : __ctxPath+ '/icm/rectify/rectifyPlanCheck.f',
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
							}
						});
					}
				} 
			}]
		};
		Ext.applyIf(me,{
			layout:{
        		align: 'stretch',
    			type: 'vbox'
        	},
			items:me.items
		});
		me.callParent(arguments);
		select.on('change',function(t,newValue, oldValue,op){
			if(newValue.isPassId=='workflow_approve_status_agree'){//同意
				//Ext.getCmp('rectifytextfieldId').show();
				Ext.getCmp('rectifydatefieldId').hide();
				Ext.getCmp('afterDefectLevelIdId').show();
				Ext.getCmp('rectify_plan_check_submit_btn').setDisabled(false);

			}else if(newValue.isPassId=='workflow_approve_status_disagree'){//不同意
				Ext.getCmp('rectifytextfieldId').hide();
				Ext.getCmp('rectifydatefieldId').show();
				Ext.getCmp('afterDefectLevelIdId').hide();
				Ext.getCmp('rectify_plan_check_submit_btn').setDisabled(true);
			}
		});
	},
	loadData: function(improveId,executionId){
		var me = this;
		me.improveId = improveId;
		me.executionId = executionId;
		me.reloadData();
	},
	reloadData:function(){
		var me=this;
		
		if(me.improveId && me.executionId){
			me.rectifyview.loadData(me.improveId);
			me.improveplanviewform.loadData(me.improveId,me.executionId);
			me.getForm().load({
				url:__ctxPath + '/icm/improve/findimproveplanCheckbyimproveid.f?improveId='+me.businessId+'&executionId='+me.executionId,
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return false;
				}
			});
		}
	}
});