package com.fhd.sys.interfaces;

import java.util.List;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 员工列表接口
 * @author 1
 *
 */
public interface IEmpGridBO {
	
	/**
	 * 查询员工分页
	 * @param empName
	 * @param page
	 * @param sort
	 * @param dir
	 * @return
	 */
	public abstract Page<SysEmployee> findEmpBySome(String empName, Page<SysEmployee> page, String sort, String dir, String orgIds,String positionIds);
	/**
	 * 通过员工id查询员工实体
	 * @param empId
	 * @return
	 */
	public abstract SysEmployee findEmpEntryByEmpId(String empId);
	/**
	 * 保存员工
	 * @param emp
	 */
	public abstract void saveEmp(SysEmployee emp);
	/**
	 * 更新员工
	 * @param emp
	 */
	public abstract void mergeEmp(SysEmployee emp);
	
	/**
	 * 通过员工id删除员工实体
	 * @param ids
	 */
	public abstract void removeEmpEntrys(String ids);
	
	/**
	 * 查询员工职务
	 * @param companyId
	 * @return
	 */
	public abstract List<Duty> findDutyByCompanyId(String companyId);
	/**
	 * 通过员工职务id查询职务实体
	 * @param dutyId
	 * @return
	 */
	public abstract Duty findDutyEntryBydutyId(String dutyId);
	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return
	 */
	public abstract SysUser findUserByuserName(String username);
	/**
	 * 保存用户
	 * @param user
	 */
	public abstract void saveUser(SysUser user);

}
