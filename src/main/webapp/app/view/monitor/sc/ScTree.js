Ext.define('FHD.view.monitor.sc.ScTree', {
					extend : 'FHD.ux.TreePanel',
	url : __ctxPath + "/kpi/category/categorytreeloader.f",

	root : {
		"id" : "category_root",
		"text" : FHD.locale.get('fhd.kpi.categoryroot'),
		"dbid" : "category_root",
		"leaf" : false,
		"code" : "category",
		"type" : "kpi_category",
		"expanded" : true,
		'iconCls' : 'icon-ibm-icon-scorecards'
	},
	autoScroll : true,
	animate : false,
	rootVisible : true,
	collapsible : false,
	border : false,
	multiSelect : true,
	rowLines : false,
	singleExpand : false,
	checked : false,
	
	/**
	 * 添加右键菜单函数
	 */
	contextItemMenuFun : function(view, rec, node, index, e) {
		var me = this;
		var id = rec.data.id;
		if ("allkpi" == id) {
			return;
		}
		var name = rec.data.text;
		var menu = Ext.create('Ext.menu.Menu', {
			margin : '0 0 10 0',
			items : []
		});
		// 添加下级菜单
		var subLevel = {
			iconCls : 'icon-add',
			text : FHD.locale.get('fhd.strategymap.strategymapmgr.subLevel'),
			handler : function() {

			}
		};
		menu.add(subLevel);
		if (index != 0) {
			// 添加同级菜单
			var sameLevel = {
				iconCls : 'icon-add',
				text : FHD.locale
						.get('fhd.strategymap.strategymapmgr.sameLevel'),
				handler : function() {

				}
			};
			menu.add(sameLevel);
		}
		menu.add('-');

		if (index != 0) {
			// 删除菜单
			var delmenu = {
				iconCls : 'icon-delete-icon',
				text : FHD.locale.get('fhd.strategymap.strategymapmgr.delete'),
				handler : function() {

				}
			}
			menu.add(delmenu);
			menu.add('-');
		}
		// 刷新菜单
		var refresh = {
			iconCls : 'icon-arrow-refresh',
			text : FHD.locale.get('fhd.strategymap.strategymapmgr.refresh'),
			handler : function() {
				me.refreshTree();// 刷新菜单
			}
		}
		menu.add(refresh);

		return menu;

	},

	/**
	 * 刷新树
	 */
	refreshTree: function () {
        var me = this;
        me.getStore().load();
    },
    
    /**
     * 点击树节点执行函数
     */
    onTreepanelItemClick: function(tablepanel, record, item, index, e, options) {
    	var me = this;
    	var scorecardmainpanel = Ext.getCmp('scorecardmainpanel');
    	me.monitorContainer.reRightLayout(scorecardmainpanel);
    	scorecardmainpanel.reLoadData(record);
    	
    },
    
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			
			listeners: {
                itemclick: me.onTreepanelItemClick
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
	},

});