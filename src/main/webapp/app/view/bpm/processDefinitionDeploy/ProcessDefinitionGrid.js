
Ext.define('FHD.view.bpm.processDefinitionDeploy.ProcessDefinitionGrid',{
    extend: 'FHD.ux.GroupGridPanel',
    alias: 'widget.ProcessDefinitionGrid',
    
    initComponent : function() {
    	var me = this;
    	var tbarItems=new Array();
    	tbarItems.push({name:'save', text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',handler:me.save, scope : this});
    	tbarItems.push({name:'update', text : FHD.locale.get('fhd.common.edit'),iconCls: 'icon-edit',handler:me.update, disabled : true, scope : this});
    	tbarItems.push({name:'remove', text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',handler:me.remove, disabled : true, scope : this});
    	var listeners={
			/*按钮控制监听*/
			selectionchange:function(selection,selecteds){
				me.down("[name='update']").disable();
				me.down("[name='remove']").disable();
			   	if(selecteds.length>0){
		   			me.down("[name='remove']").enable();
			   	}
			   	if(selecteds.length==1){
		   			me.down("[name='update']").enable();
			   	}
			}
		};
        Ext.apply(me, {
			url : __ctxPath + '/jbpm/processDefinitionDeploy/vJbpmDeploymentPage.f',
    		cols : [
				{dataIndex:'id',hidden:true},
				{dataIndex:'processDefinitionDeployId',hidden:true},
				{header: "流程定义名称",dataIndex: 'disName',sortable: true,flex:1},
				{header: "流程定义ID",dataIndex: 'pdid',sortable: true,flex:1},
				{header: "流程版本",dataIndex: 'pdversion',sortable: true,flex:1},
				{header: "流程实例数量",dataIndex: 'executionCount',sortable: true,flex:1}
			],
			/*默认排序方式*/
			sorter:[{property:'disName',direction:'asc'}],
			/*工具栏*/
			tbarItems:tbarItems,
			listeners:listeners
        });
        me.callParent(arguments);
    },
    save:function(){
    	var me=this;
    	var winId = "win" + Math.random()+"$ewin";
		var panel = Ext.create("FHD.view.bpm.processDefinitionDeploy.ProcessDefinitionDeployFrom",{
			winId: winId
		});
		var window = Ext.create('FHD.ux.Window',{
			id:winId,
			title:"流程定义",
			iconCls: 'icon-edit',
			items: panel, 
			maximizable: true,
			resizable: false,
			listeners:{
				close : function(){
					me.store.load();
				}
			}
		});
		window.show();
    },
    update:function(){
    	var me=this;
    	var selecteds = me.getSelectionModel().getSelection();
    	var processDefinitionDeployId=selecteds[0].get('processDefinitionDeployId');
    	var winId = "win" + Math.random()+"$ewin";
		var panel = Ext.create("FHD.view.bpm.processDefinitionDeploy.ProcessDefinitionDeployFrom",{
			winId: winId,
			processDefinitionDeployId:processDefinitionDeployId
		});
		var window = Ext.create('FHD.ux.Window',{
			id:winId,
			title:"流程定义",
			iconCls: 'icon-edit',
			items: panel, 
			maximizable: true,
			resizable: false,
			listeners:{
				close : function(){
					me.store.load();
				}
			}
		});
		window.show();
    },
    remove:function(){
    	var me=this;
    	Ext.MessageBox.confirm('警告', '确认删除？', function showResult(btn){
	        if("yes"==btn){
		    	var selecteds = me.getSelectionModel().getSelection();
		    	var ids=new Array();
		    	for(var i=0;i<selecteds.length;i++){
		    		ids.push(selecteds[i].get('id'));
				}
				jQuery.ajax({
					type: "POST",
					url: __ctxPath +"/jbpm/removeDeployment.f",
					data: "deploymentIdsStr="+ids,
					success: function(msg){
						me.store.load();
						Ext.MessageBox.alert('提示', '操作成功!');
					},
					error: function(){
						Ext.MessageBox.alert('提示',"操作失败!");
					}
				});
	        }
	    });
    },
    processImpShow : function (processDefinitionDeployId){
    	var me=this;
		var panel = Ext.create('FHD.view.bpm.processDefinitionDeploy.ProcessImpShow',{
			processDefinitionDeployId : processDefinitionDeployId
		});
		var window = Ext.create('FHD.ux.Window',{
			title:"查看流程图",
			iconCls:  __ctxPath + '/images/icons/action_order_20.gif',//标题前的图片
			items: panel,
			maximizable: true,
			resizable: false
		});
		window.show();
	},
    showBusiness : function (businessId,url){
    	if(null!=url){
			var taskPanel = Ext.create(url,{
				businessId : businessId
			});
			var window = Ext.create('FHD.ux.Window',{
				title:"业务查看",
				iconCls:  __ctxPath + '/images/icons/icon_views.gif',//标题前的图片
				items: taskPanel, 
				maximizable: true,
				resizable: false
			});
			window.show();
    	}else{
    		Ext.MessageBox.alert('提示', '未设定业务查看路径');
    	}
	}
    
});
