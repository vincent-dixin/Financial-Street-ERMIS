/**
 * 用户授权角色Grid
 * 
 * @author 王再冉
 */
Ext.define('FHD.view.sys.organization.emp.EmpRoleGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.empRoleGridPanel',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = 'empRoleGridPanel';
    	me.empIds = null;
    	me.queryUrl = 'sys/organization/queryemprolepage.f';//角色查询列表Url
    	me.addEmpRoleUrl = 'sys/organization/saveemproleentry.f';//保存员工角色
    	
    	//员工列表项
    	me.gridColums =[
    	                {header: 'ID' , dataIndex: 'id', sortable: true, flex : 1,hidden : true},//角色id
    	             	{header: '角色名称' , dataIndex: 'roleName', sortable: true, flex : 1}//角色名称
    	];
    	
    	me.tbar =[//角色菜单项
   	           {text : '保存',iconCls: 'icon-save', handler:function(){me.save(me);}, scope : this}
   	           ];	
    	
    	Ext.apply(me, {
	       multiSelect: true,
	       border:true,
	       rowLines:true,//显示横向表格线
	       checked: true, //复选框
	       autoScroll:true,
	       cols:me.gridColums,//cols:为需要显示的列
	       layout: 'fit',
	       tbarItems:me.tbar,
	       url:me.queryUrl
        });
        me.callParent(arguments);
    },
    
    save : function(me){//员工角色新增方法
    	var selections = me.getSelectionModel().getSelection();//得到选中的记录
    	
    	var ids = [];
		for(var i=0;i<selections.length;i++){
			ids.push(selections[i].get('id'));
		}
		FHD.ajax({//ajax调用
			url : me.addEmpRoleUrl,//为员工新增角色
			params : {
				ids:ids.join(','),
				empIds:me.empIds	// 员工列表选中员工id
			},
			callback : function(){
				//保存成功，刷新页面
				me.store.load();
				//机构节点下员工列表
				var tabpanelempGridPanel = Ext.getCmp('tabpanelempGridPanel');
				tabpanelempGridPanel.rolewin.hide();
				tabpanelempGridPanel.store.load();
				//岗位节点下员工列表
				var positionempGridPanel = Ext.getCmp('positionempGridPanel');
				positionempGridPanel.rolewin.hide();
				positionempGridPanel.store.load();
			}
		});
    }

    
});