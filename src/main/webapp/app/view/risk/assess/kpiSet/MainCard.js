/**
 * 
 * 行业卡片面板
 */

Ext.define('FHD.view.risk.assess.kpiSet.MainCard',{
	extend: 'FHD.ux.CardPanel',
    
    requires: [
				 'FHD.view.risk.assess.kpiSet.SetGrid',
				 'FHD.view.risk.assess.utils.GridTreeStr',
				 'FHD.view.risk.assess.kpiSet.SetFormAdd'
              ],
              
    showSetGrid : function(){
		var me = this;
		me.getLayout().setActiveItem(me.items.items[0]);
	},
	
	showGridFhart : function(){
		var me = this;
		me.getLayout().setActiveItem(me.items.items[1]);
	},
              
    initComponent: function () {
        var me = this;
        
        me.id = 'kpisetmaincard';
        
        me.setGrid = Ext.widget('setgrid');
        me.gridFhart = Ext.widget('gridTreeStr');
        var searchField = Ext.create('Ext.ux.form.SearchField', {
			width : 150,
			paramName:me.searchParamName,
			store:me.setGrid.store,
			emptyText : FHD.locale.get('searchField.emptyText')
		});
		var configButton = Ext.create('Ext.Button', {
			    tooltip: '操作',
			    text:'操作',
	            iconCls: 'icon-cog',
	            menu:[{
					text : 'custom',
					iconCls : 'icon-ibm-icon-allmetrics-list',
					handler : function() {
					}
				}]
			});
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.setGrid, me.gridFhart],
            tbar : {
				items : [ {
					text : '列表展示',
					iconCls : 'icon-ibm-icon-allmetrics-list',
					handler : function() {
						me.showSetGrid();
					}
				}, '-', {
					text : '图表展示',
					iconCls : 'icon-chart-bar',
					handler : function() {
						me.showGridFhart();
					}
				}, '-', {
					text:'工作说明',
					iconCls : 'icon-emp',
					id : 'icm_assessguidelinesprop_workstep',
					handler : me.saveGrid,
					disabled :false,
					scope : this
				} , '-', {
					text : '添加',
					iconCls : 'icon-add',
					handler : me.addGrid,
					scope : this
				}, '-',configButton,'->',searchField]
			}
        });
        
        me.callParent(arguments);
    },
    addGrid:function(){
    	var me = this;
    	 me.win=Ext.create('FHD.ux.Window',{
				title : '目标设定',
				buttons: [
  					{ text: '确定',
  					handler:function(){
  						me.win.close();
  					} },
  					{ text: '取消',
  					handler:function(){
  						me.win.close();
  					} }
				],
				collapsible : true,
				modal : true,
				maximizable : true,
				listeners:{
					close : function(){
						}
					},
				items:[Ext.create('FHD.view.risk.assess.kpiSet.SetFormAdd')] 
			}).show();
    },
    nextStep:function(){
    	
    }

});