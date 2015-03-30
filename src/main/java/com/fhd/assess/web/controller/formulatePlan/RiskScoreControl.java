package com.fhd.assess.web.controller.formulatePlan;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.assess.business.formulatePlan.RiskAssessPlanBO;
import com.fhd.assess.business.formulatePlan.RiskScoreBO;
import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.assess.web.form.formulatePlan.RiskScoreObjectForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
/**
 * 打分对象，打分部门control类
 * @author 王再冉
 *
 */
@Controller
public class RiskScoreControl {
	@Autowired
	private RiskScoreBO r_riskScoreBO;
	@Autowired
	private RiskAssessPlanBO r_planBO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	
	/**
	 * 按部门查询风险并保存打分对象
	 * @param orgIds	部门id
	 * @param planId	评估计划id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/queryriskandsavescoreobject.f")
	public Map<String, Object> queryRiskAndSaveScoreObject(String orgIds, String planId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<RiskOrg> riskOrgs = r_riskScoreBO.findRiskOrgsById(orgIds);//根据部门id查询所有部门风险实体
		List<Risk> riskList = new ArrayList<Risk>();
		RiskAssessPlan plan = r_planBO.findRiskAssessPlanById(planId);
		if(riskOrgs.size()>0){
			for(RiskOrg riskOrg : riskOrgs){
				if("1".equals(riskOrg.getRisk().getDeleteStatus())){
					riskList.add(riskOrg.getRisk());//查询跟部门相关的所有风险
				}
			}
		}
		for(Risk risk : riskList){
			//打分对象
			RiskScoreObject scoreObj = new RiskScoreObject();
			scoreObj.setId(Identities.uuid());
			scoreObj.setRisk(risk);
			scoreObj.setAssessPlan(plan);
			RiskScoreObject exitObj =
					r_riskScoreBO.findRiskScoreObjsByPlanAndRisk(scoreObj.getAssessPlan().getId(), scoreObj.getRisk().getId());
			if(!(null != exitObj)){//已经存在相同的打分对象
				r_riskScoreBO.saveRiskScoreObject(scoreObj);//保存打分对象
				//根据风险id查出所有风险部门实体
				riskOrgs = r_riskScoreBO.findRiskOrgsByriskId(risk.getId());
				for(RiskOrg riskO : riskOrgs){
					//打分部门
					RiskScoreDept scoreDept = new RiskScoreDept();
					scoreDept.setId(Identities.uuid());
					scoreDept.setScoreObject(scoreObj);
					scoreDept.setOrganization(riskO.getSysOrganization());
					scoreDept.setOrgType(riskO.getType());//部门类型
					//判断打分部门表中是否存在记录
					List<RiskScoreDept> deptList = 
							r_riskScoreBO.findRiskScoreDeptIsSave(scoreObj.getId(), riskO.getId(), riskO.getType());
					if(!(deptList.size()>0)){//不存在，保存
						r_riskScoreBO.saveRiskScoreDept(scoreDept);//保存打分部门
					}
				}
			}
		}
		map.put("success", true);
		return map;
	}
	/**
	 * 查询评估计划下的风险列表分页
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @param planId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/queryriskscoreobjspage.f")
	public Map<String, Object> queryRiskScoreObjsPage(int start, int limit, String query, String sort, String planId){
		String property = "";
		String direction = "";
		Page<RiskScoreObject> page = new Page<RiskScoreObject>();
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
        	property = "assessPlan";
        	direction = "ASC";
        }
		page = r_riskScoreBO.findScoreObjsPageBySome(query, page, property, direction, planId);
		
		
		List<RiskScoreObject> entityList = page.getResult();
		List<RiskScoreObject> datas = new ArrayList<RiskScoreObject>();
		for(RiskScoreObject de : entityList){
			List<RiskScoreDept> scoDepts = r_riskScoreBO.findRiskDeptByObjId(de.getId());
			datas.add(new RiskScoreObjectForm(de,scoDepts));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	/**
	 * 删除打分对象，打分部门
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/removeriskscoresbyIds.f")
	public boolean removeRiskScoresByIds(String ids) {
		  List<String> ObjIdList = new ArrayList<String>();
		  if (StringUtils.isNotBlank(ids)) {
			  String[] idArray = ids.split(",");
			  for (String id : idArray) {
				  RiskScoreObject scoreObj = r_riskScoreBO.findRiskScoreObjById(id);
				  if(null != scoreObj){
					  ObjIdList.add(id);
					  List<RiskScoreDept> deptList = r_riskScoreBO.findRiskDeptByObjId(scoreObj.getId());
					  if(deptList.size()>0){
						  r_riskScoreBO.removeRiskScoreDepts(deptList);
					  }
					  r_riskScoreBO.removeRiskScoreObj(scoreObj);
				  }
			  }
			   return true;
		  } else {
			   return false;
		  }
	}
	/**
	 * 查询承办人列表
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @param deptIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/access/formulateplan/queryscoredeptandempgrid.f")
	public List<Map<String,Object>> queryscoreDeptAndEmpGrid(String deptIds){
		List<String> deptIdList = new ArrayList<String>();
		if (StringUtils.isNotBlank(deptIds)) {
			  String[] idArray = deptIds.split(",");
			  for (String id : idArray) {
				  deptIdList.add(id);
			  }
		}
		List<SysOrganization> list = o_orgGridBO.findOrganizationByOrgIds(deptIdList);
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		if(null != list){
			for(SysOrganization org : list){
				SysEmployee cbr = r_riskScoreBO.findEmpsByRoleIdAnddeptId(org.getId());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", org.getId());
				map.put("deptName", org.getOrgname());
				map.put("empId", null!=cbr?cbr.getId():"");//默认显示该部门风险管理员
				map.put("empName", null!=cbr?cbr.getEmpname():"");//默认显示该部门风险管理员
				listmap.add(map);
			}
		}
		return listmap;
	}
	/**
	 * 查询打分对象的所有部门（去重）
	 * @param planId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/formulateplan/findscoredeptids.f")
	public Map<String, Object> findScoreDeptIds(String planId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> deptIdlist = r_riskScoreBO.findScoreDeptIdsByPlanId(planId);
		map.put("deptIds", deptIdlist);
		return map;
	}
	/**
	 * 查询出计划所对应的的承办人，作为stroe中的数据
	 * @param deptIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/access/formulateplan/findempsbydeptids.f")
	public List<Map<String,String>> findEmpsBydeptIds(String deptId){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<SysEmployee> empList = r_riskScoreBO.findEmpsBydeptId(deptId);
		for(SysEmployee emp : empList){
			Map<String,String> mapEmp = new HashMap<String,String>();
			mapEmp.put("id", emp.getId());
			mapEmp.put("name",emp.getEmpname());
			list.add(mapEmp);
		}
		return list;
	}

	/**
	 * 评估计划提交
	 * @param empIds
	 * @param executionId	流程id
	 * @return
	 */
	@RequestMapping("/access/formulateplan/submitassessriskplan.f")
	public void submitAssessRiskPlan(String empIds, String approverId, String planId, String executionId, HttpServletResponse response) {
		PrintWriter out = null;
		String approver = "";
		JSONArray jsonArray = JSONArray.fromObject(approverId);
		if (jsonArray.size() > 0){
			 JSONObject jsobj = jsonArray.getJSONObject(0);
			 approver = jsobj.getString("id");//审批人
		}
		try {
			out = response.getWriter();
			r_riskScoreBO.submitAssessRiskPlanToApprover(empIds, approver, planId, executionId);
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
}
