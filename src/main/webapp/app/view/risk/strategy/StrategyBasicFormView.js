Ext.define('FHD.view.risk.strategy.StrategyBasicFormView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.strategybasicformview',
    requires:[
              'FHD.view.risk.strategy.AlarmGrid'
             ],
    border: false,
    title: '基础信息查看',
    /**
     * form表单中添加控件
     */
    addComponent: function () {
        var me = this;
        me.basicfieldSet = Ext.widget('fieldset', {
        	region:'center',
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: false,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo')
        });

        //上级目标
        var parentname = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.parent'), //上级目标
            value: me.smparentname,
            name: 'parentName',
            columnWidth: .5
        });

        me.basicfieldSet.add(parentname);

        //编码
        var code = Ext.widget('displayfield', {
            xtype: 'displayfield',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'), //编码
            margin: '3 3 3 30',
            name: 'code',
            columnWidth: .5
        });
        me.basicfieldSet.add(code);

        //名称
        var name = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.name'), //名称
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        me.basicfieldSet.add(name);

        //短名称
        var shortName = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'), //短名称
            margin: '7 30 3 30',
            name: 'shortName',
            columnWidth: .5
        });
        me.basicfieldSet.add(shortName);

        //主维度
        var mainDim = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'), //主维度
            name: 'mainDim',
            margin: '7 30 3 30',
            columnWidth: .5
        });

        me.basicfieldSet.add(mainDim);

        //战略主题
        var mainTheme = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainTheme'), //战略主题
            name: 'mainTheme',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(mainTheme);

        //辅助纬度
        var otherDim = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'), //辅助纬度
            name: 'otherDim',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(otherDim);
        //辅助战略主题
        var otherTheme = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherTheme'), //辅助战略主题
            name: 'otherTheme',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(otherTheme);

        //所属部门人员
        var ownDept = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept"), //所属部门人员
            name: 'ownDept',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(ownDept);

        //是否启用
        var estatus = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows : 2,
            fieldLabel: FHD.locale.get('fhd.common.enable'), //是否启用
            name: 'estatus',
            margin: '7 30 3 30',
            columnWidth: .5
            
        });
        me.basicfieldSet.add(estatus);
        
        //查看人
        var viewDept = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows : 2,
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.viewEmp"), //查看人
            name: 'viewDept',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(viewDept);
        
        //预警公式
        var warningFormula = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows : 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
            name: 'warningFormula',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(warningFormula);
        
        //报告人
        var reportDept = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows : 2,
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.reportEmp"), //报告人
            name: 'reportDept',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(reportDept);

        //评估值公式
        var assessmentFormula = Ext.widget('displayfield', {
        	xtype: 'displayfield',
            rows : 2,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula'), //评估值公式
            name: 'assessmentFormula',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(assessmentFormula);

        //说明
        var desc = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'), //说明
            name: 'desc',
            margin: '7 30 3 30',
            columnWidth: .5
        });
        me.basicfieldSet.add(desc);

        me.add(me.basicfieldSet);
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

        //向form表单中添加控件,并将form添加到panel中
        me.addComponent();
        
        //将告警列表添加到panel中
        me.alarmGrid = Ext.widget('riskAlarmGrid',{columnWidth:1});
        me.alarmfieldSet = Ext.widget('fieldset', {
        	region:'center',
            xtype: 'fieldset', 
            autoHeight: true,
            autoWidth: true,
            collapsible: false,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: "告警方案"
        });
        me.alarmfieldSet.add(me.alarmGrid);
        me.add(me.alarmfieldSet);
        //me.add(me.alarmGrid);
    },
    reLoadData: function (json) {
        var me = this;
        me.form.setValues({
            parentName: json.parentName,
            code:json.code,
            name:json.name,
            shortName:json.shortName,
            mainDim:json.mainDim,
            mainTheme:json.mainTheme,
            otherDim:json.otherDim,
            otherTheme:json.otherTheme,
            ownDept:json.ownDept,
            estatus:json.estatus,
            viewDept:json.viewDept,
            warningFormula:json.warningFormula,
            reportDept:json.reportDept,
            assessmentFormula:json.assessmentFormula,
            desc:json.desc
        });
        
        //重新加载告警列表数据
        me.alarmGrid.store.load({params:{id:json.smid}});
    }

});