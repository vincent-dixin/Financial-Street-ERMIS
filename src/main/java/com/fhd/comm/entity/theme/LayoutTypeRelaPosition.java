package com.fhd.comm.entity.theme;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**

 * 布局类型布局位置设置关系
 * 
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_TYPE_RELA_POSITION")
public class LayoutTypeRelaPosition extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主题信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_TYPE_ID")
	private LayoutType layoutType ;
	
	/**
	 * 布局信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSITION_ID")
	private LayoutPosition layoutPosition ;
	
	public LayoutTypeRelaPosition(){
		
	}

	public LayoutType getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}

	public LayoutPosition getLayoutPosition() {
		return layoutPosition;
	}

	public void setLayoutPosition(LayoutPosition layoutPosition) {
		this.layoutPosition = layoutPosition;
	}

	
}




