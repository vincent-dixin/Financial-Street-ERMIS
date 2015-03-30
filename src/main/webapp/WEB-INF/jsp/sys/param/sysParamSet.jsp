<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
	<div id="test"></div>
	<div id="prop-grid"></div>
	<div id="button-container"></div>
	<div id="systemtrigger"></div>
	<select name="status" id="status" style="display: none;"> 
	      <option value="0">关闭</option>
	      <option value="1">开启</option>
	</select>
	<form action="">
		<input type="button" value="公司名称图片" id="name.gif" onclick="showChangePic(this)">
		<input type="button" value="公司标志图片" id="logo.gif" onclick="showChangePic(this)">
		<input type="button" value="首页banner图片" id="banner.jpg" onclick="showChangePic(this)">
		<input type="button" value="文件上传控件" id="fileUpload" onclick="openFileUploadWindow(500,300,'myCallBack','targetGatherImportTemplate.xls')">
	</form>
</body>
<script type="text/javascript">
	function myCallBack(id){
		alert(id);
	}
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
					if(rows.length==0)
					{
						Ext.MessageBox.alert('提示','最少选择一条信息进行删除！');
						return;
					}
					else
					{
						Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
							if(btn=='yes')
							{
								for(var i=0;i<rows.length;i++)
								{
									if('undefine'==rows[i].get('id'))
									{
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
									}
									else
									{
										delIds.push(rows[i].get('id'));
										store.remove(rows[i]);
									}
								}
							}
							else
							{
								return;
							}
						});
					}
				}
			},
			{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					grid.stopEditing();
					var modifiedRecord=store.getModifiedRecords();
					var jsonArray=[];
					Ext.each(modifiedRecord,function(item){
						jsonArray.push(item.data);
					});
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
				if('1'==value)
				{
					return '<span style="color:green;">开启</span>';
				}
				else
				{
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
	],null,'系统定时任务',900,500,'${ctx}/system/param/getSystemTrigger.do',false,true,ttb);
	grid=fhdgrid.grid;
	store=grid.getStore();
	function showChangePic(dom)
	{
		FHD.openWindow('修改系统图片', 700, 262, '${ctx}/sys/param/showSysPicImport.do?name='+dom.id);
	}	 
	Ext.onReady(function(){
		var keyArray=new Array();
		var valueArray=new Array();
	    var json=${source};
	    var propsGrid = new Ext.grid.PropertyGrid({
	        renderTo: 'prop-grid',
	        width: 300,
	        autoHeight: true,
	        propertyNames: {
	            tested: 'QA',
	            borderWidth: 'Border Width'
	        },
	        source: json,
	        viewConfig : {
	            forceFit: true,
	            scrollOffset: 2 // the grid will never have scrollbars
	        }
	    });
		propsGrid.on('afteredit',afterEdit);
		function afterEdit(e)
		{
			//alert(e.record.get('name')+" is modified:"+e.value);
			keyArray.push(e.record.get('name'));
			//alert(keyArray.length);
			valueArray.push(e.value);
			//alert(valueArray.length);
		}
	    // simulate updating the grid data via a button click
	    new Ext.Button({
	        renderTo: 'button-container',
	        text: 'Update source',
	        handler: function(){
	        	if(keyArray.length==0)
	        	{
	        		Ext.Msg.alert('提示','没有修改任何数据！');
	        		return;
	        	}
	        	propsGrid.stopEditing();
				Ext.Ajax.request({
					url:'${ctx}/sys/param/modifySysParam.do',
					success:function(response){
						if('true'==response.responseText)
						{
							Ext.Msg.alert('提示','修改成功!');
						}
						else
						{
							Ext.Msg.alert('提示','修改失败!');
						}
						keyArray=new Array();
					},
					failure:function(){
						Ext.Msg.alert('提示','请求失败!');
						valueArray=new Array();
					},
					params:{
						keyArray:keyArray.toString(),
						valueArray:valueArray.toString()
					}
				});
	        }
	    });
	    new Ext.Button({
	        renderTo: 'test',
	        text: 'test system prop',
	        handler: function(){
				Ext.Ajax.request({
					url:'${ctx}/sys/param/testModifySysParam.do',
					success:function(response){
						alert(response.responseText);
					},
					failure:function(){
						alert(response.responseText);
					}
				});
	        }
	    });
	    grid.on('validateedit',function(e){
	    	if('classname'==e.field)
	    	{
	    		Ext.Ajax.request({
					url:'${ctx}/sys/param/checkClassName.do',
					params:{className:e.value},
					success:function(response){
						if('true'==response.responseText)
						{
							e.cancel=false;
						}
						else
						{
							Ext.Msg.alert('提示','此类不是JOB接口的实现类！');
							e.cancel=true;
						}
					},
					failure:function(){
						Ext.Msg.alert('提示','检测类名失败');
						e.cancel=true;
					}
				});
	    	}
	    	if('cronexpression'==e.field)
	    	{
    		  	Ext.Ajax.request({
					url:'${ctx}/sys/param/checkCronExpression.do',
					params:{cronExpression: e.value},
					success:function(response){
						if('true'==response.responseText)
						{
							e.cancel=false;
						}
						else
						{
							Ext.Msg.alert('提示','时间表达式不正确！');
							e.cancel=true;
						}
					},
					failure:function(){
						Ext.Msg.alert('提示','检测类名失败');
						e.cancel=true;
					}
				});
	    	}
	    });
	});
</script>
</html>