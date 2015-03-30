<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>指标类型树</title>
            <script type="text/javascript">
                var fhd_kpi_kpitypetree_view;
                var kpitypetreeUrl = __ctxPath + "/kpi/kpi/kpitypetreeloader.f";

                Ext.define('FHD.kpi.kpitypetree.view', {
                    url: __ctxPath + "/pages/kpi/kpi/opt/kpitypeedit.jsp?",
                    levelHandler: function (id) {
                        var me = this;
                        fhd_kpi_kpiaccordion_view.initRightPanel(me.url + "&editflag=false");
                    },
                    //点击指标类型面板时触发
                    clicked: function () {
                        var url;
                        var selectedNode;
                        var tree = fhd_kpi_kpitypetree_view.tree;
                        var nodeItems = tree.getSelectionModel().selected.items;
                        if (nodeItems.length > 0) {
                            selectedNode = nodeItems[0];
                        }
                        if (selectedNode) {
                            var data = selectedNode.data;
                            url = fhd_kpi_kpitypetree_view.url + "editflag=true" + "&id=" + data.id + "&name=" + encodeURIComponent(data.text);
                        } else {
                            url = fhd_kpi_kpitypetree_view.url + "&editflag=false";
                        }
                        fhd_kpi_kpiaccordion_view.initRightPanel(url);
                    },
                    deleteHandler: function (id) {
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
                                            	fhd_kpi_kpitypetree_view.refreshTree();
                                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    },
                    refreshTree: function () {
                        var me = this;
                        me.tree.getStore().load();
                    },
                    clickFunction: function (node) {
                        var me = this;
                        if (node.parentNode == null) return;
                        var id = node.data.id;
                        var name = node.data.text;
                        fhd_kpi_kpiaccordion_view.initRightPanel(me.url + "editflag=true" + "&id=" + id + "&name=" + encodeURIComponent(name));
                    },
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
                                    fhd_kpi_kpitypetree_view.levelHandler(id);
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
                                    fhd_kpi_kpitypetree_view.levelHandler(id);
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
                                    fhd_kpi_kpitypetree_view.deleteHandler(id);
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
                                fhd_kpi_kpitypetree_view.refreshTree();
                            }
                        }
                        menu.add(refresh);
                        return menu;
                    },
                    init: function () {
                        var me = this;
                        me.tree = Ext.create('FHD.ux.TreePanel', {
                        	autoScroll:true,
                            clickFunction: me.clickFunction,
                            contextItemMenuFc: me.contextItemMenuFun,
                            renderTo: 'FHD.kpi.kpitypetree.view${param._dc}',
                            animate: false,
                            rootVisible: false,
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
                            url: kpitypetreeUrl, //调用后台url
                            rootVisible: true,
                            height: FHD.getCenterPanelHeight() - 5,
                            root: {
                                "id": "type_1",
                                "text": FHD.locale.get('fhd.kpi.kpi.form.etype'),
                                "dbid": "type_1",
                                "leaf": false,
                                "code": "zblx",
                                "type": "kpi_type",
                                "expanded": true
                            },
                            listeners: {
                                itemclick: function (node, record, item) {
                                    fhd_kpi_kpitypetree_view.clickFunction(record);
                                },
                            	load: function (store, records) { //默认选择首节点
                                    var tree = fhd_kpi_kpitypetree_view.tree;
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

                Ext.onReady(function () {
                    fhd_kpi_kpitypetree_view = Ext.create('FHD.kpi.kpitypetree.view');
                    fhd_kpi_kpitypetree_view.init();
                })
            </script>
        </head>
        
        <body>
            <div id="FHD.kpi.kpitypetree.view${param._dc}"></div>
        </body>
    
    </html>