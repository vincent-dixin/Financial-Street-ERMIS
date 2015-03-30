
Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanelForView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planprocessedittabpanelforview',
    requires: [
        'FHD.view.icm.icsystem.FlowNoteMainPanelForView',
        'FHD.view.icm.icsystem.FlowEditPanelForView',
        'FHD.view.icm.icsystem.RiskMeasureMainPanelForView'
    ],
    readOnly : false,
    layout : {
    	type : 'vbox',
    	align : 'stretch'
    },
    plain: true,
    autoScroll : true,
    //传递的参数对象
    paramObj:{},
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        // 流程维护form
        me.floweditpanelforview = Ext.widget('floweditpanelforview',{id : 'floweditpanelforview',readOnly:me.readOnly});
        // 流程节点列表
        me.flownotemainpanelforview = Ext.widget('flownotemainpanelforview');
        // 风险控制维护
        me.riskmeasuremainpanelforview = Ext.widget('riskmeasuremainpanelforview');
        Ext.applyIf(me, {
        	items: [me.floweditpanelforview,me.flownotemainpanelforview,me.riskmeasuremainpanelforview]
        });
        me.callParent(arguments);
    },
   
    reloadData : function() {
    	var me = this;
    	me.floweditpanelforview.initParam({
			processId : me.paramObj.processId
		});
    	me.floweditpanelforview.reloadData();
    	me.flownotemainpanelforview.initParam({
    		processId : me.paramObj.processId
    	});
    	me.flownotemainpanelforview.reloadData();
    	me.riskmeasuremainpanelforview.initParam({
    		processId : me.paramObj.processId
    	});
    	me.riskmeasuremainpanelforview.reloadData();
    	
    }
});