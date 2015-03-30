package com.fhd.sys.dao.sysapp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.app.MailremindTemp;
/**
 * 
 * ClassName:MailremindTempDAO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   万业
 * @version  
 * @since    Ver 1.0
 * @Date	 2011	2011-5-10		下午02:53:23
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class MailremindTempDAO extends HibernateEntityDao<MailremindTemp>{

	public List<MailremindTemp> getMailremindTempByExecuteClassName(String className){
		String hql="select mrt from MailremindTemp mrt where mrt.executeClass='"+className+"'";
		return this.getSession().createQuery(hql).list();
	}
	public List<MailremindTemp> getByStatus(String status){
		String hql="select mrt from MailremindTemp mrt where mrt.status='"+status+"'";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}
	public MailremindTemp getMailremindTempByCode(String code){
		String hql="select mrt from MailremindTemp mrt where mrt.tempCode='"+code+"'";
		return (MailremindTemp)this.getSession().createQuery(hql).uniqueResult();
	}
	
}
