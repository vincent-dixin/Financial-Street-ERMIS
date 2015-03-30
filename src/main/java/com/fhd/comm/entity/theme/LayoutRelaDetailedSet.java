package com.fhd.comm.entity.theme;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**

 * 布局信息布局详细设置关系
 * 
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_RELA_DETAILED_SET")
public class LayoutRelaDetailedSet extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主题信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_DETAILED_ID")
	private LayoutDetailedSet layoutDetailed ;
	
	/**
	 * 布局信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_ID")
	private LayoutInfo layout ;
	
	/**
	 * 位置信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_RELA_POSI_ID")
	private LayoutTypeRelaPosition layoutTypeRelaPosition ;
	
	public LayoutRelaDetailedSet(){
		
	}

	public LayoutDetailedSet getLayoutDetailedId() {
		return layoutDetailed;
	}

	public void setLayoutDetailed(LayoutDetailedSet layoutDetailed) {
		this.layoutDetailed = layoutDetailed;
	}

	public LayoutInfo getLayout() {
		return layout;
	}

	public void setLayoutId(LayoutInfo layout) {
		this.layout = layout;
	}

	public LayoutTypeRelaPosition getLayoutTypeRelaPosition() {
		return layoutTypeRelaPosition;
	}

	public void setLayoutTypeRelaPosition(
			LayoutTypeRelaPosition layoutTypeRelaPosition) {
		this.layoutTypeRelaPosition = layoutTypeRelaPosition;
	}
	
	
}




