package com.fhd.comm.business.theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.theme.AnalysisDAO;
import com.fhd.comm.entity.theme.Analysis;

/**
 * 主题分析树BO
 * @author 王再冉
 *
 */
@Service
public class AnalysisTreeBO {
	@Autowired
	private AnalysisDAO o_analysisDAO;

	/**
	 * 查找树节点
	 * @param id
	 * @param query
	 * @return
	 */
	public List<Map<String, Object>> treeLoader(String companyId,String query) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();		
		
		if(StringUtils.isNotBlank(companyId)){
			List<Analysis> analyList = findAnalysByCompanyId(companyId);
			if(null != analyList){
				if(StringUtils.isNotBlank(query)){
					analyList = findAnalysisByanalyName(companyId,query);
				}
				for(Analysis analy : analyList){
					if(!analy.getDeleteStatus().equalsIgnoreCase("0")){//状态为0的不显示
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", analy.getId());
						item.put("text", analy.getAnalyname());
						item.put("leaf", true);//是否是叶子节点
						item.put("expanded", false);
						nodes.add(item);
					}
				}
			}
		}
		return nodes;
	}
	/**
	 * 根据companyId查找主题实体
	 * @param companyId	公司id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Analysis> findAnalysByCompanyId(String companyId) {
		Criteria criteria = o_analysisDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("company.id", companyId));
		return criteria.list();
    }
	/**
	 * 更新Analysis实体
	 * @param analy
	 */
	@Transactional
	public void mergeAnalysis(Analysis analy) {
		o_analysisDAO.merge(analy);
	}
	/**
	 * 保存Analysis实体
	 * @param analy
	 */
	@Transactional
	public void saveAnalysis(Analysis analy) {
		o_analysisDAO.save(analy);
	}
	/**
	 * 模糊查询主题分析名称
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Analysis> findAnalysisByanalyName(String companyId,String query){
		Criteria criteria = o_analysisDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.like("analyname", query, MatchMode.ANYWHERE));
		List<Analysis> list = criteria.list();
		if (list.size() > 0) {
			return list;
		}else{
			return null;
		}
	}
	
}
