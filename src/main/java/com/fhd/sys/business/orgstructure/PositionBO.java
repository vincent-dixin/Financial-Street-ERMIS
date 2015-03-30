/**
 * PosiManagementBusiness.java
 * com.fhd.fdc.commons.business.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-9 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
*/

package com.fhd.sys.business.orgstructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.core.dao.utils.PropertyFilter;
import com.fhd.fdc.utils.UserContext;
import com.fhd.fdc.utils.comparator.SysEmpPosiComparator;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.auth.SysUserDAO;
import com.fhd.sys.dao.orgstructure.SysPositionDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;


/**
 * 岗位BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-19 
 * @since    Ver 1.1
 * @Date	 2010-9-19		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class PositionBO {

	@Autowired
	private SysPositionDAO o_sysPositionDAO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private SysUserDAO o_sysUserDAO;
	
	/**
	 * 构造岗位的全部树结点：所有员工结点.
	 * @author 吴德福
	 * @param id 岗位id.
	 * @param contextPath 发布应用路径.
	 * @return List<Map<String, Object>> 岗位的树结点集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadPosiTree(Serializable id,String contextPath,String empfilter) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>();
		SysPosition sysPosition = o_sysPositionDAO.get(id);
		Set<SysEmpPosi> sysEmpPosis = sysPosition.getSysEmpPosis();
		ArrayList<SysEmpPosi> sortCol=new ArrayList<SysEmpPosi>();
		sortCol.addAll(sysEmpPosis);
		Collections.sort(sortCol, new SysEmpPosiComparator());
		for (SysEmpPosi sysEmpPosi : sortCol) {
			//人员过滤
			if(StringUtils.isNotBlank(empfilter)){
				String fhead = empfilter.split("/")[0];
				String fitem = empfilter.split("/")[1];
				if(fhead.equalsIgnoreCase("role")){
					boolean flagisexsit=false;
					//没有用户信息存在就不装载
					if(StringUtils.isBlank(sysEmpPosi.getSysEmployee().getUserid())){
						continue;
					}
					Set<SysRole> roles = o_sysUserDAO.get(sysEmpPosi.getSysEmployee().getUserid()).getSysRoles();
					for(SysRole role:roles){
						if(role.getRoleCode().equalsIgnoreCase(fitem)){
							flagisexsit=true;
							break;
						}
					}
					//没有这样的角色就过滤掉
					if(!flagisexsit){
						continue;
					}
				}
				
			}
			
			//sysEmpPosi.getSysEmployee();
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", sysEmpPosi.getSysEmployee().getId());
			node.put("dbid", sysEmpPosi.getSysEmployee().getId());
			node.put("text", sysEmpPosi.getSysEmployee().getEmpname());
			node.put("leaf", true);
			node.put("href",contextPath + "/sys/orgstructure/emp/tabs.do?id=" + sysEmpPosi.getSysEmployee().getId());//"javascript:void(0)");
			node.put("hrefTarget", "mainframe");
			DictEntry dictEntry = new DictEntry();
			dictEntry = o_dictEntryBO.queryDictEntryById(sysEmpPosi.getSysEmployee().getGender());
			node.put("iconCls", "男".equals(dictEntry.getName()) ? "icon-male" : "icon-female");
			node.put("cls", "emp");
			node.put("draggable", true);
			nodes.add(node);
			
		}
		return nodes;
	}

	/**
	 * 根据岗位id查询岗位信息.
	 * @author 胡迪新
	 * @param id 岗位id
	 * @return SysPosition 岗位信息.
	 * @since  fhd　Ver 1.1
	 */
	public SysPosition get(Serializable id) {
		return o_sysPositionDAO.get(id);
	}
	
	/**
	 * 保存岗位信息.
	 * @author 胡迪新
	 * @param position 岗位信息.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void merge(SysPosition position) {
		try {
			SysOrganization organization = position.getSysOrganization();
			organization.setIsLeaf(false);
			o_organizationBO.merge(organization);
			o_sysPositionDAO.merge(position);
			o_businessLogBO.saveBusinessLogInterface("新增", "岗位", "成功", position.getPosicode(),position.getPosiname(),position.getPosiStatus(),String.valueOf(position.getSn()));
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "岗位", "失败", position.getPosicode(),position.getPosiname(),position.getPosiStatus(),String.valueOf(position.getSn()));
		}
	}
	
	/**
	 * 根据查询条件查询岗位.
	 * @author 胡迪新
	 * @param id
	 * @param filters
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysPosition> queryByOrg(String id,List<PropertyFilter> filters){
		return o_sysPositionDAO.find(filters, "sn", true, Restrictions.eq("sysOrganization.id", id));
	}

	/**
	 * 删除岗位.
	 * @author 胡迪新
	 * @param id 岗位id.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void delete(String id) {
		try {
			o_sysPositionDAO.createQuery("delete from SysPosition sp where sp.id=?",id).executeUpdate();
		    //o_sysPositionDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "岗位", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "岗位", "失败", id);
		}
	}
	/**
	 * 根据机构id查询该机构下的所有的岗位.
	 * @author 吴德福
	 * @param id
	 * @return List<SysPosition>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysPosition> queryPosiByOrgid(String id){
		Criteria criteria = o_sysPositionDAO.createCriteria();
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.createAlias("sysOrganization", "o");
		criteria.setFetchMode("o.sysPositions", FetchMode.SELECT);
		criteria.add(Restrictions.eq("o.id", id));
		return criteria.list();
		//return o_sysPositionDAO.findBy("sysOrganization.id", id);
	}
	/**
	 * 查询岗位当前拥有的权限.
	 * @author 吴德福
	 * @param posiid 岗位id.
	 * @return List<String> 选中的权限集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<String> queryAuthorityByPosi(String posiid){
		List<String> authorityIds = new ArrayList<String>();
		SysPosition sysPosition = o_sysPositionDAO.get(posiid);
		for (SysAuthority authority : sysPosition.getSysAuthorities()) {
			authorityIds.add(authority.getId());
		}
		return authorityIds;
	}
	/**
	 * 机构下的岗位分页 查询
	 * @author 万业
	 * @param page
	 * @param id 机构ID
	 * @param sysPosi
	 * @return
	 */
	public Page<SysPosition> queryPosiByOrgIdPage(Page<SysPosition> page, String id, SysPosition sysPosi){
		DetachedCriteria dc=DetachedCriteria.forClass(SysPosition.class);
		dc.add(Property.forName("sysOrganization.id").eq(id));
		if(StringUtils.isNotBlank(sysPosi.getPosicode())){
			dc.add(Property.forName("posicode").like(sysPosi.getPosicode(),MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(sysPosi.getPosiname())){
			dc.add(Property.forName("posiname").like(sysPosi.getPosiname(),MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("id"));
		return o_sysPositionDAO.pagedQuery(dc, page);
	}
	
	/**
	 * <pre>
	 * getPositionByPName:根据岗位名称获取岗位
	 * </pre>
	 * 
	 * @author David
	 * @param pName
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysPosition> getPositionByPName(String pName,String companyId){
		 StringBuilder hql = new StringBuilder("from SysPosition sp,SysOrganization so where sp.sysOrganization.id=so.id and sp.posiname='")
		 .append(pName).append("' and so.orgseq like '%.").append(companyId).append(".%' ");
		 
		 List<Object[]> sps = o_sysPositionDAO.find(hql.toString());
		 List<SysPosition> tempSps = new ArrayList<SysPosition>();
		 for(Object[] position:sps){
			 tempSps.add((SysPosition)position[0]);
		 }
		return tempSps;
	}
	
	/**
	 * 根据查询内容得到岗位树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @since fhd Ver 1.1
	 */
	public List<String> getPathsBySearchName(String searchName){
		DetachedCriteria dc = DetachedCriteria.forClass(SysPosition.class);
		dc.add(Restrictions.ilike("posiname", searchName, MatchMode.ANYWHERE));
		dc.add(Restrictions.eq("sysOrganization.id", UserContext.getUser().getCompanyid()));
		dc.addOrder(Order.asc("posiname"));
		List<SysPosition> positonList = o_sysPositionDAO.findByCriteria(dc);
		List<String> pathList=new ArrayList<String>();
		for(SysPosition position: positonList){
			pathList.addAll(getPositionOrgSeqById(position));
		}
		return pathList;
	}
	
	/**
	 * <pre>
	 * getPositionOrgSeqById:根据岗位ID获取组织树SEQ
	 * </pre>
	 * 
	 * @author David
	 * @param position
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<String> getPositionOrgSeqById(SysPosition position){
		List<String> rev = new ArrayList<String>();
		
		//1.岗位-部门-公司
		StringBuffer sb1 = new StringBuffer();
		sb1.append(position.getSysOrganization().getOrgseq().replace(".", "/")).append(position.getId());
		rev.add(sb1.toString());
		
		return rev;
	}
}

