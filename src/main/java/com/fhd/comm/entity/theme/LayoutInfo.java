package com.fhd.comm.entity.theme;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 布局信息实体类
 * @author 郝静
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_LAYOUT_INFO")
public class LayoutInfo extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String layoutName ;
	
	/**
	 * 删除状态
	 */
	@Column(name = "DELETE_STATUS")
	private boolean deleteStatus ;
	
	/**
	 * 布局信息布局类型中间表
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "layout")
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@BatchSize(size = 10)
	private Set<LayoutRelaType> layoutRelaType = new HashSet<LayoutRelaType>(0);
	
	
	public LayoutInfo(){
		
	}
	
	
	
	public LayoutInfo(String layoutName){
		super();

		this.layoutName = layoutName;
	}



	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}



	public boolean isDeleteStatus() {
		return deleteStatus;
	}



	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}



	public Set<LayoutRelaType> getLayoutRelaType() {
		return layoutRelaType;
	}



	public void setLayoutRelaType(Set<LayoutRelaType> layoutRelaType) {
		this.layoutRelaType = layoutRelaType;
	}

}




