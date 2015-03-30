package com.fhd.comm.entity.theme;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 布局信息布局类型关系实体类
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_RELA_TYPE")
public class LayoutRelaType extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 布局信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_ID")
	private LayoutInfo layout ;
	/**
	 * 布局类型id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_TYPE_ID")
	private LayoutType layoutType ;
	/**
	 * 布局类型
	 */
//	@Column(name = "ETYPE")
//	private String  layoutType;
	
	
	public LayoutRelaType(){
		
	}
	public LayoutInfo getLayout() {
		return layout;
	}
	public void setLayout(LayoutInfo layout) {
		this.layout = layout;
	}
	public LayoutType getLayoutType() {
		return layoutType;
	}
	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}
	


	
}




