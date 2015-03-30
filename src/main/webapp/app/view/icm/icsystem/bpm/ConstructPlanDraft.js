Ext.define('FHD.view.icm.icsystem.bpm.ConstructPlanDraft', {
    extend: 'Ext.container.Container',
    alias: 'widget.constructplandraft',
    
    requires: [
       'FHD.view.icm.icsystem.constructplan.ConstructPlanCardPanel',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
	autoScroll:true,
    
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('constructplancardpanel',{
        	flex:1,
        	businessId:me.businessId,
        	executionId:me.executionId,
        	editflag:true
        });
		me.cardpanel.initParam({
			businessId:me.businessId,
        	executionId:me.executionId
		});
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('panel',{border:false,items:Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划制定',status:'current'},
	    		{index: 2, context:'2.计划审批',status:'undo'},
	    		{index: 3, context:'3.计划发布',status:'undo'}
	    	]
    	})}),me.cardpanel]
        });
    
        me.callParent(arguments);
    },
    reloadData:function(){
    	var me=this;
    	
    }
});