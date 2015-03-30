package com.fhd.bpm.business;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.dao.HistoryProcessInstanceDAO;

@Service
@SuppressWarnings({"unchecked"})
public class HistoryProcessInstanceBO {
	@Autowired
	private HistoryProcessInstanceDAO o_historyProcessInstanceDAO;
	
	public List<HistoryProcessInstanceImpl> findByProcessInstanceId(String processInstanceId){
		Criteria criteria = o_historyProcessInstanceDAO.createCriteria();
		criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
		return criteria.list();
	}
	@Transactional
	public void update(HistoryProcessInstanceImpl historyProcessInstanceImpl){
		o_historyProcessInstanceDAO.merge(historyProcessInstanceImpl);
	}
	
	
}