package com.fhd.comm.web.form.theme;

import java.util.Set;

import com.fhd.comm.entity.theme.LayoutInfo;
import com.fhd.comm.entity.theme.LayoutRelaType;

/**
 * 
 * @author 王再冉
 *
 */
public class ThemeRecordForm extends LayoutInfo{
	
	private static final long serialVersionUID = 1L;

	private String layoutName;//名称
	
	private String icon;//布局
	
	private String layoutType;//类型
	
	public ThemeRecordForm(){
		
	}
	
	public ThemeRecordForm(LayoutInfo layoutInfo, Set<LayoutRelaType> typeSet){
		this.setLayoutName(layoutInfo.getLayoutName());
		this.setId(layoutInfo.getId());
		if(null != typeSet){
			if(typeSet.size()>0){
				for(LayoutRelaType type:typeSet){
					if(null!=type.getLayoutType()){
						this.setLayoutType(type.getLayoutType().getLayoutType());
						this.setIcon(type.getLayoutType().getIcon());
					}
				}
			}
		}
		
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}
	
	

}
