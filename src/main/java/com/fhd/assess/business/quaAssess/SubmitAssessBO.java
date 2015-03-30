package com.fhd.assess.business.quaAssess;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.assess.dao.quaAssess.ScoreResultDAO;
import com.fhd.assess.entity.quaAssess.ScoreResult;
import com.fhd.assess.interfaces.quaAssess.IQuaAssessBO;

@Service
public class SubmitAssessBO {
	
	@Autowired
	private ScoreResultDAO o_scoreResultDAO;
	
	@Autowired
	private IQuaAssessBO o_quaAssessBO;
	
	/**
	 * 通过综合ID查询打分结果
	 * @param rangObjectDeptEmpId 综合ID
	 * @return List<ScoreResult>
	 * @author 金鹏祥
	 * */
	@SuppressWarnings("unchecked")
	public List<ScoreResult> findScoreResultByrangObjectDeptEmpId(String rangObjectDeptEmpId[]) {
		Criteria criteria = o_scoreResultDAO.createCriteria();
		criteria.add(Restrictions.in("rangObjectDeptEmpId.id", rangObjectDeptEmpId));
		List<ScoreResult> list = null;
		list = criteria.list();
		
		return list;
	}
	
	/**
	 * 提交
	 * @param assessPlanId 评估ID
	 * @return String
	 * @author 金鹏祥
	 * */
	@Transactional
	public boolean submitAssess(String params){
		boolean isBool = false;
		try {
			JSONArray jsonarr = JSONArray.fromObject(params);
			String rangObjectDeptEmpId = "";
			for (Object objects : jsonarr) {
				JSONObject jsobjs = (JSONObject) objects;
				rangObjectDeptEmpId += jsobjs.get("rangObjectDeptEmpId").toString() + "--";
			}
			String rangObjectDeptEmpIds[] = rangObjectDeptEmpId.split("--");
			List<ScoreResult> scoreResultList = this.findScoreResultByrangObjectDeptEmpId(rangObjectDeptEmpIds);
			
			for (ScoreResult scoreResult : scoreResultList) {
				scoreResult.setApproval(true);
				o_quaAssessBO.mergeScoreResult(scoreResult);
			}
			isBool = true;
		} catch (Exception e) {
			isBool = false;
		}
		
		
		return isBool;
	}
}