/*整改方案预览*/
Ext.define('FHD.view.icm.rectify.form.ImprovePlanViewForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.improveplanviewform',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
    autoWidth:true,
	autoScroll:true,
	fieldsetcollapsed: true,
	border: false,
	initComponent :function() {
		var me = this;
		Ext.applyIf(me,{
			items:[{
					xtype : 'fieldset',
					defaults:{
						margin:'7 5 5 0'
					},
					layout : {
						type : 'column'
					},
					collapsed : false,
					collapsible : true,
					title : '方案信息',
					items:[{xtype: 'textfield', name: 'improvePlanId', hidden: true},
						{xtype: 'textfield', name: 'fileId', hidden: true},
						{xtype: 'displayfield', columnWidth : 0.5, fieldLabel: '责任部门', name: 'orgName'},
						{xtype: 'displayfield', columnWidth : 0.5, fieldLabel: '缺陷描述', name: 'desc'},
						Ext.create('Ext.container.Container',{
							layout:{
								type:'column'  
							},
							columnWidth : 0.5,
							items:[{xtype:'displayfield', width:95, value:'计划日期:'},
								{name: 'planStartDate', xtype: 'displayfield', format: 'Y-m-d'},
								{xtype:'displayfield', value:'~'},
								{name: 'planEndDate', xtype: 'displayfield', format: 'Y-m-d'}
							]
			         	}),
						{xtype: 'displayfield',  columnWidth : 0.5, fieldLabel: '附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件', name : 'file', renderer : function(value,field) { 
						    	return "<a href=\"javascript:void(0);\" onclick=\"downloadFile('"+field.up('form').down('[name=fileId]').value+"')\">"+value+"</a>";  
							}
						},
						{xtype: 'displayfield', columnWidth : 0.5, fieldLabel: '上&nbsp;报&nbsp;人&nbsp;', name: 'planCreateBy'},
						{xtype: 'displayfield', columnWidth : 0.5, fieldLabel: '上报日期', name: 'planCreateTime'},
						{xtype:'textareafield', hideLabel:true, readOnly:true, columnWidth : 1, fieldLabel: '方案内容', name : 'content'}
					]
				}]
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
		if(me.improveId){
			me.getForm().load({
				url:__ctxPath + '/icm/improve/findimproveplanFormbyimproveid.f?improveId='+me.improveId+'&executionId='+me.executionId,
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

