package com.fhd.sys.web.controller.organization;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.business.organization.OrgPositionBO;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.web.form.organization.PositionForm;

/**
 * 岗位Control类
 * @author user
 *
 */
@Controller
public class OrgPositionControl {
	
	@Autowired
	private OrgPositionBO o_orgPositionBO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	/*@Autowired
	private SysPosiEmpDAO o_sysPosiEmpDAO;*/
	
	
	/**
	 * 岗位列表查询
	 * @param query			岗位名称
	 * @param positionIds	岗位id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/queryPositionAll.f")
	public Map<String, Object> queryPositionAll( String query, String positionIds,String orgId) throws Exception {
		List<SysPosition> entityList = o_orgPositionBO.findPositionByQueryOrIds(query, positionIds,orgId);
		List<SysPosition> datas = new ArrayList<SysPosition>();
		for(SysPosition de : entityList){
			datas.add(new PositionForm(de));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * 岗位状态下拉菜单
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findpositionstates.f")
	public Map<String, Object> findPositionStates(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		inmap = new HashMap<String, Object>();
		inmap.put("id", "1");
		inmap.put("text", "正常");
		list.add(inmap);
		inmap = new HashMap<String, Object>();
		inmap.put("id", "0");
		inmap.put("text", "注销");
		list.add(inmap);
		map.put("datas", list);
		return map;
	}
	
	/**
	 * 保存岗位信息
	 * @param posiForm
	 * @param id
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/saveposition.f")
	public void savePosition(PositionForm posiForm,String id,String orgId, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String isSave = "false";
		String companyId = UserContext.getUser().getCompanyid();
		SysPosition posi = new SysPosition();
		posi.setId(Identities.uuid());
		//岗位的上级部门
		if(null != orgId){
			SysOrganization posiOrg = o_orgGridBO.findOrganizationByOrgId(orgId);
			if(null != posiOrg){
				posi.setSysOrganization(posiOrg);
				posiOrg.setIsLeaf(false);//设置机构是否为叶子
				o_orgGridBO.saveOrganization(posiOrg);
			}
			//posi.setSysOrganization(o_orgGridBO.findOrganizationByOrgId(orgId));
		}else{
			posi.setSysOrganization(o_orgGridBO.findOrganizationByOrgId(companyId));
		}
		posi.setPosicode(posiForm.getPosicode());
		posi.setPosiname(posiForm.getPosiname());
		if(null!=posiForm.getPosiStatus()){
			posi.setPosiStatus(posiForm.getPosiStatus());
		}else{
			posi.setPosiStatus("1");
		}
		posi.setRemark(posiForm.getRemark());
		if(null!=posiForm.getPosiSnStr()){
			int posiSn = Integer.parseInt(posiForm.getPosiSnStr());
			posi.setSn(posiSn);
		}
		posi.setSn(posiForm.getSn());
		//posi.setStartDate(posiForm.getStartDate());
		//posi.setEndDate(posiForm.getEndDate());
		//开始时间
		if(StringUtils.isNotBlank(posiForm.getPosiStartDateStr())){
			posi.setStartDate(DateUtils.parseDate(posiForm.getPosiStartDateStr(), "yyyy-MM-dd"));
		}
		//结束时间
		if(StringUtils.isNotBlank(posiForm.getPosiEndDateStr())){
			posi.setEndDate(DateUtils.parseDate(posiForm.getPosiEndDateStr(), "yyyy-MM-dd"));
		}
		try{
			if(StringUtils.isNotBlank(id)){
				//更新
				String ids[] = id.split(",");
				posi.setId(ids[0]);
				SysPosition findPosi = o_orgPositionBO.findPositionByIds(id);
				posi.setSysOrganization(findPosi.getSysOrganization());
				posi.setSn(findPosi.getSn());
				//posiForm = new PositionForm(findPosi);
				o_orgPositionBO.mergePosition(posi);
				isSave = "true";
			}else{
				//保存
				o_orgPositionBO.savePosition(posi);
				isSave = "true";
			}
			out.write(isSave);
		}finally {
			out.close();
		}
	}
	
	/**
	 * 删除岗位
	 * @param request
	 * @param ids
	 * @return
	 */
	 @ResponseBody
	  @RequestMapping(value = "/sys/organization/removeposientrybyids.f")
	  public boolean removePosiEntryByIds(HttpServletRequest request, String ids) {
		  if (StringUtils.isNotBlank(ids)) {
			  List<SysEmpPosi> resultList = new ArrayList<SysEmpPosi>();
			  resultList = o_orgPositionBO.querySysEmpPosiByPosiId(ids);
			  //岗位下没有员工，删除岗位
			  if(resultList.isEmpty()){
				  o_orgPositionBO.removePositionByIds(ids);
			  }
			   return true;
		  } else {
			   return true;
		  }
	  }
	 
	 /**
	  * 修改岗位
	  * @param request
	  * @param id
	  * @return
	  */
	 @ResponseBody
		@RequestMapping(value = "/sys/organization/findpositionbyid.f")
		public Map<String, Object> findPositionById(HttpServletRequest request, String id) {
			SysPosition sysPosi = new SysPosition();
			if(StringUtils.isNotBlank(id)){
				String ids[] = id.split(",");
				sysPosi.setId(ids[0]);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println("posid:"+sysPosi.getId());
			SysPosition posi = o_orgPositionBO.findPositionByIds(sysPosi.getId());
			Map<String, Object> inmap = new HashMap<String, Object>();
			inmap.put("posicode", posi.getPosicode());
			inmap.put("posiname", posi.getPosiname());
			inmap.put("posiStatus", posi.getPosiStatus());
			inmap.put("posiSnStr", posi.getSn());
			//开始时间
			if(null != posi.getStartDate()){
				inmap.put("posiStartDateStr", posi.getStartDate().toString().split(" ")[0]);
			}
			//结束时间
			if(null != posi.getEndDate()){
				inmap.put("posiEndDateStr", posi.getEndDate().toString().split(" ")[0]);
			}
			inmap.put("remark", posi.getRemark());
			map.put("data", inmap);
			map.put("success", true);
			return map;
	    }
	 
	 /**
	  * 添加岗位下的员工
	  * @param posiId
	  * @param empItem
	  * @return
	  */
	 @ResponseBody
		@RequestMapping(value = "/sys/organization/addempunderposi.f")
		public Map<String, Object> addEmpunderPosi(String posiId,String empItem) {
			Map<String, Object> map = new HashMap<String, Object>();
			o_orgPositionBO.addPosiEmp(posiId,empItem);
			map.put("success", true);	
			return map;	
	    }
	 /**
	  * 删除岗位员工实体
	  * @param request
	  * @param ids
	  * @return
	  */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/removeempposientrybysome.f")
	  public boolean removeEmpPosiEntryBySome(HttpServletRequest request, String ids,String posiId) {
		  if (StringUtils.isNotBlank(ids)) {
			  String[] idArray = ids.split(",");
			  for(String id:idArray){
				  SysEmpPosi empPosi = o_orgPositionBO.findEmpPosiByempIdAndPosiId(id,posiId);
					 if(null != empPosi){
						 if(empPosi.getSysPosition().getId().equals(posiId)){//判断岗位员工对应关系
							// o_sysPosiEmpDAO.delete(empPosi);
							 o_orgPositionBO.deleteEmpPosiEntryById(empPosi.getId());
						 }
					 }
			  }
			   return true;
		  } else {
			   return true;
		  }
	  }

}
