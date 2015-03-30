

Ext.define('FHD.view.icm.icsystem.RiskMeasureMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskmeasuremainpanel',
    requires: [
        'FHD.view.icm.icsystem.FlowRiskList',
        'FHD.view.icm.icsystem.form.RiskEditForm'
    ],
    layout : 'card',
    plain: true,
    
    //传递的参数对象
    paramObj:{},
    
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        //流程节点列表
        me.flowrisklist = Ext.widget('flowrisklist',{id:'flowrisklist',border:false});
        //流程维护form
        me.riskeditform = Ext.widget('riskeditform',{id:'riskeditform',border:false});
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'	
        	},
            items: [me.flowrisklist,me.riskeditform]
        });
        me.callParent(arguments);
    },
    reloadData : function() {
    	var me = this;
    	me.flowrisklist.reloadData();
    }
});