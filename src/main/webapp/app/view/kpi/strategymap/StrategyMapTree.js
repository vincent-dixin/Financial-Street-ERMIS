/**
 * 战略地图树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.kpi.strategymap.StrategyMapTree',{
    extend: 'Ext.tree.Panel',
    alias: 'widget.strategymaptree',
    
    
    root : {
    	text : '第一会达',
        iconCls : 'icon-ibm-icon-report',
        expanded : true,
        children : [{
        	text: '系统研发部',
        	expanded : true,
        	iconCls : 'icon-ibm-icon-report',
        	leaf : true
        }]
    },
    
    initComponent: function() {
    	var me = this;
    	
    	Ext.applyIf(me, {
        	listeners: {
		        itemclick: function (node, record, item) {
		            //me.clickFunction(record);
		        },
		        load:function(store,records){
		        	//me.treeload(store,records);
		        }
		        
		    }
        });
        
        me.callParent(arguments);
    }

});