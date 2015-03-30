/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.icm.entity.rectify;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 整改复核实体
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-17  上午9:57:14
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_DEFECT_TRACE")
public class DefectTrace extends AuditableEntity {

	
	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -3341092659048047710L;

	/**
	 * 整改方案涉及缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_IMPROVE_ID")
	private ImprovePlanRelaDefect improvePlanRelaDefect;
	
	/**
	 * 复核测试情况
	 */
	@Column(name = "COMPENSATION_CONTROL")
	private String compensationControl;
	
	/**
	 * 下次观察日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NEXT_CHECK_DATE")
	private Date nextCheckDate;
	
	/**
	 * 备注
	 */
	@Column(name = "CHECK_RESULT")
	private String checkResult;
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DELETE_STATUS")
	private DictEntry deleteStatus;

	
	
	/**
	 * 整改后的缺陷等级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="AFTER_DEFECT_LEVEL")
	private DictEntry afterDefectLevel;
	
	/**
	 * 整改是否通过：是/否
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="IS_PASS")
	private DictEntry isPass;
		
	public DefectTrace(){
		
	}
	
	public DefectTrace(String id){
		super.setId(id);
	}
	
	public ImprovePlanRelaDefect getImprovePlanRelaDefect() {
		return improvePlanRelaDefect;
	}

	public void setImprovePlanRelaDefect(ImprovePlanRelaDefect improvePlanRelaDefect) {
		this.improvePlanRelaDefect = improvePlanRelaDefect;
	}

	public String getCompensationControl() {
		return compensationControl;
	}

	public void setCompensationControl(String compensationControl) {
		this.compensationControl = compensationControl;
	}

	public Date getNextCheckDate() {
		return nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public DictEntry getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(DictEntry deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public DictEntry getAfterDefectLevel() {
		return afterDefectLevel;
	}

	public void setAfterDefectLevel(DictEntry afterDefectLevel) {
		this.afterDefectLevel = afterDefectLevel;
	}

	public DictEntry getIsPass() {
		return isPass;
	}

	public void setIsPass(DictEntry isPass) {
		this.isPass = isPass;
	}

}

