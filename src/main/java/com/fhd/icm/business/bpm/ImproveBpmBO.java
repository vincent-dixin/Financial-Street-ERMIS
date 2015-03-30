package com.fhd.icm.business.bpm;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.defect.DefectRelaImproveBO;
import com.fhd.icm.business.rectify.ImprovePlanBO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.rectify.ImprovePlan;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;
import com.fhd.icm.entity.rectify.ImprovePlanRelaOrg;
import com.fhd.icm.entity.rectify.ImproveRelaPlan;
import com.fhd.icm.web.controller.bpm.ImproveBpmObject;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 内控整改计划DAO.
 * @author 吴德福
 */
@Service
public class ImproveBpmBO {

	@Autowired
	private SysRoleBO o_sysRoleBO;
	
	@Autowired
	private DefectRelaImproveBO o_defectRelaImproveBO;
	
	@Autowired
	private ImprovePlanBO o_improvePlanBO;
	
	@Autowired
	private JBPMOperate o_jBPMOperate;
	
	/**
	 * 根据整改计划id查询整改计划制定审批人.
	 * @return String
	 */
	public String findRectifyEmpIdByRole(String roleKey){
		String empId = "";
		
		//配置文件读取文件路径：rectifyDraftRoleName=内控部门部长
		String roleName = ResourceBundle.getBundle("application").getString(roleKey);
		List<SysEmployee> employeeList = o_sysRoleBO.getEmpByCorpAndRole(roleName);
		if(null != employeeList && employeeList.size()>0){
			SysEmployee sysEmployee = employeeList.get(0);
			if(null != sysEmployee){
				empId = sysEmployee.getId();
			}
		}
		return empId;
	}
	
	
	/**
	 * <pre>
	 *根据整改计划id进得分发
	 * </pre>
	 * 
	 * @author 李克东
	 * @param roleKey
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public String[] findRectifyEmpIdsByRole(String roleKey){
		String[] empIds = null;
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
	
	
	/**
	 * <pre>
	 *整改计划汇总
	 * </pre>
	 * 
	 * @author 李克东
	 * @param roleKey
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Integer findRectifyEmpIdNumByRole(String roleKey){
		String roleName = ResourceBundle.getBundle("application").getString(roleKey);
		List<SysEmployee> employeeList = o_sysRoleBO.getEmpByCorpAndRole(roleName);
		return employeeList.size();
	}
	
	/**
	 * <pre>
	 * 整改方案指定完复核人分发的流程参数中涉及的人和任务的参数
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveId 整改计划ID
	 * @param ICByTestingDepartmentStaffRoleKey 被测试部门人员的角色编号
	 * @param ICDepartmentMinisterRoleKey 内控部门部长的角色编号
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImproveBpmObject> findDistributeParameterForBpmTask(String improveId,String ICByTestingDepartmentMinisterKey, String ICDepartmentMinisterRoleKey){
		String ICDepartmentMinisterRoleKeyEmpId = findRectifyEmpIdByRole(ICDepartmentMinisterRoleKey);//内控部门部长
		String[] ICByTestingDepartmentStaffEmpIds = findRectifyEmpIdsByRole(ICByTestingDepartmentMinisterKey);//被测试部门部长
		List<ImproveBpmObject> bpmObjectList = new ArrayList<ImproveBpmObject>();
		List<DefectRelaImprove> defectRelaImproveList = o_defectRelaImproveBO.findDefectRelaImproveListBySome(null, improveId);
		List<ImproveRelaPlan> improveRelaPlanList = o_improvePlanBO.findImproveRelaPlanListBySome(improveId,null);
		if(StringUtils.isNotBlank(ICDepartmentMinisterRoleKeyEmpId) && null != ICByTestingDepartmentStaffEmpIds 
				&& ICByTestingDepartmentStaffEmpIds.length>0){
			for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
				Defect defect = defectRelaImprove.getDefect();
				Set<ImprovePlanRelaDefect> improvePlanRelaDefectSet = defect.getImprovePlanRelaDefect();
				for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectSet) {
					ImprovePlan improvePlan = improvePlanRelaDefect.getImprovePlan();
					for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
						if(improveRelaPlan.getImprovePlan().getId().equals(improvePlan.getId())){
							ImproveBpmObject obj = new ImproveBpmObject();
							obj.setImprovePlanRelaDefectId(improvePlanRelaDefect.getId());
							obj.setApproverEmpId(ICDepartmentMinisterRoleKeyEmpId);
							Set<ImprovePlanRelaOrg> improvePlanRelaOrgSet = improvePlan.getImprovePlanRelaOrg();
							for (ImprovePlanRelaOrg improvePlanRelaOrg : improvePlanRelaOrgSet) {
								if(Contents.EMP_REVIEW_PERSON.equals(improvePlanRelaOrg.getType())){//方案复核人
									obj.setReviewerEmpId(improvePlanRelaOrg.getEmp().getId());
								}
								//方案进度填写人为最后修改方案的人
								obj.setReportorEmpId(improvePlanRelaOrg.getImprovePlan().getLastModifyBy().getId());
								
								/*if(Contents.EMP_HANDLER.equals(improvePlanRelaOrg.getType())){//方案进度填写人
									obj.setReportorEmpId(improvePlanRelaOrg.getEmp().getId());
								}*/
							}
							bpmObjectList.add(obj);
						}
					}
				}
			}
		}
		return bpmObjectList;
	}
	
	/**
	 * <pre>
	 * 根据流程实例ID获得类型为BpmObject的流程变量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param executionId 流程实例ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public ImproveBpmObject findBpmObjectByExecutionId(String executionId){
		return (ImproveBpmObject)o_jBPMOperate.getVariable1(executionId, "item");
	}
	
}
