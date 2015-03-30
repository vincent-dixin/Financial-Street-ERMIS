package com.fhd.sys.web.controller.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.menu.MenuManagementBO;
import com.fhd.sys.web.form.menu.MenuManagementForm;

/*
 * @author denggy
 * @describe menu Management
 * @菜单管理业务核心类  菜单 页签 按钮 统称菜单  树只展示菜单，右面的form为菜单信息，下面的grid分别为页签与
 */
@Controller
public class MenuManagementControl {
	
	@Autowired
	private MenuManagementBO  menuManagementService;
	
	/**
	 * 树查询 节点 与 查询条件
	 * @param node
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findMenuTreeSome.f")
	public Map<String , Object> findMenuTreeSome(String node,String query){
		return menuManagementService.findMenuByNodeId(node,query);
	}
	/**
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findMenuAll.f")
	public Map<String,Object> findMenuAll(){
		return null;
	}
	/**
	 * 保存或则修改实体 有ID为修改 没有ID为保存 父节点ID必须在 type为保存的类别（菜单、页签、按钮）
	 * @param menuForm
	 * @param id
	 * @param parentId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/saveMenuInfo.f")
	public Map<String,Object> saveMenuInfo(MenuManagementForm menuForm,String id,String parentId ,String type){
		Map<String,Object> data = this.menuManagementService.saveMenuInfo(id, menuForm,parentId,type);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("data", data);
		map.put("success", true);
		return map;
	}
	/**
	 * 为页面的控件获取数据字典
	 * @param dictype
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findDicEntityByDictype.f")
	public Map<String,Object> findDicEntityByDictype(String dictype ){
		return this.menuManagementService.findDicEntityByDictype(dictype);
	}
	/**
	 * 通过ID查找实体
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findMenuInfoById.f")
	public Map<String,Object> findMenuInfoById(String id){
		return this.menuManagementService.findMenuInfoByidToMap(id);
	}
	/**
	 * 通过ID删除菜单实体
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/delMenuInfoById.f")
	public Map<String,Object> delMenuInfoById(String ids){
		 return this.menuManagementService.delMenuById(ids);
	}
	/**
	 * 获取当前id下的某一个类型的菜单信息 （如菜单下的页签或者页签下的按钮）
	 * @param id
	 * @param type
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findMenuInfoByIdAndType.f")
	public List<Map<String,Object>> findMenuInfoByIdAndType(String id,String type){
		 return this.menuManagementService.convertObjectToList(menuManagementService.findChildrenListByParentIdAndType(id,type));
	}
	/**
	 * 获取当前id下的某一个类型的菜单信息 （如菜单下的页签或者页签下的按钮）
	 * @param id
	 * @param type
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findYesOrNoDataStore.f")
	public Map<String,Object> findYesOrNoDataStore(String id,String type){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", "");
		map.put("name", "");
		return null;
	}
	/**
	 * 表格的保存方法
	 * @param parentId
	 * @param modifyRecords
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/saveMenuInfoForGrid.f")
	public Map<String,Object> saveMenuInfoForGrid(String modifyRecords,String saveType){
		this.menuManagementService.saveMenuInfoForGrid( modifyRecords,saveType);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("success", true);
		return map;
	}
	/**
	 * 判断菜单编号是否重复
	 * @param parentId
	 * @param modifyRecords
	 */
	@ResponseBody
	@RequestMapping(value = "menu/menuManagement/findfindMenuInfoByCodeIfExist.f")
	public Map<String,Object> findfindMenuInfoByCodeIfExist(String menuCodes,String currentIds){
		boolean b = this.menuManagementService.findfindMenuInfoByCodeIfExist(menuCodes,currentIds);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("success", b);
		return map;
	}
}
