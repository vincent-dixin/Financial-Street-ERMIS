package com.fhd.bpm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.bpm.entity.BusinessWorkFlow;
import com.fhd.core.dao.hibernate.HibernateDao;


@Repository
public class BusinessWorkFlowDAO  extends HibernateDao<BusinessWorkFlow,String>{
}

