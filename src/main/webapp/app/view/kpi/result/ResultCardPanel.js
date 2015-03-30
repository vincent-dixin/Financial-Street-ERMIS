/**
 * 结果图片面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.kpi.result.ResultCardPanel', {
	extend : 'FHD.ux.CardPanel',
	alias : 'widget.resultCardPanel',

	requires : [ 'FHD.view.kpi.result.FunsionChartPanel',
			'FHD.view.kpi.result.TablePanel'],

	load : function(param){
		var me = this;
		me.resultParam = param;
		//向cardPanel存放图表、表格组件
		me.addComponents(me);
		
		return me;
	},
			
	// 初始化方法
	initComponent : function() {
		var me = this;
		
		me.id = 'resultCardPanel';
		
		Ext.apply(me, {
			xtype : 'cardpanel',
			border : false,
			activeItem : 0,
			items : [],
			tbar : {
				items : [ {
					id : 'tbarChartsButtonId',
					text : '图表',
					iconCls : 'icon-chart-bar',
					handler : function() {
						me.charts(me);
					}
				}, '-', {
					id : 'tbarListButtonId',
					text : '列表',
					iconCls : 'icon-ibm-icon-allmetrics-list',
					handler : function() {
						me.list(me);
					}
				}, '->', {
					id : 'gatherresulttableinputsave',
					text : '保存',
					hidden : true,
					iconCls : 'icon-page-save',
					handler : function() {
						Ext.getCmp('tablePanel').save();
					}
				}, 
//				{
//					id : 'tableEditId',
//					text : '修改',
//					iconCls : 'icon-application-form-edit',
//					hidden : true,
//					handler : function() {
//						Ext.getCmp('tablePanel').inputGatherResult(me);
//					}
//				}, 
				
				{
					text : '返回',
					iconCls : 'icon-arrow-undo',
					handler : function() {
						Ext.getCmp('tablePanel').goback();
					}
				} ]
			}
		});

		me.callParent(arguments);
	},

	addComponents : function(me) {
		me.resultParam.paraobj.eType = FHD.data.eType;
		me.resultParam.paraobj.kpiname = me.resultParam.kpiname;
		FHD.data.kpiName = me.resultParam.kpiname;
		me.resultParam.paraobj.timeId = me.resultParam.timeId;
		me.resultParam.paraobj.year = FHD.data.yearId;
		me.resultParam.paraobj.isNewValue = FHD.data.isNewValue;
		FHD.data.edit = false;
		
		FHD.ajax({
			url : __ctxPath + '/kpi/kpi/createtable.f?edit=' + FHD.data.edit,
			params : {
				condItem : Ext.JSON.encode(me.resultParam.paraobj)
			},
			callback : function(data) {
				if (data && data.success) {
					if(me.items.items.length != 0){
						me.removeAll();
					}
					if(FusionCharts("funsionChartPanel-chart") != undefined){
             		   FusionCharts("funsionChartPanel-chart").dispose();
	             	}
					me.add(Ext.widget('funsionChartPanel', {xml : data.xml}));
					me.add(Ext.widget('tablePanel', {html : data.tableHtml}));
					me.doLayout();
					me.charts(me);
					if(me.resultParam.paraobj.isNewValue){
						FHD.data.yearId = data.year;
					}
				}
			}
		});
	},
	
	charts: function(me){
    	me.getLayout().setActiveItem(me.items.items[0]);
    	Ext.getCmp('tbarChartsButtonId').toggle(true);
    	Ext.getCmp('tbarListButtonId').toggle(false);
    	//Ext.getCmp('tableEditId').hide();
    },
    
    list : function(me){
    	me.getLayout().setActiveItem(me.items.items[1]);
    	Ext.getCmp('tbarChartsButtonId').toggle(false);
    	Ext.getCmp('tbarListButtonId').toggle(true);
    	//Ext.getCmp('tableEditId').show();
    }
});