Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.constructplanpanel',
    
    requires: [
       'FHD.view.icm.icsystem.constructplan.ConstructPlanCardPanel',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
	autoScroll:true,
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	border:false,
    initComponent: function() {
        var me = this;
        
        
        me.cardpanel = Ext.widget('constructplancardpanel',{
        	flex : 1,
        	businessId:me.businessId,
        	executionId:me.executionId,
        	editflag:me.editflag,
        	border:false
        });

       Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划编辑',status:'current'},
	    		{index: 2, context:'2.计划审批',status:'undo'},
	    		{index: 3, context:'3.计划发布',status:'undo'}
	    		]
    		}),me.cardpanel
    	]
        });
    
        me.callParent(arguments);
    },
    reloadData:function(){
    	var me=this;
    	var constructplaneditpanel = me.up('constructplaneditpanel');
    	if(constructplaneditpanel){
    		me.cardpanel.initParam(me.paramObj);
        	me.cardpanel.reloadData();
    	}
    }
});