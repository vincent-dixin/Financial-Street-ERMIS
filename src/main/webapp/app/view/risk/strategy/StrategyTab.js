Ext.define('FHD.view.risk.strategy.StrategyTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.strategytab',
    
    plain: true,
    
    requires: [
               'FHD.view.risk.strategy.StrategyBasicFormView',
               'FHD.view.risk.strategy.StrategyCardPanel'
              ],
              
    //添加监听事件
    listeners: {
	  	tabchange:function(tabPanel, newCard, oldCard, eOpts){
	  		var cardid = newCard.id;
	  		if('strategycardpanel'==cardid){//基本信息页签
	  			if(tabPanel.paramObj!=undefined){
		  			tabPanel.strategycardpanel.initParam(tabPanel.paramObj);
		  			tabPanel.strategycardpanel.reLoadData();
		  			var activeId = tabPanel.strategycardpanel.getActiveItem().id;
		  			if('strategybasicform'==activeId){
	  					tabPanel.strategycardpanel.navBtnHandler(tabPanel.strategycardpanel,0);
	  				}else if('strategywarningset'==activeId){
	  					tabPanel.strategycardpanel.navBtnHandler(tabPanel.strategycardpanel,2);
	  				}
	  			}
	  		}
	  	}
    },

    initComponent: function () {
        var me = this;
        //度量指标页签
        me.strategybasicformshow =  Ext.widget('strategybasicformview',{id:'strategybasicformshow'});
        me.strategycardpanel = Ext.widget('strategycardpanel',{id:'strategycardpanel'});

        
        Ext.applyIf(me, {
        	tabBar:{//控制右侧显示
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.strategybasicformshow]	//me.strategycardpanel   strategybasicformshow
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
     * 重新加载数据,包括基本信息页签
     */
    reLoadData:function(){
    	var me = this;
    	me.strategycardpanel.initParam(me.paramObj);
    	me.strategycardpanel.reLoadData();
    }

});