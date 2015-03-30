<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>分类编辑</title>
                
                <style type="text/css">
</style>
                
                <script type="text/javascript">
                	var categoryId;
                	var name;
                	//获取当前年份
                    function getYear(){
                    	var myDate = new Date();
                    	var year = myDate.getFullYear();
                    	return year;
                    } 
                
                	function category_gatherResultFun(v){
                		FHD.data.yearId = this.getYear();
                    	FHD.data.kpiName = v;
							var rightUrl = __ctxPath + "/pages/kpi/kpi/gatherresult.jsp?kpiname="+encodeURI(v)+ "&parentid=${param.parentid}"  + "&editflag=true" + "&id=${param.id}" + "&parentname=" + encodeURIComponent("${param.parentname}&yearId=" + FHD.data.yearId);
                            fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                    }
                    var fhd_kpi_kpicategoryedit_view = (function () {
                        /*变量定义开始*/
                        var currentId = "${param.id}";
                        var categoryname = "${param.categoryname}";
                        var parentid = "${param.parentid}";
                        var editflag = "${param.editflag}";
                        var parentname = "${param.parentname}";
                        categoryId = currentId;
                        name = categoryname;
                        
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
                            fhd_kpi_kpicategoryedit_view.basicPanel.form.load({
                                waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                                url: __ctxPath + '/kpi/category/findcategoryByIdToJson.f',
                                params: {
                                    id: id
                                },
                                success: function (form, action) {
                                	var vobj = form.getValues();
                                	if(vobj.charttypehidden!=""){
                                		var charttypearr = vobj.charttypehidden.split(',');
                                		Ext.getCmp('kpicategory_charttype').setValue(charttypearr);
                                	}
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
                            var vform = fhd_kpi_kpicategoryedit_view.basicPanel.getForm();
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
                            var form = fhd_kpi_kpicategoryedit_view.basicPanel.getForm();
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
                                    if (data && data.success && form.isValid()) {
                                    	
                                        //提交指标信息
                                        var addUrl = __ctxPath + '/kpi/category/mergecategory.f';
                                        FHD.submit({
                                            form: form,
                                            url: addUrl,
                                            callback: function (data) {
                                                if (data) {
                                                    currentId = data.id;
                                                    fhd_kpi_kpicategoryedit_view.warningPanel.add(warningGrid);
                                                    fhd_kpi_kpicategoryedit_view.basicPanel.hide();
                                                    fhd_kpi_kpicategoryedit_view.warningPanel.show();
                                                    fhd_kpi_kpicategoryedit_view.container.add(fhd_kpi_kpicategoryedit_view.warningPanel);
                                                    fhd_kpi_kpicategorymgr_view.refreshTree();
                                                }
                                            }
                                        });
                                    } else {
                                        //校验失败信息
                                        // 分类不用验证名称  -- 胡迪新 
                                        /*
                                        if (data && data.error == "nameRepeat") {
                                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                                            return;
                                        }
                                        */
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
                            fhd_kpi_kpicategoryedit_view.warningPanel.hide();
                            fhd_kpi_kpicategoryedit_view.basicPanel.show();
                            fhd_kpi_kpicategoryedit_view.container.add(fhd_kpi_kpicategoryedit_view.basicPanel);
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
                            kpiListGrid.down('#kpiedit').setDisabled(kpiListGrid.getSelectionModel().getSelection().length === 0);
                            kpiListGrid.down('#kpidel').setDisabled(kpiListGrid.getSelectionModel().getSelection().length === 0);
                        }

                        function kpiDel() {

                            Ext.MessageBox.show({
                                title: FHD.locale.get('fhd.common.delete'),
                                width: 260,
                                msg: FHD.locale.get('fhd.common.makeSureDelete'),
                                buttons: Ext.MessageBox.YESNO,
                                icon: Ext.MessageBox.QUESTION,
                                fn: function (btn) {
                                    if (btn == 'yes') { //确认删除
                                        var paraobj = {};
                                        paraobj.categoryid = currentId;
                                        paraobj.kpiids = [];
                                        var selections = kpiListGrid.getSelectionModel().getSelection();
                                        Ext.Array.each(selections, function (item) {
                                            paraobj.kpiids.push(item.get("id"));
                                        });

                                        FHD.ajax({
                                            url: __ctxPath + '/kpi/kpi/removekpibatch.f',
                                            params: {
                                                kpiItems: Ext.JSON.encode(paraobj)
                                            },
                                            callback: function (data) {
                                                if (data && data.success) {
                                                    kpiListGrid.store.load();
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        }

                        function kpiEdit() {
                            var selections = kpiListGrid.getSelectionModel().getSelection();
                            var length = selections.length;
                            if (length >= 2) {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
                                return;
                            }
                            var selection = selections[0]; //得到选中的记录
                            var kpitypename = "";
                            var kpitypeid = "";
                            var kpiid = selection.get('id');
                            fhd_kpi_kpicategorymgr_view.kpiid = kpiid;

                            FHD.ajax({
                                url: __ctxPath + '/kpi/kpi/findkpitypebykid.f',
                                params: {
                                    kpiId: kpiid
                                },
                                callback: function (data) {
                                    if (data && data.success && data.result) {
                                        if (data.result.id) {
                                            kpitypeid = data.result.id;
                                        }
                                        if (data.result.name) {
                                            kpitypename = data.result.name;
                                        }
                                    }
                                    var rightUrl = __ctxPath + "/pages/kpi/kpi/kpiadd.jsp?" + "id=" + kpiid + "&editflag=true" + "&kpitypename=" + encodeURIComponent(kpitypename) + "&categoryid=" + currentId + "&kpitypeid=" + kpitypeid + "&parentCategoryid=" + parentid + "&parentCategoryname=" + encodeURIComponent(parentname) + "&kpitypeselectFlag=false" + "&categoryname=" + encodeURIComponent(categoryname) + "&opflag=edit";
                                    fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                                }
                            });



                        }
                        /*函数结束*/
                         
                        
                        var tbar = [ //菜单项
                        {
                            tooltip: FHD.locale.get('fhd.kpi.kpi.op.addkpi'),
                            iconCls: 'icon-ibm-icon-add-metric',
                            id: 'kpiadd',
                            handler: kpiaddFun,
                            scope: this
                        }, {
                            xtype: 'tbspacer'
                        }, {
                            tooltip: FHD.locale.get('fhd.kpi.kpi.op.relakpi'),
                            id: 'kpirela',
                            iconCls: 'icon-ibm-icon-add-metric-shortcut',
                            handler: kpiRelaFun,
                            scope: this
                        }, {
                            tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.editkpi"),
                            id: 'kpiedit',
                            iconCls: 'icon-edit',
                            handler: kpiEdit,
                            scope: this
                        }, {
                            tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.delkpi"),
                            id: 'kpidel',
                            iconCls: 'icon-del',
                            handler: kpiDel,
                            scope: this
                        }

                        ];
                        
                        var kpiListUrl = __ctxPath + "/kpi/category/findcategoryrelakpi.f?id="+currentId;
                        var kpiListGrid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
                            url: kpiListUrl,
                            tbarItems: tbar,
                            height: FHD.getCenterPanelHeight() - 150,
                           	type:'category'
                        });
                        kpiListGrid.store.on('beforeload', function (store, options) {
                            var new_params = {year:FHD.data.yearId,month:FHD.data.monthId,quarter:FHD.data.quarterId,week:FHD.data.weekId};
                	        Ext.apply(kpiListGrid.store.proxy.extraParams, new_params);
            	        });
                        /*变量定义结束*/


                        /*函数开始*/
                        function kpiaddFun() {
                            var rightUrl = __ctxPath + "/pages/kpi/kpi/kpiadd.jsp?categoryid=" + currentId + "&parentCategoryid=" + parentid +
                                "&parentCategoryname=" + encodeURIComponent(parentname) + "&categoryname=" + encodeURIComponent(categoryname) + "&opflag=add";
                            fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                        }

                        function kpiRelaSubmit(kpiIds) {
                            var paraobj = {
                                categoryId: currentId,
                                kpiIds: kpiIds
                            };
                            FHD.ajax({
                                url: __ctxPath + '/kpi/category/mergecategoryrelakpi.f',
                                params: {
                                    param: Ext.JSON.encode(paraobj)
                                },
                                callback: function (data) {
                                    if (data && data.success) {
                                        kpiListGrid.store.load();
                                    }
                                }
                            });

                        }

                        
                        function kpiRelaFun() {
                            var selectorWindow = Ext.create('FHD.ux.kpi.KpiSelectorWindow', {
                                single: false,
                                extraParams: {
                                    smIconType: 'display',
                                    canChecked: true
                                },
                                onSubmit: function (values) {
                                    var ids = [];
                                    values.each(function (value) {
                                        ids.push(value.data.id);
                                    });
                                    kpiRelaSubmit(ids);
                                }
                            }).show();
                        }

                        /*函数结束*/
                        Ext.define('rightPanel', {
                            extend: 'Ext.panel.Panel',
                            layout: 'fit',
                            border: false,
                            autoScroll: false,
                            region: 'center'
                        });
                        Ext.define('FHD.kpi.kpicategoryedit.view', {
                            tabpanel: null,
                            container: null,
                            kpiListPanel: null,
                            basicPanel: null,
                            warningPanel: null,
                            editflag: editflag,
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
                                kpiListGrid.store.on('load', onchange); //执行store.load()时改变按钮可用状态
                                kpiListGrid.on('selectionchange', onchange); //选择记录发生改变时改变按钮可用状态
                                kpiListGrid.store.on('update', onchange); //修改时改变按钮可用状态
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
                                            text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.common.details') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>'
                                        }, '->', {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                                            iconCls: 'icon-control-fastforward-blue',
                                            handler: function () {
                                                basicPanelLast();
                                            }
                                        }]
                                    },
                                    bodyPadding: 5,
                                    items: [
                                        {
                                        	xtype:'hidden',
                                        	name:'charttypehidden'
                                        },    
                                        {
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
                                            maxLength: 255,
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
                                            ,allowBlank: false
                                            
                                        }),
                                        {
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
                                        } , 
                                        Ext.create('FHD.ux.dict.DictSelect', {
                                        	id:'kpicategory_charttype',
                                            editable: false,
                                            labelWidth: 95,
                                            multiSelect: true,
                                            name: 'chartTypeStr',
                                            dictTypeId: '0com_catalog_chart_type',
                                            fieldLabel: "图表类型",
                                            columnWidth: .5,
                                            labelAlign: 'left',
                                            maxHeight:60
                                        }),
                                        
                                        {
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
                                            maxLength: 600,
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
                                            text: '<font color="#cccccc"><b>' + FHD.locale.get('fhd.common.details') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font>' + '<font color="#3980F4"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>'
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
                                    title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
                                    layout: "fit",
                                    height: FHD.getCenterPanelHeight(),
                                    border: false,
                                    items: [this.basicPanel]
                                });
                                this.kpiListPanel = Ext.create('Ext.panel.Panel', {
                                    border: false,
                                    title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
                                    layout: "fit",
                                    height: FHD.getCenterPanelHeight(),
                                    items: [kpiListGrid]
                                });
                                this.tabpanel = Ext.create('Ext.tab.Panel', {
                                    width: FHD.getCenterPanelWidth() - 270,
                                    height: FHD.getCenterPanelHeight() - 40,
                                    activeTab: 0,
                                    minTabWidth:80,
                                    plain: true,
                                    items: [
                                    this.container, this.kpiListPanel],
                                    renderTo: 'FHD.kpi.kpicategoryedit.view${param._dc}'
                                });
                                this.tabpanel.getTabBar().insert(0,{xtype:'tbfill'});

                                this.addListeners();

                                if (editflag == "true") {
                                    /*加载form数据*/
                                    findFormDataById(currentId);
                                }
                            }

                        });

                        var fhd_kpi_kpicategoryedit_view = Ext.create('FHD.kpi.kpicategoryedit.view');
                        return fhd_kpi_kpicategoryedit_view

                    })();

                    Ext.onReady(function () {
                        fhd_kpi_kpicategoryedit_view.init();
                        var map = new Ext.util.KeyMap({
                            target: 'FHD.kpi.kpicategoryedit.view${param._dc}',
                            key: Ext.EventObject.RIGHT, // or Ext.EventObject.ENTER
                            ctrl:true,
                            fn: function(){
                            	alert();
                            },
                            scope: this
                        });
                        FHD.componentResize(fhd_kpi_kpicategoryedit_view.container, 0, 0);
                        //$('#breadcrumbs-3${param._dc}').xBreadcrumbs();
                        //$('#breadcrumbs-32${param._dc}').xBreadcrumbs();
                        var navigationBars = new Ext.scripts.component.NavigationBars();
                        navigationBars.renderHtml('categoryDiv', categoryId, name, 'sc');
                    })
                     
                </script>
  				
            </head>
            
            <body>
            	<div id="categoryDiv"></div>
                <!--  <div style="height: 25px" >
                	<ul class="xbreadcrumbs" id="breadcrumbs-3${param._dc}">
			            <li>
			               	记分卡
			            </li>
			            <li>
			               <a href='javascript:abc()'>系统研发部</a>
			               <ul>
			                   <li><a onclick="alert();">系统研发部</a></li>
			               </ul>
			            </li>
			            <li>
			               <a>胡迪新</a>
			               <ul>
			                   <li><a onclick="alert();">吴德福</a></li>
			                   <li><a>胡迪新</a></li>
			                   <li><a>陈晓哲</a></li>
			               </ul>
			            </li>
			        </ul>
                </div>
                
                <div style="height: 25px" >
                	<ul class="xbreadcrumbs" id="breadcrumbs-32${param._dc}">
			            
			            <li>
			               <a>胡迪新</a>
			               <ul>
			                   <li><a onclick="alert();">吴德福</a></li>
			                   <li><a>胡迪新</a></li>
			                   <li><a>陈晓哲</a></li>
			               </ul>
			            </li>
			        </ul>
                </div> -->
                
                    <div id='FHD.kpi.kpicategoryedit.view${param._dc}'></div>
            </body>
        
        </html>