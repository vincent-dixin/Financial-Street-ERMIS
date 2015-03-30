package com.fhd.process.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.business.ProcessTreeBO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.process.web.form.ProcessForm;

/**
 * 流程控制
 * 
 * @author 李克东
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-12-11 上午10:35:34
 */
@Controller
public class ProcessControl {

	@Autowired
	private ProcessTreeBO o_processTreeBO;
	@Autowired
	private ProcessBO o_processBO;

	/**
	 * <pre>
	 * 查询流程树子节点
	 * </pre>
	 * 
	 * @author 李克东
	 * @param id
	 * canChecked 是否有复选框 ；query查询条件； type 类型
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/process/processTree/findrootProcessTreeLoader.f")
	public List<Map<String, Object>> findProcessTreeLoader(String node,
			boolean canChecked, String query, String type) {
		List<Map<String, Object>> result=o_processTreeBO.processTreeLoader(node, canChecked, query, type);
		return result;//o_processTreeBO.processTreeLoader(node, canChecked, query, type);
	}
	/**
	 * <pre>
	 *加载流程表单
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/editProcess.f")
	public Map<String,Object> findProcessForm(String processEditID){
		Map<String,Object> processMap=o_processBO.findProcessForm(processEditID);
		return processMap;
		
	}
	/**
	 * <pre>
	 *加载流程表单
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/process/process/constructplaneditProcess.f")
	public Map<String,Object> findconstructProcessForm(String processEditID){
		Map<String,Object> processMap=o_processBO.findconstructProcessForm(processEditID);
		return processMap;
		
	}
	/**
	 * <pre>
	 *  加载流程forview
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/process/process/findconstructprocessformforview.f")
	public Map<String,Object> findconstructProcessFormForView(String processEditID){
		Map<String,Object> processMap=o_processBO.findconstructProcessFormForView(processEditID);
		return processMap;
		
	}
	/**
	 * <pre>
	 *初始化流程组件，
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processIds：ids
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/findProcessByIds.f")
	public List<Map<String,Object>> findProcessByIds(String processIds){
		List<Map<String,Object>> lm= new ArrayList<Map<String,Object>>();
		lm=o_processBO.findProcessByIds(processIds);
		return lm;
	}
	/**
	 * <pre>
	 *通过部门Id获得相关联的流程
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param orgIds:部门的Id串（‘,’分割）
	 * @return
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/findProcessByOrgIds.f")
	public Map<String,Object> findProcessByOrgIds(String orgIds,HttpServletResponse response) throws IOException{
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		String[] orgIdArray=orgIds.split(",");
		List<Process> processList = o_processBO.findProcessByOrgIds(orgIdArray);
		List<Object[]> processRelaDefectList = o_processBO.findProcessRelaDefectList();
		if(null != processList && processList.size()>0){
			Map<String, Object> row = null;
			for (Process process : processList) {
				row = new HashMap<String, Object>();
				
				//流程id
				row.put("id", process.getId());
				//流程分类
				if(null != process.getParent()){
					row.put("parentName", process.getParent().getName());
				}else {
					row.put("parentName", "");
				}
				//流程名称
				row.put("text", process.getName());
				
				//TODO未实现--风险数量
				String riskCount = "";
				row.put("riskCount", riskCount);
				//TODO未实现--风险状态
				String riskStatus = "";
				row.put("riskStatus", riskStatus);
				
				for (Object[] objects : processRelaDefectList) {
					if(process.getId().equals(objects[0])){
						//缺陷数量
						row.put("defectCount", objects[1]);
						//缺陷状态
						row.put("defectStatus", objects[2]);
					}
				}
				
				datas.add(row);
			}
		}

		map.put("datas", datas);
		return map;
	}
	/**
	 * <pre>
	 *删除流程节点
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processID
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/removeProcess.f")
	public Map<String,Object> removeProcessByID(String processID){
		Map<String,Object> result=new HashMap<String,Object>();
		o_processBO.removeProcessByID(processID);
		result.put("success", true);
		return result;
	}
	/**
	 * <pre>
	 *流程保存
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processForm
	 * @param parentId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/saveProcess.f")
	public Map<String,Object> saveProcess(ProcessForm processForm,String parentId){
		Map<String,Object> result=new HashMap<String,Object>();
		o_processBO.saveProcess(processForm,parentId);
		result.put("success", true);
		return result;
    }
	/**
	 * <pre>
	 *自动生成编号
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processEditID
	 * @param parentId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/process/process/ProcessCode.f")
	public Map<String,Object> findProcessCode(String processEditID,String parentId){
		Map<String,Object> processMap=o_processBO.findProcessCode(processEditID, parentId);
		return processMap;
	}
	/**
	 * 根据bussnissId查询该用户所涉及到的
	 * @author 宋佳
	 * @param processId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/process/findprocesslistbypage.f")
	public Map<String,Object> findProcessListByPage(int limit, int start, String query,String constructPlanId,String executionId,String orgScoll){
		Map<String,Object> resultMap;
		Page<ConstructPlanRelaStandardEmp> page = new Page<ConstructPlanRelaStandardEmp>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
//		String companyId = UserContext.getUser().getCompanyid();
		resultMap=o_processBO.findProcessListByPage(page, query, constructPlanId,executionId,orgScoll);
		return resultMap;
		
		
	}
	/**
	 * 根据bussnissId查询该用户所涉及到的
	 * @author 宋佳
	 * @param processId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/process/findprocesslistapprovebypage.f")
	public Map<String,Object> findProcessListApproveByPage(int limit, int start, String query,String constructPlanId,String executionId,String orgScoll){
		Map<String,Object> resultMap;
		Page<ConstructPlanRelaStandardEmp> page = new Page<ConstructPlanRelaStandardEmp>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		resultMap=o_processBO.findProcessListApproveByPage(page, query, constructPlanId,executionId,orgScoll);
		return resultMap;
		
		
	}
}
