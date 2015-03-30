Ext.define('FHD.ux.layout.singlelayout.SingleTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.singletab',
    requires: [

              ],
              
    /**
	 * public
	 * 接口属性
	 */
    tabs:null,
    
    /**
	 * public
	 * ext属性
	 */
    plain: true,	//控制tab样式，右侧显示
    
    initComponent: function () {
        var me = this;
        
        Ext.apply(me, {
        	border : false,
        	tabBar:{//控制右侧显示
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: me.tabs,
            //添加监听事件
		    listeners: {
		    	tabchange:function(tabPanel, newCard, oldCard, eOpts){
		    		//var cardid = newCard.id;alert(newCard.sname);
//		    		if('scorecardkpigrid'==cardid){//度量标准页签
//		    			if(tabPanel.paramObj.categoryid!=undefined){
//		    				tabPanel.scorecardkpigrid.store.proxy.extraParams.id = tabPanel.paramObj.categoryid;
//		        			tabPanel.scorecardkpigrid.store.load();
//		    			}
//		    		}else if('scorecardbasicinfopanel'==cardid){//基本信息页签
//		    			if(tabPanel.paramObj.categoryid!=undefined){
//		    				tabPanel.basicinfoCardpanel.setAllBtnStatus(false);
//		    				tabPanel.basicinfoCardpanel.reLoadData();
//		    				var activeId = tabPanel.basicinfoCardpanel.getActiveItem().id;
//				  			if('scorecardbasicform'==activeId){
//			  					tabPanel.basicinfoCardpanel.navBtnHandler(tabPanel.basicinfoCardpanel,0);
//			  				}else if('scorecardwarningset'==activeId){
//			  					tabPanel.basicinfoCardpanel.navBtnHandler(tabPanel.basicinfoCardpanel,1);
//			  				}
//		    			}
//		    		}else if('chartanalysis'==cardid){//图表分析页签
//		    			if(tabPanel.paramObj.categoryid!=undefined){
//		    				tabPanel.chartanalysis.reLoadData();
//		    			}
//		    		}else if('scorecardhistorygrid'==cardid){//图表分析页签
//		    			if(tabPanel.paramObj.categoryid!=undefined){
//		    				tabPanel.scorecardhistorygrid.reLoadData(tabPanel.paramObj.categoryid,"sc");
//		    			}
//		    		}
		    	}
		    }
        });
        
        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
    }
});