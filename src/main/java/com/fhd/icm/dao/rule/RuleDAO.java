package com.fhd.icm.dao.rule;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.icm.entity.rule.Rule;

@Repository
public class RuleDAO extends HibernateDao<Rule, String> {

}
