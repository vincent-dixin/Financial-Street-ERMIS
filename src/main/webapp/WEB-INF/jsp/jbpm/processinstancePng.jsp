<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.jbpm.api.*,java.util.*,org.jbpm.api.model.*" %>
<%@ include file="include.jsp" %>
<% 

	String id = request.getParameter("sid");
	
	RepositoryService repositoryService = processEngine.getRepositoryService();
	ExecutionService executionService = processEngine.getExecutionService();
	ProcessInstance processInstance = executionService.findProcessInstanceById(id);
	ActivityCoordinates ac = null;
	if(processInstance!=null){
		Set<String> activityNames = processInstance.findActiveActivityNames();
		
		ac = repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(),activityNames.iterator().next());
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="overflow-x: hidden;">
<%
	if(null != ac) {
%>
<img src="taskPic.do?sid=<%=id %>" style="position:absolute;left:0px;top:0px;">
<div style="position:absolute;border:2px solid red;left:<%=ac.getX()+6%>px;top:<%=ac.getY()+6%>px;width:<%=ac.getWidth()-16%>px;height:<%=ac.getHeight()-16%>px;"></div>

<%
	}else{
%>
<img src="topdDBPic.do?id=${jbpmHistProcinst.vJbpmDeployment.pdid }" style="position:absolute;left:0px;top:0px;">
<%
	}
%>
</body>
</html>