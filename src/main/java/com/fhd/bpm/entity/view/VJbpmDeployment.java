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

import com.fhd.bpm.entity.ProcessDefinitionDeploy;

/**
 * 
 * ClassName:VJbpm4Deployment:JBPM4_流程定义视图
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-21		上午10:54:56
 *
 * @see
 */
@Entity
@Subselect(
	"select" +
	"	JBPM4_DEPLOYMENT.dbid_, JBPM4_DEPLOYMENT.name_, JBPM4_DEPLOYMENT.timestamp_, JBPM4_DEPLOYMENT.state_ ," +
	"	langid.stringval_ langid," +
	"	pdid.stringval_ pdid," +
	"	pdkey.stringval_ pdkey," +
	"	pdversion.longval_ pdversion," +
	"	T_JBPM_PROCESS_DEFINE_DEPLOY.id processdefinitiondeployid," +
	"	jbpm4_execution.jbpm4_execution_count	" +
	"from" +
	"	JBPM4_DEPLOYMENT" +
	"	left join JBPM4_DEPLOYPROP langid on langid.key_='langid' and langid.deployment_=JBPM4_DEPLOYMENT.dbid_" +
	"	left join JBPM4_DEPLOYPROP pdid on pdid.key_='pdid' and pdid.deployment_=JBPM4_DEPLOYMENT.dbid_" +
	"	left join JBPM4_DEPLOYPROP pdkey on pdkey.key_='pdkey' and pdkey.deployment_=JBPM4_DEPLOYMENT.dbid_" +
	"	left join JBPM4_DEPLOYPROP pdversion on pdversion.key_='pdversion' and pdversion.deployment_=JBPM4_DEPLOYMENT.dbid_" +
	"	left join T_JBPM_PROCESS_DEFINE_DEPLOY on T_JBPM_PROCESS_DEFINE_DEPLOY.id=JBPM4_DEPLOYMENT.name_" +
	"	LEFT JOIN (SELECT COUNT(*) jbpm4_execution_count,jbpm4_execution.PROCDEFID_ FROM jbpm4_execution GROUP BY jbpm4_execution.PROCDEFID_) jbpm4_execution ON jbpm4_execution.PROCDEFID_ = pdid.stringval_"
)
@Synchronize({"JBPM4_DEPLOYMENT","JBPM4_DEPLOYPROP"})
public class VJbpmDeployment implements Serializable{
	
	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "dbid_")
	private String id;
	
	/**
	 * 流程定义
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processdefinitiondeployid")
	private ProcessDefinitionDeploy processDefinitionDeploy;
	
	@Column(name = "timestamp_")
	private Integer timestamp;
	
	@Column(name = "STATE_")
	private String state;
	
	@Column(name = "langid")
	private String langid;
	/**
	 * 标识
	 */
	@Column(name = "pdid")
	private String pdid;
	
	@Column(name = "pdkey")
	private String pdkey;
	
	@Column(name = "pdversion")
	private Integer pdversion;
	
	@Column(name = "jbpm4_execution_count")
	private Integer executionCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProcessDefinitionDeploy getProcessDefinitionDeploy() {
		return processDefinitionDeploy;
	}

	public void setProcessDefinitionDeploy(
			ProcessDefinitionDeploy processDefinitionDeploy) {
		this.processDefinitionDeploy = processDefinitionDeploy;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLangid() {
		return langid;
	}

	public void setLangid(String langid) {
		this.langid = langid;
	}

	public String getPdid() {
		return pdid;
	}

	public void setPdid(String pdid) {
		this.pdid = pdid;
	}

	public String getPdkey() {
		return pdkey;
	}

	public void setPdkey(String pdkey) {
		this.pdkey = pdkey;
	}

	public Integer getPdversion() {
		return pdversion;
	}

	public void setPdversion(Integer pdversion) {
		this.pdversion = pdversion;
	}

	public Integer getExecutionCount() {
		return executionCount;
	}

	public void setExecutionCount(Integer executionCount) {
		this.executionCount = executionCount;
	}
	
}
