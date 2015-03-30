Ext.define('FHD.view.icm.rectify.RectifyImproveContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.rectifyimprovecontainer',
    requires: [
       'FHD.view.icm.rectify.RectifyImproveCardPanel',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    isWindow:false,  
    border:false,
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('rectifyimprovecardpanel',{
        	flex : 1,
        	border:false,
        	isWindow: me.isWindow
        });
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
    		items:[
    		Ext.widget('panel',{border:false, items:
	    		Ext.widget('flowtaskbar',{
	        		jsonArray:[
			    		{index: 1, context:'1.计划制定',status:'current'},
			    		{index: 2, context:'2.计划审核',status:'undo'},
			    		{index: 3, context:'3.计划审批',status:'undo'},
			    		{index: 4, context:'4.计划发布',status:'undo'}
			    	]
	        	})
    		}),
    		me.cardpanel]
        });
    
        me.callParent(arguments);
    },
    loadData: function(improveId,executionId){
    	var me = this;
    	me.improveId = improveId;
    	me.executionId = executionId;
    },
    reloadData:function(){
    	var me=this;
    	me.cardpanel.loadData(me.improveId, me.executionId);
    }
});