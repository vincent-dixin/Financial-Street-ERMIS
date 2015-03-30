/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.measureedit.RiskMeasureButtonGroup',{
	extend : 'FHD.ux.MenuPanel',
    alias: 'widget.riskmeasurebuttongroup',
	layout: {
	    align: 'stretch',
	    type : 'vbox'
	},
	border:false,
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
        	items:[{
		        text: '风险监控措施',
		        iconCls:'icon-btn-home',
		        scale: 'large',
				iconAlign: 'top',
				handler : me.setBefore
		    },{
		        text: '日常管控措施',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler : me.setNow
		    },{
		        text: '事后应对措施',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler : me.setLater
		    }]
        });
	 	me.callParent(arguments);
	},
	setBefore : function(){
		var riskmeasuremanage = this.up('riskmeasuremanage');
		var cardpanel = riskmeasuremanage.measurecardpanel;
		cardpanel.setActiveItem(cardpanel.riskmeasurebeforepanel);
	},
	setNow : function(){
		var riskmeasuremanage = this.up('riskmeasuremanage');
		var cardpanel = riskmeasuremanage.measurecardpanel;
		cardpanel.setActiveItem(cardpanel.riskmeasurenowpanel);
	},
	setLater : function(){
		var riskmeasuremanage = this.up('riskmeasuremanage');
		var cardpanel = riskmeasuremanage.measurecardpanel;
		cardpanel.setActiveItem(cardpanel.riskmeasurelaterpanel);
	}
});
