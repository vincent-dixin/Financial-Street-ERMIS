package com.fhd.assess.web.controller.formulatePlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.assess.business.formulatePlan.RiskAssessPlanBO;
import com.fhd.assess.business.formulatePlan.RiskScoreRangeBO;
import com.fhd.assess.entity.formulatePlan.RiskScoreRange;
import com.fhd.assess.web.form.formulatePlan.RiskScoreRangeForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;
/**
 * 打分范围，打分部门control类
 * @author 王再冉
 *
 */
@Controller
public class ScoreRangeControl {
	@Autowired
	private RiskAssessPlanBO r_planBO;
	@Autowired
	private RiskScoreRangeBO r_scoreRangeBO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	/**
	 * 保存打分范围实体
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/savescoreranges.f")
	public Map<String, Object> saveScoreRanges(String ids, String planId, String rangeType) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		//保存前清空历史记录***************
		List<RiskScoreRange> rangeList = new ArrayList<RiskScoreRange>();
		rangeList = r_scoreRangeBO.findScoreRangesPlanByPlanAndType(planId, rangeType);
		if(null != rangeList){
			r_scoreRangeBO.removeRiskAssessPlansByIds(rangeList);
		}
		RiskScoreRange scoreRange = new RiskScoreRange();
		scoreRange.setAssessPlan(r_planBO.findRiskAssessPlanById(planId));
		scoreRange.setRangeType(rangeType);//打分范围类型
		if (StringUtils.isNotBlank(ids)) {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
					scoreRange.setRangeTypeId(id);//范围类型id
					scoreRange.setId(Identities.uuid());
					r_scoreRangeBO.saveRiskScoreRange(scoreRange);
			} 
		}
		map.put("success", true);
		return map;
	}
	/**
	 * 查询打分范围列表（按部门选择）
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @param planId
	 * @param rangeType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/queryscorerangespage.f")
	public Map<String, Object> queryScoreRangesPage(int start, int limit, String query, String sort, String planId, String rangeType) throws Exception {
		String property = "";
		String direction = "";
		Page<RiskScoreRange> page = new Page<RiskScoreRange>();
		
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
        	property = "rangeType";
        	direction = "ASC";
        }
		page = r_scoreRangeBO.findScoreRangesPageBySome(query, page, property, direction, planId, rangeType);
		
		List<RiskScoreRange> entityList = page.getResult();
		List<RiskScoreRange> datas = new ArrayList<RiskScoreRange>();
		for(RiskScoreRange de : entityList){
			SysOrganization dept = o_orgGridBO.findOrganizationByOrgId(de.getRangeTypeId());
			datas.add(new RiskScoreRangeForm(de, dept));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	/**
	 * 删除打分范围
	 * @param request
	 * @param ids
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value = "/access/formulateplan/removeriskscorerangesbyIds.f")
	 public boolean removeRiskScoreRangesByIds(HttpServletRequest request, String ids) {
		  List<RiskScoreRange> rangeList = new ArrayList<RiskScoreRange>();
		  if (StringUtils.isNotBlank(ids)) {
			  String[] idArray = ids.split(",");
			  for (String id : idArray) {
				  RiskScoreRange range = r_scoreRangeBO.findScoreRangeById(id);
				  if(null != range){
					  rangeList.add(range);
				  }
			  }
			  r_scoreRangeBO.removeRiskAssessPlansByIds(rangeList);
			   return true;
		  } else {
			   return false;
		  }
	 }

}
