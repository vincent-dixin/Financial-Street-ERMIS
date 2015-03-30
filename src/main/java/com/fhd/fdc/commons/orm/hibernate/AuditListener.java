package com.fhd.fdc.commons.orm.hibernate;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.MergeEventListener;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 在自动为entity添加审计信息的Hibernate EventListener.
 * 
 * 在hibernate执行saveOrUpdate()时,自动为AuditableEntity的子类添加审计信息.
 * 
 * @author Vincent
 */
@SuppressWarnings("serial")
public class AuditListener implements MergeEventListener {

	private static Log logger = LogFactory.getLog(AuditListener.class);

	public void onMerge(MergeEvent event)  throws HibernateException {
		Object object = event.getOriginal();

		//如果对象是AuditableEntity子类,添加审计信息.
		if (object instanceof AuditableEntity) {
			AuditableEntity entity = (AuditableEntity) object;
			SysEmployee employee = null;
			if(null != UserContext.getUser()){
				employee = new SysEmployee();
				employee.setId(UserContext.getUser().getEmpid());
			}

			if (entity.getCreateBy() == null) {
				//创建新对象
				entity.setCreateTime(new Date());
				entity.setCreateBy(employee);
			} else {
				//修改旧对象
				entity.setLastModifyTime(new Date());
				entity.setLastModifyBy(employee);

				if(null != UserContext.getUser()){
					logger.info(event.getOriginal() + " 对象(ID:" + entity.getId() + ") 被" + UserContext.getUser().getUsername() + " 在 " +  new Date() + " 修改");
				}else{
					logger.info(event.getOriginal() + " 对象(ID:" + entity.getId() + ") 被" + "系统" + " 在 " +  new Date() + " 修改");
				}
			}
		}
	}


	public void onMerge(MergeEvent arg0, @SuppressWarnings("rawtypes") Map arg1) throws HibernateException {
	}
	
	
}
