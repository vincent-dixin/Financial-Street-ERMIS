/**
 * 
 * 风险整理表单
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyEdit', {
	extend : 'Ext.form.Panel',
	alias : 'widget.riskTidyEdit',

	requires : [ 'FHD.view.risk.assess.utils.AssessFChart' ],

	load : function(id) {
		var me = this;
		// alert(id);
		// alert(id);
		// me.form.load({
		// url:url,
		// params:{id:id},
		// failure:function(form,action) {
		// alert("err 155");
		// },
		// success:function(form,action){
		//       		        
		// }
		// });
	},

	// 初始化方法
	initComponent : function() {
		var me = this;
		
		var label1 = Ext.widget('textareafield', {
			xtype : 'displayfield',
			rows : 2,
			fieldLabel : '上级风险',
			margin : '0 0 3 0',
			name : 'kpiLevel',
			columnWidth : .5
		});

		var label2 = Ext.widget('textareafield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '风险编号',
			margin : '0 0 3 20',
			name : 'name',
			columnWidth : .5
		});
		
		var label44 = Ext.widget('hiddenfield', {
			xtype : 'hiddenfield',
			rows : 2,
			fieldLabel : '',
			margin : '0 0 3 0',
			columnWidth : 1
		});

		var label5 = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任部门',
			type : 'dept',
			multiSelect : true,
			margin : '0 0 3 0',
			columnWidth : .5
		});

		var label6 = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '相关部门',
			type : 'dept',
			multiSelect : true,
			margin : '0 0 3 20',
			columnWidth : .5
		});
		
		var label7 = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '修改意见',
			value : '不影响此目标建议排除',
			margin : '0 0 3 0',
			name : 'name',
			columnWidth : .5
		});
		
		var label91 = Ext.widget('kpioptselector', {
			labelWidth : 100,
			gridHeight : 55,
			btnHeight : 55,
			multiSelect : true,
			labelText : '影响指标',
			margin : '0 0 3 0',
			name : 'influProcessureName',
			columnWidth : .5
		});
		
		var label9 = Ext.widget('kpioptselector', {
			labelWidth : 100,
			gridHeight : 55,
			btnHeight : 55,
			multiSelect : true,
			labelText : '影响流程',
			margin : '0 0 3 20',
			name : 'influProcessureName',
			columnWidth : .5
		});
		
		var fieldSet = {
			xtype : 'fieldset',
			title : '对供应商交付能力预测不足',
			collapsible : true,
			width : 700,
//			height : 230,
			height : 220,
			defaultType : 'textfield',
			margin : '5 5 0 5',
			layout : {
				type : 'column'
			},
			items : [ label1, label2, label44, label5, label6, label91, label9, label7]
		};

		me.assessFChart = Ext.widget('assessFChart');

		var fieldSet2 = {
			xtype : 'fieldset',
			title : '定性评估',
			collapsible : true,
			// defaultType: 'textfield',
			margin : '5 5 0 5',
//			 layout: {
//			 type: 'column'
//			 },
			width : 270,
			height : 220,
			items : [ me.assessFChart ]
		};
		
		
		
		
		var buttons = Ext.create('Ext.panel.Panel',{
			width : 120,
//			height : 190,
			border : false,
			layout:{
//	            align: 'stretch',
				type : 'column'
			},
			items:[
			       {xtype : 'button', text : '新增', margin : '115 5 0 20', columnWidth : 1,handler : function() {
							Ext.getCmp('riskTidyCardId').riskTidyOpe.riskTidyGrid.store.load();
				   }},
			       {xtype : 'button', text : '保存并上一条',  margin : '5 5 0 20',columnWidth : 1},
			       {xtype : 'button', text : '保存并下一条',  margin : '5 5 0 20',columnWidth : 1}
			       ]
		});

		Ext.apply(me, {
			autoScroll : true,
			border : false,
//			height : 260,
//			margin : '0 15 15 15',
			layout:{
//            align: 'stretch',
				type: 'hbox'
			},
//			buttons : [ {
//				text : '新增',
//				handler : function() {
//					alert('新增');
//				}
//			}, {
//				text : '保存并上一条',
//				handler : function() {
//					alert('保存');
//				}
//			}
//
//			, {
//				text : '保存并下一条',
//				handler : function() {
//					alert('保存');
//				}
//			} ],
			items : [fieldSet, fieldSet2, buttons]
		});

		me.callParent(arguments);
	}

});