Ext.define('FHD.view.icm.assess.bpm.AssessPlanBpmSix', {
	extend: 'Ext.container.Container',
    alias: 'widget.assessplanbpmsix',
    
    requires: [
       'FHD.view.icm.assess.AssessPlanExecuteCardPanel',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoScroll:true,
    
    initComponent: function() {
        var me = this;
        
        me.assessplanexecutecardpanel = Ext.widget('assessplanexecutecardpanel',{
        	flex:1,
        	businessId:me.businessId,
        	executionId:me.executionId,
        	editflag:true
        });
		
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
    			type: 'vbox'
        	},
    		items:[Ext.widget('panel',{border:false,items:Ext.widget('flowtaskbar',{
        		jsonArray:[
		    		{index: 1, context:'1.计划制定',status:'done'},
		    		{index: 2, context:'2.计划审批',status:'done'},
		    		{index: 3, context:'3.任务分配',status:'done'},
		    		{index: 4, context:'4.任务分配审批',status:'done'},
		    		{index: 5, context:'5.计划发布',status:'done'},
		    		{index: 6, context:'6.内控测试',status:'current'},
		    		{index: 7, context:'7.测试结果复核',status:'undo'},
		    		{index: 8, context:'8.汇总整理',status:'undo'},
		    		{index: 9, context:'9.缺陷反馈',status:'undo'},
		    		{index: 10, context:'10.缺陷调整',status:'undo'},
		    		{index: 11, context:'11.缺陷确认',status:'undo'}
		    	]
	    	})}),me.assessplanexecutecardpanel]
        });
    
        me.callParent(arguments);
    },
    reloadData:function(){
    	var me=this;
    	
    }
});