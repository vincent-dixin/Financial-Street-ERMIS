<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div id="systemtrigger"></div>
	<select name="status" id="status" style="display: none;"> 
	      <option value="0">关闭</option>
	      <option value="1">开启</option>
	</select>
</body>
<script type="text/javascript">
	//定时任务的Record
	var systemTriggerRegion=Ext.data.Record.create([
		{name:'id',type:'string'},
		{name:'classname',type:'string'},
		{name:'cronexpression',type:'string'},
		{name:'desc',type:'string'},
		{name:'status',type:'string'}
	]);
	//要删除的记录的IDs;
	var delIds=new Array();
	var testArray=new Array();
	var ttb=new Ext.Toolbar({
		items:[
			{
				text:'增加定时任务',
				iconCls:'icon-add',
				handler:function(){
					var initValue={id:'undefine',classname:'full class name',cronexpression:'任务执行时间',desc:'任务描述',status:'1'};
					var newRecord=new systemTriggerRegion(initValue);
					grid.stopEditing();
					store.insert(store.getCount(),newRecord);
					grid.startEditing(store.getCount()-1,0);
				}
			},
			{
				text:'删除定时任务',
				iconCls:'icon-del',
				handler:function(){
					var rows=grid.getSelectionModel().getSelections();
					if(rows.length==0){
						Ext.MessageBox.alert('提示','最少选择一条信息进行删除！');
						return;
					}else{
						for(var i=0;i<rows.length;i++){
							if('undefine'==rows[i].get('id')){
								/*var curModified=store.getModifiedRecords();
								var index=curModified.indexOf(rows[i]);
								var item=curModified[index];
								alert(itemitem);
								alert('index of del '+index);
								alert('before del length:'+curModified.length);
								curModified.remove(item);
								alert('after del length:'+curModified.length);
								*/
								store.remove(rows[i]);
							}else{
								delIds.push(rows[i].get('id'));
								store.remove(rows[i]);
							}
						}
					}
				}
			},
			{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					grid.stopEditing();
					//var rows = grid.getSelectionModel().getSelections();
					var rows = store.getModifiedRecords();
					var jsonArray=[];
					Ext.each(rows,function(item){
						jsonArray.push(item.data);
					});
					if(delIds.length>0){
						Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
							if(btn=='yes'){
								Ext.Ajax.request({
									url:'${ctx}/system/param/modifySystemTrigger.do',
								    method:'post',
								    params:{
										delIds: delIds.toString(),
										modifiedRecord: Ext.encode(jsonArray)
								    },
								    success:function(response){
								      	if(response.responseText=='true'){
								      		store.commitChanges();
								      		store.reload();
					                		delIds = new Array();
					                		Ext.Msg.alert('提示','保存成功!');
									    }else{
									     	Ext.Msg.alert('提示','保存失败!');
									    }
								    }
								});
								
								store.commitChanges();
							}else{
								return;
							}
						});
					}else{
						Ext.Ajax.request({
							url:'${ctx}/system/param/modifySystemTrigger.do',
						    method:'post',
						    params:{
								delIds: delIds.toString(),
								modifiedRecord: Ext.encode(jsonArray)
						    },
						    success:function(response){
						      	if(response.responseText=='true'){
						      		store.commitChanges();
						      		store.reload();
			                		delIds = new Array();
			                		Ext.Msg.alert('提示','保存成功!');
							    }else{
							     	Ext.Msg.alert('提示','保存失败!');
							    }
						    }
						});
						
						store.commitChanges();
					}
				}
			}
		]
	});
	var grid; 
	var store;
	var fhdgrid=new FHD.ext.EditGrid('systemtrigger',null,[
	{
		dataIndex:'id',
		width:0
	},
	{
		header: "类名称",
		dataIndex: 'classname',
		sortable: true,
        width: 300,
        editor:new Ext.form.TextField({
        	allowBlank:false
        })
     }, 
     { 
        header: "执行时间", 
        dataIndex: 'cronexpression',
        sortable: true,
        width: 160,
        editor:new Ext.form.TextField({
        	allowBlank:false
        })		    
     },
	 {
		header:'任务描述',
		dataIndex:'desc',
		width:100,
		editor:new Ext.form.TextField({
			allowBlank:true
		})
	 },
	 {
		header:'状态',
		dataIndex:'status',
		width:100,
		renderer:function(value){
			if('1'==value){
				return '<span style="color:green;">开启</span>';
			}else{
				return '<span style="color: red;">关闭</span>';
			}
		},
		editor:new Ext.form.ComboBox({
			typeAhead:true,
			triggerAction:'all',
			lazyRender:true,
			listClass:'x-combo-list-samll',
			transform:'status'
		})
	  }
	],null,'',document.body.offsetWidth,480,'${ctx}/system/param/getSystemTrigger.do',false,true,ttb);
	grid=fhdgrid.grid;
	store=grid.getStore();

	store.on("load",function(store, recoreds, options){
		delIds=new Array();
		store.commitChanges();
	});
</script>
</html>