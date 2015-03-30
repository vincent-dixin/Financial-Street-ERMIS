Ext.define('FHD.view.risk.assess.DepartmentGrid', {
    extend: 'FHD.view.component.EditorGridPanel',
    alias: 'widget.departmentGrid',
      
      // 初始化方法
      initComponent: function() {
    	  var me = this;
          
          var cols = [
				{
					dataIndex:'id',
					hidden:true
				},
				{
				    header: "责任部门",
				    dataIndex: 'ZRName',
				    sortable: true,
				    align: 'center',
				    flex:1,
				    editor:{
						xtype:'textfield',
						editable:true
					}
				},{
				    header: "相关部门",
				    dataIndex: 'XGName',
				    sortable: true,
				    align: 'center',
				    flex:1,
				    editor:{
						xtype:'textfield',
						editable:true
					}
				},
				{
				    header: "参与部门",
				    dataIndex: 'CYName',
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
          	  url : __ctxPath + "/app/view/risk/assess/department.json",
          	  cols:cols,
  		      border:  true,
  		      checked: true,
  		      pagable : false,
  		      searchable : false,
  		      btns:[{
    			btype:'add',
    			handler:function(){
    				me.store.insert(0, '');
    			}
        	  },{
				btype:'delete',
				handler:function(){
					alert('delete');
				}
  		      }]
          });

          me.callParent(arguments);
      }

});