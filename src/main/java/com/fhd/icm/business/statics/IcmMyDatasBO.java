package com.fhd.icm.business.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.IsChineseOrNotUtil;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.statics.IcmMyDatasDAO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.orgstructure.SysOrganization;



/**
 * 内控我的数据(包括 我的流程 我的风险 我的制度 我的标准 我的控制 我的任务)
 * @author 邓广义
 * @date 2013-5-15
 * @since  fhd　Ver 1.1
 */
@Service
@SuppressWarnings("unchecked")
public class IcmMyDatasBO {
	
	@Autowired
	private IcmMyDatasDAO o_icmMyDatasDAO;
	
	@Autowired
	private OrganizationBO o_organizationBO;
	
	private String findDialect(){
		String dialect = ResourceBundle.getBundle("application").getString(
				"hibernate.dialect");
				dialect = StringUtils.lowerCase(StringUtils.substringAfterLast(dialect,
						"."));
		if (StringUtils.indexOf(dialect, "oracle") > -1) {
			dialect = "oracle";
		} else if (StringUtils.indexOf(dialect, "sqlserver") > -1) {
			dialect = "sqlserver";
		} else if (StringUtils.indexOf(dialect, "mysql") > -1) {
			dialect = "mysql";
		} else if (StringUtils.indexOf(dialect, "db2") > -1) {
			dialect = "db2";
		} else if (StringUtils.indexOf(dialect, "h2") > -1) {
			dialect = "h2";
		} else if (StringUtils.indexOf(dialect, "hsql") > -1) {
			dialect = "hsql";
		} else if (StringUtils.indexOf(dialect, "sapdb") > -1) {
			dialect = "sapdb";
		} else if (StringUtils.indexOf(dialect, "sysbase") > -1) {
			dialect = "sysbase";
		}
		return dialect;
	}
	/**
	 * <pre>
	 * 查询末级流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param result 保存总数的map
	 * @param orgId 机构ID：公司或部门
	 * @param query 查询条件：查询流程编号或流程名称
	 * @param start 
	 * @param limit
	 * @param sort
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findProcessBySome(Map<String,Object> result, String orgId, String query, String start, String limit, String sort){
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		sql.append("pro1.ORG_ID, ");//0机构ID
		sql.append("o.ORG_NAME, ");//1机构名称
		sql.append("pro2.EMP_ID, ");//2员工ID
		sql.append("e.EMP_NAME, ");//3员工姓名
		sql.append("pp.PROCESSURE_NAME  PARENT_NAME, ");//4流程分类名称
		sql.append("p.PROCESS_CLASS, ");//5发生频率
		sql.append("p.PROCESSURE_CODE, ");//6流程编号
		sql.append("p.PROCESSURE_NAME, ");//7流程名称
		sql.append("p.ID, ");//8流程ID
		sql.append("p.CREATE_TIME, ");//9创建日期
		sql.append("p.LAST_MODIFY_TIME, ");//10更新日期
		sql.append("e1.DICT_ENTRY_NAME ");//11发生频率文字
		sql.append("FROM T_IC_PROCESSURE p ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON e1.ID=p.PROCESS_CLASS ");
		sql.append("LEFT JOIN T_IC_PROCESSURE_RELA_ORG pro1 ON p.ID=pro1.PROCESSURE_ID AND pro1.ETYPE='OR' ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON pro1.ORG_ID=o.ID ");
		sql.append("LEFT JOIN T_IC_PROCESSURE_RELA_ORG pro2 ON p.ID=pro2.PROCESSURE_ID AND pro2.ETYPE='ER' ");
		sql.append("LEFT JOIN T_SYS_EMPLOYEE e ON pro2.EMP_ID=e.ID ");
		sql.append("LEFT JOIN T_IC_PROCESSURE pp ON pp.ID=p.PARENT_ID ");
		sql.append("WHERE  p.IS_LEAF='1' AND p.DELETE_STATUS='1' ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" AND p.COMPANY_ID=?  ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append("AND p.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append("AND pro1.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{//非内控部门 默认取值
			sql.append("AND pro1.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(query)){
			sql.append("AND (p.PROCESSURE_CODE LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR o.ORG_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e.EMP_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR pp.PROCESSURE_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e1.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			if(!IsChineseOrNotUtil.isChinese(query)){
				sql.append("OR p.LAST_MODIFY_TIME LIKE ").append("'%").append(query).append("%' ");
				sql.append("OR p.CREATE_TIME LIKE ").append("'%").append(query).append("%' ");
			}
			sql.append("OR p.PROCESSURE_NAME LIKE ").append("'%").append(query).append("%') ");
		}
		sql.append("ORDER BY ");
		if(StringUtils.isNotBlank(sort)){
			sql.append(sort);
		}else{
			sql.append("p.LAST_MODIFY_TIME DESC, ");
			sql.append("p.CREATE_TIME DESC ");
		}
		
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		result.put("totalCount", sqlQuery.list().size());
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	/**
	 * 我的控制措施：所属风险，控制措施编号，控制措施名称，控制方式，是否关键控制点
	 * @param map
	 * @param orgid
	 * @param limit
	 * @param query
	 * @param start
	 * @return List<Object[]>
	 * select DISTINCT cm.ID,rr.risk_id,cm.MEASURE_CODE,cm.MEASURE_NAME,cm.CONTROL_MEASURE,cm.IS_KEY_CONTROL_POINT
				from t_con_control_measure cm
				LEFT JOIN T_CON_MEASURE_RELA_RISK rr on rr.control_measure_id = cm.ID
				LEFT JOIN t_con_measure_rela_org cmo on cm.id=cmo.CONTROL_MEASURE_ID 
				LEFT JOIN t_sys_organization o on cmo.ORG_ID=o.id
				ORDER BY cm.id,cmo.ETYPE;
	 */
	public List<Object[]> findMyControlDatasByOrgId(Map<String, Object> map,String orgid,String limit,String query,String start){
		StringBuffer sql = new StringBuffer();
		StringBuffer countBuf = new StringBuffer();
		StringBuffer querySql = new StringBuffer();
		List<Object> paralist = new ArrayList<Object>();
		String param = "";
		querySql.append(" SELECT DISTINCT cm.ID,");//ID 0
		querySql.append(" rr.risk_id,");//所属风险 1
		querySql.append(" cm.MEASURE_CODE,");//控制措施编号 2
		querySql.append(" cm.MEASURE_NAME,");//控制措施名称 3
		querySql.append(" cm.CONTROL_MEASURE,");//控制方式 4 
		querySql.append(" cm.IS_KEY_CONTROL_POINT");//是否关键控制点 5
		querySql.append(" FROM T_CON_CONTROL_MEASURE cm ");
		querySql.append(" LEFT JOIN T_CON_MEASURE_RELA_RISK rr on rr.control_measure_id = cm.ID ");
		
		countBuf.append(" SELECT COUNT(*) ");
		countBuf.append(" FROM T_CON_CONTROL_MEASURE cm ");
		countBuf.append(" LEFT JOIN T_CON_MEASURE_RELA_RISK rr on rr.control_measure_id = cm.ID ");
		
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgid)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" WHERE cm.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				
				SysOrganization org = o_organizationBO.get(orgid);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" WHERE cm.COMPANY_ID=? ");
						param = UserContext.getUser().getCompanyid();
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" LEFT JOIN t_con_measure_rela_org cmo on cm.id=cmo.CONTROL_MEASURE_ID");
						sql.append(" LEFT JOIN t_sys_organization o on cmo.ORG_ID=o.id");
						sql.append(" WHERE cmo.ORG_ID=?");
						param = orgid;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" LEFT JOIN t_con_measure_rela_org cmo on cm.id=cmo.CONTROL_MEASURE_ID");
			sql.append(" LEFT JOIN t_sys_organization o on cmo.ORG_ID=o.id");
			sql.append(" WHERE cmo.ORG_ID=?");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(query)){
			sql.append(" AND cm.MEASURE_NAME LIKE "+"'%"+query+"%'");
			sql.append(" OR  cm.MEASURE_CODE LIKE "+"'%"+query+"%'");
		}
		paralist.add(param);
		Object[] paraobjects = new Object[paralist.size()];
        paraobjects = paralist.toArray(paraobjects);
		SQLQuery countQuery = o_icmMyDatasDAO.createSQLQuery(countBuf.append(sql).toString(), paraobjects);
		map.put("totalCount", countQuery.uniqueResult());
		sql.append(" ORDER BY cm.ID ");
		if(!StringUtils.isBlank(limit)){
			//sql.append(" LIMIT 0," + limit);//TODO limit 为关键字mysql
		}
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(querySql.append(sql).toString(), param);
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	/**
	 * 判断当前登录人是否为内控部门
	 * @return boolean
	 */
	public boolean judgeIfIcmDept(){
		Set<SysRole> roles = UserContext.getUser().getSysRoles();
		boolean flag = false;
		for(SysRole role :roles){
			String code = role.getRoleCode();
			if(Contents.IC_DEPARTMENT_LEADER.equals(code)||Contents.IC_DEPARTMENT_MINISTER.equals(code)||Contents.IC_DEPARTMENT_STAFF.equals(code)){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * <pre>
	 * 查询相关风险信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param result 保存总数的map
	 * @param orgId 机构ID：公司或部门
	 * @param query 查询条件
	 * @param start 开始
	 * @param limit 限制
	 * @param sort 排序
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findRiskBySome(Map<String, Object> result,String orgId,String query,String start,String limit,String sort) {
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		sql.append("r.ID, ");//0风险ID
		sql.append("r.RISK_CODE, ");//1风险编号
		sql.append("r.RISK_NAME, ");//2风险名称
		sql.append("r.PARENT_ID, ");//3风险分类ID
		sql.append("pr.RISK_NAME PARENT_NAME, ");//4风险分类名称
		sql.append("o.ID, ");//5机构ID
		sql.append("o.ORG_NAME, ");//6机构名称
		sql.append("r.CREATE_TIME, ");//7创建日期
		sql.append("r.LAST_MODIFY_TIME ");//8更新日期
		sql.append("FROM T_RM_RISKS r ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e ON e.ID=r.IS_RISK_CLASS ");
		sql.append("LEFT JOIN T_RM_RISKS pr ON pr.ID=r.PARENT_ID ");
		sql.append("LEFT JOIN T_RM_RISK_ORG ro ON ro.RISK_ID=r.ID AND ro.ETYPE='OR' ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON o.ID = ro.ORG_ID ");
		sql.append("WHERE r.IS_LEAF='1' AND r.DELETE_ESTATUS='1'");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append("AND r.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append("AND r.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append("AND ro.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append("AND ro.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(query)){
			sql.append("AND (r.RISK_CODE LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR r.RISK_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR pr.RISK_NAME LIKE ").append("'%").append(query).append("%' ");
			if(!IsChineseOrNotUtil.isChinese(query)){
				sql.append("OR r.LAST_MODIFY_TIME LIKE ").append("'%").append(query).append("%' ");
				sql.append("OR r.CREATE_TIME LIKE ").append("'%").append(query).append("%' ");
			}
			sql.append("OR o.ORG_NAME LIKE ").append("'%").append(query).append("%') ");
		}
		sql.append("ORDER BY ");
		if(StringUtils.isNotBlank(sort)){
			sql.append(sort);
		}else{
			sql.append("r.LAST_MODIFY_TIME DESC, ");
			sql.append("r.CREATE_TIME DESC ");
		}
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		result.put("totalCount", sqlQuery.list().size());
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 查询内控标准的数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param result 保存总数的map
	 * @param orgId 机构ID：公司或部门
	 * @param query 查询条件
	 * @param start 起始数
	 * @param limit 限制数
	 * @param sort 排序
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findStandardBySome(Map<String, Object> result, String orgId, String query, String start, String limit, String sort) {
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		sql.append("s.ID, ");//0内控要求ID
		sql.append("s.STANDARD_CODE, ");//1内控要求编号
		sql.append("s.STANDARD_NAME, ");//2内控要求名称
		sql.append("s.CONTROL_LEVEL, ");//3控制层级
		sql.append("e1.DICT_ENTRY_NAME CONTROL_LEVEL_NAME, ");//4控制层级中文
		sql.append("s.CONTROL_POINT, ");//5控制要素
		sql.append("e2.DICT_ENTRY_NAME CONTROL_POINT_NAME, ");//6控制要素中文
		sql.append("s.DEAL_STATUS, ");//7处理状态
		sql.append("e3.DICT_ENTRY_NAME DEAL_STATUS_NAME, ");//8处理状态中文
		sql.append("ps.ID PARENT_ID, ");//9内控标准ID
		sql.append("ps.STANDARD_NAME PARENT_NAME, ");//10内控标准名称
		sql.append("o.ID ORG_ID, ");//11机构ID
		sql.append("o.ORG_NAME, ");//12机构名称
		sql.append("s.CREATE_TIME, ");//13创建日期
		sql.append("s.LAST_MODIFY_TIME ");//14更新日期
		sql.append("FROM T_IC_CONTROL_STANDARD s ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON s.CONTROL_LEVEL=e1.ID ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e2 ON s.CONTROL_POINT=e2.ID ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e3 ON s.DEAL_STATUS=e3.ID ");
		sql.append("LEFT JOIN T_IC_CONTROL_STANDARD ps ON ps.ID=s.PARENT_ID ");
		sql.append("LEFT JOIN T_IC_STANDARD_RELA_ORG sro ON sro.CONTROL_STANDARD_ID=s.ID AND sro.ETYPE='OR' ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON sro.ORG_ID=o.ID ");
		sql.append("WHERE s.DELETE_STATUS='1' AND s.ETYPE='0' ");
		
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append("AND s.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append("AND s.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append("AND sro.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append("AND sro.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(query)){
			sql.append("AND (s.STANDARD_CODE LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR s.STANDARD_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e1.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e2.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e3.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR ps.STANDARD_NAME LIKE ").append("'%").append(query).append("%' ");
			if(!IsChineseOrNotUtil.isChinese(query)){
				sql.append("OR s.LAST_MODIFY_TIME LIKE ").append("'%").append(query).append("%' ");
				sql.append("OR s.CREATE_TIME LIKE ").append("'%").append(query).append("%' ");
			}
			sql.append("OR o.ORG_NAME LIKE ").append("'%").append(query).append("%') ");
		}
		sql.append("ORDER BY ");
		if(StringUtils.isNotBlank(sort)){
			sql.append(sort);
		}else{
			sql.append("s.LAST_MODIFY_TIME DESC, ");
			sql.append("s.CREATE_TIME DESC ");
		}
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		result.put("totalCount", sqlQuery.list().size());
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 统计内控标准的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID:公司或部门
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findStandardCountBySome(String orgId) {
		String dialect = findDialect();
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		sql.append("o.ID ORG_ID, ");//0机构ID
		sql.append("o.ORG_NAME, ");//1机构名称
		sql.append("ps.ID PARENT_ID, ");//2内控标准ID
		sql.append("ps.STANDARD_NAME PARENT_NAME, ");//3内控标准名称
		sql.append("s.DEAL_STATUS, ");//4处理状态
		sql.append("e3.DICT_ENTRY_NAME DEAL_STATUS_NAME, ");//5处理状态中文
		sql.append("s.CONTROL_POINT, ");//6控制要素
		sql.append("e2.DICT_ENTRY_NAME CONTROL_POINT_NAME, ");//7控制要素中文
		sql.append("s.CONTROL_LEVEL, ");//8控制层级
		sql.append("e1.DICT_ENTRY_NAME CONTROL_LEVEL_NAME, ");//9控制层级中文
		if("mysql".equals(dialect)){
			sql.append("YEAR(s.CREATE_TIME), ");//10创建年份
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("COUNT(s.ID) standard_count ");//11数量
		sql.append("FROM T_IC_CONTROL_STANDARD s ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON s.CONTROL_LEVEL=e1.ID ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e2 ON s.CONTROL_POINT=e2.ID ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e3 ON s.DEAL_STATUS=e3.ID ");
		sql.append("LEFT JOIN T_IC_CONTROL_STANDARD ps ON ps.ID=s.PARENT_ID ");
		sql.append("LEFT JOIN T_IC_STANDARD_RELA_ORG sro ON sro.CONTROL_STANDARD_ID=s.ID AND sro.ETYPE='OR' ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON sro.ORG_ID=o.ID ");
		sql.append("WHERE s.DELETE_STATUS='1' AND s.ETYPE='0' ");
		
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append("AND s.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append("AND s.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append("AND sro.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append("AND sro.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		sql.append("GROUP BY "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(s.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("o.ID, ");
		sql.append("ps.ID, ");
		sql.append("s.DEAL_STATUS, ");
		sql.append("s.CONTROL_POINT, ");
		sql.append("s.CONTROL_LEVEL ");
		sql.append("ORDER BY ");
		if("mysql".equals(dialect)){
			sql.append("YEAR(s.CREATE_TIME) DESC, ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("o.ID, ");
		sql.append("ps.ID, ");
		sql.append("s.DEAL_STATUS, ");
		sql.append("s.CONTROL_POINT, ");
		sql.append("s.CONTROL_LEVEL ");
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return sqlQuery.list();
	}
	
	
	/**
	 * 我的制度 ：分类，制度编号，制度名称，责任部门
	 * @param map
	 * @param orgid
	 * @param limit
	 * @param query
	 * @param start
	 * @return List<Object[]>
	 */
	public List<Object[]> findMyInstitutionDatasByOrgId(Map<String, Object> map,String orgid,String limit,String query,String start) {
		StringBuffer sql = new StringBuffer();
		StringBuffer countBuf = new StringBuffer();
		StringBuffer querySql = new StringBuffer();
		List<Object> paralist = new ArrayList<Object>();
		String param = "";
		querySql.append(" SELECT ir.ID,");//id 0 
		querySql.append(" ir.PARENT_ID,");//制度分类1
		querySql.append(" ir.RULE_CODE,");//制度编号2
		querySql.append(" ir.RULE_NAME,");//制度名称3
		querySql.append(" iro.ORG_ID");//责任部门4
		querySql.append(" FROM T_IC_RULE ir ");
		querySql.append(" LEFT JOIN T_IC_RULE_RELA_ORG iro on ir.id=iro.RULE_ID ");
		
		countBuf.append(" SELECT COUNT(*) ");
		countBuf.append(" FROM T_IC_RULE ir ");
		countBuf.append(" LEFT JOIN T_IC_RULE_RELA_ORG iro on ir.id=iro.RULE_ID ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgid)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" WHERE ir.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				
				SysOrganization org = o_organizationBO.get(orgid);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" WHERE ir.COMPANY_ID=? ");
						param = orgid;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" LEFT JOIN T_SYS_ORGANIZATION o on iro.ORG_ID=o.id");
						sql.append(" WHERE iro.ORG_ID=? ");
						param = orgid;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" LEFT JOIN T_SYS_ORGANIZATION o on iro.ORG_ID=o.id");
			sql.append(" WHERE iro.ORG_ID=?");
			param = UserContext.getUser().getMajorDeptId();
		}
		sql.append(" AND ir.IS_LEAF='1'");
		sql.append(" AND iro.ETYPE = 'OR'");
		if(StringUtils.isNotBlank(query)){
			sql.append(" AND ir.RULE_NAME LIKE "+"'%"+query+"%'");
			sql.append(" OR  ir.RULE_CODE LIKE "+"'%"+query+"%'");
		}
		paralist.add(param);
		Object[] paraobjects = new Object[paralist.size()];
		paraobjects = paralist.toArray(paraobjects);
		SQLQuery countQuery = o_icmMyDatasDAO.createSQLQuery(countBuf.append(sql).toString(), paraobjects);
		map.put("totalCount", countQuery.uniqueResult());
		sql.append(" ORDER BY ir.ID ");
		if(!StringUtils.isBlank(limit)){
			//sql.append(" LIMIT 0," + limit);//TODO limit 为关键字mysql
		}
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(querySql.append(sql).toString(), param);
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 统计缺陷数量的情况
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param result 保存总数的map
	 * @param orgId 机构ID
	 * @param planId 评价计划ID
	 * @param iPlanId 整改计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findDefectCountBySome(String orgId, String planId,String iPlanId, String processId, String processPointId, String measureId){
		
		String dialect = findDialect();
		
		
		StringBuffer sql = new StringBuffer();
		judgeIfIcmDept();
		String param = "";
		sql.append("SELECT "); 
		sql.append("o.ID ORG_ID,");//0机构ID
		sql.append("o.ORG_NAME,");//1机构名称
		sql.append("plan.ID PLAN_ID, ");//2评价计划ID
		sql.append("plan.PLAN_NAME, ");//3评价计划名称
		sql.append("p.ID PROCESSURE_ID, ");//4流程ID
		sql.append("p.PROCESSURE_NAME, ");//5流程名称
		sql.append("c.ID CONTROL_POINT_ID, ");//6流程节点ID
		sql.append("c.CONTROL_POINT_NAME, ");//7流程节点名称
		sql.append("m.ID MEASURE_ID, ");//8控制措施ID
		sql.append("m.MEASURE_NAME, ");//9控制措施名称
		sql.append("ap.ID ASSESSMENT_POINT_ID, ");//10评价点ID
		sql.append("ap.EDESC ASSESSMENT_POINT_EDESC, ");//11评价点描述
		sql.append("CASE WHEN d.DEAL_STATUS='F' THEN '已完成' ELSE  ");
		sql.append("(CASE WHEN d.DEAL_STATUS='H' THEN '处理中' ELSE ");
		sql.append("(CASE WHEN d.DEAL_STATUS='N' THEN '未开始' ELSE ");
		sql.append("'' ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END DEAL_STATUS_NAME, ");//12缺陷整改状态
		sql.append("d.DEFECT_LEVEL, ");//13缺陷等级
		sql.append("d.DEFECT_TYPE, ");//14缺陷类型
		sql.append("count(d.ID) DEFECT_COUNT, ");//15缺陷数量
		sql.append("e1.dict_entry_name DEFECT_LEVEL_NAME, ");//16缺陷等级中文
		sql.append("e2.dict_entry_name DEFECT_TYPE_NAME, ");//17缺陷类型中文	 
		if("mysql".equals(dialect)){//18创建年份
			sql.append("YEAR(d.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("iplan.ID IPLAN_ID, ");
		sql.append("iplan.IMPROVEMENT_NAME ");
		sql.append("FROM T_CA_DEFECT d ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON e1.ID=d.DEFECT_LEVEL ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e2 ON e2.ID=d.DEFECT_TYPE ");
		sql.append("LEFT JOIN T_CA_DEFECT_RELA_ORG dro ON d.ID=dro.DEFECT_ID ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON o.id=dro.ORG_ID "); 
		sql.append("LEFT JOIN T_CA_DEFECT_ASSESSMENT a ON d.ID=a.DEFECT_ID "); 
		sql.append("LEFT JOIN T_IC_PROCESSURE p ON p.ID=a.PROCESSURE_ID "); 
		sql.append("LEFT JOIN T_IC_CONTROL_POINT c ON c.ID=a.CONTROL_POINT_ID "); 
		sql.append("LEFT JOIN T_CON_CONTROL_MEASURE m ON m.ID=a.MEASURE_ID "); 
		sql.append("LEFT JOIN T_CA_ASSESSMENT_POINT ap ON ap.ID=a.POINT_ID "); 
		sql.append("LEFT JOIN T_CA_ASSESSMENT_PLAN plan ON plan.ID=a.PLAN_ID "); 
		sql.append("LEFT JOIN T_RECTIFY_DEFECT_IMPROVE_PLAN diplan ON diplan.DEFECT_ID=d.ID "); 
		sql.append("LEFT JOIN T_RECTIFY_IMPROVE iplan ON iplan.ID=diplan.IMPROVE_PLAN_ID "); 
		sql.append("WHERE d.DELETE_STATUS = '1' and dro.ETYPE='OR' ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" and d.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" and d.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" and dro.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" and dro.ORG_ID=?");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(planId)){
			sql.append(" and plan.ID = '").append(planId).append("'");
		}
		if(StringUtils.isNotBlank(iPlanId)){
			sql.append(" and iplan.ID = '").append(iPlanId).append("'");
		}
		
		if(StringUtils.isNotBlank(processId)){
			sql.append(" and p.ID = '").append(processId).append("'");
		}
		
		if(StringUtils.isNotBlank(processPointId)){
			sql.append(" and c.ID = '").append(processPointId).append("'");
		}
		
		if(StringUtils.isNotBlank(measureId)){
			sql.append(" and m.ID = '").append(measureId).append("'");
		}
		sql.append("GROUP BY "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(d.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("iplan.ID, ");
		sql.append("o.ID,");
		sql.append("plan.ID,");
		sql.append("p.ID,");
		sql.append("c.ID,");
		sql.append("m.ID,");
		sql.append("ap.ID,");
		sql.append("d.DEAL_STATUS,");
		sql.append("d.DEFECT_LEVEL,");
		sql.append("d.DEFECT_TYPE ");
		sql.append("ORDER BY ");
		if("mysql".equals(dialect)){
			sql.append("YEAR(d.CREATE_TIME) DESC, ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("iplan.ID, ");
		sql.append("o.ORG_NAME,");
		sql.append("plan.PLAN_NAME,");
		sql.append("p.PROCESSURE_NAME,");
		sql.append("c.CONTROL_POINT_NAME,");
		sql.append("m.MEASURE_NAME,");
		sql.append("ap.EDESC,");
		sql.append("d.DEAL_STATUS,");
		sql.append("d.DEFECT_LEVEL,");
		sql.append("d.DEFECT_TYPE ");
		SQLQuery query = o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return query.list();
	}
	
	/**
	 * <pre>
	 * 统计缺陷的情况
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID
	 * @param query 查询条件
	 * @param planId 评价计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @param start 起始序号
	 * @param limit 最大序号
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findDefectBySome(Map<String,Object> result, String orgId, String query, String planId, String processId, 
			String processPointId, String measureId, String start, String limit, String sort){
		StringBuffer sql = new StringBuffer();
		judgeIfIcmDept();
		String param = "";
		sql.append("SELECT "); 
		sql.append("o.ID ORG_ID,");//0机构ID
		sql.append("o.ORG_NAME,");//1机构名称
		sql.append("plan.ID PLAN_ID, ");//2评价计划ID
		sql.append("plan.PLAN_NAME, ");//3评价计划名称
		sql.append("p.ID PROCESSURE_ID, ");//4流程ID
		sql.append("p.PROCESSURE_NAME, ");//5流程名称
		sql.append("c.ID CONTROL_POINT_ID, ");//6流程节点ID
		sql.append("c.CONTROL_POINT_NAME, ");//7流程节点名称
		sql.append("m.ID MEASURE_ID, ");//8控制措施ID
		sql.append("m.MEASURE_NAME, ");//9控制措施名称
		sql.append("ap.ID ASSESSMENT_POINT_ID, ");//10评价点ID
		sql.append("ap.EDESC ASSESSMENT_POINT_EDESC, ");//11评价点描述
		sql.append("CASE WHEN d.DEAL_STATUS='F' THEN '已完成' ELSE  ");
		sql.append("(CASE WHEN d.DEAL_STATUS='H' THEN '处理中' ELSE ");
		sql.append("(CASE WHEN d.DEAL_STATUS='N' THEN '未开始' ELSE ");
		sql.append("'' ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END DEAL_STATUS_NAME, ");//12缺陷整改状态
		sql.append("d.DEFECT_LEVEL, ");//13缺陷等级
		sql.append("d.DEFECT_TYPE, ");//14缺陷类型
		sql.append("d.ID, ");//15缺陷状态
		sql.append("d.EDESC, ");//16缺陷描述
		sql.append("e1.DICT_ENTRY_NAME DEFECT_LEVEL_NAME, ");//17缺陷等级中文
		sql.append("e2.DICT_ENTRY_NAME DEFECT_TYPE_NAME, ");//18缺陷类型中文
		sql.append("d.CREATE_TIME, ");//19创建日期
		sql.append("d.LAST_MODIFY_TIME ");//20更新日期
		sql.append("FROM T_CA_DEFECT d ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON e1.ID=d.DEFECT_LEVEL ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e2 ON e2.ID=d.DEFECT_TYPE ");
		sql.append("LEFT JOIN T_CA_DEFECT_RELA_ORG dro ON d.ID=dro.DEFECT_ID ");
		sql.append("LEFT JOIN T_SYS_ORGANIZATION o ON o.id=dro.ORG_ID "); 
		sql.append("LEFT JOIN T_CA_DEFECT_ASSESSMENT a ON d.ID=a.DEFECT_ID "); 
		sql.append("LEFT JOIN T_CA_ASSESSMENT_POINT ap ON ap.ID=a.POINT_ID "); 
		sql.append("LEFT JOIN T_IC_PROCESSURE p ON p.ID=a.PROCESSURE_ID "); 
		sql.append("LEFT JOIN T_IC_CONTROL_POINT c ON c.ID=a.CONTROL_POINT_ID "); 
		sql.append("LEFT JOIN T_CON_CONTROL_MEASURE m ON m.ID=a.MEASURE_ID "); 
		sql.append("LEFT JOIN T_CA_ASSESSMENT_PLAN plan ON plan.ID=a.PLAN_ID "); 
		sql.append("WHERE d.DELETE_STATUS = '1' and dro.ETYPE='OR' ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" and d.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" and d.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" and dro.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" and dro.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		
		if(StringUtils.isNotBlank(query)){
			sql.append(" AND (o.ORG_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("or CASE WHEN d.DEAL_STATUS='F' THEN '已完成' ELSE  ");
			sql.append("(CASE WHEN d.DEAL_STATUS='H' THEN '处理中' ELSE ");
			sql.append("(CASE WHEN d.DEAL_STATUS='N' THEN '未开始' ELSE ");
			sql.append("'' ");
			sql.append("END) ");
			sql.append("END) ");
			sql.append("END like ").append("'%").append(query).append("%' ");
			sql.append("OR e1.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR e2.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			if(!IsChineseOrNotUtil.isChinese(query)){
				sql.append("OR d.LAST_MODIFY_TIME LIKE ").append("'%").append(query).append("%' ");
				sql.append("OR d.CREATE_TIME LIKE ").append("'%").append(query).append("%' ");
			}
			
			sql.append("OR d.EDESC LIKE ").append("'%").append(query).append("%') ");
		}
		
		if(StringUtils.isNotBlank(planId)){
			sql.append(" and plan.ID = '").append(planId).append("'");
		}
		
		if(StringUtils.isNotBlank(processId)){
			sql.append(" and p.ID = '").append(processId).append("'");
		}
		
		if(StringUtils.isNotBlank(processPointId)){
			sql.append(" and c.ID = '").append(processPointId).append("'");
		}
		
		if(StringUtils.isNotBlank(measureId)){
			sql.append(" and m.ID = '").append(measureId).append("'");
		}
		sql.append("ORDER BY ");
		if(StringUtils.isNotBlank(sort)){
			sql.append(sort);
		}else{
			sql.append("d.LAST_MODIFY_TIME DESC, ");
			sql.append("d.CREATE_TIME DESC ");
		}
		SQLQuery sqlQuery = o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		result.put("totalCount", sqlQuery.list().size());
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 查询评价结果的数量统计
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID
	 * @param query 查询条件
	 * @param planId 评价计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @param assessPointId 评价点ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findAssessResultCountBySome(String orgId, String planId, String empId, 
			String processId, String processPointId, String measureId, String assessPointId){
		StringBuffer sql = new StringBuffer();
		judgeIfIcmDept();
		String param = "";
		sql.append("SELECT ");
		sql.append("plan.ID PLAN_ID, ");//0评价计划ID
		sql.append("plan.PLAN_NAME, ");//1评价计划名称
		sql.append("e.ID EMP_ID, ");//2员工ID
		sql.append("e.EMP_NAME, ");//3员工姓名
		sql.append("p.ID PROCESS_ID, ");//4流程ID
		sql.append("p.PROCESSURE_NAME, ");//5流程名称
		/*sql.append("c.ID CONTROL_POINT_ID, ");//6流程节点ID
		sql.append("c.CONTROL_POINT_NAME, ");//7流程节点名称
		sql.append("m.ID MEASURE_ID, ");//8控制措施ID
		sql.append("m.MEASURE_NAME, ");//9控制措施名称
		sql.append("ap.ID ASSESSMENT_POINT_ID, ");//10评价点ID
		sql.append("ap.EDESC ASSESSMENT_POINT_EDESC, ");//11评价点名称
*/		sql.append("r.ASSESSMENT_MEASURE, ");//6评价方式
		sql.append("dicte.dict_entry_name, ");//7评价方式中文
		sql.append("CASE WHEN  ");
		sql.append("r.HAS_DEFECT_ADJUST IS NULL ");
		sql.append("THEN ");
		sql.append("(CASE WHEN r.HAS_DEFECT IS NULL THEN NULL ELSE (CASE WHEN r.HAS_DEFECT='0' THEN '不合格' ELSE '合格' END)  END)  ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN r.HAS_DEFECT_ADJUST='0' THEN '不合格' ELSE '合格' END) ");
		sql.append("END IS_QUALIFIED, ");//8是否合格
		sql.append("CASE WHEN  ");
		sql.append("r.has_defect IS NOT NULL ");
		sql.append("THEN ");
		sql.append("'已完成' ");
		sql.append("ELSE ");
		sql.append("'未完成' ");
		sql.append("END IS_DONE, ");//9是否已完成		
		sql.append("COUNT(r.ID) RESULT_COUNT ");//10评价结果数量
		sql.append("FROM T_CA_ASSESSMENT_RESULT r ");
		sql.append("LEFT JOIN t_sys_dict_entry dicte ON dicte.ID=r.ASSESSMENT_MEASURE ");
		sql.append("LEFT JOIN T_CA_ASSESSMENT_PLAN plan ON plan.ID=r.PLAN_ID ");
		sql.append("LEFT JOIN T_CA_ASSESSMENT_ASSESSOR a ON a.ID=r.ASSESSOR_ID ");
		sql.append("LEFT JOIN T_SYS_EMPLOYEE e ON e.ID=a.OPERATORID ");
		sql.append("LEFT JOIN T_SYS_EMP_ORG eo ON eo.EMP_ID=e.ID ");
		sql.append("LEFT JOIN T_IC_PROCESSURE p ON p.ID=r.PROCESSURE_ID ");
		sql.append("LEFT JOIN T_IC_CONTROL_POINT c ON c.ID=r.CONTROL_POINT_ID ");
		sql.append("LEFT JOIN T_CON_CONTROL_MEASURE m ON m.ID=r.MEASURE_ID ");
		sql.append("LEFT JOIN T_CA_ASSESSMENT_POINT ap ON ap.ID=r.POINT_ID ");
		sql.append("WHERE plan.DELETE_STATUS='1' AND plan.ESTATUS='P' ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" and plan.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" and plan.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" and eo.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" and eo.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		
		if(StringUtils.isNotBlank(planId)){
			sql.append(" and plan.ID = '").append(planId).append("'");
		}
		
		if(StringUtils.isNotBlank(empId)){
			sql.append(" and e.ID = '").append(empId).append("'");
		}
		
		if(StringUtils.isNotBlank(processId)){
			sql.append(" and p.ID = '").append(processId).append("'");
		}
		
		/*if(StringUtils.isNotBlank(processPointId)){
			sql.append(" and c.ID = '").append(processPointId).append("'");
		}
		
		if(StringUtils.isNotBlank(measureId)){
			sql.append(" and m.ID = '").append(measureId).append("'");
		}
		
		if(StringUtils.isNotBlank(assessPointId)){
			sql.append(" and ap.ID = '").append(assessPointId).append("'");
		}*/
		
		sql.append("GROUP BY ");
		sql.append("plan.ID, ");
		sql.append("e.ID, ");
		sql.append("p.ID, ");
		/*sql.append("c.ID, ");
		sql.append("m.ID, ");
		sql.append("ap.ID, ");*/
		sql.append("r.ASSESSMENT_MEASURE, ");
		sql.append("CASE WHEN  ");
		sql.append("r.HAS_DEFECT_ADJUST IS NULL ");
		sql.append("THEN ");
		sql.append("(CASE WHEN r.HAS_DEFECT IS NULL THEN NULL ELSE (CASE WHEN r.HAS_DEFECT='0' THEN '不合格' ELSE '合格' END)  END)  ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN r.HAS_DEFECT_ADJUST='0' THEN '不合格' ELSE '合格' END) ");
		sql.append("END, ");
		sql.append("CASE WHEN  ");
		sql.append("r.has_defect IS NOT NULL ");
		sql.append("THEN ");
		sql.append("'已完成' ");
		sql.append("ELSE ");
		sql.append("'未完成' ");
		sql.append("END ");
		sql.append("ORDER BY ");
		sql.append("plan.PLAN_NAME, ");
		sql.append("e.EMP_NAME, ");
		sql.append("p.PROCESSURE_NAME, ");
/*		sql.append("c.CONTROL_POINT_NAME, ");
		sql.append("m.MEASURE_NAME, ");
		sql.append("ap.EDESC, ");*/
		sql.append("r.ASSESSMENT_MEASURE ");
		SQLQuery query = o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return query.list();
	}
	
	/**
	 * <pre>
	 * 查询评价结果的情况
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID
	 * @param query 查询条件
	 * @param planId 评价计划ID
	 * @param processId 流程ID
	 * @param processPointId 流程节点ID
	 * @param measureId 控制措施ID
	 * @param assessPointId 评价点ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findAssessResultBySome(Map<String,Object> result,String orgId, String planId, String empId, 
			String processId, String processPointId, String measureId, String assessPointId, String query, String start, String limit, String sort){
		StringBuffer sql = new StringBuffer();
		judgeIfIcmDept();
		String param = "";
		sql.append("SELECT ");
		sql.append("plan.ID PLAN_ID, ");//0评价计划ID
		sql.append("plan.PLAN_NAME, ");//1评价计划名称
		sql.append("e.ID EMP_ID, ");//2员工ID
		sql.append("e.EMP_NAME, ");//3员工姓名
		sql.append("p.ID PROCESS_ID, ");//4流程ID
		sql.append("p.PROCESSURE_NAME, ");//5流程名称
		sql.append("c.ID CONTROL_POINT_ID, ");//6流程节点ID
		sql.append("c.CONTROL_POINT_NAME, ");//7流程节点名称
		sql.append("m.ID MEASURE_ID, ");//8控制措施ID
		sql.append("m.MEASURE_NAME, ");//9控制措施名称
		sql.append("ap.ID ASSESSMENT_POINT_ID, ");//10评价点ID
		sql.append("ap.EDESC ASSESSMENT_POINT_EDESC, ");//11评价点名称
		sql.append("r.ASSESSMENT_MEASURE, ");//12评价方式
		sql.append("dicte.dict_entry_name ASSESSMENT_MEASURE_NAME, ");//13评价方式中文
		sql.append("CASE WHEN  ");
		sql.append("r.HAS_DEFECT_ADJUST IS NULL ");
		sql.append("THEN ");
		sql.append("(CASE WHEN r.HAS_DEFECT IS NULL THEN NULL ELSE (CASE WHEN r.HAS_DEFECT='0' THEN '不合格' ELSE '合格' END)  END)  ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN r.HAS_DEFECT_ADJUST='0' THEN '不合格' ELSE '合格' END) ");
		sql.append("END IS_QUALIFIED, ");//14是否合格
		sql.append("CASE WHEN  ");
		sql.append("r.has_defect IS NOT NULL ");
		sql.append("THEN ");
		sql.append("'已完成' ");
		sql.append("ELSE ");
		sql.append("'未完成' ");
		sql.append("END IS_DONE, ");//15是否已完成		
		sql.append("plan.CREATE_TIME, ");//16创建日期
		sql.append("plan.LAST_MODIFY_TIME, ");//17更新日期
		sql.append("(SELECT COUNT(s.ID) from T_CA_SAMPLE s where s.ASSESSMENT_POINT_ID=r.ID AND plan.ID=s.PLAN_ID) SAMPLE_COUNT, ");//18样本数量
		sql.append("r.ID ");//19评价结果ID
		sql.append("FROM T_CA_ASSESSMENT_RESULT r ");
		sql.append("LEFT JOIN t_sys_dict_entry dicte ON dicte.ID=r.ASSESSMENT_MEASURE ");
		sql.append("LEFT JOIN T_CA_ASSESSMENT_PLAN plan ON plan.ID=r.PLAN_ID ");
		sql.append("LEFT JOIN t_ca_assessment_assessor a ON a.ID=r.ASSESSOR_ID ");
		sql.append("LEFT JOIN t_sys_employee e ON e.ID=a.OPERATORID ");
		sql.append("LEFT JOIN t_sys_emp_org eo ON eo.EMP_ID=e.ID ");
		sql.append("LEFT JOIN T_IC_PROCESSURE p ON p.ID=r.PROCESSURE_ID ");
		sql.append("LEFT JOIN T_IC_CONTROL_POINT c ON c.ID=r.CONTROL_POINT_ID ");
		sql.append("LEFT JOIN T_CON_CONTROL_MEASURE m ON m.ID=r.MEASURE_ID ");
		sql.append("LEFT JOIN T_CA_ASSESSMENT_POINT ap ON ap.ID=r.POINT_ID ");
		sql.append("WHERE plan.DELETE_STATUS='1' AND plan.ESTATUS='P' ");
		if(judgeIfIcmDept()){//是内控部门
			if(StringUtils.isBlank(orgId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				sql.append(" and plan.COMPANY_ID=? ");
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				SysOrganization org = o_organizationBO.get(orgId);
				if(null!=org){
					String orgtype = org.getOrgType();
					if("0orgtype_c".equals(orgtype)||"0orgtype_sc".equals(orgtype)){		//如果是公司
						sql.append(" and plan.COMPANY_ID=? ");
						param = orgId;
					}else if("0orgtype_d".equals(orgtype)||"0orgtype_sd".equals(orgtype)){		//如果是部门
						sql.append(" and eo.ORG_ID=? ");
						param = orgId;
					}
				}
			}
		}else{			//非内控部门 默认取值
			sql.append(" and eo.ORG_ID=? ");
			param = UserContext.getUser().getMajorDeptId();
		}
		if(StringUtils.isNotBlank(query)){
			sql.append(" AND (plan.PLAN_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR p.PROCESSURE_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR c.CONTROL_POINT_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR m.MEASURE_NAME LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR ap.EDESC LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR dicte.DICT_ENTRY_NAME LIKE ").append("'%").append(query).append("%' ");
			if(!IsChineseOrNotUtil.isChinese(query)){
				sql.append("OR plan.LAST_MODIFY_TIME LIKE ").append("'%").append(query).append("%' ");
				sql.append("OR plan.CREATE_TIME LIKE ").append("'%").append(query).append("%' ");
			}
			sql.append("OR CASE WHEN  ");
			sql.append("r.HAS_DEFECT_ADJUST IS NULL ");
			sql.append("THEN ");
			sql.append("(CASE WHEN r.HAS_DEFECT IS NULL THEN NULL ELSE (CASE WHEN r.HAS_DEFECT='0' THEN '不合格' ELSE '合格' END)  END)  ");
			sql.append("ELSE ");
			sql.append("(CASE WHEN r.HAS_DEFECT_ADJUST='0' THEN '不合格' ELSE '合格' END) ");
			sql.append("END LIKE ").append("'%").append(query).append("%' ");
			sql.append("OR CASE WHEN  ");
			sql.append("r.has_defect IS NOT NULL ");
			sql.append("THEN ");
			sql.append("'已完成' ");
			sql.append("ELSE ");
			sql.append("'未完成' ");
			sql.append("END LIKE ").append("'%").append(query).append("%') ");
		}
		if(StringUtils.isNotBlank(planId)){
			sql.append(" and plan.ID = '").append(planId).append("'");
		}
		
		if(StringUtils.isNotBlank(empId)){
			sql.append(" and e.ID = '").append(empId).append("'");
		}
		
		if(StringUtils.isNotBlank(processId)){
			sql.append(" and p.ID = '").append(processId).append("'");
		}
		
		if(StringUtils.isNotBlank(processPointId)){
			sql.append(" and c.ID = '").append(processPointId).append("'");
		}
		
		if(StringUtils.isNotBlank(measureId)){
			sql.append(" and m.ID = '").append(measureId).append("'");
		}
		
		if(StringUtils.isNotBlank(assessPointId)){
			sql.append(" and ap.ID = '").append(assessPointId).append("'");
		}
		
		sql.append("ORDER BY ");
		if(StringUtils.isNotBlank(sort)){
			sql.append(sort);
		}else{
			sql.append("plan.LAST_MODIFY_TIME DESC, ");
			sql.append("plan.CREATE_TIME DESC ");
		}
		SQLQuery sqlQuery = o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		result.put("totalCount", sqlQuery.list().size());
		sqlQuery.setFirstResult(new Integer(start));
		sqlQuery.setMaxResults(new Integer(limit));
		return sqlQuery.list();
	}

	/**
	 * <pre>
	 * 统计评价计划的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 机构ID:公司
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findAssessPlanCountBySome(String companyId) {
		String dialect = findDialect();
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");//0创建年份
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");//1评价类型
		sql.append("e1.DICT_ENTRY_NAME ETYPE_NAME, ");//2评价类型中文
		sql.append("CASE WHEN plan.EXECUTE_STATUS IS NOT NULL THEN ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='N' THEN '未开始' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='A' THEN '逾期' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='H' THEN '处理中' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='F' THEN '已完成' ");
		sql.append("ELSE ");
		sql.append("NULL ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END DEAL_STATUS, ");//3处理状态
		sql.append("plan.ASSESSMENT_MEASURE, ");//4评价方式
		sql.append("e2.DICT_ENTRY_NAME ASSESSMENT_MEASURE_NAME, ");//5评价方式中文
		sql.append("count(plan.ID) PLAN_COUNT ");//6评价计划数量
		sql.append("FROM T_CA_ASSESSMENT_PLAN plan ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON e1.ID=plan.ETYPE ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e2 ON e2.ID=plan.ASSESSMENT_MEASURE ");
		sql.append("WHERE plan.DELETE_STATUS='1' AND plan.ESTATUS='P' ");
		if(judgeIfIcmDept()){//是内控部门
			sql.append("AND plan.COMPANY_ID=? ");
			if(StringUtils.isBlank(companyId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				param = companyId;
			}
		}
		sql.append("GROUP BY "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");
		sql.append("plan.EXECUTE_STATUS, ");
		sql.append("plan.ASSESSMENT_MEASURE ");
		sql.append("ORDER BY  "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");
		sql.append("plan.EXECUTE_STATUS, ");
		sql.append("plan.ASSESSMENT_MEASURE ");
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 统计整改计划的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 机构ID:公司
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findImproveCountBySome(String companyId) {
		String dialect = findDialect();
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");//0创建年份
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("CASE WHEN plan.EXECUTE_STATUS IS NOT NULL THEN ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='N' THEN '未开始' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='A' THEN '逾期' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='H' THEN '处理中' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.EXECUTE_STATUS='F' THEN '已完成' ");
		sql.append("ELSE ");
		sql.append("NULL ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END DEAL_STATUS, ");//1处理状态
		sql.append("COUNT(plan.ID) PLAN_COUNT ");//2整改计划数量
		sql.append("FROM T_RECTIFY_IMPROVE plan ");
		sql.append("WHERE plan.DELETE_STATUS='1' AND plan.ESTATUS='P' ");
		if(judgeIfIcmDept()){//是内控部门
			sql.append("AND plan.COMPANY_ID=? ");
			if(StringUtils.isBlank(companyId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				param = companyId;
			}
		}
		sql.append("GROUP BY "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.EXECUTE_STATUS ");
		sql.append("ORDER BY  "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.EXECUTE_STATUS ");
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return sqlQuery.list();
	}
	
	/**
	 * <pre>
	 * 统计体系建设计划的数量
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 公司ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Object[]> findConstructPlanCountBySome(String companyId) {
		String dialect = findDialect();
		StringBuffer sql = new StringBuffer();
		String param = "";
		sql.append("SELECT ");
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");//0创建年份
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");//1工作内容
		sql.append("e1.DICT_ENTRY_NAME ETYPE_NAME, ");//2工作内容中文
		sql.append("CASE WHEN plan.DEAL_STATUS IS NOT NULL THEN ");
		sql.append("(CASE WHEN plan.DEAL_STATUS='N' THEN '未开始' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.DEAL_STATUS='A' THEN '逾期' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.DEAL_STATUS='H' THEN '处理中' ");
		sql.append("ELSE ");
		sql.append("(CASE WHEN plan.DEAL_STATUS='F' THEN '已完成' ");
		sql.append("ELSE ");
		sql.append("NULL ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END) ");
		sql.append("END DEAL_STATUS_NAME, ");//3处理状态
		sql.append("count(plan.ID) PLAN_COUNT ");//4体系建设计划数量
		sql.append("FROM T_CA_SYSTEM_CONSTRUCTION_PLAN plan ");
		sql.append("LEFT JOIN T_SYS_DICT_ENTRY e1 ON e1.ID=plan.ETYPE ");
		sql.append("WHERE plan.DELETE_STATUS='1' AND plan.ESTATUS='P' ");
		if(judgeIfIcmDept()){//是内控部门
			sql.append("AND plan.COMPANY_ID=? ");
			if(StringUtils.isBlank(companyId)){// orgid为null说明第一次访问 默认显示 按照companyId获取数据
				param = UserContext.getUser().getCompanyid();
			}else{//内控部门切换公司部门维度的时候
				param = companyId;
			}
		}
		sql.append("GROUP BY "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");
		sql.append("plan.DEAL_STATUS ");
		sql.append("ORDER BY  "); 
		if("mysql".equals(dialect)){
			sql.append("YEAR(plan.CREATE_TIME), ");
		} else if("sqlserver".equals(dialect)){
    		//TODO sqlserver 截取年份
		} else if("oracle".equals(dialect)){
    		//TODO oracle 截取年份
		}
		sql.append("plan.ETYPE, ");
		sql.append("plan.DEAL_STATUS ");
		SQLQuery sqlQuery = this.o_icmMyDatasDAO.createSQLQuery(sql.toString(), param);
		return sqlQuery.list();
	}
}
