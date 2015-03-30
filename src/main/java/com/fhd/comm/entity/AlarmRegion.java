package com.fhd.comm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 
 * 预警区间
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午9:52:19
 *
 * @see
 */
@Entity
@Table(name = "t_com_alarm_region")
public class AlarmRegion extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 4335885244911170470L;
	
	/**
	 * 预警方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALARM_PLAN_ID")
	private AlarmPlan alarmPlan;
	
	/**
	 * 最小公式
	 */
	@Column(name = "MIN_VALUE", length = 2000)
	private String minValue;
	
	/**
	 * 最小公式符号
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IS_CONTAIN_MIN")
	private DictEntry isContainMin;
	
	/**
	 * 最大公式
	 */
	@Column(name = "MAX_VALUE", length = 2000)
	private String maxValue;
	/**
	 * 最大公式符号
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IS_CONTAIN_MAX")
	private DictEntry isContainMax;
	
	/**
	 * 预警状态灯
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ALARM_ICON")
	private DictEntry alarmIcon;
	
	@Column(name = "ESORT")
	private Integer sort;
	
	@Column(name = "DELETE_STATUS")
	private Boolean deleteStatus;

	// Constructors

	/** default constructor */
	public AlarmRegion() {
	}

	/** minimal constructor */
	public AlarmRegion(String id) {
		setId(id);
	}

	// Property accessors
	
	public AlarmPlan getAlarmPlan() {
		return alarmPlan;
	}

	public void setAlarmPlan(AlarmPlan alarmPlan) {
		this.alarmPlan = alarmPlan;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public DictEntry getIsContainMin() {
		return isContainMin;
	}

	public void setIsContainMin(DictEntry isContainMin) {
		this.isContainMin = isContainMin;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public DictEntry getIsContainMax() {
		return isContainMax;
	}

	public void setIsContainMax(DictEntry isContainMax) {
		this.isContainMax = isContainMax;
	}

	public DictEntry getAlarmIcon() {
		return alarmIcon;
	}

	public void setAlarmIcon(DictEntry alarmIcon) {
		this.alarmIcon = alarmIcon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}


}