/*
 * 内控评价列表页面 
 * */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanList',{
	extend: 'Ext.container.Container',
    alias: 'widget.constructplanlist',
    
    pagable:true,
    layout: 'fit',
    
    initComponent: function(){
    	var me = this;
    	
		//评价计划列表
		me.constructPlanGrid = Ext.create('FHD.ux.GridPanel', {
	        border: false,
	        region: 'center',
	        url: __ctxPath + '/icm/icsystem/constructplan/findconstructplansbypage.f',
	        cols: [
	    		{header : '计划编号',dataIndex : 'code',sortable : true, flex : 1}, 
	 			{header : '计划名称',dataIndex : 'name',sortable : true, flex : 3}, 
	 			{header : '改建原因',dataIndex :'modifyReason',sortable : true,flex:1}, 
	 			{header : '计划类型',dataIndex : 'type',sortable : true, flex:1}, 
	 			{header : '计划起止日期',dataIndex : 'targetDate',sortable : true, flex : 2},
	 			{header : '状态',dataIndex :'status',sortable : true,flex:1,
					renderer:function(value){
						if(value=='S'){
							return '已保存';
						}else if(value=='P'){
							return '已提交';
						}
					}
	 			},
	 			{header : '执行状态',dataIndex :'dealStatus',sortable : true,flex:1,
					renderer:function(value){
						if(value=='N'){
							return '未开始';
						}else if(value=='H'){
							return '处理中';
						}else if(value=='F'){
							return '已完成';
						}
					}
	 			},
				{header : '创建时间',dataIndex :'createTime',sortable : true, flex : 2}
			],
            tbarItems: [
				{iconCls : 'icon-add',id:'icm_construct_add',handler :me.addPlan,scope : this},
				'-', 
				{iconCls : 'icon-edit',id:'icm_construct_edit',handler :me.editPlan,scope : this},
				'-', 
				{iconCls : 'icon-del',id:'icm_construct_del',handler :me.delPlan,disabled: true,scope : this}
			]
		});
		me.constructPlanGrid.store.on('load', function () {
            me.setstatus()
        });
        me.constructPlanGrid.on('selectionchange', function () {
            me.setstatus()
        });
        //评价计划列表
        me.callParent(arguments);
        me.add(me.constructPlanGrid);
    },
    //新增计划
    addPlan:function(){
    	var me=this;
    	
    	var constructplaneditpanel = me.up('constructplaneditpanel');
    	constructplaneditpanel.initParam({
    		editFlag : false,
    		businessId : ''
    	});
    	constructplaneditpanel.reloadData();
    	constructplaneditpanel.navBtnHandler(1);
    },
    //编辑计划
    editPlan: function(button){
    	var me=this;
    	var selection = me.constructPlanGrid.getSelectionModel().getSelection();//得到选中的记录
    	var isSubmit=selection[0].get('status');
    	if(isSubmit=='P'){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'对不起,您不能修改已提交的数据!');
    		return false;
    	}
    	
    	var constructplaneditpanel = me.up('constructplaneditpanel');
    	if(constructplaneditpanel){
    		constructplaneditpanel.initParam({
    		editFlag : true,
    		businessId : selection[0].get('id')
    	});
    	constructplaneditpanel.reloadData();
		//激活编辑面板
		constructplaneditpanel.navBtnHandler(1);
    	}
    },
    //删除计划
    delPlan: function(){
		var me=this;
		
		var selection = me.constructPlanGrid.getSelectionModel().getSelection();//得到选中的记录
		
		if(0 == selection.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : FHD.locale.get('fhd.common.makeSureDelete'),
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
					if(btn == 'yes') {
	 					var ids = [];
						for(var i = 0; i < selection.length; i++) {
							var isSubmit=selection[i].get('status');
					    	if(isSubmit=='P'){
					    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'对不起,您不能删除已提交的数据!');
					    		return false;
					    	}else{
					    		ids.push(selection[i].get('id'));
					    	}
	 					}
	 					FHD.ajax({
	 						url : __ctxPath+ '/icm/icsystem/removeconstructplanbyids.f',
	 						params : {
	 							constructPlanIds : ids
	 						},
	 						
	 						callback : function(data) {
	 							if (data.success) { 
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		 							me.constructPlanGrid.store.load();
	                            } else {
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
	                            }
	   						}
						});
					}
				}
			});
    	}
    },
    setstatus: function(){
    	var me = this;
    	
        var length = me.getSelectionModel().getSelection().length;
        me.constructPlanGrid.down('#icm_construct_del').setDisabled(length === 0);
        if(length != 1){
        	me.constructPlanGrid.down('#icm_construct_edit').setDisabled(true);
        }else{
        	me.constructPlanGrid.down('#icm_construct_edit').setDisabled(false);
        }
    },
    setCenterContainer:function(compent){
    	var me=this;
    	
    	me.removeAll(true);
    	me.add(compent);
    },
	reloadData:function(){
		var me=this;
		
		me.constructPlanGrid.store.load();
	}
});