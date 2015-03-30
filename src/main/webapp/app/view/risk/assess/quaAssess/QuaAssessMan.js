/**
 * 
 * 定性评估主面板
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessMan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.quaAssessMan',
    
    requires: [
     'FHD.view.risk.assess.utils.AssessTree',
     'FHD.view.risk.assess.quaAssess.QuaAssessPanel',
     'FHD.view.risk.assess.WeightingSetMain'
    ],
      
      // 初始化方法
      initComponent: function() {
          var me = this;
          
//          me.assessTree = Ext.widget('assessTree');
          me.quaAssessPanel = Ext.widget('quaAssessPanel');
//          me.weightingSetMain = Ext.widget('weightingSetMain');
          
          Ext.apply(me, {
          	border:true,
          	layout: {
                  type: 'border'
              },
              items: [me.quaAssessPanel]
//              items : [me.weightingSetMain]
          });

          me.callParent(arguments);
          
          me.on('resize',function(p){
          	me.setWidth(FHD.getCenterPanelWidth() - 28);
      	  });
      }

});