/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.riskedit.HistoryRiskStandardPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.historyriskstandardpanel',
    requires: [
    	'FHD.view.risk.riskedit.Quantitative',
    	'FHD.view.risk.riskedit.Quantification',
    	'FHD.view.risk.riskedit.Confidence'
    ],
	width: 800,
	autoHeight: true,
	kpiItems : [],
	layout: {
		type: 'vbox',
		align: 'center'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
        me.quantitativeContainer = Ext.widget('quantitative');
        me.addQuantitative = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDx()'>增加</a>"
        });
    	me.fieldsetQuantitative = Ext.widget('fieldset', {
            layout : {
                type: 'vbox',
                align: 'stretch'
            },
            title : '定性',
            width : 700,
            items : [me.quantitativeContainer,me.addQuantitative]
        });
        me.fieldsetQuantification = Ext.widget('quantification');
        me.addQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDl()'>增加</a>"
        });
        me.quantificationFieldset = Ext.widget('fieldset', {
            layout : {
                type: 'vbox',
                align: 'stretch'
            },
            title : '定量',
            width : 700,
            items : [me.fieldsetQuantification,me.addQuantification]
        });
        Ext.apply(me, {
            items: [
               me.fieldsetQuantitative,
               me.quantificationFieldset
            ]
        });

        me.callParent(arguments);
    },
    addDx : function(){
    	var fieldsetQuantitative = this.fieldsetQuantitative;
    	debugger;
    	fieldsetQuantitative.insert(fieldsetQuantitative.items.length-1,Ext.widget('quantitative'));
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