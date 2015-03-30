
Ext.define('FHD.view.bpm.processinstance.ProcessInstanceGrid',{
    extend: 'FHD.ux.GroupGridPanel',
    alias: 'widget.ProcessInstanceGrid',
    url : __ctxPath + '/jbpm/processInstance/processInstanceList.f',
    
    model:"show",
    extraParams:{assigneeId:__user.userId,dbversion:1},
    initComponent : function() {
    	var me = this;
    	var tbarItems=new Array();
    	var listeners={};
    	var checked=false;
    	if(me.model=="edit"){
	    	tbarItems.push({name:'del', text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',handler:me.del, disabled : true, scope : this});
	    	listeners={
				/*按钮控制监听*/
				selectionchange:function(selection,selecteds){
					me.down("[name='del']").disable();
				   	if(selecteds.length>0){
				   		var flag=true;
				   		for(var i=0;i<selecteds.length;i++){
				   			if(selecteds[i].get('endactivity')=="end1" || selecteds[i].get('endactivity')=="remove1"){
								flag=false;
				   			}
				   		}
				   		if(flag){
				   			me.down("[name='del']").enable();
				   		}
				   	}
				}
			};
			checked=true;
    	}
    	Ext.apply(me,{
    		checked:checked,
    		cols : [
				{dataIndex:'id',hidden:true},
				{dataIndex:'processInstanceId',hidden:true},
				{dataIndex:'url',hidden:true},
				{dataIndex:'businessId',hidden:true},
				{dataIndex:'endactivity',hidden:true},
				{header: "流程名称",dataIndex: 'businessName',sortable: true,flex:2}, 
				{header: "类型",dataIndex: 'jbpmDeploymentName',sortable: true,flex:2},
				{header: "发起人",dataIndex: 'createByRealname',sortable: true,flex:1},
				{header: "发起时间",dataIndex: 'createTime',sortable: true,flex:2},
				{header: "执行情况",dataIndex: 'endactivityShow',sortable: true,flex:1},
				{
				 	header: FHD.locale.get('fhd.formula.operate'),
				    xtype:'actioncolumn',
				    dataIndex: 'id',
				    flex:1,
			        align:'center',
				    items: [{
				    	icon: __ctxPath + '/images/icons/action_order_20.gif',
				        tooltip: '流程查看',
				        handler: function(grid, rowIndex, colIndex) {
		                    var rec = grid.getStore().getAt(rowIndex);
		                    me.editProcessInstance(rec.get('id'),rec.get('processInstanceId'),rec.get('url'),rec.get('businessId'));
	                	}
				    }]
				}
			],
			/*默认排序方式*/
			sorter:[{property:'endactivity',direction:'asc'},{property:'createTime',direction:'desc'}],
			/*工具栏*/
			tbarItems:tbarItems,
			listeners:listeners
    	});
        me.callParent(arguments);
    },
    del:function(){
    	var me=this;
    	var selecteds = me.getSelectionModel().getSelection();
    	var processInstanceIds=new Array();
    	for(var i=0;i<selecteds.length;i++){
    		processInstanceIds.push(selecteds[i].get('processInstanceId'));
		}
		jQuery.ajax({
			type: "POST",
			url: __ctxPath +"/jbpm/removePorcessInstance.f",
			data: "processInstanceIdsStr="+processInstanceIds,
			success: function(msg){
				me.store.load();
				Ext.MessageBox.alert('提示', '操作成功!');
			},
			error: function(){
				Ext.MessageBox.alert('提示',"操作失败!");
			}
		});
    },
    editProcessInstance : function (jbpmHistProcinstId,processInstanceId,url,businessId){
    	var me=this;
		var taskPanel = Ext.create('FHD.view.bpm.processinstance.ProcessInstanceTab',{
			jbpmHistProcinstId : jbpmHistProcinstId,
			processInstanceId : processInstanceId,
			url:url,
			businessId:businessId,
			model:me.model
		});
		var window = Ext.create('FHD.ux.Window',{
			title:"流程查看",
			iconCls:  __ctxPath + '/images/icons/icon_views.gif',//标题前的图片
			items: taskPanel,
			maximizable: true,
			border:0
		});
		window.show();
	}
    
});
