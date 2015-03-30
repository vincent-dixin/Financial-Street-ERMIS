package com.fhd.bpm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.bpm.entity.view.VJbpmHistTask;
import com.fhd.core.dao.hibernate.HibernateDao;

@Repository
public class JbpmHistTaskDAO extends HibernateDao<VJbpmHistTask,Long> {
}
