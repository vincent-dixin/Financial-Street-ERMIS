/**
 * 
 * 行业左侧树
 */

Ext.define('FHD.view.industry.IndustryTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.industryTree',
    
    onTreeItemClick : function(view,record){
    	var me = this;
    	me.setNavigation(record.data.id);
    },
    
    setNavigation : function(id){
    	Ext.getCmp('industryRightPanelId').load(id);
    },
    
    
    initNodeClick : function(){
		var me = this;
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
//    	var extraParams = {};
//    	extraParams.rbs = me.rbs || false;
//    	extraParams.canChecked = me.canChecked || false;
    	
    	me.id = 'industryTreeId';
//    	me.queryUrl = __ctxPath + '/risk/riskTree.f';	//暂时不留作参数
    	
    	var root =  {
//     		  id:'1',
//       		  text:'root',
//       		  expanded:true,
	    	expanded: true,
	        children: [
	            {id: '1', leaf: true, text : '化工行业'},
	            {id: '2', leaf: true, text : '集成行业'}
	        ]
	    };
    	
    	Ext.apply(me, {
    		region: 'west',
    		width:215,
    		rootVisible: false,
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
           	split: true,
           	collapsible : true,
//          extraParams:extraParams,
           	root : root,
//   		url: me.queryUrl,//调用后台url
   			listeners: {
   				//树单击事件
                itemclick : me.onTreeItemClick
//                /**
//                 * 右键监听事件
//                 */
//                itemcontextmenu: function (view, rec, node, index, e) {
////                    e.stopEvent();
////                    var menu = me.contextItemMenuFun(view, rec, node, index, e);
////                    if (menu) {
////                        menu.showAt(e.getPoint());
////                    }
////                    return false;
//                }
            }
        });
        me.callParent(arguments);
        me.initNodeClick();
    }
    
});