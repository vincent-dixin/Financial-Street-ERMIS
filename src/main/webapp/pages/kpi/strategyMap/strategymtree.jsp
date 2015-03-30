<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>


var fhd_kpi_sm_tree_view = (function () { 
	/**
	 * 页面view类
	 */
	Ext.define('FHD.kpi.sm.tree.view', {
		
		currentNode:'',
		
		/**
		 * 添加同级事件函数
		 */
		 smSameLevelHandler: function (id) {
	        var me = this;
	        FHD.ajax({
	            params: {
	                "id": id
	            },
	            url: me.findparentidUrl,
	            callback: function (ret) {
	                fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl + "parentid=" + ret.parentid + "&id=undefined" + "&editflag=false" + "&parentname=" + encodeURIComponent(ret.parentname));
	            }
	        });
    	 },
    	 /**
    	  * 添加下级事件函数
    	  */
   		 smSubLevelHandler: function (id, name) {
    		var me = this;
        	fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl + "parentid=" + id + "&editflag=false" + "&id=undefined" + "&parentname=" + encodeURIComponent(name));
    	 },
    	 /**
    	  * 删除节点函数
    	  */
   		 smDeleteHandler: function (id, type,node) {
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
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.haschilds'));
	                            } else {
	                                if (type == "sm") {
	                                    //me.refreshTree();
	                                	var parentnode = node.parentNode;
	                                	fhd_kpi_sm_tree_view.currentNode.removeChild(node);
	                                	if(!fhd_kpi_sm_tree_view.currentNode.hasChildNodes()){
	                                		var oldnode = fhd_kpi_sm_tree_view.currentNode;
	                                		var newnode = fhd_kpi_sm_tree_view.currentNode;
	                                		newnode.data.leaf = true;
	                                		fhd_kpi_sm_tree_view.currentNode.parentNode.replaceChild(newnode,oldnode);
	                                	}
	                                	fhd_kpi_sm_tree_view.tree.getSelectionModel().select(parentnode);
	                                	fhd_kpi_sm_tree_view.smEditHandler(parentnode);
	                                	
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
   	    refreshTree:function(obj){
    		fhd_kpi_sm_tree_view.tree.getStore().load();
    	},
    	/**
    	 * 编辑节点函数
    	 */
		smEditHandler: function (node) {
	        var me = this;
	        if (node.parentNode == null) return;//如果是根节点直接返回
	        var id = node.data.id;
	        var name = node.data.text;
	        FHD.ajax({
	            params: {
	                "id": id
	            },
	            url: me.findparentidUrl,
	            callback: function (ret) {
	                fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl + "parentid=" + ret.parentid + "&editflag=true" + "&id=" + id + "&parentname=" + encodeURIComponent(ret.parentname) + "&smname=" + encodeURIComponent(name));
	            }
	        });
	    },
	    /**
	     * 树右键菜单函数
	     */
	    smTreeContextFun: function (view, rec, node, index, e) {
	        var me = fhd_kpi_sm_tree_view;
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
	            	fhd_kpi_sm_tree_view.currentNode = rec;
            		if(!fhd_kpi_sm_tree_view.currentNode.isExpanded()&&!fhd_kpi_sm_tree_view.currentNode.isLeaf()){
	            		fhd_kpi_sm_tree_view.currentNode.expand();
            		}
	                me.smSubLevelHandler(id, name);//添加下级函数
	            }
	        };
	        menu.add(subLevel);
	        
	        if (index != 0) {
	            /*添加同级*/
	            var sameLevel = {
	                iconCls: 'icon-add',
	                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
	                handler: function () {
	                	fhd_kpi_sm_tree_view.currentNode = rec.parentNode;
	                    fhd_kpi_sm_tree_view.currentNode.expand();
	                    me.smSameLevelHandler(id);//添加同级函数
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
	                	fhd_kpi_sm_tree_view.currentNode = rec.parentNode;
	                    me.smDeleteHandler(id, "sm",rec);//删除节点函数
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
	                me.refreshTree();//刷新树函数
	            }
	        }
	        menu.add(refresh);
	        return menu;

    	},
    	/**
    	 * 初始化页面变量函数
    	 */
		init:function(){
			var me = this;
			me.editurl = __ctxPath + "/pages/kpi/strategyMap/smedit.jsp?";
			me.findparentidUrl = __ctxPath + '/kpi/kpistrategymap/findparentbyid.f';
			/**
			 * 定义树对象
			 */
			me.tree = Ext.create('FHD.ux.TreePanel', {
				//title:FHD.locale.get('fhd.strategymap.strategymapmgr.target'),
				//iconCls: 'icon-strategy',
				autoScroll:true,
				index:2,
	            animate: false,
	            rootVisible: true,
	            width: 265,
	            maxWidth: 300,
	            split: true,
	            collapsible: false,
	            border: false,
	            multiSelect: true,
	            rowLines: false,
	            singleExpand: false,
	            checked: false,
	            url: __ctxPath+ '/kpi/KpiStrategyMapTree/treeloader', 
	            height: FHD.getCenterPanelHeight() - 5,
	            /**
	             * 定义树跟节点
	             */
	            root: {
	                id: "sm_root",
	                text: FHD.locale.get('fhd.sm.strategymaps'),
	                dbid: "sm_root",
	                leaf: false,
	                code: "sm",
	                type: "sm"
	                ,expanded:true
	            },
	            /**
	             * 点击目标面板时事件
	             */
	            clicked:function(){
	            	kpiActivePanelflag = 'sm';
			    	var selectedNode;
			    	var tree = fhd_kpi_sm_tree_view.tree;
			    	var nodeItems = tree.getSelectionModel().selected.items;
			        if (nodeItems.length > 0) {
			            selectedNode = nodeItems[0];
			        }
			        if(selectedNode==null){
			        	var editurl = fhd_kpi_sm_tree_view.editurl;
			        	fhd_kpi_kpiaccordion_view.initRightPanel(editurl + "parentid=sm_root"  + "&editflag=false" + "&id=undefined" + "&parentname=" + encodeURIComponent(FHD.locale.get('fhd.sm.strategymaps')));
			        }else{
			        	fhd_kpi_sm_tree_view.smEditHandler(selectedNode);
			        }
    			},
    			/**
    			 * 添加监听事件
    			 */
	            listeners: {
	            	/**
	            	 * 点击树节点函数
	            	 */
	                itemclick: function (node, record, item) {
	                    me.smEditHandler(record);
	                },
	                /**
	                 * 树load时事件
	                 */
	               load:function(store,records){//默认选择首节点
	            	    var tree = fhd_kpi_sm_tree_view.tree;
	                	var rootNode = tree.getRootNode();
	                	if(rootNode.childNodes.length>0){
	                		tree.getSelectionModel().select(rootNode.firstChild);
	                	}
	                },
	                /**
	                 * 展开战略目标面板事件
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
	                	 * 右键监听事件
	                	 */
	                    itemcontextmenu: function (view, rec, node, index, e) {
	                        e.stopEvent();
	                        var menu = me.smTreeContextFun(view, rec, node, index, e);
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
	 var fhd_kpi_sm_tree_view = Ext.create('FHD.kpi.sm.tree.view');
     return fhd_kpi_sm_tree_view;
	


})();
/**
 * 初始化页面js变量
 */
fhd_kpi_sm_tree_view.init();
