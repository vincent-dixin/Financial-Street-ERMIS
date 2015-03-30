package com.fhd.sys.business.orgstructure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.cliping.ClipingBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.cliping.Cliping;
import com.fhd.sys.entity.group.SysGroup;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * UserDetailsBO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-19 
 * @since    Ver 1.1
 * @Date	 2010-9-19		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
public class UserDetailsBO implements UserDetailsService {

	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	@Autowired
	private ClipingBO o_clipingBO;
	@Autowired
	private SysEmpPosiBO o_sysEmpPosiBO;

	/**
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		SysUser user = o_sysUserBO.getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("无此" + username + "用户");
		}
		Set<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(user);
		functionCutAndFormControlsAuthorities(grantedAuths);
		 
		boolean enabled = user.isEnabled();
		boolean accountNonExpired = user.isAccountNonExpired();
		boolean credentialsNonExpired = user.isCredentialsNonExpired();
		boolean accountNonLocked = user.isAccountNonLocked();
		
		OperatorDetails userDetails = new OperatorDetails(user.getUsername(), user.getPassword(), enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);
		
		userDetails.setSysRoles(user.getSysRoles());
		userDetails.setRealname(user.getRealname());
		userDetails.setUserid(user.getId());
		SysEmployee employee = o_empolyeeBO.getEmployee(user.getId());
		
		if(null != employee){
			// 将员工主责部门放到session
			List<Object[]> orgs = o_empolyeeBO.findMainDeptByEmpid(employee.getId());
			if(null != orgs && orgs.size() > 0) {
				Object[] objects = orgs.get(0);
				userDetails.setMajorDeptId(String.valueOf(objects[0]));
				userDetails.setMajorDeptName(String.valueOf(objects[1]));
				userDetails.setDivisionManagerId(o_empolyeeBO.findDivisionManager(String.valueOf(objects[0])));
			}
			userDetails.setEmp(employee);
			userDetails.setEmpid(employee.getId());
			userDetails.setCompanyid(employee.getSysOrganization().getId());
			userDetails.setCompanyName(employee.getSysOrganization().getOrgname());
			
		}
		return userDetails;
	}

	/**
	 * 
	 * <pre>
	 * 获得功能剪裁和表单控制权限
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	private void functionCutAndFormControlsAuthorities(Set<GrantedAuthority> grantedAuths) {
		
		// 功能剪裁 表单控制
		List<Cliping> list = this.o_clipingBO.findClipingAll();
		for (Cliping cliping : list) {
			grantedAuths.add(new GrantedAuthorityImpl("ROLE_"+cliping.getCode()));
		}
		
		
	}
	
	/**
	 * 获得用户所有角色的权限.
	 */
	private Set<GrantedAuthority> obtainGrantedAuthorities(SysUser user) {
		Set<GrantedAuthority> grantedAuthoritySet = new HashSet<GrantedAuthority>();
		grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_USER"));
		//读取用户特有的权限
		Set<SysAuthority> sysAuthorities = user.getSysAuthorities();
		if (sysAuthorities!=null && !sysAuthorities.isEmpty()) {
			for (SysAuthority authority : sysAuthorities) {
				grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
			}
		}
		//读取用户所属角色的权限
		Set<SysRole> sysRoles = user.getSysRoles();
		if (sysRoles!=null && !sysRoles.isEmpty()) {
			for (SysRole role : sysRoles) {
				for (SysAuthority authority : role.getSysAuthorities()) {
					grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
				}
			}
		}
		//判断该用户是否有对应员工
		SysEmployee employee = o_empolyeeBO.getEmployee(user.getId());
		if(null != employee){
			//判断员工是否有岗位，如果有岗位，加入岗位的特有权限和岗位角色的权限
			List<SysEmpPosi> sysEmpPosiList = o_sysEmpPosiBO.querySysEmpPosiByEmpid(employee.getId());
			if(null != sysEmpPosiList && sysEmpPosiList.size()>0){
				for(SysEmpPosi sysEmpPosi : sysEmpPosiList){
					if(sysEmpPosi.getSysPosition()!=null){
						//读取用户所属岗位的特有权限
						for(SysAuthority authority : sysEmpPosi.getSysPosition().getSysAuthorities()){
							grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
						}
						//读取用户所属岗位的角色权限
						for(SysRole sysRole : sysEmpPosi.getSysPosition().getSysRoles()){
							for(SysAuthority authority : sysRole.getSysAuthorities()){
								grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
							}
						}
					}
				}
			}
			//如果员工属于工作组，加入工作组的特有权限和工作组角色的权限
			for(SysGroup sysGroup : employee.getSysGroups()){
				//读取用户所属工作组的特有权限
				for(SysAuthority authority : sysGroup.getSysAuthorities()){
					grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
				}
				//读取用户所属工作组的角色权限
				for(SysRole sysRole : sysGroup.getSysRoles()){
					for(SysAuthority authority : sysRole.getSysAuthorities()){
						grantedAuthoritySet.add(new GrantedAuthorityImpl("ROLE_"+authority.getAuthorityCode()));
					}
				}
			}
		}
		
		return grantedAuthoritySet;
	}
	
}
