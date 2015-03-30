
/**
 * 嵌入 fusionchart 面板
 * 继承于Ext.panel.Panel
 * 
 * @author 胡迪新
 */
Ext.define('FHD.ux.FusionChartPanel',{
	extend: 'Ext.panel.Panel',
	alias: 'widget.fusionchartpanel',
	
	/**
	 * @cfg {String} 图标类型
	 */
	chartType : 'Column2D',
	
	initComponent: function(){
		var me = this;		
		me.createChart();
		
		if(me.jsonUrl){
			me.chart.setJSONUrl(me.jsonUrl);
		}else if(me.xmlData){
			me.chart.setXMLData(me.xmlData)
		}else if(me.jsonData){
			me.chart.setJSONData(me.jsonData);
		}
		
		Ext.applyIf(me,{
			listeners:{
				afterrender : function() {
					me.chartRender();
				},
				resize : function(p,width,height) {
					me.chart.resizeTo(width,height);
				}
			}
		});
		
		me.callParent(arguments);
	},
	
	/**
	 * 创建chart对象
	 */
	createChart : function() {
		var me = this;
		if(FusionCharts(me.id + "-chart") != undefined){
			//alert(me.id + '===' + me.chartType + '.swf' + '未被加载');
			me.chart = FusionCharts(me.id + "-chart");
		}else{
			//alert('加载' + me.id + '===' + me.chartType + '.swf');
			me.chart = new FusionCharts(__ctxPath + '/images/chart/' + me.chartType + '.swf', me.id + '-chart');
		}
	},
	
	/**
	 * 重新加载 jsonurl方式
	 * @param {String} jsonUrl
	 */
	loadJSONUrl : function(jsonUrl) {
		var me = this;
		me.chart.setJSONUrl(jsonUrl);
		me.chartRender();
	},
	
	/**
	 * 重新加载 jsonData方式
	 * @param {String} jsonData
	 */
	loadJSONData : function(jsonData) {
		var me = this;
		me.chart.setJSONData(jsonData);
		me.chartRender();
	},
	
	/**
	 * 重新加载 xmlData方式
	 * @param {String} xmlData
	 */
	loadXMLData : function(xmlData) {
		var me = this;
		me.chart.setXMLData(xmlData);
		me.chartRender();
	},
	
	/**
	 * 渲染chart到页面
	 */
	chartRender : function() {
		var me = this;
		me.chart.render(me.id + '-body');
		if(me.onChartClick){
			Ext.EventManager.addListener(Ext.get(me.id),'mousedown',me.onChartClick,me);
		}
		
	}
});