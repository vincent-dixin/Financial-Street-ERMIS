/**
 * 
 * 行业左侧树
 */

Ext.define('FHD.view.risk.assess.utils.DepartmentTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.departmentTree',
    
    onTreeItemClick : function(view,record){
    	var me = this;
    	me.setNavigation(record.data.id);
    },
    
    setNavigation : function(id){
    	//Ext.getCmp('industryRightPanelId').load(id);
    	//alert(id);
    },
    
    
    initNodeClick : function(view,record){
		var me = this;
		debugger;
		var selectedNode = null;
		var nodeItems = me.getSelectionModel().selected.items;
		
		if (nodeItems.length > 0) {
			selectedNode = nodeItems[0];
		}
		//没有选中节点，默认选中第一个节点
		if (selectedNode == null) {
			me.currentNodeClick();
		}
    },
    
    /**
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick: function () {
        var me = this;
        var selectedNode;
        var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
        }

        //没有选中节点，默认选中第一个节点
        if (selectedNode == null) {	
            var firstNode = me.getRootNode().firstChild;
            if(null!=firstNode){
            	me.getSelectionModel().select(firstNode);
            	selectedNode = firstNode;
            	me.treeId = firstNode.raw.id;
            	me.setNavigation(firstNode.raw.id);
            }else{
            	return;
            }
        }

//    	//请求风险详细信息
//    	FHD.ajax({
//   			async:false,
//   			params: {
//                riskId: selectedNode.data.id
//            },
//            url: __ctxPath + '/risk/findRiskById.f',
//            callback: function (ret) {
//            	//显示目标详细信息
//            	Ext.fcache.obj.views["riskFormView"].reLoadData(ret);
//            }
//        });
 
    },
    
 // 初始化方法
    initComponent: function() {
    	var me = this;
    	//初始化参数
    	var extraParams = {};
    	extraParams.rbs = false;
    	extraParams.canChecked = false;
    	
    	me.queryUrl = __ctxPath + '/risk/riskTree.f'	//暂时不留作参数
    	
    	Ext.apply(me, {
    		rootVisible: false,
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
           	split: true,
           	collapsible : true,
            extraParams:extraParams,
           	url: me.queryUrl,//调用后台url
   			listeners: {
   				//树单击事件
                itemclick : me.onTreeItemClick,
                load :  me.initNodeClick
            }
        });
        me.callParent(arguments);
    }
    
});