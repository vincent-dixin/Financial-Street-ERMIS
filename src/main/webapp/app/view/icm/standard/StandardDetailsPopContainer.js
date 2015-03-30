/**
 * 流程查看容器包括form与grid
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.standard.StandardDetailsPopContainer',{
    extend: 'Ext.container.Container',
    alias: 'widget.standarddetailspopcontainer',
    
    requires: [
               'FHD.view.icm.standard.StandardDetailsPopPanel',
               'FHD.view.icm.standard.StandardDetailsPopGrid'
              ],
              
    initComponent: function () {
        var me = this;
        
        me.standarddetailspoppanel = Ext.widget('standarddetailspoppanel',{businessId:me.businessId});
        
        me.standarddetailspopgrid = Ext.widget('standarddetailspopgrid');
        me.standarddetailspoppanel.reloadData(me);
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[me.standarddetailspoppanel,me.standarddetailspopgrid]
        });
        me.callParent(arguments);
    }

});