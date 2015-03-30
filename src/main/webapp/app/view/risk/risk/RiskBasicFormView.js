Ext.define('FHD.view.risk.risk.RiskBasicFormView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.riskBasicFormView',
    autoScroll: true,
    requires:[
              //'FHD.view.risk.strategy.AlarmGrid'
             ],
    border: false,
    title: '信息查看',
    addBasicComponent: function () {
        var me = this;
        me.basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo')
        });

        //上级风险
        var parentName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: '上级风险',
            name: 'parentName',
            columnWidth: .5
        });

        me.basicfieldSet.add(parentName);

        //编码
        var code = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: '编码',
            margin: '3 3 3 30',
            name: 'code',
            columnWidth: .5
        });
        me.basicfieldSet.add(code);

        //风险名称
        var name = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '风险名称',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        me.basicfieldSet.add(name);
        
        //动因描述
        var desc = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '动因描述',
            margin: '7 30 3 30', 
            name: 'desc',
            columnWidth: .5
        });
        me.basicfieldSet.add(desc);
        
        //责任部门
        var respDeptName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '责任部门',
            margin: '7 30 3 30', 
            name: 'respDeptName',
            columnWidth: .5
        });
        //me.basicfieldSet.add(respDeptName);
        
        //相关部门
        var relaDeptName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '相关部门',
            margin: '7 30 3 30', 
            name: 'relaDeptName',
            columnWidth: .5
        });
        //me.basicfieldSet.add(relaDeptName);

        me.add(me.basicfieldSet);
    },
    addRelaComponent: function () {
        var me = this;
        me.relafieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            collapsed:true,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: '相关信息'
        });

        //责任岗位
        var respPositionName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: '责任岗位',
            name: 'respPositionName',
            columnWidth: .5
        });

        me.relafieldSet.add(respPositionName);

        //相关岗位
        var relaPositionName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: '相关岗位',
            margin: '3 3 3 30',
            name: 'relaPositionName',
            columnWidth: .5
        });
        me.relafieldSet.add(relaPositionName);

        //责任人
        var respName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '责任人',
            margin: '7 30 3 30', 
            name: 'respName',
            columnWidth: .5
        });
        me.relafieldSet.add(respName);
        
        //相关人
        var relaName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '相关人',
            margin: '7 30 3 30', 
            name: 'relaName',
            columnWidth: .5
        });
        me.relafieldSet.add(relaName);
        
        //风险指标
        var riskKpiName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '风险指标',
            margin: '7 30 3 30', 
            name: 'riskKpiName',
            columnWidth: .5
        });
        me.relafieldSet.add(riskKpiName);
        
        //影响指标
        var influKpiName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '影响指标',
            margin: '7 30 3 30', 
            name: 'influKpiName',
            columnWidth: .5
        });
        me.relafieldSet.add(influKpiName);
        
        //控制流程
        var controlProcessureName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '控制流程',
            margin: '7 30 3 30', 
            name: 'controlProcessureName',
            columnWidth: .5
        });
        me.relafieldSet.add(controlProcessureName);
        
        //影响流程
        var influProcessureName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '影响流程',
            margin: '7 30 3 30', 
            name: 'influProcessureName',
            columnWidth: .5
        });
        me.relafieldSet.add(influProcessureName);
        
        //继承上级模板
        var isInherit = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '继承上级模板',
            margin: '7 30 3 30', 
            name: 'isInherit',
            columnWidth: .5
        });
        me.relafieldSet.add(isInherit);
        
        //评估模板
        var templeteName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '评估模板',
            margin: '7 30 3 30', 
            name: 'templeteName',
            columnWidth: .5
        });
        me.relafieldSet.add(templeteName);
        
        //趋势相对于
        var relativeTo = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '趋势相对于',
            margin: '7 30 3 30', 
            name: 'relativeTo',
            columnWidth: .5
        });
        me.relafieldSet.add(relativeTo);
        
        //应对策略
        var responseStrategy = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '应对策略',
            margin: '7 30 3 30', 
            name: 'responseStrategy',
            columnWidth: .5
        });
        me.relafieldSet.add(responseStrategy);

        //计算公式
        var formulaDefine = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '计算公式',
            margin: '7 30 3 30', 
            name: 'formulaDefine',
            columnWidth: .5
        });
        me.relafieldSet.add(formulaDefine);
        
        //告警方案
        var alarmScenario = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '告警方案',
            margin: '7 30 3 30', 
            name: 'alarmScenario',
            columnWidth: .5
        });
        me.relafieldSet.add(alarmScenario);
        
        //收集频率
        var gatherFrequence = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '收集频率',
            margin: '7 30 3 30', 
            name: 'gatherFrequence',
            columnWidth: .5
        });
        me.relafieldSet.add(gatherFrequence);
        
        //扩充天数
        var resultCollectInterval = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '扩充天数',
            margin: '7 30 3 30', 
            name: 'resultCollectInterval',
            columnWidth: .5
        });
        me.relafieldSet.add(resultCollectInterval);
        
        //亮灯依据
        var alarmMeasure = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '亮灯依据',
            margin: '7 30 3 30', 
            name: 'alarmMeasure',
            columnWidth: .5
        });
        me.relafieldSet.add(alarmMeasure);
        
        //影响期间
        var impactTime = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '影响期间',
            margin: '7 30 3 30', 
            name: 'impactTime',
            columnWidth: .5
        });
        me.relafieldSet.add(impactTime);
        
        //是否定量
        var isFix = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '是否定量',
            margin: '7 30 3 30', 
            name: 'isFix',
            columnWidth: .5
        });
        me.relafieldSet.add(isFix);
        
        //是否启用
        var isUse = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: '是否启用',
            margin: '7 30 3 30', 
            name: 'isUse',
            columnWidth: .5
        });
        me.relafieldSet.add(isUse);
        
        me.add(me.relafieldSet);
    },
    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            autoScroll: true,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3"
        });
        me.callParent(arguments);
        
        me.addBasicComponent();
        me.addRelaComponent();
    },
    reLoadData: function (json) {
        var me = this;
        me.form.setValues({
            parentName: json.parentName,
            code:json.code,
            name:json.name,
            desc:json.desc
//            respDeptName:json.respDeptName,
//            relaDeptName:json.relaDeptName,
//            respPositionName:json.respPositionName,
//            relaPositionName:json.relaPositionName,
//            respName:json.respName,
//            relaName:json.relaName,
//            riskKpiName:json.riskKpiName,
//            influKpiName:json.influKpiName,
//            controlProcessureName:json.controlProcessureName,
//            influProcessureName:json.influProcessureName,
//            isInherit:json.isInherit,
//            templeteName:json.templeteName,
//            relativeTo:json.relativeTo,
//            responseStrategy:json.responseStrategy,
//            formulaDefine:json.formulaDefine,
//            alarmScenario:json.alarmScenario,
//            gatherFrequence:json.gatherFrequence,
//            resultCollectInterval:json.resultCollectInterval,
//            alarmMeasure:json.alarmMeasure,
//            impactTime:json.impactTime,
//            isFix:json.isFix,
//            isUse:json.isUse
        });
    }
});