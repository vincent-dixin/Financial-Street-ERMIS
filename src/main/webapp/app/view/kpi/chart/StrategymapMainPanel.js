Ext.define('FHD.view.kpi.chart.StrategymapMainPanel', {
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.strategymapMainPanel',
    
    requires: [
       'FHD.view.kpi.chart.StrategyMapAngularGaugePanel'
    ],
    
    activeItem: 0,
    
    border: false,
    
    tbar: {
        id: 'sm_analysischartmainpanel_topbar',
    	items:[]
    },
    
    initComponent: function () {
        var me = this;
        
		//仪表板
    	me.angularGaugePanel = Ext.create('FHD.view.kpi.chart.StrategyMapAngularGaugePanel', {
    		categoryid:me.categoryid,
     		chartShowType:'strategy_map_chart_type_2',
     		typeTitle : me.typeTitle,
     		dataType : me.dataType
        });

        Ext.applyIf(me, {
            items: [
                me.angularGaugePanel
            ]
        });
        me.callParent(arguments);
    },
    //设置图表按钮tbar
    setChartTbar: function () {
    	var me = this;
    	if(Ext.getCmp('strategyobjectivemainpanel').paramObj!=undefined){
    		me.categoryId = Ext.getCmp('strategyobjectivemainpanel').paramObj.smid;
            me.chartIds = Ext.getCmp('strategyobjectivemainpanel').paramObj.chartIds;
            var topbar = Ext.getCmp('sm_analysischartmainpanel_topbar');
    		topbar.removeAll();
    		var augalarGaugeButton = {
                    text: ' 仪表板',
                    icon: __ctxPath +'/images/icons/rainbow.png',
                    id: 'strategyanalysischartangulargaugebtntop',
                    handler: function () {
                    	  me.setBtnState('strategyanalysischartangulargaugebtntop');
	                      me.navBtnHandler(0);
                    }
                };
			topbar.add(augalarGaugeButton);
			
			me.setBtnState('strategyanalysischartangulargaugebtntop');
            me.navBtnHandler(0);
            
    		/*if('' != me.chartIds){
    			var chartIdArray = me.chartIds.split(",");
    			//根据图表数据字典id设置显示的图表
    			for(var i=0;i<chartIdArray.length;i++){
    				
    				if('strategy_map_chart_type_2' == chartIdArray[i]){
    					var augalarGaugeButton = {
    	                    text: ' 仪表板',
    	                    icon: __ctxPath +'/images/icons/rainbow.png',
    	                    id: 'strategyanalysischartangulargaugebtntop',
    	                    handler: function () {
    	                    	  me.setBtnState('strategyanalysischartangulargaugebtntop');
      	                          me.navBtnHandler(0);
    	                    }
    	                };
    					topbar.add(augalarGaugeButton);
            		}
    			}
    			
    			if(chartIdArray.length > 0){
    				if('strategy_map_chart_type_2' == chartIdArray[0]){
            		}
    			}else{
    			}
            }*/
    	}
    },
    //cardpanel切换
    navBtnHandler: function (index) {
    	var me = this;
        me.setActiveItem(index);
    },
    //设置按钮状态
    setBtnState: function (bid) {
    	var me = this;
        var k = 0;
        var topbar = Ext.getCmp('sm_analysischartmainpanel_topbar');
        var btns = topbar.items.items;
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
    	if(Ext.getCmp('strategyobjectivemainpanel').paramObj!=undefined){
	    	//设置图表分析tbar按钮
	    	me.setChartTbar();
	    	//重新加载仪表板图表
	    	me.angularGaugePanel.reloadData();
	    	var activeItem = me.getActiveItem();
	    	var activeid = activeItem.id;
    	}
    }
});