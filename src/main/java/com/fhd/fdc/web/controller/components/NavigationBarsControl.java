package com.fhd.fdc.web.controller.components;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.business.components.NavigationBarsBO;

@Controller
public class NavigationBarsControl {

	@Autowired
	private NavigationBarsBO o_navigationBarsBO;
	
	@ResponseBody
	@RequestMapping(value = "/components/NavigationBars/findNavigationBars.f")
	public Map<String, Object> findNavigationBars(HttpServletResponse response, String id, String type) throws Exception {
		return o_navigationBarsBO.findNavigationBars(id, type);
	}
}