/*
 * 内控评价列表页面 
 * */
Ext.define('FHD.view.icm.assess.AssessPlanList',{
	extend: 'Ext.container.Container',
    alias: 'widget.assessplanlist',
    
    pagable:true,
    layout: 'fit',
    
    initComponent: function(){
    	var me = this;
    	
		//评价计划列表
		me.assessPlanGrid = Ext.create('FHD.ux.GridPanel', {
	        border: false,
	        region: 'center',
	        url: __ctxPath + '/icm/assess/findAssessPlanListByParams.f?companyId='+__user.companyId+'&dealStatus=N',
	        cols: [
	            {dataIndex: 'id',hidden:true},
	    		{header : '计划编号',dataIndex : 'code',sortable : true, flex : 1}, 
	 			{header:'计划名称', sortable: false,dataIndex: 'name',flex:3,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showPlanViewList('" + record.data.id + "','" + record.data.dealStatus +"')\" >" + value + "</a>"; 
					}
				},
	 			{header : '评价方式',dataIndex :'assessMeasure',sortable : true,flex:1}, 
	 			{header : '评价类型',dataIndex : 'type',sortable : true, flex:1}, 
	 			{header : '计划开始日期',dataIndex : 'beginDate',sortable : true, flex : 1},
	 			{header : '计划完成日期',dataIndex : 'endDate',sortable : true, flex : 1},
	 			{header : '状态',dataIndex :'status',sortable : true,flex:1,
					renderer:function(value){
						if(value=='S'){
							return '已保存';
						}else if(value=='P'){
							return '已提交';
						}
						return value;
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
						}else if(value=='A'){
							return '逾期';
						}
						return value;
					}
	 			},
				{header : '创建时间',dataIndex :'createTime',sortable : true, flex : 1}
			],
            tbarItems: [
				{iconCls : 'icon-add',id:'icm_assessplan_add',text:'添加',tooltip: '添加评价计划',handler :me.addPlan,scope : this},
				'-', 
				{iconCls : 'icon-edit',id:'icm_assessplan_edit',text:'修改',tooltip: '修改评价计划',handler :me.edit,scope : this},
				'-', 
				{iconCls : 'icon-del',id:'icm_assessplan_del',text:'删除',tooltip: '删除评价计划',handler :me.del,disabled: true,scope : this}
			]
		});
		me.assessPlanGrid.store.on('load', function () {
            me.setstatus()
        });
        me.assessPlanGrid.on('selectionchange', function () {
            me.setstatus()
        });
        //评价计划列表
        me.callParent(arguments);
        me.add(me.assessPlanGrid);
    },
    //新增计划
    addPlan:function(){
    	var me=this;
    	
    	var assessplanmainpanel = me.up('assessplanmainpanel');
    	if(assessplanmainpanel){
    		assessplanmainpanel.paramObj.editflag=false;
    		assessplanmainpanel.paramObj.businessId='';
    		//激活新增面板
    		assessplanmainpanel.navBtnHandler(1);
    	}
    },
    //编辑计划
    edit: function(button){
    	var me=this;
    	
    	var selection = me.assessPlanGrid.getSelectionModel().getSelection();//得到选中的记录
    	var isSubmit=selection[0].get('status');
    	if(isSubmit=='P'){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'对不起,您不能修改已提交的数据!');
    		return false;
    	}
    	
    	var assessplanmainpanel = me.up('assessplanmainpanel');
    	if(assessplanmainpanel){
    		assessplanmainpanel.paramObj.editflag=true;
    		assessplanmainpanel.paramObj.businessId=selection[0].get('id');
    		
    		//激活编辑面板
    		assessplanmainpanel.navBtnHandler(1);
    	}
    },
    //删除计划
    del: function(){
		var me=this;
		
		var selection = me.assessPlanGrid.getSelectionModel().getSelection();//得到选中的记录
		
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
	 						url : __ctxPath+ '/icm/assess/removeAssessPlanByIds.f',
	 						params : {
	 							assessPlanIds : ids
	 						},
	 						callback : function(data) {
	 							if (data) { 
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		 							me.assessPlanGrid.store.load();
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
    	
        var length = me.assessPlanGrid.getSelectionModel().getSelection().length;
        me.assessPlanGrid.down('#icm_assessplan_del').setDisabled(length === 0);
        if(length != 1){
        	me.assessPlanGrid.down('#icm_assessplan_edit').setDisabled(true);
        	//me.assessPlanGrid.down('#icm_assessplan_report').setDisabled(true);
        }else{
        	me.assessPlanGrid.down('#icm_assessplan_edit').setDisabled(false);
        	//me.assessPlanGrid.down('#icm_assessplan_report').setDisabled(false);
        }
    },
    showPlanViewList:function(id,dealStatus){
    	var me=this;
    	
    	me.assessPlanPanel=Ext.create('FHD.view.icm.assess.form.AssessPlanPreview',{
    		assessPlanId:id,
			dealStatus:dealStatus
		});
		
		var win = Ext.create('FHD.ux.Window',{
			title:'评价计划详细信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.assessPlanPanel]
    	}).show();
    },
	reloadData:function(){
		var me=this;
		
		me.assessPlanGrid.store.load();
	}
});