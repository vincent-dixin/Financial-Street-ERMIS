/**
 * 
 * 风险整理卡片面板
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyCard',{
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.riskTidyCard',
    
    requires: [
				 'FHD.view.risk.assess.riskTidy.RiskTidyOpe',
				 'FHD.view.risk.assess.utils.GridTreeStr'
              ],
              
  	showRiskTidyOpe : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[0]);
  	},
  	
  	showGridTreeStr : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[1]);
  	},
              
    initComponent: function () {
        var me = this;
        
        me.id = 'riskTidyCardId';
        me.riskTidyOpe = Ext.widget('riskTidyOpe');
        me.gridTreeStr = Ext.widget('gridTreeStr');
       
        var searchField = Ext.create('Ext.ux.form.SearchField', {
			width : 150,
			paramName:me.searchParamName,
			store:me.riskTidyOpe.riskTidyGrid.store,
			emptyText : FHD.locale.get('searchField.emptyText')
		});
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.riskTidyOpe, me.gridTreeStr],
            tbar : {
				items : [ {
					text : '列表展示',
					iconCls : 'icon-ibm-icon-allmetrics-list',
					handler : function() {
						me.showRiskTidyOpe();
					}
				}, '-', {
					text : '图表展示',
					iconCls : 'icon-chart-bar',
					handler : function() {
						me.showGridTreeStr();
					}
				}, '-', {
					text:'工作说明',
					iconCls : 'icon-emp',
					handler : me.saveGrid,
					disabled :false,
					scope : this
				} , '-', {
					text : '合并',
					iconCls : 'icon-add',
					handler : me.addGrid,
					scope : this
					
				},'-',{
					text : '删除',
					iconCls : 'icon-del',
					handler : me.addGrid,
					scope : this
				},'-',{
					text:'操作',
					iconCls : 'icon-cog',
					handler : function(){},
					disabled :false,
					scope : this
				}, '->', searchField]
			},
			
			bbar : {
				items : [ '->',{
					text : '提交',
					iconCls : 'icon-operator-submit',
					handler : function() {
						alert('提交');
					}
				}]
			}
        });
        
        me.callParent(arguments);
    }

});