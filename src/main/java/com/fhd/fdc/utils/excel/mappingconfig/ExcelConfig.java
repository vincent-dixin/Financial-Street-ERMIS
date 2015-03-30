package com.fhd.fdc.utils.excel.mappingconfig;

import java.util.List;

/**
 * ClassName:ExcelConfig
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-15		下午07:06:51
 *
 * @see
 */
@SuppressWarnings("rawtypes")
public class ExcelConfig {
	
	private String mappingName;
	
	
	private List mappingList;

	public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	public List getMappingList() {
		return mappingList;
	}

	public void setMappingList(List mappingList) {
		this.mappingList = mappingList;
	}
	
}
