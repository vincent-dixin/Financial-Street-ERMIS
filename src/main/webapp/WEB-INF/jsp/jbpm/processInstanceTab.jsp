<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>流程实例列表</title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
</head>

<script type="text/javascript">
	Ext.onReady(function(){
		var tabs2 = new Ext.TabPanel({
	        renderTo: document.body,
	        activeTab: 0,	       
	        height:document.body.offsetHeight,	       
	        defaults:{autoScroll: true},
	        items:[
	   	        {
	                title: '流程实例',
	                html:"<iframe src='${ctx}/jbpm/toprocessInstance.do' scrolling='yes'  frameBorder=0></iframe>"
	            }           	
	            ,{
	                title: '历史实例',
	                html:"<iframe src='${ctx}/jbpm/hisProcessInstance.do'  scrolling='no'  frameBorder=0></iframe>"
	            }
	          
	        ]
	    });
	});
</script>
<body>
</body>
</html>