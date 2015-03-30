package com.fhd.sys.web.form.menu;



/**
 * @desc 菜单管理FORM
 * @author 邓广义
 *
 */
public class MenuManagementForm implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 父id.
	 */
	private String parentMenu;
	/**
	 * 菜单编号.
	 */
	private String authorityCode;
	/**
	 * 菜单名称.
	 */
	private String authorityName;
	/**
	 * url.
	 */
	private String url;
	/**
	 * 是否是叶子结点.
	 */
	private String isLeafs;
	/**
	 * 排列顺序.
	 */
	private int sn;
	/**
	 * 级别.
	 */
	private Integer rank;
	/**
	 * 查询序列.
	 */
	private String seqNo;
	/**
	 * icon:图标
	 */
	private String icon;
	/**
	 * ETYPE
	 */
	private String etype;



	public String getIsLeafs() {
		return isLeafs;
	}

	public void setIsLeafs(String isLeafs) {
		this.isLeafs = isLeafs;
	}

	public String getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

	public String getAuthorityCode() {
		return authorityCode;
	}

	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}

}
