/**
 * 目标树
 * 
 * @author zhengjunxiang
 */
Ext.define('FHD.view.risk.strategy.StrategyTreePanel', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.strategytreepanel',
    multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,
    url: __ctxPath + "/kpi/KpiStrategyMapTree/treeloader", //调用后台url
    rootVisible: false,
    root: {
        "id": "sm_root",
        "text": FHD.locale.get('fhd.sm.strategymaps'),
        "dbid": "sm_root",
        "leaf": false,
        "code": "sm",
        "type": "sm",
        "expanded": true
    },
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = 'strategyTreePanel';
    	me.queryUrl = 'kpi/KpiStrategyMapTree/treeloader';
    	
    	Ext.apply(me, {
   			listeners: {	//树单击事件
                itemclick: me.onTreepanelItemClick,
//                /**
//                 * 右键监听事件
//                 */
//                itemcontextmenu: function (view, rec, node, index, e) {
//                    e.stopEvent();
//                    var menu = me.contextItemMenuFun(view, rec, node, index, e);
//                    if (menu) {
//                        menu.showAt(e.getPoint());
//                    }
//                    return false;
//                },
//                load: function (store, records) {
//                	var selectedNode = null;
//                	var nodeItems = me.getSelectionModel().selected.items;
//                    if (nodeItems.length > 0) {
//                        selectedNode = nodeItems[0];
//                    }
//                    //没有选中节点，默认选中第一个节点
//                    if (selectedNode == null) {
//                    	//me.currentNodeClick();
//                    }
//                }

            }
        });
    	
        me.callParent(arguments);
    },
    /**
     * 添加右键菜单，暂时不用
     */
    contextItemMenuFun: function (view, rec, node, index, e) {
        var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });

        /*编辑*/
        var edit = {
            iconCls: 'icon-add',
            text: "编辑",
            handler: function () {
                me.currentNode = rec;
                if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
                    me.currentNode.expand();
                }

                //删除当前面板，添加修改面板
                var strategycartpanel = Ext.getCmp('strategycardpanel');
            	var tab = Ext.getCmp("strategytab");
            	tab.remove(tab.items.items[0],false);
            	tab.tabBar.removeAll();
            	tab.insert(0,strategycartpanel);
            	tab.getTabBar().insert(0,{xtype:'tbfill'});
            	//设置编辑面板按钮状态
            	strategycartpanel.navBtnState();
            	
                //加载数据
                var strategymainpanel = Ext.getCmp('strategymainpanel');
                strategymainpanel.reLoadData(rec);
 
            }
        };
        menu.add(edit);
        
        return menu;
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
            }else{
            	//alert("报错：目标树没有第一个节点");
            	return;
            }
        }
  
    	var id = selectedNode.data.id;
        var name = selectedNode.data.text;
        
    	var paramObj = {};		//过程参数
    	var detailJson = {};	//传入详细页面的json
    	FHD.ajax({
   			async:false,
            params: {
                "id": id
            },
            url:  __ctxPath + '/kpi/kpistrategymap/findparentbyid.f',
            callback: function (ret) {
            	paramObj.smid = id;
            	paramObj.smname = name;
            	paramObj.parentid = ret.parentid;
            	paramObj.parentname = ret.parentname;
            	paramObj.chartIds = ret.chartType==null?'':ret.chartType;
            }
        });
    	
    	//请求详细信息
    	FHD.ajax({
   			async:false,
   			params: {
                id: paramObj.smid
            },
            url: __ctxPath + '/kpi/kpistrategymap/findsmbyidtojsonstr.f',
            callback: function (ret) {
            	detailJson.smid = paramObj.smid;
            	detailJson.smname = paramObj.smname;
            	detailJson.parentId = paramObj.parentid;
            	detailJson.parentName = paramObj.parentname;
            	detailJson.code = ret.data.code;
            	detailJson.name = ret.data.name;
            	detailJson.shortName = ret.data.shortName;
            	detailJson.mainDim = ret.data.mainDim;
            	detailJson.mainTheme = ret.data.mainTheme;
            	detailJson.otherDim = ret.data.otherDim;
            	detailJson.otherTheme = ret.data.otherTheme;
            	detailJson.ownDept = ret.data.ownDept;
            	detailJson.estatus = ret.data.estatus;
            	detailJson.viewDept = ret.data.viewDept;
            	detailJson.warningFormula = ret.data.warningFormula;
            	detailJson.reportDept = ret.data.reportDept;
            	detailJson.assessmentFormula = ret.data.assessmentFormula;
            	detailJson.desc = ret.data.desc;
            	
            }
        });
        //显示目标详细信息
        var strategybasicformshow = Ext.getCmp('strategybasicformshow');
        strategybasicformshow.reLoadData(detailJson);
    },
    onTreepanelItemClick: function (tablepanel, record, item, index, e, options) {
    	var me = this;

    	var id = record.data.id;
        alert(id);
        //tab切换到基本信息标签
        var tab = me.face.tabpanel;
        tab.setActiveTab(1);
        
        //2 请求风险事件列表
    	var riskEventGrid = me.face.tabs[1];
    	riskEventGrid.store.proxy.url = __ctxPath + "/risk/findRiskByStrategyMapId?id="+id;
    	riskEventGrid.store.load();
    	//3 请求风险历史记录
    	var riskHistoryGrid = me.face.tabs[2];
    	riskEventGrid.store.proxy.url = __ctxPath + "/risk/findStrategyAdjustHistoryByStrategyMapId?id="+id;
    	riskEventGrid.store.load();
    }
});