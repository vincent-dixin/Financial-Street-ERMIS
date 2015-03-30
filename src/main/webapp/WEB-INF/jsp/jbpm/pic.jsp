<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.jbpm.api.*,java.io.*"%><%@ include file="include.jsp" %><% 
	RepositoryService repositoryService = processEngine
			.getRepositoryService();
	ExecutionService executionService = processEngine
			.getExecutionService();
	String id = request.getParameter("sid");
	System.out.print(id);
	ProcessInstance processInstance = executionService
			.findProcessInstanceById(id);
	System.out.print(processInstance);
	String a = processInstance.getName();
	String bb = processInstance.getKey();
	
	String processInstanceKey = id.split("\\p{Punct}")[0];
	String processDefinitionId = processInstance
			.getProcessDefinitionId();
	ProcessDefinition processDefinition = repositoryService
			.createProcessDefinitionQuery().processDefinitionId(
					processDefinitionId).uniqueResult();
	//InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processInstanceKey+".png");
	InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getImageResourceName());

	response.reset();
	
	OutputStream os = response.getOutputStream();
	IOUtils.write(IOUtils.toByteArray(inputStream),os);
	
	os.close();
	os = null;
%>