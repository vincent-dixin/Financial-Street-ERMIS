/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.icm.entity.rectify;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.icm.entity.defect.Defect;

/**
 * 整改方案涉及缺陷
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-17  上午10:34:01
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_DEFECT_IMPROVE")
public class ImprovePlanRelaDefect extends AuditableEntity implements Serializable {


	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -6065084073164538345L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVEMENT_ID")
	private ImprovePlan improvePlan;
	
	/**
	 * 缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
	
	/**
	 * 整改结果
	 */
	@Column(name="IMPROVE_RESULT",length=4000)
	private String improveResult;	
		
	/**
	 * 整改测试
	 */
	@Column(name="IMPROVE_TEST",length=4000)
	private String improveTest;	
	
	/**
	 * 测试分析
	 */
	@Column(name="TEST_ANALYZE",length=4000)
	private String testAnalyze;	
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;

	public ImprovePlanRelaDefect(){
		
	}
	
	public ImprovePlanRelaDefect(String id){
		super.setId(id);
	}

	public ImprovePlan getImprovePlan() {
		return improvePlan;
	}

	public void setImprovePlan(ImprovePlan improvePlan) {
		this.improvePlan = improvePlan;
	}

	public Defect getDefect() {
		return defect;
	}

	public void setDefect(Defect defect) {
		this.defect = defect;
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

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
}

