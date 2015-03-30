package com.fhd.kpi.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 
 * 指标关联部门员工
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午10:57:33
 *
 * @see
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "t_kpi_kpi_rela_org_emp")
public class KpiRelaOrgEmp extends IdEntity implements java.io.Serializable {

	// Fields
	
	/**
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;


	/**
	 * 指标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KPI_ID")
	private Kpi kpi;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 类型  B所属部门（维护基本信息）；R报告部门；V查看部门，G采集部门，T目标设定部门
	 */
	@Column(name = "ETYPE", length = 100)
	private String type;

	/**
	 * 员工
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee emp;

	// Constructors

	/** default constructor */
	public KpiRelaOrgEmp() {
	}

	/** minimal constructor */
	public KpiRelaOrgEmp(String id) {
		setId(id);
	}


	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}


}