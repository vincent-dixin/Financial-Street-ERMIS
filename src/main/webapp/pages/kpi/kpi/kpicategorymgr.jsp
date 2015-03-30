<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>分类管理</title>
            <script type="text/javascript">
                var fhd_kpi_kpicategorymgr_view = (function () {
                    var treeUrl = __ctxPath + "/kpi/category/categorytreeloader.f";
                    var editUrl = __ctxPath + "/pages/kpi/kpi/kpicategoryedit.jsp?";
                    var findparentUrl = __ctxPath + '/kpi/category/findparentbyid.f';

                    function sublevelHandler(id, name) {
                        var rightUrl = editUrl + "parentid=" + id + "&id=undefined" + "&editflag=false" + "&parentname=" + encodeURIComponent(name);
                        fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                    }

                    function sameLevelHandler(node) {
                    	if(node.parentNode.data.id=="category_root"){
                    		var rightUrl = editUrl + "parentid=" + "" + "&editflag=false" + "&id=undefined"  + "&parentname=" + encodeURIComponent(FHD.locale.get('fhd.kpi.categoryroot'));
                            fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                    	}else{
	                        FHD.ajax({
	                            params: {
	                                "id": node.data.id
	                            },
	                            url: findparentUrl,
	                            callback: function (ret) {
	                                var rightUrl = editUrl + "parentid=" + "" + "&id=undefined" + "&editflag=false" + "&parentname=" + encodeURIComponent(ret.parentname);
	                                fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
	                            }
	                        });
                    	}
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
                                        url: __ctxPath + '/kpi/category/removecategory.f',
                                        callback: function (ret) {
                                            if (ret && ret.result) {
                                                if (ret.result == "cascade") {
                                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.categorycasdes"));
                                                } else if (ret.result == "success") {
                                                    fhd_kpi_kpicategorymgr_view.refreshTree();
                                                }

                                            }

                                        }
                                    });
                                }
                            }
                        });
                    }

                    function clickFunction(node) {
                    	if (node.parentNode == null) return;
                    	var id = node.data.id;
                        var name = node.data.text;
                    	if(node.parentNode.data.id=="category_root"){
                    		var rightUrl = editUrl + "parentid=" + "" + "&editflag=true" + "&id=" + id + "&parentname=" + encodeURIComponent(FHD.locale.get('fhd.kpi.categoryroot')) + "&categoryname=" + encodeURIComponent(name);
                            fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                    	}else{
                    		
                            FHD.ajax({
                                params: {
                                    "id": id
                                },
                                url: findparentUrl,
                                callback: function (ret) {
                                    var rightUrl = editUrl + "parentid=" + ret.parentid + "&editflag=true" + "&id=" + id + "&parentname=" + encodeURIComponent(ret.parentname) + "&categoryname=" + encodeURIComponent(name);
                                    fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                                }
                            });
                    	}
                        
                        
                    }

                    function contextItemMenuFun(view, rec, node, index, e) {
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
                                    sublevelHandler(id, name);
                                }
                            };
                        menu.add(subLevel);
                        if (index != 0) {
                            var sameLevel = {
                                iconCls: 'icon-add',
                                text: FHD.locale.get('fhd.strategymap.strategymapmgr.sameLevel'),
                                handler: function () {
                                    sameLevelHandler(rec);
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
                                    deleteHandler(id);
                                }
                            }
                            menu.add(delmenu);
                            menu.add('-');
                        }

                        var refresh = {
                            iconCls: 'icon-arrow-refresh',
                            text: FHD.locale.get('fhd.strategymap.strategymapmgr.refresh'),
                            handler: function () {
                                fhd_kpi_kpicategorymgr_view.refreshTree();
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

                    Ext.define('FHD.kpi.kpicategorymgr.view', {
                        tree: null,
                        container: null,
                        kpiid: "",
                        refreshTree: function () {
                            this.tree.getStore().load();
                        },
                        init: function () {
                            this.tree = Ext.create('FHD.ux.TreePanel', {
                                animate: false,
                                rootVisible: true,
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
                                height: FHD.getCenterPanelHeight() - 5,
                                contextItemMenuFc: contextItemMenuFun,
                                clickFunction: clickFunction,
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
                                        this.clickFunction(record);
                                    }
                                },
                                viewConfig: {
                                    listeners: {
                                        itemcontextmenu: function (view, rec, node, index, e) {
                                            e.stopEvent();
                                            var menu = fhd_kpi_kpicategorymgr_view.tree.contextItemMenuFc(view, rec, node, index, e);
                                            if (menu) {
                                                menu.showAt(e.getPoint());
                                            }
                                            return false;
                                        }
                                    }
                                }

                            });
                            

                            this.container = Ext.create('Ext.container.Container', {
                                renderTo: 'FHD.kpi.kpicategorymgr.view${param._dc}',
                                rightpanel: '',
                                height: FHD.getCenterPanelHeight(),
                                layout: {
                                    type: 'border'
                                },
                                items: [this.tree]
                            });


							
                            this.tree.on('collapse', function (p) {
                                fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(this.container.getWidth() - 26 - 5);
                                fhd_kpi_kpicategoryedit_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                                if ((typeof addkpi_view) != "undefined") {
                                    addkpi_view.container.setWidth(this.container.getWidth() - 26 - 5);
                                    addkpi_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                                }
                            });
                            this.tree.on('expand', function (p) {
                                fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                fhd_kpi_kpicategoryedit_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                                if ((typeof addkpi_view) != "undefined") {
                                    addkpi_view.container.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                    addkpi_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                                }
                            });
                            this.container.on('resize', function (p) {
                                fhd_kpi_kpicategoryedit_view.tabpanel.setHeight(p.getHeight());
                                if (fhd_kpi_kpicategorymgr_view.tree.collapsed) {
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(p.getWidth() - 26 - 5);
                                    fhd_kpi_kpicategoryedit_view.resizeFieldset(p.getWidth() - 26 - 18);
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setHeight(p.getHeight() - 58);
                                    if ((typeof addkpi_view) != "undefined") {
                                        addkpi_view.container.setWidth(this.container.getWidth() - 26 - 5);
                                        addkpi_view.resizeFieldset(this.container.getWidth() - 26 - 35);
                                        addkpi_view.container.setHeight(p.getHeight());
                                    }
                                } else {
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(p.getWidth() - fhd_kpi_kpicategorymgr_view.tree.getWidth() - 2);
                                    fhd_kpi_kpicategoryedit_view.resizeFieldset(p.getWidth() - fhd_kpi_kpicategorymgr_view.tree.getWidth() - 18);
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setHeight(p.getHeight() - 58);
                                    if ((typeof addkpi_view) != "undefined") {
                                        addkpi_view.container.setWidth(p.getWidth() - fhd_kpi_kpicategorymgr_view.tree.getWidth() - 2);
                                        addkpi_view.resizeFieldset(p.getWidth() - fhd_kpi_kpicategorymgr_view.tree.getWidth() - 30);
                                        addkpi_view.container.setHeight(p.getHeight());
                                    }
                                }
                            });
                            this.tree.on('resize', function (p) {
                                if (p.collapsed) {
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(this.container.getWidth() - 26 - 5);
                                    fhd_kpi_kpicategoryedit_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                                    if ((typeof addkpi_view) != "undefined") {
                                        addkpi_view.container.setWidth(this.container.getWidth() - 26 - 5);
                                        addkpi_view.resizeFieldset(this.container.getWidth() - 26 - 18);
                                    }
                                } else {
                                    fhd_kpi_kpicategoryedit_view.tabpanel.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                    fhd_kpi_kpicategoryedit_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                                    if ((typeof addkpi_view) != "undefined") {
                                        addkpi_view.container.setWidth(this.container.getWidth() - p.getWidth() - 5);
                                        addkpi_view.resizeFieldset(this.container.getWidth() - p.getWidth() - 18);
                                    }
                                }
                            });

                            this.initRightPanel(editUrl);

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
                    var fhd_kpi_kpicategorymgr_view = Ext.create('FHD.kpi.kpicategorymgr.view');
                    return fhd_kpi_kpicategorymgr_view
                }

                )();

                Ext.onReady(function () {
                    fhd_kpi_kpicategorymgr_view.init();
                    var initUrl = __ctxPath + "/pages/kpi/kpi/kpicategoryedit.jsp?" + "parentid=" +""+ "&id=undefined" + "&editflag=undefined" + "&parentname=" + encodeURIComponent('分类库');
                    fhd_kpi_kpicategorymgr_view.initRightPanel(initUrl);
                    fhd_kpi_kpicategorymgr_view.tree.getRootNode().expand(false); //默认展开第一级节点
                    FHD.componentResize(fhd_kpi_kpicategorymgr_view.container, 0, 0);
                })
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.kpicategorymgr.view${param._dc}'></div>
        </body>
    
    </html>