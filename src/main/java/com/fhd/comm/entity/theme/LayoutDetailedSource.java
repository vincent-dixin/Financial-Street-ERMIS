package com.fhd.comm.entity.theme;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**

 * 布局详细设置数据源
 * 
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_DETAILED_SOURCE")
public class LayoutDetailedSource extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据源id
	 */
	@Column(name = "DATA_SOURCE_ID")
	private String dataSourceId ;
	
	/**
	 * 数据类型
	 */
	@Column(name = "DATA_TYPE")
	private String dataType ;
	
	/**
	 * 分析对象id
	 */
	@Column(name = "OBJECT_ID")
	private String objectId ;
	
	/**
	 * 时间维度
	 */
	@Column(name = "TIME_PERIOD")
	private String timePeriod ;
	
	/**
	 * 显示列
	 */
	@Column(name = "DISPLAY_COLUMN")
	private String displayColumn ;

	

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getDisplayColumn() {
		return displayColumn;
	}

	public void setDisplayColumn(String displayColumn) {
		this.displayColumn = displayColumn;
	}
	
	
}




