package com.fhd.sys.business.sysapp;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.dao.sysapp.ScheduledTaskTempDAO;

import com.fhd.sys.entity.sysapp.ScheduledTaskTemp;

/**
 * 计划任务--模板BO类.
 * @author   weilunkai
 * @version V1.0  创建时间：2012-5-24 
 * @since    Ver 1.1
 * @Date	 2012-5-24
 * Company FirstHuiDa.
 * @see 	 
 */

@Service
@SuppressWarnings("unchecked")
public class ScheduledTaskTempBO {
	@Autowired
	private ScheduledTaskTempDAO o_scheduledTaskTempDAO;
	
	
	/**
	 * 根据id查询内容发布.
	 * @authorweilunkai
	 * @param id
	 * @return ScheduledTaskTemp
	 * @since  fhd　Ver 1.1
	 */
	public ScheduledTaskTemp getTemplateById(String id){
		return o_scheduledTaskTempDAO.get(id);
	}
	
	/**
	 * 新增计划任务模版
	 * @author weilunkai
	 * @param content
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveTemplate(ScheduledTaskTemp template){
		o_scheduledTaskTempDAO.save(template);
	}
	
	/**
	 * 修改计划任务模版.
	 * @author weilunkai
	 * @param template
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateTemplate(ScheduledTaskTemp template){
		
		o_scheduledTaskTempDAO.merge(template);
			
	}
	
	/**
	 * 删除计划任务模版.
	 * @author weilunkai
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeTemplate(String id){
		o_scheduledTaskTempDAO.removeById(id);
		
	}
	/**
	 * 分页条件查询 计划任务--模板
	 * @param page
	 * @param tempName
	 * @param dealMeasure
	 * @param isEnabled
	 * @return
	 */
	
	public Page<ScheduledTaskTemp> getTemplate(Page<ScheduledTaskTemp> page,String tempName, String dealMeasure, String isEnabled) {
		DetachedCriteria dc=DetachedCriteria.forClass(ScheduledTaskTemp.class);
		
		dc.createAlias("dealMeasure", "deal");
		if(StringUtils.isNotBlank(dealMeasure)){
			dc.add(Restrictions.eq("deal.id", dealMeasure));
		}
		
		if(StringUtils.isNotBlank(tempName)){
			dc.add(Property.forName("tempName").like(tempName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(isEnabled)){
			dc.add(Restrictions.eq("isEnabled",isEnabled));
		}
		
		return o_scheduledTaskTempDAO.pagedQuery(dc, page);
	}

}
