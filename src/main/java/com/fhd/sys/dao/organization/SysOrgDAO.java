package com.fhd.sys.dao.organization;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;

@Repository
public class SysOrgDAO extends HibernateDao<SysOrganization,String> {

}