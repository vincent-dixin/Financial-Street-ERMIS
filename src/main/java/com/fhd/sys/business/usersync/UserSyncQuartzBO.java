package com.fhd.sys.business.usersync;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSyncQuartzBO {
	
	@Autowired 
	private IUserSyncTemlate o_userSyncTemlate;
	
	/**
	 * 开启用户同步任务
	 * @author 陈建毅
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public void init() throws SQLException	{
		o_userSyncTemlate.operateUserSync();
	}
}
