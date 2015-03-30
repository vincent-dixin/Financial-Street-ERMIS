<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>

var kpi_category_tree_view = (function () {
	
	Ext.define('FHD.kpi.category.tree.view', {
		
		currentNode:'',
		
		/**
		 * 记分卡树右键菜单函数
		 */
		contextItemMenuFun: function (view, rec, node, index, e) {
	        var me = this;
	        var id = rec.data.id;
	        var name = rec.data.text;
	        var menu = Ext.create('Ext.menu.Menu', {
	            margin: '0 0 10 0',
	            items: []
	        });
	        var subLevel = {
	            iconCls: 'icon-add',
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.subLevel'),
	            handler: function () {
	            	kpi_category_tree_view.currentNode = rec;
	            	if(!kpi_category_tree_view.currentNode.isExpanded()&&!kpi_category_tree_view.currentNode.isLeaf()){
		            	kpi_category_tree_view.currentNode.expand();
		            	/*setTimeout(function(){
							kpi_category_tree_view.tree.getSelectionModel().select(kpi_category_tree_view.currentNode);
						}, 400);*/
	            	}
	                me.sublevelHandler(id, name);//添加下级菜单
	            }
	        };
	        menu.add(subLevel);
	        if (index != 0) {
	            var sameLevel = {
	                iconCls: 'icon-add',
	                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
	                handler: function () {
	                    kpi_category_tree_view.currentNode = rec.parentNode;
	                    kpi_category_tree_view.currentNode.expand();
	                    me.sameLevelHandler(rec);//添加同级菜单
	                    
	                }
	            };
	            menu.add(sameLevel);
	        }
	        menu.add('-');
	
	        if (index != 0) {
	            var delmenu = {
	                iconCls: 'icon-delete-icon',
	                text: FHD.locale.get('fhd.strategymap.strategymapmgr.delete'),
	                handler: function () {
	                	kpi_category_tree_view.currentNode = rec.parentNode;
	                    me.deleteHandler(id,rec);//删除菜单
	                }
	            }
	            menu.add(delmenu);
	            menu.add('-');
	        }
	
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
	     * 刷新函数
	     */
	    refreshTree: function () {
	        var me = this;
	        me.tree.getStore().load();
	    },
	    
	    /**
	     * 添加下级函数
	     */
	    sublevelHandler: function (id, name) {
	        var me = this;
	        var rightUrl = me.url + "categoryparentid=" + id + "&categoryid=undefined" + "&editflag=false" + "&categoryparentname=" + encodeURIComponent(name);
	        fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	    },
		/**
		 * 添加同级函数
		 */	    
	    sameLevelHandler: function (node) {
	        var me = this;
	        if (node.parentNode.data.id == "category_root") {
	            var rightUrl = me.url + "categoryparentid=" + "" + "&editflag=false" + "&categoryid=undefined" + "&categoryparentname=" + encodeURIComponent(FHD.locale.get('fhd.kpi.categoryroot'));
	            fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	        } else {
	            FHD.ajax({
	                params: {
	                    "id": node.data.id
	                },
	                url: me.parentUrl,
	                callback: function (ret) {
	                    var rightUrl = me.url + "categoryparentid=" + ret.parentid + "&categoryid=undefined" + "&editflag=false" + "&categoryparentname=" + encodeURIComponent(ret.parentname);
	                    fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	                }
	            });
	        }
	    },
	    /**
	     * 删除函数
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
	                        url: __ctxPath + '/kpi/category/removecategory.f',
	                        callback: function (ret) {
	                            if (ret && ret.result) {
	                                if (ret.result == "cascade") {
	                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.categorycasdes"));
	                                } else if (ret.result == "success") {
	                                    //me.refreshTree();
	                                	var parentnode = node.parentNode;
	                                	kpi_category_tree_view.currentNode.removeChild(node);
	                                	if(!kpi_category_tree_view.currentNode.hasChildNodes()){
	                                		var oldnode = kpi_category_tree_view.currentNode;
	                                		var newnode = kpi_category_tree_view.currentNode;
	                                		newnode.data.leaf = true;
	                                		kpi_category_tree_view.currentNode.parentNode.replaceChild(newnode,oldnode);
	                                	}
	                                	kpi_category_tree_view.tree.getSelectionModel().select(parentnode);
	                                	kpi_category_tree_view.clickFunction(parentnode);
	                                }
	
	                            }
	
	                        }
	                    });
	                }
	            }
	        });
	    },
	    /**
	     * 点击节点时函数
	     */
		clickFunction: function (node) {
	        var me = this;
	        if (node.parentNode == null) return;//如果是根节点直接返回
	        kpi_category_tree_view.currentNode = node.parentNode;
	        var id = node.data.id;
	        var name = node.data.text;
	        if (node.parentNode.data.id == "category_root") {//如果父节点是根节点情况,也就是一级节点
	            var rightUrl = me.url + "categoryparentid=" + "" + "&editflag=true" + "&categoryid=" + id + "&categoryparentname=" + encodeURIComponent(FHD.locale.get('fhd.kpi.categoryroot')) + "&categoryname=" + encodeURIComponent(name);
	            fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	        } else {
	            FHD.ajax({
	                params: {
	                    "id": id
	                },
	                url: me.parentUrl,
	                callback: function (ret) {
	                    var rightUrl = me.url + "categoryparentid=" + ret.parentid + "&editflag=true" + "&categoryid=" + id + "&categoryparentname=" + encodeURIComponent(ret.parentname) + "&categoryname=" + encodeURIComponent(name);
	                    fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	                }
	            });
	        }
	    },
	    /**
	     * 页面初始化方法,初始化了记分卡树
	     */
		init: function () {
           var me = this;
           me.url =  __ctxPath + "/pages/kpi/kpi/opt/kpicategoryedit.jsp?",
           me.parentUrl =  __ctxPath + '/kpi/category/findparentbyid.f';
           //记分卡树定义
           me.tree = Ext.create('FHD.ux.TreePanel', {
           		index:3,
	            animate: false,
	            autoScroll:true,
	            //title: FHD.locale.get('fhd.kpi.categoryroot'),
                //iconCls: ' icon-ibm-icon-scorecards',
                width: 265,
                maxWidth: 300,
                split: true,
                collapsible: false,
                border: false,
                multiSelect: true,
                rowLines: false,
                singleExpand: false,
                checked: false,
                url: __ctxPath + "/kpi/category/categorytreeloader.f", //调用后台url
                rootVisible: true,
                height: FHD.getCenterPanelHeight() - 5,
	            root: {//树的根节点
	                id: "category_root",
	                text: FHD.locale.get('fhd.kpi.categoryroot'),
	                dbid: "category_root",
	                leaf: false,
	                code: "category",
	                type: "kpi_category"
	                ,expanded: true
	            },
	            /**
	             * 当点击记分卡的panel时触发
	             */
	            clicked:function(){
	            	kpiActivePanelflag = 'kpicategory';
			    	var selectedNode;
			        var tree = kpi_category_tree_view.tree;
			        var nodeItems = tree.getSelectionModel().selected.items;
			        if (nodeItems.length > 0) {
			            selectedNode = nodeItems[0];
			        }
			        if(selectedNode==null){
			        	var rightUrl = kpi_category_tree_view.url + "categoryparentid=" + "" + "&editflag=false" + "&categoryid=undefined" + "&categoryparentname=" + encodeURIComponent(FHD.locale.get('fhd.kpi.categoryroot'));
			            fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
			        }else{
			        	kpi_category_tree_view.clickFunction(selectedNode);
			        }
			    },
	            listeners: {
	            	 /**
		             * 树节点的点击事件
		             */
	                itemclick: function (node, record, item) {
	                    me.clickFunction(record);
	                },
	                /**
	                 * 树加载数据时的事件
	                 */
	                load: function (store, records) { //默认选择首节点
	                    var tree = kpi_category_tree_view.tree;
	                    var rootNode = tree.getRootNode();
	                    if (rootNode.childNodes.length > 0) {
	                        tree.getSelectionModel().select(rootNode.firstChild);
	                    }
	                },
	                /**
	                 * 记分卡面板展开前的事件
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
	                	 * 右键菜单的监听事件
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
           
           
           
           
       }
		
		
		
	});
	
	/**
	 * 定义页面view变量
	 */
	var kpi_category_tree_view = Ext.create('FHD.kpi.category.tree.view');
	return kpi_category_tree_view;
	
	
	
})();
/**
 * 初始化页面js变量
 */
kpi_category_tree_view.init();
