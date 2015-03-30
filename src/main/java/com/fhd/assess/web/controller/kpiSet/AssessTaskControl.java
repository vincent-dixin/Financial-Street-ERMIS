package com.fhd.assess.web.controller.kpiSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.assess.business.formulatePlan.RiskScoreBO;
import com.fhd.assess.business.kpiSet.AssessTaskBO;
import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.assess.entity.kpiSet.RangObjectDeptEmp;
import com.fhd.assess.web.form.formulatePlan.RiskScoreObjectForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.risk.business.RiskBO;
import com.fhd.risk.entity.Risk;
import com.fhd.sys.business.organization.EmpGridBO;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
/**
 * 任务分配control类
 * @author 王再冉
 *
 */
@Controller
public class AssessTaskControl {
	@Autowired
	private AssessTaskBO r_assessTaskBO;
	@Autowired
	private RiskScoreBO r_riskScoreBO;
	@Autowired
	private EmpGridBO o_empGridBO;
	@Autowired
	private RiskBO r_riskBO;
	
	/**
	 * 任务分配列表查询
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/kpiSet/queryassesstaskspage.f")
	public Map<String, Object> queryAssessTasksPage(int start, int limit, String query, String sort, String planId){
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
		String empId = UserContext.getUser().getEmpid();//当前登录者的公司id
		List<String> ObjIds = r_assessTaskBO.findUserDeptScoreObjIdsByempId(empId);
		/*****************************测试计划id********************************/
		planId = "68fce32a-106f-4492-b979-263091aaf134";
		page = r_assessTaskBO.findScoreObjsPageByObjids(query, page, property, direction, ObjIds, planId);
		
		
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
	 * 登录人所在部门的人员下拉列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/access/kpiSet/findempsbyuserdeptId.f")
	public List<Map<String,String>> findEmpsByuserDeptId(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String empId = UserContext.getUser().getEmpid();
		String deptId = "";
		SysEmpOrg empOrg = o_empGridBO.findEmpOrgByEmpId(empId);//查询人员主部门
		if(null != empOrg){
			deptId = empOrg.getSysOrganization().getId();
		}
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
	 * 按风险分配列表
	 * @param riskIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/access/kpiSet/queryassesstaskbyriskid.f")
	public List<Map<String,Object>> queryAssessTaskByriskId(String riskIds){
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		List<String> riskIdList = new ArrayList<String>();
		if (StringUtils.isNotBlank(riskIds)) {
			  String[] idArray = riskIds.split(",");
			  for (String id : idArray) {
				  if(!(riskIdList.contains(id))){
					  riskIdList.add(id);
				  }
			  }
		}
		List<Risk> riskList = new ArrayList<Risk>();
		for(String rId : riskIdList){
			Risk risk = r_riskBO.findRiskById(rId);
			riskList.add(risk);
		}
		for(Risk r : riskList){
			Map<String,Object> map = new HashMap<String,Object>();
			if(null != r.getParent()){//上级风险不为空
				map.put("parRiskName", r.getParent().getName());
				map.put("riskId", r.getParent().getId());
				listmap.add(map);
			}else{
				if(!(riskIdList.contains(r.getId()))){//上级风险为空，且该条风险在riskIdList不存在
					map.put("parRiskName", r.getName());
					map.put("riskId", r.getId());
					listmap.add(map);
				}
			}
		}
		return listmap;
	}
	/**
	 * 保存对象，部门，人员综合表(按风险分配)
	 * @param empIds
	 * @param planId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/kpiSet/saveObjdeptempbysome.f")
	public Map<String, Object> saveObjDeptEmpBySome(String modifyRecords, String planId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<RiskScoreObject> riskObjList = r_riskScoreBO.findRiskScoreObjByplanId(planId);
		JSONArray jsonArray=JSONArray.fromObject(modifyRecords);
		int j = jsonArray.size();
		for(int i=0;i<j;i++){
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String parRiakId = jsonObj.getString("riskId");//上级风险id
			String empId = jsonObj.getString("empId");//评估人id,可能多个评估人
			//根据上级风险id和评估计划查打分对象
			for(RiskScoreObject obj : riskObjList){
				List<RiskScoreDept> scoreDeptList = new ArrayList<RiskScoreDept>();
				if(null != obj.getRisk().getParent()){//上级风险不为空
					if(parRiakId.equals(obj.getRisk().getParent().getId())){
						//符合条件打分对象，查打分部门
						scoreDeptList = r_riskScoreBO.findRiskDeptByObjId(obj.getId());
					}
				}else if(parRiakId.equals(obj.getRisk().getId())){
					scoreDeptList = r_riskScoreBO.findRiskDeptByObjId(obj.getId());
				}
				if(scoreDeptList.size()>0){
					for(RiskScoreDept dept : scoreDeptList){
						//保存对象-部门-人员表
						RangObjectDeptEmp ode = new RangObjectDeptEmp();
						ode.setId(Identities.uuid());
						ode.setScoreDept(dept);
						ode.setScoreObject(obj);
						SysEmployee scoreEmp = o_empGridBO.findEmpEntryByEmpId(empId);
						ode.setScoreEmp(scoreEmp);
						//更新部门，对象，人员实体
						List<RangObjectDeptEmp> odeList = r_assessTaskBO.findObjDeptEmpsByAll(ode);
						if(odeList.size()>0){//清空之前记录，重新保存
							for(RangObjectDeptEmp o : odeList){
								r_assessTaskBO.deleteObjectDeptEmp(o);
							}
						}
						r_assessTaskBO.saveObjectDeptEmp(ode);
					}
				}
			}
		}
		map.put("success", true);
		return map;
	}
	/**
	 * 保存综合表（grid）
	 * @param modifyRecords
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access/kpiSet/saveobjdeptempgridbysome.f")
	public Map<String, Object> saveObjDeptEmpGridBySome(String modifyRecords, String empId){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonArray=JSONArray.fromObject(modifyRecords);
		int j = jsonArray.size();
		for(int i=0;i<j;i++){
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String riskId = jsonObj.getString("riskId");//风险id
			String planId = jsonObj.getString("planId");//评估计划id
			//
			RiskScoreObject obj = r_riskScoreBO.findRiskScoreObjsByPlanAndRisk(planId, riskId);
			List<RiskScoreDept> scoreDeptList = r_riskScoreBO.findRiskDeptByObjId(obj.getId());
			for(RiskScoreDept dept : scoreDeptList){
				SysEmployee scoreEmp = o_empGridBO.findEmpEntryByEmpId(empId);
				RangObjectDeptEmp ode = new RangObjectDeptEmp();
				ode.setId(Identities.uuid());
				ode.setScoreDept(dept);
				ode.setScoreObject(obj);
				ode.setScoreEmp(scoreEmp);
				List<RangObjectDeptEmp> odeList = r_assessTaskBO.findObjDeptEmpsByAll(ode);
				if(odeList.size()>0){//清空之前记录，重新保存
					for(RangObjectDeptEmp o : odeList){
						r_assessTaskBO.deleteObjectDeptEmp(o);
					}
				}
				r_assessTaskBO.saveObjectDeptEmp(ode);
			}
		}
		map.put("success", true);
		return map;
	}

}
