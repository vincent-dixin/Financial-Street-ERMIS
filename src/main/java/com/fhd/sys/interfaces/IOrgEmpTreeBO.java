package com.fhd.sys.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 机构树接口
 * @author 1
 *
 */
public interface IOrgEmpTreeBO {
	/**
	 * 通过公司id和搜索名查询机构树
	 * @param searchName
	 * @param companyId
	 * @return
	 */
	public abstract Map<String, Object> findOrgTreeBySearchNameAndCompanyId(String searchName, String companyId);
	/**
	 * 搜索机构树
	 * @param searchName
	 * @return
	 */
	public abstract List<SysOrganization> findOrgTreeBySearchName(String searchName);
	/**
	 * 查询当前公司所有机构树
	 * @param companyId
	 * @return
	 */
	public abstract List<SysOrganization> findOrgTreeByCompanyId( String companyId);
	/**
	 * 查询机构树
	 * @param orgList
	 * @param parentOrg
	 * @param searchName
	 * @return
	 */
	public abstract Map<String, Object> findOrgTreeBySome(List<SysOrganization> orgList, SysOrganization parentOrg, String searchName);
	/**
	 * 查询机构id集合
	 * @param searchName
	 * @return
	 */
	public abstract Set<String> findOrgIdsBySearchName(String searchName);

}
