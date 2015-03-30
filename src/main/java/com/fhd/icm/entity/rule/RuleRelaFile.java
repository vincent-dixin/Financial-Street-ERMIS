package com.fhd.icm.entity.rule;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 规章制度关联附件
 * @author   张  雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-19		上午11:44:27
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_RULE_RELA_FILE")
public class RuleRelaFile extends IdEntity implements Serializable {
	private static final long serialVersionUID = 8377563947857281791L;
	/**
	 * 规章制度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	private Rule rule;
	
	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity file;
	
	public RuleRelaFile(){
		
	}
	
	public RuleRelaFile(String id){
		super.setId(id);
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
	
}


