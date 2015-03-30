Ext.define('FHD.view.kpi.chart.TrendPanel',{
	extend : 'Ext.container.Container',
	alias : 'widget.trendpanel',
	
    iconCls:'icon-ibm-icon-reports',
    border: false,

	//默认显示图表类型：趋势分析
	chartShowType:'0com_catalog_chart_type_4',
	
    initComponent: function() {
    	var me = this;
    	
    	if('0com_catalog_chart_type_4' == me.chartShowType){
    		//1.创建gridPanel
    		me.grid = Ext.create('FHD.view.kpi.chart.TrendGrid', {
        		searchable:false,
        		flex:3,
        		pagable:false,
        		//url:me.url,
        		border:true,
        		destoryflag:'true',
        		title:FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
        		style:'padding:5px 5px 0px 5px',
        		type: 'scorecardmainpanel'
            });
    		//2.调用生成chart面板
	    	me.chart = Ext.create('FHD.ux.FusionChartPanel',{
	    		id:'category-trend',
	    		chartType:'MSLine',
    			flex:4,
    			title:FHD.locale.get('fhd.kpi.chart.trendAnalysisChart'),
    			style:'padding:5px 5px 5px 5px',
        		xmlData:''
    		});
    		me.grid.store.on('load',function(){
    			me.generateTrendChartData(me.grid);
    			/*
    			me.chart.loadXMLData(me.generateTrendChartData(me.grid));
		    	me.add(me.grid);
	    		me.add(me.chart);
	    		*/
    		});
    	}
    	Ext.applyIf(me,{
    		layout: {
				type: 'vbox',
	        	align:'stretch'
	        }
		});
		
		me.callParent(arguments);
    },
    //生成趋势图表数据
    generateTrendChartData:function(grid){
    	var me = this;
    	
    	//不带滚动条
		var data='<chart canvasBorderColor="C0C0C0" showLegend="1" legendShadow="0" legendPosition="RIGHT" legendNumColumns="1" bgColor="FFFFFF" xAxisName="'+FHD.locale.get('fhd.timestampWindow.month')+'" yAxisName="'+FHD.locale.get('fhd.kpi.kpi.form.finishValue')+'" numdivlines="4" vDivLineAlpha ="0" showValues="0" decimals="2" numVDivLines="22" anchorRadius="2" labelDisplay="rotate" slantLabels="1" lineThickness="2" xtLabelManagement="0" showAlternateHGridColor="0">';
		
		data += '<categories>';
		data += '<category label="1'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="2'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="3'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="4'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="5'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="6'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="7'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="8'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="9'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="10'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="11'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '<category label="12'+FHD.locale.get('fhd.sys.planEdit.month')+'" />';
		data += '</categories>';
		
		grid.store.each(function(record){
			data += '<dataset seriesName="'+record.get('name')+'">';
			if(null != record.get('januaryValue')){
				data += '<set value="'+record.get('januaryValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('februaryValue')){
				data += '<set value="'+record.get('februaryValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('marchValue')){
				data += '<set value="'+record.get('marchValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('aprilValue')){
				data += '<set value="'+record.get('aprilValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('mayValue')){
				data += '<set value="'+record.get('mayValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('juneValue')){
				data += '<set value="'+record.get('juneValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('julyValue')){
				data += '<set value="'+record.get('julyValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('aguestValue')){
				data += '<set value="'+record.get('aguestValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('septemberValue')){
				data += '<set value="'+record.get('septemberValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('octoberValue')){
				data += '<set value="'+record.get('octoberValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('novemberValue')){
				data += '<set value="'+record.get('novemberValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			if(null != record.get('decemberValue')){
				data += '<set value="'+record.get('decemberValue')+'" />';
			}else{
				data += '<set value="0.0" />';
			}
			data += '</dataset>';
		});
		
		data += '</chart>';
		
		//1.先删除
    	//me.removeAll();
    	me.remove(me.chart, true);
		
		//2.再创建
		me.chart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'category-trend',
    		chartType:'MSLine',
			flex:4,
			title:FHD.locale.get('fhd.kpi.chart.trendAnalysisChart'),
			style:'padding:5px 5px 5px 5px',
    		xmlData:data
		});
		me.add(me.grid);
		me.add(me.chart);
		
		//return data;
    },
    //重新加载数据
    reLoadData: function() {
    	var me = this;
    	if(Ext.getCmp('scorecardmainpanel').paramObj!=undefined){
    		me.grid.store.proxy.extraParams.id = Ext.getCmp('scorecardmainpanel').paramObj.categoryid;
        	me.grid.store.load();
    	}
    }
});