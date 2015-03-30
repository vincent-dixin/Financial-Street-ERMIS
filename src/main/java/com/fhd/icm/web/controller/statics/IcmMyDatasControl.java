package com.fhd.icm.web.controller.statics;

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

import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.icm.business.statics.IcmMyDatasBO;

/**
 * 内控我的数据(包括 我的流程 我的风险 我的制度 我的标准 我的控制措施 我的任务)
 * @author 邓广义
 * @date 2013-5-15
 * @since  fhd　Ver 1.1
 */
@Controller
public class IcmMyDatasControl {
	@Autowired
	private IcmMyDatasBO o_icmMyDatasBO;
	
	/**
	 * <pre>
	 * 流程统计情况
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：部门或公司
	 * @param limit 页容量
	 * @param start 起始值
	 * @param pagable 是否更多页面
	 * @param query 查询条件：流程编号或流程名称
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findprocessbysome.f")
	public Map<String,Object> findProcessBySome(String orgId, String query, String start, String limit, String sort){
		StringBuffer sortDirection = new StringBuffer();
		if(StringUtils.isNotBlank(sort)){//排序
			JSONArray jsonArray=JSONArray.fromObject(sort);
			if(jsonArray.size()!=0){
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String property = jsonObject.getString("property");
					String direction = jsonObject.getString("direction");
					if("processCode".equals(property)){
						sortDirection.append("p.PROCESSURE_CODE ").append(direction);
					}else if("processName".equals(property)){
						sortDirection.append("p.PROCESSURE_NAME ").append(direction);
					}else if("updateDate".equals(property)){
						sortDirection.append("p.LAST_MODIFY_TIME ").append(direction).append(", p.CREATE_TIME ").append(direction);
					}else if("orgName".equals(property)){
						sortDirection.append("o.ORG_NAME ").append(direction);
					}else if("empName".equals(property)){
						sortDirection.append("e.EMP_NAME ").append(direction);
					}else if("frequency".equals(property)){
						sortDirection.append("p.PROCESS_CLASS ").append(direction);
					}else if("parentName".equals(property)){
						sortDirection.append("pp.PROCESSURE_NAME ").append(direction);
					}
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Object[]> objectsList = o_icmMyDatasBO.findProcessBySome(result, orgId, query, start, limit, sortDirection.toString());
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for(Object[] objects: objectsList){
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("orgId", null!=objects[0]?objects[0]:"");//机构ID
			row.put("orgName", null!=objects[1]?objects[1]:"");//机构名称（责任部门）
			row.put("empId", null!=objects[2]?objects[2]:"");//员工ID
			row.put("empName", null!=objects[3]?objects[3]:"");//员工姓名（责任人）
			row.put("parentName", null!=objects[4]?objects[4]:"");//流程分类名称
			row.put("frequency", null!=objects[11]?objects[11]:"");//发生频率
			row.put("processCode", null!=objects[6]?objects[6]:"");//流程编号
			row.put("processName", null!=objects[7]?objects[7]:"");//流程名称
			row.put("id", null!=objects[8]?objects[8]:"");//流程ID
			String updateDate = "";
			if(null != objects[10]){
				updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[10]).substring(0,String.valueOf(objects[10]).lastIndexOf("."))), "yyyy-MM-dd");
			}else{
				if(null != objects[9]){
					updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[9]).substring(0,String.valueOf(objects[9]).lastIndexOf("."))), "yyyy-MM-dd");
				}
			}
			row.put("updateDate", updateDate);//更新日期
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	/**
	 * 我的控制措施
	 * @param orgid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/statics/icmmycontroldatas.f")
	public Map<String,Object> findMyControlDatasByOrgId(String orgid,String limit,String start,boolean pagable,String query){
		
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		/*List<Object[]> objects = o_icmMyDatasBO.findMyControlDatasByOrgId(result,orgid,limit,query,start);
		if(!pagable){//每个小块儿中的gird传送limit参数
			for(Object[] o: objects){
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", o[0]);
				row.put("code", null!=o[2]?o[2]:"");
				row.put("name", null!=o[3]?o[3]:"");
				datas.add(row);
			}
		}else{							//点击更多 无参数 显示所有
			for(Object[] o: objects){
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", o[0]);
				row.put("risk", null!=o[1]?this.o_riskBO.findRiskById((String)o[1]).getName():"");//所属风险
				row.put("code", o[2]);
				row.put("name", o[3]);
				row.put("controlMode", null!=o[4]?this.o_dictBO.findDictEntryById((String)o[4]).getName():"");//控制方式
				row.put("isKeyPoint", null!=o[5]?"0yn_n".equals(o[5])?"否":"是":"");//是否关键控制点
				datas.add(row);
			}
		}*/
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * 我的风险 “风险水平”？？？
	 * @param orgid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/statics/findriskbysome.f")
	public Map<String,Object> findRiskBySome(String orgId,String limit,String start,String query,String sort){
		StringBuffer sortDirection = new StringBuffer();
		if(StringUtils.isNotBlank(sort)){//排序
			JSONArray jsonArray=JSONArray.fromObject(sort);
			if(jsonArray.size()!=0){
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String property = jsonObject.getString("property");
					String direction = jsonObject.getString("direction");
					if("riskCode".equals(property)){
						sortDirection.append("r.RISK_CODE ").append(direction);
					}else if("riskName".equals(property)){
						sortDirection.append("r.RISK_NAME ").append(direction);
					}else if("parentName".equals(property)){
						sortDirection.append("pr.RISK_NAME ").append(direction);
					}else if("orgName".equals(property)){
						sortDirection.append("o.ORG_NAME ").append(direction);
					}else if("updateDate".equals(property)){
						sortDirection.append("r.LAST_MODIFY_TIME ").append(direction).append(", r.CREATE_TIME ").append(direction);
					}
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Object[]> objectsList = o_icmMyDatasBO.findRiskBySome(result, orgId, query, start, limit, sortDirection.toString());
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for(Object[] objects: objectsList){
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", null!=objects[0]?objects[0]:"");
			row.put("riskCode",  null!=objects[1]?objects[1]:"");//要求编号
			row.put("riskName", null!=objects[2]?objects[2]:"");//要求名称
			row.put("parentId",  null!=objects[3]?objects[3]:"");//内控标准ID
			row.put("parentName",  null!=objects[4]?objects[4]:"");//内控标准名称
			row.put("orgId",  null!=objects[5]?objects[5]:"");//机构ID
			row.put("orgName",  null!=objects[6]?objects[6]:"");//机构名称
			String updateDate = "";
			if(null != objects[8]){
				updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[8]).substring(0,String.valueOf(objects[8]).lastIndexOf("."))), "yyyy-MM-dd");
			}else{
				if(null != objects[7]){
					updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[7]).substring(0,String.valueOf(objects[7]).lastIndexOf("."))), "yyyy-MM-dd");
				}
			}
			row.put("updateDate", updateDate);//更新日期
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 查询内控要求信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：公司或部门
	 * @param limit 页面显示记录数
	 * @param start 开始数
	 * @param query 查询条件
	 * @param sort 排序
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findstandardbysome.f")
	public Map<String,Object> findStandardBySome(String orgId,String limit,String start,String query,String sort){
		StringBuffer sortDirection = new StringBuffer();
		if(StringUtils.isNotBlank(sort)){//排序
			JSONArray jsonArray=JSONArray.fromObject(sort);
			if(jsonArray.size()!=0){
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String property = jsonObject.getString("property");
					String direction = jsonObject.getString("direction");
					if("standardCode".equals(property)){
						sortDirection.append("s.STANDARD_CODE ").append(direction);
					}else if("standardName".equals(property)){
						sortDirection.append("s.STANDARD_NAME ").append(direction);
					}else if("controlLevel".equals(property)){
						sortDirection.append("e1.DICT_ENTRY_NAME ").append(direction);
					}else if("controlPoint".equals(property)){
						sortDirection.append("e2.DICT_ENTRY_NAME ").append(direction);
					}else if("dealStatus".equals(property)){
						sortDirection.append("e3.DICT_ENTRY_NAME ").append(direction);
					}else if("parentName".equals(property)){
						sortDirection.append("ps.STANDARD_NAME ").append(direction);
					}else if("orgName".equals(property)){
						sortDirection.append("o.ORG_NAME ").append(direction);
					}else if("updateDate".equals(property)){
						sortDirection.append("s.LAST_MODIFY_TIME ").append(direction).append(", s.CREATE_TIME ").append(direction);
					}
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Object[]> objectsList = o_icmMyDatasBO.findStandardBySome(result,orgId,query,start,limit,sortDirection.toString());
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for(Object[] objects: objectsList){
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", null!=objects[0]?objects[0]:"");
			row.put("standardCode",  null!=objects[1]?objects[1]:"");//要求编号
			row.put("standardName", null!=objects[2]?objects[2]:"");//要求名称
			row.put("controlLevel",  null!=objects[4]?objects[4]:"");//控制层级中文
			row.put("controlPoint",  null!=objects[6]?objects[6]:"");//控制要素中文
			row.put("dealStatus",  null!=objects[8]?objects[8]:"");//处理状态中文
			row.put("parentId",  null!=objects[9]?objects[9]:"");//内控标准ID
			row.put("parentName",  null!=objects[10]?objects[10]:"");//内控标准名称
			row.put("orgId",  null!=objects[11]?objects[11]:"");//机构ID
			row.put("orgName",  null!=objects[12]?objects[12]:"");//机构名称
			String updateDate = "";
			if(null != objects[14]){
				updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[14]).substring(0,String.valueOf(objects[14]).lastIndexOf("."))), "yyyy-MM-dd");
			}else{
				if(null != objects[13]){
					updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[13]).substring(0,String.valueOf(objects[13]).lastIndexOf("."))), "yyyy-MM-dd");
				}
			}
			row.put("updateDate", updateDate);//更新日期
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	/**
	 * <pre>
	 * 统计内控要求的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：公司或部门
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findstandardcountbysome.f")
	public Map<String,Object> findStandardCountBySome(String orgId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findStandardCountBySome(orgId);
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("orgId", null!=objects[0]?objects[0]:"");//机构ID
			row.put("orgName", null!=objects[1]?objects[1]:"");//机构名称
			row.put("parentId", null!=objects[2]?objects[2]:"");//内控标准ID
			row.put("parentName", null!=objects[3]?objects[3]:"");//内控标准名称
			row.put("dealStatus", null!=objects[5]?objects[5]:"");//处理状态
			row.put("controlPoint", null!=objects[7]?objects[7]:"");//控制要素
			row.put("controlLevel", null!=objects[9]?objects[9]:"");//控制层级
			row.put("createYear", null!=objects[10]?objects[10]:"");//控制层级
			row.put("standardCount", objects[11]);//内控要求数量
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * 我的制度 ：制度分类 制度编号 制度名称 责任部门
	 * @param orgid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/statics/icmmyinstitutiondatas.f")
	public Map<String,Object> findMyInstitutionDatasByOrgId(String orgid,String limit,String start,boolean pagable,String query){
		
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		/*List<Object[]> objects = o_icmMyDatasBO.findMyInstitutionDatasByOrgId(result,orgid,limit,query,start);
		if(!pagable){//每个小块儿中的gird传送limit参数
			for(Object[] o: objects){
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", o[0]);
				row.put("code", o[2]);
				row.put("name", o[3]);
				datas.add(row);
			}
		}else{
			for(Object[] o: objects){//更多
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", o[0]);
				row.put("classify", null!=o[1]?this.o_ruleBO.findRuleByRuleId(o[1].toString()).getName():"");//分类
				row.put("code", o[2]);
				row.put("name", o[3]);
				row.put("org", null!=o[4]?this.o_organizationBO.getRootOrgByCompanyId(o[4].toString()).getOrgname():"");
				datas.add(row);
			}
		}*/
		result.put("datas", datas);
		return result;
	}
	/**
	 * 判断当前登录人是否为内控部门员工
	 * @param empid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/statics/judgeificmdept.f")
	public boolean judgeIfIcmDept(){
		return this.o_icmMyDatasBO.judgeIfIcmDept();
	}
	/**
	 * <pre>
	 * 统计缺陷数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：部门或
	 * @param planId 评价计划ID
	 * @param iPlanId 整改计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/finddefectcountbysome.f")
	public Map<String,Object> findDefectCountBySome(String orgId, String planId, String iPlanId, String processId, String processPointId, String measureId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findDefectCountBySome(orgId, planId, iPlanId, processId, processPointId, measureId);
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("orgId", null!=objects[0]?objects[0]:"");//机构ID
			row.put("orgName", null!=objects[1]?objects[1]:"");//机构名称
			row.put("planId", null!=objects[2]?objects[2]:"");//评价计划ID
			row.put("planName", null!=objects[3]?objects[3]:"");//评价计划名称
			row.put("processId", null!=objects[4]?objects[4]:"");//流程ID
			row.put("processName", null!=objects[5]?objects[5]:"");//流程名称
			row.put("processPointId", null!=objects[6]?objects[6]:"");//流程节点ID
			row.put("processPointName", null!=objects[7]?objects[7]:"");//流程节点名称
			row.put("measureId", null!=objects[8]?objects[8]:"");//控制措施ID
			row.put("measureName", null!=objects[9]?objects[9]:"");//控制措施名称
			row.put("assessPointId", null!=objects[10]?objects[10]:"");//评价点ID
			row.put("assessPointName", null!=objects[11]?objects[11]:"");//评价点名称
			row.put("dealStatus", null!=objects[12]?objects[12]:"");//整改状态
			row.put("defectCount", objects[15]);//缺陷数量
			row.put("defectLevel", null!=objects[16]?objects[16]:"");//缺陷等级
			row.put("defectType", null!=objects[17]?objects[17]:"");//缺陷类型
			row.put("createYear", null!=objects[18]?objects[18]:"");//年份
			row.put("iPlanId", null!=objects[19]?objects[19]:"");//整改计划ID
			row.put("iPlanName", null!=objects[20]?objects[20]:"");//整改计划名称
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 查询缺陷信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID
	 * @param query 查询条件
	 * @param planId 评价计划ID
	 * @param iPlanId 整改计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/finddefectbysome.f")
	public Map<String,Object> findDefectBySome(String orgId, String query, String planId, String processId, String processPointId, String measureId,String limit,String start,String sort){
		
		StringBuffer sortDirection = new StringBuffer();
		if(StringUtils.isNotBlank(sort)){//排序
			JSONArray jsonArray=JSONArray.fromObject(sort);
			if(jsonArray.size()!=0){
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String property = jsonObject.getString("property");
					String direction = jsonObject.getString("direction");
					if("orgName".equals(property)){
						sortDirection.append("o.ORG_NAME ").append(direction);
					}else if("planName".equals(property)){
						sortDirection.append("plan.PLAN_NAME ").append(direction);
					}else if("processName".equals(property)){
						sortDirection.append("p.PROCESSURE_NAME ").append(direction);
					}else if("processPointName".equals(property)){
						sortDirection.append("c.CONTROL_POINT_NAME ").append(direction);
					}else if("measureName".equals(property)){
						sortDirection.append("m.MEASURE_NAME ").append(direction);
					}else if("assessPointName".equals(property)){
						sortDirection.append("ap.EDESC ").append(direction);
					}else if("dealStatus".equals(property)){
						sortDirection.append("d.DEAL_STATUS ").append(direction);
					}else if("defectDesc".equals(property)){
						sortDirection.append("d.EDESC ").append(direction);
					}else if("defectLevel".equals(property)){
						sortDirection.append("e1.DICT_ENTRY_NAME ").append(direction);
					}else if("defectType".equals(property)){
						sortDirection.append("e2.DICT_ENTRY_NAME ").append(direction);
					}else if("updateDate".equals(property)){
						sortDirection.append("d.LAST_MODIFY_TIME ").append(direction).append(", d.CREATE_TIME ").append(direction);
					}
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findDefectBySome(result,orgId, query, planId, processId, processPointId, measureId, start, limit, sortDirection.toString());
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("orgId", null!=objects[0]?objects[0]:"");//机构ID
			row.put("orgName", null!=objects[1]?objects[1]:"");//机构名称
			row.put("planId", null!=objects[2]?objects[2]:"");//评价计划ID
			row.put("planName", null!=objects[3]?objects[3]:"");//评价计划名称
			row.put("processId", null!=objects[4]?objects[4]:"");//流程ID
			row.put("processName", null!=objects[5]?objects[5]:"");//流程名称
			row.put("processPointId", null!=objects[6]?objects[6]:"");//流程节点ID
			row.put("processPointName", null!=objects[7]?objects[7]:"");//流程节点名称
			row.put("measureId", null!=objects[8]?objects[8]:"");//控制措施ID
			row.put("measureName", null!=objects[9]?objects[9]:"");//控制措施名称
			row.put("assessPointId", null!=objects[10]?objects[10]:"");//评价点ID
			row.put("assessPointName", null!=objects[11]?objects[11]:"");//评价点名称
			row.put("dealStatus", null!=objects[12]?objects[12]:"");//整改状态
			row.put("id", objects[15]);//缺陷ID
			row.put("defectDesc", null!=objects[16]?objects[16]:"");//缺陷描述
			row.put("defectLevel", null!=objects[17]?objects[17]:"");//缺陷等级
			row.put("defectType", null!=objects[18]?objects[18]:"");//缺陷类型
			String updateDate = "";
			if(null != objects[20]){
				updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[20]).substring(0,String.valueOf(objects[20]).lastIndexOf("."))), "yyyy-MM-dd");
			}else{
				if(null != objects[19]){
					updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[19]).substring(0,String.valueOf(objects[19]).lastIndexOf("."))), "yyyy-MM-dd");
				}
			}
			row.put("updateDate", updateDate);//更新日期
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 统计评价结果的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：部门或公司
	 * @param planId 评价计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @param assessPointId 评价点ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findassessresultcountbysome.f")
	public Map<String,Object> findAssessResultCountBySome(String orgId, String planId, String empId, 
			String processId, String processPointId, String measureId, String assessPointId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findAssessResultCountBySome(orgId, planId, empId, 
				processId, processPointId, measureId, assessPointId);
		Integer allCount = 0;
		for (Object[] objects : objectList) {
			allCount += Integer.valueOf(String.valueOf(objects[10]));
		}
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("planId", null!=objects[0]?objects[0]:"");//评价计划ID
			row.put("planName", null!=objects[1]?objects[1]:"");//评价计划名称
			row.put("empId", null!=objects[2]?objects[2]:"");//员工ID
			row.put("empName", null!=objects[3]?objects[3]:"");//员工姓名
			row.put("processId", null!=objects[4]?objects[4]:"");//流程ID
			row.put("processName", null!=objects[5]?objects[5]:"");//流程名称
			/*row.put("processPointId", null!=objects[6]?objects[6]:"");//流程节点ID
			row.put("processPointName", null!=objects[7]?objects[7]:"");//流程节点名称
			row.put("measureId", null!=objects[8]?objects[8]:"");//控制措施ID
			row.put("measureName", null!=objects[9]?objects[9]:"");//控制措施名称
			row.put("assessPointId", null!=objects[10]?objects[10]:"");//评价点ID
			row.put("assessPointName", null!=objects[11]?objects[11]:"");//评价点名称
*/			row.put("assessMeasure", null!=objects[7]?objects[7]:"");//评价方式
			row.put("isValid", null!=objects[8]?objects[8]:"");//是否合格
			row.put("isFinished", null!=objects[9]?objects[9]:"");//是否完成
			if(allCount!=0){
				row.put("assessResultRate", (double)Integer.valueOf(String.valueOf(objects[10]))/allCount*100);//评价结果占比
			}else{
				row.put("assessResultRate", 0);//评价结果占比
			}
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 查询评价结果的明细
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID：公司或部门
	 * @param planId 评价计划ID
	 * @param empId 评价人ID
	 * @param processPointId 流程节点ID
	 * @param processId 流程ID
	 * @param measureId 控制措施ID
	 * @param assessPointId 评价点ID
	 * @param query 查询条件
	 * @param limit 限制数
	 * @param start 起始数
	 * @param sort 排序
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findassessresultbysome.f")
	public Map<String,Object> findAssessResultBySome(String orgId, String planId, String empId, String processPointId, 
			String processId,String measureId, String assessPointId, String query, String limit,String start,String sort){
		StringBuffer sortDirection = new StringBuffer();
		if(StringUtils.isNotBlank(sort)){//排序
			JSONArray jsonArray=JSONArray.fromObject(sort);
			if(jsonArray.size()!=0){
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String property = jsonObject.getString("property");
					String direction = jsonObject.getString("direction");
					if("planName".equals(property)){
						sortDirection.append("plan.PLAN_NAME ").append(direction);
					}else if("empName".equals(property)){
						sortDirection.append("e.EMP_NAME ").append(direction);
					}else if("processName".equals(property)){
						sortDirection.append("p.PROCESSURE_NAME ").append(direction);
					}else if("processPointName".equals(property)){
						sortDirection.append("c.CONTROL_POINT_NAME ").append(direction);
					}else if("measureName".equals(property)){
						sortDirection.append("m.MEASURE_NAME ").append(direction);
					}else if("assessPointName".equals(property)){
						sortDirection.append("ap.EDESC ").append(direction);
					}else if("assessMeasure".equals(property)){
						sortDirection.append("r.ASSESSMENT_MEASURE ").append(direction);
					}else if("isQualified".equals(property)){
						sortDirection.append("CASE WHEN ")  
								.append("r.HAS_DEFECT_ADJUST IS NULL ")
								.append("THEN ")
								.append("(CASE WHEN r.HAS_DEFECT IS NULL THEN NULL ELSE (CASE WHEN r.HAS_DEFECT='0' THEN '不合格' ELSE '合格' END)  END) ")
								.append("ELSE ")
								.append("(CASE WHEN r.HAS_DEFECT_ADJUST='0' THEN '不合格' ELSE '合格' END) ")
								.append("END ").append(direction);
					}else if("isDone".equals(property)){
						sortDirection.append("CASE WHEN ")  
						.append("r.has_defect IS NOT NULL ")
						.append("THEN ")
						.append("'已完成' ")
						.append("ELSE ")
						.append("'未完成' ")
						.append("END ").append(direction);
					}else if("updateDate".equals(property)){
						sortDirection.append("plan.LAST_MODIFY_TIME ").append(direction).append(", plan.CREATE_TIME ").append(direction);
					}
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findAssessResultBySome(result, orgId, planId, empId, processId, processPointId, measureId, assessPointId, query, start, limit, sortDirection.toString());
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("planId", null!=objects[0]?objects[0]:"");//评价计划ID
			row.put("planName", null!=objects[1]?objects[1]:"");//评价计划名称
			row.put("empId", null!=objects[2]?objects[2]:"");//评价人ID
			row.put("empName", null!=objects[3]?objects[3]:"");//评价人姓名
			row.put("processId", null!=objects[4]?objects[4]:"");//流程ID
			row.put("processName", null!=objects[5]?objects[5]:"");//流程名称
			row.put("processPointId", null!=objects[6]?objects[6]:"");//流程节点ID
			row.put("processPointName", null!=objects[7]?objects[7]:"");//流程节点名称
			row.put("measureId", null!=objects[8]?objects[8]:"");//控制措施ID
			row.put("measureName", null!=objects[9]?objects[9]:"");//控制措施名称
			row.put("assessPointId", null!=objects[10]?objects[10]:"");//评价点ID
			row.put("assessPointName", null!=objects[11]?objects[11]:"");//评价点名称
			row.put("assessMeasure", null!=objects[13]?objects[13]:"");//评价方式
			row.put("isQualified", null!=objects[14]?objects[14]:"");//样本合格状态
			row.put("isDone", null!=objects[15]?objects[15]:"");//评价完成状态
			String updateDate = "";
			if(null != objects[17]){
				updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[17]).substring(0,String.valueOf(objects[17]).lastIndexOf("."))), "yyyy-MM-dd");
			}else{
				if(null != objects[16]){
					updateDate = DateUtils.formatDate(DateUtils.parseTimeStamp(String.valueOf(objects[16]).substring(0,String.valueOf(objects[16]).lastIndexOf("."))), "yyyy-MM-dd");
				}
			}
			row.put("updateDate", updateDate);//更新日期
			row.put("sampleCount", null!=objects[18]?objects[18]:0);//样本数量
			row.put("id", null!=objects[19]?objects[19]:"");//ID
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 统计评价计划的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 公司ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findassessplancountbysome.f")
	public Map<String,Object> findAssessPlanCountBySome(String orgId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findAssessPlanCountBySome(orgId);
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("createYear", null!=objects[0]?objects[0]:"");//创建年份
			row.put("type", null!=objects[2]?objects[2]:"");//评价类型中文
			row.put("dealStatus", null!=objects[3]?objects[3]:"");//处理状态
			row.put("assessMeasure", null!=objects[5]?objects[5]:"");//评价方式中文
			row.put("planCount", null!=objects[6]?objects[6]:"");//评价计划数量
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 统计整改计划数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 公司ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findimprovecountbysome.f")
	public Map<String,Object> findImproveCountBySome(String orgId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findImproveCountBySome(orgId);
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("createYear", null!=objects[0]?objects[0]:"");//创建年份
			row.put("dealStatus", null!=objects[1]?objects[1]:"");//处理状态
			row.put("planCount", null!=objects[2]?objects[2]:"");//整改计划数量
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * <pre>
	 * 统计整改计划的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 公司ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/statics/findconstructplancountbysome.f")
	public Map<String,Object> findConstructPlanCountBySome(String orgId){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Object[]> objectList = o_icmMyDatasBO.findConstructPlanCountBySome(orgId);
		for (Object[] objects : objectList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", Identities.uuid());//ID
			row.put("createYear", null!=objects[0]?objects[0]:"");//创建年份
			row.put("type", null!=objects[2]?objects[2]:"");//评价类型中文
			row.put("dealStatus", null!=objects[3]?objects[3]:"");//处理状态
			row.put("planCount", null!=objects[4]?objects[4]:"");//评价计划数量
			datas.add(row);
		}
		result.put("datas", datas);
		return result;
	}
}
