<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>指标类型管理</title>
            <script type="text/javascript">
                var fhd_kpi_kpitypemgr_view = (function () {
                    var treeUrl = __ctxPath + "/kpi/kpi/kpitypetreeloader.f";
                    var kpitypeEditUrl = __ctxPath + "/pages/kpi/kpi/kpitypeedit.jsp?";

                    function clickFunction(node) {
                    	if (node.parentNode == null) return;
                        var id = node.data.id;
                        var name = node.data.text;
                        fhd_kpi_kpitypemgr_view.initRightPanel(kpitypeEditUrl + "editflag=true" + "&id=" + id + "&name=" + encodeURIComponent(name));
                    }

                    function levelHandler(id) {
                        fhd_kpi_kpitypemgr_view.initRightPanel(kpitypeEditUrl+"&editflag=false");
                    }

                    function deleteHandler(id) {
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
                                                fhd_kpi_kpitypemgr_view.refreshTree();
                                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                    function contextItemMenuFun(view, rec, node, index, e) {
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
                                    levelHandler(id);
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
                                    levelHandler(id);
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
                                    deleteHandler(id);
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
                                fhd_kpi_kpitypemgr_view.refreshTree();
                            }
                        }
                        menu.add(refresh);
                        return menu;
                    }

                    Ext.define('rightPanel', {
                        extend: 'Ext.panel.Panel',
                        layout: 'fit',
                        border: false,
                        autoScroll: false,
                        region: 'center'
                    });

                    Ext.define('FHD.kpi.kpitypemgr.view', {
                        tree: null,
                        container: null,
                        refreshTree: function () {
                            this.tree.getStore().load();
                        },
                        init: function () {

                            this.tree = Ext.create('FHD.ux.TreePanel', {
                                contextItemMenuFc: contextItemMenuFun,
                                clickFunction: clickFunction,
                                animate: false,
                                rootVisible: false,
                                width: 265,
                                maxWidth: 300,
                                split: true,
                                collapsible: true,
                                border: true,
                                region: 'west',
                                multiSelect: true,
                                rowLines: false,
                                singleExpand: false,
                                checked: false,
                                url: treeUrl, //调用后台url
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
                                        this.clickFunction(record);
                                    }
                                },
                                viewConfig: {
                                    listeners: {
                                        itemcontextmenu: function (view, rec, node, index, e) {
                                            e.stopEvent();
                                            var menu = fhd_kpi_kpitypemgr_view.tree.contextItemMenuFc(view, rec, node, index, e);
                                            if (menu) {
                                                menu.showAt(e.getPoint());
                                            }
                                            return false;
                                        }
                                    }
                                }

                            });


                            this.container = Ext.create('Ext.container.Container', {
                                renderTo: 'FHD.kpi.kpitypemgr.view${param._dc}',
                                rightpanel: '',
                                border: false,
                                height: FHD.getCenterPanelHeight(),
                                layout: {
                                    type: 'border'
                                },
                                items: [this.tree]
                            });

                            this.tree.on('collapse', function (p) {
                                fhd_kpi_kpitypeedit_view.tabpanel.setWidth(this.container.getWidth() - 26 - 5);
                                fhd_kpi_kpitypeedit_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                            });
                            this.tree.on('expand', function (p) {
                                fhd_kpi_kpitypeedit_view.tabpanel.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                fhd_kpi_kpitypeedit_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                            });
                            this.container.on('resize', function (p) {
                                fhd_kpi_kpitypeedit_view.tabpanel.setHeight(p.getHeight());
                                if (fhd_kpi_kpitypemgr_view.tree.collapsed) {
                                    fhd_kpi_kpitypeedit_view.tabpanel.setWidth(p.getWidth() - 26 - 5);
                                    fhd_kpi_kpitypeedit_view.resizeFieldset(p.getWidth() - 26 - 18);
                                    fhd_kpi_kpitypeedit_view.tabpanel.setHeight(p.getHeight() - 0.1);
                                } else {
                                    fhd_kpi_kpitypeedit_view.tabpanel.setWidth(p.getWidth() - fhd_kpi_kpitypemgr_view.tree.getWidth() - 2);
                                    fhd_kpi_kpitypeedit_view.resizeFieldset(p.getWidth() - fhd_kpi_kpitypemgr_view.tree.getWidth() - 18);
                                    fhd_kpi_kpitypeedit_view.tabpanel.setHeight(p.getHeight() - 0.1);
                                }
                            });
                            this.tree.on('resize', function (p) {
                                if (p.collapsed) {
                                    fhd_kpi_kpitypeedit_view.tabpanel.setWidth(this.container.getWidth() - 26 - 5);
                                    fhd_kpi_kpitypeedit_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                                } else {
                                    fhd_kpi_kpitypeedit_view.tabpanel.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                    fhd_kpi_kpitypeedit_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                                }
                            });




                            this.initRightPanel(kpitypeEditUrl);

                        },
                        initRightPanel: function (url) {
                            this.container.remove(this.container.rightpanel, true);
                            this.rightpanel = Ext.create('rightPanel', {
                                autoLoad: {
                                    url: url,
                                    scripts: true
                                }
                            });
                            this.container.rightpanel = this.rightpanel;
                            this.container.add(this.rightpanel);
                        }
                    });

                    var fhd_kpi_kpitypemgr_view = Ext.create('FHD.kpi.kpitypemgr.view');
                    return fhd_kpi_kpitypemgr_view
                }

                )();

                Ext.onReady(function () {
                    fhd_kpi_kpitypemgr_view.init();
                    FHD.componentResize(fhd_kpi_kpitypemgr_view.container, 0, 0);
                });
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.kpitypemgr.view${param._dc}'></div>
        </body>
    
    </html>