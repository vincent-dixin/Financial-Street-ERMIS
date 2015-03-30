package com.fhd.fdc.commons.orm.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.compass.annotations.SearchableId;
import org.hibernate.annotations.GenericGenerator;

/**
 * 定义默认主键为String id,生成算法为IDENTITY.
 * 如果主键的定义不同,重新实现本类.
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable {

	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SearchableId()
	@GenericGenerator(name = "generator", strategy = "assigned")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID",length = 32)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
