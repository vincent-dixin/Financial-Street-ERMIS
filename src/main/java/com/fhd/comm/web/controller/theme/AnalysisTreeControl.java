package com.fhd.comm.web.controller.theme;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.theme.AnalysisTreeBO;
import com.fhd.comm.dao.theme.AnalysisDAO;
import com.fhd.comm.entity.theme.Analysis;
import com.fhd.comm.web.form.theme.AnalysisForm;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.organization.OrgGridBO;
/**
 * 主题分析树control类
 * @author 王再冉
 *
 */
@Controller
public class AnalysisTreeControl {
	@Autowired
	private AnalysisTreeBO o_analyTreeBO;
	@Autowired
	private AnalysisDAO o_analysisDAO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	
	
	/**
	 * 主题分析树加载
	 * 
	 * @param query	查询关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ic/theme/analysistreeloader.f")
	public List<Map<String, Object>> analysisTreeLoader(String query)
	{
		String companyId = UserContext.getUser().getCompanyid();// 当前登录员工所在公司id
	    return o_analyTreeBO.treeLoader(companyId,query);
	}
	/**
	 * 保存
	 * @param analyForm
	 * @param id	
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ic/theme/saveanalysisinfo.f")
	public void saveAnalysisInfo(AnalysisForm analyForm,String id, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String isSave = "false";
		Analysis analy = new Analysis();
		analy.setId(Identities.uuid());
		analy.setAnalyname(analyForm.getAnalyname());
		analy.setAnalydesc(analyForm.getAnalydesc());
		try{
			if(StringUtils.isNotBlank(id)){
				//更新
				String ids[] = id.split(",");
				analy.setId(ids[0]);
				Analysis findAnaly = o_analysisDAO.get(analy.getId());
				if(null != findAnaly){
					analy.setCompany(findAnaly.getCompany());
					analy.setAnalyseq(findAnaly.getAnalyseq());//序列
					//analy.setAnalyseq(findAnaly.getParentAnaly().getAnalyseq()+analy.getId());
					analy.setAnalyType(findAnaly.getAnalyType());
					analy.setParentAnaly(findAnaly.getParentAnaly());
					analy.setDeleteStatus(findAnaly.getDeleteStatus());
				}
				o_analyTreeBO.mergeAnalysis(analy);
				isSave = "true";
			}else{
				//保存
				String companyId = UserContext.getUser().getCompanyid();
				analy.setCompany(o_orgGridBO.findOrganizationByOrgId(companyId));//公司为当前登录员工的公司
				//analy.setAnalyType("1");//类型
				analy.setDeleteStatus("1");//删除状态,"0"为已删除
				//o_analyTreeBO.saveAnalysis(analy);
				o_analyTreeBO.mergeAnalysis(analy);
				isSave = "true";
			}
			out.write(isSave);
		}finally {
			out.close();
		}
	}
	/**
	 * 修改
	 * @param request
	 * @param id  节点id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ic/theme/findanalysisbyid.f")
	public Map<String, Object> findAnalysisById(HttpServletRequest request, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Analysis findAnaly = o_analysisDAO.get(id);		//根据id查询实体
		Map<String, Object> inmap = new HashMap<String, Object>();
		inmap.put("analyname", findAnaly.getAnalyname());
		inmap.put("analydesc", findAnaly.getAnalydesc());
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }
	/**
	 * 删除节点(逻辑删除)
	 * @param request
	 * @param id	节点id
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value = "/ic/theme/removeanalysisentrybyId.f")
	  public boolean removeAnalysisEntryById(HttpServletRequest request, String id) {
		 if(StringUtils.isNotBlank(id)){
			 Analysis analy = o_analysisDAO.get(id);
			 if(null != analy){
				 //o_analysisDAO.delete(analy);
				 analy.setDeleteStatus("0");//删除
				 o_analyTreeBO.mergeAnalysis(analy);
			 }
			 return true;
		 }else{
			 return false;
		 }
	  }
	
}
