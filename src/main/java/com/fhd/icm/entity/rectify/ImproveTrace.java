/**
 * ImproveTrace.java
 * com.fhd.icm.entity.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-26 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.rectify;

import java.io.Serializable;
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
import com.fhd.icm.entity.defect.Defect;

/**
 * 缺陷进度表
 * 
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:24:25
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVEMENT_TRACE")
public class ImproveTrace extends AuditableEntity implements Serializable {
	private static final long serialVersionUID = -2621145894200762092L;

	/**
	 * 整改方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVE_PLAN_ID")
	private ImprovePlan improvePlan;
	
	/**
	 * 评价缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
	
	/**
	 * 实际开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	private Date actualStartDate;
	
	/**
	 * 实际结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "FINISH_DATE", length = 7)
	private Date actualEndDate;			
		
	/**
	 * 完成进度
	 */
	@Column(name="FINISH_RATE")
	private Double finishRate;		
	
	/**
	 * 改进结果
	 */
	@Column(name="IMPROVE_RESULT",length=4000)
	private String improveResult;
	
	/**
	 * 说明
	 */
	@Column(name="ECOMMENT",length=4000)
	private String comment;	
	
	/**
	 * 状态
	 */
	@Column(name="ESTATUS")
	private String status;	
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;
	
	public ImproveTrace(){
		
	}
	
	public ImproveTrace(String id){
		super.setId(id);
	}
	
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Double getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(Double finishRate) {
		this.finishRate = finishRate;
	}

	public String getImproveResult() {
		return improveResult;
	}

	public void setImproveResult(String improveResult) {
		this.improveResult = improveResult;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
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
	
}

