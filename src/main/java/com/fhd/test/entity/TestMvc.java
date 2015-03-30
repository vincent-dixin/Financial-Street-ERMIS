/**
 * TestMvc.java
 * com.fhd.risk.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-8-2 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.test.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * ClassName:TestMvc
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-8-2		上午10:49:00
 *
 * @see 	 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TEST_MVC")
public class TestMvc extends IdEntity implements Serializable{

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 */
	
	private static final long serialVersionUID = 1951083660979632006L;
	/**
	 * 标题.
	 */
	@Column(name = "TITLE")
	private String title;
	/**
	 * 名称.
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 区域范围.
	 */
	@Column(name = "MY_LOCALE")
	private String myLocale;
	/**
	 * 父
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private TestMvc parent;
	
	
	/**
	 * 级别.
	 */
	@Column(name = "ELEVEL")
	private Integer level;
	/**
	 * 查询序列.
	 */
	@Column(name = "ID_SEQ")
	private String idSeq;
	
	@Column(name = "NUM")
	private Double num;
	
	@OrderBy("num ASC")
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<TestMvc> children = new HashSet<TestMvc>(0);
	
	public TestMvc(){
		
	}
	public TestMvc(String id){
		setId(id);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMyLocale() {
		return myLocale;
	}
	public void setMyLocale(String myLocale) {
		this.myLocale = myLocale;
	}
	public TestMvc getParent() {
		return parent;
	}
	public void setParent(TestMvc parent) {
		this.parent = parent;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getIdSeq() {
		return idSeq;
	}
	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Set<TestMvc> getChildren() {
		return children;
	}
	public void setChildren(Set<TestMvc> children) {
		this.children = children;
	}
	
	public static void main(String[] args) {
	    DecimalFormat df1 = new DecimalFormat("####.##");    //##.00%   百分比格式，后面不足2位的用0补齐
	     String baifenbi= df1.format(1.0);
	     System.out.println(baifenbi);
    }
	
}

