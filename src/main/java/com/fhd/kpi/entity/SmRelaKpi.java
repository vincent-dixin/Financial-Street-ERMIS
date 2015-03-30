package com.fhd.kpi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 
 * 战略目标涉及指标
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午10:24:59
 *
 * @see
 */
@Entity
@Table(name = "t_kpi_sm_rela_kpi")
public class SmRelaKpi extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	// Fields
	/**
	 * 指标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KPI_ID")
	private Kpi kpi;
	
	/**
	 * 战略目标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STRATEGY_MAP_ID")
	private StrategyMap strategyMap;
	/**
	 * 权重
	 */
	@Column(name = "EWEIGHT", precision = 8, scale = 4)
	private Double weight;

	// Constructors

	/** default constructor */
	public SmRelaKpi() {
	}

	/** minimal constructor */
	public SmRelaKpi(String id) {
		setId(id);
	}
	

	// Property accessors

	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	public StrategyMap getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(StrategyMap strategyMap) {
		this.strategyMap = strategyMap;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}


	

	

}