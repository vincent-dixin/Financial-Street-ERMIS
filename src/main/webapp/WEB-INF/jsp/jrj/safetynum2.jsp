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
                        var id = 'anquan_shigu_jiner';
                        var chartType = 'MSColumnLine3D';
                        var frequecy = "0frequecy_year";
     
                         {
                        	createSingleColumnLineChart2(id,frequecy,chartType);//单柱单折 年度
                         }
                    }
       
					//单柱单折
                    function createSingleColumnLineChart2(id,frequecy,chartType){
						var urlobj = {
		                   			      id:id,
		                   			      frequecy:frequecy
                    			      };
                    	$.ajax({
                            type: "POST",
                            url: "${ctx}/jrj/singlecolumnchart2.f",
                            data: urlobj,
                            success: function (xmljson) {
                                var xmlobj = $.parseJSON(xmljson)
                              
                                createChart2(id, xmlobj.xml, chartType,xmlobj.kpiname);
                            }
                        });
                    }
                    function createChart2(id, xmldata, type,kpiname) {
                    	$("#kpiname").text(kpiname);
                        var chart = new FusionCharts('${ctx}/images/chart/' + type + ".swf", id + '-chart', "500", "480");
                        chart.setXMLData(xmldata);
                        chart.render("chartdiv");
                    }
                    jQuery(function () {
                        init();
                    });
                </script>
            </head>
            
            <body>
              <div id='mainDiv1'>
              <div id='titleDiv' style='border:1px solid #999999;height:40px;background-color: #b5cfd2;text-align: center;border-bottom: none'><span style='font-size: 14px;font-weight: 800;color:#000000;margin: 10 0 0 110;
              float: left;width:250' id='kpiname'></span></div>
                <div id="chartdiv" style='border: 1px solid #999999;'></div>
            </body>
        
        </html>