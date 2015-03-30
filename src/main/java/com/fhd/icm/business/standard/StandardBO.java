package com.fhd.icm.business.standard;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.entity.JbpmHistProcinst;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.StandardBpmBO;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.dao.standard.StandardRelaOrgDAO;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.entity.standard.StandardRelaProcessure;
import com.fhd.icm.interfaces.standard.IStandardBO;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.controller.bpm.StandardBpmObject;
import com.fhd.icm.web.form.StandardForm;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * Standard_内控BO ClassName:StandardBO
 * 
 * @author 刘中帅
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-12-11 上午10:21:00
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class StandardBO implements IStandardBO {

	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private StandardRelaOrgBO o_standardRelaOrgBO;
	@Autowired
	private StandardRelaOrgDAO o_standardRelaOrgDAO;
	@Autowired
	private SysOrganizationDAO o_sysOrgnizationDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private StandardRelaProcessureBO o_standardRelaProcessureBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private StandardBpmBO o_standardBpmBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	/**
	 * <pre>
	 * 根据内控ID获得指标实体
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param id
	 * 内控ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public Standard findStandardById(String standardId) {
		Criteria criteria = o_standardDAO.createCriteria();
		criteria.add(Restrictions.eq("id", standardId));
		Standard standard = (Standard) criteria.uniqueResult();
		return standard;
	}
	/**
	 * <pre>
	 * 根据内控ID集合获得指标实体
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param id
	 * 内控ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String,Object>> findStandardByIds(String[] standardIds) {
		List<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
		Criteria criteria = o_standardDAO.createCriteria();
		criteria.add(Restrictions.in("id", standardIds));
		List<Standard> standardList =criteria.list();
		for(Standard standard:standardList){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", standard.getId());
			map.put("dbid", standard.getId());
			map.put("code", standard.getCode());
			map.put("text", standard.getName());
			listMap.add(map);
		}
		return listMap;
	}
	/**
	 * 根据传过来的id集合查找其下面的子节点并且etype等于0的
	 * 
	 * @param clickedNodeId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findStandardListByPage(Page<Standard> page, String query,String clickedNodeIds,
			String isLeaf,String companyId) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Standard.class);
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		String[] ids;
		if (StringUtils.isNotBlank(clickedNodeIds)) {
			ids = clickedNodeIds.split("\\,");
			criteria.add(Restrictions.eq("type", "0"));
			criteria.add(Restrictions.in("parent.id", ids));
		} else {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		List<Standard> standardList =o_standardDAO.findPage(criteria, page, false).getResult();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Standard standard : standardList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("controlRequirement", standard.getControlRequirement());
			if(StringUtils.isNotBlank(standard.getStatus())){
				map.put("status", standard.getStatus());
			}
			if(StringUtils.isNotBlank(standard.getDealStatus())){
				map.put("dealStatus", standard.getDealStatus());
			}
			if(null!=standard.getStandardRelaOrg()&&standard.getStandardRelaOrg().size()>0){
				String orgId="";
				for(StandardRelaOrg standardRelaOrg:standard.getStandardRelaOrg()){
					orgId=standardRelaOrg.getOrg().getOrgname();
				}
				map.put("dept",orgId);
			}
			String controPoint = "";
			if(null!=standard.getControlPoint()){
				 controPoint = o_dictEntryDAO.get(standard.getControlPoint()).getName();
			}
			map.put("idSeq", standard.getIdSeq());
			map.put("code", standard.getCode());
			map.put("text", standard.getName());
			map.put("name", standard.getName());
			map.put("controlPoint", controPoint);
			map.put("id", standard.getId());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", standardList.size());

		return result;
	}

	/**
	 * 返回内控标准列表
	 * @param 
	 * @author 元杰
	 * @return map 集合
	 */
	public Map<String, Object> findStandardBpmListByPage(Page<Standard> page, String standardName, String companyId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Standard.class);
		if(StringUtils.isNotBlank(standardName)){
			criteria.add(Restrictions.like("name", standardName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(companyId)) {
			criteria.add(Restrictions.eq("companyId", companyId));
		}
		//过滤内控标准
		criteria.add(Restrictions.eq("type", Contents.STANDARD_TYPE_CLASS));
		criteria.addOrder(Order.desc("createTime"));
		Map<String, Object> result = new HashMap<String, Object>();
		List<Standard> standardList =o_standardDAO.findPage(criteria, page, false).getResult();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Standard standard : standardList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("controlRequirement", standard.getControlRequirement());
			if(standard.getStatus()!=null){
				map.put("status", standard.getStatus());
			}
			if(standard.getDealStatus()!=null){
				map.put("dealstatus", standard.getDealStatus());
			}
			//实际进度
			JbpmHistProcinst jhp = o_jbpmBO.findJbpmHistProcinstByBusinessId(standard.getId());
			if(null != jhp){
				Integer processPercent = o_jbpmBO.processRateOfProgress(jhp.getId_());
				map.put("actualProgress", processPercent);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(null != standard.getCreateTime()){
				map.put("createTime", sdf.format(standard.getCreateTime()));
			}else{
				map.put("createTime", "");
			}
			map.put("code", standard.getCode());
			map.put("name", standard.getName());
			map.put("id", standard.getId());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", standardList.size());

		return result;
	}
	
	/**
	 * <pre>
	 * 根据内控标识获取所有下级内控
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param id内控标识
	 * @param self是否包含自己
	 * @Data 2012 12 11 上午10:15
	 * @return 包含Standard实体的集合
	 * @since fhd　Ver 1.1
	 */
	public List<Standard> findChildsStandardBySome(String id, String type,
			boolean self) {
		List<Standard> standardList = new ArrayList<Standard>();
		if (StringUtils.isNotBlank(id)) {
			Criteria criteria = this.o_standardDAO.createCriteria();
			if (self) {// 包含自己
				criteria.add(Restrictions.or(
						Property.forName("parent.id").eq(id),
						Property.forName("id").eq(id)));
			} else {
				criteria.add(Property.forName("parent.id").eq(id));
				if (StringUtils.isNotEmpty(type)) {
					criteria.add(Property.forName("type").eq(type));
				}
			}
			standardList = criteria.list();
		}
		return standardList;
	}

	/**
	 * <pre>
	 *通过id查询子节点
	 * </pre>
	 * @author 刘中帅
	 * @param id 内控标准id
	 * @param type 标准类型
	 * @return
	 * @since  fhd　Ver 1.2
	 * @editor 增加判断条件  type为1才增加父ID 为null查询的条件
	 */
	public List<Standard> findChildStandardById(String id,String companyId) {

		List<Standard> standardList = new ArrayList<Standard>();
		Criteria criteria = this.o_standardDAO.createCriteria();
		if (StringUtils.isNotEmpty(id)) {
			criteria.add(Restrictions.eq("parent.id", id));
		} else {
				criteria.add(Restrictions.isNull("parent.id"));
		}
		criteria.add(Restrictions.eq("companyId", companyId));
		criteria.add(Restrictions.eq("type", "1")); 
		criteria.addOrder(Order.asc("name"));
		standardList = criteria.list();
		return standardList;
	}

	/**
	 * 通过递归判断当前节点（非子节点）下的叶子节点是否匹配查询条件
	 * 
	 * @param id
	 *            当前选中节点的id
	 * @param query
	 *            查询条件
	 * @author 刘中帅
	 * @return 符合条件的子节点集合
	 */
	public List<Standard> findStandardById(String id, String query,
			List<Standard> standardResult, String companyId,
			List<Standard> standardAllList) {

		List<Standard> childList = new ArrayList<Standard>();
		// 查询子节点
		for (Standard standard : standardAllList) {
			if (null != standard.getParent()) {
				if (StringUtils.isNotBlank(standard.getParent().getId()))
					if (standard.getParent().getId().equals(id)) {
						childList.add(standard);
					}
			}
		}
		for (Standard standard : childList) {

			if (null!=query && standard.getName().indexOf(query) != -1) {
				standardResult.add(standard);
			} else {
				findStandardById(standard.getId(), query, standardResult,
						companyId, standardAllList);
			}
		}
		return standardResult;
	}
	
	/**
	 * <pre>
	 * 通过部门id查询内控标准
	 * </pre>
	 * @author 元杰
	 * @param deptId 部门ID
	 * @param type 标准类型
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Standard> findStandardsByDeptId(String deptId, String type, String companyId) {
		Criteria criteria = this.o_standardDAO.createCriteria();
		if (StringUtils.isNotBlank(deptId)){
			criteria.createAlias("standardRelaOrg.org", "sro");
			criteria.add(Restrictions.eq("sro.id", deptId));
		}
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("companyId", companyId));
		}
		if(StringUtils.isNotBlank(type)){
			criteria.add(Restrictions.eq("type", type));
		}
		return criteria.list();
	}
	
	/**
	 *   保存内控标准和要求的审批意见
	 * @author 元杰
	*/
	@Transactional
	public void saveStandardAdvice(StandardForm standardForm){
		if(StringUtils.isNotBlank(standardForm.getId())){
			Standard standard = o_standardDAO.get(standardForm.getId());
			standard.setFeedback(standardForm.getFeedback());
			o_standardDAO.merge(standard);
		}
	}
	
	/**
	 *   保存内控标准以及下属的内控要求
	 * @author 元杰
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @param step 五步中的第几部
	*/
	@SuppressWarnings("deprecation")
	@Transactional
	public Standard saveStandard(String executionId, String businessId, StandardForm standardForm, String step) throws IllegalAccessException, InvocationTargetException{
		Standard standard = null;
		if(StringUtils.isNotBlank(standardForm.getId())){//修改
			standard = o_standardDAO.get(standardForm.getId());
		}else{//新增
			standard = new Standard();
		}
		if("3".equals(step)){//第3步只保存要求对应的意见
			//保存要求信息  
	        JSONArray standardControlForm=JSONArray.fromObject(standardForm.getStandardControlFormsStr());
			if(standardControlForm != null && standardControlForm.size() > 0){   //
				for(int i = 0 ;i < standardControlForm.size(); i++){
					JSONObject jsonObject = standardControlForm.getJSONObject(i);
					//只保存内控要求对应的反馈意见
					if(StringUtils.isNotBlank(jsonObject.getString("cid"))){
						String standardControlId = jsonObject.getString("cid");
						Standard standardControl = o_standardDAO.get(standardControlId);
						String feedback = jsonObject.getString("cfeedback");
						standardControl.setFeedback(feedback);
						o_standardDAO.merge(standardControl);
					}
				}
			}
			o_standardBpmBO.startCurCompanyStandardApplyStepThree(executionId, businessId, standard); //触发本公司流程
		}else if("4".equals(step)){
			//保存要求信息  
	        JSONArray standardControlForm=JSONArray.fromObject(standardForm.getStandardControlFormsStr());
			if(standardControlForm != null && standardControlForm.size() > 0){   //
				for(int i = 0 ;i < standardControlForm.size(); i++){
					JSONObject jsonObject = standardControlForm.getJSONObject(i);
					String isSubCompany = jsonObject.getString("inferior");//是否适用于下级机构
					if(Contents.DICT_Y.equals(isSubCompany)){//是
						jsonObject.getString("csubCompanyId");
					}else if(Contents.DICT_N.equals(isSubCompany)){//否
						Standard standardControl = null;
						String standardControlId = "";
						if(StringUtils.isNotBlank(jsonObject.getString("cid"))){
							standardControlId = jsonObject.getString("cid");
							standardControl = o_standardDAO.get(standardControlId);
						}else{
							standardControl = new Standard();
							standardControl.setId(Identities.uuid2());
						}
						standardControl.setCode(this.findStandardCode());
						standardControl.setName(jsonObject.getString("cname"));
						standardControl.setIsLeaf(true);
						standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
//						standardControl.setCompanyId(UserContext.getUser().getCompanyid());
						standardControl.setParent(standard);
						String controlPoint = jsonObject.getString("cstandardControlPoint");
						standardControl.setControlPoint(controlPoint);
						if(jsonObject.has("statusId")){
							standardControl.setDealStatus(jsonObject.getString("statusId"));//更新内控要求的处理状态
						}
						o_standardDAO.merge(standardControl);
						//保存要求关联部门
						String deptId = IcmStandardUtils.findIdbyJason(jsonObject.getString("cdeptId"), "id");
						if(StringUtils.isNotBlank(deptId)){
							o_standardRelaOrgBO.delStandardRelaOrgByStandardId(standardControlId);
							StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
							standardRelaOrg.setId(Identities.uuid2());
							standardRelaOrg.setStandard(standardControl);
							standardRelaOrg.setOrg(o_sysOrgnizationDAO.get(deptId));
							standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
							o_standardRelaOrgDAO.merge(standardRelaOrg);
						}
						//保存要求相关流程
						if(StringUtils.isNotBlank(jsonObject.getString("cprocessId"))){
							o_standardRelaProcessureBO.delStandardRelaProcessureByStandardId(standardControlId);
							StandardRelaProcessure standardRelaProcessure = new StandardRelaProcessure();
							standardRelaProcessure.setId(Identities.uuid2());
							standardRelaProcessure.setStandard(standardControl);
							standardRelaProcessure.setProcessure(o_processDAO.get(jsonObject.getString("cprocessId")));
							o_standardRelaProcessureBO.saveStandardRelaProcessure(standardRelaProcessure);
						}
					}
				}
			}
			o_standardBpmBO.startCurCompanyStandardApplyStepFour(executionId, businessId, standard); //触发本公司流程
		}else{
			standard.setControlLevel(standardForm.getControlLevel());
			//编号，如果表单中code为空，则自动生成
			if(StringUtils.isNotBlank(standardForm.getCode())){
				standard.setCode(standardForm.getCode());
			}else{
				standard.setCode(this.findStandardCode());
			}
			standard.setName(standardForm.getName());
			standard.setStatus(Contents.STATUS_SUBMITTED);
			standard.setDealStatus(Contents.DEAL_STATUS_HANDLING);
			standard.setUpdateDeadline(standardForm.getUpdateDeadline());
			standard.setCompanyId(UserContext.getUser().getCompanyid());
			standard.setIsLeaf(false);
			standard.setLevel(1);
			standard.setType(Contents.STANDARD_TYPE_CLASS);//先不保存，判断如果有本级机构的要求时再保存
			if(null != standardForm.getParent() && StringUtils.isNotBlank(standardForm.getParent().getId())){
				standard.setParent(standardForm.getParent());
			}

			//保存要求信息  
	        JSONArray standardControlForm=JSONArray.fromObject(standardForm.getStandardControlFormsStr());
	        Set<String> subCompanyIdSet = new HashSet<String>();//用于存储使用与下级机构的要求ID集合
	        boolean hasSubFlag = false; //是否含有适用于下级机构的要求
	        boolean curCompanyFlag = false; //是否含有适用于本公司的要求
	        List<Standard> standardControlList = new ArrayList<Standard>();//存放针对下级机构的内控要求，在saveSubCompanyStandards方法中进行数据的写入
	        
	        //先循环一遍，确定此标准是否是只针对下级公司
			if(standardControlForm != null && standardControlForm.size() > 0){ 
				for(int i = 0 ;i < standardControlForm.size(); i++){
					JSONObject jsonObject = standardControlForm.getJSONObject(i);
					String isSubCompany = jsonObject.getString("inferior");//是否适用于下级机构
					if(Contents.DICT_N.equals(isSubCompany)){//针对本公司
						curCompanyFlag = true;
					}
				}
			}
			
			if(!curCompanyFlag){//此标准下挂要求全都针对下级公司，没有针对本级公司的，此情况不保存本机公司的保准和要求数据
				//保存数据
				if(standardControlForm != null && standardControlForm.size() > 0){ 
					for(int i = 0 ;i < standardControlForm.size(); i++){
						JSONObject jsonObject = standardControlForm.getJSONObject(i);
						String isSubCompany = jsonObject.getString("inferior");//是否适用于下级机构
						if(Contents.DICT_Y.equals(isSubCompany)){//是
							hasSubFlag = true;//是否需要下发下级机构的标示
							
							String subCompanyIdJson = jsonObject.getString("csubCompanyId");
							String subCompanyIdStr = IcmStandardUtils.findIdbyJason(subCompanyIdJson, "id");
							String[] subCompanyIds = subCompanyIdStr.split(",");
							for(String subCompanyId : subCompanyIds){
								subCompanyIdSet.add(subCompanyId);//去重后的下级机构IdSet
								
								//保存针对下级机构的内控要求
								Standard standardControl = new Standard();
								String standardControlId = Identities.uuid2();
								standardControl.setId(standardControlId);
								standardControl.setCode(this.findStandardCode());
								standardControl.setName(jsonObject.getString("cname"));
								standardControl.setIsLeaf(false);
								standardControl.setLevel(2);
								standardControl.setIdSeq(standard.getIdSeq() + standardControlId +  ".");
								standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
								standardControl.setStatus(Contents.STATUS_SAVED);
								standardControl.setCompanyId(subCompanyId);
								standardControl.setParent(standard);
								standardControlList.add(standardControl);
							}
						}else if(Contents.DICT_N.equals(isSubCompany)){//否
							
						}else if("adviceOnly".equals(isSubCompany)){
							if("5".equals(step)){
								o_standardBpmBO.startCurCompanyStandardApplyStepFive(executionId, businessId, null, null); //触发本公司流程
							}
						}
					}
				}
				if(hasSubFlag){
					List<String> standardIdList = saveSubCompanyStandards(subCompanyIdSet, standard, standardControlList);
					for(String sid : standardIdList){
						o_standardBpmBO.startSubCompanyStandardApply(sid, subCompanyIdSet);//触发子公司流程
					}
					if(StringUtils.isNotBlank(standard.getId())){//全部针对下级机构，则删除本级
						this.delStandardAndSub(standard);
					}
				}
			}else{//此情况包括全是本级机构要求和本级、下级混合的情况
				String standardId = Identities.uuid2();
				if(StringUtils.isBlank(standard.getId())){
					standard.setId(standardId);
				}else{
					standardId = standard.getId();
				}
				if(null != standardForm.getParent() && StringUtils.isNotBlank(standardForm.getParent().getId())){
					standard.setIdSeq(standardForm.getParent().getIdSeq() + standardId + ".");
				}else{
					standard.setIdSeq("." + standardId + ".");
				}
				o_standardDAO.merge(standard);
				
				//保存内控标准关联部门
				if(StringUtils.isNotBlank(UserContext.getUser().getMajorDeptId())){
					o_standardRelaOrgBO.delStandardRelaOrgByStandardId(standard.getId());
					StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
					standardRelaOrg.setId(Identities.uuid2());
					standardRelaOrg.setStandard(standard);
					standardRelaOrg.setOrg(o_sysOrgnizationDAO.get(UserContext.getUser().getMajorDeptId()));
					standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
					o_standardRelaOrgDAO.merge(standardRelaOrg);
				}

				//保存数据
				if(standardControlForm != null && standardControlForm.size() > 0){ 
					for(int i = 0 ;i < standardControlForm.size(); i++){
						JSONObject jsonObject = standardControlForm.getJSONObject(i);
						String isSubCompany = jsonObject.getString("inferior");//是否适用于下级机构
						if(Contents.DICT_Y.equals(isSubCompany)){//是
							hasSubFlag = true;//是否需要下发下级机构的标示
							
							String subCompanyIdJson = jsonObject.getString("csubCompanyId");
							String subCompanyIdStr = IcmStandardUtils.findIdbyJason(subCompanyIdJson, "id");
							String[] subCompanyIds = subCompanyIdStr.split(",");
							for(String subCompanyId : subCompanyIds){
								subCompanyIdSet.add(subCompanyId);//去重后的下级机构IdSet
								
								//保存针对下级机构的内控要求
								Standard standardControl = new Standard();
								String standardControlId = Identities.uuid2();
								standardControl.setId(standardControlId);
								standardControl.setCode(this.findStandardCode());
								standardControl.setName(jsonObject.getString("cname"));
								standardControl.setIsLeaf(false);
								standardControl.setLevel(2);
								standardControl.setIdSeq(standard.getIdSeq() + standardControlId +  ".");
								standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
								standardControl.setStatus(Contents.STATUS_SAVED);
								standardControl.setCompanyId(subCompanyId);
								standardControl.setParent(standard);
								standardControlList.add(standardControl);
							}
						}else if(Contents.DICT_N.equals(isSubCompany)){//否
							curCompanyFlag = true;
							Standard standardControl = null;
							String standardControlId = "";
							if(StringUtils.isNotBlank(jsonObject.getString("cid"))){
								standardControlId = jsonObject.getString("cid");
								standardControl = o_standardDAO.get(standardControlId);
							}else{
								standardControl = new Standard();
								standardControlId = Identities.uuid2();
								standardControl.setId(standardControlId);
							}
							standardControl.setCode(this.findStandardCode());
							standardControl.setName(jsonObject.getString("cname"));
							standardControl.setIsLeaf(true);
							standardControl.setLevel(2);
							standardControl.setIdSeq(standard.getIdSeq() + standardControlId +  ".");
							standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
							standardControl.setStatus(Contents.STATUS_SUBMITTED);
							standardControl.setCompanyId(UserContext.getUser().getCompanyid());
							standardControl.setParent(standard);
							if(jsonObject.has("cstandardControlPoint")){
								String controlPoint = jsonObject.getString("cstandardControlPoint");
								standardControl.setControlPoint(controlPoint);
							}
							o_standardDAO.merge(standardControl);
							//保存要求关联部门
							String deptId = IcmStandardUtils.findIdbyJason(jsonObject.getString("cdeptId"), "id");
							if(StringUtils.isNotBlank(deptId)){
								o_standardRelaOrgBO.delStandardRelaOrgByStandardId(standardControlId);
								StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
								standardRelaOrg.setId(Identities.uuid2());
								standardRelaOrg.setStandard(standardControl);
								standardRelaOrg.setOrg(o_sysOrgnizationDAO.get(deptId));
								standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
								o_standardRelaOrgDAO.merge(standardRelaOrg);
							}
							//保存要求相关流程
							if(StringUtils.isNotBlank(jsonObject.getString("cprocessId"))){
								o_standardRelaProcessureBO.delStandardRelaProcessureByStandardId(standardControlId);
								StandardRelaProcessure standardRelaProcessure = new StandardRelaProcessure();
								standardRelaProcessure.setId(Identities.uuid2());
								standardRelaProcessure.setStandard(standardControl);
								standardRelaProcessure.setProcessure(o_processDAO.get(jsonObject.getString("cprocessId")));
								o_standardRelaProcessureBO.saveStandardRelaProcessure(standardRelaProcessure);
							}
						}else if("adviceOnly".equals(isSubCompany)){
							if("5".equals(step)){
								o_standardBpmBO.startCurCompanyStandardApplyStepFive(executionId, businessId, null, null); //触发本公司流程
							}
						}
					}
				}
				if(hasSubFlag){
					List<String> standardIdList = saveSubCompanyStandards(subCompanyIdSet, standard, standardControlList);
					for(String sid : standardIdList){
						o_standardBpmBO.startSubCompanyStandardApply(sid, subCompanyIdSet);//触发子公司流程
					}
				}
				if(curCompanyFlag){//第一步
					o_standardBpmBO.startCurCompanyStandardApplyStepOne(executionId, businessId, standard); //触发本公司流程
				}
				//删除subCompanyids有数据的，是standard孩子节点的要求
				List<Standard> standardControlDeleteList = new ArrayList<Standard>();
				Criteria criteria = this.o_standardDAO.createCriteria();
				String parentId = standard.getId();
				if (StringUtils.isNotBlank(parentId)) {
					criteria.add(Restrictions.eq("parent.id", parentId));
				}
				criteria.add(Restrictions.isNotNull("subCompanyids"));
				standardControlDeleteList = criteria.list();
				for(Standard standardControl : standardControlDeleteList){
					o_standardDAO.delete(standardControl.getId());
				}
				
			}
			
		}
		return standard;
	}
	/**
	 * 仅保存内控标准，不触发工作流
	 * @author 元杰
	*/
	@SuppressWarnings("deprecation")
	@Transactional
	public Standard saveStandardData(StandardForm standardForm) {
		Standard standard =  new Standard();
		standard.setControlLevel(standardForm.getControlLevel());
		standard.setId(standardForm.getId());
		//编号，如果表单中code为空，则自动生成
		if(StringUtils.isNotBlank(standardForm.getCode())){
			standard.setCode(standardForm.getCode());
		}else{
			standard.setCode(this.findStandardCode());
		}
		standard.setName(standardForm.getName());
		standard.setStatus(Contents.STATUS_SAVED);
		standard.setDealStatus(Contents.DEAL_STATUS_NOTSTART);
		standard.setUpdateDeadline(standardForm.getUpdateDeadline());
		String standardId = Identities.uuid2();
		if(StringUtils.isBlank(standard.getId())){
			standard.setId(standardId);
		}else{
			standardId = standard.getId();
		}
		standard.setIsLeaf(false);
		standard.setLevel(1);
		if(null != standardForm.getParent() && StringUtils.isNotBlank(standardForm.getParent().getId())){
			standard.setParent(standardForm.getParent());
			standard.setIdSeq(standardForm.getParent().getIdSeq() + standardId + ".");
		}else{
			standard.setIdSeq("." + standardId + ".");
		}
		standard.setCompanyId(UserContext.getUser().getCompanyid());
		standard.setType(Contents.STANDARD_TYPE_CLASS);
		o_standardDAO.merge(standard);
		//保存内控标准关联部门
		if(StringUtils.isNotBlank(UserContext.getUser().getMajorDeptId())){
			o_standardRelaOrgBO.delStandardRelaOrgByStandardId(standard.getId());
			StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
			standardRelaOrg.setId(Identities.uuid2());
			standardRelaOrg.setStandard(standard);
			standardRelaOrg.setOrg(o_sysOrgnizationDAO.get(UserContext.getUser().getMajorDeptId()));
			standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
			o_standardRelaOrgDAO.merge(standardRelaOrg);
		}
		//保存要求信息  
        JSONArray standardControlForm=JSONArray.fromObject(standardForm.getStandardControlFormsStr());
		if(standardControlForm != null && standardControlForm.size() > 0){ 
			for(int i = 0 ;i < standardControlForm.size(); i++){
				JSONObject jsonObject = standardControlForm.getJSONObject(i);
				String isSubCompany = jsonObject.getString("inferior");//是否适用于下级机构
				if(Contents.DICT_Y.equals(isSubCompany)){//是
					Standard standardControl = null;
					String standardControlId = "";
					if(StringUtils.isNotBlank(jsonObject.getString("cid"))){
						standardControlId = jsonObject.getString("cid");
						standardControl = o_standardDAO.get(standardControlId);
					}else{
						standardControl = new Standard();
						standardControlId = Identities.uuid2();
						standardControl.setId(standardControlId);
					}
					String subCompanyIds = IcmStandardUtils.findIdbyJason(jsonObject.getString("csubCompanyId"), "id");
					standardControl.setCode(this.findStandardCode());
					standardControl.setName(jsonObject.getString("cname"));
					standardControl.setIsLeaf(false);
					standardControl.setLevel(2);
					standardControl.setIdSeq(standard.getIdSeq() + standardControlId +  ".");
					standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
					standardControl.setStatus(Contents.STATUS_SAVED);
					standardControl.setCompanyId(UserContext.getUser().getCompanyid());
					standardControl.setSubCompanyids(subCompanyIds);
					standardControl.setParent(standard);
					o_standardDAO.merge(standardControl);
				}else if(Contents.DICT_N.equals(isSubCompany)){//否
					Standard standardControl = null;
					String standardControlId = "";
					if(StringUtils.isNotBlank(jsonObject.getString("cid"))){
						standardControlId = jsonObject.getString("cid");
						standardControl = o_standardDAO.get(standardControlId);
					}else{
						standardControl = new Standard();
						standardControlId = Identities.uuid2();
						standardControl.setId(standardControlId);
					}
					standardControl.setCode(this.findStandardCode());
					standardControl.setName(jsonObject.getString("cname"));
					standardControl.setIsLeaf(true);
					standardControl.setLevel(2);
					standardControl.setIdSeq(standard.getIdSeq() + standardControlId +  ".");
					standardControl.setType(Contents.STANDARD_TYPE_REQUIREMENT);
					standardControl.setStatus(Contents.STATUS_SAVED);
					standardControl.setCompanyId(UserContext.getUser().getCompanyid());
					standardControl.setParent(standard);
					if(jsonObject.has("cstandardControlPoint")){
						String controlPoint = jsonObject.getString("cstandardControlPoint");
						standardControl.setControlPoint(controlPoint);
					}
					o_standardDAO.merge(standardControl);
					//保存要求关联部门
					String deptId = IcmStandardUtils.findIdbyJason(jsonObject.getString("cdeptId"), "id");
					if(StringUtils.isNotBlank(deptId)){
						o_standardRelaOrgBO.delStandardRelaOrgByStandardId(standardControlId);
						StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
						standardRelaOrg.setId(Identities.uuid2());
						standardRelaOrg.setStandard(standardControl);
						standardRelaOrg.setOrg(o_sysOrgnizationDAO.get(deptId));
						standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
						o_standardRelaOrgDAO.merge(standardRelaOrg);
					}
					//保存要求相关流程
					if(StringUtils.isNotBlank(jsonObject.getString("cprocessId"))){
						o_standardRelaProcessureBO.delStandardRelaProcessureByStandardId(standardControlId);
						StandardRelaProcessure standardRelaProcessure = new StandardRelaProcessure();
						standardRelaProcessure.setId(Identities.uuid2());
						standardRelaProcessure.setStandard(standardControl);
						standardRelaProcessure.setProcessure(o_processDAO.get(jsonObject.getString("cprocessId")));
						o_standardRelaProcessureBO.saveStandardRelaProcessure(standardRelaProcessure);
					}
				}
			}
		}
		return standard;
	}
	/**
	 * 根据传过来的id查询内控标准，id为空则新建
	 * @author 元杰
	 * @return map 集合
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> findStandardJsonById(String standardId, String executionId, String step) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(standardId)){
			formMap.put("time", DateUtils.formatShortDate(new Date()));
			formMap.put("deptName", UserContext.getUser().getMajorDeptName());
			formMap.put("deptId", UserContext.getUser().getMajorDeptId());
			formMap.put("updateDeadline", DateUtils.formatShortDate(new Date()));
		}else{
			Standard standard = o_standardDAO.get(standardId);
			formMap.put("controlRequirement", standard.getControlRequirement());
			if(null != standard.getControlLevel()){
				formMap.put("controlLevel", standard.getControlLevel().getId());
				formMap.put("controlLevelName", standard.getControlLevel().getName());
			}
			if(standard.getStatus()!=null){
				formMap.put("status", standard.getStatus());
			}
			if(null != standard.getCreateBy()){//标准的提交部门是指CreateBy这个人所对应的公司
				String deptId="";
				String deptName="";
				Iterator<SysEmpOrg> iterator = standard.getCreateBy().getSysEmpOrgs().iterator();
				if(iterator.hasNext()){
					SysOrganization org = iterator.next().getSysOrganization();
					deptId = org.getId();
					deptName = org.getOrgname();
				}
				formMap.put("deptId",deptId);
				formMap.put("deptName",deptName);
			}
			formMap.put("idSeq", standard.getIdSeq());
			formMap.put("code", standard.getCode());
			formMap.put("feedback", standard.getFeedback());
			if(null != standard.getParent()){
				formMap.put("parent", standard.getParent().getId());
				formMap.put("parentStandardName", standard.getParent().getName());
			}
			if(null != standard.getCreateTime()){
				formMap.put("time", DateUtils.formatShortDate(standard.getCreateTime()));
			}else{
				formMap.put("time", "");
			}
			formMap.put("company", standard.getCompanyId());
			formMap.put("companyName", o_sysOrgnizationDAO.get(standard.getCompanyId()).getOrgname());;
			formMap.put("name", standard.getName());
			formMap.put("id", standard.getId());
			formMap.put("dealStatus", standard.getDealStatus());
			if(null != standard.getUpdateDeadline()){
				formMap.put("updateDeadline", DateUtils.formatShortDate(standard.getUpdateDeadline()));
			}else{
				formMap.put("updateDeadline", "");
			}
			String[] standardControlIds = null; //内控标准对应的要求ID集合
			if("3".equals(step) || "4".equals(step) || "5".equals(step)){//这几步中，要求是针对部门分类并显示的
				StandardBpmObject standardBpmObject = o_standardBpmBO.findStandardBpmObjectByExecutionId(executionId);
				standardControlIds = standardBpmObject.getStandardControlIds();
			}else {
				List<Standard> standardControl = this.findStandardControlBySome(standardId, UserContext.getUser().getCompanyid());
				standardControlIds = new String[standardControl.size()];
				int i = 0;
				for(Standard sc : standardControl){
					standardControlIds[i] =  sc.getId();
					++i;
				}
			}
			formMap.put("standardControlIds", standardControlIds);//返回内控要求的ID集合
		}
		result.put("data", formMap);
		result.put("success", true);
		return result;
	}
	
	/**
	 * 根据id查询内控要求
	 * @author 元杰
	 * @return map 集合
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> findstandardControlById(String standardControlId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(standardControlId)){
			Standard standard = o_standardDAO.get(standardControlId);
			if(StringUtils.isNotBlank(standard.getSubCompanyids())){//如果要求的CompanyId不是父级标准的Company，则选中适用下级单位的Radio为“是”
				formMap.put("inferior", "0yn_y");
			}
			formMap.put("ccontrolRequirement", standard.getControlRequirement());
			formMap.put("cstandardControlAdvice", standard.getFeedback());
			if(standard.getStatus()!=null){
				formMap.put("cstatus", standard.getStatus());
			}
			if(standard.getDealStatus()!=null){//处理状态DealStatus
				formMap.put("statusId", standard.getDealStatus());
			}
			if(null!=standard.getStandardRelaOrg()&&standard.getStandardRelaOrg().size()>0){
				String orgId="";
				String orgName="";
				for(StandardRelaOrg standardRelaOrg:standard.getStandardRelaOrg()){
					orgId=standardRelaOrg.getOrg().getId();
					orgName=standardRelaOrg.getOrg().getOrgname();
				}
				formMap.put("cdeptId","[{\"id\":\""+orgId+"\"}]");
				formMap.put("cdeptName",orgName);
			}
			if(null != standard.getStandardRelaProcessure() && standard.getStandardRelaProcessure() .size()>0){
				String processureName = "";
				String processureId = "";
				for(StandardRelaProcessure standardRelaProcessure : standard.getStandardRelaProcessure()){
					processureName = standardRelaProcessure.getProcessure().getName();
					processureId = standardRelaProcessure.getProcessure().getId();
				}
				formMap.put("cprocessName", processureName);
				formMap.put("cprocessId", processureId);
			}
			if(StringUtils.isNotBlank(standard.getCompanyId())){
				formMap.put("csubCompanyName", o_sysOrgnizationDAO.get(standard.getCompanyId()).getOrgname());
			}
			String subCompanyIdStr = standard.getSubCompanyids();
			if(StringUtils.isNotBlank(subCompanyIdStr)){
				String[] subCompanyIds = subCompanyIdStr.split(",");
				StringBuilder subCompanyIdStrBuilder = new StringBuilder();
				StringBuilder subCompanyNameStrBuilder = new StringBuilder();
				subCompanyIdStrBuilder.append("[");
				for(String subCompanyId : subCompanyIds){
					subCompanyIdStrBuilder.append("{\"id\":\""+subCompanyId+"\"},");
					SysOrganization org = o_sysOrgnizationDAO.get(subCompanyId);
					subCompanyNameStrBuilder.append(org.getOrgname() + ".");
				}
				subCompanyIdStrBuilder.append("]");
				
				formMap.put("csubCompanyName", subCompanyNameStrBuilder.toString());
				formMap.put("csubCompanyId", subCompanyIdStrBuilder.toString());
			}
			if(StringUtils.isNotBlank(standard.getControlPoint())){
				formMap.put("cstandardControlPointHidden", standard.getControlPoint());
				String[] standardPoints = standard.getControlPoint().split(",");
				String standardPointStr = o_dictEntryDAO.get(standardPoints[0]).getName();
				formMap.put("cstandardControlPoint", standardPointStr);
			}else{
				formMap.put("cstandardControlPointHidden", "");
				formMap.put("cstandardControlPoint", "");
			}
			formMap.put("cidSeq", standard.getIdSeq());
			formMap.put("ccode", standard.getCode());
			formMap.put("cname", standard.getName());
			formMap.put("cid", standard.getId());
		}
		result.put("data", formMap);
		result.put("success", true);
		return result;
	}
	
	/**
	 * 保存内控标准集合List
	 * @author 元杰
	 * @param standardList 标准集合
	 */
	@Transactional
	public void mergeStandards(List<Standard> standardList) {
		if(null != standardList && standardList.size() > 0){
			for(Standard standard : standardList){
				o_standardDAO.merge(standard);
			}
		}
	}
	/**
	 * 根据内控标识获取所有下级内控要求
	 * 
	 * @author 元杰
	 * @param parentId内控父级ID
	 * @param companyId公司ID
	 */
	public List<Standard> findStandardControlBySome(String parentId, String companyId){
		List<Standard> standardControlList = new ArrayList<Standard>();
		Criteria criteria = this.o_standardDAO.createCriteria();
		if (StringUtils.isNotBlank(parentId)) {
			criteria.add(Restrictions.eq("parent.id", parentId));
		}
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("companyId", companyId));
		}else {
//			criteria.add(Restrictions.eq("companyId", UserContext.getUser().getCompanyid()));
		}
//		criteria.add(Restrictions.isNull("subCompanyids"));
//		criteria.add(Restrictions.eq("type", ));
		standardControlList = criteria.list();
		return standardControlList;
	}
	
	/**
	 * <pre>
	 * 根据内控标准id取得对应角色人
	 * </pre>
	 * 
	 * @author 元杰
	 * @param roleKey
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public String[] findStandardEmpIdsByRole(String roleKey){
		String[] empIds = null;
		String roleName = ResourceBundle.getBundle("application").getString(roleKey);
		List<SysEmployee> employeeList = o_sysRoleBO.getEmpByCorpAndRole(roleName);
		if(null != employeeList && employeeList.size()>0){
			Integer length=employeeList.size();
			empIds=new String[employeeList.size()];
			for(int i=0;i<length;i++){
				if(null != employeeList.get(i)){
					empIds[i]=employeeList.get(i).getId();
				}
			}
		}
		return empIds;
	}
	
	/**
	 * <pre>
	 * 为下级机构构造同样的标准和要求
	 * </pre>
	 * @param subCompanyIdSet 适用于下级机构的机构ID集合
	 * @param standard 最父级的标准
	 * @return standardList 下级公司内控标准集合
	 * @author 元杰
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public List<String> saveSubCompanyStandards(Set<String> subCompanyIdSet, Standard standard, List<Standard> standardControlList){
		List<String> standardIdList = new ArrayList<String>();//所有下级公司的要求ID集合，相同要求每个公司只有一个
		for(String subCompanyId : subCompanyIdSet){
			Standard subCompanyStandard = new Standard();//为下级公司添加相同的标准
			BeanUtils.copyProperties(standard, subCompanyStandard);
			String standardId = Identities.uuid2();
			subCompanyStandard.setId(standardId);
			subCompanyStandard.setCompanyId(subCompanyId);
			subCompanyStandard.setIdSeq("." + standardId + ".");
			o_standardDAO.merge(subCompanyStandard);
			standardIdList.add(standardId);
			
			for(Standard standardControl : standardControlList){//复制同样的下挂要求
				if(standardControl.getCompanyId().equals(subCompanyId)){
					Standard subCompanyStandardControl = new Standard();
					BeanUtils.copyProperties(standardControl, subCompanyStandardControl);
					String subCompanystandardControlId = Identities.uuid2();
					subCompanyStandardControl.setId(subCompanystandardControlId);
					subCompanyStandardControl.setParent(subCompanyStandard);
					subCompanyStandardControl.setIdSeq(subCompanyStandard.getIdSeq() + subCompanystandardControlId + ".");
					o_standardDAO.merge(subCompanyStandardControl);
				}
			}
		}
		return standardIdList;
	}
	/**
	 * <pre>
	 *自动生成内控标准编号
	 * </pre>
	 * 
	 * @author 元杰
	 * @return 返回根据当前时间生成Code
	 * @since  fhd　Ver 1.1
	*/
	public String findStandardCode(){
		return DateUtils.formatDate(new Date(), "yyyyMMddhhmmss");
	}
	
	/**
	 * 根据标准删除标准和其下挂要求
	 * @author 元杰
	 * @param standardId 内控标准
	 */
	@Transactional
	public void delStandardAndSub(Standard standard) {
		if(null != standard){
			Set<Standard> subStandards = standard.getChildren();
			for(Standard subStandard : subStandards ){
				o_standardDAO.delete(subStandard);
			}
			o_standardDAO.delete(standard);
		}
	}
}
