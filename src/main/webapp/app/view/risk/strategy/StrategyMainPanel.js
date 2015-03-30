Ext.define('FHD.view.risk.strategy.StrategyMainPanel',{
    extend: 'Ext.container.Container',
    alias: 'widget.strategymainpanel',
    
    requires: [
               'FHD.view.risk.strategy.StrategyTab'
              ],
              
    initComponent: function () {
        var me = this;
        me.paramObj = {};
        
        me.strategytab = Ext.widget('strategytab',{flex:1,id:'strategytab'});
        
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
    		me.strategytab]
        });
        me.callParent(arguments);
    },
    /**
     * 重新加载数据,由外部控件向内部控件传递
     */
    reLoadData : function(record) {
    	var me = this;
    	if (record.parentNode == null) {//如果是根节点直接返回
			 return;
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
   		me.strategytab.initParam(paramObj);
    	me.strategytab.reLoadData();
    }

});