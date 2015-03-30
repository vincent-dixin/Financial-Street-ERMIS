package com.fhd.icm.business.rule;

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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.rule.RuleDAO;
import com.fhd.icm.entity.rule.Rule;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-24		上午11:33:45
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class RuleTreeBO {

	@Autowired
	private RuleDAO o_ruleDAO;
	
	/**
	 * <pre>
	 *组装树的返回数据
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param rule
	 * @param canChecked 是否有多选框
	 * @param isLeaf 是否是叶子节点
	 * @param expanded 是否展开
	 * @return 组装好的Map类型Tree数据
	 * @since  fhd　Ver 1.1
	*/
	protected Map<String, Object> wrapRULENode(Rule rule, Boolean canChecked, Boolean isLeaf, Boolean expanded){
		Map<String, Object> item = new HashMap<String, Object>();
		if (null != rule){
			item.put("dbid", rule.getId());
			item.put("code", rule.getCode());
			item.put("text", rule.getName());
			item.put("type", "rule");
			//添加指标图标
			if (1 != rule.getLevel()){
				if (rule.getIsLeaf()){
					item.put("iconCls", "icon-scorecard");
				}else{
					item.put("iconCls", "icon-scorecards");
				}
			}
			if (isLeaf){
				item.put("leaf", rule.getIsLeaf());
			}
			if (!isLeaf){
				item.put("leaf", false);
			}
			if (canChecked){
				item.put("checked", false);
			}
			if (expanded){
				item.put("expanded", true);
			}
		}
		return item;
	}
	
	/**
	 * <pre>
	 *读取制度树
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param id 节点Id
	 * @param canChecked 是否可选 
	 * @param query 查询条件
	 * @param type 数据类型
	 * @return 制度树数组
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String, Object>> findRuleTree(String id, Boolean canChecked, String query, String type){
		String ruleID = null;
		Map<String, Object> node = null; //node节点临时对象
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//返回前台的nodelist
		boolean expanded = StringUtils.isNotBlank(query) ? true : false;// 是否展开节点
		String companyId = UserContext.getUser().getCompanyid();
		if (StringUtils.isNotBlank(id)){
			Criteria criteria = o_ruleDAO.createCriteria();
			if(!companyId.equals(id)){
				criteria.add(Restrictions.eq("parent.id", id));
			}else{
				criteria.add(Restrictions.isNull("parent"));
			}
			criteria.add(Restrictions.eq("company.id", companyId));
			Set<String> idSet = this.findRuleBySearchName(query, type, true, true);
			criteria.setCacheable(true);
			List<Rule> ruleList = criteria.list();
			for (Rule entity : ruleList){
				ruleID = entity.getId();
				node = new HashMap<String, Object>();
				if (idSet.size() > 0 && idSet.contains(ruleID)){
					node = this.wrapRULENode(entity, canChecked, true, expanded);
					node.put("id", ruleID);
					nodes.add(node);
				}
			}
		}
		return nodes;
	}
	
	/**
	 * <pre>
	 *模糊匹配制度名称
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param searchName 查询名称
	 * @param type 树类型
	 * @param idSeq 主键全路径
	 * @param deleteStatus 删除状态
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	protected Set<String> findRuleBySearchName(String searchName, String type, Boolean idSeq, Boolean deleteStatus){
		List<Rule> list = new ArrayList<Rule>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_ruleDAO.createCriteria();
		if (StringUtils.isNotBlank(searchName)){
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
		criteria.setCacheable(true);
	        
		list = criteria.list();
		if (idSeq){
			for (Rule entity : list){
				if (entity.getIdSeq() != null){
					String[] idsTemp = entity.getIdSeq().split("\\.");
					idSet.addAll(Arrays.asList(idsTemp));
				}
			}
		}else{
			for (Rule entity : list){
				idSet.add(entity.getId());
			}
		}
		return idSet;
	}
}