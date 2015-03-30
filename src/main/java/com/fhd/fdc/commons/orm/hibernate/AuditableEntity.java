package com.fhd.fdc.commons.orm.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 含审计信息的Entity基类.
 * 
 * @author Vincent
 */
@MappedSuperclass
public class AuditableEntity extends IdEntity implements Serializable{

	
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 创建时间.
	 */
	//本属性只在save时有效,update时无效.
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createTime;
	/**
	 * 创建的操作员的登录名.
	 */
	@JoinColumn(updatable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private SysEmployee createBy;
	/**
	 * 最后修改时间.
	 */
	//本属性只在update时有效,save时无效.
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	private Date lastModifyTime;
	/**
	 * 最后修改的操作员的登录名.
	 */
	@JoinColumn(insertable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private SysEmployee lastModifyBy;

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public SysEmployee getCreateBy() {
		return createBy;
	}

	public void setCreateBy(SysEmployee createBy) {
		this.createBy = createBy;
	}

	
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	
	public SysEmployee getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(SysEmployee lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
}
