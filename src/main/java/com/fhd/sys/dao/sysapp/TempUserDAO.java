package com.fhd.sys.dao.sysapp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.app.TempUser;

@SuppressWarnings("unchecked")
@Repository
public class TempUserDAO extends HibernateEntityDao<TempUser>{
	/**
	 * 根据员工id和模板id查询.
	 * @author 吴德福
	 * @param employeeId
	 * @param tempId
	 * @return List<TempUser>
	 * @since  fhd　Ver 1.1
	 */
	public List<TempUser> queryTempUserByUnionId(String employeeId, String tempId){
		String hql="select tu from TempUser tu where tu.sysEmployee.id='"+employeeId+"' and tu.mailremindTemp.id='"+tempId+"'";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}
	/**
	 * 查寻当前用户的所有设置
	 * @param employeeId
	 * @return
	 */
	public List<TempUser> getTempUserByEmployeeId(String employeeId){
		String hql="select tu from TempUser tu where tu.sysEmployee.id='"+employeeId+"'";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}
	/**
	 * 删除当前用户的所有设置
	 * @param employeeId
	 */
	public void removeByEmployeeId(String employeeId){
		String hql="delete TempUser tu where tu.sysEmployee.id='"+employeeId+"'";
		this.getSession().createQuery(hql).executeUpdate();
	}
	/**
	 * 查寻当前模板的所有设置
	 * @param tempId
	 * @return
	 */
	public List<TempUser> getTempUserByTempId(String tempId){
		String hql = "select tu from TempUser tu where tu.mailremindTemp.id='"+tempId+"'";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}
	/**
	 * 删除 当前模板的所有设置
	 * @param tempId
	 */
	public void deleteByTempId(String tempId){
		String hql="delete TempUser tu where tu.mailremindTemp.id='"+tempId+"'";
		Query query = this.getSession().createQuery(hql);
		query.executeUpdate();
	}
	/**
	 * 根据员工id和模板编号查询.
	 * @author 吴德福
	 * @param employeeId
	 * @param tempCode
	 * @return TempUser
	 * @since  fhd　Ver 1.1
	 */
	public TempUser getTempUserByEmployeeIdAndCode(String employeeId, String tempCode){
		String hql="select tu from TempUser tu where tu.sysEmployee.id='"+employeeId+"' and tu.mailremindTemp.tempCode='"+tempCode+"'";
		Query query = this.getSession().createQuery(hql);
		return (TempUser)query.uniqueResult();
	}
}
