package com.fhd.process.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.icsystem.ConstructPlanBpmBO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardEmpDAO;
import com.fhd.icm.dao.icsystem.ConstructRelaProcessDAO;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandard;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.icm.entity.icsystem.ConstructRelaProcess;
import com.fhd.icm.entity.rule.Rule;
import com.fhd.icm.entity.standard.StandardRelaProcessure;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.controller.bpm.icsystem.ConstructPlanBpmObject;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.dao.ProcessRelaFileDAO;
import com.fhd.process.dao.ProcessRelaOrgDAO;
import com.fhd.process.dao.ProcessRelaRuleDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaFile;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.process.entity.ProcessRelaRule;
import com.fhd.process.interfaces.IProcessBO;
import com.fhd.process.web.form.ProcessForm;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.organization.SysOrgDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
/**
 * 流程维护
 * @author   李克东
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-25		下午2:55:50
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ProcessBO implements IProcessBO{
	
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private ConstructPlanBpmBO o_constructPlanBpmBO;
	@Autowired
	private ProcessRelaOrgDAO o_processRelaOrgDAO;
	@Autowired
	private ProcessRelaRuleDAO o_processRelaRuleDAO;
	@Autowired
	private ProcessRelaFileDAO o_processRelaFileDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private ConstructPlanRelaStandardEmpDAO o_constructPlanRelaStandardEmpDAO;
	@Autowired
	private ConstructPlanRelaStandardDAO o_constructPlanRelaStandardDAO;
	@Autowired
	private JBPMOperate o_jbpmOperate;
	@Autowired
	private ConstructRelaProcessDAO o_constructRelaProcessDAO;
	@Autowired
	private SysOrgDAO o_SysOrgDAO;
	
	/**
	 * 根据id查询流程.
	 * @param processId
	 * @return Process
	 */
	public Process findProcessById(String processId) {
		return o_processDAO.get(processId);
	}

	/** (non-Javadoc)
	 * @see com.fhd.process.interfaces.IProcessBO#findChildsProcessById(java.lang.String, boolean)
	 */
	@Override
	public List<Process> findProcessBySome(String processId ,String type,boolean self) {
		List<Process> processList = new ArrayList<Process>();
		if (StringUtils.isNotBlank(processId)) {
			Criteria criteria = this.o_processDAO.createCriteria();
			if (self) {// 包含自己
				criteria.add(Restrictions.or(
						Property.forName("parent.id").eq(processId),
						Property.forName("id").eq(processId)));
			} else {
				criteria.add(Property.forName("parent.id").eq(processId));
				criteria.add(Property.forName("type").eq(type));
			}
			processList = criteria.list();
		}
		return processList;
	}
	
	/**
	 * <pre>
	 * 查询流程
	 * </pre>
	 * @author 李克东
	 * @param id
	 * @param query
	 * @param processResult
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Process> findResultByQuery(String id ,String query, List<Process> processResult){
		Criteria criteria = o_processDAO.createCriteria();
		criteria.add(Restrictions.eq("parent.id", id ));
		List<Process> processList=criteria.list();
		for(Process process:processList ){	
			if(!process.getIsLeaf()){
				findResultByQuery(process.getId(), query, processResult);
			}else{
				if(process.getName().contains(query)){
					processResult.add(process);
					break;
				}
			}
		}
		return processResult;
	}

	/**
	 * <pre>
	 * 通过部门Id查找关联的流程实体集合
	 * </pre>
	 * @author 刘中帅
	 * @param orgIdArray：部门Id数组
	 * @return 评价计划与部门关联实体的集合
	 * @since  fhd　Ver 1.1
	 */
	public List<Process> findProcessByOrgIds(String[] orgIdArray){
		List<Process> processList=new ArrayList<Process>();
		Criteria criteria=o_processRelaOrgDAO.createCriteria();
		criteria.createAlias("process", "p");
		criteria.add(Restrictions.in("org.id", orgIdArray));
		criteria.add(Restrictions.eq("type", Contents.ORG_RESPONSIBILITY));
		//过滤--只查末级流程
		criteria.add(Restrictions.eq("p.isLeaf", true));
		List<ProcessRelaOrg> processRelaOrgList=criteria.list();
		for(ProcessRelaOrg processRelaOrg:processRelaOrgList){
			if(!processList.contains(processRelaOrg.getProcess())){
				processList.add(processRelaOrg.getProcess());
			}
		}
		return processList;
	}
	
	/**
	 * 查询流程对应的缺陷数量及缺陷状态.
	 * @return List<Object[]>
	 */
	public List<Object[]> findProcessRelaDefectList(){
		StringBuilder sql = new StringBuilder();
		
		sql.append("select p.id,count(d.id),min(d.defect_level) ")
		  	.append("from t_ic_processure p left join t_ca_defect_assessment da on p.id=da.processure_id ")
		  	.append("left join t_ca_defect d on da.defect_id=d.id ")
		  	.append("where p.is_leaf = true ")
		  	.append("and p.company_id = :companyid ")
			.append("group by p.id ");
		SQLQuery sqlQuery = o_processDAO.createSQLQuery(sql.toString());
		
		sqlQuery.setParameter("companyid", UserContext.getUser().getCompanyid());
		
		return sqlQuery.list();
	}
	/**
	 * <pre>
	 * 通过流程Id查找相关联的实体集合
	 * </pre>
	 * @author 刘中帅
	 * @param processId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysOrganization> findSysOrganizationByProcessId(String[] processId,String type){
		List<ProcessRelaOrg> processRelaOrgList=null;
		List<SysOrganization> sysOrganizationList=new ArrayList<SysOrganization>();
		Criteria criteria=o_processRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.in("process.id", processId));
		criteria.add(Restrictions.eq("type", type));
		processRelaOrgList=criteria.list();
		for(ProcessRelaOrg processRelaOrg:processRelaOrgList){
			if(!sysOrganizationList.contains(processRelaOrg.getOrg())){
				sysOrganizationList.add(processRelaOrg.getOrg());
			}
		}
		return sysOrganizationList;
	}
	
	/**
	 * <pre>
	 * 通过流程Id查找相关联的实体集合
	 * </pre>
	 * @author 刘中帅
	 * @param processIds:流程Id集合
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<ProcessRelaOrg> findProcessRelaOrgByProcessId(String[] processId){
		Criteria criteria=o_processRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.in("process.id", processId));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 通过流程Id查找相关联的实体集合
	 * </pre>
	 * @author 刘中帅
	 * @param processId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> findSysEmployeeByProcessId(String[] processId,String type){
		List<ProcessRelaOrg> processRelaOrgList=null;
		List<SysEmployee> sysEmployeeList=new ArrayList<SysEmployee>();
		Criteria criteria=o_processRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.in("process.id", processId));
		criteria.add(Restrictions.eq("type", type));
		processRelaOrgList=criteria.list();
		for(ProcessRelaOrg processRelaOrg:processRelaOrgList){
			if(!sysEmployeeList.contains(processRelaOrg.getOrg())){
				sysEmployeeList.add(processRelaOrg.getEmp());
			}
		}
		return sysEmployeeList;
	}
	
	/**
	 * <pre>
	 * 通过流程Id查找相关联的实体集合
	 * </pre>
	 * @author 刘中帅
	 * @param processId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public ProcessRelaOrg findSysEmployeeByProcessId(String processId){
		ProcessRelaOrg processRelaOrg=null;
		Criteria criteria=o_processRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.eq("process.id", processId));
		criteria.add(Restrictions.eq("type", Contents.EMP_RESPONSIBILITY));
		processRelaOrg=(ProcessRelaOrg)criteria.uniqueResult();
		return processRelaOrg;
	}
	
	/**
	 * <pre>
	 * 保存流程
	 * </pre>
	 * @author 李克东
	 * @param processForm
	 * @param parentId
	 * modify 宋佳 
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveProcess(ProcessForm processForm,String parentId){
		Process process = new Process();
		String num=Identities.uuid();
		if(StringUtils.isNotBlank(processForm.getId())){
			process = this.findProcessById(processForm.getId());
		}else{
			process.setId(Identities.uuid());
		}
		String companyId = UserContext.getUser().getCompanyid();	
		
		if(StringUtils.isBlank(processForm.getCode())){
			
		}else{
			process.setCode(processForm.getCode());
		}
		process.setName(processForm.getName());
		process.setControlTarget(processForm.getControlTarget());
		process.setDesc(processForm.getDesc());
		process.setSort(processForm.getSort());
		if(processForm.getControlLevelId()!=null){
			DictEntry importance = new DictEntry();
			importance.setId(processForm.getControlLevelId());
			process.setImportance(importance);	
		}
		//影响财报科目
		if(processForm.getRelaSubject() != null){
			process.setRelaSubject(processForm.getRelaSubject());
		}
		if(processForm.getFrequency()!=null){
			DictEntry frequency = new DictEntry();
			frequency.setId(processForm.getFrequency().getId());
			process.setFrequency(frequency);	
		}
		SysOrganization company=new SysOrganization() ;
		company.setId(companyId);
		process.setCompany(company);
		process.setIsLeaf(true);
		if(!StringUtils.isBlank(parentId)){
			if("root".equals(parentId))	{
				process.setIdSeq("."+num+".");
				process.setLevel(1);
				process.setParent(null);
			}else{			
				Criteria criteria = o_processDAO.createCriteria();
				criteria.add(Restrictions.eq("id", parentId));//获得父节点		
				Process processParent=(Process)criteria.uniqueResult();
				processParent.setIsLeaf(false);
				process.setIdSeq(processParent.getIdSeq()+num+".");
				process.setLevel(processParent.getLevel()+1);
				process.setParent(processParent);
			}
		}
		o_processDAO.merge(process);
		//流程关联制度关系保存
	    if(StringUtils.isNotBlank(processForm.getRuleId())){
			//先删除processRelaOrg中此流程对应的数据
			Criteria processRelaRuleCriteria = o_processRelaRuleDAO.createCriteria();
			processRelaRuleCriteria.add(Restrictions.eq("process.id", processForm.getId()));
			List<ProcessRelaRule> processRelaRule = processRelaRuleCriteria.list();
			for(ProcessRelaRule prr : processRelaRule){
				o_processRelaRuleDAO.delete(prr.getId());
			}
			//设置流程关联制度
			String[] ruleIds = processForm.getRuleId().split(",");
			for(int i = 0; i < ruleIds.length; ++i){
				ProcessRelaRule prr = new ProcessRelaRule();
				prr.setId(Identities.uuid());
				prr.setProcess(process);
				Rule rule = new Rule();
				rule.setId(ruleIds[i]);
				prr.setRule(rule);
				o_processRelaRuleDAO.merge(prr);
			}
	 	}	
		//主责部门关系保存
        if(StringUtils.isNotBlank(processForm.getOrgId())){
    		//先删除processRelaOrg中此流程对应的数据
    		Criteria criteria = o_processRelaOrgDAO.createCriteria();
    		criteria.add(Restrictions.eq("process.id", processForm.getId()));
    		List<ProcessRelaOrg> processRelaOrg = criteria.list();
    		for(ProcessRelaOrg pro : processRelaOrg){
    			o_processRelaOrgDAO.delete(pro.getId());
    		}
    		//再添加数据
        	ProcessRelaOrg processRealOrg=new ProcessRelaOrg();
            processRealOrg.setId(Identities.uuid());
     		String orgid=IcmStandardUtils.findIdbyJason(processForm.getOrgId(), "id");//将Json转换为需要的字符串
     		processRealOrg.setOrg(o_SysOrgDAO.get(orgid));     		
     		processRealOrg.setEmp(null);
     		processRealOrg.setType(Contents.ORG_RESPONSIBILITY);
     		processRealOrg.setProcess(process);
         	o_processRelaOrgDAO.merge(processRealOrg);
     	}	
        if(!StringUtils.isEmpty(processForm.getRelaOrgId())){
			String crdorgid=IcmStandardUtils.findIdbyJason(processForm.getRelaOrgId(), "id");
			String[] crdorgidArray = crdorgid.split(",");
			for(int i = 0;i < crdorgidArray.length;i++){
				Criteria criteria = o_processRelaOrgDAO.createCriteria();
	    		criteria.add(Restrictions.eq("process.id", processForm.getId()));
				ProcessRelaOrg processRealOrg=new ProcessRelaOrg();
				processRealOrg.setProcess(process);
				processRealOrg.setId(Identities.uuid());
				crdorgid.split(",");
				processRealOrg.setOrg(o_SysOrgDAO.get(crdorgidArray[i]));
				processRealOrg.setType(Contents.ORG_PARTICIPATION);
				o_processRelaOrgDAO.merge(processRealOrg);
			}
		}
        //责任人关系保存
     	if(StringUtils.isNotBlank(processForm.getEmpId())){
     		ProcessRelaOrg processRealOrg=new ProcessRelaOrg();
     		processRealOrg.setId(Identities.uuid());
     		SysEmployee emp=new SysEmployee();
     		processRealOrg.setEmp(emp);
     		String empid=IcmStandardUtils.findIdbyJason(processForm.getEmpId(), "id");
     		emp.setId(empid);
     		processRealOrg.setOrg(null);
     		processRealOrg.setType(Contents.EMP_RESPONSIBILITY);
     		processRealOrg.setProcess(process);
         	o_processRelaOrgDAO.merge(processRealOrg);
        }
     	//先删除processRelaOrg中此流程对应的数据
     	Criteria fileCriteria=o_processRelaFileDAO.createCriteria();
     	fileCriteria.add(Restrictions.eq("process.id", processForm.getId()));
     	List<ProcessRelaFile> fileList=fileCriteria.list();
     	for(ProcessRelaFile prr : fileList){
     		o_processRelaFileDAO.delete(prr.getId());
     	}
		if(StringUtils.isNotBlank(processForm.getFileId())){
			String[] fileIds = processForm.getFileId().split(",");
			String fileId ="";
			FileUploadEntity file = new FileUploadEntity();
			ProcessRelaFile processRelaFile = new ProcessRelaFile();
			
			for(int i=0;i<fileIds.length;i++){
				String ids=Identities.uuid();
				processRelaFile.setId(ids);
				fileId=fileIds[i];
				file.setId(fileId);
				processRelaFile.setFile(file);
				processRelaFile.setProcess(process);
				o_processRelaFileDAO.merge(processRelaFile);
			}
		}
	}
	
	/**
	 * <pre>
	 * 删除流程节点
	 * </pre>
	 * @author 李克东
	 * @param processID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeProcessByID(String processID){
		o_processDAO.delete(processID);
	}
	
	/**
	 * <pre>
	 * 加载流程表单
	 * </pre>
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> findProcessForm(String processEditId){
		Criteria criteria = o_processDAO.createCriteria();
		criteria.add(Restrictions.eq("id", processEditId));
		List<Process> processList=criteria.list();
		Process process=processList.get(0);
		Map<String, Object> formMap = new HashMap<String, Object>();
		//formMap.put("code", process.getCode());
		formMap.put("controlTarget", process.getControlTarget());
		formMap.put("desc", process.getDesc());
		formMap.put("sort", process.getSort());
		Criteria orgcriteria = o_processRelaOrgDAO.createCriteria();
		orgcriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaOrg> orglist=orgcriteria.list();
		JSONArray crdorgIdJsonArray = new JSONArray();
		if(null != orglist && orglist.size()>0){
			for(ProcessRelaOrg pro : orglist){
				if(Contents.ORG_RESPONSIBILITY.equals(pro.getType())){//责任部门
					formMap.put("orgId","[{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
				}
				if(Contents.ORG_PARTICIPATION.equals(pro.getType())){//相关部门
					crdorgIdJsonArray.add("{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}");
					formMap.put("relaOrgId",crdorgIdJsonArray.toString());
				}
				if(Contents.EMP_RESPONSIBILITY.equals(pro.getType())){//责任人
					formMap.put("empId","[{\"id\":\""+pro.getEmp().getId()+"\",\"empno\":\"\",\"empname\":\"\"}]");
				}
			}
		}
		formMap.put("id", process.getId());
		if(null != process.getImportance()){
			formMap.put("controlLevelId",process.getImportance().getId());	
		}
		if(null != process.getFrequency()){
			formMap.put("frequency",process.getFrequency().getId());	
		}
		if(null != process.getRelaSubject()){
			formMap.put("relaSubject",process.getRelaSubject().split(","));
		}
		//制度
		Criteria ruleCriteria=o_processRelaRuleDAO.createCriteria();
		ruleCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaRule> ruleList=ruleCriteria.list();
		if(ruleList.size()>0){			
			String ruleIds=new String();
			boolean flag = true;
			for(ProcessRelaRule processRelaRule:ruleList){
				String ruleId=processRelaRule.getRule().getId();
				if(flag){
					ruleIds += ruleId;
					flag = false;
				}else{
					ruleIds += ","+ruleId;
				}
			}
			formMap.put("ruleId", ruleIds);
		}
		//文件
		Criteria fileCriteria=o_processRelaFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaFile> fileList=fileCriteria.list();
		if(fileList.size()>0){			
			String fileIds=new String();
			for(ProcessRelaFile processRelaFile:fileList){
				String fileId=processRelaFile.getFile().getId();
				fileIds=fileIds+","+fileId;
			}
			formMap.put("fileId", fileIds);
		}
		if(StringUtils.isNotBlank(process.getCode())){			
			formMap.put("code", process.getCode());
		}else{
			List<String> list=o_processDAO.find("select max(id) from Process");
			String maxprocessid=(String)list.get(0);
			int i=Integer.parseInt(maxprocessid);
			String processCode=Integer.toString(i+1);
			formMap.put("code","pr"+processCode);
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 * 加载流程表单
	 * </pre>
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> findconstructProcessForm(String processEditId){
		Process process=o_processDAO.get(processEditId);
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("code", process.getCode());
		formMap.put("id", process.getId());
		if(null != process.getParent()){
			formMap.put("parentid", process.getParent().getId());
			formMap.put("parentprocess", process.getParent().getName());
		}
		formMap.put("name", process.getName());
		formMap.put("controlTarget", process.getControlTarget());
		formMap.put("desc", process.getDesc());
		formMap.put("sort", process.getSort());
		Criteria orgcriteria = o_processRelaOrgDAO.createCriteria();
		orgcriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaOrg> orglist=orgcriteria.list();
		if(null != orglist && orglist.size()>0){
			JSONArray crdorgIdJsonArray = new JSONArray();
			for(ProcessRelaOrg pro : orglist){
				if(Contents.ORG_RESPONSIBILITY.equals(pro.getType())){//责任部门
					formMap.put("orgId","[{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
				}
				if(Contents.ORG_PARTICIPATION.equals(pro.getType())){//相关部门
					crdorgIdJsonArray.add("{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}");
					formMap.put("relaOrgId",crdorgIdJsonArray.toString());
				}
				if(Contents.EMP_RESPONSIBILITY.equals(pro.getType())){//责任人
					formMap.put("empId","[{\"id\":\""+pro.getEmp().getId()+"\",\"empno\":\"\",\"empname\":\"\"}]");
				}
			}
		}
		if(null != process.getImportance()){
			formMap.put("controlLevelId",process.getImportance().getId());	
		}
		if(null != process.getFrequency()){
			formMap.put("frequency",process.getFrequency().getId());	
		}
		if(null != process.getRelaSubject()){
			formMap.put("relaSubject",process.getRelaSubject().split(","));
		}
		//制度
		Criteria ruleCriteria=o_processRelaRuleDAO.createCriteria();
		ruleCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaRule> ruleList=ruleCriteria.list();
		if(ruleList.size()>0){			
			String ruleIds=new String();
			boolean flag = true;
			for(ProcessRelaRule processRelaRule:ruleList){
				String ruleId=processRelaRule.getRule().getId();
				if(flag){
					ruleIds += ruleId;
					flag = false;
				}else{
					ruleIds += ","+ruleId;
				}
			}
			formMap.put("ruleId", ruleIds);
		}
		//文件
		Criteria fileCriteria=o_processRelaFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaFile> fileList=fileCriteria.list();
		if(fileList.size()>0){			
			String fileIds=new String();
			for(ProcessRelaFile processRelaFile:fileList){
				String fileId=processRelaFile.getFile().getId();
				fileIds=fileIds+","+fileId;
			}
			formMap.put("fileId", fileIds);
		}
		if(StringUtils.isNotBlank(process.getCode())){			
			formMap.put("code", process.getCode());
		}else{
			List<String> list=o_processDAO.find("select max(id) from Process");
			String maxprocessid=(String)list.get(0);
			int i=Integer.parseInt(maxprocessid);
			String processCode=Integer.toString(i+1);
			formMap.put("code","pr"+processCode);
		}
		
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 * 加载流程表单
	 * </pre>
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> findconstructProcessFormForView(String processEditId){
		Process process=o_processDAO.get(processEditId);
		Map<String, Object> formMap = new HashMap<String, Object>();
		//formMap.put("code", process.getCode());
		if(null != process.getParent()){
			formMap.put("parentprocess", process.getParent().getName());
		}
		formMap.put("name", process.getName());
		formMap.put("controlTarget", process.getControlTarget());
		formMap.put("desc", process.getDesc());
		formMap.put("sort", process.getSort());
		StringBuffer sb = new StringBuffer();
		if(null != process.getRelaSubject()){
			for(String subjectId : process.getRelaSubject().split(",")){
				sb.append(o_dictEntryDAO.get(subjectId).getName()).append(",");
			}
			formMap.put("relaSubject", sb.toString().substring(0,sb.toString().length()-1));
		}
		Criteria orgcriteria = o_processRelaOrgDAO.createCriteria();
		orgcriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaOrg> orglist=orgcriteria.list();
		if(null != orglist && orglist.size()>0){
			StringBuffer relaBuffer = new StringBuffer();
			for(ProcessRelaOrg pro : orglist){
				if(Contents.ORG_RESPONSIBILITY.equals(pro.getType())){//责任部门
					formMap.put("orgName",pro.getOrg().getOrgname());
				}
				if(Contents.ORG_PARTICIPATION.equals(pro.getType())){//相关部门
					relaBuffer.append(pro.getOrg().getOrgname());
					relaBuffer.append(",");
				}
				if(Contents.EMP_RESPONSIBILITY.equals(pro.getType())){//责任人
					formMap.put("empName",pro.getEmp().getEmpname());
				}
			}
			if(!StringUtils.isBlank(relaBuffer.toString())){
				formMap.put("relaOrgName",relaBuffer.toString().substring(0,relaBuffer.toString().length()-1));
			}
		}
		formMap.put("id", process.getId());
		if(null != process.getImportance()){
			formMap.put("controlLevelId",process.getImportance().getName());	
		}
		if(null != process.getFrequency()){
			formMap.put("frequency",process.getFrequency().getName());	
		}
		//制度
		Criteria ruleCriteria=o_processRelaRuleDAO.createCriteria();
		ruleCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaRule> ruleList=ruleCriteria.list();
		if(ruleList.size()>0){			
			String ruleNames=new String();
			boolean flag = true;
			for(ProcessRelaRule processRelaRule:ruleList){
				String ruleName=processRelaRule.getRule().getName();
				if(flag){
					ruleNames += ruleName;
					flag = false;
				}else{
					ruleNames += ","+ruleName;
				}
			}
			formMap.put("ruleName", ruleNames);
		}
		//文件
		Criteria fileCriteria=o_processRelaFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("process.id", processEditId));
		List<ProcessRelaFile> fileList=fileCriteria.list();
		if(fileList.size()>0){			
			String fileIds=new String();
			for(ProcessRelaFile processRelaFile:fileList){
				String fileId=processRelaFile.getFile().getId();
				fileIds=fileIds+"<a href='javascript:void(0)'onclick=\"Ext.getCmp('floweditpanelforview').downloadFile('"
						+ fileId
						+ "');\" >"+processRelaFile.getFile().getNewFileName()+"</a>"+"||";
			}
			formMap.put("fileId", fileIds);
		}
		if(StringUtils.isNotBlank(process.getCode())){			
			formMap.put("code", process.getCode());
		}else{
			List<String> list=o_processDAO.find("select max(id) from Process");
			String maxprocessid=(String)list.get(0);
			int i=Integer.parseInt(maxprocessid);
			String processCode=Integer.toString(i+1);
			formMap.put("code","pr"+processCode);
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	
	/**
	 * <pre>
	 * 加载流程选择
	 * </pre>
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String,Object>> findProcessByIds(String processIds){
		String[] idArray=processIds.split(",");
		Criteria criteria = o_processDAO.createCriteria();
		criteria.add(Restrictions.in("id",idArray));
		List<Process> processList=criteria.list();
		List<Map<String,Object>> lm=new ArrayList<Map<String,Object>>();
		for(Process process:processList){
			Map<String, Object> mapBean = new HashMap<String, Object>();
			mapBean.put("id", process.getId());
			mapBean.put("dbid", process.getId());
			mapBean.put("code", process.getCode());
			mapBean.put("text", process.getName());
			lm.add(mapBean);
		}
		return lm;
	}
	
	/**
	 * <pre>
	 * 编号自动生成
	 * </pre>
	 * @author 李克东
	 * @param processEditID
	 * @param parentId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public Map<String,Object> findProcessCode(String processEditID,String parentId){
		Map<String,Object> processMap =new HashMap<String,Object>();
		if(StringUtils.isNotBlank(processEditID)){
			processMap.put("code","pr"+processEditID);
		}else{
			List<String> list=o_processDAO.find("select max(id) from Process");
			String maxprocessid=(String)list.get(0);
			int i=Integer.parseInt(maxprocessid);
			String processCode=Integer.toString(i+1);
			processMap.put("code","pr"+processCode);
		}
		
		Process processParent= new Process();
		Criteria criteria= o_processDAO.createCriteria();
		criteria.add(Restrictions.eq("id", parentId));
		List<Process> listmap=criteria.list();
		if(null != listmap && listmap.size()>0){
			processParent=listmap.get(0);
			processMap.put("Parent", processParent.getName());
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", processMap);
		node.put("success", true);
		return node;
	}
	
	/**
	 * 根据流程id查询相关风险--修改此方法时，列顺序不要修改，要展示新的列直接放在最后面.
	 * @author 吴德福
	 * @param processId 流程id
	 * @return List<Object[]> 流程及相关风险信息
	 */
	public List<Object[]> findRiskStatusByProcessId(String processId){
		StringBuilder sb = new StringBuilder();
		sb.append("select p.id,p.processure_name,r.id,r.risk_name,h.id,h.assessement_status,h.etrend,h.adjust_time,h.impacts,h.probability,h.management_urgency,h.time_period_id ");
        sb.append("from t_ic_processure p, t_processure_risk_processure pr, t_rm_risks r, t_rm_risk_adjust_history h ");
        sb.append("where p.id=pr.processure_id and pr.risk_id=r.id and r.id=h.risk_id and h.is_latest=true and r.delete_estatus='1' ");
        if (StringUtils.isNotBlank(processId)){
            sb.append("and p.id='").append(processId).append("' ");
        }
        sb.append("order by h.assessement_status asc ");
		return o_processDAO.createSQLQuery(sb.toString()).list();
	}
	
	/**
	 * 本方法不再调用，学习使用
	 * 根据流程id查找流程下那所有节点
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findProcessListByPageBak(Page<ConstructPlanRelaStandardEmp> page, String query,String constructPlanId) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ConstructPlanRelaStandardEmp.class);
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		if (!StringUtils.isNotEmpty(constructPlanId)) {
			return null;
		}
		criteria.createAlias("constructPlanRelaStandard", "constructPlanRelaStandard",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("constructPlanRelaOrg", "constructPlanRelaOrg",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("constructPlanRelaOrg.emp", "emp",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("constructPlanRelaStandard.constructPlan", "constructPlan",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("constructPlan.id",constructPlanId));
		criteria.add(Restrictions.eq("emp.id",UserContext.getUserid()));
		Map<String, Object> result = new HashMap<String, Object>();
		List<ConstructPlanRelaStandardEmp> constructPlanRelaStandardEmpList =o_constructPlanRelaStandardEmpDAO.findPage(criteria,page,false).getResult();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Process process = null;
		for (ConstructPlanRelaStandardEmp constructPlanRelaStandardEmp : constructPlanRelaStandardEmpList) {
			Map<String, Object> map = new HashMap<String, Object>();
			Set<StandardRelaProcessure> standardRelaProcessures = constructPlanRelaStandardEmp.getConstructPlanRelaStandard().getStandard().getStandardRelaProcessure();
			for (StandardRelaProcessure standardRelaProcessure : standardRelaProcessures) {
				process =  standardRelaProcessure.getProcessure();
			}
			map.put("id", process.getId());
			map.put("processCode", process.getCode());
			map.put("processName", process.getName());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", constructPlanRelaStandardEmpList.size());
		return result;
	}
	/**
	 * 本方法不再调用，学习使用
	 * 根据流程id查找流程下那所有节点
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
		
	public Map<String, Object> findProcessListByPage(Page<ConstructPlanRelaStandardEmp> page, String query,String constructPlanId,String executionId,String orgScroll) {
		ConstructPlanBpmObject constructPlanBpmObject = o_constructPlanBpmBO.findBpmObjectByExecutionId(executionId,"item");
		String constructPlanRelaProcess = constructPlanBpmObject.getForeachExecutionId();
		String[] constructPlanRelaProcessArray = constructPlanRelaProcess.split(",");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Process process = null;
		for(String constructPlanRelaProcessId : constructPlanRelaProcessArray){
			ConstructRelaProcess constructRelaProcess = o_constructRelaProcessDAO.get(constructPlanRelaProcessId);
			ConstructPlanRelaStandard constructPlanRelaStandard = constructRelaProcess.getConstructPlanRelaStandard();
			Set<StandardRelaProcessure> standardRelaProcessures = constructPlanRelaStandard.getStandard().getStandardRelaProcessure();
			for (StandardRelaProcessure standardRelaProcessure : standardRelaProcessures) {
				process =  standardRelaProcessure.getProcessure();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", process.getId());
			map.put("standardName", constructPlanRelaStandard.getStandard().getParent().getName());
			map.put("standardRequir", constructPlanRelaStandard.getStandard().getName());
			map.put("processCode", process.getCode());
			map.put("processName", process.getName());   //onclick=\"Ext.getCmp('tablePanel').oneSave(
			//设置操作的链接路径
			map.put("operate", "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+orgScroll+"').scollWindow('processEdit','"
					+ process.getId()
					+ "');\" >编辑</a>&nbsp;&nbsp;/&nbsp;&nbsp;"
					+	"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+orgScroll+"').scollWindow('noteEdit','"
					+ process.getId()
					+ "');\" >流程节点维护</a>&nbsp;&nbsp;/&nbsp;&nbsp;"
					+ "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+orgScroll+"').scollWindow('riskEdit', '"
					+ process.getId() + "');\" >风险控制矩阵维护</a>");
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", constructPlanRelaProcessArray.length);
		return result;
	}
	/**
	 * 根据流程id查找流程下那所有节点
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findProcessListApproveByPage(Page<ConstructPlanRelaStandardEmp> page, String query,String constructPlanId,String executionId,String orgScroll) {
		ConstructPlanBpmObject constructPlanBpmObject = o_constructPlanBpmBO.findBpmObjectByExecutionId(executionId,"approveitem");
		String constructPlanRelaProcess = constructPlanBpmObject.getConstructPlanRelaProcessIds();
		String[] constructPlanRelaProcessArray = constructPlanRelaProcess.split(",");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Process process = null;
		for(String constructPlanRelaProcessId : constructPlanRelaProcessArray){
			ConstructRelaProcess constructRelaProcess = o_constructRelaProcessDAO.get(constructPlanRelaProcessId);
			ConstructPlanRelaStandard constructPlanRelaStandard = constructRelaProcess.getConstructPlanRelaStandard();
			Set<StandardRelaProcessure> standardRelaProcessures = constructPlanRelaStandard.getStandard().getStandardRelaProcessure();
			for (StandardRelaProcessure standardRelaProcessure : standardRelaProcessures) {
				process =  standardRelaProcessure.getProcessure();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", process.getId());
			map.put("standardName", constructPlanRelaStandard.getStandard().getParent().getName());
			map.put("standardRequir", constructPlanRelaStandard.getStandard().getName());
			map.put("processCode", process.getCode());
			map.put("processName", process.getName());
			//设置操作的链接路径
			map.put("operate", "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+orgScroll+"').scollWindow('processEdit','"
					+ process.getId()
					+ "');\" >查看流程信息</a>");
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", dataList.size());
		return result;
	}
	/**
	 *   获取流程对应的规章制度,流程bo暴漏出的对外接口，所有流程查询方面的内容全部封在这里面
	 *   add by 宋佳
	 */
	public List<ProcessRelaRule> getProcessRelaRules(String processId){
		Criteria processRelaRuleCriteria = o_processRelaRuleDAO.createCriteria();
		processRelaRuleCriteria.add(Restrictions.eq("process.id", processId));
		List<ProcessRelaRule> processRelaRule = processRelaRuleCriteria.list();
		return processRelaRule;
	}
	
	/**
	 * 获取流程主责部门和相关部门名称，对外提供接口
	 * @param process
	 * @return
	 */
	public String[] getProcessOrg(Process process){
		String org_responsibility = "";
		StringBuffer org_participation = new StringBuffer();
		Set<ProcessRelaOrg> processRelaOrgs = process.getProcessRelaOrg();
		for (ProcessRelaOrg processRelaOrg : processRelaOrgs) {
			if (Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){ /*流程主责部门*/
				org_responsibility = processRelaOrg.getOrg().getOrgname();
			}else{
				if(processRelaOrg.getOrg() != null){
					org_participation.append(processRelaOrg.getOrg().getOrgname()).append("、");   /*流程配合部门*/
				}else{
					org_participation.append("无");
				}
			}
		}
		return new String[]{org_responsibility,org_participation.toString()};
	}
}