

Ext.define('FHD.view.icm.icsystem.bpm.ConstructPlanResultsPublish', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplanresultspublish',
   requires: [
        'FHD.ux.icm.common.FlowTaskBar'
    ],

    plain: true,
    autoScroll:true,
    //传递的参数对象
    paramObj:{},
    /**
     * 设置激活的tab页签
     */
    setActiveItem:function(index){
    	me = this;
    	me.setActiveTab(index);
    },
    
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        //流程列表
        me.planprocesseditpublish = Ext.create('FHD.view.icm.icsystem.bpm.PlanProcessEditPublish',{id:'planprocesseditpublish',executionId : me.executionId});
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type:  'vbox'
        	},
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'
        	},
            items: [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.合规诊断',status:'done'},
	    		{index: 2, context:'2.缺陷反馈',status:'done'},
	    		{index: 3, context:'3.缺陷整理',status:'done'},
	    		{index: 4, context:'4.流程和矩阵编写',status:'done'},
	    		{index: 5, context:'5.成果审核',status:'done'},
	    		{index: 6, context:'6.成果发布',status:'current'}
	    	]
    		}),me.planprocesseditpublish]
        });
        me.callParent(arguments);
    },
    submit : function(){
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/icsystem/constructplanrelaprocesssubmit.f',
		    params : {
		    	businessId : me.businessId,
		    	executionId : me.executionId
			},
			callback : function(data) {
				if(me.winId){
					Ext.getCmp(me.winId).close();
				}
			}
		});
	},
    reloadData : function() {
    	var me = this;
    	me.planprocesseditpublish.initParam({
    		constructPlanId : me.businessId,
    		executionId 	: me.executionId
    	});
    	me.planprocesseditpublish.reloadData();
    }
});