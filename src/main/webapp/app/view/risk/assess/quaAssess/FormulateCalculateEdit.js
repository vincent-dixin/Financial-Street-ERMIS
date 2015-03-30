/**
 * 公式计算
 * 
 * @author 王再冉
 */
Ext.define('FHD.view.risk.assess.quaAssess.FormulateCalculateEdit', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formulateCalculateEdit',
    border: false,
    
    save : function(){
    	var me = this;
    	var form = me.getForm();
    	if(form.isValid()){
    		FHD.submit({//ajax调用
    				form: form,
    				url : __ctxPath + '/access/quaAssess/saveformulaset.f',//查询默认值
    				params : {
    					id : me.formulaId
    				},
    				callback : function(data){
    						
    				}
    		});
    	}
    },
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        	var Store = Ext.create('Ext.data.Store', {//性别store
        	    fields: ['id', 'name'],
        	    proxy: {
        	         type: 'ajax',
        	         url: __ctxPath + '/access/quaAssess/findformulatecalculatestore.f',
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	 Store.load({
  					callback : function(records, options, success) {
  						FHD.ajax({//ajax调用
    						url : __ctxPath + '/access/quaAssess/queryformulatwcalculatevalue.f',//查询默认值
    						params : {
    						},
    						callback : function(data){
    							me.orgRiskGather.setValue(data[0].deptRiskFormula);
    							me.aimRiskGather.setValue(data[0].strategyRiskFormula);
    							me.targetRiskGather.setValue(data[0].kpiRiskFormula);
    							me.processRiskGather.setValue(data[0].processRiskFormula);
    							me.riskKindPossi.setValue(data[0].riskTypeHappen);
    							me.riskKindAffect.setValue(data[0].riskTypeImpact);
    							me.formulaId = data[0].id;
    						}
    					});
  					} });
        	
        	//组织风险值汇总计算公式
        	me.orgRiskGather = Ext.create('Ext.form.field.ComboBox', {
        		    store: Store,
        		    fieldLabel: '组织风险值汇总计算公式',
        		    labelWidth : 200,
        		    editable:false,
        		    margin: '7 30 3 30',
        		    queryMode: 'local',
        		    name:'deptRiskFormula',
        		    displayField: 'name',
        		    valueField: 'id',
        		    triggerAction :'all',
        		    columnWidth:.5
        		});
        	
        	//目标风险值汇总计算公式
        	me.aimRiskGather = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '目标风险值汇总计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'strategyRiskFormula',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//指标风险值汇总计算公式
        	me.targetRiskGather = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '指标风险值汇总计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'kpiRiskFormula',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//流程风险值汇总计算公式
        	me.processRiskGather = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '流程风险值汇总计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'processRiskFormula',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        //风险分类汇总计算公式
        	//风险发生可能性计算公式
        	me.riskKindPossi = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '风险发生可能性计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'riskTypeHappen',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//风险影响程度计算公式
        	me.riskKindAffect = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '风险影响程度计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'riskTypeImpact',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        //风险事件汇总计算公式
        	//风险发生可能性计算公式
        	/*var riskEventPossi = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '风险发生可能性计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'riskeventpossi',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//风险影响程度计算公式
        	var riskEventAffect = Ext.create('Ext.form.field.ComboBox', {
        	    store: Store,
        	    fieldLabel: '风险影响程度计算公式',
        	    labelWidth : 200,
        	    editable:false,
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'riskeventaffect',
        	    displayField: 'name',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});*/
        var bbar =[//菜单项
    	           '->',
    	           {text : "保存",iconCls: 'icon-save', handler:me.save, scope : this}];
        	
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            layout: 'column',
            bbar:bbar,
            width: FHD.getCenterPanelWidth(),
            bodyPadding: "5 5 5 5",
            items: [
			{	
				 xtype: 'fieldset',
	             autoHeight: true,
	             autoWidth: true,
	             collapsible: true,
	             defaults: {
	                    margin: '3 30 3 30',
	                    labelWidth: 200
	            },
	            layout: {
	                    type: 'column'
	            },
				title: '公式计算',
				items:[me.orgRiskGather, me.aimRiskGather, me.targetRiskGather, me.processRiskGather]
			},
			{	
				 xtype: 'fieldset',
	             autoHeight: true,
	             autoWidth: true,
	             collapsible: true,
	             defaults: {
	                    margin: '3 30 3 30',
	                    labelWidth: 200
	            },
	            layout: {
	                    type: 'column'
	            },
				title: '风险分类汇总计算公式',
				items: [me.riskKindPossi, me.riskKindAffect]
			}/*,
			{	
				 xtype: 'fieldset',
	             autoHeight: true,
	             autoWidth: true,
	             collapsible: true,
	             defaults: {
	                    margin: '3 30 3 30',
	                    labelWidth: 200
	            },
	            layout: {
	                    type: 'column'
	            },
				title: '风险事件汇总计算公式',
				items: [riskEventPossi, riskEventAffect]
			}*/]
            
        });
        
        me.callParent(arguments);
    }
});