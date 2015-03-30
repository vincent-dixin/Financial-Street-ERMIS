package com.fhd.comm.business;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.dao.CategoryDAO;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.CategoryRelaOrgEmp;
import com.fhd.comm.interfaces.ICategoryTreeBO;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

@Service
public class CategoryTreeBO implements ICategoryTreeBO{

	@Autowired CategoryBO o_categoryBO;
	
	@Autowired
	private OrganizationBO o_organizationBo;
	
	@Autowired
    private CategoryDAO o_categoryDAO;
	
	@Override
	public List<Map<String, Object>> findRootBO() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>(); // 存放个对象
		Map<String, Object> items = new HashMap<String, Object>(); // 存放
		String companyId = UserContext.getUser().getCompanyid();// 所在公司id
		SysOrganization sysOrganization = o_organizationBo.getRootOrgByCompanyId(companyId);//获取机构树根目录

		item = new HashMap<String, Object>();
		item.put("id", "category_root");
		item.put("text", "记分卡");
		item.put("dbid", "category_root");
		item.put("leaf", "false");
		item.put("code", "category");
		item.put("type", "kpi_category");
		item.put("expanded", "false");
		item.put("iconCls", "icon-ibm-icon-scorecards");
		items.put("category",item);
		
		item = new HashMap<String, Object>();
		item = this.objectPackage(sysOrganization, false, false, false);
		items.put("orgCategory", item);//机构风险树
		
		item = new HashMap<String, Object>();
		item.put("id", "my_category_root");
		item.put("text", "我的记分卡");
		item.put("dbid", "my_category_root");
		item.put("leaf", "false");
		item.put("code", "my_category");
		item.put("type", "my_kpi_category");
		item.put("expanded", "false");
		items.put("myCategory",item);
		
		list.add(items);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findCategoryTreeNodeBO(String node, Boolean canChecked, String query) {
		Map<String, Object> maps = null;
        boolean expanded = StringUtils.isNotBlank(query) ? true : false; //是否展开节点
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        String companyId = UserContext.getUser().getCompanyid();//所在公司id
        Set<String> idSet = new HashSet<String>();
        if (StringUtils.isNotBlank(node))
        {//根据父节点查询所有子节点
            Criteria criteria = o_categoryDAO.createCriteria();
            criteria.add(eq("company.id", companyId));
            if ("category_root".equals(node))
            {
                criteria.add(Restrictions.isNull("parent"));
            }
            else
            {
                criteria.add(Restrictions.eq("parent.id", node));
            }

            criteria.add(Restrictions.eq("deleteStatus", true));
            criteria.addOrder(Order.asc("name"));
            List<Category> parentCategorys = criteria.list();

            Criteria criteriaQuery = this.o_categoryDAO.createCriteria();

            if (StringUtils.isNotBlank(query))
            {
                criteriaQuery.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
            }
            criteriaQuery.add(eq("company.id", companyId));
            criteriaQuery.addOrder(Order.asc("name"));
            List<Category> categoryList = criteriaQuery.list();

            for (Category entity : categoryList)
            {
                String[] idsTemp = entity.getIdSeq().split("\\.");
                idSet.addAll(Arrays.asList(idsTemp));
            }

            for (Category category : parentCategorys)
            {
                if (idSet.size() > 0 && idSet.contains(category.getId()))
                {
                	maps = this.wrapCategoryNode(category, false, true, expanded);
                    nodes.add(maps);
                }
            }
        }
        return nodes;
	}
	
	@Override
	public List<Map<String, Object>> findCategoryTreeMyNodeBO(String node, Boolean canChecked, String query) {
		Map<String, Object> item = null;//存放tree节点
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//呈现tree节点
		List<CategoryRelaOrgEmp> categoryRelaOrgEmpList = null;
		String companyId = UserContext.getUser().getCompanyid();//所在公司id
		String orgId = "";
		orgId = UserContext.getUser().getMajorDeptId();
		categoryRelaOrgEmpList = o_categoryBO.findCategoryRelaOrgEmpBySome(companyId, orgId, true, query);
		for (CategoryRelaOrgEmp entity : categoryRelaOrgEmpList) {
			item = new HashMap<String, Object>();
			item = this.objectPackage(entity.getCategory(), canChecked, true, false);//存放节点并不已+扩展呈现展示（不呈现树型结构）
			nodes.add(item);//将接节点添加到呈现变量中
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findOrgCategoryTreeNodeBO(String id, String type, Boolean canChecked, String query) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//返回前台的nodelist
		String companyId = UserContext.getUser().getCompanyid();//所在公司id
		HashMap<String, Category> categoryMap = o_categoryBO.findCategoryMapFromNotCategoryRelaOrgEmpByCompanyId(companyId, true);//刨除部门关联记分卡MAP集合
		
		if ("orgCategory".equals(type)) {//IS-集团部门节点
			Map<String, Object> node = null;//node临时对象
			SysOrganization org = null;// 机构实体临时对象
			Set<String> orgIdSet = new HashSet<String>(); //保存目标的所属机构ID
			boolean expanded = StringUtils.isNotBlank(query) ? true : false; //是否展开节点
			List<SysOrganization> orgList = new ArrayList<SysOrganization>();//子节点机构集合
			List<CategoryRelaOrgEmp> CategoryRelaOrgEmpList = o_categoryBO.findCategoryRelaOrgEmpBySome(companyId, true, query);
			String smID = "";
			Set<String> smIdSet = new HashSet<String>();
			
			for (CategoryRelaOrgEmp categoryRelaOrgEmp : CategoryRelaOrgEmpList) {//循环每一个目标
				node = new HashMap<String, Object>();
				org = categoryRelaOrgEmp.getOrg();//机构部门实体
				if (org != null) {//IS-实体不为空
					if (org.getId().equals(id)) {//IS-部门ID等于当前节点ID
						if (categoryMap != null) {//IS-MAP不为空
							if (categoryMap.get(categoryRelaOrgEmp.getCategory().getId()) != null) {//当前风险存在叶子节点
								node = this.objectPackage(categoryRelaOrgEmp.getCategory(), canChecked, false, false);//存放节点并且已+扩展呈现展示
							} else {
								node = this.objectPackage(categoryRelaOrgEmp.getCategory(), canChecked, true, true);//存放节点不已+扩展呈现展示
							}
						}
						smID = id + "_" + categoryRelaOrgEmp.getCategory().getId();
						if (!smIdSet.contains(smID)) {
							smIdSet.add(smID);
							node.put("id", smID);
							nodes.add(node);
						}
					} else {
						org = this.findParentNodeById(org, id);
						if (org != null) {
							orgList.add(org);
						}
					}
				}
				// 将机构节点集合包装为node节点
				for (SysOrganization sysOrg : orgList) {
					if (!orgIdSet.contains(sysOrg.getId())) {
						orgIdSet.add(sysOrg.getId());
						node = new HashMap<String, Object>();
						node = this.objectPackage(sysOrg, false, false,
								expanded);
						node.put("leaf", false);
						nodes.add(node);
					}
				}
			}
			/* 加载其它目标节点 */
			SysOrganization sysorg = o_organizationBo.get(id);
			if (sysorg != null && sysorg.getParentOrg() == null) {// 说明是根组织单元
					// 加入其它节点
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", "category_other");
					item.put("dbid", "caegory_other");
					item.put("code", "category");
					item.put("text", "其它");
					if (StringUtils.isNotBlank(query)) {
						item.put("expanded", true);
					} else {
						item.put("expanded", false);
					}
					item.put("type", "other");
					item.put("leaf", "false");
					nodes.add(item);
			}
		} else {
			if ("other".equals(type)) {
				if (StringUtils.isBlank(query)) {
					List<Category> strategyList = null;
					if("category_other".equalsIgnoreCase(id)){
						strategyList = o_categoryBO.findNotInOrgStrategyMap(companyId ,null, true, true);//当前根下节点
					}else{
						strategyList = o_categoryBO.findNotInOrgStrategyMap(companyId ,id.replace("category_", ""), true, false);//当前节点下的节点
					}
					
					for (Category category : strategyList) {
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", category.getId());
						item.put("dbid", category.getId());
						item.put("code", category.getCode());
						item.put("text", category.getName());
						if(canChecked){
							item.put("checked", false);
						}
						item.put("type", "other");
						if (category.getParent() != null)
							item.put("parentid", category.getParent().getId());

						if (categoryMap != null) {
							if (categoryMap.get(category.getId()) == null) {
								item.put("leaf", category.getIsLeaf());
								item.put("expanded", false);
							}else{
								item.put("leaf", category.getIsLeaf());
								item.put("expanded", false);
							}
						}
						nodes.add(item);
					}
				}
			}
		}

		if (StringUtils.isNotBlank(query)) {//IS-查询情况
			if (nodes.size() != 0) {
				for (Map<String, Object> map : nodes) {
					if (map.get("text").toString().equalsIgnoreCase("其它")) {//IS-其他节点
						return nodes;
					}
				}
			}

			if (id.indexOf("category_") != -1) {
				id = id.replace("category_", "");//过滤节点ID
			}

			Set<String> oidSet = new HashSet<String>();
			List<Category> smList = null;
			if("other".equalsIgnoreCase(id)){
				smList = o_categoryBO.findCategoryBySome(companyId, id, true, true, query);//取当风险总节点
			}else{
				smList = o_categoryBO.findCategoryBySome(companyId, id, true, false, query);//取当风险总节点
			}
			
			Set<String> idsSet = o_categoryBO.findRiskMapFromNotCategoryRelaOrgEmpBySome(true, query);//没有和组织机构关联的Id集合
			for (Category entity : smList) {//遍历实体
				if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {
					Map<String, Object> item = this.wrapStrategyMapNode(entity, canChecked, true, true);//存放变量
					item.put("id", entity.getId());
					oidSet.add(entity.getId());
					nodes.add(item);//存放呈现节点
				}
			}
		}

		return nodes;
	}
	
	/**
	 * 组装其他节点格式
	 * 
	 * @author 金鹏祥
	 * @param category
	 * @param canChecked
	 * @param isLeaf
	 * @param expanded
	 * @return
	*/
	public Map<String, Object> wrapStrategyMapNode(Category category, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", category.getId());
		item.put("dbid", category.getId());
		item.put("code", category.getCode());
		item.put("text", category.getName());
		item.put("type", "other");
		if (category.getParent() != null)
			item.put("parentid", "category_" + category.getParent().getId());
		if (isLeaf) {
			item.put("leaf", category.getIsLeaf());
		}
		if (!isLeaf) {
			item.put("leaf", false);
		}
		if (canChecked) {
			item.put("checked", false);
		}
		if (expanded) {
			item.put("expanded", true);
		}
		return item;
	}
	
	/**
	 * 得到机构关联对象
	 * 
	 * @author 金鹏祥
	 * @param org 结构实体
	 * @param orgId 节点ID
	 * @return SysOrganization
	*/
	public SysOrganization findParentNodeById(SysOrganization org, String orgId) {
		SysOrganization sysOrg = null;
		SysOrganization parentOrg = org.getParentOrg();
		if (parentOrg != null) {
			if (parentOrg.getId().equals(orgId)) {
				sysOrg = org;
			} else {
				sysOrg = findParentNodeById(parentOrg, orgId);
			}
		}
		return sysOrg;
	}

	/**
	 * 组装格式向前台呈现TREE型结构
	 * 
	 * @author 金鹏祥
	 * @param obj 实体
	 * @param canChecked 是否有多选框
	 * @param isLeaf	是否已经最终叶子节点
	 * @param expanded 是否展开
	 * @return Map<String, Object>
	*/
	public Map<String, Object> objectPackage(Object obj, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
		Map<String, Object> item = new HashMap<String, Object>();

		if (obj instanceof SysOrganization) {
			SysOrganization sysOrganization = (SysOrganization) obj;
			item.put("id", sysOrganization.getId());
			item.put("dbid", sysOrganization.getId());
			item.put("text", sysOrganization.getOrgname());
			item.put("code", sysOrganization.getOrgcode());
			item.put("type", "orgCategory");
			if (isLeaf) {
				item.put("leaf", sysOrganization.getIsLeaf());
			}
		} 
		if (!isLeaf) {
			item.put("leaf", false);
		}else{
			item.put("leaf", true);
		}
		if (canChecked) {
			item.put("checked", false);
		}
		if (expanded) {
			item.put("expanded", true);
		}
		return item;
	}
	
	/**
     * <pre>
     * 将维度对象封装为树的node节点
     * </pre>
     * 
     * @author 陈晓哲
     * @param category
     * @param canChecked
     * @param isLeaf
     * @param expanded
     * @return
     * @since  fhd　Ver 1.1
    */
    protected Map<String, Object> wrapCategoryNode(Category category, Boolean canChecked, Boolean isLeaf,
            Boolean expanded)
    {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", category.getId());
        item.put("dbid", category.getId());
        item.put("code", category.getCode());
        item.put("text", category.getName());
        item.put("type", "kpi_category");
        item.put("iconCls", "icon-ibm-tree-scorecard");
        
        if (category.getParent() != null)
            item.put("parentid", category.getParent().getId());
        if (isLeaf)
        {
            item.put("leaf", category.getIsLeaf());
        }
        if (!isLeaf)
        {
            item.put("leaf", false);
        }
        if (canChecked)
        {
            item.put("checked", false);
        }
        if (expanded)
        {
            item.put("expanded", true);
        }
        return item;
    }
}