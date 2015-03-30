package com.fhd.fdc.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fhd.fdc.commons.exception.FHDException;
import com.fhd.fdc.utils.UserContext;

@Controller
public class DispatcherControl {

	
	
	//@Autowired
	private RepositoryService o_repositoryService;
	//@Autowired
	private RuntimeService o_runtimeService;

	@RequestMapping("/login.do")
	public String login(){
		//LicenseControl licenseControl = new LicenseControl();licenseControl = null; 
		return "login2";
	}

	@RequestMapping("/index.do")
	public String index(Model model, HttpServletRequest request) throws FHDException {

		request.getSession().getServletContext().setAttribute("pageSize", "");
		model.addAttribute("user", UserContext.getUser());
		return "index2";
	}

	@ResponseBody
	@RequestMapping("/sessiontimeout.do")
	public void sessionTimeout(HttpServletRequest request,HttpServletResponse response) throws IOException {
		if (request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase(
						"XMLHttpRequest")) { // ajax 超时处理
			response.getWriter().write("<script type=\"text/javascript\">window.location = '" + request.getContextPath() + "/login.do';</script>");
			
//			response.setHeader("x-requested-with", null);
//			response.setStatus(401);
//			response.sendRedirect(request.getContextPath() + "/login.do");
		}else { // http 超时处理
			response.sendRedirect(request.getContextPath() + "/login.do");
		}

	}

	@RequestMapping(value = "/activitiDeploy.do", method = RequestMethod.GET)
	public void g_activitiDeploy() {
	}

	@RequestMapping(value = "/activitiDeploy.do", method = RequestMethod.POST)
	public void p_activitiDeploy(@RequestParam MultipartFile file)
			throws FHDException {
		try {
			ZipInputStream inputStream = new ZipInputStream(
					file.getInputStream());
			o_repositoryService.createDeployment().name("risk")
					.addZipInputStream(inputStream).deploy();
		} catch (Exception e) {
			throw new FHDException(e);
		}
	}

	@RequestMapping(value = "/viewProcess.do", method = RequestMethod.GET)
	public void viewProcess(String id,OutputStream output) throws Exception {
		ProcessInstance processInstance = o_runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) o_repositoryService).getDeployedProcessDefinition(processInstance
	            .getProcessDefinitionId());

		for (ActivityImpl activity : processDefinition.getActivities()) {
	      System.out.println(activity.getProperty("name"));

	    }
		
	    if (processDefinition != null && processDefinition.isGraphicalNotationDefined()) {
	      InputStream definitionImageStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", 
	        o_runtimeService.getActiveActivityIds(processInstance.getId()));
	      IOUtils.write(IOUtils.toByteArray(definitionImageStream), output);
	      
	    }
	    
	    
	}
	
	
	
}
