package com.fhd.sys.entity.auth;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Searchable
@Table(name = "T_SYS_USER")
public class SysUser extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 804474197076595952L;
	/**
	 * 用户名.
	 */
	@SearchableProperty(name = "code")
	@Column(name = "USER_NAME", length = 30)
	private String username;
	/**
	 * 密码.
	 */
	@SearchableProperty
	@Column(name = "PASSWORD")
	private String password;
	/**
	 * 真实姓名.
	 */
	@SearchableProperty(name = "name")
	@Column(name = "REAL_NAME", length = 30)
	private String realname;
	/**
	 * 用户状态.
	 */
	@Column(name = "ESTATUS")
	private String userStatus;
	/**
	 * 最后登录时间.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;
	/**
	 * 错误统计.
	 */
	@Column(name = "ERR_COUNT")
	private Integer errCount;
	/**
	 * 锁定状态.
	 */
	@Column(name = "LOCK_STATE")
	private Boolean lockstate;
	/**
	 * 是否启用.
	 */
	@Column(name = "IS_ENABLE")
	private Boolean enable;
	/**
	 * 失效日期.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY_DATE", length = 7)
	private Date expiryDate;
	/**
	 * 密码过期日期.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CREDENTIALS_EXPIRY_DATE", length = 7)
	private Date credentialsexpiryDate;
	/**
	 * 注册日期.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "REG_DATE", length = 7)
	private Date regdate;
	/**
	 * MAC地址
	 */
	@Column(name = "MAC")
	private String mac;
	/**
	 * 角色(多对多关系维护).
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	private Set<SysRole> sysRoles = new HashSet<SysRole>(0);
	/**
	 * 权限(多对多关系维护).
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_USER_AUTHORITY", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "AUTH_ID", nullable = false, updatable = false) })
	private Set<SysAuthority> sysAuthorities = new HashSet<SysAuthority>(0);

	public SysUser() {
	}

	public SysUser(String username, String password, String realname,
			String userStatus, Date lastLoginTime, Integer errCount,
			Boolean lockstate, Boolean enable, Date expiryDate,
			Date credentialsexpiryDate, Date regdate, String mac,
			Set<SysRole> sysRoles, Set<SysAuthority> sysAuthorities) {
		super();
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.userStatus = userStatus;
		this.lastLoginTime = lastLoginTime;
		this.errCount = errCount;
		this.lockstate = lockstate;
		this.enable = enable;
		this.expiryDate = expiryDate;
		this.credentialsexpiryDate = credentialsexpiryDate;
		this.regdate = regdate;
		this.mac = mac;
		this.sysRoles = sysRoles;
		this.sysAuthorities = sysAuthorities;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getErrCount() {
		return this.errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	public Boolean getLockstate() {
		return this.lockstate;
	}

	public void setLockstate(Boolean lockstate) {
		this.lockstate = lockstate;
	}

	public Boolean getEnable() {
		return this.enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getCredentialsexpiryDate() {
		return this.credentialsexpiryDate;
	}

	public void setCredentialsexpiryDate(Date credentialsexpiryDate) {
		this.credentialsexpiryDate = credentialsexpiryDate;
	}

	public Date getRegdate() {
		return this.regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Set<SysRole> getSysRoles() {
		return this.sysRoles;
	}

	public void setSysRoles(Set<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

	public Set<SysAuthority> getSysAuthorities() {
		return this.sysAuthorities;
	}

	public void setSysAuthorities(Set<SysAuthority> sysAuthorities) {
		this.sysAuthorities = sysAuthorities;
	}

	public boolean isAccountNonExpired() {
		if (this.expiryDate != null && this.expiryDate.before(new Date())) {
			return false;
		}
		return true;
	}

	public boolean isAccountNonLocked() {
		return !this.lockstate;
	}

	public boolean isCredentialsNonExpired() {
		if (this.credentialsexpiryDate != null && this.credentialsexpiryDate.before(new Date())) {
			return false;
		}
		return true;
	}

	public boolean isEnabled() {
		return this.enable;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}