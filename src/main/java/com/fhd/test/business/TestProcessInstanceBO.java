
package com.fhd.test.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.test.dao.TestProcessInstanceDAO;
import com.fhd.test.entity.TestProcessInstance;

@Service
public class TestProcessInstanceBO{

	
	@Autowired
	private TestProcessInstanceDAO o_testProcessInstanceDAO;
	@Autowired
	private JBPMBO o_jbpmBO;
	
	
	@Transactional
	public void update(TestProcessInstance testProcessInstance) {
		o_testProcessInstanceDAO.merge(testProcessInstance);
	}
	
	@Transactional
	public void submitA(TestProcessInstance testProcessInstance,String empId) {
		this.update(testProcessInstance);
		String id = testProcessInstance.getId();
		String name = testProcessInstance.getName();
		String entityType="test";
		Map<String, Object> variables=new HashMap<String, Object>();
		variables.put("entityType",entityType);
		variables.put("aEmpId", UserContext.getUser().getEmpid());
		variables.put("bEmpId", empId);
		variables.put("id", id);
		variables.put("name", name);
		o_jbpmBO.startProcessInstance(entityType, variables);
	}
	@Transactional
	public void submitB(TestProcessInstance testProcessInstance,String processInstanceId,String empId) {
		this.update(testProcessInstance);
		String id = testProcessInstance.getId();
		String name = testProcessInstance.getName();
		String entityType="test";
		Map<String, Object> variables=new HashMap<String, Object>();
		variables.put("entityType",entityType);
		variables.put("aEmpId", empId);
		variables.put("id", id);
		variables.put("name", name);
		o_jbpmBO.doProcessInstance(processInstanceId, variables);
	}
	
	
	public Page<TestProcessInstance> findPageBySome(Page<TestProcessInstance> page,String name,String sort,String dir){
		DetachedCriteria dc = DetachedCriteria.forClass(TestProcessInstance.class);
		if(StringUtils.isNotBlank(name)){
			dc.add(Property.forName("name").like(name, MatchMode.ANYWHERE));
		}
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_testProcessInstanceDAO.findPage(dc, page, false);
	}
	
}

