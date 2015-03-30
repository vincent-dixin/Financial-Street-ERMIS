

Ext.define('FHD.view.icm.icsystem.FlowTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.flowtabpanel',
    requires: [
        'FHD.view.icm.icsystem.FlowNoteMainPanel',
        'FHD.view.icm.icsystem.FlowEditPanel',
        'FHD.view.icm.icsystem.RiskMeasureMainPanel'
    ],

    plain: true,
    
    //传递的参数对象
    paramObj:{},
    
    //添加监听事件
    listeners: {
    	beforetabchange : function(tabPanel, newCard, oldCard, eOpts){
    		//判断树中是否有选中的元素
    		var selectId = this.up('flowmainmanage').flowtree.selectId;   
    		if(selectId == ''){
    			Ext.Msg.alert("注意","请选择一个且唯一一个流程进行流程的节点和风险维护!");
    			return false;
    		}
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
        // 流程节点列表
        me.flownotemainpanel = Ext.widget('flownotemainpanel',{id:'flownotemainpanel',title:'流程节点信息'});
        // 流程维护form
        me.floweditpanel = Ext.widget('floweditpanel',{id:'floweditpanel',title: '基本信息',dataType : 'sc',typeTitle : '记分卡'});
        // 风险控制维护
        me.riskmeasuremainpanel = Ext.widget('riskmeasuremainpanel',{id:'ristmeasuremainpanel',title: '风险信息',dataType : 'sc',typeTitle : '记分卡'});
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'
        	},
            items: [me.floweditpanel,me.flownotemainpanel,me.riskmeasuremainpanel]
        });
        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
    },
    reloadData : function() {
    	var me = this;
    	me.flownotelist.reloadData();
    }
});