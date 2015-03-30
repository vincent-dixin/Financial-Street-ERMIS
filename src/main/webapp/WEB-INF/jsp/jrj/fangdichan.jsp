<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script src="${ctx}/scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/scripts/old-edition/chart/Charts/FusionCharts.js"></script>
<script>
	function init() {
		debugger;
		var kpiid = '${param.id}';
		var frequecy = '${param.frequecy}';
		var type = '${param.type}';
		var next = '${param.next}';
		var year = '${param.year}';
		var urlobj = {
			id : kpiid,
			frequecy : frequecy,
			next:next,
			year:year
		};
		var url = "${ctx}/jrj/kehumanyidu.f"; //14.客户满意度,房地产
		
		$.ajax({
			type : "POST",
			url:url,
			data : urlobj,
			success : function(jsonxml) {
				debugger;
				var xmlobj = $.parseJSON(jsonxml);
				var yearobj = $("#yearhidden");
				var knameobj = $("#kname");
				yearobj.val(xmlobj.year);
				knameobj.text(xmlobj.kpiname+" ("+xmlobj.year+")");
				var xmls = xmlobj.xmls;
				for(var i=0;i<xmls.length;i++){
					createChart(i+1,type,xmls[i]);
				}
				
				
			}
		});
	}

	function createChart(id, type, dataString) {
		var chart = new FusionCharts('${ctx}/images/chart/' + type + ".swf",
				'chartId' + id, "480", "390");
		chart.setXMLData(dataString);
		chart.render("chartdiv"+id);
	}

	function next(dir){
		var yearobj = $("#yearhidden");
		var newyear = yearobj.val();
		this.location.href="${ctx}/jrj/fangdichan.do?id=${param.id}&frequecy=${param.frequecy}&type=${param.type}&next="+dir+"&year="+newyear
	}
</script>


</head>

<body style="overflow-y: hidden" onload="init();">
	<input id="yearhidden" type="hidden" value="">
	<div style="width: 510px;background-image: url('${ctx}/images/jrj-portal-nav.gif');border: 1px solid #999999;">
		<table style="width: 500">
			<tr >

				<td width='10%' align="left"><a
					href="#" onclick="next('pre');" >
						<img alt="上一年" src="${ctx}/images/icons/move_left.gif"
						border="0px" />
				</a></td>
				<td width='80%' align="center" ><div style=" font-family:Microsoft YaHei,微软雅黑;border:0px solid #999999;height:40px;font-size: 15px;font-weight: 800;">
				<span style='font-size: 15px;font-weight: bold;color:#000000;margin: 10 100 0 100; float: right;width:255' id='kname'></span>
				
				</div></td>
				<td width='10%' align="right"><a
					href="#" onclick="next('next');">
						<img alt="下一年" src="${ctx}/images/icons/move_right.gif"
						border="0px" />
				</a></td>

			</tr>
		</table>
	</div>
	<div
		style="container: positioned; position =relative; overflow: auto; height: 400px; width: 510px;border: 1px solid #999999;">
		<table style="width: 400">

			<tr>
				<td><div id="chartdiv1" /></td>
			</tr>
			<tr>
				<td><div id="chartdiv2" /></td>
			</tr>
			<tr>
				<td><div id="chartdiv3" /></td>
			</tr>
			<tr>
				<td><div id="chartdiv4" /></td>
			</tr>

		<table>
	</div>
</body>
</html>
