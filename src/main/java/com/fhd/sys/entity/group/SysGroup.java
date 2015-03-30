package com.fhd.sys.entity.group;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 
 * @ClassName SysGroup.java
 * @Version 1.0
 * @author zhaotao
 * @Date 2011-1-19
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "T_SYS_GROUP")
public class SysGroup extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = -3804736285975968330L;
	/**
	 * 导入时报错信息
	 */
	@Transient
	private String impErrorInfo;
	
	/**
	 * 父节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private SysGroup parentGroup;
	/**
	 * 工作组级别
	 */
	@Column(name = "ELEVEL")
	private Integer groupLevel;
	/**
	 * 工作组编号
	 */
	@Column(name = "GROUP_CODE")
	private String groupCode;
	/**
	 * 工作组序列
	 */
	@Column(name = "ID_SEQ")
	private String groupSeq;
	/**
	 * 工作组名称
	 */
	@Column(name = "GROUP_NAME")
	private String groupName;
	/**
	 * 工作组类型
	 */
	@Column(name = "ETYPE")
	private String groupType;
	/**
	 * 工作组描述
	 */
	@Column(name = "EDESC", length = 512)
	private String groupDesc;
	/**
	 * 生效时间
	 */
	@Column(name = "START_DATE")
	private Date startDate;
	/**
	 * 失效时间
	 */
	@Column(name = "END_DATE")
	private Date endDate;
	/**
	 * 工作组状态 0 不可用 1可用
	 */
	@Column(name = "ESTATUS", length = 255)
	private String groupStatus;
	/**
	 * 是否叶子节点
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	/**
	 * 相关角色
	 */
	@ManyToMany
	@JoinTable(name = "T_SYS_GROUP_ROLE", joinColumns = { @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	private Set<SysRole> sysRoles = new HashSet<SysRole>(0);
	/**
	 * 相关员工
	 */
	@ManyToMany
	@JoinTable(name = "T_SYS_GROUP_EMPLOYEE", joinColumns = { @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "EMPLOYEE_ID", nullable = false, updatable = false) })
	private Set<SysEmployee> sysEmployees = new HashSet<SysEmployee>(0);
	/**
	 * 相关权限
	 */
	@ManyToMany
	@JoinTable(name = "T_SYS_GROUP_AUTHORITY", joinColumns = { @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "AUTH_ID", nullable = false, updatable = false) })
	private Set<SysAuthority> sysAuthorities = new HashSet<SysAuthority>(0);
	/**
	 * 子节点
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentGroup")
	private Set<SysGroup> subGroups = new HashSet<SysGroup>(0);

	public SysGroup() {
	}
	
	public SysGroup(SysGroup parentGroup, Integer groupLevel, String groupCode,
			String groupSeq, String groupName, String groupType,
			String groupDesc, Date startDate, Date endDate, String groupStatus,
			Boolean isLeaf, Set<SysRole> sysRoles,
			Set<SysEmployee> sysEmployees, Set<SysAuthority> sysAuthorities,
			Set<SysGroup> subGroups) {
		super();
		this.parentGroup = parentGroup;
		this.groupLevel = groupLevel;
		this.groupCode = groupCode;
		this.groupSeq = groupSeq;
		this.groupName = groupName;
		this.groupType = groupType;
		this.groupDesc = groupDesc;
		this.startDate = startDate;
		this.endDate = endDate;
		this.groupStatus = groupStatus;
		this.isLeaf = isLeaf;
		this.sysRoles = sysRoles;
		this.sysEmployees = sysEmployees;
		this.sysAuthorities = sysAuthorities;
		this.subGroups = subGroups;
	}

	public SysGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(SysGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public Integer getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(String groupSeq) {
		this.groupSeq = groupSeq;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Set<SysRole> getSysRoles() {
		return sysRoles;
	}

	public void setSysRoles(Set<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

	public Set<SysEmployee> getSysEmployees() {
		return sysEmployees;
	}

	public void setSysEmployees(Set<SysEmployee> sysEmployees) {
		this.sysEmployees = sysEmployees;
	}

	public Set<SysAuthority> getSysAuthorities() {
		return sysAuthorities;
	}

	public void setSysAuthorities(Set<SysAuthority> sysAuthorities) {
		this.sysAuthorities = sysAuthorities;
	}

	public Set<SysGroup> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(Set<SysGroup> subGroups) {
		this.subGroups = subGroups;
	}

	public String getImpErrorInfo() {
		return impErrorInfo;
	}

	public void setImpErrorInfo(String impErrorInfo) {
		this.impErrorInfo = impErrorInfo;
	}
}