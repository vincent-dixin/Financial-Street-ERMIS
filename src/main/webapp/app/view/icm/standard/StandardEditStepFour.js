Ext.define('FHD.view.icm.standard.StandardEditStepFour', {
    extend: 'Ext.container.Container',
    alias: 'widget.standardeditstepfour',
    requires: [
       'FHD.view.icm.standard.StandardApplyShowOnly',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    isWindow:false,  
    border:false,
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('standardapplyshowonly',{
        	flex : 1,
        	step:'4',
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
		    		{index: 4, context:'4.反馈确认',status:'current'},
		    		{index: 5, context:'5.标准审批',status:'undo'}
		    	]
	    	})}),me.cardpanel]
        });
    
        me.callParent(arguments);
//        me.reloadData();
    },
    loadData: function(businessId,executionId){
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