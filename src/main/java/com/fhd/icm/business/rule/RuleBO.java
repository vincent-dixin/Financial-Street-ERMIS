package com.fhd.icm.business.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.rule.RuleDAO;
import com.fhd.icm.dao.rule.RuleRelaFileDAO;
import com.fhd.icm.dao.rule.RuleRelaOrgDAO;
import com.fhd.icm.entity.rule.Rule;
import com.fhd.icm.entity.rule.RuleRelaFile;
import com.fhd.icm.entity.rule.RuleRelaOrg;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.form.RuleForm;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;


/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-19		上午10:05:31
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class RuleBO {

	@Autowired
	private RuleDAO o_RuleDAO;
	@Autowired
	private RuleRelaFileDAO o_RuleRelaFileDAO;
	@Autowired
	private RuleRelaOrgDAO o_RuleRelaOrgDAO;
	
	/**
	 * <pre>
	 *保持制度
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param ruleForm 制度表单，包含制度属性，制度与附件对应，制度与部门对应
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveRule(RuleForm ruleForm){
		Rule rule=new Rule();
		RuleRelaOrg ruleRealOrg=new RuleRelaOrg();
		String companyId = UserContext.getUser().getCompanyid();//获得公司ID
		if(null != ruleForm.getId() && StringUtils.isNotBlank(ruleForm.getId())){//判断是否是存储新制度
			rule.setId(ruleForm.getId());
		}else{//编辑现有制度，不修改ID值
			rule.setId(Identities.uuid());
		}
		if(null != ruleForm.getParent() && !companyId.equals(ruleForm.getParent().getId())){//判断是否有父节点
			Rule ruleparent=new Rule();
			
			Criteria criteria = o_RuleDAO.createCriteria();
			criteria.add(Restrictions.eq("id", ruleForm.getParent().getId()));//获得父节点
			
			List<Rule> listtmp=criteria.list();
			ruleparent=listtmp.get(0);
			
			rule.setParent(ruleparent);//将父节点付给需编辑或增加的制度
			rule.setIdSeq(ruleparent.getIdSeq()+rule.getId()+".");//生成新制度的IdSeq
			rule.setLevel(ruleparent.getLevel()+1);//获得该制度的层级
			if(ruleparent.getIsLeaf()){//如果父节点是叶子节点将其设为不是
				ruleparent.setIsLeaf(false);
				o_RuleDAO.merge(ruleparent);
			}
		}else{
			//无父节点
			rule.setIdSeq("."+rule.getId()+".");
			rule.setLevel(1);
			//rule.setIsLeaf(false);
		}
		if(StringUtils.isNotBlank(ruleForm.getDesc())){//如果描述不为空则为制度设置描述内容
			rule.setDesc(ruleForm.getDesc());
		}
		
		Criteria isLeafcriteria = o_RuleDAO.createCriteria();
		isLeafcriteria.add(Restrictions.eq("parent.id", rule.getId()));
		List<Rule> isLeafList=isLeafcriteria.list();
		if(null != isLeafList && isLeafList.size()>0){
			rule.setIsLeaf(false);
		}else{
			rule.setIsLeaf(true);
		}
		
		rule.setCode(ruleForm.getCode());
		rule.setName(ruleForm.getName());
		rule.setSort(ruleForm.getSort());
		SysOrganization company=new SysOrganization() ;
		company.setId(companyId);
		rule.setCompany(company);
		o_RuleDAO.merge(rule);
			
		ruleRealOrg.setId(rule.getId());
		ruleRealOrg.setRule(rule);
		ruleRealOrg.setType(Contents.ORG_RESPONSIBILITY);
		SysOrganization org=new SysOrganization();
		String orgid=IcmStandardUtils.findIdbyJason(ruleForm.getOrgid(), "id");//将Json转换为需要的字符串
		org.setId(orgid);
		ruleRealOrg.setOrg(org);
		o_RuleRelaOrgDAO.merge(ruleRealOrg);
		//保存指标关联文件
		this.editRuleRelaFile(rule, ruleForm);
	}

	/**
	 * <pre>
	 *保存制度与文件的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param rule 制度
	 * @param ruleForm 表单实体，包含文件实体
	 * @since  fhd　Ver 1.1
	*/
	public void saveRuleRelaFile(Rule rule,RuleForm ruleForm){
		RuleRelaFile ruleRealFile=new RuleRelaFile();
		ruleRealFile.setRule(rule);
		if(null != ruleForm.getFileIds()){
			String Ids=ruleForm.getFileIds().getId();
			String[] IdsList=Ids.split(",");
			FileUploadEntity fileEntity=new FileUploadEntity();
			for(int i=0;i<IdsList.length;i++){
				fileEntity.setId(IdsList[i]);
				ruleRealFile.setFile(fileEntity);
				ruleRealFile.setId(Identities.uuid());
				o_RuleRelaFileDAO.merge(ruleRealFile);
			}
		}
	}
	
	/**
	 * <pre>
	 *根据Id删除对应制度
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param ruleID 需删除的制度Id
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeRuleByID(String ruleID) {
		Rule rule=o_RuleDAO.get(ruleID);
		o_RuleDAO.delete(rule);
		
		Criteria orgcriteria = o_RuleRelaOrgDAO.createCriteria();//删除关联
		orgcriteria.add(Restrictions.eq("rule.id", ruleID));
		List<RuleRelaOrg> orglist=orgcriteria.list();
		if(orglist.size()>0){
			RuleRelaOrg ruleOrg=orglist.get(0);
			o_RuleRelaOrgDAO.delete(ruleOrg);
		}
		
			
		if(null != rule.getParent()){
			//如果父节点下已无其他子节点，则将父节点改为叶子节点
			Criteria sameparentcriteria = o_RuleDAO.createCriteria();
			sameparentcriteria.add(Restrictions.eq("parent.id", rule.getParent().getId()));
			List<Rule> sameParentRule=	sameparentcriteria.list();
			if(sameParentRule.size()==0){
				Criteria parentcriteria = o_RuleDAO.createCriteria();
				parentcriteria.add(Restrictions.eq("id", rule.getParent().getId()));
				List<Rule> parentList=parentcriteria.list();
				Rule parentRule=parentList.get(0);
				parentRule.setIsLeaf(true);
				o_RuleDAO.merge(parentRule);
			}
		}
	}

	/**
	 * <pre>
	 *编辑时修改上传文件
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param rule
	 * @param ruleForm
	 * @since  fhd　Ver 1.1
	*/
	private void editRuleRelaFile(Rule rule, RuleForm ruleForm) {
		Criteria criteria = o_RuleRelaFileDAO.createCriteria();
		criteria.add(Restrictions.eq("rule.id", rule.getId()));
		List<RuleRelaFile> ruleList=criteria.list();
		if(null != ruleList && ruleList.size()>0){
			for(RuleRelaFile ruleEntity:ruleList){
				o_RuleRelaFileDAO.delete(ruleEntity);
			}
		}
		this.saveRuleRelaFile(rule, ruleForm);
	}

	/**
	 * <pre>
	 *加载表单数据：根据规章制度ID查找表单数据
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param ruleID 制度ID
	 * @param ruleParentId 父节点ID
	 * @since  fhd　Ver 1.1
	*/
	public Map<String, Object> findRuleByID(String ruleID,String ruleParentId) {
		Criteria parentcriteria = o_RuleDAO.createCriteria();
		parentcriteria.add(Restrictions.eq("id", ruleParentId));
		List<Rule> ruleparentlist=parentcriteria.list();
		Rule ruleparent=new Rule();
		if(ruleparentlist.size()>0){
			 ruleparent=ruleparentlist.get(0);
		}
		
		Criteria criteria = o_RuleDAO.createCriteria();
		criteria.add(Restrictions.eq("id", ruleID));
		List<Rule> rulelist=criteria.list();
		Rule rule=rulelist.get(0);
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("code", rule.getCode());
		formMap.put("sort", rule.getSort());
		formMap.put("desc", rule.getDesc());
		formMap.put("name", rule.getName());

		Criteria orgcriteria = o_RuleRelaOrgDAO.createCriteria();
		orgcriteria.add(Restrictions.eq("rule.id", ruleID));
		List<RuleRelaOrg> orglist=orgcriteria.list();
		if(null != orglist && orglist.size()>0){
			for(RuleRelaOrg rro : orglist){
				if(Contents.ORG_RESPONSIBILITY.equals(rro.getType())){//责任部门
					formMap.put("orgid","[{\"id\":\""+rro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
				}
			}
		}
		if(StringUtils.isNotBlank(ruleparent.getName())){
			formMap.put("parent.name", ruleparent.getName());
		}else{
			formMap.put("parent.name", "规章制度");
		} 
		
		Criteria fileCriteria=o_RuleRelaFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("rule.id", ruleID));
		List<RuleRelaFile> fileList=fileCriteria.list();
		if(null != fileList && fileList.size()>0){
			//String fileIds=fileList.get(0).getFile().getId();
			//formMap.put("fileIds", fileIds);
			String fileIds=new String();
			for(RuleRelaFile ruleRalaFile:fileList){
				String fileId=ruleRalaFile.getFile().getId();
				fileIds=fileIds+","+fileId;
			}
			formMap.put("fileIds", fileIds);
		}
		
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		
		return node;
	}
	/**
	 * <pre>
	 *加载表单数据：根据规章制度ID查找表单数据forview
	 * </pre>
	 * 
	 * @author  宋佳
	 * @param ruleID 制度ID
	 * @param ruleParentId 父节点ID
	 * @since  fhd　Ver 1.1
	 */
	public Map<String, Object> findRuleByIdForView(String ruleId,String ruleParentId) {
		Criteria parentcriteria = o_RuleDAO.createCriteria();
		parentcriteria.add(Restrictions.eq("id", ruleParentId));
		List<Rule> ruleparentlist=parentcriteria.list();
		Rule ruleparent=new Rule();
		if(ruleparentlist.size()>0){
			ruleparent=ruleparentlist.get(0);
		}
		
		Criteria criteria = o_RuleDAO.createCriteria();
		criteria.add(Restrictions.eq("id", ruleId));
		List<Rule> rulelist=criteria.list();
		Rule rule=rulelist.get(0);
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("code", rule.getCode());
		formMap.put("sort", rule.getSort());
		formMap.put("desc", rule.getDesc());
		formMap.put("name", rule.getName());
		Criteria orgcriteria = o_RuleRelaOrgDAO.createCriteria();
		orgcriteria.add(Restrictions.eq("rule.id", ruleId));
		List<RuleRelaOrg> orglist=orgcriteria.list();
		if(null != orglist && orglist.size()>0){
			for(RuleRelaOrg rro : orglist){
				if(Contents.ORG_RESPONSIBILITY.equals(rro.getType())){//责任部门
					formMap.put("orgid",rro.getOrg().getOrgname());
				}
			}
		}
		if(StringUtils.isNotBlank(ruleparent.getName())){
			formMap.put("parent.name", ruleparent.getName());
		}else{
			formMap.put("parent.name", "规章制度");
		} 
		
		Criteria fileCriteria=o_RuleRelaFileDAO.createCriteria();
		fileCriteria.add(Restrictions.eq("rule.id", ruleId));
		List<RuleRelaFile> fileList=fileCriteria.list();
		if(null != fileList && fileList.size()>0){
			//String fileIds=fileList.get(0).getFile().getId();
			//formMap.put("fileIds", fileIds);
			String fileIds=new String();
			for(RuleRelaFile ruleRalaFile:fileList){
				String fileId=ruleRalaFile.getFile().getId();
				fileIds=fileIds+","+fileId;
			}
			formMap.put("fileIds", fileIds);
		}
		
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		
		return node;
	}

	/**
	 *	加载下拉列表中的制度内容
	 * 
	 * @author 元杰
	 * @param ruleIds 制度Ids
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String, Object>> findRuleByIds(String[] ids) {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Criteria criteria = o_RuleDAO.createCriteria();
		if(ids!=null && ids.length>0){
			criteria.add(Restrictions.in("id", ids));
			List<Rule> ruleList = criteria.list();
			for(Rule r : ruleList){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("id", r.getId());
				data.put("code", r.getCode());
				data.put("text", r.getName());
				datas.add(data);
			}
		}
		return datas;
	}
	
	/**
	 * <pre>
	 *自动生成制度编号
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param ruleID 制度ID
	 * @param ruleParentId 父节点ID
	 * @return 返回根据递增规则生成的ID和Code
	 * @since  fhd　Ver 1.1
	*/
	public Map<String, Object> findRuleCode(String ruleID,String ruleParentId){
		Map<String, Object> ruleMap = new HashMap<String, Object>();
		Rule ruleparent=new Rule();
		
		List<String> list=o_RuleDAO.find("Select max(code) from Rule");
		String maxruleid=(String) list.get(0);
		String[] maxruleidnum=maxruleid.split("-");
		int i= Integer.parseInt(maxruleidnum[1]);
		String rulecode=new String();
		if(i<10){
			int newRuleCoderNumber=i+1;
			rulecode="0"+Integer.toString(newRuleCoderNumber);
		}else{
			rulecode=Integer.toString(i+1);
		}
		ruleMap.put("code", "ZD-"+rulecode);
		
		Criteria criteria = o_RuleDAO.createCriteria();
		criteria.add(Restrictions.eq("id", ruleParentId));
		List<Rule> listtmp=criteria.list();
		if(null != listtmp && listtmp.size()>0){
			ruleparent=listtmp.get(0);
			ruleMap.put("parent.name", ruleparent.getName());
			ruleMap.put("parent.id", ruleparent.getId());
		}
		
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", ruleMap);
		node.put("success", true);
		return node;
	}
	/**根据ID查询Rule实体
	 * @author 邓广义
	 * @return
	 */
	public Rule findRuleByRuleId(String ruleid){
		return this.o_RuleDAO.get(ruleid);
	}
}