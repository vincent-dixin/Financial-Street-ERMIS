package com.fhd.risk.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.risk.dao.RiskRelaTemplateDAO;
import com.fhd.risk.dao.ScoreInstanceDAO;
import com.fhd.risk.dao.TemplateDAO;
import com.fhd.risk.dao.TemplateRelaDimensionDAO;
import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskRelaTemplate;
import com.fhd.risk.entity.Score;
import com.fhd.risk.entity.ScoreInstance;
import com.fhd.risk.entity.Template;
import com.fhd.risk.entity.TemplateRelaDimension;
import com.fhd.risk.interfaces.ITemplateBO;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 模板业务类：模板及模板相关信息的增删改查功能
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午3:48:37
 *
 * @see 	 
 */
@SuppressWarnings("unchecked")
@Service
public class TemplateBO implements ITemplateBO{

	@Autowired
	private TemplateDAO o_templateDAO;
	
	@Autowired
	private TemplateRelaDimensionDAO o_templateRelaDimensionDAO;
	
	@Autowired
	private RiskRelaTemplateDAO o_riskRelaTemplateDAO;
	
	@Autowired
	private ScoreInstanceDAO o_scoreInstanceDAO;
	
	@Autowired
	private DimensionBO o_dimensionBO;
	
	@Autowired
	private RiskBO o_riskBO;
	
	@Autowired
	private FileUploadBO o_fileUploadBO;
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#saveTemplateById(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void saveTemplateById(String templateId, String riskId){
		List<Template> templateList = new ArrayList<Template>();
		List<TemplateRelaDimension> templateRelaDimensionList = new ArrayList<TemplateRelaDimension>();
		List<ScoreInstance> scoreInstanceList = new ArrayList<ScoreInstance>();
		Template templateSource = new Template();
		List<Risk> riskList = new ArrayList<Risk>();
		int sort = 0;
		
		templateList = this.findTemplateByQuery(null);
		templateRelaDimensionList = this.findTemplateRelaDimensionBySome(null, templateId);
		scoreInstanceList = this.findScoreInstanceBySome(templateId, null);
		for (Template template : templateList) {
			if(templateId.equals(template.getId())){
				templateSource = template;//获得ID为templateId的对象
			}
			if(template.getSort() != null && sort<=template.getSort()){
				sort = template.getSort();//获得排序的最大值
			}
		}
		Template templateTarget = new Template(Identities.uuid());
		templateTarget.setCompany(templateSource.getCompany());
		templateTarget.setDesc(templateSource.getDesc());
		templateTarget.setName(templateSource.getName());
		templateTarget.setType(new DictEntry("dim_template_type_self"));
		templateTarget.setSort(++sort);
		o_templateDAO.merge(templateTarget);
		for (TemplateRelaDimension templateRelaDimension : templateRelaDimensionList) {//保存模板关联维度信息
			if(null == templateRelaDimension.getParent()){
				TemplateRelaDimension templateRelaDimensionTarget = new TemplateRelaDimension();
				BeanUtils.copyProperties(templateRelaDimension, templateRelaDimensionTarget);
				templateRelaDimensionTarget.setId(Identities.uuid());
				templateRelaDimensionTarget.setTemplate(templateTarget);
				o_templateRelaDimensionDAO.merge(templateRelaDimensionTarget);
				this.mergeTemplateRelaDimensionIdSeqAndLevel(templateRelaDimensionTarget);
				for (ScoreInstance scoreInstance : scoreInstanceList) {//保存模板关联维度下的分值信息
					if(scoreInstance.getTemplateRelaDimension().getId().equals(templateRelaDimension.getId())){
						ScoreInstance scoreInstanceTarget = new ScoreInstance();
						BeanUtils.copyProperties(scoreInstance, scoreInstanceTarget);
						scoreInstanceTarget.setId(Identities.uuid());
						scoreInstanceTarget.setTemplateRelaDimension(templateRelaDimensionTarget);
						o_scoreInstanceDAO.merge(scoreInstanceTarget);
					}
				}
				copyTemplateRelaDimensionAndScoreInstance(templateRelaDimension.getChildren(),scoreInstanceList,templateRelaDimensionTarget.getId(), templateTarget);
			}
		}
		if(StringUtils.isNotBlank(riskId)){//拷贝风险关联模板信息
			RiskRelaTemplate riskRelaTemplate = new RiskRelaTemplate(Identities.uuid());
			riskRelaTemplate.setIsCreator(true);
			riskRelaTemplate.setRisk(new Risk(riskId));
			riskRelaTemplate.setTemplate(templateTarget);
			o_riskRelaTemplateDAO.merge(riskRelaTemplate);
			//查询子风险，默认设置关联当前模板。
			riskList = o_riskBO.findRiskByIdAndCompanyIdAndDeleteStatus(riskId, templateSource.getCompany().getId(), true);
			for (Risk risk : riskList) {
				RiskRelaTemplate subRiskRelaTemplate = new RiskRelaTemplate(Identities.uuid());
				subRiskRelaTemplate.setIsCreator(false);//该风险的子风险不是创建者,不可以修改模板
				subRiskRelaTemplate.setRisk(risk);
				subRiskRelaTemplate.setTemplate(templateTarget);
				o_riskRelaTemplateDAO.merge(subRiskRelaTemplate);
			}
		}
	}
	
	/**
	 * <pre>
	 * 递归调用拷贝模板关联维度及维度分值
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimensionSet 模板关联维度的集合
	 * @param scoreInstanceList 该模板下模板关联维度的分值
	 * @param parentId 该模板关联维度集合的新的父级
	 * @param template 模板
	 * @since  fhd　Ver 1.1
	*/
	
	@Transactional
	private void copyTemplateRelaDimensionAndScoreInstance(Set<TemplateRelaDimension> templateRelaDimensionSet,List<ScoreInstance> scoreInstanceList, String parentId, Template template){
		if(null == templateRelaDimensionSet){
			return;
		}
		for (TemplateRelaDimension templateRelaDimension : templateRelaDimensionSet) {
			TemplateRelaDimension templateRelaDimensionTarget = new TemplateRelaDimension();
			BeanUtils.copyProperties(templateRelaDimension, templateRelaDimensionTarget);
			templateRelaDimensionTarget.setId(Identities.uuid());
			templateRelaDimensionTarget.setTemplate(template);
			templateRelaDimensionTarget.setParent(new TemplateRelaDimension(parentId));
			o_templateRelaDimensionDAO.merge(templateRelaDimensionTarget);
			this.mergeTemplateRelaDimensionIdSeqAndLevel(templateRelaDimensionTarget);
			for (ScoreInstance scoreInstance : scoreInstanceList) {//保存模板关联维度下的分值信息
				if(scoreInstance.getTemplateRelaDimension().getId().equals(templateRelaDimension.getId())){
					ScoreInstance scoreInstanceTarget = new ScoreInstance();
					BeanUtils.copyProperties(scoreInstance, scoreInstanceTarget);
					scoreInstanceTarget.setId(Identities.uuid());
					scoreInstanceTarget.setTemplateRelaDimension(templateRelaDimensionTarget);
					o_scoreInstanceDAO.merge(scoreInstanceTarget);
				}
			}
			copyTemplateRelaDimensionAndScoreInstance(templateRelaDimension.getChildren(),scoreInstanceList,templateRelaDimensionTarget.getId(),template);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#saveTemplateRelaDimension(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void saveTemplateRelaDimension(String parentId, String dimensionId, String templateId){
		Dimension dimension = o_dimensionBO.findDimensionById(dimensionId);
		Template template = o_templateDAO.get(templateId);
		List<Score> scoreList = o_dimensionBO.findScoreBySome(dimensionId, null);
		
		TemplateRelaDimension templateRelaDimension = new TemplateRelaDimension(Identities.uuid());
		templateRelaDimension.setDimension(dimension);
		templateRelaDimension.setTemplate(template);
		if(StringUtils.isNotBlank(parentId)){//如果没有父级Id，则设置为一级，如果有则设置其父级
			templateRelaDimension.setParent(new TemplateRelaDimension(parentId));
		}
		o_templateRelaDimensionDAO.merge(templateRelaDimension);//添加模板关联维度记录
		this.mergeTemplateRelaDimensionIdSeqAndLevel(templateRelaDimension);//递归修改该条记录的IdSeq和level
		
		for (Score score : scoreList) {//将维度下的分值复制一份到ScoreInstance
			ScoreInstance scoreInstance = new ScoreInstance(Identities.uuid());
			scoreInstance.setName(score.getName());//默认使用Score的名称
			scoreInstance.setTemplateRelaDimension(templateRelaDimension);
			scoreInstance.setScore(score);
			scoreInstance.setDesc(score.getDesc());//默认使用Score的描述
			o_scoreInstanceDAO.merge(scoreInstance);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#mergeTemplate(com.fhd.risk.entity.Template)
	 */
	@Override
	@Transactional
	public void mergeTemplate(Template template) {
		o_templateDAO.merge(template);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#mergeTemplateBatch(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void mergeTemplateBatch(String jsonString,String companyId, String riskId){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		List<Risk> riskList = new ArrayList<Risk>();
		boolean isAdd = false;
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String typeId = jsonObject.getString("type");
			String name = jsonObject.getString("name");
			String desc = jsonObject.getString("desc");
			Integer sort = jsonObject.getInt("sort");
			Template template = null;
			if(StringUtils.isNotBlank(id)){
				template = o_templateDAO.get(id);
			}else{
				template = new Template(Identities.uuid());
				isAdd = true;
			}
			DictEntry type = new DictEntry(typeId);
			template.setName(name);
			template.setType(type);
			template.setSort(sort);
			template.setDesc(desc);
			template.setCompany(new SysOrganization(companyId));
			o_templateDAO.merge(template);
			if(isAdd && StringUtils.isNotBlank(riskId)){//如果是新增，且风险ID不为空，则将该模板关联该风险及以下风险
				RiskRelaTemplate riskRelaTemplate = new RiskRelaTemplate(Identities.uuid());
				riskRelaTemplate.setIsCreator(true);//该风险为创建者，可以修改模板
				riskRelaTemplate.setRisk(new Risk(riskId));
				riskRelaTemplate.setTemplate(template);
				o_riskRelaTemplateDAO.merge(riskRelaTemplate);
				//查询子风险，默认设置关联当前模板。
				riskList = o_riskBO.findRiskByIdAndCompanyIdAndDeleteStatus(riskId, companyId, true);
				for (Risk risk : riskList) {
					RiskRelaTemplate subRiskRelaTemplate = new RiskRelaTemplate(Identities.uuid());
					subRiskRelaTemplate.setIsCreator(false);//该风险的子风险不是创建者,不可以修改模板
					subRiskRelaTemplate.setRisk(risk);
					subRiskRelaTemplate.setTemplate(template);
					o_riskRelaTemplateDAO.merge(subRiskRelaTemplate);
				}
			}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#mergeTemplateRelaDimensionIdSeqAndLevel(com.fhd.risk.entity.TemplateRelaDimension)
	 */
	@Override
	@Transactional
	public void mergeTemplateRelaDimensionIdSeqAndLevel(TemplateRelaDimension templateRelaDimension){
		if (null != templateRelaDimension.getParent()) {
			TemplateRelaDimension parentTemplateRelaDimension = o_templateRelaDimensionDAO.get(templateRelaDimension.getParent().getId());
			templateRelaDimension.setLevel(parentTemplateRelaDimension.getLevel() + 1);
			templateRelaDimension.setIdSeq(parentTemplateRelaDimension.getIdSeq()+ templateRelaDimension.getId() + ".");
		} else {
			templateRelaDimension.setLevel(1);
			templateRelaDimension.setIdSeq("." + templateRelaDimension.getId() + ".");
			templateRelaDimension.setParent(null);
		}
		o_templateRelaDimensionDAO.merge(templateRelaDimension);
		Iterator<TemplateRelaDimension> it = templateRelaDimension.getChildren().iterator();
		while(it.hasNext()){
			TemplateRelaDimension subTemplateRelaDimension = it.next();
			mergeTemplateRelaDimensionIdSeqAndLevel(subTemplateRelaDimension);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#mergeTemplateRelaDimension(com.fhd.risk.entity.TemplateRelaDimension)
	 */
	@Override
	@Transactional
	public void mergeTemplateRelaDimension(TemplateRelaDimension templateRelaDimension){
		o_templateRelaDimensionDAO.merge(templateRelaDimension);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#mergeScoreInstanceBatch(java.lang.String)
	 */
	@Override
	@Transactional
	public void mergeScoreInstanceBatch(String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String name = jsonObject.getString("name");
			String desc = jsonObject.getString("desc");
			ScoreInstance scoreInstance = null;
			if(StringUtils.isNotBlank(id)){
				scoreInstance = o_scoreInstanceDAO.get(id);
			}else{
				scoreInstance = new ScoreInstance(Identities.uuid());
			}
			scoreInstance.setName(name);
			scoreInstance.setDesc(desc);
			o_scoreInstanceDAO.merge(scoreInstance);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeScoreInstanceByTemplateRelaDimensionId(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeScoreInstanceByTemplateRelaDimensionId(String templateRelaDimensionId){
		o_scoreInstanceDAO.createQuery("delete ScoreInstance si where si.templateRelaDimension.id=:templateRelaDimensionId")
		.setString("templateRelaDimensionId", templateRelaDimensionId)
		.executeUpdate();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeScoreInstanceByTemplateId(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeScoreInstanceByTemplateId(String templateId){
		o_scoreInstanceDAO.createQuery("delete ScoreInstance si where si.templateRelaDimension.id in (select trd.id from TemplateRelaDimension trd where trd.template.id=:templateId)")
		.setString("templateId", templateId)
		.executeUpdate();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeTemplateById(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeTemplateById(String templateId){
		Template template = this.findTemplateById(templateId);
		String fileId = null;
		if(null != template && null != template.getFile()){
			fileId = template.getFile().getId();
		}
		//删除风险关联模板信息
		removeRiskRelaTemplateByTemplateId(templateId);
		//删除模板维度相关信息
		removeTemplateRelaDimensionByTemplateId(templateId);
		//删除该模板的数据
		o_templateDAO.delete(templateId);
		//删除该模板的附件
		if(StringUtils.isNotBlank(fileId)){
			o_fileUploadBO.removeFileById(fileId);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeTemplateRelaDimensionByTemplateId(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeTemplateRelaDimensionByTemplateId(String templateId){
		//删除模板维度分值相关信息
		removeScoreInstanceByTemplateId(templateId);
		//删除模板维度相关信息
		o_templateRelaDimensionDAO.createQuery("delete TemplateRelaDimension trd where trd.template.id=:templateId")
		.setString("templateId", templateId)
		.executeUpdate();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeTemplateRelaDimensionById(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeTemplateRelaDimensionById(String templateRelaDimensionId){
		//删除模板维度分值相关信息
		removeScoreInstanceByTemplateRelaDimensionId(templateRelaDimensionId);
		//删除模板维度相关信息
		o_templateRelaDimensionDAO.delete(templateRelaDimensionId);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#removeRiskRelaTemplateByTemplateId(java.lang.String)
	 */
	@Override
	public void removeRiskRelaTemplateByTemplateId(String templateId){
		o_riskRelaTemplateDAO.createQuery("delete RiskRelaTemplate riskRelaTemplate where riskRelaTemplate.template.id =:templateId)")
		.setString("templateId", templateId)
		.executeUpdate();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findScoreInstanceBySome(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ScoreInstance> findScoreInstanceBySome(String templateId,String templateRelaDimensionId){
		Criteria criteria = o_scoreInstanceDAO.createCriteria();
		criteria.createAlias("templateRelaDimension", "templateRelaDimension");
		criteria.createAlias("templateRelaDimension.template", "template");
		if(StringUtils.isNotBlank(templateId)){
			criteria.add(Restrictions.eq("template.id", templateId));
		}
		if(StringUtils.isNotBlank(templateRelaDimensionId)){
			criteria.add(Restrictions.eq("templateRelaDimension.id", templateRelaDimensionId));
		}
		criteria.createAlias("score", "score").addOrder(Order.asc("score.sort"));
		return criteria.list();
	}

	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findTemplateById(java.lang.String)
	 */
	@Override
	public Template findTemplateById(String templateId) {
		return o_templateDAO.get(templateId);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findTemplateByQuery(java.lang.String)
	 */
	@Override
	public List<Template> findTemplateByQuery(String query){
		Criteria criteria = o_templateDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("name").like(query, MatchMode.ANYWHERE), Property.forName("desc").like(query, MatchMode.ANYWHERE)));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findTemplateRelaDimensionById(java.lang.String)
	 */
	@Override
	public TemplateRelaDimension findTemplateRelaDimensionById(String templateRelaDimensionId){
		return o_templateRelaDimensionDAO.get(templateRelaDimensionId);
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findTemplateRelaDimensionIdSetByQuery(java.lang.String)
	 */
	@Override
	public Set<String> findTemplateRelaDimensionIdSetByQuery(String query){
		List<TemplateRelaDimension> templateRelaDimensionList = new ArrayList<TemplateRelaDimension>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_templateRelaDimensionDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			criteria.createAlias("dimension", "dimension").add(Restrictions.like("dimension.name",query,MatchMode.ANYWHERE));
		}
		templateRelaDimensionList = criteria.list();
		for (TemplateRelaDimension templateRelaDimension : templateRelaDimensionList) {
			String[] idsTemp = templateRelaDimension.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findTemplateRelaDimensionBySome(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TemplateRelaDimension> findTemplateRelaDimensionBySome(String query, String templateId){
		Criteria criteria = o_templateRelaDimensionDAO.createCriteria();
		Set<String> idSet = this.findTemplateRelaDimensionIdSetByQuery(query);
		if(idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		if(StringUtils.isNotBlank(templateId)){
			criteria.add(Restrictions.eq("template.id", templateId));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	/**
	 * (non-Javadoc)
	 * @see com.fhd.risk.interfaces.ITemplateBO#findRiskRelaTemplateBySome(java.lang.String, java.lang.String)
	 */
	@Override
	public List<RiskRelaTemplate> findRiskRelaTemplateBySome(String riskId, String templateId){
		Criteria criteria = o_riskRelaTemplateDAO.createCriteria();
		if(StringUtils.isNotBlank(riskId)){
			criteria.add(Restrictions.eq("risk.id", riskId));
		}
		if(StringUtils.isNotBlank(templateId)){
			criteria.add(Restrictions.eq("template.id", templateId));
		}
		return criteria.list();
	}
}

