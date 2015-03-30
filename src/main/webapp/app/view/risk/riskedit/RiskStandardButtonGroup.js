/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.riskedit.RiskStandardButtonGroup',{
	extend : 'FHD.ux.MenuPanel',
    alias: 'widget.riskstandardbuttongroup',
	layout: {
	    align: 'stretch',
	    type : 'vbox'
	},
	border:false,
	initComponent : function() {
		var me = this;
//		me.historyBtn = Ext.widget('button',{text:'历史风险上报标准',autoWidth : true});
//		me.newBtn = Ext.widget('button',{text:'潜在风险风险上报标准',scale : 'large'});
		Ext.applyIf(me, {
        	items:[{
		        text: '历史风险上报标准',
		        iconCls:'icon-btn-home',
		        scale: 'large',
				iconAlign: 'top',
				handler : me.setActiveHistory
		    },{
		        text: '潜在风险上报标准',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler : me.setActiveLatent
		    }]
        });
	 	me.callParent(arguments);
	},
	setActiveHistory : function(){
		var riskstandardmanage = this.up('riskstandardmanage');
		var cardpanel = riskstandardmanage.standardcardpanel;
		cardpanel.setActiveItem(cardpanel.historyriskstandardpanel);
	},
	setActiveLatent : function(){
		var riskstandardmanage = this.up('riskstandardmanage');
		var cardpanel = riskstandardmanage.standardcardpanel;
		cardpanel.setActiveItem(cardpanel.latentriskstandardpanel);
	}
});
