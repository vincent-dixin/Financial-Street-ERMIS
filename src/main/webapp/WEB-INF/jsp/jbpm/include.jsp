<%@page import="com.fhd.bpm.dao.BusinessWorkFlowDAO"%>
<%@ page import="org.jbpm.api.*
,org.jbpm.api.task.*
,org.springframework.context.ApplicationContext
,org.springframework.web.context.support.WebApplicationContextUtils
" %>
<%
ApplicationContext ctx = (ApplicationContext)session.getAttribute("ctx");
if(null == ctx)
{
	ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
	
	session.setAttribute("ctx",ctx);
}
    
ProcessEngine processEngine = (ProcessEngine)ctx.getBean("processEngine");
BusinessWorkFlowDAO businessWorkFlowDAO = (BusinessWorkFlowDAO)ctx.getBean("businessWorkFlowDAO");
	
	
   
 

%>