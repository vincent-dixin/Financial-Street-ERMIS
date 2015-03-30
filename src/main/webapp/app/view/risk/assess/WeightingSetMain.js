/**
 * 
 * 定性评估主面板
 */

Ext.define('FHD.view.risk.assess.WeightingSetMain', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.weightingSetMain',
    
	requires: [
	           'FHD.view.risk.assess.DepartmentGrid',
	           'FHD.view.risk.assess.RoleGrid'
	],
      
      // 初始化方法
      initComponent: function() {
          var me = this;
          
          me.departmentGrid = Ext.widget('departmentGrid');
          me.roleGrid = Ext.widget('roleGrid');
          
          me.departmentGridFieldSet = {
      			xtype : 'fieldset',
      			title : '部门权重设定方案',
//      			collapsible : true,
      			margin : '5 5 0 5',
      			width : 750,
      			items : [ me.departmentGrid ]
      		};
          
          me.roleGridFieldSet = {
      			xtype : 'fieldset',
      			title : '职务',
//      			collapsible : true,
      			margin : '5 5 0 5',
      			layout: {
            		  align: 'stretch',
                    type: 'vbox'
                },
                width : 580,
      			items : [ me.roleGrid]
      		};
          
          Ext.apply(me, {
          	  border:false,
          	  layout: {
          		  align: 'stretch',
                  type: 'hbox'
              },
              items : [me.departmentGridFieldSet, me.roleGridFieldSet]
          });

          me.callParent(arguments);
          
          me.on('resize',function(p){
          	me.departmentGrid.setHeight(FHD.getCenterPanelHeight() - 50);
          	me.roleGrid.setHeight(FHD.getCenterPanelHeight() - 52);
          	//me.departmentGrid.setWidth(FHD.getCenterPanelWidth() - 35);
//          	
//          	me.departmentGridFieldSet.setHeight(FHD.getCenterPanelHeight() - 5);
//          	me.departmentGridFieldSet.setWidth(FHD.getCenterPanelWidth() - 665);
      	  });
      }

});