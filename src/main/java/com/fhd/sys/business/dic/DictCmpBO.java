package com.fhd.sys.business.dic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.dic.DictEntryI18DAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryI18;
import com.fhd.sys.web.form.dic.DictEntryForm;

/**
 * 数据字典组件业务类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:46:13
 *
 * @see 	 
 */
@Service
public class DictCmpBO{
    @Autowired
    private DictEntryDAO o_dictEntryDAO;
    @Autowired
    private DictEntryI18DAO o_dictEntryI18DAO;
    @Autowired
    private TimePeriodDAO o_timePeriodDAO;
    /**
     * 
     * findDictEntryByTypeId:根据typeid取字典列表
     * 
     * @author 王钊
     * @param typeId 字典类型ID
     * @return List<DictEntryForm>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public List<DictEntryForm> findDictEntryByTypeId(String typeId) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(typeId)) {
			criteria.add(Restrictions.eq("dictType.id", typeId));
		} else {
		    return null;
		}
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.asc("sort"));	//按排序号排序
		List<DictEntry> list= criteria.list();
		List<DictEntryForm> result=new ArrayList<DictEntryForm>();
		for(DictEntry entry:list)
		{
		    DictEntryForm form=new DictEntryForm();
		    form.setId(entry.getId());
		    form.setName(entry.getName());
		    form.setLevel(entry.getLevel());
		    result.add(form);
		}
		return result;
    }
    
    /**
     * <pre>
     * findDictEntryI18NameBySome:通过entryId,locale查询DictEntry实体
     * </pre>
     * 
     * @author 金鹏祥
     * @param entryId 字典项ID
     * @param locale 本地化
     * @return
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
	public String findDictEntryI18NameBySome(String entryId,String locale){
		Criteria criteria = o_dictEntryI18DAO.createCriteria();
		criteria.setCacheable(true);
		criteria.add(Restrictions.eq("dictEntry.id", entryId));
		criteria.add(Restrictions.eq("myLocale", locale));
		List<DictEntryI18> l=criteria.list();
		if(l!=null && l.size()>0)
		{
		    return l.get(0).getName();
		}
		return null;
    }
    
    /**
	 * 通过日期类型、年、季、月查询
	 * 
	 * @author 金鹏祥
	 * @param eType 日期类型
	 * @param eYear年
	 * @param eQuarter 季
	 * @param eMonth 月ID
	 * @return Map<String,Object>
	 * @since  fhd　Ver 1.1
	*/
    @SuppressWarnings("unchecked")
	public List<TimePeriod> findTimePeriodByEtype(String eType, String eYear, String eQuarter, String eMonthId){
    	Criteria criteria = o_timePeriodDAO.createCriteria();
		criteria.add(Restrictions.eq("type", eType));
		if(eType.equalsIgnoreCase("0frequecy_quarter")){
			criteria.add(Restrictions.eq("year", eYear));
		}else if(eType.equalsIgnoreCase("0frequecy_month")){
			criteria.add(Restrictions.eq("year", eYear));
			criteria.add(Restrictions.eq("quarter", eQuarter));
		}else if(eType.equalsIgnoreCase("0frequecy_week")){
			criteria.add(Restrictions.eq("parent.id", eMonthId));
		}
		return criteria.list();
    }
}

