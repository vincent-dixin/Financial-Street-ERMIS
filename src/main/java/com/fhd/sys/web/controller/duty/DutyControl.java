package com.fhd.sys.web.controller.duty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.duty.DutyBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.web.form.duty.DutyForm;

/**
 * 职位Controller类.
 * 
 * @author zhaotao
 * @version V1.0 创建时间：2010-12-30 Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types = DutyForm.class)
public class DutyControl {

	@Autowired
	private DutyBO o_dutyBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	/**
	 * 加载机构树.
	 * @author lihuanhuan
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/duty/dutyStructure.do")
	public void orgStructure(HttpServletRequest request) {
		request.setAttribute("orgRoot", o_dutyBO.getRootOrg());
	}
	/**
	 * 载入职务树
	 * 
	 * @param response
	 * @param id
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/sys/duty/loadTree.do")
	public void loadTree(HttpServletRequest request, HttpServletResponse response, String node,Boolean checkNode,String choose) throws IOException {
		response.getWriter().print(JSONArray.fromObject(o_dutyBO.loadTree(node,request.getContextPath(),checkNode,choose)).toString());
	}
   /**
	 * 构造根结点的全部树结点.
	 * 
	 * @author lihuanhuan
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @throws IOException
	 * @since fhd Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/duty/loadOrgTree.do")
	public List<Map<String, Object>> loadOrgTree(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String node = request.getParameter("node");
		String id = StringUtils.substring(node, 0, node.length()-4);
		return o_dutyBO.loadOrgTree(id,request.getContextPath());
	}
	@RequestMapping(value = "/sys/duty/openEmpSelectorTreePage.do")
	public String openSelectorTreePage(HttpServletRequest request, String type, String checkNode, String onlyLeafCheckable, String checkModel, String tag) {
		request.setAttribute("orgRoot", this.o_organizationBO.getRootOrg());
		request.setAttribute("checkNode", checkNode);
		request.setAttribute("onlyLeafCheckable", onlyLeafCheckable);
		request.setAttribute("checkModel", checkModel);
		request.setAttribute("tag", tag);

		if ("mutiple".equals(type))
			return "sys/orgstructure/tag/dutySelector_mutiple";
		return "sys/orgstructure/tag/dutySelector_single";
	}

	/**
	 * 下级人员
	 * 
	 * @param request
	 * @param response
	 * @param node
	 * @return
	 * @throws IOException
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/nexPeople.do")
	public Map<String, Object> nexPeople(Model model,int start,int limit, String id,String empname) throws IOException {
		Page<SysEmployee> page = new Page<SysEmployee>();
		page.setPageNumber((limit == 0 ? 0 : start / limit) + 1);
		page.setObjectsPerPage(limit);
		
		o_dutyBO.loadPeople(page,id, empname);
		
		List<SysEmployee> users = page.getList();
		Iterator<SysEmployee> des = users.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		while (des.hasNext()) {
			Map<String, Object> row = new HashMap<String, Object>();
			SysEmployee user = des.next();
			row.put("id", user.getId());
			row.put("empname", user.getEmpname());
			row.put("username", user.getUsername());
			datas.add(row);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);

		return map;
		
	}

	/**
	 * 下级职务
	 * @author 万业修改
	 * @param request
	 * @param response
	 * @param id
	 * @throws IOException
	 * @return void
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/nexDuty.do")
	public HashMap<String, Object> nexDuty(Model model,int start,int limit, String id,String dutyName) throws IOException {
		//response.getWriter().print(JSONObject.fromObject(o_dutyBO.loadDutysByOrgId(id)).toString());
		Page<Duty> page = new Page<Duty>();
		page.setObjectsPerPage(20);
		page.setPageNumber((start + limit) / page.getObjectsPerPage());
		
		this.o_dutyBO.loadDutysByOrgId(page,id,dutyName);
		
		List<Duty> users = page.getList();
		Iterator<Duty> des = users.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		while (des.hasNext()) {
			Map<String, Object> row = new HashMap<String, Object>();
			Duty duty = des.next();
			row.put("id", duty.getId());
			row.put("dutyName", duty.getDutyName());
			datas.add(row);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);

		return map;
	}

	/**
	 * 职务管理tab页面
	 * 
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/sys/duty/tabs.do")
	public String tabs(HttpServletRequest request,String id) {
		request.setAttribute("id", id);
		return "/sys/duty/tabs";
	}

	/**
	 * 下级职务页面
	 * 
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/sys/duty/nexdutyList.do")
	public String nexDutyList() {
		return "/sys/duty/nexdutyList";
	}

	/**
	 * 下级人员页面
	 * @param model
	 * @param id 职务id
	 * @return String
	 */
	@RequestMapping(value = "/sys/duty/nexpeopleList.do")
	public String nexpeopleList(Model model, String id) {
		model.addAttribute("id", id);
		return "/sys/duty/nexpeopleList";
	}

	/**
	 * 增加职务页面
	 * @author 李焕焕修改
	 * @param request
	 * @param model
	 * @param node
	 * @return String
	 */
	@RequestMapping(value = "/sys/duty/addPage.do")
	public String addPage(HttpServletRequest request, Model model, String node) {
		SysOrganization org = o_organizationBO.get(StringUtils.substring(node, 0, node.length()-4));
		DutyForm dutyForm = new DutyForm();
		dutyForm.setCompany(org);
		request.setAttribute("org", org);
		model.addAttribute("dutyForm",dutyForm);
		//request.setAttribute("operation", request.getParameter("operation"));
		model.addAttribute("operation", request.getParameter("operation"));
		//model.addAttribute("operation", request.getParameter("operation"));
		return "/sys/duty/add";
	}

	/**
	 * 增加职务
	 * @author 李焕焕修改
	 * @param request
	 * @param dutyForm
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/add.do")
	public String add(Model model, HttpServletRequest request, DutyForm dutyForm,HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String flag = "false";
		Duty duty = new Duty();
		BeanUtils.copyProperties(dutyForm, duty);
		duty.setId(Identities.uuid());
		duty.setCompany(dutyForm.getCompany());
		duty.setStatus(dutyForm.getDutyStatus());
		try{
		    o_dutyBO.merge(duty);
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
		//request.setAttribute("afterAdd", "true");
		return null;
	}

	/**
	 * 删除职务
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @throws IOException
	 * @return void
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/delete.do")
	public void duty_delete(HttpServletRequest request, HttpServletResponse response, String ids) throws IOException {
		String[] idarr = ids.split(",");
		for (String id : idarr) {
			if (StringUtils.isNotBlank(id)) {
				Duty duty = o_dutyBO.get(id);
				if(null != duty){
					Set<SysEmployee> employees = duty.getEmployees();
					if(null != employees && employees.size()>0){
						response.getWriter().print("有员工依赖于该职务，不能删除!");
						return;
					}else{
						o_dutyBO.deleteDutyById(id);
					}
				}
			}
		}
		response.getWriter().print("true");
	}

	/**
	 * 编辑职务页面
	 * 
	 * @param request
	 * @param model
	 * @param node
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/sys/duty/editPage.do")
	public String editPage(HttpServletRequest request, Model model, String id) {
		Duty duty = o_dutyBO.get(id);
		if(null != duty.getCompany()){
			request.setAttribute("org", duty.getCompany());
		}
		DutyForm form = new DutyForm();
		BeanUtils.copyProperties(duty, form);
		form.setOrgId(duty.getCompany().getId());
		form.setDutyCompany(duty.getCompany().getOrgname());
		model.addAttribute("dutyForm", form);
		model.addAttribute("operation", request.getParameter("operation"));
		return "sys/duty/edit";
	}

	/**
	 * 保存职务信息
	 * @author 李焕焕修改
	 * @param request
	 * @param dutyForm
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/edit.do")
	public void edit(DutyForm dutyForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Duty duty = o_dutyBO.get(dutyForm.getId());
		BeanUtils.copyProperties(dutyForm, duty);
		duty.setStatus(dutyForm.getDutyStatus());
		duty.setCompany(dutyForm.getCompany());
		o_dutyBO.merge(duty);
		//request.setAttribute("afterAdd", "true");
		response.getWriter().write("true");
	}
	/**
	 * 职务添加人员.
	 * @author 吴德福
	 * @param ids
	 * @param id
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/duty/addPeople.do")
	public void addPeople(HttpServletResponse response, String ids, String id) throws IOException {
		PrintWriter out = null;
		String flag = "false";
		
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			Duty duty = null;
			if(StringUtils.isNotBlank(id)){
				duty = o_dutyBO.get(id);
				//数据处理
				if(StringUtils.isNotBlank(ids)){
					String[] arr = ids.split(",");
					
					o_dutyBO.addPeople(id, arr);
					flag = "true";
				}else{
					flag = "请选择要添加的职务'"+duty.getDutyName()+"'的下级人员!";
				}
			}
			
			out.write(flag);
		} catch (Exception e) {
        	e.printStackTrace();
        	out.write(flag);
        } finally {
            out.close();
        }
	}
	/**
	 * 删除职务人员
	 * 
	 * @param response
	 * @param ids
	 * @throws IOException
	 * @return void
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/deletePeople.do")
	public void deletePeople(HttpServletResponse response, String ids) throws IOException {
		String[] arr = ids.split(",");
		o_dutyBO.deletePeople(arr);
		response.getWriter().print("true");

	}

	
	/**
	 * 根据查询内容得到员工树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @throws IOException
	 * @since fhd Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/duty/getPathsBySearchName.do")
	public String getPathsBySearchName(String searchName,HttpServletRequest request,HttpServletResponse response) throws IOException {
 		 List<String>  pathList=o_dutyBO.getPathsBySearchName(searchName);
 		 StringBuilder sbd=new StringBuilder();
 		 for(String path:pathList){
 			 sbd.append(",").append(path);
 		 }
 		 if(sbd.length()==0){
 			return "1";
 		 }
		 return sbd.toString().substring(1);
	}
}
