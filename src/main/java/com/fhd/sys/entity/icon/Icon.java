/**
 * Icon.java
 * com.fhd.sys.entity.icon
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.icon;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * ClassName:Icon
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:49:03
 *
 * @see 	 
 */
@Entity
@Table(name = "T_SYS_ICON")
public class Icon extends IdEntity implements Serializable{

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 4736587127834260587L;

	/**
	 * path:文件路径
	 */
	@Column(name = "EPATH")
	private String path;
	/**
	 * fileName:文件名称
	 */
	@Column(name = "FILE_NAME")
	private String fileName;
	/**
	 * css:样式名
	 */
	@Column(name = "CSS")
	private String css;
	
	/**
	 * desc:说明
	 */
	@Column(name = "EDESC")
	private String desc;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}

