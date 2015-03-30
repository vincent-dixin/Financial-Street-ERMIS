/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.RiskMeasureBeforePanel', {
    extend: 'Ext.Panel',
    alias: 'widget.riskmeasurebeforepanel',
    requires: [
    	'FHD.view.risk.riskedit.Quantification',
    	'FHD.view.risk.measureedit.MeasurePrognosis',
    	'FHD.view.risk.measureedit.RiskClassBaseMainFieldSet'
    ],
    border : 0,
    autoScroll : true,
	autoWidth: true,
	autoHeight: true,
	layout: {
		type: 'vbox',
		align : 'stretch'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
        me.fieldsetQuantification = Ext.widget('quantification');
        me.addQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDl()'>增加</a>"
        });
        me.quantificationFieldset = Ext.widget('fieldset', {
            layout : {
                type: 'vbox',
                algin: 'stretch'
            },
            margin: '7 10',
            title : '监控指标',
            items : [me.fieldsetQuantification,me.addQuantification]
        });
        me.measureprognosis = Ext.widget('measureprognosis');
        me.riskclassbasemainfieldset = Ext.widget('riskclassbasemainfieldset');
        Ext.apply(me, {
            items: [
               me.quantificationFieldset,
               me.measureprognosis,
               me.riskclassbasemainfieldset
               ]
        });

        me.callParent(arguments);
    },
    addDl : function(){
    	var me = this;
    	me.kpiItems = [];
    	var quantificationFieldset = me.quantificationFieldset;   /*定量的fieldset*/
    	quantificationFieldset.removeAll();
    	var insertobj = {};
    	me.kpiselectwindow =  Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow',{
			    multiSelect:true,
			    onSubmit:function(store){
			    	debugger; 
			    	var kpis = store.data.items;
					Ext.Array.each(kpis,function(kpi){
						debugger;
						var item =kpi.data;
						insertobj = {
							id : item.id,
							text : item.text
							};
						Ext.Array.push(me.kpiItems,insertobj);
						quantificationFieldset.add(Ext.widget('quantification',{kpiname : item.name}));
						})
				    }
		});
		me.kpiselectwindow.show();
    }
});