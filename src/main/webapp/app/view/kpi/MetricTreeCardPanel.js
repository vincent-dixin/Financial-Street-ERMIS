/**
 * 
 * 度量标准树卡片面板
 * 使用card布局
 * 
 * 下级有两个组件 我的文件夹树（id : 'myfoldertree'）、 记分卡树（id : 'scorecardtree'）
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.kpi.MetricTreeCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.metrictreecardpanel',

    requires: [
        'FHD.view.kpi.scorecard.ScorecardTree',
        'FHD.view.kpi.myfolder.MyFolderTree',
        'FHD.view.kpi.kpitype.KpiTypeTree',
        'FHD.view.kpi.strategyobjective.StrategyObjectiveTree',
        'FHD.view.kpi.strategymap.StrategyMapTree'
    ],

    initComponent: function() {
        var me = this;

        me.myfoldertree = Ext.widget('myfoldertree',{id:'myfoldertree'});
        me.kpitypetree = Ext.widget('kpitypetree',{id:'kpitypetree'});
        me.scorecardtree = Ext.widget('scorecardtree',{id:'scorecardtree'});
        me.strategyobjectivetree = Ext.widget('strategyobjectivetree',{id:'strategyobjectivetree'});
        me.strategymaptree = Ext.widget('strategymaptree',{id:'strategymaptree'});
        
        
        Ext.apply(me, {
            items: [
            	me.myfoldertree,
            	me.kpitypetree,
                me.scorecardtree,
                me.strategyobjectivetree,
                me.strategymaptree
            ]
        });

        me.callParent(arguments);
    }

});