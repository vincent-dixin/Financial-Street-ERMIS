Ext.define('FHD.view.risk.assess.RoleGrid', {
    extend: 'FHD.view.component.EditorGridPanel',
    alias: 'widget.roleGrid',
      
      // 初始化方法
      initComponent: function() {
    	  var me = this;
          
          var cols = [
				{
					dataIndex:'id',
					hidden:true
				},
				{
				    header: "职务",
				    dataIndex: 'name',
				    sortable: true,
				    align: 'center',
				    flex:1
				},{
				    header: "权重",
				    dataIndex: 'weighting',
				    sortable: true,
				    align: 'center',
				    flex:1,
				    editor:{
						xtype:'textfield',
						editable:true
					}
				}
          ];
          
          Ext.apply(me, {
        	  region:'center',
          	  url : __ctxPath + "/app/view/risk/assess/role.json",
              extraParams:{
              	riskId:1
              },
          	  cols:cols,
  		      border: true,
  		      checked: false,
  		      pagable : false,
  		      searchable : false,
          });

          me.callParent(arguments);
      }

});