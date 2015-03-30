Ext.define('pages.chart.kpi.MultiDimComparePanel',{
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
    		me.grid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
        		searchable:false,
        		checked:false,
        		flex:3,
        		pagable:false,
        		url:me.url,
        		border:true,
        		title:'指标列表',
        		destoryflag:'true',
                isDisplayPreResult:true,
    		    style:'padding:5px 5px 0px 5px',
                type: 'category'
            });
    		//2.调用生成chart面板
	    	me.chart = Ext.create('FHD.ux.FusionChartPanel',{
    			chartType:'MSColumn2D',
    			title:'指标多维对比分析图',
    			flex:4,
    			style:'padding:5px 5px 5px 5px',
    			xmlData:'<chart caption="Country Comparison" showLabels="1" showvalues="1" decimals="0" numberPrefix="$" placeValuesInside="1" rotateValues="1">\n\
		    				<categories><category label="Austria" /><category label="Brazil" /><category label="France" /><category label="Italy" /><category label="USA" /></categories>\n\
		    				<dataset seriesName="1996" color="AFD8F8" >\n\
		    				<set value="25601.34" />\n\
		    				<set value="20148.82" />\n\
		    				<set value="17372.76" />\n\
		    				<set value="35407.15" />\n\
		    				<set value="38105.68" />\n\
		    				</dataset>\n\
		    				<dataset seriesName="1997" color="F6BD0F" >\n\
		    				<set value="57401.85" />\n\
		    				<set value="41941.19" />\n\
		    				<set value="45263.37" />\n\
		    				<set value="117320.16" />\n\
		    				<set value="114845.27" />\n\
		    				</dataset>\n\
		    				<dataset seriesName="1998" color="8BBA00" >\n\
		    				<set value="45000.65" />\n\
		    				<set value="44835.76" />\n\
		    				<set value="18722.18" />\n\
		    				<set value="77557.31" />\n\
		    				<set value="92633.68" />\n\
		    				</dataset>\n\
    					</chart>'
    		});
    		me.grid.store.on('load',function(){
    			me.chart.loadXMLData(me.generateMultiDimCompareChartData(me.grid));
		    	me.add(me.grid);
	    		me.add(me.chart);
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
    /**
     * 生成多维对比图表数据
     */
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
		
		data += '<dataset seriesName="目标值">';
		data += targetValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="实际值">';
		data += finishValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="上期值">';
		data += preFinishValueItems;
		data += '</dataset>';
		
		data += '<dataset seriesName="去年同期值">';
		data += preYearFinishValueItems;
		data += '</dataset>';
		
		data += '</chart>';
		
		return data;
    }
});