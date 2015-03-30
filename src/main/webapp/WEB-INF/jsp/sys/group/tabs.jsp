<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.group.group"/><spring:message code="fhd.common.tabs"/></title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
</head>
<body>
<script type="text/javascript">
	Ext.onReady(function(){
		 var tabs2 = new Ext.TabPanel({
	        renderTo: Ext.getBody(),
	        activeTab: 0,
	        height:Ext.getBody().getHeight(),
	        width:'auto',
	        defaults:{autoScroll: true},
	        items:[{
	                title: '<spring:message code="fhd.common.details"/>',
	                html:"<iframe src='${ctx}/sys/group/edit.do?id=${param.id}' scrolling='no'  frameBorder=0></iframe>"
	            },{id:'frame2',
	                title: '<spring:message code="fhd.common.next"/><spring:message code="fhd.sys.group.group"/>',
	                html:"<iframe src='${ctx}/sys/group/nextGroup.do?id=${param.id}'  scrolling='no' frameBorder=0></iframe>"
	            },{
	                title: '<spring:message code="fhd.sys.group.group"/><spring:message code="fhd.sys.auth.role.role"/>',
	                html:"<iframe src='${ctx}/sys/group/roleListPage.do?id=${param.id}'  scrolling='no'  frameBorder=0></iframe>"
	            }
	        ]
		});
	});
</script>
</body>
</html>