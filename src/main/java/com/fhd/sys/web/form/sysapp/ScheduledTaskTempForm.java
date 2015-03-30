package com.fhd.sys.web.form.sysapp;

import com.fhd.sys.entity.sysapp.ScheduledTaskTemp;

/**
 * 
 * @author user
 *
 */
public class ScheduledTaskTempForm extends ScheduledTaskTemp {
	
	private static final long serialVersionUID = 3370672274252612097L;

	//触发类型
	private String dictEntryName;
	//模板内容
	private String contents;
	
	public String getDictEntryName() {
		return dictEntryName;
	}

	public void setDictEntryName(String dictEntryName) {
		this.dictEntryName = dictEntryName;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
