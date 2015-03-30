Ext.define('FHD.view.kpi.chart.StructuralAnalysisPanel',{
	extend : 'Ext.container.Container',
	alias : 'widget.structuralanalysispanel',
	
    iconCls:'icon-ibm-icon-reports',
    border: false,

	//默认显示图表类型：趋势分析
	chartShowType:'0com_catalog_chart_type_5',
	
    initComponent: function() {
    	var me = this;
    	
    	if('0com_catalog_chart_type_5' == me.chartShowType){
    		//1.创建gridPanel
    		me.grid = Ext.create('FHD.view.kpi.chart.StructuralAnalysisGrid', {
        		searchable:false,
        		flex:2,
        		pagable:false,
        		//url:me.url,
        		border:true,
        		destoryflag:'true',
        		title:FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
        		style:'padding:5px 0px 5px 5px',
        		type: 'scorecardmainpanel'
            });
    		//2.调用生成chart面板
	    	me.chart = Ext.create('FHD.ux.FusionChartPanel',{
	    		id:'category-structural-analysis',
	    		chartType:'Doughnut3D',
    			flex:3,
    			title:FHD.locale.get('fhd.kpi.chart.structureProportionChart'),
    			style:'padding:5px 5px 5px 5px',
    			xmlData:''
    		});
    		me.grid.store.on('load',function(){
    			me.generatePieChartData(me.grid);
    			/*
		    	me.chart.loadXMLData(me.generatePieChartData(me.grid));
		    	me.add(me.grid);
	    		me.add(me.chart);
	    		*/
    		});
    	}
    	Ext.applyIf(me,{
    		layout: {
				type: 'hbox',
	        	align:'stretch'
	        }
		});
		
		me.callParent(arguments);
    },
    //生成饼图图表数据
    generatePieChartData:function(grid){
    	var me = this;
    	
    	//不带滚动条//legendPosition='right' 
		var data = "<chart legendShadow='0' chartBottomMargin='100' bgAlpha='30,100' bgAngle='45' pieYScale='50' startingAngle='175'  smartLineColor='7D8892' smartLineThickness='2' baseFontSize='12' showLegend='1' legendShadow='0' showPlotBorder='1' >";
		
			grid.store.each(function(record){
				if(null != record.get('finishValue')){
					data += '<set label="'+record.get("name")+'" value="'+record.get('finishValue')+'" />';
				}else{
					data += '<set label="'+record.get("name")+'" value="0.0" />';
				}
			});
			data += "<styles>";
				data += "<definition>";
					data += "<style name='CaptionFont' type='FONT' face='Verdana' size='11' color='7D8892' bold='1' />";
					data += "<style name='LabelFont' type='FONT' color='7D8892' bold='1'/>";
				data += "</definition>";
				data += "<application>";
					data += "<apply toObject='DATALABELS' styles='LabelFont' />";
		    		data += "<apply toObject='CAPTION' styles='CaptionFont' />";
				data += "</application>";
			data += "</styles>";
		data += "</chart>";
		
		//1.先删除
    	//me.removeAll();
		me.remove(me.chart, true);
		
		//2.再创建
		me.chart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'category-structural-analysis',
    		chartType:'Doughnut3D',
			flex:3,
			title:FHD.locale.get('fhd.kpi.chart.structureProportionChart'),
			style:'padding:5px 5px 5px 5px',
			xmlData:data
		});
		me.add(me.grid);
		me.add(me.chart);
		
		//return data;
    },
    //重新加载数据
    reLoadData: function(){
    	var me = this;
    	
    	if(Ext.getCmp('scorecardmainpanel').paramObj!=undefined){
    		me.grid.store.proxy.extraParams.id = Ext.getCmp('scorecardmainpanel').paramObj.categoryid;
        	me.grid.store.load();
    	}
    }
});