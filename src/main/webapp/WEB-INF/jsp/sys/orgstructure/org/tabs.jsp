<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.org.organization"/><spring:message code="fhd.common.tabs"/></title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
</head>
<body style="overflow:hidden">

	<script type="text/javascript">
	    var height=Ext.getBody().getHeight();
		Ext.onReady(function(){
			 var tabs2 = new Ext.TabPanel({
				    id:'tabs',
			        renderTo: document.body,
			        activeTab: 0,
			        //width:document.body.getHeight(),
			        height:height,
			        plain:true, 
			        defaults:{autoScroll: false,
				        width:'auto'},
			        items:[{
			                title: '<spring:message code="fhd.sys.orgstructure.org.orgInfo"/>',
			                html:"<iframe src='${ctx}/sys/orgstructure/org/edit.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },{id:'frame2',
				            layout:'fit',
			                title: '<spring:message code="fhd.sys.orgstructure.org.nextorg"/>',
			                html:"<iframe id='iframe1' src='${ctx}/sys/orgstructure/org/query.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            },{
			                title: '<spring:message code="fhd.sys.orgstructure.org.nextposi"/>',
			                html:"<iframe id='iframe2' src='${ctx}/sys/orgstructure/posi/query.do?id=${id}'  scrolling='no'  frameBorder=0></iframe>"
			            },{
			                title: '<spring:message code="fhd.sys.orgstructure.org.nextemp"/>',
			                html:"<iframe  src='${ctx}/sys/orgstructure/emp/queryNextEmp.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            }
			        ]
			 });
			 
			 Ext.EventManager.onWindowResize(function(width,height){
				 Ext.getCmp("tabs").setWidth(width);
				 Ext.getCmp("tabs").setHeight(height);
				
			 });
		});
		
	</script>
</body>
</html>