package com.fhd.bpm.dao;

import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;

@Repository
public class HistoryProcessInstanceDAO extends HibernateDao<HistoryProcessInstanceImpl,Long> {
}
