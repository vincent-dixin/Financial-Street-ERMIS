package com.fhd.bpm.entity;

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
import javax.persistence.Table;

import com.fhd.bpm.entity.view.VJbpmDeployment;

@Entity
@Table(name="JBPM4_HIST_PROCINST")
public class JbpmHistProcinst implements Serializable{
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
	@JoinColumn(name = "PROCDEFID_",referencedColumnName="pdid")
	private VJbpmDeployment vJbpmDeployment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_",referencedColumnName="PROCESS_INSTANCE_ID")
	private BusinessWorkFlow businessWorkFlow;
	
	@Column(name = "ID_",insertable=false,updatable=false)
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
	 * 状态：end1：完成,remove1:已删除,"":进行中
	 */
	@Column(name = "ENDACTIVITY_")
	private String endactivity;
	
	@Column(name = "NEXTIDX_")
	private Long nextidx;
	
	@OrderBy("end DESC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jbpmHistProcinst")
	private List<JbpmHistActinst> jbpmHistActinsts = new ArrayList<JbpmHistActinst>();
	
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

}
