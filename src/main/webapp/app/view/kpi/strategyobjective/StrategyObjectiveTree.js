Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.strategyobjectivetree',


    multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,
    url: __ctxPath + "/kpi/KpiStrategyMapTree/treeloader", //调用后台url
    rootVisible: true,
    root: {
        "id": "sm_root",
        "text": FHD.locale.get('fhd.sm.strategymaps'),
        "dbid": "sm_root",
        "leaf": false,
        "code": "sm",
        "type": "sm",
        "expanded": true,
        'iconCls':'icon-strategy'
    },
    /**
     * 添加右键菜单
     */
    contextItemMenuFun: function (view, rec, node, index, e) {
        var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });

        /*添加下级*/
        var subLevel = {
            iconCls: 'icon-add',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.subLevel'),
            handler: function () {
                me.currentNode = rec;
                if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
                    me.currentNode.expand();
                }
                me.smSubLevelHandler(id, name); //添加下级函数
            }
        };
        menu.add(subLevel);
        if (index != 0) {
            /*添加同级*/
            var sameLevel = {
                iconCls: 'icon-add',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
                handler: function () {
                    me.currentNode = rec.parentNode;
                    me.currentNode.expand();
                    me.smSameLevelHandler(id); //添加同级函数
                }
            };
            menu.add(sameLevel);
            menu.add('-');
        }
        if (index != 0) {
            /*删除菜单*/
            var delmenu = {
                iconCls: 'icon-delete-icon',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.delete'),
                handler: function () {
                    me.currentNode = rec.parentNode;
                    me.smDeleteHandler(id, "sm", rec); //删除节点函数
                }
            }
            menu.add(delmenu);
            menu.add('-');
        }
        /* ‘刷新’右键菜单*/
        var refresh = {
            iconCls: 'icon-arrow-refresh',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.refresh'),
            handler: function () {
                me.refreshTree(); //刷新树函数
            }
        }
        menu.add(refresh);
        if(index!=0){
        	menu.add('-');
        	//启用停用菜单
        	var enablemenu = {
                    iconCls: 'icon-plan-start',
                    text: FHD.locale.get('fhd.sys.planMan.start'),
                    handler: function () {
                    	me.enables("0yn_y", id,rec);
                    }
                };
            menu.add(enablemenu);
            var disablemenu = {
                    iconCls: 'icon-plan-stop',
                    text: FHD.locale.get('fhd.sys.planMan.stop'),
                    handler: function () {
                    	me.enables("0yn_n", id,rec);
                    }
                };
            menu.add(disablemenu);
            menu.add('-');
        }
        return menu;
    },
    enables:function(enable,id,rec){
    	FHD.ajax({
            params: {
                "strategyMapId": id,
                "enable":enable
            },
            url: __ctxPath + '/kpi/kpistrategymap/mergestrategymapenable.f',
            callback: function (ret) {
            	if(ret.success){
            		if(ret.iconCls){
                  	  var data = rec.data;
                  	  data.iconCls = ret.iconCls;
                  	  rec.updateInfo(data);
            		}
            		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
            	}
            }
        });
    },
    deleteNodeAfterSelectNode:function(node){
    	var me = this;
    	var parentnode = node.parentNode;
     	me.currentNode.removeChild(node);
     	if(!me.currentNode.hasChildNodes()&&null!=me.currentNode.parentNode){
     		var oldnode = me.currentNode;
     		var newnode = me.currentNode;
     		newnode.data.leaf = true;
     		me.currentNode.parentNode.replaceChild(newnode,oldnode);
     	}
     	me.getSelectionModel().select(parentnode);
     	Ext.getCmp('strategyobjectivemainpanel').reLoadData(parentnode);
     	if(!me.getRootNode().hasChildNodes()){
     		me.disablePanel(true);
        }
    },
    /**
     * 删除节点函数
     */
    smDeleteHandler: function (id, type, node) {
        var me = this;
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
                    FHD.ajax({
                        params: {
                            "id": id
                        },
                        url: __ctxPath + '/kpi/kpistrategymap/removestrategymap.f',
                        callback: function (ret) {
                            if (ret && !ret.result) {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '存在下级,不能删除!');
                            } else {
                                if (type == "sm") {
                                   me.deleteNodeAfterSelectNode(node);
                                }
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                            }
                        }
                    });
                }
            }
        });
    },
    /**
     *刷新树函数 
     */
    refreshTree: function (obj) {
    	var me = this;
        me.getStore().load();
    },
    /**
     * 添加同级事件函数
     */
    smSameLevelHandler: function (id) {
        var me = this;
        var paramObj = {};
        FHD.ajax({
            async: false,
            params: {
                "id": id
            },
            url:  __ctxPath + '/kpi/kpistrategymap/findparentbyid.f',
            callback: function (ret) {

                paramObj.parentid = ret.parentid;
                paramObj.smid = 'undefined';
                paramObj.parentname = ret.parentname;
                paramObj.editflag = false;
            }
        });
        var strategyobjectivetab = Ext.getCmp('strategyobjectivetab');
        var strategyobjectivebasicform = Ext.getCmp('strategyobjectivebasicform');
        var strategyobjectivekpiset = Ext.getCmp('strategyobjectivekpiset');
        var strategyobjectivewarningset = Ext.getCmp('strategyobjectivewarningset');
        //设置战略目标主面板为右侧显示
        Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('strategyobjectivemainpanel'));
        
        me.disablePanel(false);

        strategyobjectivebasicform.initParam(paramObj);
        strategyobjectivekpiset.initParam(paramObj);
        strategyobjectivewarningset.initParam(paramObj);
        strategyobjectivetab.initParam(paramObj);

        strategyobjectivebasicform.clearFormData();

        strategyobjectivetab.setActiveTab(2);

        Ext.getCmp('strategyobjectivecardpanel').setFirstItemFoucs();

        strategyobjectivebasicform.reLoadData();
        strategyobjectivekpiset.reLoadData();
        strategyobjectivewarningset.reLoadData();


    },
    smSubLevelHandler: function (id, name) {
        var me = this;
        var paramObj = {};
        paramObj.parentid = id;
        paramObj.smid = 'undefined';
        paramObj.parentname = name;
        paramObj.editflag = false;
        var strategyobjectivetab = Ext.getCmp('strategyobjectivetab');
        var strategyobjectivebasicform = Ext.getCmp('strategyobjectivebasicform');
        var strategyobjectivekpiset = Ext.getCmp('strategyobjectivekpiset');
        var strategyobjectivewarningset = Ext.getCmp('strategyobjectivewarningset');
        //初始化cardpanel按钮为可用状态
        strategyobjectivetab.strategyobjectivecardpanel.setAllBtnStatus(false);
        
        me.disablePanel(false);
        
        //设置战略目标主面板为右侧显示
        strategyobjectivebasicform.initParam(paramObj);
        strategyobjectivekpiset.initParam(paramObj);
        strategyobjectivewarningset.initParam(paramObj);
        strategyobjectivetab.initParam(paramObj);

        strategyobjectivebasicform.clearFormData();

        strategyobjectivetab.setActiveTab(2);

        Ext.getCmp('strategyobjectivecardpanel').setFirstItemFoucs();

        strategyobjectivebasicform.reLoadData();
        strategyobjectivekpiset.reLoadData();
        strategyobjectivewarningset.reLoadData();
    },
    initComponent: function () {
        var me = this;
        me.navNode = {};
        Ext.applyIf(me, {
            listeners: {
                itemclick: me.onTreepanelItemClick,
                load: function (store, records) {
                }

            },
            viewConfig: {
                listeners: {
                    /**
                     * 右键监听事件
                     */
                    itemcontextmenu: function (view, rec, node, index, e) {
                        e.stopEvent();
                        var menu = me.contextItemMenuFun(view, rec, node, index, e);
                        if (menu) {
                            menu.showAt(e.getPoint());
                        }
                        return false;
                    }
                }
            }
        });

        me.callParent(arguments);
    },
    disablePanel:function(status){
    	Ext.getCmp('strategyobjectivetab').strategyobjectivekpigrid.setDisabled(status);
    	Ext.getCmp('strategyobjectivetab').strategyobjectivecardpanel.setDisabled(status);
    	Ext.getCmp('strategyobjectivetab').chartanalysis.setDisabled(status);
    },
    /**
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick: function () {
        var me = this;
        var selectedNode;
        var strategyobjectivemainpanel = Ext.getCmp('strategyobjectivemainpanel');
        var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
            me.currentNode = selectedNode;
        }
        if (selectedNode == null) {
            var firstNode = me.getRootNode().firstChild;
            if(null!=firstNode){
            	me.getSelectionModel().select(firstNode);
            	me.currentNode = firstNode;
                strategyobjectivemainpanel.reLoadData(firstNode); //默认选中首节点
            }else{
            	me.disablePanel(true);
            }
        } else {
        	me.currentNode = selectedNode;
            strategyobjectivemainpanel.reLoadData(selectedNode); //当前选中节点
        }
    },
    findNode:function(root,nodeid){
    	var me = this;
    	var childnodes = root.childNodes;//获取根节点的子节点
        for(var i=0; i < childnodes.length; i++){
           var node = childnodes[i];
           if(node.data.id == nodeid)
           {
        	   me.navNode = node;
           }
           if(node.hasChildNodes()){
        	   me.findNode(node,nodeid);//递归调用
           }
        }
        return me.navNode;
    },
    selectedNodeClick:function(selectedNode){
    	 var me = this;
    	 var metrictreecardpanel = Ext.getCmp('metrictreecardpanel');
         var strategyobjectivemainpanel = Ext.getCmp('strategyobjectivemainpanel');
         var activeItem = metrictreecardpanel.getActiveItem();
         if (activeItem.id == 'strategyobjectivetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡strategyobjectivemainpanel主面板在右侧
             Ext.getCmp('metriccentercardpanel').setActiveItem(strategyobjectivemainpanel);
         }
        if (selectedNode != null) {
        	me.getSelectionModel().select(selectedNode);
        	strategyobjectivemainpanel.reLoadData(selectedNode); //当前选中节点
        }
    },
    onTreepanelItemClick: function (tablepanel, record, item, index, e, options) {
    	var me = this;
    	var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
            me.currentNode = selectedNode;
        }
        var metrictreecardpanel = Ext.getCmp('metrictreecardpanel');
        var strategyobjectivemainpanel = Ext.getCmp('strategyobjectivemainpanel');
        var activeItem = metrictreecardpanel.getActiveItem();
        if (activeItem.id == 'strategyobjectivetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡strategyobjectivemainpanel主面板在右侧
            Ext.getCmp('metriccentercardpanel').setActiveItem(strategyobjectivemainpanel);
        }
        me.disablePanel(false);
        strategyobjectivemainpanel.reLoadData(record);
    }

});