/**
 * DictEntryForm.java
 * com.fhd.fdc.commons.web.form.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.dic;

import java.util.Map;

import com.fhd.sys.entity.dic.DictEntry;

/**
 * 数据字典条目Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-8-9  
 * @since    Ver 1.1
 * @Date	 2010-8-14		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class DictEntryForm extends DictEntry{

	private static final long serialVersionUID = 7345721441122950419L;
	/**
	 * 数据字典类型id.
	 */
	private String dictTypeId;
	/**
	 * 状态map，修改时保存值.
	 */
	private Map<String,String> statusMap;
	/**
	 * 数据字典类型map，修改时保存值.
	 */
	private Map<String,String> dictTypeMap;
	
	/**
	 * 编号
	 */
	private String num;
	


	/**
	 * 
	 * <pre>
	 * getNum:
	 * </pre>
	 * 
	 * @author 王 钊
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public String getNum() {
	    return num;
	}
	
	/**
	 * 
	 * <pre>
	 * setNum:
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param num
	 * @since  fhd　Ver 1.1
	 */
	public void setNum(String num) {
	    this.num = num;
	}
	/**
	 * @return String
	 */
	public String getDictTypeId() {
		return dictTypeId;
	}
	/**
	 * @param dictTypeId
	 */
	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	/**
	 * @return Map<String, String>
	 */
	public Map<String, String> getStatusMap() {
		return statusMap;
	}
	/**
	 * @param statusMap
	 */
	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}
	/**
	 * @return Map<String, String>
	 */
	public Map<String, String> getDictTypeMap() {
		return dictTypeMap;
	}
	/**
	 * @param dictTypeMap
	 */
	public void setDictTypeMap(Map<String, String> dictTypeMap) {
		this.dictTypeMap = dictTypeMap;
	}
	/**
	 * @return long
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

