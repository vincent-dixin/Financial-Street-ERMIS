package com.fhd.sys.business.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.ObjectUtil;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.auth.SysAuthorityDAO;
import com.fhd.sys.dao.auth.SysRoleDAOold;
import com.fhd.sys.dao.group.GroupDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.group.SysGroup;
import com.fhd.sys.web.form.group.GroupForm;

/**
 * 工作组BO.
 * @ClassName GroupBO.java
 * @Version 1.0
 * @author zhaotao
 * @Date 2011-1-19
 */
@Service
public class GroupBO {
	
	@Autowired
	private GroupDAO o_groupDAO;
	@Autowired
	private SysRoleDAOold o_sysRoleDAO;
	@Autowired
	private SysAuthorityDAO o_sysAuthorityDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;

	/**
	 * 获取更节点
	 * @return SysGroup
	 */
	@SuppressWarnings("rawtypes")
	public SysGroup getRoot()throws Exception {
		SysGroup sysGroup = new SysGroup();
		DetachedCriteria dc = DetachedCriteria.forClass(SysGroup.class);
		dc.add(Property.forName("parentGroup").isNull());
		List list=o_groupDAO.findByCriteria(dc);
		if(null != list && list.size()!=0){
			sysGroup = (SysGroup)list.get(0);
		}
		return sysGroup;
	}
	/**
	 * 根据id查询工作组.
	 * @author 吴德福
	 * @param id
	 * @return SysGroup
	 * @since  fhd　Ver 1.1
	 */
	public SysGroup get(String id) {
		return o_groupDAO.get(id);
	}
	/**
	 * 保存工作组.
	 * @param group
	 * @return void
	 */
	@Transactional
	public void save(SysGroup group) {
		try {
			o_groupDAO.merge(group);
			o_businessLogBO.saveBusinessLogInterface("修改", "工作组", "成功", group.getGroupName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("修改", "工作组", "失败", group.getGroupName());
		}
	}
	/**
	 * 新增工作组.
	 * @param form
	 * @return void
	 */
	@Transactional
	public void add(GroupForm form) {
		SysGroup parentSysGroup = o_groupDAO.get(form.getParentId());
		parentSysGroup.setIsLeaf(false);
		o_groupDAO.merge(parentSysGroup);
		
		SysGroup group = new SysGroup();
		BeanUtils.copyProperties(form, group);
		group.setId(Identities.uuid());
		group.setParentGroup(parentSysGroup);
		group.setIsLeaf(true);
		group.setGroupStatus("1");
		StringBuilder seq = new StringBuilder(".");
		SysGroup g = group;
		while (g != null) {
			seq.insert(0, g.getId());
			seq.insert(0, ".");
			g = g.getParentGroup();
		}
		group.setGroupSeq(seq.toString());
		try {
			o_groupDAO.merge(group);
			o_businessLogBO.saveBusinessLogInterface("新增", "工作组", "成功", group.getGroupName());
		} catch (BeansException e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "工作组", "失败", group.getGroupName());
		}

	}
	/**
	 * 删除工作组
	 * @param ids
	 */
	@Transactional
	public void delele(String[] ids) {
		try{
			for (String id : ids) {
				o_groupDAO.removeById(id);
				o_businessLogBO.delBusinessLogInterface("批量删除", "工作组", "成功", id);
			}
		}catch(Exception e){
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("批量删除", "工作组", "失败");
		}
	}
	/**
	 *單個刪除工作組 
	 *@param id
	 */
	@Transactional
	public void deleteSingle(String id){
		try {
			o_groupDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "工作组", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("删除", "工作组", "失败");
		}
	}
	/**
	 * 相关角色列表数据
	 * 
	 * @param id
	 * @return
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getRelativeRole(String id) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		SysGroup group = this.o_groupDAO.get(id);
		if(null==group){
			return null;
		}
		for (SysRole r : group.getSysRoles()) {
			Map<String, String> m = new HashMap<String, String>();
			ObjectUtil.ObjectPropertyToMap(m, r);
			datas.add(m);
		}
		reMap.put("totalCount", group.getSubGroups().size());
		reMap.put("datas", datas);
		return reMap;
	}

	/**
	 * 添加相关角色
	 * 
	 * @param ids
	 * @return void
	 */
	@Transactional
	public void addRelativeRole(String id, String ids) {
		if (ids == null)
			return;
		SysGroup group = this.o_groupDAO.get(id);
		String[] arr = ids.split(",");
		for (String oid : arr) {
			SysRole role = this.o_sysRoleDAO.get(oid);
			if (role != null) {
				group.getSysRoles().add(role);
			}
		}
		this.o_groupDAO.merge(group);
	}

	/**
	 * 删除相关角色
	 * 
	 * @param id
	 * @param ids
	 * @return void
	 */
	@Transactional
	public void deleteRelativeRole(String id, String ids) {
		SysGroup group = this.o_groupDAO.get(id);
		String[] arr = ids.split(",");
		for (String oid : arr) {
			SysRole role = this.o_sysRoleDAO.get(oid);
			if (role != null) {
				group.getSysRoles().remove(role);
			}
		}
		this.o_groupDAO.merge(group);
	}

	/**
	 * 将已经选择的权限拼接成字符串
	 * 
	 * @param id
	 * @return
	 * @return String
	 */
	public String selectedAuthority2String(String id) {
		StringBuilder sb = new StringBuilder(",");
		SysGroup group = this.o_groupDAO.get(id);
		for (SysAuthority r : group.getSysAuthorities()) {
			sb.append(r.getId() + ",");
		}
		return sb.toString();
	}

	/**
	 * 权限树
	 * 
	 * @param id
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getAuthority(String id) {
		SysAuthority auth = null;
		if ("rootId".equals(id)) {
			DetachedCriteria dc = DetachedCriteria.forClass(SysAuthority.class);
			dc.add(Property.forName("parentAuthority").isNull());
			auth = (SysAuthority) this.o_sysAuthorityDAO.findByCriteria(dc).get(0);
		} else {
			auth = this.o_sysAuthorityDAO.get(id);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (SysAuthority a : auth.getSubAuthoritys()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", a.getId());
			m.put("text", a.getAuthorityName());
			m.put("leaf", a.getSubAuthoritys().size() == 0);
			m.put("checked", false);
			list.add(m);
		}
		return list;
	}

	/**
	 * 处理权限
	 * 
	 * @param id
	 * @param ids
	 * @return
	 * @return String
	 */
	public void saveAuthority(String id, String ids) {
		if (ids != null) {
			SysGroup group = this.o_groupDAO.get(id);
			group.getSysAuthorities().clear();
			String[] arr = ids.split(",");
			for (String tid : arr) {
				SysAuthority au = this.o_sysAuthorityDAO.get(tid);
				if (au != null) {
					if (!group.getSysAuthorities().contains(au))
						group.getSysAuthorities().add(au);
				}
			}
		}
	}

	/**
	 * 根据编号获取工作组
	 * 
	 * @param groupCode
	 * @return
	 * @return Object
	 */
	public Object queryGroupByCode(String groupCode) {
		List<SysGroup> list = this.o_groupDAO.findBy("groupCode", groupCode);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	/**
	 * 构造根结点的全部树结点：所有子工作组的全部结点.
	 * @author 吴德福
	 * @param id 工作组id.
	 * @param contextPath 发布应用路径.
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadSysGroupTree(String id, String contextPath) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(id)){
			SysGroup sysGroup = o_groupDAO.get(id);
			if(null != sysGroup){
				Set<SysGroup> sysGroups = sysGroup.getSubGroups();
				if(null != sysGroups && sysGroups.size()>0){
					for (SysGroup group : sysGroups) {
						Map<String, Object> treeNode = new HashMap<String, Object>();
						treeNode.put("id", group.getId());
						treeNode.put("text", group.getGroupName());
						treeNode.put("name",group.getId());
						treeNode.put("leaf", group.getIsLeaf());
						treeNode.put("href", contextPath + "/sys/group/tabs.do?id=" + group.getId());
						treeNode.put("hrefTarget", "mainframe");
						treeNode.put("iconCls", "icon-group");
						treeNode.put("cls", "sysGroup");
						treeNode.put("draggable", true);
						nodes.add(treeNode);
					}
				}
			}
		}
		return nodes;
	}
	public List<SysGroup> getNextGroup(String id) {
		List<SysGroup> sysGroups = null;
		SysGroup parGroup = o_groupDAO.get(id);
		String parId = parGroup.getId();
		Criteria criteria = o_groupDAO.createCriteria(Restrictions.eq("parentGroup.id", id));
		sysGroups = criteria.list();
		return sysGroups;
		
	}
}
