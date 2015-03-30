package com.fhd.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "t_test_process_instance")
public class TestProcessInstance extends IdEntity implements Serializable{

	
	private static final long serialVersionUID = 1951083660979632006L;
	/**
	 * 名称.
	 */
	@Column(name = "t_NAME")
	private String name;
	public TestProcessInstance(String name) {
		super();
		this.name = name;
	}
	public TestProcessInstance() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

