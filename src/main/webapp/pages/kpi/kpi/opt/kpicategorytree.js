Ext.define('Ext.kpi.kpi.opt.kpicategorytree', {
    extend: 'Ext.container.Container',

    url: __ctxPath + "/pages/kpi/kpi/opt/kpicategoryedit.jsp?",
    treeUrl: __ctxPath + "/kpi/category/categorytreeloader.f",
    parentUrl: __ctxPath + '/kpi/category/findparentbyid.f',
    
    clicked:function(){
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
    refreshTree: function () {
        var me = this;
        me.tree.getStore().load();
    },
    sublevelHandler: function (id, name) {
        var me = this;
        var rightUrl = me.url + "categoryparentid=" + id + "&categoryid=undefined" + "&editflag=false" + "&categoryparentname=" + encodeURIComponent(name);
        fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
    },
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
    deleteHandler: function (id) {
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
                                    me.refreshTree();
                                }

                            }

                        }
                    });
                }
            }
        });
    },
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
                me.sublevelHandler(id, name);
            }
        };
        menu.add(subLevel);
        if (index != 0) {
            var sameLevel = {
                iconCls: 'icon-add',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
                handler: function () {
                    me.sameLevelHandler(rec);
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
                    me.deleteHandler(id);
                }
            }
            menu.add(delmenu);
            menu.add('-');
        }

        var refresh = {
            iconCls: 'icon-arrow-refresh',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.refresh'),
            handler: function () {
                me.refreshTree();
            }
        }
        menu.add(refresh);

        return menu;

    },
    clickFunction: function (node) {
        var me = this;
        if (node.parentNode == null) return;
        var id = node.data.id;
        var name = node.data.text;
        if (node.parentNode.data.id == "category_root") {
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
    initComponent: function () {
        var me = this;
        me.tree = Ext.create('FHD.ux.TreePanel', {
        	autoScroll:true,
            animate: false,
            rootVisible: true,
            width: 265,
            maxWidth: 300,
            split: true,
            collapsible: false,
            border: false,
            region: 'west',
            multiSelect: true,
            rowLines: false,
            singleExpand: false,
            checked: false,
            url: me.treeUrl, //调用后台url
            height: FHD.getCenterPanelHeight() - 5,
            root: {
                "id": "category_root",
                "text": FHD.locale.get('fhd.kpi.categoryroot'),
                "dbid": "category_root",
                "leaf": false,
                "code": "category",
                "type": "kpi_category",
                "expanded": true
            },
            listeners: {
                itemclick: function (node, record, item) {
                    me.clickFunction(record);
                },
                load: function (store, records) { //默认选择首节点
                    var tree = kpi_category_tree_view.tree;
                    var rootNode = tree.getRootNode();
                    if (rootNode.childNodes.length > 0) {
                        tree.getSelectionModel().select(rootNode.firstChild);
                    }
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

        Ext.applyIf(me, {
            renderTo: me.renderTo,
            items: [me.tree]
        });


        me.callParent(arguments);
    }

});