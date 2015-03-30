Ext.define('pages.chart.kpi.AngularGaugePanel',{
	extend : 'Ext.container.Container',
	alias : 'widget.angulargaugepanel',
    iconCls:'icon-ibm-icon-reports',
    border: false,
    dataType : '',
    isCount : false,
    typeTitle : '',
    //默认显示图表类型：仪表板
	chartShowType:'0com_catalog_chart_type_1',
	
	 //获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
	
    initComponent: function() {
    	var me = this;
    	if('0com_catalog_chart_type_1' == me.chartShowType){
    		//1.历史数据图
    		me.downRegion = Ext.create('FHD.ux.FusionChartPanel',{
    			//style:'border-top: 1px  #99bce8 solid;',
    			border:true,
    			style:'padding:5px 5px 5px 5px',
    			chartType:'MSLine',
    			flex:2,
    			title:me.typeTitle + '历史数据图',
    			width:600,//legendPosition="RIGHT"
    			xmlData:''
    		});
    		
    		//2.仪表盘图
    		me.angularGaugeChart = Ext.create('FHD.ux.FusionChartPanel',{
    			//style:'border-right: 1px  #99bce8 solid;',
    			border:true,
    			style:'padding:5px 5px 0px 5px',
    			chartType:'AngularGauge',
    			flex:0.3,
    			title:me.typeTitle + '最新数据图',
    			width:300,
    			xmlData:''
    		});
    		//3.下级数据图
    		me.gridPanel = Ext.create('FHD.ux.kpi.KpiGridPanel', {
        		searchable:false,
        		checked:false,
        		pagable:false,
        		destoryflag:'true',
        		url:me.url,
                isDisplayPreResult:true
            });
    		//2.调用生成chart面板
	    	me.barChart = Ext.create('FHD.ux.FusionChartPanel',{
	    		border:true,
    			style:'padding:5px 5px 0px 0px',
    			chartType:'Bar2D',
    			flex:0.7,
    			title:'指标列表',
    			width:300,
    			xmlData:'<chart caption="Brand Winner" yAxisName="Brand Value ($ m)" xAxisName="Brand" bgColor="F1F1F1" showValues="0" canvasBorderThickness="1" canvasBorderColor="999999" plotFillAngle="330" plotBorderColor="999999" showAlternateVGridColor="1" divLineAlpha="0">\n\
					        <set label="Coca-Cola" value="67000" toolText="2006 Rank: 1, Country: US"/>\n\
					        <set label="Microsoft" value="56926" toolText="2006 Rank: 2, Country: US"/>\n\
					        <set label="IBM" value="56201" toolText="2006 Rank: 3, Country: US"/>\n\
					        <set label="GE" value="48907" toolText="2006 Rank: 4, Country: US"/>\n\
					        <set label="Intel" value="32319" toolText="2006 Rank: 5, Country: US"/>\n\
					        <set label="Nokia" value="30131" toolText="2006 Rank: 6, Country: Finland"/>\n\
					        <set label="Toyota" value="27941" toolText="2006 Rank: 7, Country: Japan"/>\n\
					        <set label="Disney" value="27848" toolText="2006 Rank: 8, Country: US"/>\n\
					        <set label="McDonalds" value="27501" toolText="2006 Rank: 9, Country: US"/>\n\
					        <set label="Mercedes-Benz" value="21795" toolText="2006 Rank: 10, Country: Germany"/>\n\
						</chart>'
    		});
	    	me.upRegion = Ext.create('Ext.container.Container',{
	        	flex:2,
	        	layout: {
    				type: 'hbox',
    	        	align:'stretch'
    	        },
	        	items:[
		        	me.angularGaugeChart,
		        	me.barChart
	        	]
    		});
    		me.gridPanel.store.on('load',function(){
    			me.categoryPanel(me, me.targetId, me.angularGaugeChart, me.upRegion, me.barChart, me.generateBarChartData(me.gridPanel));
    			//me.barChart.loadXMLData(me.generateBarChartData(me.gridPanel));
    			me.categoryHistory(me, me.targetId, me.upRegion, me.downRegion);
    			
	    		//me.add(me.upRegion);
	    		//me.add(me.downRegion);
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
    
    categoryPanel : function(param, targetId, angularGaugeChart, upRegion, barChartPanel, barChartXml){
    	var typeTitle = this.typeTitle;
    	var paraobj = {};
        paraobj.isNewValue = FHD.data.isNewValue
        if(FHD.data.yearId == ''){
        	paraobj.yearId = this.getYear();
        }else{
        	paraobj.yearId = FHD.data.yearId;
        }
        paraobj.monthId = FHD.data.monthId;
        paraobj.objectId = targetId;
        paraobj.dataType = this.dataType;
        
    	FHD.ajax({
            url: __ctxPath + '/kpi/categoryPanel.f',
            params: {
            	condItem: Ext.JSON.encode(paraobj)
            },
            callback: function (data) {
                if (data && data.success) {
                	upRegion.removeAll();
                	angularGaugeChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
            			//style:'border-right: 1px  #99bce8 solid;',
            			border:true,
            			style:'padding:5px 5px 0px 5px',
            			chartType:'AngularGauge',
            			flex:0.3,
            			title:typeTitle + '最新数据图',
            			width:300,
            			xmlData:data.xml
            		});
                	
                	barChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
        	    		border:true,
            			style:'padding:5px 5px 0px 0px',
            			chartType:'Bar2D',
            			flex:0.7,
            			title:'指标列表',
            			width:300,
            			xmlData:barChartXml
            		});
                	
                	upRegion.add(angularGaugeChartPanel);
                	upRegion.add(barChartPanel);
                }
            }
        });
    },
    
    categoryHistory : function(param, targetId, upRegion, downRegion){
    	var typeTitle = this.typeTitle;
    	var paraobj = {};
        paraobj.isNewValue = FHD.data.isNewValue
        if(FHD.data.yearId == ''){
        	paraobj.yearId = this.getYear();
        }else{
        	paraobj.yearId = FHD.data.yearId;
        }
        paraobj.monthId = FHD.data.monthId;
        paraobj.objectId = targetId;
        paraobj.dataType = this.dataType;
        
    	FHD.ajax({
            url: __ctxPath + '/kpi/categoryHistory.f',
            params: {
            	condItem: Ext.JSON.encode(paraobj)
            },
            callback: function (data) {
                if (data && data.success) {
                	downRegion.loadXMLData(data.xml);
                }
            }
        });
    	
    	if(!this.isCount){
    		param.add(upRegion);
    		this.isCount = true;
    	}
    	
    	param.add(downRegion);
    },
    
    
    /**
     * 生成仪表板bar图表数据
     */
    generateBarChartData:function(gridPanel){
    	var me = this;
    	//finishValue  assessmentValue
		var data='<chart canvasRightMargin="30" canvasBottomMargin="60" plotBorderColor="C0C0C0" showBorder="0" canvasBorderColor="C0C0C0" showAlternateHGridColor="0" showAlternateVGridColor="0" bgColor="FFFFFF" showValues="0">';

		gridPanel.store.each(function(record){
			if(null != record.get('assessmentValue')){
				data += '<set label="'+record.get("name")+'" value="'+record.get("assessmentValue")+'" toolText="评估值：'+record.get("assessmentValue")+' 指标名称:'+record.get("name")+'"/>';
			}else{
				data += '<set label="'+record.get("name")+'" value="0.0" toolText="0.0 Name:'+record.get("name")+'"/>';
			}
		});
		
		data += '</chart>';
		return data;
    }
});