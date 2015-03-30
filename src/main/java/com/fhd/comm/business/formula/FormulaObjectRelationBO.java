package com.fhd.comm.business.formula;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.formula.FormulaObjectRelationDAO;
import com.fhd.comm.entity.formula.FormulaObjectRelation;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.entity.Kpi;

/**
 * 公式计算对象关系BO.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2012   2012-12-8       下午20:48:52
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class FormulaObjectRelationBO {

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private FormulaObjectRelationDAO o_formulaObjectRelationDAO;

    @Autowired
    private FormulaCalculateBO o_formulaCalculateBO;

    /**
     * 保存公式计算对象关系.
     */
    @Transactional
    public void saveFormulaObjectRelation(FormulaObjectRelation formulaObjectRelation) {
        o_formulaObjectRelationDAO.save(formulaObjectRelation);
    }

    /**
     * 修改公式计算对象关系.
     * @param formulaObjectRelation
     */
    @Transactional
    public void mergeFormulaObjectRelation(FormulaObjectRelation formulaObjectRelation) {
        o_formulaObjectRelationDAO.merge(formulaObjectRelation);
    }

    /**
     * 根据id删除公式计算对象关系.
     * @param id
     */
    @Transactional
    public void removeFormulaObjectRelationById(String id) {
        o_formulaObjectRelationDAO.delete(id);
    }

    /**
     * 根据公式计算对象关系属性查询公式计算对象关系集合.
     * @param objectId
     * @param objectType
     * @param objectColumn
     * @param relaObjectId
     * @param relaObjectType
     * @param relaObjectColumn
     * @return List<FormulaObjectRelation>
     */
    public List<FormulaObjectRelation> findFormulaObjectRelationListBySome(String objectId, String objectType, String objectColumn,
            String relaObjectId, String relaObjectType, String relaObjectColumn) {
        Criteria criteria = o_formulaObjectRelationDAO.createCriteria();
        //对象
        if (StringUtils.isNotBlank(objectId)) {
            criteria.add(Restrictions.eq("objectId", objectId));
        }
        if (StringUtils.isNotBlank(objectType)) {
            criteria.add(Restrictions.eq("objectType", objectType));
        }
        if (StringUtils.isNotBlank(objectColumn)) {
            criteria.add(Restrictions.eq("objectColumn", objectColumn));
        }
        //关联对象
        if (StringUtils.isNotBlank(relaObjectId)) {
            criteria.add(Restrictions.eq("relaObjectId", relaObjectId));
        }
        if (StringUtils.isNotBlank(relaObjectType)) {
            criteria.add(Restrictions.eq("relaObjectType", relaObjectType));
        }
        if (StringUtils.isNotBlank(relaObjectColumn)) {
            criteria.add(Restrictions.eq("relaObjectColumn", relaObjectColumn));
        }
        return criteria.list();
    }

    /**
     * <pre>
     * 保存公式关联关系
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId 指标ID
     * @param type kpi或是risk
     * @param objectColumn targetValueFormula或是resultValueFormula或是assessmentValueFormula
     * @param formula 公式
     * @since  fhd　Ver 1.1
    */
    public void saveFormulaObjectRelation(String kpiId, String type, String objectColumn, String formula) {
        String targetName = "";
        if ("kpi".equals(type)) {
            Kpi kpi = o_kpiBO.findKpiById(kpiId);
            if (null != kpi) {
                targetName = kpi.getName();
            }
        }
        //TODO风险
        //else if ("risk".equals(type)) {
        //}
        String kid = "";
        String column = "";
        String columnType = "";
        List<Map<String, String>> decompositionFormulaResult = o_formulaCalculateBO.decompositionFormulaString(targetName, formula);
        for (Map<String, String> formulaMap : decompositionFormulaResult) {
            column = formulaMap.get("type");//计算列
            if (Contents.TARGET_VALUE.equals(column) || Contents.RESULT_VALUE.equals(column) || Contents.ASSEMENT_VALUE.equals(column)) {//指标
                columnType = "kpi";
                Kpi kpi = o_kpiBO.findKpiByName(formulaMap.get("name"));//名称
                if (null != kpi) {
                    kid = kpi.getId();
                    List<FormulaObjectRelation> list = this.findFormulaObjectRelationListBySome(kid, columnType, column, null, null, null);

                    for (int i = 0; i < list.size(); i++) {
                        FormulaObjectRelation tmpObj = list.get(i);
                        List<FormulaObjectRelation> relaList = this.findFormulaObjectRelationListBySome(kpiId, columnType, objectColumn,
                                tmpObj.getRelaObjectId(), columnType, tmpObj.getRelaObjectColumn());
                        if (null != relaList && relaList.size() == 0) {
                            FormulaObjectRelation formulaObject = new FormulaObjectRelation();
                            formulaObject.setId(Identities.uuid());
                            formulaObject.setObjectId(kpiId);
                            formulaObject.setObjectColumn(objectColumn);
                            formulaObject.setObjectType(type);
                            formulaObject.setRelaObjectId(tmpObj.getRelaObjectId());
                            formulaObject.setRelaObjectColumn(tmpObj.getRelaObjectColumn());
                            formulaObject.setRelaObjectType(tmpObj.getRelaObjectType());
                            this.mergeFormulaObjectRelation(tmpObj);
                        }
                    }

                    List<FormulaObjectRelation> formulaObjectRelationList = this.findFormulaObjectRelationListBySome(kpiId, type, objectColumn, kid,
                            columnType, column);
                    if (null != formulaObjectRelationList && formulaObjectRelationList.size() == 0) {
                        FormulaObjectRelation formulaObject = new FormulaObjectRelation();
                        formulaObject.setId(Identities.uuid());
                        formulaObject.setObjectId(kpiId);
                        formulaObject.setObjectColumn(objectColumn);
                        formulaObject.setObjectType(type);
                        formulaObject.setRelaObjectId(kid);
                        formulaObject.setRelaObjectColumn(column);
                        formulaObject.setRelaObjectType(columnType);
                        this.mergeFormulaObjectRelation(formulaObject);
                    }
                }
            }
           //TODO风险或是其它
            //else {
            //}

        }
    }
}
