package com.fhd.bpm.entity.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import com.fhd.bpm.entity.BusinessWorkFlow;
import com.fhd.sys.entity.orgstructure.SysEmployee;

@Entity
@Subselect(
	"select" +
	"	JBPM4_EXECUTION.dbid_, JBPM4_EXECUTION.class_, JBPM4_EXECUTION.dbversion_, JBPM4_EXECUTION.activityname_, JBPM4_EXECUTION.procdefid_, JBPM4_EXECUTION.hasvars_, JBPM4_EXECUTION.name_, JBPM4_EXECUTION.key_, JBPM4_EXECUTION.id_, JBPM4_EXECUTION.state_, JBPM4_EXECUTION.susphiststate_, JBPM4_EXECUTION.priority_, JBPM4_EXECUTION.hisactinst_, JBPM4_EXECUTION.parent_, JBPM4_EXECUTION.instance_, JBPM4_EXECUTION.superexec_, JBPM4_EXECUTION.subprocinst_, JBPM4_EXECUTION.parent_idx_," +
	"	businessName.String_Value_ businessname," +
	"	startuserid.string_value_ startuserid," +
	"	curruserid.string_value_ curruserid," +
	"	businessId.String_Value_ businessid," +
	"	processInstanceId.String_Value_ processinstanceid," +
	"	T_JBPM_BUSINESS_WORKFLOW.Id businessworkflowid," +
	"	JBPM4_DEPLOYMENT.name_ JBPMDEPLOYMENTNAME	" +
	"from" +
	"	JBPM4_EXECUTION" +
	"	left join JBPM4_VARIABLE businessName on businessName.Execution_=JBPM4_EXECUTION.Dbid_ and businessName.key_='businessName'" +
	"	left join JBPM4_VARIABLE startuserid on startuserid.Execution_=JBPM4_EXECUTION.Dbid_ and startuserid.key_='startuserid'" +
	"	left join JBPM4_VARIABLE curruserid on curruserid.Execution_=JBPM4_EXECUTION.Dbid_ and curruserid.key_='curruserid'" +
	"	left join JBPM4_VARIABLE businessId on businessId.Execution_=JBPM4_EXECUTION.Dbid_ and businessId.key_='businessId'" +
	"	left join JBPM4_VARIABLE processInstanceId on processInstanceId.Execution_=JBPM4_EXECUTION.Dbid_ and processInstanceId.key_='processInstanceId'" +
	"	left join T_JBPM_BUSINESS_WORKFLOW on T_JBPM_BUSINESS_WORKFLOW.Process_Instance_Id=JBPM4_EXECUTION.Id_" +
	"	left join JBPM4_DEPLOYPROP pdid on pdid.key_='pdid' and pdid.stringval_=JBPM4_EXECUTION.Procdefid_" +
	"	left join JBPM4_DEPLOYMENT on JBPM4_DEPLOYMENT.DBID_=pdid.DEPLOYMENT_"
)
@Synchronize({"JBPM4_EXECUTION","JBPM4_VARIABLE","T_JBPM_BUSINESS_WORKFLOW","JBPM4_DEPLOYPROP"})
public class VJbpmExecution implements Serializable {
	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "dbid_")
	private String id;
	
	@Column(name = "class_")
	private String class_;
	
	@Column(name = "dbversion_")
	private Integer dbversion;
	
	@Column(name = "activityname_")
	private String activityname;
	
	@Column(name = "procdefid_")
	private String procdefid;
	
	@Column(name = "hasvars_")
	private Integer hasvars;
	
	@Column(name = "name_")
	private String name;
	
	@Column(name = "key_")
	private String key;
	
	@Column(name = "id_")
	private String id_;
	
	@Column(name = "state_")
	private String state;
	
	@Column(name = "susphiststate_")
	private String susphiststate;
	
	@Column(name = "priority_")
	private Integer priority;
	
	@Column(name = "HISACTINST_")
	private Integer hisactinst;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_")
	private VJbpmExecution vJbpmExecution;
	
	@Column(name = "INSTANCE_")
	private Integer instance;
	
	@Column(name = "SUPEREXEC_")
	private Integer superexec;
	
	@Column(name = "SUBPROCINST_")
	private Integer subprocinst;
	
	@Column(name = "PARENT_IDX_")
	private Integer parentIDX;
	
	/**
	 * 流程名称
	 */
	@Column(name = "BUSINESSNAME")
	private String businessName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "startuserid")
	private SysEmployee startEmp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curruserid")
	private SysEmployee currEmp;
	
	@Column(name = "businessid")
	private String businessId;
	
	@Column(name = "processinstanceid")
	private String processInstanceId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "businessworkflowid")
	private BusinessWorkFlow businessWorkFlow;
	
	@Column(name = "JBPMDEPLOYMENTNAME")
	private String jbpmDeploymentName;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClass_() {
		return class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public Integer getDbversion() {
		return dbversion;
	}

	public void setDbversion(Integer dbversion) {
		this.dbversion = dbversion;
	}

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getProcdefid() {
		return procdefid;
	}

	public void setProcdefid(String procdefid) {
		this.procdefid = procdefid;
	}

	public Integer getHasvars() {
		return hasvars;
	}

	public void setHasvars(Integer hasvars) {
		this.hasvars = hasvars;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSusphiststate() {
		return susphiststate;
	}

	public void setSusphiststate(String susphiststate) {
		this.susphiststate = susphiststate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getHisactinst() {
		return hisactinst;
	}

	public void setHisactinst(Integer hisactinst) {
		this.hisactinst = hisactinst;
	}

	public VJbpmExecution getvJbpmExecution() {
		return vJbpmExecution;
	}

	public void setvJbpmExecution(VJbpmExecution vJbpmExecution) {
		this.vJbpmExecution = vJbpmExecution;
	}

	public Integer getInstance() {
		return instance;
	}

	public void setInstance(Integer instance) {
		this.instance = instance;
	}

	public Integer getSuperexec() {
		return superexec;
	}

	public void setSuperexec(Integer superexec) {
		this.superexec = superexec;
	}

	public Integer getSubprocinst() {
		return subprocinst;
	}

	public void setSubprocinst(Integer subprocinst) {
		this.subprocinst = subprocinst;
	}

	public Integer getParentIDX() {
		return parentIDX;
	}

	public void setParentIDX(Integer parentIDX) {
		this.parentIDX = parentIDX;
	}

	public String getId_() {
		return id_;
	}

	public void setId_(String id_) {
		this.id_ = id_;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public SysEmployee getStartEmp() {
		return startEmp;
	}

	public void setStartEmp(SysEmployee startEmp) {
		this.startEmp = startEmp;
	}

	public SysEmployee getCurrEmp() {
		return currEmp;
	}

	public void setCurrEmp(SysEmployee currEmp) {
		this.currEmp = currEmp;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public BusinessWorkFlow getBusinessWorkFlow() {
		return businessWorkFlow;
	}

	public void setBusinessWorkFlow(BusinessWorkFlow businessWorkFlow) {
		this.businessWorkFlow = businessWorkFlow;
	}

	public String getJbpmDeploymentName() {
		return jbpmDeploymentName;
	}

	public void setJbpmDeploymentName(String jbpmDeploymentName) {
		this.jbpmDeploymentName = jbpmDeploymentName;
	}

}
