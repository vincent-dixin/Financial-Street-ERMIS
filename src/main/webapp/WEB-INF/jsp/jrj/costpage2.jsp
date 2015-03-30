<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style type="text/css">
				</style>
                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <script src="${ctx}/scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
                <script type="text/javascript" src="${ctx}/scripts/old-edition/chart/Charts/FusionCharts.js"></script>
                <script>
                
                
                function init() {
                    var id = 'maolilv';
                    var chartType = 'MSColumnLine3D';
                    var frequecy = "0frequecy_month";
                   {    
                	createDoubleColumnLineChart2(id,frequecy,chartType);
                   }
               }
                function createDoubleColumnLineChart2(id,frequecy,chartType){
                	var urlobj = {
             			      id:id,
             			      frequecy:frequecy
      			      };
			          	$.ajax({
			                  type: "POST",
			                  url: "${ctx}/jrj/doublecolumnchart2.f",
			                  data: urlobj,
			                  success: function (xmljson) {
			                      var xmlobj = $.parseJSON(xmljson)
			                      createChart(id, xmlobj.xml, chartType,xmlobj.kpiname);
			                  }
			              });
                } 
                function createChart(id, xmldata, type,kpiname) {
                	$("#kpiname").text(kpiname);
                    var chart = new FusionCharts('${ctx}/images/chart/' + type + ".swf", id + '-chart', "500", "480");
                    chart.setXMLData(xmldata);
                    chart.render("chartdiv");
                }
                $(function () {
                    init();
                });
            </script>
                
</head>
<body>
            <div id='mainDiv'>
             <div id='titleDiv' style='font-family:Microsoft YaHei,微软雅黑;border:1px solid #999999;height:40px;background-color: #b5cfd2;text-align: center;border-bottom: none'><span style='font-size: 14px;font-weight: 800;color:#000000;margin: 10 0 0 110;
float: left;width:300' id='kpiname'></span></div>
             <div id="chartdiv" style='border: 1px solid #999999;'></div>
            </div>
</body>
</html>