/*审批并判断是否更新内控手册 */
Ext.define('FHD.view.icm.rectify.RectifyApproveJudge', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyapprovejudge',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding: '0 3 3 3',
	requires: [
		'FHD.view.icm.rectify.form.RectifyView',
		'FHD.view.comm.bpm.ApprovalIdea',
		'FHD.view.icm.rectify.form.ImprovePlanViewForm',
        'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoWidth:true,
	collapsed : false,
	autoScroll:true,
	border: false,
	initComponent: function() {
		var me = this;
		me.rectifyview = Ext.widget('rectifyview');
		me.approvalidea = Ext.widget('approvalidea',{executionId:me.executionId}); 
		me.improveplanviewform = Ext.widget('improveplanviewform');
		me.items= [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.方案上报',status:'done'},
	    		{index: 2, context:'2.指定复核人',status:'done'},
	    		{index: 3, context:'3.进度汇报',status:'done'},
	    		{index: 4, context:'4.整改复核',status:'done'},
	    		{index: 5, context:'5.结果审核',status:'current'}
	    	]
    	}),me.rectifyview, me.improveplanviewform,
    		
    	{
			xtype : 'fieldset',
			defaults : {
				columnWidth : 1
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : true,
			autoScroll: false,
			title : '复核结果',
			items:[{xtype:'displayfield', fieldLabel:'是否通过', name:'isPassName',columnWidth : 1/2},
					{xtype:'displayfield', fieldLabel:'整改后缺陷等级', name:'afterDefectLevelName',columnWidth : 1/2},
					{xtype:'displayfield', fieldLabel:'实际贡献值', hidden:true, name:'num',columnWidth : 1/3},
				    {xtype:'displayfield', fieldLabel:'下次观察日期', hidden:true, format:'Y-m-d', name:'nextCheckDate',columnWidth : 1/3},
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
				title : '整改结果',
				items:[ 
			       	{xtype:'textareafield', hideLabel:true,  readOnly:true, fieldLabel:'整改结果', name:'improveResult', rows:3}
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
				title : '复核测试情况',
				items:[ 
					{xtype:'textfield', name:'improvePlanRelaDefectId', hidden:true},
			       	{xtype:'textareafield', hideLabel:true, readOnly:true, fieldLabel:'复核测试情况', name:'compensationControl'}
				]
			},{
				xtype : 'fieldset',
				layout : {
					type : 'column'
				},
				collapsed : false,
				collapsible : false,
				title : '复核说明',
				items:[  
					
				    {xtype:'textareafield', hideLabel:true, border:false,  readOnly:true, fieldLabel : '复核说明', name : 'checkResult' ,columnWidth : 1}
				]
	        }]
		},me.approvalidea
    ];
	me.bbar={
		items: [
			'->',{//保存，操作后 执行返回功能 
				text: '提交',
				iconCls: 'icon-operator-submit',
				handler: function () {
					this.setDisabled(true);
					FHD.ajax({//ajax调用
   						url : __ctxPath+ '/icm/rectify/rectifyPlanCheckApprove.f',
   					    params : {
   					    	businessId:me.businessId,
   					    	executionId:me.executionId,
   					    	isPass:me.approvalidea.isPass,
							examineApproveIdea:me.approvalidea.getValue()
   						},
   						callback : function(data) {
   							if(me.winId){
   								Ext.getCmp(me.winId).close();
   							}
   						}
   					});
             	} 
			}]
		};
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
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