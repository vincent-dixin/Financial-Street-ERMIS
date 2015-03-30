/**
 * 记分卡右侧tab面板,包含了度量标准 记分卡基本信息 图表分析 历史数据页签
 * 继承于Ext.tab.Panel
 * 
 * @author 陈晓哲
 */

Ext.define('FHD.view.kpi.scorecard.ScorecardTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.scorecardtab',

    requires: [
        'FHD.view.kpi.scorecard.ScorecardKpiGrid',
        'FHD.view.kpi.scorecard.ScorecardBasicinfoPanel',
        'FHD.view.kpi.chart.AnalysisChartMainPanel',
        'FHD.view.kpi.scorecard.ScorecardHistoryGrid'
    ],

    plain: true,
    
    //传递的参数对象
    paramObj:{},
    
    //添加监听事件
    listeners: {
    	tabchange:function(tabPanel, newCard, oldCard, eOpts){
    		var cardid = newCard.id;
    		if('scorecardkpigrid'==cardid){//度量标准页签
    			if(tabPanel.paramObj.categoryid!=undefined){
    				tabPanel.scorecardkpigrid.store.proxy.extraParams.id = tabPanel.paramObj.categoryid;
        			tabPanel.scorecardkpigrid.store.load();
    			}
    		}else if('scorecardbasicinfopanel'==cardid){//基本信息页签
    			if(tabPanel.paramObj.categoryid!=undefined){
    				tabPanel.basicinfoCardpanel.setAllBtnStatus(false);
    				tabPanel.basicinfoCardpanel.reLoadData();
    				var activeId = tabPanel.basicinfoCardpanel.getActiveItem().id;
		  			if('scorecardbasicform'==activeId){
	  					tabPanel.basicinfoCardpanel.navBtnHandler(tabPanel.basicinfoCardpanel,0);
	  				}else if('scorecardwarningset'==activeId){
	  					tabPanel.basicinfoCardpanel.navBtnHandler(tabPanel.basicinfoCardpanel,1);
	  				}
    			}
    		}else if('chartanalysis'==cardid){//图表分析页签
    			if(tabPanel.paramObj.categoryid!=undefined){
    				tabPanel.chartanalysis.reLoadData();
    			}
    		}else if('scorecardhistorygrid'==cardid){//图表分析页签
    			if(tabPanel.paramObj.categoryid!=undefined){
    				tabPanel.scorecardhistorygrid.reLoadData(tabPanel.paramObj.categoryid,"sc");
    			}
    		}
    	}
    },
    /**
     * 设置激活的tab页签
     */
    setActiveItem:function(index){
    	me = this;
    	me.setActiveTab(index);
    },
    
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        //度量标准页签
        me.scorecardkpigrid = Ext.widget('scorecardkpigrid',{id:'scorecardkpigrid'});
        //图表分析页签
        me.chartanalysis = Ext.widget('analysischartmainpanel',{id:'chartanalysis',title: FHD.locale.get('fhd.kpi.analysischart'),dataType : 'sc',typeTitle : FHD.locale.get('fhd.kpi.categoryroot')});
        //基本信息页签
        me.basicinfoCardpanel = Ext.widget('scorecardbasicinfopanel',{id:'scorecardbasicinfopanel',title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo')});
        //历史数据
        me.scorecardhistorygrid = Ext.widget('scorecardhistorygrid',{id:'scorecardhistorygrid',title:'历史数据'});
        
        Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.scorecardkpigrid,me.chartanalysis,me.basicinfoCardpanel,me.scorecardhistorygrid]
        });

        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
    },
    reLoadData : function() {
    	var me = this;
    	var activeTab = me.getActiveTab();
    	var scorecardtab  = Ext.getCmp('scorecardtab');
    	var cardid = activeTab.id;
    	if('scorecardkpigrid'==cardid){//度量标准页签
			me.scorecardkpigrid.store.proxy.extraParams.id = scorecardtab.paramObj.categoryid;
			me.scorecardkpigrid.store.load();
		}else if('scorecardbasicinfopanel'==cardid){//基本信息页签
			me.basicinfoCardpanel.reLoadData();
		}else if('chartanalysis'==cardid){//图表分析页签
			me.chartanalysis.reLoadData();
		}else if('scorecardhistorygrid'==cardid){//图表分析页签
			me.scorecardhistorygrid.reLoadData(scorecardtab.paramObj.categoryid,"sc");
		}
    }
});