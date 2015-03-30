package com.fhd.sys.interfaces;

import java.util.List;
import java.util.Set;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 机构列表接口
 * @author 1
 *
 */
public interface IOrgGridBO {
	/**
	 * 查询机构分页
	 * @param orgName
	 * @param page
	 * @param sort
	 * @param dir
	 * @param orgId
	 * @return
	 */
	public abstract Page<SysOrganization> findOrgPageBySome(String orgName, Page<SysOrganization> page, String sort, String dir, String orgId);
	/**
	 * 通过机构id查询机构实体
	 * @param orgId
	 * @return
	 */
	public abstract SysOrganization findOrganizationByOrgId(String orgId);
	/**
	 * 查询机构集合
	 * @param orgId
	 * @return
	 */
	public abstract List<SysOrganization> findOrganizationByOrgIds(List<String> orgIds);
	/**
	 * 通过公司id查询机构集合
	 * @param companyId
	 * @return
	 */
	public abstract List<SysOrganization> findOrganizationByCompanyIds(String companyId);
	/**
	 * 查询机构id集合
	 * @param companyId
	 * @return
	 */
	public abstract Set<String> findOrgIdsByCompanyId(String companyId);
	/**
	 * 根据机构id查询下级所有机构集合
	 * @param orgId
	 * @return
	 */
	public abstract List<SysOrganization> findChildOrgByOrgId(String orgId);
	/**
	 * 通过机构名称模糊查找机构
	 * @param orgName
	 * @return
	 */
	public abstract SysOrganization findOrganizationByorgName(String orgName);
	/**
	 * 删除机构By id
	 * @param ids
	 */
	public abstract void removeOrganizationByIds(List<String> ids);
	/**
	 * 删除机构
	 * @param orgEntry
	 * @param orgChildeSnet
	 */
	public abstract void removeOrganizationBySome(SysOrganization orgEntry, Set<SysOrganization> orgChildeSnet);
	/**
	 * 保存机构
	 * @param org
	 */
	public abstract void saveOrganization(SysOrganization org);
	/**
	 * 更新机构
	 * @param org
	 */
	public abstract void mergeOrganization(SysOrganization org);

}
