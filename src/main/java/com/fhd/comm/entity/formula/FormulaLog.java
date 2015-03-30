package com.fhd.comm.entity.formula;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 公式计算日志表.
 * @author   吴德福
 * @since    fhd Ver 4.5
 * @Date	 2012-12-8  上午10:28:47
 * @see 	 
 */
@Entity
@Table(name = "T_COM_FORMULA_LOG")
public class FormulaLog extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 对象id
	 */
	@Column(name = "OWNER_OBJECT_ID")
	private String objectId;
	/**
	 * 对象类型：kpi/risk
	 */
	@Column(name = "OBJECT_TYPE")
	private String objectType;
	/**
	 * 对象名称
	 */
	@Column(name = "OBJECT_NAME")
	private String objectName;
	/**
	 * 对象所属列
	 * kpi:
	 * targetValueFormula目标值公式
	 * resultValueFormula结果值公式
	 * assessmentValueFormula评估值公式
	 * risk:
	 * impactsFormula影响程度公式
	 * probabilityFormula发生可能性公式
	 */
	@Column(name = "OWNER_OBJECT_COLUMN")
	private String objectColumn;
	/**
	 * 公式内容
	 */
	@Column(name = "FORMULA_CONTENT")
	private String formulaContent;
	/**
	 * 成功/失败类型：成功-success;失败-failure;
	 */
	@Column(name = "FAILURE_TYPE")
	private String failureType;
	/**
	 * 成功/失败原因
	 */
	@Column(name = "FAILURE_REASON")
	private String failureReason;
	/**
     * 时间区间
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_PERIOD_ID")
    private TimePeriod timePeriod;
    /**
     * 计算时间
     */
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CALCULATE_DATE")
    private Date calculateDate;
    /**
     * 删除状态
     */
    @Column(name = "DELETE_STATUS")
    private Boolean deleteStatus;
	/**
	 * 所属人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee emp;
	
	public FormulaLog() {
		super();
	}

	public FormulaLog(String objectId, String objectType, String objectName,
			String objectColumn, String formulaContent, String failureType,
			String failureReason, TimePeriod timePeriod, Date calculateDate,
			Boolean deleteStatus, SysEmployee emp) {
		super();
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectName = objectName;
		this.objectColumn = objectColumn;
		this.formulaContent = formulaContent;
		this.failureType = failureType;
		this.failureReason = failureReason;
		this.timePeriod = timePeriod;
		this.calculateDate = calculateDate;
		this.deleteStatus = deleteStatus;
		this.emp = emp;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectColumn() {
		return objectColumn;
	}

	public void setObjectColumn(String objectColumn) {
		this.objectColumn = objectColumn;
	}

	public String getFormulaContent() {
		return formulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		this.formulaContent = formulaContent;
	}

	public String getFailureType() {
		return failureType;
	}

	public void setFailureType(String failureType) {
		this.failureType = failureType;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Date getCalculateDate() {
		return calculateDate;
	}

	public void setCalculateDate(Date calculateDate) {
		this.calculateDate = calculateDate;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}
}
