/**
 * ITempBO.java
 * com.fhd.sys.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.interfaces;

import java.util.List;

import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.st.Temp;

/**
 * 任务计划模版接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-11		下午03:57:34
 *
 * @see 	 
 */
public interface ITempBO {
	/**
	 * 
	 * 保存RemindTemplate实体
	 * 
	 * @author 金鹏祥
	 * @param Temp 实体
	 * @since fhd　Ver 1.1
	 */
	public abstract void saveTemp(Temp entry);
	
	/**
	 * 
	 * 更新RemindTemplate实体
	 * 
	 * @author 金鹏祥
	 * @param Temp 实体
	 * @since fhd　Ver 1.1
	 */
	public abstract void mergeTemp(Temp entry);
	
	/**
	 * 
	 * 根据字典项ID取DictEntry实体
	 * 
	 * @author 金鹏祥
	 * @param dictEntryId 字典项ID
	 * @return DictEntry
	 * @since fhd　Ver 1.1
	 */
	public abstract DictEntry findDictEntryById(String dictEntryId);
	
	/**
	 * 
	 * 根据category取Temp实体
	 * 
	 * @author 金鹏祥
	 * @param dictEntryId 字典项ID
	 * @return Temp
	 * @since fhd　Ver 1.1
	 */
	public abstract Temp findTempByCategory(String dictEntryId);
	
	/**
	 * 
	 * 查询temp实体对象集合
	 * 
	 * @author 金鹏祥
	 * @return Temp
	 * @since fhd　Ver 1.1
	 */
	public abstract List<Temp> findTempAll();
}