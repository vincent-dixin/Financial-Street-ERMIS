/**
 * 内控标准预览
 */
Ext.define('FHD.view.icm.standard.form.StandardPlanPreview',{
	extend : 'Ext.form.Panel',
	alias: 'widget.standardplanpreview',
	requires: [
       'FHD.view.icm.standard.form.StandardPreview'
    ],
	isWindow:false,  
    border:false,
    initComponent: function() {
        var me = this;
        me.cardpanel = Ext.widget('standardpreview',{
        	flex : 1,
        	border:false,
        	isWindow: me.isWindow
        });
        
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
    		items:[
    			me.cardpanel
    		]
        });
    
        me.callParent(arguments);
        me.reloadData1();
    },
    loadData: function(standardId, executionId){
    	var me = this;
    	me.standardPlanId  = standardId;
    	me.executionId = executionId;
    },
    reloadData1:function(){
    	var me=this;
    	me.cardpanel.loadData(me.standardPlanId , me.executionId);
    },
    reloadData:function(){
    }
 });