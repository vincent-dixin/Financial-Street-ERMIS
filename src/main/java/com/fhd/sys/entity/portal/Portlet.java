/**
 * Portlet.java
 * com.fhd.fdc.commons.entity.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.portal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * Portal实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-20
 * Company FirstHuiDa.
 */
@Entity
@Table(name ="T_SYS_PORTLET")
public class Portlet extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -3555323402802051450L;
	/**
	 * 高度.
	 */
	@Column(name = "HEIGHT")
	private String height;
	/**
	 * 排列顺序.
	 */
	@Column(name = "ESORT")
	private int sort;
	/**
	 * 标题.
	 */
	@Column(name = "ETITLE")
	private String title;
	/**
	 * URL链接地址.
	 */
	@Column(name = "URL")
	private String url;
	/**
	 * 操作时间.
	 */
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	/**
	 * 操作员.
	 */
	@Column(name = "OPERATOR")
	private String operator;
	/**
	 * 保留字段1.
	 */
	@Column(name = "RESERVED1")
	private String reserved1;
	/**
	 * 保留字段2.
	 */
	@Column(name = "RESERVED2")
	private String reserved2;
	
	public Portlet() {
	}

	public Portlet(String height, int sort, String title,
			String url, Date updateTime, String operator, String reserved1,
			String reserved2) {
		super();
		this.height = height;
		this.sort = sort;
		this.title = title;
		this.url = url;
		this.updateTime = updateTime;
		this.operator = operator;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
	}


	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}
}

