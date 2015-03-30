Ext.define('pages.chart.kpi.TrendPanel',{
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
    		me.grid = Ext.create('pages.chart.kpi.TrendGrid', {
        		searchable:false,
        		flex:3,
        		pagable:false,
        		url:me.url,
        		border:true,
        		destoryflag:'true',
        		title:'指标列表',
        		style:'padding:5px 5px 0px 5px',
        		type: 'category'
            });
    		//2.调用生成chart面板
	    	me.chart = Ext.create('FHD.ux.FusionChartPanel',{
    			chartType:'MSLine',
    			flex:4,
    			title:'指标趋势分析图',
    			style:'padding:5px 5px 5px 5px',
        		xmlData:'<chart palette="2" caption="Sales Comparison" showValues="0" numVDivLines="10" drawAnchors="0" numberPrefix="$" divLineAlpha="30" alternateHGridAlpha="20"  setAdaptiveYMin="1" >\n\
							<categories>\n\
				                <category label="Jan" />\n\
				                <category label="Feb" />\n\
				                <category label="Mar" />\n\
				                <category label="Apr" />\n\
				                <category label="May" />\n\
				                <category label="Jun" />\n\
				                <category label="Jul" />\n\
				                <category label="Aug" />\n\
				                <category label="Sep" />\n\
				                <category label="Oct" />\n\
				                <category label="Nov" />\n\
				                <category label="Dec" />\n\
					        </categories>\n\
					        <dataset seriesName="Current Year" color="A66EDD">\n\
				                <set value="1127654" />\n\
				                <set value="1226234" />\n\
				                <set value="1299456" />\n\
				                <set value="1311565" />\n\
				                <set value="1324454" />\n\
				                <set value="1357654" />\n\
				                <set value="1296234" />\n\
				                <set value="1359456" />\n\
				                <set value="1391565" />\n\
				                <set value="1414454" />\n\
				                <set value="1671565" />\n\
				                <set value="1134454" />\n\
					        </dataset>\n\
					        <styles>\n\
				                <definition>\n\
			                        <style name="XScaleAnim" type="ANIMATION" duration="0.5" start="0" param="_xScale" />\n\
			                        <style name="YScaleAnim" type="ANIMATION" duration="0.5" start="0" param="_yscale" />\n\
			                        <style name="XAnim" type="ANIMATION" duration="0.5" start="0" param="_yscale" />\n\
			                        <style name="AlphaAnim" type="ANIMATION" duration="0.5" start="0" param="_alpha" />\n\
			                    </definition>\n\
				                <application>\n\
			                        <apply toObject="CANVAS" styles="XScaleAnim, YScaleAnim,AlphaAnim" />\n\
			                        <apply toObject="DIVLINES" styles="XScaleAnim,AlphaAnim" />\n\
			                        <apply toObject="VDIVLINES" styles="YScaleAnim,AlphaAnim" />\n\
			                        <apply toObject="HGRID" styles="YScaleAnim,AlphaAnim" />\n\
				                </application>\n\
				            </styles>\n\
				      </chart>'
    		});
    		me.grid.store.on('load',function(){
    			me.chart.loadXMLData(me.generateTrendChartData(me.grid));
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
     * 生成趋势图表数据
     */
    generateTrendChartData:function(grid){
    	var me = this;
    	
    	//不带滚动条
		var data='<chart canvasBorderColor="C0C0C0" showLegend="1" legendShadow="0" legendPosition="RIGHT" legendNumColumns="1" bgColor="FFFFFF" xAxisName="月份" yAxisName="实际值" numdivlines="4" vDivLineAlpha ="0" showValues="0" decimals="2" numVDivLines="22" anchorRadius="2" labelDisplay="rotate" slantLabels="1" lineThickness="2" xtLabelManagement="0" showAlternateHGridColor="0">';
		
		data += '<categories>';
		data += '<category label="一月" />';
		data += '<category label="二月" />';
		data += '<category label="三月" />';
		data += '<category label="四月" />';
		data += '<category label="五月" />';
		data += '<category label="六月" />';
		data += '<category label="七月" />';
		data += '<category label="八月" />';
		data += '<category label="九月" />';
		data += '<category label="十月" />';
		data += '<category label="十一月" />';
		data += '<category label="十二月" />';
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
		
		return data;
    }
});