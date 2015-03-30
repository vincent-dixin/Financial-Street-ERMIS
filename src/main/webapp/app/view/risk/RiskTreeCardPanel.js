/**
 * 
 * 度量标准树卡片面板
 * 使用card布局
 * 
 * 下级有两个组件 我的文件夹树（id : 'myfoldertree'）、 记分卡树（id : 'scorecardtree'）
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.risk.RiskTreeCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.risktreecardpanel',

    requires: [
        'FHD.view.risk.strategy.StrategyTreePanel',
        'FHD.ux.risk.RiskStrategyMapTree',
        'FHD.view.risk.risk.RiskTreePanel'
    ],

    initComponent: function() {
        var me = this;

        me.strategyTreePanel = Ext.widget('strategytreepanel',{id:'strategyTreePanel'});
        me.riskTreePanel = Ext.widget('risktreepanel',{id:'riskTreePanel',rbs:true});
      
        //将新创建的对象引用放在主页面page的views对象中
        me.page = Ext.fcache.obj;
        me.page.views["riskTree"] = me.riskTreePanel;
        
        //me.riskTreePanel = Ext.widget('riskstrategymaptree',{id:'riskTreePanel',rbsVisible : 'true',riskTreeRoot:'false'});
        
        Ext.apply(me, {
            items: [
                me.riskTreePanel,
                me.strategyTreePanel
            ]
        });

        me.callParent(arguments);
    }

});