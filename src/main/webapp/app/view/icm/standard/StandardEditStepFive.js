Ext.define('FHD.view.icm.standard.StandardEditStepFive', {
    extend: 'Ext.container.Container',
    alias: 'widget.standardeditstepfive',
    requires: [
       'FHD.view.icm.standard.StandardApplyApproval',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    isWindow:false,  
    border:false,
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('standardapplyapproval',{
        	flex : 1,
        	step:'5',
        	border:false,
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
		    		{index: 1, context:'1.申请更新',status:'done'},
		    		{index: 2, context:'2.申请审批',status:'done'},
		    		{index: 3, context:'3.反馈意见',status:'done'},
		    		{index: 4, context:'4.反馈确认',status:'done'},
		    		{index: 5, context:'5.标准审批',status:'current'}
		    	]
	    	})}),me.cardpanel]
        });
    
        me.callParent(arguments);
//        me.reloadData();
    },
    loadData: function(businessId, executionId){
    	var me = this;
    	me.businessId = businessId;
    	me.executionId = executionId;
//    	me.reloadData();
    },
    reloadData:function(){
    	var me=this;
    	me.cardpanel.loadData(me.businessId, me.executionId);
    }
});