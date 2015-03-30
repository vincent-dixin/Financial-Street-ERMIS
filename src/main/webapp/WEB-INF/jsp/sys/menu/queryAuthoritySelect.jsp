<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="function"/><spring:message code="list"/></title>
	<link rel="stylesheet" type="text/css" href="../css/style.css"/>
	<script type="text/javascript">
		var store;
		var grid;
		Ext.onReady(function(){
		    Ext.QuickTips.init();
		    
			var sm=new Ext.grid.CheckboxSelectionModel({singleSelect:true});
			var cm = new Ext.grid.ColumnModel([ 
		  		new Ext.grid.RowNumberer({
			  		width : 25,
			  		renderer:function(value,metadata,record,rowIndex){
			   			return record.store.lastOptions.params.start + 1 + rowIndex;
			  		}
			 	}),
			  	sm,
		   		{id:'authorityCode',header: "功能编码", dataIndex: 'authorityCode', sortable: true, width: 200,
		  			renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
					 }
				  },
		   		{id:'authorityName',header: "功能名称", dataIndex: 'authorityName', sortable: true, width: 200,
			   		renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
					  return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
			   		}
		   		},
		   		{id:'parentAuthority',header: "功能组名称", dataIndex: 'parentAuthority', sortable: true,width: 250,
		   			renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
					 }}
		   	]); 
		   	
		   	var proxy = new Ext.data.HttpProxy({
				method:'POST',
				url:'${ctx}/sys/auth/functionList.do'
			});
			var reader = new Ext.data.JsonReader({
				idProperty:'id',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['id','authorityCode','authorityName','parentAuthority']
			});
			store = new Ext.data.Store({
			 	autoDestroy: true,
			 	//baseParams:{id:$("#targetId").attr("value")},
			   	proxy: proxy,
			    reader: reader,
			    sortInfo:{field:'parentAuthority', direction:'ASC'}
			});
			var height = Ext.getBody().getHeight()-Ext.get("formShowId").getHeight();
			grid = new Ext.grid.GridPanel({
				sm: sm,
		        store: store,
		        cm: cm, 
		        renderTo: 'authority-grid',
		        height:height, 
		        autoWidth:true,
		         autoScroll:true,
				stripeRows:true,
				viewConfig:{forceFit:true},
				tbar: new Ext.Toolbar({
			        items: [{
			        	id:'chaxue',
			        	text:'查询',
				    	iconCls:'icon-zoom',
				    	handler:showAndHide
			        },
			        '-',
			        {   text:"保存",
			        	icon: '${ctx}/images/icons/save.gif',    //'${ctx}/images/icons/select.gif',
				        handler:saveButton
					 },
					 '-',
				     {text:"取消",
					  icon: '${ctx}/images/icons/unchecked.gif',
					  handler:close}]
			    }),
				bbar: new Ext.PagingToolbar({
		           	pageSize: 15,
		           	store: store,
		           	displayInfo: true,
		           	displayMsg: '显示{0} - {1}条 共{2}条记录', 
		           	emptyMsg: "无记录显示",
		           	plugins: new Ext.ux.ProgressBarPager()
		         /*
		        	items:[
							'-',
							{
							    icon: '${ctx}/images/icons/select.gif',
							   	tooltip: '选择',
								handler: function(btn, pressed){
								 	var rows=grid.getSelectionModel().getSelections();
								 	var code = rows[0].get("id");
								 	var name = rows[0].get("authorityName");
								 	if('selectFunction' in parentWindow()){
				         				parentWindow().selectFunction(code,name);
				         			}
								 	closeWindow();
								}
							},
							'-',
							{
								icon:'${ctx}/images/icons/deselect.gif',
								tooltip:'取消选择值',
								handler:function(){
									if('deSelectFunction' in parentWindow()){
				         				parentWindow().deSelectFunction();
				         			}
									closeWindow();
								}
							}
						]
						*/
				
				})
			});
			store.load({
		    	params:{
		    		start:0,
		    		limit:15
		    	}
			});
			
		});
		
		function saveButton(){
	            var rows=grid.getSelectionModel().getSelections();
	            if(rows.length==0){
	                Ext.Msg.alert("提示","请选择一条记录！");
	            }else{
				 	var code = rows[0].get("id");
				 	var name = rows[0].get("authorityName");
				 	if('selectFunction' in parentWindow()){
         				parentWindow().selectFunction(code,name);
         			}
				 	closeWindow();
			}
		}
	    function close(){
		    if('deSelectFunction' in parentWindow()){
				parentWindow().deSelectFunction();
			}
		closeWindow();
		}
		

		//查询
		function queryAuthority(){
			// 数据重新加载
		    store.baseParams = {
		    	authorityCode: document.getElementById("authorityCode").value,
	    		authorityName: document.getElementById("authorityName").value
			};
		    store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:15			    		
		    	}
			});	
		}
	
		function keypup(){
			if(event.keyCode==13){
				queryAuthority();
				return;
			}
		}
		function showAndHide(){
			var formShowId=jQuery("#formShowId");
			if(formShowId.is(":visible")){
				formShowId.hide();
				grid.getTopToolbar().get("chaxue").setText("查询");
			}else{
				formShowId.show();
				grid.getTopToolbar().get("chaxue").setText("隐藏");
			}
			var height = Ext.getBody().getHeight()-Ext.get("formShowId").getHeight();
			grid.setHeight(height);
	    }
		Ext.EventManager.onWindowResize(function(width ,height){
   			var height1 = height-Ext.get("formShowId").getHeight();
   			if(showTable=='none'){
   				grid.setWidth(width);
   			    grid.setHeight(height);
   			}else{
   			    grid.setWidth(width);
			    grid.setHeight(height1);
   	   			}
			});
		
	</script>
</head>
<body>
  <form id="sysAuthorityForm" name="sysAuthorityForm" action="" method="post" onkeypress="keypup();" >
		<input type="hidden" id="id" name="id" value=""/>
		 <div id="formShowId" style="background:#f9f9f9; display:none;"  >
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" >
				<tr><td class="fhd_query_title" >&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide(this)" width="15" height="15" /><spring:message code="querycondition"></spring:message></td></tr>
				</table>
				<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display: block;">
				 <tr>  
				<th>功能编码：</th>
				<td><input type="text" name="authorityCode" id="authorityCode" value="${param.authorityCode}" /></td>                                            
				<th>功能名称：</th>
				<td><input type="text" name="authorityName" id="authorityName" value="${param.authorityName}" /></td>
				</tr>
				<tr>
				<td align="center" colspan="4">
				<input type="button" value="<spring:message code="search_btn" />" class="fhd_btn" onclick="queryAuthority();" />
				</td>
				  </tr>
			   </table>
			</div>
			</form>
		  <div id='authority-grid'></div>
</body>
</html>
