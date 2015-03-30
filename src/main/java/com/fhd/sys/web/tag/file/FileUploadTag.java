/**
 * FileUploadSelect.java
 * com.fhd.sys.web.tag.file
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-7-18 		吴德福
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.file;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.dao.file.FileUploadDAO;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 自定义文件上传组件.
 * 
 * <pre>
 * 自定义文件上传组件标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:fileUpload multiple="false" fileCount="1" width="300" choosedFileIds=""/>
 * 参数multiple:文件上传组件multiple.
 * 参数fileCount:文件上传组件fileCount.
 * 参数width:文件上传组件width.
 * 参数choosedFileIds:文件上传组件choosedFileIds.
 * </pre>
 * 
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-7-18		下午02:29:20
 * @see 	 
 */
@SuppressWarnings("unchecked")
public class FileUploadTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 是否上传多个文件，默认为false.
	 */
	private boolean multiple = false;
	/**
	 * 控件宽度.
	 */
	private int width = 300;
	/**
	 * 上传文件个数，默认为单个文件上传.
	 */
	private String fileCount = "2";
	/**
	 * 已上传的文件列表.
	 */
	private String choosedFileIds;
	
	/**
	 * 删除
	 */
	private String delFun = "callback";
	/**
	 * 无参构造方法.
	 */
	public FileUploadTag() {
	}
	/**
	 * 有参构造方法.
	 */
	public FileUploadTag(boolean multiple, int width, String fileCount,
			String choosedFileIds) {
		super();
		this.multiple = multiple;
		this.width = width;
		this.fileCount = fileCount;
		this.choosedFileIds = choosedFileIds;
	}

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		try{
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			FileUploadDAO fileUploadBean = (FileUploadDAO) webApplicationContext.getBean("fileUploadDAO");
			JspWriter out = pageContext.getOut();
			
			StringBuffer s = new StringBuffer();
			
			//默认有一个上传文件框
			s.append("<div id='div'>");
			//s.append("<input type='file' name='file' onkeydown='this.blur();' style='width:"+width+"px'/>");
			s.append("<input type='file' name='file' style='width:"+width+"px'/>");

			if (multiple) {
				s.append("&nbsp;&nbsp;<input type='button' value='追加' onclick='javascript:addNew()'/>");
				s.append("</div>");
				if (StringUtils.isNotBlank(choosedFileIds)) {
					String queryStr[] = choosedFileIds.split(",");
					StringBuilder stringBuilder = new StringBuilder("from FileUploadEntity fu where fu.id in (");
					for (int i = 0; i < queryStr.length; i++) {
						stringBuilder.append("'").append(queryStr[i]).append("'");
						if (i != queryStr.length - 1){
							stringBuilder.append(",");
						}
					}
					stringBuilder.append(")");

					List<FileUploadEntity> fileUploadList = fileUploadBean.find(stringBuilder.toString());
					if (null != fileUploadList && fileUploadList.size() > 0) {
						//显示已经上传的文件列表
						s.append("<div>");
						s.append("<table style=\"width:300px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"fhd_form_table\">");
							s.append("<tr>");
								s.append("<td>文件名称</td>");
								s.append("<td>操作</td>");
							s.append("</tr>");
						for (FileUploadEntity fileUploadEntity : fileUploadList){
								s.append("<tr id=\"file_").append(fileUploadEntity.getId()).append("\">");
									s.append("<td>").append(fileUploadEntity.getOldFileName()).append("</td>");
									s.append("<td><a href=\"").append(pageContext.getServletContext().getContextPath()).append("/sys/file/download.do?id=")
									.append(fileUploadEntity.getId()).append("\">下载</a>||<a href=\"#\" onclick=\"delfilefunction(").append(delFun).append(",'").append(fileUploadEntity.getId()).append("')\">删除</a></td>");
								s.append("</tr>");
						}
						s.append("</table>");
						s.append("</div>");
					}
				}
			}else{
				s.append("</div>");
				if (StringUtils.isNotBlank(choosedFileIds)) {
					String queryStr[] = choosedFileIds.split(",");
					FileUploadEntity fileUploadEntity = fileUploadBean.get(queryStr[0]);
					if (null != fileUploadEntity) {
						//显示已经上传的文件列表
						s.append("<div>");
						s.append("<table style=\"width:300px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"fhd_form_table\">");
							s.append("<tr>");
								s.append("<td>文件名称</td>");
								s.append("<td>操作</td>");
							s.append("</tr>");
							s.append("<tr id=\"file_").append(fileUploadEntity.getId()).append("\">");
								s.append("<td>").append(fileUploadEntity.getOldFileName()).append("</td>");
								s.append("<td><a href=\"").append(pageContext.getServletContext().getContextPath()).append("/sys/file/download.do?id=")
								.append(fileUploadEntity.getId()).append("\">下载</a>||<a href=\"#\" onclick=\"delfilefunction('").append(delFun).append("','").append(fileUploadEntity.getId()).append("')\">删除</a></td>");
							s.append("</tr>");
						s.append("</table>");
						s.append("</div>");
					}
				}
			}
			
			//函数封装
			s.append("<script type=\"text/javascript\"> function delfilefunction(callback,id){  Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){if(btn=='yes'){ var flag = delf(id);if(flag){$('#file_'+id).remove();}}else{return;}});}");
			s.append("var i=1;");
			//动态添加行函数
			s.append("function addNew(){");
				s.append("var j=document.getElementsByName('file').length;");
				s.append("if(j < "+fileCount+"){");
					s.append("$('#div').append('<div id=\""+"div'"+"+i+"+"'\">"+"'+'"+"<input type=\"file\" name=\"file\" style=\"width:"+width+"px\" onkeydown=\"'+"+"'this.blur();'+'\"/>"+"'+'"+"&nbsp;&nbsp;<input id=\"'+"+"i"+"+'\" type=\"button\" value=\"删除\" onclick=\"delRow(this)\"/><br></div>');");
					s.append("i++;");
				s.append("}else{");
					s.append("alert('最多只能上传"+fileCount+"个附件!');");
				s.append("}");
			s.append("}");
			//动态删除行函数
			s.append("function delRow(tag){");
				s.append("var divDel=document.getElementById('div'+tag.id);");
				s.append("var divValue=document.getElementById('div').innerHTML;");
				s.append("var c=document.getElementsByTagName('input');");
				s.append("for(var j=0;j<c.length;j++){");
					s.append("if(c[j].id==tag.id){");
						s.append("document.getElementById('div').removeChild(divDel);");
					s.append("}");
				s.append("}");
			s.append("}");
			s.append("</script>");
			
			out.write(s.toString());
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
		return super.doEndTag();
	}
	
	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getChoosedFileIds() {
		return choosedFileIds;
	}

	public void setChoosedFileIds(String choosedFileIds) {
		this.choosedFileIds = choosedFileIds;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getFileCount() {
		return fileCount;
	}

	public void setFileCount(String fileCount) {
		this.fileCount = fileCount;
	}
	public String getDelFun() {
		return delFun;
	}
	public void setDelFun(String delFun) {
		this.delFun = delFun;
	}
}

