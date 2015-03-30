package com.fhd.icm.business.icsystem;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.icsystem.ConstructPlanBpmBO;
import com.fhd.icm.dao.defect.DefectDAO;
import com.fhd.icm.dao.defect.DefectRelaOrgDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaOrgDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardEmpDAO;
import com.fhd.icm.dao.icsystem.DiagnosesDAO;
import com.fhd.icm.dao.icsystem.DiagnosesRelaDefectDAO;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandard;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.icm.entity.icsystem.Diagnoses;
import com.fhd.icm.entity.icsystem.DiagnosesRelaDefect;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.controller.bpm.icsystem.ConstructPlanBpmObject;
import com.fhd.icm.web.form.icsystem.DefectClearUpGroupForm;
import com.fhd.icm.web.form.icsystem.GroupForm;
import com.fhd.process.dao.ProcessRelaStandardDAO;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.process.entity.ProcessRelaStandard;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   宋  佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-24		上午11:43:41
 *
 * @see 	 
 */
@SuppressWarnings("unchecked")
@Service
public class PlanRelaStandardDiagnosesBO {
	
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private ConstructPlanBpmBO o_constructPlanBpmBO;
	@Autowired
	private ConstructPlanRelaStandardDAO o_constructPlanRelaStandardDAO;
	@Autowired
	private ConstructPlanRelaStandardEmpDAO o_constructPlanRelaStandardEmpDAO;
	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private DiagnosesDAO o_diagnosesDAO;
	@Autowired
	private ConstructPlanRelaOrgDAO o_constructPlanRelaOrgDAO;
	@Autowired
	private ConstructPlanDAO o_constructPlanDAO;
	@Autowired
	private ProcessRelaStandardDAO o_processRelaStandardDAO;
	@Autowired
	private DiagnosesRelaDefectDAO o_diagnosesRelaDefectDAO;
	@Autowired
	private ConstructPlanRelaStandardEmpDAO o_constructPlanRelaStandardEmp;
	@Autowired
	private DefectDAO o_defectDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private ConstructPlanRelaStandardBO o_constructPlanRelaStandardBO;
	@Autowired
	private DefectRelaOrgDAO o_defectRelaOrgDAO;
	

	/**
	 * 制定评价人中选择标准
	 * @author 宋佳
	 * @param processIds：流程Ids，多选是以‘,’间隔的字符串
	 * @param assessPlanId 评价计划Id
	 * @param assessPlanDepartIds 评价计划相关部门ids，多选是以‘,’间隔的字符串
	 * @param selectType 流程选择类型：按部门/按流程
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveConstructPlanRelaStandard(String standardIds,String constructPlanId){
		//查询评价计划
		ConstructPlan constructPlan = o_constructPlanDAO.get(constructPlanId);
		//查询出已经有的流程，流程Id有重复的不予添加
		List<ConstructPlanRelaStandard> appList= findConstructPlanRelaStandardListByConstructPlanId(constructPlanId);
		boolean isProcessEdit=true,isNormallyDiagnosis=true;//穿行，抽样
		
		//穿行测试
		if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(constructPlan.getType().getId())){
			isProcessEdit=false;
		}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(constructPlan.getType().getId())){//抽样测试
			isNormallyDiagnosis=false;
		}
		String[] processIdArray=null;
		processIdArray=standardIds.split(",");
		//判断是否有重复数据,没有重复的数据才添加
		for(String pId : processIdArray){
			boolean flag=false;
			for(ConstructPlanRelaStandard app:appList){
				if(pId.equals(app.getStandard().getId())){
					flag=true;
				}
			}
			if(!flag){
				String constructPlanRelaStandard = Identities.uuid();
				//内控评价计划与流程的关联
				ConstructPlanRelaStandard planRelaStandard =new ConstructPlanRelaStandard();
				planRelaStandard.setId(constructPlanRelaStandard);
				planRelaStandard.setIsProcessEdit(isProcessEdit);
				planRelaStandard.setIsNormallyDiagnosis(isNormallyDiagnosis);
				//内控评价计划
				planRelaStandard.setConstructPlan(constructPlan);	
				//流程实体
				planRelaStandard.setStandard(o_standardDAO.get(pId));
				
				o_constructPlanRelaStandardDAO.merge(planRelaStandard);
			}
		}
	}
	@Transactional
	public void mergeConstructPlanRelaDiagnosesBatch(String jsonString,String type) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");   //diagnosesId
			Diagnoses diagnoses = o_diagnosesDAO.get(id);
			diagnoses.setDiagnosis(jsonObject.getBoolean("diagnosis"));
			if(StringUtils.isBlank(jsonObject.getString("proof")) || "null".equals(jsonObject.getString("proof"))){
				diagnoses.setProof("");
			}else{
				diagnoses.setProof(jsonObject.getString("proof"));
			}
			if(StringUtils.isBlank(jsonObject.getString("description")) || "null".equals(jsonObject.getString("description"))){
				diagnoses.setDescription("");
			}else{
				diagnoses.setDescription(jsonObject.getString("description"));
			}
			if(StringUtils.isBlank(jsonObject.getString("controldesc")) || "null".equals(jsonObject.getString("controldesc"))){
				diagnoses.setControldesc("");
			}else{
				diagnoses.setControldesc(jsonObject.getString("controldesc"));
			}
			diagnoses.setEstatus(Contents.STATUS_SUBMITTED);
			o_diagnosesDAO.merge(diagnoses);
		}
		
	}
	
	// 根据关联标准id和计划关联empid  找到 关联标准emp实体
	private ConstructPlanRelaStandardEmp getConstructPlanRelaStandardEmpEntryById(String standardId,String empId){
		Criteria cri = o_constructPlanRelaStandardEmpDAO.createCriteria();
		cri.add(Restrictions.eq("constructPlanRelaStandard.id", standardId));
		cri.add(Restrictions.eq("constructPlanRelaOrg.id", empId));
		ConstructPlanRelaStandardEmp empEntry = (ConstructPlanRelaStandardEmp) cri.uniqueResult();
		if(empEntry == null){
			return new ConstructPlanRelaStandardEmp();
		}else{
			return empEntry;
		}
		
		
	}
	// 根据关联标准id和计划关联empid  找到 关联标准emp实体
	public ConstructPlanRelaStandardEmp getPlanStandardEmp(String planStandardId){
		Criteria cri = o_constructPlanRelaStandardEmpDAO.createCriteria();
		cri.add(Restrictions.eq("constructPlanRelaStandard.id", planStandardId));
		ConstructPlanRelaStandardEmp empEntry = (ConstructPlanRelaStandardEmp) cri.uniqueResult();
		return empEntry;
	}
	
	
	/**
	 * <pre>
	 * 根据建设计划id查询计划关联标准
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @return List<AssessPlanRelaProcess>
	 * @since  fhd　Ver 1.1
	*/
	public List<ConstructPlanRelaStandard> findConstructPlanRelaStandardListByConstructPlanId(String constructPlanId){
		Criteria criteria = o_constructPlanRelaStandardDAO.createCriteria();
		criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 查询评价计划关联的流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param page 分页类
	 * @param query 查询条件模糊匹配流程名称及上级流程名称
	 * @param assessPlanId 评价计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<Diagnoses> findPlanRelaStandardDiagnosesListByPage(Page<Diagnoses> page, String query, String constructPlanId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Diagnoses.class);
		detachedCriteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		if(StringUtils.isNotBlank(query)){//模糊匹配流程的名称及上级流程名称
			//detachedCriteria.add(Restrictions.or(Property.forName("process.name").like(query, MatchMode.ANYWHERE), Property.forName("parentProcess.name").like(query, MatchMode.ANYWHERE)));
		}
		return o_diagnosesDAO.findPage(detachedCriteria, page, false);
	}
	

	
	//根据标准获取标准流程对应关系
	public ProcessRelaStandard getProcessRelaStandard(String standardId){
		Criteria cri = o_processRelaStandardDAO.createCriteria();
		cri.add(Restrictions.eq("standard.id", standardId));
		ProcessRelaStandard dd = (ProcessRelaStandard) cri.uniqueResult();
		return dd;
	}
	
	public List<ConstructPlanRelaOrg> findConstructPlanEmp(String constructPlanId){
		Criteria criteria = o_constructPlanRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.eq("constructPlan.id",constructPlanId));
		List<ConstructPlanRelaOrg> list= criteria.list();
		return list;
	}
	
	// 当基本信息中计划对应责任人发生变化时，删除计划标准对应的相关人员信息
	public int deletePlanStandarEmp(String planOrgId){
		String hqlDelete = "delete ConstructPlanRelaStandardEmp c where c.constructPlanRelaOrg = :empId";
		int deletedEntities = o_constructPlanRelaStandardEmpDAO.createQuery(hqlDelete)
		.setString("empId",planOrgId)
		.executeUpdate();
		return deletedEntities;
	} 
	
	/**
	 * <pre>
	 *加载流程表单，将数据库中信息写入表单
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> findDefectIdbyConstructPlanId(String constructPlanId){
		Criteria criteria = o_diagnosesRelaDefectDAO.createCriteria();
		criteria.createAlias("diagnoses", "diagnoses",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("diagnoses.constructPlan", "constructPlan",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		List<DiagnosesRelaDefect> diagnosesRelaDefectList = criteria.list();
		String[] diagnosesDefectId = new String[diagnosesRelaDefectList.size()];
		int i = 0;
		for(DiagnosesRelaDefect diagnosesRelaDefect : diagnosesRelaDefectList){
			diagnosesDefectId[i] = diagnosesRelaDefect.getId();
			i++;
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", diagnosesDefectId);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 *加载流程表单，将数据库中信息写入表单
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> findDefectIdbyExecutionId(String executionId){
		ConstructPlanBpmObject constructPlanBpmObject = o_constructPlanBpmBO.findBpmObjectByExecutionId(executionId,"backItem");
		String dianosesRelaDefectIds = constructPlanBpmObject.getDianosesRelaDefectId();
		String[] dianosesRelaDefectIdArray = dianosesRelaDefectIds.split(",");
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", dianosesRelaDefectIdArray);
		node.put("success", true);
		return node;
	}
	
	/**
	 * <pre>
	 *加载
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> loadDiagnosesDefectFormData(String defectId){
		DiagnosesRelaDefect diagnosesRelaDefect = o_diagnosesRelaDefectDAO.get(defectId);
		Standard standard = o_standardDAO.get(diagnosesRelaDefect.getDiagnoses().getConstructPlanRelaStandard().getStandard().getId());
		ProcessRelaStandard processRelaStandard = o_constructPlanRelaStandardBO.getProcessRelaStandard(standard.getId());
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("standardName", standard.getParent().getName());
		if(processRelaStandard != null){
			formMap.put("processName", processRelaStandard.getProcess().getName());
		}
		//formMap.put("pointNote", measure.getp());
		Set<StandardRelaOrg> orgList = standard.getStandardRelaOrg();
		for(Iterator<StandardRelaOrg> it = orgList.iterator();it.hasNext();){
			StandardRelaOrg org = (StandardRelaOrg) it.next();
			if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
				formMap.put("standardRelaOrg",org.getOrg().getOrgname());
			}
		}
		formMap.put("diagnosesDefectId", defectId);
		if(StringUtils.isBlank(diagnosesRelaDefect.getDiagnoses().getProof()) || "null".equals(diagnosesRelaDefect.getDiagnoses().getProof())){
			formMap.put("proof", "无");
		}else{
			formMap.put("proof", diagnosesRelaDefect.getDiagnoses().getProof());
		}
		if(StringUtils.isBlank(diagnosesRelaDefect.getIsAgree())){
			formMap.put("isAgree", "0yn_n");
		}else{
			formMap.put("isAgree", diagnosesRelaDefect.getIsAgree());
		}
		formMap.put("feedbackoptions", diagnosesRelaDefect.getFeedbackOptions());
		if(StringUtils.isBlank(diagnosesRelaDefect.getDiagnoses().getControldesc()) || "null".equals(diagnosesRelaDefect.getDiagnoses().getControldesc())){
			formMap.put("controldesc", "无");
		}else{
			formMap.put("controldesc", diagnosesRelaDefect.getDiagnoses().getControldesc());
		}
		formMap.put("controlRequirement", standard.getName());
		if(diagnosesRelaDefect.getDiagnoses().getDiagnosis()){
			formMap.put("diagnosis", "合格");
		}else{
			formMap.put("diagnosis", "不合格");
		}
		
		formMap.put("description", diagnosesRelaDefect.getDiagnoses().getDescription());
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	
	/**
	 * <pre>
	 *   保存缺陷反馈信息
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processPointForm
	 * @param parentId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveDiagnosesDefect(GroupForm groupForm){
		//保存控制措施信息
        JSONArray msForm=JSONArray.fromObject(groupForm.getDefectFormstr());
		//String[] measureCode = riskForm.getMeasurecode();
		if(msForm != null){   //如果没有控制节点就只保存风险
		for(int i = 0 ;i < msForm.size(); i++){
			JSONObject jsonObject = msForm.getJSONObject(i);
			DiagnosesRelaDefect diagnosesRelaDefect = o_diagnosesRelaDefectDAO.get(jsonObject.getString("diagnosesDefectId"));
			diagnosesRelaDefect.setIsAgree(jsonObject.getString("isAgree"));
			diagnosesRelaDefect.setFeedbackOptions(jsonObject.getString("feedbackoptions"));
			diagnosesRelaDefect.setFeedbackTime(new Date());
			diagnosesRelaDefect.setFeedbackBy(UserContext.getUser().getUserid());
			o_diagnosesRelaDefectDAO.merge(diagnosesRelaDefect);
		}
	}
	}
	/**
	 * <pre>
	 *加载
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param defectId 
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> loadDefectClearUpFormData(String defectId){
		DiagnosesRelaDefect diagnosesRelaDefect = o_diagnosesRelaDefectDAO.get(defectId);
		Standard standard = diagnosesRelaDefect.getDiagnoses().getConstructPlanRelaStandard().getStandard();
		Defect defect = diagnosesRelaDefect.getDefect();
		Diagnoses diagnoses = diagnosesRelaDefect.getDiagnoses();
		//通过标准Id 找到对应关系 
		ProcessRelaStandard processRelaStandard = o_constructPlanRelaStandardBO.getProcessRelaStandard(standard.getId());
//		constructPlanRelaStandardMap.put("orgName", StringUtils.join(orgNameSet, ","));
		Map<String, Object> formMap = new HashMap<String, Object>();
		if (null != processRelaStandard) {// 显示末级流程名称
			formMap.put("processName",processRelaStandard.getProcess().getName());
		}
		formMap.put("standardName", standard.getParent().getName());
		//formMap.put("pointNote", measure.getp());
		Set<StandardRelaOrg> orgList = standard.getStandardRelaOrg();
		for(Iterator<StandardRelaOrg> it = orgList.iterator();it.hasNext();){
			StandardRelaOrg org = (StandardRelaOrg) it.next();
			if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
				formMap.put("standardRelaOrg",org.getOrg().getOrgname());
			}
		}
		Set<DefectRelaOrg> defectorgList = defect.getDefectRelaOrg();
		for(Iterator<DefectRelaOrg> it = defectorgList.iterator();it.hasNext();){
			DefectRelaOrg org = (DefectRelaOrg) it.next();
			if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
				formMap.put("orgId","[{\"id\":\""+org.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
			}
		}
		if(processRelaStandard != null){
     		formMap.put("processName", processRelaStandard.getProcess().getName());
     	}
		if(StringUtils.isBlank(diagnoses.getControldesc()) || "null".equals(diagnoses.getControldesc())){
			formMap.put("controldesc", "无");
		}else{
			formMap.put("controldesc", diagnoses.getControldesc());
		}
		formMap.put("diagnosesDefectId", defectId);
		formMap.put("controlRequirement",standard.getName());
		if(StringUtils.isBlank(diagnoses.getProof()) || "null".equals(diagnoses.getProof())){
			formMap.put("proof", "无");
		}else{
			formMap.put("proof", diagnoses.getProof());
		}
		if(diagnoses.getDiagnosis()){
			formMap.put("diagnosis", "合格");
		}else{
			formMap.put("diagnosis", "不合格");
		}
		if("0yn_n".equals(diagnosesRelaDefect.getIsAgree())){
			formMap.put("isAgree", "否");
			formMap.put("feedbackoptions", diagnosesRelaDefect.getFeedbackOptions());
		}else{
			formMap.put("isAgree", "是");
			formMap.put("feedbackoptions", "无");
		}
		if(!(defect.getLevel() == null)){
			formMap.put("level", defect.getLevel().getId());
		}
		if(!(defect.getType() == null)){
			formMap.put("type", defect.getType().getId());
		}
		formMap.put("desc", defect.getDesc());
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	
	/**
	 * <pre>
	 *   保存缺陷反馈信息
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processPointForm
	 * @param parentId
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveDefectClearUp(DefectClearUpGroupForm groupForm){
		//保存控制措施信息
		JSONArray msForm=JSONArray.fromObject(groupForm.getDefectClearUpFormstr());
		//String[] measureCode = riskForm.getMeasurecode();
		if(msForm != null){   //如果没有控制节点就只保存风险
			for(int i = 0 ;i < msForm.size(); i++){
				JSONObject jsonObject = msForm.getJSONObject(i);
				DiagnosesRelaDefect diagnosesRelaDefect = o_diagnosesRelaDefectDAO.get(jsonObject.getString("diagnosesDefectId"));
				Defect defect = diagnosesRelaDefect.getDefect();
				defect.setType(o_dictEntryDAO.get(jsonObject.getString("type")));
				defect.setLevel(o_dictEntryDAO.get(jsonObject.getString("level")));
				defect.setDesc(jsonObject.getString("desc"));
				this.saveDefectRelaOrg(jsonObject.getString("orgId"), defect);
				o_diagnosesRelaDefectDAO.merge(diagnosesRelaDefect);
			}
		}
	}
	
	/**
	 *  保存整改主管机关
	 */
	//主责部门关系保存
    private void saveDefectRelaOrg(String orgId,Defect defect){
    	//将Json转换为需要的字符串
    	if(StringUtils.isNotBlank(IcmStandardUtils.findIdbyJason(orgId, "id"))){
			//先删除processRelaOrg中此流程对应的数据
			Criteria criteria = o_defectRelaOrgDAO.createCriteria();
			criteria.add(Restrictions.eq("defect.id", defect.getId()));
			List<DefectRelaOrg> defectRelaOrgList = criteria.list();
			for(DefectRelaOrg pro : defectRelaOrgList){
				o_defectRelaOrgDAO.delete(pro.getId());
			}
			//再添加数据
	    	DefectRelaOrg defectRelaOrg=new DefectRelaOrg();
	    	defectRelaOrg.setId(Identities.uuid());
	        SysOrganization org=new SysOrganization();
	        defectRelaOrg.setOrg(org);     		
	 		String orgid= IcmStandardUtils.findIdbyJason(orgId, "id");
	 		org.setId(orgid);
	 		defectRelaOrg.setEmp(null);
	 		defectRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
	 		defectRelaOrg.setDefect(defect);
	 		o_defectRelaOrgDAO.merge(defectRelaOrg);
	 	}	
    }
	/**
	 *  通过id 找到   diagnosis 实体
	 */
	public Diagnoses findDiagnosesById(String id){
		return o_diagnosesDAO.get(id);
		}
}

