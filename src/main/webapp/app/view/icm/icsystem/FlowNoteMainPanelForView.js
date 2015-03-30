

Ext.define('FHD.view.icm.icsystem.FlowNoteMainPanelForView', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget.flownotemainpanelforview',
    requires: [
        'FHD.view.icm.icsystem.FlowNoteListForView'
    ],
    layout : 'fit',
//    plain: true,
    border : true,
    //传递的参数对象
    paramObj:{},
    collapsible: true,
    title : '流程节点列表',
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        //流程节点列表
        me.flownotelistforview = Ext.widget('flownotelistforview',{id:'flownotelistforview',searchable:false,border : false,pagable : false});
        Ext.applyIf(me, {
            items: [me.flownotelistforview]
        });
        me.callParent(arguments);
    },
    reloadData : function() {
    	var me = this;
    	me.flownotelistforview.initParam(me.paramObj);
    	me.flownotelistforview.reloadData();
    }
});