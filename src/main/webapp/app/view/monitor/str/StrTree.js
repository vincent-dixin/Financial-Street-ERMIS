Ext.define('FHD.view.monitor.str.StrTree', {
    extend: 'FHD.ux.TreePanel',

    border : false,
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
            	
            }
        };
        menu.add(subLevel);
        if (index != 0) {
            /*添加同级*/
            var sameLevel = {
                iconCls: 'icon-add',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
                handler: function () {
                	
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
                    	
                    }
                };
            menu.add(enablemenu);
            var disablemenu = {
                    iconCls: 'icon-plan-stop',
                    text: FHD.locale.get('fhd.sys.planMan.stop'),
                    handler: function () {
                    	
                    }
                };
            menu.add(disablemenu);
            menu.add('-');
        }
        return menu;
    },
    /**
     *刷新树函数 
     */
    refreshTree: function (obj) {
    	var me = this;
        me.getStore().load();
    },
    
    initComponent: function () {
        var me = this;
        
        Ext.applyIf(me, {
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
    }

});