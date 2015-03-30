package com.fhd.sys.business.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.file.FileUploadDAO;
import com.fhd.sys.dao.file.VFileUploadDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.file.VFileUploadEntity;
import com.fhd.sys.web.form.file.FileUploadForm;

/**
 * 文件BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-8 
 * @since    Ver 1.1
 * @Date	 2010-9-8		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */

@Service
@SuppressWarnings("unchecked")
public class FileUploadBO {
	
	@Autowired
	private VFileUploadDAO o_vFileUploadDAO;
	@Autowired
	private FileUploadDAO o_fileUploadDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * 
	 * findById:
	 * 
	 * @author 杨鹏
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public FileUploadEntity findById(String id){
		return o_fileUploadDAO.get(id);
	}
	
	/**
	 * 
	 * findPageBySome:
	 * 
	 * @author 杨鹏
	 * @param page
	 * @param fileTypeId
	 * @param oldFileName
	 * @param sort
	 * @param dir
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<VFileUploadEntity> findPageBySome(Page<VFileUploadEntity> page,String fileTypeId,String oldFileName, List<Map<String, String>> sortList){
		DetachedCriteria criteria=DetachedCriteria.forClass(VFileUploadEntity.class);
		criteria.createAlias("fileType", "fileType");
		if(StringUtils.isNotBlank(oldFileName)){
			criteria.add(Restrictions.like("oldFileName", oldFileName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(fileTypeId)){
			DetachedCriteria fileTypeCriteria=DetachedCriteria.forClass(DictEntry.class);
			fileTypeCriteria.add(Restrictions.like("idSeq", "."+fileTypeId+".",MatchMode.ANYWHERE));
			fileTypeCriteria.setProjection(Property.forName("id"));
			criteria.add(Property.forName("fileType.id").in(fileTypeCriteria));
		}
		if(null!=sortList){
			for (Map<String, String> sort : sortList) {
				String property=sort.get("property");
				String direction = sort.get("direction");
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("fileTypeDictEntryName".equalsIgnoreCase(property)){
						property="fileType.sort";
					}
					if("desc".equalsIgnoreCase(direction)){
						criteria.addOrder(Order.desc(property));
					}else{
						criteria.addOrder(Order.asc(property));
					}
				}
			}
		}else{
			criteria.addOrder(Order.desc("uploadTime"));
		}
		return o_vFileUploadDAO.findPage(criteria, page, false);
	}
	
	public List<VFileUploadEntity> findBySome(String fileType,String oldFileName,String[] ids){
		Criteria criteria = o_vFileUploadDAO.createCriteria();
		if(ids!=null){
			if(ids.length>0){
				criteria.add(Restrictions.in("id", ids));
			}else{
				criteria.add(Restrictions.isNull("id"));
			}
		}
		if(StringUtils.isNotBlank(oldFileName)){
			criteria.add(Restrictions.like("oldFileName", oldFileName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(fileType)){
			criteria.add(Restrictions.eq("fileType", fileType));
		}
		return criteria.list();
	}
	/**
	 * 保存文件.
	 * @author 吴德福
	 * @param fileUploadForm 文件Form.
	 * @param request
	 * @param response
	 * @return boolean 是否上传成功.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveFileUpload(FileUploadEntity fileUpload) throws Exception{
		try {
			o_fileUploadDAO.merge(fileUpload);
			o_businessLogBO.saveBusinessLogInterface("新增", "文件上传", "成功", fileUpload.getOldFileName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "文件上传", "失败", fileUpload.getOldFileName());
		}
	}
	
	/**
	 * 修改文件.
	 * @author 吴德福
	 * @param file 上传文件.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateFile(FileUploadEntity fileUpload){
		try {
			o_fileUploadDAO.merge(fileUpload);
			o_businessLogBO.modBusinessLogInterface("修改", "文件上传", "成功", fileUpload.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "文件上传", "失败", fileUpload.getId());
		}
	}
	
	/**
	 * 删除文件.
	 * @author 吴德福
	 * @param id 文件id.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeFileById(String id){
		try {
			o_fileUploadDAO.delete(id);
			o_businessLogBO.delBusinessLogInterface("删除", "文件上传", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "文件上传", "失败", id);
		}
	}
	
	/**
	 * 根据id查询上传文件.
	 * @author 吴德福
	 * @param id 文件id.
	 * @return FileUpload 文件.
	 * @since  fhd　Ver 1.1
	 */
	public FileUploadEntity queryFileById(String id){
		return o_fileUploadDAO.get(id);
	}
	
	/**
	 * 查询所有的上传文件.
	 * @author 吴德福
	 * @return List<FileUpload> 上传文件集合.
	 * @since  fhd　Ver 1.1
	 */
	public Page<FileUploadEntity> queryAllFileUpload(Page<FileUploadEntity> page){
		return o_fileUploadDAO.findPage(DetachedCriteria.forClass(FileUploadEntity.class), page,false);
	}
	
	/**
	 * 根据查询条件查询文件信息.
	 * @author 吴德福
	 * @param fileUploadForm.
	 * @return List<FileUpload> 文件信息集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<FileUploadEntity> fineFileUploadEntityBySome(FileUploadForm fileUploadForm){
		StringBuilder hql = new StringBuilder();
		hql.append("From FileUpload Where 1=1 ");
		String username = "";
		if(fileUploadForm != null && !"".equals(fileUploadForm.getUsername()) && fileUploadForm.getUsername() != null){
			username = fileUploadForm.getUsername();
			hql.append(" and sysUser.username like '%"+username+"%'");
		}
		if(fileUploadForm != null && !"".equals(fileUploadForm.getOldFileName()) && fileUploadForm.getOldFileName() != null){
			hql.append(" and oldFileName like '%"+fileUploadForm.getOldFileName()+"%'");
		}
		if(fileUploadForm != null && fileUploadForm.getNewFileName() != null && fileUploadForm.getNewFileName() != null){
			hql.append(" and newFileName like '%"+fileUploadForm.getNewFileName()+"%'");
		}
		return o_fileUploadDAO.find(hql.toString());
	}
	/**
	 *  根据查询条件查询文件信息
	 * @param page
	 * @param fileUploadEntity
	 * @return
	 */
	public Page<FileUploadEntity> queryAllFileByCondandPage(Page<FileUploadEntity> page,FileUploadEntity fileUploadEntity,String sort,String dir){
		DetachedCriteria dc = DetachedCriteria.forClass(FileUploadEntity.class);
		dc.createCriteria("sysUser","user");
		if(StringUtils.isNotBlank(fileUploadEntity.getOldFileName())){
			dc.add(Restrictions.like("oldFileName", fileUploadEntity.getOldFileName(), MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(fileUploadEntity.getNewFileName())){
			dc.add(Restrictions.like("newFileName", fileUploadEntity.getNewFileName(), MatchMode.ANYWHERE));
		}
		//用户名
		if(StringUtils.isNotBlank(fileUploadEntity.getNotes())){
			dc.add(Restrictions.like("user.username", fileUploadEntity.getNotes(), MatchMode.ANYWHERE));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"userName".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.asc(sort));
			}else if("id".equals(sort)){
				dc.addOrder(Order.asc("uploadTime"));
			}else{
				dc.addOrder(Order.asc("user.username"));
			}
		}else {
			if(!"userName".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.desc(sort));
			}else if("id".equals(sort)){
				dc.addOrder(Order.desc("uploadTime"));
			}else{
				dc.addOrder(Order.desc("user.username"));
			}
		}
		return o_fileUploadDAO.findPage(dc, page,false);
	}
	/**
	 * 保存文件全文引自FileUploadControl 返回类型改为文件ID
	 * @param chooseWay
	 * @param aliasName
	 * @param file
	 * @param response
	 * @return 文件ID
	 * @throws Exception
	 */
	@Transactional
	public String uploadFile(String chooseWay, String aliasName,
			MultipartFile file) throws Exception {

		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "txt",
				"doc", "mp3", "wma", "m4a", "rar", "zip", "xls" };

		// 初始化参数
		String path = "";
		long size = 0;
		// 当前文件为空
		/*byte[] bytes = file.getBytes();
		if (file.getBytes() == null || file.getBytes().length == 0) {
			return null;
		}*/
		// 得到文件的完整路径
		path = file.getOriginalFilename();
		// 构建文件目录
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		// 得到文件的大小
		// size = fileItem.getSize();
/*		size = file.getSize();
		if ("".equals(path) || size == 0) {
			return null;
		}*/
		// 得到去除路径的文件名
		String t_name = path.substring(path.lastIndexOf("\\") + 1);
		// 得到文件的扩展名(无扩展名时将得到全名)
		String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
		// 拒绝接受规定文件格式之外的文件类型
		int allowFlag = 0;
		int allowedExtCount = allowedExt.length;
		if (allowFlag == allowedExtCount) {
			return t_ext+"_fileTypeout";
		}
		long now = System.currentTimeMillis();
		// 根据系统时间生成上传后保存的文件名
		String prefix = String.valueOf(now);

		// 时间数字文件名称
		// String filename = t_name.substring(0, t_name.length() -
		// t_ext.length() - 1)+ prefix + "." + t_ext;
		String filename = prefix + "." + t_ext;

		// 配置文件读取文件路径：F:\\upload\\
		String fileUploadpath = ResourceBundle.getBundle("application")
				.getString("fileUploadPath");

		if ("fileDir".equals(chooseWay)) {
			FileOutputStream fout = new FileOutputStream(new File(
					fileUploadpath + filename));
			IOUtils.copy(file.getInputStream(), fout);
		}

		// 保存文件到数据库中
		FileUploadEntity fileUpload = new FileUploadEntity();
		fileUpload.setId(Identities.uuid());
		fileUpload.setOldFileName(t_name.substring(0,
				t_name.length() - t_ext.length() - 1)
				+ "." + t_ext);
		if (!"".equals(aliasName) && null != aliasName) {
			fileUpload.setNewFileName(aliasName + "." + t_ext);
		} else {
			fileUpload.setNewFileName(t_name.substring(0, t_name.length()
					- t_ext.length() - 1)
					+ "." + t_ext);
		}
		if ("fileDir".equals(chooseWay)) {
			fileUpload.setFileAddress(fileUploadpath + filename);
		}
		fileUpload.setFileSize(String.valueOf(size));
		DictEntry fileType = o_dictEntryBO.getDictEntryByValueAndDicTypeId(t_ext, "0file_type");
		fileUpload.setFileType(fileType);
		fileUpload.setUploadTime(new Date());
		fileUpload.setCountNum(0);

		if ("dataBase".equals(chooseWay)) {
			fileUpload.setContents(file.getBytes());
		}

		saveFileUpload(fileUpload);

		return fileUpload.getId();

	}

}

