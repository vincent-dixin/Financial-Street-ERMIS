package com.fhd.fdc.web.controller.components;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.business.components.ComponentBO;
import com.fhd.fdc.utils.ObjectUtil;
import com.fhd.fdc.utils.fileupload.FileUpload;
import com.fhd.fdc.utils.fileupload.FileUploadBean;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.business.orgstructure.PositionBO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

@Controller
public class ComponentControl {
	
	@Autowired
	private FileUploadBO o_fileUploadBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private SysRoleBO o_roleBO;
	@Autowired
	private ComponentBO o_componentBO;
	@Autowired
	private PositionBO o_positionBO;
	@Autowired
	private SysUserBO o_sysUserBO;
	
	/**
	 * 打开文件上传页面；
	 * @author 陈燕杰
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/components/sys/showUploadPage.do")
	public String  showUploadPage(HttpServletRequest request,Model model)
	{
		String templateName=request.getParameter("templateName");
		if(null!=templateName&&!"".equals(templateName))
		{
			model.addAttribute("hasTemplate", true);
			model.addAttribute("templateFile", templateName);
		}return "tag/systemImport";
	}
	/**
	 * 保存上传的文件； 
	 * @author 陈燕杰
	 * @param fileUploadBean:文件实体
	 * @param result
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/components/sys/saveUploadFile.do")
	public void uploadFile(FileUploadBean fileUploadBean,
			BindingResult result, HttpServletResponse response)
			throws IOException {
		PrintWriter out = null;
		
		String flag = "false";
		// 设置接收类型
		response.setContentType("text/html");
		// 设置字符编码为UTF-8, 统一编码，处理出现乱码问题
		response.setCharacterEncoding("UTF-8");
		out = response.getWriter();
		try {
			if (fileUploadBean != null&& fileUploadBean.getUploadFile() != null) 
			{
				FileUploadEntity file = FileUpload.uploadFile(fileUploadBean.getUploadFile());
				if (StringUtils.isBlank(file.getUploadErrorMessage())) 
				{
					o_fileUploadBO.saveFileUpload(file);
					flag = file.getId();
					
				} 
				else
				{
					flag = file.getUploadErrorMessage();// 上传的文件有错误
				}
			}
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 弹出组织选择树页面
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/components/org/jstreepage.do")
	public String jsOrgTreePage(HttpServletRequest request) {
		SysOrganization org = o_organizationBO.getRootOrg();
		request.setAttribute("root", org);
		return "tag/orgJSSelectorTree";
	}
	
	/**
	 * 弹出人员选择树页面
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/components/emp/jstreepage.do")
	public String jsEmpTreePage(HttpServletRequest request) {
		SysOrganization org = o_organizationBO.getRootOrg();
		request.setAttribute("root", org);
		String selects = request.getParameter("selects");
		if(StringUtils.isNotBlank(selects)){
			String[] ids = selects.split(",");
			List<SysEmployee> emps = o_componentBO.getSelectedEmpByIds(ids);
			List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
			for (SysEmployee emp : emps) {
				Map<String, String> record = new HashMap<String, String>();
				ObjectUtil.ObjectPropertyToMap(record, emp);
				datas.add(record);
			}
			JSONArray jsonData = JSONArray.fromObject(datas);  
			request.setAttribute("selects", jsonData.toString());
		}else{
			request.setAttribute("selects", "");
		}
		request.setAttribute("defaultOrg", request.getParameter("defaultOrg"));
		
		return "tag/empJSSelectorTree";
	}
	/**
	 * 弹出人员选择树页面 升级版添加查询选择功能
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/components/emp/jstreepageEx.do")
	public String jsEmpTreePageEx(HttpServletRequest request) {
		SysOrganization org = o_organizationBO.getRootOrg();
		request.setAttribute("orgRoot", org);
		request.setAttribute("root", org);
		String selects = request.getParameter("selects");
		if("sessioncope".equals(selects)){
			Object object = request.getSession().getAttribute("selectId_session");
			selects=null!=object?object.toString():"";
			
		}
		String jsonobj="";
		if(StringUtils.isNotBlank(selects)){
			String[] ids = selects.split(",");
			
			StringBuilder sb=new StringBuilder();
			for(String id:ids){
				if(StringUtils.isBlank(id)){
					continue;
				}
				SysUser user = o_sysUserBO.get(id);
				//{"id":"asdf","realname":"asdf"},{}
				sb.append("{\"id\":\"");
				sb.append(id);
				sb.append("\",\"realname\":\"");
				sb.append(null!=user?(StringUtils.isNotBlank(user.getRealname())?user.getRealname():user.getUsername()):"");
				sb.append("\"},");
			}
			
			String jsonstr=sb.toString();
			if(StringUtils.isNotBlank(jsonstr.trim())){
				jsonobj="["+jsonstr.substring(0, jsonstr.length()-1)+"]";
			}else{
				jsonobj="[]";
			}
		}
		request.setAttribute("selects", jsonobj);
		return "tag/empJSSelectorTreeEx";
	}
	/**
	 * 机构或岗位下的人员列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/components/org/empsEx.do")
	public String jsEmpListEx(HttpServletRequest request){
		String parentType=request.getParameter("parentType");
		String id=request.getParameter("id");
		String abName=request.getParameter("abName");
		String single=request.getParameter("type");
		request.setAttribute("abName", abName);
		request.setAttribute("parentType", parentType);
		request.setAttribute("id", id);
		request.setAttribute("single", single);
		return "tag/empJSSelectList";
	}
	/**
	 * 弹出角色选择树页面
	 * @return String
	 */
	@RequestMapping(value = "/components/role/jstreepage.do")
	public String jsRoleTreePage() {
		return "tag/roleJSSelectorTree";
	}

	/**
	 * 角色数据
	 * @param request
	 * @param selects
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping(value = "/components/role/tree.do")
	public Object riskList(HttpServletRequest request, String selects) {
		List<SysRole> roleList = this.o_roleBO.getUnselectedRole(selects);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (SysRole r : roleList) {
			Map<String, Object> record = new HashMap<String, Object>();
			record.put("id", r.getId());
			record.put("text", r.getRoleName());
			record.put("leaf", true);
			datas.add(record);
		}
		return datas;
	}

	
	/**
	 * 给数据类型下拉框设置名字
	 * @param name
	 * @param selectHtml
	 * @return
	 * @return Object
	 */
	public String getSelectHtmlForDataType(String name,String selectMeasure){
		selectMeasure = selectMeasure.replace("<select name='dataType' style='width: 100px'>", "<select name='"+name+"_dataType' style='width: 100px'>");
		return selectMeasure;
	}
	/**
	 * 给度量下拉框设置名字
	 * @param name
	 * @param selectHtml
	 * @return
	 * @return Object
	 */
	public String getSelectHtml(String name,String selectMeasure){
		selectMeasure = selectMeasure.replace("<select name='measure' style='width: 100px'>", "<select name='"+name+"_measure' style='width: 100px'>");
		return selectMeasure;
	}

	/**
	 * 构造根结点的全部树结点.
	 * 
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @throws IOException
	 * @since fhd Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/components/org/loadOrgTree.do")
	public List<Map<String, Object>> loadOrgTree(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String single=request.getParameter("type");
		String abName = request.getParameter("abName");
		String id = StringUtils.substring(request.getParameter("node"), 0, 32);
		return o_organizationBO.loadOrgTree(id,request.getContextPath(),null,single,abName);
	}
	/**
	 * 构造岗位的全部树结点.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 岗位的树结点集合.
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/components/posi/loadPosiTree.do")
	public List<Map<String, Object>> loadPosiTree(HttpServletRequest request,
			HttpServletResponse response) {
		String id = StringUtils.substring(request.getParameter("node"), 0, 32);
		String empfilter=request.getParameter("empfilter");
		return o_positionBO.loadPosiTree(id,request.getContextPath(),empfilter);
	}
	
}
 