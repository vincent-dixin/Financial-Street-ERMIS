/**
 * 计划任务
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.st.PlanMan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planMan',
    
    requires: [
           'FHD.view.sys.st.PlanEdit',
           'FHD.view.sys.st.PlanLog'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = 'planManId';
    	
		me.planManPanel;
    	me.isAdd;
    	me.formwindow;
    	me.viewwindow;
    	me.planDelUrl = 'sys/st/removePlanBatch.f'; //删除url
    	me.planQueryUrl = 'sys/st/queryPlanPage.f'; //查询分页url
    	me.planStatusUrl = 'sys/st/mergePlanBatch.f';//启用/停用url
    	me.planViewUrl = 'sys/st/viewPlan.f';//查看url
    	me.logGridUrl = 'pages/sys/st/planDealLogMan.jsp';
    	me.gridColums =[
     		{header:'',dataIndex: 'planEmpId', sortable: true, flex : 1,hidden : true},
     		{header: FHD.locale.get('fhd.sys.planEdit.name') ,dataIndex: 'name', defaultSortable:true, sortable: true,flex : 1,
     			renderer:function(value,metaData,record,colIndex,store,view) { 
     				metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
     	    		return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('planManId').view('" + record.get('id') + "', Ext.getCmp('planManId'))\">"+value+"</a>";
     			}
     		},
     		{header: FHD.locale.get('fhd.sys.planMan.triggerType') ,dataIndex: 'triggerName',sortable: true,flex : 1},
     		{header: FHD.locale.get('fhd.sys.planMan.triggerDataSet') ,dataIndex: 'triggerDateText', sortable: true, flex : 1},
     		{header: FHD.locale.get('fhd.sys.planMan.task') ,dataIndex: 'zq', sortable: true, flex : 1},
     		{header: FHD.locale.get('fhd.sys.planMan.operate') ,dataIndex: 'statusName', sortable: true, flex : 1,
     			renderer:function(value,metaData,record,colIndex,store,view) {
	     			if(record.get('status') == 1){
	     				//启用
	     				return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('planManId').stop('" + record.get('id') + "', Ext.getCmp('planManId'))\">" + 
	     						"<image src='images/icons/stop.gif' width='15' height='15'/></a>&nbsp;&nbsp;&nbsp;" + 
	     						"<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('planManId').viewLog('" + record.get('id') + "', Ext.getCmp('planManId'))\">" +
	     						"<image src='images/icons/table_go.png' width='15' height='15'/></a>";
	     			}else{
	     				//停用
	     				return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('planManId').start('" + record.get('id') + "', Ext.getCmp('planManId'))\">" + 
	     						"<image src='images/icons/start.gif' width='15' height='15'/></a>&nbsp;&nbsp;&nbsp;" + 
	     						"<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('planManId').viewLog('" + record.get('id') + "', Ext.getCmp('planManId'))\">" +
	     						"<image src='images/icons/table_go.png' width='15' height='15'/></a>";
	     						
	     			}
     			}
     		},
     		{header: FHD.locale.get('fhd.sys.planMan.status') ,dataIndex: 'status', sortable: true, flex : 1,hidden : true}
     	];
    	
    	var tbar =[//菜单项
	    	{text : FHD.locale.get('fhd.common.add'), iconCls: 'icon-add',id:'planMan_add',
	    		handler:function(){me.edit(me, 'planMan_add');}, scope : this},'-',
	    	{text : FHD.locale.get('fhd.common.modify'), iconCls: 'icon-edit',id:'planMan_edit',
	    			handler:function(){me.edit(me, 'planMan_edit');}, disabled : true, scope : this},'-',
	    	{text : FHD.locale.get('fhd.common.del'), iconCls: 'icon-del',id:'planMan_del',
	    		handler:function(){me.del(me);}, disabled : true, scope : this},'-',
	    	{text : FHD.locale.get('fhd.sys.planMan.start'), iconCls: 'icon-plan-start',id:'planMan_start',
	    		handler:function(){me.start(null, me);}, disabled : true, scope : this},'-',
	    	{text : FHD.locale.get('fhd.sys.planMan.stop'), iconCls: 'icon-plan-stop',id:'planMan_stop',
	    		handler:function(){me.stop(null, me);}, disabled : true, scope : this}
     				
  		];
    	
    	me.grid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
    		url: me.planQueryUrl,//调用后台url
    		region: 'center',
    		cols:me.gridColums,//cols:为需要显示的列
    		tbarItems:tbar
    		//listeners:{itemdblclick:function(){me.edit(this)}}//双击执行修改方法
    	});
    	
    	me.grid.store.on('load',function(){me.setstatus(me)});
    	me.grid.on('selectionchange',function(){me.setstatus(me)});
    	
        Ext.apply(me, {
        	//height:FHD.getCenterPanelHeight(),
     	    border:false,
     		layout: {
     	        type: 'fit'
     	    },
     	    items:[me.grid]
        });

        me.callParent(arguments);
	},
	
	edit : function(me, id){//新增方法
		var selection = me.grid.getSelectionModel().getSelection()[0];//得到选中的记录
		if(selection != null){
			me.planId = selection.get('id');
		}
		me.planEdit = Ext.widget('planEdit', {planManId : me});
		me.formwindow = new Ext.Window({
			layout:'fit',
			iconCls: 'icon-edit',//标题前的图片
			modal:true,//是否模态窗口
			collapsible:true,
			width:600,
			height:600,
			maximizable:true,//（是否增加最大化，默认没有）
			constrain:true,
			items : [me.planEdit]
			//autoLoad:{url:me.planAddUrl,nocache:true,scripts:true}
		});
		me.formwindow.show();
		
		if(id == 'planMan_add'){
			me.isAdd = true;
			me.formwindow.setTitle(FHD.locale.get('fhd.common.add'));
		}else{
			me.formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
			me.formwindow.initialConfig.id = selection.get('id') + ',' + selection.data.planEmpId;
			me.isAdd = false;
		}
	},
	
	setstatus : function(me){//设置你按钮可用状态
		me.grid.down('#planMan_edit').setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
		me.grid.down('#planMan_del').setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
		me.grid.down('#planMan_start').setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
		me.grid.down('#planMan_stop').setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
	},
	
	close : function(me){
		me.viewwindow.close();
	},

	del : function(me){//删除方法
		var selection = me.grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {//确认删除
					var ids = [];
					
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({//ajax调用
						url : me.planDelUrl,
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){//删除成功！
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								me.grid.store.load();
							}
						}
					});
				}
			}
		});
	},

	start : function(id, me){//启用
		var selection = me.grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.sys.planMan.start'),
			width : 260,
			msg : FHD.locale.get('fhd.sys.planMan.isStart'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var ids = [];
					if(selection.length == 0){
						ids.push(id);
					}else{
						for(var i=0;i<selection.length;i++){
							ids.push(selection[i].get('id'));
						}
					}
					FHD.ajax({//ajax调用
						url : me.planStatusUrl + '?status=1',
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								me.grid.store.load();
							}
						}
					});
				}
			}
		});
	},

	stop : function(id, me){//停用
		var selection = me.grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.sys.planMan.stop'),
			width : 260,
			msg : FHD.locale.get('fhd.sys.planMan.isStop'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var ids = [];
					if(selection.length == 0){
						ids.push(id);
					}else{
						for(var i=0;i<selection.length;i++){
							ids.push(selection[i].get('id'));
						}
					}
					FHD.ajax({//ajax调用
						url : me.planStatusUrl + '?status=0',
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								me.grid.store.load();
							}
						}
					});
				}
			}
		});
	},

	viewLog : function(id, me){
		var selection = me.grid.getSelectionModel().getSelection();//得到选中的记录
		me.planId = id;
		me.planLog = Ext.widget('planLog', {planManId : me});
		me.formwindow = new Ext.Window({
			layout:'fit',
			iconCls: 'icon-table-go',//标题前的图片
			modal:true,//是否模态窗口
			collapsible:true,
			width:800,
			height:600,
			maximizable:true,//（是否增加最大化，默认没有）
			items : [me.planLog]
			//autoLoad:{url:me.logGridUrl,nocache:true,scripts:true}
		});
		me.formwindow.show();
		me.formwindow.setTitle(FHD.locale.get('fhd.sys.planDealLogMan.log'));
		me.formwindow.initialConfig.id = selection[0].data.planEmpId;
	},
	
	view : function(id, me){
		FHD.ajax({//ajax调用
			url : me.planViewUrl,
			params : {
				id:id
			},
			callback : function(data){
				me.viewwindow = new Ext.Window({
					title:FHD.locale.get('fhd.sys.planMan.look'),
			    	layout:'fit',
					iconCls: 'icon-report',//标题前的图片
					modal:true,//是否模态窗口
					collapsible:true,
					width:450,
					height:350,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[Ext.create('Ext.panel.Panel',{
	            		width: 300,
	                    bodyStyle: "padding:5px;font-size:12px;"
	            	})],
					listeners: {
		                afterlayout: function() {
							var panel = this.down('panel');
							var content = '';
							var content2 = '';
							var contentTime = '';
							if(data.triggerTypes == 'st_trigger_type_time'){
							  content = '<tr>' +
					  '<td class="alt">' + FHD.locale.get('fhd.sys.planMan.triggerType') + '：</td>' +
					  '<td>{triggerName}</td>' +
					  '</tr>' +
					  '<tr>' +
					    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.lookType') + '：</td>' +
					    '<td>{lookName}</td>'+
					    '</tr>' +
					  '<tr>' +
					    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.createBy') + '：</td>' +
					    '<td>{tempName}</td>' +
					  '</tr>';
						if(data.recycle){
							content2 =
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerTime') + '：</td>' +
						    '<td>{triggetTime}</td>' +
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.model') + '：</td>' +
						    '<td>{triggerDateText}</td>' +
						  '</tr>';

						  if(data.timeType == '1'){
							  contentTime = '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.startTime') + '：</td>' +
							    '<td>{startTime}</td>' +
							  '</tr>' +
							  '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.entTime') + '：</td>' +
							    '<td>{endTime}</td>' + '</tr>';
						  }else{
							  contentTime = '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.periodScope') + '：</td>' +
							    '<td>' + FHD.locale.get('fhd.sys.planEdit.timeForever') + '</td>' + '</tr>';
						  }
						}else{
							content2 = '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerData') + '：</td>' +
						    '<td>{triggerData}</td>' +
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerTime') + '：</td>' +
						    '<td>{triggetTime}</td>' +
						    '</tr>';
						    }
							}else{
								content = '<td class="alt">' + FHD.locale.get('fhd.sys.planMan.triggerType') + '：</td>' +
						  '<td>{triggerName}</td>' +
						  '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.lookType') + '：</td>' +
						    '<td>{lookName}</td>'+
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.createBy') + '：</td>' +
						    '<td>{tempName}</td>' +
						  '</tr>';
							}
							
			                tpl = Ext.create('Ext.Template', 
			                		'<fieldset class="x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default">',
									'<legend class="x-fieldset-header x-fieldset-header-default">' + FHD.locale.get('fhd.common.details') + '</legend>',
									'<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="0" class="fhd_add">',
									  '<tr >',
									    '<td class="alt" width="15%">' + FHD.locale.get('fhd.sys.planEdit.name') + '：</td>',
									    '<td width="35%">{name}</td>',
									  '</tr>' + content + content2 + contentTime,
									  '<tr>',
									    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.status') + '：</td>',
									    '<td>{statusName}</td>',
									  '</tr>',
									 '</table>',
									 '</fieldset>'
			                );
			
			                tpl.overwrite(panel.body, data);
		                }
		            },
		            buttons:[
		            	{text: FHD.locale.get('fhd.common.cancel'),handler:function(){me.close(me)}}
		            ]
			    }).show();
			}
		});
	}
});