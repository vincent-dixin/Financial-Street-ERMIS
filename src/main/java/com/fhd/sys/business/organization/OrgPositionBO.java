package com.fhd.sys.business.organization;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.sys.dao.organization.SysPosiDAO;
import com.fhd.sys.dao.organization.SysPosiEmpDAO;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
/**
 * 岗位BO类
 * @author 王再冉
 *
 */

@Service
public class OrgPositionBO {
	
	@Autowired
	private SysPosiDAO o_sysPositionDAO;
	@Autowired
	private SysPosiEmpDAO o_sysPosiEmpDAO; 
	@Autowired
	private EmpGridBO o_sysEmpGridBO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	/**
	 * 查询岗位列表
	 * @param posiName	岗位名称
	 * @param posiId  	岗位id
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysPosition> findPositionByQueryOrIds(String posiName, String posiId,String orgId) {
		Criteria c = o_sysPositionDAO.createCriteria();
		List<SysPosition> list = null;
		//String sysorgId = "";//当前岗位对应的机构id
		if (StringUtils.isNotBlank(posiName)) {
			c.add(Restrictions.eq("posiname", posiName));
		} 
		if (StringUtils.isNotBlank(posiId)) {
			c.add(Restrictions.eq("id", posiId));
			//sysorgId = findPositionByIds(posiId).getSysOrganization().getId();
		}
		//c.add(Restrictions.eq("sysOrganization.id", sysorgId));
		if(StringUtils.isNotBlank(orgId)){
			c.add(Restrictions.eq("sysOrganization.id", orgId));
		}
		
		list = c.list();
		return list;
	}
	/**
	 * 根据机构id查询岗位集合
	 * @param orgId
	 * @return  
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysPosition> findPositionByOrgIds(String orgId) {
		Criteria c = o_sysPositionDAO.createCriteria();
		List<SysPosition> list = null;
		c.add(Restrictions.eq("sysOrganization.id", orgId));
		list = c.list();
		return list;
	}
	
	/**
	 * 根据id查询岗位实体
	 * @param posiId 岗位id
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public SysPosition findPositionByIds(String posiId) {
		Criteria c = o_sysPositionDAO.createCriteria();
		List<SysPosition> list = null;
		
		if (StringUtils.isNotBlank(posiId)) {
			c.add(Restrictions.eq("id", posiId));
		} 
		
		list = c.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	
	/**
	 * 更新岗位实体
	 * @param posi
	 */
	@Transactional
	public void mergePosition(SysPosition posi) {
		o_sysPositionDAO.merge(posi);
	}
	
	/**
	 * 保存岗位
	 * @param posi
	 */
	@Transactional
	public void savePosition(SysPosition posi) {
		o_sysPositionDAO.merge(posi);
		//o_sysPositionDAO.save(posi);
	}
	
	/**
	 * 删除岗位 
	 * @param ids
	 */
	@Transactional
	 public void removePositionByIds(String ids) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			//SysPosition posiEntry = findPositionByIds(id);
			//o_sysPositionDAO.remove(posiEntry);
			SysPosition posi = o_sysPositionDAO.get(id);
			SysOrganization posiorg = posi.getSysOrganization();//得到岗位的上级机构
			if(posiorg.getSysPositions().size()<=1){
				posiorg.setIsLeaf(true);
				o_orgGridBO.mergeOrganization(posiorg);
			}
			o_sysPositionDAO.createQuery("delete from SysPosition sp where sp.id=?",id).executeUpdate();
		}
	}	
	
	/**
	 * 根据岗位id查询是否存在岗位员工关联
	 * @param posiId
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<SysEmpPosi> querySysEmpPosiByPosiId(String posiId){
		Criteria criteria = o_sysPosiEmpDAO.createCriteria();
		criteria.setCacheable(true);
		criteria.setFetchMode("sysPosition", FetchMode.SELECT);
		criteria.createAlias("sysPosition", "p");
		criteria.add(Restrictions.eq("p.id", posiId));
		return criteria.list();
	}
	
	/**
	 * 添加岗位员工实体
	 * @param posiId
	 * @param empItem
	 */
	@Transactional
	public void addPosiEmp(String posiId,String empItem){
		JSONArray jsarr = JSONArray.fromObject(empItem);
		//SysRole role=o_sysoRoleDAO.get(roleId);
		SysPosition posi = findPositionByIds(posiId);
		 for (int i = 0; i < jsarr.size(); i++){
			JSONObject jsobj = jsarr.getJSONObject(i);
			String  empId = jsobj.getString("id"); 
			//SysUser sysUser= o_sysoUserDAO.get(userId);
			SysEmployee emp = o_sysEmpGridBO.findEmpEntryByEmpId(empId);
			SysEmpPosi empPosi = new SysEmpPosi();
			empPosi.setId(Identities.uuid());
			empPosi.setSysEmployee(emp);
			empPosi.setSysPosition(posi);
			//role.getSysUsers().add(sysUser);
			//o_sysoUserDAO.merge(sysUser);
			o_sysPosiEmpDAO.merge(empPosi);
		 }	 
	}

	/**
	 * 通过员工id查询岗位员工实体
	 * @param empId	员工id
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public SysEmpPosi findEmpPosiByempIdAndPosiId(String empId,String posiId) {
		Criteria c = o_sysPosiEmpDAO.createCriteria();
		c.setCacheable(true);
		List<SysEmpPosi> list = null;
		if (StringUtils.isNotBlank(empId)) {
			c.add(Restrictions.eq("sysEmployee.id", empId));
		}
		list = c.list();
		if(list.size()>0){
			for(SysEmpPosi empPosi : list){
				//查询岗位与员工匹配的实体
				if(empPosi.getSysPosition().getId().equalsIgnoreCase(posiId)){
					return empPosi;
				}
			}
		}
		return null;
	}
	/**
	 * 删除员工岗位关联实体
	 * @param empPosiId
	 */
	@Transactional
	public void deleteEmpPosiEntryById(String empPosiId){
		String sql = "delete from t_sys_emp_posi where ID = ?";
		SQLQuery sqlquery = o_sysPosiEmpDAO.createSQLQuery(sql, empPosiId);
		sqlquery.executeUpdate();
	}
	
}
