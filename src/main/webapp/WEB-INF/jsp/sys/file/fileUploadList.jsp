<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><spring:message code="fhd.sys.file.file"/><spring:message code="fhd.common.manage"/></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody
		Ext.onReady(function(){
			Ext.QuickTips.init();
			heightBody=Ext.getBody().getHeight();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fileupdate.update'/>",handler:saveFileUpload,iconCls:'icon-add'},
					{text: "<spring:message code='fileupdate.delete'/>",id:'del',handler:removeFileUpload,iconCls:'icon-del',disabled:true},
					{text: "<spring:message code='fileupdate.query'/>",id:'adquery',handler:showAndHide,iconCls:'icon-zoom'},
					{text: "<spring:message code='fileupdate.testupdate'/>",handler:fileUploadTest,iconCls:'icon-edit'}
				]
			});
	
			mv_grid = new FHD.ext.Grid('datalist',{riskId:5},
				[ 
	   				{header: "id", dataIndex: 'id', width: 0},
	   				{header: "<spring:message code='fileupdate.oldFileName'/>", dataIndex: 'oldFileName', sortable: true, width: 60},
					{header: "<spring:message code='fileupdate.newFileName'/>", dataIndex: 'newFileName', sortable: true, width: 60},
					{header: "<spring:message code='fileupdate.username'/>", dataIndex: 'userName', sortable: true, width: 60},
					{header: "<spring:message code='fileupdate.time'/>", dataIndex: 'uploadTime', sortable: true, width: 60},
					{header: "<spring:message code='fileupdate.downTimes'/>", dataIndex: 'countNum', sortable: true, width: 60},
					{header: "<spring:message code='fileupdate.operate'/>", dataIndex: 'id', width: 60, renderer:function(val){
						return "<a href='${ctx}/sys/file/download.do?id="+val +"'><spring:message code='fhd.sys.file.download'/></a>"
					}}
				],
				null,false,'100%',heightBody,'${ctx}/sys/file/queryfileUploadList.do?id=${id}'
				,false,true,
				toolbar
			);
			 mv_grid.grid.getSelectionModel().on('selectionchange',function(sm,rowIndex,record){
			    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			        if(rows.length==0){
			    		 Ext.getCmp("del").disable();
			        }else{
			             Ext.getCmp("del").enable();
			        }
			    });
		});

		function saveFileUpload(){
			var ret = FHD.openWindow( "<spring:message code='fileupdate.add'/>", 600,200, "${ctx}/sys/file/fileUploadAdd.do",'no');
		}

		function fileUploadTest(){
			var ret = FHD.openWindow( "<spring:message code='fileupdate.testupdate'/>", 800,196, "${ctx}/sys/file/fileUploadTest.do",'no');
		}
		
		function removeFileUpload(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg( "<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
		
			Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.makeSureDelete'/>",function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++){
						ids+=rows[i].get('id')+',';
					}
					FHD.ajaxReq('${ctx}/sys/file/fileUploadDel.do',{ids:ids},function(data){
						if(data!='true'){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
						}else{
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
						}
						mv_grid.grid.store.reload();
					});
				}else{
					return;
				}
			});
		}
	
		//打开或关闭高级查询
		function showAndHide(){
			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
			if(divShows == 'none'){
				document.getElementById("rmRiskTaxisDivId").style.display='';
				mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.hide'/>");
				var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
				var height=heightBody-divHeight;
				mv_grid.grid.setHeight(height);			
			}else{
				document.getElementById("rmRiskTaxisDivId").style.display='none';
				mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.query'/>");
				mv_grid.grid.setHeight(heightBody);
				}
		}
		//查询
		function queryRole(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		oldFileName: document.getElementById("oldFileName").value,
	    		newFileName: document.getElementById("newFileName").value,
	    		username:document.getElementById("username").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}

		function keypup(){
			if(event.keyCode==13){
				queryRole();
				return;
			}
		}
		 Ext.EventManager.onWindowResize(function(width ,height){
	   			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
	   			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
	   			if(divShows=='none'){
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height);
	   			}else{
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height1);
	   			}
	 });
	</script>
</head>
<body style='overflow:hidden'>
<div id="rmRiskTaxisDivId" style='display:none'>

	<form id="fileUploadForm" name="fileUploadForm" method="post" action="" onkeypress="keypup();">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
		</table>
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0"
			class="fhd_query_table" style="display:block;">
			<tr>                                               
				<th><spring:message code="fhd.sys.file.oldFileName"/>：</th>
				<td><input name="oldFileName" /></td>
				<th><spring:message code="fhd.sys.file.newFileName"/>：</th>
				<td><input id="newFileName" name="newFileName"/></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
				<td><input id="username" name="username"/></td>
				<th>&nbsp;</th>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td align="center" colspan="4">
					<input type="button" id="submits" onclick="queryRole();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
				</td>
			</tr>
		</table>
		
	</form>
</div>

<div id="datalist">    </div>



<c:if test="${not empty success }">
<script>
<c:choose>
<c:when test="${success eq '1'}">
	alert("<spring:message code="fhd.common.operateSuccess" />");
</c:when>
<c:when test="${success eq '0'}">
	alert("<spring:message code="fhd.common.operateFailure" />");
</c:when>
</c:choose>
</script>
</c:if>
</body>
</html>