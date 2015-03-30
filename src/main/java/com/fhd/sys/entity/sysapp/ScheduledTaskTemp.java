package com.fhd.sys.entity.sysapp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 计划任务--模板.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_ST_TEMP")
public class ScheduledTaskTemp extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//模板名称
	@Column(name = "TEMP_NAME")
	private String tempName;
	//触发方式:ST_DEAL_MEASURE,短信；邮件；其他
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEAL_MEASURE")
	private DictEntry dealMeasure;
	//内容
	@Lob
	@Column(name = "ECONTENT")
	private String content;
	//是否有效
	@Column(name = "IS_ENABLED")
	private String isEnabled;
	
	public ScheduledTaskTemp() {
	}

	public ScheduledTaskTemp(String tempName, DictEntry dealMeasure,
			String content, String isEnabled) {
		super();
		this.tempName = tempName;
		this.dealMeasure = dealMeasure;
		this.content = content;
		this.isEnabled = isEnabled;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public DictEntry getDealMeasure() {
		return dealMeasure;
	}

	public void setDealMeasure(DictEntry dealMeasure) {
		this.dealMeasure = dealMeasure;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
}
