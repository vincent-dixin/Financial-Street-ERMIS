/**
 * PortalPortlet.java
 * com.fhd.fdc.commons.entity.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-03-16 		杨鹏
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.portal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
/**
 * 面板布局关系实体类
 * @author yangpeng
 * @version V1.0  创建时间：2011-03-16
 * Company FirstHuiDa.
 */
@Entity
@Table(name ="T_SYS_PORTAL_PORTLET")
public class PortalPortlet  extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 面板布局(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PORTAL_ID")
	private Portal portal;
	/**
	 * 面板(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PORTLET_ID")
	private Portlet portlet;
	/**
	 * 行号.
	 */
	@Column(name="ROW_NO")
	private int rowNo;
	/**
	 * 列号.
	 */
	@Column(name="COL_NO")
	private int colNo;
	/**
	 * 排序.
	 */
	@Column(name="ESORT")
	private int sort;
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
	
	public PortalPortlet(){
	}
	public PortalPortlet(Portal portal,Portlet portlet,int rowNo,int colNo,int sort,Date updateTime,String operator){
		super();
		this.portal = portal;
		this.portlet = portlet;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.sort = sort;
		this.updateTime = updateTime;
		this.operator = operator;
	}
	public Portal getPortal() {
		return portal;
	}
	public void setPortal(Portal portal) {
		this.portal = portal;
	}
	public Portlet getPortlet() {
		return portlet;
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public int getColNo() {
		return colNo;
	}
	public void setColNo(int colNo) {
		this.colNo = colNo;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
	
}
