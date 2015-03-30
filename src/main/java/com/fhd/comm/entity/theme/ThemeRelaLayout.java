package com.fhd.comm.entity.theme;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 主题布局关系实体类
 * 
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_THEME_RELA_LAYOUT")
public class ThemeRelaLayout extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主题信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "THEME_ID")
	private Analysis theme ;
	
	/**
	 * 布局信息id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_ID")
	private LayoutInfo layout ;
	
	public ThemeRelaLayout(){
		
	}
	


	public Analysis getTheme() {
		return theme;
	}

	public void setTheme(Analysis theme) {
		this.theme = theme;
	}

	public LayoutInfo getLayout() {
		return layout;
	}

	public void setLayout(LayoutInfo layout) {
		this.layout = layout;
	}
	
	
}




