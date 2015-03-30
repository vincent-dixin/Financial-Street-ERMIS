package com.fhd.sys.entity.orgstructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 岗位员工实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_SYS_EMP_POSI")
public class SysEmpPosi extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = -5973930587924161492L;
	/**
	 * 员工.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee sysEmployee;
	/**
	 * 岗位.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSI_ID")
	private SysPosition sysPosition;
	/**
	 * 是否主岗位.
	 */
	@Column(name = "ISMAIN")
	private Boolean ismain;

	// Constructors

	/** default constructor */
	public SysEmpPosi() {
	}

	/** full constructor */
	public SysEmpPosi(SysEmployee sysEmployee, SysPosition sysPosition,
			Boolean ismain) {
		this.sysEmployee = sysEmployee;
		this.sysPosition = sysPosition;
		this.ismain = ismain;
	}

	// Property accessors

	public SysEmployee getSysEmployee() {
		return this.sysEmployee;
	}

	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}

	public SysPosition getSysPosition() {
		return this.sysPosition;
	}

	public void setSysPosition(SysPosition sysPosition) {
		this.sysPosition = sysPosition;
	}

	public Boolean getIsmain() {
		return ismain;
	}

	public void setIsmain(Boolean ismain) {
		this.ismain = ismain;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}