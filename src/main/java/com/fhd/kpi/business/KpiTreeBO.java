package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.dao.KpiDAO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.kpi.entity.SmRelaKpi;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 
 * ClassName:KpiTreeBO:指标树BO
 * 
 * @author 张帅
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-9-17 上午13:11:00
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class KpiTreeBO {

    @Autowired
    private KpiDAO o_kpiDAO;

    @Autowired
    private StrategyMapTreeBO o_strategyMapTreeBO;

    @Autowired
    private KpiBO o_kpiBO;
    @Autowired
    private OrganizationBO o_organizationBO;


    
    /**
     * 
     * 根据当前登陆人所在公司id查询指标根目录id、目标根目录id、机构根目录id
     * 
     * @author 张帅
     * @since fhd　Ver 1.1
     */

    public List<Map<String, Object>> findRootByCompanyId()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>(); // 存放个对象
        Map<String, Object> items = new HashMap<String, Object>(); // 存放
        String companyId = UserContext.getUser().getCompanyid();// 所在公司id
        //指标和目标不需要根节点
        SysOrganization sysOrganization = o_organizationBO.getRootOrgByCompanyId(companyId);// 获取机构根目录
        if (null != sysOrganization)
        {
            item = this.o_strategyMapTreeBO.wrapOrgNode(sysOrganization, false, false, false);
            items.put("org", item);
        }
        list.add(items);
        return list;
    }
    /**
     * <pre>
     * this.queryKpiBySearchName:模糊匹配指标名称
     * </pre>
     * 
     * @author 张 帅
     * @param searchName
     *            查询条件
     * @param type
     *            根据当前员工emp、机构org过滤
     * @param idseq
     *            是否是idseq
     * @param deleteStatus
     *            删除状态
     * @return
     * @since fhd　Ver 1.1
     */
    protected Set<String> findKpiBySearchName(String searchName, String type, Boolean idseq, Boolean deleteStatus) {
        List<Kpi> list = new ArrayList<Kpi>();
        Set<String> idSet = new HashSet<String>();
        Criteria criteria = o_kpiDAO.createCriteria().add(Restrictions.eq("isKpiCategory", "KPI"));
        if (StringUtils.isNotBlank(searchName)) {
            criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
        }

        String companyid = null;
        String empId = null;
        String orgId = null;

        if (UserContext.getUser() != null) {
            companyid = UserContext.getUser().getCompanyid();
        }
        criteria.add(Restrictions.eq("company.id", companyid));
        if (StringUtils.isNotBlank(type)) {

            empId = UserContext.getUser().getEmpid();
            orgId = UserContext.getUser().getMajorDeptId();

            criteria.createAlias("kpiRelaOrgEmps", "kroe");
            if ("emp".equals(type)) {
                criteria.add(Restrictions.eq("kroe.emp.id", empId));
            }
            else if ("org".equals(type)) {
                criteria.add(Restrictions.eq("kroe.org.id", orgId));
            }
        }
        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
        //criteria.setCacheable(true);
        //暂时不加载无用的关联对象//add by chenxiaozhe
        o_kpiBO.removeKpiRelaObject(criteria);
        list = criteria.list();
        if (idseq) {
            for (Kpi entity : list) {
                if (entity.getIdSeq() != null) {
                    String[] idsTemp = entity.getIdSeq().split("\\.");
                    idSet.addAll(Arrays.asList(idsTemp));
                }
            }
        }
        else {
            for (Kpi entity : list) {
                idSet.add(entity.getId());
            }
        }
        return idSet;
    }

    /**
     * <pre>
     * 查找org的父机构,条件为org的父机构的ID要和当前传入的机构ID相同
     * </pre>
     * 
     * @author 陈晓哲
     * @param org
     *            机构实体
     * @param orgId
     *            机构ID-当点击节点时传入的机构ID
     * @return
     * @since fhd　Ver 1.1
     */
    protected SysOrganization findParentNodeById(SysOrganization org, String orgId) {
        SysOrganization sysOrg = null;
        SysOrganization parentOrg = org.getParentOrg();
        if (parentOrg != null) {
            if (parentOrg.getId().equals(orgId)) {
                sysOrg = org;
            }
            else {
                sysOrg = findParentNodeById(parentOrg, orgId);
            }
        }
        return sysOrg;
    }

    /**
     * <pre>
     * 查找目标实体的父目标,条件为目标实体的父目标的ID要和当前传入的目标ID相同
     * </pre>
     * 
     * @author 陈晓哲
     * @param sm
     *            目标实体
     * @param smId
     *            目标实体ID
     * @return
     * @since fhd　Ver 1.1
     */
    private StrategyMap findParentNodeById(StrategyMap sm, String smId) {
        StrategyMap smap = null;
        StrategyMap parentSm = sm.getParent();
        if (parentSm != null) {
            if (parentSm.getId().equals(smId)) {
                smap = sm;
            }
            else {
                smap = findParentNodeById(parentSm, smId);
            }
        }
        return smap;
    }

    /**
     * 
     * 目标指标查询
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param id
     *            节点id
     * @param query
     *            查询条件
     * @param type
     *            根据当前员工emp、机构org过滤
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> strategyMapTreeLoader(String id, Boolean canChecked, String query, String type, String smIconType) {

        Map<String, Object> node = null;// node临时对象
        StrategyMap strategyMap = null;// 目标实体临时对象
        Set<SmRelaKpi> smRelaKpiSet = null;// 目标关联KPI指标临时集合
        Set<String> smIdSet = new HashSet<String>(); // 保存指标的所属目标ID
        Set<String> kpiIdSet = new HashSet<String>();
        List<StrategyMap> smList = new ArrayList<StrategyMap>();// 子节点目标集合
        List<Kpi> kpiTmpList = new ArrayList<Kpi>();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist

        String companyId = UserContext.getUser().getCompanyid();// 所在公司id
        boolean expanded = StringUtils.isNotBlank(query) ? true : false; // 是否展开节点
        // 取出所有KPI对象,条件为
        Criteria criteria = o_kpiDAO.createCriteria().add(Restrictions.eq("company.id", companyId)).add(Restrictions.eq("deleteStatus", true));
        // 暂时不加载无用的对象.//add by chenxiaozhe
        o_kpiBO.removeKpiRelaObject(criteria);
        criteria.createAlias("dmRelaKpis", "dmRelaKpis").setFetchMode("dmRelaKpis.edirection", FetchMode.SELECT);

        if (StringUtils.isNotBlank(query)) {
            criteria.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
        }
        // criteria.setCacheable(true);
        List<Kpi> kpiList = criteria.list();// 取出符合条件的KPI

        for (Kpi kpi : kpiList) {// 循环每一个KPI
            node = new HashMap<String, Object>();
            smRelaKpiSet = kpi.getDmRelaKpis();
            for (SmRelaKpi sm : smRelaKpiSet) {
                strategyMap = sm.getStrategyMap();
                if (strategyMap != null) {
                    if (StringUtils.equals(strategyMap.getId(), id)) {
                        kpiTmpList.add(kpi);
                    }
                    else {
                        // 如果传入的id为sm_root假根,那么不需要查询父亲节点
                        if (!"sm_root".equals(id)) {
                            strategyMap = findParentNodeById(strategyMap, id);
                        }
                        if (strategyMap != null) {
                            smList.add(strategyMap);
                        }
                    }
                }
            }

        }

        // 将目标节点集合包装为node节点
        for (StrategyMap smap : smList) {
            if (!smIdSet.contains(smap.getId())) {
                canChecked = false;// 目标不可以选择
                smIdSet.add(smap.getId());
                node = this.o_strategyMapTreeBO.wrapStrategyMapNode(smap, canChecked, false, expanded);
                if ("display".equals(smIconType)) {
                    node.put("iconCls", "icon-ibm-icon-strategy-element");
                }
                node.put("leaf", false);
                nodes.add(node);
            }
        }
        /*
         * 将指标节点集合包装为node节点
         */
        for (Kpi kpi : kpiTmpList) {
            if (!kpiIdSet.contains(kpi.getId())) {
                kpiIdSet.add(kpi.getId());
                node = this.wrapKPINode(kpi, canChecked, true, false);
                node.put("id", id + "_" + kpi.getId());
                node.put("leaf", true);
                nodes.add(node);
            }
        }

        return nodes;
    }

    /**
     * 
     * 机构指标查询
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param id
     *            节点id
     * @param query
     *            查询条件
     * @param type
     *            根据当前员工emp、机构org过滤
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> orgTreeLoader(String id, Boolean canChecked, String query, String type) {
        SysOrganization org = null;// 机构实体临时对象
        Map<String, Object> node = null;// node临时对象
        Set<KpiRelaOrgEmp> kpiRelaOrgEmpSet = null;// 机构和KPI关联对象临时集合
        Set<String> orgIdSet = new HashSet<String>(); // 保存指标的所属机构ID
        List<SysOrganization> orgList = new ArrayList<SysOrganization>();// 子节点机构集合
        List<Map<String, Object>> nodelist = new ArrayList<Map<String, Object>>();// 返回前台的nodelist

        String companyId = UserContext.getUser().getCompanyid();// 当前登录用户所在公司ID

        boolean expanded = StringUtils.isNotBlank(query) ? true : false; // 是否展开节点

        // 取出所有的KPI对象,查询条件为
        Criteria criteria = o_kpiDAO.createCriteria().add(Restrictions.eq("company.id", companyId)).add(Restrictions.eq("deleteStatus", true))
                .createAlias("kpiRelaOrgEmps", "kroe").add(Restrictions.eq("kroe.type", "B"));
        //暂时不加载无用的对象.//add by chenxiaozhe
        o_kpiBO.removeKpiRelaObject(criteria);

        if (StringUtils.isNotBlank(query)) {
            criteria.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
        }
        criteria.setCacheable(true);
        List<Kpi> kpiList = criteria.list();// 取出符合条件的KPI

        for (Kpi kpi : kpiList) {// 循环每一个KPI

            node = new HashMap<String, Object>();
            kpiRelaOrgEmpSet = kpi.getKpiRelaOrgEmps();
            for (KpiRelaOrgEmp relaOrgEmp : kpiRelaOrgEmpSet) {
                org = relaOrgEmp.getOrg();// 取出KPI关联的org
                if (org != null) {
                    if (org.getId().equals(id)) {// 如果取出的org的ID为当前传入的节点ID,则将这个KPI加入nodelist中.
                        node = this.wrapKPINode(kpi, canChecked, true, false);
                        node.put("id", id + "_" + kpi.getId());
                        node.put("leaf", true);
                        nodelist.add(node);
                    }
                    else {
                        // 递归查询当前传入节点的下级子节点
                        org = findParentNodeById(org, id);
                        if (org != null) {
                            orgList.add(org);
                        }
                    }
                }
            }
        }
        // 将机构节点集合包装为node节点
        for (SysOrganization sysOrg : orgList) {
            if (!orgIdSet.contains(sysOrg.getId())) {
                canChecked = false;// 机构不可选
                orgIdSet.add(sysOrg.getId());
                node = this.o_strategyMapTreeBO.wrapOrgNode(sysOrg, canChecked, false, expanded);
                node.put("leaf", false);
                nodelist.add(node);
            }
        }
        return nodelist;
    }

    /**
     * 
     * 我的指标、指标树查询
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param id
     *            节点id
     * @param query
     *            查询条件
     * @param type
     *            根据当前员工emp、机构org过滤
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> kpiTreeLoader(String id, Boolean canChecked, String query, String type) {
        String kpiID = null;
        Map<String, Object> node = null; // node节点临时对象
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist
        boolean expanded = StringUtils.isNotBlank(query) ? true : false;// 是否展开节点

        if (StringUtils.isNotBlank(id)) {
            Criteria criteria = o_kpiDAO.createCriteria();
            // 修改指标树根,指标树没有根节点
            if ("kpi_root".equals(id)) {
                criteria.add(Restrictions.isNull("parent"));
            }
            else {
                criteria.add(Restrictions.eq("parent.id", id));
            }
            criteria.add(Restrictions.eq("deleteStatus", true)).add(Restrictions.ne("isKpiCategory", "KC"));
            //暂时不加载无用的关联对象//add by chenxiaozhe
            o_kpiBO.removeKpiRelaObject(criteria);
            Set<String> idSet = this.findKpiBySearchName(query, type, true, true);
            criteria.setCacheable(true);
            List<Kpi> kpiList = criteria.list();
            for (Kpi entity : kpiList) {
                kpiID = entity.getId();
                node = new HashMap<String, Object>();
                if (idSet.size() > 0 && idSet.contains(kpiID)) {
                    node = this.wrapKPINode(entity, canChecked, true, expanded);
                    node.put("id", kpiID);
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }

    /**
     * <pre>
     * 指标类型树查询
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @param query
     * @return
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> kpiTypeTreeLoader(String id, String query, Boolean canChecked) {
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist
        Criteria criteria = o_kpiDAO.createCriteria().add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
        criteria.add(Restrictions.eq("deleteStatus", true)).add(Restrictions.eq("isKpiCategory", "KC"));
        criteria.addOrder(Order.asc("sort"));
        criteria.addOrder(Order.asc("name"));
        // 不加载无关的对象
        o_kpiBO.removeKpiRelaObject(criteria);
        if (StringUtils.isNotBlank(query)) {
            criteria.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
        }
        criteria.setCacheable(true);
        List<Kpi> list = criteria.list();
        for (Kpi kpi : list) {
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("id", kpi.getId());
            node.put("text", kpi.getName());
            node.put("dbid", kpi.getId());
            node.put("leaf", true);
            node.put("code", kpi.getCode());
            node.put("type", "kpi_type");
            node.put("iconCls", "icon-ibm-icon-metrictypes");
            if (null != canChecked && canChecked) {
                node.put("checked", false);
            }
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * <pre>
     * 封装KPI指标对象到Map中,给树提供数据
     * </pre>
     * 
     * @author 陈晓哲
     * @param obj
     *            目标对象
     * @param canChecked
     *            是否带复选框
     * @param isLeaf
     *            是否带有叶子节点
     * @param expanded
     *            是否展开
     * @return
     * @since fhd　Ver 1.1
     */
    protected Map<String, Object> wrapKPINode(Kpi kpi, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
        Map<String, Object> item = new HashMap<String, Object>();
        if (kpi != null) {
            item.put("dbid", kpi.getId());
            item.put("code", kpi.getCode());
            item.put("text", kpi.getName());
            item.put("type", "kpi");
            // 添加指标图标
            if (kpi != null) {
                if (null!=kpi.getIsLeaf()&&kpi.getIsLeaf()) {
                    item.put("iconCls", "icon-scorecard");
                }
                else {
                    item.put("iconCls", "icon-scorecards");
                }
            }
            if (isLeaf) {
                item.put("leaf", kpi.getIsLeaf());
            }
            if (!isLeaf) {
                item.put("leaf", false);
            }
            if (null != canChecked && canChecked) {
                item.put("checked", false);
            }
            if (expanded) {
                item.put("expanded", true);
            }
        }

        return item;
    }

}
