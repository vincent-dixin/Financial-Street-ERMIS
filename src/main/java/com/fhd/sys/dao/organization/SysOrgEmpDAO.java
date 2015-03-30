package com.fhd.sys.dao.organization;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
/**
 * 员工部门关联DAO
 * @author 王再冉
 *
 */

@Repository
public class SysOrgEmpDAO extends HibernateDao<SysEmpOrg,String> {

}