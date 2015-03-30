package com.fhd.process.interfaces;

import java.util.List;

import com.fhd.process.entity.Process;

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
public interface IProcessBO
{

    /**
     * <pre>
     * 根据流程ID获得流程实体
     * </pre>
     * 
     * @author 李克东
     * @param id 流程ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public Process findProcessById(String processId);

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
    public List<Process> findProcessBySome(String id ,String type, boolean self);
    
}



 

