

Ext.define('FHD.view.comm.theme.ThemeTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.themetab',

    requires: [
        'FHD.view.comm.theme.ThemeRecordGrid',
        'FHD.view.comm.theme.ThemeBasicinfoPanel'
    ],

    plain: true,
    
    //传递的参数对象
    paramObj:{},
    
    //添加监听事件
    listeners: {
    	tabchange:function(tabPanel, newCard, oldCard, eOpts){
    		var me = this;
    		var cardid = newCard.id;
    		var themeTree = Ext.getCmp('themetree');
    		var themebasicform = Ext.getCmp('themebasicform');
    		if(null!=themeTree.currentNode){
    			if('themerecordgrid'==cardid){//统计列表页签
    			if(null != themeTree.currentNode.data.id&&themeTree.getRootNode()!=themeTree.currentNode){
        			me.themerecordgrid.store.proxy.url = me.themerecordgrid.queryUrl;//动态赋给机构列表url
    	  			me.themerecordgrid.store.proxy.extraParams.themeId = themeTree.currentNode.data.id;
    	  			me.themerecordgrid.store.load();
        		}else{//如果是根节点，清空列表数据
        			me.themerecordgrid.store.proxy.url = me.themerecordgrid.queryUrl;
    	  			me.themerecordgrid.store.proxy.extraParams.themeId = null;
    	  			me.themerecordgrid.store.load();
        		}
    		}else if('themebasicinfopanel'==cardid){//基本信息页签
    			if(null != themeTree.currentNode.data.id&&themeTree.getRootNode()!=themeTree.currentNode){//树节点id不为空并且不是根节点
    	  			themebasicform.analytreeId = themeTree.currentNode.data.id;
        		}else{
        			themebasicform.getForm().reset();
        		}
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
        //统计列表页签
        me.themerecordgrid = Ext.widget('themerecordgrid',{id:'themerecordgrid'});
     
        //基本信息页签
        me.themebasicinfopanel = Ext.widget('themebasicinfopanel',{id:'themebasicinfopanel',title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo')});
        
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.themerecordgrid,me.themebasicinfopanel]
        });

        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
    },
    reLoadData : function() {
    	var me = this;
    	var activeTab = me.getActiveTab();
    	var themetab  = Ext.getCmp('themetab');
    	var cardid = activeTab.id;
    	if('themerecordgrid'==cardid){//度量标准页签
			me.themerecordgrid.store.proxy.extraParams.id = themetab.paramObj.categoryid;
			me.themerecordgrid.store.load();
		}else if('themebasicinfopanel'==cardid){//基本信息页签
			me.themebasicinfopanel.reLoadData();
		}
    }
});