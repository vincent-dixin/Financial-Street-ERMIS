package com.fhd.process.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fhd.fdc.utils.UserContext;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhd.process.entity.Process;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.interfaces.IProcessTreeBO;


/**
 * 查询流程树
 * @author   李克东
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-25		下午2:52:33
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ProcessTreeBO implements IProcessTreeBO {
	
	@Autowired
	private ProcessDAO o_processDAO;
	
	@Autowired
	private ProcessBO o_processBO;

	/** (non-Javadoc)
	 * @see com.fhd.process.interfaces.IProcessTreeBO#processTreeLoader(java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> processTreeLoader(String id,
			Boolean canChecked, String query, String type) {
		Map<String, Object> node = null; // node节点临时对象
		 String companyId = UserContext.getUser().getCompanyid();// 所在公司id
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist
		boolean expanded = StringUtils.isNotBlank(query) ? true : false;// 是否展开节点
		if (StringUtils.isNotBlank(id)) {
			Criteria criteria = o_processDAO.createCriteria();
			if("root".equals(id)){
				criteria.add(Restrictions.isNull("parent.id"));
			}
			else{
				criteria.add(Restrictions.eq("parent.id", id));	
			}
			
			criteria.add(Restrictions.eq("company.id", companyId));
			criteria.setCacheable(true);
			List<Process> processList = criteria.list();
			for (Process entity : processList) {
				node = new HashMap<String, Object>();
				if(expanded){
					List<Process> resultList= new ArrayList<Process>();
					if(entity.getIsLeaf()){
						if(entity.getName().contains(query)){
							
								if(entity.getIsLeaf()){
									node = setNodesAttributes(entity, expanded,
											canChecked);
									nodes.add(node);
								}
						}
					}else{
						int length = o_processBO.findResultByQuery(entity.getId(), query, resultList).size();
						if(length>0){
						
							node = setNodesAttributes(entity, expanded,
									canChecked);
							nodes.add(node);
						}
					}
				}else{
					
					node = setNodesAttributes(entity, expanded, canChecked);
					nodes.add(node);
				}
				
				
			}
		}
		return nodes;
	}
	/**
	 * 给节点属性设置值
	 * 
	 * @param entity
	 * @param expanded
	 * @param canChecked
	 * @return
	 */
	private Map<String, Object> setNodesAttributes(Process entity,
			boolean expanded, boolean canChecked) {

		Map<String, Object> node = new HashMap<String, Object>();
		if (canChecked) {			
				
					node.put("checked", false);				
			
		}
		node.put("id", entity.getId());
		node.put("dbid", entity.getId());
		node.put("text", entity.getName());
		node.put("leaf", entity.getIsLeaf());
		node.put("expanded", expanded);
		return node;

	}
	
}
