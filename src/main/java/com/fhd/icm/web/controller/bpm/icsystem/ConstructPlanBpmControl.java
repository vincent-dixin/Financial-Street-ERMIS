package com.fhd.icm.web.controller.bpm.icsystem;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.icm.business.bpm.icsystem.ConstructPlanBpmBO;
import com.fhd.icm.business.icsystem.ConstructPlanBO;

/**
 * 体系建设涉及到的工作流操作
 * @author 宋佳
 * @since 2013-4-10
 */
@Controller
public class ConstructPlanBpmControl {

	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private ConstructPlanBpmBO o_constructPlanBpmBO;
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	/**
	 * 体系建设计划制定.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplandraft.f")
	public void constructPlanDraft(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.mergeConstructPlanStatusBpmPlanDraft(executionId, businessId);
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
	/**
	 * 建设计划审批.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param isPass 审批:通过--‘yes’,不通过--‘no’
	 * @param examineApproveIdea 审批意见
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplanapproval.f")
	public void constructPlanApproval(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			o_constructPlanBpmBO.mergeConstructPlanApproval(executionId, businessId, isPass, examineApproveIdea);
			
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
	/**
	 * 计划任务发布.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplanpublish.f")
	public void constructPlanPublish(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.mergeConstructPlanPublish(executionId,businessId);
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
	/**
	 * 流程梳理提交.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplanrelaprocesssubmit.f")
	public void constructPlanRelaProcessSubmit(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.constructPlanRelaProcessSubmit(executionId, businessId);
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
	/**
	 * 建设计划流程梳理成果审批.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param isPass 审批:通过--‘yes’,不通过--‘no’
	 * @param examineApproveIdea 审批意见
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/planprocesseditapproval.f")
	public void planProcessEditApproval(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.mergePlanProcessEditApproval(executionId, businessId, isPass, examineApproveIdea);
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
	/**
	 * 梳理完毕发布
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplanresultspublish.f")
	public void ConstructPlanResultsPublish(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.ConstructPlanResultsPublish(executionId, businessId);
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
	/**
	 * 流程修复完毕提交
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/constructplanresultsrepair.f")
	public void ConstructPlanResultsRepair(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.ConstructPlanResultsRepair(executionId, businessId);
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
	/**
	 * 合规诊断任务提交
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/icsystem/diagnosessubmit.f")
	public void diagnosesSubmit(String executionId, String constructPlanId, HttpServletResponse response,String modifiedRecord) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.diagnosesSubmit(executionId,constructPlanId,modifiedRecord);
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
	/**
	 * <pre>
	 * 保存缺陷反馈意见
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointForm
	 * @param parentId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/submitdiagnosesdefect.f")
	public void submitDiagnosesDefect(String executionId,String constructPlanId,HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.submitDiagnosesDefect(executionId,constructPlanId);
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
	/**
	 * <pre>
	 * 提交工作流
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointForm
	 * @param parentId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/submitdefectclearup.f")
	public void submitDefectClearUp(String executionId,String constructPlanId,HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			o_constructPlanBpmBO.submitDefectClearUp(executionId,constructPlanId);
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