/**
 * 度量标准中部控件
 * 使用card布局
 * 下级有两个组件 我的文件夹tab（id : 'allmetrictab'）、 记分卡tab（id : 'scorecardtab'）
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.kpi.MetricCenterCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.metriccentercardpanel',

    requires: [
        'FHD.view.kpi.myfolder.AllMetricTab',
        'FHD.view.kpi.scorecard.ScorecardMainPanel',
        'FHD.view.kpi.strategymap.StrategyMapMainPanel',
        'FHD.view.kpi.kpi.KpiMainPanel',
        'FHD.view.kpi.strategyobjective.StrategyObjectiveMainPanel',
        'FHD.view.kpi.kpitype.KpiTypeMainPanel',
        'FHD.view.kpi.result.ManPanel',
        'FHD.view.kpi.myfolder.MyFolderTab'
    ],

    
    // 初始化方法
    initComponent: function() {
        var me = this;

        me.allmetricmainpanel = Ext.widget('allmetrictab',{id : 'allmetricmainpanel'});
        me.myfoldertab = Ext.widget('myfoldertab',{id : 'myfoldertab'});
        me.scorecardmainpanel = Ext.widget('scorecardmainpanel',{id : 'scorecardmainpanel'});
        me.strategymapmainpanel = Ext.widget('strategymapmainpanel',{id : 'strategymapmainpanel'});
        me.kpimainpanel = Ext.widget('kpimainpanel',{id : 'kpimainpanel'});
        me.strategyobjectivemainpanel = Ext.widget('strategyobjectivemainpanel',{id : 'strategyobjectivemainpanel'});
        me.kpitypemainpanel = Ext.widget('kpitypemainpanel',{id : 'kpitypemainpanel'});
        me.manPanel = Ext.widget('manPanel');
        
        Ext.apply(me, {
            items: [
            	// 我的文件夹
            	me.myfoldertab,
            	// 战略地图
            	me.strategymapmainpanel,
            	// 记分卡
            	me.scorecardmainpanel,
            	//指标
            	me.kpimainpanel,
            	//战略目标
            	me.strategyobjectivemainpanel,
            	//指标类型
            	me.kpitypemainpanel,
            	//指标采集结果主面板
            	me.manPanel
            ]
        });

        me.callParent(arguments);
        
    }

});