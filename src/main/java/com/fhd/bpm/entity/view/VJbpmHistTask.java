package com.fhd.bpm.entity.view;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import com.fhd.bpm.entity.ExamineApproveIdea;
import com.fhd.sys.entity.orgstructure.SysEmployee;

@Entity
@Subselect(
		"select" +
		"	jbpm4_hist_task.dbid_, jbpm4_hist_task.dbversion_, jbpm4_task.execution_, jbpm4_hist_task.outcome_, jbpm4_hist_task.assignee_, jbpm4_hist_task.priority_, jbpm4_hist_task.state_, jbpm4_hist_task.create_, jbpm4_hist_task.end_, jbpm4_hist_task.duration_, jbpm4_hist_task.nextidx_, jbpm4_hist_task.supertask_, jbpm4_task.assignee_ jbpm4_taskassignee_, jbpm4_task.form_ jbpm4_taskform_ " +
		"from" +
		"	JBPM4_HIST_TASK" +
		"	left join jbpm4_task on jbpm4_hist_task.dbid_=jbpm4_task.dbid_"
)
@Synchronize("JBPM4_HIST_TASK")
public class VJbpmHistTask implements Serializable {
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
	@JoinColumn(name = "EXECUTION_")
	private VJbpmExecution vJbpmExecution;
	
	@Column(name = "OUTCOME_")
	private String outcome;
	/**
	 * 执行人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNEE_")
	private SysEmployee assignee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jbpm4_taskassignee_")
	private SysEmployee taskAssignee;

	@Column(name = "PRIORITY_")
	private Long priority;
	
	@Column(name = "STATE_")
	private String state;
	
	@Column(name = "CREATE_")
	private Date create;
	
	@Column(name = "END_")
	private Date end;
	
	@Column(name = "DURATION_")
	private Long duration;
	
	@Column(name = "NEXTIDX_")
	private Long nextidx;
	
	@Column(name = "SUPERTASK_")
	private Long supertask;
	
	@Column(name = "jbpm4_taskform_")
	private String form;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "jbpmHistTask")
	private ExamineApproveIdea examineApproveIdea;
	
	
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

	public VJbpmExecution getvJbpmExecution() {
		return vJbpmExecution;
	}

	public void setvJbpmExecution(VJbpmExecution vJbpmExecution) {
		this.vJbpmExecution = vJbpmExecution;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public SysEmployee getAssignee() {
		return assignee;
	}

	public void setAssignee(SysEmployee assignee) {
		this.assignee = assignee;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
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

	public Long getNextidx() {
		return nextidx;
	}

	public void setNextidx(Long nextidx) {
		this.nextidx = nextidx;
	}

	public Long getSupertask() {
		return supertask;
	}

	public void setSupertask(Long supertask) {
		this.supertask = supertask;
	}

	public SysEmployee getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(SysEmployee taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public ExamineApproveIdea getExamineApproveIdea() {
		return examineApproveIdea;
	}

	public void setExamineApproveIdea(ExamineApproveIdea examineApproveIdea) {
		this.examineApproveIdea = examineApproveIdea;
	}
	
}
