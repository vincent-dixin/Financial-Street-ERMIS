package com.fhd.process.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * @ClassName: FlowchartControl 
 * @Description: 流程设计视图Control类
 * @author 张雷
 * @date 2011-4-12 下午04:40:15 
 *  
 */
@Controller
public class FlowchartControl {
	@RequestMapping(value = "/process/flowchart.do")
	public String flowchartQuery(String processId,HttpServletRequest request) throws Exception{
		return "flowchart/flowchart";
	}
}
