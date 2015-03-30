<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>添加指标</title>
                <script type="text/javascript">
                    var addkpi_view = (function () {
                    	var resultFormulaSelector = Ext.create('FHD.ux.kpi.FormulaSelector',{
  	                		type:'kpi',
  	                		column:'resultValueFormula',
  	                		showType:'all',
  	                		fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.resultFormula')+ '<font color=red>*</font>',
  	                		targetId:'',
  	                		targetName:'',
  	                		columnWidth:0.5,
  	                		formulaTypeName:'isResultFormula', 
  	                		formulaName:'resultFormula'
  	                		
  	                	});
  	                	var targetFormulaSelector = Ext.create('FHD.ux.kpi.FormulaSelector',{
							type:'kpi',
							showType:'all',
							column:'targetValueFormula',
							targetId:'',
							targetName:'',
							formulaTypeName:'isTargetFormula', 
  	                		formulaName:'targetFormula',
                          	columnWidth: .5,
                          	fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.targetFormula')+ '<font color=red>*</font>'
                         });
                                 	                	
                        var assessmentFormulaSelector = Ext.create('FHD.ux.kpi.FormulaSelector',{
							type:'kpi',
							showType:'all',
							fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula')+ '<font color=red>*</font>',
							column:'assessmentValueFormula',
							targetId:'',
							targetName:'',
							formulaTypeName:'isAssessmentFormula', 
  	                		formulaName:'assessmentFormula',
                        	columnWidth: .5
                        });
                        var alarmFormulaSelector = Ext.create('FHD.ux.kpi.FormulaSelector',{
  	                		type:'kpi',
  	                		//column:'resultValueFormula',
  	                		showType:'all',
  	                		fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.alarmFormula'),
  	                		targetId:'',
  	                		targetName:'',
  	                		columnWidth:0.5,
  	                		formulaTypeName:'isforecastFormula', 
  	                		formulaName:'forecastFormula'
  	                		
  	                	});
  	                	var relationFormulaSelector = Ext.create('FHD.ux.kpi.FormulaSelector',{
							type:'kpi',
							showType:'all',
							//column:'targetValueFormula',
							targetId:'',
							targetName:'',
							formulaTypeName:'isrelationFormula', 
  	                		formulaName:'relationFormula',
                          	columnWidth: .5,
                          	fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.relationFormula')
                         });
                        
                        var currentId = "${param.id}";
                        var editflag = "${param.editflag}";
                        var kpitypename = "${param.kpitypename}";
                        var parentCategoryid = "${param.parentCategoryid}";
                        var parentCategoryname = "${param.parentCategoryname}";
                        var kpitypeid = "${param.kpitypeid}";
                        var kpitypeselectFlag = "${param.kpitypeselectFlag}";
                        var opflag = "${param.opflag}";
                        var parentKpiId = "${param.parentKpiId}";
                        if (kpitypeselectFlag == "true") {
                            currentId = kpitypeid;
                        }

                        var categoryid = "${param.categoryid}";
                        var categoryname = "${param.categoryname}";
                        if (categoryid == "undefined") {
                            //加载根维度
                            loadRootCategory();
                        }
                        var warningUrl = __ctxPath + "/kpi/kpi/findkpirelaalarmbysome.f?id=" + currentId;
                        var otherDimMultiCombo = Ext.create('FHD.ux.dict.DictSelect', {
                            maxHeight: 60,
                            labelWidth: 105,
                            name: 'otherDim',
                            dictTypeId: 'kpi_dimension',
                            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'),
                            columnWidth: .5,
                            labelAlign: 'left',
                            multiSelect: true
                        });
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
                                id: 'delBtn${param._dc}' /*,text : FHD.locale.get('fhd.common.delete')*/
                                ,
                                iconCls: 'icon-del',
                                handler: warningDel,
                                disabled: true,
                                scope: this
                            }]
                        });
                        var authGrid = Ext.create('FHD.ux.EditorGridPanel', {
                            border: false,
                            pagable: false,
                            url: warningUrl,
                            height: FHD.getCenterPanelHeight() - 150,
                            cols: [{
                                dataIndex: 'id',
                                id: 'id',
                                width: 0
                            }, {
                                header: FHD.locale.get('fhd.kpi.kpi.form.name'),
                                dataIndex: 'name',
                                sortable: true,
                                flex: 2
                            }, {
                                header: FHD.locale.get('fhd.common.type'),
                                dataIndex: 'type',
                                sortable: true,
                                flex: 2
                            }, {
                                header: FHD.locale.get('fhd.sys.auth.authority.authority'),
                                dataIndex: 'auth',
                                sortable: true,
                                flex: 2
                            }],
                            tbarItems: [{ /*text : FHD.locale.get('fhd.common.add'),*/
                                id: 'authaddBtn${param._dc}',
                                iconCls: 'icon-add',
                                /*handler:addWarning, */
                                scope: this
                            }, '-', {
                                id: 'authupdBtn${param._dc}',
                                /*text : FHD.locale.get('fhd.common.modify'),*/
                                iconCls: 'icon-edit' /*, handler:warningDel*/
                                ,
                                disabled: true,
                                scope: this
                            }, '-', {
                                id: 'authdelBtn${param._dc}' /*,text : FHD.locale.get('fhd.common.delete')*/
                                ,
                                iconCls: 'icon-del' /*, handler:warningDel*/
                                ,
                                disabled: true,
                                scope: this
                            }]
                        });

                        function loadRootCategory() {
                            FHD.ajax({
                                url: __ctxPath + '/kpi/category/findcategoryroot.f',
                                callback: function (node) {
                                    if (node && node.id) {
                                        categoryid = node.id;
                                    }
                                }
                            });
                        }

                        function findFormDataById(id) {
                            addkpi_view.basicPanel.form.load({
                                waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                                url: __ctxPath + '/kpi/Kpi/findKpiByIdToJson.f',
                                params: {
                                    id: id
                                },
                                success: function (form, action) {
                                    var otherDimArray = action.result.data.otherDimArray;
                                    if (otherDimArray) {
                                        var arr = Ext.JSON.decode(otherDimArray);
                                        otherDimMultiCombo.setValue(arr);
                                    }
                                    if (action.result.data.parentKpiId) {
                                        parentKpiId = action.result.data.parentKpiId;
                                        Ext.getCmp("kpi_parentKpiID").initValue(parentKpiId);
                                    }
                                    if (opflag == "edit") {
                                        Ext.getCmp("kpi_parentKpiID").initValue(parentKpiId);
                                    }
                                    var form = addkpi_view.basicPanel.getForm();
                                    var vobj = form.getValues();
                                    var name = vobj.name;
                                    valuetoFormulaSelector(id,name);
                                    return true;
                                },
                                failure: function (form, action) {
                                    //alert("err");
                                }
                            });
                            addkpi_view.gatherPanel.form.load({
                                url: __ctxPath + '/kpi/Kpi/findkpitypecalculatetojson.f',
                                params: {
                                    id: id
                                },
                                success: function (form, action) {
                                	var othervalue = action.result.othervalue;
									var resultgatherfrequence = Ext.getCmp('kpi_resultgatherfrequence');
                                	
                                	var targetSetFrequence = Ext.getCmp('kpi_targetSetFrequence');
                                	var reportFrequence = Ext.getCmp('kpi_reportFrequence');
                                	var targetSetReportFrequence = Ext.getCmp('kpi_targetSetReportFrequence');
                                	
                                	if(othervalue.targetSetFrequenceDictType){
                                		targetSetFrequence.valueDictType = othervalue.targetSetFrequenceDictType;
                                	}
                                	if(othervalue.targetSetFrequenceRule){
                                		targetSetFrequence.valueRadioType = othervalue.targetSetFrequenceRule;
                                	}
                                	if(othervalue.targetSetFrequence){
                                		targetSetFrequence.setValue(othervalue.targetSetFrequence);
                                	}
                                	
                                	if(othervalue.reportFrequenceDictType){
                                		reportFrequence.valueDictType = othervalue.reportFrequenceDictType;
                                	}
                                	if(othervalue.reportFrequenceRule){
                                		reportFrequence.valueRadioType = othervalue.reportFrequenceRule;
                                	}
                                	if(othervalue.reportFrequence){
                                		reportFrequence.setValue(othervalue.reportFrequence);
                                	}
                                	
                                	if(othervalue.targetSetReportFrequenceDictType){
                                		targetSetReportFrequence.valueDictType = othervalue.targetSetReportFrequenceDictType;
                                	}
                                	if(othervalue.targetSetReportFrequenceRule){
                                		targetSetReportFrequence.valueRadioType = othervalue.targetSetReportFrequenceRule;
                                	}
                                	if(othervalue.targetSetReportFrequence){
                                		targetSetReportFrequence.setValue(othervalue.targetSetReportFrequence);
                                	}
                                	
                                	if(othervalue.resultgatherfrequenceDictType){
                                    	resultgatherfrequence.valueDictType = othervalue.resultgatherfrequenceDictType;
                                	}
                                	if(othervalue.resultgatherfrequenceRule){
                                    	resultgatherfrequence.valueRadioType = othervalue.resultgatherfrequenceRule;
                                	}
                                    return true;
                                },
                                failure: function (form, action) {
                                    //alert("err");
                                }
                            });
                        }

                        function createCode() {
                            var submitKpiId = currentId;
                            if (opflag == "edit") {
                                submitKpiId = fhd_kpi_kpicategorymgr_view.kpiid;
                            } else {
                                submitKpiId = "";
                            }
                            var vform = addkpi_view.basicPanel.getForm();
                            var vobj = vform.getValues();
                            var paraobj = {};
                            paraobj.id = submitKpiId;
                            if (vobj.parentStr != "") {
                                paraobj.parentid = vobj.parentKpiId;
                                FHD.ajax({
                                    url: __ctxPath + '/kpi/kpi/findcodebyparentid.f',
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

                        function formload(id) {
                            addkpi_view.basicPanel.form.load({
                                waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                                url: __ctxPath + '/kpi/Kpi/findKpiByIdToJson.f',
                                params: {
                                    id: id
                                },
                                success: function (form, action) {
                                    var otherDimArray = action.result.data.otherDimArray;
                                    if (otherDimArray) {
                                        var arr = Ext.JSON.decode(otherDimArray);
                                        otherDimMultiCombo.setValue(arr);
                                    }
                                    return true;
                                },
                                failure: function (form, action) {
                                    //alert("err");
                                }
                            });

                        }

                        function selectKpiType() {
                            addkpi_view.formwindow = new Ext.Window({
                                constrain: true,
                                layout: 'fit',
                                iconCls: 'icon-edit', //标题前的图片
                                modal: true, //是否模态窗口
                                collapsible: true,
                                scroll: 'auto',
                                width: 400,
                                height: 650,
                                maximizable: true, //（是否增加最大化，默认没有）
                                autoLoad: {
                                    url: 'pages/kpi/kpi/kpitypeselect.jsp',
                                    nocache: true,
                                    scripts: true
                                }
                            });
                            addkpi_view.formwindow.initialConfig.categoryid = categoryid;
                            addkpi_view.formwindow.initialConfig.parentCategoryid = parentCategoryid;
                            addkpi_view.formwindow.initialConfig.parentCategoryname = parentCategoryname;
                            addkpi_view.formwindow.initialConfig.categoryname = categoryname;
                            addkpi_view.formwindow.initialConfig.opflag = opflag;
                            addkpi_view.formwindow.initialConfig.parentKpiId = parentKpiId;
                            addkpi_view.formwindow.show();
                        }

                        function basicPanelLast() {
                        	//if(isheritFun()){
                        	//	readonlyComponet(false);
                        	//}
                            var form = addkpi_view.basicPanel.getForm();
							Ext.getCmp('kpiname').setDisabled(false);
                            var vobj = form.getValues();
                            var paramObj = {};
                            paramObj.name = vobj.name;
                            paramObj.code = vobj.code;
                            paramObj.categoryname = categoryname;
                            paramObj.type = "KPI";
                            var submitKpiId = currentId;
                            if (opflag == "edit") {
                                submitKpiId = fhd_kpi_kpicategorymgr_view.kpiid;
                            } else {
                                submitKpiId = "";
                            }
                            FHD.ajax({
                                url: __ctxPath + '/kpi/kpi/validate.f',
                                params: {
                                    id: submitKpiId,
                                    validateItem: Ext.JSON.encode(paramObj)
                                },
                                callback: function (data) {
                                    if (data && data.success && form.isValid()) {
                                        //提交指标信息
                                        var addUrl = __ctxPath + '/kpi/kpi/mergekpi.f?id=' + submitKpiId;
                                        FHD.submit({
                                            form: form,
                                            url: addUrl,
                                            callback: function (data) {
                                                if (data) {
                                                    currentId = data.id;
                                                    valuetoFormulaSelector(data.id,vobj.name);
                                                    //submitKpiId = currentId;
                                                    //Ext.getCmp('kpi_id').setValue(currentId)
                                                    addkpi_view.basicPanel.hide();
                                                    addkpi_view.gatherPanel.show();
                                                    addkpi_view.container.add(addkpi_view.gatherPanel);
                                                }
                                            }
                                        });
                                    } else {
                                        //校验失败信息
                                        if (data && data.error == "nameRepeat") {
                                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                                            Ext.getCmp('namedefault_false').checked = true;
                                            Ext.getCmp('namedefault_false').setValue(true);
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

                        function gatherPanelPre() {
                            addkpi_view.gatherPanel.hide();
                            addkpi_view.basicPanel.show();
                            addkpi_view.container.add(addkpi_view.basicPanel);
                        }

                        function gatherPanelLast() {
                            var form = addkpi_view.gatherPanel.getForm();
                            var resultgatherfrequence = Ext.getCmp('kpi_resultgatherfrequence');
                            var  targetSetFrequence = Ext.getCmp('kpi_targetSetFrequence');
                            var  reportFrequence = Ext.getCmp('kpi_reportFrequence');
                            var  targetSetReportFrequence  = Ext.getCmp('kpi_targetSetReportFrequence');
                            
                            var valueDictType = resultgatherfrequence.valueDictType;
                            var valueRadioType = resultgatherfrequence.valueRadioType;
                            var gatherValueCron = resultgatherfrequence.valueCron;
                            
                            var targetSetFrequenceDictType = targetSetFrequence.valueDictType;
                            var targetSetFrequenceRadioType = targetSetFrequence.valueRadioType;
                            
                            var reportFrequenceDictType = reportFrequence.valueDictType;
                            var reportFrequenceRadioType = reportFrequence.valueRadioType;
                            
                            var targetSetReportFrequenceDictType = targetSetReportFrequence.valueDictType;
                            var targetSetReportFrequenceRadioType = targetSetReportFrequence.valueRadioType;
                            
                            var resultformulaDict = resultFormulaSelector.getRadioValue();
                            var resultformula = resultFormulaSelector.getTriggerValue();
                            var targetformulaDict = targetFormulaSelector.getRadioValue();
                            var targetformula = targetFormulaSelector.getTriggerValue();
                            
                            
                            var assessmentformulaDict = assessmentFormulaSelector.getRadioValue();
                            var assessmentformula = assessmentFormulaSelector.getTriggerValue();
                            
                            
                            var addUrl = __ctxPath + '/kpi/kpi/mergekpitypecalculate.f?id=' + currentId+"&gatherfrequence="
                            		+valueDictType+"&gatherfrequenceRule="+encodeURIComponent(valueRadioType)
                            		+"&targetSetFrequenceDictType="+targetSetFrequenceDictType+"&targetSetFrequenceRadioType="+targetSetFrequenceRadioType
                            		+"&reportFrequenceDictType="+reportFrequenceDictType+"&reportFrequenceRadioType="+reportFrequenceRadioType
                            		+"&targetSetReportFrequenceDictType="+targetSetReportFrequenceDictType
                            		+"&targetSetReportFrequenceRadioType="+targetSetReportFrequenceRadioType
                            		+"&gatherValueCron="+gatherValueCron
                            		+"&resultformulaDict="+resultformulaDict+"&resultformula="+encodeURIComponent(resultformula)
                            		+"&targetformulaDict="+targetformulaDict+"&targetformula="+encodeURIComponent(targetformula)
                            		+"&assessmentformulaDict="+assessmentformulaDict+"&assessmentformula="+encodeURIComponent(assessmentformula);
                            
                            if(form.isValid()){
	                            FHD.submit({
	                                form: form,
	                                url: addUrl,
	                                callback: function (data) {
	                                    if (data) {
	                                        addkpi_view.warningPanel.add(warningGrid);
	                                        addkpi_view.gatherPanel.hide();
	                                        addkpi_view.warningPanel.show();
	                                        addkpi_view.container.add(addkpi_view.warningPanel);
	                                    }
	                                }
	                            });
                            }
                        }

                        function authPanelPre() {
                            addkpi_view.authPanel.hide();
                            addkpi_view.warningPanel.show();
                            addkpi_view.container.add(addkpi_view.warningPanel);
                        }

                        function authPanelLast() {
                            var rightUrl = __ctxPath + "/pages/kpi/kpi/kpicategoryedit.jsp?" + "parentid=" + parentCategoryid + "&editflag=true" + "&id=" + categoryid + "&parentname=" + encodeURIComponent(parentCategoryname);
                            fhd_kpi_kpicategorymgr_view.initRightPanel(rightUrl);
                        }

                        function warningPanelPre() {
                            addkpi_view.warningPanel.hide();
                            addkpi_view.gatherPanel.show();
                            addkpi_view.container.add(addkpi_view.gatherPanel);
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
                                    url: __ctxPath + '/kpi/kpi/mergekpirelaalarm.f',
                                    params: {
                                        modifiedRecord: Ext.JSON.encode(jsonArray),
                                        id: currentId
                                    },
                                    callback: function (data) {
                                        if (data && data.success) {
                                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                            //跳转到基本信息页
                                            addkpi_view.authPanel.add(authGrid);
                                            addkpi_view.warningPanel.hide();
                                            addkpi_view.authPanel.show();
                                            addkpi_view.container.add(addkpi_view.authPanel);
                                        }
                                    }
                                });

                            }
                            warningGrid.store.commitChanges();
                        }

                        function onchange() {
                            warningGrid.down('#delBtn${param._dc}').setDisabled(warningGrid.getSelectionModel().getSelection().length === 0);
                        }

                        function loadKpiRoot() {
                            FHD.ajax({
                                url: __ctxPath + '/kpi/kpi/findrootkpi',
                                params: {},
                                callback: function (data) {
                                    if (data && data.success) {
                                        Ext.getCmp("kpi_parentKpiID").initValue(data.rootid);
                                    }
                                }
                            });
                        }
                        
                        function readonlyComponet(isreadonly){
                        	if(isreadonly){//只读
                        		var assinfoSet = Ext.getCmp('assinfo');
                        		assinfoSet.setDisabled(true);
                        		var reportSet = Ext.getCmp('reportSet');
                        		reportSet.setDisabled(true);
                        		var gatherSet = Ext.getCmp('gatherSet');
                        		gatherSet.setDisabled(true);
                        		var cainfoSet = Ext.getCmp('cainfoSet');
                        		cainfoSet.setDisabled(true);
                        		warningGrid.setDisabled(true);
                        		authGrid.setDisabled(true);
                        		
                        		
                        		
                        		
                        	}else{
                        		var assinfoSet = Ext.getCmp('assinfo');
                        		assinfoSet.setDisabled(false);
                        		var reportSet = Ext.getCmp('reportSet');
                        		reportSet.setDisabled(false);
                        		var gatherSet = Ext.getCmp('gatherSet');
                        		gatherSet.setDisabled(false);
                        		var cainfoSet = Ext.getCmp('cainfoSet');
                        		cainfoSet.setDisabled(false);
                        		warningGrid.setDisabled(false);
                        		authGrid.setDisabled(false);
                        		
                        	}
                        	
                        }
                       	
                        function isheritFun(){
                        	var isherit = Ext.getCmp('isherit');
                        	var isheritItems = isherit.items;
                        	for(var i = 0; i < isheritItems.length; i++){
								if(isheritItems.items[i].checked){
									if(isheritItems.items[i].inputValue == '0yn_y'){
										return true;
									}else{
										return false;
									}
								}
							}
                        }
                        
                        function disableComponet(){
                        	if(isheritFun()){
                        		readonlyComponet(true);
                        	}else{
                        		readonlyComponet(false);
                        	}
                        }
                        
                        function valuetoFormulaSelector(id,name){
                        	resultFormulaSelector.setTargetId(id);
                        	resultFormulaSelector.setTargetName(name);
                        	targetFormulaSelector.setTargetId(id);
                        	targetFormulaSelector.setTargetName(name);
                        	assessmentFormulaSelector.setTargetId(id);
                        	assessmentFormulaSelector.setTargetName(name);
                        }

                        Ext.define('addkpi.view', {
                            basicPanel: null,
                            gatherPanel: null,
                            warningPanel: null,
                            formwindow: null,
                            load: function (id) {
                                formload(id);
                            },
                            addListeners: function () {
                                warningGrid.store.on('load', onchange); //执行store.load()时改变按钮可用状态
                                warningGrid.on('selectionchange', onchange); //选择记录发生改变时改变按钮可用状态
                                warningGrid.store.on('update', onchange); //修改时改变按钮可用状态
                            },
                            resizeFieldset: function (size) {
                                var fieldItems = this.basicPanel.items.items;
                                Ext.Array.each(fieldItems, function (fieldobj) {
                                    fieldobj.setWidth(size);
                                });
                            },
                            init: function () {
                                this.basicPanel = Ext.create('Ext.form.Panel', {
                                    waitMsgTarget: true,
                                    autoScroll: true,
                                    border: false,
                                    layout: 'column',
                                    width: FHD.getCenterPanelWidth() - 280,
                                    bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                    bbar: {
                                        style: 'background-image:url() !important;background-color:rgb(250,250,250);',
                                        items: [{
                                            xtype: 'tbtext',
                                            text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.common.details') + '</b></font>&nbsp;&rArr;&rArr;&nbsp;<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.caculate") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.sys.auth.authority.authority") + '</b></font>'
                                        }, '->', {
                                            text: FHD.locale.get("fhd.kpi.kpi.form.back"),
                                            iconCls: 'icon-control-rewind-blue',
                                            handler: function () {
                                                authPanelLast();
                                            }
                                        }, {
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
                                        width: FHD.getCenterPanelWidth() - 300,
                                        defaults: {
                                            margin: '7 30 3 30',
                                            labelWidth: 105
                                        },
                                        layout: {
                                            type: 'column'
                                        },
                                        title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
                                        items: [

                                        {
                                            xtype: 'hidden',
                                            hidden: true,
                                            name: 'kpitypeid',
                                            value: kpitypeid
                                        }, {
                                            xtype: 'hidden',
                                            hidden: true,
                                            name: 'opflag',
                                            value: opflag
                                        }, {
                                            xtype: 'hidden',
                                            hidden: true,
                                            name: 'id',
                                            value: currentId,
                                            id:'kpi_id'
                                        }, {
                                            xtype: 'hidden',
                                            hidden: true,
                                            name: 'categoryId',
                                            value: categoryid
                                        }, {
                                            xtype: 'textfield',
                                            disabled: true,
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'),
                                            margin: '7 3 3 30',
                                            name: 'kpitype',
                                            maxLength: 255,
                                            columnWidth: .4,
                                            value: kpitypename
                                        }, {
                                            xtype: 'button',
                                            margin: '7 30 3 3',
                                            text: FHD.locale.get('fhd.common.select'),
                                            handler: selectKpiType,
                                            columnWidth: .1
                                        }, {
                                        	id:'isherit',
                                            xtype: 'radiogroup',
                                            width: 105,
                                            name: 'isInheritStr',
                                            columns: 4,
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.herit'),
                                            defaultValue: '0yn_y',
                                            labelAlign: 'left',
                                            allowBlank: false,
                                            columnWidth: .5,
                                            items: [{
                                                boxLabel: FHD.locale.get('fhd.common.false'),
                                                name: 'isInheritStr',
                                                inputValue: '0yn_n'
                                            }, {
                                                boxLabel: FHD.locale.get('fhd.common.true'),
                                                name: 'isInheritStr',
                                                inputValue: '0yn_y',
                                                checked: true
                                            }]
                                           ,listeners: {
        										  click: {
        									          element: 'el',
        									          fn: function(){
        								      			var isherit = Ext.getCmp('isherit');
        								      			
        								      			for(var i = 0; i < isherit.items.length; i++){
  														if(isherit.items.items[i].checked){
  															if(isherit.items.items[i].inputValue == '0yn_n'){
  																//readonlyComponet(false);
  															}else if(isherit.items.items[i].inputValue == '0yn_y'){
  																//readonlyComponet(true);
  																
  															}
  														}
  													}
        													
        									          }
        								      	  }
        									  }
                                        }, 
                                        {
                                            id:'namedefault',
                                            xtype: 'radiogroup',
                                            columns: 4,
                                            fieldLabel: "默认名称",
                                            defaultValue: '0yn_y',
                                            labelAlign: 'left',
                                            allowBlank: false,
                                            columnWidth: .5,
                                            items: [
											{
												id:'namedefault_true',
											    boxLabel: FHD.locale.get('fhd.common.true'),
											    name: 'namedefault',
											    inputValue: '0yn_y',
											    checked: true
											},
                                            {
												id:'namedefault_false',
                                                boxLabel: FHD.locale.get('fhd.common.false'),
                                                name: 'namedefault',
                                                inputValue: '0yn_n'
                                            }],
                                        listeners: {
      										  click: {
      									          element: 'el',
      									          fn: function(){
      								      			var namedefault = Ext.getCmp('namedefault');
      								      			
      								      			for(var i = 0; i < namedefault.items.length; i++){
														if(namedefault.items.items[i].checked){
															if(namedefault.items.items[i].id == 'namedefault_true'){
																Ext.getCmp('namedefault_false').checked = false;
																Ext.getCmp('kpiname').setDisabled(true);
															}else if(namedefault.items.items[i].id == 'namedefault_false'){
																Ext.getCmp('namedefault_true').checked = false;
																Ext.getCmp('kpiname').setDisabled(false);
																
																
															}
														}
													}
      													
      									          }
      								      	  }
      									  }
                                        },

                                        {
                                            id:'kpiname',
                                        	xtype: 'textareafield',
                                            rows: 3,
                                            labelAlign: 'left',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>',
                                            name: 'name',
                                            maxLength: 255,
                                            columnWidth: .5,
                                            allowBlank: false,
                                            disabled: true
                                        }, 
                                        {
                                            id: 'kpi_parentKpiID',
                                            xtype: 'kpiselector',
                                            extraParams: {
                                                smIconType: 'display',
                                                canChecked: true
                                            },
                                            name: 'parentKpiId',
                                            single: false,
                                            //value:'1',
                                            height: 45,
                                            labelText: FHD.locale.get('fhd.kpi.kpi.form.parentKpi'),
                                            labelAlign: 'left',
                                            columnWidth: .5
                                        },
                                         {
                                            xtype: 'textareafield',
                                            rows: 3,
                                            labelAlign: 'left',
                                            name: 'desc',
                                            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),
                                            maxLength: 2000,
                                            columnWidth: .5
                                        }, {
                                            xtype: 'textfield',
                                            name: 'shortName',
                                            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'),
                                            value: '',
                                            maxLength: 255,
                                            columnWidth: .5
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
                                        }

                                        ,
                                        Ext.create('FHD.ux.org.CommonSelector', {
                                            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>',
                                            labelAlign: 'left',
                                            columnWidth: .5,
                                            name: 'ownDept',
                                            multiSelect: false,
                                            type: 'dept_emp',
                                            labelWidth: 105
                                            ,allowBlank: false
                                        }), {
                                            xtype: 'textfield',
                                            name: 'null',
                                            value: '',
                                            maxLength: 100,
                                            columnWidth: .5,
                                            hideMode: "visibility",
                                            hidden: true
                                        },
                                        Ext.create('FHD.ux.org.CommonSelector', {
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.gatherdept') + '<font color=red>*</font>',
                                            labelAlign: 'left',
                                            columnWidth: .5,
                                            name: 'gatherDept',
                                            multiSelect: false,
                                            type: 'dept_emp',
                                            labelWidth: 105
                                            ,allowBlank: false
                                        }), Ext.create('FHD.ux.org.CommonSelector', {
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetdept') + '<font color=red>*</font>',
                                            labelAlign: 'left',
                                            columnWidth: .5,
                                            name: 'targetDept',
                                            multiSelect: false,
                                            type: 'dept_emp',
                                            labelWidth: 105
                                            ,allowBlank: false
                                        }), Ext.create('FHD.ux.org.CommonSelector', {
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.reportdept'),
                                            growMax: 60,
                                            labelAlign: 'left',
                                            columnWidth: .5,
                                            name: 'reportDept',
                                            multiSelect: true,
                                            type: 'dept_emp',
                                            maxHeight: 60,
                                            labelWidth: 105
                                            //,allowBlank: false
                                        }), Ext.create('FHD.ux.org.CommonSelector', {
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.viewdept'),
                                            growMax: 60,
                                            labelAlign: 'left',
                                            columnWidth: .5,
                                            name: 'viewDept',
                                            multiSelect: true,
                                            type: 'dept_emp',
                                            maxHeight: 60,
                                            labelWidth: 105
                                        })



                                        ]
                                    }, , {
                                    	id:'assinfo',
                                        xtype: 'fieldset',
                                        width: FHD.getCenterPanelWidth() - 300,
                                        collapsible: true,
                                        defaults: {
                                            margin: '7 30 3 30',
                                            labelWidth: 105
                                        },
                                        layout: {
                                            type: 'column'
                                        },
                                        title: FHD.locale.get('fhd.kpi.kpi.form.assinfo'),
                                        items: [{
                                            xtype: 'dictradio',
                                            labelWidth: 105,
                                            name: 'statusStr',
                                            columns: 4,
                                            dictTypeId: '0yn',
                                            fieldLabel: FHD.locale.get('fhd.common.enable'),
                                            defaultValue: '0yn_y',
                                            labelAlign: 'left',
                                            allowBlank: false,
                                            columnWidth: .5
                                        }, {
                                            xtype: 'radiogroup',
                                            width: 105,
                                            name: 'monitorStr',
                                            columns: 4,
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.ismonitor'),
                                            defaultValue: '0yn_y',
                                            labelAlign: 'left',
                                            allowBlank: false,
                                            columnWidth: .5,
                                            items: [{
                                                boxLabel: FHD.locale.get('fhd.common.false'),
                                                name: 'monitorStr',
                                                inputValue: '0yn_n'
                                            }, {
                                                boxLabel: FHD.locale.get('fhd.common.true'),
                                                name: 'monitorStr',
                                                inputValue: '0yn_y',
                                                checked: true
                                            }]
                                        },
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            multiSelect: false,
                                            name: 'unitsStr',
                                            dictTypeId: '0units',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.unit'),
                                            columnWidth: .5,
                                            labelAlign: 'left',
                                            labelWidth: 105
                                        }), {
                                            xtype: 'datefield',
                                            format: 'Y-m-d',
                                            name: 'startDateStr',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.startdate') + '<font color=red>*</font>',
                                            columnWidth: .5 ,allowBlank: false
                                        },
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'typeStr',
                                            dictTypeId: 'kpi_etype',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'),
                                            columnWidth: .5,
                                            labelAlign: 'left'
                                        }), Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'kpiTypeStr',
                                            dictTypeId: 'kpi_kpi_type',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.type'),
                                            columnWidth: .5,
                                            labelAlign: 'left'
                                        }), Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'alarmMeasureStr',
                                            dictTypeId: 'kpi_alarm_measure',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmMeasure'),
                                            columnWidth: .5,
                                            labelAlign: 'left'
                                        }), Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'alarmBasisStr',
                                            dictTypeId: 'kpi_alarm_basis',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmBasis'),
                                            columnWidth: .5,
                                            labelAlign: 'left'
                                        }), Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'mainDim',
                                            dictTypeId: 'kpi_dimension',
                                            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'),
                                            columnWidth: .5,
                                            labelAlign: 'left'
                                        }), otherDimMultiCombo, {
                                            xtype: 'textfield',
                                            name: 'targetValueAlias',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'),
                                            value: '',
                                            maxLength: 255,
                                            columnWidth: .5
                                        }, {
                                            xtype: 'textfield',
                                            name: 'resultValueAlias',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias'),
                                            value: '',
                                            maxLength: 255,
                                            columnWidth: .5
                                        }




                                        ]
                                    }

                                    ]
                                });
                                this.gatherPanel = Ext.create('Ext.form.Panel', {
                                    waitMsgTarget: true,
                                    autoScroll: true,
                                    border: false,
                                    layout: 'column',
                                    width: FHD.getCenterPanelWidth() - 280,
                                    bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                    bbar: {
                                        style: 'background-image:url() !important;background-color:rgb(250,250,250);',
                                        items: [{
                                            xtype: 'tbtext',
                                            text: '<font color="#cccccc"><b>' + FHD.locale.get('fhd.common.details') + '</b></font>&nbsp;&rArr;&rArr;&nbsp;<font color="#3980F4"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.caculate") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.sys.auth.authority.authority") + '</b></font>'
                                        }, '->', {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
                                            iconCls: 'icon-control-rewind-blue',
                                            handler: function () {
                                                gatherPanelPre();
                                            }
                                        }, {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                                            iconCls: 'icon-control-fastforward-blue',
                                            handler: function () {
                                                gatherPanelLast();
                                            }
                                        }]
                                    },
                                    bodyPadding: 5,
                                    items: [{
                                    	id:'cainfoSet',
                                        xtype: 'fieldset',
                                        collapsible: true,
                                        width: FHD.getCenterPanelWidth() - 300,
                                        defaults: {
                                            margin: '7 30 3 30',
                                            labelWidth: 105
                                        },
                                        layout: {
                                            type: 'column'
                                        },
                                        title: FHD.locale.get('fhd.kpi.kpi.form.cainfo'),
                                        items: [
                                        
										resultFormulaSelector,
										targetFormulaSelector,
										assessmentFormulaSelector,
										alarmFormulaSelector,
										relationFormulaSelector,
										{
											xtype: 'numberfield',
	                                        step: 100,
                                            name: 'modelValue',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.modelValue'),
                                            value: '',
                                            maxLength: 255,
                                            columnWidth: .5
                                        },
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            labelAlign: 'left',
                                            multiSelect: false,
                                            name: 'resultSumMeasureStr',
                                            dictTypeId: 'kpi_sum_measure',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultSumMeasure'),
                                            defaultValue: 'kpi_sum_measure_sum',
                                            columnWidth: .5
                                        }),
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            labelAlign: 'left',
                                            multiSelect: false,
                                            name: 'targetSumMeasureStr',
                                            dictTypeId: 'kpi_sum_measure',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSumMeasure'),
                                            defaultValue: 'kpi_sum_measure_sum',
                                            columnWidth: .5
                                        }),
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            labelAlign: 'left',
                                            multiSelect: false,
                                            name: 'assessmentSumMeasureStr',
                                            dictTypeId: 'kpi_sum_measure',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentMeasure'),
                                            defaultValue: 'kpi_sum_measure_avg',
                                            columnWidth: .5
                                        })


                                        ]
                                    },  {
                                    	id:'gatherSet',
                                        xtype: 'fieldset',
                                        width: FHD.getCenterPanelWidth() - 300,
                                        collapsible: true,
                                        defaults: {
                                            margin: '7 30 3 30',
                                            labelWidth: 105
                                        },
                                        layout: {
                                            type: 'column'
                                        },
                                        title: FHD.locale.get('fhd.kpi.kpi.form.gatherset'),
                                        items: [
                                        
                                                {
                                        	columnWidth: .5,
                                        	id : 'kpi_resultgatherfrequence',
                                        	name:'resultgatherfrequence',
                                            xtype: 'collectionSelector',
                                            label : FHD.locale.get('fhd.kpi.kpi.form.gatherfrequence')+ '<font color=red>*</font>',
                                            dictType : '',
                                            single:false,
                                            value:'',
                                            labelWidth: 105,
                                            allowBlank: false
                                		}
                                        ,{
                                        	columnWidth: .5,
                                        	id : 'kpi_targetSetFrequence',
                                        	name:'targetSetFrequenceStr',
                                            xtype: 'collectionSelector',
                                            label : FHD.locale.get('fhd.kpi.kpi.form.targetSetFrequence'),
                                            dictType : '',
                                            single:false,
                                            value:'',
                                            labelWidth: 105
                                		}
                                        , {
                                       		xtype: 'numberfield',
                                           	step: 1,
                                           	maxValue: 5,
                                           	minValue: 1,
                                            name: 'resultCollectIntervalStr',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultCollectInterval')+'<font color=red>*</font>',
                                            maxLength: 100,
                                            columnWidth: .5,
                                            allowBlank: false,
                                            value: '5'
                                        }, {
                                        	xtype: 'numberfield',
                                            step: 1,
                                            maxValue: 5,
                                           	minValue: 1,
                                            name: 'targetSetIntervalStr',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSetInterval'),
                                            maxLength: 100,
                                            columnWidth: .5,
                                            value: '5'
                                        }, 
                                        
                                        {
                                        	columnWidth: .5,
                                        	id : 'kpi_reportFrequence',
                                        	name:'reportFrequenceStr',
                                            xtype: 'collectionSelector',
                                            label : FHD.locale.get('fhd.kpi.kpi.form.reportFrequence'),
                                            dictType : '',
                                            single:false,
                                            value:'',
                                            labelWidth: 105
                                		},
                                		{
                                        	columnWidth: .5,
                                        	id : 'kpi_targetSetReportFrequence',
                                        	name:'targetSetReportFrequenceStr',
                                            xtype: 'collectionSelector',
                                            label : FHD.locale.get('fhd.kpi.kpi.form.targetSetReportFrequence'),
                                            dictType : '',
                                            single:false,
                                            value:'',
                                            labelWidth: 105
                                		}
                                		]
                                    }, {
                                    	id:'reportSet',
                                        xtype: 'fieldset',
                                        collapsible: true,
                                        width: FHD.getCenterPanelWidth() - 300,
                                        defaults: {
                                            margin: '7 30 3 30',
                                            labelWidth: 105
                                        },
                                        layout: {
                                            type: 'column'
                                        },
                                        title: FHD.locale.get('fhd.kpi.kpi.form.reportset'),
                                        items: [{
                                            xtype: 'numberfield',
                                            step: 1,
                                            maxValue: 6,
                                            minValue: 0,
                                            name: 'scale',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.scale'),
                                            value: '2',
                                            columnWidth: .5
                                        },
                                        Ext.create('widget.dictselectforeditgrid', {
                                            editable: false,
                                            labelWidth: 105,
                                            multiSelect: false,
                                            name: 'relativeToStr',
                                            dictTypeId: 'kpi_relative_to',
                                            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relativeto'),
                                            columnWidth: .5,
                                            labelAlign: 'left',
                                            defaultValue: 'kpi_relative_to_previs'
                                        })]
                                    } ]
                                });

                                this.authPanel = Ext.create('Ext.panel.Panel', {
                                    border: false,
                                    layout: "fit",
                                    items: [],
                                    bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                    bbar: {
                                        style: 'background-image:url() !important;background-color:rgb(250,250,250);',
                                        items: [{
                                            xtype: 'tbtext',
                                            text: '<font color="#cccccc"><b>' + FHD.locale.get('fhd.common.details') + '</b></font>&nbsp;&rArr;&rArr;&nbsp;<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.caculate") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#3980F4"><b>' + FHD.locale.get("fhd.sys.auth.authority.authority") + '</b></font>'
                                        }, '->', {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
                                            iconCls: 'icon-control-rewind-blue',
                                            handler: function () {
                                                authPanelPre();
                                            }
                                        }, {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                                            iconCls: 'icon-control-stop-blue',
                                            handler: function () {
                                                authPanelLast();
                                            }
                                        }]
                                    }
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
                                            text: '<font color="#cccccc"><b>' + FHD.locale.get('fhd.common.details') + '</b></font>&nbsp;&rArr;&rArr;&nbsp;<font color="#cccccc"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.caculate") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#3980F4"><b>' + FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset") + '</b></font>&nbsp;&rArr;&rArr;&nbsp;' + '<font color="#cccccc"><b>' + FHD.locale.get("fhd.sys.auth.authority.authority") + '</b></font>'
                                        }, '->', {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
                                            iconCls: 'icon-control-rewind-blue',
                                            handler: function () {
                                                warningPanelPre();
                                            }
                                        }, {
                                            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                                            iconCls: 'icon-control-stop-blue',
                                            handler: function () {
                                                warningPanelLast();
                                            }
                                        }]
                                    }
                                });

                                this.container = Ext.create('Ext.panel.Panel', {
                                    boder: false,
                                    renderTo: 'addkpi${param._dc}',
                                    layout: "fit",
                                    height: FHD.getCenterPanelHeight(),
                                    items: [this.basicPanel]
                                });

                                if (editflag == "true") {
                                    /*加载form数据*/
                                    findFormDataById(currentId);
                                    //disableComponet();
                                }

                                this.addListeners();
								/*指标没有根节点
                                if (opflag == "add") {
                                    loadKpiRoot();
                                }
								*/
                            }

                        });

                        var addkpi_view = Ext.create('addkpi.view');
                        return addkpi_view;

                    })();

                    Ext.onReady(function () {
                        addkpi_view.init();
                    })


                     Ext.apply(Ext.form.field.VTypes, {

                    });
                </script>
            </head>
            
            <body>
                <div id='addkpi${param._dc}'></div>
            </body>
        
        </html>