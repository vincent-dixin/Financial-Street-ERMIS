<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <style type="text/css">
			.windowIcon{background:url(../scripts/ext-3.4.0/resources/images/default/tree/leaf.gif) no-repeat !important;background-position:bottom}
		</style>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.authority.authority"/><spring:message code="fhd.common.list"/></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody;
		Ext.onReady(function(){
			heightBody=Ext.getBody().getHeight();
			Ext.QuickTips.init();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fhd.common.addFun'/>",handler:saveAuthority,iconCls:'icon-add'},'-',
					{text: "<spring:message code='fhd.common.modify'/>",id:'edit',handler:updateAuthority,iconCls:'icon-edit', disabled:true},'-',
					{text: "<spring:message code='fhd.common.delete'/>",id:'del',handler:removeAuthority,iconCls:'icon-del',disabled:true},'-',
					//{text: '导出',handler:exportData,iconCls:'export'},
					{text: "<spring:message code='fhd.common.query'/>",handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
			
			mv_grid = new FHD.ext.Grid('authlist',{riskId:5},
				[ 
				   {header: "id", dataIndex: 'id', width: 0},
				   {header: "<spring:message code='fhd.sys.auth.authority.authorityCode'/>", dataIndex: 'authCode', sortable: true, width: 60,renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
					  }},
				   {header: "<spring:message code='fhd.sys.auth.authority.authorityName'/>", dataIndex: 'authName', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.common.sn'/>", dataIndex: 'authSn', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.common.preName'/>", dataIndex: 'authpname', sortable: true, width: 60,renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
					  }}
				   
				   
				],
				null,false,'100%',heightBody,'${ctx}/sys/auth/authority/queryList.do?id=${id}'
				,false,true,
				toolbar
			);
			mv_grid.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==1){
		    		 Ext.getCmp("edit").enable();
		    	}else{
		    		Ext.getCmp("edit").disable();
		    	}
		        Ext.getCmp("del").enable();
			});
			mv_grid.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==0){
		    		 Ext.getCmp("edit").disable();
		    		 Ext.getCmp("del").disable();
		    	}else if(rows.length!=1){
		    		Ext.getCmp("edit").disable();
		    	}else if(rows.length==1){
		    		Ext.getCmp("edit").enable();
		    	}
			});
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
		});

		function saveAuthority(){
			FHD.openWindow("<spring:message code='fhd.common.addFun'/>",480,228, "${ctx}/sys/auth/authority/add.do?operation=page&id=${param.id}",'no');
			/*
			var ret = window.showModalDialog("${ctx}/sys/auth/authority/add.do?id=${param.id}",window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
			if(ret == "refresh"){
			     window.location.reload();
			}
			parent.parent.selectNodeReload();
			*/
		}

		function updateAuthority(){
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			var ret = FHD.openWindow("<spring:message code='fhd.common.editFun'/>", 515,274, "${ctx}/sys/auth/authority/update.do?id="+id,'no');
			
			
		}

		function removeAuthority(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
			
			Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.makeSureDelete'/>",function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++)
						ids+=rows[i].get('id')+',';
						
					FHD.ajaxReq('${ctx}/sys/auth/authority/delete.do',{ids:ids},function(data){
						if(data!='true')
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
						else 
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
							mv_grid.grid.store.reload();
		         			parent.parent.selectNodeReload();
		         			Ext.getCmp("edit").disable();
				    		Ext.getCmp("del").disable();
					});
				}else{
					return;
				}
			});
		}

		
		//查询
		function queryAuth(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		authorityCode: document.getElementById("authorityCode").value,
	    		authorityName: document.getElementById("authorityName").value,
	    		parentId:document.getElementById("parentId").value
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
				queryAuth();
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
	<div id="rmRiskTaxisDivId" style="background:#f9f9f9; display: none;">
		<form id="sysAuthorityForm" name="sysAuthorityForm" method="post" action="" onkeypress="keypup();">
			<input type="hidden" id="id" name="id" value="${param.id}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide(this)" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
				<tr>  
					<th><spring:message code="fhd.sys.auth.authority.authorityCode"/>：</th>
					<td><input type="text" name="authorityCode" id="authorityCode" value="${param.authorityCode}"/></td>                                             
					<th><spring:message code="fhd.sys.auth.authority.authorityName"/>：</th>
					<td><input type="text" name="authorityName" id="authorityName" value="${param.authorityName}"/></td>
				</tr>
				<tr>  
					<th><spring:message code="fhd.sys.auth.authority.parentAuthorityName"/>：</th>
					<td>
						<fhd:authoritySelect path="parentAuthority.id" name="parentId" id="parentId" value="${param.id}"/>
					</td>                                             
					<th>&nbsp;</th>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" onclick="queryAuth();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div id="authlist"></div>
</body>
</html>