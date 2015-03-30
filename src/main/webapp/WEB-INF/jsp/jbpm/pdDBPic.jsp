<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.jbpm.api.*,java.io.*" %><%@ include file="include.jsp" %><% 
	RepositoryService repositoryService = processEngine
			.getRepositoryService();
	ProcessDefinition processDefinition = null;
	try
	{
		processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(
						request.getParameter("id")).uniqueResult();
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	//InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processInstanceKey+".png");
	InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getImageResourceName());
	
	response.reset();
	
	OutputStream os = response.getOutputStream();
	IOUtils.write(IOUtils.toByteArray(inputStream),os);
	
	os.close();
	os = null;
	//out.clear(); 
	//out = pageContext.pushBody(); 
%>