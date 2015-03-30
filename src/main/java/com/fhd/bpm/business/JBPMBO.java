package com.fhd.bpm.business;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.Execution;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.dao.BusinessWorkFlowDAO;
import com.fhd.bpm.dao.DeploymentImplDAO;
import com.fhd.bpm.dao.ExamineApprovalOpinionsDAO;
import com.fhd.bpm.dao.ExamineApproveIdeaDAO;
import com.fhd.bpm.dao.JbpmHistActinstDAO;
import com.fhd.bpm.dao.JbpmHistProcinstDAO;
import com.fhd.bpm.dao.ProcessDefinitionDeployDAO;
import com.fhd.bpm.dao.VJbpmDeploymentDAO;
import com.fhd.bpm.entity.BusinessWorkFlow;
import com.fhd.bpm.entity.ExamineApproveIdea;
import com.fhd.bpm.entity.JbpmHistActinst;
import com.fhd.bpm.entity.JbpmHistProcinst;
import com.fhd.bpm.entity.ProcessDefinitionDeploy;
import com.fhd.bpm.entity.view.VJbpmDeployment;
import com.fhd.bpm.entity.view.VJbpmHistTask;
import com.fhd.bpm.jbpm.JBPMConstant;
import com.fhd.bpm.jbpm.JBPMContext;
import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.file.FileUploadEntity;
/**
 * 
 * ClassName:JBPMBO
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-4		下午4:23:47
 *
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class JBPMBO {

	//jbpm操作对象
	@Autowired
	private JBPMOperate o_jBPMOperate;
	//审批意见Dao
	@Autowired
	private ExamineApproveIdeaDAO o_examineApproveIdeaDAO;
	//审批意见DAO
	@Autowired
	private ExamineApprovalOpinionsDAO o_examineApprovalOpinionsDAO;
	//员工Dao
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	//工作流业务关系表DAO
	@Autowired
	private BusinessWorkFlowDAO o_businessWorkFlowDAO;
	@Autowired
	private VJbpmDeploymentDAO o_vJbpmDeploymentDAO;
	//流程定义部署DAO
	@Autowired
	private ProcessDefinitionDeployDAO o_processDefinitionDeployDAO;
	//历史流程实例DAO
	@Autowired
	private JbpmHistProcinstDAO o_jbpmHistProcinstDAO;
	@Autowired
	private JbpmHistActinstDAO o_jbpmHistActinstDAO;
	@Autowired
	private DeploymentImplDAO o_deploymentImplDAO;
	@Autowired
	private FileUploadBO o_fileUploadBO;
	@Autowired
	private HistoryProcessInstanceBO o_historyProcessInstanceBO;
	/**
	 * startProcessInstance:开启流程
	 * @author 杨鹏
	 * @param entityType 流程名称
	 * @param variables 流程变量
	 */
	@Transactional
	public String startProcessInstance(String entityType,Map<String,Object> variables) {
		// 开启工作流流程并得到流程实例id
		String processInstanceId = o_jBPMOperate.startPocessInstance(entityType, variables);
		
		// 处理业务表
		String id = Identities.uuid();
		BusinessWorkFlow businessWorkFlow = new BusinessWorkFlow();
		businessWorkFlow.setId(id);
		businessWorkFlow.setBusinessId(String.valueOf(variables.get("id")));
		businessWorkFlow.setBusinessName(String.valueOf(variables.get("name")));
		businessWorkFlow.setProcessInstanceId(processInstanceId);
		businessWorkFlow.setBusinessObjectType(entityType);
		businessWorkFlow.setState("1");// 开始正常状态
		businessWorkFlow.setXbState("1");// 协办正常状态
		if(null != UserContext.getUser()) {
			variables.put("startUserId", UserContext.getUser().getEmpid());
			businessWorkFlow.setCreateBy(UserContext.getUser().getEmpid());
		} else {
			variables.put("startUserId", Contents.SYSTEM);
			businessWorkFlow.setCreateBy(Contents.SYSTEM);
		}
		
		businessWorkFlow.setCreateTime(new Date());
		
		List<JbpmHistProcinst> jbpmHistProcinsts = this.findJbpmHistProcinstBySome(processInstanceId);
		String url = jbpmHistProcinsts.get(0).getvJbpmDeployment().getProcessDefinitionDeploy().getUrl();
		if(StringUtils.isBlank(url)){
			url = new JBPMContext().getUrl().get(entityType);
		}
		businessWorkFlow.setUrl(url);
		o_businessWorkFlowDAO.save(businessWorkFlow);
		
		return processInstanceId;  
	}
	
	/**
	 * 
	 * doProcessInstance:执行流程
	 * 
	 * @author 杨鹏
	 * @param processInstanceId 流程实例ID
	 * @param variables 流程变量
	 */
	@Transactional
	public void doProcessInstance(String processInstanceId,Map<String,Object> variables) {
		String businessId=o_jBPMOperate.getVariable(processInstanceId, "id");
		String path = String.valueOf(variables.get("path"));
		String showIdea = String.valueOf(variables.get("showIdea"));
		if("true".equals(showIdea)||null!=variables.get("examineApproveIdea")){
			String examineApproveIdea = String.valueOf(variables.get("examineApproveIdea"));
			this.createExamineApproveIdea(businessId,processInstanceId,path,examineApproveIdea,UserContext.getUser().getEmpid());
		}
		//保存流程变量
		o_jBPMOperate.saveVariables(processInstanceId, variables);
		
		o_jBPMOperate.singelPocessInstance(processInstanceId,variables);
	}
	
	/**
	 * 
	 * <pre>
	 * 获取流程当前的进度
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param executionId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Integer processRateOfProgress(String executionId) {
		
		Integer progress = 0;
		Integer joinProgress = 0;
		
		// 取流程实例ID
		Execution execution = o_jBPMOperate.getExecutionService().findExecutionById(executionId);
		if(null != execution) {
			String processInstanceId = execution.getProcessInstance().getId();
			
			// 当前正在执行的任务
			List<Task> list = o_jBPMOperate.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
			
			String joinCount = o_jBPMOperate.getVariable(executionId, "joinCount");
			
			for (Task task : list) {
				String[] progresses = StringUtils.split(String.valueOf(task.getDescription()),",");
				if(progresses.length > 0 &&  StringUtils.isNotBlank(progresses[0]) && !"null".equals(progresses[0])){
					progress += Integer.valueOf(progresses[0]);
				}
				// 取多任务节点执行进度
				if(progresses.length > 1) {
					joinProgress = Integer.valueOf(progresses[1]);
				}
			}
			
			// 计算分发节点的任务进度
			if(StringUtils.isNotEmpty(joinCount) && !"0".equals(joinCount)) {
				Integer count = Integer.valueOf(joinCount);
				// 已完成进度加上当前任务进度除以任务总数
				progress = (joinProgress * (count-list.size()) + progress) / count;
			}
			
			// 如果没有可执行任务，进度为0
			if(list.size() == 0) {
				progress = 100;
			}
		}else {
			progress = 100;
		}
		
		return progress;
	}
	
	
	/**
	 * 添加审批意见
	 * @param businessId 业务id
	 * @param processInstanceId 流程id
	 * @param operate 审批操作
	 * @param content 审批意见
	 * @param processPointName 流程节点名称
	 * @param empId 审批人
	 */
	@Transactional
	public void createExamineApproveIdea(String businessId,String processInstanceId,String operate,String content, String empId) {
		ExamineApproveIdea eai = new ExamineApproveIdea();
		eai.setId(Identities.uuid());
		eai.setBusinessId(businessId);
		eai.setProcessInstanceId(processInstanceId);
		eai.setEa_Operate(operate);
		eai.setEa_Content(content);
		eai.setEa_Date(new Date());
		eai.setGatherBy(o_sysEmployeeDAO.get(empId));
		VJbpmHistTask jbpmHistTask=new VJbpmHistTask();
		List<JbpmHistActinst> jbpmHistActinsts = this.findJbpmHistActinstBySome(null, 0L, processInstanceId, empId);
		if(jbpmHistActinsts!=null&&jbpmHistActinsts.size()>0){
			JbpmHistActinst jbpmHistActinst = jbpmHistActinsts.get(0);
			jbpmHistTask.setId(jbpmHistActinst.getJbpmHistTask().getId());
		}
		eai.setJbpmHistTask(jbpmHistTask);
		o_examineApproveIdeaDAO.save(eai);
	}
	
	/**
	 * 
	 * mergeDeployProcessDefinitionDeploy:发布流程
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeploy
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void mergeDeployProcessDefinitionDeploy(ProcessDefinitionDeploy processDefinitionDeploy)throws Exception{
		o_processDefinitionDeployDAO.merge(processDefinitionDeploy);
		
		Criteria criteria = o_deploymentImplDAO.createCriteria();
		List<DeploymentImpl> list = criteria.list();
		List<Long> idList = new ArrayList<Long>();
		for (DeploymentImpl deploymentImpl : list) {
			idList.add(Long.valueOf(deploymentImpl.getId()));
		}
		
		FileUploadEntity fileUploadEntity = o_fileUploadBO.findById(processDefinitionDeploy.getFileUploadEntity().getId());
		ByteArrayInputStream byteArrayInputStream=null;
		byteArrayInputStream = new ByteArrayInputStream(fileUploadEntity.getContents());
		o_jBPMOperate.deploy(byteArrayInputStream);
		
		criteria.add(Restrictions.not(Restrictions.in("id", idList.toArray())));
		list = criteria.list();
		DeploymentImpl deploymentImpl = list.get(0);
		deploymentImpl.setName(processDefinitionDeploy.getId());
		o_deploymentImplDAO.merge(deploymentImpl);
		
	}
	
	/**
	 * 
	 * updateProcessDefinitionDeploy:更改发布流程
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeploy
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void mergeProcessDefinitionDeploy(ProcessDefinitionDeploy processDefinitionDeploy)throws Exception{
		o_processDefinitionDeployDAO.merge(processDefinitionDeploy);
	}
	
	/**
	 * 
	 * removeDeployment:删除发布流程
	 * 
	 * @author 杨鹏
	 * @param ids
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeDeployment(String...ids) throws Exception{
		List<VJbpmDeployment> vJbpmDeployments = this.findVJbpmDeploymentBySome(ids);
		for (VJbpmDeployment vJbpmDeployment : vJbpmDeployments) {
			ProcessDefinitionDeploy processDefinitionDeploy = vJbpmDeployment.getProcessDefinitionDeploy();
			processDefinitionDeploy.setDeleteStatus(Contents.DELETE_STATUS_DELETED);
			this.mergeProcessDefinitionDeploy(processDefinitionDeploy);
			FileUploadEntity fileUploadEntity = processDefinitionDeploy.getFileUploadEntity();
			o_fileUploadBO.removeFileById(fileUploadEntity.getId());
			o_jBPMOperate.deletePorcessDefinition(vJbpmDeployment.getId());
			System.out.println(123);
		}
	}
	
	/**
	 * 
	 * findByPage:条件查询流程定义
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param query
	 * @param sort
	 * @param dir
	 * @param sortList 
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<ProcessDefinitionDeploy> findProcessDefinitionDeployPageBySome(Page<ProcessDefinitionDeploy> page,String query,String deleteStatus, List<Map<String, String>> sortList){
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessDefinitionDeploy.class);
		criteria.setFetchMode("fileUploadEntity", FetchMode.JOIN);
		if(StringUtils.isNotBlank(query)){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("disName", query,MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if(StringUtils.isNotBlank(deleteStatus)){
			criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
		}
		
		if(sortList!=null&&sortList.size()>0){
			for (Map<String, String> sort : sortList) {
				String property=sort.get("property");
				String direction = sort.get("direction");
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("desc".equalsIgnoreCase(direction)){
						criteria.addOrder(Order.desc(property));
					}else{
						criteria.addOrder(Order.asc(property));
					}
				}
			}
		}
		
		return o_processDefinitionDeployDAO.findPage(criteria, page, false);
	}
	/**
	 * 
	 * findById:根据ID查询流程定义
	 * 
	 * @author 杨鹏
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public ProcessDefinitionDeploy findProcessDefinitionDeployById(String id){
		ProcessDefinitionDeploy processDefinitionDeploy=null;
		Criteria criteria = o_processDefinitionDeployDAO.createCriteria();
		criteria.add(Restrictions.idEq(id));
		List<ProcessDefinitionDeploy> list = criteria.list();
		if(list!=null&&list.size()>0){
			processDefinitionDeploy = list.get(0);
		}
		return processDefinitionDeploy;
	}
	/**
	 * 
	 * findProcessDefinitionDeployByIds:
	 * 
	 * @author 杨鹏
	 * @param ids
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<ProcessDefinitionDeploy> findProcessDefinitionDeployByIds(String[] ids){
		Criteria criteria = o_processDefinitionDeployDAO.createCriteria();
		criteria.add(Restrictions.in("id", ids));
		return criteria.list();
	}
	
	/**
	 * 
	 * findVJbpmDeploymentBySome:查询所有发布流程
	 * 
	 * @author 杨鹏
	 * @param vJbpmDeploymentIdS
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public List<VJbpmDeployment> findVJbpmDeploymentBySome(String...vJbpmDeploymentIdS)throws Exception{
		Criteria criteria = o_vJbpmDeploymentDAO.createCriteria();
		criteria.setFetchMode("processDefinitionDeploy", FetchMode.JOIN);
		criteria.add(Restrictions.in("id", vJbpmDeploymentIdS));
		return criteria.list();
	}
	
	/**
	 * 
	 * findProcessDefinitionDeployByPage:分页查询流程
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param query
	 * @param sort
	 * @param dir
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<VJbpmDeployment> findVJbpmDeploymentPageBySome(Page<VJbpmDeployment> page,String query,List<Map<String, String>> sortList){
		DetachedCriteria criteria = DetachedCriteria.forClass(VJbpmDeployment.class);
		criteria.createAlias("processDefinitionDeploy", "processDefinitionDeploy");
		if(StringUtils.isNotBlank(query)){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("processDefinitionDeploy.disName", query,MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("pdid", query,MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if(sortList!=null&&sortList.size()>0){
			for (Map<String, String> sort : sortList) {
				String property=sort.get("property");
				String direction = sort.get("direction");
				if("disName".equalsIgnoreCase(property)){
					property="processDefinitionDeploy.disName";
				}
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("desc".equalsIgnoreCase(direction)){
						criteria.addOrder(Order.desc(property));
					}else{
						criteria.addOrder(Order.asc(property));
					}
				}
			}
		}
		return o_vJbpmDeploymentDAO.findPage(criteria, page,false);
	}
	
	

	/**
	 * 根据流程id和员工id查询该流程的审批意见相关信息.
	 * @param processInstanceId
	 * @param empId
	 * @return List<Object[]>
	 */
	public List<Object[]> findExamineApproveIdeaByParams(String processInstanceId, String empId,String query){
		StringBuffer sb = new StringBuffer();
		sb.append("select  a.ACTIVITY_NAME_,e.EMP_NAME,i.EA_OPERATE,i.EA_CONTENT,i.EA_DATE,i.id,i.PROCESS_INSTANCE_ID,i.BUSINESS_ID ");
		sb.append("from t_jbpm_examine_approve_idea i LEFT JOIN jbpm4_hist_actinst a on i.TASK_ID=a.HTASK_  LEFT JOIN t_sys_employee e ON i.EA_BY=e.ID ");
		sb.append("where 1=1 ");
		if(StringUtils.isNotBlank(processInstanceId)){
			String[] processInstanceIdArray = processInstanceId.split("\\.");
			if(null != processInstanceIdArray && processInstanceIdArray.length>=2){
				sb.append("and (i.PROCESS_INSTANCE_ID='").append(processInstanceIdArray[0]).append(".").append(processInstanceIdArray[1]).append("' ")
					.append(" or i.PROCESS_INSTANCE_ID='").append(processInstanceId).append("') ");
			}
		}
		if(StringUtils.isNotBlank(empId)){
			sb.append("and i.EA_BY='").append(empId).append("' ");
		}
		if(StringUtils.isNotBlank(query)){
			sb.append("and (a.ACTIVITY_NAME_ like '%").append(query).append("%' ").append(" or ").append("e.EMP_NAME like '%").append(query).append("%') ");
		}
		sb.append("order by i.EA_DATE desc ");
		return o_examineApprovalOpinionsDAO.createSQLQuery(sb.toString()).list();
	}


	/**
	 * 
	 * deletePorcessInstance:删除流程实例
	 * 
	 * @author 杨鹏
	 * @param processInstanceIds
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void deletePorcessInstance(String...processInstanceIds) {
		for (String processInstanceId : processInstanceIds) {
			o_jBPMOperate.deletePorcessInstance(processInstanceId);
			List<HistoryProcessInstanceImpl> historyProcessInstanceImpls = o_historyProcessInstanceBO.findByProcessInstanceId(processInstanceId);
			HistoryProcessInstanceImpl historyProcessInstanceImpl = historyProcessInstanceImpls.get(0);
			historyProcessInstanceImpl.setEndActivityName("remove1");
			o_historyProcessInstanceBO.update(historyProcessInstanceImpl);
		}
	}
	
	/**
	 * 
	 * 暂停流程实例
	 * 
	 */
	@Transactional
	public void pausePorcessInstance(String id) {
		this.operateWorkFlow(id, JBPMConstant.JBPM_WORKFLOW_PAUSE);
	}

	/**
	 * 
	 * 启动流程实例
	 * 
	 */
	@Transactional
	public void startPorcessInstance(String id) {
		this.operateWorkFlow(id, JBPMConstant.JBPM_WORKFLOW_RUN);
	}

	/**
	 * 
	 * 操作
	 * 
	 */
	public void operateWorkFlow(String id, String state) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("state", state);
		// 如果不是子流程就改变该流程下的所有流程实例的状态
		if (!o_jBPMOperate.isSubPorcess(id)) {
			o_jBPMOperate.saveVariables(id,variables);			
			//修改其下所有子流程的状态
			List<String> subIds = o_jBPMOperate.findSubPorcessInstanceIds(id, null);
			for(String subId : subIds){
				o_jBPMOperate.saveVariables(subId,variables);
			}			
		} else // 如果是子流程就改变该流程实例的状态
		{
			String parentId = o_jBPMOperate.findParentExcetionId(id);
			// 如果开子流程，也要把父流程开起来
			if (state.equals(JBPMConstant.JBPM_WORKFLOW_RUN)) {				
				o_jBPMOperate.saveVariables(id,variables);								
				if(parentId != id)
					o_jBPMOperate.saveVariables(parentId,variables);			
			}
			// 如果关子流程，要看其他的子流程是否全关了，来决定父流程的开启和关闭
			else {				
				o_jBPMOperate.saveVariables(id,variables);				
				Collection<ExecutionImpl> subInsts = o_jBPMOperate.findSubPorcessInstance(parentId);
				boolean flag = true;
				for(ExecutionImpl subInst:subInsts){
					state = (String) subInst.getVariable("state");
					if(JBPMConstant.JBPM_WORKFLOW_RUN.equals(state));
						flag = false;
				}
				if(flag==true)
					o_jBPMOperate.saveVariables(parentId,variables);
			}
		}
	}

	/**
	 * 
	 * deploy:发布流程定义
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeployId
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void deploy(String processDefinitionDeployId) throws Exception{
		Criteria criteria = o_deploymentImplDAO.createCriteria();
		List<DeploymentImpl> list = criteria.list();
		List<Long> idList = new ArrayList<Long>();
		for (DeploymentImpl deploymentImpl : list) {
			idList.add(Long.valueOf(deploymentImpl.getId()));
		}
		
		ProcessDefinitionDeploy processDefinitionDeploy = this.findProcessDefinitionDeployById(processDefinitionDeployId);
		FileUploadEntity fileUploadEntity = processDefinitionDeploy.getFileUploadEntity();
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(fileUploadEntity.getContents());
		o_jBPMOperate.deploy(byteArrayInputStream);
		
		criteria.add(Restrictions.not(Restrictions.in("id", idList.toArray())));
		list = criteria.list();
		DeploymentImpl deploymentImpl = list.get(0);
		deploymentImpl.setName(processDefinitionDeployId);
		o_deploymentImplDAO.merge(deploymentImpl);
	}


	/**
	 * 
	 * 更新操作者
	 */
	public void updateAssignee(String taskId, String emp2) {
		
		o_jBPMOperate.updateAssignee(taskId,emp2);
		
	}

	/**
	 * 
	 * findJbpmHistProcinstPageBySome:分页条件查询历史定义
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param businessName
	 * @param jbpmDeploymentName
	 * @param dbversion 执行状态：0-未执行，1-已执行
	 * @param assigneeId 執行人
	 * @param sort
	 * @param dir
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<JbpmHistProcinst> findJbpmHistProcinstPageBySome(Page<JbpmHistProcinst> page,String query,String businessName,String jbpmDeploymentName,String startEmpName,Long dbversion,String assigneeId,List<Map<String, String>> sortList){
		DetachedCriteria dc=DetachedCriteria.forClass(JbpmHistProcinst.class);
		dc.createAlias("businessWorkFlow", "businessWorkFlow");
		dc.createAlias("vJbpmDeployment", "vJbpmDeployment");
		dc.createAlias("vJbpmDeployment.processDefinitionDeploy", "processDefinitionDeploy");
		dc.createAlias("businessWorkFlow.createByEmp", "createByEmp");
		if(StringUtils.isNotBlank(query)){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("businessWorkFlow.businessName", query,MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("createByEmp.realname", query,MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("processDefinitionDeploy.disName", query,MatchMode.ANYWHERE));
			if("已删除".equals(query)){
				disjunction.add(Restrictions.eq("endactivity", "remove1"));
			}else if("已完成".equals(query)){
				disjunction.add(Restrictions.eq("endactivity", "end1"));
			}else if("执行中".equals(query)){
				disjunction.add(Restrictions.isNull("endactivity"));
			}
			dc.add(disjunction);
		}
		if(StringUtils.isNotBlank(businessName)){
			dc.add(Restrictions.like("businessWorkFlow.businessName", businessName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(jbpmDeploymentName)){
			dc.add(Restrictions.like("processDefinitionDeploy.disName", jbpmDeploymentName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(startEmpName)){
			dc.add(Restrictions.like("businessWorkFlow.createBy", startEmpName, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(assigneeId)||dbversion!=null){
			Criteria criteria = o_jbpmHistProcinstDAO.createCriteria();
			if(StringUtils.isNotBlank(assigneeId)){
				criteria.add(Restrictions.or(Restrictions.eq("taskAssignee.id", assigneeId), Restrictions.eq("assignee.id", assigneeId)));
			}
			if(dbversion!=null){
				criteria.add(Restrictions.eq("taskDbversion", dbversion));
			}
			criteria.setProjection(Property.forName("id"));
			List<String> list = criteria.list();
			if(null!=list&&list.size()!=0){
				dc.add(Property.forName("id").in(list));
			}else{
				dc.add(Property.forName("id").isNull());
			}
		}
		if(sortList!=null&&sortList.size()>0){
			for (Map<String, String> sort : sortList) {
				String property=sort.get("property");
				String direction = sort.get("direction");
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("businessName".equalsIgnoreCase(property)){
						property="businessWorkFlow.businessName";
					}else if("createByRealname".equalsIgnoreCase(property)){
						property="createByEmp.realname";
					}else if("createTime".equalsIgnoreCase(property)){
						property="businessWorkFlow.createTime";
					}else if("jbpmDeploymentName".equalsIgnoreCase(property)){
						property="processDefinitionDeploy.disName";
					}else if("endactivityShow".equalsIgnoreCase(property)){
						property="endactivity";
					}
					if("desc".equalsIgnoreCase(direction)){
						dc.addOrder(Order.desc(property));
					}else{
						dc.addOrder(Order.asc(property));
					}
				}
			}
		}
		return o_jbpmHistProcinstDAO.findPage(dc, page,false);
	}
	
	/**
	 * 
	 * findJbpmHistProcinstBySome:条件查询历史定义
	 * 
	 * @author 杨鹏
	 * @param id_:流程实例标示
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<JbpmHistProcinst> findJbpmHistProcinstBySome(String id_){
		Criteria criteria = o_jbpmHistProcinstDAO.createCriteria();
		if(id_!=null){
			criteria.add(Restrictions.eq("id_", id_));
		}
		return criteria.list();
	}
	/**
	 * 根据流程实例ID 查询JbpmHistProcinst
	 * @param processInstanceId
	 * @return
	 */
	public List<JbpmHistProcinst> findJbpmHistProcinstByProcessInstanceId(String processInstanceId){
		Criteria criteria = o_jbpmHistProcinstDAO.createCriteria();
		criteria.createAlias("businessWorkFlow", "businessWorkFlow");
		criteria.add(Restrictions.eq("businessWorkFlow.processInstanceId", processInstanceId));
		return criteria.list();
	}
	
	/**
	 * 
	 * findJbpmHistProcinstById:根据ID查询历史流程定义
	 * 
	 * @author 杨鹏
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public JbpmHistProcinst findJbpmHistProcinstById(Long id){
		return o_jbpmHistProcinstDAO.get(id);
	}
	/**
	 * 
	 * findJbpmHistActinstPageBySome:分页条件查询历史节点
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param jbpmHistProcinstId
	 * @param dbversion
	 * @param sort
	 * @param dir
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<JbpmHistActinst> findJbpmHistActinstPageBySome(Page<JbpmHistActinst> page,String query,String assigneeId,Long jbpmHistProcinstId,String endactivity,Long dbversion, List<Map<String, String>> sortList){
		DetachedCriteria dc=DetachedCriteria.forClass(JbpmHistActinst.class);
		dc.createAlias("jbpmHistTask", "jbpmHistTask");
		dc.createAlias("jbpmHistProcinst", "jbpmHistProcinst");
		dc.createAlias("jbpmHistProcinst.vJbpmDeployment", "vJbpmDeployment");
		dc.createAlias("jbpmHistProcinst.vJbpmDeployment.processDefinitionDeploy", "processDefinitionDeploy");
		dc.setFetchMode("vJbpmExecution", FetchMode.JOIN);
		dc.setFetchMode("jbpmHistTask.assignee", FetchMode.JOIN);
		dc.setFetchMode("jbpmHistTask.assignee.sysOrganization", FetchMode.JOIN);
		dc.setFetchMode("jbpmHistTask.examineApproveIdea", FetchMode.JOIN);
		dc.setFetchMode("jbpmHistProcinst.businessWorkFlow", FetchMode.JOIN);
		dc.setFetchMode("jbpmHistProcinst.businessWorkFlow.createByEmp", FetchMode.JOIN);
		
		if(StringUtils.isNotBlank(query)){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("activityName", query,MatchMode.ANYWHERE));
			if("未处理".equals(query)){
				disjunction.add(Restrictions.eq("dbversion", 0));
			}else if("已处理".equals(query)){
				disjunction.add(Restrictions.eq("dbversion", 1));
			}
			dc.add(disjunction);
		}
		
		if(StringUtils.isNotBlank(assigneeId)){
			dc.add(Restrictions.eq("jbpmHistTask.assignee.id", assigneeId));
		}
		if(jbpmHistProcinstId!=null){
			dc.add(Restrictions.eq("jbpmHistProcinst.id", jbpmHistProcinstId));
		}
		if(StringUtils.isNotBlank(endactivity)){
			if("execut1".equals(endactivity)){
				dc.add(Restrictions.isNull("jbpmHistProcinst.endactivity"));
			}else{
				dc.add(Restrictions.eq("jbpmHistProcinst.endactivity", endactivity));
			}
		}
		if(dbversion!=null){
			dc.add(Restrictions.eq("dbversion", dbversion));
		}
		if(sortList!=null&&sortList.size()>0){
			for (Map<String, String> sort : sortList) {
				String property=sort.get("property");
				String direction = sort.get("direction");
				if("disName".equals(property)){
					property="processDefinitionDeploy.disName";
				}else if("dbversionStr".equals(property)){
					property="dbversion";
				}else if("startStr".equals(property)){
					property="start";
				}else if("endStr".equals(property)){
					property="jbpmHistTask.end";
				}
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("desc".equalsIgnoreCase(direction)){
						dc.addOrder(Order.desc(property));
					}else{
						dc.addOrder(Order.asc(property));
					}
				}
			}
		}
		return o_jbpmHistActinstDAO.findPage(dc, page,false);
	}
	/**
	 * 
	 * findJbpmHistActinstPageBySome:分页条件查询历史节点
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param jbpmHistProcinstId
	 * @param dbversion
	 * @param vJbpmExecutionId 
	 * @param assigneeId :执行人ID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<JbpmHistActinst> findJbpmHistActinstBySome(Long jbpmHistProcinstId,Long dbversion, String vJbpmExecutionId, String assigneeId){
		Criteria criteria = o_jbpmHistActinstDAO.createCriteria();
		criteria.createAlias("jbpmHistTask", "jbpmHistTask");
		if(jbpmHistProcinstId!=null){
			criteria.add(Restrictions.eq("jbpmHistProcinst.id", jbpmHistProcinstId));
		}
		if(dbversion!=null){
			criteria.add(Restrictions.eq("dbversion", dbversion));
		}
		if(StringUtils.isNotBlank(vJbpmExecutionId)){
			criteria.add(Restrictions.eq("vJbpmExecution.id_", vJbpmExecutionId));
		}
		if(StringUtils.isNotBlank(assigneeId)){
			criteria.add(Restrictions.eq("jbpmHistTask.assignee.id", assigneeId));
		}
		return criteria.list();
	}
	
	/**
	 * 
	 * findBusinessWorkFlowBySome:
	 * 
	 * @author 杨鹏
	 * @param id
	 * @param processInstanceId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<BusinessWorkFlow> findBusinessWorkFlowBySome(String id,String processInstanceId){
		Criteria criteria = o_businessWorkFlowDAO.createCriteria();
		if(StringUtils.isNotBlank(id)){
			criteria.add(Restrictions.idEq(id));
		}
		if(StringUtils.isNotBlank(processInstanceId)){
			criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
		}
		return criteria.list();
	}
	
	/**
	 * 
	 * findBusinessWorkFlowBySome:查询流程、业务实体关系
	 * 
	 * @author 杨鹏
	 * @param id
	 * @param processInstanceId 流程实例ID
	 * @param businessId 业务ID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<BusinessWorkFlow> findBusinessWorkFlowBySome(String id,String processInstanceId,String businessId){
		Criteria criteria = o_businessWorkFlowDAO.createCriteria();
		if(StringUtils.isNotBlank(id)){
			criteria.add(Restrictions.idEq(id));
		}
		if(StringUtils.isNotBlank(processInstanceId)){
			criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
		}
		if(StringUtils.isNotBlank(businessId)){
			criteria.add(Restrictions.eq("businessId", businessId));
		}
		return criteria.list();
	}
	/**
	 * 根据业务实体ID 查询JbpmHistProcinst实体
	 * @author 邓广义
	 * @param businessId
	 * @return
	 */
	public JbpmHistProcinst findJbpmHistProcinstByBusinessId(String businessId){
		if(StringUtils.isNotBlank(businessId)){
			List<BusinessWorkFlow> bwf = this.findBusinessWorkFlowBySome(null,null,businessId);
			if(null!=bwf && bwf.size()==1){
				List<JbpmHistProcinst> list = this.findJbpmHistProcinstByProcessInstanceId(bwf.get(0).getProcessInstanceId());
				if(null!=list && list.size()==1){
					return list.get(0);
				}
			}
		}
		return null;
	}
}