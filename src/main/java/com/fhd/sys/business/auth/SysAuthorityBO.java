/**
 * SysAuthorityBO.java
 * com.fhd.fdc.commons.business.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.fhd.fdc.commons.orm.sql.SqlBuilder;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.auth.SysAuthorityDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.web.form.auth.SysAuthorityForm;

/**
 * 功能权限BO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */
@Service
@SuppressWarnings("unchecked")
public class SysAuthorityBO {

	@Autowired
	private SysAuthorityDAO o_sysAuthorityDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * 取得根结点.
	 * @author 吴德福
	 * @return SysAuthority
	 * @since  fhd　Ver 1.1
	 */
	public SysAuthority getRootOrg() {
		SysAuthority sysAuthority = new SysAuthority();
		DetachedCriteria dc = DetachedCriteria.forClass(SysAuthority.class);
		dc.add(Restrictions.isNull("parentAuthority"));
		dc.add(Restrictions.eq("rank", 1));
		List<SysAuthority> sysAuthorityList = o_sysAuthorityDAO.findByCriteria(dc);
		if(null != sysAuthorityList && sysAuthorityList.size()>0){
			sysAuthority = sysAuthorityList.get(0);
		}
		return sysAuthority;
	}
	/**
	 * 构造根结点的全部树结点：所有子权限的全部结点.
	 * @author 吴德福
	 * @param id 机构id.
	 * @param contextPath 发布应用路径.
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadAuthorityTree(Serializable id,
			String contextPath) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		SysAuthority sysAuthority = o_sysAuthorityDAO.get(id);
		Set<SysAuthority> subAuthoritys = sysAuthority.getSubAuthoritys();
		for (SysAuthority subAuthority : subAuthoritys) {
			Map<String, Object> node = new HashMap<String, Object>();
			//node.put("id", subAuthority.getId() + RandomUtils.nextInt(9999));
			node.put("id", subAuthority.getId());
			node.put("name", subAuthority.getId());
			node.put("text", subAuthority.getAuthorityName());
			node.put("leaf", subAuthority.getIsLeaf());
			node.put("href", contextPath + "/sys/auth/authority/tabs.do?id=" + subAuthority.getId());
			node.put("hrefTarget", "mainframe");
			String iconCls = null;
			if(subAuthority.getIsLeaf()==true)
			{   
				iconCls="icon-menu";
			}else{
			    iconCls="icon-tree-folder";
			}
			node.put("iconCls", iconCls);
			node.put("cls", "authority");
			node.put("draggable", true);
			nodes.add(node);
		}

		return nodes;
	}
	/**
	 * 构造根结点的静态树json数据.
	 * @author 吴德福
	 * @param id 权限id.
	 * @param roleid 角色id.
	 * @return String json数据
	 * @since  fhd　Ver 1.1
	 */
	public String loadStaticAuthorityTree(Serializable id) {
		Criteria criteria = o_sysAuthorityDAO.createCriteria();
		criteria.setFetchMode("parentAuthority", FetchMode.SELECT);
		criteria.setFetchMode("subAuthoritys", FetchMode.SELECT);
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysUsers", FetchMode.SELECT);
		criteria.setFetchMode("sysPosition", FetchMode.SELECT);
		//criteria.setFetchMode("sysGroup", FetchMode.SELECT);
		criteria.add(Restrictions.not(Restrictions.eq("id", id)));
		criteria.addOrder(Order.asc("seqNo"));
		List<SysAuthority> list = criteria.list();
		SysAuthority sysAuthority = o_sysAuthorityDAO.get(id);
		String res = this.getChildTest(new HashSet<SysAuthority>(list), sysAuthority);
		if(res.length()>0){
			return res.substring(0, res.length()-1);
		}else{
			return res;
		}
	}
	/**
	 * 构造根结点的静态树json数据.
	 * @author 吴德福
	 * @param list
	 * @param parentSysAuthority
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	private String getChildTest(Set<SysAuthority> set,SysAuthority parentSysAuthority) {
        String result = "";
        List<SysAuthority> childrenList = (List<SysAuthority>) o_sysAuthorityDAO.createCriteria()
        		.add(Restrictions.eq("parentAuthority", parentSysAuthority)).list();
        Iterator<SysAuthority> iterator = childrenList.iterator();
        if (childrenList.size() > 0 ){//不是叶子节点
        	if(null != parentSysAuthority.getParentAuthority()){
        		result="{"
                    +"id:'"+parentSysAuthority.getId()+"',"
                    +"text:'"+parentSysAuthority.getAuthorityName()+"',"
                    +"expanded:true,"
                    +"checked:true,"
                    +"children:";
        	}
            for (int i=0;i<childrenList.size();i++) {
            	SysAuthority sysAuthority = (SysAuthority)iterator.next();
                if (childrenList.size() == 1) {
                    result += "["+ getChildTest(sysAuthority.getSubAuthoritys(),sysAuthority) +"]";
                } else {
                    if (i == childrenList.size() -1) {//last row
                        result +=  getChildTest(sysAuthority.getSubAuthoritys(),sysAuthority) +"]";
                    } else if( i == 0 ){//first row
                        result += "["+ getChildTest(sysAuthority.getSubAuthoritys(),sysAuthority) +"," ;
                    } else {//middle row
                        result +=  getChildTest(sysAuthority.getSubAuthoritys(),sysAuthority) +"," ;
                    }
                }
                
                    
            }
            result=result+"}"; 
        } else {//是叶子节点
             result="{"
                +"id:'"+parentSysAuthority.getId()+"',"
                +"text:'"+parentSysAuthority.getAuthorityName()+"',"
                +"checked:true,"
                +"leaf: true"
                +"}"; 
        }    
        return result;
    }
	/**
	 * 查询选择权限的所有的下级权限.
	 * @author 吴德福
	 * @param id 选择的权限id.
	 * @return List<SysAuthority> 权限集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysAuthority> query(String id, List<PropertyFilter> filters) {
		return o_sysAuthorityDAO.find(filters, "sn", true, Restrictions.like("seqNo", "." + id
						+ ".", MatchMode.ANYWHERE), Restrictions
						.not(Restrictions.eq("id", id)));
	}
	/**
	 * 查询所有的权限.SelectTag控件中┠展示.
	 * @author 吴德福
	 * @return List<SysModule> 模块集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysAuthority> queryAllAuthorityBySqlServer(){
		Map<String,String> model = new HashMap<String,String>();
		String sql = SqlBuilder.getSql("select_authorityName", model);
		return o_sysAuthorityDAO.find(sql);
	}
	/**
	 * 新增功能权限.
	 * @author 吴德福
	 * @param sysAuthority
	 * @since  fhd　Ver 1.1
	 */
	@Transactional	
	public Object saveAuthority(SysAuthority sysAuthority){
		try {
			SysAuthority parentAuthority = queryAuthorityById(sysAuthority.getParentAuthority().getId());
			parentAuthority.setIsLeaf(false);
			updateAuthority(parentAuthority);
			sysAuthority.setParentAuthority(parentAuthority);
			sysAuthority.setRank(parentAuthority.getRank() + 1);
			sysAuthority.setSeqNo(parentAuthority.getSeqNo() + sysAuthority.getId() + ".");
			o_businessLogBO.saveBusinessLogInterface("新增", "功能权限", "成功", sysAuthority.getAuthorityCode(),sysAuthority.getAuthorityName(),String.valueOf(sysAuthority.getSn()));
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "功能权限", "失败", sysAuthority.getAuthorityCode(),sysAuthority.getAuthorityName(),String.valueOf(sysAuthority.getSn()));
		}
		return updateAuthority(sysAuthority);
	}
	/**
	 * 修改功能权限.
	 * @author 吴德福
	 * @param sysAuthority
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public Object updateAuthority(SysAuthority sysAuthority){
		try {
			o_businessLogBO.modBusinessLogInterface("修改", "功能权限", "成功", sysAuthority.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "功能权限", "失败", sysAuthority.getId());
		}
		return o_sysAuthorityDAO.merge(sysAuthority);
	}
	/**
	 * 删除功能权限.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeAuthority(String id){
		try {
			o_sysAuthorityDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "功能权限", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "功能权限", "失败", id);
		}
	}
	/**
	 * 查询所有的权限.
	 * @author 吴德福
	 * @return List<SysAuthority>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysAuthority> queryAllAuthority(){
		return o_sysAuthorityDAO.getAll();
	}
	/**
	 * 根据id查询功能权限
	 * @author 吴德福
	 * @param id
	 * @return SysAuthority 功能权限.
	 * @since  fhd　Ver 1.1
	 */
	public SysAuthority queryAuthorityById(String id){
		return o_sysAuthorityDAO.get(id);
	}
	/**
	 * 查询作用于菜单的所有功能权限.
	 * @author 吴德福
	 * @return List<SysAuthority>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysAuthority> queryAllAuthorityByAuthority(){
		Criteria criteria = o_sysAuthorityDAO.createCriteria();
		criteria.setFetchMode("parentAuthority", FetchMode.SELECT);
		criteria.setFetchMode("subAuthoritys", FetchMode.SELECT);
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysUsers", FetchMode.SELECT);
		criteria.setFetchMode("sysPosition", FetchMode.SELECT);
		criteria.add(Restrictions.eq("isAuthority", true));
		return criteria.list();
	}
	/**
	 * 根据查询条件查询所有的作用于菜单的权限.
	 * @author 吴德福
	 * @param page
	 * @param authorityCode
	 * @param authorityName
	 * @return Page<SysAuthority>
	 * @since  fhd　Ver 1.1
	 */
	public Page<SysAuthority> queryAuthorityList(Page<SysAuthority> page, String authorityCode,String authorityName){
		DetachedCriteria dc = DetachedCriteria.forClass(SysAuthority.class);
		
		dc.add(Restrictions.eq("isAuthority", true));
		if(StringUtils.isNotBlank(authorityCode)){
			dc.add(Restrictions.like("authorityCode", authorityCode, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(authorityName)){
			dc.add(Restrictions.like("authorityName", authorityName, MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("authorityCode"));
		return o_sysAuthorityDAO.pagedQuery(dc, page);
	}
	/**
	 * 根据查询条件查询模块.
	 * @author 吴德福
	 * @param filters 页面查询条件传递的参数集合.
	 * @return List<SysAuthority> 功能权限集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysAuthority> query(SysAuthorityForm sysAuthorityForm) {
		StringBuilder hql = new StringBuilder();
		hql.append("From SysAuthority Where 1=1 ");
		String authorityCode = "";
		String authorityName = "";
		String parentId = "";
		if(sysAuthorityForm != null && !"".equals(sysAuthorityForm.getAuthorityCode()) && sysAuthorityForm.getAuthorityCode() != null){
			authorityCode = sysAuthorityForm.getAuthorityCode();
			hql.append(" and authorityCode like '%"+authorityCode+"%'");
		}
		if(sysAuthorityForm != null && !"".equals(sysAuthorityForm.getAuthorityName()) && sysAuthorityForm.getAuthorityName() != null){
			authorityName = sysAuthorityForm.getAuthorityName();
			hql.append(" and authorityName like '%"+authorityName+"%'");
		}
		if(sysAuthorityForm != null && !"".equals(sysAuthorityForm.getParentId()) && sysAuthorityForm.getParentId() != null){
			parentId = sysAuthorityForm.getParentId();
			hql.append(" and seqNo like '%."+parentId+".%'");
		}
		return o_sysAuthorityDAO.find(hql.toString());
	}
	/**
	 * Ext拖拽移动node是否成功.
	 * @author 吴德福
	 * @param currentId 当前拖拽的结点id.
	 * @param pid 当前拖拽的父结点id.
	 * @param targetId 当前拖拽的结点的目标结点id.
	 * @return Boolean 操作是否成功.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public Boolean moveNode(String currentId,String pid,String targetId){
		try {
			SysAuthority sysAuthority = queryAuthorityById(currentId);
			sysAuthority.setParentAuthority(queryAuthorityById(targetId));
			o_sysAuthorityDAO.merge(sysAuthority);
			o_businessLogBO.modBusinessLogInterface("修改", "权限", "成功", currentId, targetId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "权限", "失败", currentId, targetId);
			return false;
		}
	}
	/**
	 * 获取Authority 分页列表
	 * @author 万业
	 * @param page
	 * @param authForm
	 * @return
	 */
	public Page<SysAuthority> queryAuthPage(Page<SysAuthority> page,String authorityCode, String authorityName, String parentId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(SysAuthority.class,"auth");
		if(StringUtils.isNotBlank(authorityCode)){
			dc.add(Property.forName("authorityCode").like(authorityCode, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(authorityName)){
			dc.add(Property.forName("authorityName").like(authorityName, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(parentId)){
			dc.add(Property.forName("auth.parentAuthority.id").eq(parentId));
		}
		
		return o_sysAuthorityDAO.pagedQuery(dc, page);
		
	}
	/**
	 * 把seqid转成auth名字
	 * @author 万业
	 * @param ids
	 * @return
	 */
	public String getGuidName(String[] ids){
		StringBuffer guidNames=new StringBuffer("");
		String result="";
		if(ids.length!=0){
			for (int i = 0; i < ids.length-1; i++) {
				if(StringUtils.isNotBlank(ids[i])){
					guidNames.append(this.o_sysAuthorityDAO.get(ids[i]).getAuthorityName());
					guidNames.append(">>");
				}
			}
			result=guidNames.substring(0, guidNames.length()-2);
		}
		return result;
	}
}

