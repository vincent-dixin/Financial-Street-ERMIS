<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.auth.user.user"/><spring:message code="fhd.sys.auth.authority.assignAuthority"/></title>
<base target="_self">
<script type="text/javascript">
	function selectCheckbox(idValue){
		//alert(idValue);
		//清空
		var nodes = tree.getChecked();
		for(var i=0;i<nodes.length;i++){
			var node = tree.getNodeById(nodes[i].attributes.id);
			if(node!=undefined){
				node.ui.toggleCheck(false);   
				node.attributes.checked = false;
			}	
		}
	    //ajax查询当前用户的权限
		$.ajax({
			type: "GET",
			url: "${ctx}/sys/auth/queryAuthorityByUserAjax.do",
			data: "uid="+idValue,
			success: function(msg){
				if(msg != "NO"){
					var ids = msg.substring(0,msg.length-1);
					var ida = ids.split(',');
					for(var i=0;i<ida.length;i++){
						checkTreeNode(ida[i]);
					}
				}
			}
		});
	}
	
	//选中结点
	function checkTreeNode(nodeId){
		var node = tree.getNodeById(nodeId);
		//alert(node);
		if(node!=undefined && node.firstChild==undefined){
			node.ui.toggleCheck(true);   
			node.attributes.checked = true;
		}
	}
	
	
		//保存权限
	function assignAuthority(){
		var id = document.getElementById("id").value;
		//alert(id);
		var nodes = tree.getChecked();
		var selectIds = '';
		for(var i=0;i<nodes.length;i++){
			//if(nodes[i].isLeaf()){
				selectIds += nodes[i].attributes.id + ",";
			//}
		}
		//alert(selectIds);
		if(selectIds == null || selectIds == ""){
			alert("<spring:message code="fhd.common.chooseAuthority"/>");
			return false;
		}else{
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/group/saveAuthority.do",
				data: "userid="+id+"&selectIds="+selectIds,
				success: function(msg){
					if("true"==msg){
						window.top.Ext.MessageBox.alert('提示', "<spring:message code="fhd.common.operateSuccess"/>");
						closeWindow();
					}else{
						window.top.Ext.MessageBox.alert('提示',"<spring:message code="fhd.common.operateFailure"/>");
						//closeWindow();
					}
				}
			});
		}
		window.close();
	}
</script>
</head>
<body>
<form id="groupForm" name="groupForm" action="" method="post">
	<input type="hidden" name="id" id="id" value="${param.id}"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="display:block;">
		<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="hideTable(this)" width="15" height="15" /><spring:message code="fhd.sys.auth.user.user"/><spring:message code="fhd.sys.auth.authority.assignAuthority"/></td></tr>
	</table>
	<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
		<tr>  
			<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
			<td><input type="text" name="username" id="username" value="${sysUserForm.username}" readonly="readonly"/></td>
			<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
			<td><input type="text" name="realname" id="realname" value="${sysUserForm.realname}" readonly="readonly"/></td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#f4faff" style="border-collapse: collapse;width:100%;border:1px solid #d0cfcd;">
		<tr>
			<td class="left" style="width:100px;text-align:center"><spring:message code="fhd.sys.auth.authority.authority"/><spring:message code="fhd.common.list"/></td>
			<td class="right">
				<div id="authority-tree"></div>
			</td>
		</tr>
		<tr align="center">
			<td class="left">
				&nbsp;
			</td>
			<td class="right">
				<input type="button" onclick="javascript:assignAuthority();" name="save" id="save" class="fhd_btn" value="<spring:message code="fhd.common.submit" />" />
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		var tree;
		var mytreeload;
		mytreeload = new Ext.tree.TreeLoader({
					//preloadChildren: true,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					},
					dataUrl : '${ctx}/sys/auth/authority/loadStaticAuthorityTree.do'
				});
				
		Ext.onReady(function() {
			tree = new Ext.tree.TreePanel({
				el : 'authority-tree',
				autoScroll : true,
				animate : true,
				height:350,
				//autoHeight : true,
				//width : 590,
				autoWidth:true,
				//enableDD:true,
				authoScroll : true,
				containerScroll : true,
				hlDrop : false,
				//checkNode:false,
				checkModel : 'cascade',
				//onlyLeafCheckable:true,
				containerScroll : true,
				rootVisible : true,
				//frame:true,
				loader : mytreeload

			});
			
			mytreeload.on("load", function(val, node, response ){
				selectCheckbox('${param.id}');
				
			});

			var root = new Ext.tree.AsyncTreeNode( {
				id : '${orgRoot.id}',
				text : '${orgRoot.authorityName}',
				draggable : false,
				expanded : true,
				iconCls : 'authority-tree-icon',
				cls : 'root'
			});
			tree.setRootNode(root);
			tree.render();

			
		});
	</script>
</form>
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