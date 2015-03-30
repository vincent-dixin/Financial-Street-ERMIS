package com.fhd.icm.business.bpm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.standard.StandardBO;
import com.fhd.icm.business.standard.StandardRelaOrgBO;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.interfaces.standard.IStandardBO;
import com.fhd.icm.web.controller.bpm.StandardBpmObject;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * Standard_内控BpmBO ClassName:StandardBpmBO
 * 
 * @author 元杰
 * @version
 * @since Ver 1.1
 * @Date 2013-4-11 上午9:36:00
 * 
 * @see
 */
@Service
public class StandardBpmBO implements IStandardBO {
	@Autowired
	private StandardBO o_standardBO;
	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private JBPMOperate o_jbpmOperate;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;
	@Autowired
	private StandardRelaOrgBO o_standardRelaOrgBO;
	
	/**
	 * <pre>
	 * 内控标准分发，给三个角色赋数据
	 * </pre>
	 * 
	 * @author 元杰
	 * @param standardControlIds 对应的标准ID集合
	 * @param improveId 整改计划ID
	 * @param ICByTestingDepartmentStaffRoleKey 被测试部门人员的角色编号
	 * @param ICDepartmentMinisterRoleKey 内控部门部长的角色编号
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<StandardBpmObject> returnStandardBpmObjList(String[] standardControlIds){
		//存放工作流3、4、5步中的人员及其他数据
		List<StandardBpmObject> standardBpmObjList = new ArrayList<StandardBpmObject>();
		List<String> orgIdList = new ArrayList<String>();
		List<StandardRelaOrg> sros = new ArrayList<StandardRelaOrg>();
		//内控要求对应OrgID,去重
		for(String standardControlId : standardControlIds){
			StandardRelaOrg sro = o_standardRelaOrgBO.findStandardRelaOrgByStandardId(standardControlId);
			sros.add(sro);//将标准下挂要求的所有StandardRelaOrg关系保存
			if(!orgIdList.contains(sro.getOrg().getId())){
				orgIdList.add(sro.getOrg().getId());
			}
		}
		
		for(String orgId : orgIdList){
			StandardBpmObject standardBpmObj = new StandardBpmObject();
			Set<String> orgIdSet = new HashSet<String>(); 
			orgIdSet.add(orgId);
			
			String roleName3 = ResourceBundle.getBundle("application").getString("ICByTestingDepartmentMinister");//被测试部门部长,节点3
			List<Object[]> empObj3 = o_employeeBO.findEmployeeListByOrgIdsAndRoleName(orgIdSet, roleName3);
			String[] empIds4 = this.findStandardPlanEmpIdByRole("ICDepartmentStaff");//内控部门员工,节点4
			String[] empIds5 = this.findStandardPlanEmpIdByRole("ICDepartmentMinister");//内控部门部长,节点5
			String empId5 = "";
			if(null != empIds5 && empIds5.length>0){
				empId5 = empIds5[0];
			}
			
			//判断以下三个节点的人不为空
			if(null != empObj3 && empObj3.size() > 0 && null != empIds4 && empIds4.length > 0 && StringUtils.isNotBlank(empId5)){
				standardBpmObj.setReviewerEmpId(empObj3.get(0)[0].toString());
				standardBpmObj.setReportorEmpId(empIds4[0]);
				standardBpmObj.setApproverEmpId(empId5);
			}else{
				//有角色没有设置的则跳过此代办
				break;
			}
			//设置standardBpmObj中的对应内控要求，针对部门进行分类。
			List<StandardRelaOrg> sroTemp = new ArrayList<StandardRelaOrg>();
			for(StandardRelaOrg sro : sros){
				if(sro.getOrg().getId().equals(orgId)){
					sroTemp.add(sro);
				}
			}
			String[] controlIds = new String[sroTemp.size()];
			int i = 0;
			for(StandardRelaOrg sro : sroTemp){
				controlIds[i] = sro.getStandard().getId();
				++i;
			}
			
			standardBpmObj.setStandardControlIds(controlIds);
			
			standardBpmObjList.add(standardBpmObj);
		}
		return standardBpmObjList;
	}
	
	/**
	 * 根据流程实例ID获得类型为StandardBpmObject的流程变量
	 * @author 元杰
	 * @param executionId 流程实例ID
	 * @return StandardBpmObject
	 * @since  fhd　Ver 1.1
	*/
	public StandardBpmObject findStandardBpmObjectByExecutionId(String executionId){
		return (StandardBpmObject) o_jbpmOperate.getVariable1(executionId, "item");
	}
	
	/**
	 * 下级公司开启新的内控要求代办
	 * @author 元杰
	 * @param standardId 
	 * @param subCompanyIdList 
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startSubCompanyStandardApply(String standardId, Set<String> subCompanyIdList) throws IllegalAccessException, InvocationTargetException {
		Standard standard = o_standardBO.findStandardById(standardId);
		String roleName1 = ResourceBundle.getBundle("application").getString("ICDepartmentStaff");//内控部门员工长,节点1
		String roleName2 = ResourceBundle.getBundle("application").getString("ICDepartmentMinister");//被测试部门部长,节点2
		String companyId = standard.getCompanyId();//得到内控标注对应公司ID
		List<SysEmployee> roleList1 = o_sysRoleBO.getEmpByCorpAndRole(companyId, roleName1);
		List<SysEmployee> roleList2 = o_sysRoleBO.getEmpByCorpAndRole(companyId, roleName2);
		
		if(null != roleList1 && roleList1.size() > 0 && null != roleList2 && roleList2.size() > 0){
			//JBPM
			Map<String, Object> variables=new HashMap<String, Object>();
			String entityType = "icmStandardPlan";
			variables.put("entityType",entityType);
			variables.put("ICDepartmentStaffEmpId", roleList1.get(0).getUsername());//设置当前处理人
			variables.put("ICDepartmentMinisterEmpId", roleList2.get(0).getUsername());//设置下一节点处理人
			variables.put("id", standard.getId());
			variables.put("name", standard.getName());
			o_jbpmBO.startProcessInstance(entityType, variables);
		}
	}
	
	/**
	 * 本公司开启新的内控标准代办 第一步
	 * @author 元杰
	 * @param businessId 
	 * @param executionId 流程实例ID
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startCurCompanyStandardApplyStepOne(String executionId, String businessId, Standard standard){
		//JBPM
		Map<String, Object> variables=new HashMap<String, Object>();
		String entityType = "icmStandardPlan";
		variables.put("entityType",entityType);
		variables.put("ICDepartmentStaffEmpId",UserContext.getUser().getEmpid());//设置当前处理人
		Set<String> orgIdSet = new HashSet<String>(); 
		if(null != o_standardRelaOrgBO.findStandardRelaOrgByStandardId(standard.getId())){
			StandardRelaOrg sro = o_standardRelaOrgBO.findStandardRelaOrgByStandardId(standard.getId());
			orgIdSet.add(sro.getOrg().getId());
		}
		//ICDepartmentStaff=内控部门员工
		String[] empIds = this.findStandardPlanEmpIdByRole("ICDepartmentStaff");
		variables.put("ICDepartmentMinisterEmpId",empIds[0]);//设置下一节点处理人
		variables.put("id", standard.getId());
		variables.put("name", standard.getName());
		if(StringUtils.isBlank(executionId)){
			executionId = o_jbpmBO.startProcessInstance(entityType, variables);
			o_jbpmBO.doProcessInstance(executionId, variables);
		}else{
			o_jbpmBO.doProcessInstance(executionId, variables);
		}
	}
	
	/**
	 * 本公司开启新的内控标准代办 第二步
	 * @author 元杰
	 * @param businessId 
	 * @param executionId 流程实例ID
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startCurCompanyStandardApplyStepTwo(String executionId, String businessId, Standard standard,
			String[] standardControlIds, String isPass, String examineApproveIdea){
		List<StandardBpmObject> standardBpmObjList = this.returnStandardBpmObjList(standardControlIds);
		//JBPM
		if(null != standardBpmObjList && standardBpmObjList.size()>0){
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("path", isPass);
			variables.put("examineApproveIdea", examineApproveIdea);
			variables.put("items", standardBpmObjList);
			variables.put("joinCount", standardBpmObjList.size());//填写进度及复核的任务数
			o_jbpmBO.doProcessInstance(executionId, variables);
		}
	}
	
	/**
	 * 本公司开启新的内控标准代办 第三步
	 * @author 元杰
	 * @param businessId 
	 * @param executionId 流程实例ID
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startCurCompanyStandardApplyStepThree(String executionId, String businessId, Standard standard){
		//JBPM
		Map<String, Object> variables=new HashMap<String, Object>();
		if(StringUtils.isNotBlank(executionId)){
			o_jbpmBO.doProcessInstance(executionId, variables);
		}
	}
	
	/**
	 * 本公司开启新的内控标准代办 第四步
	 * @author 元杰
	 * @param businessId 
	 * @param executionId 流程实例ID
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startCurCompanyStandardApplyStepFour(String executionId, String businessId, Standard standard){
		//JBPM
		Map<String, Object> variables=new HashMap<String, Object>();
		if(StringUtils.isNotBlank(executionId)){
			o_jbpmBO.doProcessInstance(executionId, variables);
		}
	}
	
	/**
	 * 本公司开启新的内控标准代办 第五步
	 * @author 元杰
	 * @param businessId 
	 * @param executionId 流程实例ID
	 * @return subCompanyStandardControlIdList 下级机构对应的要求ID集合，所有下级公司对应的，未分类
	 * @since  fhd　Ver 1.1
	*/
	public void startCurCompanyStandardApplyStepFive(String executionId, String businessId, String isPass, String examineApproveIdea){
		//JBPM
		Map<String, Object> variables=new HashMap<String, Object>();
		if(StringUtils.isNotBlank(executionId)){
			variables.put("path", isPass);
			variables.put("examineApproveIdea", examineApproveIdea);
			o_jbpmBO.doProcessInstance(executionId, variables);
		}
	}
	
	/**
	 * 从配置文件中获得对应角色的人员Id
	 * @author 元杰
	 * @param roleKye :配置文件中角色的属性名称
	 * @return String
	 */
	public String[] findStandardPlanEmpIdByRole(String roleKey){
		String[] empIds = null;
		//配置文件读取文件路径
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
}
