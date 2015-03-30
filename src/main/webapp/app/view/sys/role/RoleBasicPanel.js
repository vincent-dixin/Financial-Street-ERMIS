/**
 * 为角色添加人员,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleBasicPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.roleBasicPanel',
    /**
     * 初始化组件方法
     */
    /**
     * 加载form数据前台显示
     */
    formLoad : function(){
    	var me = this;
    	if(typeof(me.roleTreeId) != 'undefined') {
    		me.form.load({
    	        url: 'sys/autho/findRoleById.f',//修改显示
    	        params:{id:me.roleTreeId},
    	        failure:function(form,action) {
    	            alert("err 155");
    	        },
    	        success:function(form,action){
    	        	var formValue = form.getValues();
    	        }
    	    });
    	}
    },
 
  //判断添加修改
	 edit : function(){
		var me = this;
    	var rolePersonGridPanel = Ext.getCmp('rolePersonGridPanel');//得到grid
     	var roleTreePanel = Ext.getCmp('roleTreePanel');//得到角色树
		var form = me.getForm();
		if(form.isValid()){
			if(me.isAdd){//修改
				FHD.submit({
					form:form,
					url:me.roleSaveUrl + '?id=' + me.roleTreeId,
						callback:function(data){
							rolePersonGridPanel.store.load();
							roleTreePanel.store.load();	       
						}
				});
			}else{//保存
				FHD.submit({
					form:form,
					url:me.roleSaveUrl,
					callback:function(data){
						if(data.addrole=='roleCodeisExist'){
							window.top.Ext.ux.Toast.msg("操作不成功","角色编号已存在");
						}else if(data.addrole=='roleNameisExist'){
							window.top.Ext.ux.Toast.msg("操作不成功","角色名称已存在");
						}else {
							rolePersonGridPanel.store.load();
							roleTreePanel.store.load();	
						}	       
					}
				});
			}
		}
	},
    initComponent: function () {
    	var me = this;
    	
    	me.id = 'roleBasicPanel';
    	me.roleSaveUrl = 'sys/autho/saveRole.f';//添加保存&修改保存
        Ext.applyIf(me, {
            autoScroll: true,
            title:"基本信息",
            border: me.border,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3",
            tbar: {
                items: ['->',
	            {
	            	text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me.edit();
                    }
	            }]
            },
            bbar:{
                items: ['->',
	            {
	            	text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
	                 iconCls: 'icon-control-stop-blue',
	                 handler: function () {
	                     me.edit();
	                 }
	            }]
            },
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                autoHeight:true,
                autoWidth:true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.common.baseInfo'),
                items: [{
                    xtype: 'textfield',
                    name: 'roleCode',
                    fieldLabel: '角色编号'+'<font color=red>*</font>',//角色编号
                    maxLength: 200,
                    columnWidth: .5,
                    allowBlank: false,
                  
                }, {
                    xtype: 'textfield',
                    name:'roleName',
                    fieldLabel: '角色名称'+'<font color=red>*</font>',//角色名称
                    maxLength: 200,
                    columnWidth: .5,
                    allowBlank: false,
                },
                /*Ext.create('FHD.ux.org.CommonSelector',{
                	fieldLabel: '分配人员',
                    type:'emp',
                    maxLength: 200,
                    columnWidth: .5,
                    name:'addPersonJson',
                    multiSelect:true,
                    labelAlign: 'left',
                    growMax: 60,
                    maxHeight: 70,
                    labelWidth: 95,   
                })*/]
            }]
        });
        me.callParent(arguments);
    },
    load:function(){
    	var me = this;
    	if(typeof(me.roleTreeId) != 'undefined'){
        	//编辑时加载form数据
            me.formLoad();
    	}
    }
    
});