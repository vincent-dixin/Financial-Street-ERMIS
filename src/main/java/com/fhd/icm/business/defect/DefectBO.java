package com.fhd.icm.business.defect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.defect.DefectDAO;
import com.fhd.icm.dao.defect.DefectRelaImproveDAO;
import com.fhd.icm.dao.defect.DefectRelaOrgDAO;
import com.fhd.icm.dao.rectify.ImproveDAO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.form.DefectForm;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   李克东
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-17		下午5:04:36
 *
 * @see 	 
 */
@Service
@SuppressWarnings({"unchecked","deprecation"})
public class DefectBO{
	
	@Autowired
	private DefectDAO o_defectDAO;
	@Autowired
	private DefectRelaImproveDAO o_defectRelaImproveDAO;
	@Autowired
	private ImproveDAO o_improveDAO; 
	@Autowired
	private DefectRelaOrgDAO o_defectRelaOrgDAO;
	@Autowired
	private SysOrganizationDAO o_sysOrganizationDAO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	/**
	 * <pre>
	 * 储存缺陷
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param jsonString
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveDefects(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		if(jsonArray.size() == 0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String defectId = jsonObject.getString("defectId");//缺陷ID
			String desc=jsonObject.getString("desc");//缺陷描述
			String level=jsonObject.getString("level");//缺陷级别
			String type=jsonObject.getString("type");//缺陷类型
			String orgId = jsonObject.getString("orgId");//责任部门id
			if(StringUtils.isNotBlank(defectId)){
				Defect defect = o_defectDAO.get(defectId);
				if(StringUtils.isNotBlank(desc)){
					defect.setDesc(desc);
				}
				if(StringUtils.isNotBlank(type)){
					defect.setType(new DictEntry(type));
				}
				if(StringUtils.isNotBlank(level)){
					defect.setLevel(new DictEntry(level));
				}
				o_defectDAO.merge(defect);
				
				if(StringUtils.isNotBlank(orgId)){
					Criteria criteria = o_defectRelaOrgDAO.createCriteria();
					criteria.add(Restrictions.eq("defect.id", defectId));
					criteria.add(Restrictions.eq("org.id", orgId));
					criteria.add(Restrictions.eq("type", Contents.ORG_RESPONSIBILITY));
					List<DefectRelaOrg> defectRelaOrgList = criteria.list();
					if(null != defectRelaOrgList && defectRelaOrgList.size()>0){
						//判断缺陷是否存在关联责任部门,已存在不需要再插入
					}else{
						//删除缺陷关联责任部门
						o_defectRelaOrgDAO.createQuery("delete DefectRelaOrg dro where dro.defect.id=:defectId and dro.type=:type").setString("defectId", defectId).setString("type", Contents.ORG_RESPONSIBILITY).executeUpdate();
						
						DefectRelaOrg defectRelaOrg = new DefectRelaOrg();
						defectRelaOrg.setId(Identities.uuid());
						defectRelaOrg.setDefect(defect);
						SysOrganization sysOrganization = new SysOrganization();
						sysOrganization.setId(orgId);
						defectRelaOrg.setOrg(sysOrganization);
						defectRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
						//defectRelaOrg.setEmp(emp);//责任人员
						o_defectRelaOrgDAO.merge(defectRelaOrg);
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 新增一条缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectForm 要新增的缺陷表单
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public Defect saveDefect(DefectForm defectForm, String submittype) {
		Defect defect = null;
		if(null != defectForm.getDefectId() && StringUtils.isNotBlank(defectForm.getDefectId())){
			defect = o_defectDAO.get(defectForm.getDefectId());
		}else{
			defect = new Defect(Identities.uuid());
		}
		//defect.setCode(defectForm.getCode());
		defect.setCode(DateUtils.formatDate(new Date(), "yyyyMMddhhmmss"));
		defect.setDefectAnalyze(defectForm.getDefectAnalyze());
		defect.setDesc(defectForm.getDesc());
		defect.setDesignDefect(defectForm.getDesignDefect());
		defect.setExecuteDefect(defectForm.getExecuteDefect());
		defect.setImproveAdivce(defectForm.getImproveAdivce());
		defect.setSort(defectForm.getSort());
		defect.setLevel(defectForm.getLevel());
		defect.setType(defectForm.getType());
		defect.setDesignDefect(defectForm.getDesignDefect());
		defect.setExecuteDefect(defectForm.getExecuteDefect());
		defect.setDealStatus(Contents.DEAL_STATUS_NOTSTART);
		if("save".equals(submittype)){
			defect.setStatus(Contents.STATUS_SAVED);
		}else if("submit".equals(submittype)){
			defect.setStatus(Contents.STATUS_SUBMITTED);
		}
		defect.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
		defect.setCompany(o_sysOrganizationDAO.get(UserContext.getUser().getCompanyid()));
		o_defectDAO.merge(defect);
		if(defectForm.getOrg() != null){
			DefectRelaOrg defectRela = new  DefectRelaOrg();
			defectRela.setId(Identities.uuid());
			defectRela.setDefect(defect);
			defectRela.setType(Contents.ORG_RESPONSIBILITY);
			SysOrganization iorg=new SysOrganization();
			String orgid=IcmStandardUtils.findIdbyJason(defectForm.getOrg(), "id");
			iorg.setId(orgid);
			defectRela.setOrg(iorg);
			o_defectRelaOrgDAO.merge(defectRela);
		}
		return defect;
	}
	
	/**
	 * <pre>
	 *保存缺陷跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectForm
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveDefectFollow(DefectForm defectForm) {
		
		
		Defect defect = new Defect(Identities.uuid());
		defect.setDesc(defectForm.getDesc());
		o_defectDAO.merge(defect);
		Improve improve = new Improve(Identities.uuid());
		improve.setName(defectForm.getImprove());
		o_improveDAO.merge(improve);
		DefectRelaImprove defectRelaImprove = new DefectRelaImprove(defectForm.getId());			
		defectRelaImprove.setImprove(improve);
		defectRelaImprove.setDefect(defect);
	/*	defectRelaImprove.setImproveResult(defectForm.getImproveResult());
		defectRelaImprove.setImproveTest(defectForm.getImproveTest());
		defectRelaImprove.setTestAnalyze(defectForm.getTestAnalyze());
		defectRelaImprove.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);*/
		o_defectRelaImproveDAO.merge(defectRelaImprove);
	}
	
	/**
	 * <pre>
	 * 保存一条缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defect 要保存的缺陷
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeDefect(DefectForm defectForm) {
		Defect defect = o_defectDAO.get(defectForm.getId());
		
		String companyId = defectForm.getCompanyId();
		String levelId = defectForm.getLevelId();
		String typeId = defectForm.getTypeId();
		defect.setCode(defectForm.getCode());
		defect.setDefectAnalyze(defectForm.getDefectAnalyze());
		defect.setDesc(defectForm.getDesc());
		defect.setDesignDefect(defectForm.getDesignDefect());
		defect.setExecuteDefect(defectForm.getExecuteDefect());
		defect.setImproveAdivce(defectForm.getImproveAdivce());
		defect.setSort(defectForm.getSort());
		defect.setDealStatus(Contents.DEAL_STATUS_NOTSTART);
		defect.setStatus(Contents.STATUS_SAVED);
		defect.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
		
		defect.setCompany(new SysOrganization(companyId));
		if(StringUtils.isNotBlank(levelId)){
			defect.setLevel(new DictEntry(levelId));
		}
		if(StringUtils.isNotBlank(typeId)){
			defect.setType(new DictEntry(typeId));
		}
	
		o_defectDAO.merge(defect);
	}
	
	/**
	 * <pre>
	 * 保存缺陷信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defect 待保存的缺陷
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeDefect(Defect defect){
		o_defectDAO.merge(defect);
	}
	
	/**
	 * 缺陷列表批量保存.
	 * @author 吴德福
	 * @param jsonString
	 */
	@Transactional
	public void mergeDefectBatch(String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String defectId = jsonObject.getString("defectId");//缺陷id
			String orgId = jsonObject.getString("orgId");//责任部门
			String defectDesc = jsonObject.getString("desc");//缺陷描述
			String defectLevel = jsonObject.getString("level");//缺陷级别
			String defectType = jsonObject.getString("type");//缺陷类型
			if(StringUtils.isNotBlank(defectId)){
				Defect defect = o_defectDAO.get(defectId);
				if(null != defect){
					if(StringUtils.isNotBlank(defectDesc)){
						defect.setDesc(defectDesc);
					}
					if(StringUtils.isNotBlank(defectLevel)){
						DictEntry defectLevelEntry = new DictEntry();;
						defectLevelEntry.setId(defectLevel);
						defect.setLevel(defectLevelEntry);
					}
					if(StringUtils.isNotBlank(defectType)){
						DictEntry defectTypeEntry = new DictEntry();;
						defectTypeEntry.setId(defectType);
						defect.setType(defectTypeEntry);
					}
					if(StringUtils.isNotBlank(orgId)){
						//删除缺陷关联责任部门
						o_defectRelaOrgDAO.createQuery("delete DefectRelaOrg dro where dro.defect.id=:defectId and dro.type=:type").setString("defectId", defectId).setString("type", Contents.ORG_RESPONSIBILITY).executeUpdate();
						//插入缺陷关联责任部门
						DefectRelaOrg defectRelaOrg = new DefectRelaOrg();
						defectRelaOrg.setId(Identities.uuid());
						SysOrganization org = new SysOrganization();
						org.setId(orgId);
						defectRelaOrg.setOrg(org);
						defectRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
						defectRelaOrg.setDefect(defect);
						o_defectRelaOrgDAO.merge(defectRelaOrg);
					}
				}
			}
		}
	}
	/**
	 * <pre>
	 * 根据缺陷的ID串逻辑删除缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectIds 缺陷的ID串，多个以“,”隔开，例如：ddddd,radfsaf
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeDefectByIdBatch(String defectIds){
		if(StringUtils.isNotBlank(defectIds)){
			String[] defectIdArray = defectIds.split(",");
			StringBuffer hql = new StringBuffer();
			hql.append("update Defect defect set defect.deleteStatus=:deleteStatus");
			hql.append(" where defect.id in('");
			for (int i = 0; i<defectIdArray.length; i++) {
				if(i==defectIdArray.length-1){
					hql.append(defectIdArray[i]).append("')");
				}else{
					hql.append(defectIdArray[i]).append("','");
				}
			}
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("deleteStatus", Contents.DELETE_STATUS_DELETED);
			o_defectDAO.batchExecute(hql.toString(), values);
		}
	}
	
	
	/**
	 * <pre>
	 *删除缺陷跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectIds
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeDefectFollowByIdBatch(String defectIds){
		if(StringUtils.isNotBlank(defectIds)){
			String[] defectIdArray = defectIds.split(",");
			StringBuffer hql = new StringBuffer();
			hql.append("update DefectRelaImprove defectRelaImprove set defectRelaImprove.deleteStatus=:deleteStatus");
			hql.append(" where defectRelaImprove.id in('");
			for (int i = 0; i<defectIdArray.length; i++) {
				if(i==defectIdArray.length-1){
					hql.append(defectIdArray[i]).append("')");
				}else{
					hql.append(defectIdArray[i]).append("','");
				}
			}
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("deleteStatus", Contents.DELETE_STATUS_DELETED);
			o_defectDAO.batchExecute(hql.toString(), values);
		}
	}
	
	/**
	 * <pre>
	 * 分页查询缺陷 
	 * 可以通过以下条件过滤：查询条件模糊匹配编号和名称，评价计划ID，整改计划ID
	 * </pre>
	 * 
	 * @author 元杰
	 * @param page 分页类
	 * @param query 查询条件
	 * @param assessPlanId 评价计划ID
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<Defect> findCompanyDefectListbyPage(Page<Defect> page, String query, String assessPlanId, String improveId, String companyId, String dealStatus, String status){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Defect.class);
		if(StringUtils.isNotBlank(assessPlanId)){//ID为assessPlanId的评价计划关联的缺陷
			detachedCriteria.createAlias("assessRelaDefect", "ard", CriteriaSpecification.LEFT_JOIN);
			detachedCriteria.createAlias("ard.assessPlan", "ap", CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("ap.id", assessPlanId));
		}else if(StringUtils.isNotBlank(improveId)){//ID为improveId的整改计划关联的缺陷
			detachedCriteria.createAlias("defectRelaImprove", "dri", CriteriaSpecification.LEFT_JOIN);
			detachedCriteria.createAlias("dri.improve", "i").add(Restrictions.eq("i.id", improveId));
		}
		if(StringUtils.isNotBlank(query)){//模糊匹配缺陷的编号和名称
			detachedCriteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("desc").like(query, MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(companyId)){//添加本公司以及下级公司
			detachedCriteria.add(Restrictions.eq("company.id", companyId));
		}else{
			String companyIdOnline = UserContext.getUser().getCompanyid();
			List<SysOrganization> orgList = o_orgGridBO.findSubCompanyList(companyIdOnline);
			Set<String> orgIdSet = new HashSet<String>();
			for (SysOrganization org : orgList) {
				orgIdSet.add(org.getId());
			}
			orgIdSet.add(companyIdOnline);
			if(orgIdSet.size()>0){
				detachedCriteria.add(Restrictions.in("company.id", orgIdSet));
			}
		}
		if(StringUtils.isNotBlank(dealStatus)){
			detachedCriteria.add(Restrictions.eq("dealStatus", dealStatus));
		}
		if(StringUtils.isNotBlank(status)){
			detachedCriteria.add(Restrictions.eq("status", status));
		}
		detachedCriteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		return o_defectDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 *缺陷跟踪分页查询
	 * </pre>
	 * 
	 * @author 李克东
	 * @param page
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<DefectRelaImprove> findDefectRelaImproveListbyPage(Page<DefectRelaImprove> page){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DefectRelaImprove.class);
		detachedCriteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));		
		return o_defectRelaImproveDAO.findPage(detachedCriteria, page, false);
		
	}
	
	
	/**
	 * <pre>
	 *根据缺陷ID查询获得缺陷
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Defect findDefectById(String defectId){
		return o_defectDAO.get(defectId);
	}
	/**
	 * <pre>
	 * 查询缺陷.
	 * </pre>
	 * 
	 * @author 李克东
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Defect> findDefectFollowListByAll(){
		return o_defectDAO.getAll();
	}
	/**
	 * <pre>
	 * 查询整改计划
	 * </pre>
	 * 
	 * @author 李克东
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Improve> findImproveFollowListByAll(){
		return o_improveDAO.getAll();
	}
	/**
	 * <pre>
	 * 涉及缺陷组件
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectIds 缺陷id字符串，以","分隔
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String,Object>> findDefectByIds(String defectIds){
		String[] idArray=defectIds.split(",");
		Criteria criteria = o_defectDAO.createCriteria();
		criteria.add(Restrictions.in("id",idArray));
		List<Defect> defectList=criteria.list();
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Defect defect:defectList){
			Map<String, Object> mapBean = new HashMap<String, Object>();
			mapBean.put("id", defect.getId());
			mapBean.put("dbid", defect.getId());
			mapBean.put("code", defect.getCode());
			mapBean.put("text", defect.getDesc());
			returnList.add(mapBean);
		}
		return returnList;
		
	}
	/**
	 * <pre>
	 *根据缺陷跟踪ID获得缺陷
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectRelaImproveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public DefectRelaImprove findDefectRelaImprovebyId(String defectRelaImproveId){
		return o_defectRelaImproveDAO.get(defectRelaImproveId);
	}
	/**
	 * <pre>
	 *根据计划ID获得计划
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Improve findImproveById(String improveId){
		return o_improveDAO.get(improveId);
	}
	/**
	 * <pre>
	 *查询缺陷部门关联
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<DefectRelaOrg> findDefectRelaOrgById(String defectId){
		Criteria criteriaOrg=o_defectRelaOrgDAO.createCriteria();
		criteriaOrg.add(Restrictions.eq("defect.id", defectId));
		List<DefectRelaOrg> defectListOrg=criteriaOrg.list();
		return defectListOrg;
	}

	/**
	 * <pre>
	 * 查询所有缺陷
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Defect> findAllDefect() {
		return o_defectDAO.getAll();
	}
	
	/**
	 * 根据评价计划id查询按缺陷级别和部门分组查询缺陷信息.
	 * @param assessPlanId
	 * @param type 分组类别
	 * @return List<Object[]>
	 */
	public List<Object[]> findDefectRelaInfoByAssessPlanId(String assessPlanId, String type){
		List<Object[]> list = null;
		StringBuilder selectBuffer = new StringBuilder();
		StringBuilder fromBuffer = new StringBuilder();
		StringBuilder whereBuffer = new StringBuilder();
		StringBuilder groupByBuffer = new StringBuilder();
		StringBuilder orderByBuffer = new StringBuilder();
		
		if(StringUtils.isNotBlank(type)){
			if("level".equals(type)){
				selectBuffer.append("select de.dict_entry_name,count(d.id) ");
				
				groupByBuffer.append("group by de.dict_entry_name ");
				orderByBuffer.append("order by de.id ");
			}
			if("org".equals(type)){
				selectBuffer.append("select o.org_name,count(d.id) ");
				
				groupByBuffer.append("group by o.org_name ");
				orderByBuffer.append("order by o.id ");
			}
			
			fromBuffer.append("from t_ca_defect d ")
				.append("left join t_ca_defect_assessment da on d.id=da.defect_id ")
				.append("left join t_ca_defect_rela_org dro on d.id=dro.defect_id ")
				.append("left join t_sys_dict_entry de on d.defect_level=de.id ")
				.append("left join t_sys_organization o on dro.org_id=o.id ");
			
			if(StringUtils.isNotBlank(assessPlanId)){
				whereBuffer.append("where da.plan_id = :assessPlanId ");
			}
			
			SQLQuery sqlQuery = o_defectDAO.createSQLQuery(selectBuffer.append(fromBuffer).append(whereBuffer).append(groupByBuffer).append(orderByBuffer).toString());
			if(StringUtils.isNotBlank(assessPlanId)){
				sqlQuery.setString("assessPlanId", assessPlanId);
			}
			list = sqlQuery.list();
		}
		
		return list;
	}
	
	/**
	 * 根据companyId查询穿行测试和抽样测试的缺陷级别分组信息.
	 * @param companyId
	 * @return List<Object[]>
	 */
	public List<Object[]> findAssessMeasureTestListByCompanyId(String companyId){
		List<Object[]> list = null;
		/*
		select p.company_id,r.assessment_measure,
		sum(case when exists(select  a.id from t_ca_defect_assessment a where a.plan_id = r.plan_id and a.plan_id = p.id and p.execute_status='f' and a.point_id = r.point_id ) then 1 else 0 end)/count(r.id) control_point_id_count,
		sum(case when exists(select  a.id from t_ca_defect_assessment a where a.plan_id = r.plan_id and a.point_id = r.point_id ) then 1 else 0 end),
		count(r.id)
		from t_ca_assessment_result r left join t_ca_assessment_plan p on r.plan_id = p.id
		where  p.execute_status='f' and r.assessment_measure is not null 
		and p.company_id='xd02'
		group by p.company_id,r.assessment_measure;
		*/
		
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append("select r.assessment_measure, ")
			.append("sum(case when exists(select  a.id from t_ca_defect_assessment a where a.plan_id = r.plan_id and a.plan_id = p.id and p.execute_status='f' and a.point_id = r.point_id ) then 1 else 0 end)/count(r.id) control_point_id_count, ")
			.append("sum(case when exists(select  a.id from t_ca_defect_assessment a where a.plan_id = r.plan_id and a.point_id = r.point_id ) then 1 else 0 end), ")
			.append("count(r.id) ")
			.append("from t_ca_assessment_result r left join t_ca_assessment_plan p on r.plan_id = p.id ")
			.append("where  p.execute_status='f' and r.assessment_measure is not null ");
		
		if(StringUtils.isNotBlank(companyId)){
			sqlBuffer.append("and p.company_id=:companyId ");
		}
		
		sqlBuffer.append("group by r.assessment_measure ");
			
		SQLQuery sqlQuery = o_defectDAO.createSQLQuery(sqlBuffer.toString());
		if(StringUtils.isNotBlank(companyId)){
			list = sqlQuery.setString("companyId", companyId).list();
		}else{
			list = sqlQuery.list();
		}
		
		return list;
	}
	
	/**
	 * 根据companyId查询缺陷级别分组信息.
	 * @param companyId
	 * @param type 评价：assess，整改：improve
	 * @return List<Object[]>
	 */
	
	public List<Object[]> findDefectLevelListByCompanyId(String companyId,String type){
		List<Object[]> list = null;
		/*
		select p.company_id,de.dict_entry_name,count(d.id) 
		from t_ca_defect_assessment da left join t_ca_defect d on da.defect_id=d.id left join t_sys_dict_entry de on d.defect_level=de.id 
		left join t_ca_assessment_plan p on da.plan_id=p.id 
		where p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') and p.company_id='xd02'
		group by p.company_id,de.dict_entry_name
		*/
		
		StringBuilder sqlBuffer = new StringBuilder();
		if("assess".equals(type)){
			sqlBuffer.append("select de.dict_entry_name,count(d.id) ")
			.append("from t_ca_defect_assessment da left join t_ca_defect d on da.defect_id=d.id left join t_sys_dict_entry de on d.defect_level=de.id ")
			.append("left join t_ca_assessment_plan p on da.plan_id=p.id ")
			.append("where p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') ");
			if(StringUtils.isNotBlank(companyId)){
				sqlBuffer.append("and p.company_id=:companyId ");
			}
		}else if("improve".equals(type)){
			sqlBuffer.append("select de.dict_entry_name,count(d.id) ")
			.append("from t_rectify_defect_improve_plan da left join t_ca_defect d on da.defect_id=d.id left join t_sys_dict_entry de on d.defect_level=de.id ")
			.append("left join t_rectify_improve p on da.improve_plan_id=p.id ")
			.append("where p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') ");
			if(StringUtils.isNotBlank(companyId)){
				sqlBuffer.append("and p.company_id=:companyId ");
			}
		}
		
		
		sqlBuffer.append("group by de.dict_entry_name order by de.id ");
		SQLQuery sqlQuery = o_defectDAO.createSQLQuery(sqlBuffer.toString());
		if(StringUtils.isNotBlank(companyId)){
			list = sqlQuery.setString("companyId", companyId).list();
		}else{
			list = sqlQuery.list();
		}
		
		return list;
	}
	
	/**
	 * 根据companyId查询缺陷级别分组信息.
	 * @param companyId
	 * @param type 评价：assess，整改：improve
	 * @return List<Object[]>
	 */
	public List<Object[]> findOrgDefectListByCompanyId(String companyId,String type){
		List<Object[]> list = null;
		/*
		select p.company_id,o.org_name,count(d.id)  
		from t_ca_defect_rela_org dro left join t_sys_organization o on dro.org_id=o.id 
		left join t_ca_defect d on d.id=dro.defect_id 
		left join t_ca_defect_assessment da on d.id=da.defect_id left join t_ca_assessment_plan p on da.plan_id=p.id 
		where d.id in(select defect_id from t_ca_defect_assessment) and p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') 
		and p.company_id='xd02'
		group by p.company_id,o.org_name
		*/
		
		StringBuilder sqlBuffer = new StringBuilder();
		
		if("assess".equals(type)){
			sqlBuffer.append("select o.org_name,count(d.id) ")
			.append("from t_ca_defect_rela_org dro left join t_sys_organization o on dro.org_id=o.id ")
			.append("left join t_ca_defect d on d.id=dro.defect_id ")
			.append("left join t_ca_defect_assessment da on d.id=da.defect_id left join t_ca_assessment_plan p on da.plan_id=p.id ")
			.append("where d.id in(select defect_id from t_ca_defect_assessment) and p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') ");
			if(StringUtils.isNotBlank(companyId)){
				sqlBuffer.append("and p.company_id=:companyId ");
			}
		}else if("improve".equals(type)){
			sqlBuffer.append("select o.org_name,count(d.id) ")
			.append("from t_ca_defect_rela_org dro left join t_sys_organization o on dro.org_id=o.id ")
			.append("left join t_ca_defect d on d.id=dro.defect_id ")
			.append("left join t_rectify_defect_improve_plan da on d.id=da.defect_id left join t_rectify_improve p on da.improve_plan_id=p.id ")
			.append("where d.id in(select defect_id from t_rectify_defect_improve_plan) and p.estatus = 'p' and (p.execute_status='f' or p.execute_status='a') ");
			if(StringUtils.isNotBlank(companyId)){
				sqlBuffer.append("and p.company_id=:companyId ");
			}
		}
		
		
		
		sqlBuffer.append("group by o.org_name order by o.id ");
			
		SQLQuery sqlQuery = o_defectDAO.createSQLQuery(sqlBuffer.toString());
		if(StringUtils.isNotBlank(companyId)){
			list = sqlQuery.setString("companyId", companyId).list();
		}else{
			list = sqlQuery.list();
		}
		
		return list;
	}
}