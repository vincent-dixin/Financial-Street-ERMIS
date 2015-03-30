package com.fhd.bpm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.bpm.entity.view.VJbpmHistTask;
import com.fhd.bpm.entity.view.VJbpmExecution;

@Entity
@Table(name = "JBPM4_HIST_ACTINST")
public class JbpmHistActinst implements Serializable {
	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DBID_")
	private Long id;
	
	@Column(name = "CLASS_")
	private String class_;
	/**
	 * 执行状态：0-未执行，1-已执行
	 */
	@Column(name = "DBVERSION_")
	private Long dbversion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HPROCI_")
	private JbpmHistProcinst jbpmHistProcinst;
	
	@Column(name = "TYPE_")
	private String type;
	
	/**
	 * activityName:节点名称
	 */
	@Column(name = "ACTIVITY_NAME_")
	private String activityName;
	
	@Column(name = "START_")
	private Date start;

	@Column(name = "END_")
	private Date end;
	
	@Column(name = "DURATION_")
	private Long duration;
	
	@Column(name = "TRANSITION_")
	private String transition;
	
	@Column(name = "NEXTIDX_")
	private Long nextidx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXECUTION_",referencedColumnName="id_")
	private VJbpmExecution vJbpmExecution;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HTASK_")  //*******************************************
	private VJbpmHistTask jbpmHistTask;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClass_() {
		return class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public Long getDbversion() {
		return dbversion;
	}

	public void setDbversion(Long dbversion) {
		this.dbversion = dbversion;
	}

	public JbpmHistProcinst getJbpmHistProcinst() {
		return jbpmHistProcinst;
	}

	public void setJbpmHistProcinst(JbpmHistProcinst jbpmHistProcinst) {
		this.jbpmHistProcinst = jbpmHistProcinst;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public VJbpmExecution getvJbpmExecution() {
		return vJbpmExecution;
	}

	public void setvJbpmExecution(VJbpmExecution vJbpmExecution) {
		this.vJbpmExecution = vJbpmExecution;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

	public Long getNextidx() {
		return nextidx;
	}

	public void setNextidx(Long nextidx) {
		this.nextidx = nextidx;
	}

	public VJbpmHistTask getJbpmHistTask() {
		return jbpmHistTask;
	}

	public void setJbpmHistTask(VJbpmHistTask jbpmHistTask) {
		this.jbpmHistTask = jbpmHistTask;
	}

}
