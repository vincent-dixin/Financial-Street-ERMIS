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
@Table(name = "T_COM_LAYOUT_POSITION")
public class LayoutPosition extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	

	/**
	 * 类型
	 */
	@Column(name = "NAME")
	private String name ;
	

	
	public LayoutPosition(){
		
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}
	
	

}




