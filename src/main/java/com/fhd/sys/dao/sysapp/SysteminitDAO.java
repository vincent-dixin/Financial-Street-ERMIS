package com.fhd.sys.dao.sysapp;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateGenericDao;
/**
 * 
 * ClassName:SysteminitDAO
 * Function: 系统初始化的数据库操作
 * Reason:	 TODO ADD REASON
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-6-13		上午09:49:55
 *
 */
@Repository
public class SysteminitDAO extends HibernateGenericDao {

	/**
	 * 系统初始化数据的导入
	 * @author 万业
	 * @param list 本地sql列表 
	 * @return
	 */
	public boolean initSystemData(List<String> list){
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		
		for(int i=0;i<list.size();i++){
			
			session.createSQLQuery(list.get(i)).executeUpdate();
			if(i%100==0){
				session.flush();
				session.clear();
			}
			
		}
		transaction.commit();
		
		return false;
		
	}

	@Override
	public String getIdName() {
		return null;
	}
}
