Ext.define('FHD.view.kpi.chart.AnalysisChartMainPanel', {
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.analysischartmainpanel',
    
    requires: [
       'FHD.view.kpi.chart.AngularGaugePanel',
       'FHD.view.kpi.chart.MultiDimComparePanel',
       'FHD.view.kpi.chart.StructuralAnalysisPanel',
       'FHD.view.kpi.chart.TrendPanel'
    ],
         
    activeItem: 0,
    
    border: false,
    
    tbar: {
        id: 'analysischartmainpanel_topbar',
    	items:[
			{
			    text: FHD.locale.get('fhd.kpi.chart.angularGauge'),
			    icon: __ctxPath +'/images/icons/rainbow.png',
			    id: 'analysischartangulargaugebtntop',
			    handler: function () {
			        me.setBtnState('analysischartangulargaugebtntop');
			        me.navBtnHandler(this.up('panel'), 0);
			    }
			}
    	]
    },
    
    initComponent: function () {
        var me = this;
        
		//仪表板
    	me.angularGaugePanel = Ext.create('FHD.view.kpi.chart.AngularGaugePanel', {
    		categoryid:me.categoryid,
     		chartShowType:'0com_catalog_chart_type_1',
     		typeTitle : me.typeTitle,
     		dataType : me.dataType
        });
     	//多维对比分析
     	me.multiDimComparePanel = Ext.create('FHD.view.kpi.chart.MultiDimComparePanel', {
     		categoryid:me.categoryid,
     		chartShowType:'0com_catalog_chart_type_3',
     		tableType: me.dataType
        });
     	//趋势分析
     	me.trendPanel = Ext.create('FHD.view.kpi.chart.TrendPanel', {
     		categoryid:me.categoryid,
     		chartShowType:'0com_catalog_chart_type_4'
        });
     	//结构化分析
     	me.structuralAnalysisPanel = Ext.create('FHD.view.kpi.chart.StructuralAnalysisPanel', {
     		categoryid:me.categoryid,
     		chartShowType:'0com_catalog_chart_type_5'
        });

        Ext.applyIf(me, {
            items: [
                me.angularGaugePanel,me.multiDimComparePanel,me.trendPanel,me.structuralAnalysisPanel
            ]
        });
        me.callParent(arguments);
    },
    //设置图表按钮tbar
    setChartTbar: function () {
    	var me = this;
    	if(Ext.getCmp('scorecardmainpanel').paramObj!=undefined){
    		me.categoryId = Ext.getCmp('scorecardmainpanel').paramObj.categoryid;
            me.chartIds = Ext.getCmp('scorecardmainpanel').paramObj.chartIds;
            
    		var topbar = Ext.getCmp('analysischartmainpanel_topbar');
    		topbar.removeAll();
    		
    		if('' != me.chartIds){
    			var chartIdArray = me.chartIds.split(",");
    			//根据图表数据字典id设置显示的图表
    			for(var i=0;i<chartIdArray.length;i++){
    				//中间加分隔符
    				if(i != 0){
    					topbar.add('-');
    				}
    				
    				if('0com_catalog_chart_type_1' == chartIdArray[i]){
    					var augalarGaugeButton = {
    	                    text: FHD.locale.get('fhd.kpi.chart.angularGauge'),
    	                    icon: __ctxPath +'/images/icons/rainbow.png',
    	                    id: 'analysischartangulargaugebtntop',
    	                    handler: function () {
    	                        me.setBtnState('analysischartangulargaugebtntop');
    	                        me.navBtnHandler(0);
    	                    }
    	                };
    					topbar.add(augalarGaugeButton);
            		}else if('0com_catalog_chart_type_3' == chartIdArray[i]){
            			var multiDimCompareButton = {
                            text: FHD.locale.get('fhd.kpi.chart.multiDimCompare'),
                            icon: __ctxPath +'/images/icons/chart_bar.png',
                            id: 'analysischartmultiDimComparebtntop',
                            handler: function () {
                                me.setBtnState('analysischartmultiDimComparebtntop');
                                me.navBtnHandler(1);
                            }
                        };
            			topbar.add(multiDimCompareButton);
            		}else if('0com_catalog_chart_type_4' == chartIdArray[i]){
            			var trendButton = {
                            text: FHD.locale.get('fhd.kpi.chart.trendAnalysis'),
                            icon: __ctxPath +'/images/icons/chart_trend.png',
                            id: 'analysischarttrendbtntop',
                            handler: function () {
                                me.setBtnState('analysischarttrendbtntop');
                                me.navBtnHandler(2);
                            }
                        };
            			topbar.add(trendButton);
            		}else if('0com_catalog_chart_type_5' == chartIdArray[i]){
            			var structuralAnalysisButton = {
                            text: FHD.locale.get('fhd.kpi.chart.structuralAnalysis'),
                            icon: __ctxPath +'/images/icons/chart_pie.png',
                            id: 'analysischartstructuralAnalysisbtntop',
                            handler: function () {
                                me.setBtnState('analysischartstructuralAnalysisbtntop');
                                me.navBtnHandler(3);
                            }
                        };
            			topbar.add(structuralAnalysisButton);
            		}
    			}
    			me.tbar = topbar;
    			
    			if(chartIdArray.length > 0){
    				if('0com_catalog_chart_type_1' == chartIdArray[0]){
    					me.setBtnState('analysischartangulargaugebtntop');
                        me.navBtnHandler(0);
            		}else if('0com_catalog_chart_type_3' == chartIdArray[0]){
            			me.setBtnState('analysischartmultiDimComparebtntop');
                        me.navBtnHandler(1);
            		}else if('0com_catalog_chart_type_4' == chartIdArray[0]){
            			me.setBtnState('analysischarttrendbtntop');
                        me.navBtnHandler(2);
            		}else if('0com_catalog_chart_type_5' == chartIdArray[0]){
            			me.setBtnState('analysischartstructuralAnalysisbtntop');
                        me.navBtnHandler(3);
            		}
    			}else{
    				//me.setBtnState('analysischartangulargaugebtntop');
    				me.navBtnHandler(0);
    			}
            }
    	}
		
    },
    //cardpanel切换
    navBtnHandler: function (index) {
    	var me = this;
    	
        me.setActiveItem(index);
        if(0 == index){
        	//重新加载仪表板图表
        	me.angularGaugePanel.reloadData();
        }else if(1 == index){
        	//重新加载多维对比分析图表
         	me.multiDimComparePanel.reLoadData();
        }else if(2 == index){
        	//重新加载趋势分析图表
         	me.trendPanel.reLoadData();
        }else if(3 == index){
        	//重新加载结构化分析图表
         	me.structuralAnalysisPanel.reLoadData();
        }
    },
    //设置按钮状态
    setBtnState: function (bid) {
    	var me = this;
    	
        var k = 0;
        var topbar = Ext.getCmp('analysischartmainpanel_topbar');
        var btns = topbar.items.items;
        //var btns = me.tbar.items.items;
        for (var i = 0; i < btns.length; i++) {
            var item = btns[i];
            if (item.pressed != undefined) {
                if (item.id == bid) {
                    item.toggle(true);
                } else {
                    item.toggle(false);
                }
                k++;
            }
        }
    },
    //重新加载数据
    reLoadData: function (){
    	var me = this;
    	//设置图表分析tbar按钮
    	me.setChartTbar();
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	if(activeid==me.angularGaugePanel.id){
    		//设置仪表板按钮被选中
    		me.setBtnState('analysischartangulargaugebtntop');
    		//重新加载仪表板图表
        	me.angularGaugePanel.reloadData();
    	}else if(activeid==me.multiDimComparePanel.id){
    		//设置多维对比分析按钮被选中
    		me.setBtnState('analysischartmultiDimComparebtntop');
    		//重新加载多维对比分析图表
         	me.multiDimComparePanel.reLoadData();
    	}else if(activeid==me.trendPanel.id){
    		//设置趋势分析按钮被选中
    		me.setBtnState('analysischarttrendbtntop');
    		//重新加载趋势分析图表
         	me.trendPanel.reLoadData();
    	}else if(activeid==me.structuralAnalysisPanel.id){
    		//设置结构化分析按钮被选中
    		me.setBtnState('analysischartstructuralAnalysisbtntop');
    		//重新加载结构化分析图表
         	me.structuralAnalysisPanel.reLoadData();
    	}
    }
});