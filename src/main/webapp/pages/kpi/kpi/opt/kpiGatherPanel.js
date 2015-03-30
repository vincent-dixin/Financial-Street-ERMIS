/**
 * 添加指标时,采集计算报告面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */

Ext.define('Ext.kpi.kpi.opt.kpiGatherPanel', {
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
        var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' + kpi_kpi_paramdc);
        var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' + kpi_kpi_paramdc);
        var reportFrequence = Ext.getCmp('kpi_kpi_reportFrequence' + kpi_kpi_paramdc);
        var targetSetReportFrequence = Ext.getCmp('kpi_kpi_targetSetReportFrequence' + kpi_kpi_paramdc);
        var valueDictType = resultgatherfrequence.valueDictType;
        var valueRadioType = resultgatherfrequence.valueRadioType;
        //alert("valueRadioType==>"+valueRadioType);
        var gatherValueCron = resultgatherfrequence.valueCron;
        //alert("gatherValueCron==>"+gatherValueCron);
        var targetValueCron = targetSetFrequence.valueCron;

        var gatherreportCron = reportFrequence.valueCron;
        var targetReportCron = targetSetReportFrequence.valueCron;
        var targetSetFrequenceDictType = targetSetFrequence.valueDictType;
        var targetSetFrequenceRadioType = targetSetFrequence.valueRadioType;
        //alert("targetSetFrequenceRadioType==>"+targetSetFrequenceRadioType);
        var reportFrequenceDictType = reportFrequence.valueDictType;
        var reportFrequenceRadioType = reportFrequence.valueRadioType;
        var targetSetReportFrequenceDictType = targetSetReportFrequence.valueDictType;
        var targetSetReportFrequenceRadioType = targetSetReportFrequence.valueRadioType;
        var resultformulaDict = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' + kpi_kpi_paramdc).getRadioValue();
        var resultformula = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' + kpi_kpi_paramdc).getTriggerValue();
        var targetformulaDict = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' + kpi_kpi_paramdc).getRadioValue();
        var targetformula = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' + kpi_kpi_paramdc).getTriggerValue();
        var assessmentformulaDict = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc).getRadioValue();
        var assessmentformula = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc).getTriggerValue();
        var resultSumMeasureStr = Ext.getCmp('kpi_kpi_resultSumMeasure'+kpi_kpi_paramdc).getValue();
        var targetSumMeasureStr = Ext.getCmp('kpi_kpi_targetSumMeasure'+kpi_kpi_paramdc).getValue();
        var assessmentSumMeasureStr = Ext.getCmp('kpi_kpi_assessmentSumMeasure'+kpi_kpi_paramdc).getValue();
        var scale = Ext.getCmp('kpi_kpi_scale'+kpi_kpi_paramdc).getValue();
        var resultCollectInterval = Ext.getCmp('kpi_kpi_resultCollectInterval'+kpi_kpi_paramdc).getValue();
        var targetSetInterval = Ext.getCmp('kpi_kpi_targetSetInterval'+kpi_kpi_paramdc).getValue();
        var relativeTo = Ext.getCmp('kpi_kpi_relativeTo'+kpi_kpi_paramdc).getValue();
        var modelValue = Ext.getCmp('kpi_kpi_modelValue'+kpi_kpi_paramdc).getValue();
        var maxValue = Ext.getCmp('maxValue'+kpi_kpi_paramdc).getValue();
        var minValue = Ext.getCmp('minValue'+kpi_kpi_paramdc).getValue();
        
        jsobj.id = kpiBasicPanel.kpiId;
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
                            kpiMainPanel._navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                            /*同时将告警设置按钮为可用状态*/
                            Ext.getCmp('kpi_kpi_alarmset_btn' + kpi_kpi_paramdc).setDisabled(false);
                            Ext.getCmp('kpi_kpi_alarmset_btn_top' + kpi_kpi_paramdc).setDisabled(false);
                        }else{
                        	kpiMainPanel._gotopage();
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
                collapsible: true,
                autoHeight:true,
                autoWidth:true,
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
                    id: 'kpi_kpi_kpityperesultFormulaSelector' + kpi_kpi_paramdc

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
                    id: 'kpi_kpi_kpitypetargetFormulaSelector' + kpi_kpi_paramdc
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
                    id: 'kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc
                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    showType: 'all',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmFormula'),//预警公式
                    targetId: '',
                    targetName: '',
                    columnWidth: 0.5,
                    formulaTypeName: 'isforecastFormula',
                    formulaName: 'forecastFormula',
                    id: 'kpi_kpi_kpitypealarmFormulaSelector' + kpi_kpi_paramdc

                }),
                Ext.create('FHD.ux.kpi.FormulaSelector', {
                    type: 'kpi',
                    showType: 'all',
                    targetId: '',
                    targetName: '',
                    formulaTypeName: 'isrelationFormula',
                    formulaName: 'relationFormula',
                    columnWidth: .5,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relationFormula'),//关联关系公式
                    id: 'kpi_kpi_kpityperelationFormulaSelector' + kpi_kpi_paramdc
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
                    id:'kpi_kpi_modelValue'+kpi_kpi_paramdc,
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
                	id:'kpi_kpi_resultSumMeasure'+kpi_kpi_paramdc,
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
                	id:'kpi_kpi_targetSumMeasure'+kpi_kpi_paramdc,
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
                	id:'kpi_kpi_assessmentSumMeasure'+kpi_kpi_paramdc,
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
                    id:'maxValue'+kpi_kpi_paramdc,
                    fieldLabel: FHD.locale.get('fhd.kpi.form.maxvalue'),//最大值
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                },
                {
                    xtype: 'numberfield',
                    step: 5,
                    name: 'minValue',
                    id:'minValue'+kpi_kpi_paramdc,
                    fieldLabel: FHD.locale.get('fhd.kpi.form.minvalue'),//最小值
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                }

                ]
            }, {
                xtype: 'fieldset',//采集设置fieldset
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
                    id: 'kpi_kpi_resultgatherfrequence' + kpi_kpi_paramdc,
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
                    id: 'kpi_kpi_targetSetFrequence' + kpi_kpi_paramdc,
                    name: 'targetSetFrequenceStr',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.targetSetFrequence'),//目标收集频率
                    valueDictType: '',
                    single: false,
                    value: '',
                    labelWidth: 105,
                    columnWidth: .5
                }, {
                	id:'kpi_kpi_resultCollectInterval'+kpi_kpi_paramdc,
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
                    id:'kpi_kpi_targetSetInterval'+kpi_kpi_paramdc,
                    name: 'targetSetIntervalStr',
                    maxValue: 5,
                    minValue: 1,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSetInterval'),//目标收集延期天
                    value: '3',
                    maxLength: 100,
                    columnWidth: .5
                }, {
                    columnWidth: .5,
                    id: 'kpi_kpi_reportFrequence' + kpi_kpi_paramdc,
                    name: 'reportFrequenceStr',
                    xtype: 'collectionSelector',
                    label: FHD.locale.get('fhd.kpi.kpi.form.reportFrequence'),//结果收集报告频率
                    valueDictType: '',
                    single: false,
                    value: '',
                    labelWidth: 105
                }, {
                    columnWidth: .5,
                    id: 'kpi_kpi_targetSetReportFrequence' + kpi_kpi_paramdc,
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
                	id:'kpi_kpi_scale'+kpi_kpi_paramdc,
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
                    id:'kpi_kpi_relativeTo'+kpi_kpi_paramdc,
                    name: 'relativeToStr',
                    dictTypeId: 'kpi_relative_to',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relativeto') + '<font color=red>*</font>',//趋势相对于
                    columnWidth: .5,
                    labelAlign: 'left',
                    defaultValue: 'kpi_relative_to_previs',
                    allowBlank: false
                })
                
                ]
            }],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "false" || me.editflag.length == 0) {
                        //添加时,需要添加评估值的默认公式
                        Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc).setTriggerValue("#[myself~结果值]/#[myself~目标值]*100");
                        Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc).setRadioValue("0sys_use_formular_formula");
                        //设置采集频率默认值为按月采集
                        var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' + kpi_kpi_paramdc);
                        resultgatherfrequence.valueDictType = "0frequecy_month";
                        resultgatherfrequence.valueRadioType = "5,星期五";
                        resultgatherfrequence.setValue("每月,期间的最后一个星期五,每1期间");
                        resultgatherfrequence.valueCron = "0 0 0 ? * 6L";
                        
                        var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' + kpi_kpi_paramdc);
                        targetSetFrequence.valueDictType = "0frequecy_month";
                        targetSetFrequence.valueRadioType = "4,星期一";
                        targetSetFrequence.setValue("每月,期间的首个星期一,每1期间");
                        targetSetFrequence.valueCron = "0 0 0 ? * 2#1";
                        
                    }

                    if (me.editflag == "true") {
                        me.formLoad();
                        //给公式赋值
                        kpiValueToFormula(me.kpiId, me.kpiname);
                    }

                }
            }

        });


        me.callParent(arguments);
    },
    /**
     * form加载数据方法
     */
    formLoad: function () {
        var me = this;
        me.form.waitMsgTarget = true;
        me.form.load({
            url: __ctxPath + '/kpi/Kpi/findkpitypecalculatetojson.f',
            params: {
                id: me.kpiId
            },
            success: function (form, action) {
                var othervalue = action.result.othervalue;
                var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' + kpi_kpi_paramdc);
                var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' + kpi_kpi_paramdc);
                var reportFrequence = Ext.getCmp('kpi_kpi_reportFrequence' + kpi_kpi_paramdc);
                var targetSetReportFrequence = Ext.getCmp('kpi_kpi_targetSetReportFrequence' + kpi_kpi_paramdc);
                if(othervalue.gatherDayCron){
                	resultgatherfrequence.valueCron = othervalue.gatherDayCron;
                }
                if(othervalue.targetDayCron){
                	targetSetFrequence.valueCron = othervalue.targetDayCron;
                }
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