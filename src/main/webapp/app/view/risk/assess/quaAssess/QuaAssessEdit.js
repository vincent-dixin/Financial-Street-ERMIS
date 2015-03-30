/**
 * 
 * 风险整理表单
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessEdit', {
	extend : 'Ext.form.Panel',
	alias : 'widget.quaAssessEdit',

	requires : [ 'FHD.view.risk.assess.utils.AssessFChart' ],

//	load : function(id, templateId) {
//		var me = this;
//		me.templateId = templateId;
//		FHD.ajax({
//            url: 'findRiskById.f?riskId=' + id,
//            callback: function (data) {
//            	//me.fieldSetShowFun();        	
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
	
	getFieldSetShow : function(datas){
		var me = this;
		
		me.fieldParentRiskName = Ext.widget('displayfield', {
			xtype : 'displayfield',
			rows : 2,
			fieldLabel : '上级风险',
			value : datas.parentRiskName,
			margin : '5 0 3 20',
			columnWidth : .5
		});
		
		me.fieldCode = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '风险编号',
			value : datas.code,
			margin : '5 0 3 0',
			columnWidth : .5
		});
		
		me.fieldRespDeptName = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '责任部门',
			value : datas.respDeptName,
			margin : '5 0 3 20',
			columnWidth : .5
		});
		
		me.fieldRelaDeptName = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '相关部门',
			value : datas.relaDeptName,
			margin : '5 0 3 0',
			columnWidth : .5
		});
		
		me.fieldInfluKpiName = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '影响指标',
			value : datas.influKpiName,
			margin : '5 0 3 20',
			columnWidth : .5
		});
		
		me.fieldInfluProcessureName = Ext.widget('displayfield', {
			xtype : 'textareafield',
			rows : 2,
			fieldLabel : '影响流程',
			value : datas.influProcessureName,
			margin : '5 0 3 0',
			columnWidth : .5
		});
		
		me.panelShow = Ext.create('Ext.panel.Panel',{
			border:false,
			layout : {
				type : 'column'
			},
			items : [ me.fieldParentRiskName, me.fieldCode, 
			          me.fieldRespDeptName, me.fieldRelaDeptName, me.fieldInfluKpiName, me.fieldInfluProcessureName]
		});
		
		return me.panelShow;
	},
	
	saveUpFun : function(){
		var me = this;
		var dicValues = '';
		
		for(var i = 0; i < me.assessFChart.map.size(); i++){
			dicValues += me.assessFChart.map.get(me.assessFChart.map.keys[i]) + ',';
		}
		
		FHD.ajax({
            url: 'saveDicValue.f?dicValues=' + dicValues,
            callback: function (data) {
            	if (data && data.success) {
//            		alert('评估完成');
            	}
            	
            }
        });
		
	},
	
	getFieldSetAssess : function(result){
		var me = this;
		
		me.assessFChart = Ext.widget('assessFChart');
		me.assessFChart.load(result);
		
		me.fieldSetAssess = {
				xtype : 'fieldset',
				title : '定性评估',
				margin : '5 5 0 5',
				items : [ me.assessFChart ]
		};
		
		return me.fieldSetAssess;
	},
});