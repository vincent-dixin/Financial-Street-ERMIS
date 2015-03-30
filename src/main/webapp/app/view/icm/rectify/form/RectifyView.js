Ext.define('FHD.view.icm.rectify.form.RectifyView', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyview',
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth: 1
	},
	autoWidth: true,
	collapsed : false,
	autoScroll: false,
	fieldsetCollapsed: true,
	border: false,
	initComponent :function() {
		var me = this;
		me.items = [{//基本信息
				xtype : 'fieldset',
				defaults : {
					labelWidth: 95,
					labelAlign: 'left',
					columnWidth: .5,
					margin:'7 5 5 0'
				},
				layout : {
					type : 'column'
				},
				flex: 1,
				collapsed : false,
				collapsible : false,
				title : '基本信息',
				items : [{xtype : 'textfield', hidden: true, name : 'id'},
					{xtype:'displayfield', fieldLabel:'所属公司', name : 'companyName'},
					{xtype:'displayfield', fieldLabel:'编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号', name:'code'},
					{xtype:'displayfield', fieldLabel : '名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称', name : 'name'},
					Ext.create('Ext.container.Container',{
	             	    layout:{
	             		 type:'column'  
	             	    },
	             	    items:[{xtype:'displayfield', width:95, value:'计划日期:'},
							{name: 'planStartDate', xtype: 'displayfield', format: 'Y-m-d'},
							{xtype:'displayfield', value:'~'},
							{name: 'planEndDate', xtype: 'displayfield', format: 'Y-m-d'}
						]
			         }),
			         {xtype:'displayfield', hidden: true, fieldLabel : '改进原因', name : 'improvementSourceName'},
			         {xtype:'displayfield', fieldLabel : '创&nbsp;建&nbsp;人&nbsp;', name : 'createBy'},
			         {xtype:'displayfield', fieldLabel : '创建日期', format: 'Y-m-d', name : 'createTime'}
				]
			},{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : true,
				collapsible : true,
				autoScroll: false,
				title : '更多信息',
				items:[{
					xtype : 'fieldset',
					layout : {
						type : 'column'
					},
					defaults : {
						columnWidth : 1
					},//每行显示一列，可设置多列
					collapsed : false,
					collapsible : false,
					title : '改进目标',
					items:[
						{xtype:'textareafield', hideLabel:true, readOnly:true, name : 'improvementTarget', hideLabel: true, rows : 3}
					]
		        },{
					xtype : 'fieldset',
					layout : {
						type : 'column'
					},
					defaults : {
						columnWidth : 1
					},//每行显示一列，可设置多列
					collapsed : false,
					collapsible : false,
					title : '具体原因',
					items:[
						{xtype:'textareafield', hideLabel:true, readOnly:true, name : 'reasonDetail', hideLabel: true, rows : 3}
					]
		        }]
			}
	    ];
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
	},
	loadData: function(improveId){
		var me = this;
		me.improveId = improveId;
		me.reloadData();
	},
	reloadData:function(){
		var me=this;
		if(me.improveId){
			me.getForm().load({
	            url:__ctxPath + '/icm/improve/findImproveAdviceview.f?improveId='+me.improveId,
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