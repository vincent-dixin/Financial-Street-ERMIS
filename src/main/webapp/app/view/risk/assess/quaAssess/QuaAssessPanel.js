/**
 * 
 * 风险整理上下面板
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.quaAssessPanel',
    
    requires: [
               'FHD.view.risk.assess.utils.InfoNav',
               'FHD.view.risk.assess.quaAssess.QuaAssessCard'
              ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.id = 'quaAssessPanelId';
        
        me.infoNav = Ext.widget('infoNav',{height : 40});
        me.quaAssessCard = Ext.widget('quaAssessCard');
        
        Ext.apply(me, {
        	border:false,
        	region:'center',
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: [me.infoNav, me.quaAssessCard]
        });

        me.callParent(arguments);
        
        me.on('resize',function(p){
//        	me.quaAssessCard.setWidth(FHD.getCenterPanelWidth() - 200);
    	});
    }

});