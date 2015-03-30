/**
 * 添加指标类型时,采集计算报告面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpiTypeGatherPanel', {
    extend: 'Ext.form.Panel',
    /**
	 * @cfg {boolean} 是否显示边框
	 */
    border: false,
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel, finishflag) {
        var me = this;
        var jsobj = {};
        var form = me.getForm();
        var resultgatherfrequence = Ext.getCmp('resultgatherfrequence' + param_dc);
        var targetSetFrequence = Ext.getCmp('targetSetFrequence' + param_dc);
        var reportFrequence = Ext.getCmp('reportFrequence' + param_dc);
        var targetSetReportFrequence = Ext.getCmp('targetSetReportFrequence' + param_dc);
        var valueDictType = resultgatherfrequence.valueDictType;
        var valueRadioType = resultgatherfrequence.valueRadioType;
        var gatherValueCron = resultgatherfrequence.valueCron;
        var targetValueCron = targetSetFrequence.valueCron;
        var gatherreportCron = reportFrequence.valueCron;
        var targetReportCron = targetSetReportFrequence.valueCron;
        var targetSetFrequenceDictType = targetSetFrequence.valueDictType;
        var targetSetFrequenceRadioType = targetSetFrequence.valueRadioType;
        var reportFrequenceDictType = reportFrequence.valueDictType;
        var reportFrequenceRadioType = reportFrequence.valueRadioType;
        var targetSetReportFrequenceDictType = targetSetReportFrequence.valueDictType;
        var targetSetReportFrequenceRadioType = targetSetReportFrequence.valueRadioType;
        var resultformulaDict = Ext.getCmp('kpityperesultFormulaSelector' + param_dc).getRadioValue();
        var resultformula = Ext.getCmp('kpityperesultFormulaSelector' + param_dc).getTriggerValue();
        var targetformulaDict = Ext.getCmp('kpitypetargetFormulaSelector' + param_dc).getRadioValue();
        var targetformula = Ext.getCmp('kpitypetargetFormulaSelector' + param_dc).getTriggerValue();
        var assessmentformulaDict = Ext.getCmp('kpitypeassessmentFormulaSelector' + param_dc).getRadioValue();
        var assessmentformula = Ext.getCmp('kpitypeassessmentFormulaSelector' + param_dc).getTriggerValue();
        
        //添加预警公式和关联关系公式
        
        var kpitypealarmFormulaSelector = Ext.getCmp('kpitypealarmFormulaSelector' + param_dc);
        var kpityperelationFormulaSelector = Ext.getCmp('kpityperelationFormulaSelector' + param_dc);
        var alarmformulaDict = kpitypealarmFormulaSelector.getRadioValue();
        var alarmformula = kpitypealarmFormulaSelector.getTriggerValue();
        var relationformulaDict = kpityperelationFormulaSelector.getRadioValue();
        var relationformula = kpityperelationFormulaSelector.getTriggerValue();
        
        
        var resultSumMeasureStr = Ext.getCmp('resultSumMeasure'+param_dc).getValue();
        var targetSumMeasureStr = Ext.getCmp('targetSumMeasure'+param_dc).getValue();
        var assessmentSumMeasureStr = Ext.getCmp('assessmentSumMeasure'+param_dc).getValue();
        var scale = Ext.getCmp('scale'+param_dc).getValue();
        var resultCollectInterval = Ext.getCmp('resultCollectInterval'+param_dc).getValue();
        var targetSetInterval = Ext.getCmp('targetSetInterval'+param_dc).getValue();
        var relativeTo = Ext.getCmp('relativeTo'+param_dc).getValue();
        var modelValue = Ext.getCmp('modelValue'+param_dc).getValue();
        var maxValue = Ext.getCmp('maxValue'+param_dc).getValue();
        var minValue = Ext.getCmp('minValue'+param_dc).getValue();
        
        jsobj.id = currentId;
        jsobj.gatherfrequence = valueDictType;
        jsobj.gatherfrequenceRule = valueRadioType ;
        jsobj.targetSetFrequenceDictType = targetSetFrequenceDictType ;
        jsobj.targetSetFrequenceRadioType = targetSetFrequenceRadioType ;
        jsobj.reportFrequenceDictType = reportFrequenceDictType ;
        jsobj.reportFrequenceRadioType = reportFrequenceRadioType ;
        jsobj.targetSetReportFrequenceDictType = targetSetReportFrequenceDictType ;
        jsobj.targetSetReportFrequenceRadioType = targetSetReportFrequenceRadioType ;
        jsobj.gatherValueCron = gatherValueCron ;
        jsobj.targetValueCron = targetValueCron ;
        jsobj.gatherreportCron = gatherreportCron;
        jsobj.targetReportCron = targetReportCron;
        jsobj.resultformulaDict = resultformulaDict ;
        jsobj.resultformula = resultformula ;
        jsobj.targetformulaDict = targetformulaDict ;
        jsobj.targetformula = targetformula ;
        jsobj.assessmentformulaDict = assessmentformulaDict ;
        jsobj.assessmentformula = assessmentformula ;
        jsobj.resultSumMeasureStr = resultSumMeasureStr;
        jsobj.targetSumMeasureStr = targetSumMeasureStr;
        jsobj.assessmentSumMeasureStr = assessmentSumMeasureStr;
        jsobj.scale = scale;
        jsobj.resultCollectInterval = resultCollectInterval;
        jsobj.targetSetInterval = targetSetInterval;
        jsobj.relativeTo = relativeTo;
        jsobj.modelValue = modelValue;
        jsobj.maxValue = maxValue;
        jsobj.minValue = minValue;
        jsobj.targetSetFrequenceStr = targetSetFrequence.getValue();
        jsobj.reportFrequenceStr = reportFrequence.getValue();
        jsobj.resultgatherfrequence = resultgatherfrequence.getValue();
        jsobj.targetSetReportFrequenceStr = targetSetReportFrequence.getValue();
        
        /*jsobj.alarmformulaDict = alarmformulaDict;
        jsobj.alarmformula = alarmformula;
        jsobj.relationformulaDict = relationformulaDict;
        jsobj.relationformula = relationformula;*/
        
        
        
        if (form.isValid()) {
	        FHD.ajax({
	            url: __ctxPath + '/kpi/kpi/mergekpitypecal.f',
	            params: {
	                param: Ext.JSON.encode(jsobj)
	            },
	            callback: function (data) {
	                if (data && data.success) {
	                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                        if (!finishflag) {//如果点击的不是完成按钮,需要移动到下一个面板
                        	cardPanel.pageMove("next");
                            kpiTypeMainPanel._navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                            /*同时将告警设置按钮为可用状态*/
                            Ext.getCmp('alarmset_btn' + param_dc).setDisabled(false);
                            Ext.getCmp('alarmset_btn_top' + param_dc).setDisabled(false);
                        }else{
                        	kpiTypeMainPanel._gotopage();
                        }
                        
	                }
	            }
	        });
        }

    },
    /**
     * 初始化组件方法
     */
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            autoRender: false,
            autoScroll: true,
            border: me.border,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3",
            items: [{
                xtype: 'fieldset',//计算信息fieldset
                autoHeight:true,
                autoWidth:true,
                collapsible: true,
                defaults: {
                    margin: '15 30 15 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.cainfo'),
                items: [
				
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    column: 'resultValueFormula',
                    showType: 'all',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultFormula'),//结果值公式
                    targetId: '',
                    targetName: '',
                    columnWidth: 0.5,
                    formulaTypeName: 'isResultFormula',
                    formulaName: 'resultFormula',
                    id: 'kpityperesultFormulaSelector' + param_dc

                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    showType: 'all',
                    column: 'targetValueFormula',
                    targetId: '',
                    targetName: '',
                    formulaTypeName: 'isTargetFormula',
                    formulaName: 'targetFormula',
                    columnWidth: .5,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetFormula'),//目标值公式
                    id: 'kpitypetargetFormulaSelector' + param_dc
                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    showType: 'all',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula') + '<font color=red>*</font>',//评估值公式
                    column: 'assessmentValueFormula',
                    targetId: '',
                    targetName: '',
                    formulaTypeName: 'isAssessmentFormula',
                    formulaName: 'assessmentFormula',
                    columnWidth: .5,
                    allowBlank: false,
                    id: 'kpitypeassessmentFormulaSelector' + param_dc
                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    //column:'resultValueFormula',
                    showType: 'all',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmFormula'),//预警公式
                    targetId: '',
                    targetName: '',
                    columnWidth: 0.5,
                    formulaTypeName: 'isforecastFormula',
                    formulaName: 'forecastFormula',
                    id: 'kpitypealarmFormulaSelector' + param_dc

                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    showType: 'all',
                    //column:'targetValueFormula',
                    targetId: '',
                    targetName: '',
                    formulaTypeName: 'isrelationFormula',
                    formulaName: 'relationFormula',
                    columnWidth: .5,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relationFormula'),//关联关系公式
                    id: 'kpityperelationFormulaSelector' + param_dc
                }), {
                    xtype: 'textfield',
                    name: 'null_a',
                    value: '',
                    maxLength: 100,
                    columnWidth: .5,
                    hideMode: "visibility",
                    hidden: true
                }, {
                    xtype: 'numberfield',
                    step: 100,
                    name: 'modelValue',
                    id:'modelValue'+param_dc,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.modelValue'),//标杆值
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                }, {
                    xtype: 'textfield',
                    name: 'null_b',
                    value: '',
                    maxLength: 100,
                    columnWidth: .5,
                    hideMode: "visibility",
                    hidden: true
                },
                Ext.create('widget.dictselectforeditgrid', {
                	id:'resultSumMeasure'+param_dc,
                    editable: false,
                    labelWidth: 105,
                    labelAlign: 'left',
                    multiSelect: false,
                    name: 'resultSumMeasureStr',
                    dictTypeId: 'kpi_sum_measure',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultSumMeasure'),//结果值累计计算
                    defaultValue: 'kpi_sum_measure_sum',
                    columnWidth: .5
                }),
                Ext.create('widget.dictselectforeditgrid', {
                	id:'targetSumMeasure'+param_dc,
                    editable: false,
                    labelWidth: 105,
                    labelAlign: 'left',
                    multiSelect: false,
                    name: 'targetSumMeasureStr',
                    dictTypeId: 'kpi_sum_measure',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSumMeasure'),//目标值累计计算
                    defaultValue: 'kpi_sum_measure_sum',
                    columnWidth: .5
                }),
                Ext.create('widget.dictselectforeditgrid', {
                	id:'assessmentSumMeasure'+param_dc,
                    editable: false,
                    labelWidth: 105,
                    labelAlign: 'left',
                    multiSelect: false,
                    name: 'assessmentSumMeasureStr',
                    dictTypeId: 'kpi_sum_measure',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentMeasure'),//评估值累计计算
                    defaultValue: 'kpi_sum_measure_last',
                    columnWidth: .5
                }),
                
                {
                    xtype: 'numberfield',
                    step: 5,
                    name: 'maxValue',
                    id:'maxValue'+param_dc,
                    fieldLabel: FHD.locale.get('fhd.kpi.form.maxvalue'),//最大值
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                },
                {
                    xtype: 'numberfield',
                    step: 5,
                    name: 'minValue',
                    id:'minValue'+param_dc,
                    fieldLabel: FHD.locale.get('fhd.kpi.form.minvalue'),//最小值
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                }

                ]
            }, {
                xtype: 'fieldset',
                autoHeight:true,
                autoWidth:true,
                collapsible: true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.gatherset'),
                items: [{
                    columnWidth: .5,
                    id: 'resultgatherfrequence' + param_dc,
                    name: 'resultgatherfrequence',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.gatherfrequence') + '<font color=red>*</font>',//结果收集频率
                    valueDictType: '',
                    valueRadioType: '',
                    single: false,
                    value: '',
                    labelWidth: 105,
                    allowBlank: false,
                    columnWidth: .5
                }, {
                    columnWidth: .5,
                    id: 'targetSetFrequence' + param_dc,
                    name: 'targetSetFrequenceStr',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.targetSetFrequence'),//目标收集频率
                    valueDictType: '',
                    single: false,
                    value: '',
                    labelWidth: 105,
                    columnWidth: .5
                }, {
                	id:'resultCollectInterval'+param_dc,
                    xtype: 'numberfield',
                    step: 1,
                    maxValue: 5,
                    minValue: 1,
                    name: 'resultCollectIntervalStr',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultCollectInterval') + '<font color=red>*</font>',//结果收集延期天
                    maxLength: 100,
                    columnWidth: .5,
                    allowBlank: false,
                    value: '3'

                }, {
                    xtype: 'numberfield',
                    step: 1,
                    id:'targetSetInterval'+param_dc,
                    name: 'targetSetIntervalStr',
                    maxValue: 5,
                    minValue: 1,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSetInterval'),//目标收集延期天
                    value: '3',
                    maxLength: 100,
                    columnWidth: .5
                }, {
                    columnWidth: .5,
                    id: 'reportFrequence' + param_dc,
                    name: 'reportFrequenceStr',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.reportFrequence'),//结果收集报告频率
                    valueDictType: '',
                    single: false,
                    value: '',
                    labelWidth: 105
                }, {
                    columnWidth: .5,
                    id: 'targetSetReportFrequence' + param_dc,
                    name: 'targetSetReportFrequenceStr',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.targetSetReportFrequence'),//目标收集报告频率
                    valueDictType: '0frequecy_quarter', //字典类型
                    valueRadioType: '1', //单选项第几个
                    single: false,
                    value: '',
                    labelWidth: 105,
                    columnWidth: .5
                }]
            }, {
                xtype: 'fieldset',//报告设置fieldset
                collapsible: true,
                autoHeight:true,
                autoWidth:true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.reportset'),
                items: [{
                	id:'scale'+param_dc,
                    xtype: 'numberfield',
                    step: 1,
                    maxValue: 6,
                    minValue: 0,
                    name: 'scale',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.scale'),//报表小数点位置
                    value: '2',
                    columnWidth: .5
                },
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    id:'relativeTo'+param_dc,
                    name: 'relativeToStr',
                    dictTypeId: 'kpi_relative_to',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relativeto') + '<font color=red>*</font>',//趋势相对于
                    columnWidth: .5,
                    labelAlign: 'left',
                    allowBlank: false,
                    defaultValue: 'kpi_relative_to_previs'
                })
                
                ]
            }],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "false" || me.editflag.length == 0) {
                        //添加时,需要添加评估值的默认公式
                        Ext.getCmp('kpitypeassessmentFormulaSelector' + param_dc).setTriggerValue("#[myself~结果值]/#[myself~目标值]*100");
                        Ext.getCmp('kpitypeassessmentFormulaSelector' + param_dc).setRadioValue("0sys_use_formular_formula");
                        //设置采集频率默认值为按月采集
                        var resultgatherfrequence = Ext.getCmp('resultgatherfrequence' + param_dc);
                        resultgatherfrequence.valueDictType = "0frequecy_month";
                        resultgatherfrequence.valueRadioType = "5,星期五";
                        resultgatherfrequence.setValue("每月,期间的最后一个星期五,每1期间");
                        resultgatherfrequence.valueCron = "0 0 0 ? * 6L";
                        
                        var targetSetFrequence = Ext.getCmp('targetSetFrequence' + param_dc);
                        targetSetFrequence.valueDictType = "0frequecy_month";
                        targetSetFrequence.valueRadioType = "4,星期一";
                        targetSetFrequence.setValue("每月,期间的首个星期一,每1期间");
                        targetSetFrequence.valueCron = "0 0 0 ? * 2#1";
                        
                    }

                    if (me.editflag == "true") {
                    	//编辑时加载form数据
                        me.formLoad();
                        //给公式赋值
                        var id = me.currentId;
                        var name = me.kpitypename;
                        kpitypeValueToFormula(id, name);
                    }

                }
            }

        });


        me.callParent(arguments);
    },
    /**
     * 加载form数据
     */
    formLoad: function () {
        var me = this;
        me.form.waitMsgTarget = true;
        me.form.load({
            url: __ctxPath + '/kpi/Kpi/findkpitypecalculatetojson.f',
            params: {
                id: me.currentId
            },
            success: function (form, action) {
                var othervalue = action.result.othervalue;
                var resultgatherfrequence = Ext.getCmp('resultgatherfrequence' + param_dc);
                var targetSetFrequence = Ext.getCmp('targetSetFrequence' + param_dc);
                var reportFrequence = Ext.getCmp('reportFrequence' + param_dc);
                var targetSetReportFrequence = Ext.getCmp('targetSetReportFrequence' + param_dc);
                if (othervalue.targetSetFrequenceDictType) {
                    targetSetFrequence.valueDictType = othervalue.targetSetFrequenceDictType;
                }
                if (othervalue.targetSetFrequenceRule) {
                    targetSetFrequence.valueRadioType = othervalue.targetSetFrequenceRule;
                }
                if (othervalue.targetSetFrequence) {
                    targetSetFrequence.setValue(othervalue.targetSetFrequence);
                }
                if (othervalue.reportFrequenceDictType) {
                    reportFrequence.valueDictType = othervalue.reportFrequenceDictType;
                }
                if (othervalue.reportFrequenceRule) {
                    reportFrequence.valueRadioType = othervalue.reportFrequenceRule;
                }
                if (othervalue.reportFrequence) {
                    reportFrequence.setValue(othervalue.reportFrequence);
                }
                if (othervalue.targetSetReportFrequenceDictType) {
                    targetSetReportFrequence.valueDictType = othervalue.targetSetReportFrequenceDictType;
                }
                if (othervalue.targetSetReportFrequenceRule) {
                    targetSetReportFrequence.valueRadioType = othervalue.targetSetReportFrequenceRule;
                }
                if (othervalue.targetSetReportFrequence) {
                    targetSetReportFrequence.setValue(othervalue.targetSetReportFrequence);
                }
                if (othervalue.resultgatherfrequenceDictType) {
                    resultgatherfrequence.valueDictType = othervalue.resultgatherfrequenceDictType;
                }
                if (othervalue.resultgatherfrequenceRule) {
                    resultgatherfrequence.valueRadioType = othervalue.resultgatherfrequenceRule;
                }
                return true;
            },
            failure: function (form, action) {}
        });
    }

});