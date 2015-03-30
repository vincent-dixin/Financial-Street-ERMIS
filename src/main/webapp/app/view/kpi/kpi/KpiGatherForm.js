Ext.define('FHD.view.kpi.kpi.KpiGatherForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.kpigatherform',

    border: false,
    /**
     * 清除form数据
     */
    clearFormData:function(){
    	var me = this;
    	me.getForm().reset();
    	var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' );
        var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' );
        var reportFrequence = Ext.getCmp('kpi_kpi_reportFrequence' );
        var targetSetReportFrequence = Ext.getCmp('kpi_kpi_targetSetReportFrequence' );
        resultgatherfrequence.reset();
        targetSetFrequence.reset();
        reportFrequence.reset();
        targetSetReportFrequence.reset();
    },
    /**
     * 初始化form默认值
     */
    initFormData:function(){
    	var me = this;
    	//添加时,需要添加评估值的默认公式
    	Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' ).setTriggerValue("IF(#[myself~实际值]>=#[myself~目标值],100,#[myself~实际值]/#[myself~目标值]*100)");
        Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' ).setRadioValue("0sys_use_formular_formula");
        
        Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' ).setRadioValue("0sys_use_formular_manual");
        Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' ).setRadioValue("0sys_use_formular_manual");
//        Ext.getCmp('kpi_kpi_kpityperelationFormulaSelector' ).setRadioValue("0sys_use_formular_manual");
//        Ext.getCmp('kpi_kpi_kpitypealarmFormulaSelector' ).setRadioValue("0sys_use_formular_manual");
        //设置采集频率默认值为按月采集
        var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' );
        resultgatherfrequence.valueDictType = "0frequecy_month";
        resultgatherfrequence.valueRadioType = "2,";
        resultgatherfrequence.setValue("每月,期间末日");
        resultgatherfrequence.valueCron = "0 0 0 L * ?";
        
        var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' );
        targetSetFrequence.valueDictType = "0frequecy_month";
        targetSetFrequence.valueRadioType = "1,";
        targetSetFrequence.setValue("每月,期间首日");
        targetSetFrequence.valueCron = "0 0 0 1 * ?";
        me.getForm().setValues(
    			{
    				resultSumMeasureStr:'kpi_sum_measure_sum',
    				targetSumMeasureStr:'kpi_sum_measure_sum',
    				assessmentSumMeasureStr:'kpi_sum_measure_avg',
    				relativeToStr:'kpi_relative_to_previs'
    				
    			});
        
    },
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel, finishflag) {
        var me = this;
        var jsobj = {};
        var form = me.getForm();
        if(!form.isValid()){
        	return false;
        }
        var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' );
        var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence');
        var reportFrequence = Ext.getCmp('kpi_kpi_reportFrequence' );
        var targetSetReportFrequence = Ext.getCmp('kpi_kpi_targetSetReportFrequence' );
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
        var resultformulaDict = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector').getRadioValue();
        var resultformula = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' ).getTriggerValue();
        var targetformulaDict = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' ).getRadioValue();
        var targetformula = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' ).getTriggerValue();
        var assessmentformulaDict = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' ).getRadioValue();
        var assessmentformula = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' ).getTriggerValue();
        var resultSumMeasureStr = Ext.getCmp('kpi_kpi_resultSumMeasure').getValue();
        var targetSumMeasureStr = Ext.getCmp('kpi_kpi_targetSumMeasure').getValue();
        var assessmentSumMeasureStr = Ext.getCmp('kpi_kpi_assessmentSumMeasure').getValue();
        var scale = Ext.getCmp('kpi_kpi_scale').getValue();
        var resultCollectInterval = Ext.getCmp('kpi_kpi_resultCollectInterval').getValue();
        var targetSetInterval = Ext.getCmp('kpi_kpi_targetSetInterval').getValue();
        var relativeTo = Ext.getCmp('kpi_kpi_relativeTo').getValue();
        var modelValue = Ext.getCmp('kpi_kpi_modelValue').getValue();
        var maxValue = Ext.getCmp('maxValue').getValue();
        var minValue = Ext.getCmp('minValue').getValue();
        var calcValue = Ext.getCmp('kpi_calc').getValue().calcStr;
        jsobj.id = Ext.getCmp('kpibasicform').paramObj.kpiId;
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
        jsobj.calcValue = calcValue;
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
	                	var kpicardpanel =  Ext.getCmp("kpicardpanel");
	                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                        if (!finishflag) {//如果点击的不是完成按钮,需要移动到下一个面板
                        	cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                        	cardPanel.pageMove("next");
                        	kpicardpanel.navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                            /*同时将告警设置按钮为可用状态*/
                            Ext.getCmp('kpi_kpi_alarmset_btn' ).setDisabled(false);
                            Ext.getCmp('kpi_kpi_alarmset_btn_top').setDisabled(false);
                        }else{
                        	kpicardpanel.gotopage();
                        }
                        
	                }
	            }
	        });
        }

    },
    
    addComponent: function () {
        var me = this;
        var calcfieldset = Ext.widget('fieldset', {
            xtype: 'fieldset', //计算信息fieldset
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '15 30 15 30',
                labelWidth: 105
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.cainfo')

        });
        //结果值公式
        var resultFormula = Ext.create('FHD.ux.kpi.FormulaSelector', {
            type: 'kpi',
            column: 'resultValueFormula',
            showType: 'all',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultFormula'), //结果值公式
            targetId: '',
            targetName: '',
            columnWidth: 0.5,
            formulaTypeName: 'isResultFormula',
            formulaName: 'resultFormula',
            id: 'kpi_kpi_kpityperesultFormulaSelector'

        });
        calcfieldset.add(resultFormula);
        //目标值公式
        var targetFormula = Ext.create('FHD.ux.kpi.FormulaSelector', {
            type: 'kpi',
            showType: 'all',
            column: 'targetValueFormula',
            targetId: '',
            targetName: '',
            formulaTypeName: 'isTargetFormula',
            formulaName: 'targetFormula',
            columnWidth: .5,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetFormula'), //目标值公式
            id: 'kpi_kpi_kpitypetargetFormulaSelector'
        });

        calcfieldset.add(targetFormula);

        //评估值公式
        var assessmentFormula = Ext.create('FHD.ux.kpi.FormulaSelector', {
            type: 'kpi',
            showType: 'all',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula') , //评估值公式
            column: 'assessmentValueFormula',
            targetId: '',
            targetName: '',
            formulaTypeName: 'isAssessmentFormula',
            formulaName: 'assessmentFormula',
            columnWidth: .5,
           // allowBlank: false,
            id: 'kpi_kpi_kpitypeassessmentFormulaSelector'
        });
        calcfieldset.add(assessmentFormula);

        //预警公式
        /*var forecastFormula = Ext.create('FHD.ux.kpi.FormulaSelector', {
            type: 'kpi',
            showType: 'all',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmFormula'), //预警公式
            targetId: '',
            targetName: '',
            columnWidth: 0.5,
            formulaTypeName: 'isforecastFormula',
            formulaName: 'forecastFormula',
            id: 'kpi_kpi_kpitypealarmFormulaSelector'

        });
        calcfieldset.add(forecastFormula);

        //关联关系公式
        var relationFormula = Ext.create('FHD.ux.kpi.FormulaSelector', {
            type: 'kpi',
            showType: 'all',
            targetId: '',
            targetName: '',
            formulaTypeName: 'isrelationFormula',
            formulaName: 'relationFormula',
            columnWidth: .5,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relationFormula'), //关联关系公式
            id: 'kpi_kpi_kpityperelationFormulaSelector'
        });
        calcfieldset.add(relationFormula);*/

        var hi = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'null_a',
            value: '',
            maxLength: 100,
            columnWidth: .5,
            hideMode: "visibility",
            hidden: true
        });
        calcfieldset.add(hi);

        //标杆值
        var modelValue = Ext.widget('numberfield', {
            xtype: 'numberfield',
            step: 100,
            name: 'modelValue',
            id: 'kpi_kpi_modelValue',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.modelValue'), //标杆值
            value: '',
            maxLength: 255,
            minValue:0,
            columnWidth: .5
        });
        calcfieldset.add(modelValue);

        var hb = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'null_b',
            value: '',
            maxLength: 100,
            columnWidth: .5,
            hideMode: "visibility",
            hidden: true
        });

        calcfieldset.add(hb);

        //结果值累计计算
        var resultSumMeasure = Ext.create('widget.dictselectforeditgrid', {
            id: 'kpi_kpi_resultSumMeasure',
            editable: false,
            labelWidth: 105,
            labelAlign: 'left',
            multiSelect: false,
            name: 'resultSumMeasureStr',
            dictTypeId: 'kpi_sum_measure',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultSumMeasure'), //结果值累计计算
            defaultValue: 'kpi_sum_measure_sum',
            columnWidth: .5
        });
        calcfieldset.add(resultSumMeasure);

        //目标值累计计算
        var targetSumMeasure = Ext.create('widget.dictselectforeditgrid', {
            id: 'kpi_kpi_targetSumMeasure',
            editable: false,
            labelWidth: 105,
            labelAlign: 'left',
            multiSelect: false,
            name: 'targetSumMeasureStr',
            dictTypeId: 'kpi_sum_measure',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSumMeasure'), //目标值累计计算
            defaultValue: 'kpi_sum_measure_sum',
            columnWidth: .5
        });
        calcfieldset.add(targetSumMeasure);

        //评估值累计计算
        var assessmentSumMeasure = Ext.create('widget.dictselectforeditgrid', {
            id: 'kpi_kpi_assessmentSumMeasure',
            editable: false,
            labelWidth: 105,
            labelAlign: 'left',
            multiSelect: false,
            name: 'assessmentSumMeasureStr',
            dictTypeId: 'kpi_sum_measure',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentMeasure'), //评估值累计计算
            defaultValue: 'kpi_sum_measure_last',
            columnWidth: .5
        });
        calcfieldset.add(assessmentSumMeasure);
        //最大值
        var maxValue = Ext.widget('numberfield', {
            xtype: 'numberfield',
            step: 5,
            name: 'maxValue',
            id: 'maxValue',
            fieldLabel: '最大值' /*FHD.locale.get('fhd.kpi.form.maxvalue')*/
            , //最大值
            value: '',
            maxLength: 255,
            minValue:0,
            columnWidth: .5,
            labelWidth: 105
        });
        calcfieldset.add(maxValue);
        //最小值
        var minValue = Ext.widget('numberfield', {
            xtype: 'numberfield',
            step: 5,
            name: 'minValue',
            id: 'minValue',
            minValue:0,
            fieldLabel: '最小值' /* FHD.locale.get('fhd.kpi.form.minvalue')*/
            , //最小值
            value: '',
            maxLength: 255,
            labelWidth: 105,
            columnWidth: .5
        });
        calcfieldset.add(minValue);
        
      //是否计算
        var iscalcKpi = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'calcStr',
            id:'kpi_calc',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: '是否计算' ,
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5
        });
        calcfieldset.add(iscalcKpi);

        //采集设置fieldset
        var gatherfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //采集设置fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.gatherset')

        });

        //结果收集频率
        var c_resultgatherfrequence = Ext.widget('collectionSelector', {
            columnWidth: .5,
            id: 'kpi_kpi_resultgatherfrequence',
            name: 'resultgatherfrequence',
            xtype: 'collectionSelector',
            label: FHD.locale.get('fhd.kpi.kpi.form.gatherfrequence') + '<font color=red>*</font>', //结果收集频率
            valueDictType: '',
            valueRadioType: '',
            single: false,
            value: '',
            labelWidth: 105,
            allowBlank: false,
            columnWidth: .5
        });

        gatherfieldSet.add(c_resultgatherfrequence);

        //目标收集频率
        var c_targetSetFrequence = Ext.widget('collectionSelector', {
            columnWidth: .5,
            id: 'kpi_kpi_targetSetFrequence',
            name: 'targetSetFrequenceStr',
            xtype: 'collectionSelector',
            label: FHD.locale.get('fhd.kpi.kpi.form.targetSetFrequence')+ '<font color=red>*</font>', //目标收集频率
            valueDictType: '',
            single: false,
            value: '',
            labelWidth: 105,
            columnWidth: .5,
            allowBlank: false
        });
        gatherfieldSet.add(c_targetSetFrequence);

        //结果收集延期天
        var c_resultCollectInterval = Ext.widget('numberfield', {
            id: 'kpi_kpi_resultCollectInterval',
            xtype: 'numberfield',
            step: 1,
            maxValue: 5,
            minValue: 1,
            name: 'resultCollectIntervalStr',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultCollectInterval') + '<font color=red>*</font>', //结果收集延期天
            maxLength: 100,
            columnWidth: .5,
            labelWidth: 105,
            allowBlank: false,
            value: '3'

        });

        gatherfieldSet.add(c_resultCollectInterval);

        //目标收集延期天
        var c_targetSetInterval = Ext.widget('numberfield', {
            xtype: 'numberfield',
            step: 1,
            id: 'kpi_kpi_targetSetInterval',
            name: 'targetSetIntervalStr',
            maxValue: 5,
            minValue: 1,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetSetInterval'), //目标收集延期天
            value: '3',
            maxLength: 100,
            labelWidth: 105,
            columnWidth: .5
        });
        gatherfieldSet.add(c_targetSetInterval);

        //结果收集报告频率
        var c_reportFrequence = Ext.widget('collectionSelector', {
            columnWidth: .5,
            id: 'kpi_kpi_reportFrequence',
            name: 'reportFrequenceStr',
            xtype: 'collectionSelector',
            label: FHD.locale.get('fhd.kpi.kpi.form.reportFrequence'), //结果收集报告频率
            valueDictType: '',
            single: false,
            value: '',
            labelWidth: 105
        });
        gatherfieldSet.add(c_reportFrequence);

        //目标收集报告频率
        var c_targetSetReportFrequence = Ext.widget('collectionSelector', {
            columnWidth: .5,
            id: 'kpi_kpi_targetSetReportFrequence',
            name: 'targetSetReportFrequenceStr',
            xtype: 'collectionSelector',
            label: FHD.locale.get('fhd.kpi.kpi.form.targetSetReportFrequence'), //目标收集报告频率
            valueDictType: '', 
            single: false,
            value: '',
            labelWidth: 105,
            columnWidth: .5
        });

        gatherfieldSet.add(c_targetSetReportFrequence);

        //报告设置fieldset
        var reportfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //报告设置fieldset
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.reportset')
        });

        //报表小数点位置
        var c_scale = Ext.widget('numberfield', {
            id: 'kpi_kpi_scale',
            xtype: 'numberfield',
            step: 1,
            maxValue: 6,
            minValue: 0,
            name: 'scale',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.scale'), //报表小数点位置
            value: '2',
            columnWidth: .5
        });
        reportfieldSet.add(c_scale);
        //趋势相对于
        var c_relativeTo = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            id: 'kpi_kpi_relativeTo',
            name: 'relativeToStr',
            dictTypeId: 'kpi_relative_to',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.relativeto') + '<font color=red>*</font>', //趋势相对于
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue: 'kpi_relative_to_previs',
            allowBlank: false
        });

        reportfieldSet.add(c_relativeTo);


        me.add(calcfieldset);
        me.add(gatherfieldSet);
        me.add(reportfieldSet);

    },

    // 初始化方法
    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            autoRender: false,
            autoScroll: true,
            border: me.border,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            height: FHD.getCenterPanelHeight() - 75,
            bodyPadding: "0 3 3 3"


        });

        me.callParent(arguments);

        //向form表单中添加控件
        me.addComponent();

    },
    
    /**
     * form加载数据方法
     */
    loadFormById: function (id) {
        var me = this;
        if(!Ext.getCmp('kpimainpanel').paramObj.editflag){
        	Ext.getCmp('kpi_calc').setValue('0yn_y');
        }
        //me.form.waitMsgTarget = true;
        me.load({
        	//waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/kpi/Kpi/findkpitypecalculatetojson.f',
            params: {
                id: id
            },
            success: function (form, action) {
                var othervalue = action.result.othervalue;
                var resultgatherfrequence = Ext.getCmp('kpi_kpi_resultgatherfrequence' );
                var targetSetFrequence = Ext.getCmp('kpi_kpi_targetSetFrequence' );
                var reportFrequence = Ext.getCmp('kpi_kpi_reportFrequence' );
                var targetSetReportFrequence = Ext.getCmp('kpi_kpi_targetSetReportFrequence' );
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
            }
        });
    }

});