/**
 * OrgManagementController.java
 * com.fhd.fdc.commons.web.controller.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-30 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
 */

package com.fhd.sys.web.controller.orgstructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.fdc.utils.excel.importexcel.ReadExcel;
import com.fhd.fdc.utils.excel.util.ExcelUtil;
import com.fhd.fdc.utils.fileupload.FileUpload;
import com.fhd.fdc.utils.fileupload.FileUploadBean;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.business.orgstructure.SysEmpOrgBO;
import com.fhd.sys.business.orgstructure.SysEmpPosiBO;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.group.SysGroup;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.web.form.orgstructure.OrgForm;

/**
 * 机构Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-10
 * Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types =OrgForm.class)
public class OrganizationControl {

	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private SysEmpOrgBO o_sysEmpOrgBO;
	@Autowired
	private SysEmpPosiBO o_sysEmpPosiBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	@Autowired
	private FileUploadBO o_fileUploadBO;
	
	/**
	 * 加载机构树.
	 * @author 吴德福
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/org/orgStructure.do")
	public void orgStructure(HttpServletRequest request) {
		request.setAttribute("orgRoot", o_organizationBO.getRootOrg());
	}
	
	/**
	 * 打开选择岗位树页面.
	 * @author David.Niu
	 * @param type 打开页面类型；如果为"multiple",打开多选页面；如果为"single",打开单选页面
	 * @param checkNode 是否有复选框
	 * @param onlyLeafCheckable 是否只是叶子节点加复选框
	 * @param checkModel 多选: 'multiple'(默认)、单选: 'single'、级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	 * @return String 如果输入参数type为为"multiple",打开positionSelector_mutiple.jsp；如果为"single",打开positionSelector_single.jsp.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/org/getRootOrg.do")
	public String getRootOrg(HttpServletRequest request,String type,String checkNode,String onlyLeafCheckable,String checkModel,String tag) {
		String companyId = request.getParameter("companyId");
		if(StringUtils.isBlank(companyId))
			request.setAttribute("orgRoot", o_organizationBO.getRootOrg());
		else
			request.setAttribute("orgRoot", o_organizationBO.getRootOrgByCompanyId(companyId));
		request.setAttribute("checkNode", checkNode);
		request.setAttribute("onlyLeafCheckable", onlyLeafCheckable);
		request.setAttribute("checkModel", checkModel);
		request.setAttribute("showType", request.getParameter("showType"));
		request.setAttribute("tag", tag);
		if("mutiple".equalsIgnoreCase(type))
			return "/sys/orgstructure/tag/positionSelector_mutiple";
		else
			return "/sys/orgstructure/tag/positionSelector_single";
	}

	/**
	 * 点击机构，加载tab页面.
	 * @author 吴德福
	 * @param id 机构id.
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/org/tabs.do")
	public void showTabs(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
		
		
	}
	/**
	 * 查询选择机构的所有的下级机构.
	 * @author 吴德福
	 * @param id 机构id.
	 * @param success 操作是否成功.
	 * @param request
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/org/query.do")
	public String query(String id,String success,HttpServletRequest request,Model model){
		model.addAttribute("id",id);
		return "sys/orgstructure/org/query";
	}
	/**
	 * @author 万业
	 * @param id 机构ID
	 * @param limit
	 * @param start
	 * @param orgcode
	 * @param orgname
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sys/orgstructure/org/querynextorg.do")
	public Map<String, Object> queryNextOrg(String id, int limit, int start, String orgcode, String orgname){
		SysOrganization sysOrg=new SysOrganization();
		if(StringUtils.isNotBlank(orgcode)){
			sysOrg.setOrgcode(orgcode);
		}
		if(StringUtils.isNotBlank(orgname)){
			sysOrg.setOrgname(orgname);
		}
		Page<SysOrganization> page = new Page<SysOrganization>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		page = o_organizationBO.queryNextSysOrgByIdAndCond(page, id, sysOrg);
		return convertsysOrgValues(page);
		
	}
	/**
	 * page to map
	 * @author 万业
	 * @param page
	 * @return
	 */
	public Map<String, Object>convertsysOrgValues(Page<SysOrganization> page){
		List<SysOrganization> sysOrgs=page.getList();
		Iterator<SysOrganization> iterator=sysOrgs.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			SysOrganization org=iterator.next();
			
			Map<String, Object> row = new HashMap<String, Object>();
			
			row.put("id", org.getId());
			row.put("orgcode", org.getOrgcode());
			row.put("orgname", org.getOrgname());
			row.put("orgType", null!=org.getOrgType()?o_dictEntryBO.getDictEntrybyId(org.getOrgType()).getName():"");
			row.put("parentOrg", org.getParentOrg().getOrgname());
			row.put("address", org.getAddress());
			row.put("linkMan", org.getLinkMan());
			row.put("linkTel", org.getLinkTel());
			row.put("orgStatus", (null!=org.getOrgStatus())?org.getOrgStatus():"");
			
			datas.add(row);
			
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
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
	@RequestMapping("/sys/orgstructure/org/loadOrgTree.do")
	public List<Map<String, Object>> loadOrgTree(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String node = request.getParameter("node");
		//传入id截掉4位 
		String id = StringUtils.substring(request.getParameter("node"), 0, node.length()-4);

		return o_organizationBO.loadOrgTree(id,request.getContextPath());
	}
	
	
	/**
	 * 根据节点类型构造树结点.
	 * @author David.Niu
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/orgstructure/org/loadPositionTree.do")
	public List<Map<String, Object>> loadPositionTree(HttpServletRequest request,
			HttpServletResponse response,Boolean checkNode,String choose) throws IOException {
		String id = StringUtils.substring(request.getParameter("node"), 0, 32);
		String leaf = request.getParameter("leaf");
		String selects = request.getParameter("selects");
		String empfilter=request.getParameter("empfilter");
		String defaultOrg = request.getParameter("defaultOrg");
		return o_organizationBO.loadPositionTree(id,request.getContextPath(),leaf,selects,empfilter,defaultOrg,checkNode,choose);
	}
	
	/**
	 * get方式修改机构.
	 * @author 吴德福
	 * @param id 机构id.
	 * @param model
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/edit.do", method = RequestMethod.GET)
	public void g_editOrg(String id,Model model) throws IllegalAccessException, InvocationTargetException {
		OrgForm orgForm = new OrgForm();
		BeanUtils.copyProperties(o_organizationBO.get(id), orgForm);
		model.addAttribute("orgForm",orgForm);
	}
	/**
	 * post方式修改机构.
	 * @author 吴德福
	 * @param orgForm 机构Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/edit.do", method = RequestMethod.POST)
	public String p_editOrg(OrgForm orgForm,BindingResult result) throws IllegalAccessException, InvocationTargetException {
		SysOrganization org = o_organizationBO.get(orgForm.getId());
		BeanUtils.copyProperties(orgForm, org,new String[]{"parentOrg"});

		o_organizationBO.merge(org);
		return "redirect:/sys/orgstructure/org/edit.do?id=" + orgForm.getId();
	}
	/**
	 * get方式添加机构.
	 * @author 吴德福
	 * @param id 机构id.
	 * @param model
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/add.do", method = RequestMethod.GET)
	public String g_add(String id,Model model,HttpServletRequest request) {
		OrgForm orgForm = new OrgForm();
		orgForm.setParentOrg(o_organizationBO.get(id));
		model.addAttribute(orgForm);
		model.addAttribute("operation",request.getParameter("operation"));
		return "sys/orgstructure/org/add-model";
	}
	/**
	 * post方式添加机构.
	 * @author 吴德福
	 * @param orgForm 机构Form.
	 * @param result
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/org/add.do", method = RequestMethod.POST)
	public void p_add(OrgForm orgForm,BindingResult result,HttpServletResponse response)throws Exception {
		PrintWriter out = null;
		String flag = "false";
		
		try {
			out = response.getWriter();
			//只有超级管理员admin能增加子公司，其它人员只能增加部门
			String type =  orgForm.getOrgType();
			if("0orgtype_c".equals(type) || "0orgtype_sc".equals(type)){
				if(!"admin".equals(UserContext.getUsername())){
					flag = "只有超级管理员admin才能增加子公司!";
				}else{
					SysOrganization org = new SysOrganization();
					BeanUtils.copyProperties(orgForm, org);
					org.setId(Identities.uuid2());
					SysOrganization parOrganization = o_organizationBO.get(orgForm.getParentId());
					org.setParentOrg(parOrganization);
					SysOrganization parCompany = o_organizationBO.getOrgByOrgtype(org);
					org.setCompany(parCompany);
					org.setOrgcode(orgForm.getOrgcode().toUpperCase());
					org.setIsLeaf(true);
					o_organizationBO.save(org);
					flag = "true";
				}
			}else{
				SysOrganization org = new SysOrganization();
				BeanUtils.copyProperties(orgForm, org);
				org.setId(Identities.uuid2());
				org.setParentOrg(o_organizationBO.get(orgForm.getParentId()));
				org.setOrgcode(orgForm.getOrgcode().toUpperCase());
				org.setIsLeaf(true);
				SysOrganization parCompany = o_organizationBO.getOrgByOrgtype(org);
				org.setCompany(parCompany);
				o_organizationBO.save(org);
				flag = "true";
			}
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * get方式更新机构.
	 * @author 吴德福
	 * @param id
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/update.do", method = RequestMethod.GET)
	public String g_update(String id,Model model) {
		OrgForm orgForm = new OrgForm();
		BeanUtils.copyProperties(o_organizationBO.get(id), orgForm);
		orgForm.setParentId(o_organizationBO.get(id).getParentOrg().getId());
		model.addAttribute(orgForm);
		return "sys/orgstructure/org/update-model";
	}
	/**
	 * post方式更新机构.
	 * @author 吴德福
	 * @param orgForm
	 * @param result
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/org/update.do", method = RequestMethod.POST)
	public void p_update(OrgForm orgForm,BindingResult result,HttpServletResponse response)throws Exception {
		SysOrganization org = new SysOrganization();
		BeanUtils.copyProperties(orgForm, org);
		org.setParentOrg(o_organizationBO.get(orgForm.getParentId()));
		org.setOrgcode(orgForm.getOrgcode().toUpperCase());
		SysOrganization parCompany = o_organizationBO.getOrgByOrgtype(org);
		org.setCompany(parCompany);
		o_organizationBO.merge(org);
		response.getWriter().write("true");
	}
	/**
	 * post方式删除机构.
	 * @author 吴德福
	 * @param id 机构id集合.
	 * @param parentid 父id.
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/org/delete.do", method = RequestMethod.POST)
	public void p_delete(String id,String parentid,HttpServletResponse response)throws Exception {
		PrintWriter out= response.getWriter();
		String[] ids=id.split(",");
		
		for(int i=0;i<ids.length;i++){
			if(StringUtils.isNotBlank(ids[i])){
				
				String orgid = StringUtils.substring(ids[i], 0, 32);
				
				//机构下有员工存在，不允许删除
				List<SysEmpOrg> empOrgs = new ArrayList<SysEmpOrg>();
				empOrgs = o_sysEmpOrgBO.querySysEmpOrgByOrgid(orgid);
				if(null != empOrgs && empOrgs.size()>0){
					out.write("机构下有员工存在，不允许删除");
					return;
				}
				
				//查询出所有的机构和子机构
				List<SysOrganization> orgs = new ArrayList<SysOrganization>();
				orgs = o_organizationBO.queryOrgsByOrgid(orgid);
				for(SysOrganization org : orgs){
					Set<SysPosition> positions = org.getSysPositions();
					for(SysPosition position : positions){
						//岗位下有员工存在，不允许删除
						List<SysEmpPosi> empPosis = new ArrayList<SysEmpPosi>();
						empPosis = o_sysEmpPosiBO.querySysEmpPosiByPosiId(position.getId());
						if(null != empPosis && empPosis.size()>0){
							out.write("岗位下有员工存在，不允许删除");
							return;
						}
					}
				}
				
				o_organizationBO.deleteOrg(StringUtils.substring(ids[i], 0, 32));
			}
		}
		out.write("true");
		out.close();
	}
	/**
	 * get方式删除机构.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/delete.do", method = RequestMethod.GET)
	public void g_delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try {
			String orgid = request.getParameter("id");
			
			//机构下有员工存在，不允许删除
			List<SysEmpOrg> empOrgs = new ArrayList<SysEmpOrg>();
			empOrgs = o_sysEmpOrgBO.querySysEmpOrgByOrgid(orgid);
			if(null != empOrgs && empOrgs.size()>0){
				out.write(flag);
				return;
			}
			
			//查询出所有的机构和子机构
			List<SysOrganization> orgs = new ArrayList<SysOrganization>();
			orgs = o_organizationBO.queryOrgsByOrgid(orgid);
			for(SysOrganization org : orgs){
				Set<SysPosition> positions = org.getSysPositions();
				for(SysPosition position : positions){
					//岗位下有员工存在，不允许删除
					List<SysEmpPosi> empPosis = new ArrayList<SysEmpPosi>();
					empPosis = o_sysEmpPosiBO.querySysEmpPosiByPosiId(position.getId());
					if(null != empPosis && empPosis.size()>0){
						out.write(flag);
						return;
					}
				}
			}
			
			o_organizationBO.deleteOrg(orgid);
			flag="true";
			out.write(flag);
				
		}catch(Exception e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	/**
	 * 移动node弹出页面.
	 * @author 吴德福
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/choose.do")
	public String choose(Model model){
		return "sys/orgstructure/org/choose";
	}
	/**
	 * 数据导入页面
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/sys/orgstructure/org/import.do")
	public String importPage(Model model){
		return "sys/orgstructure/org/import";
	}
	
	/**
	 * <pre>
	 * uploadExcel:上传文件
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param fileUploadBean
	 * @param result
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/sys/orgstructure/org/uploadExcel.do")
	public void uploadExcel(FileUploadBean fileUploadBean,
			BindingResult result, HttpServletResponse response)
			throws IOException {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();

		try {
			if (fileUploadBean != null
					&& fileUploadBean.getUploadFile() != null) {
				FileUploadEntity file = FileUpload.uploadFile(fileUploadBean
						.getUploadFile());
				if (StringUtils.isBlank(file.getUploadErrorMessage())) {
					o_fileUploadBO.saveFileUpload(file);
					flag = "id:"+file.getId();
				} else
					flag = file.getUploadErrorMessage();// 上传的文件有错误
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(flag);
		}
	}
	
	/**
	 * 导入机构数据预览前验证树根.
	 * @author 吴德福
	 * @param fileId
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/importOrgCheckRoot.do")
	public void importOrgCheckRoot(String fileId, HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";

		try {
			out = response.getWriter();
			FileUploadEntity file =  o_fileUploadBO.queryFileById(fileId);
			if(null != file){
				//判断导入的机构数据里是否有根结点，如果存在的话，则判断数据库中已有的机构是否有根结点，如果存在，则给出错误提示
				ReadExcel<SysOrganization> readRiskExcel = new ReadExcel<SysOrganization>(SysOrganization.class);
				List<SysOrganization> orgs = readRiskExcel.readExcel(file.getFileAddress(),"机构");
				if(null != orgs && orgs.size()>0){
					for (int i = orgs.size() - 1; i >= 0; i--) {
						SysOrganization org = orgs.get(i);
						if(null != org.getOrgseq()){
							continue;
						}else{
							SysOrganization root = o_organizationBO.getRootOrg();
							if(null != root){
								flag = "数据库中已存在机构名称为'"+root.getOrgname()+"'的根结点，导入的excel数据中存在机构名称为'"+org.getOrgname()+"'根结点，不能进行导入，请调整导入的excel数据!";
							}
						}
					}
				}
			}
			out.write(flag);
		}catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		}finally {
			out.close();
		}
	}
	
	/**
	 * 数据导入验证
	 * @param model
	 * @return
	 * @return String
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sys/orgstructure/org/importPreview.do")
	public String importPreview(Model model,String fileId,HttpServletRequest request,String importType) throws Exception{
		if(StringUtils.isNotBlank(fileId) && StringUtils.isNotBlank(importType)){
			//把上传在服务器上文件转成要导入的对象 放在httpsession中
			List[] datas = this.o_organizationBO.getImportData(fileId,importType);
			model.addAttribute("importType", importType);
			request.getSession().setAttribute("datas", datas[0]);
			request.getSession().setAttribute("edatas", datas[1]);
			request.setAttribute("errorNum", datas[1].size());
		}
		return "sys/orgstructure/org/importPreview"+importType;
	}
	/**
	 * 数据导入
	 * @param importType
	 * @return
	 * @throws Exception
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/org/importData.do")
	public boolean importData(HttpServletRequest request,String importType) throws Exception{
		List data = (List)request.getSession().getAttribute("datas");
		List edata = (List)request.getSession().getAttribute("edatas");
		if(importType.equals("1")){
			this.o_organizationBO.importOrg((List<SysOrganization>)data,(List<SysOrganization>)edata);
		}
		if(importType.equals("2")){
			this.o_organizationBO.importPosi((List<SysPosition>)data,(List<SysPosition>)edata);
		}
		if(importType.equals("3")){
			this.o_organizationBO.importDuty((List<Duty>)data,(List<Duty>)edata);
		}
		if(importType.equals("4")){
			this.o_organizationBO.importSysEmployee((List<SysEmployee>)data,(List<SysEmployee>)edata);
		}
		if(importType.equals("5")){
			this.o_organizationBO.importSysGroup((List<SysGroup>)data,(List<SysGroup>)edata);
		}
		request.getSession().removeAttribute("datas");
		request.getSession().removeAttribute("edatas");
		return true;
	}
	/**
	 * 初始化数据.
	 * 添加注释-吴德福
	 * @author wanye
	 * @param companyid
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/orgstructure/org/initData.do")
	public boolean initData(String companyid){
		o_organizationBO.initData(companyid);
		return true;
	}
	/**
	 * 机构导出.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/org/exportData.do")
	public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("orgid");
		String orgcode = request.getParameter("orgcode");
		String orgname = request.getParameter("orgname");
		List<SysOrganization> sysOrganizations = o_organizationBO.findNextSysOrgBySome(id, orgcode, orgname);
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] objects = null;
		for(int i=0;i<sysOrganizations.size();i++){
			objects = new Object[6];
			objects[0] = sysOrganizations.get(i).getOrgcode();
			objects[1] = sysOrganizations.get(i).getOrgname();
			if(null != sysOrganizations.get(i).getParentOrg()){
				objects[2] = sysOrganizations.get(i).getParentOrg().getOrgname();
			}else{
				objects[2] = "";
			}
			objects[3] = o_dictEntryBO.queryDictEntryById(sysOrganizations.get(i).getOrgType()).getName();
			if(null != sysOrganizations.get(i).getParentOrg()){
				objects[4] = o_organizationBO.getOrgByOrgtype(sysOrganizations.get(i)).getOrgname();
			}else{
				objects[4] = "";
			}
			objects[5] = String.valueOf(i+1);
			list.add(objects);
		}
		String[] fieldTitle = {"机构编号","机构名称","上级机构名称","机构类型（总公司、总公司部门、分公司、分公司部门）","部门所属分公司","序号"};
    	String exportFileName = "机构导出.xls";
    	String sheetName = "机构";
    	int position = 0;
    	ExcelUtil.write(list, fieldTitle, exportFileName, sheetName, position, request, response);
	}
}
