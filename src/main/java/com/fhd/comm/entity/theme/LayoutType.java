package com.fhd.comm.entity.theme;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 布局类型实体类
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_TYPE")
public class LayoutType extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	

	/**
	 * 类型
	 */
	@Column(name = "ETYPE")
	private String layoutType ;
	
	/**
	 * 描述
	 */
	@Column(name = "EDESC")
	private String layoutTypeDesc ;
	
	/**
	 * 名称
	 */
	@Column(name = "ICON")
	private String icon ;

	
	public LayoutType(){
		
	}
	
	public LayoutType(String layoutType,String layoutTypeDesc,String icon){
		super();

		this.layoutType = layoutType;
		this.layoutTypeDesc = layoutTypeDesc;
		this.icon = icon;
	}


	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	public String getLayoutTypeDesc() {
		return layoutTypeDesc;
	}

	public void setLayoutTypeDesc(String layoutTypeDesc) {
		this.layoutTypeDesc = layoutTypeDesc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


}




