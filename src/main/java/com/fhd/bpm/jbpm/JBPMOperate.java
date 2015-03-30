/**
 * JBPMOperate.java
 * com.fdc.jbpm.fxsb
 *
 * Function：  
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-1-11 		史永亮
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
 */

package com.fhd.bpm.jbpm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.history.HistoryTaskQuery;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:JBPMOperate Function:  JBPM操作接口 
 * Reason: 
 * 
 * @author 史永亮
 * @version
 * @since Ver 1.1
 * @Date 2011-1-11 上午09:35:05
 * 
 * @see 
 */
@Component
public class JBPMOperate {

	/**
	 * 流程引擎对象 此对象在resources/spring/spring-config-jbpm.xml文件中配置
	 */
	@Autowired
	protected ProcessEngine processEngine;	
	
	/**
	 * （??? 似乎没有用，删除吗）
	 * <pre>
	 * getRepositoryService:(获取流程资源服务的接口)
	 * 	
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public RepositoryService getRepositoryService() {
		return processEngine.getRepositoryService();
	}

	/**
	 * 
	 * <pre>
	 * getExecutionService:(获取流程执行服务的接口)
	 * 	
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public ExecutionService getExecutionService() {
		return processEngine.getExecutionService();
	}

	/**
	 * 
	 * <pre>
	 * getTaskService:(获取任务服务的接口)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public TaskService getTaskService() {
		return processEngine.getTaskService();
	}

	/**
	 * 
	 * <pre>
	 * getHistoryService:(获取流程历史服务的接口)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public HistoryService getHistoryService() {
		return processEngine.getHistoryService();
	}

	/**
	 * 
	 * <pre>
	 * getIdentityService:(获取身份认证服务的接口)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public IdentityService getIdentityService() {
		return processEngine.getIdentityService();
	}

	/**
	 * 
	 * <pre>
	 * getManagementService:(获取流程管理控制服务的接口)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @return
	 * @since fhd Ver 1.1
	 */
	public ManagementService getManagementService() {
		return processEngine.getManagementService();
	}

	/**
	 * 
	 * <pre>
	 * startPocessInstance:(开启流程实例)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param processDefinitionKey
	 *            流程部署id
	 * @param variables
	 *            传入流程变量的Map集合
	 * @return 刚开启流程实例的ID
	 * @since fhd Ver 1.1
	 */
	public String startPocessInstance(String processDefinitionKey,Map<String, ?> variables) {
		String processId = null;

		//根据流程定义key和参数，开启流程实例且返回该实例id。
		processId = this.getExecutionService().startProcessInstanceByKey(processDefinitionKey, variables).getId();

		return processId;
	}

	/**
	 * 
	 * <pre>
	 * singelPocessInstance:(触发流程实例)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param processInstanceId
	 *            流程实例id
	 * @param variables
	 *            参数
	 * @return
	 * @since fhd Ver 1.1
	 */
	public String singelPocessInstance(String processInstanceId,Map<String, ?> variables) {
		String signalName = (String)variables.get("signalName");
		//根据流程实例id和参数，触发工作流
		
		if(StringUtils.isBlank(signalName)){
			this.getExecutionService().signalExecutionById(processInstanceId,variables);
		} else {
			this.getExecutionService().signalExecutionById(processInstanceId,signalName,variables);
		}
		
		
		return processInstanceId;
	}

	/**
	 * 
	 * <pre>
	 * isRiskComplete:(判断风险流程是否结束)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param businessId
	 *            业务id
	 * @return 该业务对象是否流转结束
	 * @since fhd Ver 1.1
	 */
	public boolean isComplete(String processInstanceId) {
		
		//根据流程实例id来查找执行
		Execution execution = this.getExecutionService().findProcessInstanceById(processInstanceId);
		
		//如果没找到，就到历史中去找
		if (execution == null) {
			List<HistoryProcessInstance> hpiList = this.getHistoryService().createHistoryProcessInstanceQuery().list();
			
			for (HistoryProcessInstance hpi : hpiList) {
				if (hpi.getProcessInstanceId().equals(processInstanceId)) {
					return true;
				}
			}
			return false;
		}
		return execution.isEnded();
	}

	/**
	 * 
	 * <pre>
	 * getLastAssignee:(获取上一结点参与人)
	 * 	
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param processInstanceId
	 *            流程实例id
	 * @return 流程参与人的员工id
	 * @since fhd Ver 1.1
	 */
	public String getLastAssignee(String processInstanceId) {
		String pexecutionid = processInstanceId.split("\\.")[0]+"."+processInstanceId.split("\\.")[1];
		//根据实例id来查找历史任务集合
		List<HistoryTask> l = this.getHistoryService().createHistoryTaskQuery()	.executionId(pexecutionid).orderDesc(HistoryTaskQuery.PROPERTY_ID).list();

		HistoryTask ht = l.get(1);
		return ht.getAssignee();
	}


	/**
	 * 
	 * <pre>
	 * findParentExcetionId:(返回当前流程父流程exceutionId)
	 * 如果没有父流程，则返回当前流程exceutionId
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param executionid
	 * @return
	 * @since fhd Ver 1.1
	 */
	public String findParentExcetionId(String executionid) {

		//找该执行id的父执行
		Execution parentExcetion = ((ExecutionImpl) this.processEngine.getExecutionService().findExecutionById(executionid)).getSuperProcessExecution();
		// 如果有父执行就返回该父执行的id
		if (parentExcetion != null) {

			return parentExcetion.getParent().getId();
		}
		return executionid;

	}

	/**
	 * 
	 * <pre>
	 * isSubPorcess:(返回当前流程是否是子流程)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param executionid
	 * @return
	 * @since fhd Ver 1.1
	 */
	public boolean isSubPorcess(String executionid) {
		//找该执行id的父执行
		Execution parentExcetion = ((ExecutionImpl) this.processEngine.getExecutionService().findExecutionById(executionid)).getSuperProcessExecution();
		//如果有父执行，则该流程就是子的
		if (parentExcetion != null) {
			return true;
		}
		return false;

	}


	/**
	 * 
	 * <pre>
	 * deletePorcessInstance:(根据id删除流程实例)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param id
	 * @since fhd Ver 1.1
	 */
	@Transactional
	public void deletePorcessInstance(String id) {
		ExecutionImpl exceution = ((ExecutionImpl) this.processEngine.getExecutionService().findExecutionById(id));
		// 有父流程的，也就是子流程的，直接结束
		if(exceution!=null){
			if (exceution.getSuperProcessExecution() != null) {
				int intx = 0;
				Map<String, Object> variables2 = new HashMap<String, Object>();
				variables2.put("spuserid", "");
				variables2.put("operate", "y");
				if (exceution.getActivityName().equals("")){
					intx = 1;
				}else{
					intx = 2;
				}
				for (int i = 0; i < intx; i++) {
					this.processEngine.getExecutionService().signalExecutionById(id, variables2);
				}
			}else{//没有的，也就是单一流程实例的，直接删除
				this.processEngine.getExecutionService().deleteProcessInstance(id);
			}
		}

	}

	/**
	 * 
	 * <pre>
	 * findSubPorcessInstanceIds:(根据parentId查出该流程下的所有子流程id，除了参数id的)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param parentId
	 *            父流程id
	 * @param id
	 *            要排除的id
	 * @return
	 * @since fhd Ver 1.1
	 */
	public List<String> findSubPorcessInstanceIds(String parentId, String id) {

		//根据流程实例id查询到该流程实例对象
		ExecutionImpl exceution = ((ExecutionImpl) this.processEngine
				.getExecutionService().findExecutionById(parentId));
		
		//获取子执行容器
		Collection<ExecutionImpl> subList = exceution.getExecutions();
		//排除子执行id等于参数id的对象
		List<String> re = new ArrayList<String>();
		for (ExecutionImpl ei : subList) {
			if (!ei.getId().equals(id)) {
				re.add(ei.getSubProcessInstance().getId());

			}
		}
		return re;

	}

	/**
	 * 
	 * <pre>
	 * findSubPorcessInstance:(根据parentId查出该流程下的所有子流程)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param parentId
	 *            父流程id
	 * @return
	 * @since fhd Ver 1.1
	 */
	public Collection<ExecutionImpl> findSubPorcessInstance(String parentId) {
		//根据流程实例id查询到该流程实例对象
		ExecutionImpl exceution = ((ExecutionImpl) this.processEngine
				.getExecutionService().findExecutionById(parentId));
		//获取子执行容器
		Collection<ExecutionImpl> subList = exceution.getExecutions();
		Collection<ExecutionImpl> re = new ArrayList<ExecutionImpl>();
		//全部加入到返回结果中
		for (ExecutionImpl ei : subList) {
			if(ei.getSubProcessInstance() != null)
			{
				re.add(ei.getSubProcessInstance());
			}
		}

		return re;

	}
	/**
	 * 
	 * deploy:发布流程
	 * 
	 * @author 杨鹏
	 * @param inputStream
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public void deploy(InputStream inputStream) throws Exception {
		ZipInputStream zis = new ZipInputStream(inputStream);
		this.processEngine.getRepositoryService().createDeployment().addResourcesFromZipInputStream(zis).deploy();
	}


	/**
	 * 
	 * <pre>
	 * deletePD:(根据流程定义id删除该流程定义)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	public void deletePorcessDefinition(String id) {
		//根据id删除该流程定义，并删除该定义下的所有流程实例和任务等。级联删除
		this.processEngine.getRepositoryService().deleteDeploymentCascade(id);
		
	}

	/**
	 * 
	 * <pre>
	 * updateAssignee:(把id为taskId的任务参与人改变为emp2)
	 * </pre>
	 * 
	 * @author 史永亮
	 * @param taskId
	 * @param emp2
	 * @since  fhd　Ver 1.1
	 */
	public void updateAssignee(String taskId, String emp2) {
		
		//改变任务结点id为taskId的任务的参与人为emp2
		this.processEngine.getTaskService().assignTask(taskId, emp2);
		
	}
	
	/**
	 * 
	 * <pre>
	 * saveVariables:(根据流程id保存流程参数)
	 * </pre>
	 * ·	
	 * @author 杨光
	 * @param executionid
	 * @param variables
	 * @return 
	 * @since fhd Ver 1.1
	 */
	@Transactional
	public void saveVariables(String executionid,Map<String, ?> variables) {

		this.processEngine.getExecutionService().createVariables(executionid, variables,true);

	}
	
	public String getVariable(String executionid,String name) {
		if(this.processEngine.getExecutionService().getVariable(executionid,name)!=null){
			return this.processEngine.getExecutionService().getVariable(executionid,name).toString();
		}else{
			 return null;
		}

	}
	
	public Object getVariable1(String executionid,String name) {

		return this.processEngine.getExecutionService().getVariable(executionid,name);

	}


}
