package com.fhd.risk.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

@Entity
@Table(name = "T_RM_EVENT")
public class RiskEvent extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = -2715166040530468157L;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	private String eventCode;
	private String etype;
	private String motivation;
	private Date occurDate;
	private String dealStatus;
	private String estatus;
	private String ecomment;
	private Date createTime;
	private String createBy;
	private String createEmpName;
	private Date lastModifyTime;
	private String lastModifyBy;
	private String companyId;
	private Integer empDeadNum;
	private Integer empLightHurtNum;
	private Integer empSeriouslyHurtNum;
	private String eventOccuredBeforeStatus;
	private String eventOccuredObject;
	private String eventOccuredReason;
	private String eventOccuredStory;
	private String equipmentEffect;
	private BigDecimal financeLostAmount;
	private String financeLostDesc;
	private String operationEffect;
	private String controlPlan;
	private String preventionPlan;
	private String reputationEffect;
	private String treatmentDealResults;
	private String treatmentPositOrEmp;
	private String treatmentDepartment;
	private String reportOrgId;
	private String eventName;
	private String eventLevel;
	private String occurePlace;
	private String empHurt;
	private String responseDetermin;
	private Date dealTime;
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;
	private String reserved6;
	private String reserved7;
	private String reserved8;
	private Date reserved9;
	private Date reserved10;

	public RiskEvent() {
	}

	public RiskEvent(String id) {
		super.setId(id);
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	@Column(name = "EVENT_CODE")
	public String getEventCode() {
		return this.eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	@Column(name = "ETYPE", length = 100)
	public String getEtype() {
		return this.etype;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}

	@Column(name = "MOTIVATION", length = 65535)
	public String getMotivation() {
		return this.motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OCCUR_DATE", length = 0)
	public Date getOccurDate() {
		return this.occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	@Column(name = "DEAL_STATUS", length = 100)
	public String getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	@Column(name = "ESTATUS", length = 100)
	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@Column(name = "ECOMMENT", length = 65535)
	public String getEcomment() {
		return this.ecomment;
	}

	public void setEcomment(String ecomment) {
		this.ecomment = ecomment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_BY", length = 100)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_EMP_NAME")
	public String getCreateEmpName() {
		return this.createEmpName;
	}

	public void setCreateEmpName(String createEmpName) {
		this.createEmpName = createEmpName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFY_TIME", length = 0)
	public Date getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Column(name = "LAST_MODIFY_BY", length = 100)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Column(name = "COMPANY_ID", length = 100)
	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "EMP_DEAD_NUM")
	public Integer getEmpDeadNum() {
		return this.empDeadNum;
	}

	public void setEmpDeadNum(Integer empDeadNum) {
		this.empDeadNum = empDeadNum;
	}

	@Column(name = "EMP_LIGHT_HURT_NUM")
	public Integer getEmpLightHurtNum() {
		return this.empLightHurtNum;
	}

	public void setEmpLightHurtNum(Integer empLightHurtNum) {
		this.empLightHurtNum = empLightHurtNum;
	}

	@Column(name = "EMP_SERIOUSLY_HURT_NUM")
	public Integer getEmpSeriouslyHurtNum() {
		return this.empSeriouslyHurtNum;
	}

	public void setEmpSeriouslyHurtNum(Integer empSeriouslyHurtNum) {
		this.empSeriouslyHurtNum = empSeriouslyHurtNum;
	}

	@Column(name = "EVENT_OCCURED_BEFORE_STATUS", length = 100)
	public String getEventOccuredBeforeStatus() {
		return this.eventOccuredBeforeStatus;
	}

	public void setEventOccuredBeforeStatus(String eventOccuredBeforeStatus) {
		this.eventOccuredBeforeStatus = eventOccuredBeforeStatus;
	}

	@Column(name = "EVENT_OCCURED_OBJECT", length = 65535)
	public String getEventOccuredObject() {
		return this.eventOccuredObject;
	}

	public void setEventOccuredObject(String eventOccuredObject) {
		this.eventOccuredObject = eventOccuredObject;
	}

	@Column(name = "EVENT_OCCURED_REASON", length = 65535)
	public String getEventOccuredReason() {
		return this.eventOccuredReason;
	}

	public void setEventOccuredReason(String eventOccuredReason) {
		this.eventOccuredReason = eventOccuredReason;
	}

	@Column(name = "EVENT_OCCURED_STORY", length = 65535)
	public String getEventOccuredStory() {
		return this.eventOccuredStory;
	}

	public void setEventOccuredStory(String eventOccuredStory) {
		this.eventOccuredStory = eventOccuredStory;
	}

	@Column(name = "EQUIPMENT_EFFECT", length = 65535)
	public String getEquipmentEffect() {
		return this.equipmentEffect;
	}

	public void setEquipmentEffect(String equipmentEffect) {
		this.equipmentEffect = equipmentEffect;
	}

	@Column(name = "FINANCE_LOST_AMOUNT", precision = 23, scale = 4)
	public BigDecimal getFinanceLostAmount() {
		return this.financeLostAmount;
	}

	public void setFinanceLostAmount(BigDecimal financeLostAmount) {
		this.financeLostAmount = financeLostAmount;
	}

	@Column(name = "FINANCE_LOST_DESC", length = 65535)
	public String getFinanceLostDesc() {
		return this.financeLostDesc;
	}

	public void setFinanceLostDesc(String financeLostDesc) {
		this.financeLostDesc = financeLostDesc;
	}

	@Column(name = "OPERATION_EFFECT", length = 65535)
	public String getOperationEffect() {
		return this.operationEffect;
	}

	public void setOperationEffect(String operationEffect) {
		this.operationEffect = operationEffect;
	}

	@Column(name = "CONTROL_PLAN", length = 65535)
	public String getControlPlan() {
		return this.controlPlan;
	}

	public void setControlPlan(String controlPlan) {
		this.controlPlan = controlPlan;
	}

	@Column(name = "PREVENTION_PLAN", length = 65535)
	public String getPreventionPlan() {
		return this.preventionPlan;
	}

	public void setPreventionPlan(String preventionPlan) {
		this.preventionPlan = preventionPlan;
	}

	@Column(name = "REPUTATION_EFFECT", length = 65535)
	public String getReputationEffect() {
		return this.reputationEffect;
	}

	public void setReputationEffect(String reputationEffect) {
		this.reputationEffect = reputationEffect;
	}

	@Column(name = "TREATMENT_DEAL_RESULTS", length = 65535)
	public String getTreatmentDealResults() {
		return this.treatmentDealResults;
	}

	public void setTreatmentDealResults(String treatmentDealResults) {
		this.treatmentDealResults = treatmentDealResults;
	}

	@Column(name = "TREATMENT_POSIT_OR_EMP", length = 65535)
	public String getTreatmentPositOrEmp() {
		return this.treatmentPositOrEmp;
	}

	public void setTreatmentPositOrEmp(String treatmentPositOrEmp) {
		this.treatmentPositOrEmp = treatmentPositOrEmp;
	}

	@Column(name = "TREATMENT_DEPARTMENT", length = 100)
	public String getTreatmentDepartment() {
		return this.treatmentDepartment;
	}

	public void setTreatmentDepartment(String treatmentDepartment) {
		this.treatmentDepartment = treatmentDepartment;
	}

	@Column(name = "REPORT_ORG_ID", length = 100)
	public String getReportOrgId() {
		return this.reportOrgId;
	}

	public void setReportOrgId(String reportOrgId) {
		this.reportOrgId = reportOrgId;
	}

	@Column(name = "EVENT_NAME")
	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Column(name = "EVENT_LEVEL", length = 100)
	public String getEventLevel() {
		return this.eventLevel;
	}

	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}

	@Column(name = "OCCURE_PLACE", length = 65535)
	public String getOccurePlace() {
		return this.occurePlace;
	}

	public void setOccurePlace(String occurePlace) {
		this.occurePlace = occurePlace;
	}

	@Column(name = "EMP_HURT", length = 100)
	public String getEmpHurt() {
		return this.empHurt;
	}

	public void setEmpHurt(String empHurt) {
		this.empHurt = empHurt;
	}

	@Column(name = "RESPONSE_DETERMIN", length = 65535)
	public String getResponseDetermin() {
		return this.responseDetermin;
	}

	public void setResponseDetermin(String responseDetermin) {
		this.responseDetermin = responseDetermin;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEAL_TIME", length = 0)
	public Date getDealTime() {
		return this.dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	@Column(name = "RESERVED1", length = 600)
	public String getReserved1() {
		return this.reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	@Column(name = "RESERVED2", length = 600)
	public String getReserved2() {
		return this.reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	@Column(name = "RESERVED3", length = 600)
	public String getReserved3() {
		return this.reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	@Column(name = "RESERVED4", length = 600)
	public String getReserved4() {
		return this.reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	@Column(name = "RESERVED5", length = 600)
	public String getReserved5() {
		return this.reserved5;
	}

	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}

	@Column(name = "RESERVED6", length = 600)
	public String getReserved6() {
		return this.reserved6;
	}

	public void setReserved6(String reserved6) {
		this.reserved6 = reserved6;
	}

	@Column(name = "RESERVED7", length = 65535)
	public String getReserved7() {
		return this.reserved7;
	}

	public void setReserved7(String reserved7) {
		this.reserved7 = reserved7;
	}

	@Column(name = "RESERVED8", length = 65535)
	public String getReserved8() {
		return this.reserved8;
	}

	public void setReserved8(String reserved8) {
		this.reserved8 = reserved8;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESERVED9", length = 0)
	public Date getReserved9() {
		return this.reserved9;
	}

	public void setReserved9(Date reserved9) {
		this.reserved9 = reserved9;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESERVED10", length = 0)
	public Date getReserved10() {
		return this.reserved10;
	}

	public void setReserved10(Date reserved10) {
		this.reserved10 = reserved10;
	}

}
