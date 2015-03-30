/**   
* @Title: ClipingBO.java 
* @Package com.fhd.fdc.commons.business.sys.cliping 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 张雷 
* @date 2011-3-22 下午02:57:00 
* @version v1.0 
*/ 
package com.fhd.sys.business.cliping;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.dao.cliping.ClipingDAO;
import com.fhd.sys.entity.cliping.Cliping;

/**
 * 
 * ClassName:ClipingBO
 *
 * @author   王 钊
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-10-11		上午10:00:03
 *
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class ClipingBO {

	@Autowired
	private ClipingDAO o_clipingDAO;

	/**
	 * 
	 * <pre>
	 * 查询所有功能
	 * </pre>
	 * 
	 * @author 王 钊
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Cliping> findClipingAll(){
		return o_clipingDAO.getAll();
	}

	/**
	 * 
	 * <pre>
	 * 查询顶部tab信息
	 * </pre>
	 * 
	 * @author 王 钊
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Object> findClipingTab(){
	    Criteria dc = o_clipingDAO.createCriteria();
 
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("category"));
		dc.setProjection(Projections.distinct(projectionList));	// >>>> 这里差别 生成如下SQL: select distinct id, name from ....
		 return dc.list();
	}

	
	/**
	 * 
	 * <pre>
	 * 根据顶部tab信息查询功能信息
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param category
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Cliping> findClipingByCategory(String category){
	    Criteria dc = o_clipingDAO.createCriteria();
		dc.add(Restrictions.eq("category", category));
		List<Cliping> list = dc.list();
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * 更新功能信息
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param id 功能id
	 * @param Status 是否启用
	 * @since  fhd　Ver 1.1
	 */
	 @Transactional
	public void saveCliping(String id, String Status)
	{
	    Cliping cliping = o_clipingDAO.get(id);
	    cliping.setStatus(Status);
	    o_clipingDAO.merge(cliping);
	}
	
	
}
