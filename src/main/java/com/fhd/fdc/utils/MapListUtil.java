package com.fhd.fdc.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;

import net.sf.json.JSONException;

public class MapListUtil {
	private static MapListUtil instance = null;
    
    static public List<HashMap<String, Object>> toMapList(Object obj) throws JSONException{
        if(instance == null){
        	instance = new MapListUtil();
        }
        
        return instance.getMapListFromObjectList(obj);
    }
    
    public List<HashMap<String,Object>> getMapListFromObjectList(Object bean){
    	if(!(bean instanceof List)){
    		return null;
    	}
    	
        //beans是一个集合
    	ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        Iterator iterator = ((List)bean).iterator();
        while(iterator.hasNext()){
            Object rowObj = iterator.next();
            
            //将对象转变成hashmap
            HashMap<String, Object> beanMap = new HashMap<String, Object>();
            Class klass = rowObj.getClass();
            Method[] methods = klass.getMethods();
            for (int i = 0; i < methods.length; i ++) {
                try {
                    Method method = methods[i];
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {	//取得所有get方法
                        key = name.substring(3);
                    } else{
                        continue;
                    }
                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        }else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }else{}
                        
                        Object elementObj = method.invoke(rowObj);
                        //对象中复杂属性不进行转换
                        if(elementObj instanceof Set){
                        	//子关联去掉
                        }else if(elementObj instanceof IdEntity){
                        	//父关联去掉
                        }else{
                        	if(!key.equals("class")){	//去掉getClass方法
                        		beanMap.put(key, elementObj==null?"":elementObj.toString());
                        	}
                        }
                    }
                } catch (Exception e) {
                    /* forget about it */
                }
            }
            
            arrayList.add(beanMap);
        }
        
        return arrayList;
    }
    
    /**
	 * 执行某对象的方法
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private Object invokeMethod(Object owner,String methodName,Object[] args) throws Exception{
		Class owerClass = owner.getClass();
		
		Class[] argsClass = new Class[args.length];
		for(int i=0,j=args.length;i<j;i++){
			argsClass[i] = args[i].getClass();
		}
		Method method = owerClass.getMethod(methodName, argsClass);
		
		return method.invoke(owner, argsClass);
	}
	
	/**
	 * 新建一个类名的实例
	 * @param className
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private Object newInstance(Class clz,Object[] args) throws Exception{
		//Class clz = Class.forName(className);
		Class[] argsClass = new Class[args.length];
		for(int i=0,j=args.length;i<j;i++){
			argsClass[i] = args[i].getClass();
		}
		Constructor constructor = clz.getConstructor(argsClass);
		
		return constructor.newInstance(args);
	}
	
    public static void main(String[] args) {
		List<SysRole> list = new ArrayList<SysRole>();
		
		SysRole role1 = new SysRole();
		role1.setId("111");
		role1.setRoleCode("code1");
		role1.setRoleName("name1");
		Set<SysUser> users = new HashSet<SysUser>();
		SysUser user1 = new SysUser();
		user1.setRealname("realName1");
		users.add(user1);
		SysUser user2 = new SysUser();
		user2.setRealname("realName2");
		users.add(user2);
		role1.setSysUsers(users);
		list.add(role1);
		
		SysRole role2 = new SysRole();
		role2.setId("222");
		role2.setRoleCode("code2");
		role2.setRoleName("name2");
		Set<SysUser> myusers = new HashSet<SysUser>();
		SysUser user3 = new SysUser();
		user3.setRealname("realName3");
		myusers.add(user3);
		SysUser user4 = new SysUser();
		user4.setRealname("realName4");
		myusers.add(user4);
		role2.setSysUsers(myusers);
		list.add(role2);
		
		//开始转换
		List<HashMap<String, Object>> datas = MapListUtil.toMapList(list);
		System.out.println(datas.toString());

	}
}
