package com.fhd.assess.web.controller.formulatePlan;

import java.text.ParseException;
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

import com.fhd.assess.business.formulatePlan.RiskAssessPlanBO;
import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.assess.web.form.formulatePlan.RiskAssessPlanForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.risk.business.TemplateBO;
import com.fhd.risk.entity.Template;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.business.organization.EmpGridBO;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 计划制定控制类
 * @author 王再冉
 *
 */
@Controller
public class RiskAssessPlanControl {
	@Autowired
	private RiskAssessPlanBO r_planBO;
	@Autowired
	private DictBO o_dictBO;
	@Autowired
	private OrgGridBO o_OrgGridBO ;
	@Autowired
	private EmpGridBO o_empBO ;
	@Autowired
	private TemplateBO o_tempBO ;

	/**
	 * 查询计划列表（分页）
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/queryAccessPlansPage.f")
	public Map<String, Object> queryAccessPlansPage(int start, int limit, String query, String sort){
		String property = "";
		String direction = "";
		Page<RiskAssessPlan> page = new Page<RiskAssessPlan>();
		
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
        	property = "planName";
        	direction = "ASC";
        }
		String companyId = UserContext.getUser().getCompanyid();//当前登录者的公司id
		page = r_planBO.findPlansPageBySome(query, page, property, direction, companyId);
		
		List<RiskAssessPlan> entityList = page.getResult();
		List<RiskAssessPlan> datas = new ArrayList<RiskAssessPlan>();
		for(RiskAssessPlan de : entityList){
			datas.add(new RiskAssessPlanForm(de));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	/**
	 * 保存计划任务
	 * @param planForm
	 * @param id
	 * @param response
	 * @throws ParseException 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/saveriskplan.f")
	public Map<String, Object> saveRiskPlan(RiskAssessPlanForm planForm,String id) throws Exception{
		String companyId = UserContext.getUser().getCompanyid();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inmap = new HashMap<String, Object>();
		RiskAssessPlan assessPlan = new RiskAssessPlan();
		assessPlan.setId(Identities.uuid());
		assessPlan.setPlanName(planForm.getPlanName());
		assessPlan.setPlanCode(planForm.getPlanCode());
		assessPlan.setCompany(o_OrgGridBO.findOrganizationByOrgId(companyId));//保存当前机构
		assessPlan.setWorkType(planForm.getWorkType());//工作类型
		assessPlan.setRangeReq(planForm.getRangeReq());//范围要求
		//assessPlan.setRangeType(planForm.getRangeType());//评估范围类型
		assessPlan.setCollectRate(planForm.getCollectRate());//采集频率
		assessPlan.setWorkTage(planForm.getWorkTage());//工作目标
		if(null != r_planBO.findTemplateById(planForm.getTemplateName())){
			Template temp = r_planBO.findTemplateById(planForm.getTemplateName());
			if(null != temp){
				assessPlan.setTemplate(temp);//评估模板
				assessPlan.setTemplateType(temp.getType().getId());//模板类型
			}
		}
		if(StringUtils.isNotBlank(planForm.getBeginDataStr())){//开始时间
			assessPlan.setBeginDate(DateUtils.parseDate(planForm.getBeginDataStr(), "yyyy-MM-dd"));
		}
		if(StringUtils.isNotBlank(planForm.getEndDataStr())){//开始时间
			assessPlan.setEndDate(DateUtils.parseDate(planForm.getEndDataStr(), "yyyy-MM-dd"));
		}
		
		if(null != planForm.getContactName()){
			String conEmpId = "";
			JSONArray conArray = JSONArray.fromObject(planForm.getContactName());
			if(null != conArray && conArray.size()>0){
				conEmpId = (String)JSONObject.fromObject(conArray.get(0)).get("id");
			}
			SysEmployee conEmp = o_empBO.findEmpEntryByEmpId(conEmpId);
			if(null != conEmp){
				assessPlan.setContactPerson(conEmp);//保存联系人
			}
		}
		if(null != planForm.getResponsName()){
			String respEmpId = "";
			JSONArray respArray = JSONArray.fromObject(planForm.getResponsName());
			if(null != respArray && respArray.size()>0){
				respEmpId = (String)JSONObject.fromObject(respArray.get(0)).get("id");
			}
			SysEmployee respEmp = o_empBO.findEmpEntryByEmpId(respEmpId);
			if(null != respEmp){
				assessPlan.setResponsPerson(respEmp);//保存负责人
			}
		}
		assessPlan.setDealStatus(Contents.DEAL_STATUS_NOTSTART);//点保存按钮，处理状态为"未开始"
		assessPlan.setStatus(Contents.STATUS_SAVED);//保存：状态为"已保存"
		if(StringUtils.isNotBlank(id)){//修改
			String ids[] = id.split(",");
			assessPlan.setId(ids[0]);
			RiskAssessPlan plan = r_planBO.findRiskAssessPlanById(assessPlan.getId());
			if(null==assessPlan.getTemplate()&& null!=plan.getTemplate()){
				assessPlan.setTemplate(plan.getTemplate());//评估模板
				assessPlan.setTemplateType(plan.getTemplateType());//模板类型
			}
			r_planBO.mergeRiskAssessPlan(assessPlan);
		}else{
			r_planBO.saveRiskAssessPlan(assessPlan);//保存
		}
		inmap.put("planId", assessPlan.getId());
		//inmap.put("rangeType", assessPlan.getRangeType());
		map.put("data", inmap);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 修改计划
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/findriskassessplanById.f")
	public Map<String, Object> findRiskAssessPlanById(HttpServletRequest request, String id) {
		RiskAssessPlan riskPlan = new RiskAssessPlan();
		if(StringUtils.isNotBlank(id)){
			String ids[] = id.split(",");
			riskPlan.setId(ids[0]);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		RiskAssessPlan plan = r_planBO.findRiskAssessPlanById(riskPlan.getId());
		Map<String, Object> inmap = new HashMap<String, Object>();
		inmap.put("planName", plan.getPlanName());
		inmap.put("templateType", plan.getTemplateType());
		inmap.put("effective", plan.getEffective());
		inmap.put("planCode", plan.getPlanCode());
		//inmap.put("rangeType", plan.getRangeType());
		inmap.put("rangeReq", plan.getRangeReq());
		//联系人
		JSONArray conarray = new JSONArray();
		JSONObject conjson = new JSONObject();
		if(null != plan.getContactPerson()){
			conjson.put("id", plan.getContactPerson().getId());
			conarray.add(conjson);
			inmap.put("contactName",conarray.toString() );
		}else{
			conjson.put("id", "");
			conarray.add(conjson);
			inmap.put("contactName",conarray.toString() );
		}
		//负责人
		JSONArray resparray = new JSONArray();
		JSONObject respjson = new JSONObject();
		if(null != plan.getResponsPerson()){
			respjson.put("id", plan.getResponsPerson().getId());
			resparray.add(respjson);
			inmap.put("responsName",resparray.toString() );
		}else{
			respjson.put("id", "");
			resparray.add(respjson);
			inmap.put("responsName",resparray.toString() );
		}
		inmap.put("collectRate", plan.getCollectRate());
		inmap.put("workType", plan.getWorkType());
		inmap.put("workTage", plan.getWorkTage());
		//模板
		if(null != plan.getTemplate()){
			inmap.put("templateName", plan.getTemplate().getName());
		}
		//开始时间
		if(null != plan.getBeginDate()){
			inmap.put("beginDataStr", plan.getBeginDate().toString().split(" ")[0]);
		}
		//结束时间
		if(null != plan.getEndDate()){
			inmap.put("endDataStr", plan.getEndDate().toString().split(" ")[0]);
		}
			
		map.put("data", inmap);
		map.put("success", true);
		return map;
		}
	/**
	 * 删除计划任务
	 * @param request
	 * @param ids
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value = "/access/formulateplan/removeriskassessplanbyid.f")
	 public boolean removeRiskAssessPlanById(HttpServletRequest request, String ids) {
		  List<String> idList = new ArrayList<String>();
		  if (StringUtils.isNotBlank(ids)) {
			  String[] idArray = ids.split(",");
			  for (String id : idArray) {
				  RiskAssessPlan riskPlan = r_planBO.findRiskAssessPlanById(id);
				  if(null != riskPlan){
					  idList.add(id);
				  }
			  }
			  r_planBO.removeRiskAssessPlansByIds(idList);
			   return true;
		  } else {
			   return false;
		  }
	 }
	
	/**
	 * 评估范围类型下拉列表
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/findrangetype.f")
	public Map<String, Object> findRangeType(HttpServletResponse response){
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId("rm_assess_range");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("type", dictEntry.getId());
			inmap.put("name",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	/**
	 * 模板类型下拉列表
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/findmodeltype.f")
	public Map<String, Object> findModelType(HttpServletResponse response){
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId("dim_template_type");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("type", dictEntry.getId());
			inmap.put("name",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	/**
	 * 评估模板下拉列表
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/findtemplates.f")
	public Map<String, Object> findTemplates(HttpServletResponse response){
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Template> tempEntryList = o_tempBO.findTemplateByQuery(null);
		for (Template tempEntry : tempEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("type", tempEntry.getId());
			inmap.put("name",tempEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		return map;
	}
	
}
