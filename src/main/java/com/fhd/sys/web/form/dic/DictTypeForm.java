package com.fhd.sys.web.form.dic;

import java.util.Map;

import com.fhd.sys.entity.dic.DictType;

/**
 * 数据字典类型Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-9
 * Company FirstHuiDa.
 */

public class DictTypeForm extends DictType{

	private static final long serialVersionUID = 7161768340761198518L;
	/**
	 * 上一级名称，修改时默认选中值.
	 */
	private Map<String,String> parentMap;
	/**
	 * @return Map<String, String>
	 */
	public Map<String, String> getParentMap() {
		return parentMap;
	}
	/**
	 * @param parentMap
	 */
	public void setParentMap(Map<String, String> parentMap) {
		this.parentMap = parentMap;
	}
	/**
	 * @return long
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
