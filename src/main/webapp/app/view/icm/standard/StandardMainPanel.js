/**
 *    @description 
 *    列表展示： /standard/standardGrid/findStandardByPage.f
 *    编辑明细： /standard/standardTree/findStandardByIdToJson.do
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.standardmainpanel',
    
    requires: [
        'FHD.view.icm.standard.StandardTab',
        'Ext.scripts.component.NavigationBars'
    ],
    
    initComponent: function() {
        var me = this;
        
        me.standardtab = Ext.widget('standardtab',{flex:1});
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[
    			me.standardtab
    		]
        });
        
        me.standardtab.setActiveTab(1);
        me.callParent(arguments);
    },
    // 重新加载数据
    reloadData : function(record) {
    	var me = this;

    	me.standardtab.reloadData();
    }
});