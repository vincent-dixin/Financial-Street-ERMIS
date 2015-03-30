<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<script type="text/javascript">
		var list;
		Ext.onReady(function(){
			var cols = [{hidden:true,dataIndex:"id"},
			            {header:"编号",width:100,dataIndex:"topicCode"},
			            {header:"名称",width:100,dataIndex:"topicName"},
			            {header:"类型",width:100,dataIndex:"type",
					           renderer:function(value, metaData, record, rowIndex, colIndex, store){
					        	   var s = "";
					        	   if(value === "catalog") {
					        		   s = "目录帮助";
					        	   }else {
					        		   s = "关键词提示";
					        	   }
					        	   return s;
								}},
			            {header:"内容",width:500,dataIndex:"content"}
			            ];
			var toolbars = [{text:'添加',handler:add,iconCls:'icon-add'}, 
			               {id:'edit',text:'修改',handler:edit,iconCls:'icon-edit',disabled:true}, 
			               {id:'del',text:'删除',handler:del,iconCls:'icon-del',disabled:true},
			               {id:'view',text:'预览',handler:view,iconCls:'icon-text-columns',disabled:true}];
			list = new FHD.ext.Grid('grid', {catalogid:'${param.catalogid}'}, cols, null,"", "auto", Ext.getBody().getHeight(),'queryHelpTopic.do', false,true, toolbars);
			
			//复选框被选中
		 	list.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
		 		var rows =list.grid.getSelectionModel().getSelections(); 
		 		
		 		if(rows.length==1){
		 			 Ext.getCmp("edit").enable();
		 			 Ext.getCmp("view").enable();
		 		}else{
		 			Ext.getCmp("edit").disable();
		 			Ext.getCmp("view").disable();
		 		}
		 	    Ext.getCmp("del").enable();
		 	});
		 	//复选框被选中
		 	list.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
		 		var rows =list.grid.getSelectionModel().getSelections(); 
		 		if(rows.length==0){
		 			 Ext.getCmp("edit").disable();
		 			 Ext.getCmp("del").disable();
		 			 Ext.getCmp("view").disable();
		 		}else if(rows.length!=1){
		 			Ext.getCmp("edit").disable();
		 			Ext.getCmp("view").disable();
		 		}else if(rows.length==1){
		 			Ext.getCmp("edit").enable();
		 			Ext.getCmp("view").enable();
		 		}
		 	});
			function add() {
				FHD.openWindow("添加主题",950, window.top.document.body.offsetHeight*0.82,"${ctx}/sys/helponline/addHelpTopic.do?catalogid=${param.catalogid}","yes");
			}
			
			function edit() {
				var rows=list.grid.getSelectionModel().getSelections();
			 	if(rows.length==0){
			 		window.top.Ext.ux.Toast.msg('提示','请先选择要编辑的条目!');
			 		return false;
			 	}else if(rows.length==1){
			 		var id = rows[0].get('id');
					FHD.openWindow("添加主题",950,window.top.document.body.offsetHeight*0.82, "${ctx}/sys/helponline/editHelpTopic.do?id="+id,"yes");
				
			 	}
			}
			
			function del() {
				
				var rows = list.grid.getSelectionModel().getSelections();
				if(rows.length==0){
					window.top.Ext.ux.Toast.msg('警告', '最少选择一条信息，进行删除!');
					return false;
				}else{ 
					Ext.MessageBox.confirm('提示框', '您确定要删除？',function(btn){ 
						if(btn=='yes'){
							if(rows){
								var ids = "";
								for(var i=0;i<rows.length;i++){
									ids += rows[i].get('id');
									if(i!=rows.length-1){
										ids += ",";
									}
								}
								//执行删除操作
								FHD.ajaxReq("${ctx}/sys/helponline/delHelpTopic.do",{ids:ids},reload);
							}
						}
					});
				}
			}
			function view() {
				var rows=list.grid.getSelectionModel().getSelections();
				var id = rows[0].get('id');
				FHD.openWindow("添加主题",950, window.top.document.body.offsetHeight*0.8,"${ctx}/sys/helponline/helpTopicView.do?topicid="+id,"yes");
				
			}
			
			function beforeDeleteCheckCallBack(data){
			//	alert(data)
				window.top.Ext.ux.Toast.msg('提示', '操作成功!');
			}
		});
		function reload(){
			list.store.reload();
		}
	</script>
</head>
<body style="overflow:hidden;">
	<div id="grid"></div>
</body>
</html>