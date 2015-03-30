

Ext.define('FHD.view.icm.icsystem.RiskMeasureMainPanelForView', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget.riskmeasuremainpanelforview',
    requires: [
        'FHD.view.icm.icsystem.FlowRiskListForView'
    ],
    layout : 'fit',
//    plain: true,
    border : true,
    //传递的参数对象
    paramObj:{},
    collapsible: true,
    title : '风险控制矩阵列表',
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    initComponent: function() {
        var me = this;
        //流程节点列表
        me.flowrisklistforview = Ext.widget('flowrisklistforview',{id:'flowrisklistforview',searchable:false,border : false,pagable : false});
        Ext.applyIf(me, {
            items: [me.flowrisklistforview]
        });
        me.callParent(arguments);
    },
    reloadData : function() {
    	var me = this;
    	me.flowrisklistforview.initParam(me.paramObj);
    	me.flowrisklistforview.reloadData();
    }
});