Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.strategyobjectivetab',
    
    plain: true,
    
    requires: [
               'FHD.view.kpi.strategyobjective.StrategyObjectiveKpiGrid',
               'FHD.view.kpi.strategyobjective.StrategyObjectiveCardPanel',
               'FHD.view.kpi.chart.StrategymapMainPanel'
              ],
              
  //添加监听事件
    listeners: {
	  	tabchange:function(tabPanel, newCard, oldCard, eOpts){
	  		var cardid = newCard.id;
	  		if('strategyobjectivekpigrid'==cardid){//度量标准页签
	  			if(tabPanel.paramObj!=undefined){
	  				tabPanel.strategyobjectivekpigrid.initParam(tabPanel.paramObj);
		  			tabPanel.strategyobjectivekpigrid.reLoadData();
	  			}
	  		}
	  		else if('strategyobjectivecardpanel'==cardid){//基本信息页签
	  			if(tabPanel.paramObj!=undefined){
		  			tabPanel.strategyobjectivecardpanel.initParam(tabPanel.paramObj);
		  			tabPanel.strategyobjectivecardpanel.reLoadData();
		  			var activeId = tabPanel.strategyobjectivecardpanel.getActiveItem().id;
		  			if('strategyobjectivebasicform'==activeId){
	  					tabPanel.strategyobjectivecardpanel.navBtnHandler(tabPanel.strategyobjectivecardpanel,0);
	  				}else if('strategyobjectivewarningset'==activeId){
	  					tabPanel.strategyobjectivecardpanel.navBtnHandler(tabPanel.strategyobjectivecardpanel,2);
	  				}
	  			}
	  		}else if('chartanalysis2'==cardid){//图表分析页签
	  			if(tabPanel.paramObj!=undefined){
	  				Ext.getCmp('strategyobjectivetab').chartanalysis.reLoadData();
	  			}
    		}else if('strategyhistorygrid'==cardid){//图表分析页签
	  			if(tabPanel.paramObj!=undefined){
	  				Ext.getCmp('strategyobjectivetab').strategyhistorygrid.reLoadData(tabPanel.paramObj.smid,"str");
	  			}
    		}
	  	}
    },

    initComponent: function () {
        var me = this;
        //度量指标页签
        me.strategyobjectivekpigrid = Ext.widget('strategyobjectivekpigrid',{id:'strategyobjectivekpigrid'});
        me.strategyobjectivecardpanel = Ext.widget('strategyobjectivecardpanel',{id:'strategyobjectivecardpanel'});
        //图表分析页签
        me.chartanalysis = Ext.widget('strategymapMainPanel',{id:'chartanalysis2',title:  FHD.locale.get('fhd.kpi.analysischart'),dataType : 'str',typeTitle : ''});
        //历史数据
        me.strategyhistorygrid = Ext.widget('scorecardhistorygrid',{id:'strategyhistorygrid',title:'历史数据'});
        
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.strategyobjectivekpigrid, me.chartanalysis, me.strategyobjectivecardpanel,me.strategyhistorygrid]
        });
        
        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
        
    },
    /**
     * 初始化该类所用到的参数
     */
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    /**
     * 重新加载数据,包括度量标准页签和基本信息页签
     */
    reLoadData:function(){
    	var me = this;
    	me.strategyobjectivekpigrid.initParam(me.paramObj);
    	me.strategyobjectivecardpanel.initParam(me.paramObj);
    	var activeTab = me.getActiveTab();
    	var cardid = activeTab.id;
    	if('strategyobjectivekpigrid'==cardid){//度量标准页签
    		me.strategyobjectivekpigrid.reLoadData();
		}
    	else if('strategyobjectivecardpanel'==cardid){//基本信息页签
			me.strategyobjectivecardpanel.reLoadData();
			
		}else if('chartanalysis2'==cardid){//图表分析页签
			me.chartanalysis.reLoadData();
		}else if('strategyhistorygrid'==cardid){//图表分析页签
			me.strategyhistorygrid.reLoadData(me.paramObj.smid,"str");
		}
    }



});