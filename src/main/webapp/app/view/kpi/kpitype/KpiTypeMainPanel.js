Ext.define('FHD.view.kpi.kpitype.KpiTypeMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.kpitypemainpanel',
    
    requires: [
               'FHD.view.kpi.kpitype.KpiTypeTab'
              ],
        
    initComponent: function() {
    	var me = this;
    	
    	me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'kpi',
        	id : '' 
        });
    	
        me.kpitypetab = Ext.widget('kpitypetab',{flex:1,id:'kpitypetab'});
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[{
    			xtype:'box',
    			height:20,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="kpitypecontainer" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.kpitypetab]
        });
        
        me.callParent(arguments);
    },
    
	/**
	 * 重新加载数据
	 */
	reLoadData : function(record) {
		var me = this;
		if (record.parentNode == null) {
			 me.navigationBar.renderHtml('kpitypecontainer', '' , '', 'kpi');
			 return;//如果是根节点直接返回
		}
	   	var id = record.data.id;
        var name = record.data.text;
  		var paramObj = {};
  		paramObj.kpitypeid = id;
  		paramObj.editflag = true;
  		paramObj.kpitypename = name;
  		me.paramObj = paramObj;
  		me.kpitypetab.initParamObj(paramObj);
		me.kpitypetab.reLoadData();
		me.navigationBar.renderHtml('kpitypecontainer', paramObj.kpitypeid , '', 'kpi');
	}


});