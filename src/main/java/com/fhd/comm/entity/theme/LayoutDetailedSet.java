package com.fhd.comm.entity.theme;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 布局详细信息设置
 * 
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_DETAILED_SET")
public class LayoutDetailedSet extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 图标类型
	 */
	@Column(name = "CHART_TYPE")
	private String chartType ;
	
	/**
	 * 数据源
	 */
	@Column(name = "DATA_SOURCE")
	private String dataSource ;
	
//	/**
//	 * 图表效果
//	 */
//	@Column(name = "CHART_EFFECT")
//	private String chartEffect ;
//	
//	/**
//	 * 图表形式
//	 */
//	@Column(name = "CHART_FORM")
//	private String chartForm ;
//	
//	/**
//	 * 是否显示单位
//	 */
//	@Column(name = "IS_SHOW_UNIT")
//	private String isShowUnit ;
//	
//	/**
//	 * 是否主源
//	 */
//	@Column(name = "IS_MAIN_SOURCE")
//	private String isMainSource ;
//	
//	/**
//	 * url
//	 */
//	@Column(name = "URL")
//	private String url ;
	
	
	public LayoutDetailedSet(){
		
	}


	public String getChartType() {
		return chartType;
	}


	public void setChartType(String chartType) {
		this.chartType = chartType;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


//	public String getChartEffect() {
//		return chartEffect;
//	}
//
//
//	public void setChartEffect(String chartEffect) {
//		this.chartEffect = chartEffect;
//	}
//
//
//	public String getChartForm() {
//		return chartForm;
//	}
//
//
//	public void setChartForm(String chartForm) {
//		this.chartForm = chartForm;
//	}
//
//
//	public String getIsShowUnit() {
//		return isShowUnit;
//	}
//
//
//	public void setIsShowUnit(String isShowUnit) {
//		this.isShowUnit = isShowUnit;
//	}
//
//
//	public String getIsMainSource() {
//		return isMainSource;
//	}
//
//
//	public void setIsMainSource(String isMainSource) {
//		this.isMainSource = isMainSource;
//	}
//
//
//	public String getUrl() {
//		return url;
//	}
//
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	
//

	
}




