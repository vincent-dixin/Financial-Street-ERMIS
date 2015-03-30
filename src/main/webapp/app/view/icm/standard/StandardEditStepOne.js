Ext.define('FHD.view.icm.standard.StandardEditStepOne', {
    extend: 'Ext.container.Container',
    alias: 'widget.standardeditstepone',
    requires: [
       'FHD.view.icm.standard.StandardApply',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    isWindow:false,  
    border:false,
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('standardapply',{
        	flex : 1,
        	border:false,
        	step : '1',
        	isWindow: me.isWindow
        });
        
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
    		items:[
	    		Ext.widget('panel',{items:Ext.widget('flowtaskbar',{
		    		jsonArray:[
			    		{index: 1, context:'1.申请更新',status:'current'},
			    		{index: 2, context:'2.申请审批',status:'undo'},
			    		{index: 3, context:'3.反馈意见',status:'undo'},
			    		{index: 4, context:'4.反馈确认',status:'undo'},
			    		{index: 5, context:'5.标准审批',status:'undo'}
			    	]
		    	})})
    		,me.cardpanel]
        });
    
        me.callParent(arguments);
        me.reloadData1();
    },
    loadData: function(standardId, executionId){
    	var me = this;
    	me.businessId = standardId;
    	me.executionId = executionId;
    },
    reloadData1:function(){
    	var me=this;
    	me.cardpanel.loadData(me.businessId, me.executionId);
    },
    reloadData:function(){
    }
});