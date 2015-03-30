Ext.define('FHD.view.kpi.kpitype.KpiTypeTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.kpitypetab',
    
    plain: true,
    
    requires: [
               'FHD.view.kpi.kpitype.KpiTypeKpiGrid',
               'FHD.view.kpi.kpitype.KpiTypeCardPanel'
              ],
              
   //添加监听事件
    listeners: {
	  	tabchange:function(tabPanel, newCard, oldCard, eOpts){
	  		var cardid = newCard.id;
	  		if('kpitypekpigrid'==cardid){//度量标准页签
	  			if(tabPanel.paramObj!=undefined){
	  				tabPanel.kpitypekpigrid.reLoadData();
	  			}
	  		}
	  		else if('kpitypecardpanel'==cardid){//基本信息页签
	  			if(tabPanel.paramObj!=undefined){
	  				var activeId = tabPanel.kpitypecardpanel.getActiveItem().id;
	  				tabPanel.kpitypecardpanel.reLoadData();
	  				if('kpitypebasicform'==activeId){
	  					tabPanel.kpitypecardpanel.navBtnHandler(tabPanel.kpitypecardpanel,0);
	  				}else if('kpitypewarningset'==activeId){
	  					tabPanel.kpitypecardpanel.navBtnHandler(tabPanel.kpitypecardpanel,2);
	  				}
	  			}
	  			
	  		}
	  	}
    },
    	

	initComponent: function() {
	    var me = this;
	    me.paramObj = {};
	    me.kpitypekpigrid = Ext.widget('kpitypekpigrid',{id:'kpitypekpigrid'});
	    //基本信息页签
        me.kpitypecardpanel = Ext.widget('kpitypecardpanel',{id:'kpitypecardpanel',title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo')});
	    
	    Ext.applyIf(me, {
        	tabBar:{
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.kpitypekpigrid,me.kpitypecardpanel]
        });

        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
	    
	},
	
	initParamObj:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	
	/**
	 * 重新加载数据
	 */
	reLoadData:function(){
		var me = this;
		var activeTab = me.getActiveTab();
    	var cardid = activeTab.id;
    	if('kpitypekpigrid'==cardid){//度量标准页签
    		me.kpitypekpigrid.reLoadData();
		}
    	else if('kpitypecardpanel'==cardid){//基本信息页签
			me.kpitypecardpanel.reLoadData();
			
		}
	}


});