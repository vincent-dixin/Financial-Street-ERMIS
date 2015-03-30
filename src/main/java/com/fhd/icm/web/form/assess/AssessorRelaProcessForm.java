/**
 * AssessorRelaProcessForm.java
 * com.fhd.icm.web.form.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-15 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.form.assess;

import java.io.Serializable;

import com.fhd.process.entity.Process;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-15		上午11:51:22
 *
 * @see 	 
 */
public class AssessorRelaProcessForm extends Process implements Serializable{

	private static final long serialVersionUID = -4945054754724545409L;
	/**
	 * 穿行的评价点总数
	 */
	public Integer allNumByPracticeTest;
	
	/**
	 * 抽样的评价点总数
	 */
	public Integer allNumBySampleTest;
	
	/**
	 * 待穿行的评价点数
	 */
	public Integer numByPracticeTest;
	
	/**
	 * 待抽样的评价点数
	 */
	public Integer numBySampleTest;
	
	public String assessorId;
	
	public String parentProcess;
	
	public String firstProcess;
	
	public String orgName;
	
	//风险水平
	private String assessLeaver;


	public String getAssessLeaver() {
		return assessLeaver;
	}

	public void setAssessLeaver(String assessLeaver) {
		this.assessLeaver = assessLeaver;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentProcess() {
		return parentProcess;
	}

	public void setParentProcess(String parentProcess) {
		this.parentProcess = parentProcess;
	}

	public String getFirstProcess() {
		return firstProcess;
	}

	public void setFirstProcess(String firstProcess) {
		this.firstProcess = firstProcess;
	}

	public Integer getAllNumByPracticeTest() {
		return allNumByPracticeTest;
	}

	public void setAllNumByPracticeTest(Integer allNumByPracticeTest) {
		this.allNumByPracticeTest = allNumByPracticeTest;
	}

	public Integer getAllNumBySampleTest() {
		return allNumBySampleTest;
	}

	public void setAllNumBySampleTest(Integer allNumBySampleTest) {
		this.allNumBySampleTest = allNumBySampleTest;
	}

	public Integer getNumByPracticeTest() {
		return numByPracticeTest;
	}

	public void setNumByPracticeTest(Integer numByPracticeTest) {
		this.numByPracticeTest = numByPracticeTest;
	}

	public Integer getNumBySampleTest() {
		return numBySampleTest;
	}

	public void setNumBySampleTest(Integer numBySampleTest) {
		this.numBySampleTest = numBySampleTest;
	}

	public String getAssessorId() {
		return assessorId;
	}

	public void setAssessorId(String assessorId) {
		this.assessorId = assessorId;
	}


	
}

