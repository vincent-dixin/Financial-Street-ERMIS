/**
 * 
 * 计划制定卡片面板
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanCard',{
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.formulatePlanCard',
    
    requires: [
               'FHD.view.risk.assess.formulatePlan.FormulatePlanEdit',
               'FHD.view.risk.assess.formulatePlan.FormulateGrid',
               'FHD.view.risk.assess.formulatePlan.FormulatePlanMainPanel'
              ],
       
	 showFormulateGrid : function(){
			var me = this;
			me.getLayout().setActiveItem(me.formulateGrid);
	 },          
              
              
    showFormulatePlanMainPanel : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.formulatePlanMainPanel);
  		me.formulatePlanMainPanel.basicPanel.navToFirst();
  	},
  	
  	
              
    initComponent: function () {
        var me = this;
        me.id = 'formulatePlanCardId';
        me.formulateGrid = Ext.widget('formulateGrid');
        me.formulatePlanMainPanel = Ext.widget('formulateplanmainpanel');
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.formulateGrid, me.formulatePlanMainPanel]
        });
        
        me.callParent(arguments);
    }

});