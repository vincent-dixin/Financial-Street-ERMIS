package com.fhd.sys.entity.orgstructure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;

/**
 * 岗位实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_SYS_POSITION")
public class SysPosition extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 772378508316048533L;
	
	/**
	 * 导入时报错信息
	 */
	@Transient
	private String impErrorInfo;
	
	/**
	 * 机构(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
	/**
	 * 岗位编号.
	 */
	@Column(name = "POSI_CODE", length = 30)
	private String posicode;
	/**
	 * 岗位名称.
	 */
	@Column(name = "POSI_NAME", length = 50)
	private String posiname;
	/**
	 * 开始日期.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;
	/**
	 * 结束日期.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;
	/**
	 * 状态.
	 */
	@Column(name = "ESTATUS")
	private String posiStatus;
	/**
	 * 备注.
	 */
	@Column(name = "ECOMMENT", length = 1024)
	private String remark;
	/**
	 * 排列顺序.
	 */
	@OrderBy("sn ASC")
	@Column(name = "ESORT")
	private Integer sn;
	/**
	 * 岗位员工中间表集合.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysPosition")
	private Set<SysEmpPosi> sysEmpPosis = new HashSet<SysEmpPosi>(0);
	/**
	 * 岗位角色集合.
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_POSI_ROLE", joinColumns = { @JoinColumn(name = "POSI_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	private Set<SysRole> sysRoles = new HashSet<SysRole>(0);
	/**
	 * 岗位权限集合.
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_POSI_AUTHORITY", joinColumns = { @JoinColumn(name = "POSI_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "AUTH_ID", nullable = false, updatable = false) })
	private Set<SysAuthority> sysAuthorities = new HashSet<SysAuthority>(0);

	// Constructors

	/** default constructor */
	public SysPosition() {
	}

	/** full constructor */
	public SysPosition(SysOrganization sysOrganization, String posicode,
			String posiname, Date startDate, Date endDate, String posiStatus,
			String remark, Integer sn, Set<SysEmpPosi> sysEmpPosis,
			Set<SysRole> sysRoles, Set<SysAuthority> sysAuthorities) {
		super();
		this.sysOrganization = sysOrganization;
		this.posicode = posicode;
		this.posiname = posiname;
		this.startDate = startDate;
		this.endDate = endDate;
		this.posiStatus = posiStatus;
		this.remark = remark;
		this.sn = sn;
		this.sysEmpPosis = sysEmpPosis;
		this.sysRoles = sysRoles;
		this.sysAuthorities = sysAuthorities;
	}

	// Property accessors


	public SysOrganization getSysOrganization() {
		return this.sysOrganization;
	}

	public Set<SysRole> getSysRoles() {
		return sysRoles;
	}

	public void setSysRoles(Set<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

	public Set<SysAuthority> getSysAuthorities() {
		return sysAuthorities;
	}

	public void setSysAuthorities(Set<SysAuthority> sysAuthorities) {
		this.sysAuthorities = sysAuthorities;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}

	public String getPosicode() {
		return this.posicode;
	}

	public void setPosicode(String posicode) {
		this.posicode = posicode;
	}

	
	public String getPosiname() {
		return this.posiname;
	}

	public void setPosiname(String posiname) {
		this.posiname = posiname;
	}

	
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<SysEmpPosi> getSysEmpPosis() {
		return this.sysEmpPosis;
	}

	public void setSysEmpPosis(Set<SysEmpPosi> sysEmpPosis) {
		this.sysEmpPosis = sysEmpPosis;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getPosiStatus() {
		return posiStatus;
	}

	public void setPosiStatus(String posiStatus) {
		this.posiStatus = posiStatus;
	}

	public String getImpErrorInfo() {
		return impErrorInfo;
	}

	public void setImpErrorInfo(String impErrorInfo) {
		this.impErrorInfo = impErrorInfo;
	}
}