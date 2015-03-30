Ext.define('pages.chart.kpi.AnalysisChartMainPanel', {
    extend: 'Ext.container.Container',
    border: false,
    layout: "fit",
    title:'图表分析',
    
    _navBtnHandler: function (cardPanel, index) {
    	var me = this;
        cardPanel.getLayout().setActiveItem(index);
    },
    _setBtnState: function (bid) {
    	var me = this;
        var k = 0;
        var btns = me.cardpanel.tbar.items.items;
        for (var i = 0; i < btns.length; i++) {
        	//debugger;
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
    initComponent: function () {
        //var arr = '&rArr;&rArr;';
    	var arr = '-';
        var me = this;
        
        me.cardpanel = Ext.create('FHD.ux.CardPanel', {
            height: FHD.getCenterPanelHeight()-35,
            xtype: 'cardpanel',
            activeItem: 0,
            border: false,
            tbar: Ext.create('Ext.toolbar.Toolbar',{
                id: 'analysischart_topbar' + kpicategory_paramdc,
                items: [
                {
                    text: ' 仪表板',
                    icon: __ctxPath +'/images/icons/rainbow.png',
                    id: 'analysischart_angulargauge_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: '多维对比分析',
                    icon: __ctxPath +'/images/icons/chart_bar.png',
                    id: 'analysischart_multiDimCompare_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr, {
                    text: '趋势分析',
                    icon: __ctxPath +'/images/icons/chart_trend.png',
                    id: 'analysischart_trend_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                arr, {
                    text: '结构化分析',
                    icon: __ctxPath +'/images/icons/chart_pie.png',
                    id: 'analysischart_structuralAnalysis_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(3);
                        me._navBtnHandler(this.up('panel'), 3);
                    }
                }]
            }),
            
            items: me.cardItems
        });

        Ext.applyIf(me, {
        	renderTo: me.renderTo,
            items: [me.cardpanel],
            listeners: {
            	activate: function(){
            		var topbar = Ext.getCmp('analysischart_topbar' + kpicategory_paramdc);
            		topbar.removeAll();
            		
            		if('' != chartIds){
            			var chartIdArray = chartIds.split(",");
            			//根据图表数据字典id设置显示的图表
            			for(var i=0;i<chartIdArray.length;i++){
            				//中间加分隔符
            				if(i != 0){
            					topbar.add('-');
            				}
            				
            				if('0com_catalog_chart_type_1' == chartIdArray[i]){
            					var augalarGaugeButton = {
        		                    text: ' 仪表板',
        		                    icon: __ctxPath +'/images/icons/rainbow.png',
        		                    id: 'analysischart_angulargauge_btn_top' + kpicategory_paramdc,
        		                    handler: function () {
        		                        me._setBtnState('analysischart_angulargauge_btn_top' + kpicategory_paramdc);
        		                        me._navBtnHandler(this.up('panel'), 0);
        		                    }
        		                };
            					topbar.add(augalarGaugeButton);
                    		}else if('0com_catalog_chart_type_3' == chartIdArray[i]){
                    			var multiDimCompareButton = {
                                    text: '多维对比分析',
                                    icon: __ctxPath +'/images/icons/chart_bar.png',
                                    id: 'analysischart_multiDimCompare_btn_top' + kpicategory_paramdc,
                                    handler: function () {
                                        me._setBtnState('analysischart_multiDimCompare_btn_top' + kpicategory_paramdc);
                                        me._navBtnHandler(this.up('panel'), 1);
                                    }
                                };
                    			topbar.add(multiDimCompareButton);
                    		}else if('0com_catalog_chart_type_4' == chartIdArray[i]){
                    			var trendButton = {
                                    text: '趋势分析',
                                    icon: __ctxPath +'/images/icons/chart_trend.png',
                                    id: 'analysischart_trend_btn_top' + kpicategory_paramdc,
                                    handler: function () {
                                        me._setBtnState('analysischart_trend_btn_top' + kpicategory_paramdc);
                                        me._navBtnHandler(this.up('panel'), 2);
                                    }
                                };
                    			topbar.add(trendButton);
                    		}else if('0com_catalog_chart_type_5' == chartIdArray[i]){
                    			var structuralAnalysisButton = {
                                    text: '结构化分析',
                                    icon: __ctxPath +'/images/icons/chart_pie.png',
                                    id: 'analysischart_structuralAnalysis_btn_top' + kpicategory_paramdc,
                                    handler: function () {
                                        me._setBtnState('analysischart_structuralAnalysis_btn_top' + kpicategory_paramdc);
                                        me._navBtnHandler(this.up('panel'), 3);
                                    }
                                };
                    			topbar.add(structuralAnalysisButton);
                    		}
            			}
            			me.cardpanel.tbar = topbar;
            			
            			if(chartIdArray.length > 0){
            				if('0com_catalog_chart_type_1' == chartIdArray[0]){
            					me._setBtnState('analysischart_angulargauge_btn_top' + kpicategory_paramdc);
    	                        me._navBtnHandler(me.cardpanel, 0);
                    		}else if('0com_catalog_chart_type_3' == chartIdArray[0]){
                    			me._setBtnState('analysischart_multiDimCompare_btn_top' + kpicategory_paramdc);
                                me._navBtnHandler(me.cardpanel, 1);
                    		}else if('0com_catalog_chart_type_4' == chartIdArray[0]){
                    			me._setBtnState('analysischart_trend_btn_top' + kpicategory_paramdc);
                                me._navBtnHandler(me.cardpanel, 2);
                    		}else if('0com_catalog_chart_type_5' == chartIdArray[0]){
                    			me._setBtnState('analysischart_structuralAnalysis_btn_top' + kpicategory_paramdc);
                                me._navBtnHandler(me.cardpanel, 3);
                    		}
            			}else{
            				//me._setBtnState('analysischart_angulargauge_btn_top' + kpicategory_paramdc);
            				me._navBtnHandler(me.cardpanel, 0);
            			}
            		}
            	}
            }
        });
        me.callParent(arguments);
    }
});