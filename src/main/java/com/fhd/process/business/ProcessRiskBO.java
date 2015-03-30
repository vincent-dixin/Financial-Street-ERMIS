package com.fhd.process.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.assess.AssessFrequenceDAO;
import com.fhd.icm.dao.assess.AssessPointDAO;
import com.fhd.icm.dao.assess.MeasureDAO;
import com.fhd.icm.dao.assess.MeasureRelaOrgDAO;
import com.fhd.icm.dao.assess.MeasureRelaRiskDAO;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.icm.entity.control.Measure;
import com.fhd.icm.entity.control.MeasureRelaOrg;
import com.fhd.icm.entity.control.MeasureRelaRisk;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.dao.ProcessPointDAO;
import com.fhd.process.dao.ProcessPointRelaMeasureDAO;
import com.fhd.process.dao.ProcessPointRelaOrgDAO;
import com.fhd.process.dao.ProcessPointRelaPointSelfDAO;
import com.fhd.process.dao.ProcessRelaFileDAO;
import com.fhd.process.dao.ProcessRelaMeasureDAO;
import com.fhd.process.dao.ProcessRelaRiskDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.process.entity.ProcessPointRelaMeasure;
import com.fhd.process.entity.ProcessRelaMeasure;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.process.interfaces.IProcessRiskBO;
import com.fhd.process.web.form.RiskForm;
import com.fhd.risk.dao.RiskDAO;
import com.fhd.risk.entity.Risk;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.web.form.dic.DictEntryForm;
/**
 * 流程节点维护
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-3-11		下午1:17:50
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ProcessRiskBO implements IProcessRiskBO{
	
	@Autowired
	private ProcessPointDAO o_processpointDAO;
	@Autowired
	private ProcessRelaRiskDAO o_ProcessRelaRiskDAO;
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private ProcessPointRelaOrgDAO o_procespointRelaOrgDAO;
	@Autowired
	private ProcessRelaFileDAO o_processRelaFileDAO;
	@Autowired
	private SysOrgDao o_SysOrgDAO;
	@Autowired
    private OrganizationBO o_organizationBO;
	@Autowired
	private SysEmployeeDAO o_SysEmployeeDAO;
	@Autowired
	private ProcessPointRelaPointSelfDAO o_processPointRelaPointSelfDAO;
	@Autowired
	private DictEntryDAO o_DictEntryDAO;
	@Autowired
	private AssessPointDAO o_assessPointDAO;
	@Autowired
	private AssessFrequenceDAO o_AssessFrequenceDAO;
	@Autowired
	private MeasureRelaRiskDAO o_MeasureRelaRisk;
	@Autowired
	private ProcessRelaMeasureDAO o_ProcessRelaMeasureDAO;
	@Autowired
	private MeasureRelaOrgDAO o_measureRelaOrgDAO;
	@Autowired
	private ProcessPointRelaMeasureDAO o_ProcessPointRelaMeasureDAO;
	@Autowired
	private RiskDAO o_RiskDAO;
	@Autowired
	private MeasureDAO o_MeasureDAO;   
	
	/**
	 * <pre>
	 *   保存风险信息
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processPointForm
	 * @param parentId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveRiskMeasure(RiskForm riskForm){
		Risk risk = new Risk();  // 风险实体 
		ProcessRelaRisk proRelaRisk = new ProcessRelaRisk();    // 风险和流程关系实体
		Measure measure = null;
		MeasureRelaRisk measureRelaRisk = null;
		ProcessRelaMeasure processRelaMeasure = null;
		ProcessPointRelaMeasure processPointRelaMeasure = null;
		//获得机构对象
        SysOrganization company = o_organizationBO.get(UserContext.getUser().getCompanyid());
        SysEmployee employee = new SysEmployee();
        employee.setId(UserContext.getUser().getUserid());
        if(StringUtils.isEmpty(riskForm.getProcessRiskId())){    //如果风险Id为空，说明为新增内容
			//保存风险基本信息
			risk.setId(Identities.uuid());
			risk.setCode(riskForm.getCode());
			risk.setName(riskForm.getName());
			risk.setDesc(riskForm.getDesc());
			o_RiskDAO.merge(risk);
			//保存风险和流程关系信息
			proRelaRisk.setId(Identities.uuid());
			proRelaRisk.setProcess(o_processDAO.get(riskForm.getProcessId()));
			proRelaRisk.setRisk(risk);
			o_ProcessRelaRiskDAO.merge(proRelaRisk);
        }else{
        	//修改风险内容
			risk = o_RiskDAO.get(riskForm.getProcessRiskId());
			risk.setCode(riskForm.getCode());
			risk.setName(riskForm.getName());
			risk.setDesc(riskForm.getDesc());
			o_RiskDAO.merge(risk);
			// 删除风险对应的控制措施
			Criteria riskrelameasure = o_MeasureRelaRisk.createCriteria();
			riskrelameasure.createAlias("risk", "risk");
			riskrelameasure.add(Restrictions.eq("risk.id", riskForm.getProcessRiskId()));
			List<MeasureRelaRisk> riskRelaRiskList = riskrelameasure.list();
			for(MeasureRelaRisk riskRelaRisk : riskRelaRiskList){
				//删除控制对应的流程节点
				Criteria pointelameasurectr = o_ProcessPointRelaMeasureDAO.createCriteria();
				pointelameasurectr.createAlias("controlMeasure", "controlMeasure");
				pointelameasurectr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
				List<ProcessPointRelaMeasure> processpointrelameasureList = pointelameasurectr.list();
				for(ProcessPointRelaMeasure processpointrelameasure : processpointrelameasureList){
					o_ProcessPointRelaMeasureDAO.delete(processpointrelameasure);
				}
				//删除控制和流程对应关系
				Criteria processRelaMeasureCtr = o_ProcessRelaMeasureDAO.createCriteria();
				processRelaMeasureCtr.createAlias("controlMeasure", "controlMeasure");
				processRelaMeasureCtr.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
				List<ProcessRelaMeasure> processrelameasureList = processRelaMeasureCtr.list();
				for(ProcessRelaMeasure processrelameasure : processrelameasureList){
					o_ProcessRelaMeasureDAO.delete(processrelameasure);
				}
				//删除控制和部门的关系
				Criteria measureRelaOrgCtr = o_measureRelaOrgDAO.createCriteria();
				measureRelaOrgCtr.createAlias("controlMeasure", "controlMeasure");
				measureRelaOrgCtr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
				List<MeasureRelaOrg> measurerelaorgList = measureRelaOrgCtr.list();
				for(MeasureRelaOrg measurerelaorg : measurerelaorgList){
					o_measureRelaOrgDAO.delete(measurerelaorg);
				}
				o_MeasureRelaRisk.delete(riskRelaRisk);
				//逻辑删除控制措施
				this.removeMearSure(riskRelaRisk.getControlMeasure().getId());
				//物理删除评价点内容
				Criteria criteriaAssess = o_assessPointDAO.createCriteria();
				criteriaAssess.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
				List<AssessPoint> assessPointList = criteriaAssess.list(); 
				for(AssessPoint assesspoint : assessPointList ){
					o_assessPointDAO.delete(assesspoint);
				}
//				o_MeasureDAO.delete(riskRelaRisk.getControlMeasure().getId());
			}
			// 删除
		}
		//保存控制措施信息
        JSONArray msForm=JSONArray.fromObject(riskForm.getMeasureFormstr());
		//String[] measureCode = riskForm.getMeasurecode();
		if(msForm != null){   //如果没有控制节点就只保存风险
		for(int i = 0 ;i < msForm.size(); i++){
			JSONObject jsonObject = msForm.getJSONObject(i);
			measure = new Measure();	
			measure.setId(Identities.uuid());
			measure.setCode(jsonObject.getString("measurecode"));
			measure.setCompany(company);
			if(jsonObject.has("controlFrequency")){
				measure.setControlFrequency(o_DictEntryDAO.get(jsonObject.getString("controlFrequency")));
			}
			if(jsonObject.has("controlMeasure")){
				measure.setControlMeasure(o_DictEntryDAO.get(jsonObject.getString("controlMeasure")));
			}
			measure.setControlPoint(jsonObject.getString("controlPoint"));
			measure.setControlTarget(jsonObject.getString("controlTarget"));
			measure.setCreateBy(employee);
			measure.setCreateTime(new Date());
			measure.setName(jsonObject.getString("meaSureDesc"));
			measure.setImplementProof(jsonObject.getString("implementProof"));
			measure.setIsKeyControlPoint(jsonObject.getString("isKeyPoint"));
//			measure.setRelaSubject(jsonObject.getString("relaSubject"));
			measure.setDeleteStatus("1");
			o_MeasureDAO.merge(measure);
			//保存风险和控制关系信息
			measureRelaRisk = new MeasureRelaRisk();
			measureRelaRisk.setId(Identities.uuid());
			measureRelaRisk.setControlMeasure(measure);
			measureRelaRisk.setRisk(risk);
			o_MeasureRelaRisk.merge(measureRelaRisk);
			//保存控制和流程节点关系,控制节点可以对应多个流程节点
		    String pointNote = jsonObject.getString("pointNote");
			if(!StringUtils.isBlank(pointNote)){              //当流程节点不为空的时候执行
			    String[] pointArray = pointNote.split(",");
				for(int j = 0;j<pointArray.length; j++){
					processPointRelaMeasure = new ProcessPointRelaMeasure();
					processPointRelaMeasure.setId(Identities.uuid());
					processPointRelaMeasure.setProcessPoint(o_processpointDAO.get(pointArray[j].replace("[", "").replace("]", "").replace("\"", "")));
					processPointRelaMeasure.setProcess(o_processDAO.get(riskForm.getProcessId()));
					processPointRelaMeasure.setControlMeasure(measure);
					o_ProcessPointRelaMeasureDAO.merge(processPointRelaMeasure);
				}
			}
			//保存控制和流程关系
			processRelaMeasure = new ProcessRelaMeasure();
			processRelaMeasure.setId(Identities.uuid());
			processRelaMeasure.setControlMeasure(measure);
			processRelaMeasure.setProcess(o_processDAO.get(riskForm.getProcessId()));
			o_ProcessRelaMeasureDAO.merge(processRelaMeasure);
			//保存控制信息和部门关联关系
			MeasureRelaOrg measureRelaOrg = new MeasureRelaOrg();
			if(!StringUtils.isEmpty(jsonObject.getString("meaSureorgId"))){
				measureRelaOrg.setId(Identities.uuid());
				measureRelaOrg.setControlMeasure(measure);
				String orgid=IcmStandardUtils.findIdbyJason(jsonObject.getString("meaSureorgId"), "id");
				measureRelaOrg.setOrg(o_SysOrgDAO.get(orgid));
				measureRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
				o_measureRelaOrgDAO.merge(measureRelaOrg);
			}
			if(!StringUtils.isEmpty(jsonObject.getString("meaSureempId"))){
				measureRelaOrg = new MeasureRelaOrg();
				measureRelaOrg.setControlMeasure(measure);
				measureRelaOrg.setId(Identities.uuid());
				String empid=IcmStandardUtils.findIdbyJason(jsonObject.getString("meaSureempId"), "id");
				measureRelaOrg.setEmp(o_SysEmployeeDAO.get(empid));
				measureRelaOrg.setType(Contents.EMP_RESPONSIBILITY);
				o_measureRelaOrgDAO.merge(measureRelaOrg);
			}
			//保存控制评价点
			this.saveAssessPointEditGrid("",measure.getId(),jsonObject.getString("editGridJson"),"E");
		}
	}
	}
	@Transactional
	public void removeMearSure(String measureId){
		Measure measure = o_MeasureDAO.get(measureId);
		measure.setDeleteStatus("0");
		o_MeasureDAO.merge(measure);
	}
	//保存评价点信息
	@Transactional
	public void saveAssessPointEditGrid(String processPointId,String measureId,String modifiedRecord,String type) {
		JSONArray jsonArray=JSONArray.fromObject(modifiedRecord);
		if(jsonArray.size()==0){
			
		}else{
			try {
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String id = jsonObject.getString("id");
					String pointId = processPointId;
					String processId = jsonObject.getString("processId");
					String assessSampleName = jsonObject.getString("assessSampleName");
					String desc = jsonObject.getString("desc");
					String comment = jsonObject.getString("comment");
					AssessPoint self = new AssessPoint();
					if(id.equalsIgnoreCase(""))
						id = Identities.uuid();
					self.setId(id);
					self.setProcess(o_processDAO.get(processId));
					self.setProcessPoint(o_processpointDAO.get(pointId));
					self.setControlMeasure(o_MeasureDAO.get(measureId));
					self.setAssessSampleName(assessSampleName);
					self.setDesc(desc);
					self.setComment(comment);
					self.setType(type);
					o_assessPointDAO.merge(self);
				}
			} catch (Exception e) {
			}
		}
	}
	/**
	 * <pre>
	 *加载流程表单，将数据库中信息写入表单
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> loadRiskEditFormData(String processRiskId,String processId){
		Map<String, Object> node=new HashMap<String, Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(processRiskId)){
			Criteria criteria = o_RiskDAO.createCriteria();
			criteria.add(Restrictions.eq("id", processRiskId));
			Risk risk=(Risk) criteria.uniqueResult();
			Set<ProcessRelaRisk> processRelaRiskSet =  risk.getRiskProcessures();
			for (ProcessRelaRisk processRelaRisk : processRelaRiskSet) {
				Set<ProcessRelaOrg> processRelaOrgSet = processRelaRisk.getProcess().getProcessRelaOrg();
				for(ProcessRelaOrg processRelaOrg : processRelaOrgSet){
					if(processRelaOrg.getType().equals(Contents.ORG_RESPONSIBILITY)){
						formMap.put("measureInitOrgId", processRelaOrg.getOrg().getId());
					}else if(processRelaOrg.getType().equals(Contents.EMP_RESPONSIBILITY)){
						formMap.put("measureInitEmpId", processRelaOrg.getEmp().getId());
					}
				}
			}
			//formMap.put("code", process.getCode());
			formMap.put("code", risk.getCode());
			formMap.put("name", risk.getName());
			formMap.put("desc", risk.getDesc());
		}
		else{
			Process process = o_processDAO.get(processId);
			Set<ProcessRelaOrg> processRelaOrgSet = process.getProcessRelaOrg();
			for(ProcessRelaOrg processRelaOrg : processRelaOrgSet){
				if(processRelaOrg.getType().equals(Contents.ORG_RESPONSIBILITY)){
					formMap.put("measureInitOrgId", processRelaOrg.getOrg().getId());
				}else if(processRelaOrg.getType().equals(Contents.EMP_RESPONSIBILITY)){
					formMap.put("measureInitEmpId", processRelaOrg.getEmp().getId());
				}
			}
		}
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 *   加载控制措施form
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> loadMeasureEditFormData(String measureId){
		Criteria criteria = o_MeasureDAO.createCriteria();
		criteria.add(Restrictions.eq("id", measureId));
		Criteria orgctr = o_measureRelaOrgDAO.createCriteria();
		orgctr.createAlias("controlMeasure", "controlMeasure").add(Restrictions.eq("controlMeasure.id",measureId));
		//获取控制措施对应节点信息
		Criteria measureRelaPointCtr = o_ProcessPointRelaMeasureDAO.createCriteria();
		measureRelaPointCtr.createAlias("controlMeasure", "controlMeasure");
		measureRelaPointCtr.add(Restrictions.eq("controlMeasure.id", measureId));
		Measure measure=(Measure) criteria.uniqueResult();
		Map<String, Object> formMap = new HashMap<String, Object>();
		//formMap.put("code", process.getCode());
		formMap.put("measurecode", measure.getCode());
//		formMap.put("meaSureDesc", measure.getDesc());
		if(measure.getControlFrequency()!=null){
			formMap.put("controlFrequency", measure.getControlFrequency().getId());
		}
		formMap.put("isKeyPoint", measure.getIsKeyControlPoint());
		formMap.put("implementProof", measure.getImplementProof());
		formMap.put("controlPoint", measure.getControlPoint());
		formMap.put("meaSureDesc", measure.getName());
		if(measure.getControlMeasure() != null){
			formMap.put("controlMeasure", measure.getControlMeasure().getId());
		}
		if(measure.getControlTarget() != null){
			formMap.put("controlTarget", measure.getControlTarget());
		}
//		formMap.put("relaSubject", measure.getRelaSubject());
		//formMap.put("pointNote", measure.getp());
		
		//获取责任和部门
		List<MeasureRelaOrg> orglist=orgctr.list();
		for(MeasureRelaOrg measurerelaorg : orglist){
			 if(Contents.ORG_RESPONSIBILITY.equalsIgnoreCase(measurerelaorg.getType())){
				 formMap.put("meaSureorgId","[{\"id\":\""+measurerelaorg.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
			 }else if(Contents.EMP_RESPONSIBILITY.equalsIgnoreCase(measurerelaorg.getType())){
				 formMap.put("meaSureempId","[{\"id\":\""+measurerelaorg.getEmp().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
			 }else{
			 }
		}
		List<ProcessPointRelaMeasure> measureRelaPointList = measureRelaPointCtr.list();
		String[] array = new String[measureRelaPointList.size()];
		int i = 0;
		for(ProcessPointRelaMeasure pointrelameasure : measureRelaPointList){
			array[i] = pointrelameasure.getProcessPoint().getId();
			i++ ;
		}
		formMap.put("pointNote",array);
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 *   加载控制措施formview
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> loadMeasureEditFormDataForView(String measureId){
		Criteria criteria = o_MeasureDAO.createCriteria();
		criteria.add(Restrictions.eq("id", measureId));
		Criteria orgctr = o_measureRelaOrgDAO.createCriteria();
		orgctr.createAlias("controlMeasure", "controlMeasure").add(Restrictions.eq("controlMeasure.id",measureId));
		//获取控制措施对应节点信息
		Criteria measureRelaPointCtr = o_ProcessPointRelaMeasureDAO.createCriteria();
		measureRelaPointCtr.createAlias("controlMeasure", "controlMeasure");
		measureRelaPointCtr.add(Restrictions.eq("controlMeasure.id", measureId));
		measureRelaPointCtr.add(Restrictions.eq("controlMeasure.deleteStatus", "1"));
		Measure measure=(Measure) criteria.uniqueResult();
		Map<String, Object> formMap = new HashMap<String, Object>();
		//formMap.put("code", process.getCode());
		formMap.put("measurecode", measure.getCode());
//		formMap.put("meaSureDesc", measure.getDesc());
		if(measure.getControlFrequency()!=null){
			formMap.put("controlFrequency", measure.getControlFrequency().getName());
		}
		if(StringUtils.isNotBlank(measure.getIsKeyControlPoint())){
			formMap.put("isKeyPoint", o_DictEntryDAO.get(measure.getIsKeyControlPoint()).getName());
		}
		if(measure.getImplementProof() != null){
			formMap.put("implementProof", measure.getImplementProof());
		}
		formMap.put("controlPoint", measure.getControlPoint());
		formMap.put("meaSureDesc", measure.getName());
		if(measure.getControlMeasure() != null){
			formMap.put("controlMeasure", measure.getControlMeasure().getName());
		}
		formMap.put("controlTarget", measure.getControlTarget());
		//获取责任和部门
		List<MeasureRelaOrg> orglist=orgctr.list();
		for(MeasureRelaOrg measurerelaorg : orglist){
			if(Contents.ORG_RESPONSIBILITY.equalsIgnoreCase(measurerelaorg.getType())){
				formMap.put("orgName",measurerelaorg.getOrg().getOrgname());
			}else if(Contents.EMP_RESPONSIBILITY.equalsIgnoreCase(measurerelaorg.getType())){
				formMap.put("empName",measurerelaorg.getEmp().getEmpname());
			}else{
			}
		}
		List<ProcessPointRelaMeasure> measureRelaPointList = measureRelaPointCtr.list();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(ProcessPointRelaMeasure pointrelameasure : measureRelaPointList){
			sb.append(pointrelameasure.getProcessPoint().getName());
		}
		formMap.put("pointNote",sb.toString());
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 *加载流程表单，将数据库中信息写入表单
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> findMeasureIdbyRiskId(String processId,String processRiskId){
		Criteria criteria = o_MeasureRelaRisk.createCriteria();
		criteria.createAlias("risk", "risk");
		criteria.add(Restrictions.eq("risk.id", processRiskId));
		List<MeasureRelaRisk> measureRelaRiskList = criteria.list();
		String[] measureId = new String[measureRelaRiskList.size()];
		int i = 0;
		for(MeasureRelaRisk measureRelaRisk : measureRelaRiskList){
			measureId[i] = measureRelaRisk.getControlMeasure().getId();
			i++;
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", measureId);
		node.put("success", true);
		return node;
	}
	/**
	 * <pre>
	 *加载流程选择
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processEditID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String,Object>> findProcessPointByIds(String processpointIds){
		
		String[] idArray=processpointIds.split(",");
		Criteria criteria = o_processpointDAO.createCriteria();
		criteria.add(Restrictions.in("id",idArray));
		List<Process> processList=criteria.list();
		List<Map<String,Object>> lm=new ArrayList<Map<String,Object>>();
		for(Process process:processList){
			Map<String, Object> mapBean = new HashMap<String, Object>();
			mapBean.put("id", process.getId());
			mapBean.put("dbid", process.getId());
			mapBean.put("code", process.getCode());
			mapBean.put("text", process.getName());
			lm.add(mapBean);
		}
		return lm;
		
	}
	/**
	 * <pre>
	 *编号自动生成
	 * </pre>
	 * 
	 * @author 李克东
	 * @param processEditID
	 * @param parentId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public Map<String,Object> findProcessPointCode(String processEditID,String parentId)
	{
		Map<String,Object> processMap =new HashMap<String,Object>();
		if(processEditID!=null){
			processMap.put("code",Identities.uuid());
		}else{
//			List<String> list=o_processpointDAO.find("select max(code) from ProcessPoint");
//			String maxprocessid=(String)list.get(0);
//			int i=Integer.parseInt(maxprocessid);
//			String processCode=Integer.toString(i+1);
//			processMap.put("code",processCode);
			processMap.put("code",Identities.uuid());
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", processMap);
		node.put("success", true);
		return node;
	}
	/**
	 * 根据流程编号查找流程下所有的风险
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findProcessRiskPageBySome(Page<ProcessRelaRisk> page, String query,String processId,String companyId) {
		DetachedCriteria ctrProcessRelaRisk = DetachedCriteria.forClass(ProcessRelaRisk.class);   //o_ProcessRelaRiskDAO.createCriteria();   //风险流程关联表 
		ctrProcessRelaRisk.createAlias("risk", "risk", CriteriaSpecification.LEFT_JOIN);   //关联风险实体
		ctrProcessRelaRisk.createAlias("process", "process",CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("process.id",processId));     //关联流程实体
		if(StringUtils.isNotBlank(query)){
			ctrProcessRelaRisk.add(Restrictions.or(Property.forName("risk.code").like(query, MatchMode.ANYWHERE), Property.forName("risk.name").like(query, MatchMode.ANYWHERE)));
		}
		if (!StringUtils.isNotEmpty(processId)) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProcessRelaRisk> ctrProcessRelaRiskList =o_ProcessRelaRiskDAO.findPage(ctrProcessRelaRisk,page,false).getResult();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (ProcessRelaRisk processrelarisk : ctrProcessRelaRiskList) {
			Map<String, Object> map = new HashMap<String, Object>();
			//Risk risk = processrelarisk.getRisk();
			map.put("id", processrelarisk.getRisk().getId());
			map.put("code", processrelarisk.getRisk().getCode());
			map.put("name", processrelarisk.getRisk().getName());
			map.put("desc", processrelarisk.getRisk().getDesc());
			Criteria criteria = o_MeasureRelaRisk.createCriteria();
			criteria.createAlias("risk", "risk",CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("risk.id",processrelarisk.getRisk().getId()));
			criteria.createAlias("controlMeasure", "controlMeasure",CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("controlMeasure.deleteStatus","1"));
			map.put("measureNum", criteria.list().size());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", ctrProcessRelaRiskList.size());

		return result;
	}
	public List<DictEntryForm> findAllProcessPointByProcessId(String processId){
		Criteria criteria = o_processpointDAO.createCriteria().setCacheable(true);
		criteria.createAlias("process", "o").add(Restrictions.eq("o.id",processId));
		List<ProcessPoint> list= criteria.list();
		List<DictEntryForm> result=new ArrayList<DictEntryForm>();
		for(ProcessPoint entry:list)
		{
		    DictEntryForm form=new DictEntryForm();
		    form.setId(entry.getId());
		    form.setName(entry.getName());
//		    form.setLevel(entry.getLevel());
		    result.add(form);
		}
		return result;
	}
	/**
	 * <pre>
	 *		删除流程节点
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeProcessRiskById(String riskId){
		Criteria ctrprocessRelaRisk = o_ProcessRelaRiskDAO.createCriteria();
		ctrprocessRelaRisk.add(Restrictions.eq("risk.id", riskId));
		List<ProcessRelaRisk> processRelaRiskList = ctrprocessRelaRisk.list();
		for(ProcessRelaRisk processRelaRisk : processRelaRiskList){
			o_ProcessRelaRiskDAO.delete(processRelaRisk);
		}
		// 删除风险对应的控制措施
		Criteria riskrelameasure = o_MeasureRelaRisk.createCriteria();
		riskrelameasure.createAlias("risk", "risk");
		riskrelameasure.add(Restrictions.eq("risk.id", riskId));
		List<MeasureRelaRisk> riskRelaRiskList = riskrelameasure.list();
		for(MeasureRelaRisk riskRelaRisk : riskRelaRiskList){
			//删除控制对应的流程节点
			Criteria pointelameasurectr = o_ProcessPointRelaMeasureDAO.createCriteria();
			pointelameasurectr.createAlias("controlMeasure", "controlMeasure");
			pointelameasurectr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
			List<ProcessPointRelaMeasure> processpointrelameasureList = pointelameasurectr.list();
			for(ProcessPointRelaMeasure processpointrelameasure : processpointrelameasureList){
				o_ProcessPointRelaMeasureDAO.delete(processpointrelameasure);
			}
			//删除控制和流程对应关系
			Criteria processRelaMeasureCtr = o_ProcessRelaMeasureDAO.createCriteria();
			processRelaMeasureCtr.createAlias("controlMeasure", "controlMeasure");
			processRelaMeasureCtr.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
			List<ProcessRelaMeasure> processrelameasureList = processRelaMeasureCtr.list();
			for(ProcessRelaMeasure processrelameasure : processrelameasureList){
				o_ProcessRelaMeasureDAO.delete(processrelameasure);
			}
			//删除控制和部门的关系
			//删除控制和流程对应关系
			Criteria measureRelaOrgCtr = o_measureRelaOrgDAO.createCriteria();
			measureRelaOrgCtr.createAlias("controlMeasure", "controlMeasure");
			measureRelaOrgCtr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
			List<MeasureRelaOrg> measurerelaorgList = measureRelaOrgCtr.list();
			for(MeasureRelaOrg measurerelaorg : measurerelaorgList){
				o_measureRelaOrgDAO.delete(measurerelaorg);
			}
			o_MeasureRelaRisk.delete(riskRelaRisk);
			o_MeasureDAO.delete(riskRelaRisk.getControlMeasure().getId());
			
			//删除控制评价点信息
			Criteria criteriaAssess = o_assessPointDAO.createCriteria();
			criteriaAssess.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
			List<AssessPoint> assessPointList = criteriaAssess.list(); 
			for(AssessPoint assesspoint : assessPointList ){
				o_assessPointDAO.delete(assesspoint);
			}
		}
	
	}
	@Transactional
	public void removeMeasureByIds(String ids){
		// 删除风险对应的控制措施
		Criteria riskrelameasure = o_MeasureRelaRisk.createCriteria();
		riskrelameasure.createAlias("controlMeasure", "controlMeasure");
		riskrelameasure.add(Restrictions.eq("controlMeasure.id", ids));
		List<MeasureRelaRisk> riskRelaRiskList = riskrelameasure.list();
		for(MeasureRelaRisk riskRelaRisk : riskRelaRiskList){
			//删除控制对应的流程节点
			Criteria pointelameasurectr = o_ProcessPointRelaMeasureDAO.createCriteria();
			pointelameasurectr.createAlias("controlMeasure", "controlMeasure");
			pointelameasurectr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
			List<ProcessPointRelaMeasure> processpointrelameasureList = pointelameasurectr.list();
			for(ProcessPointRelaMeasure processpointrelameasure : processpointrelameasureList){
				o_ProcessPointRelaMeasureDAO.delete(processpointrelameasure);
			}
			//删除控制和流程对应关系
			Criteria processRelaMeasureCtr = o_ProcessRelaMeasureDAO.createCriteria();
			processRelaMeasureCtr.createAlias("controlMeasure", "controlMeasure");
			processRelaMeasureCtr.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
			List<ProcessRelaMeasure> processrelameasureList = processRelaMeasureCtr.list();
			for(ProcessRelaMeasure processrelameasure : processrelameasureList){
				o_ProcessRelaMeasureDAO.delete(processrelameasure);
			}
			//删除控制和部门的关系
			//删除控制和流程对应关系
			Criteria measureRelaOrgCtr = o_measureRelaOrgDAO.createCriteria();
			measureRelaOrgCtr.createAlias("controlMeasure", "controlMeasure");
			measureRelaOrgCtr.add(Restrictions.eq("controlMeasure.id", riskRelaRisk.getControlMeasure().getId()));
			List<MeasureRelaOrg> measurerelaorgList = measureRelaOrgCtr.list();
			for(MeasureRelaOrg measurerelaorg : measurerelaorgList){
				o_measureRelaOrgDAO.delete(measurerelaorg);
			}
			o_MeasureRelaRisk.delete(riskRelaRisk);
			o_MeasureDAO.delete(riskRelaRisk.getControlMeasure().getId());
			
			//删除控制评价点信息
			Criteria criteriaAssess = o_assessPointDAO.createCriteria();
			criteriaAssess.add(Restrictions.eq("controlMeasure.id",riskRelaRisk.getControlMeasure().getId()));
			List<AssessPoint> assessPointList = criteriaAssess.list(); 
			for(AssessPoint assesspoint : assessPointList ){
				o_assessPointDAO.delete(assesspoint);
			}
		}
	
	}
}

