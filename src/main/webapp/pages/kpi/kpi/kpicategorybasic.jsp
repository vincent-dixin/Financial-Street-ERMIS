<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>多维结构编辑</title>
            <script type="text/javascript">
                var fhd_kpi_kpicategorybasic_view = (function () {
                    /*变量定义开始*/
                    var currentId = "${param.id}";
                    var parentid = "${param.parentid}";
                    var editflag = "${param.editflag}";
                    var parentname = "${param.parentname}";
                    var categoryname = "${param.categoryname}";
                    var warningUrl = __ctxPath + "/kpi/category/findcategoryrelaalarmbysome.f?id=" + currentId;
                    var editUrl = __ctxPath + "/pages/kpi/kpi/kpicategoryedit.jsp?";
                    Ext.define('warningModel', {
                        extend: 'Ext.data.Model',
                        fields: ['id', 'sort', 'date', 'alarm', 'warning']
                    });
                    var alarmModels = Ext.define('alarmModels', {
                        extend: 'Ext.data.Model',
                        fields: [{
                            name: 'id',
                            type: 'string'
                        }, {
                            name: 'name',
                            type: 'string'
                        }]
                    });
                    var warningStore = Ext.create('Ext.data.Store', {
                        model: 'alarmModels',
                        proxy: {
                            type: 'ajax',
                            url: __ctxPath + '/kpi/kpistrategymap/findwarningbytype.f?type=warningtype',
                            reader: {
                                type: 'json',
                                root: 'warninglist'
                            }
                        },
                        autoLoad: true
                    });
                    var alarmStore = Ext.create('Ext.data.Store', {
                        model: 'alarmModels',
                        proxy: {
                            type: 'ajax',
                            url: __ctxPath + '/kpi/kpistrategymap/findwarningbytype.f?type=alarmtype',
                            reader: {
                                type: 'json',
                                root: 'warninglist'
                            }
                        },
                        autoLoad: true
                    });
                    var warningGrid = Ext.create('FHD.ux.EditorGridPanel', {
                        border: false,
                        pagable: false,
                        url: warningUrl,
                        height: FHD.getCenterPanelHeight() - 150,
                        cols: [{
                            dataIndex: 'id',
                            id: 'id',
                            width: 0
                        }, {
                            header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.date"),
                            dataIndex: 'date',
                            sortable: true,
                            flex: 1,
                            renderer: function (value) {
                                if (value instanceof Date) {
                                    return Ext.Date.format(value, 'Y-m-d')
                                } else {
                                    return value;
                                }
                            },
                            editor: new Ext.form.DateField({
                                //在编辑器里面显示的格式,这里为10/20/09的格式  
                                format: 'm/d/y'
                            })
                        }, {
                            header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.alarm"),
                            dataIndex: 'alarm',
                            sortable: true,
                            flex: 2,
                            editor: Ext.create('Ext.form.field.ComboBox', {
                                multiSelect: false,
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                labelWidth: 40,
                                store: alarmStore,
                                queryMode: 'alarmModels',
                                name: 'alarm'
                            }),
                            renderer: function (value) {
                                return alarmRenderFun(value);
                            }
                        }, {
                            header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.warning"),
                            dataIndex: 'warning',
                            sortable: true,
                            flex: 2,
                            editor: Ext.create('Ext.form.field.ComboBox', {
                                multiSelect: false,
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                labelWidth: 40,
                                store: warningStore,
                                queryMode: 'alarmModels',
                                name: 'warning'
                            }),
                            renderer: function (value) {
                                return warningRenderFun(value);
                            }
                        }],
                        tbarItems: [{ /*text : FHD.locale.get('fhd.common.add'),*/
                            id: 'addBtn${param._dc}',
                            iconCls: 'icon-add',
                            handler: addWarning,
                            scope: this
                        }, '-', {
                            id: 'delBtn${param._dc}',
                            /*text : FHD.locale.get('fhd.common.delete'),*/
                            iconCls: 'icon-del',
                            handler: warningDel,
                            disabled: true,
                            scope: this
                        }]
                    });

                    /*变量定义结束*/

                    /*函数开始*/
                    function findFormDataById(id) {
                        fhd_kpi_kpicategorybasic_view.basicPanel.form.load({
                            waitMsg: '数据加载中,请稍候.....',
                            url: __ctxPath + '/kpi/category/findcategoryByIdToJson.f',
                            params: {
                                id: id
                            },
                            success: function (form, action) {
                                return true;
                            },
                            failure: function (form, action) {
                                //alert("err");
                            }
                        });
                    }

                    function alarmRenderFun(value) {
                        var valuestr = "";
                        Ext.Array.each(value, function (v) {
                            var index = alarmStore.find('id', v);
                            var record = alarmStore.getAt(index);
                            if (record != null) {
                                valuestr += record.data.name + ",";
                            }
                        });
                        if (valuestr.length > 0) {
                            valuestr = valuestr.substring(0, valuestr.length - 1);
                        }
                        return "<div data-qtitle='' data-qtip='" + valuestr + "'>" + valuestr + "</div>";
                    }

                    function warningRenderFun(value) {
                        var values = "";
                        Ext.Array.each(value, function (v) {
                            var index = warningStore.find('id', v);
                            var record = warningStore.getAt(index);
                            if (record != null) {
                                values += record.data.name + ",";
                            }
                        });
                        if (values.length > 0) {
                            values = values.substring(0, values.length - 1);
                        }
                        return "<div data-qtitle='' data-qtip='" + values + "'>" + values + "</div>";
                    }

                    function warningDel() { //预警信息删除方法
                        var selection = warningGrid.getSelectionModel().getSelection();
                        Ext.MessageBox.show({
                            title: FHD.locale.get('fhd.common.delete'),
                            width: 260,
                            msg: FHD.locale.get('fhd.common.makeSureDelete'),
                            buttons: Ext.MessageBox.YESNO,
                            icon: Ext.MessageBox.QUESTION,
                            fn: function (btn) {
                                if (btn == 'yes') {
                                    var ids = [];
                                    for (var i = 0; i < selection.length; i++) {
                                        warningGrid.store.remove(selection[i]);
                                    }
                                }
                            }
                        });
                    }

                    function addWarning(btn) {
                        var r = Ext.create('warningModel');
                        warningGrid.store.add(r);
                        warningGrid.editingPlugin.startEditByPosition({
                            row: 0,
                            column: 0
                        });
                        warningGrid.doComponentLayout();
                    }

                    function createCode() {
                        var vform = fhd_kpi_kpicategorybasic_view.basicPanel.getForm();
                        var vobj = vform.getValues();
                        var paraobj = {};
                        paraobj.id = currentId;
                        if (vobj.parentStr != "") {
                            paraobj.parentid = vobj.parentStr;
                            FHD.ajax({
                                url: __ctxPath + '/kpi/category/findcodebyparentid.f',
                                params: {
                                    param: Ext.JSON.encode(paraobj)
                                },
                                callback: function (data) {
                                    if (data && data.success) {
                                        vform.setValues({
                                            code: data.code
                                        });
                                    }
                                }
                            });
                        }
                    }

                    function basicPanelLast() {
                        var form = fhd_kpi_kpicategorybasic_view.basicPanel.getForm();
                        var vobj = form.getValues();
                        var paramObj = {};
                        paramObj.name = vobj.name;
                        paramObj.code = vobj.code;

                        FHD.ajax({
                            url: __ctxPath + '/kpi/category/validate.f',
                            params: {
                                id: currentId,
                                validateItem: Ext.JSON.encode(paramObj)
                            },
                            callback: function (data) {
                                if (data && data.success) {
                                    //提交指标信息
                                    var addUrl = __ctxPath + '/kpi/category/mergecategory.f';
                                    FHD.submit({
                                        form: form,
                                        url: addUrl,
                                        callback: function (data) {
                                            if (data) {
                                                currentId = data.id;
                                                fhd_kpi_kpicategorybasic_view.warningPanel.add(warningGrid);
                                                fhd_kpi_kpicategorybasic_view.basicPanel.hide();
                                                fhd_kpi_kpicategorybasic_view.warningPanel.show();
                                                fhd_kpi_kpicategorybasic_view.container.add(fhd_kpi_kpicategorybasic_view.warningPanel);
                                                fhd_kpi_kpicategorymgr_view.refreshTree();
                                            }
                                        }
                                    });
                                } else {
                                    //校验失败信息
                                    if (data && data.error == "nameRepeat") {
                                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                                        return;
                                    }
                                    if (data && data.error == "codeRepeat") {
                                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                                        return;
                                    }
                                }
                            }
                        });
                    }

                    function validateData(arr) {
                        var hash = {};
                        for (var i in arr) {
                            if (hash[arr[i]]) return true;
                            hash[arr[i]] = true;
                        }
                        return false;
                    }

                    function validateContent(jsonArray) {
                        for (var i = 0; i < jsonArray.length; i++) {
                            if (jsonArray[i].date == "") {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.strategymap.strategymapmgr.prompt.dateIsNull"));
                                return false;
                            }
                            if (jsonArray[i].alarm == "") {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.strategymap.strategymapmgr.prompt.alarmIsNull"));
                                return false;
                            }
                            if (jsonArray[i].warning == "") {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.strategymap.strategymapmgr.prompt.warningIsNull"));
                                return false;
                            }
                        }
                        return true;
                    }

                    function warningPanelPre() {
                        fhd_kpi_kpicategorybasic_view.warningPanel.hide();
                        fhd_kpi_kpicategorybasic_view.basicPanel.show();
                        fhd_kpi_kpicategorybasic_view.container.add(fhd_kpi_kpicategorybasic_view.basicPanel);
                    }

                    function warningPanelLast() {
                        var rows = warningGrid.store.data.items;
                        var jsonArray = [];
                        var dataArray = [];
                        Ext.Array.each(rows, function (item) {
                            if (item.data.date instanceof Date) {
                                dataArray.push(Ext.Date.format(new Date(item.data.date), 'Y-m-d'));
                            } else {
                                dataArray.push(item.data.date);
                            }
                            jsonArray.push(item.data);
                        });
                        if (validateContent(jsonArray)) {
                            if (validateData(dataArray)) {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.dateRepeat'));
                                return;
                            }

                            FHD.ajax({
                                url: __ctxPath + '/kpi/category/mergecategoryrelaalarm.f',
                                params: {
                                    modifiedRecord: Ext.JSON.encode(jsonArray),
                                    id: currentId
                                },
                                callback: function (data) {
                                    if (data && data.success) {
                                        //跳转到首页
                                        toFirstPage();


                                    }
                                }
                            });
                        }
                        warningGrid.store.commitChanges();

                    }

                    function toFirstPage() {
                        var rightUrl = editUrl + "parentid=" + parentid + "&editflag=true" + "&id=" + currentId + "&parentname=" + encodeURIComponent(parentname);
                        fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                    }

                    function onchange() {
                        warningGrid.down('#delBtn${param._dc}').setDisabled(warningGrid.getSelectionModel().getSelection().length === 0);
                    }
                    /*函数结束*/

                    Ext.define('FHD.kpi.kpicategorybasic.view', {
                        container: null,
                        basicPanel: null,
                        warningPanel: null,
                        resizeFieldset: function (size) {
                            var fieldItems = this.basicPanel.items.items;
                            Ext.Array.each(fieldItems, function (fieldobj) {
                                fieldobj.setWidth(size);
                            });
                        },
                        addListeners: function () {
                            warningGrid.store.on('load', onchange); //执行store.load()时改变按钮可用状态
                            warningGrid.on('selectionchange', onchange); //选择记录发生改变时改变按钮可用状态
                            warningGrid.store.on('update', onchange); //修改时改变按钮可用状态
                        },
                        init: function () {
                            this.basicPanel = Ext.create('Ext.form.Panel', {
                                waitMsgTarget: true,
                                autoScroll: true,
                                border: false,
                                layout: 'column',
                                width: FHD.getCenterPanelWidth() - 300,
                                bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                bbar: {
                                    style: 'background-image:url() !important;background-color:rgb(250,250,250);',
                                    items: [{
                                        xtype: 'tbtext',
                                        text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + "2.告警设置" + '</b></font>'
                                    }, '->', {
                                        text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                                        iconCls: 'icon-control-fastforward-blue',
                                        handler: function () {
                                            basicPanelLast();
                                        }
                                    }]
                                },
                                bodyPadding: 5,
                                items: [{
                                    xtype: 'fieldset',
                                    collapsible: true,
                                    width: FHD.getCenterPanelWidth() - 285,
                                    defaults: {
                                        margin: '7 30 3 30',
                                        labelWidth: 95
                                    },
                                    layout: {
                                        type: 'column'
                                    },
                                    title: FHD.locale.get('fhd.common.baseInfo'),
                                    items: [{
                                        xtype: 'hidden',
                                        hidden: true,
                                        name: 'id',
                                        value: currentId
                                    }, {
                                        xtype: 'hidden',
                                        hidden: true,
                                        name: 'parentStr',
                                        value: parentid
                                    }, {
                                        xtype: 'textfield',
                                        readOnly: true,
                                        disabled: true,
                                        name: 'parentStr',
                                        fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.parentCategory'),
                                        value: '',
                                        maxLength: 200,
                                        columnWidth: .5,
                                        allowBlank: false,
                                        value: parentname
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'),
                                        margin: '7 3 3 30',
                                        name: 'code',
                                        maxLength: 255,
                                        columnWidth: .4
                                    }, {
                                        xtype: 'button',
                                        margin: '7 30 3 3',
                                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
                                        handler: createCode,
                                        columnWidth: .1
                                    }, {
                                        xtype: 'textareafield',
                                        name: 'name',
                                        rows: 3,
                                        fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>',
                                        value: '',
                                        maxLength: 100,
                                        columnWidth: .5,
                                        allowBlank: false
                                    }, {
                                        xtype: 'textareafield',
                                        rows: 3,
                                        labelAlign: 'left',
                                        name: 'desc',
                                        fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),
                                        maxLength: 4000,
                                        columnWidth: .5
                                    }

                                    ,
                                    Ext.create('FHD.ux.org.CommonSelector', {
                                        fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>',
                                        growMax: 60,
                                        labelAlign: 'left',
                                        columnWidth: .5,
                                        name: 'ownDept',
                                        multiSelect: false,
                                        type: 'dept_emp',
                                        maxHeight: 70,
                                        labelWidth: 95
                                    }), {
                                        xtype: 'dictradio',
                                        labelWidth: 105,
                                        name: 'statusStr',
                                        columns: 4,
                                        dictTypeId: '0yn',
                                        fieldLabel: FHD.locale.get('fhd.common.enable') + '<font color=red>*</font>',
                                        defaultValue: '0yn_y',
                                        labelAlign: 'left',
                                        allowBlank: false,
                                        columnWidth: .5
                                    }, , {
                                        xtype: 'textareafield',
                                        rows: 3,
                                        labelAlign: 'left',
                                        fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.categoryresultFormula'),
                                        name: 'resultFormula',
                                        columnWidth: .5
                                    }, {
                                        xtype: 'textareafield',
                                        rows: 3,
                                        labelAlign: 'left',
                                        fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
                                        name: 'forecastFormula',
                                        columnWidth: .5
                                    }

                                    ]
                                }

                                ]
                            });

                            this.warningPanel = Ext.create('Ext.panel.Panel', {
                                border: false,
                                layout: "fit",
                                items: [],
                                bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                bbar: {
                                    style: 'background-image:url() !important;background-color:rgb(250,250,250);',
                                    items: [{
                                        xtype: 'tbtext',
                                        text: '<font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font>' + '<font color="#3980F4"><b>' + "2.告警设置" + '</b></font>'
                                    }, '->', {
                                        text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
                                        iconCls: 'icon-control-rewind-blue',
                                        handler: function () {
                                            warningPanelPre();
                                        }
                                    }, {
                                        text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),
                                        iconCls: 'icon-control-stop-blue',
                                        handler: function () {
                                            warningPanelLast();
                                        }
                                    }]
                                }
                            });

                            this.container = Ext.create('Ext.panel.Panel', {
                                border: false,
                                renderTo: 'FHD.kpi.kpicategorybasic.view${param._dc}',
                                layout: "fit",
                                height: FHD.getCenterPanelHeight() - 60,
                                items: [this.basicPanel]
                            });

                            this.addListeners();

                            if (editflag == "true") {
                                /*加载form数据*/
                                findFormDataById(currentId);
                            }


                        }

                    });

                    var fhd_kpi_kpicategorybasic_view = Ext.create('FHD.kpi.kpicategorybasic.view');
                    return fhd_kpi_kpicategorybasic_view

                })();

                Ext.onReady(function () {
                    fhd_kpi_kpicategorybasic_view.init();
                    FHD.componentResize(fhd_kpi_kpicategorybasic_view.container, 0, 0);
                })


                 Ext.apply(Ext.form.field.VTypes, {

                });
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.kpicategorybasic.view${param._dc}'></div>
        </body>
    
    </html>