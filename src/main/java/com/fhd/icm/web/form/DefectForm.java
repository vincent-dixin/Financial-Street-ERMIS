package com.fhd.icm.web.form;

import com.fhd.icm.entity.defect.Defect;

/**
 * 缺陷表单
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-8		下午2:33:33
 *
 * @see 	 
 */
public class DefectForm extends Defect {
	private static final long serialVersionUID = -6652003700245725633L;

	/**
	 * 缺陷ID
	 */
	private String defectId;
	
	/**
	 * 部门关联
	 */
	private String org;
	/**
	 * 整改计划
	 */
	private String improve;
	/**
	 * 整改结果
	 */
	private String improveResult;
	/**
	 * 整改测试
	 */

	private String improveTest;	
	
	/**
	 * 测试分析
	 */
	private String testAnalyze;	
	/**
	 * 所属公司ID
	 */
	private String companyId;
	
	/**
	 * 缺陷等级的数据字典的ID
	 */
	private String levelId;
	
	/**
	 * 缺陷类型的数据字典的ID
	 */
	private String typeId;
	
	/**
	 * 流程的数据字典的ID
	 */
	private String processId;
	
	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getImproveResult() {
		return improveResult;
	}

	public void setImproveResult(String improveResult) {
		this.improveResult = improveResult;
	}

	public String getImproveTest() {
		return improveTest;
	}

	public void setImproveTest(String improveTest) {
		this.improveTest = improveTest;
	}

	public String getTestAnalyze() {
		return testAnalyze;
	}

	public void setTestAnalyze(String testAnalyze) {
		this.testAnalyze = testAnalyze;
	}

	public String getImprove() {
		return improve;
	}

	public void setImprove(String improve) {
		this.improve = improve;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}
	

}

