/**
 * 规章制度树
 * 
 * @author 元杰
 */
Ext.define('FHD.view.icm.rule.RuleTree',{
	extend: 'FHD.ux.TreePanel',
    alias: 'widget.ruletree',
	url : __ctxPath + '/rule/ruleTree/ruletreeloader.f',
    
	root: {
        id: __user.companyId,
        canChecked: false,
        text: '规章制度库',
        dbid: 'rule_root',
        leaf: false,
        code: 'category',
        type: '',
        expanded: true
    },
    extraParams: {
    	node: '50000',
    	canChecked: false
    },
	autoScroll:true,
    animate: false,
    rootVisible: true,
    collapsible: true,
    border: false,
    multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,
    contextItemMenuFun: function (view, rec, node, index, e) {
        var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
//        alert('me' + me + 'id' + id + 'name' + name);
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });
        //添加下级菜单
        var subLevel = {
            iconCls: 'icon-add',
            text : '添加下级',
            handler: function () {
            	me.currentNode = rec;
            	if(!me.currentNode.isExpanded()&&!me.currentNode.isLeaf()){
	            	me.currentNode.expand();
            	}
                me.addNextLevel(id, name);//添加下级菜单
            }
	    };
	    menu.add(subLevel);
	    
	    //添加同级
	    if(rec.data.id != __user.companyId){
		    var addNowLevel = {
				iconCls : 'icon-add',
				text : '添加同级',
				handler : function () {
	                me.addSameLevel(rec);
	            }
			};
	    	menu.add(addNowLevel);
	    }
	    //非根节点才有删除菜单
	    if(rec.data.id != __user.companyId){
		    /*删除*/
			var deleteRela = {
				iconCls : 'icon-delete-icon',
				text : '删除',
				handler : function() {
					var makeSureDelete;
					if(node.hasChildNodes()){
						makeSureDelete = '存在下级流程，确定要删除吗？';
					}else{
						makeSureDelete = FHD.locale.get('fhd.common.delete');
					}
					var processNode = me.treeRightNode;
			        var deleId = rec.data.id;
					Ext.MessageBox.show({
						title : FHD.locale.get('fhd.common.delete'),
						width : 260,
						msg : makeSureDelete,
						buttons : Ext.MessageBox.YESNO,
						icon : Ext.MessageBox.QUESTION,
						fn : function(btn) {
							if (btn == 'yes') {
								FHD.ajax({
								   params : {
								   		"ruleID" : deleId
								   }, 
								  url : __ctxPath+ '/icm/rule/removeRule.f',
								  callback : function(ret) {
									  me.refreshTree()//刷新
								  }
								 });
							}
						}
					});
				}
			};
	    }
	    menu.add(deleteRela);
	    
	    /* ‘刷新’右键菜单*/
		var refresh = {
			iconCls : 'icon-arrow-refresh',
			text :'刷新',
			handler : function() {
				 me.refreshTree();
			}
		};
	    menu.add(refresh);
	    return menu;
    },
    refreshTree: function(){
		var me = this;
        me.getStore().load();
	},
	onTreepanelItemClick: function(store, records){
		var me = this;
		me.up('panel').ruleeditpanel.removeAll(true);//删除
    	me.up('panel').ruleeditpanel.addComponent();
		var parentNode = records.parentNode;
		me.up('panel').ruleeditpanel.reLoadData(store, records, parentNode);
	},
	addSameLevel: function(rec){
		var me = this;
		me.up('panel').ruleeditpanel.removeAll(true);//删除
    	me.up('panel').ruleeditpanel.addComponent();
    	var parentNode = rec.parentNode;
    	me.up('panel').ruleeditpanel.parentID.setValue(parentNode.data.id);
    	me.up('panel').ruleeditpanel.parentRule.setValue(parentNode.data.text);
	},
    addNextLevel: function(id, name) {
    	var me = this;
    	me.up('panel').ruleeditpanel.removeAll(true);//删除
    	me.up('panel').ruleeditpanel.addComponent();
    	me.up('panel').ruleeditpanel.addNextLevel(null, id);
    	me.up('panel').ruleeditpanel.parentID.setValue(id);
    	me.up('panel').ruleeditpanel.parentRule.setValue(name);
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            
            listeners: {
                itemclick: me.onTreepanelItemClick,
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
    }

});