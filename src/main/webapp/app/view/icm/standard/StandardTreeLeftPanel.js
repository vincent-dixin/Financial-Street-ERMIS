/**
 * 
 */
Ext.define('FHD.view.icm.standard.StandardTreeLeftPanel', {
	extend : 'FHD.ux.TreePanel',
	alias : 'widget.standardtreeleftpanel',
	
	requires: [
        'FHD.view.icm.standard.StandardList'
    ],
	multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,
    paramGridObj : {},
    extraParams :{
    	companyId : __user.companyId
    },
    selectId:null,
    url: __ctxPath + '/standard/standardTree/findStandardTreeLoader.do',
    rootVisible: true,
	root:{
		text : '内控标准库',
		id : '',
		iconCls: 'icon-note',
		expanded : true
	},
	//border:false,
	
	initComponent: function() {
    	var me = this;
    	Ext.applyIf(me, {
           listeners: {
                itemclick: me.currentNodeClick,
                load: function (store, records) { //默认选择首节点
                    /*var rootNode = me.getRootNode();
                    if (rootNode.childNodes.length > 0) {
                        me.getSelectionModel().select(rootNode.firstChild);//默认选择首节点
                        Ext.getCmp('scorecardmainpanel').reLoadData(rootNode.firstChild);//加载首节点数据
                    }*/
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
     	var standardmainpanel = me.up('standardmanage').standardMainPanel;
    	var standardtab = standardmainpanel.standardtab;
    	standardtab.reloadData();
    },
	/**
     * 删除函数
     */
    deleteHandler: function (id,node) {
        var me = this;
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: '本标准的子节点及相关的内控要求也一并被删除，您确定要删除吗？',
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
                    FHD.ajax({
                        params: {
                           "standardIds" : id
                        },
                        url : __ctxPath+ '/standard/standardTree/removeStandards.do',
                        callback: function (ret) {
                            if (ret && ret.result) {
                                if (ret.result == "cascade") {
                                	alert(ret.result);
                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.categorycasdes"));
                                } else if (ret.result == "success") {
                                    me.deleteNodeAfterSelectNode(node);
                                }

                            }

                        }
                    });
                }
            }
        });
    },
    /**
     * 
     * 添加同级菜单
     */
    levelHandler : function (id,rec) {
   		var me = this;
   		var standardmainpanel = me.up('standardmanage').standardMainPanel;
    	var standardtab = standardmainpanel.standardtab;
    	var standardedit = standardtab.standardedit;
        paramObj = {};
        paramObj.nodeId = rec.parentNode.data.id;
        paramObj.controlType = 'addTree';
        paramObj.addType = 'addNow';
        paramObj.isLeaf = false;
        paramObj.standardId = '';
		paramObj.idSeq=rec.data.idSeq;
        //设置参数
		standardedit.getForm().reset();
        standardedit.initParam(paramObj);
        standardtab.setActiveTab(standardedit);
		standardedit.getForm().setValues({'upName':rec.parentNode.data.text});
    },
    /**
     * 添加下级函数
     */
    sublevelHandler: function (id, rec) {
        var me = this;
        var standardmainpanel = me.up('standardmanage').standardMainPanel;
    	var standardtab = standardmainpanel.standardtab;
    	var standardedit = standardtab.standardedit;
        paramObj = {};
        paramObj.nodeId = id;
        paramObj.controlType = 'addTree';
        paramObj.addType = 'addNext';
        paramObj.isLeaf = true;
        paramObj.standardId = '';
        //设置参数
        standardedit.getForm().reset();
		paramObj.idSeq=rec.data.idSeq;
        standardedit.initParam(paramObj);
        standardtab.setActiveTab(standardedit);
		standardedit.getForm().setValues({'upName':rec.data.text});
	//	Ext.getCmp('standardCreateCodeButtonId').setDisabled(false); FHD_icm_standard_standardEdit.addType='addNow';
        
    },
    /**
     * 刷新函数
     */
    refreshTree: function () {
        var me = this;
        me.getStore().load();
    },
	contextItemMenuFun:function(view, rec, node, index, e){
    	var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });
       //添加下级菜单
        var subLevel = {
            iconCls: 'icon-add',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.subLevel'),
            handler: function () {
            	me.currentNode = rec;
            	if(!me.currentNode.isExpanded()&&!me.currentNode.isLeaf()){
	            	me.currentNode.expand();
            	}
                me.sublevelHandler(id, rec);//添加下级菜单
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
                    me.levelHandler(id, rec);//添加同级菜单
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
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick:function(){
    	var me = this;
    	var standardmainpanel = me.up('standardmanage').standardMainPanel;
    	var standardtab = standardmainpanel.standardtab;
    	var standardlist = standardtab.standardlist;
    	var standardedit = standardtab.standardedit;
    	//alert(standardmainpstandardlistanel);
    	var paramObj = {};
		paramObj.controlType='addTree';
		standardedit.initParam(paramObj);
    	var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            me.selectId = nodeItems[0].data.id;
        }
        if(me.selectId==''){
        	var firstNode = me.getRootNode().firstChild;
        	if(null!=firstNode){
        		me.getSelectionModel().select(firstNode);
            	//standardmainpanel.reloadData(firstNode);//默认选中首节点
        		standardlist.reloadData();//当前选中节点	
        	}
        }else{
        	var form=standardedit.getForm();
			form.reset();
			form.setValues({'upName':nodeItems[0].parentNode.data.text});
			//Ext.getCmp('standardedit').controlType='listEdit';
			form.load({
                  url: __ctxPath + '/standard/standardTree/findStandardByIdToJson.do',
                  params: {
                	  standardId:me.selectId
                  },
                  success: function (form, action) {
                      return true;
                  },
                  failure: function (form, action) {
                  }
              });
        	standardtab.setActiveItem(standardlist);
        	standardlist.reloadData();//当前选中节点
        	//standardedit.reloadData(selectedNode);//当前选中节点
        }
    }
});