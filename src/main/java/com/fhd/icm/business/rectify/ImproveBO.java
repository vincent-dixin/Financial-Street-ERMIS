package com.fhd.icm.business.rectify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.defect.DefectRelaImproveDAO;
import com.fhd.icm.dao.rectify.ImproveDAO;
import com.fhd.icm.dao.rectify.ImproveFileDAO;
import com.fhd.icm.dao.rectify.ImprovePlanDAO;
import com.fhd.icm.dao.rectify.ImprovePlanFileDAO;
import com.fhd.icm.dao.rectify.ImproveRelaOrgDAO;
import com.fhd.icm.dao.rectify.ImproveRelaProcessDAO;
import com.fhd.icm.dao.rectify.ImproveTraceDAO;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.icm.entity.rectify.ImprovePlan;
import com.fhd.icm.entity.rectify.ImprovePlanFile;
import com.fhd.icm.entity.rectify.ImproveRelaFile;
import com.fhd.icm.entity.rectify.ImproveRelaOrg;
import com.fhd.icm.entity.rectify.ImproveRelaProcess;
import com.fhd.icm.entity.rectify.ImproveTrace;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.dao.file.FileUploadDAO;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 整改计划的业务类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-5-6		下午1:31:04
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ImproveBO{
	@Autowired
	private ImproveDAO o_improveDAO;
	
	@Autowired
	private ImproveTraceDAO o_improveTraceDAO; 
	
	@Autowired
	private ImproveRelaOrgDAO o_improveRelaOrgDAO;
	@Autowired
	private ImproveFileDAO o_improveFileDAO;
	@Autowired
	private ImproveRelaProcessDAO o_improveRelaProcessDAO;
	@Autowired
	private DefectRelaImproveDAO o_defectRelaImproveDAO;
	@Autowired
	private ImprovePlanDAO o_improvePlanDAO;
	@Autowired
	private ImprovePlanFileDAO o_improvePlanFileDAO;
	@Autowired
	private FileUploadDAO o_fileUploadDAO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	/**
	 * <pre>
	 * 保存整改计划
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improve 要保存的整改计划
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprove(Improve improve){
		o_improveDAO.merge(improve);
	}
	
	/**
	 * <pre>
	 *批量修改整改方案 开始结束日期
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @param jsonString
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlan(String improvePlanId,String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//ImprovePlan的ID
			String content = jsonObject.getString("content");
			ImprovePlan improvePlan = new ImprovePlan();
			improvePlan.setId(id);
			improvePlan.setContent(content);
			improvePlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
			o_improvePlanDAO.merge(improvePlan);
		}
		
	}
	
	/**
	 * <pre>
	 *保存整改方案附件关联 
	 * </pre>
	 * 
	 * @author 李克东
	 * @param fileId
	 * @param improvePlanId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlanFile(String fileId, String improvePlanId){
		removeImprovePlanFile(improvePlanId);
		ImprovePlanFile improvePlanFile = new ImprovePlanFile();
		improvePlanFile.setFile(new FileUploadEntity(fileId));
		ImprovePlan improvePlan = new ImprovePlan();
		improvePlan.setId(improvePlanId);
		improvePlanFile.setImprovePlan(improvePlan);
		improvePlanFile.setId(Identities.uuid());
		o_improvePlanFileDAO.merge(improvePlanFile);
	}
	
	/**
	 * <pre>
	 *删除整改方案附件关联
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improvePlanId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeImprovePlanFile(String improvePlanId){
		o_improvePlanFileDAO.createQuery("delete ImprovePlanFile improvePlanFile where improvePlanFile.improvePlan.id=:improvePlanId")
		.setString("improvePlanId", improvePlanId).executeUpdate();
	}
	/**
	 * <pre>
	 *逻辑删除整改计划
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveIds
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeImproveByIdBatch(String improveIds){
		if(StringUtils.isNotBlank(improveIds)){
			String[] improveIdArray = improveIds.split(",");
			StringBuffer hql = new StringBuffer();
			hql.append("update Improve improve set improve.deleteStatus=:deleteStatus");
			hql.append(" where improve.id in('");
			for (int i = 0; i<improveIdArray.length; i++) {
				if(i==improveIdArray.length-1){
					hql.append(improveIdArray[i]).append("')");
				}else{
					hql.append(improveIdArray[i]).append("','");
				}
			}
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("deleteStatus", Contents.DELETE_STATUS_DELETED);
			o_improveDAO.batchExecute(hql.toString(), values);
		}
	}
	
	/**
	 * <pre>
	 *分页查询
	 * </pre>
	 * 
	 * @author 李克东
	 * @param page
	 * @param query
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<Improve> findImproveListbyPage(Page<Improve> page, String query, String companyId, String dealStatus){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Improve.class);
		
		if(StringUtils.isNotBlank(query)){//模糊匹配缺陷的编号和名称
			detachedCriteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(companyId)){
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
			detachedCriteria.add(Restrictions.in("dealStatus", dealStatus.split(",")));
		}
		detachedCriteria.addOrder(Order.desc("createTime"));
		detachedCriteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		return o_improveDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 *整改方案分页查询
	 * </pre>
	 * 
	 * @author 李克东
	 * @param page
	 * @param query
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<ImprovePlan> findImprovePlanListbyPage(Page<ImprovePlan> page,String query){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ImprovePlan.class);
		detachedCriteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		return o_improvePlanDAO.findPage(detachedCriteria,page,false);
	}
	
	
	
	/**
	 * <pre>
	 *整改跟踪分页查询
	 * </pre>
	 * 
	 * @author 李克东
	 * @param page
	 * @param query
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<ImproveTrace> findImproveTraceListbyPage(Page<ImproveTrace> page){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ImproveTrace.class);
		detachedCriteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		return o_improveTraceDAO.findPage(detachedCriteria, page,false);
	}
	

	/**
	 * <pre>
	 *加载整改计划跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String, Object> findImproveForview(String improveId) {
		Map<String, Object> improveMap = new HashMap<String, Object>();
		//Improve improve=o_improveBO.findImproveAdviceById(improveId);
		Map<String, Object> map=new HashMap<String,Object>();
		Criteria criteria = o_improveDAO.createCriteria();
		criteria.add(Restrictions.eq("id", improveId));
		Improve improve=(Improve)criteria.uniqueResult();
		improveMap.put("improveScenario", improve.getImproveScenario());
		Criteria fileCriteria=o_improveFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("improve.id", improve.getId()));
		List<ImproveRelaFile> fileList=fileCriteria.list();
		if(fileList.size()>0){
			//String fileIds=fileList.get(0).getFile().getId();
			//formMap.put("fileIds", fileIds);
			String fileIds= "";
			for(ImproveRelaFile improveRelaFile:fileList){
				String fileId=improveRelaFile.getFile().getId();
				fileIds=fileIds+","+fileId;
			}
			improveMap.put("file", fileIds);
		}
		map.put("data", improveMap);
		map.put("success", true);
	return map;
	}
	/**
	 * <pre>
	 *通过整改计划ID获得整改计划
	 * </pre>
	 * 
	 * @author 李克东
	 * @param page
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Improve findImproveById(String improveId){
		return o_improveDAO.get(improveId);
	}
	
	/**
	 * <pre>
	 *查询缺陷整改计划关联
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<DefectRelaImprove> findDefectRelaImprove(String improveId){
		Criteria criteria = o_defectRelaImproveDAO.createCriteria();
		criteria.add(Restrictions.eq("improve.id", improveId));
		List<DefectRelaImprove> defectRelaImprove = criteria.list();
		return defectRelaImprove;
	}
	/**
	 * <pre>
	 *查询整改计划部门关联
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImproveRelaOrg> findImproveRelaOrgById(String improveId){
		Criteria criteriaOrg=o_improveRelaOrgDAO.createCriteria();
		criteriaOrg.add(Restrictions.eq("improve.id", improveId));
		List<ImproveRelaOrg> improveListOrg=criteriaOrg.list();
		return improveListOrg;
	}
	
	/**
	 * <pre>
	 *查询流程关联
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImproveRelaProcess> findImproveRelaProcessById(String improveId){
		Criteria criteriaProcess=o_improveRelaProcessDAO.createCriteria();
		criteriaProcess.add(Restrictions.eq("improve.id", improveId));
		List<ImproveRelaProcess> improveRelaProcess = criteriaProcess.list();
		return improveRelaProcess;
	}
	/**
	 * <pre>
	 *查询缺陷关联整改计划
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public DefectRelaImprove findDefectRelaImproveById(String improveId){
		return o_defectRelaImproveDAO.get(improveId);
	}
	/**
	 * <pre>
	 *查询整改方案关联附件
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improvePlanId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImprovePlanFile> findImprovePlan(String improvePlanId){
		Criteria criteria = o_improvePlanFileDAO.createCriteria();
		criteria.add(Restrictions.eq("improvePlan.id", improvePlanId));
		List<ImprovePlanFile> improvePlanFileList = criteria.list();
		return improvePlanFileList;
	}
	/**
	 * <pre>
	 *查询上报方案附件
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improvePlanIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<FileUploadEntity> findFileUploadEntity(String improvePalnId){
		Criteria criteriafile = o_fileUploadDAO.createCriteria();
		criteriafile.add(Restrictions.eq("id", improvePalnId));
		List<FileUploadEntity> fileUploadEntityList = criteriafile.list();
		return fileUploadEntityList;
	}
	
	
	
}