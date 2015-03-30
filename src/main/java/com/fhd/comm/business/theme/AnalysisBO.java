package com.fhd.comm.business.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.theme.AnalysisDAO;
import com.fhd.comm.dao.theme.LayoutDetailedSetDAO;
import com.fhd.comm.dao.theme.LayoutDetailedSourceDAO;
import com.fhd.comm.dao.theme.LayoutInfoDAO;
import com.fhd.comm.dao.theme.LayoutPositionDAO;
import com.fhd.comm.dao.theme.LayoutRelaDetailedSetDAO;
import com.fhd.comm.dao.theme.LayoutRelaTypeDAO;
import com.fhd.comm.dao.theme.LayoutTypeDAO;
import com.fhd.comm.dao.theme.LayoutTypeRelaPositionDAO;
import com.fhd.comm.dao.theme.ThemeRelaLayoutDAO;
import com.fhd.comm.entity.theme.Analysis;
import com.fhd.comm.entity.theme.LayoutDetailedSet;
import com.fhd.comm.entity.theme.LayoutDetailedSource;
import com.fhd.comm.entity.theme.LayoutInfo;
import com.fhd.comm.entity.theme.LayoutPosition;
import com.fhd.comm.entity.theme.LayoutRelaDetailedSet;
import com.fhd.comm.entity.theme.LayoutRelaType;
import com.fhd.comm.entity.theme.LayoutType;
import com.fhd.comm.entity.theme.LayoutTypeRelaPosition;
import com.fhd.comm.entity.theme.ThemeRelaLayout;
import com.fhd.comm.web.form.theme.ThemeLayoutDetailedForm;
import com.fhd.comm.web.form.theme.ThemeLayoutForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.kpi.dao.SmRelaKpiDAO;
import com.fhd.kpi.entity.SmRelaKpi;

/**
 * 布局信息BO
 * @author 郝静
 *
 */
@Service
public class AnalysisBO {
	@Autowired
	private LayoutInfoDAO o_layoutInfoDAO;
	@Autowired
	private LayoutTypeDAO o_layoutTypeDAO;
	@Autowired
	private LayoutRelaTypeDAO o_layoutRelaTypeDAO;
	@Autowired
	private ThemeRelaLayoutDAO o_themeRelaLayoutDAO;
	@Autowired
	private LayoutDetailedSetDAO o_layoutDetailedSetDAO;
	@Autowired
	private LayoutRelaDetailedSetDAO o_layoutRelaDetailedDAO;
	@Autowired
	private AnalysisDAO o_analysisDAO;
	@Autowired
	private LayoutTypeRelaPositionDAO o_layoutTypeRelaPositionDAO;
	@Autowired
	private LayoutPositionDAO o_layoutPositionDAO;
	@Autowired
	private LayoutDetailedSourceDAO o_layoutDetailedSourceDAO;
	@Autowired
	private SmRelaKpiDAO o_smRelaKpiDAO;
	
    /**
     * <pre>
     * 根据查询条件查询布局信息名称的个数
     * </pre>
     * 
     * @author 郝静
     * @param name
     *            布局信息名称
     * @return
     * @since fhd　Ver 1.1
     */
    public long findCountByName(String name, String id) {
        long count = 0;
//        Criteria criteria = this.o_analysisDAO.createCriteria().add(Restrictions.eq("deleteStatus", true));
//        if (StringUtils.isNotBlank(id)) {// update
//            count = (Long) criteria.add(Restrictions.eq("name", name)).add(Restrictions.ne("id", id)).setProjection(Projections.rowCount())
//                    .uniqueResult();
//        }
//        else {// add
//            count = (Long) criteria.add(Restrictions.eq("name", name)).setProjection(Projections.rowCount()).uniqueResult();
//        }
        return count;
    }
    
    
	/**
	 * 根据layoutId查找布局信息实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutInfo findLayoutInfoByLayoutId(String layoutId) {
		LayoutInfo layoutInfo = null;
		Criteria criteria = o_layoutInfoDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", layoutId));
		
		List<LayoutInfo> list = criteria.list();
        if (list.size() > 0) {
        	layoutInfo = list.get(0);
        }
        return layoutInfo;
    }
	
	/**
	 * 根据layoutId查找布局信息布局类型关系实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutRelaType findLayoutRelaTypeByLayoutId(String layoutId) {
		LayoutRelaType layoutRelaType = null;
		Criteria criteria = o_layoutRelaTypeDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("layout.id", layoutId));
		
		List<LayoutRelaType> list = criteria.list();
        if (list.size() > 0) {
        	layoutRelaType = list.get(0);
        }
        return layoutRelaType;
    }
	/**
	 * 根据layoutTypeId查找布局信息实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutType findLayoutTypeByLayoutTypeId(String layoutTypeId) {
		LayoutType layoutType = null;
		Criteria criteria = o_layoutTypeDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", layoutTypeId));
		
		List<LayoutType> list = criteria.list();
        if (list.size() > 0) {
        	layoutType = list.get(0);
        }
        return layoutType;
    }
	/**
	 * 根据layoutTypeid查找布局类型实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutType findLayoutTypeByLayoutType(String layoutType) {
		LayoutType type = null;
		Criteria criteria = o_layoutTypeDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("layoutType", layoutType));
		
		List<LayoutType> list = criteria.list();
        if (list.size() > 0) {
        	type = list.get(0);
        }
        return type;
    }
	
	/**
	 * 根据layoutDetailedSetid查找布局详细实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutDetailedSet findLayoutDetailedSetByLayoutDetailedSetId(String id) {
		LayoutDetailedSet ld = null;
		Criteria criteria = o_layoutDetailedSetDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", id));
		
		List<LayoutDetailedSet> list = criteria.list();
        if (list.size() > 0) {
        	ld = list.get(0);
        }
        return ld;
    }
	
	/**
	 * 根据layoutInfoid查找布局详细关系实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LayoutRelaDetailedSet> findLayoutRelaDetailedSetByLayoutInfoId(String layoutInfoId) {
		List<LayoutRelaDetailedSet> layoutRelaDetailedSet = new ArrayList<LayoutRelaDetailedSet>();
		Criteria criteria = o_layoutRelaDetailedDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("layout.id", layoutInfoId));
		
		List<LayoutRelaDetailedSet> list = criteria.list();
        if (list.size() > 0) {
        	for(LayoutRelaDetailedSet obj:list){
        		layoutRelaDetailedSet.add(obj);
        	}
        	
        }
        return layoutRelaDetailedSet;
    }
	
	/**
	 * 根据layoutInfoid查找布局详细关系实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LayoutDetailedSource> findLayoutDetailedSourceByDataSourceId(String id) {
		List<LayoutDetailedSource> layoutDetailedSource = new ArrayList<LayoutDetailedSource>();
		Criteria criteria = o_layoutDetailedSourceDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("dataSourceId", id));
		
		List<LayoutDetailedSource> list = criteria.list();
        if (list.size() > 0) {
        	for(LayoutDetailedSource obj:list){
        		layoutDetailedSource.add(obj);
        	}
        	
        }
        return layoutDetailedSource;
    }
	
	/**
	 * 根据layoutDetailedSetid查找布局详细实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutDetailedSet findLayoutRelaDetailedSetByLayoutDetailedSetId(String layoutDetailedSetId) {
		LayoutDetailedSet layoutDetailedSet = null;
		Criteria criteria = o_layoutDetailedSetDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("layoutDetailedId.id", layoutDetailedSetId));
		
		List<LayoutDetailedSet> list = criteria.list();
        if (list.size() > 0) {
        	layoutDetailedSet = list.get(0);
        }
        return layoutDetailedSet;
    }
	
	
	/**
	 * 根据themeid查找Analysis实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Analysis findLayoutAnalysisByThemeId(String id) {
		Analysis analysis = null;
		Criteria criteria = o_analysisDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", id));
		
		List<Analysis> list = criteria.list();
        if (list.size() > 0) {
        	analysis = list.get(0);
        }
        return analysis;
    }
	
	/**
	 * 根据LayoutPosition.name查找布局位置实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutPosition findLayoutPositionByName(String name) {
		LayoutPosition lp = null;
		Criteria criteria = o_layoutPositionDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("name", name));
		
		List<LayoutPosition> list = criteria.list();
        if (list.size() > 0) {
        	lp = list.get(0);
        }
        return lp;
    }
	/**
	 * 根据LayoutPosition.id查找布局位置实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutPosition findLayoutPositionById(String id) {
		LayoutPosition lp = null;
		Criteria criteria = o_layoutPositionDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", id));
		
		List<LayoutPosition> list = criteria.list();
        if (list.size() > 0) {
        	lp = list.get(0);
        }
        return lp;
    }
	
	/**
	 * 根据LayoutTypeRelaPosition.id查找布局位置实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LayoutTypeRelaPosition findLayoutTypeRelaPositionById(String id) {
		LayoutTypeRelaPosition lrp = null;
		Criteria criteria = o_layoutTypeRelaPositionDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", id));
		
		List<LayoutTypeRelaPosition> list = criteria.list();
        if (list.size() > 0) {
        	lrp = list.get(0);
        }
        return lrp;
    }
	/**
	 * 根据SmRelaKpi.kpiid查找实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SmRelaKpi findSmRelaKpiBykpiId(String id) {
		SmRelaKpi srk = null;
		Criteria criteria = o_smRelaKpiDAO.createCriteria();
		//criteria.setCacheable(true);
		criteria.add(Restrictions.eq("kpi.id", id));
		
		List<SmRelaKpi> list = criteria.list();
        if (list.size() > 0) {
        	srk = list.get(0);
        }
        return srk;
    }
    /**
     * <pre>
     * 保存布局信息(第一步)
     * </pre>
     * 
     * @author 郝静
     * @param layoutInfo
     * @param layoutType
     * @param layoutRelaType
     * @param themeRelaLayout
     * 
     *            布局信息实体
     * @since fhd　Ver 1.1
     */
    @Transactional
    public String saveLayoutInfo(ThemeLayoutForm themeLayoutFrom) {
    	
    	LayoutInfo layoutInfo = new LayoutInfo();
    	layoutInfo.setId(Identities.uuid());
    	layoutInfo.setLayoutName(themeLayoutFrom.getLayoutName());
    	layoutInfo.setDeleteStatus(true);
    	o_layoutInfoDAO.merge(layoutInfo);
    	
    	ThemeRelaLayout themeRelaLayout = new ThemeRelaLayout();
    	themeRelaLayout.setId(Identities.uuid());
    	themeRelaLayout.setTheme(this.findLayoutAnalysisByThemeId(themeLayoutFrom.getThemeId()));
    	themeRelaLayout.setLayout(layoutInfo);
    	o_themeRelaLayoutDAO.merge(themeRelaLayout);
    	
    	
    	LayoutType layoutType = this.findLayoutTypeByLayoutType(themeLayoutFrom.getLayoutType());
    	
    	LayoutRelaType layoutRelaType = new LayoutRelaType();
    	layoutRelaType.setId(Identities.uuid());
    	layoutRelaType.setLayout(layoutInfo);
    	layoutRelaType.setLayoutType(layoutType);
    	o_layoutRelaTypeDAO.merge(layoutRelaType);
    	
    	String layoutInfoId = layoutInfo.getId();
       
        
        return layoutInfoId;
    }
    
    /**
     * <pre>
     * 更新布局信息(第一步)
     * </pre>
     * 
     * @author 郝静
     * @param layoutInfo
     * @param layoutType
     * @param layoutRelaType
     * @param themeRelaLayout
     * 
     *            布局信息实体
     * @since fhd　Ver 1.1
     */
	@Transactional
	public String mergeLayoutInfo(ThemeLayoutForm themeLayoutForm,LayoutInfo layoutInfo) {
		
	           
    	layoutInfo.setLayoutName(themeLayoutForm.getLayoutName());
    	o_layoutInfoDAO.merge(layoutInfo);
    	
    	LayoutType layoutType = this.findLayoutTypeByLayoutType(themeLayoutForm.getLayoutType());
    	if(!layoutType.getLayoutType().equals(themeLayoutForm.getLayoutType())){
    		this.removeLayInfoDetailsByid(themeLayoutForm.getLayoutId());
    	}
    	
    	LayoutRelaType layoutRelaType = this.findLayoutRelaTypeByLayoutId(layoutInfo.getId());
    	layoutRelaType.setLayoutType(layoutType);
    	o_layoutRelaTypeDAO.merge(layoutRelaType);
    	
    	String layoutInfoId = layoutInfo.getId();
       
        return layoutInfoId;
	}

	
	
    /**
     * <pre>
     * 保存布局详细信息(第二步)
     * </pre>
     * 
     * @author 郝静
     * @param layoutDetailedSet
     * @param layoutRelaDetailedSet
     * 
     *            布局信息实体
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveLayoutDetailedInfo(ThemeLayoutDetailedForm themeLayoutDetailedForm) {
    	
        if (themeLayoutDetailedForm!=null) {
           
        	LayoutDetailedSet layoutDetailedSet = new LayoutDetailedSet();
        	layoutDetailedSet.setId(Identities.uuid());
        	layoutDetailedSet.setChartType(themeLayoutDetailedForm.getChartType());
        	layoutDetailedSet.setDataSource(Identities.uuid());
        	o_layoutDetailedSetDAO.merge(layoutDetailedSet);
        	
        	String[] kpiids = themeLayoutDetailedForm.getDataSource().split(",");
        	for(String kpiid : kpiids){
        		LayoutDetailedSource lds = new LayoutDetailedSource();
            	lds.setId(Identities.uuid());
            	lds.setDataSourceId(layoutDetailedSet.getDataSource());
            	lds.setObjectId(kpiid);
            	o_layoutDetailedSourceDAO.merge(lds);
        	}
        	LayoutTypeRelaPosition layoutTypeRelaPosition = new LayoutTypeRelaPosition();
        	layoutTypeRelaPosition.setId(Identities.uuid());
        	layoutTypeRelaPosition.setLayoutPosition(this.findLayoutPositionByName(themeLayoutDetailedForm.getPosition()));
        	layoutTypeRelaPosition.setLayoutType(this.findLayoutTypeByLayoutType(themeLayoutDetailedForm.getLayoutType()));
        	o_layoutTypeRelaPositionDAO.merge(layoutTypeRelaPosition);
        	
        	LayoutRelaDetailedSet layoutRelaDetailedSet = new LayoutRelaDetailedSet();
        	layoutRelaDetailedSet.setId(Identities.uuid());
        	layoutRelaDetailedSet.setLayoutDetailed(layoutDetailedSet);
        	layoutRelaDetailedSet.setLayoutId(this.findLayoutInfoByLayoutId(themeLayoutDetailedForm.getLayoutInfoId()));
        	layoutRelaDetailedSet.setLayoutTypeRelaPosition(layoutTypeRelaPosition);
        	o_layoutRelaDetailedDAO.merge(layoutRelaDetailedSet);
            }
    }
    /**
     * <pre>
     * 更新布局详细信息(第二步)
     * </pre>
     * 
     * @author 郝静
     * @param layoutDetailedSet
     * @param layoutRelaDetailedSet
     * 
     *            布局信息实体
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void mergeLayoutDetailedInfo(ThemeLayoutDetailedForm themeLayoutDetailedForm) {
    	
        if (themeLayoutDetailedForm!=null) {
	        
        	List<LayoutRelaDetailedSet> list = this.findLayoutRelaDetailedSetByLayoutInfoId(themeLayoutDetailedForm.getLayoutInfoId());
        	if(list.size()>0){
        		int flag = 0;
        		for(LayoutRelaDetailedSet obj:list){
        			
        	     	LayoutTypeRelaPosition lrp = this.findLayoutTypeRelaPositionById(obj.getLayoutTypeRelaPosition().getId());
        	     	LayoutPosition lp = this.findLayoutPositionById(lrp.getLayoutPosition().getId());
        	     	
        	     	if(themeLayoutDetailedForm.getPosition().equals(lp.getName())){
        	     		LayoutDetailedSet layoutDetailedSet = this.findLayoutDetailedSetByLayoutDetailedSetId(obj.getLayoutDetailedId().getId());
        	     		layoutDetailedSet.setChartType(themeLayoutDetailedForm.getChartType());
            	     	o_layoutDetailedSetDAO.merge(layoutDetailedSet);
            	     	
            	     	List<LayoutDetailedSource> dlist = this.findLayoutDetailedSourceByDataSourceId(layoutDetailedSet.getDataSource());
            	     	if(dlist.size()>0){
            	     		for(LayoutDetailedSource lds:dlist){
                	     		o_layoutDetailedSourceDAO.delete(lds);
                	     	}
            	     		String[] kpiids = themeLayoutDetailedForm.getDataSource().split(",");
            	        	for(String kpiid : kpiids){
            	        		kpiid = kpiid.replace("'", "");
            	        		LayoutDetailedSource lds = new LayoutDetailedSource();
            	            	lds.setId(Identities.uuid());
            	            	lds.setDataSourceId(layoutDetailedSet.getDataSource());
            	            	lds.setObjectId(kpiid);
            	            	o_layoutDetailedSourceDAO.merge(lds);
            	        	}
            	     	}
            	     	flag = 1;
        	     	}
        		}
        		if(flag==0){
        			LayoutDetailedSet layoutDetailedSet = new LayoutDetailedSet();
    	     		layoutDetailedSet.setId(Identities.uuid());
                	layoutDetailedSet.setChartType(themeLayoutDetailedForm.getChartType());
                	layoutDetailedSet.setDataSource(Identities.uuid());
                	o_layoutDetailedSetDAO.merge(layoutDetailedSet);
                	
                	String[] kpiids = themeLayoutDetailedForm.getDataSource().split(",");
                	for(String kpiid : kpiids){
                		LayoutDetailedSource lds = new LayoutDetailedSource();
                    	lds.setId(Identities.uuid());
                    	lds.setDataSourceId(layoutDetailedSet.getDataSource());
                    	lds.setObjectId(kpiid);
                    	o_layoutDetailedSourceDAO.merge(lds);
                	}
                	
                	LayoutTypeRelaPosition layoutTypeRelaPosition = new LayoutTypeRelaPosition();
                	layoutTypeRelaPosition.setId(Identities.uuid());
                	layoutTypeRelaPosition.setLayoutPosition(this.findLayoutPositionByName(themeLayoutDetailedForm.getPosition()));
                	layoutTypeRelaPosition.setLayoutType(this.findLayoutTypeByLayoutType(themeLayoutDetailedForm.getLayoutType()));
                	o_layoutTypeRelaPositionDAO.merge(layoutTypeRelaPosition);
                	
                	LayoutRelaDetailedSet layoutRelaDetailedSet = new LayoutRelaDetailedSet();
                	layoutRelaDetailedSet.setId(Identities.uuid());
                	layoutRelaDetailedSet.setLayoutDetailed(layoutDetailedSet);
                	layoutRelaDetailedSet.setLayoutId(this.findLayoutInfoByLayoutId(themeLayoutDetailedForm.getLayoutInfoId()));
                	layoutRelaDetailedSet.setLayoutTypeRelaPosition(layoutTypeRelaPosition);
                	o_layoutRelaDetailedDAO.merge(layoutRelaDetailedSet);
        		}
        		
        	}else{
        		LayoutDetailedSet layoutDetailedSet = new LayoutDetailedSet();
            	layoutDetailedSet.setId(Identities.uuid());
            	layoutDetailedSet.setChartType(themeLayoutDetailedForm.getChartType());
            	layoutDetailedSet.setDataSource(Identities.uuid());
            	o_layoutDetailedSetDAO.merge(layoutDetailedSet);
            	
            	String[] kpiids = themeLayoutDetailedForm.getDataSource().split(",");
            	for(String kpiid : kpiids){
	        		LayoutDetailedSource lds = new LayoutDetailedSource();
	            	lds.setId(Identities.uuid());
	            	lds.setDataSourceId(layoutDetailedSet.getDataSource());
	            	lds.setObjectId(kpiid);
	            	o_layoutDetailedSourceDAO.merge(lds);
	        	}
            	
            	LayoutTypeRelaPosition layoutTypeRelaPosition = new LayoutTypeRelaPosition();
            	layoutTypeRelaPosition.setId(Identities.uuid());
            	layoutTypeRelaPosition.setLayoutPosition(this.findLayoutPositionByName(themeLayoutDetailedForm.getPosition()));
            	layoutTypeRelaPosition.setLayoutType(this.findLayoutTypeByLayoutType(themeLayoutDetailedForm.getLayoutType()));
            	o_layoutTypeRelaPositionDAO.merge(layoutTypeRelaPosition);
            	
            	LayoutRelaDetailedSet layoutRelaDetailedSet = new LayoutRelaDetailedSet();
            	layoutRelaDetailedSet.setId(Identities.uuid());
            	layoutRelaDetailedSet.setLayoutDetailed(layoutDetailedSet);
            	layoutRelaDetailedSet.setLayoutId(this.findLayoutInfoByLayoutId(themeLayoutDetailedForm.getLayoutInfoId()));
            	layoutRelaDetailedSet.setLayoutTypeRelaPosition(layoutTypeRelaPosition);
            	o_layoutRelaDetailedDAO.merge(layoutRelaDetailedSet);
        	}
         }
    }
    
    /**
     * <pre>
     * 逻辑删除布局类型信息
     * </pre>
     * 
     * @param id
     *            layoutInfoId
     * @since fhd　Ver 1.1
     */
    @Transactional
    public boolean removeLayoutInfo(String id) {
        LayoutInfo layoutInfo = this.findLayoutInfoByLayoutId(id);
        layoutInfo.setDeleteStatus(false);
        o_layoutInfoDAO.merge(layoutInfo);
        
        LayoutRelaType layoutRelaType = this.findLayoutRelaTypeByLayoutId(layoutInfo.getId());
        o_layoutRelaTypeDAO.delete(layoutRelaType);
        return true;
        
        
    }
    
	
    /**
	 * 查询统计信息列表
	 * 
	 * @author 王再冉
	 * @param query
	 * @param pages
	 * @param sort
	 * @param dir
	 * @param themeId	
	 * @return
	 */
	public Page<LayoutInfo> findLayInfoGridBySomes(String query, Page<LayoutInfo> pages, String sort, String dir, String themeId) {
		DetachedCriteria dc = DetachedCriteria.forClass(LayoutInfo.class);
		List<String> idSet = new ArrayList<String>();
		if(null != themeId){
			Analysis analy = findLayoutAnalysisByThemeId(themeId);
			if(analy.getThemeRelaLayout().size()>0){
				Set<ThemeRelaLayout> relaSet = analy.getThemeRelaLayout();//根据主题id获得主题布局关联实体
				for(ThemeRelaLayout tRelaL:relaSet){
					if(null!=tRelaL.getLayout()){
						idSet.add(tRelaL.getLayout().getId());//得到匹配的布局信息id集合
					}
				}
			}
		}
		if(idSet.size()==0){
			return new Page<LayoutInfo>();
		}
		dc.add(Restrictions.in("id", idSet));
		dc.add(Restrictions.eq("deleteStatus", true));
		if(StringUtils.isNotBlank(query)){//搜索框不为空
			dc.add(Property.forName("layoutName").like(query,MatchMode.ANYWHERE));
		}
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_layoutInfoDAO.findPage(dc, pages, false);
		
	}
	/**
	 * 删除布局信息
	 * @param ids
	 * @author 王再冉
	 */
	@Transactional
	 public void removeLayInfosBylayIds(List<String> ids,String themeId) {
		for (String id : ids) {
			LayoutInfo layInfo = o_layoutInfoDAO.get(id);
			layInfo.setDeleteStatus(false);//将删除状态置为“0”
			if(null!=layInfo.getLayoutRelaType()){
				if(null!=findLayoutRelaTypeByLayoutId(layInfo.getId())){
					o_layoutRelaTypeDAO.delete(findLayoutRelaTypeByLayoutId(layInfo.getId()));
				}
			}
			if(null!=themeId){
				ThemeRelaLayout tr = findLayInfoAndThemeBySomeId(layInfo.getId(),themeId);
				if(null!=tr){
					o_themeRelaLayoutDAO.delete(tr);
				}
			}
			 o_layoutInfoDAO.merge(layInfo);
			
			 List<LayoutRelaDetailedSet> lrds = this.findLayoutRelaDetailedSetByLayoutInfoId(id);
			 if(lrds.size()>0){
				 for(LayoutRelaDetailedSet obj : lrds){
					 LayoutDetailedSet lds = this.findLayoutDetailedSetByLayoutDetailedSetId(obj.getLayoutDetailedId().getId());
					 LayoutTypeRelaPosition ltrp = this.findLayoutTypeRelaPositionById(obj.getLayoutTypeRelaPosition().getId());
				     List<LayoutDetailedSource> dlist = this.findLayoutDetailedSourceByDataSourceId(lds.getDataSource());
            	     	if(dlist.size()>0){
            	     		for(LayoutDetailedSource ls:dlist){
                	     		o_layoutDetailedSourceDAO.delete(ls);
                	     	}
            	     	}
					 if(obj!=null){
						 o_layoutRelaDetailedDAO.delete(obj); 
					 }
					 if(lds!=null){
						 o_layoutDetailedSetDAO.delete(lds);
					 }
					 if(ltrp!=null){
						 o_layoutTypeRelaPositionDAO.delete(ltrp);
					 }
				 }
			 }
		}
	}
	/**
	 * 查找主题布局关系实体
	 * @param infoId	布局id
	 * @param themeId	主题id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ThemeRelaLayout findLayInfoAndThemeBySomeId(String infoId,String themeId) {
		Criteria criteria = o_themeRelaLayoutDAO.createCriteria();
		//criteria.setCacheable(true);
		List<ThemeRelaLayout> list = null;
		criteria.add(Restrictions.and(Restrictions.eq("layout.id", infoId), Restrictions.eq("theme.id", themeId)));
		;
		list = criteria.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
    }
	/**
	 * 删除数据源信息
	 * @param ids
	 * @author 郝静
	 */
	@Transactional
	 public void removeLayInfoDetailsByid(String layoutId) {
			
		 List<LayoutRelaDetailedSet> lrds = this.findLayoutRelaDetailedSetByLayoutInfoId(layoutId);
		 if(lrds.size()>0){
			 for(LayoutRelaDetailedSet obj : lrds){
				 LayoutDetailedSet lds = this.findLayoutDetailedSetByLayoutDetailedSetId(obj.getLayoutDetailedId().getId());
				 LayoutTypeRelaPosition ltrp = this.findLayoutTypeRelaPositionById(obj.getLayoutTypeRelaPosition().getId());
			     List<LayoutDetailedSource> dlist = this.findLayoutDetailedSourceByDataSourceId(lds.getDataSource());
	    	     	if(dlist.size()>0){
	    	     		for(LayoutDetailedSource ls:dlist){
	        	     		o_layoutDetailedSourceDAO.delete(ls);
	        	     	}
	    	     	}
				 if(obj!=null){
					 o_layoutRelaDetailedDAO.delete(obj); 
				 }
				 if(lds!=null){
					 o_layoutDetailedSetDAO.delete(lds);
				 }
				 if(ltrp!=null){
					 o_layoutTypeRelaPositionDAO.delete(ltrp);
				 }
			 }
		 }
	}
	
}
