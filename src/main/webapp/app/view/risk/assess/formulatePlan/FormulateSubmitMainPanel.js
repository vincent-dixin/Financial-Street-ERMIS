/**
 * 提交主面板
 * 
 * @author	王再冉
 */
Ext.define('FHD.view.risk.assess.formulatePlan.FormulateSubmitMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.formulatesubmitmainPanel',
    
    requires: [
               'FHD.view.risk.assess.formulatePlan.FormulateSubmitGrid',
               'FHD.view.risk.assess.formulatePlan.FormulateApproverEdit'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	//列表
    	me.formulatesubmitgrid = Ext.widget('formulatesubmitgrid',{
    		height:200
    	});
    	
    	//审批人表单
    	me.formulateapproveredit = Ext.widget('formulateapproveredit');
    	
    	Ext.apply(me, {
            border:false,
     		layout: {
     			align: 'stretch',
     	        type: 'vbox',
     	    },
     	    items:[me.formulatesubmitgrid, me.formulateapproveredit]
        });
    	
        me.callParent(arguments);
        
    }
});