
Ext.define('FHD.view.kpi.kpitype.KpiTypeTree',{
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.kpitypetree',
    
    
    multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,
    url: __ctxPath + "/kpi/kpi/kpitypetreeloader.f", //调用后台url
    rootVisible: true,
    root: {
        "id": "type_1",
        "text": FHD.locale.get('fhd.kpi.kpi.form.etype'),
        "dbid": "type_1",
        "leaf": false,
        "code": "zblx",
        "type": "kpi_type",
        "expanded": true,
        'iconCls':'icon-ibm-icon-metrictypes'
    },
    
    contextItemMenuFun:function(view, rec, node, index, e){
    	var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });
        if (index == 0) {
            var subLevel = {
                iconCls: 'icon-add',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.subLevel'),
                handler: function () {
            		me.currentNode = rec;
	            	if(!me.currentNode.isExpanded()&&!me.currentNode.isLeaf()){
		            	me.currentNode.expand();
	            	}
	            	
                    me.levelHandler(id);//添加下级菜单
                }
            };
            menu.add(subLevel);
        }
        if (index != 0) {
            /*添加同级*/
            var sameLevel = {
                iconCls: 'icon-add',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
                handler: function () {
                	me.currentNode = rec.parentNode;
                    me.currentNode.expand();
                    me.levelHandler(id);//添加同级菜单
                }
            };
            menu.add(sameLevel);
        }
        menu.add('-');
        if (index != 0) {
            /*删除菜单*/
            var delmenu = {
                iconCls: 'icon-delete-icon',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.delete'),
                handler: function () {
                	me.currentNode = rec.parentNode;
                    me.deleteHandler(id,rec);//删除菜单
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
                me.refreshTree();//刷新菜单
            }
        }
        menu.add(refresh);
        return menu;
    },
    
    /**
     *刷新树函数 
     */
    refreshTree: function (obj) {
    	var me = this;
        me.getStore().load();
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
     	Ext.getCmp('kpitypemainpanel').reLoadData(parentnode);
     	if(!me.getRootNode().hasChildNodes()){
     		me.disablePanel(true);
        }
    },
    /**
     * 删除事件
     */
    deleteHandler: function (id,node) {
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
                        url: __ctxPath + '/kpi/kpi/removekpitype.f',
                        callback: function (ret) {
                            if (ret && !ret.result) {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.kpitypecasdes"));
                            } else {
                                me.deleteNodeAfterSelectNode(node);
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                            }
                        }
                    });
                }
            }
        });
     },
    
    levelHandler:function(){
    	var me = this;
        var paramObj = {};
        paramObj.editflag = false;
        //设置指标类型 主面板为右侧显示
        Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('kpitypemainpanel'));
        var kpitypetab = Ext.getCmp('kpitypetab');
        var kpitypebasicform = Ext.getCmp('kpitypebasicform');
        var kpitypegatherform = Ext.getCmp('kpitypegatherform');
        
        me.disablePanel(false);
        
        kpitypetab.initParamObj(paramObj);
        
        kpitypebasicform.clearFormData();
        kpitypegatherform.clearFormData();
        
        //load预警列表数据;
        Ext.getCmp('kpitypewarningset').reLoadData();
        
        kpitypetab.setActiveTab(1);
        
        Ext.getCmp('kpitypecardpanel').setFirstItemFoucs(true);
        
        //赋默认值
        kpitypebasicform.initFormData();
        kpitypegatherform.initFormData();
     		
    },
    
    initComponent: function() {
    	var me = this;
    	me.navNode = {};
    	Ext.applyIf(me, {
        	listeners: {
        		itemclick: me.onTreepanelItemClick,
		        load:function(store,records){
		        	//me.treeload(store,records);
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
    /**
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick: function () {
        var me = this;
        var selectedNode;
        var kpitypemainpanel = Ext.getCmp('kpitypemainpanel');
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
	            kpitypemainpanel.reLoadData(firstNode); //默认选中首节点
            }else{
            	 me.disablePanel(true);
            }
        } else {
        	me.currentNode = selectedNode;
            kpitypemainpanel.reLoadData(selectedNode); //当前选中节点
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
    	var kpitypemainpanel = Ext.getCmp('kpitypemainpanel');
        var metrictreecardpanel = Ext.getCmp('metrictreecardpanel');
        var activeItem = metrictreecardpanel.getActiveItem();
        if (activeItem.id == 'kpitypetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡kpitypemainpanel主面板在右侧
            Ext.getCmp('metriccentercardpanel').setActiveItem(kpitypemainpanel);
        }
        if (selectedNode != null) {
        	me.getSelectionModel().select(selectedNode);
            kpitypemainpanel.reLoadData(selectedNode); //当前选中节点
        }
    },
    disablePanel:function(status){
    	Ext.getCmp('kpitypetab').kpitypekpigrid.setDisabled(status);
    	Ext.getCmp('kpitypetab').kpitypecardpanel.setDisabled(status);
    },
    onTreepanelItemClick: function(tablepanel, record, item, index, e, options) {
    	var me = this;
    	var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
            me.currentNode = selectedNode;
        }
    	var kpitypemainpanel = Ext.getCmp('kpitypemainpanel');
        var metrictreecardpanel = Ext.getCmp('metrictreecardpanel');
        var activeItem = metrictreecardpanel.getActiveItem();
        if (activeItem.id == 'kpitypetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡kpitypemainpanel主面板在右侧
            Ext.getCmp('metriccentercardpanel').setActiveItem(kpitypemainpanel);
        }
        me.disablePanel(false);
        kpitypemainpanel.reLoadData(record);
    }

});