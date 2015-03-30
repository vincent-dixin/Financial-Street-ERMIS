package com.fhd.icm.business.icsystem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.assess.AssessRelaDefectDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaOrgDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardEmpDAO;
import com.fhd.icm.dao.icsystem.DiagnosesRelaDefectDAO;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandard;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.icm.entity.icsystem.DiagnosesRelaDefect;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.process.dao.ProcessRelaStandardDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaStandard;

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
public class ConstructPlanRelaStandardBO {
	
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private ConstructPlanRelaStandardDAO o_constructPlanRelaStandardDAO;
	@Autowired
	private ConstructPlanRelaStandardEmpDAO o_constructPlanRelaStandardEmpDAO;
	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private ConstructPlanRelaOrgDAO o_constructPlanRelaOrgDAO;
	@Autowired
	private ConstructPlanDAO o_constructPlanDAO;
	@Autowired
	private ProcessRelaStandardDAO o_processRelaStandardDAO;
	@Autowired
	private ConstructPlanRelaStandardEmpDAO o_constructPlanRelaStandardEmp;
	@Autowired
	private AssessRelaDefectDAO o_assessRelaDefectDAO;
	@Autowired
	private DiagnosesRelaDefectDAO o_diagnosesRelaDefectDAO;

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
		
		//合规诊断
		if(Contents.RECONSTRUCT_PLAN_TYPE_DIAGNOSES.equals(constructPlan.getType().getId())){
			isProcessEdit=false;
		}else if(Contents.RECONSTRUCT_PLAN_TYPE_PROCESS.equals(constructPlan.getType().getId())){//流程梳理
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
	public void saveConstructPlanRelaDefect(String defectIds,String constructPlanId){
		//查询评价计划
		ConstructPlan constructPlan = o_constructPlanDAO.get(constructPlanId);
		//查询出已经有的流程，流程Id有重复的不予添加
		List<ConstructPlanRelaStandard> appList= findConstructPlanRelaStandardListByConstructPlanId(constructPlanId);
		boolean isProcessEdit=true,isNormallyDiagnosis=true;//穿行，抽样
		
		//合规诊断
		if(Contents.RECONSTRUCT_PLAN_TYPE_DIAGNOSES.equals(constructPlan.getType().getId())){
			isProcessEdit=false;
		}else if(Contents.RECONSTRUCT_PLAN_TYPE_PROCESS.equals(constructPlan.getType().getId())){//流程梳理
			isNormallyDiagnosis=false;
		}
		String[] defectIdArray=null;
		defectIdArray=defectIds.split(",");
		//判断是否有重复数据,没有重复的数据才添加
		for(String pId : defectIdArray){
			///////////////////////// 将缺陷转换成
			String standardId = "";
			////   评价关联缺陷
			Criteria cri = o_assessRelaDefectDAO.createCriteria();
			cri.add(Restrictions.eq("defect.id", pId));
			List<AssessRelaDefect> assessRelaDefectList = cri.list();
			for(AssessRelaDefect assessRelaDefect : assessRelaDefectList){
				Process process = assessRelaDefect.getProcess();
				Criteria crit = o_processRelaStandardDAO.createCriteria();
				crit.add(Restrictions.eq("process.id", process.getId()));
				ProcessRelaStandard processRelaStandard = (ProcessRelaStandard) crit.uniqueResult();
				Standard standard = processRelaStandard.getStandard();
				standardId = standard.getId();
			}
			//合规诊断关联的缺陷
			cri = o_diagnosesRelaDefectDAO.createCriteria();
			cri.add(Restrictions.eq("defect.id", pId));
			List<DiagnosesRelaDefect> diagnosesRelaDefectList = cri.list();
			for(DiagnosesRelaDefect diagnosesRelaDefect : diagnosesRelaDefectList){
				standardId = diagnosesRelaDefect.getDiagnoses().getConstructPlanRelaStandard().getStandard().getId();
			}
			/////////////////////////
			boolean flag=false;
			for(ConstructPlanRelaStandard app:appList){
				if(standardId.equals(app.getStandard().getId())){
					flag=true;
				}
			}
			if(!(flag || StringUtils.isBlank(standardId))){
				String constructPlanRelaStandard = Identities.uuid();
				//内控评价计划与流程的关联
				ConstructPlanRelaStandard planRelaStandard =new ConstructPlanRelaStandard();
				planRelaStandard.setId(constructPlanRelaStandard);
				planRelaStandard.setIsProcessEdit(isProcessEdit);
				planRelaStandard.setIsNormallyDiagnosis(isNormallyDiagnosis);
				//内控评价计划
				planRelaStandard.setConstructPlan(constructPlan);	
				//流程实体
				planRelaStandard.setStandard(o_standardDAO.get(standardId));
				
				o_constructPlanRelaStandardDAO.merge(planRelaStandard);
			}
		}
	}
	@Transactional
	public void mergeConstructPlanRelaStandardBatch(String jsonString,String defaultValue) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		Set<String> constructPlanRelaStandardIdSet = new HashSet<String>();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//AssessPlanRelaProcess的ID
			constructPlanRelaStandardIdSet.add(id);
		}
		List<ConstructPlanRelaStandard> constructPlanRelaStandardList = o_constructPlanRelaStandardDAO.get(constructPlanRelaStandardIdSet);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//AssessPlanRelaProcess的ID
			String planRelaStandardEmpId = jsonObject.getString("constructPlanEmp");
			Boolean isNormallyDiagnosis = jsonObject.getBoolean("isNormallyDiagnosis");
			Boolean isProcessEdit = jsonObject.getBoolean("isProcessEdit");
			
			if(StringUtils.isNotBlank(id)){
				for (ConstructPlanRelaStandard constructPlanRelaStandard : constructPlanRelaStandardList) {
					if(id.equals(constructPlanRelaStandard.getId())){
						//删除以前的emp
						this.removeStandardRelaOrgEmp(constructPlanRelaStandard.getId());
						
						if(StringUtils.isBlank(defaultValue))
						{
							ConstructPlanRelaStandardEmp emp = this.getConstructPlanRelaStandardEmpEntryById(constructPlanRelaStandard.getId(), planRelaStandardEmpId);
							if(StringUtils.isBlank(emp.getId())){
								emp.setId(Identities.uuid());
							}
							emp.setConstructPlanRelaStandard(constructPlanRelaStandard);
							emp.setConstructPlanRelaOrg(o_constructPlanRelaOrgDAO.get(planRelaStandardEmpId));
							o_constructPlanRelaStandardEmpDAO.merge(emp);
						}else{
							ConstructPlanRelaStandardEmp emp = this.getConstructPlanRelaStandardEmpEntryById(constructPlanRelaStandard.getId(), planRelaStandardEmpId);
							if(StringUtils.isBlank(emp.getId())){
								emp.setId(Identities.uuid());
							}
							emp.setConstructPlanRelaStandard(constructPlanRelaStandard);
							emp.setConstructPlanRelaOrg(o_constructPlanRelaOrgDAO.get(defaultValue));
							o_constructPlanRelaStandardEmpDAO.merge(emp);
						}
						constructPlanRelaStandard.setIsNormallyDiagnosis(isNormallyDiagnosis);
						constructPlanRelaStandard.setIsProcessEdit(isProcessEdit);
						o_constructPlanRelaStandardDAO.merge(constructPlanRelaStandard);
					}
				}
			}
		}
	}
	private void removeStandardRelaOrgEmp(String standardId){
		Query query = null;
		StringBuffer hql = new StringBuffer();
		hql.append("delete ConstructPlanRelaStandardEmp app where app.constructPlanRelaStandard.id in('").append(standardId).append("')");
		query=o_constructPlanRelaStandardDAO.createQuery(hql.toString());
		query.executeUpdate();
		
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
	 * 物理删除评价计划ID为assessPlanId的评价计划关联的流程范围
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeConstructPlanRelaStandardByConstructPlanId(List<String> idList){
		Query query = null;
		if(null !=idList && idList.size()>0){
			StringBuffer hql = new StringBuffer();
			hql.append("delete ConstructPlanRelaStandard app where app.id in('").append(StringUtils.join(idList,"','")).append("')");
			query=o_constructPlanRelaStandardDAO.createQuery(hql.toString());
			query.executeUpdate();
		}
		for(String id : idList){
			String hql="delete ConstructPlanRelaStandardEmp mp where constructPlanRelaStandard.id=:constructPlanRelaStandardId";
			query=o_constructPlanRelaStandardEmpDAO.createQuery(hql);
			query.setParameter("constructPlanRelaStandardId",id);
			query.executeUpdate();
		}
	}
	
	/**
	 * <pre>
	 * 根据建设计划id查询计划关联标准
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param constructPlanId 评价计划ID
	 * @return List<AssessPlanRelaProcess>
	 * @since  fhd　Ver 1.1
	*/
	public List<ConstructPlanRelaStandard> findConstructPlanRelaStandardListByConstructPlanId(String constructPlanId){
		Criteria criteria = o_constructPlanRelaStandardDAO.createCriteria();
		criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		return criteria.list();
	}
	
	/**
	 * 根据评价计划id和流程id查询评价计划关联流程.
	 * @author 吴德福
	 * @param assessPlanId 评价计划ID
	 * @param processId 流程id
	 * @return List<AssessPlanRelaProcess>
	 * @since  fhd　Ver 1.1
	*/
//	public List<AssessPlanRelaProcess> findAssessPlanRelaProcessListBySome(String assessPlanId, String processId){
//		Criteria criteria = o_assessPlanRelaProcessDAO.createCriteria();
//		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
//		criteria.add(Restrictions.eq("process.id", processId));
//		return criteria.list();
//	}
	
	/**
	 * <pre>
	 * 根据评价关联的流程ID查询AssessPlanRelaProcess实体
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaProcessId 评价关联流程的ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
//	public AssessPlanRelaProcess findAssessPlanRelaProcessById(String assessPlanRelaProcessId){
//		return o_assessPlanRelaProcessDAO.get(assessPlanRelaProcessId);
//	}
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
	public Page<ConstructPlanRelaStandard> findConstructPlanRelaStandardListByPage(Page<ConstructPlanRelaStandard> page, String query, String constructPlanId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ConstructPlanRelaStandard.class);
		detachedCriteria.createAlias("standard", "standard");
		detachedCriteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		if(StringUtils.isNotBlank(query)){//模糊匹配流程的名称及上级流程名称
			detachedCriteria.add(Property.forName("standard.name").like(query, MatchMode.ANYWHERE));
		}
		return o_constructPlanRelaStandardDAO.findPage(detachedCriteria, page, false);
	}
	//根据标准获取标准流程对应关系
	public ProcessRelaStandard getProcessRelaStandard(String standardId){
		Criteria cri = o_processRelaStandardDAO.createCriteria();
		cri.add(Restrictions.eq("standard.id", standardId));
		ProcessRelaStandard processRelaStandard = (ProcessRelaStandard) cri.uniqueResult();
		return processRelaStandard;
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
}

