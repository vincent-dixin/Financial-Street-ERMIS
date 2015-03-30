package com.fhd.comm.web.form.theme;

import com.fhd.comm.entity.theme.Analysis;
/**
 * 
 * @author 王再冉
 *
 */
public class AnalysisForm extends Analysis{

	private static final long serialVersionUID = 1L;
	
	public AnalysisForm(){
		
	}
	
	public AnalysisForm(Analysis analy){
		this.setId(analy.getId());
		this.setAnalyname(analy.getAnalyname());
		this.setAnalydesc(analy.getAnalydesc());
		
	}
	

}
