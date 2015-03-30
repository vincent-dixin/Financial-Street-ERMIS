Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveMainPanel',{
    extend: 'Ext.container.Container',
    alias: 'widget.strategyobjectivemainpanel',
    
    requires: [
               'FHD.view.kpi.strategyobjective.StrategyObjectiveTab'
              ],
              
    initComponent: function () {
        var me = this;
        me.paramObj = {};
        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'sm',
        	id : '' 
        });
        
        me.strategyobjectivetab = Ext.widget('strategyobjectivetab',{flex:1,id:'strategyobjectivetab'});
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[{
    			xtype:'box',
    			height:20,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="smtabcontainer" class="navigation" ></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.strategyobjectivetab]
        });
        me.callParent(arguments);
    },
    /**
     * 重新加载数据
     */
    reLoadData : function(record) {
    	var me = this;
    	if (record.parentNode == null) {
    		 me.navigationBar.renderHtml('smtabcontainer', '' , '', 'sm');
			 return;//如果是根节点直接返回
		}
	   	var id = record.data.id;
        var name = record.data.text;
   		var paramObj = {};
   		
   		FHD.ajax({
   			async:false,
            params: {
                "id": id
            },
            url:  __ctxPath + '/kpi/kpistrategymap/findparentbyid.f',
            callback: function (ret) {
            	paramObj.editflag = true;
            	paramObj.smid = id;
            	paramObj.parentid = ret.parentid;
            	paramObj.smname = name;
            	paramObj.parentname = ret.parentname;
            	paramObj.chartIds = ret.chartType==null?'':ret.chartType;
            }
        });
   		me.paramObj = paramObj;
   		me.strategyobjectivetab.initParam(paramObj);
    	me.strategyobjectivetab.reLoadData();
    	me.navigationBar.renderHtml('smtabcontainer', paramObj.smid, '', 'sm');
    }

});