
package com.fhd.bpm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.bpm.entity.ProcessDefinitionDeploy;
import com.fhd.core.dao.hibernate.HibernateDao;

@Repository
public class ProcessDefinitionDeployDAO  extends HibernateDao<ProcessDefinitionDeploy,String>{
}

