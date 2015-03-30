package com.fhd.icm.web.form;

import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.sys.entity.dic.DictType;

/**
 * @author   宋佳  
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-6		下午3:09:52
 *
 * @see 	 
 */
public class ConstructPlanForm extends ConstructPlan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//补充的组织机构内容
	private String groupLeaderId;
	private String groupLeaderIdHid;
	
	private String groupPersId;
	private String groupPersIdHid;
	
	public String getGroupLeaderIdHid() {
		return groupLeaderIdHid;
	}
	public void setGroupLeaderIdHid(String groupLeaderIdHid) {
		this.groupLeaderIdHid = groupLeaderIdHid;
	}
	public String getGroupPersIdHid() {
		return groupPersIdHid;
	}
	public void setGroupPersIdHid(String groupPersIdHid) {
		this.groupPersIdHid = groupPersIdHid;
	}
	public String getGroupPersId() {
		return groupPersId;
	}
	public void setGroupPersId(String groupPersId) {
		this.groupPersId = groupPersId;
	}
	public void setGroupLeaderId(String groupLeaderId) {
		this.groupLeaderId = groupLeaderId;
	}
	public String getGroupLeaderId() {
		return groupLeaderId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}

