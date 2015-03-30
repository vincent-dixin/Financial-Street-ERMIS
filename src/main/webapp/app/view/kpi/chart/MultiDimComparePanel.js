Ext.define('FHD.view.kpi.chart.MultiDimComparePanel',{
	extend : 'Ext.container.Container',
	alias : 'widget.multidimcomparepanel',
	
    iconCls:'icon-ibm-icon-reports',
    border: false,

    //默认显示图表类型：多维对比分析
	chartShowType:'0com_catalog_chart_type_3',
	
    initComponent: function() {
    	var me = this;
    	
    	if('0com_catalog_chart_type_3' == me.chartShowType){
    		//1.创建gridPanel
    		me.grid = Ext.create('FHD.view.kpi.chart.MultiDimCompareGrid', {
        		searchable:false,
        		checked:false,
        		flex:3,
        		pagable:false,
        		border:true,
        		title:FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
        		destoryflag:'true',
                isDisplayPreResult:true,
    		    style:'padding:5px 5px 0px 5px',
    		    tableType: me.tableType
            });
    		//2.调用生成chart面板
	    	me.chart = Ext.create('FHD.ux.FusionChartPanel',{
	    		id:'category-multidim-compare',
    			chartType:'MSColumn2D',
    			title:FHD.locale.get('fhd.kpi.chart.multiDimCompareChart'),
    			flex:4,
    			style:'padding:5px 5px 5px 5px',
    			xmlData:''
    		});
    		me.grid.store.on('load',function(){
    			me.generateMultiDimCompareChartData(me.grid);
    			/*
    			me.chart.loadXMLData(me.generateMultiDimCompareChartData(me.grid));
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
    //生成多维对比图表数据
    generateMultiDimCompareChartData:function(grid){
    	var me = this;
    	
		var categoriesItems;
		
		var finishValueItems;
		var targetValueItems;
		var preFinishValueItems;
		var preYearFinishValueItems;
		
		grid.store.each(function(record){
			categoriesItems += '<category label="'+record.get('name')+'" />';
			if(null != record.get('finishValue')){
				finishValueItems += '<set value="'+record.get('finishValue')+'" />';
			}else{
				finishValueItems += '<set value="0.0" />';
			}
			if(null != record.get('targetValue')){
				targetValueItems += '<set value="'+record.get('targetValue')+'" />';
			}else{
				targetValueItems += '<set value="0.0" />';
			}
			if(null != record.get('preFinishValue')){
				preFinishValueItems += '<set value="'+record.get('preFinishValue')+'" />';
			}else{
				preFinishValueItems += '<set value="0.0" />';
			}
			if(null != record.get('preYearFinishValue')){
				preYearFinishValueItems += '<set value="'+record.get('preYearFinishValue')+'" />';
			}else{
				preYearFinishValueItems += '<set value="0.0" />';
			}
		});
		//不带滚动条
		var data='<chart canvasBorderColor="C0C0C0" canvasBottomMargin="70" plotBorderColor="C0C0C0" showLegend="1" legendShadow="0" legendPosition="RIGHT" legendNumColumns="1" showAlternateHGridColor="0" bgColor="FFFFFF" showLabels="1" showvalues="0" decimals="2" placeValuesInside="1" rotateValues="1">';
		
		data += '<categories>';
		data += categoriesItems;
		data += '</categories>';
		
		data += '<dataset seriesName="'+FHD.locale.get('fhd.kpi.kpi.form.targetValue')+'">';
		data += targetValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="'+FHD.locale.get('fhd.kpi.kpi.form.finishValue')+'">';
		data += finishValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="'+FHD.locale.get('fhd.kpi.kpi.form.prefinishValue')+'">';
		data += preFinishValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="'+FHD.locale.get('fhd.kpi.kpi.form.preYearfinishValue')+'">';
		data += preYearFinishValueItems;
		data += '</dataset>';
		
		data += '</chart>';
		
		//1.先删除
    	//me.removeAll();
		me.remove(me.chart, true);
		
		//2.再创建
		me.chart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'category-multidim-compare',
			chartType:'MSColumn2D',
			title:FHD.locale.get('fhd.kpi.chart.multiDimCompareChart'),
			flex:4,
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