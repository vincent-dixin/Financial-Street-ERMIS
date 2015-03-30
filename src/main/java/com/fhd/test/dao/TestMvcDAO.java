package com.fhd.test.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.test.entity.TestMvc;

@Repository
public class TestMvcDAO extends HibernateDao<TestMvc,String> {

	

}

