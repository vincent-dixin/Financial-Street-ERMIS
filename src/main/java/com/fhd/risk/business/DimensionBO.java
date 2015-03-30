package com.fhd.risk.business;

import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.risk.dao.DimensionDAO;
import com.fhd.risk.dao.ScoreDAO;
import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Score;
import com.fhd.risk.interfaces.IDimensionBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 维度及维度分值业务类：增删改查维度及维度分值
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午3:49:10
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class DimensionBO implements IDimensionBO{

	@Autowired
	private DimensionDAO o_dimensionDAO;
	
	@Autowired
	private ScoreDAO o_scoreDAO;

	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#saveDimensionById(java.lang.String)
	 */
	@Override
	@Transactional
	public void saveDimensionById(String dimensionId){
		List<Dimension> dimensionList = this.findDimensionBySome(null,null,null);
		Dimension dimensionSource = null;
		int sort = 0;
		for (Dimension dimension : dimensionList) {
			if(dimensionId.equals(dimension.getId())){
				dimensionSource = dimension;//获得ID为dimensionId的对象
			}
			if(dimension.getSort() != null && sort<=dimension.getSort()){
				sort = dimension.getSort();//获得排序的最大值
			}
		}
		Dimension dimensionTarget = new Dimension(Identities.uuid());
		dimensionTarget.setCompany(dimensionSource.getCompany());
		dimensionTarget.setCode(dimensionSource.getCode());
		dimensionTarget.setDeleteStatus(dimensionSource.getDeleteStatus());
		dimensionTarget.setDesc(dimensionSource.getDesc());
		dimensionTarget.setName(dimensionSource.getName());
		dimensionTarget.setSort(++sort);
		o_dimensionDAO.merge(dimensionTarget);
		List<Score> scoreList = this.findScoreBySome(dimensionId, null);
		for (Score score : scoreList) {//拷贝其关联的分值数据
			Score scoreTarget = new Score(Identities.uuid());
			scoreTarget.setDesc(score.getDesc());
			scoreTarget.setDimension(dimensionTarget);
			scoreTarget.setName(score.getName());
			scoreTarget.setSort(score.getSort());
			scoreTarget.setValue(score.getValue());
			o_scoreDAO.merge(scoreTarget);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#mergeDimensionBatch(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void mergeDimensionBatch(String jsonString,String companyId){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String code = jsonObject.getString("code");
			String name = jsonObject.getString("name");
			String desc = jsonObject.getString("desc");
			String deleteStatus = jsonObject.getString("deleteStatus");
			Integer sort = jsonObject.getInt("sort");
			Dimension dimension = null;
			if(StringUtils.isNotBlank(id)){
				dimension = o_dimensionDAO.get(id);
			}else{
				dimension = new Dimension(Identities.uuid());
			}
			dimension.setName(name);
			dimension.setCode(code);
			dimension.setSort(sort);
			dimension.setDesc(desc);
			dimension.setDeleteStatus(deleteStatus);
			dimension.setCompany(new SysOrganization(companyId));
			o_dimensionDAO.merge(dimension);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#mergeScoreBatch(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void mergeScoreBatch(String jsonString,String dimensionId){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String name = jsonObject.getString("name");
			String desc = jsonObject.getString("desc");
			Double value = jsonObject.getDouble("value");
			Integer sort = jsonObject.getInt("sort");
			Score score = null;
			if(StringUtils.isNotBlank(id)){
				score = o_scoreDAO.get(id);
			}else{
				score = new Score(Identities.uuid());
			}
			score.setName(name);
			score.setSort(sort);
			score.setDesc(desc);
			score.setValue(value);
			score.setDimension(new Dimension(dimensionId));
			o_scoreDAO.merge(score);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#removeDimensionById(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeDimensionById(String id){
		this.removeScoreByDimensionIdBatch(id);//删除该维度的维度分值的数据
		o_dimensionDAO.delete(id);//删除该维度的数据
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#removeScoreById(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeScoreById(String id){
		o_scoreDAO.delete(id);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#removeScoreByDimensionIdBatch(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeScoreByDimensionIdBatch(String dimensionId){
		o_scoreDAO.createQuery("delete Score score where score.dimension.id=:dimensionId")
		.setString("dimensionId", dimensionId)
		.executeUpdate();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#findDimensionBySome(java.lang.String, java.lang.String, java.util.Set)
	 */
	@Override
	public List<Dimension> findDimensionBySome(String query, String deleteStatus, Set<String> ingnorDimensionIdSet){
		Criteria criteria = o_dimensionDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("name").like(query, MatchMode.ANYWHERE), Property.forName("code").like(query, MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(deleteStatus)){
			criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
		}
		if(null !=ingnorDimensionIdSet && ingnorDimensionIdSet.size()>0){
			criteria.add(Restrictions.not(Restrictions.in("id", ingnorDimensionIdSet)));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#findDimensionById(java.lang.String)
	 */
	@Override
	public Dimension findDimensionById(String dimensionId){
		return o_dimensionDAO.get(dimensionId);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.IDimensionBO#findScoreBySome(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Score> findScoreBySome(String dimensionId,String query){
		Criteria criteria = o_scoreDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("name").like(query, MatchMode.ANYWHERE), Property.forName("desc").like(query, MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(dimensionId)){
			criteria.add(Restrictions.eq("dimension.id", dimensionId));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
}

