/**
 * Temp.java
 * com.fhd.sys.entity.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.st;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 任务计划模版表对应实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-11		下午03:14:58
 *
 * @see 	 
 */
@Entity
@Table(name = "T_ST_TEMP")
public class Temp extends IdEntity implements Serializable {
	private static final long serialVersionUID = 153516067977916240L;
	
	/**
	 * 字段模版类型
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMP_CATEGORY")
	private DictEntry dictEntry;
	
	/**
	 * 计划任务模版标题
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "TEMP_NAME", nullable = false)
	private String name;
	
	/**
	 * 计划任务模版内容
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "ECONTENT", nullable = false)
	private String content;
	
	/**
	 * 计划任务模版删除状态
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "DELETE_STATUS")
	private String status;
	
	public Temp(){	
	}
	
	public Temp(String id){
		this.setId(id);
	}
	
	public DictEntry getDictEntry() {
		return dictEntry;
	}

	public void setDictEntry(DictEntry dictEntry) {
		this.dictEntry = dictEntry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}