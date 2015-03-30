Ext.define('FHD.view.icm.standard.StandardTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.standardtab',
    
    requires: [
        'FHD.view.icm.standard.StandardList',
        'FHD.view.icm.standard.StandardEdit'
    ],
    plain: true,
    //传递的参数对象
    paramObj:{},
    
    //添加监听事件
    listeners: {
    	tabchange:function(tabPanel, newCard, oldCard, eOpts){
    		var cardid = newCard.id;
    		if('standardlist'==cardid){//信息列表页签
    			if(tabPanel.paramObj!=undefined){
    				//tabPanel.store.proxy.extraParams.id = tabPanel.paramObj.standardid;
        			//tabPanel.store.load();
    			}
    		}else if('standardedit'==cardid){//基本信息页签
    			if(tabPanel.paramObj!=undefined){
    				//Ext.getCmp('standardtab').standardedit.reLoadData();
    			}
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
        //信息列表页签
        me.standardlist = Ext.widget('standardlist',{title:'内控要求'});
        //基本信息页签
        me.standardedit = Ext.widget('standardedit',{title:'基本信息'});
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'
        	},
            items: [me.standardedit,me.standardlist,me.riskstandardmanage]
        });
        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
    },
    reloadData : function() {
    	var me = this;
    	me.standardlist.reloadData();
    	
    	//me.standardedit.reloadData();
    }
});