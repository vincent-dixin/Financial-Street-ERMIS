package com.fhd.fdc.business.components;

import java.util.List;

import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 组件BO类.
 * @author zhaotao
 * @version V1.0 创建时间：2010-12-27 
 * Company FirstHuiDa.
 */

@Service
@SuppressWarnings("unchecked")
public class ComponentBO {
	
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;

	/**
	 * 根据Id获取人员信息
	 * @param ids
	 * @return List<SysEmployee>
	 */
	public List<SysEmployee> getSelectedEmpByIds(String[] ids) {
		return this.o_sysEmployeeDAO.createCriteria(Property.forName("id").in(ids)).list();
	}
}