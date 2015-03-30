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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.assess.AssessFrequenceDAO;
import com.fhd.icm.dao.assess.AssessPointDAO;
import com.fhd.icm.dao.assess.MeasureDAO;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.dao.ProcessPointDAO;
import com.fhd.process.dao.ProcessPointRelaOrgDAO;
import com.fhd.process.dao.ProcessPointRelaPointSelfDAO;
import com.fhd.process.dao.ProcessRelaFileDAO;
import com.fhd.process.dao.ProcessRelaOrgDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.process.entity.ProcessPointRelaOrg;
import com.fhd.process.entity.ProcessPointRelaPointSelf;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.process.interfaces.IProcessPointBO;
import com.fhd.process.web.form.ProcessPointForm;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
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
public class ProcessPointBO implements IProcessPointBO{
	
	@Autowired
	private ProcessPointDAO o_processpointDAO;
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private ProcessPointRelaOrgDAO o_procespointRelaOrgDAO;
	@Autowired
	private ProcessRelaOrgDAO o_procesRelaOrgDAO;
	@Autowired
	private ProcessRelaFileDAO o_processRelaFileDAO;
	@Autowired
	private SysOrgDao o_SysOrgDAO;
	@Autowired
	private SysEmployeeDAO o_SysEmployeeDAO;
	@Autowired
	private ProcessPointRelaPointSelfDAO o_processPointRelaPointSelfDAO;
	@Autowired
	private DictEntryDAO o_DictEntryDAO;
	@Autowired
	private AssessPointDAO o_AssessPointDAO;
	@Autowired
	private AssessFrequenceDAO o_AssessFrequenceDAO;
	@Autowired
	private MeasureDAO o_MeasureDAO;   
	
	@Override
	public ProcessPoint findProcessPointById(String processpointId) {
		
		Criteria criteria = o_processpointDAO.createCriteria();
		criteria.add(Restrictions.eq("id", processpointId));
		ProcessPoint process = (ProcessPoint) criteria.uniqueResult();
		return process;
	}
	/**
	 * <pre>
	 *   保存流程,流程节点直接修改，流程节点关联关系表 先删后加
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processPointForm
	 * @param parentId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveProcessPoint(ProcessPointForm processForm){
		ProcessPoint processpoint = new ProcessPoint();
		if(StringUtils.isEmpty(processForm.getEditProcessPointId())){
			if(processForm.getId()!=null && !"".equals(processForm.getId())){
				processpoint = this.findProcessPointById(processForm.getId());
			}
			processpoint.setId(Identities.uuid());
			processpoint.setCode(processForm.getCode());
			processpoint.setName(processForm.getName());
			processpoint.setProcess(o_processDAO.get(processForm.getParentid()));
			processpoint.setDesc(processForm.getDesc());
			processpoint.setSort(processForm.getSort());
			processpoint.setRelaProcess(processForm.getRelaProcess());
			//增加流程节点和选择流程两个字段
			processpoint.setPointType(processForm.getPointType());
			o_processpointDAO.merge(processpoint);    //保存到节点表
			//保存到关联的部门表中1 : 增加主管部门；2：增加配合部门；3：增加责任人
			ProcessPointRelaOrg processpointrelaorg = new ProcessPointRelaOrg();
			if(!StringUtils.isEmpty(processForm.getOrgId())){
				processpointrelaorg.setId(Identities.uuid());
				processpointrelaorg.setProcessPoint(processpoint);
				String orgid=IcmStandardUtils.findIdbyJason(processForm.getOrgId(), "id");
				processpointrelaorg.setOrg(o_SysOrgDAO.get(orgid));
				processpointrelaorg.setType(Contents.ORG_RESPONSIBILITY);
				o_procespointRelaOrgDAO.merge(processpointrelaorg);
			}
			if(!StringUtils.isEmpty(processForm.getCrdorgId())){
				String crdorgid=IcmStandardUtils.findIdbyJason(processForm.getCrdorgId(), "id");
				String[] crdorgidArray = crdorgid.split(",");
				for(int i = 0;i < crdorgidArray.length;i++){
					processpointrelaorg = new ProcessPointRelaOrg();
					processpointrelaorg.setProcessPoint(processpoint);
					processpointrelaorg.setId(Identities.uuid());
					crdorgid.split(",");
					processpointrelaorg.setOrg(o_SysOrgDAO.get(crdorgidArray[i]));
					processpointrelaorg.setType(Contents.ORG_PARTICIPATION);
					o_procespointRelaOrgDAO.merge(processpointrelaorg);
				}
			}
			if(!StringUtils.isEmpty(processForm.getEmpId())){
				processpointrelaorg = new ProcessPointRelaOrg();
				processpointrelaorg.setProcessPoint(processpoint);
				processpointrelaorg.setId(Identities.uuid());
				String empid=IcmStandardUtils.findIdbyJason(processForm.getEmpId(), "id");
				processpointrelaorg.setEmp(o_SysEmployeeDAO.get(empid));
				processpointrelaorg.setType(Contents.EMP_RESPONSIBILITY);
				o_procespointRelaOrgDAO.merge(processpointrelaorg);
			}
		}else{
			//修改流程节点
			processpoint = o_processpointDAO.get(processForm.getEditProcessPointId());
			processpoint.setCode(processForm.getCode());
			processpoint.setName(processForm.getName());
			processpoint.setProcess(o_processDAO.get(processForm.getParentid()));
			processpoint.setDesc(processForm.getDesc());
			processpoint.setSort(processForm.getSort());
			o_processpointDAO.merge(processpoint);    //保存到节点表
			//修改关联关系
			//ProcessPointRelaOrg RelaOrg = o_procespointRelaOrgDAO. 
			//删除关联关系表
			Criteria orgcriteria = o_procespointRelaOrgDAO.createCriteria();
			orgcriteria.createAlias("processPoint", "o").add(Restrictions.eq("o.id",processForm.getEditProcessPointId()));
			List<ProcessPointRelaOrg> orglist=orgcriteria.list();
			for(ProcessPointRelaOrg processpointrelaorg : orglist){
				o_procespointRelaOrgDAO.delete(processpointrelaorg);
			}
			//保存到关联的部门表中1 : 增加主管部门；2：增加配合部门；3：增加责任人
			ProcessPointRelaOrg processpointrelaorg = new ProcessPointRelaOrg();
			if(!StringUtils.isEmpty(processForm.getOrgId())){
				processpointrelaorg.setId(Identities.uuid());
				processpointrelaorg.setProcessPoint(processpoint);
				String orgid=IcmStandardUtils.findIdbyJason(processForm.getOrgId(), "id");
				processpointrelaorg.setOrg(o_SysOrgDAO.get(orgid));
				processpointrelaorg.setType(Contents.ORG_RESPONSIBILITY);
				o_procespointRelaOrgDAO.merge(processpointrelaorg);
			}
			if(!StringUtils.isEmpty(processForm.getCrdorgId())){
				String crdorgid=IcmStandardUtils.findIdbyJason(processForm.getCrdorgId(), "id");
				String[] crdorgidArray = crdorgid.split(",");
				for(int i = 0;i < crdorgidArray.length;i++){
					processpointrelaorg = new ProcessPointRelaOrg();
					processpointrelaorg.setProcessPoint(processpoint);
					processpointrelaorg.setId(Identities.uuid());
					crdorgid.split(",");
					processpointrelaorg.setOrg(o_SysOrgDAO.get(crdorgidArray[i]));
					processpointrelaorg.setType(Contents.ORG_PARTICIPATION);
					o_procespointRelaOrgDAO.merge(processpointrelaorg);
				}
			}
			if(!StringUtils.isEmpty(processForm.getEmpId())){
				processpointrelaorg = new ProcessPointRelaOrg();
				processpointrelaorg.setProcessPoint(processpoint);
				processpointrelaorg.setId(Identities.uuid());
				String empid=IcmStandardUtils.findIdbyJason(processForm.getEmpId(), "id");
				processpointrelaorg.setEmp(o_SysEmployeeDAO.get(empid));
				processpointrelaorg.setType(Contents.EMP_RESPONSIBILITY);
				o_procespointRelaOrgDAO.merge(processpointrelaorg);
			}
			}
		    //保存可编辑列表的内容
			this.saveParentPointEditGrid(processpoint.getId(), processForm.getEditGridJson());
			//保存评价点信息
			this.saveAssessPointEditGrid(processpoint.getId(),"", processForm.getAssessEditGridJson(),"D");
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
	public void removeProcessPointByID(String processPointID){
		//删除与机关部门关联关系表
		Criteria orgcriteria = o_procespointRelaOrgDAO.createCriteria();
		orgcriteria.createAlias("processPoint", "o").add(Restrictions.eq("o.id",processPointID));
		List<ProcessPointRelaOrg> orglist=orgcriteria.list();
		for(ProcessPointRelaOrg processpointrelaorg : orglist){
			o_procespointRelaOrgDAO.delete(processpointrelaorg);
		}
		//删除父节点对应关系 
		Criteria parentcriteria = o_processPointRelaPointSelfDAO.createCriteria();
//		parentcriteria.createAlias("processPoint", "processPoint");
//		parentcriteria.createAlias("PreviousProcessPoint", "PreviousProcessPoint");
		parentcriteria.add(Restrictions.or(Restrictions.eq("processPoint.id", processPointID), Restrictions.eq("PreviousProcessPoint.id",processPointID)));
		List<ProcessPointRelaPointSelf> parantSelfList=parentcriteria.list();
		for(ProcessPointRelaPointSelf parantSelf : parantSelfList){
			o_processPointRelaPointSelfDAO.delete(parantSelf);
		}
		//删除评价点表
		Criteria criteriaAssess = o_AssessPointDAO.createCriteria();
		criteriaAssess.add(Restrictions.eq("processPoint.id",processPointID));
		List<AssessPoint> assessPointList = criteriaAssess.list(); 
		for(AssessPoint assesspoint : assessPointList ){
			o_AssessPointDAO.delete(assesspoint);
		}
		//删除节点表
		Criteria criteria = o_processpointDAO.createCriteria();
		criteria.add(Restrictions.eq("id", processPointID));
		List<ProcessPoint> processpointList=criteria.list();
		ProcessPoint processpoint=processpointList.get(0);
		o_processpointDAO.delete(processpoint);
	}
	/**
	 * <pre>
	 *	    删除对应父亲节点
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param processID
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeParentPointByID(String id){
		//删除关联关系表
		Criteria orgcriteria = o_processPointRelaPointSelfDAO.createCriteria();
		ProcessPointRelaPointSelf processpointrelapointself = (ProcessPointRelaPointSelf) orgcriteria.add(Restrictions.eq("id", id)).uniqueResult() ;
		o_processPointRelaPointSelfDAO.delete(processpointrelapointself);
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
	public Map<String,Object> findProcessPointForm(String processEditId,String processId){
		Map<String, Object> formMap = new HashMap<String, Object>();
		if(!StringUtils.isBlank(processEditId)){
			Criteria criteria = o_processpointDAO.createCriteria();
			criteria.add(Restrictions.eq("id", processEditId));
			ProcessPoint processPoint=(ProcessPoint) criteria.uniqueResult();
			//formMap.put("code", process.getCode());
			formMap.put("code", processPoint.getCode());
			formMap.put("name", processPoint.getName());
			formMap.put("desc", processPoint.getDesc());
			formMap.put("sort", processPoint.getSort());
			if(!(processPoint.getPointType()==null))
				formMap.put("pointType",processPoint.getPointType().getId());
//			if(!(processPoint.getRelaProcess() == null )){  
//				formMap.put("relaProcess",processPoint.getRelaProcess().getId());
//			}
			Criteria orgcriteria = o_procespointRelaOrgDAO.createCriteria();
			orgcriteria.createAlias("processPoint", "o").add(Restrictions.eq("o.id",processEditId));
			List<ProcessPointRelaOrg> orglist=orgcriteria.list();
			JSONArray crdorgIdJsonArray = new JSONArray();
			for(ProcessPointRelaOrg processpointrelaorg : orglist){
				 if(Contents.ORG_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
					 formMap.put("orgId","[{\"id\":\""+processpointrelaorg.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
				 }else if(Contents.EMP_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
					 formMap.put("empId","[{\"id\":\""+processpointrelaorg.getEmp().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
				 }else if(Contents.ORG_PARTICIPATION.equalsIgnoreCase(processpointrelaorg.getType())){
					 crdorgIdJsonArray.add("{\"id\":\""+processpointrelaorg.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}");
					 formMap.put("CrdorgId",crdorgIdJsonArray.toString());
				 }else{
					 
				 }
			}
		}
		else{
			Criteria orgprocesscriteria = o_procesRelaOrgDAO.createCriteria();
			orgprocesscriteria.add(Restrictions.eq("process.id", processId));
			List<ProcessRelaOrg> orgProcesslist=orgprocesscriteria.list();
			if(null != orgProcesslist && orgProcesslist.size()>0){
				for(ProcessRelaOrg pro : orgProcesslist){
					if(Contents.ORG_RESPONSIBILITY.equals(pro.getType())){//责任部门
						formMap.put("orgId","[{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
					}
					if(Contents.ORG_PARTICIPATION.equals(pro.getType())){//相关部门
						formMap.put("relaOrgId","[{\"id\":\""+pro.getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
					}
					if(Contents.EMP_RESPONSIBILITY.equals(pro.getType())){//责任人
						formMap.put("empId","[{\"id\":\""+pro.getEmp().getId()+"\",\"empno\":\"\",\"empname\":\"\"}]");
					}
				}
			}
		}
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
	public Map<String,Object> findProcessPointFormForView(String processEditId){
		ProcessPoint processPoint = o_processpointDAO.get(processEditId);
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("code", processPoint.getCode());
		formMap.put("name", processPoint.getName());
		formMap.put("desc", processPoint.getDesc());
		formMap.put("sort", processPoint.getSort());
		if(!(processPoint.getPointType()==null))
			formMap.put("pointType",processPoint.getPointType().getName());
//		if(!(processPoint.getRelaProcess() == null )){  
//			formMap.put("relaProcess",processPoint.getRelaProcess().getName());
//		}
		Criteria orgcriteria = o_procespointRelaOrgDAO.createCriteria();
		orgcriteria.createAlias("processPoint", "o").add(Restrictions.eq("o.id",processEditId));
		List<ProcessPointRelaOrg> orglist=orgcriteria.list();
		StringBuffer sb = new StringBuffer();
		for(ProcessPointRelaOrg processpointrelaorg : orglist){
			 if(Contents.ORG_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
				 formMap.put("orgName",processpointrelaorg.getOrg().getOrgname());
			 }else if(Contents.EMP_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
				 formMap.put("empName",processpointrelaorg.getEmp().getEmpname());
			 }else if(Contents.ORG_PARTICIPATION.equalsIgnoreCase(processpointrelaorg.getType())){
				 sb.append(processpointrelaorg.getOrg().getOrgname()).append(",");
			 }else{
				 
			 }
		}
		if(!StringUtils.isBlank(sb.toString())){
			formMap.put("CrdorgName",sb.toString().substring(0,sb.toString().length()-1));
		}else{
			formMap.put("CrdorgName","无");
		}
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", formMap);
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
		processMap.put("code",DateUtils.formatDate(new Date(), "yyyyMMddhhmmss"));
		Map<String, Object> node=new HashMap<String, Object>();
		node.put("data", processMap);
		node.put("success", true);
		return node;
	}
	/**
	 * 根据流程id查找流程下那所有节点
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findProcessPointListByPage(Page<ProcessPoint> page, String query,String processId,String companyId) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessPoint.class);
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		if (!StringUtils.isNotEmpty(processId)) {
			return null;
		}
		criteria.createAlias("process", "o").add(Restrictions.eq("o.id",processId));
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProcessPoint> ProcessPointList =o_processpointDAO.findPage(criteria,page,false).getResult();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (ProcessPoint processpoint : ProcessPointList) {
			Map<String, Object> map = new HashMap<String, Object>();
			Set<ProcessPointRelaOrg> processpointRelaOrgList = processpoint.getProcessPointRelaOrg();
			for(ProcessPointRelaOrg processpointrelaorg : processpointRelaOrgList){
				 if(Contents.ORG_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
					 map.put("orgName", processpointrelaorg.getOrg().getOrgname());
				 }else if(Contents.EMP_RESPONSIBILITY.equalsIgnoreCase(processpointrelaorg.getType())){
					 map.put("responsilePersionId", processpointrelaorg.getEmp().getEmpname());
				 }else{
					 
				 }
			}
			map.put("code", processpoint.getCode());
			map.put("text", processpoint.getName());
			map.put("name", processpoint.getName());
			map.put("id", processpoint.getId());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", ProcessPointList.size());

		return result;
	}
	/**
	 * 根据流程id查找流程下那所有节点 不翻页
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public List<ProcessPoint> findProcessPointListByProcessId(String processId) {
		Criteria pointCtr = o_processpointDAO.createCriteria();
		pointCtr.createAlias("process", "o").add(Restrictions.eq("o.id",processId));
		List<ProcessPoint> processPointList = pointCtr.list();
		return processPointList;
	}
	/**
	 * 根据节点ID 找到上级节点和节点进入条件
	 * @autor 宋佳
	 * @param processId
	 *            前台传过来的id
	 * @return map 集合
	 */
	public Map<String, Object> findParentListByPointId(String processPointId,String companyId) {
		Criteria criteria = o_processPointRelaPointSelfDAO.createCriteria();
		criteria.createAlias("processPoint", "o").add(Restrictions.eq("o.id",processPointId));
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProcessPointRelaPointSelf> processpointrelapointselfList = criteria.list();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(ProcessPointRelaPointSelf processpointrelapointself : processpointrelapointselfList){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", processpointrelapointself.getId());
			map.put("pointId", processpointrelapointself.getProcessPoint().getId());
			map.put("processId", processpointrelapointself.getProcess().getId());
			map.put("pointName", processpointrelapointself.getProcessPoint().getName());
			map.put("pointPreId", processpointrelapointself.getPreviousProcessPoint().getId());
			map.put("pointPreName", processpointrelapointself.getPreviousProcessPoint().getName());
			map.put("contition", processpointrelapointself.getDesc());
			dataList.add(map);
		}
		result.put("datas", dataList);
		result.put("totalCount", processpointrelapointselfList.size());
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
	
	@Transactional
	public void saveParentPointEditGrid(String processPointId,String modifiedRecord) {
		JSONArray jsonArray=JSONArray.fromObject(modifiedRecord);
		if(jsonArray.size()==0){
			
		}else{
			try {
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					String id = jsonObject.getString("id");
					String pointId = processPointId;
					String processId = jsonObject.getString("processId");
					String poiintPreId = jsonObject.getString("pointPreId");
					String contition = jsonObject.getString("contition");
					ProcessPointRelaPointSelf self = new ProcessPointRelaPointSelf();
					if(id.equalsIgnoreCase(""))
						id = Identities.uuid();
					self.setId(id);
					self.setProcess(o_processDAO.get(processId));
					self.setProcessPoint(o_processpointDAO.get(pointId));
					self.setPreviousProcessPoint(o_processpointDAO.get(poiintPreId));
					self.setDesc(contition);
					o_processPointRelaPointSelfDAO.merge(self);
				}
			} catch (Exception e) {
			}
		}
	}
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
					o_AssessPointDAO.merge(self);
				}
			} catch (Exception e) {
			}
		}
	}
	@Override
	public List<ProcessPoint> findProcessPointBySome(String id, String type,
			boolean self) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

