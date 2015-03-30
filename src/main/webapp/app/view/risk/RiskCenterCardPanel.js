/**
 * 风险中心card面板
 * @author zhengjunxiang
 */
Ext.define('FHD.view.risk.RiskCenterCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.riskcentercardpanel',

    requires: [
        //'FHD.view.risk.strategy.view.StrategyMainPanelView',
        'FHD.view.risk.strategy.StrategyMainPanel',
        'FHD.view.risk.risk.RiskContainer'
    ],

    
    // 初始化方法
    initComponent: function() {
        var me = this;

        me.strategymainpanel = Ext.widget('strategymainpanel',{id : 'strategymainpanel'});
        var riskPanel = Ext.widget('riskContainer',{id : 'riskPanel'});
        
        //将新创建的对象引用放在主页面page的views对象中
        Ext.fcache.obj.views["riskContainer"] = riskPanel;
        
        Ext.apply(me, {
            items: [
				//风险树
				riskPanel,
				//目标树
				me.strategymainpanel
            ]
        });

        me.callParent(arguments);
        
    }

});