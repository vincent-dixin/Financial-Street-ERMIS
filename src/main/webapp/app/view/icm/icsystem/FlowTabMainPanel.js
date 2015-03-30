/**
 *    @description 
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.icsystem.FlowTabMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.flowtabmainpanel',
    
    requires: [
        'FHD.view.icm.icsystem.FlowTabPanel',
        'Ext.scripts.component.NavigationBars'
    ],
    
    initComponent: function() {
        var me = this;
            me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	   type: 'sc',
        	   id : ''
            });
        me.flowtabpanel = Ext.widget('flowtabpanel',{flex:1,id:'flowtabpanel'});
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[
    			me.flowtabpanel
    		]
        });
        me.callParent(arguments);
    }
});