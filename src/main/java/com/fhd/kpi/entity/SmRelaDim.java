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
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 
 * 战略目标相关维度
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午10:31:17
 *
 * @see
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "t_kpi_sm_rela_dim")
public class SmRelaDim extends IdEntity implements java.io.Serializable {

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	// Fields
	/**
	 * 战略目标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STRATEGY_MAP_ID")
	private StrategyMap strategyMap;
	
	/**
	 * 维度
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SM_DIM_ID")
	private DictEntry smDim;
	
	/**
	 * 类型  M：主要维度；A：辅助维度
	 */
	@Column(name = "ETYPE")
	private String type;

	// Constructors

	/** default constructor */
	public SmRelaDim() {
	}

	/** minimal constructor */
	public SmRelaDim(String id) {
		setId(id);
	}

	public StrategyMap getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(StrategyMap strategyMap) {
		this.strategyMap = strategyMap;
	}

	public DictEntry getSmDim() {
		return smDim;
	}

	public void setSmDim(DictEntry smDim) {
		this.smDim = smDim;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}