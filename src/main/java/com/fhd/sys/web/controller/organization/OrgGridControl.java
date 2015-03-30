/**
 * OrgGridControl.java
 * com.fhd.sys.web.controller.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-14 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.business.organization.OrgPositionBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.web.form.organization.OrganizationForm;

/**
 * @author   王再冉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-14		上午11:34:23
 *
 * @see 	 
 */
@Controller
public class OrgGridControl {
	
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	@Autowired
	private DictBO o_dictBO;
	@Autowired
	private OrgPositionBO o_orgPositionBO;

	/**
	 * 机构修改
	 * 通过id查找对象，将信息显示在表单上
	 * @author 黄晨曦
	 * @param request
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findorgbyid.f")
	public Map<String, Object> findOrgById(HttpServletRequest request, String id) {
		SysOrganization sysOrg = new SysOrganization();
		if(StringUtils.isNotBlank(id)){
			String ids[] = id.split(",");
			sysOrg.setId(ids[0]);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		SysOrganization org = o_orgGridBO.findOrganizationByOrgId(sysOrg.getId());
		Map<String, Object> inmap = new HashMap<String, Object>();
		inmap.put("orgcode", org.getOrgcode());
		inmap.put("orgname", org.getOrgname());
		JSONArray orgarray = new JSONArray();
		JSONObject orgjson = new JSONObject();
		if(null != org.getParentOrg()){//上级机构
			orgjson.put("id", org.getParentOrg().getId());
			orgarray.add(orgjson);
			inmap.put("parentOrgStr",orgarray.toString() );
		}else{
			orgjson.put("id", "");
			orgarray.add(orgjson);
			inmap.put("parentOrgStr",orgarray.toString() );
		}
		inmap.put("orgType", org.getOrgType());
		inmap.put("orgLevel", org.getOrgLevel()+"");
		//inmap.put("orgseq", org.getOrgseq());
		inmap.put("forum", org.getForum());
		inmap.put("address",org.getAddress());
		inmap.put("region", org.getRegion());
		inmap.put("email", org.getEmail());
		inmap.put("zipcode", org.getZipcode());
		inmap.put("linkMan", org.getLinkMan());
		inmap.put("linkTel", org.getLinkTel());
		inmap.put("weburl", org.getWeburl());
		inmap.put("orgStatus", org.getOrgStatus());
		inmap.put("snStr", org.getSn());
		//开始时间
		if(null != org.getStartDate()){
			inmap.put("startDataStr", org.getStartDate().toString().split(" ")[0]);
		}
		//结束时间
		if(null != org.getEndDate()){
			inmap.put("endDataStr", org.getEndDate().toString().split(" ")[0]);
		}
		inmap.put("remark", org.getRemark());
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }
	/**
	 * 查找同级/下级机构的父机构
	 * @param request
	 * @param id	节点机构id
	 * @param isSameLevel 是否添加同级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findparentorgbynodeId.f")
	public Map<String, Object> findParentOrgByNodeId(HttpServletRequest request, String id,boolean isSameLevel) {
		SysOrganization sysOrg = new SysOrganization();
		String companyId = UserContext.getUser().getCompanyid();
		if(StringUtils.isNotBlank(id)){
			String ids[] = id.split(",");
			sysOrg.setId(ids[0]);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		SysOrganization org = o_orgGridBO.findOrganizationByOrgId(sysOrg.getId());
		Map<String, Object> inmap = new HashMap<String, Object>();
		JSONArray orgarray = new JSONArray();
		JSONObject orgjson = new JSONObject();
		if(null != org){//上级机构
			if(!isSameLevel){//添加下级机构
				orgjson.put("id", org.getId());//将节点机构作为父机构
				orgarray.add(orgjson);
				inmap.put("parentOrgStr",orgarray.toString() );
			}else{//添加同级机构
				if(null!=companyId&&companyId.equalsIgnoreCase(id)){//机构id为登陆者的公司id
					orgjson.put("id", companyId);//上级机构最高为登陆者公司的id
					orgarray.add(orgjson);
					inmap.put("parentOrgStr",orgarray.toString() );
				}else{
					if(null != org.getParentOrg()){
						orgjson.put("id", org.getParentOrg().getId());//与节点机构同级
						orgarray.add(orgjson);
						inmap.put("parentOrgStr",orgarray.toString() );
					}else{//上级机构的父机构为空
						orgjson.put("id", org.getId());//将节点机构作为父机构
						orgarray.add(orgjson);
						inmap.put("parentOrgStr",orgarray.toString() );
					}
				}
			}
		}
		
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }
	
	/**
	 *	查询分页
	 * 
	 * @author 
	 * @param start	开始条数
	 * @param limit	结束条数
	 * @param query	查询条件
	 * @param sort	排序字段及方式
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/queryOrgPage.f")
	public Map<String, Object> queryOrgPage(int start, int limit, String query, String sort, String orgIds,String positionIds) throws Exception {
		String property = "";
		String direction = "";
		Page<SysOrganization> page = new Page<SysOrganization>();
		
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		
		if (StringUtils.isNotBlank(sort)){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                direction = jsobj.getString("direction");
                
                if(property.equalsIgnoreCase("zq")){
                	property = "isRecycle";
    			}else if(property.equalsIgnoreCase("statusName")){
    				property = "status";
    			}else if(property.equalsIgnoreCase("triggerName")){
    				property = "triggerType";
    			}
            }
        }else{
        	property = "orgLevel";
        	direction = "ASC";
        }
		
		//根据岗位查询机构id
		if(null != positionIds){
			orgIds = o_orgPositionBO.findPositionByIds(positionIds).getSysOrganization().getId();
		}
		
		page = o_orgGridBO.findOrgPageBySome(query, page, property, direction, orgIds);
		
		List<SysOrganization> entityList = page.getResult();
		List<SysOrganization> datas = new ArrayList<SysOrganization>();
		for(SysOrganization de : entityList){
			datas.add(new OrganizationForm(de));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * 
	 * 机构类型下拉菜单
	 * 
	 * @author 
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findOrgAll.f")
	public Map<String, Object> findOrganizationAll(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId("0orgtype");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", dictEntry.getId());
			inmap.put("text",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	
	/**
	 * 机构状态下拉列表
	 * @author 
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findorgstates.f")
	public Map<String, Object> findOrganizationStates(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		inmap = new HashMap<String, Object>();
		inmap.put("id", "1");
		inmap.put("text", "正常");
		list.add(inmap);
		inmap = new HashMap<String, Object>();
		inmap.put("id", "0");
		inmap.put("text", "注销");
		list.add(inmap);
		map.put("datas", list);
		return map;
	}
	
	/**
	 * 业务板块下拉菜单
	 * @author 
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findOrgForumAll.f")
	public Map<String, Object> findOrganizationForumAll(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId("0forum");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", dictEntry.getId());
			inmap.put("text",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	
	/**
	 * 区域下拉列表
	 * @author 
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findOrgRegionAll.f")
	public Map<String, Object> findOrganizationRegionAll(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId("0district");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", dictEntry.getId());
			inmap.put("text",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	
	/**
	 * 保存机构信息
	 * @author 
	 * @param data
	 * @param id	SysOrganization对象实体id
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/saveOrgInfo.f")
	public Map<String, Object> saveOrgInfo(OrganizationForm orgForm,String id/*, HttpServletResponse response*/) throws Exception {
		//PrintWriter out = response.getWriter();
		//String isSave = "false";
		String companyId = UserContext.getUser().getCompanyid();
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inmap = new HashMap<String, Object>();
		
		SysOrganization org = new SysOrganization(Identities.uuid());
		
		org.setOrgname(orgForm.getOrgname());
		org.setOrgcode(orgForm.getOrgcode());
		//上级机构
		if(null != orgForm.getParentOrgStr()){
			String orgid = "";
			JSONArray orgArray = JSONArray.fromObject(orgForm.getParentOrgStr());
			if(null!=orgArray&&orgArray.size()>0){
				orgid = (String)JSONObject.fromObject(orgArray.get(0)).get("id");
			}
			if(null != o_orgGridBO.findOrganizationByOrgId(orgid)){
				org.setParentOrg(o_orgGridBO.findOrganizationByOrgId(orgid));
				//org.setCompany(o_orgGridBO.findOrganizationByOrgId(orgid));
				//修改
				org.setCompany(o_orgGridBO.findOrganizationByOrgId(companyId));
			}
		}
		//org.setOrgLevel(orgForm.getOrgLevel());
		org.setOrgType(orgForm.getOrgType());
		org.setForum(orgForm.getForum());
		org.setRegion(orgForm.getRegion());
		org.setAddress(orgForm.getAddress());
		org.setZipcode(orgForm.getZipcode());
		org.setLinkTel(orgForm.getLinkMan());
		org.setLinkMan(orgForm.getLinkMan());
		org.setWeburl(orgForm.getWeburl());
		org.setEmail(orgForm.getEmail());
		if(null != orgForm.getSnStr()){//排列顺序
			int snInt = Integer.parseInt(orgForm.getSnStr());
			org.setSn(snInt);
		}
		//org.setSn(orgForm.getSnStr());
		//开始时间
		if(StringUtils.isNotBlank(orgForm.getStartDataStr())){
			org.setStartDate(DateUtils.parseDate(orgForm.getStartDataStr(), "yyyy-MM-dd"));
		}
		//结束时间
		if(StringUtils.isNotBlank(orgForm.getEndDataStr())){
			org.setEndDate(DateUtils.parseDate(orgForm.getEndDataStr(), "yyyy-MM-dd"));
		}
		//org.setStartDate(orgForm.getStartDate());
		//org.setEndDate(orgForm.getEndDate());
		org.setOrgStatus(orgForm.getOrgStatus());
		org.setRemark(orgForm.getRemark());
		try{
			if(StringUtils.isNotBlank(id)){
				//更新
				String ids[] = id.split(",");
				org.setId(ids[0]);
				SysOrganization findOrg = o_orgGridBO.findOrganizationByOrgId(org.getId());
				if(null == org.getParentOrg()){
					org.setParentOrg(findOrg.getParentOrg());
					org.setCompany(findOrg.getCompany());
				}
				org.setSn(findOrg.getSn());
				orgForm = new OrganizationForm(findOrg);
				//机构层级
				org.setOrgLevel(orgForm.getOrgLevel());
				org.setOrgseq(org.getParentOrg().getOrgseq()+"."+org.getId());
				org.setIsLeaf(findOrg.getIsLeaf());//设置是否叶子节点
				o_orgGridBO.mergeOrganization(org);
				//isSave = "true";
			}else{
				//保存
				//org.setOrgseq(org.getCompany().getId()+"."+org.getId());//机构序列: 公司id.机构id
			    org.setOrgseq(org.getParentOrg().getOrgseq()+"."+org.getId());
			    org.setOrgLevel(org.getParentOrg().getOrgLevel()+1);
			    org.setIsLeaf(true);//设置为叶子节点
			    if(org.getParentOrg().getIsLeaf()){
			    	org.getParentOrg().setIsLeaf(false);//如果上级机构是叶子节点，将isLeaf设置为false
			    	o_orgGridBO.saveOrganization(org.getParentOrg());
			    }
				o_orgGridBO.saveOrganization(org);
				//isSave = "true";
				inmap.put("isSave", "true");
				inmap.put("id", org.getId());
				inmap.put("text", org.getOrgname());
				inmap.put("leaf", org.getIsLeaf());
				inmap.put("expanded", false);
				inmap.put("type", "jg");
				inmap.put("keys", org.getId()+"jg");
				
			}
			//out.write(isSave);
		}catch (Exception e) {
			// TODO: handle exception
		}/*finally {
			out.close();
		}*/
		map.put("data", inmap);
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 
	 * 删除机构
	 * @author 黄晨曦
	 * @param request
	 * @param ids
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	  @ResponseBody
	  @RequestMapping(value = "/sys/organization/removeOrgEntryById.f")
	  public boolean removeOrgEntryById(HttpServletRequest request, String ids) {
		  List<String> idList = new ArrayList<String>();
		  if (StringUtils.isNotBlank(ids)) {
			  String[] idArray = ids.split(",");
			  for (String id : idArray) {
					SysOrganization orgEntry = o_orgGridBO.findOrganizationByOrgId(id);
					if(!(orgEntry.getChildrenOrg().isEmpty()&&orgEntry.getSysEmpOrgs().isEmpty())){//有子集提示不能删除
						return false;
					}else{
						idList.add(id);
					}
				}
			   o_orgGridBO.removeOrganizationByIds(idList);
			   return true;
		  } else {
			   return false;
		  }
	  }
	  
	  /**
	   * 自动生成编号control
	   * @param orgid
	   * @param parentOrgid
	   * @return
	   */
	  @ResponseBody
		@RequestMapping("/sys/organization/findorgcode.f")
		public Map<String, Object> findOrgCode(String orgid,String parentOrgid){
			Map<String, Object> orgMap=o_orgGridBO.findOrgCode(orgid,parentOrgid);
			return orgMap;
		}
	
}

