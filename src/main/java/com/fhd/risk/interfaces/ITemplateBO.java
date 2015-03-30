package com.fhd.risk.interfaces;

import java.util.List;
import java.util.Set;

import com.fhd.risk.entity.RiskRelaTemplate;
import com.fhd.risk.entity.ScoreInstance;
import com.fhd.risk.entity.Template;
import com.fhd.risk.entity.TemplateRelaDimension;

/**
 * 模板BO的接口
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-22		上午10:36:57
 * @see 	 
 */
public interface ITemplateBO {
	
	/**
	 * <pre>
	 * 拷贝模板及关联信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @param riskId 风险ID
	 * @since  fhd　Ver 1.1
	*/
	public void saveTemplateById(String templateId, String riskId);
	
	/**
	 * <pre>
	 * 从待选维度列表里新增模板关联维度数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId 上级模板关联维度ID
	 * @param dimensionId 维度ID
	 * @param templateId 模板ID
	 * @since  fhd　Ver 1.1
	*/
	public void saveTemplateRelaDimension(String parentId, String dimensionId, String templateId);
	/**
	 * <pre>
	 * 保存模板
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param template 模板
	 * @since  fhd　Ver 1.1
	*/
	public void mergeTemplate(Template template);
	/**
	 * <pre>
	 * 批量更新模板信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的信息
	 * @param companyId 所属公司ID
	 * @param riskId 风险ID
	 * @since  fhd　Ver 1.1
	*/
	public void mergeTemplateBatch(String jsonString,String companyId, String riskId);
	
	/**
	 * <pre>
	 * 保存模板关联维度的IdSeq和level属性
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimension 模板关联维度对象
	 * @since  fhd　Ver 1.1
	*/
	public void mergeTemplateRelaDimensionIdSeqAndLevel(TemplateRelaDimension templateRelaDimension);
	/**
	 * <pre>
	 * 保存一条模板关联维度信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimension 模板关联维度对象
	 * @since  fhd　Ver 1.1
	*/
	public void mergeTemplateRelaDimension(TemplateRelaDimension templateRelaDimension);
	
	/**
	 * <pre>
	 * 批量更新模板信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的信息
	 * @since  fhd　Ver 1.1
	*/
	public void mergeScoreInstanceBatch(String jsonString);
	/**
	 * <pre>
	 * 删除模板维度分值相关信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimensionId 模板关联维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeScoreInstanceByTemplateRelaDimensionId(String templateRelaDimensionId);
	
	/**
	 * <pre>
	 * 删除模板维度相关信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeScoreInstanceByTemplateId(String templateId);
	/**
	 * <pre>
	 * 级联删除模板,模板维度相关信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeTemplateById(String templateId);
	/**
	 * <pre>
	 * 级联删除模板维度相关，模板维度分值相关信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeTemplateRelaDimensionByTemplateId(String templateId);
	
	/**
	 * <pre>
	 * 级联删除模板维度相关，模板维度分值相关信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimensionId 模板关联维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeTemplateRelaDimensionById(String templateRelaDimensionId);
	/**
	 * <pre>
	 * 根据模板ID删除风险关联模板数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeRiskRelaTemplateByTemplateId(String templateId);
	/**
	 * <pre>
	 * 查找模板关联维度下的ScoreInstance
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @param templateRelaDimensionId 模板关联维度ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ScoreInstance> findScoreInstanceBySome(String templateId,String templateRelaDimensionId);
	/**
	 * <pre>
	 * 模糊匹配名称或描述获得所有的满足条件的模板列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Template> findTemplateByQuery(String query);
	/**
	 * <pre>
	 * 根据模板ID查找模板对象
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateId 模板ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Template findTemplateById(String templateId);
	/**
	 * <pre>
	 * 根据ID获得TemplateRelaDimension
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimensionId 模板关联维度ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public TemplateRelaDimension findTemplateRelaDimensionById(String templateRelaDimensionId);
	
	/**
	 * <pre>
	 * 模糊匹配维度名称查找TemplateRelaDimension对象的ID及上级ID的集合
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Set<String> findTemplateRelaDimensionIdSetByQuery(String query);
	
	/**
	 * <pre>
	 *	查找TemplateRelaDimension对象及上级对象的集合
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @param templateId 模板ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<TemplateRelaDimension> findTemplateRelaDimensionBySome(String query, String templateId);
	
	/**
	 * <pre>
	 * 查询风险关联模板
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param riskId 风险ID
	 * @param templateId 模板ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<RiskRelaTemplate> findRiskRelaTemplateBySome(String riskId, String templateId);
}

