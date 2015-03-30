package com.fhd.assess.business.quaAssess;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.assess.dao.quaAssess.ScoreResultDAO;
import com.fhd.assess.entity.kpiSet.RangObjectDeptEmp;
import com.fhd.assess.entity.quaAssess.ScoreResult;
import com.fhd.assess.interfaces.quaAssess.IQuaAssessBO;
import com.fhd.core.utils.Identities;
import com.fhd.risk.dao.ScoreDAO;
import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Score;

@Service
public class SaveAssessBO {

	@Autowired
	private ScoreResultDAO o_scoreResultDAO;
	
	@Autowired
	private ScoreDAO o_scoreDAO;
	
	@Autowired
	private IQuaAssessBO o_quaAssessBO;
	
	/**
	 * 通过维度ID、维度分值查询维度关联分值ID
	 * @author 金鹏祥
	 * @return String
	 * */
	@SuppressWarnings("unchecked")
	private String findScoreBydimensionIdAndValue(String dimId, double value){
		Criteria criteria = o_scoreDAO.createCriteria();
		criteria.add(Restrictions.eq("dimension.id", dimId));
		criteria.add(Restrictions.eq("value", value));
		List<Score> list = null;
		list = criteria.list();
		
		return list.get(0).getId();
	}
	
	/**
	 * 通过维度ID、维度分值查询维度关联分值ID
	 * @author 金鹏祥
	 * @return String
	 * */
	@SuppressWarnings("unchecked")
	private List<ScoreResult> findScoreBydimensionIdAndDicIdAndRangObjectDeptEmpId(String scoreId, String rangObjectDeptEmpId){
		Criteria criteria = o_scoreResultDAO.createCriteria();
		criteria.add(Restrictions.eq("dimension.id", scoreId));
		criteria.add(Restrictions.eq("rangObjectDeptEmpId.id", rangObjectDeptEmpId));
		List<ScoreResult> list = null;
		list = criteria.list();
		
		return list;
	}
	
	/**
	 * 评估保存操作
	 * @param params 前台JSON
	 * @author 金鹏祥
	 * */
	@Transactional
	public boolean assessSaveOper(String params) {
		JSONArray jsonarr = JSONArray.fromObject(params);
		try {
			for (Object objects : jsonarr) {
				JSONObject jsobjs = (JSONObject) objects;
				
				String dimId = jsobjs.get("dimId").toString();
				String rangObjectDeptEmpId = jsobjs.get("rangObjectDeptEmpId").toString();
				double dimValue = Double.parseDouble(jsobjs.get("dimValue").toString());
				RangObjectDeptEmp rangObjectDeptEmp = new RangObjectDeptEmp();
				Dimension dimension = new Dimension();
				Score score = new Score();
				ScoreResult scoreResult = new ScoreResult();
				String scoreId = this.findScoreBydimensionIdAndValue(dimId, 
						dimValue);
				List<ScoreResult> scoreResultList = this.findScoreBydimensionIdAndDicIdAndRangObjectDeptEmpId(dimId, rangObjectDeptEmpId);
				
				if(scoreResultList.size() != 0){
					//修改打分结果
					scoreResult.setId(scoreResultList.get(0).getId());
				}else{
					//添加打分结果
					scoreResult.setId(Identities.uuid());
				}
				
				rangObjectDeptEmp.setId(rangObjectDeptEmpId);
				dimension.setId(dimId);
				score.setId(scoreId);
				
				scoreResult.setDimension(dimension);
				scoreResult.setScore(score);
				scoreResult.setRangObjectDeptEmpId(rangObjectDeptEmp);
				scoreResult.setSubmitTime(new Date());
				scoreResult.setApproval(false);
				scoreResult.setStatus(null);
				
				o_quaAssessBO.mergeScoreResult(scoreResult);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}