package com.fhd.process.interfaces;

import java.util.List;

import com.fhd.process.entity.ProcessPoint;

/**
 * 
 * 增加、修改、删除、查询流程对象
 *
 * @author   李克东
 * @since    fhd Ver 4.5
 * @Date	 
 *
 * @see
 */
public interface IProcessPointBO
{

    /**
     * <pre>
     * 根据流程ID获得流程节点
     * </pre>
     * 
     * @author 宋佳
     * @param id 流程ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public ProcessPoint findProcessPointById(String processpointId);

    /**
     * <pre>
     * 根据流程ID查询
     * </pre>
     * 
     * @author 李克东
     * @param id
     * @param self
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<ProcessPoint> findProcessPointBySome(String id ,String type, boolean self);
    
}



 

