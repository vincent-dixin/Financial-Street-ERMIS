<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>

var fhd_kpi_kpitypetree_view = (function () { 


	Ext.define('FHD.kpi.kpitype.tree.view', {
		/**
		 * 点击节点事件
		 */
		clickFunction: function (node) {
            var me = this;
            if (node.parentNode == null) return;
            var id = node.data.id;
            var name = node.data.text;
            fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl + "editflag=true" + "&id=" + id + "&name=" + encodeURIComponent(name));
        },
        /**
         * 删除事件
         */
        deleteHandler: function (id,node) {
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
                                    //fhd_kpi_kpitypetree_view.refreshTree();
                                    var parentnode = node.parentNode;
	                                	fhd_kpi_kpitypetree_view.currentNode.removeChild(node);
	                                	if(!fhd_kpi_kpitypetree_view.currentNode.hasChildNodes()){
	                                		var oldnode = fhd_kpi_kpitypetree_view.currentNode;
	                                		var newnode = fhd_kpi_kpitypetree_view.currentNode;
	                                		newnode.data.leaf = true;
	                                		fhd_kpi_kpitypetree_view.currentNode.parentNode.replaceChild(newnode,oldnode);
	                                	}
	                                	fhd_kpi_kpitypetree_view.tree.getSelectionModel().select(parentnode);
	                                	fhd_kpi_kpitypetree_view.clickFunction(parentnode);
                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                }
                            }
                        });
                    }
                }
            });
         },
         /**
          * 刷新树函数
          */
         refreshTree: function () {
            var me = this;
            me.tree.getStore().load();
         },
         /**
          * 添加类型事件
          */
         levelHandler: function (id) {
            var me = this;
            fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl + "&editflag=false");
         },
         /**
		 * 指标类型树右键菜单函数
		 */
         contextItemMenuFun: function (view, rec, node, index, e) {
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
                    	fhd_kpi_kpitypetree_view.currentNode = rec;
	            		if(!fhd_kpi_kpitypetree_view.currentNode.isExpanded()&&!fhd_kpi_kpitypetree_view.currentNode.isLeaf()){
		            		fhd_kpi_kpitypetree_view.currentNode.expand();
	            		}
                        fhd_kpi_kpitypetree_view.levelHandler(id);//添加下级菜单
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
                    	fhd_kpi_kpitypetree_view.currentNode = rec.parentNode;
	                    fhd_kpi_kpitypetree_view.currentNode.expand();
                        fhd_kpi_kpitypetree_view.levelHandler(id);//添加同级菜单
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
                    	fhd_kpi_kpitypetree_view.currentNode = rec.parentNode;
                        fhd_kpi_kpitypetree_view.deleteHandler(id,rec);//删除菜单
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
                    fhd_kpi_kpitypetree_view.refreshTree();//刷新菜单
                }
            }
            menu.add(refresh);
            return menu;
        },
        /**
         * 初始化页面函数
         */            
		init: function () {
           var me = this;
           me.editurl = __ctxPath + "/pages/kpi/kpi/opt/kpitypeedit.jsp?";
           me.kpitypetreeUrl = __ctxPath + "/kpi/kpi/kpitypetreeloader.f";
           /**
            * 定义指标类型树
            */
           me.tree = Ext.create('FHD.ux.TreePanel', {
           			index:4,
           			autoScroll:true,
           			//title:FHD.locale.get('fhd.kpi.kpi.form.etype'),
           			//iconCls: ' icon-ibm-icon-metrictypes',
                    clickFunction: me.clickFunction,
                    contextItemMenuFc: me.contextItemMenuFun,
                    animate: false,
                    rootVisible: false,
                    width: 265,
                    maxWidth: 300,
                    split: true,
                    collapsible: false,
                    border: false,
                    multiSelect: true,
                    rowLines: false,
                    singleExpand: false,
                    checked: false,
                    url: me.kpitypetreeUrl, //调用后台url
                    rootVisible: true,
                    height: FHD.getCenterPanelHeight() - 5,
                    //指标类型树根
                    root: {
                        id: "type_1",
                        text: FHD.locale.get('fhd.kpi.kpi.form.etype'),
                        dbid: "type_1",
                        leaf: false,
                        code: "zblx",
                        type: "kpi_type"
                        ,expanded: true
                    },
                    /**
                     * 点击指标类型面板事件
                     */
                    clicked: function () {
                    	kpiActivePanelflag = 'kpitype';
                        var url;
                        var selectedNode;
                        var tree = fhd_kpi_kpitypetree_view.tree;
                        var nodeItems = tree.getSelectionModel().selected.items;
                        if (nodeItems.length > 0) {
                            selectedNode = nodeItems[0];
                        }
                        if (selectedNode) {
                            var data = selectedNode.data;
                            url = me.editurl + "editflag=true" + "&id=" + data.id + "&name=" + encodeURIComponent(data.text);
                        } else {
                            url = me.editurl + "&editflag=false";
                        }
                        fhd_kpi_kpiaccordion_view.initRightPanel(url);
                    },
                    //添加监听事件
                    listeners: {
                    	/**
                    	 * 点击树节点事件
                    	 */
                        itemclick: function (node, record, item) {
                            fhd_kpi_kpitypetree_view.clickFunction(record);
                        },
                        /**
                         * 树加载后台数据事件
                         */
                    	load: function (store, records) { //默认选择首节点
                            var tree = fhd_kpi_kpitypetree_view.tree;
                            var rootNode = tree.getRootNode();
                            if (rootNode.childNodes.length > 0) {
                                tree.getSelectionModel().select(rootNode.firstChild);
                            }
                        },
                        /**
                         * 指标类型面板展开事件
                         */
                        beforeexpand: function (p) {
		                     fhd_kpi_kpiaccordion_view.accordion.remove(p, false);
		                     fhd_kpi_kpiaccordion_view.accordion.insert(0, p);
		                     panelSort(p);
		                     if (p.clicked) {
		                         p.clicked();
		                     }
                 	    }
                    },
                    viewConfig: {
                        listeners: {
                        	/**
                        	 * 右键菜单监听事件
                        	 */
                            itemcontextmenu: function (view, rec, node, index, e) {
                                e.stopEvent();
                                var menu = fhd_kpi_kpitypetree_view.tree.contextItemMenuFc(view, rec, node, index, e);
                                if (menu) {
                                    menu.showAt(e.getPoint());
                                }
                                return false;
                            }
                        }
                    }

           });
                        
        }
        
        
	
	
	
	
	
	
	});
	/**
	 * 定义页面view变量
	 */
	var fhd_kpi_kpitypetree_view = Ext.create('FHD.kpi.kpitype.tree.view');
    return fhd_kpi_kpitypetree_view;


})();
/**
 * 初始化页面js变量
 */
fhd_kpi_kpitypetree_view.init();