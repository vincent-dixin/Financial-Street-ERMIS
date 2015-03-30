package com.fhd.sys.business.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.impl.cmd.FindActiveActivityIdsCmd;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.dic.DictEntryI18DAO;
import com.fhd.sys.dao.dic.DictEntryRelationDAO;
import com.fhd.sys.dao.dic.DictEntryRelationTypeDAO;
import com.fhd.sys.dao.dic.DictTypeDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryI18;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.dic.DictEntryRelationType;
import com.fhd.sys.entity.dic.DictType;
import com.fhd.sys.interfaces.IDictBO;
import com.fhd.sys.interfaces.IDictRelationBO;

/**
 * 数据字典业务表关联类
 * @author 郑军祥
 * @Date 2013-6-4
 * 
 * @see
 */
@Service
public class DictEntryRelationBO implements IDictRelationBO {
	@Autowired
	private DictEntryRelationTypeDAO o_dictEntryRelationTypeDao;
	
    @Autowired
    private DictEntryRelationDAO o_dictEntryRelationDao;

    @Transactional
    public void saveDictEntryRelation(DictEntryRelation dictEntryRelation) {
		o_dictEntryRelationDao.merge(dictEntryRelation);
    }
    
    @Transactional
    public void removeDictEntryRelation(String id) {
    	o_dictEntryRelationDao.delete(id);
    }
    
    public DictEntryRelationType findDictEntryRelationTypeByTypeId(String typeId){
    	Criteria criteria = o_dictEntryRelationTypeDao.createCriteria();
    	criteria.add(Restrictions.eq("id", typeId));
    	
    	return (DictEntryRelationType)criteria.uniqueResult();
    }
}