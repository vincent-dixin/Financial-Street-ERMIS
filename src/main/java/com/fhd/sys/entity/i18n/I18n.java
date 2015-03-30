/**
 * I18n.java
 * com.fhd.sys.entity.i18n
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */
/**
 * I18n.java
 * com.fhd.sys.entity.i18n
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
 */

package com.fhd.sys.entity.i18n;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 国际化实体
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012-11-20 上午09:59:31
 * 
 * @see
 */
@Entity
@Table(name = "T_COM_OBJECT_I18N")
public class I18n extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * 关键值
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "OBJECT_KEY", nullable = false)
	private String objectKey;
	
	/**
	 * 国际类别
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@Column(name = "OBJECT_TYPE", nullable = false)
	private String objectType;

	/**
	 * 英文
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@Column(name = "OBJECT_NAME_EN")
	private String objectEn;

	/**
	 * 中文
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@Column(name = "OBJECT_NAME_ZHCN")
	private String objectCn;

	
	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectEn() {
		return objectEn;
	}

	public void setObjectEn(String objectEn) {
		this.objectEn = objectEn;
	}

	public String getObjectCn() {
		return objectCn;
	}

	public void setObjectCn(String objectCn) {
		this.objectCn = objectCn;
	}

}
