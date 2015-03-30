Ext.define('FHD.demo.riskEvent.RiskEventFilterTreeDemo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskeventfiltertree',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        var extraParams = {ids:'2,3'};
        var orgTreeUrl = __ctxPath + '/potentialRiskEvent/getOrgTreeRecordByEventIds';
        var strategyTreeUrl = __ctxPath + '/potentialRiskEvent/getStrategyMapTreeRecordByEventIds';
        var processTreeUrl = __ctxPath + '/potentialRiskEvent/getProcessTreeRecordByEventIds';
        var riskTreeUrl = __ctxPath + '/potentialRiskEvent/getRiskTreeRecordByEventIds';
        
        var orgTree = Ext.create("FHD.ux.TreePanel", {
        	treeTitle:'组织',
        	treeIconCls : 'icon-ibm-new-group-view',
    		rootVisible: true,
    		root: {
    	        "id": "root",
    	        "text": "组织",
    	        "expanded": false,
    	        'iconCls':'icon-ibm-new-group-view'	//样式
    	    },
           	extraParams:extraParams,
   			url: orgTreeUrl,
   			listeners: {
                itemclick: function(tablepanel, record, item, index, e, options){
                	alert(record.data.id);
                }
            }
        });
        var strategyTree = Ext.create("FHD.ux.TreePanel", {
        	treeTitle:'目标',
        	treeIconCls : 'icon-strategy',
    		rootVisible: true,
    		root: {
    	        "id": "root",
    	        "text": "目标",
    	        "expanded": false,
    	        'iconCls':'icon-strategy'	//样式
    	    },
           	extraParams:extraParams,
   			url: strategyTreeUrl,
   			listeners: {
                itemclick: function(tablepanel, record, item, index, e, options){
                	alert(record.data.id);
                }
            }
        });
        var processTree = Ext.create("FHD.ux.TreePanel", {
        	treeTitle:'流程',
        	treeIconCls : 'icon-ibm-icon-metrictypes',
    		rootVisible: true,
    		root: {
    	        "id": "root",
    	        "text": "流程",
    	        "expanded": false,
    	        'iconCls':'icon-ibm-icon-metrictypes'	//样式
    	    },
           	extraParams:extraParams,
   			url: processTreeUrl,
   			listeners: {
                itemclick: function(tablepanel, record, item, index, e, options){
                	alert(record.data.id);
                }
            }
        });
      	var riskTree = Ext.create("FHD.ux.TreePanel", {
      		treeTitle:'风险',
        	treeIconCls : 'icon-ibm-icon-scorecards',
    		rootVisible: true,
    		root: {
    	        "id": "root",
    	        "text": "风险",
    	        "dbid": "sm_root",
    	        "leaf": false,
    	        "code": "sm",
    	        "type": "orgRisk",
    	        "expanded": false,
    	        'iconCls':'icon-ibm-icon-scorecards'	//样式
    	    },
           	extraParams:extraParams,
   			url: riskTreeUrl,
   			listeners: {
                itemclick: function(tablepanel, record, item, index, e, options){
                	alert(record.data.id);
                }
            }
        });
      	
        var accordionTree = Ext.create("FHD.ux.layout.AccordionTree",{
        	region:'center',
        	title: '组织',
            iconCls: 'icon-ibm-icon-scorecards',
            width:250,
        	treeArr:[orgTree,strategyTree,processTree,riskTree]
        });
        
        Ext.apply(me, {
            layout:'border',
            items: [accordionTree]
            
        });
        
        me.callParent(arguments);
      
    }
});