/**
 * 
 * 风险整理表单
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessAdd', {
	extend : 'Ext.form.Panel',
	alias : 'widget.quaAssessAdd',

	//requires : [ 'FHD.view.risk.assess.utils.AssessFChart' ],

//	load : function(id, templateId) {
//		var me = this;
//		me.templateId = templateId;
//		FHD.ajax({
//            url: 'findRiskById.f?riskId=' + id,
//            callback: function (data) {
//            	me.fieldSetShowFun();        	
//                if (data && data.success) {
//                	me.fieldParentRiskName.setValue(data.data.parentRiskName);
//                	me.fieldRiskName.setValue(data.data.riskName);
//                	me.fieldCode.setValue(data.data.code);
//                	me.fieldRespDeptName.setValue(data.data.respDeptName);
//                	me.fieldRelaDeptName.setValue(data.data.relaDeptName);
//                	me.fieldInfluKpiName.setValue(data.data.influKpiName);
//                	me.fieldInfluProcessureName.setValue(data.data.influProcessureName);
//                }
//            }
//        });
//	},
	
//	getFieldSetShow : function(){
//		var me = this;
//		
//		me.fieldParentRiskName = Ext.widget('displayfield', {
//			xtype : 'displayfield',
//			rows : 2,
//			fieldLabel : '上级风险',
//			value : '财务风险',
//			margin : '5 0 3 0',
//			columnWidth : .5
//		});
//
//		me.fieldRiskName = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '风险名称',
//			margin : '5 0 3 20',
//			columnWidth : .5
//		});
//		
//		me.fieldCode = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '风险编号',
//			value : '00000ADF',
//			margin : '5 0 3 0',
//			columnWidth : .5
//		});
//		
//		me.fieldRespDeptName = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '责任部门',
//			value : '财会部,影响部',
//			margin : '5 0 3 20',
//			columnWidth : .5
//		});
//		
//		me.fieldRelaDeptName = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '相关部门',
//			value : '人力资源部,流程部',
//			margin : '5 0 3 0',
//			columnWidth : .5
//		});
//		
//		me.fieldInfluKpiName = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '影响指标',
//			value : '周转率,本期平均库存占用',
//			margin : '5 0 3 20',
//			columnWidth : .5
//		});
//		
//		me.fieldInfluProcessureName = Ext.widget('displayfield', {
//			xtype : 'textareafield',
//			rows : 2,
//			fieldLabel : '影响流程',
//			value : 'A流程,B流程',
//			margin : '5 0 3 0',
//			columnWidth : .5
//		});
//		
//		var label991 = Ext.widget('hiddenfield', {
//			 xtype: 'hiddenfield',
//			 rows: 2,
////			 fieldLabel: '修改意见',
//			 margin: '20 0 3 0',
//			 columnWidth: 1
//		 });
//		
//		me.fieldIdea = Ext.widget('textareafield', {
//			 xtype: 'textareafield',
//			 rows: 2,
//			 fieldLabel: '修改意见',
//			 margin: '5 0 3 0',
//			 columnWidth: .5
//		 });
//
//		me.fieldSetShow = {
//			xtype : 'fieldset',
//			collapsible : true,
//			width : 700,
//			height : 220,
//			defaultType : 'textfield',
//			margin : '5 5 0 5',
//			layout : {
//				type : 'column'
//			},
//			items : [ me.fieldParentRiskName, me.fieldRiskName, me.fieldCode, 
//			          me.fieldRespDeptName, me.fieldRelaDeptName, me.fieldInfluKpiName, me.fieldInfluProcessureName, label991, me.fieldIdea]
//		};
//		
//		return me.fieldSetShow;
//	},
	
	getFieldSetAdd : function(){
		var me = this;
		
		me.fieldParentRiskNameAdd = Ext.widget('textareafield', {
			xtype : 'displayfield',
			rows : 2,
			fieldLabel : '上级风险',
			margin : '5 0 3 0',
			name : 'kpiLevel',
			columnWidth : .25
		});
		
		me.fieldRiskNameAdd = Ext.widget('textareafield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '风险名称',
			margin : '5 0 3 20',
			columnWidth : .25
		});

		me.fieldCodeAdd = Ext.widget('textareafield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '风险编号',
			margin : '5 0 3 20',
			name : 'name',
			columnWidth : .25
		});
		
		me.fieldIdeaAdd = Ext.widget('textareafield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '修改意见',
			margin : '5 0 3 20',
			name : 'name',
			columnWidth : .25
		});

		me.fieldRespDeptNameAdd = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任部门',
			type : 'dept',
			height : 120,
			multiSelect : true,
			margin : '5 0 3 0',
			columnWidth : .25
		});

		me.fieldRelaDeptNameAdd = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '相关部门',
			type : 'dept',
			height : 125,
			multiSelect : true,
			margin : '0 0 3 20',
			columnWidth : .25
		});
		
		me.fieldInfluKpiNameAdd = Ext.widget('kpioptselector', {
			labelWidth : 100,
			gridHeight : 120,
			multiSelect : true,
			labelText : '影响指标',
			margin : '5 0 3 20',
			name : 'influProcessureName',
			columnWidth : .25
		});
		
		me.fieldInfluProcessureNameAdd = Ext.widget('kpioptselector', {
			labelWidth : 100,
			gridHeight : 120,
			multiSelect : true,
			labelText : '影响流程',
			margin : '5 0 3 20',
			name : 'influProcessureName',
			columnWidth : .25
		});
		
		me.fieldSetAdd = {
			xtype : 'fieldset',
			collapsible : true,
			width : 1200,
			height : 220,
			defaultType : 'textfield',
			margin : '5 5 0 5',
			layout : {
				type : 'column'
			},
			items : [ me.fieldParentRiskNameAdd, me.fieldRiskNameAdd, me.fieldCodeAdd, me.fieldIdeaAdd, me.fieldRespDeptNameAdd, 
			          me.fieldRelaDeptNameAdd, me.fieldInfluKpiNameAdd, me.fieldInfluProcessureNameAdd]
		};
		
		return me.fieldSetAdd;
	},
	
//	saveUpFun : function(){
//		var me = this;
//		debugger;
//		
//		var dicValues = '';
//		for(var i = 0; i < me.assessFChart.map.size(); i++){
//			dicValues += me.assessFChart.map.get(me.assessFChart.map.keys[i]) + ',';
//		}
//		
//		FHD.ajax({
//            url: 'saveDicValue.f?dicValues=' + dicValues,
//            callback: function (data) {
//            	if (data && data.success) {
////            		alert('评估完成');
//            	}
//            	
//            }
//        });
//		
//	},
	
//	getMyButtons : function(){
//		var me = this;
//		
//		me.myButtons = Ext.create('Ext.panel.Panel',{
//			width : 120,
//			border : false,
//			layout:{
//				type : 'column'
//			},
//			items:[
//			       {xtype : 'button', text : '新增', margin : '115 5 0 20', columnWidth : 1,handler : function() {
//							me.fieldSetAddFun();
//				   }},
//			       {xtype : 'button', text : '保存并上一条',  margin : '5 5 0 20',columnWidth : 1,handler : function() {
//							me.saveUpFun();
//				   }},
//			       {xtype : 'button', text : '保存并下一条',  margin : '5 5 0 20',columnWidth : 1}
//			       ]
//		});
//		
//		return me.myButtons;
//	},
	
	getFieldSetAssess : function(){
		var me = this;
		
		me.assessFChart = Ext.widget('assessFChart');
		me.assessFChart.load(me.templateId);
		
		me.fieldSetAssess = {
				xtype : 'fieldset',
				title : '定性评估',
				collapsible : true,
				margin : '5 5 0 5',
				width : 270,
				height : 220,
				items : [ me.assessFChart ]
		};
		
		return me.fieldSetAssess;
	},
	
//	fieldSetAddFun : function(){
//		var me = this;
//		
//		if(me.items.length != 0){
//			me.removeAll();
//		}
//		me.add(me.getFieldSetAdd());
//		me.add(me.getMyButtons());
//	},
//	
//	fieldSetShowFun : function(){
//		var me = this;
//		
//		if(me.items.length != 0){
//			me.removeAll();
//		}
//		me.add(me.getFieldSetShow());
//		me.add(me.getFieldSetAssess());
//		me.add(me.getMyButtons());
//	},

	// 初始化方法
	initComponent : function() {
		var me = this;
		
		Ext.apply(me, {
			autoScroll : true,
			border : false,
			layout:{
				type: 'hbox'
			},
			items : [me.getFieldSetAdd()]
		});

		me.callParent(arguments);
	}

});