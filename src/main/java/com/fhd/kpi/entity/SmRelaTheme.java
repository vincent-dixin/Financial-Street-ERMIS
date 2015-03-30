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
 * 战略目标相关战略主题
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午10:19:23
 *
 * @see
 */

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "t_kpi_sm_rela_theme")
public class SmRelaTheme extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 战略主题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STRATEGY_MAP_ID")
	private StrategyMap strategyMap;
	
	/**
	 * 战略目标
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "THEME_ID")
	private DictEntry theme;
	
	/**
	 * 类型  M：主要；A：辅助
	 */
	@Column(name = "ETYPE")
	private String type;

	// Constructors

	/** default constructor */
	public SmRelaTheme() {
	}

	/** minimal constructor */
	public SmRelaTheme(String id) {
		setId(id);
	}


	

	// Property accessors
	
	public StrategyMap getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(StrategyMap strategyMap) {
		this.strategyMap = strategyMap;
	}

	public DictEntry getTheme() {
		return theme;
	}

	public void setTheme(DictEntry theme) {
		this.theme = theme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	


}