/**
 * II18nBO.java
 * com.fhd.sys.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * II18nBO.java
 * com.fhd.sys.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.interfaces;

import java.util.List;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.i18n.I18n;

/**
 * 国际化接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:11:45
 *
 * @see 	 
 */
public interface II18nBO {

	/**
	 * 删除i18n实体
	 * 
	 * @author 金鹏祥
	 * @param i18n
	 * @since  fhd　Ver 1.1
	*/
	public void deleteI18n(I18n i18n);
	
	/**
	 * 更新I18n实体
	 * @author 金鹏祥
	 * @param i18n
	 * @since  fhd　Ver 1.1
	*/
	public void mergeI18n(I18n i18n);
	
	/**
	 * 通过ID查询i18n实体
	 * 
	 * @author 金鹏祥
	 * @param id 主键
	 * @return I18n
	 * @since  fhd　Ver 1.1
	*/
	public I18n findI18nById(String id);
	
	/**
	 * 查询i18n实体内容
	 * 
	 * @author 金鹏祥
	 * @return List<I18n>
	 * @since  fhd　Ver 1.1
	*/
	public List<I18n> findI18nAll();
	
	
	/**
	 * 分组查询所有类型
	 * 
	 * @author 金鹏祥
	 * @return List<String>
	*/
	public List<String> findAll();
	
	/**
	 * 通过类别查询i18n实体
	 * 
	 * @author 金鹏祥
	 * @param objectType 类别
	 * @return List<I18n>
	 * @since  fhd　Ver 1.1
	*/
	public List<I18n> findI18nByObjectType(String objectType);
	
	/**
	 * 模糊查询i18n实体类别
	 * 
	 * @author 金鹏祥
	 * @param objectType 类别
	 * @return List<I18n>
	 * @since  fhd　Ver 1.1
	*/
	public List<String> findI18nByLikeObjectType(String objectType);
	
	/**
	 * 分页查询
	 * 
	 * @author 金鹏祥
	 * @param objectKey 关键值
	 * @param page 分页实体
	 * @param sort 排序字段
	 * @param dir 排序方式
	 * @param objectType 类别
	 * @return Page<I18n>
	 * @since  fhd　Ver 1.1
	*/
	public Page<I18n> findI18nBySome(String objectKey, Page<I18n> page, String sort, String dir, String objectType);
}