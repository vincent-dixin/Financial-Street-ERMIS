package com.fhd.kpi.entity;

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
 * 指标相关维度
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午11:01:23
 *
 * @see
 */
@Entity
@Table(name = "t_kpi_kpi_rela_dim")
public class KpiRelaDim extends IdEntity implements java.io.Serializable {

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
	 * 指标管理维度
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SM_DIM_ID")
	private DictEntry smDim;
	
	/**
	 * 类型  M：主要维度；A：辅助维度
	 */
	@Column(name = "ETYPE", length = 100)
	private String type;

	// Constructors

	/** default constructor */
	public KpiRelaDim() {
	}

	/** minimal constructor */
	public KpiRelaDim(String id) {
		setId(id);
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

	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}


	
}