package com.fhd.comm.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fhd.comm.entity.AlarmPlan;

/**
 * 预警区间接口.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-06		下午10:47:27
 * @see 	 
 */
@Service
public interface IAlarmPlanBO {
	/**
	 * 根据预警方案id获取对应的预警区间极值(最大值和最小值).
	 * @param alarmPlanId 预警方案id
	 * @return List<Object[]>
	 */
	public List<Object[]> findExtremeValueByAlarmPlanId(String alarmPlanId);
	
	/**
	 * 按类型获取告警方案
	 * @author zhengjunxiang
	 * @param type
	 * @return
	 */
	public List<AlarmPlan> findAlarmPlanByType(String type);
}
