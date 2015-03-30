
package com.fhd.process.web.form;
import com.fhd.icm.entity.control.Measure;
/**
 * @author  宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-17		下午2:40:43
 *
 * @see 	 
 */
public  class MeasureForm extends Measure{
	private static final long serialVersionUID = 657313864694079311L;
	/**
	 * 控制关联节点
	 */
	private String pointNote;
	/**
	 * 人员Id
	 */
	private String meaSureempId; 
	/**
	 * 组织机构Id
	 */
	private String meaSureorgId; 
	public String getPointNote() {
		return pointNote;
	}
	public void setPointNote(String pointNote) {
		this.pointNote = pointNote;
	}
	public String getMeaSureempId() {
		return meaSureempId;
	}
	public void setMeaSureempId(String meaSureempId) {
		this.meaSureempId = meaSureempId;
	}
	public String getMeaSureorgId() {
		return meaSureorgId;
	}
	public void setMeaSureorgId(String meaSureorgId) {
		this.meaSureorgId = meaSureorgId;
	}
   
}

