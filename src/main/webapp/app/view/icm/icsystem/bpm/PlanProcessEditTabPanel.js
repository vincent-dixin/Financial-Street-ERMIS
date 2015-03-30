

Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.planprocessedittabpanel',
    requires: [
        'FHD.view.icm.icsystem.FlowNoteMainPanel',
        'FHD.view.icm.icsystem.FlowEditPanelForWorkflow',
        'FHD.view.icm.icsystem.RiskMeasureMainPanel'
    ],
    readOnly : false,
    plain: true,
    border:false,
    //传递的参数对象
    paramObj:{},
    autoHeight : true,
    //添加监听事件
    listeners: {
    	beforetabchange : function(tabPanel, newCard, oldCard, eOpts){
    	},
    	tabchange : function(tabPanel, newCard, oldCard, eOpts){
    		var cardid = newCard.id;
    		var me = this;
    		if(cardid == 'floweditpanel'){
    			me.floweditpanel.paramObj.processId = me.paramObj.processId;
    			me.floweditpanel.reloadData();
    		}
    		else if(cardid == 'flownotemainpanel'){
    			me.flownotemainpanel.initParam(
    				{
    					processId : me.paramObj.processId
    				});
    		    me.flownotemainpanel.flownotelist.paramObj.processId = me.paramObj.processId;
    			me.flownotemainpanel.reloadData();
    		}
    		else if(cardid == 'ristmeasuremainpanel'){
    			me.riskmeasuremainpanel.flowrisklist.paramObj.processId = me.paramObj.processId;
    			me.riskmeasuremainpanel.reloadData();
    		}
    		else{}
    	}
    },
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
        // 流程维护form
        me.floweditpanelforworkflow = Ext.widget('floweditpanelforworkflow',{id:'floweditpanelforworkflow',title: '1.基本信息',readOnly:me.readOnly});
        // 流程节点列表
        me.flownotemainpanel = Ext.widget('flownotemainpanel',{id:'flownotemainpanel',title:'2.流程节点维护',border :false});
        // 风险控制维护
        me.riskmeasuremainpanel = Ext.widget('riskmeasuremainpanel',{id:'ristmeasuremainpanel',title: '3.风险控制矩阵维护',border :false});
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'
        	},
            items: [me.floweditpanelforworkflow,me.flownotemainpanel,me.riskmeasuremainpanel]
        });
        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
    },
   
    reloadData : function() {
    	var me = this;
    	me.planprocesseditdraft.initParam({
    		constructPlanId : me.businessId,
    		executionId 	: me.executionId
    	});
    	me.planprocesseditdraft.reloadData();
    }
});