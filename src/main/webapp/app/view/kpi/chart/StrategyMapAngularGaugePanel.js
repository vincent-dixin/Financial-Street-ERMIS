Ext.define('FHD.view.kpi.chart.StrategyMapAngularGaugePanel',{
	extend : 'Ext.container.Container',
	alias : 'widget.strategyMapAngularGaugePanel',
    iconCls:'icon-ibm-icon-reports',
    border: false,
    dataType : '',
    isCount : false,
    typeTitle : '',
    //默认显示图表类型：仪表板
	chartShowType:'0com_catalog_chart_type_1',
	
    initComponent: function() {
    	var me = this;
    	if('strategy_map_chart_type_2' == me.chartShowType){
    		//1.历史数据图
    		me.downRegion = Ext.create('FHD.ux.FusionChartPanel',{
    			//style:'border-top: 1px  #99bce8 solid;',
    			border:true,
    			style:'padding:5px 5px 5px 5px',
    			chartType:'MSLine',
    			flex:2,
    			id : 'histF',
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
    			id : 'newF',
    			title:me.typeTitle + '最新数据图',
    			width:300,
    			xmlData:''
    		});

    		//3.下级数据图
    		me.gridPanel = Ext.create('FHD.view.kpi.chart.MultiDimCompareGrid', {
        		searchable:false,
        		checked:false,
        		pagable:false,
        		destoryflag:'true',
                isDisplayPreResult:true,
                tableType : me.dataType
            });
    		//2.调用生成chart面板
	    	me.barChart = Ext.create('FHD.ux.FusionChartPanel',{
	    		border:true,
    			style:'padding:5px 5px 0px 0px',
    			chartType:'Bar2D',
    			flex:0.7,
    			id : 'zhibiaoF',
    			title:FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
    			width:300,
    			xmlData:''
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
    			if(Ext.getCmp('strategyobjectivemainpanel').paramObj != undefined){
    				me.categoryPanel(me, Ext.getCmp('strategyobjectivemainpanel').paramObj.smid, me.angularGaugeChart, me.upRegion, me.barChart, me.generateBarChartData(me.gridPanel), me.downRegion);
//    				me.categoryHistory(me, Ext.getCmp('strategyobjectivemainpanel').paramObj.smid, me.upRegion, me.downRegion);
    			}
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
    //上部分图表
    categoryPanel : function(param, targetId, angularGaugeChart, upRegion, barChartPanel, barChartXml, downRegion){
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
                	param.remove(downRegion, true);
                	
                	if(FusionCharts("newF-chart") != undefined){
                		FusionCharts("newF-chart").dispose();
                	}
                	
                	angularGaugeChart = Ext.create('FHD.ux.FusionChartPanel',{
            			border:true,
            			style:'padding:5px 5px 0px 5px',
            			chartType:'AngularGauge',
            			flex:0.3,
            			id : 'newF',
            			title:typeTitle + '最新数据图',
            			width:300,
            			xmlData:data.xml
            		});
                	
                	barChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
        	    		border:true,
            			style:'padding:5px 5px 0px 0px',
            			chartType:'Bar2D',
            			flex:0.7,
            			id : 'zhibiaoF',
            			title:'指标列表',
            			width:300,
            			xmlData:barChartXml
            		});
                	
                	var down = Ext.create('FHD.ux.FusionChartPanel',{
            			border:true,
            			style:'padding:5px 5px 5px 5px',
            			chartType:'MSLine',
            			flex:2,
            			id : 'histF',
            			title:typeTitle + '历史数据图',
            			width:600,
            			xmlData:data.histXml
            		});
                	
                	if(!this.isCount){
                		param.add(upRegion);
                		this.isCount = true;	
                	}
                	
                	upRegion.add(angularGaugeChart);
                	upRegion.add(barChartPanel);
                	
                	param.add(upRegion);
                	param.add(down);
                }
            }
        });
    },
    
    //生成仪表板bar图表数据
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
    },
	//获取当前年份
    getYear: function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
    //重新加载数据
    reloadData: function(){
    	var me = this;
    	if(Ext.getCmp('strategyobjectivemainpanel').paramObj!=undefined){
    		me.gridPanel.store.proxy.extraParams.id = Ext.getCmp('strategyobjectivemainpanel').paramObj.smid;
        	me.gridPanel.store.load();
    	}
    }
});