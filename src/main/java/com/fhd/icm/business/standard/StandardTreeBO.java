package com.fhd.icm.business.standard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.dao.standard.StandardRelaFileDAO;
import com.fhd.icm.dao.standard.StandardRelaOrgDAO;
import com.fhd.icm.dao.standard.StandardRelaProcessureDAO;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.entity.standard.StandardRelaFile;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.entity.standard.StandardRelaProcessure;
import com.fhd.icm.interfaces.standard.IStandardTreeBO;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.form.StandardForm;
import com.fhd.process.entity.Process;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 
 * ClassName:StandardTreeBO:内控树BO
 * 
 * @author 刘中帅
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-12-11 上午11:20:00
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class StandardTreeBO implements IStandardTreeBO {

	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private StandardBO o_standardBO;
	@Autowired
	private StandardRelaFileDAO o_standardRelaFileDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private StandardRelaOrgBO o_standardRelaOrgBO;
	@Autowired
	private StandardRelaOrgDAO o_standardRelaOrgDAO;
	@Autowired
	private StandardRelaFileDAO o_StandardRelaFileDAO;
	
	@Autowired
	private StandardRelaProcessureDAO o_StandardRelaProcessureDAO;

	/**
	 * <pre>
	 * 内控维护，修改数据的时候，将信息读取到form表单
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param standardId
	 *            内控标准Id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public Map<String, Object> findStandardByIdToJson(String standardId) {
		Map<String, Object> formMap = new HashMap<String, Object>();

		Standard standard = o_standardBO.findStandardById(standardId);
		formMap.put("code", standard.getCode());// 设置编码
		if (null != standard.getParent()) {
			formMap.put("upName", standard.getParent().getName());
			formMap.put("parent.id", standard.getParent().getId());
		}
		if (StringUtils.isNotBlank(standard.getStatus())) {
			formMap.put("status", standard.getStatus());
		}
		if (StringUtils.isNotBlank(standard.getDealStatus())) {
			formMap.put("statusId", standard.getDealStatus());
		}
		if (null != standard.getControlLevel()) {
			formMap.put("controlLevelId", standard.getControlLevel().getId());
		}
		// 查询内控标准所关联的部门，及部门名称
		StandardRelaOrg standardRelaOrg = null;
		List<StandardRelaFile> standardRelaFileList = null;
		standardRelaOrg = o_standardRelaOrgBO.findStandardRelaOrgById(standardId);
		standardRelaFileList = o_StandardRelaFileDAO.findStandardRelaFileById("standard.id", standardId);
		// 关联流程
		if(null != standard.getStandardRelaProcessure() && standard.getStandardRelaProcessure() .size()>0){
			String processureName = "";
			String processureId = "";
			for(StandardRelaProcessure standardRelaProcessure : standard.getStandardRelaProcessure()){
				processureName = standardRelaProcessure.getProcessure().getName();
				processureId = standardRelaProcessure.getProcessure().getId();
			}
			formMap.put("processName", processureName);
			formMap.put("processId", processureId);
		}
		// 附件
		if (null != standardRelaFileList && standardRelaFileList.size() > 0) {
			String fileIds = "";
			for (StandardRelaFile standardRelaFile : standardRelaFileList) {
				if (StringUtils.isNotEmpty(fileIds)) {
					fileIds = fileIds + ",";
				}
				fileIds =fileIds+ standardRelaFile.getFile().getId();
			}
			formMap.put("fileId", fileIds);
		}
		// 机构
		if (null != standardRelaOrg) {
			// 查询机构表，根据内控标准id获得机构Id，再由机构id得到对应的机构名称
			String sroId = standardRelaOrg.getOrg().getId();
			String resultJason = "[{id:'" + sroId + "',deptname:''}]";
			formMap.put("deptId", resultJason);
		}else{
			formMap.put("deptId","");
		}
		formMap.put("id", standard.getId());// id
		formMap.put("name", standard.getName());// 设置内控名称
		formMap.put("controlRequirement", standard.getControlRequirement());// 设置控制要求
		formMap.put("controlPoint", standard.getControlPoint());
		formMap.put("level", standard.getLevel());
		formMap.put("idSeqp", standard.getIdSeq());
		return formMap;
	}

	/**
	 * 
	 * <pre>
	 * 根据内控标准Id删除内控标准记录
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param standardId
	 *            内控标准id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public String removeStandardsById(String standardId) {
		String idSql = "";
		String[] idSqls = null;
		String[] standardIds = standardId.split(",");
		for (String id : standardIds) {
			List<Standard> standardList = null;
			StringBuffer sb = new StringBuffer();
			// 首先查询父节点下的所有子节点，因为删除的时候子节点的关联也需要删除
			standardList = o_standardBO.findChildsStandardBySome(id, "", false);
			for (Standard standard : standardList) {
				sb.append(standard.getIdSeq());
			}
			sb.append("." + id);
			idSql = sb.toString();
			idSqls = idSql.split("\\.");
			if (idSqls.length > 0) {
				// 删除文件关联
				String delRelaFile = "delete StandardRelaFile st where st.standard.id in (:standardId)";
				Query delRelaFileQuery = o_standardRelaFileDAO
						.createQuery(delRelaFile);
				delRelaFileQuery.setParameterList("standardId", idSqls);
				delRelaFileQuery.executeUpdate();

				// 删除与机构关联
				String delRelaOrg = "delete StandardRelaOrg st where st.standard.id in (:standardId)";
				Query delRelaOryQuery = o_standardRelaOrgDAO
						.createQuery(delRelaOrg);
				delRelaOryQuery.setParameterList("standardId", idSqls);
				delRelaOryQuery.executeUpdate();
			}
			// 删除内控
			StringBuffer delStandard = new StringBuffer("delete Standard st where st.id in('").append(StringUtils.join(idSqls,"','")).append("')"); // 删除与文件关联
			Query delStandardQuery = o_standardDAO.createQuery(delStandard.toString());
			delStandardQuery.executeUpdate();
		}
		return "success";
	}

	/**
	 * 标准维护，保存数据前做验证
	 * 
	 * @author 刘中帅，张 雷
	 * @param id 标准ID
	 * @param standardCode 标准编号
	 * @return 标识（codeRepeat:编号重复,notRepeat:没有重复）
	 */
	public String validateStandard(String id, String standardCode) {
		String flagString = "notRepeat";

		String[] names = new String[1];
		names[0] = "code";
		String[] codeValues = new String[1];
		codeValues[0] = standardCode;
		List<Standard> nameList = findStandardBySome(names, codeValues);
		if (nameList.size() >0) {
			for (Standard standard : nameList) {
				if (!standard.getId().equals(id) && standard.getCode().equals(standardCode)) {
					flagString = "codeRepeat";
				}
			}
		}
		return flagString;
	}

	/**
	 * <pre>
	 * 多条件查询standard(只支持查询条件为eq方式,按本公司Id查询)
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param param 参数名称
	 * @param paramValue 条件
	 * @return
	 * @since fhd　Ver 1.1
	 */
	private List<Standard> findStandardBySome(String[] param,
			String[] paramValue) {
		Criteria criteria = o_standardDAO.createCriteria();
		for (int i = 0; i < param.length; i++) {
			criteria.add(Restrictions.eq(param[i], paramValue[i]));
		}
		String companyId = UserContext.getUser().getCompanyid();
		criteria.add(Restrictions.eq("companyId", companyId));
		criteria.addOrder(Order.asc("name"));
		List<Standard> list = criteria.list();
		return list;
	}

	/**
	 * 
	 * <pre>
	 * 保存内控标准
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param standardId
	 * @param form：表单
	 * @param nodeId：节点id
	 * @param addType:添加类型
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveStandard(StandardForm form, String nodeId, String idSeq, String standardId) {
		if (null != form) {
			String standardFormId = form.getId();
			Standard standard = null;
			String codeNew = Identities.uuid();
			if(StringUtils.isNotBlank(standardFormId)){
				standard = o_standardBO.findStandardById(standardFormId);
			}else{
				standard = new Standard();
				if (null == idSeq) {
					idSeq = ".";
				}
				standard.setIdSeq(idSeq + codeNew + ".");
				standard.setId(codeNew);
				
				Standard standardParent = null;
				// 查询父节点
				standardParent = o_standardBO.findStandardById(nodeId);
				standard.setIsLeaf(true);
				if (null == standardParent) {
					standard.setLevel(1);
				}else if(standardParent.getIsLeaf()) {// 如果是叶子节点
					if(StringUtils.isNotEmpty(standardId)) {
						standardParent.setIsLeaf(true);// 设置父节点成文件夹样式
					}else {
						standardParent.setIsLeaf(false);// 设置父节点成文件夹样式
					}
					standard.setLevel(standardParent.getLevel() + 1);
				}else {
					standard.setLevel(standardParent.getLevel() + 1);
				}
				standard.setIsLeaf(true);
				standard.setParent(standardParent);
			}
			
			String companyId = UserContext.getUser().getCompanyid();
			if (StringUtils.isNotEmpty(form.getControlLevelId()) && null != form.getControlLevelId()) {
				// 控制层级
				DictEntry dictEntryControl = new DictEntry();
				dictEntryControl.setId(form.getControlLevelId());
				standard.setControlLevel(dictEntryControl);
			}
			// 状态
			standard.setStatus(Contents.STATUS_SAVED);
			standard.setDealStatus(Contents.DEAL_STATUS_FINISHED);
			standard.setDeleteStatus(true);
			//处理状态
			if (StringUtils.isNotEmpty(form.getStatusId())) {
				standard.setDealStatus(form.getStatusId());
			}
			if (StringUtils.isNotEmpty(standardId) && !"undefined".equals(standardId)) {
				nodeId = standardId;
				standard.setType(Contents.STANDARD_TYPE_REQUIREMENT);
			} else {
				standard.setType(Contents.STANDARD_TYPE_CLASS);
			}

			standard.setControlPoint(form.getControlPoint());// 控制要素
			standard.setControlRequirement(form.getControlRequirement());
			standard.setName(form.getName());
			standard.setCode(form.getCode());
			standard.setUpdateDeadline(new Date());
			standard.setCompanyId(companyId);

			// 保存内控标准
			o_standardDAO.merge(standard);
			// 附件
			if (StringUtils.isNotEmpty(form.getFileId()) && null != form.getFileId()) {
				String[] fileIds=form.getFileId().split(",");
				for(String fId:fileIds){
					if(StringUtils.isNotEmpty(fId)){
						FileUploadEntity fileUploadEntity = new FileUploadEntity();
						fileUploadEntity.setId(fId);
						Standard standardFile = new Standard();
						standardFile.setId(codeNew);
						StandardRelaFile standardRelaFile = new StandardRelaFile();
						standardRelaFile.setId(Identities.uuid());
						standardRelaFile.setStandard(standardFile);
						standardRelaFile.setFile(fileUploadEntity);
						o_StandardRelaFileDAO.merge(standardRelaFile);
					}
				}
			}

			if (StringUtils.isNotEmpty(form.getDeptId())
					&& null != form.getDeptId()) {
				String orgId = "";
				// 内控与责任部门的关联
				StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
				// 责任部门
				SysOrganization sysOrganization = new SysOrganization();
				orgId = IcmStandardUtils.findIdbyJason(form.getDeptId(), "id");
				sysOrganization.setId(orgId);
				// 内控关联表中的内控实体
				Standard standardDept = new Standard();
				standardDept.setId(codeNew);

				standardRelaOrg.setId(Identities.uuid());
				standardRelaOrg.setStandard(standardDept);
				standardRelaOrg.setOrg(sysOrganization);
				standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
				// 保存内控标准与部门的关联表
				o_standardRelaOrgDAO.merge(standardRelaOrg);
			}
		}
	}
	/**
	 * <pre>
	 *删除内控与部门的关联
	 * </pre>
	 * @author 刘中帅
	 * @param standardId：内控Id
	 * @since  fhd　Ver 1.1
	 */
	public void removeStandardRelaOrgByStandardId(String standardId){
		String delRelaOrg = "delete StandardRelaOrg st where st.standard.id=:standardId";
		// 首先删除关联的责任部门
		Query delRelaOrgQuery = o_standardRelaOrgDAO.createQuery(delRelaOrg);
		delRelaOrgQuery.setString("standardId",standardId);
		delRelaOrgQuery.executeUpdate();
	}
	/**
	 * <pre>
	 *删除内控与文件的关联
	 * </pre>
	 * @author 刘中帅
	 * @param standardId：内控Id
	 * @since  fhd　Ver 1.1
	 */
	public void removeStandardRelaFileByStandardId(String standardId){
		String delRelaFile = "delete StandardRelaFile st where st.standard.id=:standardId";
		Query delRelaFileQuery = o_StandardRelaFileDAO.createQuery(delRelaFile);
		delRelaFileQuery.setString("standardId",standardId);
		delRelaFileQuery.executeUpdate();
	}
	
	/**
	 * <pre>
	 * 根据标准ID删除标准对应的流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param standardId 标准ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeStandardRelaProcessureByStandardId(String standardId){
		String hql = "delete StandardRelaProcessure srp where srp.standard.id=:standardId";
		Query query = o_StandardRelaFileDAO.createQuery(hql);
		query.setString("standardId",standardId);
		query.executeUpdate();
	}
	/**
	 * 
	 * <pre>
	 * 内控标准编辑方法
	 * 添加标准关联流程信息的保存
	 * </pre>
	 * 
	 * @author 刘中帅，张雷
	 * @param form
	 *            以封装的standardFrom实体
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void editStandard(StandardForm form) {
		if (null != form) {
			Standard standard = new Standard();
			standard.setControlPoint(form.getControlPoint());// 控制要素
			standard.setControlRequirement(form.getControlRequirement());// 控制要求
			String companyId = UserContext.getUser().getCompanyid();
			standard.setId(form.getId());
			standard.setName(form.getName());
			standard.setCode(form.getCode());
			standard.setCompanyId(companyId);
			standard.setIdSeq(form.getIdSeqp());
			Standard standardParent = new Standard();
			standardParent.setId(form.getParent().getId());
			standard.setParent(standardParent);
			standard.setLevel(form.getLevel());
			if (null != form.getControlLevelId()) {
				// 控制层级
				DictEntry dictEntryControl = new DictEntry();
				dictEntryControl.setId(form.getControlLevelId());
				standard.setControlLevel(dictEntryControl);
			}
			//状态
			standard.setStatus(Contents.STATUS_SAVED);
			standard.setDeleteStatus(true);
			//处理状态
			if (StringUtils.isNotBlank(form.getStatusId())) {
				standard.setDealStatus(form.getStatusId());
			}
			//删除相关联的文件
			removeStandardRelaFileByStandardId(form.getId());
			// 附件
			if (StringUtils.isNotEmpty(form.getFileId())&& null != form.getFileId()) {
				String[] fileIds=form.getFileId().split(",");
					for(String fId:fileIds){
						if(StringUtils.isNotEmpty(fId)){
							FileUploadEntity fileUploadEntity = new FileUploadEntity();
							fileUploadEntity.setId(fId);
							Standard standardFile = new Standard();
							standardFile.setId(form.getId());
							StandardRelaFile standardRelaFile = new StandardRelaFile();
							standardRelaFile.setId(Identities.uuid());
							standardRelaFile.setStandard(standardFile);
							standardRelaFile.setFile(fileUploadEntity);
							o_StandardRelaFileDAO.merge(standardRelaFile);
						}
					}
			}
			// 在保存数据之前，查询一下，获得，type,和isleaf
			Standard standardResult = o_standardBO.findStandardById(form.getId());
			standard.setType(standardResult.getType());
			standard.setIsLeaf(standardResult.getIsLeaf());
			o_standardDAO.merge(standard);
			//删除与部门关联
			removeStandardRelaOrgByStandardId(form.getId());
			String orgId = "";
			if (StringUtils.isNotEmpty(form.getDeptId())) {
				// 内控与责任部门的关联
				StandardRelaOrg standardRelaOrg = new StandardRelaOrg();
				// 责任部门
				SysOrganization sysOrganization = new SysOrganization();
				orgId = IcmStandardUtils.findIdbyJason(form.getDeptId(), "id");
				sysOrganization.setId(orgId);
				// 内控关联表中的 内控实体
				Standard standardDept = new Standard();
				standardDept.setId(form.getId());
				standardRelaOrg.setId(Identities.uuid());
				standardRelaOrg.setStandard(standardDept);
				standardRelaOrg.setOrg(sysOrganization);
				standardRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
				// 保存内控标准与部门的关联表
				o_standardRelaOrgDAO.merge(standardRelaOrg);
			}
			removeStandardRelaProcessureByStandardId(form.getId());//先删除与流程的关联
			if(StringUtils.isNotBlank(form.getProcessId())){//再创建与流程的关联
				StandardRelaProcessure standardRelaProcessure = new StandardRelaProcessure();
				standardRelaProcessure.setId(Identities.uuid());
				standardRelaProcessure.setStandard(standard);
				standardRelaProcessure.setProcessure(new Process(form.getProcessId()));
				o_StandardRelaProcessureDAO.merge(standardRelaProcessure);
			}
			
		}
	}
	/**
	 * <pre>
	 * 根据内控标识获取所有下级内控
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param node 
	 * @param canChecked 是否有复选框
	 * @param query查询条件
     * @param myType 类型
     * @param companyId 公司id
	 * @return List<Map<String, Object>>
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> standardTreeLoader(String id, Boolean canChecked, String query, String myType ,String companyId) {
		// 有查询条件的树节点
		List<Standard> childList = null;
		List<Map<String, Object>> resuls = new ArrayList<Map<String, Object>>();
		boolean expanded = StringUtils.isNotBlank(query) ? true : false;// 是否展开节点
		if (!StringUtils.isNotEmpty(id) && expanded) {
			List<Standard> standardAllList = null;
			// 首先查询出所有哦的节点，供下面的递归使用
			childList = o_standardBO.findChildStandardById(id,companyId);
			List<Standard> resulstLists = new ArrayList<Standard>();
			standardAllList = o_standardDAO.createCriteria().list();
			for (Standard standard : childList) {
				List<Standard> resulstList = new ArrayList<Standard>();
				if (standard.getName().indexOf(query) != -1) {
					resulstLists.add(standard);
					continue;
				} else {
					o_standardBO.findStandardById(standard.getId(), query,
							resulstList, companyId, standardAllList);
					if (resulstList.size() > 0) {
						resulstLists.add(standard);
					}
				}
			}
			// 循环取值
			for (Standard standard : resulstLists) {
				Map<String, Object> reMap = null;
				reMap = setNodesAttributes(standard, expanded, canChecked, myType);
				resuls.add(reMap);
			}
		} else {
			childList = o_standardBO.findChildStandardById(id,companyId);
			for (Standard standard : childList) {
				Map<String, Object> reMap = null;
				reMap = setNodesAttributes(standard, expanded, canChecked, myType);
				resuls.add(reMap);
			}
		}
		return resuls;
	}
	/**
	 * 给节点属性设置值（自用）
	 * 
	 * @param standard
	 * @param expanded
	 * @param canChecked
	 * @return
	 */
	private Map<String, Object> setNodesAttributes(Standard standard,
			boolean expanded, boolean canChecked, String myType) {

		Map<String, Object> node = new HashMap<String, Object>();
			if("standard".equals(myType)){
				node.put("checked", false);
			}
			String controlPoint = "";
			if(!StringUtils.isBlank(standard.getControlPoint())){
				controlPoint = this.o_dictEntryDAO.get(standard.getControlPoint()).getName();
			}
			node.put("id", standard.getId());
			node.put("code", standard.getCode());
			node.put("dbid", standard.getId());
			node.put("text", standard.getName());
			node.put("idSeq", standard.getIdSeq());
			node.put("leaf", standard.getIsLeaf());
			node.put("type", standard.getType());
			node.put("controlPoint", controlPoint);
			node.put("expanded", expanded);
			node.put("iconCls", "icon-note");
		return node;
	}
	/**
	 * <pre>
	 * 通过standardId创建standardCode
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public String createStandardcodeById(String nodeId) {
	/*	String standardCode = "BZ-00";
		String queryHql = "select max(code) from Standard";
		List<String> resultList = null;
		resultList = o_standardDAO.find(queryHql);
		if (resultList.size() > 0 && null != resultList.get(0)) {
			Integer newCodeNumber = 0;
			String code = resultList.get(0);
			String codeChar = code.substring(0, code.indexOf("-") + 1);
			String codeNumber = code.substring(code.indexOf("-") + 1);
			newCodeNumber = Integer.parseInt(codeNumber) + 1;
			standardCode = codeChar + newCodeNumber;
		}*/
		return DateUtils.formatDate(new Date(), "yyyyMMddhhmmss");
	}
}
