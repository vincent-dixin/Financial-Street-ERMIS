/**
 * FileUploadDAO.java
 * com.fhd.fdc.commons.dao.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 文件DAO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-8 
 * @since    Ver 1.1
 * @Date	 2010-9-8		下午12:38:50
 * Company FirstHuiDa.
 * @see 	 
 */

@Repository
@SuppressWarnings("unchecked")
public class FileUploadDAO extends HibernateDao<FileUploadEntity,String>{
	public List<FileUploadEntity> getUploadedFile(String type,String id){
		String hql="select fue from FileUploadEntity fue where fue.id in(select iaf.fileId from IARelaFiles iaf where iaf.iaType='"+type.trim()+"' and iaf.iaTableId='"+id.trim()+"')";
		return this.getSession().createQuery(hql).list();
	}
}

