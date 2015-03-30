package com.fhd.kpi.business;

import static org.hibernate.FetchMode.JOIN;
import static org.hibernate.FetchMode.SELECT;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.ne;
import static org.hibernate.criterion.Restrictions.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.dao.SmRelaOrgEmpDAO;
import com.fhd.kpi.dao.StrategyMapDAO;
import com.fhd.kpi.entity.SmRelaOrgEmp;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 
 * ClassName:KpiStrategyMapTreeBO:战略目标树BO
 * 
 * @author 杨鹏
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-8-29 上午10:11:00
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class StrategyMapTreeBO {
    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private StrategyMapBO o_kpiStrategyMapBO;

    @Autowired
    private KpiTreeBO o_kpiTreeBO;

    @Autowired
    private StrategyMapDAO o_strategyMapDAO;

    @Autowired
    private OrganizationBO o_organizationBO;

    @Autowired
    private SmRelaOrgEmpDAO o_smRelaOrgEmpDAO;

    /**
     * <pre>
     * 我的目标树加载
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标id
     * @param canChecked
     *            是否出现多选框
     * @param query
     *            查询条件
     * @param smIconType
     *            图标类型; display和manager
     * @return
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> mineTreeLoader(String id, Boolean canChecked, String query, String smIconType) {
        boolean isLeaf = true;
        Map<String, Object> item = null;
        boolean expanded = false;
        if (StringUtils.isNotBlank(query)) {
            expanded = true;
        }
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        String empId = UserContext.getUser().getEmpid();
        Criteria criteria = o_smRelaOrgEmpDAO
                .createCriteria()
                .add(Restrictions.eq("emp.id", empId))
                .createAlias("strategyMap", "sm")
                .setProjection(
                        Projections.projectionList().add(Property.forName("sm.id").as("id")).add(Property.forName("sm.code").as("code"))
                                .add(Property.forName("sm.name").as("name")))
                .setResultTransformer(new AliasToBeanResultTransformer(StrategyMap.class));
        if (StringUtils.isNotBlank(query)) {
            criteria.add(Property.forName("sm.name").like(query, MatchMode.ANYWHERE));
        }
        List<StrategyMap> list = criteria.list();
        Set<String> idSet = new HashSet<String>();
        if (list.size() > 0) {
            for (StrategyMap sm : list) {
                item = new HashMap<String, Object>();
                idSet.add(sm.getId());
                item.put("id", sm.getId());
                item.put("dbid", sm.getId());
                item.put("code", sm.getCode());
                item.put("text", sm.getName());
                item.put("type", "sm");
                item.put("leaf", isLeaf);
                if (canChecked) {
                    item.put("checked", false);
                }
                if (expanded) {
                    item.put("expanded", true);
                }
                nodes.add(item);
            }
            nodes = wrapIconNode(idSet, smIconType, nodes);
        }
        return nodes;
    }

    /**
     * 目标树、我的目标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @param type
     *            用于区别目标树和我的目标树
     * @return
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> treeLoader(String id, Boolean canChecked, String query, String type, String smIconType) {
        Set<String> idsSet = new HashSet<String>(); // 保存根据查询条件查出来的所有的目标id
        Map<String, Object> item = null;
        List<StrategyMap> smList = null; // 保存根据父节点查出的目标
        boolean expanded = false;
        if (StringUtils.isNotBlank(query)) {
            expanded = true;
        }
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(id)) {
            String companyid = UserContext.getUser().getCompanyid();
            smList = o_kpiStrategyMapBO.findBySome(null, companyid, id, false, null, true);
            idsSet = o_kpiStrategyMapBO.findStrategyMapBySearchName(query, type, true, true);
            List<String> smIdList = new ArrayList<String>();
            for (StrategyMap entity : smList) {
                if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {
                    smIdList.add(entity.getId());
                    item = this.wrapStrategyMapNode(entity, canChecked, true, expanded);
                    nodes.add(item);
                }
            }
            nodes = this.wrapIconNode(smIdList, smIconType, nodes);

        }
        Collections.sort(nodes, new Comparator<Map<String, Object>>() {

            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String s1 = (String) o1.get("iconCls");
                String s2 = (String) o2.get("iconCls");
                if ("icon-status-high".equals(s1)) {
                    s1 = "high";
                }
                if ("icon-status-high".equals(s2)) {
                    s2 = "high";
                }
                if ("icon-status-mid".equals(s1)) {
                    s1 = "imid";
                }
                if ("icon-status-mid".equals(s2)) {
                    s2 = "imid";
                }
                if ("icon-status-low".equals(s1)) {
                    s1 = "low";
                }
                if ("icon-status-low".equals(s2)) {
                    s2 = "low";
                }
                if ("icon-symbol-status-sm".equals(s1)) {
                    s1 = "up";
                }
                if ("icon-symbol-status-sm".equals(s2)) {
                    s2 = "up";
                }
                if ("icon-status-disable".equals(s1)) {
                    s1 = "nothing";
                }
                if ("icon-status-disable".equals(s2)) {
                    s2 = "nothing";
                }
                return (s1).compareTo(s2);
            }
        });
        return nodes;
    }

    /**
     * 机构目标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> orgTreeLoader(String id, String type, Boolean canChecked, String query, String smIcon) {
        Set<String> oidSet = new HashSet<String>();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist
        if (type.contains("other")) {
            type = type.split("_")[1];
        }
        if ("org".equals(type)) {
            String orgSmID = "";
            boolean isLeaf = true;
            Map<String, Object> node = null;// node临时对象
            SysOrganization org = null;// 机构实体临时对象
            Set<SmRelaOrgEmp> smRelaOrgEmpSet = null;// 目标和机构关联对象集合
            Set<String> orgIdSet = new HashSet<String>(); // 保存目标的所属机构ID
            String companyId = UserContext.getUser().getCompanyid();// 所在公司id
            boolean expanded = false;
            if (StringUtils.isNotBlank(query)) {
                expanded = true;
            }
            List<SysOrganization> orgList = new ArrayList<SysOrganization>();// 子节点机构集合
            // 取出所有的目标对象,查询条件为
            Criteria criteria = o_strategyMapDAO.createCriteria().setCacheable(true).add(eq("company.id", companyId)).add(eq("deleteStatus", true))
                    .createAlias("smRelaOrgEmps", "sroe").add(eq("sroe.type", "B"))
                    // 去掉无用的关联对象//add by chenxiaozhe
                    .setFetchMode("parent", SELECT).setFetchMode("status", SELECT).setFetchMode("createBy", SELECT)
                    .setFetchMode("lastModifyBy", SELECT).setFetchMode("relativeTo", SELECT).setFetchMode("company", SELECT);

            List<SmRelaOrgEmp> smRelaOrgEmps = this.o_smRelaOrgEmpDAO.createCriteria().setFetchMode("strategyMap", JOIN).setFetchMode("org", JOIN)
                    .setFetchMode("strategyMap.parent", SELECT).setFetchMode("strategyMap.status", SELECT)
                    .setFetchMode("strategyMap.createBy", SELECT).setFetchMode("strategyMap.lastModifyBy", SELECT)
                    .setFetchMode("strategyMap.relativeTo", SELECT).setFetchMode("strategyMap.company", SELECT).setCacheable(true).list();

            if (StringUtils.isNotBlank(query)) {
                criteria.add(like("name", query, MatchMode.ANYWHERE));
            }
            List<StrategyMap> smList = criteria.list();// 取出符合条件的目标
            Set<String> smIdSet = new HashSet<String>();
            for (StrategyMap sm : smList) {// 循环每一个目标
                smRelaOrgEmpSet = new TreeSet<SmRelaOrgEmp>();
                node = new HashMap<String, Object>();

                for (SmRelaOrgEmp smRelaOrgEmp : smRelaOrgEmps) {
                    if (smRelaOrgEmp.getStrategyMap().getId().equals(sm.getId())) {
                        smRelaOrgEmpSet.add(smRelaOrgEmp);
                    }
                }
                for (SmRelaOrgEmp smRelaOrgItem : smRelaOrgEmpSet) {
                    org = smRelaOrgItem.getOrg();
                    if (org != null) {
                        if (org.getId().equals(id)) {
                            node = new HashMap<String, Object>();
                            node.put("id", sm.getId());
                            node.put("dbid", sm.getId());
                            node.put("code", sm.getCode());
                            node.put("text", sm.getName());
                            node.put("type", "sm");
                            node.put("leaf", isLeaf);
                            if (canChecked) {
                                node.put("checked", false);
                            }
                            if (expanded) {
                                node.put("expanded", true);
                            }
                            orgSmID = id + "_" + sm.getId();
                            if (!smIdSet.contains(orgSmID)) {
                                smIdSet.add(orgSmID);
                                node.put("id", orgSmID);
                                nodes.add(node);
                            }
                            if (!oidSet.contains(sm.getId())) {
                                oidSet.add(sm.getId());
                                node.put("oid", sm.getId());
                            }
                        }
                        else {
                            org = o_kpiTreeBO.findParentNodeById(org, id);
                            if (org != null) {
                                orgList.add(org);
                            }
                        }
                    }
                }
                // 将机构节点集合包装为node节点
                for (SysOrganization sysOrg : orgList) {
                    if (!orgIdSet.contains(sysOrg.getId())) {
                        canChecked = false;// 机构不可以选择
                        orgIdSet.add(sysOrg.getId());
                        node = this.wrapOrgNode(sysOrg, canChecked, false, expanded);
                        node.put("leaf", false);
                        nodes.add(node);
                    }
                }
            }
            /* 加载其它目标节点 */
            if (StringUtils.isBlank(query)) {
                SysOrganization sysorg = this.o_organizationBO.get(id);
                if (sysorg != null && sysorg.getParentOrg() == null)// 说明是根组织单元
                {
                    nodes.add(this.wrapOtherNode(false));
                }
            }
        }
        else {
            if ("sm".equals(type) && StringUtils.isBlank(query)) {
                if (id.contains("other_")) {
                    id = id.split("_")[1];
                }
                List<StrategyMap> strategyList = this.findNotInOrgStrategyMap(id, query);
                for (StrategyMap sm : strategyList) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    oidSet.add(sm.getId());
                    item.put("id", sm.getId());
                    item.put("oid", sm.getId());
                    item.put("dbid", sm.getId());
                    item.put("code", sm.getCode());
                    item.put("text", sm.getName());
                    item.put("type", "sm");
                    if (sm.getParent() != null) {
                        item.put("parentid", sm.getParent().getId());
                    }
                    item.put("leaf", sm.getIsLeaf());
                    if (canChecked) {
                        item.put("checked", false);
                    }
                    nodes.add(item);
                }
            }
        }
        // 查询时,需要处理
        if (StringUtils.isNotBlank(query)) {
            /* 加载其它目标节点 */
            SysOrganization sysorg = null;
            if (!id.contains("other_") && "org".equals(type)) {
                sysorg = this.o_organizationBO.get(id);
            }
            if (sysorg != null && sysorg.getParentOrg() == null)// 说明是根组织单元
            {
                nodes.add(this.wrapOtherNode(true));
            }
            else {
                if (id.contains("other_")) {
                    id = id.split("_")[1];
                }
                List<StrategyMap> smList = o_kpiStrategyMapBO.findBySome(null, null, id, false, null, true);
                Set<String> idsSet = findNotNiOrgSmIdSet(id, query);
                for (StrategyMap entity : smList) {
                    if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {
                        Map<String, Object> item = this.wrapStrategyMapNode(entity, canChecked, true, true);
                        item.put("oid", entity.getId());
                        oidSet.add(entity.getId());
                        nodes.add(item);
                    }
                }
            }
        }
        if(!"org".equals(type)){
            nodes = this.wrapIconNode(oidSet, smIcon, nodes);
        }
        return nodes;
    }

    /**
     * <pre>
     * 查找没有和组织机构关联的Id集合
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    private Set<String> findNotNiOrgSmIdSet(String id, String query) {

        Set<String> otheridSet = new HashSet<String>();
        List<SmRelaOrgEmp> smRelaOrgList = this.o_kpiStrategyMapBO.findSmRelaOrgEmpAll();
        for (SmRelaOrgEmp smRelaOrgEmp : smRelaOrgList) {
            otheridSet.add(smRelaOrgEmp.getStrategyMap().getId());
        }
        Criteria criteria = this.o_strategyMapDAO.createCriteria().setCacheable(true)
                // 去掉无用的关联对象
                .setFetchMode("parent", SELECT).setFetchMode("status", SELECT).setFetchMode("createBy", SELECT).setFetchMode("lastModifyBy", SELECT)
                .setFetchMode("relativeTo", SELECT).setFetchMode("company", SELECT);
        criteria.add(not(in("id", otheridSet)));
        if (StringUtils.isNotBlank(query)) {
            criteria.add(like("name", query, MatchMode.ANYWHERE)).add(ne("deleteStatus", false));
        }
        List<StrategyMap> strategyList = criteria.list();
        Set<String> idSet = new HashSet<String>();
        for (StrategyMap entity : strategyList) {
            String[] idsTemp = entity.getIdSeq().split("\\.");
            idSet.addAll(Arrays.asList(idsTemp));
        }
        return idSet;
    }

    /**
     * <pre>
     * 查找没有和组织机构关联的目标对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    protected List<StrategyMap> findNotInOrgStrategyMap(String id, String query) {
        if (StringUtils.isNotBlank(query)) {
            StrategyMap rootsm = (StrategyMap) o_strategyMapDAO.createCriteria().add(Restrictions.isNull("parent")).uniqueResult();
            id = rootsm.getId();
        }
        Set<String> otheridSet = new HashSet<String>();
        List<SmRelaOrgEmp> smRelaOrgList = this.o_kpiStrategyMapBO.findSmRelaOrgEmpAll();
        for (SmRelaOrgEmp smRelaOrgEmp : smRelaOrgList) {
            otheridSet.add(smRelaOrgEmp.getStrategyMap().getId());
        }
        Criteria criteria = this.o_strategyMapDAO.createCriteria().setCacheable(true);
        /* 不加载无关对象 */
        criteria.setFetchMode("createBy", SELECT).setFetchMode("lastModifyBy", SELECT).setFetchMode("status", SELECT).setFetchMode("company", SELECT)
                .setFetchMode("relativeTo", SELECT).setFetchMode("parent.createBy", SELECT).setFetchMode("parent.lastModifyBy", SELECT)
                .setFetchMode("parent.status", SELECT).setFetchMode("parent.company", SELECT).setFetchMode("parent.relativeTo", SELECT);
        if (otheridSet.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
        }
        criteria.add(Restrictions.eq("parent.id", id));
        criteria.add(Restrictions.eq("deleteStatus", true));
        if (StringUtils.isNotBlank(query)) {
            criteria.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
        }
        List<StrategyMap> strategyList = criteria.list();
        return strategyList;
    }

    /**
     * <pre>
     * 封装目标对象到Map中,给树提供数据
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
    protected Map<String, Object> wrapStrategyMapNode(StrategyMap sm, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", sm.getId());
        item.put("dbid", sm.getId());
        item.put("code", sm.getCode());
        item.put("text", sm.getName());
        item.put("type", "sm");
        if (sm.getParent() != null)
            item.put("parentid", sm.getParent().getId());
        if (isLeaf) {
            item.put("leaf", sm.getIsLeaf());
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
        return item;
    }

    /**
     * <pre>
     * 封装机构对象到Map中,给树提供数据
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
    protected Map<String, Object> wrapOrgNode(SysOrganization sysOrganization, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", sysOrganization.getId());
        item.put("dbid", sysOrganization.getId());
        item.put("text", sysOrganization.getOrgname());
        item.put("code", sysOrganization.getOrgcode());
        item.put("type", "org");
        if (isLeaf) {
            item.put("leaf", sysOrganization.getIsLeaf());
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
     * <pre>
     * 加载其它目标节点
     * </pre>
     * 
     * @author 陈晓哲
     * @param expanded
     *            是否展开'其它'节点
     * @return
     * @since fhd　Ver 1.1
     */
    private Map<String, Object> wrapOtherNode(boolean expanded) {
        Map<String, Object> item = new HashMap<String, Object>();
        StrategyMap rootsm = (StrategyMap) o_strategyMapDAO.createCriteria().setCacheable(true).add(Restrictions.isNull("parent"))
                // 去掉无用的关联对象
                .setFetchMode("parent", SELECT).setFetchMode("status", SELECT).setFetchMode("createBy", SELECT).setFetchMode("lastModifyBy", SELECT)
                .setFetchMode("relativeTo", SELECT).setFetchMode("company", SELECT).uniqueResult();
        if (rootsm != null) {
            // 加入其它节点
            item = new HashMap<String, Object>();
            item.put("id", "other_" + rootsm.getId());
            item.put("dbid", rootsm.getId());
            item.put("code", rootsm.getCode());
            item.put("text", "其它");
            item.put("type", "other_sm");
            item.put("leaf", false);
            if (expanded) {
                item.put("expanded", true);
            }
        }
        return item;
    }

    /**
     * <pre>
     * 添加目标图标
     * </pre>
     * 
     * @author 陈晓哲
     * @param ids目标id集合
     * @param iconType
     *            图标类型 display:显示时使用 , manager:管理时使用
     * @param nodes
     *            node节点集合
     * @return
     * @since fhd　Ver 1.1
     */
    private List<Map<String, Object>> wrapIconNode(Collection<String> ids, String iconType, List<Map<String, Object>> nodes) {
        if ("display".equals(iconType)) {
            for (Map<String, Object> map : nodes) {
                if ("sm".equals(map.get("type"))) {
                    map.put("iconCls", "icon-ibm-icon-strategy-element");
                }
            }
        }
        else {
            if (null != ids && ids.size() > 0) {
                String smidstr = "";
                StringBuffer smBuf = new StringBuffer();
                for (String id : ids) {
                    smBuf.append("'").append(id).append("'").append(",");
                }
                if (smBuf.length() > 0) {
                    smidstr = smBuf.toString().substring(0, smBuf.length() - 1);
                }
                if (smidstr.length() > 0) {
                    HashMap<String, String> findAssessmentMaxEntTimeAllMap = o_relaAssessResultBO.findAssessmentMaxEntTimeAll("str", smidstr);
                    for (Map<String, Object> map : nodes) {
                        if ("sm".equals(map.get("type"))) {
                            String css = "icon-status-disable";
                            String smid = (String) map.get("id");
                            StrategyMap strategyMap = o_kpiStrategyMapBO.findById(smid);
                            if(null!=strategyMap.getStatus()){
                                String status = strategyMap.getStatus().getId();
                                if (!Contents.DICT_Y.equals(status)) {
                                    css = "icon-symbol-status-sm";
                                }
                                else {
                                    String cssdict = findAssessmentMaxEntTimeAllMap.get(map.get("id"));
                                    if ("0alarm_startus_h".equals(cssdict)) {
                                        css = "icon-status-high";
                                    }
                                    else if ("0alarm_startus_m".equals(cssdict)) {
                                        css = "icon-status-mid";
                                    }
                                    else if ("0alarm_startus_l".equals(cssdict)) {
                                        css = "icon-status-low";
                                    }
                                }
                            }
                            map.put("iconCls", css);
                        }
                    }
                }

            }

        }
        return nodes;
    }
}