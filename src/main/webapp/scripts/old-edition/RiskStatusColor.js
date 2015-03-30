function getStatusMsg(v) {
	var high = "高";// 红色
	var mid = "中";// 黄色
	var low = "低";// 绿色
	var gray = "";// 灰色
	return p_status(v,high, mid, low, gray);
}
/**
 * 红黄绿图标统一取值 刘静
 */
function getColor(v) {
	var high = "<img src=" + contextPath + "/images/kpi/cockpit/high.gif />";// 红色
	var mid = "<img src=" + contextPath + "/images/kpi/cockpit/mid.gif />";// 黄色
	var low = "<img src=" + contextPath + "/images/kpi/cockpit/low.gif />";// 绿色
	var gray = "<img src=" + contextPath + "/images/kpi/cockpit/gray.gif />";// 灰色
	return p_status(v,high, mid, low, gray);
}

function p_status(v,high, mid, low, gray) {
	var colorStr = " ";
	if ('ff0000' == v) {
		colorStr = high;
	} else if ('ffff00' == v) {
		colorStr = mid;
	} else if ('00ff00' == v) {
		colorStr = low;
	} else if ('5800A3D2B7182E78A41860D505DFD0DA' == v) {
		colorStr = high;
	} else if ('1D90847CFCC115D16E27BE11EB496D37' == v) {
		colorStr = mid;
	} else if ('817C9253789B27D70F3A5E16E4D34B3D' == v) {
		colorStr = low;
	} else if ('cc0000' == v) {
		colorStr = high;
	} else if ('ffcc00' == v) {
		colorStr = mid;
	} else if ('00cc00' == v) {
		colorStr = low;
	} else if ('1_high' == v) {
		colorStr = high;
	} else if ('2_mid' == v) {
		colorStr = mid;
	} else if ('3_low' == v) {
		colorStr = low;
	} else if ("4_gray" == v) {
		colorStr = gray;
	} else if ('red-icon' == v) {
		colorStr = high;
	} else if ('yellow-icon' == v) {
		colorStr = mid;
	} else if ('green-icon' == v) {
		colorStr = low;
	} else if ('2' == v) {
		colorStr = high;
	} else if ('1' == v) {
		colorStr = mid;
	} else if ('0' == v) {
		colorStr = low;
	} else {
		colorStr = gray;
	}
	return colorStr;
}