/**
 * 流程查看容器包括form与grid
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.assess.AssessPlanDetailsPopContainer',{
    extend: 'Ext.container.Container',
    alias: 'widget.assessplandetailspopcontainer',
    
    requires: [
               'FHD.view.icm.assess.AssessPlanDetailsPopPanel',
               'FHD.view.icm.assess.AssessPlanDetailsPopGrid'
              ],
              
    initComponent: function () {
        var me = this;
        
        me.assessplandetailspoppanel = Ext.widget('assessplandetailspoppanel',{businessId:me.businessId});
        
        me.assessplandetailspopgrid = Ext.widget('assessplandetailspopgrid');
        me.assessplandetailspoppanel.reloadData(me);
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[me.assessplandetailspoppanel,me.assessplandetailspopgrid]
        });
        me.callParent(arguments);
    }

});