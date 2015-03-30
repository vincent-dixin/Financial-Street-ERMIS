

Ext.define('FHD.view.icm.icsystem.FlowNoteMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.flownotemainpanel',
    requires: [
        'FHD.view.icm.icsystem.FlowNoteList',
        'FHD.view.icm.icsystem.form.NoteEditForm'
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
        me.flownotelist = Ext.widget('flownotelist',{id:'flownotelist',border:false});
        //流程维护form
        me.noteeditform = Ext.widget('noteeditform',{id:'noteeditform',dataType : 'sc',typeTitle : '记分卡'});
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'	
        	},
            items: [me.flownotelist,me.noteeditform]
        });
        me.callParent(arguments);
    },
    reloadData : function() {
    	var me = this;
    	me.flownotelist.reloadData();
    }
});