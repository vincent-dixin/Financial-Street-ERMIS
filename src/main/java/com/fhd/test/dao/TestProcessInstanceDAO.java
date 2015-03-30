package com.fhd.test.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.test.entity.TestProcessInstance;

@Repository
public class TestProcessInstanceDAO extends HibernateDao<TestProcessInstance,String> {

	

}

