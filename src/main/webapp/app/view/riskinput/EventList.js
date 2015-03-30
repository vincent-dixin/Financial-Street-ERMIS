/*
 * 内控评价列表页面 
 * */
Ext.define('FHD.view.riskinput.EventList',{
	extend: 'FHD.ux.GridPanel',
    alias: 'widget.eventlist',
    requires: [
    	'FHD.view.riskinput.form.ConstructPlanRangeFormForView',
    	'FHD.view.riskinput.SelectRiskPanel'
    ],
    pagable:true,
    layout: 'fit',
    flex : 12,
    border:false,
	//可编辑列表为只读属性
	readOnly : false,
	url : '',//__ctxPath + '/icm/icsystem/constructplan/findconstructplansbypage.f',
    initComponent: function(){
    	var me = this;
    	me.selectriskpanel = Ext.widget('selectriskpanel',{
    		pr : me,
    		constrain: true,
	        layout: 'fit',
	        resizable:true,
//	        iconCls: 'icon-edit', //标题前的图片
	        title:'风险选择',
	        modal: true, //是否模态窗口
	        collapsible: true,
	        scroll: 'auto',
	        closeAction:'destory',
	        width: 500,
	        height: 200,
	        maximizable:true
    	});
		//评价计划列表
        me.on('selectionchange',me.setstatus);//选择记录发生改变时改变按钮可用状态
        
        me.cols = [
 			{header : '风险',dataIndex : 'name',sortable : false, flex : 2},
 			{header : '发生可能性',dataIndex : 'probability',sortable : false, flex:2}, 
 			{header : '是否已采取应对',dataIndex : 'response',sortable : false, flex : 2,
 				renderer:function(value){
					if(value=='Y'){
						return '已采取';
					}else if(value=='N'){
						return '未采取';
					}
				}
 			},
 			{header : '状态',dataIndex :'status',sortable : false,flex:2,
				renderer:function(value){
					if(value=='S'){
						return '已保存';
					}else if(value=='P'){
						return '已提交';
					}else{
						return '已处理';
					}
				}
 			},
 			{header : '执行状态',dataIndex :'dealStatus',sortable : false,flex:2,
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
			{header : '创建时间',dataIndex :'createTime',sortable : false, flex : 2}
		];
		if(me.readOnly){
			me.searchable = false;
		}else{
	        me.tbarItems = [
	        	{iconCls : 'icon-add',id:'riskinput_add',text: '添加',tooltip: '添加',handler :me.addPlan,scope : this},
				'-', 
				{iconCls : 'icon-edit',id:'riskinput_edit',text: '修改',tooltip: '修改',handler :me.editPlan,scope : this},
				'-', 
				{iconCls : 'icon-del',id:'riskinput_del',text: '删除',tooltip: '删除',handler :me.delPlan,disabled: true,scope : this}
	        ];
		}
        //评价计划列表
        Ext.apply(me,{
        	border:me.border
        });
        me.callParent(arguments);
    },
    //新增计划
    addPlan:function(){
    	var me=this;
        me.selectriskpanel.show();
    },
    //编辑计划
    editPlan: function(button){
    	var me=this;
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	var isSubmit=selection[0].get('status');
    	if(isSubmit=='P' || isSubmit == 'D'){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'对不起,您不能修改已提交或者是已处理的数据!');
    		return false;
    	}
    	
    	var eventeditpanel = me.up('eventeditpanel');
    	if(eventeditpanel){
    		eventeditpanel.initParam({
    		editFlag : true,
    		businessId : selection[0].get('id')
    	});
    	eventeditpanel.reloadData();
		//激活编辑面板
		eventeditpanel.navBtnHandler(1);
    	}
    },
    //删除计划
    delPlan: function(){
		var me=this;
		
		var selection = me.getSelectionModel().getSelection();//得到选中的记录
		
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
	 						url : __ctxPath+ '..........',
	 						params : {
	 							riskEventIds : ids
	 						},
	 						
	 						callback : function(data) {
	 							if (data.success) { 
	                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		 							me.store.load();
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
        me.down('#icm_construct_del').setDisabled(length == 0);
        if(length != 1){
        	me.down('#icm_construct_edit').setDisabled(true);
        }else{
        	me.down('#icm_construct_edit').setDisabled(false);
        }
    },
    setCenterContainer:function(compent){
    	var me=this;
    	
    	me.removeAll(true);
    	me.add(compent);
    },
	reloadData:function(){
		var me=this;
		me.store.proxy.url = __ctxPath + '/icm/icsystem/constructplan/findconstructplansbypage.f',
        me.store.proxy.extraParams = {
        	status : 'S'
        };
		me.store.load();
	},
	showPlanViewList:function(id,dealStatus){
    	var me=this;
    	me.constructPlanRangeForm = Ext.widget('constructplanrangeformforview',{
        	businessId : id,
        	editflag: false
        });
        me.constructPlanRangeForm.initParam({
        	businessId : id
        });
        me.constructPlanRangeForm.reloadData();
		var win = Ext.create('FHD.ux.Window',{
			title:'建设计划详细信息',
			//modal:true,//是否模态窗口
			collapsible:true,
			maximizable:true,//（是否增加最大化，默认没有）
			autoScroll : true,
			items:[ me.constructPlanRangeForm ]
    	}).show();
    }
});