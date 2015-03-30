package com.fhd.bpm.entity.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import com.fhd.bpm.entity.BusinessWorkFlow;
import com.fhd.bpm.entity.JbpmHistActinst;
import com.fhd.sys.entity.orgstructure.SysEmployee;

@Entity
@Subselect(
"	SELECT DISTINCT" +
"		jbpm4_hist_procinst.*,JbpmHistTask.jbpm4_taskassignee_,JbpmHistTask.assignee_,jbpm4_hist_actinst.dbversion_ taskdbversion_" +
"	FROM" +
"		jbpm4_hist_procinst jbpm4_hist_procinst" +
"	INNER JOIN jbpm4_hist_actinst jbpm4_hist_actinst ON jbpm4_hist_procinst.dbid_ = jbpm4_hist_actinst.hproci_" +
"	INNER JOIN (" +
"		SELECT" +
"			jbpm4_hist_task.dbid_," +
"			jbpm4_hist_task.dbversion_," +
"			jbpm4_hist_task.execution_," +
"			jbpm4_hist_task.outcome_," +
"			jbpm4_hist_task.assignee_," +
"			jbpm4_hist_task.priority_," +
"			jbpm4_hist_task.state_," +
"			jbpm4_hist_task.create_," +
"			jbpm4_hist_task.end_," +
"			jbpm4_hist_task.duration_," +
"			jbpm4_hist_task.nextidx_," +
"			jbpm4_hist_task.supertask_," +
"			jbpm4_task.assignee_ jbpm4_taskassignee_" +
"		FROM" +
"			JBPM4_HIST_TASK" +
"		LEFT JOIN jbpm4_task ON jbpm4_hist_task.dbid_ = jbpm4_task.dbid_" +
"	) JbpmHistTask ON jbpm4_hist_actinst.htask_ = JbpmHistTask.dbid_"
)
@Synchronize({"jbpm4_hist_procinst","JBPM4_HIST_TASK"})
public class VJbpmHistProcinst implements Serializable{
	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DBID_")
	private Long id;
	
	@Column(name = "DBVERSION_")
	private Long dbversion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_",referencedColumnName="PROCESS_INSTANCE_ID")
	private BusinessWorkFlow businessWorkFlow;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCDEFID_",referencedColumnName="pdid")
	private VJbpmDeployment vJbpmDeployment;
	
	@Column(name = "id_",insertable=false,updatable=false)
	private String id_;
	
	@Column(name = "KEY_")
	private String key;
	
	@Column(name = "START_")
	private Date start;
	
	@Column(name = "END_")
	private Date end;
	
	@Column(name = "DURATION_")
	private Long duration;
	
	@Column(name = "STATE_")
	private String state;
	
	/**
	 * 状态：end1：完成
	 */
	@Column(name = "ENDACTIVITY_")
	private String endactivity;
	
	@Column(name = "NEXTIDX_")
	private Long nextidx;
	
	@OrderBy("end DESC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jbpmHistProcinst")
	private List<JbpmHistActinst> jbpmHistActinsts = new ArrayList<JbpmHistActinst>();
	
	/**
	 * 执行人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNEE_")
	private SysEmployee assignee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jbpm4_taskassignee_")
	private SysEmployee taskAssignee;
	
	@Column(name = "TASKDBVERSION_")
	private Long taskDbversion;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDbversion() {
		return dbversion;
	}

	public void setDbversion(Long dbversion) {
		this.dbversion = dbversion;
	}

	public BusinessWorkFlow getBusinessWorkFlow() {
		return businessWorkFlow;
	}

	public void setBusinessWorkFlow(BusinessWorkFlow businessWorkFlow) {
		this.businessWorkFlow = businessWorkFlow;
	}

	public VJbpmDeployment getvJbpmDeployment() {
		return vJbpmDeployment;
	}

	public void setvJbpmDeployment(VJbpmDeployment vJbpmDeployment) {
		this.vJbpmDeployment = vJbpmDeployment;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEndactivity() {
		return endactivity;
	}

	public void setEndactivity(String endactivity) {
		this.endactivity = endactivity;
	}

	public Long getNextidx() {
		return nextidx;
	}

	public void setNextidx(Long nextidx) {
		this.nextidx = nextidx;
	}

	public List<JbpmHistActinst> getJbpmHistActinsts() {
		return jbpmHistActinsts;
	}

	public void setJbpmHistActinsts(List<JbpmHistActinst> jbpmHistActinsts) {
		this.jbpmHistActinsts = jbpmHistActinsts;
	}

	public String getId_() {
		return id_;
	}

	public void setId_(String id_) {
		this.id_ = id_;
	}

	public SysEmployee getAssignee() {
		return assignee;
	}

	public void setAssignee(SysEmployee assignee) {
		this.assignee = assignee;
	}

	public SysEmployee getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(SysEmployee taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public Long getTaskDbversion() {
		return taskDbversion;
	}

	public void setTaskDbversion(Long taskDbversion) {
		this.taskDbversion = taskDbversion;
	}

}
